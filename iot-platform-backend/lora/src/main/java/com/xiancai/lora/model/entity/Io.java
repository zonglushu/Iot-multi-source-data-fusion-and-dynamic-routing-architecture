package com.xiancai.lora.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName IO
 */
@TableName(value ="IO")
@Data
public class Io implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * I/O控制的器件名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * I/O隶属于哪个node
     */
    @TableField(value = "node_id")
    private Integer nodeId;

    /**
     * 表示是这个节点的第几个I/O口
     */
    @TableField(value = "ox")
    private Integer ox;

    /**
     * 表示当前I/O的状态  0表示关，1表示开
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 表示当前I/O的状态  0表示关，1表示开
     */
    @TableField(value = "type")
    private Integer type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}