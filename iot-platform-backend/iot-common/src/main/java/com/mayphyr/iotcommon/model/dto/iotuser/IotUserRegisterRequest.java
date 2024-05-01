package com.mayphyr.iotcommon.model.dto.iotuser;


import lombok.Data;



@Data
public class IotUserRegisterRequest {

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 确认密码
     */
    private String checkPassword;

    /**
     * 用户名称
     */
    private String name;






}
