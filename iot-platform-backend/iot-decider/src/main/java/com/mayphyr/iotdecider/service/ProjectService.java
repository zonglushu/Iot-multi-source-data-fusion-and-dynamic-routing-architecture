package com.mayphyr.iotdecider.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mayphyr.iotcommon.common.Result;
import com.mayphyr.iotcommon.model.entity.Project;

/**
* @author mayphyr
* @description 针对表【project】的数据库操作Service
* @createDate 2023-07-29 22:49:46
*/
public interface ProjectService extends IService<Project> {

    Result searchAllProjects();
}
