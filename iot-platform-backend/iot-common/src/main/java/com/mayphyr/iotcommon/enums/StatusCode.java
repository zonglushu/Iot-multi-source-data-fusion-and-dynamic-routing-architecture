package com.mayphyr.iotcommon.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
/**
 * 生成一个参数为所有实例变量的构造方法
 * */
@AllArgsConstructor
public enum StatusCode {
     NULL_ERR(401,"对象为空"),
     CURRENT_USER(200,"已有用户登录"),

     NO_CURRENT_USER(401,"未有用户登录"),

    /**
     * 状态码以1结尾表示成功，以0结尾表示失败,以2结尾表示没查询到
     * */
     NO_PERMISSIONS(403,"该用户没有权限访问该方法"),
     NO_AUTH(401,"该用户不是管理员"),
    /**
     * 操作成功时调用的状态码
     */
     SUCCESS(200,"操作成功"),
      FAIL(201,"操作失败"),
    ERROR(400,"发生错误"),

//    /**
//     * 添加成功
//     */
//    SAVE_OK(20001, "添加成功"),
//    /**
//     * 查询成功
//     */
//    SELECT_OK(20011, "查询成功"),
//    /**
//     * 登陆成功
//     */
//    LOGIN_OK(20061,"登陆成功"),
//    /**
//     * 登陆失败
//     */
//    LOGIN_ERR(20060,"登陆失败"),
    /**
     * 未登陆
     */
    NO_LOGIN(401,"未登录"),
//    /**
//     * 更新成功
//     */
//    UPDATE_OK(20021, "更新成功"),
//    /**
//     * 删除成功
//     */
//    DELETE_OK(20031, "删除成功"),
//    /**
//     * 添加失败
//     */
//    SAVE_ERR(20000, "添加失败"),
//    /**
//     * 查询失败
//     */
//    SELECT_ERR(20010, "查询失败"),
//    /**
//     * 更新失败
//     */
//    UPDATE_ERR(20020, "更新失败"),
//    /**
//     * 删除失败
//     */
//    DELETE_ERR(20030, "删除失败"),
//    /**
//     * 查询成功，但未查到
//     */
//    SELECT_NOTFOUND(20002,"未查到，请检查查询条件"),
    /**
     * 系统错误
     */
    SYSTEM_ERR(404,"系统内部异常！"),
    /**
     * 参数错误错误
     */
    PARAMS_ERR(401,"参数错误");


    /**
     * 状态码
     */

    private final Integer code;

    /**
     * 描述
     */
    private final String message;

}
