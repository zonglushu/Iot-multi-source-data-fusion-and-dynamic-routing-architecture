package com.xiancai.lora.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.model.DTO.ChartSensorDataDTO;
import com.xiancai.lora.model.DTO.DataType;
import com.xiancai.lora.model.VO.data.DataVo;
import com.xiancai.lora.model.VO.data.SearchDataListVo;
import com.xiancai.lora.model.entity.DataS;
import com.xiancai.lora.model.entity.Module;
import com.xiancai.lora.service.DataService;
import com.xiancai.lora.mapper.DataMapper;
import com.xiancai.lora.service.ModuleService;
import com.xiancai.lora.service.NodeService;
import com.xiancai.lora.utils.Result;
import com.xiancai.lora.utils.convert.Converter;
import com.xiancai.lora.utils.wrong.check.CheckNormalWrong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.xiancai.lora.enums.StatusCode.PARAMS_ERR;
import static java.util.stream.Collectors.groupingBy;

/**
* @author 86156
* @description 针对表【data】的数据库操作Service实现
* @createDate 2022-11-30 10:48:48
*/
@Service
public class DataServiceImpl extends ServiceImpl<DataMapper, DataS>
    implements DataService{

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private CheckNormalWrong checkNormalWrong;

    @Autowired
    private NodeService nodeService;
    @Override
    public Result getSensorData(ChartSensorDataDTO chartSensorDataDTO) {
        Integer nodeId = chartSensorDataDTO.getNodeId();
        Integer port = chartSensorDataDTO.getPort();
        String dataType = chartSensorDataDTO.getDataType();
        String ids = chartSensorDataDTO.getIds();
        String[] dataTime = chartSensorDataDTO.getDataTime();
        //要先要找到那个唯一module,条件ids 和nodeId,port二选一
        Module module = moduleService.getOne(new QueryWrapper<Module>().eq(StrUtil.isBlank(ids), "port", port)
                .eq(StrUtil.isBlank(ids), "node_id", nodeId)
                .eq(nodeId == null || port == null, "ids", ids));
        checkNormalWrong.isNULL(module,"数据库查找module错误");
        //然后判断module里面的是不是sensor,是就查sensor表的数据，如果不是，查其他表的数据，这以后再说
        String moduleType = module.getModuleType();
        if(moduleType.equals("sensor")){
            List<DataS> list = this.list(new QueryWrapper<DataS>().eq("sensor_id", module.getDetailId()).between("data_time", dataTime[0], dataTime[1]).eq("data_type", dataType));
            if(list.size()==0){
                throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "数据库查询为空");
            }
            List<DataVo> dataVo = Converter.getDataVo(list, module.getModuleName());
            return Result.success(dataVo);
        }else {
            //TODO 以后如果不是sensor，再说
            return Result.success("现在还没有查询module为除sensor以外的类型的数据");
        }

    }

    /**
     * 原来是直接传的是sensorId,是一个具体的sensor
     * 现在是nodeId,moduleType是多个sensor
     * @param nodeId
     * @param moduleType
     * @return
     */
    @Override
    public Result searchDataToList(Integer nodeId,String moduleType) {
        List<Module> moduleList = moduleService.list(new QueryWrapper<Module>().eq("node_id", nodeId)
                .eq("module_type", moduleType));
        Map<Integer, List<Module>> collect = moduleList.stream().collect(groupingBy(Module::getDetailId));
        List<SearchDataListVo> list = list(new QueryWrapper<DataS>()
                .in(moduleList!=null||!moduleList.isEmpty(),"sensor_id"
                        ,moduleList.stream().map(Module::getDetailId).collect(Collectors.toList())))
                .stream().map(dataS -> {
                    SearchDataListVo searchDataListVo = BeanUtil.copyProperties(dataS, SearchDataListVo.class);
                    searchDataListVo.setSensorName(collect.get(dataS.getSensorId()).get(0).getModuleName());
                    return searchDataListVo;
                }).collect(Collectors.toList());
        checkNormalWrong.checkList(list,"查询不到data");
        return Result.success(list);

    }

    @Override
    public Result searchDataType(Integer nodeId, Integer port, Integer userId) {
        Long count = nodeService.query().eq("id", nodeId).eq("user_id", userId).count();
        if(count==null||count<=0){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "该节点不是您名下的，不能操作他人的节点");
        }
        Module one = moduleService.query().eq("node_id", nodeId).eq("port", port).eq("module_type","sensor").one();
        if(one==null){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "未查到对应module，请检查nodeId和port是否有误");
        }
        Integer sensorId = one.getDetailId();
        if(sensorId==null){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "sensorId为空，检查数据库");
        }
        QueryWrapper<DataS> wrapper = new QueryWrapper<>();
        wrapper.select("data_type as type").eq("sensor_id",sensorId).groupBy("data_type");
        List<Map<String, Object>> maps = this.listMaps(wrapper);

        List<DataType> types = maps.stream().map(map -> DataType.builder().type((String) map.get("type")).build()).collect(Collectors.toList());
        return Result.success(types);

    }
}




