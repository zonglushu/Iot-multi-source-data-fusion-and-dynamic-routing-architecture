package com.xiancai.lora.controller;

import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.service.CostService;
import com.xiancai.lora.utils.Result;
import com.xiancai.lora.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.xiancai.lora.enums.StatusCode.PARAMS_ERR;

@RestController
@RequestMapping("/cost")
public class CostController {

    @Autowired
    private CostService costService;

    /**
     * 查询用户在某一段时间的计费
     * @param costTime
     * @return
     */
    @GetMapping("/search")
    public Result search(@RequestParam("costTime") String[] costTime){
        if(costTime==null||costTime.length!=2){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "传入消费时间的数目不足两个");
        }
        Integer userId = UserHolder.getUser().getId();
        return costService.search(costTime,userId);
    }

    /**
     * 查询用户最新的余额
     * @return
     */
    @GetMapping("/balance")
    public Result searchBalance(){
        Integer userId = UserHolder.getUser().getId();
        return costService.searchBalance(userId);
    }

    /**
     * 查询用户的充值总额
     * @return
     */
    @GetMapping("/recharge")
    public Result searchRecharge(){
        Integer userId = UserHolder.getUser().getId();
        return costService.searchRecharge(userId);
    }
    @GetMapping("/spend")
    public Result searchSpend(){
        Integer userId = UserHolder.getUser().getId();
        return costService.searchSpend(userId);
    }
}
