package com.iot.usercenter.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.iot.usercenter.common.ErrorCode;
import com.iot.usercenter.service.IotUserService;
import com.iot.usercenter.mapper.IotUserMapper;
import com.iot.usercenter.utils.JWTUtil;
import com.iot.usercenter.utils.SignUtil;
import com.mayphyr.iotcommon.common.Result;
import com.mayphyr.iotcommon.enums.StatusCode;
import com.mayphyr.iotcommon.exception.BusinessException;
import com.mayphyr.iotcommon.exception.ThrowUtils;
import com.mayphyr.iotcommon.model.dto.iotuser.IotUserRegisterRequest;
import com.mayphyr.iotcommon.model.entity.ApiPass;
import com.mayphyr.iotcommon.model.entity.IotUser;
import com.mayphyr.iotcommon.model.vo.IotUserVo;
import com.mayphyr.iotcommon.model.vo.UserLoginInfo;
import com.mayphyr.iotcommon.utils.RegexUtils;
import com.mayphyr.iotfeign.rpc.apipass.ApiPassClient;
import com.mayphyr.iotfeign.rpc.interfaces.CheckAccessClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.iot.usercenter.common.ErrorCode.PARAMS_ERROR;
import static com.iot.usercenter.common.ErrorCode.SYSTEM_ERROR;
import static com.mayphyr.iotcommon.constant.InterfaceConstant.AK_SK_SALT;
import static com.mayphyr.iotcommon.constant.RedisConstants.LOGIN_USER_KEY;
import static com.mayphyr.iotcommon.constant.RedisConstants.LOGIN_USER_TTL;
import static com.mayphyr.iotcommon.enums.StatusCode.*;

/**
* @author mayphyr
* @description 针对表【iot_user】的数据库操作Service实现
* @createDate 2023-07-28 11:59:50
*/
@Service
public class IotUserServiceImpl extends ServiceImpl<IotUserMapper, IotUser>
    implements IotUserService{


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ApiPassClient apiPassClient;

    @Resource
    private CheckAccessClient checkAccessClient;


    @Override
    public Result login(String email, String password) {
        QueryWrapper<IotUser> wrapper = new QueryWrapper<>();
        wrapper.eq("email",email).eq("password",password);
        IotUser user =this.getOne(wrapper) ;
        //3.校验账号和密码是否匹配
        ThrowUtils.throwIf(user==null,ERROR,"未找到该用户");
        Map<String,Object> map=new HashMap<>();
        map.put("id",user.getId());
        map.put("email",user.getEmail());
        String token = JWTUtil.createToken(map);
        IotUserVo userVo = BeanUtil.copyProperties(user, IotUserVo.class);
        CopyOptions copyOptions = CopyOptions.create()
                .setIgnoreNullValue(true)
                .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString());
        Map<String, Object> bean = BeanUtil.beanToMap(userVo ,new HashMap<>(),copyOptions );

        //现在还要获取用户的AK和SK，但是如果这时候直接把AK和SK给传输过去，那不是还是可以直接获取到嘛，所以应该还是跟后端那个验证的思路一样
        //先获取用户的AK和SK
//        Result<Boolean> booleanResult = apiPassClient.genApiPass(new GenApiPassRequest());
//        ApiPass apiPass = checkAccessClient.checkAccessUser("12");
        ApiPass akSk = apiPassClient.getAKSK(user.getId()).getData();

        String accessKey = akSk.getAccessKey();
        String secretKey = akSk.getSecretKey();
        String encryptSK = SignUtil.genSign(AK_SK_SALT, secretKey);
        // 将token存储到redis中 加密
        stringRedisTemplate.opsForHash().putAll(LOGIN_USER_KEY+token,bean);
        // 设置过期时间
        stringRedisTemplate.expire(LOGIN_USER_KEY+token,LOGIN_USER_TTL, TimeUnit.SECONDS);
        // 将ak，sk，token传给用户
        UserLoginInfo userLoginInfo = UserLoginInfo.builder().ak(accessKey).token(token).encryptSk(secretKey).build();
        return Result.success(userLoginInfo);
    }



    @Override
    public Result register(IotUserRegisterRequest iotUserRegisterRequest){
        String email = iotUserRegisterRequest.getEmail();
        String password = iotUserRegisterRequest.getPassword();
        String name = iotUserRegisterRequest.getName();
        String checkPassword = iotUserRegisterRequest.getCheckPassword();
        ThrowUtils.throwIf(StringUtils.isBlank(email)||RegexUtils.isEmailInvalid(email)
                , SYSTEM_ERR,"邮箱为空或不符合规范");
        ThrowUtils.throwIf(!password.equals(checkPassword), PARAMS_ERR,"两次密码不一致");
        Long count = query().eq("email", email).count();
        ThrowUtils.throwIf(count>0,ERROR,"该邮箱已被注册");
        IotUser user = BeanUtil.copyProperties(iotUserRegisterRequest, IotUser.class);
        boolean save = save(user);
        return Result.success(save);
    }

    @Override
    public Result unEncrypt(String encryptSK, String ak) {
        ApiPass apiPass = checkAccessClient.checkAccessUser(ak);
//        ApiPass akSk = apiPassClient.getAKSK(userId).getData();
        String createdSK = SignUtil.genSign(AK_SK_SALT, apiPass.getSecretKey());
        ThrowUtils.throwIf(!createdSK.equals(encryptSK),StatusCode.SYSTEM_ERR,"传输的加密后的SK与系统生成的不一致");
        return Result.success(true);
    }

    @Override
    public Result<IotUserVo> getCurrentUser(String token) {
        Integer id = Integer.parseInt((String) stringRedisTemplate.opsForHash().get(LOGIN_USER_KEY + token, "id"));
        ThrowUtils.throwIf(id<=0, PARAMS_ERR,"id为null或id<=0");
        QueryWrapper<IotUser> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        IotUser user = this.getOne(wrapper);
        ThrowUtils.throwIf(user ==null,PARAMS_ERR,"该用户不存在");
        IotUserVo userVo = BeanUtil.copyProperties(user, IotUserVo.class);
        return Result.success(userVo);
    }
}




