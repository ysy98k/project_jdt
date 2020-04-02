package com.raising.backstage.service.privilege;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.common.NewBaseService;
import com.raising.forward.service.PropertiesValue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Transactional
@Service
public class BackstageUserService extends NewBaseService {



    /**
     * 根据userIds集合获取所有User
     * @param paramInfo
     * @return
     */
    public JSONObject getRowsWithUserIds(JSONObject paramInfo){
        JSONObject returnInfo = new JSONObject();
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        List<Integer> userIds = (List<Integer>)inquStatus.getObject("userIds",List.class);

        if(userIds == null || userIds.size() < 1){
            returnInfo.put("status", Constants.EXECUTE_SUCCESS);
            returnInfo.put("returnMsg", "查询成功！本次返回0条记录，总共0条记录！");
            returnInfo.put("total", "0");
            returnInfo.put("rows", new JSONArray());
            return returnInfo;
        }

        String querySql = "com.raising.forward.mapper.UserDao.getRows";
        String countSql = "com.raising.forward.mapper.UserDao.count";
        String entity = "com.raising.backstage.entity.User";
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, querySql);
        paramInfo.put(BaseService.COUNT_SQL, countSql);
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        returnInfo = daoUtil.query(paramInfo);
        return returnInfo;
    }


    public JSONObject getRows(String paramStr){
        JSONObject returnInfo = new JSONObject();
        JSONObject paramInfo = JSONObject.parseObject(paramStr);
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        String querySql = "com.raising.forward.mapper.UserDao.getRows";
        String countSql = "com.raising.forward.mapper.UserDao.count";
        String entity = "com.raising.backstage.entity.User";
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, querySql);
        paramInfo.put(BaseService.COUNT_SQL, countSql);
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        returnInfo = daoUtil.query(paramInfo);
        return returnInfo;
    }

    /**
     * 根据传来的组名，获取其下所有后代组
     * @param ajaxParam
     * @return
     */
    public List<JSONObject> getProgenyGroup(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        ArrayList<String> groupNamesList = null;
        if(ajaxParam.indexOf(",")>0){
            String[] split = ajaxParam.split(",");
            groupNamesList = new ArrayList<String>(Arrays.asList(split));
        }else{
            groupNamesList = new ArrayList<String>();
            groupNamesList.add(ajaxParam);
        }
        JSONObject paramInfo = new JSONObject();
        paramInfo.put("groupNames",groupNamesList);
        String querySql = "com.raising.backstage.entity.Role.getTree";
        List<JSONObject> roles = sqlSessionTemplate.selectList(querySql, paramInfo);
        return roles;
    }

    public JSONObject addRow(JSONObject data,String token) {
        JSONObject returnInfo = new JSONObject();

        restDao.setHost(PropertiesValue.AAS_ADRESS);
        restDao.setServiceName(PropertiesValue.AAS_APP_PATH);

        data.put("tenantId",10000);
        JSONArray resultRow = new JSONArray();
        data.put("display_name",data.getString("displayName"));
        resultRow.add(data);

        JSONObject mapIn = new JSONObject();
        mapIn.put("users",resultRow);
        mapIn.put("token",token);
        JSONObject post = restDao.invoke("POST", "/api/user", mapIn);
        if ("0".equals(post.get("errcode"))){//添加成功则查询用户信息

            returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        }else{//用户添加失败
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("message",post.getString("errinfo")+"。用户添加失败！");
        }
        return returnInfo;

    }
    public JSONObject strictQuery(JSONObject data){
        String sql = "com.raising.forward.mapper.UserDao.strictQuery";
        List<JSONObject> usersList =  sqlSessionTemplate.selectList(sql,data);
        JSONObject user = usersList.get(0);
        user.put("status",Constants.EXECUTE_SUCCESS);
        return  user;

    }

    /**
     * 删除用户
     * @param ajaxParam
     * @return
     * @throws Exception
     */
    public JSONObject deleteRows(String ajaxParam) throws Exception{
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONArray dataArray = paramInfo.getJSONObject("detail").getJSONArray("resultRow");
        for(int i=0;i<dataArray.size();i++){
            JSONObject dataJson = dataArray.getJSONObject(i);
            JSONObject temp = new JSONObject();
            temp.put("userId",dataJson.getInteger("userId"));
            temp.put("deleteFlag",dataJson.getInteger("userId"));
            dataArray.set(i,temp);
        }
        String sql = "com.raising.forward.mapper.UserDao.updateRow";
        String entity = "com.raising.backstage.entity.User";
        paramInfo.put(BaseService.UPDATE_SQL, sql);
        paramInfo.put(BaseService.RESULT_BLOCK, "detail");
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        JSONObject returnInfo = daoUtil.update(paramInfo);
        if(Constants.EXECUTE_SUCCESS.equals(returnInfo.getString("status")) ){
            returnInfo.put("returnMsg","删除了一条记录");
        }
        return returnInfo;
    }

    public JSONObject updateRows(String ajaxParam,String code) throws Exception {
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONObject ajaxParamObj = paramInfo.getJSONObject("result");
        JSONArray aJaxParamArr = ajaxParamObj.getJSONArray("resultRow");
        for(int i=0;i<aJaxParamArr.size();i++){
            JSONObject oneRecord = aJaxParamArr.getJSONObject(i);
            oneRecord.put("userId",oneRecord.getInteger("userId"));
            if("禁用".equals(code)){
                oneRecord.put("validity","1970-01-01 00:00:00:000");
                continue;
            }else if("永久有效".equals(code)){
                oneRecord.put("validity","9999-01-01 00:00:00:000");
                continue;
            }else if("设置有效期".equals(code)){
                continue;
            }
        }
        String sql = "com.raising.forward.mapper.UserDao.updateRow";
        String entity = "com.raising.backstage.entity.User";
        paramInfo.put(BaseService.UPDATE_SQL, sql);
        paramInfo.put(BaseService.RESULT_BLOCK, "result");
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        JSONObject returnInfo = daoUtil.update(paramInfo);
        if(Constants.EXECUTE_SUCCESS.equals(returnInfo.getString("status"))){
            if("禁用".equals(code)){
                returnInfo.put("returnMsg", "用户禁用成功，将在5分钟后生效。");
            }else if("永久有效".equals(code)){
                returnInfo.put("returnMsg","设置永久有效成功，将在5分钟后生效。");
            }
        }else{
            if("禁用".equals(code)){
                throw new Exception("用户禁用失败！错误信息：" + returnInfo.getString("returnMsg"));
            }else if("永久有效".equals(code)){
                throw new Exception( "设置永久有效失败！ 错误信息：" + returnInfo.getString("returnMsg"));
            }
        }
        return returnInfo;
    }



}
