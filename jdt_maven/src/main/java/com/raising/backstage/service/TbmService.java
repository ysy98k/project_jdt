package com.raising.backstage.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.common.NewBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.baosight.common.utils.DateUtils.String2Date;
import static com.baosight.common.utils.DateUtils.date2String;

@Service("tbmService")
@Transactional
public class TbmService extends NewBaseService {

    /**
     * 查询盾构机信息
     * @param ajaxParam
     * @return
     */
    public JSONObject getTbmInfos(String ajaxParam){
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = new JSONObject();
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        String querySql = "com.raising.forward.mapper.TbmInfoMapper.getRows";
        String countSql = "com.raising.forward.mapper.TbmInfoMapper.count";
        String entity = "com.raising.forward.entity.TbmInfo";
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, querySql);
        paramInfo.put(BaseService.COUNT_SQL, countSql);
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        returnInfo = daoUtil.query(paramInfo);
        if(Constants.EXECUTE_FAIL.equals(returnInfo.getString("status"))){
            return returnInfo;
        }
        JSONArray detailsArray = (JSONArray)returnInfo.get("rows");
        if(detailsArray == null || detailsArray.size() < 1){
            return returnInfo;
        }
        for(int i = 0;i<detailsArray.size();i++){
            JSONObject temp = detailsArray.getJSONObject(i);
            temp.put("createTime", date2String("yyyy-MM-dd HH:mm:ss", new Date(temp.getLong("createTime"))));
            if(!StringUtils.isNullOrEmpty(temp.getString("produceTime"))){
                temp.put("produceTime", date2String("yyyy-MM-dd", new Date(temp.getLong("produceTime"))));
            }
        }
        return returnInfo;
    }

    /**
     * 操作查询出的数据,避免一对多查询时出现相同的记录
     * @param dataArray
     * @return
     */
    private JSONArray operatorData(JSONArray dataArray){
        //对一对多环境下，有可能出现多条重复记录，做过滤，
        //取出多条记录的数据
        Map<Integer,List<Integer>> duplicateData = new HashMap();

        Map<Integer,Integer> compareMap = new HashMap<>();//键 为tbmId，值为JsonArray index。
        for(int i = 0;i<dataArray.size();i++){
            JSONObject temp = dataArray.getJSONObject(i);
            //初始化操作。
            temp.put("createTime", date2String("yyyy-MM-dd HH:mm:ss", new Date(temp.getLong("createTime"))));
            if(!StringUtils.isNullOrEmpty(temp.getString("produceTime"))){
                temp.put("produceTime", date2String("yyyy-MM-dd", new Date(temp.getLong("produceTime"))));
            }

            int tbmId = temp.getInteger("tbmId");
            if( !compareMap.containsKey( tbmId ) ){
                compareMap.put(tbmId,i);
            }else{//如果出现重复的数据。将其放在Map中。
                List<Integer> integers = duplicateData.get(tbmId);
                if(integers == null){
                    integers = new ArrayList<Integer>();
                    Integer previous = compareMap.get(tbmId);
                    integers.add(previous);
                }
                integers.add(i);
                duplicateData.put(tbmId,integers);
            }
        }
        //对多条记录做操作。
        Set<Integer> integers = duplicateData.keySet();
        List<Integer> deleteIndex = new ArrayList<>();//存放要删除的index.
        for(Integer i : integers){
            List<Integer> indexList = duplicateData.get(i);//存放重复数据的index。
            Collections.sort(indexList);
            for(int j = 0;j<indexList.size();j++){//
                if(j > 0){
                    deleteIndex.add(j);
                }
                Integer index = indexList.get(j);
                JSONObject temp = dataArray.getJSONObject(index);
                String projectStatus = temp.getString("projectStatus");
                if("prostatus.building".equals(temp.getString(projectStatus))){
                    Integer firstIndex = indexList.get(0);
                    JSONObject firstObj = dataArray.getJSONObject(firstIndex);
                    firstObj.put("projectStatus","prostatus.building");
                    break;
                }
            }
            //删除重复数据。
        }
        Collections.sort(deleteIndex);
        for(int i = deleteIndex.size()-1;i>= 0;i--){//从最大的index开始删除
            dataArray.remove(i);
        }
        return dataArray;
    }


    public JSONObject getTbmInfoWithProjectId(String ajaxParam){
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = new JSONObject();
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        String querySql = "com.raising.forward.mapper.TbmInfoMapper.getRowWithProjectId";
        String entity = "com.raising.forward.entity.TbmInfo";
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, querySql);
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        returnInfo = daoUtil.query(paramInfo);
        return returnInfo;
    }

    /**
     * 新增
     * @param ajaxParam
     * @return
     */
    public JSONObject addTbmInfos(String ajaxParam) throws Exception {
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        //新增之前做盾构机名称校验。盾构机名称唯一

        JSONArray dataArray = paramInfo.getJSONObject("detail").getJSONArray("resultRow");

        List<String> tbmNames = new ArrayList<>();
        for(int i=0;i<dataArray.size();i++){
            JSONObject dataJson = dataArray.getJSONObject(i);
            tbmNames.add(dataJson.getString("tbmName"));
            //将时间字符串转换为Date类型
            String createTimeStr = dataJson.getString("createTime");
            String produceTimeStr = dataJson.getString("produceTime");

            //将时间字符串转换为Date类型
            Date date = new java.util.Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(StringUtils.isNullOrEmpty(createTimeStr)){
                dataJson.put("createTime", String2Date(new String(simpleDateFormat.format(date)), "yyyy-MM-dd HH:mm:ss"));
            }else{
                dataJson.put("createTime", String2Date(new String(createTimeStr), "yyyy-MM-dd HH:mm:ss"));
            }
            dataJson.put("produceTime", String2Date(new String(produceTimeStr), "yyyy-MM-dd"));
            dataJson.put("updateTime",String2Date(new String(simpleDateFormat.format(date)), "yyyy-MM-dd HH:mm:ss"));
        }
        boolean b = checkTbmName(tbmNames);
        if(b == false){
            paramInfo.put("returnMsg", "插入出错，盾构机名称冲突！");
            paramInfo.put("status", "-1");
            return paramInfo;
        }

        String insertSql = "com.raising.forward.mapper.TbmInfoMapper.addRows";
        String entity = "com.raising.forward.entity.TbmInfo";
        paramInfo.put(BaseService.RESULT_BLOCK,"detail");
        paramInfo.put(BaseService.INSERT_SQL, insertSql);
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        JSONObject returnInfo = daoUtil.insert(paramInfo);
        return returnInfo;
    }

    /**
     * 删除
     * @param ajaxParam
     * @return
     */
    public JSONObject deleteTbmInfos(String ajaxParam) throws Exception {
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONArray dataArray = paramInfo.getJSONObject("result").getJSONArray("resultRow");
        for(int i=0;i<dataArray.size();i++){
            JSONObject dataJson = dataArray.getJSONObject(i);
            dataJson.remove("produceTime");
            dataJson.remove("createTime");
            dataJson.remove("updateTime");
            dataJson.put("tbmId",dataJson.getIntValue("tbmId"));
        }
        String deleteSql = "com.raising.forward.mapper.TbmInfoMapper.delete";
        String entity = "com.raising.forward.entity.TbmInfo";
        paramInfo.put(BaseService.DELETE_SQL, deleteSql);
        paramInfo.put(BaseService.RESULT_BLOCK, "result");
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        JSONObject returnInfo = null;

        returnInfo = daoUtil.delete(paramInfo);
        return returnInfo;
    }

    /**
     * 更新
     * @param
     * @return 输出JSONObject
     */
    public JSONObject updateTbmInfos(String ajaxParam) throws Exception {
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        List<String> tbmNames = new ArrayList<>();
        JSONArray dataArray = paramInfo.getJSONObject("detail").getJSONArray("resultRow");
        for(int i=0;i<dataArray.size();i++){
            JSONObject dataJson = dataArray.getJSONObject(i);
            tbmNames.add(dataJson.getString("tbmName"));
            //将时间字符串转换为Date类型
            String createTimeStr = dataJson.getString("createTime");
            String produceTimeStr = dataJson.getString("produceTime");
            if(!StringUtils.isNullOrEmpty(createTimeStr)) {
                dataJson.put("createTime", String2Date(new String(createTimeStr), "yyyy-MM-dd HH:mm:ss"));
            }
            dataJson.put("produceTime", String2Date(new String(produceTimeStr), "yyyy-MM-dd"));
            Date date = new java.util.Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dataJson.put("updateTime",String2Date(new String(simpleDateFormat.format(date)), "yyyy-MM-dd HH:mm:ss"));
            dataJson.put("tbmId",dataJson.getIntValue("tbmId"));
        }
        String updateSql = "com.raising.forward.mapper.TbmInfoMapper.update";
        String entity = "com.raising.forward.entity.TbmInfo";
        paramInfo.put(BaseService.UPDATE_SQL, updateSql);
        paramInfo.put(BaseService.RESULT_BLOCK, "detail");
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        JSONObject returnInfo = daoUtil.update(paramInfo);
        return returnInfo;
    }


    public JSONObject initQuery(){
        JSONObject returnInfo = new JSONObject();
        String sql = "com.raising.forward.mapper.TbmInfoMapper.getRows";
        JSONObject paramJson = new JSONObject();
        paramJson.put("tenant",request.getSession().getAttribute("tenant"));
        List<JSONObject> tbmObjects = sqlSessionTemplate.selectList(sql, paramJson);
        if(tbmObjects == null || tbmObjects.size() < 1){
            returnInfo.put("haveData",Constants.EXECUTE_FAIL);
            return returnInfo;
        }
        List<String>  ownersList = new ArrayList<>();
        List<String> rmsVersionsList = new ArrayList<>();
        List<String> factorysList = new ArrayList<>();
        for(int i =0;i<tbmObjects.size();i++){
            JSONObject jsonObject = tbmObjects.get(i);
            String owner = jsonObject.getString("owner");
            String rmsVersion = jsonObject.getString("rmsVersion");
            String factory = jsonObject.getString("factory");


            if(StringUtils.isNullOrEmpty(owner)){//如果是空
                if(!ownersList.contains("无")){
                    ownersList.add("无");
                }
            }else if(!ownersList.contains(owner)){
                ownersList.add(owner);
            }
            if(StringUtils.isNullOrEmpty(rmsVersion) ){
                if(!rmsVersionsList.contains("无")){
                    rmsVersionsList.add("无");
                }
            }else if(!rmsVersionsList.contains(rmsVersion)){
                rmsVersionsList.add(rmsVersion);
            }
            if(!StringUtils.isNullOrEmpty(factory) && !factorysList.contains(factory)){
                factorysList.add(factory);
            }
        }
        Collections.sort(ownersList);
        Collections.sort(rmsVersionsList);
        Collections.sort(factorysList);
        returnInfo.put("ownersList",ownersList);
        returnInfo.put("rmsVersionsList",rmsVersionsList);
        returnInfo.put("factorysList",factorysList);
        returnInfo.put("haveData",Constants.EXECUTE_SUCCESS);
        return returnInfo;
    }


    private boolean checkTbmName(List<String> tbmNames){
        JSONObject json = new JSONObject();
        json.put("tbmNames",tbmNames);
        json.put("tenant",request.getSession().getAttribute("tenant"));

        String sql = "com.raising.forward.mapper.TbmInfoMapper.checkNames";
        Integer count = sqlSessionTemplate.selectOne(sql, json);
        if (count < 1) {
            return true;
        }
        return false;
    }


}
