package com.mayphyr.iotcommon.model.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class ParamExample {

    private String key;

    private Object example;




}
