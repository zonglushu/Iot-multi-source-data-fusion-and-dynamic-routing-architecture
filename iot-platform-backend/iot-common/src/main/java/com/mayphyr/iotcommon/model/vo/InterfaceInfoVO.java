package com.mayphyr.iotcommon.model.vo;


import com.mayphyr.iotcommon.model.entity.InterfaceInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * 这个只是用来
 */
@Data
public class InterfaceInfoVO {
    /**
     * 自增id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 接口描述
     */
    private String description;
    /**
     * 接口主机
     */
    private String host;

    /**
     * 接口地址
     */
    private String url;



    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 请求类型（GET,POST）
     */
    private String method;

    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 响应头示例
     * {"Content":"application/json"}
     */
    private String responseHeaderExample;


    /**
     * 请求头示例
     {"Content":"application/json"}
     */
    private String requestHeaderExample;


    /**
     * 请求参数示例
     {"name":"John Doe","age":30,"email":"john@example.com"}
     */
    private  List<ParamExample>requestParamExample;

    /**
     * 响应参数示例
     * {"name":"John Doe","age":30,"email":"john@example.com"}
     */
    private  List<ParamExample> responseParamExample;


    /**
     * 
     * 请求参数描述
     */
    private List<InfaRequestParamDescVO> requestParamDesc;


    /**
     * 响应参数描述
     */
    private List<InfaRequestParamDescVO> responseParamDesc;

    /**
     * 接口项目id
     */
    private Long projectId;

    public static InterfaceInfoVO poToVo(InterfaceInfo interfaceInfo) {
        InterfaceInfoVO vo = new InterfaceInfoVO();
        BeanUtils.copyProperties(interfaceInfo, vo);
        return vo;
    }

    public static InterfaceInfo voToPo(InterfaceInfoVO interfaceInfoVO) {
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoVO, interfaceInfo);
        return interfaceInfo;
    }
}
