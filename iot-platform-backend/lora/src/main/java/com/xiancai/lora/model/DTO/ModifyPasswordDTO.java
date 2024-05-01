package com.xiancai.lora.model.DTO;

import lombok.Data;

@Data
public class ModifyPasswordDTO {
    /**
     *
     */
    private String email;
    /**
     *
     */
    private String oldPassword;
    /**
     *
     */
    private String newPassword;

    private String emailCode;
}
