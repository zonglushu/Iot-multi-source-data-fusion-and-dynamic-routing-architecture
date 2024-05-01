package com.mayphyr.iotcommon.model.entity;


import lombok.Data;

@Data
public class InterfaceUser {
    /**
     * 接口id
     */
    private Long interfaceId;
    /**
     * 调用者的用户id
     */
    private Long userId;
    /**
     * 剩余调用测试
     */
    private Long count;

    /**
     * 表的名称：标示这条数据属于哪个动态表
     */
    private String tableName;

}
