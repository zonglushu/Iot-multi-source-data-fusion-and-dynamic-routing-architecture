package com.mayphyr.iotgateway.filter;

import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.util.StringUtils;
import com.mayphyr.iotapiclientsdk.utils.SignUtil;
import com.mayphyr.iotcommon.common.Result;
import com.mayphyr.iotcommon.enums.StatusCode;
import com.mayphyr.iotcommon.exception.BusinessException;
import com.mayphyr.iotcommon.exception.ThrowUtils;
import com.mayphyr.iotcommon.model.dto.ApiInfo;
import com.mayphyr.iotcommon.model.entity.ApiPass;
import com.mayphyr.iotcommon.model.entity.InterfaceUser;
import com.mayphyr.iotfeign.rpc.interfaces.CheckAccessClient;
import com.mayphyr.iotgateway.manager.ApiManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

/**
 * 访问api接口的filter
 * 主要功能：
 * 1.打印request和response日志.
 * 2.判断api接口是否存在
 * 3.调用接口，打印响应，如果成功后，将调用次数增加
 *
 * @date 2023-05-30
 */
@Component
@Slf4j
@AllArgsConstructor
public class ApiGlobalFilter implements GlobalFilter, Ordered {


    @Resource
    private ApiManager apiManager;

    @Resource
    private CheckAccessClient checkAccessClient;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static Set<String> whitelist = new HashSet<>();

    static {
        whitelist.add("/api/iotuser/**");
        whitelist.add("/api/decider/interfaceInfo/admin/**");
        whitelist.add("/api/decider/project/**");
        whitelist.add("/api/decider/interfaceInfo/invoke");
    }

    //TODO 其实应该只拦截供外界人员调用的API，像我们这种管理的接口应该不用拦截，那既然不用拦截，那你干嘛要走网关
    //TODO 其实还是为了监控作用，最后可以做一个监控作用，对于这个系统的管理员对系统进行的操作都进行一个统计
    //TODO 那为觉得最好还是给管理的接口加一个管理的前缀
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getPath().toString();
        whitelist.forEach(System.out::println);
        if (isInWhitelist(requestPath, whitelist)) {
            // 放行
            return chain.filter(exchange);
        }
        ApiInfo apiInfo = logRequest(exchange);

        log.info("------------------进行API过滤器");
        // api 接口判断
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String sign = headers.getFirst("sign");
        String method = headers.getFirst("method");
        apiInfo.setMethod(method);
        // 对于传过来的请求，要进行随机数的判断，时间的判断，accessKey的判断，请求头的判断
        ApiPass apiPass = null;
        try {
            apiPass = apiManager.authorize(headers);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 将加密的请求体解析出来
        String unDecodedBody = headers.getFirst("body");
        // 这个解析出来的body就是前端带来的参数，但是是json字符串类型的
        String body = URLUtil.decode(unDecodedBody, CharsetUtil.CHARSET_UTF_8);
        // 自己也生成一个sign，看这个sign与穿过来的sign是否一致
        String createdSign = SignUtil.genSign(body, apiPass.getSecretKey());
        ThrowUtils.throwIf(!createdSign.equals(sign), StatusCode.ERROR, "签名认证错误");
        // 判断要调用的接口是否存在，虽然在decider里面判断过了，但是如果是开发者调用starter的话，
        // 是直接从这里进来的，所以，这里还是要判断一下
//        InterfaceInfo interfaceInfo = checkAccessClient.checkInterfaceInfo(apiInfo);
//        ThrowUtils.throwIf(interfaceInfo==null,StatusCode.ERROR,"接口不存在");
//
//        //判断接口使用测试次数还有没有
//        hasRemainCount(interfaceInfo.getId(), apiPass.getUserId());
        return logResponse(exchange, chain, 5l, 1l);
    }

    // 判断请求路径是否在白名单中
    private boolean isInWhitelist(String requestPath, Set<String> whitelist) {
        for (String pattern : whitelist) {
            if (pathMatcher.matchStart(pattern, requestPath)) {
                return true;
            }
        }
        return false;
    }

    private ApiInfo logRequest(ServerWebExchange exchange) {
        // 获得要调用接口在网关的路由信息
//        Route{id='lora', uri=lb://lora-backend, order=0, predicate=((Paths: [/api/lora/**], match trailing slash: true && Before: 2099-01-20T17:42:47.789-07:00[America/Denver]) && Before: 2099-01-20T17:42:47.789-07:00[America/Denver]), gatewayFilters=[[[AddRequestHeader mayphyr = 'gateway'], order = 1]], metadata={}}
        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);

