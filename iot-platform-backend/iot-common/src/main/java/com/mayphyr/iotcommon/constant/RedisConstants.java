package com.mayphyr.iotcommon.constant;

public class RedisConstants {


    public static final String EMAIL_LOGIN_CODE_KEY = "email:login:";

    /**
     * 注册邮箱验证码的key
     */
    public static final String EMAIL_REGISTER_CODE_KEY = "email:register:";

    /**
     * 修改密码的邮箱验证码的key
     */

    public static final String EMAIL_CHANGE_PASSWORD_CODE_KEY = "email:change_password:";

    /**
     * 邮箱验证码的过期时间
     */
    public static final Long EMAIL_CODE_TTL = 5L;

    public static final Long LOGIN_CODE_TTL = 2L;

    /**
     * 物联网用户登陆时，token的key
     */
    public static final String LOGIN_USER_KEY = "iot-login:token:";
    /**
     * 后端给硬件发消息的key
     */
    public static final String MQTT_MESSAGE = "mqtt:message:";

    /**
     * 用户登陆持续的时间
     */

    public static final Long LOGIN_USER_TTL = 3600L;




    /**
     * 存储每个用户操作的消息Id，便于操作，格式是：邮箱:mqtt:messageId:命令Id
     */
    public static final String MQTT_MESSAGE_ID=":mqtt:messageId:";

}
