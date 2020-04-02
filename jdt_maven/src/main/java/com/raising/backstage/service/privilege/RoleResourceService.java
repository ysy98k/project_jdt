package com.raising.backstage.service.privilege;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.entity.GridData;
import com.common.NewBaseService;
import com.raising.forward.service.PropertiesValue;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class RoleResourceService extends NewBaseService {




    public JSONObject getResourcesRows(String ajaxParam,String token) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        restDao.setHost(PropertiesValue.AAS_ADRESS);
        restDao.setServiceName(PropertiesValue.AAS_APP_PATH);

        JSONObject mapIn = new JSONObject();
        mapIn.put("token",token);

        int offset = (ajaxParamObj.getInteger("curPage").intValue() - 1) * ajaxParamObj.getInteger("curRowNum").intValue();

        String param = "?service=" + ajaxParamObj.get("service") + "&offset=" + offset + "&limit=" + ajaxParamObj.getString("curRowNum") +
                "&orderby=name&ascend=true&name=" + ajaxParamObj.get("name");

        JSONObject returnInfo = restDao.invoke("GET",  "/api/usergroup/" + ajaxParamObj.getString("selectgroup") + "/resource" + param, mapIn);

        if ("0".equals(returnInfo.get("errcode"))) {
            JSONArray gridRes = new JSONArray();
            JSONArray resource = returnInfo.getJSONArray("resources");

            //GridData gridData = new GridData(returnInfo.getInteger("total_count"), gridRes);
            GridData gridData = new GridData(returnInfo.getInteger("total_count"), resource);

            gridData.fillGridAttribute(ajaxParam);
            JSONObject outJSONObj = JSONObject.parseObject(JSON.toJSONString(gridData));

            outJSONObj.put("status", "0");
            outJSONObj.put("returnMsg", "查询成功！本次返回" + returnInfo.getJSONArray("resources").size() + "条记录，总共" + gridData.getRecords() + "条记录！");

            return outJSONObj;
        }
        GridData gridData = new GridData(Integer.valueOf(0), new ArrayList());
        gridData.fillGridAttribute(ajaxParam);
        JSONObject outJSONObj = JSONObject.parseObject(JSON.toJSONString(gridData));

        outJSONObj.put("status", "-1");
        outJSONObj.put("returnMsg", "错误代码： " + returnInfo.getString("errcode") + " 错误信息： " + returnInfo.getString("errinfo"));

        return outJSONObj;

    }




    public JSONObject insertRows(String ajaxParam){
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);

        JSONObject mapIn = new JSONObject();

        mapIn.put("resources", ajaxParamObj.getJSONArray("resource"));
        JSONObject returnInfo = restDao.invoke("PUT",  "/api/usergroup/" + ajaxParamObj.getString("selectgroup") + "/resource", mapIn);

        if ("0".equals(returnInfo.get("errcode")))
        {
            returnInfo.put("status", "0");
            returnInfo.put("returnMsg", "为群组授权资源成功！");
            return returnInfo;
        }
        returnInfo.put("status", "-1");
        returnInfo.put("returnMsg", "错误代码： " + returnInfo.getString("errcode") + " 错误信息： " + returnInfo.getString("errinfo"));

        return returnInfo;
    }


    public JSONObject deleteRows(String ajaxParam,String token){
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        restDao.setHost(PropertiesValue.AAS_ADRESS);
        restDao.setServiceName(PropertiesValue.AAS_APP_PATH);

        JSONObject mapIn = new JSONObject();
        mapIn.put("token",token);

        String param = "?name=" + ajaxParamObj.getString("name") + "&service=" + ajaxParamObj.getString("service");

        JSONObject returnInfo = restDao.invoke("DELETE",  "/api/usergroup/" + ajaxParamObj.getString("selectgroup") + "/resource" + param, mapIn);

        if ("0".equals(returnInfo.get("errcode")))
        {
            returnInfo.put("status", "0");
            returnInfo.put("returnMsg", "为角色删除资源成功！");
            return returnInfo;
        }
        returnInfo.put("status", "-1");
        returnInfo.put("returnMsg", "错误代码： " + returnInfo.getString("errcode") + " 错误信息： " + returnInfo.getString("errinfo"));

        return null;
    }

    public JSONArray getProjectResources(String groupNamesStr){
        ArrayList<String> groupNamesList = new ArrayList<>();
        if (groupNamesStr.indexOf(",") > 0) {
            String[] split = groupNamesStr.split(",");
            groupNamesList = new ArrayList<String>(Arrays.asList(split));
        } else {
            groupNamesList.add(groupNamesStr);
        }


        return null;
    }
}
