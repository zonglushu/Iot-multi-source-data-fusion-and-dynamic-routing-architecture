package com.xiancai.lora.model.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.models.auth.In;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SensorVo {

    /**
     * 其实是module 中 type为 sensor的名字
     */
    private String sensorName;
    /**
     *
     */
    private String sensorType;
    /**
     * module的ids
     */
    private String ids;
    /**
     * module的port
     */
    private Integer port;
    /**
     * module的上层节点id
     */
    private Integer nodeId;
    /**
     * module的状态
     */
    private String moduleStatus;




}
