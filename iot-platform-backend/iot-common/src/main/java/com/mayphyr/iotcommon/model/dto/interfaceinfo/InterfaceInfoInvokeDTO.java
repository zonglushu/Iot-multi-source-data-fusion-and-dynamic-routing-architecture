package com.mayphyr.iotcommon.model.dto.interfaceinfo;


import lombok.Data;

import java.io.Serializable;


/**
 * 模拟请求调用类
 */
@Data
public class InterfaceInfoInvokeDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 请求方法
     */
    private String method;
    /**
     * 请求参数
     */
    private String requestParams;
    /**
     * 主机号
     */
    private String host;

}
