package com.xiancai.lora.service;

import com.xiancai.lora.model.DTO.BindDTO;
import com.xiancai.lora.model.DTO.MoveNode;
import com.xiancai.lora.model.DTO.SearchNode;
import com.xiancai.lora.model.DTO.SetNodeGPS;
import com.xiancai.lora.model.entity.Node;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiancai.lora.utils.Result;

import java.util.Map;

/**
* @author 86156
* @description 针对表【node】的数据库操作Service
* @createDate 2022-11-10 21:23:05
*/

public interface NodeService extends IService<Node> {
    void bindUpdate(BindDTO bindDTO);

    Result getNodeNumber();

    Result getOfflineNodeNumber();

    Result searchNodeGroup(String ids);

    Result searchNodeDetail(Integer nodeId, Integer userId);

    Result renameNode(Integer nodeId, String nodeName,Integer userId);

    Result modifyCycle(Integer reportCycle, Integer nodeId, Integer userId);

    Result searchNodeArgs(SearchNode searchNode);

    Result searchNodeBehindNode(Integer loraId, Integer userId);

    Result searchNodeList(Integer userId);

    Result moveNode(MoveNode moveNode);

    Result getNodeGps(Integer nodeId, Integer userId);
    Result setNodeGPS(SetNodeGPS setNodeGPS);

    Result getLowPowerNodeNum();

    Result nodeInit(Map<String, Object> map);


    Result setDataUploadInterval(Map<String, Object> data);

    Result restart(Map<String, Object> data);

    Result init(Map<String, Object> data);

    Result getLocation(Map<String, Object> data);


}
