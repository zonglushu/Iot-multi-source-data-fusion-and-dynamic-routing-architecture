package com.xiancai.lora.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.model.DTO.*;
import com.xiancai.lora.model.VO.UserVo;
import com.xiancai.lora.model.entity.User;
import com.xiancai.lora.service.UserService;
import com.xiancai.lora.mapper.UserMapper;
import com.xiancai.lora.utils.EmailUtil;
import com.xiancai.lora.utils.JWTUtil;
import com.xiancai.lora.utils.RegexUtils;
import com.xiancai.lora.utils.Result;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import static com.xiancai.lora.constant.RedisConstants.*;
import static com.xiancai.lora.enums.StatusCode.PARAMS_ERR;
import static com.xiancai.lora.enums.StatusCode.SYSTEM_ERR;


/**
* @author 86156
* @description 针对表【user】的数据库操作Service实现
* @createDate 2022-11-02 07:35:17
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private EmailUtil emailUtil;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result register(RegisterDTO registerDTO) {
        String password = registerDTO.getPassword();
        String email = registerDTO.getEmail();
        String phone = registerDTO.getPhone();
        String checkPassword = registerDTO.getCheckPassword();
        //4.校验两次输入的密码是否一致
        if(!password.equals(checkPassword)){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "两次密码不一致");
        }
        //5.检查是否已经注册
        Long count = query().eq("email", email).count();
        Long countPhone = query().eq("phone", phone).count();
        if(count>0){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "该邮箱已被注册");
        }
        if(countPhone>0){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "该手机号已被注册");
        }
        String code = stringRedisTemplate.opsForValue().get(EMAIL_REGISTER_CODE_KEY+email);
        //6.校验邮箱验证码是否正确
        String emailCode = registerDTO.getEmailCode();
        if(!emailCode.equals(code)){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "邮箱验证码错误");
        }
        User user = BeanUtil.copyProperties(registerDTO, User.class);
        boolean save = this.save(user);
        return Result.success(save);
    }

    @Override
    public Result login(LoginDTO loginDTO) {
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email",email).eq("password",password);
        User entity =this.getOne(wrapper) ;
        //3.校验账号和密码是否匹配
        if(entity==null){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "未找到该用户，请检查账号或密码");
        }
        Map<String,Object> map=new HashMap<>();
        map.put("id",entity.getId());
        map.put("email",entity.getEmail());
        String token = JWTUtil.createToken(map);
        UserVo userVo = BeanUtil.copyProperties(entity, UserVo.class);
        Map<String, Object> bean = BeanUtil.beanToMap(userVo ,new HashMap<>(),
        CopyOptions.create()
                .setIgnoreNullValue(true)
                .setFieldValueEditor((fieldName,fieldValue)->fieldValue.toString()));
        stringRedisTemplate.opsForHash().putAll(LOGIN_USER_KEY+token,bean);
        stringRedisTemplate.expire(LOGIN_USER_KEY+token,LOGIN_USER_TTL, TimeUnit.SECONDS);
        return Result.success(token);
    }

    @Override
    public Result getCurrentUser(String token) {
        Integer id = Integer.parseInt((String) stringRedisTemplate.opsForHash().get(LOGIN_USER_KEY + token, "id"));
        if(id==null||id<=0){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "id为null或id<=0");
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        User user = this.getOne(wrapper);
        if(user ==null){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "该用户不存在");
        }
        UserVo userVo = BeanUtil.copyProperties(user, UserVo.class);
        //
        return Result.success(userVo);
    }

    @Override
    public Result modifyPassword(Integer id ,ModifyPasswordDTO modifyPasswordDTO) {
        User user = this.getById(id);
        String email = user.getEmail();
        if(!email.equals(modifyPasswordDTO.getEmail())){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "输入的邮箱与已登录用户的邮箱不一致");
        }
        return forgetPassword(modifyPasswordDTO);
    }
    public Result forgetPassword(ModifyPasswordDTO modifyPasswordDTO) {
        String newPassword = modifyPasswordDTO.getNewPassword();
        verity(modifyPasswordDTO.getEmail(),newPassword);
        //应该是由旧密码查找用户
        String oldPassword = modifyPasswordDTO.getOldPassword();
        User user = this.getOne(new QueryWrapper<User>().eq("password",oldPassword));
        String emailCode = modifyPasswordDTO.getEmailCode();
        String code = stringRedisTemplate.opsForValue().get(EMAIL_CHANGE_PASSWORD_CODE_KEY+modifyPasswordDTO.getEmail());
        if(!emailCode.equals(code)){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "邮箱验证码错误");
        }
        user.setPassword(newPassword);
        boolean update = this.updateById(user);
        if(!update){
            throw new BusinessException(SYSTEM_ERR.getMessage(), SYSTEM_ERR.getCode(),"数据库更新错误");
        }
        return Result.success(update);
    }
    @Override
    public Result addUserBySuperUser(AddedUserDTO addedUserDTO, UserVo superUser) {
        String email = addedUserDTO.getEmail();
        String password = addedUserDTO.getPassword();
        String name = addedUserDTO.getName();
        verity(email,password);
        if(StrUtil.isBlank(name)){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "名称为空或长度为0");
        }
        User build = User.builder()
                .email(email)
                .company(superUser.getCompany())
                .phone(superUser.getPhone())
                .website(superUser.getWebsite())
                .username(name)
                .password(password)
                .parentId(superUser.getId())
                .build();
        boolean isSave = save(build);
        if(!isSave){
            throw new BusinessException(SYSTEM_ERR.getMessage(), SYSTEM_ERR.getCode(),"数据库新增错误");
        }
        return Result.success(isSave);
    }

    @Override
    public Result selectUserListBySuper(Integer superUserId) {
        List<User> userList = this.list(new QueryWrapper<User>().eq("parent_id", superUserId));
        if(userList==null||userList.isEmpty()){
            return Result.success(Collections.emptyList());
        }
        return Result.success(userList);
    }

    @Override
    public Result deleteUserBySuper(Integer superUserId, Integer userId) {
        boolean isRemove = remove(new QueryWrapper<User>().eq("parent_id", superUserId).eq("id", userId));
        if(!isRemove){
            throw new BusinessException(SYSTEM_ERR.getMessage(), SYSTEM_ERR.getCode(),"数据库删除错误");
        }
        return Result.success(isRemove);
    }

    private void verity(String email ,String password){
        //1.校验邮箱是否正确
        if(RegexUtils.isEmailInvalid(email)){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "邮箱格式错误");
        }
        //2.校验密码是否符合规范
        if(StrUtil.isBlank(password)){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "密码为空");
        }
        if(password.length()>20||password.length()<6){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "密码不符合规范");
        }
    }

    @Override
    public Result sendCode(EmailCodeDTO emailCodeDTO)   {
        String code="";
        String email = emailCodeDTO.getEmail();
        int emailStatus = emailCodeDTO.getEmailStatus();
        //1.校验邮箱是否正确
        if(RegexUtils.isEmailInvalid(email)){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "邮箱格式错误");
        }
        try {
            code = emailUtil.sendEmail(email, emailStatus);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return Result.success(code);
    }
}




