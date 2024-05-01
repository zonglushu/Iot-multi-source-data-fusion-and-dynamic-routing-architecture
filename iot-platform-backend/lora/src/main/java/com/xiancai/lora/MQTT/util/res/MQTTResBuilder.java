package com.xiancai.lora.MQTT.util.res;

import com.xiancai.lora.MQTT.bean.MQTTReq;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface MQTTResBuilder {
    MQTTReq buildMQTTReq(Map<String, Object> data, String address, Integer commandId, String type);
}
