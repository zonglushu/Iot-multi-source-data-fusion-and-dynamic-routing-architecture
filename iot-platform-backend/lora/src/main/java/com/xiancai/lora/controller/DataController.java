package com.xiancai.lora.controller;

import com.xiancai.lora.enums.StatusCode;
import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.service.DataService;
import com.xiancai.lora.utils.Result;
import com.xiancai.lora.utils.UserHolder;
import com.xiancai.lora.utils.wrong.check.CheckNormalWrong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.xiancai.lora.enums.StatusCode.NULL_ERR;

@RestController
@RequestMapping("/data")
public class DataController {
    @Autowired
    private DataService dataService;

    @Resource
    private CheckNormalWrong checkNormalWrong;


    /**
     * 数据搜索，根据 nodeId和 moduleType 查询该节点下所有的传感器的数据
     * @param nodeId
     * @param moduleType
     * @return
     */
    @GetMapping("/search/list")
    public Result searchDataToList(@RequestParam("nodeId")Integer nodeId,
                                   @RequestParam("moduleType")String moduleType){
        checkNormalWrong.checkString(moduleType,"moduleType为空");
        checkNormalWrong.checkId(nodeId,"nodeId错误");
        return dataService.searchDataToList(nodeId,moduleType);
    }

    @GetMapping("/datatype")
    public Result searchDataType(@RequestParam("nodeid") Integer nodeId,
                                 @RequestParam("port") Integer port){
        checkNormalWrong.checkId(nodeId,"传入的nodeId错误");
        checkNormalWrong.checkInteger(port,"传入的port错误");
        Integer userId = UserHolder.getUser().getId();
        return dataService.searchDataType(nodeId,port,userId);
    }
}
