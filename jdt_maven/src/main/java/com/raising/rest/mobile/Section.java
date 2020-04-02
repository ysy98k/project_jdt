package com.raising.rest.mobile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.raising.ccs.ResourceService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * 获取平台区间段模块数据接口
 */
@Component
@Path("/raising")
public class Section {
    //租户名称
    private static final String TENANT_NAME = "raising";

    @Autowired
    protected SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    protected ResourceService resourceService;

    /**
     * 获取平台区间段模块数据方法     *
     * @param paramJson
     * @return
     */
    @POST
    @Path("/section")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject getRows(JSONObject paramJson){
        //返回值
        JSONObject returnInfo = new JSONObject();


        //获取权限控制下的资源
        String token = paramJson.getString("token");
        String group = paramJson.getString("group");
        List<String> resources = resourceService.getCollectionNames(group);
        if(resources == null){
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("returnMsg","access fail！");
            return returnInfo;
        }
        Integer projectId = paramJson.getInteger("project_id");
        JSONObject projectParam = new JSONObject();
        projectParam.put("tenant",TENANT_NAME);
        projectParam.put("projectId",projectId);
        String sql = "com.raising.forward.mapper.ProjectInfoMapper.getRows";
        JSONObject projectJson = sqlSessionTemplate.selectOne(sql, projectParam);
        String collectorName = projectJson.getString("collectorName");
        if(!resources.contains(collectorName)){
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("returnMsg","access fail！");
            return returnInfo;
        }


        List<String> columnNames = getColumnNames(paramJson);
        Map paramMap = new HashMap();
        paramMap.put("tenant", TENANT_NAME);
        paramMap.put("projectId", projectId);
        paramMap.put("tableName", paramJson.getString("tableName"));
        paramMap.put("columnNames",columnNames);
        List<JSONObject> jsonObjects = sqlSessionTemplate.selectList("com.raising.rest.DataUpload.getRows", paramMap);
        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        returnInfo.put("data",jsonObjects);
        return returnInfo;

    }



    /**
     * 获得列名
     * @param paramJson
     * @return
     */
    private ArrayList<String> getColumnNames(JSONObject paramJson){
        ArrayList<String> columnNames = new ArrayList();//存放列名,
        JSONArray dataArray = paramJson.getJSONArray("data");
        //封装列
        Set<String> columnNameSet = dataArray.getJSONObject(0).keySet();
        for (String temp : columnNameSet) {
            columnNames.add(temp);
        }
        return columnNames;
    }
}
