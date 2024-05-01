package com.xiancai.lora.model.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SetNodeGPS {
    private Integer nodeId;
    /**
     * 经度
     */
    private Float longitude;
    /**
     * 维度
     */
    private Float latitude;
}
