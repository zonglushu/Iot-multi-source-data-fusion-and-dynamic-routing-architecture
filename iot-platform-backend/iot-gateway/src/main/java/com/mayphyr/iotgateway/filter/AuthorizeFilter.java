package com.mayphyr.iotgateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

// 数越小，优先级越高
//@Order(0)
//@Component
public class AuthorizeFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        1.获取请求参数
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, String> params = request.getQueryParams();
//        2.获取参数中的 authorization参数
        String auth = params.getFirst("authorization");
        if(auth.equals("auth")){
//           放行
            return chain.filter(exchange);
        }
        //设置状态码
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//        拦截
        return exchange.getResponse().setComplete();
    }
}
