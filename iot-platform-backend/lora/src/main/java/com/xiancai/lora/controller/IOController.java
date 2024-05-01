package com.xiancai.lora.controller;

import com.xiancai.lora.model.VO.UserVo;
import com.xiancai.lora.service.IoService;
import com.xiancai.lora.utils.Result;
import com.xiancai.lora.utils.UserHolder;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/io")
public class IOController {

    @Resource
    private IoService ioService;

    @GetMapping("/get/by/node")
    public Result gerIOByNode(){
        UserVo user = UserHolder.getUser();
        Integer userId = user.getId();
        return ioService.getIOByNode(userId);
    }
}
