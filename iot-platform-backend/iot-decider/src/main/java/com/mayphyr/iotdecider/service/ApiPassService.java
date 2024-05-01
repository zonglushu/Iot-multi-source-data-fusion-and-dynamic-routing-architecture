package com.mayphyr.iotdecider.service;

import com.mayphyr.iotcommon.model.entity.ApiPass;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author mayphyr
* @description 针对表【api_pass】的数据库操作Service
* @createDate 2023-07-22 17:29:53
*/
public interface ApiPassService extends IService<ApiPass> {

    boolean genApiKeys(Long id, String accessKey, String sercetKey,String email);

    boolean addApiPass(Long userId, String email);
}
