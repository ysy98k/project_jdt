package com.raising.backstage.service.privilege;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.common.NewBaseService;
import com.raising.backstage.entity.Role;
import com.raising.forward.service.PropertiesValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 角色管理
 */

@Service(value = "roleManagementService")
public class RoleManagementService extends NewBaseService {

    public JSONObject getRows(JSONObject paramInfo){
        JSONObject returnInfo = new JSONObject();
        String querySql = "com.raising.backstage.entity.Role.getTree";
        List<JSONObject> roles = sqlSessionTemplate.selectList(querySql, paramInfo);
        //去重
        List<Integer> checkInteger = new ArrayList<>();
        for(int i=roles.size()-1;i>= 0;i--){
            JSONObject roleTemp = roles.get(i);
            int tempId = roleTemp.getIntValue("groupId");
            if(checkInteger.contains(tempId)){
                roles.remove(i);
            }else{
                checkInteger.add(tempId);
            }
        }
        returnInfo.put("status", Constants.EXECUTE_SUCCESS);
        returnInfo.put("rows", roles);
        return returnInfo;
    }

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public JSONObject addRows(JSONObject paramJson,String token) {
        JSONObject returnInfo = new JSONObject();
        restDao.setHost(PropertiesValue.AAS_ADRESS);
        restDao.setServiceName(PropertiesValue.AAS_APP_PATH);
        Integer groupId = null;
        Integer parentId = paramJson.getInteger("parentId");
        String groupName = paramJson.getString("name");

        paramJson.remove("parentId");
        JSONObject mapIn = new JSONObject();
        mapIn.put("token",token);
        JSONArray groups = new JSONArray();
        groups.add(paramJson);
        mapIn.put("groups",groups);

        returnInfo = restDao.invoke("POST", "/api/usergroup", mapIn);
        if("0".equals(returnInfo.get("errcode"))){//如果插入成功。查询出groupId，并修改parentId值
            String querySql = "com.raising.backstage.entity.Role.getRows";
            JSONObject  groupParam = new JSONObject();
            groupParam.put("groupName",paramJson.getString("name"));
            List<JSONObject> groupList =  sqlSessionTemplate.selectList(querySql,groupParam);
            JSONObject group = groupList.get(0);
            groupId = group.getInteger("groupId");
            groupParam.remove("groupName");
            //修改parentId值
            JSONObject updateParam = new JSONObject();
            updateParam.put("parentId",parentId);
            updateParam.put("groupId",groupId);
            String sql = "com.raising.backstage.entity.Role.updateRow";
            int update = sqlSessionTemplate.update(sql, updateParam);
        }else{
            returnInfo.put("status", Constants.EXECUTE_FAIL);
            returnInfo.put("message",  "错误代码： " + returnInfo.getString("errcode") + " 错误信息： " + returnInfo.getString("errinfo").replace("群组","角色"));
            return returnInfo;
        }
        //新增角色后，添加默认资源AAS资源
        JSONObject addPower = addDefaultAASPower(groupName,token);
        returnInfo.put("groupId",groupId);
        returnInfo.put("message","新增角色成功。");
        return returnInfo;
    }

    /**
     * 添加AAS默认权限。
     * @return
     */
    public JSONObject addDefaultAASPower(String groupName,String token){
        JSONObject returnInfo = new JSONObject();
        String[] aas = {"User","Group","Resource","CCS","Member","Permission","Organization","OrganizationUser","Validity"};
        JSONArray resources = new JSONArray();
        for(int i=0;i<aas.length;i++){
            String temp = aas[i];
            JSONObject resource = new JSONObject();
            resource.put("name",temp);
            resource.put("service","AAS");
            JSONObject action = new JSONObject();
            action.put("DELETE","true");
            action.put("GET","true");
            action.put("POST","true");
            action.put("PUT","true");
            resource.put("action",action);
            resources.add(resource);
        }
        restDao.setHost(PropertiesValue.AAS_ADRESS);
        restDao.setServiceName(PropertiesValue.AAS_APP_PATH);
        JSONObject mapIn = new JSONObject();
        mapIn.put("token",token);
        mapIn.put("resources",resources);
        returnInfo = restDao.invoke("PUT", "/api/usergroup/"+groupName+"/resource", mapIn);
        return returnInfo;
    }


    public JSONObject updateRows(JSONObject paramJson,String token) {
        JSONObject returnInfo = new JSONObject();

        restDao.setHost(PropertiesValue.AAS_ADRESS);
        restDao.setServiceName(PropertiesValue.AAS_APP_PATH);

        JSONObject mapIn = new JSONObject();
        mapIn.put("token",token);
        JSONArray groups = new JSONArray();
        groups.add(paramJson);
        mapIn.put("groups",groups);

        returnInfo = restDao.invoke("PUT", "/api/usergroup", mapIn);

        if("0".equals(returnInfo.get("errcode"))){
            returnInfo.put("status", "0");
            returnInfo.put("message", "修改角色成功！");
        }else{
            returnInfo.put("status", Constants.EXECUTE_FAIL);
            returnInfo.put("message",  "错误代码： " + returnInfo.getString("errcode") + " 错误信息： " + returnInfo.getString("errinfo").replace("群组","角色"));
        }
        return returnInfo;
    }

    public JSONObject delete(JSONArray paramArray,String token){
        JSONObject returnInfo = new JSONObject();
        restDao.setHost(PropertiesValue.AAS_ADRESS);
        restDao.setServiceName(PropertiesValue.AAS_APP_PATH);
        Integer groupId = null;

        String[] name = paramArray.toArray( new String[paramArray.size()]);
        String nameParam = StringUtils.join(name,",");

        JSONObject mapIn = new JSONObject();
        mapIn.put("token",token);
        returnInfo = restDao.invoke("DELETE", "/api/usergroup?name="+nameParam, mapIn);

        if ("0".equals(returnInfo.get("errcode")))
        {
            returnInfo.put("status", "0");
            returnInfo.put("message", "删除角色成功！");
            return returnInfo;
        }
        returnInfo.put("status", "-1");
        returnInfo.put("message", "错误代码： " + returnInfo.getString("errcode") + " 错误信息： " + returnInfo.getString("errinfo").replace("群组","角色"));
        return returnInfo;
    }

    /**
     * 检查默认群组是否存在。
     * @param groupName
     * @param parentId
     * @return
     */
    public JSONObject checkDefault(String groupName,Integer parentId){
        JSONObject returnInfo = new JSONObject();
        String checkSql = "com.raising.backstage.entity.Role.getRows";
        JSONObject checkJson = new JSONObject();
        checkJson.put("groupName",groupName);
        JSONObject group = sqlSessionTemplate.selectOne(checkSql, checkJson);
        if(group != null && group.getInteger("groupId") != null){
            returnInfo.put("exists",Constants.EXECUTE_SUCCESS);
            returnInfo.put("groupId",group.getInteger("groupId"));
            return returnInfo;
        }
        returnInfo.put("exists",Constants.EXECUTE_FAIL);
        return returnInfo;
    }

}
