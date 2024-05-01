package com.xiancai.lora.utils.wrong.entity;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class WrongChecker<T> {
    public void checkWrong(T t){
        Map<String, Object> map = BeanUtil.beanToMap(t);

    }

}
