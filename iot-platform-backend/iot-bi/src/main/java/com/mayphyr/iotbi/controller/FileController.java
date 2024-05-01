package com.mayphyr.iotbi.controller;



import com.mayphyr.iotbi.entity.dto.chart.GenChartByAiRequest;
import com.mayphyr.iotbi.entity.vo.BIResponse;
import com.mayphyr.iotbi.manger.AIManger;
import com.mayphyr.iotcommon.common.Result;
import com.mayphyr.iotcommon.enums.StatusCode;
import com.mayphyr.iotcommon.exception.BusinessException;
import com.mayphyr.iotcommon.exception.ThrowUtils;
import com.mayphyr.iotcommon.utils.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Arrays;

/**
 * 文件接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

//    @Resource
//    private UserService userService;
//
//    @Resource
//    private CosManager cosManager;


    @Resource
    private AIManger aiManger;

    /**
     * 文件上传
     *
     * @param multipartFile
     * @param uploadFileRequest
     * @param request
     * @return
     */
    @PostMapping("/gen")
    public Result<BIResponse> genChartByAi(@RequestPart("file") MultipartFile multipartFile,
                                     GenChartByAiRequest uploadFileRequest, HttpServletRequest request) {

//      获取前端传入的参数
        String name = uploadFileRequest.getName();
        String goal = uploadFileRequest.getGoal();
        String chartType = uploadFileRequest.getChartType();

//       构造用户输入:  要喂给 AI 的数据：系统预设（提前告诉AI职责，功能，回复格式等要求）+ 分析目标 + 压缩后的数据
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");
        userInput.append(goal).append("\n");
        userInput.append("原始数据：").append("\n");
//      分别对参数进行校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal),StatusCode.PARAMS_ERR,"目标为空");
//      处理Excel,获得压缩后的数据
        String fileString = ExcelUtil.excelToCsv(multipartFile);
        userInput.append("我的数据：").append(fileString).append("\n");

        long biModelId=1678976265899597826l;
        String resultStr = aiManger.doChat(biModelId, userInput.toString());

        String[] splits = resultStr.split("【【【【【");
        if(splits.length<3){
            throw new BusinessException(StatusCode.ERROR,"AI生成错误");
        }
        String genChart = splits[1].trim();
        String genResult = splits[2].trim();
        // 插入到数据库

        BIResponse result = BIResponse.builder().genChart(genChart).genResult(genResult).chartId(1l).build();
        return Result.success(result);

    }

    /**
     * 校验文件
     *
     * @param multipartFile
     * @param fileUploadBizEnum 业务类型
     */
//    private void validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum) {
//        // 文件大小
//        long fileSize = multipartFile.getSize();
//        // 文件后缀
//        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
//        final long ONE_M = 1024 * 1024L;
//        if (FileUploadBizEnum.USER_AVATAR.equals(fileUploadBizEnum)) {
//            if (fileSize > ONE_M) {
//                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 1M");
//            }
//            if (!Arrays.asList("jpeg", "jpg", "svg", "png", "webp").contains(fileSuffix)) {
//                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
//            }
//        }
//    }
}
