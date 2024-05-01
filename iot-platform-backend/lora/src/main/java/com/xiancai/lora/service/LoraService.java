package com.xiancai.lora.service;

import com.xiancai.lora.model.DTO.GroupMoveDTO;
import com.xiancai.lora.model.DTO.SearchLora;
import com.xiancai.lora.model.entity.Lora;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiancai.lora.utils.Result;

/**
* @author 86156
* @description 针对表【lora】的数据库操作Service
* @createDate 2022-11-30 11:07:36
*/
public interface LoraService extends IService<Lora> {

    Result getLoraNumber();

    Result getOfflineLoraNumber();

    Result getLoraList();

    Result moveLoraFromGroup(GroupMoveDTO groupMoveDTO);

    Result searchLora(SearchLora searchLora);

    Result searchGroupLora(Integer groupId);

    Result getAllDevice(Integer userId);
}
