package com.xiancai.lora.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

    @TableField(exist = false)
    public List<TreeNode> nextNodes;

    public TreeNode() {
        this.nextNodes=new ArrayList<>();
    }

    public void addAll(List< ? extends TreeNode> treeNodes){
        this.nextNodes.addAll(treeNodes);
    }
}
