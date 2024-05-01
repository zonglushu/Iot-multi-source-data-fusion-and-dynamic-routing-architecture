package com.xiancai.lora.model.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data

public class UserVo implements Serializable {
    /**
     *
     */
    private Integer id;
    /**
     *
     */
    private String username;

    /**
     *
     */
    private String phone;
    /**
     *
     */
    private String company;
    /**
     *
     */
    private String website;

    private Integer parentId;

    private String email;

    private static final long serialVersionUID = 1L;
}
