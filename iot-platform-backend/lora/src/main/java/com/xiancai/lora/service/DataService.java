package com.xiancai.lora.service;

import com.xiancai.lora.model.DTO.ChartSensorDataDTO;
import com.xiancai.lora.model.entity.DataS;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiancai.lora.utils.Result;

/**
* @author 86156
* @description 针对表【data】的数据库操作Service
* @createDate 2022-11-30 10:48:48
*/
public interface DataService extends IService<DataS> {

    Result getSensorData(ChartSensorDataDTO chartSensorDataDTO);

    Result searchDataToList(Integer nodeId,String moduleType);

    Result searchDataType(Integer nodeId, Integer port, Integer userId);
}
