package com.raising.backstage.service.privilege;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.common.NewBaseService;
import org.activiti.engine.impl.juel.IdentifierNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleUserService extends NewBaseService {
    /**
     * 通过检查表中，是否有用户与群组的关联纪录，来判断是否可以删除角色
     * @param groupIds
     * @return
     */
    public boolean checkDeleteRole(JSONArray groupIds){
        String sql = "com.raising.backstage.entity.RoleUser.getRowsWithGroups";
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("groupIds",groupIds);
        List<Object> objects = sqlSessionTemplate.selectList(sql, paramMap);
        if(objects != null && objects.size() > 0){
           return  false;
        }
        return true;
    }


    public JSONObject getRows(String paramStr){
        JSONObject returnInfo = new JSONObject();

        JSONObject paramInfo = JSONObject.parseObject(paramStr);
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");

        String querySql = "com.raising.backstage.entity.RoleUser.getRows";
        String countSql = "com.raising.backstage.entity.RoleUser.count";
        String entity = "com.raising.backstage.entity.RoleUser";
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, querySql);
        paramInfo.put(BaseService.COUNT_SQL, countSql);
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        returnInfo = daoUtil.query(paramInfo);
        return returnInfo;
    }

    public JSONObject deleteRows(String paramStr,Integer userId) throws Exception {
        JSONObject returnInfo = new JSONObject();

        JSONObject paramInfo = JSONObject.parseObject(paramStr);
        JSONArray dataArray = paramInfo.getJSONObject("result").getJSONArray("resultRow");
        for(int i=0;i<dataArray.size();i++){
            JSONObject dataJson = dataArray.getJSONObject(i);
            dataJson.put("roleMemberId",dataJson.getIntValue("roleMemberId"));
            dataJson.put("modifyPersonId",userId);
        }
        String deleteSql = "com.raising.backstage.entity.RoleUser.deleteRows";
        String entity = "com.raising.backstage.entity.RoleUser";
        paramInfo.put(BaseService.DELETE_SQL, deleteSql);
        paramInfo.put(BaseService.RESULT_BLOCK, "result");
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        returnInfo = daoUtil.delete(paramInfo);
        return returnInfo;
    }

    public List<Integer> getUserIds(JSONArray roleArray,Integer currentGroupId){
        List<JSONObject> groupMemeber = getGroupMemeber(roleArray,currentGroupId);
        List<Integer> userIds = new ArrayList<>();
        for(int i=0;i<groupMemeber.size();i++){
            JSONObject temp =  groupMemeber.get(i);
            if(!userIds.contains(temp.getInteger("userId"))){
                userIds.add(temp.getInteger("userId"));
            }
        }
        return userIds;
    }
    /**
     * 根据组Id集合 和当前组Id，查询在组Id集合不在当前组Id的用户Id集合
     *
     * @param roleArray
     * @param currentGroupId
     * @return
     */
    public List<JSONObject> getGroupMemeber(JSONArray roleArray,Integer currentGroupId){
        List<Integer> groupIds = new ArrayList<>();
        for(int i =0;i<roleArray.size();i++){
            JSONObject role = roleArray.getJSONObject(i);
            groupIds.add(role.getInteger("groupId"));
        }
        String getUserIdsSql = "com.raising.backstage.entity.RoleUser.getUserIds";
        JSONObject getUserParam = new JSONObject();
        getUserParam.put("groupIds",groupIds);
        getUserParam.put("currentGroupId",currentGroupId);
        List<JSONObject> groupMemeber = sqlSessionTemplate.selectList(getUserIdsSql, getUserParam);
        return groupMemeber;

    }

    /**
     * 建立用户与角色的关系
     * @param ajaxParam
     * @return
     */
    public JSONObject bindUser(String ajaxParam,Integer userId) throws Exception {
        JSONObject returnInfo = new JSONObject();

        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONArray resultRow =  paramInfo.getJSONObject("detail").getJSONArray("resultRow");
        for(int i=0;i<resultRow.size();i++){
            JSONObject temp = resultRow.getJSONObject(i);
            temp.put("modifyPersonId",userId);
        }

        String insertSql = "com.raising.backstage.entity.RoleUser.addRow";
        String daoEntity = "com.raising.backstage.entity.RoleUser";
        paramInfo.put(BaseService.DAO_ENTITY,daoEntity);
        paramInfo.put(BaseService.RESULT_BLOCK,"detail");
        paramInfo.put(BaseService.INSERT_SQL, insertSql);
        returnInfo  = daoUtil.insert(paramInfo);
        return returnInfo;
    }

    /**
     * 删除用户与群组关系时，做校验。判断用户所在群组数量。如果数量为1.则返回false。大于1返回true
     * 如果数量为1.需要把删除用户放至默认群组。如果默认群组不存在，需要创建默认群组。
     * @return
     */
    public JSONObject deleteUserCheck(JSONArray dataArray){
        JSONObject returnInfo = new JSONObject();
        List<Integer> allUserIds = new ArrayList<>();
        List<Integer> deleteUserIds = new ArrayList<>();
        List<JSONObject> deleteUsers = new ArrayList<>();
        List<Integer> defaultGroupUserIds = new ArrayList<>();
        List<JSONObject> defaultGroupUsers = new ArrayList<>();
        for(int i=0;i<dataArray.size();i++){
            Integer userId = dataArray.getJSONObject(i).getIntValue("userId");
            allUserIds.add(userId);
        }
        String checkSql = "com.raising.backstage.entity.RoleUser.deleteCheck";
        JSONObject checkJson = new JSONObject();
        checkJson.put("userIds",allUserIds);
        deleteUserIds = sqlSessionTemplate.selectList(checkSql, checkJson);
        for(int i=0;i<dataArray.size();i++){
            int userId = dataArray.getJSONObject(i).getIntValue("userId");
            if(deleteUserIds.contains(userId)){
                deleteUsers.add(dataArray.getJSONObject(i));
            }else{
                defaultGroupUserIds.add(userId);
                defaultGroupUsers.add(dataArray.getJSONObject(i));
            }
        }
        returnInfo.put("deleteUsers",deleteUsers);
        returnInfo.put("defaultGroupUserIds",defaultGroupUserIds);
        returnInfo.put("defaultGroupUsers",defaultGroupUsers);
        return returnInfo;
    }


}
