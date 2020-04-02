package com.raising.forward.controller.progressManage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.common.BaseController;
import com.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static com.baosight.common.utils.DateUtils.date2String;

/**
 * 当前进度
 */
@Controller("currentProgressController")
@RequestMapping("/raising/forward/progressManage/currentProgress")
public class CurrentProgressController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(CurrentProgressController.class);

    @RequestMapping(value = "getRows.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getRows(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject paramJson = JSONObject.parseObject(ajaxParam);
        Integer projectId = paramJson.getInteger("projectId");
        JSONObject getWorkPlanParam = new JSONObject();
        JSONObject inqu_status = new JSONObject();
        inqu_status.put("projectId",projectId);
        getWorkPlanParam.put("inqu_status",inqu_status);
        getWorkPlanParam.put("curPage",1);
        getWorkPlanParam.put("curRowNum",1000);
        JSONObject workPlan = workPlanService.getRows(getWorkPlanParam.toJSONString());
        JSONArray workPlanRows = null;
        if(Constants.EXECUTE_SUCCESS.equals(workPlan.getString("status"))){
            workPlanRows = workPlan.getJSONArray("rows");
        }
        if(workPlanRows != null && workPlanRows.size() > 1){
            JSONObject end = workPlanRows.getJSONObject(workPlanRows.size() - 1);
            JSONObject endPrevious = workPlanRows.getJSONObject(workPlanRows.size() - 2);
            String endTimeStr = end.getString("planTime");
            String previousTimeStr =  endPrevious.getString("planTime");
            long daySub = DateUtils.getDaySub(previousTimeStr, endTimeStr)+1;
            end.put("schedule",daySub);
            workPlanRows.remove(0);
        }
        try {
            returnInfo = currentProgressService.getRows(paramJson,workPlanRows);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return returnInfo;
    }

}
