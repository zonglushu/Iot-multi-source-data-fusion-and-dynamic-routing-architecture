package com.iot.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mayphyr.iotcommon.common.Result;
import com.mayphyr.iotcommon.model.dto.iotuser.IotUserRegisterRequest;
import com.mayphyr.iotcommon.model.entity.IotUser;
import com.mayphyr.iotcommon.model.vo.IotUserVo;

/**
* @author mayphyr
* @description 针对表【iot_user】的数据库操作Service
* @createDate 2023-07-28 11:59:50
*/
public interface IotUserService extends IService<IotUser> {


    Result login(String email, String password);

    Result register(IotUserRegisterRequest iotUserRegisterRequest);

    Result unEncrypt(String encryptSK, String ak);

    Result<IotUserVo> getCurrentUser(String token);
}
