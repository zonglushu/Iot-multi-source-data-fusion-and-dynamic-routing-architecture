package com.xiancai.lora.model.VO.tree;

import com.xiancai.lora.model.entity.TreeNode;
import lombok.Data;

@Data
public class ModuleNode extends TreeNode {
    private String moduleName;

    private String moduleType;

    private Integer port;

    private Integer detailId;
}
