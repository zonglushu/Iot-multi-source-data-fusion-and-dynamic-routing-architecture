package com.xiancai.lora.MQTT.publish.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mqtt")
@Data
public class MqttProperties {
    private String brokerUrl;

    private String clientId;

    private String username;

    private String password;
    /**
     * Java客户端向硬件发布的主题
     */
    private String webTopic;
    /**
     * 硬件向Java客户端发布的主题
     */
    private String hardTopic;
}
