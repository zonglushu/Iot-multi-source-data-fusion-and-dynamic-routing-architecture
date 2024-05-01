package com.xiancai.lora.service;

import com.xiancai.lora.model.entity.Cost;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiancai.lora.utils.Result;

/**
* @author 86156
* @description 针对表【cost】的数据库操作Service
* @createDate 2023-01-04 18:21:54
*/
public interface CostService extends IService<Cost> {

    Result search(String[] costTime,Integer userId);

    Result searchBalance(Integer userId);

    Result searchRecharge(Integer userId);

    Result searchSpend(Integer userId);
}
