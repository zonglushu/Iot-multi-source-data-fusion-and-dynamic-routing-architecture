package com.xiancai.lora.model.DTO;

import lombok.Data;

@Data
public class GroupMoveDTO {
    private Integer loraId;

    private Integer oldGroupId;

    private Integer newGroupId;
}
