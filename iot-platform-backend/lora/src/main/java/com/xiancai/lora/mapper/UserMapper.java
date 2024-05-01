package com.xiancai.lora.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiancai.lora.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86156
* @description 针对表【user】的数据库操作Mapper
* @createDate 2022-11-02 07:35:17
* @Entity com.xiancai.lora.model.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




