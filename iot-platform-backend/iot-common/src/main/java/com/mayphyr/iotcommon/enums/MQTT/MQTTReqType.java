package com.mayphyr.iotcommon.enums.MQTT;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MQTTReqType {
    MQTT_REQ_GET("get"),
    MQTT_REQ_SET("get");
    private String type;
}
