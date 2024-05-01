package com.xiancai.lora.model.DTO.mqtt;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Data
@Builder
public class SensorData {
    /**
     * 数据本身
     */
    private String data;
    /**
     * 数据的类型
     */
    private String dataType;
    /**
     * 数据的时间
     */
    private String time;
    /**
     * 数据的单位
     */
    private String unit;
}
