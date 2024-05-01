package com.xiancai.lora.model.DTO;


import lombok.Data;

@Data
public class RenameGroupDTO {
    /**
     * 组群 id
     */
    private int groupId;
    /**
     * 新的组群名字
     */
    private String groupName;
}
