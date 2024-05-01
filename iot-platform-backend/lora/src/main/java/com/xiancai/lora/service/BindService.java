package com.xiancai.lora.service;

import com.xiancai.lora.model.DTO.BindDTO;
import com.xiancai.lora.model.entity.Bind;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiancai.lora.utils.Result;
import org.springframework.stereotype.Service;


/**
* @author 86156
* @description 针对表【bind】的数据库操作Service
* @createDate 2022-11-10 21:17:18
*/

public interface BindService extends IService<Bind> {

    Result setBind(BindDTO bindDTO);

     void setBind(BindDTO bindDTO,Bind one);

    Result relieveBind(Integer nodeId);
}
