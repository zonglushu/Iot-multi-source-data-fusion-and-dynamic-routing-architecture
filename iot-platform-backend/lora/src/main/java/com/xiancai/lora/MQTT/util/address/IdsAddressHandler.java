package com.xiancai.lora.MQTT.util.address;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiancai.lora.model.entity.Node;
import com.xiancai.lora.service.NodeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
public class IdsAddressHandler extends AbstractAddressHandler {

    @Resource
    private NodeService nodeService;

    /**
     * 这里ids地址处理器接收的地址都是ids
     * @param Base64Address
     * @return
     */
    @Override
    public Map<String, String> parseHardWareAddress(String Base64Address) {
        //先是把Base64转换为普通字符串
//        String address = parseBase64(Base64Address);
        Map<String,String> map=new HashMap<>();
        //先进行@分割，把port给分开
        String[] adr = Base64Address.split("@");
        if(adr.length==2){
            map.put("port",adr[1]);
        }
        String[] adrS = adr[0].split("#");
        map.put("ids",adrS[adrS.length-1]);
        return map;
    }

    @Override
    public LinkedList<String> findAddress(Integer nodeId) {
        // 前端传过来的是直接执行命令的nodeId
        LinkedList<String> addressQueue = new LinkedList<>();
        // 找到这个直接执行命令的节点的上一级网关Id
        Node node = nodeService.getById(nodeId);
        Integer loraId = node.getLoraId();
        // loraId是-1说明已经到一级节点了
        addressQueue.add(node.getIds()+"");
        if (loraId != -1) {
            List<String> list = new ArrayList<>();
            process(loraId, list);
            addressQueue.addAll(list);
        }
        return addressQueue;

    }

    @Override
    public String combineAddress(LinkedList<String> addresses) {
        StringBuilder s= new StringBuilder();
        while (!addresses.isEmpty()){
            s.append("#").append(addresses.pollLast());
        }
        return s.toString();
    }

    /**
     *
     * @param loraId 是直接执行命令的节点
     * @param list
     */
    private void process(Integer loraId, List<String> list){
        //如果队列中由两个或者找到了一级网关,就说明到头了
        if(list.size()==2||loraId==-1){
            return;
        }
        //拿出来后面的id,找到这个id的节点的lora_id,看他的上一级节点是谁
        Node node = nodeService.getOne(new QueryWrapper<Node>().eq("is_lora", loraId));
        String beforeAddress = node.getIds();
        list.add(beforeAddress);
        process(node.getLoraId(),list);
    }
}
