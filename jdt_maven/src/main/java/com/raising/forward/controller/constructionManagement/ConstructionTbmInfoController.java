package com.raising.forward.controller.constructionManagement;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.common.BaseController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 前台盾构机信息
 */
@RestController
@RequestMapping("/raising/forward/construction/tbmInfo")
public class ConstructionTbmInfoController extends BaseController {


    /**
     * 获取盾构机基本信息
     * @param ajaxParam
     * @return
     */
    @RequestMapping(value = "/getTbmInfo.do",method = RequestMethod.GET)
    public JSONObject getTbmInfo(String ajaxParam){
        JSONObject returnInfo = constructionTbmInfoService.getInfo(ajaxParam);
        return returnInfo;
    }

    /**
     * 获得盾构机履历信息
     * @param ajaxParam
     * @return
     */
    @RequestMapping(value = "/getConstructionPeroid.do",method = RequestMethod.POST)
    public JSONObject getTbmRecord(String ajaxParam){
        JSONObject tbmRecord = constructionTbmInfoService.getTbmRecord(ajaxParam);
        return tbmRecord;
    }


    /**
     * 获得地图查询功能，盾构厂家占比
     */
    @RequestMapping(value = "/getTbmFactoryPie.do",method = RequestMethod.GET)
    public JSONObject  getTbmFactoryPie(){
        JSONObject returnInfo = new JSONObject();
        Map<String,JSONObject> factoryMap = new HashMap<>();
        //先获取资源
        String groupNames = request.getSession().getAttribute("groupNames").toString();
        List<String> collectionNames = resourceService.getCollectionNames(groupNames);
        //根据资源，获取项目信息。包含ccsID信息
        List<JSONObject> projects =  projectForwardService.getResourcesProject(collectionNames);

        //从项目中提前盾构机厂家信息，并归类
        List<String> markList = new ArrayList<>();
        for(int i=0;i<projects.size();i++){
            JSONObject projectTemp = projects.get(i);
            String factory = projectTemp.getString("factory");
            if(!markList.contains(factory)){//如果此盾构机厂家第一次出现。则新建一条json
                JSONObject temp = new JSONObject();
                temp.put("name",factory);
                temp.put("value",1);
                factoryMap.put(factory,temp);
                markList.add(factory);
            }
            //如果此盾构机厂家不是第一次出现
            JSONObject dataJson = factoryMap.get(factory);
            dataJson.put("value",dataJson.getIntValue("value")+1);
        }
        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        returnInfo.put("data",factoryMap.values());
        return returnInfo;
    }


















}
