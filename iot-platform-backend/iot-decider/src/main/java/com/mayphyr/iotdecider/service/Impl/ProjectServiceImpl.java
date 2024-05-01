package com.mayphyr.iotdecider.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.mayphyr.iotcommon.common.Result;
import com.mayphyr.iotcommon.model.entity.Project;
import com.mayphyr.iotdecider.mapper.ProjectMapper;
import com.mayphyr.iotdecider.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author mayphyr
* @description 针对表【project】的数据库操作Service实现
* @createDate 2023-07-29 22:49:46
*/
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project>
    implements ProjectService{

    @Override
    public Result searchAllProjects() {
        List<Project> projectList = query().list();
        return Result.success(projectList);
    }
}




