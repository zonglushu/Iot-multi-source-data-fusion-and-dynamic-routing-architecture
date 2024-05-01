package com.mayphyr.iotcommon.model.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Builder
public class ParamDesc {

    private String key;

    private Object value;


    public static ParamDesc getMustParamDesc(int isMust) {
        return ParamDesc.builder().key("must").value(isMust).build();
    }

    public static ParamDesc getTypeParamDesc(String type){
        return ParamDesc.builder().key("type").value(type).build();
    }


    public static ParamDesc getDescParamDesc(String description){
        return ParamDesc.builder().key("description").value(description).build();
    }

    public static List<ParamDesc> getParamDescList(ParamDesc... paramDescs){
        return Arrays.stream(paramDescs).collect(Collectors.toList());
    }


}
