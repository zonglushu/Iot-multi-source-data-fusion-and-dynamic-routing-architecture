package com.xiancai.lora.service;

import com.xiancai.lora.model.entity.Io;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiancai.lora.utils.Result;

/**
* @author 86156
* @description 针对表【IO】的数据库操作Service
* @createDate 2023-05-12 17:09:50
*/
public interface IoService extends IService<Io> {

    Result getIOByNode(Integer userId);
}
