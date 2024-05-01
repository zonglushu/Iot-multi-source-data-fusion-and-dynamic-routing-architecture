package com.xiancai.lora.model.entity;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Tree {
    private List<List<TreeNode>> nextNodes;

    public Tree() {
        nextNodes=new ArrayList<>();
    }
    public void add(List<TreeNode> nextNodes){
        this.nextNodes.add(nextNodes);
    }
}
