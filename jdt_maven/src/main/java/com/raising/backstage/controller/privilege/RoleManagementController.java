package com.raising.backstage.controller.privilege;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.common.BaseController;
import com.util.CSVUtils;
import com.util.MultipleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 角色管理Controller
 */
@RestController
@RequestMapping(value = "/raising/backstage/privilege/roleManagement")
public class RoleManagementController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(RoleManagementController.class);

    /**
     * 获得当前用户所在群组的，后代群组
     * @param ajaxParam
     * @return
     */
    @RequestMapping(value = "getRows.do",method = RequestMethod.POST)
    public JSONObject getRows(String ajaxParam) {
        JSONObject returnInfo = new JSONObject();

        JSONObject paramJson = JSONObject.parseObject(ajaxParam);
        if (paramJson == null) {
            paramJson = new JSONObject();
        }
        String groupNamesStr = request.getSession().getAttribute("groupNames").toString();
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



    @RequestMapping(value = "addOrUpdateRows.do",method = RequestMethod.POST)
    public JSONObject addOrUpdateRow(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject paramJson = JSONObject.parseObject(ajaxParam);
        String token = request.getSession().getAttribute("token").toString();
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
            String groupNamesStr =  request.getSession().getAttribute("groupNames").toString();
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

    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    public JSONObject registerAASPower(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject ajaxJson = JSONObject.parseObject(ajaxParam);
        String groupName = ajaxJson.getString("groupName");
        String token = request.getSession().getAttribute("token").toString();
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


    @RequestMapping(value = "deleteRows.do",method = RequestMethod.POST)
    public JSONObject deleteRows(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject paramInfo  = JSONObject.parseObject(ajaxParam);
        JSONArray groupIdsArr = paramInfo.getJSONArray("groupIds");
        JSONArray groupNamesArr = paramInfo.getJSONArray("groupNames");
        String token = request.getSession().getAttribute("token").toString();
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
            String[] t = request.getSession().getAttribute("groupNames").toString().split(",");
            List<String> groupNames = new ArrayList<String>(Arrays.asList(t)) ;
            queryJson.put("groupNames",groupNames);
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
