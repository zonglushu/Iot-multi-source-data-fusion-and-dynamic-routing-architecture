package com.xiancai.lora.utils.wrong.check;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.utils.wrong.check.special.CheckSpecialContext;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xiancai.lora.enums.StatusCode.FAIL;
import static com.xiancai.lora.enums.StatusCode.PARAMS_ERR;

@Component
public class CheckNormalWrong {

    @Resource
    private CheckSpecialContext checkSpecialContext;
    protected static void throwException(String description) {
        throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), description);
    }

    public void isNULL(Object obj ,String description) {
        if (obj == null) {
            throwException(description+"为null");
        }
    }

    public    void checkString(String checkedString, String description) {
        if (StrUtil.isBlank(checkedString)) {
            throwException(description);
        }
    }

    public void checkId(Integer id, String description) {
        if (id==null||id<=0) {
            throwException(description);
        }
    }

    /**
     * 为了多选一的 eq 要返回 boolean值
     * @param checkedString
     * @return
     */
    public  boolean checkString(String checkedString) {
        return !StrUtil.isBlank(checkedString);
    }

    public  boolean  checkId(Integer id) {
        if (id==null||id<=0) {
            return false;
        }
        return true;
    }

    public   void checkUser(Integer deviceUserId, Integer userId) {
        checkId(deviceUserId, "设备id错误");
        checkId(userId, "用户id错误");
        if (!deviceUserId.equals(userId)) {
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "该设备是他人的，您无法操作");
        }
    }

    private   void checkNodeStatus(String status) {
        if (!status.equals("在线")) {
            throwException("该节点未开启");
        }
    }

    public void checkList(List list,String description){
        if(list==null||list.isEmpty()){
            throwException(description);
        }
    }

    public  void checkWrong(Object obj) {
        isNULL(obj,"前端传入的参数");
        Map<String, Object> map = BeanUtil.beanToMap(obj);
        check(map);
    }

    public void checkWrongIgnoreSome(Object obj,String... strings){
        Map<String, Object> map = BeanUtil.beanToMap(obj,new HashMap<>(),
                new CopyOptions().setIgnoreProperties(strings));
        check(map);
    }

    private void check(Map<String, Object> map){
        map.forEach((key,value)->{
            isNULL(value,key);
            if (value instanceof Integer) {
                checkInteger((Integer) value, value + "错误");
            }
            if (value instanceof String) {
                checkString((String) value, value + "错误");
            }
            checkSpecialContext.checkWrong(key,value);
        });
    }

    public void checkBool(boolean is,String description){
        if(!is){
            throwException(description);
        }
    }
    public void checkInteger(Integer integer,String description){
        if(integer==null){
            throwException(description);
        }
    }

}

