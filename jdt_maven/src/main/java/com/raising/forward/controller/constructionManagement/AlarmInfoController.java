package com.raising.forward.controller.constructionManagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.common.controller.DownloadExcelController;
import com.raising.backstage.service.TbmService;
import com.raising.forward.service.constructionManagement.AlarmInfoService;
import com.raising.forward.service.tbmManage.TbmResumeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

@RestController
@RequestMapping("/raising/forward/construction/alarmInfo")
public class AlarmInfoController extends DownloadExcelController {

    private static final Logger logger = LoggerFactory.getLogger(AlarmInfoController.class);

    @Autowired
    private AlarmInfoService alarmInfoService;

    @Autowired
    private TbmResumeService tbmResumeService;

    @RequestMapping(value = "/getRows.do",method = RequestMethod.POST)
    public JSONObject getAlarmInfo(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        try {
            returnInfo = alarmInfoService.getAlarmInfo(ajaxParam);
        } catch (Exception e) {
            e.printStackTrace();
            String message = "获取报警信息失败！";
            if(e.getMessage().length() < 10){
                message = e.getMessage();
            }
            returnInfo.put("status", Constants.EXECUTE_FAIL);
            returnInfo.put("message",message);
        }
        if(Constants.EXECUTE_SUCCESS.equals(returnInfo.getString("status"))){
            JSONArray rows = returnInfo.getJSONArray("rows");
            if(rows == null || rows.size() < 1){
                return returnInfo;
            }
            JSONObject paramJson = JSONObject.parseObject(ajaxParam);
            JSONObject tbmJson = tbmResumeService.getRowWithProjectId(paramJson.getJSONObject("inqu_status").getInteger("projectId"));
            String tbmName = tbmJson.getString("tbmName");
            for(int i =0;i<rows.size();i++){
                JSONObject alarmObj = rows.getJSONObject(i);
                alarmObj.put("tbmName",tbmName);
            }
        }
        return returnInfo;
    }

    @RequestMapping(value = "getHistoryRows.do",method = RequestMethod.POST)
    public JSONObject getHistory(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        try {
            returnInfo = alarmInfoService.getHistory(ajaxParam);
        } catch (Exception e) {
            e.printStackTrace();
            String message = "获取报警信息失败！";
            if(e.getMessage().length() < 10){
                message = e.getMessage();
            }
            returnInfo.put("status", Constants.EXECUTE_FAIL);
            returnInfo.put("message",message);
        }
        if(Constants.EXECUTE_SUCCESS.equals(returnInfo.getString("status"))){
            JSONArray rows = returnInfo.getJSONArray("rows");
            if(rows == null || rows.size() < 1){
                return returnInfo;
            }
            JSONObject paramJson = JSONObject.parseObject(ajaxParam);
            JSONObject tbmJson = tbmResumeService.getRowWithProjectId(paramJson.getJSONObject("inqu_status").getInteger("projectId"));
            String tbmName = tbmJson.getString("tbmName");
            for(int i =0;i<rows.size();i++){
                JSONObject alarmObj = rows.getJSONObject(i);
                alarmObj.put("tbmName",tbmName);
            }
        }
        return returnInfo;
    }

    @RequestMapping(value = "/downloadExcel.do",method = RequestMethod.GET)
    public void download(String ajaxParam, HttpServletRequest request, HttpServletResponse response) {
        try {
            ajaxParam = URLDecoder.decode(ajaxParam, "UTF-8");
            JSONObject returnInfo = new JSONObject();
            try {
                returnInfo = alarmInfoService.getHistory(ajaxParam);
            } catch (Exception e) {
                e.printStackTrace();
                String message = "获取报警信息失败！";
                if(e.getMessage().length() < 10){
                    message = e.getMessage();
                }
                returnInfo.put("status", Constants.EXECUTE_FAIL);
                returnInfo.put("message",message);
            }
            if(Constants.EXECUTE_SUCCESS.equals(returnInfo.getString("status"))){
                JSONArray rows = returnInfo.getJSONArray("rows");
                if(rows != null && rows.size() > 0){
                    JSONObject paramJson = JSONObject.parseObject(ajaxParam);
                    JSONObject tbmJson = tbmResumeService.getRowWithProjectId(paramJson.getJSONObject("inqu_status").getInteger("projectId"));
                    String tbmName = tbmJson.getString("tbmName");
                    for(int i =0;i<rows.size();i++){
                        JSONObject alarmObj = rows.getJSONObject(i);
                        alarmObj.put("tbmName",tbmName);
                    }
                }
            }
            JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
            ajaxParamObj.put("downloadData", returnInfo.getJSONArray("rows"));
            download(request, response, ajaxParamObj.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
    }
}
