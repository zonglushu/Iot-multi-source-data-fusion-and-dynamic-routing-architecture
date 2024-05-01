package com.xiancai.lora.controller;

import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.model.DTO.GroupMoveDTO;
import com.xiancai.lora.model.DTO.SearchLora;
import com.xiancai.lora.service.LoraService;
import com.xiancai.lora.utils.Result;
import com.xiancai.lora.utils.wrong.check.CheckNormalWrong;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.xiancai.lora.enums.StatusCode.PARAMS_ERR;

/**
 * 网关控制器
 */
@RestController
@RequestMapping("/lora")
public class LoraController {
    @Resource
    private LoraService loraService;

    @Resource
    private CheckNormalWrong checkNormalWrong;

    /**
     * 查询网关数目
     * @return
     */
    @GetMapping("/loranum")
    public Result getLoraNumber(){
        return loraService.getLoraNumber();
    }

    @GetMapping("/loraoffline")
    public Result getOfflineLora(){
        return loraService.getOfflineLoraNumber();
    }

    @GetMapping("/loralist")
    public Result getLoraList(){
        return loraService.getLoraList();
    }

    @PostMapping("/move")
    public Result moveLoraFromGroup(@RequestBody GroupMoveDTO groupMoveDTO){
        //不改
        checkNormalWrong.checkWrong(groupMoveDTO);
        return loraService.moveLoraFromGroup(groupMoveDTO);
    }
    @PostMapping("/search")
    public Result searchLora(@RequestBody SearchLora searchLora){
        //按给的参数查，给那个查那个，所以不用在这判断
        return loraService.searchLora(searchLora);
    }
    @GetMapping("/grouplora")
    public Result searchGroupLora(@RequestParam("groupId") Integer groupId){
        checkNormalWrong.checkId(groupId);
        return loraService.searchGroupLora(groupId);
    }

}
