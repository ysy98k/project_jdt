package com.raising.forward.controller.group.progress;


import com.alibaba.fastjson.JSONObject;
import com.common.BaseController;
import com.raising.forward.controller.group.StatisticalAnalysisController;
import com.raising.forward.service.group.progress.GroupProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 进度分析
 */
@RestController("groupProgressController")
@RequestMapping("/raising/forward/statisticalAnalysis/progress/groupProgress")
public class GroupProgressController extends StatisticalAnalysisController {

    @Autowired
    private GroupProgressService groupProgressService;


    @RequestMapping(value = "getRows.do",method = RequestMethod.GET)
    public JSONObject getRows(String ajaxParam){

        List<JSONObject> projects = getProjects();
        if(projects == null || projects.size() < 1){
            return null;
        }
        List<Integer> projectIds = new ArrayList<>();
        for(int i =0;i<projects.size();i++){
            JSONObject jsonObject = projects.get(i);
            projectIds.add(jsonObject.getInteger("projectId"));
        }

        JSONObject paramJson = JSONObject.parseObject(ajaxParam);
        paramJson.put("projectIds",projectIds);
        paramJson.put("startTime",new Date(paramJson.getLongValue("startTime")));
        paramJson.put("endTime",new Date(paramJson.getLongValue("endTime")));
        List<JSONObject> ringData = jRingDataService.getRowsOfStatis(paramJson);
        JSONObject returnJson = groupProgressService.getData(paramJson, projects, ringData);
        return returnJson;

    }


}
