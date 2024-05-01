package com.mayphyr.iotdecider.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mayphyr.iotcommon.model.dto.interfaceinfo.InterfaceInfoQueryDTO;
import com.mayphyr.iotcommon.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mayphyr.iotcommon.model.vo.InterfaceInfoVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author mayphyr
* @description 针对表【interface_info】的数据库操作Service
* @createDate 2023-07-22 16:15:30
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryDTO interfaceInfoQueryDTO);

    Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage);
}
