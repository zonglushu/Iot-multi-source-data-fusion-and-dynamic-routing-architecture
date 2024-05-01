package com.mayphyr.iotapiclientsdk.config;

import com.mayphyr.iotapiclientsdk.client.SunApiClient;
import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("sunapi.client")
@Data
@ComponentScan
public class SunApiClientConfig {

    /**
     * 访问密钥
     */
    private String accessKey;
    /**
     *  校验密钥
     */
    private String secretKey;


    @Bean
    public SunApiClient sunApiClient(){
        return new SunApiClient(accessKey,secretKey);
    }


}
