package com.mayphyr.iotdecider.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {

    @Bean
    public Logger.Level logLevel(){
        return Logger.Level.BASIC;
    }
}
