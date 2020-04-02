package com.raising.backstage.service.privilege;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.common.NewBaseService;
import com.raising.forward.service.PropertiesValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserManageService extends NewBaseService {

    public JSONObject addRow(String ajaxParam) throws Exception {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        restDao.setHost(PropertiesValue.AAS_ADRESS);
        restDao.setServiceName(PropertiesValue.AAS_APP_PATH);

        JSONObject mapIn = new JSONObject();
        mapIn.put("token",request.getSession().getAttribute("token").toString());

        int offset = (ajaxParamObj.getInteger("curPage").intValue() - 1) * ajaxParamObj.getInteger("curRowNum").intValue();

        String param = "?service=" + ajaxParamObj.get("service") + "&offset=" + offset + "&limit=" + ajaxParamObj.getString("curRowNum") +
                "&orderby=name&ascend=true&name=" + ajaxParamObj.get("name");

        JSONObject returnInfo = restDao.invoke("GET", "api/" + ajaxParamObj.getString("selectgroup") + "/resource" + param, mapIn);
        return returnInfo;
    }

    public JSONObject updateRows(String paramStr) throws Exception {
        JSONObject returnInfo = new JSONObject();

        JSONObject paramInfo = JSONObject.parseObject(paramStr);
        JSONArray dataArray = paramInfo.getJSONObject("result").getJSONArray("resultRow");
        for(int i=0;i<dataArray.size();i++){
            JSONObject dataJson = dataArray.getJSONObject(i);
            dataJson.put("roleMemberId",dataJson.getIntValue("roleMemberId"));
            dataJson.put("modifyPersonId",dataJson.getIntValue("modifyPersonId"));
        }
        String deleteSql = "com.raising.backstage.entity.RoleUser.deleteRows";
        String entity = "com.raising.backstage.entity.RoleUser";
        paramInfo.put(BaseService.DELETE_SQL, deleteSql);
        paramInfo.put(BaseService.RESULT_BLOCK, "result");
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        returnInfo = daoUtil.delete(paramInfo);
        return returnInfo;

    }

    public JSONObject deleteRows(String paramStr) throws Exception {
        JSONObject returnInfo = new JSONObject();

        JSONObject paramInfo = JSONObject.parseObject(paramStr);
        JSONArray dataArray = paramInfo.getJSONObject("result").getJSONArray("resultRow");
        for(int i=0;i<dataArray.size();i++){
            JSONObject dataJson = dataArray.getJSONObject(i);
            dataJson.put("roleMemberId",dataJson.getIntValue("roleMemberId"));
            dataJson.put("modifyPersonId",dataJson.getIntValue("modifyPersonId"));
        }
        String deleteSql = "com.raising.backstage.entity.RoleUser.deleteRows";
        String entity = "com.raising.backstage.entity.RoleUser";
        paramInfo.put(BaseService.DELETE_SQL, deleteSql);
        paramInfo.put(BaseService.RESULT_BLOCK, "result");
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        returnInfo = daoUtil.delete(paramInfo);
        return returnInfo;

    }
}
