package com.xiancai.lora.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiancai.lora.model.entity.Command;
import com.xiancai.lora.service.CommandService;
import com.xiancai.lora.mapper.CommandMapper;
import org.springframework.stereotype.Service;

/**
* @author 86156
* @description 针对表【command】的数据库操作Service实现
* @createDate 2023-01-14 21:37:39
*/
@Service
public class CommandServiceImpl extends ServiceImpl<CommandMapper, Command>
    implements CommandService{

}




