package com.xiancai.lora.utils;


import com.xiancai.lora.enums.StatusCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel(value = "返回类型",description = "这是后端响应的数据类型，不管请求失败还是成功都会返回该类型的数据")
public class Result<T> implements Serializable {
    /**
     * 返回状态码
     * */
    @ApiModelProperty(value="响应状态码")
    private Integer code;
    /**
     * 返回信息
     */
    @ApiModelProperty(value = "返回信息")
    private String message;
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回的数据，前端需要数据可以从这里面拿")
    private T data;
    /**
    * 生成Result的方法，在下面的success和fail方法中都会用到
    */

    private static <T> Result<T> createResult(Integer code,T data,  String message){
        Result<T> apiResult = new Result<>();
        apiResult.setData(data);
        apiResult.setCode(code);
        apiResult.setMessage(message);
        return apiResult;
    }

    public static <T> Result<T> no_permissions(T data){
        return createResult(StatusCode.NO_PERMISSIONS.getCode(),data,StatusCode.NO_PERMISSIONS.getMessage());
    }
    /**
     * 下面是成功时调用的方法
     * */

    public static <T> Result<T> success(T data ){
        return createResult(StatusCode.SUCCESS.getCode(),data, StatusCode.SUCCESS.getMessage());
    }
//    public static <T> Result<T> select_success(T data){
//        return createResult(StatusCode.SELECT_OK.getCode(),data, StatusCode.SELECT_OK.getMessage());
//    }
//    public static <T> Result<T> select_notFound(T data){
//        return createResult(StatusCode.SELECT_NOTFOUND.getCode(),data,StatusCode.SELECT_NOTFOUND.getMessage());
//    }
//
//    public static <T> Result<T> update_success(T data ){
//        return createResult(StatusCode.UPDATE_OK.getCode(),data, StatusCode.UPDATE_OK.getMessage());
//    }
//    public static <T> Result<T> delete_success(T data ){
//        return createResult(StatusCode.DELETE_OK.getCode(),data, StatusCode.DELETE_OK.getMessage());
//    }
//    public static <T> Result<T> login_success(T data ){
//        return createResult(StatusCode.LOGIN_OK.getCode(),data, StatusCode.LOGIN_OK.getMessage());
//    }
//    public static <T> Result<T> currentUserSuccess(T data ){
//        return createResult(StatusCode.CURRENT_USER.getCode(),data, StatusCode.CURRENT_USER.getMessage());
//    }
    /**
     * 下面是失败调用的方法
     * */

//    public static <T> Result<T> save_fail(T data){
//        return createResult(StatusCode.FAIL.getCode(),data, StatusCode.SAVE_ERR.getMessage());
//    }
//    public static <T> Result<T> select_fail(T data){
//        return createResult(StatusCode.SELECT_ERR.getCode(),data, StatusCode.SELECT_ERR.getMessage());
//    }
//
//    public static <T> Result<T> update_fail(T data){
//        return createResult(StatusCode.UPDATE_ERR.getCode(),data, StatusCode.UPDATE_ERR.getMessage());
//    }
//    public static <T> Result<T> delete_fail(T data ){
//        return createResult(StatusCode.DELETE_ERR.getCode(),data, StatusCode.DELETE_ERR.getMessage());
//    }
    public static <T> Result<T> currentUserFail(T data ){
        return createResult(StatusCode.NO_CURRENT_USER.getCode(),data, StatusCode.NO_CURRENT_USER.getMessage());
    }


//    public static <T> Result<T> login_fail(T data ){
//        return createResult(StatusCode.LOGIN_ERR.getCode(),data, StatusCode.LOGIN_ERR.getMessage());
//    }
}
