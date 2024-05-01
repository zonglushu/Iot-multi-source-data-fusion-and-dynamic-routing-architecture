package com.xiancai.lora.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiancai.lora.model.VO.IOByNode;
import com.xiancai.lora.model.VO.SensorByNode;
import com.xiancai.lora.model.entity.Io;
import com.xiancai.lora.model.entity.Module;
import com.xiancai.lora.model.entity.Node;
import com.xiancai.lora.service.IoService;
import com.xiancai.lora.mapper.IoMapper;
import com.xiancai.lora.service.NodeService;
import com.xiancai.lora.utils.Result;
import com.xiancai.lora.utils.wrong.check.CheckNormalWrong;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
* @author 86156
* @description 针对表【IO】的数据库操作Service实现
* @createDate 2023-05-12 17:09:50
*/
@Service
public class IoServiceImpl extends ServiceImpl<IoMapper, Io>
    implements IoService{

    @Resource
    private NodeService nodeService;

    @Resource
    private CheckNormalWrong checkNormalWrong;
    @Override
    public Result getIOByNode(Integer userId) {
        List<IOByNode> ioByNodes = new ArrayList<>();
        List<Node> userNodes = nodeService.query().eq("user_id", userId).list();
        boolean isNull = ObjectUtil.isNull(userNodes);
        if(isNull){
            return Result.success(Collections.emptyList());
        }else {
            userNodes.forEach(node -> {
                checkNormalWrong.isNULL(node,"node");
                List<Io> ios = this.query().eq("node_id", node.getId()).list();
                IOByNode ioByNode = IOByNode.builder().nodeName(node.getNodeName()).IOList(ios).build();
                ioByNodes.add(ioByNode);
            });
        }
        return Result.success(ioByNodes);
    }
}




