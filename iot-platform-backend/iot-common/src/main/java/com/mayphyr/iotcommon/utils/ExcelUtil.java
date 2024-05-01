package com.mayphyr.iotcommon.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.sun.deploy.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.util.StringUtil;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Excel 相关工具类
 */

@Slf4j
public class ExcelUtil {

    public static String excelToCsv(MultipartFile multipartFile)  {
////        读取本地的文件
//        File file = null;
//        try {
//            file = ResourceUtils.getFile("classpath:网站数据.xlsx");
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
        // 读取 excel的数据
        List<Map<Integer, String>> list = null;
        try {
            list = EasyExcel.read(multipartFile.getInputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            log.error("表格处理错误",e);
            throw new RuntimeException(e);
        }
        // 判断是否为空
        if(CollUtil.isEmpty(list)){
            return "";
        }

        // 转换为 csv 格式
        StringBuilder fileString = new StringBuilder();
        // 读取表头
        LinkedHashMap<Integer, String> headMap = (LinkedHashMap) list.get(0);
        // 过滤掉为null的列
        List<String> headerList = headMap.values().stream().filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
        fileString.append(StringUtils.join(headerList,",")).append("\n");
        // 读取后面的数据
        for (int i = 1; i <list.size() ; i++) {
            LinkedHashMap<Integer, String> dataMap = (LinkedHashMap) list.get(i);
            List<String> dataList = dataMap.values().stream().filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
            fileString.append(StringUtils.join(dataList,",")).append("\n");
        }

        System.out.println(fileString.toString());
        return fileString.toString();
    }

    public static void main(String[] args) {
        excelToCsv(null);
    }
}
