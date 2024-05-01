package com.mayphyr.iotgateway.manager;

import com.mayphyr.iotcommon.enums.StatusCode;
import com.mayphyr.iotcommon.exception.ThrowUtils;
import com.mayphyr.iotcommon.model.entity.ApiPass;
import com.mayphyr.iotfeign.rpc.interfaces.CheckAccessClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@Component
public class ApiManager {
    /**
     * 用户鉴权 （判断 accessKey 和 secretKey 是否合法）
     * @param headers
     */

    @Resource
    private CheckAccessClient checkAccessClient;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public  ApiPass authorize(HttpHeaders headers) throws ExecutionException, InterruptedException {
        String accessKey = headers.getFirst("accessKey");
        String timestamp = headers.getFirst("timestamp");
        String nonce = headers.getFirst("nonce");
        String sign = headers.getFirst("sign");
        String method = headers.getFirst("method");
        boolean isAll = StringUtils.isEmpty(nonce)
                || StringUtils.isEmpty(sign)
                || StringUtils.isEmpty(timestamp)
                || StringUtils.isEmpty(method);

        ThrowUtils.throwIf(isAll,StatusCode.ERROR,"请求头不完整");


        // 判断随机数是否存在，防止重放攻击
        String existNonce = stringRedisTemplate.opsForValue().get(nonce);
        // 如果这个随机数作为值，其value如果有值，只要有值，那就说明这个nonce是用过的
        ThrowUtils.throwIf(StringUtils.isNotBlank(existNonce),StatusCode.ERROR,"请求重复，请勿重放接口");


        // 时间戳 和当前时间不能超过5分钟
        long currentTimeMillis = System.currentTimeMillis() / 1000;
        long difference = currentTimeMillis - Long.parseLong(timestamp);
        ThrowUtils.throwIf(Math.abs(difference)>300000,StatusCode.ERROR,"请求超时");

        // TODO 用feign同步调用会造成阻塞，
        //根据accessKey去找user_id
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<ApiPass> future = executorService.submit(() -> checkAccessClient.checkAccessUser(accessKey));
        ApiPass apiPass = future.get();
//        ApiPass apiPass = checkAccessClient.checkAccessUser(accessKey);
        ThrowUtils.throwIf(apiPass==null,StatusCode.ERROR,"accessKey 不合法");
        ApiPass apiPass1 = new ApiPass();
        apiPass1.setAccessKey("123");
        apiPass1.setSecretKey("456");
        apiPass1.setUserId(1l);
        return apiPass;
    }
}
