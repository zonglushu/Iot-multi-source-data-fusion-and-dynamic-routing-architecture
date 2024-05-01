package com.mayphyr.iotdecider.service.Impl;



import com.mayphyr.iotcommon.enums.StatusCode;
import com.mayphyr.iotcommon.exception.ThrowUtils;
import com.mayphyr.iotcommon.model.entity.InterfaceUser;
import com.mayphyr.iotdecider.mapper.InterfaceUserMapper;
import com.mayphyr.iotdecider.service.InterfaceInfoService;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;

import static com.mayphyr.iotcommon.constant.InterfaceConstant.DEFAULT_INTERFACE_COUNT;

@Service
public class CheckServiceImpl {
    @Resource
    private InterfaceUserMapper interfaceUserMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     *    创建表，肯定是创建一次就够了，那也不能每次都去数据库里面判断一下，有没有这个表，所以，这个是要写给新增接口的
     *    每新增一个接口，就要创建一张表
     * @param interfaceId
     */
    public void createInterUserDynamicTable(String interfaceId) {
        // 接受传入的表名，进行基于白名单的验证和过滤
        isValidTableName(interfaceId);
        // 创建接口-用户信息表
        interfaceUserMapper.createInterUserDynamicTable(interfaceId);
    }

    /**
     * 看看是否还有剩余调用次数
     */
    public InterfaceUser hasRemainCount(String interfaceId,String userId){
        return interfaceUserMapper.hasRemainCount(interfaceId,userId);
    }

    /**
     * 如果还有剩余调用次数，更新调用次数-1
     * @param
     */
    public void decreaseInvokeCount( Long interfaceId,Long userId){
        interfaceUserMapper.updateCount(interfaceId.toString(),userId.toString());
    }

    /**
     * 如果该用户每调用过该接口，直接创建一条数据
     * @param interfaceId
     * @param userId
     */
    public void addDefaultInterfaceUser(Long interfaceId, Long userId){
        InterfaceUser interfaceUser = new InterfaceUser();;
        interfaceUser.setInterfaceId(interfaceId);
        interfaceUser.setUserId(userId);
        interfaceUser.setCount(DEFAULT_INTERFACE_COUNT);
        interfaceUserMapper.insertRecord(interfaceId.toString(),userId.toString());
    }

    private void isValidTableName(String interfaceId) {
        // 根据业务需求设置合适的表名验证规则，例如使用正则表达式、白名单等。
        // 此处仅做示例，可根据实际情况自行实现
        Long count = interfaceInfoService.query().eq("id", interfaceId).count();
        ThrowUtils.throwIf(count==null||count==0, StatusCode.ERROR,"未找到该接口，无法创建接口-用户信息表");
    }


}
