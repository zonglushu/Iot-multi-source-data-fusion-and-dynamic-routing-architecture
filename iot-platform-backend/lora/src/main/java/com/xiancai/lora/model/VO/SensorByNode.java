package com.xiancai.lora.model.VO;

import com.xiancai.lora.model.entity.Module;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SensorByNode {
    private String nodeName;

    private List<Module> moduleList;
}
