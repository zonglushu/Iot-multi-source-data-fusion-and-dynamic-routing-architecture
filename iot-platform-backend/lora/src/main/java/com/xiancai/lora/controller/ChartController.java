package com.xiancai.lora.controller;


import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.model.DTO.ChartSensorDataDTO;
import com.xiancai.lora.service.DataService;
import com.xiancai.lora.service.LoraService;
import com.xiancai.lora.service.SensorService;
import com.xiancai.lora.utils.Result;
import com.xiancai.lora.utils.UserHolder;
import com.xiancai.lora.utils.wrong.check.CheckNormalWrong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.xiancai.lora.enums.StatusCode.PARAMS_ERR;

/**
 * 图表数据控制器
 */
@RestController
@RequestMapping("/chart")
public class ChartController {
    @Autowired
    private DataService dataService;

    @Resource
    private CheckNormalWrong checkNormalWrong;

    /**
     * 返回指定时间段内的数据
     * @param chartSensorDataDTO
     * @return
     */
    @PostMapping("/sensordata")
    public Result getSensorData(@RequestBody ChartSensorDataDTO chartSensorDataDTO){
        checkNormalWrong.checkWrongIgnoreSome(chartSensorDataDTO,"ids","port","nodeId");
        return dataService.getSensorData(chartSensorDataDTO);
    }

}
