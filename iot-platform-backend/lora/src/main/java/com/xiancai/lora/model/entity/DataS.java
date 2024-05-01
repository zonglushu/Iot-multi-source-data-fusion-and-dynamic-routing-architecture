package com.xiancai.lora.model.entity;

import java.io.Serializable;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
* 
* @TableName data
*/
@Builder
@Data
@TableName(value ="data")
public class DataS implements Serializable {

    /**
    * 
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
    * 
    */
    @TableField(value = "sensor_id")
    private Integer sensorId;
    /**
    * 
    */
    @TableField(value = "data_type")
    private String dataType;
    /**
    * 
    */

    private String data;
    /**
    * 
    */
    @TableField(value = "data_time")
    private Date dataTime;







}
