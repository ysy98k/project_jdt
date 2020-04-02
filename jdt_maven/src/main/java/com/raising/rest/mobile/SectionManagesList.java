package com.raising.rest.mobile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;

import com.raising.ccs.ResourceService;
import com.raising.forward.service.CodeItemService;
import com.util.*;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 获取列表数据接口
 */
@Component
@Path("/raising")
public class SectionManagesList {

    private static final Logger logger = LoggerFactory.getLogger(SectionManagesList.class);
    //租户名称
    private static final String TENANT_NAME = "raising";

    @Autowired
    protected SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private CodeItemService codeItemService;

    @Autowired
    protected ResourceService resourceService;

    /**
     * 获取列表数据接口
     * @param paramJson
     * @return
     */
    @POST
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject getRows(JSONObject paramJson){


        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        //返回值
        JSONObject returnInfo = new JSONObject();
        JSONArray resultArray = new JSONArray();
        JSONArray cityArray = new JSONArray();
        JSONArray lineArray = new JSONArray();
        JSONArray cityList= new JSONArray();
        JSONArray lineList= new JSONArray();
        JSONObject cityInfo = new JSONObject();



        //获取权限控制下的资源
        String group = paramJson.getString("group");
        List<String> collectorNames = resourceService.getCollectionNames(group);
        if(collectorNames==null ){
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("returnMsg","access fail！");
            return returnInfo;
        }
        JSONObject projectParam  = new JSONObject();
        projectParam.put("collectorNameList",collectorNames);
        projectParam.put("tenant",TENANT_NAME);
        String projectInfoSql = "com.raising.forward.mapper.ProjectInfoMapper.getResourcesAndOrderBy";
        List<JSONObject> projects =  this.sqlSessionTemplate.selectList(projectInfoSql,projectParam);



        if(projects != null && projects.size() > 0){
            //获取tsdb数据
            JSONArray tagNamesArray = new JSONArray();
            for(String collectorName : collectorNames){
                JSONObject obj1 = new JSONObject();
                JSONObject obj2 = new JSONObject();
                obj1.put("instance_name",collectorName+"_MR_Ring_Num");
                obj2.put("instance_name",collectorName+"MR_Des_A1Mileage");
                tagNamesArray.add(obj1);
                tagNamesArray.add(obj2);
            }
            JSONObject tagJson = null;
            try {
                tagJson = TsdbUtil.getInstanceOfRest( tagNamesArray);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            JSONArray tagsArray  = null;
            if(tagJson!= null && Constants.EXECUTE_SUCCESS.equals(tagJson.getString("status"))){
                tagsArray =  tagJson.getJSONArray("dataArray");
            }

            for(int i = 0;i<projects.size();i++) {
                JSONObject projectJson = projects.get(i);
                Object digLength = null;

                String startMileage = projectJson.getString("startMileage");
                String endMileage = projectJson.getString("endMileage");
                if (startMileage != null && endMileage != null) {
                    float s = Float.parseFloat(startMileage);
                    float e = Float.parseFloat(endMileage);
                    digLength = e - s;
                } else {
                    digLength = null;

                }
                String collectorName = projectJson.getString("collectorName");
                fillProjectJson(tagsArray,projectJson,collectorName,decimalFormat);

                JSONObject temp = new JSONObject();
                String sTime=projectJson.getString("createTime");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    String format = dateFormat.format(dateFormat.parse(sTime));
                    temp.put("createTime",format);


                    if(projectJson.get("ringTotal")==null) {
                        temp.put("ringTotal", "— —");
                    }else {
                        temp.put("ringTotal", projectJson.get("ringTotal"));//总环数
                    }
                    if(digLength==null) {
                        digLength = "— —";
                    }
                    temp.put("digLength",digLength);//掘进距离

                    if(!projectJson.containsKey("currentRing")){
                        projectJson.put("currentRing","— —");
                        projectJson.put("currentRingQuality","192");
                    }
                    if(!projectJson.containsKey("cutMileage")){
                        projectJson.put("cutMileage","— —");
                        projectJson.put("cutMileageQuality","192");
                    }
                    temp.put("currentRing",projectJson.get("currentRing"));
                    temp.put("currentRingQuality",projectJson.get("currentRingQuality"));
                    temp.put("cutMileage",projectJson.get("cutMileage"));
                    temp.put("cutMileageQuality",projectJson.get("cutMileageQuality"));
                    temp.put("ccsId",projectJson.get("ccsId"));//放入ccsId，用来查找所属城市
                    temp.put("project",projectJson.get("projectName"));//项目名称
                    temp.put("section",projectJson.get("sectionName"));
                    temp.put("buildstatusCcsId",projectJson.get("status"));//施工状态
                    temp.put("project_id",projectJson.get("projectId"));
                    temp.put("tbmName",projectJson.get("tbmName"));
                    temp.put("collectorName",projectJson.get("collectorName"));
                    temp.put("sectionOwner",projectJson.get("sectionOwner"));
                    temp.put("supervisor",projectJson.get("supervisor"));
                    temp.put("projectSituation",projectJson.get("projectSituation"));
                    temp.put("projectLocation",projectJson.get("projectLocation"));
                    temp.put("factory",projectJson.get("factory"));
                    temp.put("buildUnit",projectJson.get("buildUnit"));
                    resultArray.add(temp);
                }catch (Exception e){
                }

            }
        }


        //添加城市线路信息
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);

