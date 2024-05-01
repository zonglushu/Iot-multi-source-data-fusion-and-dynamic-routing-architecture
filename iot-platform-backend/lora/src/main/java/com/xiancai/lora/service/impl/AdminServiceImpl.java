package com.xiancai.lora.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiancai.lora.model.entity.Admin;
import com.xiancai.lora.service.AdminService;
import com.xiancai.lora.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
* @author 86156
* @description 针对表【admin】的数据库操作Service实现
* @createDate 2022-11-06 16:55:52
*/
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
    implements AdminService{


}




