package com.xiancai.lora.exception;


import lombok.Data;

/**
 * 自定义异常类
 * @author xiancai
 */
@Data
public class BusinessException extends RuntimeException{

    private int code;

    private String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }
}

