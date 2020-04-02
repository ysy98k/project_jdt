package com.raising.rest.aasPublic.privateRest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.common.BaseController;
import com.util.CSVUtils;
import com.util.MultipleDataSource;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 角色管理——角色成员管理Controller
 */
@Component
@Path("/raising")
public class RoleUserRest extends BaseController {



    /**
     * 查询当前群组的用户
     * @param ajaxParam
     * @return
     */
    @POST
    @Path("/roleUserRest/getRows.do")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject getRows(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            returnInfo = roleUserService.getRows(ajaxParam);

        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return returnInfo;
    }

    @POST
    @Path("/roleUserRest/delete.do")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject delete(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject paramJson = JSONObject.parseObject(ajaxParam);
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            //预处理
            //删除用户与群组关系时，做校验。判断用户所在群组数量。如果数量为1..需要把删除用户放至默认群组。如果默认群组不存在，需要创建默认群组。
            //如果数量不为1。直接删除用户与群组关系
            JSONObject temp = pushDefaultGroup(paramJson);
            if(Constants.EXECUTE_FAIL.equals(temp.getString("status"))){
                return temp;
            }
            String defaultGroupMember = temp.getString("defaultGroupMember");
            String returnMsg = temp.getString("returnMsg");
            //做好预处理以后。删除所有用户
            try {
                Integer userId = paramJson.getInteger("userId");
                returnInfo = roleUserService.deleteRows(ajaxParam,userId);
                if(defaultGroupMember != null){
                    returnInfo.put("returnMsg",returnInfo.getString("returnMsg")+";"+defaultGroupMember+"删除失败！用户至少拥有一个角色");
                }
                if(returnMsg !=null){
                    returnInfo.put("returnMsg",returnInfo.getString("returnMsg")+";"+returnMsg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                returnInfo.put("status", Constants.EXECUTE_FAIL);
                returnInfo.put("returnMsg","删除失败");
            }
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return returnInfo;
    }

    /**
     * 删除用户与群组关系时，做校验。判断用户所在群组数量。如果数量为1..需要把删除用户放至默认群组。如果默认群组不存在，需要创建默认群组。
     * 如果数量不为1。直接删除用户与群组关系
     * @param paramJson
     * @return
     */
    private JSONObject pushDefaultGroup(JSONObject paramJson){
        JSONObject returnInfo = new JSONObject();
        String defaultGroupMember = null;
        String message = null;

        JSONObject addDefaultResult = new JSONObject();
        Integer defaultGroupId = null;//默认群组Id

        String token = paramJson.getString("token");
        Integer userId = paramJson.getInteger("userId");
        JSONArray dataArray = paramJson.getJSONObject("result").getJSONArray("resultRow");
        JSONObject checkResult = roleUserService.deleteUserCheck(dataArray);
        List<Integer> defaultGroupUserIds = checkResult.getObject("defaultGroupUserIds",List.class);
        List<JSONObject> defaultGroupUsers = checkResult.getObject("defaultGroupUsers",List.class);
        //将单群组用户，添加至默认组
        if(defaultGroupUserIds.size() > 0){
            Integer parentId = paramJson.getInteger("currentUserGroupId");
            String parentName = paramJson.getString("currentUserGroupName");
            String selectGroupName = paramJson.getString("selectGroupName");
            StringBuffer deleteUserNames = new StringBuffer();
            for(int i =0;i<defaultGroupUsers.size();i++){
                String username = defaultGroupUsers.get(i).getString("username");
                deleteUserNames.append(username).append(",");
            }
            if( selectGroupName.indexOf("_default")>=0 &&  (selectGroupName.length() - selectGroupName.lastIndexOf("_default")) == 8 ){//如果当前是默认组，则不删除。
                defaultGroupMember = deleteUserNames.toString();
                returnInfo.put("status", Constants.EXECUTE_FAIL);
                returnInfo.put("returnMsg","删除失败！用户至少有一个群组！无法删除"+defaultGroupMember+"用户");
                return returnInfo;
            }

            JSONObject  checkDefaultGroup = roleManagementService.checkDefault(parentName+"_default",parentId);
            if("-1".equals(checkDefaultGroup.getString("exists")) ){//如果默认群组不存在则创建
                //如果不存在则创建
                JSONObject defaultGroupParam = new JSONObject();
                defaultGroupParam.put("name",parentName+"_default");
                defaultGroupParam.put("display_name",parentName+"_default");
                defaultGroupParam.put("description",parentName+"_default");
                defaultGroupParam.put("parentId",parentId);
                addDefaultResult = roleManagementService.addRows(defaultGroupParam,token);
                if("-1".equals(addDefaultResult.getString("status")) ){//如果添加默认组失败，则删除失败。直接返回
                    if(addDefaultResult.getString("message").indexOf("新增角色成功") < 0){//如果新增角色失败
                        returnInfo.put("status", Constants.EXECUTE_FAIL);
                        returnInfo.put("returnMsg","删除失败！用户至少有一个群组！创建默认群组失败！无法将"+deleteUserNames+"用户添加至默认群组。");
                        return  returnInfo;
                    }
                    //如果新增角色成功，但是删除用户失败
                    message = "为默认群组"+parentName+"_default注册默认资源失败！请单独注册！";
                }
                defaultGroupId = addDefaultResult.getInteger("groupId");
            }else{
                defaultGroupId = checkDefaultGroup.getInteger("groupId");
            }
            //如果添加群组成功。那么先将要删除用户与默认群组关联
            JSONObject bindUserJson = new JSONObject();
            bindUserJson.put("detail",new JSONObject());
            JSONArray bindData = new JSONArray();
            bindUserJson.getJSONObject("detail").put("resultRow",bindData);
            for(int i =0;i<defaultGroupUserIds.size();i++){
                JSONObject groupMember = new JSONObject();
                groupMember.put("userId",defaultGroupUserIds.get(i));
                groupMember.put("groupId",defaultGroupId);
                bindData.add(groupMember);
            }
            try {
                roleUserService.bindUser(bindUserJson.toJSONString(),userId);
            } catch (Exception e) {
                e.printStackTrace();
                returnInfo.put("status", Constants.EXECUTE_FAIL);
                returnInfo.put("returnMsg","删除失败！用户至少有一个群组！将"+deleteUserNames+"用户添加至默认群组。");
                return returnInfo;
            }
        }
        returnInfo.put("status", Constants.EXECUTE_SUCCESS);
        returnInfo.put("defaultGroupMember",defaultGroupMember);
        returnInfo.put("returnMsg",message);
        return returnInfo;
    }

    /**
     * 给当前群组添加用户。查询出后代群组的用户信息（权限过滤）。不显示当前登录用户。
     * @param ajaxParam
     * @return
     */
    @POST
    @Path("/roleUserRest/getUserSelectRows.do")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject getUserSelectRows(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject paramJson = JSONObject.parseObject(ajaxParam);
        JSONObject inqu_status = paramJson.getJSONObject("inqu_status");
        String groupNamesStr = paramJson.getString("groupNames");
        Integer userId = paramJson.getInteger("userId");
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            //1.根据当前用户所在的组。获取其所有后代组的Id
            String[] groupNamesArr = groupNamesStr.split(",");
            ArrayList<String> groupNames = new ArrayList<>(Arrays.asList(groupNamesArr));
            JSONObject roleJson = new JSONObject();
            roleJson.put("groupNames", groupNames);
            returnInfo = roleManagementService.getRows(roleJson);
            JSONArray roleArray = returnInfo.getJSONArray("rows");
            //2.根据组Id集合，获取所有的User
            List<Integer> userIds = roleUserService.getUserIds(roleArray,inqu_status.getInteger("selectGroupId"));
            //3.移除当前登陆用户的UserId
            userIds.remove(userId);
            inqu_status.put("userIds",userIds);
            paramJson.put("inqu_status",inqu_status);
            //4.查找用户
            returnInfo = backstageUserService.getRowsWithUserIds(paramJson);

        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return returnInfo;
    }

    /**
     * 建立用户与角色的关系
     * @param ajaxParam
     * @return
     */
    @POST
    @Path("/roleUserRest/bindUser.do")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject bindUser(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        Integer userId = paramInfo.getInteger("userId");
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            try {
                returnInfo = roleUserService.bindUser(ajaxParam,userId);
            } catch (Exception e) {
                e.printStackTrace();
                returnInfo.put("status", Constants.EXECUTE_FAIL);
                returnInfo.put("message","添加失败");
            }
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return returnInfo;
    }


}
