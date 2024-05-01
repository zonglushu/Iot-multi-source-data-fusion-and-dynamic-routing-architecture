package com.xiancai.lora.controller;

import com.xiancai.lora.MQTT.publish.PublishMessage;
import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.model.DTO.SearchSensor;
import com.xiancai.lora.model.VO.UserVo;
import com.xiancai.lora.service.SensorService;
import com.xiancai.lora.utils.Result;
import com.xiancai.lora.utils.UserHolder;
import com.xiancai.lora.utils.wrong.check.CheckNormalWrong;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.xiancai.lora.enums.StatusCode.NULL_ERR;

/**
 * 传感器控制器
 */
@RestController
@RequestMapping("/sensor")
public class SensorController {
    @Resource
    private SensorService sensorService;

    @Resource
    private CheckNormalWrong checkNormalWrong;

    /**
     * 根据ids或nodeId查找传感器
     * @param searchSensor
     * @return
     */
    @PostMapping("/search")
    public Result searchSensor(@RequestBody SearchSensor searchSensor){
        return sensorService.searchSensor(searchSensor);
    }

    /**
     * 查找某个用户下的所以传感器
     * @return
     */
    @GetMapping("/sensorlist")
    public Result searchSensorList(){
        Integer userId = UserHolder.getUser().getId();
        return sensorService.searchSensorList(userId);
    }

    /**
     * 查找某个节点下所有传感器的数据
     * @param nodeId
     * @return
     */
    @GetMapping("/datalist")
    public Result searchSensorDataByNode(@RequestParam("nodeid")Integer nodeId,
                                         @RequestParam("moduleType")String moduleType){
        checkNormalWrong.checkString(moduleType,"前端传入的moduleType错误");
        checkNormalWrong.checkId(nodeId,"前端传入的nodeId错误");
        Integer userId = UserHolder.getUser().getId();
        return sensorService.searchSensorDataByNode(nodeId,userId);
    }

    /**
     * 买的传感器的数据
     * @param deviceId
     * @param startTime
     * @param endTime
     * @param sensorId
     * @return
     */
    @GetMapping("/other/data")
    public Result searchOtherDeviceData(@RequestParam("deviceid")String deviceId,
                                        @RequestParam("start") String startTime,
                                        @RequestParam("end") String endTime,
                                        @RequestParam("sensorid")Integer sensorId){
        checkNormalWrong.checkString(deviceId,"前端传入的设备id错误");
        checkNormalWrong.checkInteger(sensorId,"前端传入的传感器id错误");
        checkNormalWrong.checkString(startTime,"前端传入的起始时间错误");
        checkNormalWrong.checkString(endTime,"前端传入的结束时间错误");
        return sensorService.searchOtherDeviceData(deviceId,startTime,endTime,sensorId);
    }
    /**
     * 买的传感器的数据(VR设备调用)
     * @param deviceId
     * @param startTime
     * @param endTime
     * @param sensorId
     * @return
     */
    @GetMapping("/other/data/vr")
    public Result searchOtherDeviceDataVR(@RequestParam("deviceid")String deviceId,
                                        @RequestParam("start") String startTime,
                                        @RequestParam("end") String endTime,
                                        @RequestParam("sensorid")Integer sensorId){
        checkNormalWrong.checkString(deviceId,"前端传入的设备id错误");
        checkNormalWrong.checkInteger(sensorId,"前端传入的传感器id错误");
        checkNormalWrong.checkString(startTime,"前端传入的起始时间错误");
        checkNormalWrong.checkString(endTime,"前端传入的结束时间错误");
        return sensorService.searchOtherDeviceData(deviceId,startTime,endTime,sensorId);
    }

    @GetMapping("/by/order")
    public Result searchSensorByOrder(){
        UserVo user = UserHolder.getUser();
        Integer id = user.getId();
        return sensorService.searchSensorByOrder(id);
    }






}
