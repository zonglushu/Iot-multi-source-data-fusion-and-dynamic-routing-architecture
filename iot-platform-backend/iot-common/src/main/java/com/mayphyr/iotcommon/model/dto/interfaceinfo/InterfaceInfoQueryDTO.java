package com.mayphyr.iotcommon.model.dto.interfaceinfo;


import com.mayphyr.iotcommon.model.dto.PageRequest;
import lombok.Data;

import java.util.Date;

@Data
public class InterfaceInfoQueryDTO extends PageRequest {

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 描述
     */
    private String description;

    /**
     * 请求方法 GET,HEAD,POST,PUT,PATCH,DELETE,OPTIONS,TRACE
     */
    private String requestMethod;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 创建人
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 接口项目id
     */
    private Long projectID;

    /**
     * 是否删除
     */
    private Integer isDelete;
}
