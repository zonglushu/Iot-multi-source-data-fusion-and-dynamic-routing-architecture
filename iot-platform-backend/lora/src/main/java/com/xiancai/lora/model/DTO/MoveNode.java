package com.xiancai.lora.model.DTO;

import lombok.Data;

@Data
public class MoveNode {
    /**
     * 被移动的节点id
     */
    private Integer moveNodeId;
    /**
     * 将要移动到的节点id
     */
    private Integer newNodeId;
}
