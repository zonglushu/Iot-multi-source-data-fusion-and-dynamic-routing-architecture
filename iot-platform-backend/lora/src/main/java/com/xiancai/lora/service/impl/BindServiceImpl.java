package com.xiancai.lora.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.model.DTO.BindDTO;
import com.xiancai.lora.model.entity.Bind;
import com.xiancai.lora.service.BindService;
import com.xiancai.lora.mapper.BindMapper;
import com.xiancai.lora.service.NodeService;
import com.xiancai.lora.utils.Result;
import com.xiancai.lora.utils.wrong.check.CheckNormalWrong;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


import static com.xiancai.lora.enums.StatusCode.PARAMS_ERR;
import static com.xiancai.lora.enums.StatusCode.SYSTEM_ERR;

/**
* @author 86156
* @description 针对表【bind】的数据库操作Service实现
* @createDate 2022-11-10 21:17:18
*/
@Service
public class BindServiceImpl extends ServiceImpl<BindMapper, Bind>
    implements BindService{
    @Resource
    private NodeService nodeService;

    @Resource
    private CheckNormalWrong checkNormalWrong;
    @Override
    public Result setBind(BindDTO bindDTO) {
        //新买的设备会先放在bind表中
        String ids = bindDTO.getIds();
        Long idsCount = this.query().eq("ids", ids).count();
        //如何查询到的idsCount为0，说明这个设备还没买
        if(idsCount==0){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "该ids不存在,请检查购买信息");
        }
        String code = bindDTO.getCode();
        QueryWrapper<Bind> wrapper = new QueryWrapper<>();
        wrapper.eq("ids",ids).eq("code",code);
        Bind one = this.getOne(wrapper);
        //如果查询到的数据为null,说明这个ids和code不匹配
        if(one==null){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "该code与ids不匹配");
        }
        //如果之前就已经被激活了，那么就不能再激活
        if(one.getStatus().equals("已激活")){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "该设备已被激活");
        }
        //去往数据库操作，由于涉及两个表了，要用事务锁一下。
        setBind(bindDTO,one);
        return Result.success(true);
    }

    @Transactional(rollbackFor = Exception.class)
    public void setBind(BindDTO bindDTO,Bind one){
        //如果前面这两个查询都做完了，那么就说明用户已经买了这个装备了，并且信息也匹配上了，就要改变状态了
        one.setStatus("已激活");
        boolean update = this.updateById(one);
        if(update==false){
            throw new BusinessException(SYSTEM_ERR.getMessage(), SYSTEM_ERR.getCode(), "数据库更新失败");
        }
        // 该往node里面添加信息了
        nodeService.bindUpdate(bindDTO);
    }

    @Override
    public Result relieveBind(Integer nodeId) {

        return null;
    }
}




