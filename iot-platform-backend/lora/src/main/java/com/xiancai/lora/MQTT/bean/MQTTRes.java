package com.xiancai.lora.MQTT.bean;

import com.xiancai.lora.enums.MQTT.MQTTResultStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 硬件返回的消息
 */
@Data
@Builder
public class MQTTRes {
    /**
     *   硬件返回的地址-对应发送的 地址-address
     */
    private String adr;
    /**
     * 硬件返回的消息id
     */
    private String mid;

    /**
     * 硬件返回的命令
     *
     */
    private Integer rcmd;

    /**
     * 硬件返回的消息
     */
    private Map<String,Object> ct;

    /**
     * 返回类型，暂时不写 TODO
     */
    private Integer dytype;
    /**
     * 返回的状态，
     * -1-fail
     * 0-success
     * 1-warning
     * 2-error
     * error还有 error
     */

    private String res;
}
