package com.xiancai.lora.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiancai.lora.enums.StatusCode;
import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.model.VO.Balance;
import com.xiancai.lora.model.VO.Recharge;
import com.xiancai.lora.model.VO.Spend;
import com.xiancai.lora.model.entity.Cost;
import com.xiancai.lora.service.CostService;
import com.xiancai.lora.mapper.CostMapper;
import com.xiancai.lora.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
* @author 86156
* @description 针对表【cost】的数据库操作Service实现
* @createDate 2023-01-04 18:21:54
*/
@Service
public class CostServiceImpl extends ServiceImpl<CostMapper, Cost>
    implements CostService{

    @Autowired
    private CostMapper costMapper;

    @Override
    public Result search(String[] costTime,Integer userId) {
//        QueryWrapper<Cost> between = new QueryWrapper<Cost>().eq("user_id", userId).
//                between("cost_time", costTime[0], costTime[1]);
//        List<Cost> costList = list(between);

        String start = costTime[0];
        String end = costTime[1];
        QueryWrapper<Cost> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId).last("cost_time between"+start+"and"+end);
        List<Cost> costList = costMapper.searchByTime(start, end, userId);
//        List<Cost> costList = list(wrapper);
        if(costList.isEmpty()||costList==null){
            return Result.success(Collections.emptyList());
        }
        return Result.success(costList);

    }

    @Override
    public Result searchBalance(Integer userId) {
        QueryWrapper<Cost> wrapper = new QueryWrapper<>();
        wrapper.select("balance").eq("user_id", userId).orderByDesc("cost_time").last("LIMIT 1");
        Cost entity = getOne(wrapper);
        if(entity==null){
            throw new BusinessException(StatusCode.NULL_ERR.getMessage(),StatusCode.NULL_ERR.getCode(),"数据库查询为空");
        }
        System.out.println(entity);
        Balance balance = Balance.builder().balance(entity.getBalance()).build();
        return Result.success(balance);
    }

    @Override
    public Result searchRecharge(Integer userId) {
        Cost cost = searchSomething(userId, "充值");
        Recharge build = Recharge.builder().recharge(cost.getRecharge()).build();
        return Result.success(build);
    }

    @Override
    public Result searchSpend(Integer userId) {
        Cost cost = searchSomething(userId, "消费");
        Spend build = Spend.builder().spend(cost.getSpend()).build();
        return Result.success(build);
    }

    private Cost searchSomething(Integer userId,String mode){
        QueryWrapper<Cost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("mode", mode)
                .select("sum(amount) as recharge ");
        Cost entity = this.getOne(queryWrapper);
//        Cost entity = query().eq("user_id", userId).eq("mode", mode)
//                .select("sum(account) as recharge ").getEntity();
        if(entity==null){
            throw new BusinessException(StatusCode.NULL_ERR.getMessage(),StatusCode.NULL_ERR.getCode(),"数据库查询为空");
        }

        return entity;
    }
}




