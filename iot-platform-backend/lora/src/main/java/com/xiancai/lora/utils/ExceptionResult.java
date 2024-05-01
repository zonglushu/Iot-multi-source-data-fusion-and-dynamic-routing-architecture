package com.xiancai.lora.utils;

import lombok.Data;

@Data
public class ExceptionResult {
    private int code;

    private String message;

    private String description;

    private Object data;

    public ExceptionResult(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
        this.data=null;
    }
}
