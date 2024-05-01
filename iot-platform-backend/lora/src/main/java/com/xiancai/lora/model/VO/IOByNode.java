package com.xiancai.lora.model.VO;

import com.xiancai.lora.model.entity.Io;
import com.xiancai.lora.model.entity.Module;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class IOByNode {
    private String nodeName;

    private List<Io> IOList;
}
