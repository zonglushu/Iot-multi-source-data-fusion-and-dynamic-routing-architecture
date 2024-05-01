package com.xiancai.lora.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.model.DTO.BindDTO;
import com.xiancai.lora.model.DTO.MoveNode;
import com.xiancai.lora.model.DTO.SearchNode;
import com.xiancai.lora.model.DTO.SetNodeGPS;
import com.xiancai.lora.model.VO.*;
import com.xiancai.lora.model.entity.Group;
import com.xiancai.lora.model.entity.Lora;
import com.xiancai.lora.model.entity.Module;
import com.xiancai.lora.model.entity.Node;
import com.xiancai.lora.service.GroupService;
import com.xiancai.lora.service.LoraService;
import com.xiancai.lora.service.NodeService;
import com.xiancai.lora.mapper.NodeMapper;
import com.xiancai.lora.utils.Result;
import com.xiancai.lora.utils.UserHolder;
import com.xiancai.lora.utils.convert.Converter;
import com.xiancai.lora.utils.wrong.check.CheckNormalWrong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.xiancai.lora.enums.StatusCode.*;

/**
 * @author 86156
 * @description 针对表【node】的数据库操作Service实现
 * @createDate 2022-11-10 21:23:05
 */
@Service
public class NodeServiceImpl extends ServiceImpl<NodeMapper, Node>
        implements NodeService {

    @Autowired
    private LoraService loraService;

    @Autowired
    private GroupService groupService;

    @Resource
    private CheckNormalWrong checkNormalWrong;

    @Override
    public void bindUpdate(BindDTO bindDTO) {
        //获得操作bind的用户
        Integer userId = UserHolder.getUser().getId();
        //生成node的信息
        Node build = Node.builder().nodeName(bindDTO.getDeviceName())
                .ids(bindDTO.getIds()).latitude(bindDTO.getLatitude())
                .longitude(bindDTO.getLongitude()).onlineStatus("在线")
                .userId(userId).bindTime(new Date()).build();
        //TODO 硬件传过来硬件的信息

        boolean save = this.save(build);
        if (save == false) {
            throw new BusinessException(SYSTEM_ERR.getMessage(), SYSTEM_ERR.getCode(), "数据库插入数据失败");
        }
    }

    @Override
    public Result<NodeNumber> getNodeNumber() {
        Integer id = UserHolder.getUser().getId();
        long nodeNumber = this.count(new QueryWrapper<Node>().eq("user_id", id));
        return Result.success(NodeNumber.builder().nodeNumber(nodeNumber).build());
    }

    @Override
    public Result getOfflineNodeNumber() {
        Integer id = UserHolder.getUser().getId();
        long offlineNodeNumber = this.count(new QueryWrapper<Node>().eq("user_id", id).eq("online_status", "离线"));
        return Result.success(OfflineNodeNumber.builder().offlineNodeNumber(offlineNodeNumber).build());
    }

    @Override
    public Result searchNodeGroup(String ids) {
        Node node = this.getOne(new QueryWrapper<Node>().eq("ids", ids));
        if (node == null) {
            throw new BusinessException(SYSTEM_ERR.getMessage(), SYSTEM_ERR.getCode(), "未查到该传感器的数据");
        }

        Lora topLora = getLora(node);
        if(topLora==null){
            throw new BusinessException(SYSTEM_ERR.getMessage(), SYSTEM_ERR.getCode(), "未查到该节点的群组，请检查ids");
        }
        Group group = groupService.getById(topLora.getGroupId());
        return Result.success(group);

    }

    @Override
    public Result searchNodeDetail(Integer nodeId, Integer userId) {
        Node node = getById(nodeId);
        checkNormalWrong.isNULL(node,"未查到该节点，请检查群组id");
        checkNormalWrong.checkUser(node.getUserId(),userId);
        NodeVo nodeVo = transNodeVo(node);
        return Result.success(nodeVo);
    }


    @Override
    public Result renameNode(Integer nodeId, String nodeName,Integer userId) {
        //找到结点id
        Node node = getById(nodeId);
        if(!node.getUserId().equals(userId)){
            throw new BusinessException(FAIL.getMessage(), FAIL.getCode(), "该节点是他人的，您无法操作");
        }
        boolean isUpdate = update().set("node_name", nodeName).eq("id",nodeId).update();
        if(!isUpdate){
            throw new BusinessException(FAIL.getMessage(), FAIL.getCode(), "数据库更新失败");
        }
        return Result.success(isUpdate);
    }

    @Override
    public Result modifyCycle(Integer reportCycle, Integer nodeId, Integer userId) {
        Node node = getById(nodeId);
        if(!node.getUserId().equals(userId)){
            throw new BusinessException(FAIL.getMessage(), FAIL.getCode(), "该节点是他人的，您无法操作");
        }
        boolean isUpdate = update().set("report_cycle", reportCycle).update();
        if(!isUpdate){
            throw new BusinessException(FAIL.getMessage(), FAIL.getCode(), "数据库更新失败");
        }
        return Result.success(isUpdate);
    }

    @Override
    public Result searchNodeArgs(SearchNode searchNode) {
        Integer userId = UserHolder.getUser().getId();
        String ids = searchNode.getIds();
        String[] bindTime = searchNode.getBindTime();
        String onlineStatus = searchNode.getOnlineStatus();
        QueryWrapper<Node> nodeQW = new QueryWrapper<Node>().eq("user_id", userId).eq(StrUtil.isNotBlank(ids), "ids", ids).eq(StrUtil.isNotBlank(onlineStatus), "online_status", onlineStatus);

        if(bindTime.length==2){
            nodeQW.between("bind_time", bindTime[0], bindTime[1]);
        }
        List<Node> list = this.list(nodeQW);
//        List<Node> list = query().eq("user_id", userId).eq(StrUtil.isNotBlank(ids),"ids", ids).between(bindTime.length==2,"bind_time", bindTime[0], bindTime[1]).eq(StrUtil.isNotBlank(onlineStatus),"online_status",onlineStatus).list();
        if(list.isEmpty()||list==null){
            return Result.success(Collections.emptyList());
        }
        return Result.success(list);
    }

    @Override
    public Result searchNodeBehindNode(Integer loraId, Integer userId) {

        Node node = query().eq("is_lora",loraId).one();;
        if(!node.getUserId().equals(userId)){
            throw new BusinessException(FAIL.getMessage(), FAIL.getCode(), "该节点是他人的，您无法操作");
        }
        List<Node> list = query().eq("lora_id", loraId).eq("user_id", userId).list();
        if(list.isEmpty()||list==null){
            return Result.success(Collections.emptyList());
        }
        Node loraNode = this.query().eq("is_lora", loraId).one();
        list.add(loraNode);
        List<NodeVo> collect = list.stream().map(node1 -> {
            return transNodeVo(node1);
        }).collect(Collectors.toList());

        return Result.success(collect);
    }

    @Override
    public Result searchNodeList(Integer userId) {
        List<Node> list = query().eq("user_id", userId).list();
        if(list.isEmpty()||list==null){
            return Result.success(Collections.emptyList());
        }
        List<NodeVo> collect = list.stream().map(node -> {
            return transNodeVo(node);
        }).collect(Collectors.toList());
        return Result.success(collect);
    }

    @Override
    public Result moveNode(MoveNode moveNode) {
        Integer userId = UserHolder.getUser().getId();
        Integer moveNodeId = moveNode.getMoveNodeId();
        if(moveNodeId==null||moveNodeId<0){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "moveNodeId错误");
        }
        Integer newNodeId = moveNode.getNewNodeId();
        if(newNodeId==null||newNodeId<0){
            throw new BusinessException(SYSTEM_ERR.getMessage(), SYSTEM_ERR.getCode(), "newNodeId错误");
        }
        //得到要移动的结点
        Node movedNode = getById(moveNodeId);
        if(!movedNode.getUserId().equals(userId)){
            throw new BusinessException(FAIL.getMessage(), FAIL.getCode(), "该节点是他人的，您无法移动");
        }
        //得到目标结点
        Node newNode = getById(newNodeId);
        if(!newNode.getUserId().equals(userId)){
            throw new BusinessException(FAIL.getMessage(), FAIL.getCode(), "该节点是他人的，您无法移动到这个节点");
        }
        if(newNode.getIsLora()<=0){
            throw new BusinessException(FAIL.getMessage(), FAIL.getCode(), "该节点不是网关，您无法移动到这个节点");
        }
        boolean isUpdate = update().eq("id", moveNodeId).set("lora_id", newNode.getIsLora()).update();
        if(!isUpdate){
            throw new BusinessException(FAIL.getMessage(), FAIL.getCode(), "数据库更新失败");
        }
        return Result.success(isUpdate);
    }

    @Override
    public Result getNodeGps(Integer nodeId, Integer userId) {
        Node node = getById(nodeId);
        if(!node.getUserId().equals(userId)){
            throw new BusinessException(FAIL.getMessage(), FAIL.getCode(), "该节点是他人的，您无法获取到这个节点");
        }
        NodeGPS nodeGPS = BeanUtil.copyProperties(node, NodeGPS.class);
        return Result.success(nodeGPS);
    }

    @Override
    public Result setNodeGPS(SetNodeGPS setNodeGPS) {
        Integer userId = UserHolder.getUser().getId();
        Integer nodeId = setNodeGPS.getNodeId();
        //纬度
        Float latitude = setNodeGPS.getLatitude();
        //经度
        Float longitude = setNodeGPS.getLongitude();
        if(nodeId==null||nodeId<=0){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "nodeId错误");
        }
        if(latitude==null||longitude==null){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "经纬度错误");
        }
        Node node = getById(nodeId);
        if(!node.getUserId().equals(userId)){
            throw new BusinessException(FAIL.getMessage(), FAIL.getCode(), "该节点是他人的，您无法获取这个节点");
        }
        node.setLatitude(latitude);
        node.setLongitude(longitude);
        boolean isUpdate = updateById(node);
        if(!isUpdate){
            throw new BusinessException(FAIL.getMessage(), FAIL.getCode(), "数据库更新失败");
        }
        return Result.success(isUpdate);
    }

    @Override
    public Result getLowPowerNodeNum() {
        long pow = count(new QueryWrapper<Node>().lt("battery", 20)
                .eq("user_id",UserHolder.getUser().getId()));
        return Result.success(LowPowerNodeNumVo.builder().lowPowerNodeNum(pow).build());
    }

    @Override
    public Result nodeInit(Map<String, Object> map) {
        return null;
    }

    @Override
    public Result setDataUploadInterval(Map<String, Object> data) {
        UpdateWrapper<Node> wrapper = getUpdateWrapper(data);
        wrapper.set("report_interval",data.get("report_interval"));
        boolean isUpdate = update(wrapper);
        checkNormalWrong.checkBool(isUpdate,"数据库设置上报周期异常");
        return Result.success(isUpdate);
    }

    @Override
    public Result restart(Map<String, Object> data) {
        return Result.success(true);
    }

    @Override
    public Result init(Map<String, Object> data) {
        return Result.success(true);
    }

    @Override
    public Result getLocation(Map<String, Object> data) {
        UpdateWrapper<Node> wrapper = getUpdateWrapper(data);
        List<BigDecimal> gps = Converter.castList(data.get("gps"), BigDecimal.class);
        wrapper.set("longitude",gps.get(0)).set("latitude",gps.get(1));
        boolean isUpdate = update(wrapper);
        checkNormalWrong.checkBool(isUpdate,"数据库设置经纬度失败");
        return Result.success(NodeGPS.builder().longitude( gps.get(0)).latitude(gps.get(1)).build());
    }




    private UpdateWrapper<Node> getUpdateWrapper(Map<String, Object> data){
        UpdateWrapper<Node> wrapper = new UpdateWrapper<>();
        wrapper.eq("ids",data.get("ids"));
        return wrapper;
    }

    //TODO
    private NodeVo transNodeVo(Node node){
        NodeVo nodeVo = BeanUtil.copyProperties(node, NodeVo.class);
        nodeVo.setBindUsername(UserHolder.getUser().getUsername());
        Node entity =getOne(new QueryWrapper<Node>().select("node_name").eq("is_lora", node.getLoraId()));
        if(entity==null){
            nodeVo.setParentLoraName("null");
            return nodeVo;
        }
        nodeVo.setParentLoraName(entity.getNodeName());
        return nodeVo;
    }
    private Lora getLora(Node node) {
        Node one = this.getOne(new QueryWrapper<Node>().eq("is_lora", node.getLoraId()));
        while (true){
            //找到这个节点对应的网关信息
            Lora lora = loraService.getById(one.getIsLora());
            if(lora.getGroupId() != 0){
                return lora;
            }
            one=this.getOne(new QueryWrapper<Node>().eq("is_lora", one.getLoraId()));
        }
    }

}
