package com.xiancai.lora.mapper;

import com.xiancai.lora.model.entity.Cost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 86156
* @description 针对表【cost】的数据库操作Mapper
* @createDate 2023-01-04 18:21:54
* @Entity com.xiancai.lora.model.entity.Cost
*/
@Mapper
public interface CostMapper extends BaseMapper<Cost> {

    List<Cost> searchByTime(String start,String end,Integer userId);
}




