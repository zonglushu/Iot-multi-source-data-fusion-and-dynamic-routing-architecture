package com.xiancai.lora.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiancai.lora.model.entity.Error;
import com.xiancai.lora.service.ErrorService;
import com.xiancai.lora.mapper.ErrorMapper;
import org.springframework.stereotype.Service;

/**
* @author 86156
* @description 针对表【error】的数据库操作Service实现
* @createDate 2023-01-16 12:13:26
*/
@Service
public class ErrorServiceImpl extends ServiceImpl<ErrorMapper, Error>
    implements ErrorService{

}




