package com.raising.backstage.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.common.BaseController;
import com.util.CSVUtils;
import com.util.MultipleDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/raising/backstage/mobile")
public class MobileController extends BaseController{

    @RequestMapping(value = "/getRows.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getRows(String ajaxParam){
        JSONObject rows = mobileService.getRows(ajaxParam);

        JSONArray array = rows.getJSONArray("rows");
        if(array == null || array.size() < 1){
            return rows;
        }else {
            try{
                MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
                for(int i=0;i<array.size();i++){
                    JSONObject temp = array.getJSONObject(i);
                    Integer userId = temp.getInteger("userId");
                    JSONObject paramJson = new JSONObject();
                    paramJson.put("userId",userId);
                    List<JSONObject> users = userService.getUsers2(paramJson);
                    String userName = users.get(0).getString("userName");
                    temp.put("userName",userName);
                }
                JSONObject paramJson = new JSONObject();
                paramJson.put("tenant","raising");
            }finally {
                //数据源切换回来，
                MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
            }
        }
        return rows;
    }

    @RequestMapping(value = "/add.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject add(String ajaxParam){

        JSONObject jsonObject = null;
        try {
            jsonObject = mobileService.addRows(ajaxParam);
        } catch (Exception e) {
            jsonObject = new JSONObject();
            jsonObject.put("status", Constants.EXECUTE_FAIL);
            jsonObject.put("returnMsg",e.getMessage());

        }
        return jsonObject;
    }

    @RequestMapping(value = "/update.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject update(String ajaxParam){
        JSONObject jsonObject = null;
        try {
            jsonObject = mobileService.updateRows(ajaxParam);
        } catch (Exception e) {
            jsonObject = new JSONObject();
            jsonObject.put("status", Constants.EXECUTE_FAIL);
            jsonObject.put("returnMsg",e.getMessage());

        }
        return jsonObject;
    }

    @RequestMapping(value = "/delete.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject delete(String ajaxParam){
        JSONObject jsonObject = null;
        try {
            jsonObject = mobileService.delete(ajaxParam);
        } catch (Exception e) {
            jsonObject = new JSONObject();
            jsonObject.put("status", Constants.EXECUTE_FAIL);
            jsonObject.put("returnMsg",e.getMessage());

        }
        return jsonObject;
    }

    @RequestMapping(value="getUsers.do",method = RequestMethod.POST)
    @ResponseBody
    public List<JSONObject> getUsers(){
        List<JSONObject> statusList = null;
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            JSONObject paramJson = new JSONObject();
            paramJson.put("tenant","raising");
            statusList = userService.getUsers2(paramJson);
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return  statusList;
    }
}