        ServerHttpRequest request = exchange.getRequest();
        // 获得 想要请求的 主机名和 uri

        String host = Objects.requireNonNull(route).getUri().toString();
        String uri = request.getPath().value();
        log.info("请求唯一标识：" + request.getId());
        log.info("请求主机：" + host);
        log.info("请求路径：" + uri);
        log.info("请求方法：" + request.getMethodValue());
        log.info("请求参数：" + request.getQueryParams());
        String sourceAddress = Objects.requireNonNull(request.getLocalAddress()).getHostName();
        log.info("请求来源地址：" + sourceAddress);
        log.info("请求来源地址：" + request.getRemoteAddress());
        return new ApiInfo(host, uri);
    }

    /**
     * @param exchange
     * @param chain
     * @param interfaceId 被调用接口的id值
     * @param userId      想要调用接口的用户id
     * @return
     */
    public Mono<Void> logResponse(ServerWebExchange exchange, GatewayFilterChain chain, Long interfaceId, Long userId) {
        try {
            ServerHttpResponse response = exchange.getResponse();
            DataBufferFactory bufferFactory = response.bufferFactory();
            // 响应装饰
            // 这里只是一个装饰器，也就是只是声明了要处理的内容，然后是把装饰器给上下文
            // 随后当路由转发过去后，调用成功时，才会调用这个方法，也就是跟函数式声明一样
            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

                    // 好像是当路由转发过，并且接口调用完成后，会将返回值包装成一个响应式变量返回到这里
                    if (getStatusCode() != null && body instanceof Flux) {

                        // 下面这个操作就是在获取这个响应式变量里面的body,也就是我们实际上转发过去接口的返回值

                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        return super.writeWith(fluxBody.map(dataBuffer -> {
                            // 如果响应过大，会进行截断，出现乱码，看api DefaultDataBufferFactory
                            // 有个join方法可以合并所有的流，乱码的问题解决
                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            byte[] content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            // 释放掉内存
                            DataBufferUtils.release(dataBuffer);
                            String result = new String(content, StandardCharsets.UTF_8);
//                            Object userId1=JSONUtil.parseObj(result).get("userId");
//                            Long userId=(Long)userId1;
                            log.info("response code = {}, content = {}", response.getStatusCode(), result);
                            Result r = JSONUtil.toBean(result, Result.class);
                            if (r.getCode() == 0) {
                                apiPostHandler(exchange.getRequest(), interfaceId, userId);
                            }
                            return bufferFactory.wrap(content);
                        }));
                    }
                    // if body is not a flux. never got there.
                    return super.writeWith(body);
                }
            };

            //TODO 流量染色，，，，其实可以用配置文件写

            // 在配置文件中写流量染色了
            // replace response with decorator,将装饰器配到上下文中
            return chain.filter(exchange.mutate().response(decoratedResponse).build());
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }


    private void hasRemainCount(Long interfaceId, Long userId) {
        InterfaceUser interfaceUser = checkAccessClient.hasRemainCount(interfaceId, userId);
        if (interfaceUser == null) {
            checkAccessClient.addDefaultInterfaceUser(interfaceId, userId);
        }
        Long count = interfaceUser.getCount();
        ThrowUtils.throwIf(count == null || count == 0, StatusCode.ERROR, "接口次数已使用完，请购买接口使用次数或者购买会员");
    }

    private void addInterfaceNum(ServerHttpRequest request, Long interfaceInfoId, Long userId) {
        String nonce = request.getHeaders().getFirst("nonce");
        if (StringUtils.isEmpty(nonce)) {
            throw new BusinessException(StatusCode.ERROR, "请求重复");
        }
        checkAccessClient.decreaseInvokeCount(interfaceInfoId, userId);
        stringRedisTemplate.opsForValue().set(nonce, "1", 5, TimeUnit.MINUTES);
    }

    /**
     * 调用成功后的后置处理,
     *
     * @param interfaceId
     * @param userId
     */
    private void apiPostHandler(ServerHttpRequest request, Long interfaceId, Long userId) {
        log.info("api调用成功，开始进行数据库处理");
        addInterfaceNum(request, interfaceId, userId);
    }

    /**
     * 要设置的小一点，避免{@link NettyWriteResponseFilter} 先返回
     *
     * @return
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 300;
    }


}
