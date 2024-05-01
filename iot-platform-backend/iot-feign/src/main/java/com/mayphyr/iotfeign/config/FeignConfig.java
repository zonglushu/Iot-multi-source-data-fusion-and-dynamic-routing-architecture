package com.mayphyr.iotfeign.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.stream.Collectors;

/**
 * FeignConfig.
 *
 * @date 2023-06-02
 */
@Configuration
public class FeignConfig {

    /**
     * spring Cloud Gateway是基于WebFlux的，是ReactiveWeb，
     * 所以HttpMessageConverters不会自动注入。在gateway服务中配置以下Bean，即可解决。
     * @param converters
     * @return
     */

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }

    /**
     * 认证，会在请求头添加Authorization
     * @return
     */
//    @Bean
//    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
//        return new BasicAuthRequestInterceptor("api-backend", "api-backend");
//    }
}

