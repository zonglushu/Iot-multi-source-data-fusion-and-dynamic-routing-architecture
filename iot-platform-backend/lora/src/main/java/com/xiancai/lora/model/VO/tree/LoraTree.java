package com.xiancai.lora.model.VO.tree;

import com.xiancai.lora.model.entity.TreeNode;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LoraTree extends TreeNode {
    private Integer loraId;

    private String loraName;


}
