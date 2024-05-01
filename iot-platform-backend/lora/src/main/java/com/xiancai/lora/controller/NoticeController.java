package com.xiancai.lora.controller;

import com.xiancai.lora.model.VO.Notice;
import com.xiancai.lora.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 公告控制器
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {
    @GetMapping("/notice")
    private Result getNotice(){
        return Result.success(Notice.builder().notice("公告功能测试版 v1.0.0").build());
    }
}
