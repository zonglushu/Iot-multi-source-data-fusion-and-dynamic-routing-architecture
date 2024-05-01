package com.xiancai.lora.model.DTO.mqtt.IO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class MQTTIoInfo {
    private List<Object> ios;
    /**
     * 结点id
     */
    private Integer nodeId;

    public MQTTIoInfo(List<Object> nodeio,Integer nodeId) {
        this.ios = nodeio;
        this.nodeId=nodeId;
    }

    public static MQTTIoInfo toMQTT(IoInfo ioInfo){
        ArrayList<Object> integers = new ArrayList<>();
        integers.add(ioInfo.getOx());
        integers.add(ioInfo.getStatus());
        return new MQTTIoInfo(integers,ioInfo.getNodeId());
    }
}
