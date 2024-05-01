package com.xiancai.lora.constant;

public class RedisConstants {
    public static final String EMAIL_LOGIN_CODE_KEY = "email:login:";

    public static final String EMAIL_REGISTER_CODE_KEY = "email:register:";


    public static final String EMAIL_CHANGE_PASSWORD_CODE_KEY = "email:change_password:";

    public static final Long EMAIL_CODE_TTL = 5L;

    public static final Long LOGIN_CODE_TTL = 2L;
    public static final String LOGIN_USER_KEY = "login:token:";
    public static final String MQTT_MESSAGE = "mqtt:message:";
    public static final Long LOGIN_USER_TTL = 3600L;
    /**
     * 存储每个用户操作的消息Id，便于操作，格式是：邮箱:mqtt:messageId:命令Id
     */
    public static final String MQTT_MESSAGE_ID=":mqtt:messageId:";

}
