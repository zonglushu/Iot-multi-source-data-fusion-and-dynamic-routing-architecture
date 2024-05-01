package com.xiancai.lora.model.DTO;

import lombok.Data;

@Data
public class SearchLora {
    private String ids;

    private String onlineStatus;

    private String[] bindTime;

    private String[] updateTime;
}
