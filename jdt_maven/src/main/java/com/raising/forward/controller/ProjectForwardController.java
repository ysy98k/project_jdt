package com.raising.forward.controller;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.common.BaseController;
import com.util.CSVUtils;
import com.util.MultipleDataSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 前台项目信息
 */
@RestController("projectForwardController")
@RequestMapping("/raising/forward/projectManage")
public class ProjectForwardController extends BaseController {



    @RequestMapping(value="/getProject.do",method = RequestMethod.GET)
    public JSONObject getProjectInfo(String ajaxParam){
        JSONObject returnInfo = projectForwardService.getProjectInfo(ajaxParam);
        if(returnInfo == null){
            return null;
        }
        returnInfo.put("status", Constants.EXECUTE_SUCCESS);
        return returnInfo;
    }

    @RequestMapping(value="/updateProject.do",method = RequestMethod.POST)
    public JSONObject updateProject(String ajaxParam){
        JSONObject returnInfo = projectForwardService.updateProject(ajaxParam);
        return returnInfo;
    }


    /**
     * 获得线路树
     * @param
     * @return
     */
    @RequestMapping(value = "/getLineTreeJson.do",method = RequestMethod.GET)
    public JSONObject getLineTree() {
        JSONObject returnInfo = new JSONObject();
        List<JSONObject> lineTree = new ArrayList<>();
        //先获取资源
        String groupNames = request.getSession().getAttribute("groupNames").toString();
        List<String> collectionNames = resourceService.getCollectionNames(groupNames);
        //根据资源，获取项目信息。包含ccsID信息
        List<JSONObject> projects =  projectForwardService.getResourcesProject(collectionNames);
        //获得ccsId集合
        List<String> ccsIdsList = new ArrayList<>();
        for(int i=0;i<projects.size();i++){
            JSONObject projectTemp = projects.get(i);
            ccsIdsList.add(projectTemp.getString("ccsId"));
        }
        //获取所有线路列表集合。
        try {
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            lineTree = codeItemService.queryLineTree2(ccsIdsList);
        } finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        //获取盾构机厂家占比
        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        returnInfo.put("data",lineTree);
        return returnInfo;
    }
















}
