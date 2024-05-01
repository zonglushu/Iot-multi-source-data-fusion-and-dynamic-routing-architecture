package com.xiancai.lora.model.DTO;


import lombok.Data;

import java.util.Date;

@Data
public class RegisterDTO {
    /**
     *
     */
    private String username;

    /**
     *
     */
    private String password;
    /**
     *
     */
    private String checkPassword;

    /**
     *
     */
    private String email;

    /**
     *
     */
    private String emailCode;

    /**
     *
     */
    private String phone;

    /**
     *
     */
    private String company;

    /**
     *
     */
    private String website;
}
