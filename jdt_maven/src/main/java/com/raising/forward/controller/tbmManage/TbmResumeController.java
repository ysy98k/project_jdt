package com.raising.forward.controller.tbmManage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.raising.forward.service.tbmManage.TbmResumeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

import static com.baosight.common.utils.DateUtils.date2String;

@Controller("tbmResume")
@RequestMapping("/raising/forward/tbmManage/tbmResume")
public class TbmResumeController {

    private static final Logger logger = LoggerFactory.getLogger(TbmResumeController.class);

    @Autowired
    private TbmResumeService service;

    @RequestMapping(value = "/getRows.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getRows(String ajaxParam){
        JSONObject rows = service.getRows(ajaxParam);
        JSONArray detailsArray = (JSONArray)rows.get("rows");
        if(detailsArray.size() < 1){
            return rows;
        }
        for(int i = 0;i<detailsArray.size();i++) {
            JSONObject tempObj = detailsArray.getJSONObject(i);
            tempObj.put("createTime", date2String("yyyy-MM-dd", new Date(tempObj.getLong("createTime"))));

        }
        return rows;
    }



}
