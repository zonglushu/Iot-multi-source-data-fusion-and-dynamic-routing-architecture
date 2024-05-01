package com.xiancai.lora.model.DTO;

import io.swagger.models.auth.In;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchSensor {
    /**
     * ids 和 nodeId二选一
     */
    private String ids;

//    private String onlineStatus;

    private Integer nodeId;
}
