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
 * @TableName subscribe
 */
@TableName(value ="subscribe")
@Data
@Builder
public class Subscribe implements Serializable {
    /**
     * 
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 
     */
    @TableField(value = "topic")
    private String topic;

    /**
     * 
     */
    @TableField(value = "payload")
    private String payload;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}