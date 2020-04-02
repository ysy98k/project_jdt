package com.raising.forward.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.common.NewBaseService;

import com.util.TsdbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.*;

/**
 * 信息总览Service
 */
@Service("totalInfoService")
public class TotalInfoService extends NewBaseService {

    private static final Logger logger = LoggerFactory.getLogger(TotalInfoService.class);
    /**
     * 得到该用户下的所有项目信息
     * @return
     */
    public JSONObject getTotalInfos(List<String> collectorNames){
        JSONObject returnInfo = new JSONObject();
        List<JSONObject> projectsList = new ArrayList<>();
        List<String> ccsIdList = new ArrayList<>();

        if(collectorNames ==null || collectorNames.size() < 1){
            returnInfo.put("dataList",projectsList);
            return returnInfo;
        }
        JSONObject projectParam = new JSONObject();
        projectParam.put("collectorNameList",collectorNames);
        projectParam.put("tenant","raising");
        String projectInfoSql = "com.raising.forward.mapper.ProjectInfoMapper.getTotalInfos";
        projectsList =  this.sqlSessionTemplate.selectList(projectInfoSql,projectParam);
        if(projectsList == null || projectsList.size() < 1){
            returnInfo.put("dataList",projectsList);
            return returnInfo;
        }
        //获取tsdb数据
        JSONArray tagArray  = null;
        String groupNames = request.getSession().getAttribute("groupNames").toString();
        String tsdb_totalInfo = newRedisUtils.get("tsdb_totalInfo_"+groupNames);
        //String tsdb_totalInfo2 = redisUtils.get("tsdb_totalInfo2_"+groupNames);
        if(StringUtils.isNullOrEmpty(tsdb_totalInfo)){
            JSONArray tagNamesArray = new JSONArray();
            for(String collectorName : collectorNames){
                JSONObject obj1 = new JSONObject();
                JSONObject obj2 = new JSONObject();
                JSONObject obj3 = new JSONObject();
                obj1.put("instance_name",collectorName+"_MR_Ring_Num");
                obj2.put("instance_name",collectorName+"_FTunneling");
                obj3.put("instance_name",collectorName+"_FAssembly");
                tagNamesArray.add(obj1);
                tagNamesArray.add(obj2);
                tagNamesArray.add(obj3);
            }
            JSONObject tagJson = null;
            try {
                tagJson = TsdbUtil.getInstanceOfRest( tagNamesArray);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            if(tagJson != null && Constants.EXECUTE_SUCCESS.equals(tagJson.getString("status"))){
                tagArray =  tagJson.getJSONArray("dataArray");
                newRedisUtils.set("tsdb_totalInfo_"+groupNames,tagArray.toJSONString(),60);
                //redisUtils.set("tsdb_totalInfo2_"+groupNames,tagArray.toJSONString(),60);
            }
        }else{
            tagArray = JSONObject.parseArray(newRedisUtils.get("tsdb_totalInfo_"+groupNames));
        }


        for(int i=0;i<projectsList.size();i++){
            JSONObject projectJson = projectsList.get(i);
            String cityCCSIdStr = projectJson.getString("cityCCSId");
            ccsIdList.add(cityCCSIdStr);

            projectJson.put("totalMileage",projectJson.getString("endMileage"));
            String collectorName = projectJson.getString("collectorName");
            //得到tsdb数据

            fillProjectJson(tagArray, projectJson, collectorName);

            if(!projectJson.containsKey("ringNum")){
                projectJson.put("ringNum","----");
                projectJson.put("quality","192");
                projectJson.put("communicationState","未连接");
            }
            if(!projectJson.containsKey("constructionState")){
                projectJson.put("constructionState","等待中");
            }

        }
        returnInfo.put("dataList",projectsList);
        returnInfo.put("ccsIdList",ccsIdList);
        return returnInfo;
    }


    private void fillProjectJson(JSONArray instanceArr,JSONObject projectJson,String collectorName){
        long currentTime = new Date().getTime();
        if(instanceArr == null){
            return;
        }
        for(int j = 0;j<instanceArr.size();j++){
            JSONObject instance = instanceArr.getJSONObject(j);
            if(collectorName.contains(instance.getString("collectorName")) && instance.getString("tagName").contains("MR_Ring_Num") ){
                String value = instance.getString("value");
                if(value.indexOf(".")>0){
                    value = value.substring(0,value.indexOf("."));
                }
                projectJson.put("ringNum",value);
                projectJson.put("quality",instance.getString("quality"));

                String timestamp = instance.getString("timestamp");
                Date ringDate = new Date(Long.parseLong(timestamp));
                Long ringtime = ringDate.getTime();
                System.out.println(currentTime - ringtime);
                if(currentTime - ringtime > 600000){
                    projectJson.put("communicationState","已断开");
                    continue;
                }
                projectJson.put("communicationState","已连接");
            }
            if(collectorName.equals(instance.getString("collectorName")) && "FTunneling".equals(instance.getString("tagName"))){
                int quality = instance.getInteger("quality");
                String value = instance.getString("value");
                if(value.indexOf(".")>0){
                    value = value.substring(0,value.indexOf("."));
                }
                if(quality ==192 && ("1".equals(value) || "-1".equals(value) || "true".equals(value) )){
                    projectJson.put("constructionState","掘进中");
                }
            }
            if(collectorName.equals(instance.getString("collectorName")) && "FAssembly".equals(instance.getString("tagName"))){
                int quality = instance.getInteger("quality");
                String value = instance.getString("value");
                if(value.indexOf(".")>0){
                    value = value.substring(0,value.indexOf("."));
                }
                if(quality ==192 && ("1".equals(value) || "-1".equals(value) || "true".equals(value) )){
                    projectJson.put("constructionState","拼装中");
                }
            }
        }

    }

}
