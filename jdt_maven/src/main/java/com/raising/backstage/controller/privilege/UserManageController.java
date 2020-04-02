package com.raising.backstage.controller.privilege;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.common.BaseController;
import com.util.CSVUtils;
import com.util.MultipleDataSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/raising/backstage/privilege/userManagement")
public class UserManageController extends BaseController {


    /**
     * 获得当前用户所在群组及其后代群组关联的User
     * @param ajaxParam
     * @return
     */
    @RequestMapping(value = "getRows.do",method = RequestMethod.POST)
    public JSONObject getRows(String ajaxParam){
        JSONObject returnInfo = new JSONObject();

        JSONObject paramJson = JSONObject.parseObject(ajaxParam);
        JSONObject inqu_status = paramJson.getJSONObject("inqu_status");
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            //1.根据当前用户所在的组。获取其所有后代组的Id
            String groupNames = inqu_status.getString("groupNames");
            ArrayList groupNamesList = null;
            if(groupNames.indexOf(",") > 0){
                String[] split = groupNames.split(",");
                groupNamesList = new ArrayList<String>(Arrays.asList(split));
            }else{
                groupNamesList = new ArrayList<String>();
                groupNamesList.add(groupNames);
            }

            JSONObject roleJson = new JSONObject();
            roleJson.put("groupNames", groupNamesList);
            returnInfo = roleManagementService.getRows(roleJson);
            JSONArray roleArray = returnInfo.getJSONArray("rows");
            //2.根据组Id集合，获取所有的User
            List<JSONObject> groupMemeber = roleUserService.getGroupMemeber(roleArray,null);
            for(int i=0;i<groupMemeber.size();i++){
                JSONObject temp = groupMemeber.get(i);
                int groupId = temp.getIntValue("groupId");
                for(int j=0;j<roleArray.size();j++){
                    JSONObject temp2 = roleArray.getJSONObject(j);
                    int groupId2 = temp2.getIntValue("groupId");
                    if(groupId == groupId2){
                        temp.put("groupName",temp2.getString("groupName"));
                    }
                }
            }

            List<Integer> userIds = new ArrayList<>();
            int currrentUserId =  Integer.parseInt(request.getSession().getAttribute("userId").toString());//列表中不可以显示当前用户。不提供操作登陆用户的功能
            for(int i=0;i<groupMemeber.size();i++){
                JSONObject temp =  groupMemeber.get(i);
                int tempId =  temp.getInteger("userId");
                if(!userIds.contains(tempId) && (currrentUserId != tempId)){
                    userIds.add(temp.getInteger("userId"));
                }
            }
            inqu_status.put("userIds",userIds);
            paramJson.put("inqu_status",inqu_status);
            inqu_status.remove("groupNames");
            returnInfo = backstageUserService.getRowsWithUserIds(paramJson);

            JSONArray rows =  returnInfo.getJSONArray("rows");
            for(int i =0;i<rows.size();i++){
                JSONObject temp = rows.getJSONObject(i);
                if(!temp.containsKey("validity")){
                    temp.put("validity","9999-01-01 00:00:00");
                }
                //根据userId.匹配。找出每个用户 所在的角色
                int currentUserId = temp.getIntValue("userId");
                ArrayList<String> groupNamesListTemp = new ArrayList<>();
                for(int j=0;j<groupMemeber.size();j++){
                    if(currentUserId == groupMemeber.get(j).getIntValue("userId")){
                        groupNamesListTemp.add(groupMemeber.get(j).getString("groupName"));
                    }
                }
                String groupNamesStr = StringUtils.join(groupNamesListTemp.toArray(), ",");
                temp.put("groupNamesStr",groupNamesStr);

            }
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return returnInfo;
    }

    @RequestMapping(value = "getProgenyGroup.do",method = RequestMethod.GET)
    public JSONObject getProgenyGroup(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            List<JSONObject> progenyGroup = backstageUserService.getProgenyGroup(ajaxParam);
            returnInfo.put("status", Constants.EXECUTE_SUCCESS);
            returnInfo.put("groupList",progenyGroup);
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return returnInfo;
    }

