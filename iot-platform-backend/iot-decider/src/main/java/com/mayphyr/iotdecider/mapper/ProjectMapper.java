package com.mayphyr.iotdecider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mayphyr.iotcommon.model.entity.Project;
import org.apache.ibatis.annotations.Mapper;

/**
* @author mayphyr
* @description 针对表【project】的数据库操作Mapper
* @createDate 2023-07-29 22:49:46
* @Entity generator.domain.Project
*/

@Mapper
public interface ProjectMapper extends BaseMapper<Project> {

}




