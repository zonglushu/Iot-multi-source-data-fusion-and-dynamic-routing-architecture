package com.xiancai.lora.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiancai.lora.constant.PurchasedDevice;
import com.xiancai.lora.mapper.DataMapper;
import com.xiancai.lora.model.DTO.LatestDataDTO;
import com.xiancai.lora.model.DTO.SearchSensor;
import com.xiancai.lora.model.VO.SensorByNode;
import com.xiancai.lora.model.VO.data.DataVo;
import com.xiancai.lora.model.VO.SensorVo;
import com.xiancai.lora.model.VO.data.OtherDeviceVo;
import com.xiancai.lora.model.VO.data.SearchDataListVo;
import com.xiancai.lora.model.entity.*;
import com.xiancai.lora.service.*;
import com.xiancai.lora.mapper.SensorMapper;
import com.xiancai.lora.utils.Result;
import com.xiancai.lora.utils.UserHolder;
import com.xiancai.lora.utils.wrong.check.CheckNormalWrong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 86156
 * @description 针对表【sensor】的数据库操作Service实现
 * @createDate 2022-11-30 11:07:04
 */
@Slf4j
@Service
public class SensorServiceImpl extends ServiceImpl<SensorMapper, Sensor>
        implements SensorService {
    @Autowired
    private DataMapper dataMapper;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private DataService dataService;

    @Resource
    private ModuleService moduleService;

    @Resource
    private CheckNormalWrong checkNormalWrong;

    @Resource
    private MappingService mappingService;

    @Override
    public Result searchLatestData(LatestDataDTO latestDataDTO) {
        Integer nodeId = latestDataDTO.getNodeId();
        String port = latestDataDTO.getPort();
        String dataType = latestDataDTO.getDataType();
        Module module = moduleService.getOne(new QueryWrapper<Module>().eq("node_id", nodeId)
                .eq("port", port).eq("module_type","sensor"));
        checkNormalWrong.isNULL(module,"根据nodeId和port找不到对应的module");
        DataS latestData = dataMapper.getLatestData(module.getDetailId(),dataType );
        checkNormalWrong.isNULL(latestData,"未找到该传感器的数据");
        SearchDataListVo latestDataVo = BeanUtil.copyProperties(latestData, SearchDataListVo.class);
        latestDataVo.setSensorName(module.getModuleName());
        return Result.success(latestDataVo);
    }



    @Override
    public Result searchSensor(SearchSensor searchSensor) {
        Integer userId = UserHolder.getUser().getId();
        List<Sensor> sensors = new ArrayList<>();
        Integer nodeId = searchSensor.getNodeId();
        String ids = searchSensor.getIds();
        // 创建一个node队列，
        LinkedList<Node> queue = new LinkedList<>();

        Node one = nodeService.getOne(new QueryWrapper<Node>()
                .eq(checkNormalWrong.checkId(nodeId), "id", nodeId)
                .eq(checkNormalWrong.checkString(ids), "ids", ids)
                .eq("user_id",userId));
        checkNormalWrong.isNULL(one,"根据ids或nodeId未找到node");
        queue.add(one);
        List<Module> modules = moduleService.query().eq("node_id", one.getId()).list();
//        List<Module> modules = traverseSensor(queue);
        List<SensorVo> sensorVos = modules.stream().map(module ->
                SensorVo.builder().sensorType(sensorService.getById(module.getDetailId()).getSensorType())
                        .sensorName(module.getModuleName()).moduleStatus(module.getModuleStatus())
                        .nodeId(module.getNodeId()).ids(module.getIds()).port(module.getPort())
                        .build()
        ).collect(Collectors.toList());
        checkNormalWrong.checkList(sensorVos,"根据ids或nodeId未找到传感器");
        return Result.success(sensorVos);
    }
    @Override
    public Result searchSensorList(Integer userId) {
        //创建一个node队列
        LinkedList<Node> queue = new LinkedList<>();
        QueryWrapper<Node> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        //这里就限定了用户id,因为用户的网关下不允许链接其他用户的节点，所以再这里查就好了
        //我们先是把所有的属于该用户的节点查出来了，其中有一级节点，也有最终节点，所以我们要区分
        List<Node> nodes = nodeService.list(wrapper);
        //将这个用户所有的node填进队列中，然后赵到对应的module
        List<Module> modules=new ArrayList<>();
        for (Node node : nodes) {
            List<Module> userModule = moduleService.query().eq("node_id", node.getId()).list();
            modules.addAll(userModule);
        }

        List<SensorVo> sensorVos = modules.stream().map(module -> {
            Sensor sensor = sensorService.getById(module.getDetailId());
            return SensorVo.builder().ids(module.getIds()).sensorName(module.getModuleName())
                    .port(module.getPort()).moduleStatus(module.getModuleStatus())
                    .sensorType(sensor.getSensorType()).nodeId(module.getNodeId())
                    .build();

        }).collect(Collectors.toList());
        checkNormalWrong.checkList(sensorVos,"未找到该用户名下的传感器");

        return Result.success(sensorVos);
    }

    @Override
    public Result searchSensorDataByNode(Integer nodeId, Integer userId) {
        Node node = nodeService.getById(nodeId);
        checkNormalWrong.checkUser(node.getUserId(),userId);
        LinkedList<Node> queue=new LinkedList<>();
        queue.add(node);
        List<Module> moduleList = traverseSensor(queue);
        List<DataVo> dataVoList=new ArrayList<>();
        List<List<DataVo>> dataVos = moduleList.stream().map(module ->
                dataService.listObjs(new QueryWrapper<DataS>()
                                .eq("id", sensorService.getById(module.getDetailId()).getId()),
                        data -> {
                            DataVo dataVo = BeanUtil.copyProperties(data, DataVo.class);
                            dataVo.setSensorName(module.getModuleName());
                            return dataVo;
                        })
        ).collect(Collectors.toList());
        checkNormalWrong.checkList(dataVos,"通过nodeId未查询到该节点下面传感器的数据");
        for (List<DataVo> dataVo : dataVos) {
            dataVoList.addAll(dataVo);
        }
        return Result.success(dataVoList);
    }

    @Override
    public Result searchOtherDeviceData(String deviceId, String startTime, String endTime, Integer sensorId) {
        //拼接url
        String url = String.format(PurchasedDevice.URL + "?deviceid=%s&start=%s&end=%s"
                , deviceId, startTime, endTime);
        //发送url，得到数据
        String jsonResult = HttpUtil.get(url);
        //将得到的json数据转换为JsonArray
        JSONArray array = JSONUtil.parseArray(jsonResult);
        List<DataS> dataList=new ArrayList<>();
        List<OtherDeviceVo> dataVoList=new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //得到传感器的名称
        Sensor sensor = sensorService.getById(sensorId);
        for (Object obj : array.stream().toArray()) {
            //处理数据
            JSONObject sObj = (JSONObject) obj;
            Date date = sObj.get("dtime", Date.class);
            String EnType = sObj.get("item", String.class);
            String formatTime = sdf.format(date);
            Mapping en = mappingService.query().eq("en", EnType).one();
            // 封装数据库的数据对象
            DataS build = DataS.builder().dataType(en.getCh()).data(sObj.get("data", String.class))
                    .dataTime(date).sensorId(sensor.getId()).build();

            OtherDeviceVo dataVo = OtherDeviceVo.builder().data(sObj.get("data", String.class)).dataTime(formatTime).sensorName("测试传感器").sensorName(sensor.getSensorType()).dataType(en.getCh()).unit(en.getUnit()).build();
            //插入数据库的数据
            dataList.add(build);
            //返回前端的数据
            dataVoList.add(dataVo);
        }
        for (DataS dataS : dataList) {
            System.out.println(dataS);
        }
//        dataService.saveBatch(dataList);
        return Result.success(dataVoList);
    }

    @Override
    public Result searchSensorByOrder(Integer userId) {
        List<SensorByNode> sensorByNodes = new ArrayList<>();
        List<Node> userNodes = nodeService.query().eq("user_id", userId).list();
        boolean isNull = ObjectUtil.isNull(userNodes);
        if(isNull){
            return Result.success(Collections.emptyList());
        }else {
            userNodes.forEach(node -> {
                checkNormalWrong.isNULL(node,"node");
                List<Module> modules = moduleService.query().eq(ObjectUtil.isNull(node), "node_id", node.getId()).list();
                SensorByNode sensors = SensorByNode.builder().nodeName(node.getNodeName()).moduleList(modules).build();
                sensorByNodes.add(sensors);
            });
        }
        return Result.success(sensorByNodes);
    }

    private List<Module> traverseSensor(LinkedList<Node> queue){
        List<Module> modules= new ArrayList<>();
        while (!queue.isEmpty()) {
            //从队列中拿出来一个node
            Node root = queue.pop();
            Integer isLora = root.getIsLora();
            //判断这个node是不是网关，
            if (isLora!=null&&isLora>0) {
                //如果是网关的话，那就先找到这个网关下的节点
                for (Node node : nodeService.list(new QueryWrapper<Node>().eq("lora_id",isLora))) {
                    //判断得到的一级网关下的节点是不是网关，如果是的话，就再次加到队列中
                    if(node.getIsLora()!=null&&node.getIsLora()>0){
                        queue.add(node);
                    }else {
                        //如果不是的话，还是直接找module
                        List<Module> moduleList = moduleService.list(new QueryWrapper<Module>()
                                .eq("node_id", node.getId())
                                .eq("module_type","sensor"));
                        modules.addAll(moduleList);
                    }
                }
                //如果这个node不是一级网关节点，那就直接找这个网关下的module
            }else {
                List<Module> moduleList = moduleService.list(new QueryWrapper<Module>().eq("node_id", root.getId()));
                modules.addAll(moduleList);
            }
        }
        System.out.println("已经找完module了");
        return modules;
    }



}




