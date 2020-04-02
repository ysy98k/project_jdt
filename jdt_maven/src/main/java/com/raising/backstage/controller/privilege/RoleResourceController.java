package com.raising.backstage.controller.privilege;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.entity.GridData;
import com.baosight.common.constants.Constants;
import com.common.BaseController;
import com.raising.forward.service.PropertiesValue;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 群组授权
 */
@RestController
@RequestMapping(value = "/raising/backstage/privilege/roleResource")
public class RoleResourceController extends BaseController {

    @RequestMapping(value="getPage.do",method = RequestMethod.GET)
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response){
        return new ModelAndView("/raising/backstage/privilege/RoleResource");
    }

    @RequestMapping(value="getResourcePage.do",method = RequestMethod.GET)
    public ModelAndView getResourcePage(HttpServletRequest request, HttpServletResponse response){
        return new ModelAndView("/raising/backstage/privilege/ResourceSelect");
    }
    /*@RequestMapping
    public JSONObject*/

    /**
     * 获取资源列表。
     * @return
     */
    @RequestMapping(value = "getResourcesRows.do",method = RequestMethod.POST)
    public JSONObject getResourcesRows(String ajaxParam){
        String token = request.getSession().getAttribute("token").toString();
        JSONObject resourcesRows = roleResourceService.getResourcesRows(ajaxParam,token);
        return resourcesRows;
    }

    @CacheEvict(cacheNames = "collectionNames",allEntries=true )
    @RequestMapping(value = "deleteRows.do",method = RequestMethod.POST)
    public JSONObject deleteRows(String ajaxParam){
        String token = request.getSession().getAttribute("token").toString();
        JSONObject resourcesRows = roleResourceService.deleteRows(ajaxParam,token);
        return resourcesRows;
    }

    @RequestMapping(value = "getResourceSelectRows.do",method = RequestMethod.POST)
    public JSONObject getResourceSelectRows(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject paramJson = JSONObject.parseObject(ajaxParam);
        String type = paramJson.getString("type");
        if("tree".equals(type)){//如果是树
            returnInfo = getTree();
        }else{//如果不是树
            restDao.setHost(PropertiesValue.AAS_ADRESS);
            restDao.setServiceName(PropertiesValue.AAS_APP_PATH);
            String[] groupNamesArr =  request.getSession().getAttribute("groupNames").toString().split(",");
            String service = paramJson.getString("service");
            String resourceName = paramJson.getString("name");
            String selectGroup = paramJson.getString("selectGroup");

            int offset = (paramJson.getInteger("curPage").intValue() - 1) * paramJson.getInteger("curRowNum").intValue();
            int limit = paramJson.getInteger("curRowNum");
            int totalCount = 0;
            List<JSONObject> resultList = new ArrayList<>();
            String param = "?service=" +service + "&offset=0&limit=1000&orderby=name&ascend=true&name="+resourceName;
            JSONArray resultArr = new JSONArray();
            JSONArray ownResourceArr = new JSONArray();

            //获得用户已经拥有的资源
            String ownResourceParam = "?service=" +service + "&offset=0&limit=1000&orderby=name&ascend=true&name=" + resourceName;
            JSONObject tempParam = new JSONObject();
            tempParam.put("token",request.getSession().getAttribute("token").toString());
            JSONObject ownResource = restDao.invoke("GET",  "/api/usergroup/" + selectGroup + "/resource" + ownResourceParam, tempParam);
            if ("0".equals(ownResource.get("errcode"))) {
                ownResourceArr = ownResource.getJSONArray("resources");
            }

            for(int i= 0;i<groupNamesArr.length;i++){//获得用户所有组的资源
                String groupNameTemp = groupNamesArr[i];
                JSONObject mapIn = new JSONObject();
                mapIn.put("token",request.getSession().getAttribute("token").toString());
                JSONObject restResult = restDao.invoke("GET",  "/api/usergroup/" + groupNameTemp + "/resource" + param, mapIn);
                if ("0".equals(restResult.get("errcode"))) {
                    JSONArray resource = restResult.getJSONArray("resources");
                    for(int j = 0;j<resource.size();j++){
                        JSONObject temp = resource.getJSONObject(j);
                        if(resultArr.contains(temp)){//如果结果集合中已经存在。
                            continue;
                        }
                        if(ownResourceArr != null && ownResourceArr.contains(temp)){//如果用户已经拥有该资源
                            continue;
                        }
                        resultArr.add(temp);
                    }
                }
            }


            totalCount = resultArr.size();
            int toIndex = offset+limit > resultArr.size() -1 ?resultArr.size() :offset+limit;
            List<Object> dataList = resultArr.subList(offset,toIndex);
            Collections.addAll(resultList, dataList.toArray(new JSONObject[dataList.size()]));

            GridData gridData = new GridData(totalCount, resultList);

            gridData.fillGridAttribute(ajaxParam);
            returnInfo = JSONObject.parseObject(JSON.toJSONString(gridData));
            returnInfo.put("status", Constants.EXECUTE_SUCCESS);
            returnInfo.put("returnMsg", "查询成功！本次返回" +resultList.size()+ "条记录，总共" +totalCount + "条记录！");
        }

        return returnInfo;
    }

    /**
     *
     * @return
     */
    private JSONObject getTree(){
        JSONObject returnInfo = new JSONObject();
        List<JSONObject> menusList = new ArrayList<>();
        tenantMenuService.dao.setHost(PropertiesValue.AAS_ADRESS);
        tenantMenuService.dao.setServiceName(PropertiesValue.AAS_APP_PATH);
        JSONObject menuObj = new JSONObject();
        //先获取 上侧，盾构项目，后台左侧菜单。三种菜单列表。然后拼凑成zTree使用的树Json
        menuObj.put("menuType","topMenu");
        menuObj.put("fieldName","menuOrder");
        menuObj.put("ascDesc","asc");
        JSONObject topMenu = tenantMenuService.queryHomeAndBackgroundMenu(menuObj);//上侧菜单
        //记录盾构项目，技术服务管理，统计分析id..用来填充其子目录的 parentId
        Integer sectionInfo = null;//记录盾构项目Id
        Integer tbmManage = null;//记录技服管理Id
        Integer deviceManage = null;//记录设备管理Id
        Integer statisticalAnalysisId = null;//记录统计分析Id.
        JSONArray topArray =  topMenu.getJSONArray("authMenus");
        for(int i=0;i<topArray.size();i++){
            JSONObject topTemp = topArray.getJSONObject(i);
            if("盾构项目".equals(topTemp.getString("dispName"))){
                sectionInfo = topTemp.getInteger("menuId");
            }
            if("技服管理".equals(topTemp.getString("dispName"))){
                tbmManage = topTemp.getInteger("menuId");
            }
            if("设备管理".equals(topTemp.getString("dispName"))){
                deviceManage = topTemp.getInteger("menuId");
            }
            if("统计分析".equals(topTemp.getString("dispName"))){
                statisticalAnalysisId = topTemp.getInteger("menuId");
            }
            menusList.add(topTemp);//将上侧菜单添加至 树集合中
        }
        //填充盾构项目下的在左侧菜单
        menuObj.put("menuType","custMenu");
        JSONObject custMenu = tenantMenuService.queryHomeAndBackgroundMenu(menuObj);//盾构项目下的左侧菜单
        JSONArray custArray =  custMenu.getJSONArray("authMenus");
        for(int i=0;i<custArray.size();i++){
            JSONObject custTemp = custArray.getJSONObject(i);
            custTemp.put("parentCode",sectionInfo);
            menusList.add(custTemp);
            JSONArray subArry = custTemp.getJSONArray("subMenus");
            if(subArry != null && subArry.size() > 0){
                for(int j =0;j<subArry.size();j++){
                    JSONObject subTemp = subArry.getJSONObject(j);
                    subTemp.put("parentCode",custTemp.getInteger("menuId"));
                    menusList.add(subTemp);
                }
            }
        }

        //填充技术服务管理下的在左侧菜单
        menuObj.put("menuType","tbmMenu");
        JSONObject tbmMenu = tenantMenuService.queryHomeAndBackgroundMenu(menuObj);//技服管理下的左侧菜单
        JSONArray tbmArray =  tbmMenu.getJSONArray("authMenus");
        for(int i=0;i<tbmArray.size();i++){
            JSONObject tbmTemp = tbmArray.getJSONObject(i);
            tbmTemp.put("parentCode",tbmManage);
            menusList.add(tbmTemp);
            JSONArray subArry = tbmTemp.getJSONArray("subMenus");
            if(subArry != null && subArry.size() > 0){
                for(int j =0;j<subArry.size();j++){
                    JSONObject subTemp = subArry.getJSONObject(j);
                    subTemp.put("parentCode",tbmTemp.getInteger("menuId"));
                    menusList.add(subTemp);
                }
            }
        }
        //填充设备管理下的在左侧菜单
        menuObj.put("menuType","deviceMenu");
        JSONObject deviceMenu = tenantMenuService.queryHomeAndBackgroundMenu(menuObj);//技服管理下的左侧菜单
        JSONArray deviceArray =  deviceMenu.getJSONArray("authMenus");
        for(int i=0;i<deviceArray.size();i++){
            JSONObject deviceTemp = deviceArray.getJSONObject(i);
            deviceTemp.put("parentCode",deviceManage);
            menusList.add(deviceTemp);
            JSONArray subArry = deviceTemp.getJSONArray("subMenus");
            if(subArry != null && subArry.size() > 0){
                for(int j =0;j<subArry.size();j++){
                    JSONObject subTemp = subArry.getJSONObject(j);
                    subTemp.put("parentCode",deviceTemp.getInteger("menuId"));
                    menusList.add(subTemp);
                }
            }
        }
        //填充统计分析下的在左侧菜单
        menuObj.put("menuType","statisticalAnalysisMenu");
        JSONObject statisticalMenu = tenantMenuService.queryHomeAndBackgroundMenu(menuObj);//技服管理下的左侧菜单
        JSONArray statisticalMenuArray =  statisticalMenu.getJSONArray("authMenus");
        for(int i=0;i<statisticalMenuArray.size();i++){
            JSONObject statisticalTemp = statisticalMenuArray.getJSONObject(i);
            statisticalTemp.put("parentCode",statisticalAnalysisId);
            menusList.add(statisticalTemp);
            JSONArray subArry = statisticalTemp.getJSONArray("subMenus");
            if(subArry != null && subArry.size() > 0){
                for(int j =0;j<subArry.size();j++){
                    JSONObject subTemp = subArry.getJSONObject(j);
                    subTemp.put("parentCode",statisticalTemp.getInteger("menuId"));
                    menusList.add(subTemp);
                }
            }
        }
        //先创建后台管理根节点
        JSONObject backstage = new JSONObject();
        backstage.put("menuId","0-#");
        backstage.put("parentCode",null);
        backstage.put("dispName","后台管理");
        backstage.put("menuType","leftMenu");
        backstage.put("linkPath","frame");
        menusList.add(backstage);
        //填充后台管理下的在左侧菜单
        menuObj.put("menuType","leftMenu");
        JSONObject leftMenu = tenantMenuService.queryHomeAndBackgroundMenu(menuObj);//后台管理
        JSONArray leftArray =  leftMenu.getJSONArray("authMenus");
        for(int i=0;i<leftArray.size();i++){
            JSONObject leftTemp = leftArray.getJSONObject(i);
            leftTemp.put("parentCode","0-#");
            menusList.add(leftTemp);
            JSONArray subArry = leftTemp.getJSONArray("subMenus");
            if(subArry != null && subArry.size() > 0){
                for(int j =0;j<subArry.size();j++){
                    JSONObject subTemp = subArry.getJSONObject(j);
                    subTemp.put("parentCode",leftTemp.getInteger("menuId"));
                    menusList.add(subTemp);
                }
            }
        }
        returnInfo.put("dataList",menusList);
        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        return returnInfo;
    }

    /**
     * 建立用户与角色的关系
     * @param ajaxParam
     * @return
     */
    @CacheEvict(cacheNames = "collectionNames",allEntries=true )
    @RequestMapping(value = "bindResource.do",method = RequestMethod.POST)
    public JSONObject bindUser(String ajaxParam){
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        restDao.setHost(PropertiesValue.AAS_ADRESS);
        restDao.setServiceName(PropertiesValue.AAS_APP_PATH);

        String groupName = ajaxParamObj.getString("selectgroup");


        JSONObject mapIn = new JSONObject();
        mapIn.put("resources", ajaxParamObj.getJSONArray("resource"));
        mapIn.put("token",request.getSession().getAttribute("token").toString());

        JSONObject returnInfo = (JSONObject)restDao.invoke("PUT", "/api/usergroup/" + groupName + "/resource", mapIn);
        if ("0".equals(returnInfo.get("errcode"))){
            returnInfo.put("status", "0");
            returnInfo.put("returnMsg", "为角色授权资源成功！");
            return returnInfo;
        }
        returnInfo.put("status", "-1");
        returnInfo.put("returnMsg", "错误代码： " + returnInfo.getString("errcode") + " 错误信息： " + returnInfo.getString("errinfo"));
        return returnInfo;
    }
}
