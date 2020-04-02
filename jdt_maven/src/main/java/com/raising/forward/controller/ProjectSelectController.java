package com.raising.forward.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.BaseController;
import com.raising.forward.service.CodeItemService;
import com.raising.forward.service.ListQueryService;
import com.util.CSVUtils;
import com.util.MultipleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页处理器
 */
@Controller("projectSelectController")
@RequestMapping("/raising/forward/project")
public class ProjectSelectController extends BaseController {


    @Autowired
    private ListQueryService listQueryService;

    @Autowired
    private CodeItemService codeItemService;

    /**
     * 查询主页显示的数据信息
     * 查询条件信息
     * 查询列表信息
     * @return
     */
    @RequestMapping(value = "/getProject.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject queryMainPageInfo(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        //查询列表信息
        String groupNames = request.getSession().getAttribute("groupNames").toString();
        List<String> collectionNames = resourceService.getCollectionNames(groupNames);
        JSONArray detailsArray = listQueryService.getProjectForSelect(collectionNames);
        if(detailsArray == null){
            returnInfo.put("detailsArray",null);
            returnInfo.put("codeName",null);
            return returnInfo;
        }
        //获得查询条件信息ccsIds
        List<String> ccsIdsList = new ArrayList<>();
        for(Object obj : detailsArray){
            JSONObject tempObj = (JSONObject)obj;
            ccsIdsList.add(tempObj.getString("ccsId"));
        }
        //更换数据源，从aasccs库中的字典表中，查询ccsIds对应的值
        JSONObject codeName;
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            codeName = codeItemService.getLineCondition2(ccsIdsList);
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        returnInfo.put("detailsArray",detailsArray);
        returnInfo.put("codeName",codeName);
        return returnInfo;
    }
}
