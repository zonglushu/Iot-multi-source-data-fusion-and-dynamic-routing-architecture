package com.mayphyr.iotdecider.controller;

import com.mayphyr.iotcommon.common.Result;
import com.mayphyr.iotcommon.constant.FeignConstant;
import com.mayphyr.iotcommon.enums.StatusCode;
import com.mayphyr.iotcommon.exception.BusinessException;
import com.mayphyr.iotcommon.exception.ThrowUtils;
import com.mayphyr.iotcommon.model.dto.apipass.AddApiPassRequest;
import com.mayphyr.iotcommon.model.dto.apipass.GenApiPassRequest;
import com.mayphyr.iotcommon.model.dto.iotuser.FeignUserAKSK;
import com.mayphyr.iotcommon.model.entity.ApiPass;
import com.mayphyr.iotdecider.service.ApiPassService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;



@RestController
@RequestMapping("/pass")
@Slf4j
public class ApiPassController {


    @Resource
    private ApiPassService apiPassService;

    /**
     * 根据旧的ak和sk生成新的ak和sk
     *
     * @param genApiPassRequest
     * @return
     */
    @PostMapping("/gen")
    public Result<Boolean> genApiPass(@RequestBody GenApiPassRequest genApiPassRequest) {
        if (genApiPassRequest == null) {
            throw new BusinessException(StatusCode.PARAMS_ERR);
        }

        Long id = genApiPassRequest.getId();
        String accessKey = genApiPassRequest.getAccessKey();
        String sercetKey = genApiPassRequest.getSercetKey();
        String email = genApiPassRequest.getEmail();

        if (Objects.isNull(id) || StringUtils.isAnyBlank(accessKey, sercetKey)) {
            throw new BusinessException(StatusCode.PARAMS_ERR);
        }
        boolean result = apiPassService.genApiKeys(id, accessKey, sercetKey, email);
        return Result.success(result);
    }

    /**
     * 根据用户id获取AK和SK
     * @param userId
     * @return
     */
    @RequestMapping(value = "/get/all/{userid}", method = RequestMethod.GET)
    public Result<ApiPass> getAKSK(@PathVariable("userid") Long userId){
//        Long userId = feignUserAKSK.getUserId();
        ThrowUtils.throwIf(Objects.isNull(userId),StatusCode.PARAMS_ERR, FeignConstant.INVOKE_ERROR+"getAKSK");
        ApiPass apiPass = apiPassService.query().eq("user_id", userId).one();
        return Result.success(apiPass) ;
    }


    @PostMapping("/add")
    public Result<Boolean> addApiPass(@RequestBody AddApiPassRequest addApiPassRequest) {
        ThrowUtils.throwIf(addApiPassRequest == null, StatusCode.PARAMS_ERR);
        String email = addApiPassRequest.getEmail();
        Long userId = addApiPassRequest.getUserId();
        ThrowUtils.throwIf(Objects.isNull(userId) || StringUtils.isBlank(email), StatusCode.PARAMS_ERR);
        //TODO 邮箱格式验证
        boolean isAdd = apiPassService.addApiPass(userId, email);
        ThrowUtils.throwIf(!isAdd, StatusCode.ERROR, "数据库插入错误");
        return Result.success(isAdd);
    }
}