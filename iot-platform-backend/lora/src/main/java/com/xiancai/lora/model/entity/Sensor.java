package com.xiancai.lora.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName sensor
 */
@TableName(value ="sensor")
@Data
public class Sensor extends TreeNode implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * sensor的类型
     */
    @TableField(value = "sensor_type")
    private String sensorType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}