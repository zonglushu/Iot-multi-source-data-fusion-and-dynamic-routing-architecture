package com.mayphyr.iotgateway.config;

import com.mayphyr.iotfeign.rpc.interfaces.CheckAccessClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

//@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder, CheckAccessClient feignClient) {
        return builder.routes()
                .route(r -> r.path("/check/access/{accessKey}")
                        .uri("lb://iot-decider")) // 这里使用 Feign 客户端进行转发
                .build();
    }
}