package com.raising.forward.controller.progressManage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.common.BaseController;
import com.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/raising/forward/progressManage/progressAnalysis")
public class ProgressAnalysisController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ProgressAnalysisController.class);

    @RequestMapping(value = "getRows.do",method = RequestMethod.POST)
    public JSONObject getRows(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject paramJson = JSONObject.parseObject(ajaxParam);
        try {
            returnInfo = progressAnalysisService.getRows(paramJson);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return returnInfo;
    }
}
