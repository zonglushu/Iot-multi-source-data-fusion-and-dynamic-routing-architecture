package com.mayphyr.iotdecider.service.Impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mayphyr.iotcommon.constant.CommonConstant;
import com.mayphyr.iotcommon.model.dto.interfaceinfo.InterfaceInfoQueryDTO;
import com.mayphyr.iotcommon.model.entity.InterfacResponseParamDesc;
import com.mayphyr.iotcommon.model.entity.InterfaceInfo;
import com.mayphyr.iotcommon.model.entity.InterfaceRequestParamDesc;
import com.mayphyr.iotcommon.model.entity.IotUser;
import com.mayphyr.iotcommon.model.vo.InfaRequestParamDescVO;
import com.mayphyr.iotcommon.model.vo.InterfaceInfoVO;
import com.mayphyr.iotcommon.model.vo.ParamDesc;
import com.mayphyr.iotcommon.model.vo.ParamExample;
import com.mayphyr.iotcommon.utils.SqlUtils;
import com.mayphyr.iotdecider.mapper.InterfacResponseParamDescMapper;
import com.mayphyr.iotdecider.mapper.InterfaceRequestParamDescMapper;
import com.mayphyr.iotdecider.service.InterfacResponseParamDescService;
import com.mayphyr.iotdecider.service.InterfaceInfoService;
import com.mayphyr.iotdecider.mapper.InterfaceInfoMapper;
import com.mayphyr.iotdecider.service.InterfaceRequestParamDescService;
import com.mayphyr.iotdecider.utils.converts.TypeConverter;
import com.mayphyr.iotdecider.utils.converts.TypeConverterFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author mayphyr
 * @description 针对表【interface_info】的数据库操作Service实现
 * @createDate 2023-07-22 16:15:30
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {


    @Resource
    private InterfacResponseParamDescService interfacResponseParamDescService;

    @Resource
    private InterfaceRequestParamDescService interfaceRequestParamDescService;


    @Resource
    private InterfaceRequestParamDescMapper interfaceRequestParamDescMapper;

    @Resource
    private InterfacResponseParamDescMapper interfacResponseParamDescMapper;

    @Override
    public QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryDTO interfaceInfoQueryDTO) {
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        if (interfaceInfoQueryDTO == null) {
            return queryWrapper;
        }

        String name = interfaceInfoQueryDTO.getName();
        String url = interfaceInfoQueryDTO.getUrl();
        String requestMethod = interfaceInfoQueryDTO.getRequestMethod();
        Integer status = interfaceInfoQueryDTO.getStatus();
        Long userId = interfaceInfoQueryDTO.getUserId();
        Integer isDelete = interfaceInfoQueryDTO.getIsDelete();
        String sortField = interfaceInfoQueryDTO.getSortField();
        String sortOrder = interfaceInfoQueryDTO.getSortOrder();
        Long projectID = interfaceInfoQueryDTO.getProjectID();

        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(url), "url", url);
        queryWrapper.eq(StringUtils.isNotBlank(requestMethod), "requestMethod", requestMethod);
        queryWrapper.eq(Objects.nonNull(status), "status", status);
        queryWrapper.eq(Objects.nonNull(userId), "userId", userId);
        queryWrapper.eq(Objects.nonNull(isDelete), "isDelete", isDelete);
        queryWrapper.eq(Objects.nonNull(projectID), "project_id", projectID);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


    /**
     * 将得到的接口数据分页数据 转换成 接口VO分页数据
     *
     * @param interfaceInfoPage
     * @return
     */
    @Override
    public Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage) {
        List<InterfaceInfo> postList = interfaceInfoPage.getRecords();

        Page<InterfaceInfoVO> interfaceInfoVOPage = new Page<>(interfaceInfoPage.getCurrent(), interfaceInfoPage.getSize(), interfaceInfoPage.getTotal());
        if (CollectionUtils.isEmpty(postList)) {
            return interfaceInfoVOPage;
        }
        // 填充信息,把vo和po共有的属性填充上
        List<InterfaceInfoVO> postVOList = postList.stream().map(InterfaceInfoVO::poToVo).collect(Collectors.toList());
        // 接口id列表
        List<Long> interfaceIdList = postVOList.stream().map(InterfaceInfoVO::getId).collect(Collectors.toList());
        // 把涉及到接口的请求参数和响应参数都拿出来
        LambdaQueryWrapper<InterfaceRequestParamDesc> lambdaQueryWrapperRequest = Wrappers.lambdaQuery(InterfaceRequestParamDesc.class);
        LambdaQueryWrapper<InterfacResponseParamDesc> lambdaQueryWrapperResponse = Wrappers.lambdaQuery(InterfacResponseParamDesc.class);
        lambdaQueryWrapperRequest.in(InterfaceRequestParamDesc::getInfaId, interfaceIdList);
        lambdaQueryWrapperResponse.in(InterfacResponseParamDesc::getInfaId,interfaceIdList);
        // 把接口列表里面所涉及到的属性全部提取出来
        List<InterfaceRequestParamDesc> interfaceRequestParamDescs = interfaceRequestParamDescMapper.selectList(lambdaQueryWrapperRequest);
        List<InterfacResponseParamDesc> interfaceResponseParamDescs = interfacResponseParamDescMapper.selectList(lambdaQueryWrapperResponse);
        postVOList.forEach(interfaceInfoVO -> {
            Long interfaceId = interfaceInfoVO.getId();
            //获得该接口的所有请求参数描述还有参数示例
            List<InterfaceRequestParamDesc> requestParamDescList = interfaceRequestParamDescs.stream().filter(interfaceRequestParamDesc ->
                    interfaceRequestParamDesc.getInfaId().equals(interfaceId)).collect(Collectors.toList());
            List<InterfacResponseParamDesc> responseParamDescList = interfaceResponseParamDescs.stream().filter(interfacResponseParamDesc ->
                    interfacResponseParamDesc.getInfaId().equals(interfaceId)).collect(Collectors.toList());
            // 获得该接口的请求参数示例
            //获得key-value类型的请求参数描述
            List<InfaRequestParamDescVO> keyValueRequestDesc = getKeyValueRequestDesc(requestParamDescList);
            List<ParamExample> requestParamExample = getRequestParamExample(requestParamDescList);
            List<InfaRequestParamDescVO> keyValueResponseDesc = getKeyValueResponseDesc(responseParamDescList);
            List<ParamExample> responseParamExample = getResponseParamExample(responseParamDescList);
            // 将获得的参数都设置上去
            interfaceInfoVO.setRequestParamExample(requestParamExample);
            interfaceInfoVO.setRequestParamDesc(keyValueRequestDesc);
            interfaceInfoVO.setResponseParamDesc(keyValueResponseDesc);
            interfaceInfoVO.setResponseParamExample(responseParamExample);
        });
        interfaceInfoVOPage.setRecords(postVOList);
        return interfaceInfoVOPage;
    }


    //获得key-value类型的请求参数描述
    private List<InfaRequestParamDescVO> getKeyValueRequestDesc(List<InterfaceRequestParamDesc> requestParamDescList){
        System.out.println("-----------------------------------------");
        return requestParamDescList.stream().map(requestParamDesc -> {
            ParamDesc mustParamDesc = ParamDesc.getMustParamDesc(requestParamDesc.getMust());
            ParamDesc typeParamDesc = ParamDesc.getTypeParamDesc(requestParamDesc.getType());
            ParamDesc descParamDesc = ParamDesc.getDescParamDesc(requestParamDesc.getDescription());
            return InfaRequestParamDescVO.builder().key(requestParamDesc.getName())
                    .value(ParamDesc.getParamDescList(mustParamDesc, typeParamDesc, descParamDesc)).build();
        }).collect(Collectors.toList());
    }
    //获得key-value类型的响应参数描述
    private List<InfaRequestParamDescVO> getKeyValueResponseDesc(List<InterfacResponseParamDesc> responseParamDescList ){
        return responseParamDescList.stream().map(responseParamDesc -> {
            ParamDesc typeParamDesc = ParamDesc.getTypeParamDesc(responseParamDesc.getType());
            ParamDesc descParamDesc = ParamDesc.getDescParamDesc(responseParamDesc.getDescription());
            return InfaRequestParamDescVO.builder().key(responseParamDesc.getName())
                    .value(ParamDesc.getParamDescList(typeParamDesc, descParamDesc)).build();
        }).collect(Collectors.toList());
    }
    // 获得该接口的请求参数示例
    private  List<ParamExample> getRequestParamExample (List<InterfaceRequestParamDesc> requestParamDescList ){
        return requestParamDescList.stream().map(requestParamDesc -> {
            String name = requestParamDesc.getName();
            String example = requestParamDesc.getExample();
            String type = requestParamDesc.getType();
            TypeConverter typeConverter = TypeConverterFactory.getTypeConverter(type);
            Object convertExample = typeConverter.convert(example);
            return ParamExample.builder().key(name).example(convertExample).build();
        }).collect(Collectors.toList());
    }

    // 获得该接口的响应参数示例
    private  List<ParamExample> getResponseParamExample (List<InterfacResponseParamDesc> responseParamDescList){
        return responseParamDescList.stream().map(responseParamDesc -> {
            String name = responseParamDesc.getName();
            String example = responseParamDesc.getExample();
            String type = responseParamDesc.getType();
            TypeConverter typeConverter = TypeConverterFactory.getTypeConverter(type);
            Object convertExample = typeConverter.convert(example);
            return ParamExample.builder().key(name).example(convertExample).build();
        }).collect(Collectors.toList());
    }
}




