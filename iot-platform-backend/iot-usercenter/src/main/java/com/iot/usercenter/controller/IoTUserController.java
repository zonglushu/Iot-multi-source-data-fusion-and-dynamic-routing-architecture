package com.iot.usercenter.controller;

import com.iot.usercenter.service.IotUserService;
import com.mayphyr.iotcommon.common.Result;
import com.mayphyr.iotcommon.enums.StatusCode;
import com.mayphyr.iotcommon.exception.ThrowUtils;
import com.mayphyr.iotcommon.model.dto.iotuser.DecryptSK;
import com.mayphyr.iotcommon.model.dto.iotuser.IotUserLoginRequest;
import com.mayphyr.iotcommon.model.dto.iotuser.IotUserRegisterRequest;
import com.mayphyr.iotcommon.model.vo.IotUserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 物联网接口开放平台用户管理
 */
@RestController
@RequestMapping("/iotuser")
@Slf4j
public class IoTUserController {

    @Resource
    private IotUserService iotUserService;



    @PostMapping("/login")
    public Result login(@RequestBody IotUserLoginRequest iotUserLoginRequest){
        ThrowUtils.throwIf(iotUserLoginRequest==null,StatusCode.PARAMS_ERR,"登陆用户参数为空");
        String email = iotUserLoginRequest.getEmail();
        String password = iotUserLoginRequest.getPassword();
        ThrowUtils.throwIf(StringUtils.isBlank(email),StatusCode.PARAMS_ERR,"邮箱为空");
        ThrowUtils.throwIf(StringUtils.isBlank(password),StatusCode.PARAMS_ERR,"密码为空");
        return iotUserService.login(email,password);

    }

    //TODO 要有一个内测码，并且内测码还要时不时的改变，可以在redis里面写
    @PostMapping("/register")
    public Result register(@RequestBody IotUserRegisterRequest iotUserRegisterRequest){
        ThrowUtils.throwIf(iotUserRegisterRequest==null,StatusCode.PARAMS_ERR,"登陆用户参数为空");
        return iotUserService.register(iotUserRegisterRequest);
    }


    @PostMapping("/decrypt")
    public Result decryptSK(@RequestBody DecryptSK decryptSK , HttpServletRequest request){
        String pathInfo = request.getRemoteAddr();
        System.out.println(pathInfo);
        ThrowUtils.throwIf(decryptSK==null,StatusCode.PARAMS_ERR,"要解密的对象为空");
        String encryptSK = decryptSK.getEncryptSK();
        String ak = decryptSK.getAk();
        return iotUserService.unEncrypt(encryptSK,ak);

    }
    @GetMapping("/get/login")
    public Result<IotUserVo> getLoginUser(@RequestParam("token") String token){
        System.out.println("被调用了");
        return iotUserService.getCurrentUser(token);
    }


}
