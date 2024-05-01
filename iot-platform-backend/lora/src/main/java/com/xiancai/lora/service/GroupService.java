package com.xiancai.lora.service;

import com.xiancai.lora.model.DTO.RenameGroupDTO;
import com.xiancai.lora.model.entity.FirstLoraMove;
import com.xiancai.lora.model.entity.Group;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiancai.lora.utils.Result;

/**
* @author 86156
* @description 针对表【set】的数据库操作Service
* @createDate 2022-12-01 19:11:54
*/
public interface GroupService extends IService<Group> {

    Result addGroupByName(String groupName);

    Result renameGroup(RenameGroupDTO renameGroupDTO);

    Result deleteGroupById(Integer groupId);

    Result searchGroupList();

    Result searchGroup(String groupName);

    Result moveFirstLora(FirstLoraMove firstLoraMove);
}
