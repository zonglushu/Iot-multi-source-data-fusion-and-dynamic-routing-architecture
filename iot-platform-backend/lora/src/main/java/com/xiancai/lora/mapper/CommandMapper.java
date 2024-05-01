package com.xiancai.lora.mapper;

import com.xiancai.lora.model.entity.Command;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86156
* @description 针对表【command】的数据库操作Mapper
* @createDate 2023-01-14 21:37:39
* @Entity com.xiancai.lora.model.entity.Command
*/
@Mapper
public interface CommandMapper extends BaseMapper<Command> {

}




