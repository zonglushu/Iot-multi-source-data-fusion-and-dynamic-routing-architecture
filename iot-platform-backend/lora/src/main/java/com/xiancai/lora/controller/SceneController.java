package com.xiancai.lora.controller;

import cn.hutool.core.bean.BeanUtil;
import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.model.DTO.LatestDataDTO;
import com.xiancai.lora.service.LoraService;
import com.xiancai.lora.service.SensorService;
import com.xiancai.lora.utils.Result;
import com.xiancai.lora.utils.UserHolder;
import com.xiancai.lora.utils.wrong.check.CheckNormalWrong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.Map;

import static com.xiancai.lora.enums.StatusCode.PARAMS_ERR;
import static com.xiancai.lora.enums.StatusCode.SYSTEM_ERR;

/**
 * 场景控制器
 */
@RestController
@RequestMapping("/scene")
public class SceneController {
    @Autowired
    private LoraService loraService;

    @Resource
    private MQTTController mqttController;

    @Resource
    private CheckNormalWrong checkNormalWrong;

    @Autowired
    private SensorService sensorService;

    @GetMapping("/search")
    public Result searchAllDevice(){
        Integer userId = UserHolder.getUser().getId();
        return loraService.getAllDevice(userId);
    }

    /**
     * 计算Io本次用电时长，计算方法是：
     * 先确认这次是开的，得先跟硬件联调得到当前的IO口的状态，如果是开的话才计算，如果是关的话直接报错
     * 然后Redis里面找最近一次他开的信息，因为messageId就是时间戳，所以拿时间戳减就好了
     * @param nodeId
     * @param ox 哪个io,io口的位置
     * @return
     */
    @GetMapping("/io/thisDuration")
    public Result getIoThisDuration(@RequestParam("nodeid") Integer nodeId,Integer ox){
        checkNormalWrong.checkId(nodeId);
        checkNormalWrong.checkInteger(ox,"o口的下标错误");
        Result ioInfo = mqttController.getIOInfo(nodeId);
        Object data = ioInfo.getData();
        Map<String, Object> map = BeanUtil.beanToMap(data);
        Character status = (Character) map.get("o" + ox);
        if(status.equals("0")){
            throw new BusinessException(SYSTEM_ERR.getMessage(), SYSTEM_ERR.getCode(), "当前o"+ox+"未打开");
        }

        return Result.success(status);
    }

    /**
     * 计算Io总用电时长，计算方法是：
     * 从Redis里面找到第一次开的信息，然后拿当前的时间戳减去就好了
     * @param nodeId
     * @param ox
     * @return
     */
    @GetMapping("/io/allDuration")
    public Result getIoAllDuration(@RequestParam("nodeid") Integer nodeId,Integer ox){
        return null;
    }


}
