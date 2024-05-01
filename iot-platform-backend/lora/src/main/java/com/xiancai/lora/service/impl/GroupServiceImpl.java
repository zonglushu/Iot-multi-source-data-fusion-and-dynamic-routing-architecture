package com.xiancai.lora.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.model.DTO.RenameGroupDTO;
import com.xiancai.lora.model.entity.FirstLoraMove;
import com.xiancai.lora.model.entity.Group;
import com.xiancai.lora.service.LoraService;
import com.xiancai.lora.service.GroupService;
import com.xiancai.lora.mapper.SetMapper;
import com.xiancai.lora.utils.Result;
import com.xiancai.lora.utils.UserHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

import static com.xiancai.lora.enums.StatusCode.PARAMS_ERR;
import static com.xiancai.lora.enums.StatusCode.SYSTEM_ERR;

/**
* @author 86156
* @description 针对表【set】的数据库操作Service实现
* @createDate 2022-12-01 19:11:54
*/
@Service
public class GroupServiceImpl extends ServiceImpl<SetMapper, Group>
    implements GroupService {

// Autowired 可以让springboot选择在合适的时机注入，resource会有依赖注入的问题

    @Autowired
    private LoraService loraService;
    @Override
    public Result addGroupByName(String groupName) {
        // 这不用再判断 id异常情况了，因为有拦截器，异常情况进不来
        Integer id = UserHolder.getUser().getId();
        Group group = Group.builder().groupName(groupName)
                .user_id(id).build();
        boolean isSave = this.save(group);
        isDataBaseSafety(isSave);
        return Result.success(isSave);
    }

    @Override
    public Result renameGroup(RenameGroupDTO renameGroupDTO) {
        int groupId = renameGroupDTO.getGroupId();
        String groupName = renameGroupDTO.getGroupName();
        Group group = this.getById(groupId);
        if(group ==null){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "该群组id不存在");
        }
        group.setGroupName(groupName);
        boolean isUpdate = this.updateById(group);
        isDataBaseSafety(isUpdate);
        return Result.success(isUpdate);
    }

    @Transactional
    @Override
    public Result deleteGroupById(Integer groupId) {
        Group group = this.getById(groupId);
        if(group ==null){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "该群组id不存在");
        }
        boolean isRemove = this.removeById(group);
        isDataBaseSafety(isRemove);
        //但是又因为是两张表，所以要加事务
        //删除后，还要把有该set_id的lora的set_id置为 -1
        loraService.update().eq("cohort_id", groupId).set("cohort_id", -1).update();
        return Result.success(isRemove);
    }

    @Override
    public Result searchGroupList() {
        Integer userId = UserHolder.getUser().getId();
        List<Group> userGroups = this.list(new QueryWrapper<Group>().eq("user_id", userId));
        if(userGroups.size()==0){
            throw new BusinessException(SYSTEM_ERR.getMessage(), SYSTEM_ERR.getCode(), "未找到数据");
        }
        return Result.success(userGroups);
    }

    @Override
    public Result searchGroup(String groupName) {
        Group group = this.getOne(new QueryWrapper<Group>().eq("cohort_name", groupName));
        if(group==null){
            throw new BusinessException(SYSTEM_ERR.getMessage(),SYSTEM_ERR.getCode(),"未找到该群组，请检查群组名称");
        }
        return Result.success(group);

    }

    @Override
    public Result moveFirstLora(FirstLoraMove firstLoraMove) {
        Integer loraId = firstLoraMove.getLoraId();
        Integer groupId = firstLoraMove.getGroupId();
        boolean update = loraService.update().eq("id", loraId).set("cohort_id", groupId).update();
        if(!update){
            throw new BusinessException(SYSTEM_ERR.getMessage(),SYSTEM_ERR.getCode(),"移动失败");
        }
        return Result.success(update);
    }

    private void isDataBaseSafety(boolean isSafety){
        if(!isSafety){
            throw new BusinessException(SYSTEM_ERR.getMessage(), SYSTEM_ERR.getCode(), "数据库更新数据失败");
        }
    }
}




