package com.xiancai.lora.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiancai.lora.model.DTO.*;
import com.xiancai.lora.model.VO.UserVo;
import com.xiancai.lora.model.entity.User;
import com.xiancai.lora.utils.Result;

/**
* @author 86156
* @description 针对表【user】的数据库操作Service
* @createDate 2022-11-02 07:35:17
*/
public interface UserService extends IService<User> {

    Result register(RegisterDTO registerDTO);

    Result sendCode(EmailCodeDTO emailCodeDTO);

    Result login(LoginDTO loginDTO);

    Result getCurrentUser(String token);

    Result modifyPassword(Integer id,ModifyPasswordDTO modifyPasswordDTO);
    Result forgetPassword(ModifyPasswordDTO modifyPasswordDTO);
    Result addUserBySuperUser(AddedUserDTO addedUserDTO, UserVo superUser);

    Result selectUserListBySuper(Integer superUserId);

    Result deleteUserBySuper(Integer superUserId, Integer userId);
}
