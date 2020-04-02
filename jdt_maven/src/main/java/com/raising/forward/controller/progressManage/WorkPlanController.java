package com.raising.forward.controller.progressManage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.common.BaseController;
import com.util.DateUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.baosight.common.utils.DateUtils.date2String;

@Controller("workPlanController")
@RequestMapping("/raising/forward/progressManage/workPlan")
public class WorkPlanController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(WorkPlanController.class);

    @RequestMapping(value = "getRows.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getRows(String ajaxParam){
        JSONObject rows = workPlanService.getRows(ajaxParam);
        JSONArray detailsArray = (JSONArray)rows.get("rows");
        if(detailsArray == null || detailsArray.size() < 1){//如果表中没有该项目数据，就需要取项目表中查询起始和结束值
            if(detailsArray == null){
                detailsArray = new JSONArray();
            }
            JSONObject ajaxJson = JSONObject.parseObject(ajaxParam);
            JSONObject inqu_status = ajaxJson.getJSONObject("inqu_status");
            JSONObject projectParam = new JSONObject();
            projectParam.put("projectId",inqu_status.getInteger("projectId"));
            JSONObject projectInfo = projectForwardService.getProjectInfo(projectParam.toJSONString());

            String startTimeStr = "";
            String endTimeStr = "";
            String daySubStr = "";

            Long startTime = projectInfo.getLong("startTime");
            Long endTime = projectInfo.getLong("endTime");

            startTimeStr = startTime != null ? date2String("yyyy-MM-dd",new Date(startTime)) : "";
            endTimeStr = endTime != null ? date2String("yyyy-MM-dd",new Date(endTime)) : "";

            if(startTime != null && endTime != null){
                long daySub = DateUtils.getTimeLimitForProject(startTimeStr, endTimeStr);//计算工期
                daySubStr = daySub + "";
            }
            Integer ringTotal =  projectInfo.getInteger("ringTotal");
            JSONObject firstRow = new JSONObject();
            firstRow.put("planTime",startTimeStr);
            firstRow.put("startTime",startTimeStr);
            firstRow.put("projectId",projectInfo.getInteger("projectId"));
            firstRow.put("project",true);
            firstRow.put("schedule","");
            firstRow.put("ringNum","");
            firstRow.put("remark","");
            JSONObject endRow = new JSONObject();
            endRow.put("planTime",endTimeStr);
            endRow.put("startTime",startTimeStr);
            endRow.put("projectId",projectInfo.getInteger("projectId"));
            endRow.put("project",true);
            endRow.put("end",true);
            endRow.put("schedule",daySubStr);
            endRow.put("ringNum",ringTotal);
            endRow.put("remark","");
            detailsArray.add(0,firstRow);
            detailsArray.add(endRow);
        }
        return rows;
    }

    @RequestMapping(value = "addRow.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addRow(String ajaxParam){
        JSONObject renturnInfo = null;
        try {
            renturnInfo = workPlanService.addRow(ajaxParam);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            renturnInfo = new JSONObject();
            renturnInfo.put("status", Constants.EXECUTE_FAIL);
            renturnInfo.put("returnMsg", e.getMessage());
        }
        return renturnInfo;
    }

    @RequestMapping(value = "updateRow.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateRow(String ajaxParam){
        JSONObject ajaxJson = JSONObject.parseObject(ajaxParam);
        JSONObject renturnInfo = null;
        JSONObject detail = ajaxJson.getJSONObject("detail");
        JSONArray resultRows = detail.getJSONArray("resultRow");

        JSONObject projectJson = new JSONObject();
        for(int i =0;i<resultRows.size();i++){//判断是否修改了 project相关记录。
            JSONObject temp = resultRows.getJSONObject(i);
            if(temp.getBoolean("project")!= null && temp.getBoolean("project") == true){
                projectJson.put("projectId",temp.getInteger("projectId"));
                Boolean end = temp.getBoolean("end");
                if(end != null && end == true){
                    projectJson.put("endTime",temp.getString("planTime")+" 00:00:00");
                    projectJson.put("ringTotal",temp.getInteger("ringNum"));
                }else{
                    projectJson.put("startTime",temp.getString("planTime")+" 00:00:00");
                }
                resultRows.remove(i);//此项目记录删去
                i--;
            }
        }
        if(projectJson.getInteger("projectId") != null ){//如果修改的是项目记录则更新项目表
            renturnInfo = projectForwardService.updateProject(projectJson.toJSONString());
        }

        if(resultRows.size() < 1){//如果没有修改work_plan表数据
            return renturnInfo;
        }
        try {
            renturnInfo = workPlanService.updateRow(ajaxJson.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            renturnInfo = new JSONObject();
            renturnInfo.put("status", Constants.EXECUTE_FAIL);
            renturnInfo.put("returnMsg", e.getMessage());
        }
        return renturnInfo;
    }

    @RequestMapping(value = "deleteRows.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject deleteRows(String ajaxParam){
        JSONObject renturnInfo = null;
        try {
            renturnInfo = workPlanService.deleteRows(ajaxParam);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            renturnInfo = new JSONObject();
            renturnInfo.put("status", Constants.EXECUTE_FAIL);
            renturnInfo.put("returnMsg", e.getMessage());
        }
        return renturnInfo;
    }



}