            JSONObject one = resultArray.getJSONObject(0);
            Map<String,String> buildstatusCcsIdMap = codeItemService.getCodeNameFromSystemCode(one.getString("buildstatusCcsId"));

            for(int i=0;i<resultArray.size();i++){
                JSONObject temp = resultArray.getJSONObject(i);
                String ccsId = temp.getString("ccsId");
                String buildstatusCcsId = temp.getString("buildstatusCcsId");
                String cityCcsId = ccsId.substring(0,ccsId.lastIndexOf("."));
                String city = codeItemService.getCodeName(cityCcsId);
                String line = codeItemService.getCodeName(ccsId);
                //String buildstatus = codeItemService.getCodeNameFromSystemAndItem(buildstatusCcsId);
                String buildstatus = buildstatusCcsIdMap.get(buildstatusCcsId);
                temp.put("city",city);//所属城市

                if(!cityList.contains(city)) {
                    cityList.add(city);
                }
                if(!lineList.contains(line)) {
                    lineList.add(line);
                }
                temp.put("line",line);//线路
                temp.put("buildstatus",buildstatus);//施工状态
                temp.remove("ccsId");
                temp.remove("buildstatusCcsId");
            }

        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        for(int j=0;j<cityList.size();j++){
            /*cityInfo.put("ct",cityList.get(j));*/
            JSONObject temp = new JSONObject();
            temp.put("ct",cityList.get(j));
            cityArray.add(j,temp);
        }

        for(int j=0;j<lineList.size();j++){
            JSONObject temp = new JSONObject();
            temp.put("ll",lineList.get(j));
            lineArray.add(j,temp);
        }

        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        returnInfo.put("listInfo",resultArray);
        returnInfo.put("ctInfo",cityArray);
        return returnInfo;
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
                projectJson.put("currentRingQuality",tagObj.getString("quality"));
            }
            if(collectorName.equals(tagObj.getString("collectorName")) && "MR_Des_A1Mileage".equals(tagObj.getString("tagName"))){
                String value = tagObj.getString("value");
                if(value.indexOf(".")>0){
                    value = value.substring(0,value.indexOf("."));
                }
                float v = Float.parseFloat(value);                ;
                projectJson.put("cutMileage",df.format(v));
                projectJson.put("cutMileageQuality",tagObj.getInteger("quality"));

            }
        }
    }





}
