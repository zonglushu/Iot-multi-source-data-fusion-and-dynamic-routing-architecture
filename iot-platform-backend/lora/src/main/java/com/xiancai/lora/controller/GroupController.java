package com.xiancai.lora.controller;


import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.model.DTO.RenameGroupDTO;
import com.xiancai.lora.model.entity.FirstLoraMove;
import com.xiancai.lora.service.GroupService;
import com.xiancai.lora.utils.Result;
import com.xiancai.lora.utils.wrong.check.CheckNormalWrong;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.xiancai.lora.enums.StatusCode.PARAMS_ERR;

/**
 * 群组控制器
 */
@RestController
@RequestMapping("/group")
public class GroupController {
    @Resource
    private GroupService groupService;
    @Resource
    private CheckNormalWrong checkNormalWrong;

    @GetMapping("/add")
    public Result addGroupByName(@RequestParam("groupName") String groupName){
        checkNormalWrong.checkString(groupName,"前端传入的群组名错误");
        return groupService.addGroupByName(groupName);
    }

    @PostMapping("/rename")
    public Result renameGroup(@RequestBody RenameGroupDTO renameGroupDTO){
        checkNormalWrong.checkWrong(renameGroupDTO);
        return groupService.renameGroup(renameGroupDTO);
    }
    @GetMapping("/delete")
    public Result deleteGroupById(@RequestParam("groupId") Integer groupId ){
        checkNormalWrong.checkId(groupId);
        return groupService.deleteGroupById(groupId);
    }

    @GetMapping("/grouplist")
    public Result searchGroupList(){
        return groupService.searchGroupList();
    }

    @GetMapping("/search")
    public Result searchGroup(@RequestParam("groupName") String groupName ){
        checkNormalWrong.checkString(groupName,"群组名称为空或不存在");
        return groupService.searchGroup(groupName);
    }

    @PostMapping("/move")
    public Result moveFirstLora(@RequestBody FirstLoraMove firstLoraMove){
        checkNormalWrong.checkWrong(firstLoraMove);
        return groupService.moveFirstLora(firstLoraMove);
    }



}
