package com.raising.forward.controller.guidance;


import com.alibaba.fastjson.JSONObject;
import com.common.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 导向管理Controller
 */
@RestController
@RequestMapping("/raising/forward/guidance/guidanceManagement")
public class GuidanceManagementController extends BaseController {

    /*
     * 获得导向管理数据
     * @param ajaxParam
     * @return
     */
    @RequestMapping(value = "/getGuidanceData.do",method = RequestMethod.GET)
    public JSONObject getGuidanceData(String ajaxParam){
        JSONObject tbm = tbmResumeService.getRowWithProjectId(Integer.parseInt(ajaxParam));
        String hingeType = tbm.getString("hingeType");
        Integer ringTotal = tbm.getInteger("ringTotal");
        JSONObject guidanceData = guidanceManagementService.getGuidanceData(ajaxParam,hingeType,ringTotal);
        return guidanceData;
    }


}
