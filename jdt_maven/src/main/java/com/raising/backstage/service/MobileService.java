package com.raising.backstage.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.common.NewBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
@Service("mobileService")
public class MobileService extends NewBaseService {


    public JSONObject getRows(String ajaxParam){
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = new JSONObject();
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        String querySql = "com.raising.backstage.entity.Mobile.getRows";
        String countSql = "com.raising.backstage.entity.Mobile.count";
        String entity = "com.raising.backstage.entity.Mobile";
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, querySql);
        paramInfo.put(BaseService.COUNT_SQL, countSql);
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        returnInfo = daoUtil.query(paramInfo);
        return returnInfo;
    }

    public JSONObject addRows(String ajaxParam) throws Exception {
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONArray dataArray = paramInfo.getJSONObject("detail").getJSONArray("resultRow");
        for(int i=0;i<dataArray.size();i++){
            JSONObject dataJson = dataArray.getJSONObject(i);
            String temp = dataJson.getString("userName");
            dataJson.remove("userName");
            String[] split = temp.split("_");
            dataJson.put("userId",Integer.parseInt(split[0]));
            dataJson.put("tenantId",Integer.parseInt(split[1]));
            dataJson.put("groupName","undefined".equals(split[2]) ? "" : split[2]);
            dataJson.put("bind","false");

        }
        String insertSql = "com.raising.backstage.entity.Mobile.add";
        String entity = "com.raising.backstage.entity.Mobile";
        paramInfo.put(BaseService.RESULT_BLOCK,"detail");
        paramInfo.put(BaseService.INSERT_SQL, insertSql);
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        JSONObject returnInfo = daoUtil.insert(paramInfo);
        return returnInfo;
    }

    public JSONObject updateRows(String ajaxParam) throws Exception {
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONArray dataArray = paramInfo.getJSONObject("detail").getJSONArray("resultRow");
        for(int i=0;i<dataArray.size();i++){
            JSONObject dataJson = dataArray.getJSONObject(i);
            String temp = dataJson.getString("userName");
            dataJson.remove("userName");
            String[] split = temp.split("_");
            dataJson.put("userId",Integer.parseInt(split[0]));
            dataJson.put("tenantId",Integer.parseInt(split[1]));
            dataJson.put("groupName","undefined".equals(split[2]) ? "" : split[2]);
            dataJson.remove("bind");
        }
        String updateSql = "com.raising.backstage.entity.Mobile.update";
        String entity = "com.raising.backstage.entity.Mobile";
        paramInfo.put(BaseService.RESULT_BLOCK,"detail");
        paramInfo.put(BaseService.UPDATE_SQL, updateSql);
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        JSONObject returnInfo = daoUtil.update(paramInfo);
        return returnInfo;
    }

    public JSONObject delete(String ajaxParam) throws Exception {
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONArray dataArray = paramInfo.getJSONObject("result").getJSONArray("resultRow");
        for(int i=0;i<dataArray.size();i++){
            JSONObject dataJson = dataArray.getJSONObject(i);
            dataJson.put("id",dataJson.getIntValue("id"));
        }
        String deleteSql = "com.raising.backstage.entity.Mobile.delete";
        String entity = "com.raising.backstage.entity.Mobile";
        paramInfo.put(BaseService.DELETE_SQL, deleteSql);
        paramInfo.put(BaseService.RESULT_BLOCK, "result");
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        JSONObject returnInfo = daoUtil.delete(paramInfo);
        return returnInfo;
    }


    /**
     * 根据openId得到用户信息
     * @param paramJson
     * @return
     */
    public List<JSONObject> getUsers(JSONObject paramJson){
        String sql = "com.raising.backstage.entity.Mobile.getUsers";
        List result = sqlSessionTemplate.selectList(sql,paramJson);
        return result;
    }

    public JSONObject login(String openId){
        String sql = "com.raising.backstage.entity.Mobile.login";
        JSONObject paramJson = new JSONObject();
        paramJson.put("openId",openId);
        JSONObject result = sqlSessionTemplate.selectOne(sql,paramJson);
        return result;
    }

    /**
     * 根据电话号绑定OpenId
     * @param paramJson
     * @return
     */
    public JSONObject bindingOpenId(JSONObject paramJson){
        JSONObject returnInfo = new JSONObject();
        String sql = "com.raising.backstage.entity.Mobile.bindingOpenId";
        int result = sqlSessionTemplate.update(sql,paramJson);
        try {
            if (result <= 0) {
                throw new Exception();
            }else{
                returnInfo.put("status", Constants.EXECUTE_SUCCESS);
                return returnInfo;
            }
        }catch (Exception e){
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            return returnInfo;
        }

    }


}
