package com.mayphyr.iotcommon.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName interface_request_param_desc
 */
@TableName(value ="interface_request_param_desc")
@Data
public class InterfaceRequestParamDesc implements Serializable {
    /**
     * 接口id
     */
    private Long infaId;

    /**
     * 参数名称
     */
    private String name;

    /**
     * 参数类型
     */
    private String type;

    /**
     * 参数是否必须传递.  (0-不是必须，1-是必须)
     */
    private Integer must;


    /**
     * 请求参数的示例数据
     */
    private String example;

    /**
     * 请求参数的描述
     */
    private String description;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}