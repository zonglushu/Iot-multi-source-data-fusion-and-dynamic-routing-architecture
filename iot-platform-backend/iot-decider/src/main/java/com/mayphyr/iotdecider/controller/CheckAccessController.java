package com.mayphyr.iotdecider.controller;

import com.mayphyr.iotcommon.enums.StatusCode;
import com.mayphyr.iotcommon.exception.ThrowUtils;
import com.mayphyr.iotcommon.model.dto.ApiInfo;
import com.mayphyr.iotcommon.model.entity.ApiPass;
import com.mayphyr.iotcommon.model.entity.InterfaceInfo;
import com.mayphyr.iotcommon.model.entity.InterfaceUser;
import com.mayphyr.iotdecider.mapper.InterfaceUserMapper;
import com.mayphyr.iotdecider.service.ApiPassService;
import com.mayphyr.iotdecider.service.Impl.CheckServiceImpl;
import com.mayphyr.iotdecider.service.InterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/check")
@Slf4j
public class CheckAccessController {

    @Resource
    private ApiPassService apiPassService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private CheckServiceImpl checkService;


    /**
     * 判断该aceessKey是否有效
     * @param accessKey
     * @return
     */
    @GetMapping("/access/{accessKey}")
    public ApiPass checkAccessUser(@PathVariable("accessKey") String accessKey){

        ApiPass apiPass = apiPassService.query().eq("access_key", accessKey).one();
        return apiPass;

    }

    /**
     * 获取该接口的信息
     * @param apiInfo
     * @return
     */
    @PostMapping("/interface/info")
    public InterfaceInfo checkInterfaceInfo(@RequestBody ApiInfo apiInfo){
        ThrowUtils.throwIf(apiInfo==null, StatusCode.ERROR,"ApiInfo对象生成错误");
        String host = apiInfo.getHost();
        String uri = apiInfo.getUri();
        String method = apiInfo.getMethod();

        return interfaceInfoService.query().eq("host", host).eq("uri", uri).eq("method", method).one();
    }

    /**
     * 判断该接口，该用户是否还有调用次数
     * @param interfaceId
     * @param userId
     * @return
     */
    @GetMapping("/interface/remain")
    public InterfaceUser hasRemainCount(@RequestParam("interfaceId") Long interfaceId,@RequestParam("userId") Long userId){
        return checkService.hasRemainCount(interfaceId.toString(), userId.toString());
    }

    /**
     * 添加接口-用户数据
     * @param interfaceId
     * @param userId
     */
    @GetMapping("/interface/add")
    public void addDefaultInterfaceUser(@RequestParam("interfaceId") Long interfaceId, @RequestParam("userId") Long userId){
        checkService.addDefaultInterfaceUser(interfaceId,userId);
    }

    /**
     * 更新接口调用次数
     * @param interfaceId
     * @param userId
     */
    @GetMapping("/interface/decrease")
    public void decreaseInvokeCount(@RequestParam("interfaceId") Long interfaceId, @RequestParam("userId") Long userId){
        checkService.decreaseInvokeCount(interfaceId,userId);
    }



}
