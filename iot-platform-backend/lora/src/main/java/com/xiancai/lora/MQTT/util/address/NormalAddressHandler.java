package com.xiancai.lora.MQTT.util.address;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.model.entity.Node;
import com.xiancai.lora.service.NodeService;
import com.xiancai.lora.service.impl.NodeServiceImpl;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

import static com.xiancai.lora.enums.StatusCode.SYSTEM_ERR;


@Component
public class NormalAddressHandler extends AbstractAddressHandler {
    //因为这里的NormalAddress是反射new出来的，是用newInstance默认构造器造出来的，所以这个类为被

    @Resource
    private NodeService nodeService;




    @Override
    public LinkedList<String> findAddress(Integer nodeId) {
        // 前端传过来的是直接执行命令的nodeId
        LinkedList<String> addressQueue = new LinkedList<>();
        addressQueue.add(nodeId+"");
        // 找到这个直接执行命令的节点的上一级网关Id
        Integer loraId = nodeService.getById(nodeId).getLoraId();
        List<String> list=new ArrayList<>();
        process(loraId,list);
        addressQueue.addAll(list);
        return addressQueue;
    }

    /**
     * 递归得到地址
     * @param loraId
     * @param list
     */
    private void process(Integer loraId,List<String> list){
        //如果队列中由两个或者找到了一级网关,就说明到头了
        if(list.size()==2||loraId==-1){
            return;
        }
        //拿出来后面的id,找到这个id的节点的lora_id,看他的上一级节点是谁
        Node node = nodeService.getOne(new QueryWrapper<Node>().eq("is_lora", loraId));
        Integer beforeAddress = node.getId();
        list.add(beforeAddress + "");
        process(node.getLoraId(),list);
    }

    @Override
    public String combineAddress(LinkedList<String> addresses) {
        StringBuilder s= new StringBuilder();
        while (!addresses.isEmpty()){
            s.append("$").append(addresses.pollLast());
        }
        return s.toString();
    }

    @Override
    public Map<String, String> parseHardWareAddress(String Base64Address) {
        //先是把Base64转换为普通字符串
//        String address = parseBase64(Base64Address);
        Map<String,String> map=new HashMap<>();
        String[] adr = Base64Address.split("@");
        if(adr.length==2){
            map.put("port",adr[1]);
        }
        String[] split = adr[0].split("$");
        Node one = nodeService.query().eq("id", split[split.length - 1]).one();
        if(one==null){
            throw new BusinessException(SYSTEM_ERR.getMessage(),SYSTEM_ERR.getCode(),"执行命令的节点不存在");
        }
        map.put("ids",one.getIds());
        return map;
    }
}
