package com.mayphyr.iotcommon.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户视图（脱敏）
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class IotUserVo implements Serializable {

    /**
     * id
     */
    private Long id;


    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户昵称
     */
    private String name;

    /**
     * 用户头像
     */
    private String avatar;


    /**
     * 用户角色：user/admin/ban
     */
    private Integer role;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}