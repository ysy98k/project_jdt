package com.raising.ccs;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.xinsight.constant.AasKeyConstants;
import com.common.NewBaseService;
import com.raising.forward.service.PropertiesValue;
import com.util.CSVUtils;
import com.util.MultipleDataSource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 资源类
 */
@Service(value = "resourceService")
public class ResourceService extends NewBaseService {

    //盾构服务资源名称
    private static final String PROP_SERVICE_NAME = "PROP";

    private static final String api = "/api/resource";

    public List<String> getCollectionNames(String groupNamesStr){
        String sql = "com.raising.backstage.entity.Resource.getRows";
        List<String> groupNames = null;
        if(groupNamesStr.indexOf(",") > 0){
            String[] split = groupNamesStr.split(",");
            groupNames = Arrays.asList(split);
        }else{
            groupNames = new ArrayList<>();
            groupNames.add(groupNamesStr);
        }
        JSONObject paramJson = new JSONObject();
        paramJson.put("groupNamesList",groupNames);
        List<String> collectionNames = null;
        try {
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            collectionNames = sqlSessionTemplate.selectList(sql,paramJson);
        } finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return collectionNames;
    }

    /**
     * 更改资源的工具方法。
     * @param returnInfo
     * @param operatorArray
     * @return
     */
    //@CacheEvict(value="collectionNames", allEntries=true)
    @Caching(evict={
            @CacheEvict(value="upload_check_project",allEntries=true)
    })
    public JSONObject addResource(JSONObject returnInfo, JSONArray operatorArray)throws Exception{
        restDao.setHost(PropertiesValue.AAS_ADRESS);
        restDao.setServiceName(PropertiesValue.AAS_APP_PATH);
        //如果code 为1，执行新增，如果code为2，执行删除
        HttpSession session = this.request.getSession();
        //得到token
        String token = session.getAttribute("token").toString();
        /*String groupNames = session.getAttribute("groupNames").toString();
        redisTemplate.delete(groupNames);*/
        JSONObject mapIn = new JSONObject();
        //添加token
        mapIn.put("token", token);

        //新建resourcesArray。保存要增加的资源数组
        JSONArray resourcesArray = new JSONArray();
        for (int i = 0; i < operatorArray.size(); i++) {
            JSONObject project = operatorArray.getJSONObject(i);
            JSONObject resourceTemp = new JSONObject();
            resourceTemp.put("name", project.getString("collectorName"));
            resourceTemp.put("display_name", project.getString("projectName"));
            resourceTemp.put("description", "");
            resourceTemp.put("service", PROP_SERVICE_NAME);
            resourcesArray.add(i,resourceTemp);
        }
        //添加resourcesArray
        mapIn.put("resources", resourcesArray);
        //插入
        JSONObject restInfo = restDao.invoke("post", api, mapIn);
        if (Constants.EXECUTE_SUCCESS.equals(restInfo.getString(AasKeyConstants.KEY_ERRCODE))) {
            returnInfo.put("returnMsg", returnInfo.getString("returnMsg")+ "资源新增成功.");
        } else {
            String returnMsg = returnInfo.getString("returnMsg")+ "资源新增失败，错误码为："
                    + restInfo.getString(AasKeyConstants.KEY_ERRCODE)
                    + "；错误信息为："+ restInfo.getString(AasKeyConstants.KEY_ERRINFO);
            throw new Exception(returnMsg);
            //returnInfo.put("returnMsg",);
        }
        return returnInfo;
    }

    /**
     * 更改资源的工具方法。
     * @param returnInfo
     * @param operatorArray
     * @return
     */
    @Caching(evict={
            @CacheEvict(value="upload_check_project",allEntries=true)
    })
    public JSONObject deleteResource(JSONObject returnInfo, JSONArray operatorArray)throws Exception{
        restDao.setHost(PropertiesValue.AAS_ADRESS);
        restDao.setServiceName(PropertiesValue.AAS_APP_PATH);
        //如果code 为1，执行新增，如果code为2，执行删除
        HttpSession session = this.request.getSession();
        //得到token
        String token = session.getAttribute("token").toString();
        /*String groupNames = session.getAttribute("groupNames").toString();
        redisTemplate.delete(groupNames);*/
        JSONObject mapIn = new JSONObject();
        //添加token
        mapIn.put("token", token);
        //封装数据
        StringBuffer resoutceName = new StringBuffer();
        for (int i = 0; i < operatorArray.size(); i++) {
            JSONObject resource = operatorArray.getJSONObject(i);
            resoutceName.append(resource.getString("collectorName"));
            if (i < operatorArray.size() - 1) {
                resoutceName.append(",");
            }
        }
        mapIn.put("service", PROP_SERVICE_NAME);
        mapIn.put("name", resoutceName.toString());
        JSONObject restInfo = restDao.invoke("delete", api, mapIn);
        if (Constants.EXECUTE_SUCCESS.equals(restInfo.getString(AasKeyConstants.KEY_ERRCODE))) {
            returnInfo.put("returnMsg", returnInfo.getString("returnMsg")+ "资源删除成功");
        } else {
            String returnMsg = returnInfo.getString("returnMsg")+
                    "资源删除失败，错误码为："+ restInfo.getString(AasKeyConstants.KEY_ERRCODE)
                    + "；错误信息为："+ restInfo.getString(AasKeyConstants.KEY_ERRINFO);
            throw new Exception(returnMsg);
        }
        return returnInfo;
    }

}
