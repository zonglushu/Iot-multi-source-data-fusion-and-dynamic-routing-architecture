package com.xiancai.lora.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {
    public static String getMethodName(String str) {
        char[] cs=str.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }

    //时间戳转换日期方法
    public static String stampToDate(String value) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(value);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static Date stampToDateObject(String value) {
        String time = stampToDate(value);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=null;
        try {
            date=simpleDateFormat.parse(time);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }


}
