package com.mayphyr.iotcommon.model.vo;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InfaRequestParamDescVO {

    private String key;

    private List<ParamDesc> value;
}
