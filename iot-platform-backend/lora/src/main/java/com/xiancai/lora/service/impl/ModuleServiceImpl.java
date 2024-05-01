package com.xiancai.lora.service.impl;

import cn.hutool.Hutool;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiancai.lora.model.DTO.mqtt.SensorData;
import com.xiancai.lora.model.entity.*;
import com.xiancai.lora.service.DataService;
import com.xiancai.lora.service.ModuleService;
import com.xiancai.lora.mapper.ModuleMapper;
import com.xiancai.lora.service.NodeService;
import com.xiancai.lora.utils.Result;
import com.xiancai.lora.utils.StringUtils;
import com.xiancai.lora.utils.wrong.check.CheckNormalWrong;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author 86156
* @description 针对表【module】的数据库操作Service实现
* @createDate 2023-01-16 12:13:19
*/
@Service
public class ModuleServiceImpl extends ServiceImpl<ModuleMapper, Module>
    implements ModuleService{

    @Resource
    private CheckNormalWrong checkNormalWrong;

    @Resource
    private NodeService nodeService;

    @Resource
    private DtypeServiceImpl dtypeService;

    @Resource
    private DataService dataService;
    /**
     * MQTT命令 module_poweron
     * @param data
     * @return
     */
    @Override
    public Result poweron(Map<String, Object> data) {
        //统一封装
        UpdateWrapper<Module> wrapper = setUpdateWrapper(data);
        //自定义设置
        wrapper.set("module_status","开启");
        boolean update = update(wrapper);
        checkNormalWrong.checkBool(update,"传感器启动失败");
        return Result.success(update);
    }

    @Override
    public Result poweroff(Map<String, Object> data) {
        UpdateWrapper<Module> wrapper = setUpdateWrapper(data);
        wrapper.set("module_status","关闭");
        boolean update = update(wrapper);
        checkNormalWrong.checkBool(update,"传感器关闭失败");
        return Result.success(update);
    }

    /**
     * data里面是硬件穿过来的
     * @param data
     * @return
     */
    @Override
    public Result operate(Map<String, Object> data) {
        List<Float> sensorDatas = Convert.toList(Float.class, data.get("data"));
        String port = (String) data.get("port");
        //假的id,实际上是ids
        String ids = (String)data.get("ids");
        Node node = nodeService.query().eq("ids", ids).one();
        Integer nodeId = node.getId();
        Module module = this.query().eq("node_id", nodeId).eq("port",port).one();
        Integer sensorId = module.getDetailId();
        List<SensorData> result=new ArrayList<>();
        checkNormalWrong.checkId(sensorId,"数据库查到的sensorId错误");
        for (int i = 0; i < sensorDatas.size(); i++) {
            String time = StringUtils.stampToDate((String) data.get("messageId"));
            Dtype dtype = dtypeService.query().eq("sensor_id", sensorId).eq("sweat", i).one();
            SensorData sensorData = SensorData.builder().data(sensorDatas.get(i).toString()).dataType(dtype.getDataType())
                    .time(time).unit(dtype.getUnit()).build();
            DataS dataS = DataS.builder().data(sensorDatas.get(i).toString()).sensorId(sensorId)
                    .dataTime(StringUtils.stampToDateObject((String) data.get("messageId"))).dataType(dtype.getDataType()).build();
            dataService.save(dataS);
            result.add(sensorData);
        }
        return Result.success(result);

    }

    private  UpdateWrapper<Module> setUpdateWrapper(Map<String,Object> data){
        UpdateWrapper<Module> wrapper = new UpdateWrapper<>();
        wrapper.eq("module_type","sensor");
        data.forEach((k,v)->{
            if(k.equals("id")){
                k="node_id";
            }
            wrapper.eq(k,v.toString());

        });

        return wrapper;
    }
}




