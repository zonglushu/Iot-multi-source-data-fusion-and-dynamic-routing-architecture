package com.xiancai.lora.MQTT.service.proxy;


import com.xiancai.lora.model.VO.mqtt.LEDStatus;
import com.xiancai.lora.model.entity.Node;
import com.xiancai.lora.service.NodeService;
import com.xiancai.lora.utils.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Map;


@Service
public class ProxyNodeService  {


    @Resource
    private NodeService nodeService;


    public Result init(Map<String,Object> data){
        return nodeService.init(data);
    }

    public Node getById(Serializable id){
        return nodeService.getById(id);
    }

    public Result setDataUploadInterval(Map<String,Object> data){
        return nodeService.setDataUploadInterval(data);
    }

    public Result restart(Map<String,Object> data){
        return nodeService.restart(data);
    }

    public Result getLocation(Map<String,Object> data){
        return nodeService.getLocation(data);
    }


    /**
     * 对应命令 node_io_get
     * @param data
     * @return
     */
    public Result ioGet(Map<String,Object> data){

        return Result.success(data);
    }

    /**
     * 对应命令 node_get_io_num
     * @param data
     * @return
     */
    public Result getIoNum(Map<String,Object> data){
        return Result.success(data);
    }

    /**
     * 对应命令 node_led_info
     * @param data
     * @return
     */
    public Result ledInfo(Map<String,Object> data){
        return Result.success(LEDStatus.builder().status((int)data.get("status")==1?"开":"关").build());
    }



}
