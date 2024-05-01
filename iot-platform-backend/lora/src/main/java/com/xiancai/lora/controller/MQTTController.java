package com.xiancai.lora.controller;

import cn.hutool.json.JSONArray;
import com.xiancai.lora.MQTT.util.process.MQTTProcess;
import com.xiancai.lora.model.DTO.LatestDataDTO;
import com.xiancai.lora.model.DTO.mqtt.IO.IoInfo;
import com.xiancai.lora.model.DTO.mqtt.IO.MQTTIoInfo;
import com.xiancai.lora.model.DTO.mqtt.MQTTModuleSensorData;
import com.xiancai.lora.model.DTO.mqtt.MQTTNormalDTO;
import com.xiancai.lora.model.DTO.mqtt.MQTTSensor;
import com.xiancai.lora.model.DTO.mqtt.UpdateInterval;
import com.xiancai.lora.service.IoService;
import com.xiancai.lora.service.SensorService;
import com.xiancai.lora.utils.Result;
import com.xiancai.lora.utils.wrong.check.CheckNormalWrong;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mqtt")
public class MQTTController {

    @Resource
    private IoService ioService;

    @Resource
    private MQTTProcess mqttProcess;

    @Resource
    private CheckNormalWrong checkNormalWrong;

    @Resource
    private SensorService sensorService;


    /**
     * 开启传感器
     * @param MQTTSensor
     * @return
     */
    @PostMapping("/sensor/open")
    public Result openSensorByMQTT(@RequestBody MQTTSensor MQTTSensor){
        checkNormalWrong.checkWrong(MQTTSensor);
        return mqttProcess.MQTTProcess(MQTTSensor,"#",13);
    }

    /**
     * 关闭传感器
     * @param MQTTSensor
     * @return
     */
    @PostMapping("/sensor/off")
    public Result offSensorByMQTT(@RequestBody MQTTSensor MQTTSensor){
        checkNormalWrong.checkWrong(MQTTSensor);
        return mqttProcess.MQTTProcess(MQTTSensor,"#",12);
    }

    /**
     * 修改节点上报周期
     * @param updateInterval
     * @return
     */
    @PostMapping("/node/modify/interval")
    public Result modifyNodeInterval(@RequestBody UpdateInterval updateInterval){
        checkNormalWrong.checkWrong(updateInterval);
        return mqttProcess.MQTTProcess(updateInterval,"#",17);
    }

    /**
     * 重启节点
     * @param nodeId
     * @return
     */
    @GetMapping("/node/restart")
    public Result restartNode(@RequestParam("nodeid")  Integer nodeId){
        checkNormalWrong.checkId(nodeId,"前端传入的nodeId错误");
        return mqttProcess.MQTTProcess(MQTTNormalDTO.builder().nodeId(nodeId).build(),"#",3);
    }

    /**
     * 初始化节点
     * @param nodeId
     * @return
     */
    @GetMapping("/node/init")
    public Result initNode(@RequestParam("nodeid")  Integer nodeId){
        checkNormalWrong.checkId(nodeId,"前端传入的nodeId错误");
        return mqttProcess.MQTTProcess(MQTTNormalDTO.builder().nodeId(nodeId).build(),"#",2);
    }

    /**
     * 获取节点定位
     * @param nodeId
     * @return
     */
    @GetMapping("/node/getgps")
    public Result getNodeGps(@RequestParam("nodeid")  Integer nodeId){
        checkNormalWrong.checkId(nodeId,"前端传入的nodeId错误");
        return mqttProcess.MQTTProcess(MQTTNormalDTO.builder().nodeId(nodeId).build(),"#",5);
    }

