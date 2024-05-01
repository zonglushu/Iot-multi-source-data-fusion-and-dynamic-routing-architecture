package com.xiancai.lora.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName module
 */
@TableName(value ="module")
@Data
public class Module extends TreeNode implements Serializable  {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 前端传来的EUI
     */
    @TableField(value = "ids")
    private String ids;

    /**
     * 该 module的名称
     */
    @TableField(value = "module_name")
    private String moduleName;

    /**
     * 表示该moudule是哪种设备
     */
    @TableField(value = "module_type")
    private String moduleType;

    /**
     * 关联设备的具体id
     */
    @TableField(value = "detail_id")
    private Integer detailId;

    /**
     * module的上级节点id
     */
    @TableField(value = "node_id")
    private Integer nodeId;

    /**
     * 表示该module位于上级node的哪个port
     */
    @TableField(value = "port")
    private Integer port;

    /**
     * 表示该module的状态
     */
    @TableField(value = "module_status")
    private String moduleStatus;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}