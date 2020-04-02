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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * @author ysy
 * @date 2018/5/11 9:50
 * @description*/


@Service
public class ListQueryService extends NewBaseService {

     private static final Logger logger = LoggerFactory.getLogger(ListQueryService.class);




    /**
     * 根据条件，查询列表信息
     * @param ajaxParam
     * @return
     */
    public JSONArray getProjet(String ajaxParam,List<String> collectorsNames) {

        JSONObject ajaxObj = JSONObject.parseObject(ajaxParam);
        JSONArray resultArray = new JSONArray();
        JSONObject projectJson= new JSONObject();
        DecimalFormat df =  new DecimalFormat("0.00");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if(collectorsNames == null || collectorsNames.size() < 1){
            return resultArray;
        }

        JSONObject projectParam  = new JSONObject();
        projectParam.put("collectorNameList",collectorsNames);
        projectParam.put("tenant",request.getSession().getAttribute("tenant"));
        String projectInfoSql = "com.raising.forward.mapper.ProjectInfoMapper.getRowsWithResources";
        List<JSONObject> projects =  this.sqlSessionTemplate.selectList(projectInfoSql,projectParam);

        //获取tsdb数据
        JSONArray tagNamesArray = new JSONArray();
        for(String collectorName : collectorsNames){
            JSONObject obj1 = new JSONObject();
            JSONObject obj2 = new JSONObject();
            obj1.put("instance_name",collectorName+"_MR_Ring_Num");
            obj2.put("instance_name",collectorName+"_MR_Des_A1Mileage");
            tagNamesArray.add(obj1);
            tagNamesArray.add(obj2);
        }
        JSONObject tagJson = null;
        try {
            tagJson = TsdbUtil.getInstanceOfRest(tagNamesArray);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        JSONArray tagsArray  = null;
        if(tagJson != null && Constants.EXECUTE_SUCCESS.equals(tagJson.getString("status"))){
            tagsArray =  tagJson.getJSONArray("dataArray");
        }

        if(projects != null && projects.size() > 0){
            for(int i = 0;i<projects.size();i++) {
                projectJson = projects.get(i);
                String ccsIdStr = ajaxObj.getString("ccsId");
                String projectName = ajaxObj.getString("projectName");
                if (!StringUtils.isNullOrEmpty(ccsIdStr)) {
                    String ccsIdTemp = projectJson.getString("ccsId");
                    if (ccsIdTemp.indexOf(ccsIdStr) < 0) {
                        continue;
                    }
                }
                if (!StringUtils.isNullOrEmpty(projectName)) {
                    String projectTemp = projectJson.getString("projectName");
                    if (projectTemp.indexOf(projectName) < 0) {
                        continue;
                    }
                }
                String createTimeStr = projectJson.getString("createTime");

                projectJson.put("digLength", projectJson.getString("endMileage"));
                String format = null;
                try {
                    format = dateFormat.format(dateFormat.parse(createTimeStr));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                projectJson.put("createTime", format);
                String collectorName = projectJson.getString("collectorName");

                fillProjectJson(tagsArray,projectJson,collectorName,df);
                if(!projectJson.containsKey("currentRing")){
                    projectJson.put("currentRing","— —");
                    projectJson.put("quality","192");
                }
                if(!projectJson.containsKey("cutMileage")){
                    projectJson.put("cutMileage","— —");
                }

                resultArray.add(projectJson);
            }
        }

        return resultArray;

    }

    private void fillProjectJson(JSONArray tagsArray,JSONObject projectJson,String collectorName,DecimalFormat df){
        if(tagsArray == null){
            return;
        }
        for(int j = 0;j<tagsArray.size();j++){
            JSONObject tagObj = tagsArray.getJSONObject(j);
            if(collectorName.equals(tagObj.getString("collectorName")) && "MR_Ring_Num".equals(tagObj.getString("tagName")) ){
                String value = tagObj.getString("value");
                if(value.indexOf(".")>0){
                    value = value.substring(0,value.indexOf("."));
                }
                projectJson.put("currentRing",value);
                projectJson.put("quality",tagObj.getString("quality"));
            }
            if(collectorName.equals(tagObj.getString("collectorName")) && "MR_Des_A1Mileage".equals(tagObj.getString("tagName"))){
                String value = tagObj.getString("value");
                float v = Float.parseFloat(value);                ;
                projectJson.put("cutMileage",df.format(v));
                projectJson.put("quality",tagObj.getInteger("quality"));

            }
        }
    }

    public JSONArray getProjectForSelect(List<String> collectorsNames){
        JSONArray resultArray = new JSONArray();
        JSONObject projectJson= new JSONObject();
        try {
            if(collectorsNames == null || collectorsNames.size() < 1){
                return  null;
            }
            JSONObject projectParam  = new JSONObject();
            projectParam.put("collectorNameList",collectorsNames);
            projectParam.put("tenant",request.getSession().getAttribute("tenant"));
            String projectInfoSql = "com.raising.forward.mapper.ProjectInfoMapper.getRowsWithResources";
            List<JSONObject> projects =  this.sqlSessionTemplate.selectList(projectInfoSql,projectParam);
            if(projects != null && projects.size() > 0){
                for(int i = 0;i<projects.size();i++) {
                    projectJson = projects.get(i);
                    resultArray.add(projectJson);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultArray;

    }


}
