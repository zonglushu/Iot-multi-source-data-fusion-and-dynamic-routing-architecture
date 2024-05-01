package com.mayphyr.iotdecider.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mayphyr.iotcommon.enums.StatusCode;
import com.mayphyr.iotcommon.exception.ThrowUtils;
import com.mayphyr.iotcommon.model.entity.ApiPass;
import com.mayphyr.iotcommon.model.entity.IotUser;
import com.mayphyr.iotcommon.utils.EncryKeyGenerator;
import com.mayphyr.iotdecider.mapper.ApiPassMapper;
import com.mayphyr.iotdecider.service.ApiPassService;

import org.springframework.stereotype.Service;

/**
* @author mayphyr
* @description 针对表【api_pass】的数据库操作Service实现
* @createDate 2023-07-22 17:29:53
*/
@Service
public class ApiPassServiceImpl extends ServiceImpl<ApiPassMapper, ApiPass>
    implements ApiPassService{

    public void test(){

    }

    @Override
    public boolean genApiKeys(Long id, String accessKey, String sercetKey,String email) {
        QueryWrapper<ApiPass> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", id);
        wrapper.eq("accessKey", accessKey);
        wrapper.eq("sercetKey", sercetKey);
        ApiPass apiPass = baseMapper.selectOne(wrapper);
        ThrowUtils.throwIf(apiPass == null , StatusCode.PARAMS_ERR, "accessKey或者sercetKey已过期");
        apiPass.setAccessKey(EncryKeyGenerator.gen(id+email));
        int i = baseMapper.updateById(apiPass);
        return i > 0;
    }

    @Override
    public boolean addApiPass(Long userId, String email) {
        ApiPass apiPass = new ApiPass();
        apiPass.setAccessKey(EncryKeyGenerator.gen(userId+email));
        apiPass.setSecretKey(EncryKeyGenerator.gen(userId+email));
        return save(apiPass);
    }
}




