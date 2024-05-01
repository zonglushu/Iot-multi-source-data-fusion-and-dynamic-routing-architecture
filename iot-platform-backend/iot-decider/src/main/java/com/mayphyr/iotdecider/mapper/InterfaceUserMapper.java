package com.mayphyr.iotdecider.mapper;

import com.mayphyr.iotcommon.model.entity.InterfaceUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

/**
 * @author mayphyr
 * @description 针对表【interface_info】的数据库操作Mapper
 * @createDate 2023-07-22 16:15:30
 * @Entity com.mayphyr.iotdecider.model.entity.InterfaceUser
 */
@Mapper
public interface InterfaceUserMapper {

    //动态创建表
    void createInterUserDynamicTable(@Param("interfaceId") String interfaceId);

    // 查询是否还有剩余容量
    InterfaceUser hasRemainCount(@Param("interfaceId") String interfaceId,@Param("userId") String userId);

    // 插入一条数据
    void insertRecord(@Param("interfaceId") String interfaceId,@Param("userId") String userId);
    // 更新调用次数
    void updateCount(@Param("interfaceId") String interfaceId,@Param("userId") String userId);
}
