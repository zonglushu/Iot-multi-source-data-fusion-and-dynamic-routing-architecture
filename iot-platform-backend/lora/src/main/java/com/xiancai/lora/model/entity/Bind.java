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
* @TableName bind
*/
@TableName(value ="bind")
@Data
@Builder
public class Bind implements Serializable {

    /**
    * 
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
    * 对应EUI
    */
    private String ids;
    /**
    * 
    */
    @TableField(value = "code")
    private String code;
    /**
    * 
    */
    @TableField(value = "status")
    private String status;
    /**
    * 
    */
    @TableField(value = "production_time")
    private Date productionTime;

}
