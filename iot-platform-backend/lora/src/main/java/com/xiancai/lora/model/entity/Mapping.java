package com.xiancai.lora.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName mapping
 */
@TableName(value ="mapping")
@Data
public class Mapping implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 数据类型的英文
     */
    @TableField(value = "en")
    private String en;

    /**
     * 数据类型的中文
     */
    @TableField(value = "ch")
    private String ch;

    /**
     * 数据的单位
     */
    @TableField(value = "unit")
    private String unit;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}