package com.xiancai.lora.exception;

import com.xiancai.lora.enums.StatusCode;
import com.xiancai.lora.utils.ExceptionResult;
import com.xiancai.lora.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ExceptionResult businessExceptionHandler(BusinessException e){
        log.error("businessException:   "+ e.getMessage(),e);
        return new ExceptionResult(e.getCode(),e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(Exception.class)
    public ExceptionResult runtimeExceptionHandler(Exception e){
        log.error("runtimeException:       ",e);
        return new ExceptionResult(200,e.getMessage(),"非程序异常，检测参数或请求方式等");
    }



}
