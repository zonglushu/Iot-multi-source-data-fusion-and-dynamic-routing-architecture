package com.xiancai.lora.service;

import com.xiancai.lora.model.DTO.LatestDataDTO;
import com.xiancai.lora.model.DTO.SearchSensor;
import com.xiancai.lora.model.entity.Sensor;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiancai.lora.utils.Result;

/**
* @author 86156
* @description 针对表【sensor】的数据库操作Service
* @createDate 2022-11-30 11:07:04
*/
public interface SensorService extends IService<Sensor> {


    Result searchLatestData(LatestDataDTO latestDataDTO);



    Result searchSensor(SearchSensor searchSensor);

    Result searchSensorList(Integer userId);

    Result searchSensorDataByNode(Integer nodeId, Integer userId);

    Result searchOtherDeviceData(String deviceId,String startTime,String endTime,Integer sensorId);

    Result searchSensorByOrder(Integer userId);
}
