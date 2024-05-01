package com.xiancai.lora.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.model.DTO.GroupMoveDTO;
import com.xiancai.lora.model.DTO.SearchLora;
import com.xiancai.lora.model.VO.LoraNumber;
import com.xiancai.lora.model.VO.OfflineLoraNumber;
import com.xiancai.lora.model.VO.tree.LoraTree;
import com.xiancai.lora.model.VO.tree.ModuleNode;
import com.xiancai.lora.model.VO.tree.NodeTree;
import com.xiancai.lora.model.entity.*;
import com.xiancai.lora.service.*;
import com.xiancai.lora.mapper.LoraMapper;
import com.xiancai.lora.utils.Result;
import com.xiancai.lora.utils.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.xiancai.lora.enums.StatusCode.*;

/**
 * @author 86156
 * @description 针对表【lora】的数据库操作Service实现
 * @createDate 2022-11-30 11:07:36
 */
@Service
public class LoraServiceImpl extends ServiceImpl<LoraMapper, Lora>
        implements LoraService {

    @Autowired
    private GroupService groupService;

    @Autowired
    private NodeService nodeService;

    @Resource
    private ModuleService moduleService;

    @Override
    public Result<LoraNumber> getLoraNumber() {
        Integer id = UserHolder.getUser().getId();
        long loraNumber = this.count(new QueryWrapper<Lora>().eq("user_id", id));
        return Result.success(LoraNumber.builder().loraNumber(loraNumber).build());
    }

    @Override
    public Result<OfflineLoraNumber> getOfflineLoraNumber() {
        Integer id = UserHolder.getUser().getId();
        long loraOfflineNumber = this.count(new QueryWrapper<Lora>().eq("user_id", id).eq("online_status", "离线"));
        return Result.success(OfflineLoraNumber.builder().offlineLoraNumber(loraOfflineNumber).build());
    }

    @Override
    public Result getLoraList() {
        Integer userId = UserHolder.getUser().getId();
        List<Lora> loraList = this.query().eq("user_id", userId).list();
        if(loraList==null){
            return Result.success(Collections.emptyList());
        }
        return Result.success(loraList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result moveLoraFromGroup(GroupMoveDTO groupMoveDTO) {
        Integer loraId = groupMoveDTO.getLoraId();
        Integer oldGroupId = groupMoveDTO.getOldGroupId();
        Integer newGroupId = groupMoveDTO.getNewGroupId();

        // TODO 如果不再验证，如果有人恶意请求，数据库怎么版
        //  如果要验证，性能怎么办   为啥要先查询，是因为怕这个条件不符合，然后又去遍历数据库了，是这样才要先查出来
        boolean isUpdate = this.update().eq("id", loraId).eq("cohort_id", oldGroupId).set("cohort_id", newGroupId).update();
        isDataBaseSafety(isUpdate);
        //对应的群组的device_num也应该减少1
        boolean isOld = groupService.update().eq("id", oldGroupId).setSql("device_num=device_num-1").update();
        isDataBaseSafety(isOld);
        boolean isNew = groupService.update().eq("id", newGroupId).setSql("device_num=device_num+1").update();
        isDataBaseSafety(isNew);
        return Result.success(true);
    }

    /**
     * 模糊查询
     *
     * @param searchLora
     * @return
     */
    @Override
    public Result searchLora(SearchLora searchLora) {
        //update_time,online_status在lora
        //ids,bind_time在node
        String ids = searchLora.getIds();
        String[] bindTime = searchLora.getBindTime();
        String onlineStatus = searchLora.getOnlineStatus();
        String[] updateTime = searchLora.getUpdateTime();
        List<Lora> loraList =new ArrayList<>();
        if ((bindTime != null && bindTime.length == 2) || StringUtils.isNotBlank(ids)) {
            List<Node> list = nodeService.query().between("bind_time", bindTime[0], bindTime[1])
                    .eq(StrUtil.isNotBlank(ids), "ids", ids)
                    .list();
            List<Integer> collect = list.stream().map(Node::getIsLora).collect(Collectors.toList());
            loraList =collect.stream().map(id ->
                    this.getById(id)
            ).collect(Collectors.toList());
        } else {
            if(updateTime.length == 2){
                loraList = this.query()
                        .between(updateTime.length == 2, "update_time", updateTime[0], updateTime[1])
                        .eq(StrUtil.isNotBlank(onlineStatus),"online_status",onlineStatus)
                        .list();
            }else {
                loraList=this.query().eq(StrUtil.isNotBlank(onlineStatus),"online_status",onlineStatus).list();
            }

        }
        return process(loraList);
    }

    @Override
    public Result searchGroupLora(Integer groupId) {
        Integer userId = UserHolder.getUser().getId();
        if (groupId <= 0) {
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "传入的群组id小于等于0");
        }
        List<Lora> list = this.list(new QueryWrapper<Lora>().eq("user_id", userId).eq("cohort_id", groupId));
        return process(list);
    }

    @Override
    public Result getAllDevice(Integer userId) {
        List<Group> group = groupService.list(new QueryWrapper<Group>().eq("user_id", userId));
        // 供查询 lora用的群组id
        Tree tree = new Tree();
        for (Group group1 : group) {
            List<Lora> lora = this.list(new QueryWrapper<Lora>().eq("cohort_id", group1.getId()));
            List<LoraTree> loraTreeList = new ArrayList<>();
            for (int i = 0; i < lora.size(); i++) {
                Lora lora1 = lora.get(i);
                LoraTree loraTree = BeanUtil.copyProperties(lora1, LoraTree.class);
                loraTree.setLoraId(lora1.getId());
                loraTreeList.add(loraTree);
                List<Node> nodes = nodeService.list(new QueryWrapper<Node>().eq("lora_id", lora1.getId()));
                List<Node> isLoraNodes = nodes.stream().filter(node1 -> node1.getIsLora() > 0).collect(Collectors.toList());
                List<TreeNode> isLoraTreeNodeList = getIsLoraTreeNodeList(isLoraNodes);
                List<Node> noLoraNodes = nodes.stream().filter(node1 -> node1.getIsLora() == 0).collect(Collectors.toList());
                List<TreeNode> noLoraTreeNodeList = getNoLoraTreeNodeList(noLoraNodes);
                loraTree.addAll(isLoraTreeNodeList);
                loraTree.addAll(noLoraTreeNodeList);
                if (isLoraNodes.size() != 0) {
                    loraFor(isLoraNodes, isLoraTreeNodeList);
                }
                nodeFor(noLoraNodes, noLoraTreeNodeList);
            }
            List<TreeNode> treeNodes = new ArrayList<>(loraTreeList);
            tree.add(treeNodes);
        }
        // 是 群组直接关联的lora 在关系层级中为1
        return Result.success(tree);
    }


    private void loraFor(List<Node> isLoraNodes, List<TreeNode> treeNodeList) {
        for (int i = 0; i < isLoraNodes.size(); i++) {
            Node isLoraNode = isLoraNodes.get(i);
            List<Node> nodes = nodeService.list(new QueryWrapper<Node>().eq("lora_id", isLoraNode.getIsLora()));
            List<Node> isLoraTwoNodes = nodes.stream().filter(node1 -> node1.getIsLora() > 0).collect(Collectors.toList());
            List<TreeNode> isLoraTreeNodeList = getIsLoraTreeNodeList(isLoraTwoNodes);
            List<Node> noLoraNodes = nodes.stream().filter(node1 -> node1.getIsLora() == 0).collect(Collectors.toList());
            List<TreeNode> noLoraTreeNodeList = getNoLoraTreeNodeList(noLoraNodes);
            treeNodeList.get(i).addAll(isLoraTreeNodeList);
            treeNodeList.get(i).addAll(noLoraTreeNodeList);
            nodeFor(noLoraNodes, noLoraTreeNodeList);
            if (isLoraTwoNodes.size() != 0) {
                loraFor(isLoraTwoNodes, isLoraTreeNodeList);
            } else {
                return;
            }
        }
    }

    private void nodeFor(List<Node> noLoraNodes, List<TreeNode> treeNodeList) {
        for (int i = 0; i < noLoraNodes.size(); i++) {
            Node noLoraNode = noLoraNodes.get(i);
            NodeTree nodeTree = BeanUtil.copyProperties(noLoraNode, NodeTree.class);
            nodeTree.setNodeId(noLoraNode.getId());
            List<ModuleNode> moduleNodeList = moduleService.list(new QueryWrapper<Module>()
                            .eq("node_id", noLoraNode.getId()).eq("module_type", "sensor"))
                    .stream().map(module -> BeanUtil.copyProperties(module, ModuleNode.class))
                    .collect(Collectors.toList());
            List<TreeNode> treeNodes = new ArrayList<>();
            treeNodes.addAll(moduleNodeList);
            treeNodeList.get(i).addAll(treeNodes);
        }


    }

    private List<TreeNode> getIsLoraTreeNodeList(List<Node> isLoraNodes) {
        return isLoraNodes.stream().map(node -> {
                    NodeTree nodeTree = BeanUtil.copyProperties(node, NodeTree.class);
                    nodeTree.setNodeId(node.getId());
                    return nodeTree;
                })
                .collect(Collectors.toList());
    }

    private List<TreeNode> getNoLoraTreeNodeList(List<Node> noLoraNodes) {
        return noLoraNodes.stream().map(node -> {
                    NodeTree nodeTree = BeanUtil.copyProperties(node, NodeTree.class);
                    nodeTree.setNodeId(node.getId());
                    return nodeTree;
                })
                .collect(Collectors.toList());
    }

    private Result process(List<Lora> list) {
        if (list.size() == 0 || list == null) {
            throw new BusinessException(FAIL.getMessage(), FAIL.getCode(), "数据库未查到");
        }
        return Result.success(list);

    }

    private void isDataBaseSafety(boolean isSafety) {
        if (!isSafety) {
            throw new BusinessException(SYSTEM_ERR.getMessage(), SYSTEM_ERR.getCode(), "数据库更新数据失败");
        }
    }
}





