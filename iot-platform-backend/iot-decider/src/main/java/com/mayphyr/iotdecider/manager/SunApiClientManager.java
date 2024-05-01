package com.mayphyr.iotdecider.manager;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import com.mayphyr.iotapiclientsdk.client.SunApiClient;
import com.mayphyr.iotcommon.common.Result;
import com.mayphyr.iotcommon.enums.StatusCode;
import com.mayphyr.iotcommon.exception.ThrowUtils;
import com.mayphyr.iotcommon.model.entity.ApiPass;
import com.mayphyr.iotcommon.model.vo.IotUserVo;
import com.mayphyr.iotcommon.model.vo.LoginUserVO;
import com.mayphyr.iotdecider.service.ApiPassService;
import com.mayphyr.iotfeign.rpc.UserClient;
import org.springframework.stereotype.Component;
import sun.security.provider.Sun;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
public class SunApiClientManager {

    @Resource
    private UserClient userClient;

    @Resource
    private ApiPassService apiPassService;




    public  SunApiClient getSunApiClientTest(HttpServletRequest request){
        String token = request.getHeader("token");
        System.out.println("获取到到token"+token);
        Result<IotUserVo> loginUser = userClient.getLoginUser(token);
        IotUserVo userVO = loginUser.getData();
        Long userId = userVO.getId();
        ApiPass apiPass = apiPassService.query().eq("user_id", userId).one();
        ThrowUtils.throwIf(ObjectUtil.isNull(apiPass), StatusCode.SYSTEM_ERR,"该用户还未分配AK和SK");
        return new SunApiClient(apiPass.getAccessKey(),apiPass.getSecretKey());

    }


//    public  SunApiClient getSunApiClient(HttpServletRequest request){
//        Result<LoginUserVO> loginUser = userClient.getLoginUser(request);
//        LoginUserVO userVO = loginUser.getData();
//        Long userId = userVO.getId();
//        ApiPass apiPass = apiPassService.query().eq("user_id", userId).one();
//        return new SunApiClient(apiPass.getAccessKey(),apiPass.getSecretKey());
//
//    }
}
