package com.xiancai.lora.controller;

import cn.hutool.core.util.StrUtil;
import com.xiancai.lora.exception.BusinessException;
import com.xiancai.lora.model.DTO.MoveNode;
import com.xiancai.lora.model.DTO.SearchNode;
import com.xiancai.lora.model.DTO.SetNodeGPS;
import com.xiancai.lora.model.VO.GPSVo;
import com.xiancai.lora.model.entity.Node;
import com.xiancai.lora.service.NodeService;
import com.xiancai.lora.utils.Result;
import com.xiancai.lora.utils.UserHolder;
import com.xiancai.lora.utils.wrong.check.CheckNormalWrong;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.xiancai.lora.enums.StatusCode.NULL_ERR;
import static com.xiancai.lora.enums.StatusCode.PARAMS_ERR;

/**
 * 节点控制器
 */
@RestController
@RequestMapping("/node")
public class NodeController {
    @Autowired
    private NodeService nodeService;

    @Resource
    private CheckNormalWrong checkNormalWrong;

    @GetMapping("/nodenum")
    public Result getNodeNumber(){
        return nodeService.getNodeNumber();
    }

    @GetMapping("/nodeoffline")
    public Result getOfflineNodeNumber(){
        return nodeService.getOfflineNodeNumber();
    }

    @GetMapping("/searchgroup")
    public Result searchNodeGroup(@RequestParam("ids") String ids){
        checkNormalWrong.checkString(ids,"前端传入的ids错误");
        return nodeService.searchNodeGroup(ids);
    }

    /**
     * 通过多个参数查找节点
     * @param searchNode
     * @return
     */
    @PostMapping("/search")
    public Result searchNodeByArgs(@RequestBody SearchNode searchNode){

        return nodeService.searchNodeArgs(searchNode);
    }
    /**
     * 查找电量低于 10%的节点
     * @return
     */
    @GetMapping("/lowpower")
    public Result getLowPowerNodeNum(){
        return nodeService.getLowPowerNodeNum();
    }
    /**
     * 查找某个网关下的节点
     * @param loraId
     * @return
     */
    @GetMapping("/loranode")
    public Result searchNodeBehindNode(@RequestParam("loraid") Integer loraId){
        Integer userId = UserHolder.getUser().getId();
        if(loraId==null||loraId<0){
            throw new BusinessException(NULL_ERR.getMessage(), NULL_ERR.getCode(),"网关id错误");
        }
        return nodeService.searchNodeBehindNode(loraId,userId);
    }

    /**
     * 查找某个用户的所有节点
     * @return
     */
    @GetMapping("/nodelist")
    public Result searchNodeList(){
        Integer userId = UserHolder.getUser().getId();
        return nodeService.searchNodeList(userId);
    }

    /**
     * 将一个节点从一个网关移动到另一个网关
     * @param moveNode
     * @return
     */
    @PostMapping("/move")
    public Result moveNode(@RequestBody MoveNode moveNode){
        if(moveNode==null){
            throw new BusinessException(NULL_ERR.getMessage(), NULL_ERR.getCode(),"参数为空");
        }
        return nodeService.moveNode(moveNode);
    }
    /**
     * 查询某节点的详细信息
     * @param nodeId
     * @return
     */
    @GetMapping("/detail")
    public Result searchNodeDetail(@RequestParam("nodeid") Integer nodeId){
        checkNormalWrong.checkId(nodeId,"前端传入的nodeId错误");
        Integer userId = UserHolder.getUser().getId();
        return nodeService.searchNodeDetail(nodeId,userId);
    }

    /**
     * 重命名节点
     * @param nodeName
     * @param nodeId
     * @return
     */
    @GetMapping("/rename")
    public Result renameNode(@RequestParam("nodename")String nodeName,@RequestParam("nodeid") Integer nodeId){
        if(StrUtil.isBlank(nodeName)){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "节点名称错误");
        }
        if(nodeId==null||nodeId<0){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "节点ID错误");
        }
        Integer userId = UserHolder.getUser().getId();
        return nodeService.renameNode(nodeId,nodeName,userId);
    }

    /**
     * 修改节点的上报周期
     * @param reportCycle
     * @param nodeId
     * @return
     */
    @GetMapping("/modifycycle")
    public Result modifyCycle(@RequestParam("reportcycle")Integer reportCycle,@RequestParam("/nodeid") Integer nodeId){
        if(reportCycle==null||reportCycle<0){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "节点名称错误");
        }
        if(nodeId==null||nodeId<0){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "节点ID错误");
        }
        Integer userId = UserHolder.getUser().getId();
        return nodeService.modifyCycle(reportCycle,nodeId,userId);
    }

    /**
     * 获得节点的GPS
     * @param nodeId
     * @return
     */
    @GetMapping("/getgps")
    public Result getNodeGPS(@RequestParam("nodeid")Integer nodeId){
        if(nodeId==null||nodeId<=0){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "节点ID错误");
        }
        Integer userId = UserHolder.getUser().getId();
        return nodeService.getNodeGps(nodeId,userId);
    }

    /**
     * 设置节点的GPS
     * @param setNodeGPS
     * @return
     */
    @PostMapping("/setgps")
    public Result setNodeGPS(@RequestBody SetNodeGPS setNodeGPS){
        if(setNodeGPS==null){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "参数为null");
        }
        return nodeService.setNodeGPS(setNodeGPS);
    }

    @GetMapping("/test/gps")
    public Result getNodeGPSTest(){
        Integer userId = UserHolder.getUser().getId();
        List<Node> list = nodeService.query().eq("user_id", userId).list();
        List<GPSVo> gpsVos = list.stream().map(node -> {
            return GPSVo.builder().nodeName(node.getNodeName()).latitude(BigDecimal.valueOf(node.getLatitude())).longitude(BigDecimal.valueOf(node.getLongitude())).build();
        }).collect(Collectors.toList());
        return Result.success(gpsVos);
    }

}
