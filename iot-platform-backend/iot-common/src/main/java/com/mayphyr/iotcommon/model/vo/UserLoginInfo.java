package com.mayphyr.iotcommon.model.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginInfo {

    /**
     * 用户登陆需要的token
     */
    private String token;

    /**
     * 用户的ak
     */
    private String ak;
    /**
     * 用户的加密后的sk
     */
    private String encryptSk;
}
