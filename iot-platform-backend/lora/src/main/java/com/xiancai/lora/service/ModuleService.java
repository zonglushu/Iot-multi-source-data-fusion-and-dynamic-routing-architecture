package com.xiancai.lora.service;

import com.xiancai.lora.model.entity.Module;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiancai.lora.utils.Result;

import java.util.Map;

/**
* @author 86156
* @description 针对表【module】的数据库操作Service
* @createDate 2023-01-16 12:13:19
*/
public interface ModuleService extends IService<Module> {

    Result poweron(Map<String, Object> data);

    Result poweroff(Map<String, Object> data);

    Result operate(Map<String, Object> data);
}
