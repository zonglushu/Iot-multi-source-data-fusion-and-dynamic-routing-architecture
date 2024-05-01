package com.mayphyr.iotcommon.model.dto;

import lombok.Data;

@Data
public class ApiInfo {
    /**
     * 请求主机名：如 124.xxx.xxx.xxx:8081
     */
    private String host;
    /**
     * 请求地址：如 api/user/login
     */
    private String uri;
    /**
     * 请求方式：如GET,POST
     */
    private String method;

    public ApiInfo(String host, String uri) {
        this.host = host;
        this.uri = uri;
    }
}
