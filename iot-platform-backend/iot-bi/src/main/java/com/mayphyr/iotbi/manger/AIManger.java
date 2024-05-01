package com.mayphyr.iotbi.manger;


import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AIManger {

    @Resource
    private YuCongMingClient AIClient;

    /**
     * AI对话
     * @param message
     * @return
     */
    public String doChat(long modelId,String message){
//        1678976265899597826l   智能BI模型的模型ID
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(modelId);
        devChatRequest.setMessage(message);
        BaseResponse<DevChatResponse> response = AIClient.doChat(devChatRequest);
        return response.getData().getContent();

    }
}
