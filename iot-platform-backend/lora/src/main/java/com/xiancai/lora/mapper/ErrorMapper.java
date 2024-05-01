package com.xiancai.lora.mapper;

import com.xiancai.lora.model.entity.Error;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86156
* @description 针对表【error】的数据库操作Mapper
* @createDate 2023-01-16 12:13:26
* @Entity com.xiancai.lora.model.entity.Error
*/
@Mapper
public interface ErrorMapper extends BaseMapper<Error> {

}




