package com.xiancai.lora.model.DTO;

import lombok.Data;

@Data
public class ChartSensorDataDTO {
    private String ids;
    /**
     * nodeId 和 port共同才能找到 module
     */
    private Integer nodeId;

    private Integer port;

    private String dataType;

    private String[] dataTime;

}
