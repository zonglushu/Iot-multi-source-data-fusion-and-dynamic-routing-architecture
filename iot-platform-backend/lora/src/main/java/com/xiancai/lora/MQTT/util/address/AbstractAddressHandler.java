package com.xiancai.lora.MQTT.util.address;



import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Map;

/**
 * 构造地址的模板方法
 */
public  abstract class AbstractAddressHandler {
    /**
     * 给硬件的地址
     * @param nodeId
     * @return
     */
    public String produceAddress(Integer nodeId){
        //先是把要所有的节点id找到
        LinkedList<String> address = findAddress(nodeId);
        //然后拼成串
        String normalAddress = combineAddress(address);
        //然后转Base64
        return normalAddress;
    }

    /**
     * 解析硬件传来的地址，先把Base64的编码转换为正常格式，再进行拆分
     * @param address
     * @return
     */
    public abstract Map<String,String> parseHardWareAddress(String address);


    /**
     * 要给硬件的地址，要先找到前几个设备的id/ids/admin,
     * 因为最终执行命令的都是节点，所以我们只要找节点就可以了
     * 参数给的是最终的那个节点id,我们要往上找
     */
    public abstract LinkedList<String> findAddress(Integer nodeId);

    public abstract String combineAddress(LinkedList<String> addresses);

    /**
     * 转换为 Base64编码的格式
     * @param address
     * @return
     */
    public String toBase64(String address){
        String base64Address = Base64.getEncoder().encodeToString(address.getBytes(StandardCharsets.UTF_8));
        while (base64Address.length()<16){
            base64Address='A'+base64Address;
        }
        return base64Address;
    }

    /**
     * 将硬件传来的base64的地址转换为正常的
     * @param base64
     * @return
     */
    public String parseBase64(String base64){
        String removeAString = removeA(base64);
        return new String(Base64.getDecoder().decode(removeAString));

    }
//    AAAAABSADSDSDAAA
    //AAADSDSDASBAAAAAA
    //以不确定的长度的A作为前缀
    private String removeA(String normalAddress){
        if(normalAddress.charAt(0)!='A'){
            return normalAddress;
        }
        char[] chars = normalAddress.toCharArray();
        int i=0;
        for (  ; i < chars.length; i++) {
            if(chars[i]!='A') break;
        }
        String substring = normalAddress.substring(i);
        return substring;

    }

}
