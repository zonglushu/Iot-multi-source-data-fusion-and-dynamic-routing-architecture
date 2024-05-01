package com.xiancai.lora.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.xiancai.lora.annotation.AuthCheck;
import com.xiancai.lora.annotation.SuperCheck;
import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.model.DTO.*;
import com.xiancai.lora.model.VO.UserVo;
import com.xiancai.lora.model.entity.User;
import com.xiancai.lora.service.UserService;
import com.xiancai.lora.utils.JWTUtil;
import com.xiancai.lora.utils.Result;
import com.xiancai.lora.utils.UserHolder;
import com.xiancai.lora.utils.wrong.check.CheckNormalWrong;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.xiancai.lora.enums.StatusCode.NULL_ERR;
import static com.xiancai.lora.enums.StatusCode.PARAMS_ERR;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private CheckNormalWrong checkNormalWrong;
    /**
     * 用户注册
     * @param registerDTO
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO registerDTO){
        checkNormalWrong.checkWrong(registerDTO);
        return userService.register(registerDTO);
    }


    /**
     *注册验证码
     * @param emailCodeDTO
     * @return
     */
    @PostMapping("/register/email")
    public Result sendRegisterCode(@RequestBody EmailCodeDTO emailCodeDTO){
        checkNormalWrong.checkWrong(emailCodeDTO);
        if(emailCodeDTO.getEmailStatus()!=1){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "注册邮箱验证码的emailStatus应为1");
        }
        return userService.sendCode(emailCodeDTO);
    }

    /**
     * 修改和忘记密码验证码
     * @param emailCodeDTO
     * @return
     */
    @PostMapping("/modify/email")
    public Result sendModifyCode(@RequestBody EmailCodeDTO emailCodeDTO){
        checkNormalWrong.checkWrong(emailCodeDTO);
        if(emailCodeDTO.getEmailStatus()!=2){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "忘记邮箱验证码的emailStatus应为2");
        }
        return userService.sendCode(emailCodeDTO);
    }
    /**
     * 用户登录
     * @param loginDTO
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO){
        checkNormalWrong.checkWrong(loginDTO);
        return userService.login(loginDTO);
    }
    /**
     * 获取当前用户
     * @param request
     * @return
     */
    @GetMapping("/info")
    public Result getCurrentUser(HttpServletRequest request){
        String token = request.getHeader("token");
        return userService.getCurrentUser(token);
    }
    /**
     * 修改密码
     * @param modifyPasswordDTO
     * @return
     */
    @PostMapping("/modify/password")
    public Result modifyPassword(@RequestBody ModifyPasswordDTO modifyPasswordDTO ){
        UserVo user = UserHolder.getUser();
        Integer id = user.getId();
        checkNormalWrong.checkWrong(modifyPasswordDTO);
        return userService.modifyPassword(id,modifyPasswordDTO);
    }
    /**
     * 忘记密码
     * @param modifyPasswordDTO
     * @return
     */
    @PostMapping("/forget/password")
    public Result forgetPassword(@RequestBody ModifyPasswordDTO modifyPasswordDTO ){

        checkNormalWrong.checkWrong(modifyPasswordDTO);
        return userService.forgetPassword(modifyPasswordDTO);
    }

    /**
     * 超级用户新增用户
     * @param addedUserDTO
     * @return
     */
    @SuperCheck
    @PostMapping("/add")
    public Result addUserBySuper(@RequestBody AddedUserDTO addedUserDTO){
        checkNormalWrong.checkWrong(addedUserDTO);
        UserVo superUser = UserHolder.getUser();
        return userService.addUserBySuperUser(addedUserDTO,superUser);
    }

    /**
     * 超级用户查找用户列表
     * @return
     */
    @SuperCheck
    @GetMapping("/userlist")
    public Result selectUserListBySuper(){
        UserVo user = UserHolder.getUser();
        Integer superUserId = user.getId();
        return userService.selectUserListBySuper(superUserId);
    }

    /**
     * 超级用户删除用户
     * @return
     */
    @SuperCheck
    @GetMapping("/del")
    public Result deleteUserListBySuper(@RequestParam("userId") Integer userId){
        checkNormalWrong.checkId(userId,"前端传入的userId为空");
        UserVo user = UserHolder.getUser();
        Integer superUserId = user.getId();
        return userService.deleteUserBySuper(superUserId,userId);
    }

    @ApiOperation(value = "testAdminLevel0",notes = "测试方法，没有实际意义，测试只有用户的权限是0才可以使用该方法")
    @AuthCheck(mustRole = 0)
    @PostMapping("/admin/0")
    public Result testAdminLevel0(){
        return Result.success(true);
    }

    @ApiOperation(value = "testAdminLevel1",notes = "测试方法，没有实际意义，测试只有用户的权限是1才可以使用该方法")
    @AuthCheck(mustRole = 1)
    @PostMapping("/admin/1")
    public Result testAdminLevel1(){
        return Result.success(true);
    }

    @ApiOperation(value = "testAdminLevel2",notes = "测试方法，没有实际意义，测试只有用户的权限是2才可以使用该方法")
    @AuthCheck(mustRole = 2)
    @PostMapping("/admin/2")
    public Result testAdminLevel2(){
        return Result.success(true);
    }

    @ApiOperation(value = "testAdminLevel",notes = "测试方法，没有实际意义，测试只要是用户的权限是1，2，3的一种就可以使用该方法")
    @AuthCheck(anyRole = {0,1,2})
    @PostMapping("/admin/3")
    public Result testAdminLevel(){
        return Result.success(true);
    }



}
