package com.mayphyr.iotcommon.model.dto.apipass;

import lombok.Data;

@Data
public class AddApiPassRequest {
    private Long userId;

    private String email;
}
