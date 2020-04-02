package com.raising.forward.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;

import com.baosight.common.constants.Constants;
import com.common.NewBaseService;
import com.raising.forward.entity.CodeItem;
import com.raising.forward.mapper.CodeItemDao;
import com.util.MultipleDataSource;
import org.junit.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典表service
 * 根据code，获取name
 */
@Service
public class CodeItemService extends NewBaseService{

    @Autowired
    @Qualifier("codeItemDao")
    private CodeItemDao codeItemDao;

    /**
     * 根据传入的fdLevelCode信息获取fdItemName值。
     * @param fdLvelCode
     * @return
     */
    public String getCodeName(String fdLvelCode){
        String fdItemName = codeItemDao.getFdItemName(fdLvelCode);
        return fdItemName;
    }

    /**
     * 获得前台的条件信息
     * //读取城市，线路等条件信息
     * @param ccsIdsList
     * @return
     */
    public JSONObject getLineCondition2(List<String> ccsIdsList){
        //读取的信息，返回returnInfo对象中
        JSONObject returnInfo = new JSONObject();
        //封装city数据。用做前台显示
        JSONArray cityArray = new JSONArray();
        returnInfo.put("cityArray",cityArray);
        if(ccsIdsList.size() < 1){
            return returnInfo;
        }
        //去重,并提取城市ccsId
        List<String> ccsIds = new ArrayList<>();
        for(int i=0;i<ccsIdsList.size();i++){
            String temp = ccsIdsList.get(i);
            if(ccsIds.contains(temp)){
                continue;
            }
            String cityCode = temp.substring(0, temp.lastIndexOf("."));
            ccsIds.add(temp);
            if(ccsIds.contains(cityCode)){
                continue;
            }
            ccsIds.add(cityCode);
        }

        List<JSONObject> data = codeItemDao.getCCSIds(ccsIds);
        for(int i=0;i<data.size();i++){
            JSONObject temp = data.get(i);
            String itemName = temp.getString("fdItemName");
            String levelCode = temp.getString("fdLevelCode");

            if(levelCode.split("\\.").length == 3){//判断此记录是市还是线路
                //如果是线路
                String cityCode = levelCode.substring(0, levelCode.lastIndexOf("."));
                //将城市和线路信息添加至reurnInfo中
                if(returnInfo.containsKey(cityCode)){
                    //如果returnInfo中已经存放这个城市信息
                    JSONArray lineArray = (JSONArray) returnInfo.get(cityCode);
                    JSONObject lineObject = new JSONObject();
                    lineObject.put("label",itemName);
                    lineObject.put("value",levelCode);
                    //判断线路数组中，是否包含。
                    for(int t=0;t<lineArray.size();t++){
                        JSONObject lineArrayTemp = lineArray.getJSONObject(t);
                        if(levelCode.equals(lineArrayTemp.getString("value"))){
                            continue;
                        }
                    }
                    lineArray.add(lineObject);
                }else if (cityCode != null && cityCode !="" ) {
                    //如果returnInfo中没有存放这个城市信息,且这个信息不是null或者"";
                    //则将线路和城市建立关联，并放入returnInfo中。
                    //并封装city数据。用做前台显示
                    JSONArray lineArray = new JSONArray();
                    JSONObject lineObject = new JSONObject();
                    lineObject.put("label",itemName);
                    lineObject.put("value",levelCode);
                    lineArray.add(lineObject);
                    returnInfo.put(cityCode, lineArray);

                    //封装city数据
                    for(int j =0;j<data.size();j++){
                        JSONObject dataTemp = data.get(j);
                        if(cityCode.equals(dataTemp.getString("fdLevelCode"))){
                            JSONObject cityObject = new JSONObject();
                            cityObject.put("label",dataTemp.getString("fdItemName"));
                            cityObject.put("value",cityCode);
                            cityArray.add(cityObject);
                        }
                    }
                }

            }else{
                //如果是市。
            }
        }
        return returnInfo;
    }

    /**
     * 获得城市树的方法
     * @param paramInfo
     * @return
     */
    public JSONArray querySubTree(JSONObject paramInfo) {

        JSONArray menuLstarray = new JSONArray();
        if (paramInfo.get("treeparentId").equals("#")) {
            JSONObject menu = new JSONObject();
            menu.put("id", "0-#");
            menu.put("parent", "#");
            menu.put("children", true);
            menu.put("text", "线路选择");
            menuLstarray.add(menu);
        } else {
            paramInfo.put("tenant","raising");
            paramInfo.put("fdParentId",Integer.parseInt(paramInfo.get("fdParentId").toString()));
            List<JSONObject> menuLst = codeItemDao.getLineTree(paramInfo);
            if (menuLst != null && menuLst.size() > 0) {
                for (int j = 0; j < menuLst.size(); j++) {
                    JSONObject menu = menuLst.get(j);
                    menu.put("id",menu.get("menuCode")+"-"+menu.get("level_code"));
                    menu.put("parent", paramInfo.get("treeparentId"));
                    menu.put("children", true);
                    menuLstarray.add(menu);
                }
            }
        }
        return menuLstarray;
    }