    @RequestMapping(value = "insert.do",method = RequestMethod.POST)
    public JSONObject insert(String ajaxParam) {
        JSONObject returnInfo = new JSONObject();
        JSONObject paramJson = JSONObject.parseObject(ajaxParam);
        String token = request.getSession().getAttribute("token").toString();
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            JSONObject data = paramJson.getJSONObject("detail").getJSONArray("resultRow").getJSONObject(0);
            //如果当前用户没有后代分组。指定了默认分组时。需要先创建默认分组。
            boolean addDefalutGroup = false;
            if(data.getString("groupId").indexOf("_default") > 0 ){
                addDefalutGroup = true;
                JSONObject groupJson = new JSONObject();
                groupJson.put("name",data.getString("groupId"));
                groupJson.put("display_name",data.getString("groupId"));
                groupJson.put("description",data.getString("groupId"));
                groupJson.put("parentId",data.getInteger("parentGroupId"));
                groupJson.put("tenantId",10000);
                JSONObject insertGroup = roleManagementService.addRows(groupJson,token);//如果失败抛出异常被捕获
                data.put("groupId",insertGroup.getInteger("groupId"));
            }
            returnInfo = backstageUserService.addRow(data,request.getSession().getAttribute("token").toString());
            if(!Constants.EXECUTE_SUCCESS.equals(returnInfo.getString("status"))){
                returnInfo.put("status",Constants.EXECUTE_FAIL);
                if(addDefalutGroup){
                    returnInfo.put("groupId",data.getInteger("groupId"));
                }
                returnInfo.put("returnMsg",returnInfo.getString("message"));
                return returnInfo;
            }
            //
            JSONObject userObj = backstageUserService.strictQuery(data);
            Integer currentUserId = paramJson.getInteger("currentUserId");
            Integer userId = userObj.getInteger("userId");
            JSONObject groupUserJson = new JSONObject();
            groupUserJson.put("userId",userId);
            groupUserJson.put("groupId",data.getInteger("groupId"));
            groupUserJson.put("modifyPersonId",currentUserId);

            JSONObject groupUserParam = new JSONObject();
            groupUserParam.put("detail",new JSONObject());
            groupUserParam.getJSONObject("detail").put("resultRow",new JSONArray());
            groupUserParam.getJSONObject("detail").getJSONArray("resultRow").add(groupUserJson);
            returnInfo = roleUserService.bindUser(groupUserParam.toJSONString(),currentUserId);

            if(!Constants.EXECUTE_SUCCESS.equals(returnInfo.getString("status"))){
                //如果出错，删除用户
                JSONObject deleteUser = new JSONObject();
                deleteUser.put("userId",userId);

                JSONObject userJson = new JSONObject();
                userJson.put("result",new JSONObject());
                userJson.getJSONObject("result").put("resultRow",new JSONArray());
                userJson.getJSONObject("result").getJSONArray("resultRow").add(deleteUser);
                backstageUserService.deleteRows(userJson.toJSONString());
                returnInfo.put("returnMsg","建立角色与用户的关系失败！");
            }else{
                if(addDefalutGroup){
                    returnInfo.put("groupId",data.getInteger("groupId"));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            //String message = null;
            String message = e.getMessage().indexOf("失败！") > 0 ? e.getMessage() : "用户添加失败！";
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("returnMsg",message);
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return returnInfo;
    }

    @RequestMapping(value = "update.do",method = RequestMethod.POST)
    public JSONObject update(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            try {

                returnInfo = backstageUserService.updateRows(ajaxParam,"修改");
            } catch (Exception e) {
                e.printStackTrace();
                returnInfo.put("status", Constants.EXECUTE_FAIL);
                returnInfo.put("message","修改失败，唯一键错误，请修改内容以后在提交！");
            }
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return returnInfo;
    }


    @RequestMapping(value = "delete.do",method = RequestMethod.POST)
    public JSONObject delete(String ajaxParam) {
        JSONObject returnInfo = new JSONObject();
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            try {
                returnInfo = backstageUserService.deleteRows(ajaxParam);
            } catch (Exception e) {
                e.printStackTrace();
                returnInfo.put("status", Constants.EXECUTE_FAIL);
                returnInfo.put("message","删除失败");
            }
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return returnInfo;
    }



    /**
     * 设置有效期
     * @param ajaxParam
     * @return
     */
    @RequestMapping(value = "setValidity.do",method = RequestMethod.POST)
    public JSONObject setValidity(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            try {
                returnInfo = backstageUserService.updateRows(ajaxParam,"设置有效期");
            } catch (Exception e) {
                e.printStackTrace();
                returnInfo.put("status", "-1");
                returnInfo.put("returnMsg", "调用禁用有效期接口失败！" + e.toString());
            }
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return returnInfo;
    }

    /**
     * 禁用
     * @param ajaxParam
     * @return
     */
    @RequestMapping(value = "abandonValidity.do",method = RequestMethod.POST)
    public JSONObject abandonValidity(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            try {
                returnInfo = backstageUserService.updateRows(ajaxParam,"禁用");
            } catch (Exception e) {
                e.printStackTrace();
                returnInfo.put("status", "-1");
                returnInfo.put("returnMsg", "调用禁用有效期接口失败！" + e.toString());
            }
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return returnInfo;
    }

    /**
     * 永久有效
     * @param ajaxParam
     * @return
     */
    @RequestMapping(value = "keepValidity.do",method = RequestMethod.POST)
    public JSONObject keepValidity(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            try {
                returnInfo = backstageUserService.updateRows(ajaxParam,"永久有效");
            } catch (Exception e) {
                e.printStackTrace();
                returnInfo.put("status", "-1");
                returnInfo.put("returnMsg", "调用设置永久有效期接口失败！捕捉到错误异常信息：" + e.toString());
            }
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return returnInfo;
    }


}
