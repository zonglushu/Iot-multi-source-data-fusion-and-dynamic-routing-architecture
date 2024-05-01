package com.mayphyr.iotcommon.model.dto.apipass;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 */
@Data
public class GenApiPassRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private Long id;

    private String email;

    /**
     * 旧的accessKey
     */
    private String accessKey;

    /**
     * 旧的sercetKey
     */
    private String sercetKey;
}
