package com.mayphyr.iotcommon.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName interfac_response_param_desc
 */
@TableName(value ="interfac_response_param_desc")
@Data
public class InterfacResponseParamDesc implements Serializable {
    /**
     * 接口id
     */
    private Long infaId;

    /**
     * 响应参数名称
     */
    private String name;

    /**
     * 响应参数类型
     */
    private String type;

    /**
     * 响应参数的示例数据
     */
    private String example;

    /**
     * 响应参数的描述
     */
    private String description;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}