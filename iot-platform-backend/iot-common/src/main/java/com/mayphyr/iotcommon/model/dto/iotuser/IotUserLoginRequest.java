package com.mayphyr.iotcommon.model.dto.iotuser;

import lombok.Data;

@Data
public class IotUserLoginRequest {

    private String email;

    private String password;

}
