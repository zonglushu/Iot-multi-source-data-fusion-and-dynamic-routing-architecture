package com.xiancai.lora;

import com.xiancai.lora.MQTT.client.EmqClient;
import com.xiancai.lora.MQTT.publish.properties.MqttProperties;
import com.xiancai.lora.enums.QosEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class LoraApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoraApplication.class, args);
    }

    @Autowired
    private EmqClient emqClient;
    @Autowired
    private MqttProperties mqttProperties;

    @PostConstruct
    public void  init() throws InterruptedException {
        //配置连接信息
        emqClient.connect(mqttProperties.getUsername(), mqttProperties.getPassword());
//        emqClient.subscribe(mqttProperties.getHardTopic(), QosEnum.QOS0);
//        while (true){
//            emqClient.publish(mqttProperties.getWebTopic(),"sjidsdjs", QosEnum.QOS0, false);
//            TimeUnit.SECONDS.sleep(1);
//
//        }
    }



}
