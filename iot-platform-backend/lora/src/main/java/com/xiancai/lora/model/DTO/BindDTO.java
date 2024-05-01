package com.xiancai.lora.model.DTO;

import lombok.Data;

@Data
public class BindDTO {

    /**
     * 设备的ids
     */
    private String ids;
    /**
     * 用以激活的激活码
     */
    private String code;

    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 经度
     */
    private Float longitude;

    /**
     * 纬度
     */
    private Float latitude;
}
