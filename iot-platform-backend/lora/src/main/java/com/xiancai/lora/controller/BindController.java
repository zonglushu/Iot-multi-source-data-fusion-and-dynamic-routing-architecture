package com.xiancai.lora.controller;


import com.xiancai.lora.model.DTO.BindDTO;
import com.xiancai.lora.service.BindService;
import com.xiancai.lora.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/bind")
public class BindController {

    @Resource
    private BindService bindService;

    @PostMapping("/binddevice")
    public Result setBind(@RequestBody BindDTO bindDTO){
       return bindService.setBind(bindDTO);
    }

    @GetMapping("unbind")
    public Result relieveBind(@RequestParam("nodeid") Integer nodeId){
        return bindService.relieveBind(nodeId);
    }

    @GetMapping("/transfer")
    public Result TranserBind(@RequestParam("nodeid") Integer nodeId){
        return null;
    }
}
