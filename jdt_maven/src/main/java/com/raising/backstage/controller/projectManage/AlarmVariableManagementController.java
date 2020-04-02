package com.raising.backstage.controller.projectManage;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.common.controller.DownloadExcelController;
import com.raising.backstage.service.privilege.projectManage.AlarmVariableManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

@RestController
@RequestMapping("/raising/backstage/projectManage/alarmVariableManagement")
public class AlarmVariableManagementController extends DownloadExcelController {

    private final static Logger logger = LoggerFactory.getLogger(AlarmVariableManagementController.class);

    @Autowired
    private AlarmVariableManagementService alarmVariableManagementService;

    @RequestMapping(value = "/getRows.do",method = RequestMethod.POST)
    public JSONObject getRows(String ajaxParam){
        JSONObject rows = alarmVariableManagementService.getRows(ajaxParam);
        return rows;
    }

    @RequestMapping(value = "/add.do",method = RequestMethod.POST)
    public JSONObject add(String ajaxParam){
        JSONObject jsonObject = null;
        try {
            jsonObject = alarmVariableManagementService.addRows(ajaxParam);
        } catch (Exception e) {
            jsonObject = new JSONObject();
            jsonObject.put("status", Constants.EXECUTE_FAIL);
            jsonObject.put("returnMsg",e.getMessage());
        }
        return jsonObject;
    }

    @RequestMapping(value = "/update.do",method = RequestMethod.POST)
    public JSONObject update(String ajaxParam){
        JSONObject jsonObject = null;
        try {
            jsonObject = alarmVariableManagementService.updateRows(ajaxParam);
        } catch (Exception e) {
            jsonObject = new JSONObject();
            jsonObject.put("status", Constants.EXECUTE_FAIL);
            jsonObject.put("returnMsg",e.getMessage());

        }
        return jsonObject;
    }

    @RequestMapping(value = "/delete.do",method = RequestMethod.POST)
    public JSONObject delete(String ajaxParam){
        JSONObject jsonObject = null;
        try {
            jsonObject = alarmVariableManagementService.delete(ajaxParam);
        } catch (Exception e) {
            jsonObject = new JSONObject();
            jsonObject.put("status", Constants.EXECUTE_FAIL);
            jsonObject.put("returnMsg",e.getMessage());
        }
        return jsonObject;
    }


    @RequestMapping(value = "/download.do",method = RequestMethod.GET)
    public void download(String ajaxParam,HttpServletRequest request, HttpServletResponse response) {
        try {
            ajaxParam = URLDecoder.decode(ajaxParam, "UTF-8");

            JSONObject result = alarmVariableManagementService.getRows(ajaxParam);
            JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
            ajaxParamObj.put("downloadData", result.getJSONArray("rows"));
            download(request, response, ajaxParamObj.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
    }

}