    /**
     * 查看全部的i/o口的状态
     * @param nodeId
     * @return
     */
    @GetMapping("/io/info")
    public Result getIOInfo(@RequestParam("nodeid") Integer nodeId){
         return mqttGetIOInfo(nodeId);
    }
    private Result mqttGetIOInfo(Integer nodeId){
        // 先调 获取io状态的命令
        checkNormalWrong.checkId(nodeId,"输入的nodeId有误");
        Result status = mqttProcess.MQTTProcess(MQTTNormalDTO.builder().nodeId(nodeId).build(), "#", 28);
        HashMap<String, Object> data = (HashMap<String, Object>) status.getData();
        JSONArray ioStatus = (JSONArray) data.get("nodeio");
        System.out.println("io的状态码"+ioStatus);
        //调用返回io数量的命令
        Result num = mqttProcess.MQTTProcess(MQTTNormalDTO.builder().nodeId(nodeId).build(), "#", 32);
        HashMap<String, Object> data1= (HashMap<String, Object>) num.getData();
        JSONArray ioNum = (JSONArray) data1.get("ionum");
        System.out.println("io的数量"+ioNum);
        Map<String,Character> result=new HashMap();
        //分两步，i=0的时候表示 i口，i=1的时候，表示o口
        for (int i = 0; i < 2; i++) {
            // 取出状态，例如[5,10],要转成二进制
            String binaryStatus = Integer.toBinaryString(ioStatus.get(i,Integer.class));
            System.out.println("i/o的二进制状态"+binaryStatus);
            //如果转成二进制后，他的数量小于预定的数量那就往前填0
            Integer ioNumbers = ioNum.get(i, Integer.class);
            while (binaryStatus.length()<ioNumbers){
                binaryStatus= "0"+binaryStatus;
            }
            //最后我们要返回的信息肯定是i1,i2,o1,o2这类的
            String role= i==0 ? "i":"o";
            char[] chars = binaryStatus.toCharArray();
            for (int j = ioNumbers-1; j >=0  ; j--) {
                result.put(role+(ioNumbers-j),chars[j]);
            }
        }
        return Result.success(result);
    }

    /**
     * 设置某个i/o口的状态
     * @param ioInfo
     * @return
     */
    @PostMapping ("/io/set")
    public Result setIO(@RequestBody IoInfo ioInfo){
        ioService.update().eq("node_id",ioInfo.getNodeId()).eq("ox",ioInfo.getOx()).set("status",ioInfo.getStatus()==true?1:0 ).update();
        checkNormalWrong.checkWrong(ioInfo);
        MQTTIoInfo mqttIoInfo = MQTTIoInfo.toMQTT(ioInfo);
        return mqttProcess.MQTTProcess(mqttIoInfo,"#",27);
    }


//    @PostMapping ("/io/set/test")
//    public Result getIOInfoTest(@RequestBody IoInfo ioInfo){
//        checkNormalWrong.checkWrong(ioInfo);
//        MQTTIoInfo mqttIoInfo = MQTTIoInfo.toMQTT(ioInfo);
//        return mqttProcess.MQTTProcess(mqttIoInfo,"#",27);
//    }

    /**
     * 开某个节点上的led灯
     * @param nodeId
     * @return
     */
    @GetMapping("/led/open")
    public Result openLED(@RequestParam("nodeId") Integer nodeId){
        checkNormalWrong.checkId(nodeId,"节点id错误");
        ioService.update().eq("node_id",nodeId).eq("type",1).set("status",1 ).update();
        return mqttProcess.MQTTProcess(MQTTNormalDTO.builder().nodeId(nodeId).build(),"#",29);
    }

    /**
     * 关某个节点上的led灯
     * @param nodeId
     * @return
     */
    @GetMapping("/led/close")
    public Result closeLED(@RequestParam("nodeId") Integer nodeId){
        checkNormalWrong.checkId(nodeId,"节点id错误");
        ioService.update().eq("node_id",nodeId).eq("type",1).set("status",0 ).update();
        return mqttProcess.MQTTProcess(MQTTNormalDTO.builder().nodeId(nodeId).build(),"#",30);
    }

    /**
     * 得到某个节点上led灯上的状态
     * @param nodeId
     * @return
     */
    @GetMapping("/led/info")
    public Result getLEDInfo(@RequestParam("nodeId") Integer nodeId){
        checkNormalWrong.checkId(nodeId,"节点id错误");
        return mqttProcess.MQTTProcess(MQTTNormalDTO.builder().nodeId(nodeId).build(),"#",31);
    }
    /**
     * 返回最新的一条记录,跟硬件联调
     */
    @PostMapping("/sensordata")
    public Result searchLatestData(@RequestBody LatestDataDTO latestDataDTO){
        checkNormalWrong.checkWrong(latestDataDTO);
        return sensorService.searchLatestData(latestDataDTO);
    }

    /**
     * 向硬件请求数据,测试命令
     */
    @GetMapping("/module/data")
    public Result searchSensorData(){
        return mqttProcess.MQTTProcess(MQTTModuleSensorData.builder().scmd(new Integer[]{2,4,0,0}).build(),"#",24);
    }







}
