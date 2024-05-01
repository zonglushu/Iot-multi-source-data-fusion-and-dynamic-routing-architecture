package com.xiancai.lora.MQTT.service.proxy;

import cn.hutool.core.convert.Convert;
import com.xiancai.lora.model.DTO.mqtt.SensorData;
import com.xiancai.lora.model.entity.Dtype;
import com.xiancai.lora.service.DtypeService;
import com.xiancai.lora.service.ModuleService;
import com.xiancai.lora.service.impl.DtypeServiceImpl;
import com.xiancai.lora.utils.Result;
import com.xiancai.lora.utils.wrong.check.CheckNormalWrong;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Service
public class ProxyModuleService {


    /**
     * 拿到的命令中
     */
    @Resource
    private ModuleService moduleService;

    @Resource
    private DtypeService dtypeService;

    @Resource
    private CheckNormalWrong checkNormalWrong;

    public Result poweron(Map<String,Object> data){
        return moduleService.poweron(data);
    }

    public Result poweroff(Map<String,Object> data){
        return moduleService.poweroff(data);
    }

    public Result operate(Map<String,Object> data){

//        List<Float> sensorDatas = Convert.toList(Float.class, data.get("data"));
//        Integer port = (Integer) data.get("port");
//        Integer nodeId = (Integer)data.get("id");
//        Integer sensorId = moduleService.query().eq("port", port).eq("node_id", nodeId).one().getDetailId();
//        checkNormalWrong.checkId(sensorId,"数据库查到的sensorId错误");
//        List<SensorData> result=new ArrayList<>();
//        for (int i = 0; i < sensorDatas.size(); i++) {
//            Dtype dtype = dtypeService.query().eq("sensor_id", sensorId).eq("sweat", i).one();
//            SensorData sensorData = SensorData.builder().data(sensorDatas.get(i).toString()).dataType(dtype.getDataType())
//                    .time((Date) data.get("messageId")).unit(dtype.getUnit()).build();
//            result.add(sensorData);
//        }
        return moduleService.operate(data);
    }
}
