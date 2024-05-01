package com.xiancai.lora.mapper;

import com.xiancai.lora.model.entity.DataS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86156
* @description 针对表【data】的数据库操作Mapper
* @createDate 2022-11-30 10:48:48
* @Entity com.xiancai.lora.model.entity.Data
*/
@Mapper
public interface DataMapper extends BaseMapper<DataS> {
     DataS getLatestData(Integer sensorId,String dataType);

}




