package com.mayphyr.iotdecider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mayphyr.iotcommon.model.entity.ApiPass;
import org.apache.ibatis.annotations.Mapper;

/**
* @author mayphyr
* @description 针对表【api_pass】的数据库操作Mapper
* @createDate 2023-07-22 17:29:53
* @Entity com.mayphyr.iotcommon.model.entity.ApiPass
*/
@Mapper
public interface ApiPassMapper extends BaseMapper<ApiPass> {

}




