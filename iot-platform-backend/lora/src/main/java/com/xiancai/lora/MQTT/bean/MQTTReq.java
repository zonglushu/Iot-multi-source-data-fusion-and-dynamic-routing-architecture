package com.xiancai.lora.MQTT.bean;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class MQTTReq {
    /**
     * 客户端发给硬件的执行命令的节点地址，有三种,客户端传给硬件是那种形式，那硬件传给客户端就是那种形式
     * #id1.id2.ids3 用于设备首次绑定
     * $id.id.id 常用
     * #ids1.ids2.ids3 保留，第三方
     * %domain.domain.domain
     */
    private String adr;

    private String mid;

    /**
     * 具体的命令
     */
    private Integer cmd;


    /**
     *
     */
    private Map<String,Object> ct;

}
