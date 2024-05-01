package com.xiancai.lora.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * 
 * @TableName dtype
 */
@TableName(value ="dtype")
@Data
@Builder
public class Dtype implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 数据类型
     */
    @TableField(value = "data_type")
    private String dataType;

    /**
     * 数据对应的传感器id
     */
    @TableField(value = "sensor_id")
    private Integer sensorId;

    /**
     * 该类型的数据在硬件传来的数组中的位置
     */
    @TableField(value = "sweat")
    private String sweat;

    /**
     * 数据的单位
     */
    @TableField(value = "unit")
    private String unit;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}