    /**
     * 获得线路树的方法2
     * 该方法只返回原始数据。以便根据jsTree,zTree需要，调整数据结构
     */
    public List<JSONObject> queryLineTree2(List<String> ccsIds){
        List<JSONObject> returnInfo = new ArrayList<>();

        String sql = "com.raising.forward.mapper.CodeItemDao.getLineTree2";
        List<JSONObject> lineTree = sqlSessionTemplate.selectList(sql);
        //lineTree 是一个fdLevelNum升序排序的集合。省的下标最小，市较大，线路最大。
        if(ccsIds == null || ccsIds.size() < 1){//如果没有权限过滤要求。直接返回结果。
            //改变北京，上海的三层结构 北京-北京市-一号线 为 两层结构  北京-一号线
            for(int i=0;i<lineTree.size();i++){
                JSONObject temp = lineTree.get(i);
                if(2 == temp.getIntValue("fdLevelNum") ){//如果是线路
                    break;
                }
                if("beijing".equals(temp.getString("fdLevelCode"))){
                    lineTree.remove(temp);
                    i--;
                }
                if("shanghai".equals(temp.getString("fdLevelCode"))){
                    lineTree.remove(temp);
                    i--;
                }
            }
            return lineTree;
        }
        //如果有权限过滤要求，则权限过滤
        List<String> provinceStr = new ArrayList<>();
        List<String> cityStr = new ArrayList<>();
        List<Integer> provinceId = new ArrayList<>();//要保留省的id
        List<Integer> cityId = new ArrayList<>();//要保留事的Id
        for(int i=0;i<ccsIds.size();i++){
            String idTemp = ccsIds.get(i);
            String[] arr = idTemp.split("\\.");
            if(!provinceStr.contains(arr[0])){
                provinceStr.add(arr[0]);
            }
            if(!cityStr.contains(arr[1])){
                cityStr.add(arr[1]);
            }
        }
        for(int i =0;i<lineTree.size();i++){
            JSONObject record = lineTree.get(i);
            if(1 == record.getIntValue("fdLevelNum")){//如果是省
                if(provinceStr.contains(record.getString("fdItemCode"))){
                    returnInfo.add(record);
                    provinceId.add(record.getIntValue("fdItemId"));
                }
                if("beijing".equals(record.getString("fdLevelCode"))){
                    returnInfo.remove(record);
                }
                if("shanghai".equals(record.getString("fdLevelCode"))){
                    returnInfo.remove(record);
                }
            }else if(2 == record.getIntValue("fdLevelNum")){//如果是市
                //判断该市所在省是否在权限内。这样比较比较快
                if( provinceId.contains(record.getInteger("fdParentId")) && cityStr.contains(record.getString("fdItemCode"))){
                    returnInfo.add(record);
                    cityId.add(record.getIntValue("fdItemId"));
                }
            }else{//线路
                //判断该市所在市是否在权限内。这样比较比较快
                if( cityId.contains(record.getInteger("fdParentId")) && ccsIds.contains(record.getString("fdLevelCode"))  ){
                    returnInfo.add(record);
                }
            }
        }
        return returnInfo;
    }

    /**
     * 获得始发树的方法
     * @param paramInfo
     * @param request
     * @return
     */
    public JSONArray getStartManageSubTree(JSONObject paramInfo) {

        JSONArray menuLstarray = new JSONArray();
        if (paramInfo.get("treeparentId").equals("#")) {
            JSONObject menu = new JSONObject();
            menu.put("id", "0-#");
            menu.put("parent", "#");
            menu.put("children", true);
            menu.put("text", "始发管理");
            menuLstarray.add(menu);
        } else {
            /*paramInfo.put(
                    "tenant",
                    request.getSession().getAttribute(
                            Constants.SESSION_TENANT_KEY));*/
            paramInfo.put("fdParentId",Integer.parseInt(paramInfo.get("fdParentId").toString()));
            List<JSONObject> menuLst = codeItemDao.getStartManageSubTree(paramInfo);
            if (menuLst != null && menuLst.size() > 0) {
                for (int j = 0; j < menuLst.size(); j++) {
                    JSONObject menu = menuLst.get(j);
                    menu.put("id",menu.get("menuCode")+"-"+menu.get("level_code"));
                    menu.put("parent", paramInfo.get("treeparentId"));
                    menu.put("children", true);
                    menuLstarray.add(menu);
                }
            }
        }
        return menuLstarray;
    }



    /**
     * 根据传入的code值。分别向t_code_system 和 t_code_item表中查询数据。传入tbmtype.epb。获取所有tbmtype类型集合。
     * 用来取代getCodeNameFromSystemAndItem（）方法
     */
    public Map<String,String> getCodeNameFromSystemCode(String code){
        String fdSystemCode = code.substring(0,code.indexOf("."));
        List<JSONObject> itemList = codeItemDao.getCodeNameFromSystemCode(fdSystemCode);
        Map<String,String> resultMap = new HashMap<>();
        for(int i=0;i<itemList.size();i++){
            JSONObject temp = itemList.get(i);
            String itemCode = temp.getString("levelCode");
            String itemName = temp.getString("itemName");
            resultMap.put(fdSystemCode+"."+itemCode,itemName);
        }
        return resultMap;
    }
    /**
     * 根据传入的systemCode。获取其下的列表信息
     */
    public List<JSONObject> getCodeListFromSystemCode(String systemCode){
        List<JSONObject> codesList = codeItemDao.getCodeListFromSystemCode(systemCode);
        if(codesList == null || codesList.size() < 1){
            return null;
        }
        for(int i=0;i<codesList.size();i++){
            JSONObject temp = codesList.get(i);
            String sysCode = temp.getString("systemCode");
            String itemCode = temp.getString("itemCode");
            //通用
            temp.put("ccsId",sysCode+"."+itemCode);
            temp.put("ccsStr",temp.getString("itemName"));
            //只针对project管理用
            temp.put("statusCode",sysCode+"."+itemCode);
            temp.put("statusStr",temp.getString("itemName"));
        }
        return codesList;

    }

    /**
     *根据参数，获得表中行
     */
    public List<JSONObject> getRows(CodeItem paramJson){
        String sql = "com.raising.forward.mapper.CodeItemDao.getRows";
        List<JSONObject> objects = sqlSessionTemplate.selectList(sql, paramJson);
        return objects;
    }








}
