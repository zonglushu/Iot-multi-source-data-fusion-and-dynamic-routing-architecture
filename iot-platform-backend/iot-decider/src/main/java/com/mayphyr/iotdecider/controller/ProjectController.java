package com.mayphyr.iotdecider.controller;


import com.mayphyr.iotcommon.common.Result;
import com.mayphyr.iotcommon.model.entity.Project;
import com.mayphyr.iotdecider.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 接口管理与调用
 *
 */
@RestController
@RequestMapping("/project")
@Slf4j
public class ProjectController {


    @Resource
    private ProjectService projectService;


    @GetMapping("/search/all")
    public Result searchAllProjects(){
        return projectService.searchAllProjects();
    }
}
