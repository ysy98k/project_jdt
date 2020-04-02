package com.raising.rest.aasPublic.privateRest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.common.BaseController;
import com.util.CSVUtils;
import com.util.MultipleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 角色管理Controller
 */
@Component
@Path("/raising")
public class RoleManagementRest extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(RoleManagementRest.class);

    /**
     * 获得当前用户所在群组的，后代群组
     * @param ajaxParam
     * @return
     */
    @POST
    @Path("/roleManagementRest/getRows.do")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject getRows(String ajaxParam) {
        JSONObject returnInfo = new JSONObject();

        JSONObject paramJson = JSONObject.parseObject(ajaxParam);
        if (paramJson == null) {
            paramJson = new JSONObject();
        }

        String groupNamesStr = paramJson.getString("groupNamesStr");
        ArrayList<String> groupNames = new ArrayList<>();
        if (groupNamesStr.indexOf(",") > 0) {
            String[] split = groupNamesStr.split(",");
            groupNames = new ArrayList<String>(Arrays.asList(split));
        } else {
            groupNames.add(groupNamesStr);
        }
        paramJson.put("groupNames", groupNames);

        try {
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            returnInfo = roleManagementService.getRows(paramJson);
        } finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return returnInfo;
    }


    @POST
    @Path("/roleManagementRest/addOrUpdateRows.do")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject addOrUpdateRow(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject paramJson = JSONObject.parseObject(ajaxParam);
        String token = paramJson.getString("token");
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            if(paramJson.containsKey("groupId")){//如果包含groupId则为修改
                try {
                    returnInfo = roleManagementService.updateRows(paramJson,token);
                } catch (Exception e) {
                    returnInfo.put("status", Constants.EXECUTE_FAIL);
                    returnInfo.put("message","更新失败");
                    e.printStackTrace();
                    logger.error(e.getMessage());
                }
            }else{//如果不包含，则为新增
                try {
                    returnInfo = roleManagementService.addRows(paramJson,token);
                } catch (Exception e) {
                    returnInfo.put("status",Constants.EXECUTE_FAIL);
                    returnInfo.put("message","插入失败");
                    e.printStackTrace();
                    logger.error(e.getMessage());
                }
            }
            //操作完以后，在查询
            JSONObject queryJson = new JSONObject();
            String groupNamesStr = paramJson.getString("groupNamesStr");
            String[] split = groupNamesStr.split(",");
            List<String> groupNames = new ArrayList<String>(Arrays.asList(split)) ;
            queryJson.put("groupNames", groupNames);
            JSONObject rows =  roleManagementService.getRows(queryJson);
            List<JSONObject> row = rows.getObject("rows", List.class);
            returnInfo.put("rows",row);
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }

        return returnInfo;
    }

    @POST
    @Path("/roleManagementRest/register.do")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject registerAASPower(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject ajaxJson = JSONObject.parseObject(ajaxParam);
        String groupName = ajaxJson.getString("groupName");
        String token = ajaxJson.getString("token");
        JSONObject result = roleManagementService.addDefaultAASPower(groupName,token);
        if("0".equals(result.get("errcode"))){
            returnInfo.put("status", "0");
            returnInfo.put("message", "注册默认权限成功！");
        }else{
            returnInfo.put("status", "-1");
            returnInfo.put("message", "注册默认权限失败！"+" 错误信息： " + returnInfo.getString("errinfo").replace("群组","角色"));
        }
        return returnInfo;
    }

    @POST
    @Path("/roleManagementRest/deleteRows.do")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject deleteRows(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject paramInfo  = JSONObject.parseObject(ajaxParam);
        JSONArray groupIdsArr = paramInfo.getJSONArray("groupIds");
        JSONArray groupNamesArr = paramInfo.getJSONArray("groupNames");
        String token = paramInfo.getString("token");
        String groupNames = paramInfo.getString("groupNamesStr");
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            //先检查是否可以删除
            boolean b = roleUserService.checkDeleteRole(groupIdsArr);
            if(b == false){
                returnInfo.put("status",Constants.EXECUTE_FAIL);
                returnInfo.put("message","所删除角色中，仍有角色关联着用户，删除失败！");
            }else {
                try {
                    returnInfo = roleManagementService.delete(groupNamesArr,token);
                } catch (Exception e) {
                    returnInfo.put("status",Constants.EXECUTE_FAIL);
                    returnInfo.put("message","删除失败！");
                    e.printStackTrace();
                    logger.error(e.getMessage());
                }
            }
            //操作完以后，在查询
            JSONObject queryJson = new JSONObject();
            String[] t = groupNames.split(",");
            List<String> groupNamesList = new ArrayList<String>(Arrays.asList(t)) ;
            queryJson.put("groupNames",groupNamesList);
            JSONObject rows =  roleManagementService.getRows(queryJson);
            List<JSONObject> row = rows.getObject("rows", List.class);
            returnInfo.put("rows",row);
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return returnInfo;
    }



}
