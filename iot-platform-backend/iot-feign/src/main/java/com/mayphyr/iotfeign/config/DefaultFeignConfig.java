package com.mayphyr.iotfeign.config;

import com.mayphyr.iotfeign.fallback.UserClientFallbackFactory;
import com.mayphyr.iotfeign.fallback.agrculture.NodeClientFallbackFactory;
import com.mayphyr.iotfeign.fallback.interfaces.CheckAccessClientFallbackFactory;
import feign.Logger;
import org.springframework.context.annotation.Bean;


public class DefaultFeignConfig {

    @Bean
    public Logger.Level logLevel(){
        return Logger.Level.BASIC;
    }

    @Bean
    public UserClientFallbackFactory userClientFallbackFactory(){
        return new UserClientFallbackFactory();
    }

    @Bean
    public NodeClientFallbackFactory nodeClientFallbackFactory(){
        return new NodeClientFallbackFactory();
    }

    @Bean
    public CheckAccessClientFallbackFactory checkAccessClientFallbackFactory(){
        return new CheckAccessClientFallbackFactory();
    }

}
