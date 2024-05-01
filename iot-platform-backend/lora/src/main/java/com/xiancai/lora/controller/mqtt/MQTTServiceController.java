package com.xiancai.lora.controller.mqtt;

import com.xiancai.lora.service.ModuleService;
import com.xiancai.lora.service.NodeService;
import com.xiancai.lora.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 给消息传输模块提供操作数据库的接口
 */
@RestController
@RequestMapping("/ms")
public class MQTTServiceController {


    @Resource
    private NodeService nodeService;

    @Resource
    private ModuleService moduleService;


    /**
     * 开启模块
     * @param data
     * @return
     */
    @PostMapping("module/poweron")
    public Result poweron(Map<String, Object> data){
       return moduleService.poweron(data);
    }
}
