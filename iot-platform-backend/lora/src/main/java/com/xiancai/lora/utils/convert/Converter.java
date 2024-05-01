package com.xiancai.lora.utils.convert;
import com.xiancai.lora.model.VO.data.DataVo;

import com.xiancai.lora.model.entity.DataS;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Converter {


    public static List<DataVo> getDataVo(List<DataS> list,String name){
        return list.stream().map(dataS ->
             DataVo.builder().sensorName(name).data(dataS.getData()).dataTime(dataS.getDataTime()).build()
        ).collect(Collectors.toList());
    }

    public static <T> List<T> castList(Object obj, Class<T> clazz)
    {
        List<T> result = new ArrayList<T>();
        if(obj instanceof List<?>)
        {
            for (Object o : (List<?>) obj)
            {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }






}
