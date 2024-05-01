package com.xiancai.lora.model.VO.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtherDeviceVo {
    /**
     *
     */
    private String sensorName;

    /**
     *
     */

    private String data;
    /**
     *
     */
    private String dataTime;
    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 数据单位
     */
    private String unit;
}
