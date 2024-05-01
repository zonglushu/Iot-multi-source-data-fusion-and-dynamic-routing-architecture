package com.mayphyr.iotdecider.controller;


import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mayphyr.iotapiclientsdk.client.SunApiClient;
import com.mayphyr.iotcommon.common.Result;
import com.mayphyr.iotcommon.enums.InterfaceInfoStatusEnum;
import com.mayphyr.iotcommon.enums.StatusCode;

import com.mayphyr.iotcommon.exception.ThrowUtils;
import com.mayphyr.iotcommon.model.dto.interfaceinfo.InterfaceInfoInvokeDTO;

import com.mayphyr.iotcommon.model.dto.interfaceinfo.InterfaceInfoQueryDTO;
import com.mayphyr.iotcommon.model.entity.InterfaceInfo;
import com.mayphyr.iotcommon.model.vo.InterfaceInfoVO;
import com.mayphyr.iotdecider.manager.SunApiClientManager;
import com.mayphyr.iotdecider.service.InterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * 接口管理与调用
 *
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private SunApiClientManager sunApiClientManager;

    /**
     * 模拟调用接口
     * @param interfaceInfoInvokeDTO
     * @param request
     * @return
     */
    @PostMapping(value = "/invoke")
    public Result invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeDTO interfaceInfoInvokeDTO,
                                              HttpServletRequest request){
        ThrowUtils.throwIf(interfaceInfoInvokeDTO==null, StatusCode.PARAMS_ERR,"前端传入的interfaceInfoInvokeDTO为null");
        Long interfaceId = interfaceInfoInvokeDTO.getId();
        ThrowUtils.throwIf(interfaceId==null||interfaceId<=0,StatusCode.PARAMS_ERR,"接口id错误，重新传入");
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(interfaceId);
        ThrowUtils.throwIf(interfaceInfo==null,StatusCode.PARAMS_ERR,"未查到有关接口信息");
        ThrowUtils.throwIf(interfaceInfo.getStatus().equals(InterfaceInfoStatusEnum.OFFLINE.getValue()),StatusCode.PARAMS_ERR,"该接口已下线，请联系管理员");

        //获取接口地址，如：/api/lora/search
        String url = interfaceInfo.getUrl();
        //获取请求方式
        String method = interfaceInfo.getMethod();
        //获取用户传入的参数
        String requestParams = interfaceInfoInvokeDTO.getRequestParams();
        ThrowUtils.throwIf(StringUtils.isBlank(requestParams),StatusCode.PARAMS_ERR,"传入的参数错误");
        //获取一个SDK客户端进行接口的调用
        SunApiClient sunApiClient = sunApiClientManager.getSunApiClientTest(request);
        String invokeResult=null;
        try {
            invokeResult = sunApiClient.invokeInterface(requestParams, url, method);
            ThrowUtils.throwIf(StringUtils.isBlank(invokeResult),StatusCode.PARAMS_ERR,"接口数据为空");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return Result.success(invokeResult);
    }
    /**
     * 根据项目分页获取所有的接口列表
     *
     * @param interfaceInfoQueryRequest
     * @param
     * @return
     */
    @PostMapping("/admin/search/page/by/project")
    public Result<Page<InterfaceInfoVO>> listMyInterfaceInfoVOByPage(
            @RequestBody InterfaceInfoQueryDTO interfaceInfoQueryRequest) {
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, StatusCode.PARAMS_ERR);
        //
        Page<InterfaceInfo> postPage = interfaceInfoService.page(new Page<>(current, size),
                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
        return Result.success(interfaceInfoService.getInterfaceInfoVOPage(postPage));
    }
}
