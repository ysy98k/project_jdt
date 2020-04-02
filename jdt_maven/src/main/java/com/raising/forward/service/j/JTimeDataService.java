package com.raising.forward.service.j;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.common.NewBaseService;
import com.raising.forward.controller.constructionManagement.DataQueryControlller;
import com.raising.thread.GetTsdbTimeRangeData;
import com.raising.thread.SetTsdbToRedis;
import com.util.TsdbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

@Service
public class JTimeDataService extends NewBaseService {

    private static final Logger logger = LoggerFactory.getLogger(JTimeDataService.class);

    private JSONObject getTSDBData(JSONObject paramJson){
        JSONObject retrunInfo = new JSONObject();//存放结果。key是实例名。value是数组
        JSONArray columnArr = paramJson.getJSONArray("columnArr");
        String collectorName = paramJson.getString("collectorName");
        int limit = 1000;
        Long sTime = paramJson.getLong("sTime");
        Long eTime = paramJson.getLong("eTime");
        int interval = paramJson.getInteger("interval");
        int cursor_mask = -1;
        JSONObject instancesResult = null;
        JSONArray instanceData = null;
        
        JSONArray instancesParamArr = new JSONArray();//参数
        for (int i=0;i<columnArr.size();i++){
            String string = columnArr.getString(i);
            String str = collectorName +"_"+string;
            instancesParamArr.add(str);
        }
        try {
            instancesResult = TsdbUtil.getInstancesArr(instancesParamArr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(instancesResult == null || Constants.EXECUTE_FAIL.equals(instancesResult.getString("status")) ){
            return null;
        }
        instanceData = instancesResult.getJSONArray("data");

        List< GetTsdbTimeRangeData> callableList = new ArrayList<>();
        for(int i=0;i<instanceData.size();i++){
            JSONObject instanceTemp = instanceData.getJSONObject(i);
            Integer instanceId = instanceTemp.getInteger("instance_id");
            JSONArray properties = instanceTemp.getJSONArray("properties");
            if(properties.size() > 0){
                Integer propertyId = properties.getJSONObject(0).getInteger("id");
                GetTsdbTimeRangeData data = new GetTsdbTimeRangeData(instanceId,propertyId,limit,sTime,eTime,interval,cursor_mask);
                callableList.add(data);
            }

        }
        if(callableList.size() < 1){
            return null;
        }
        //多线程获取数据
        ExecutorService exec = Executors.newFixedThreadPool(10);
        try {
            List<Future<JSONObject>> futures = exec.invokeAll(callableList, 5, TimeUnit.SECONDS);
            for(Future<JSONObject> future:futures){
                JSONObject resultObject = future.get();
                if(Constants.EXECUTE_SUCCESS.equals( resultObject.getString("status"))){
                    Integer instanceId = resultObject.getInteger("instanceId");
                    String key = null;
                    for(int i=0;i<instanceData.size();i++){
                        JSONObject instanceTemp = instanceData.getJSONObject(i);
                        if(instanceId == instanceTemp.getInteger("instance_id")){
                            String instanceName = instanceTemp.getString("instance_name");
                            key  = instanceName.substring( instanceName.indexOf("_")+1 );
                            break;
                        }
                    }
                    JSONArray records = resultObject.getJSONArray("records");
                    retrunInfo.put(key,records);
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }finally {
            exec.shutdown();
        }

        return retrunInfo;
    }

    public JSONObject getData(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        JSONArray rows = new JSONArray();
        Integer records = null;
        Integer total = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String startTimeStr = inquStatus.getString("startTime");
        String endTimeStr = inquStatus.getString("endTime");
        String collectorName = inquStatus.getString("collectorName");
        JSONArray columnArr = paramInfo.getJSONArray("columnArr");
        Integer curPage = paramInfo.getInteger("curPage");
        Integer curRowNum = paramInfo.getInteger("curRowNum");
        if(StringUtils.isNullOrEmpty(startTimeStr) || StringUtils.isNullOrEmpty(endTimeStr)){
            return null;
        }
        //redisUtils.
        Long sTime = null;
        Long eTime = null;
        JSONObject tsdbData = null;
        try {
            sTime = dateFormat.parse(startTimeStr).getTime();
            eTime = dateFormat.parse(endTimeStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String tsdbDataStr = redisUtils.get(collectorName + "_" + sTime + "_" + eTime+"_"+columnArr.toJSONString());
        if(StringUtils.isNullOrEmpty(tsdbDataStr)){
            int interval = Math.round((eTime - sTime)/1000);
            if(interval <= 5000 ){//如果小于5s。则使用查询原始值的方式查询
                interval = 0;
            }
            paramInfo.put("sTime",sTime);
            paramInfo.put("eTime",eTime);
            paramInfo.put("interval",interval);
            paramInfo.put("collectorName",collectorName);
            tsdbData = getTSDBData(paramInfo);
            //保存10分钟
            if(tsdbData == null){
                return null;
            }
            SetTsdbToRedis setTsdbToRedis = new SetTsdbToRedis(redisUtils,collectorName + "_" + sTime + "_" + eTime+"_"+columnArr.toJSONString(),tsdbData.toJSONString(),600);
            new Thread(setTsdbToRedis).start();
        }else{
            tsdbData = JSONObject.parseObject(tsdbDataStr);
        }
        Set<String> keySet = tsdbData.keySet();

        for(int i=0;i<curRowNum;i++){
            JSONObject row = new JSONObject();
            for(int q=0;q<columnArr.size();q++){//填充row
                String column = columnArr.getString(q);
                if(keySet.contains(column)){
                    JSONArray tsdbDataArr = tsdbData.getJSONArray(column);
                    int index = (curPage-1)*curRowNum +i;
                    JSONObject tsdbTemp = tsdbDataArr.getJSONObject(index);
                    row.put(column,tsdbTemp.getString("value"));
                    row.put("dt",tsdbTemp.getString("dateTime"));
                }else{
                    row.put(column,null);
                }
            }
            rows.add(row);
        }
        for(String key : keySet){
            JSONArray tsdbDataArr = tsdbData.getJSONArray(key);
            if(records == null ){
                records = tsdbDataArr.size();
            }
        }
        total = (records%curRowNum == 0) ? (records/curRowNum) : (records/curRowNum +1);

        returnInfo.put("total",total);//总页
        returnInfo.put("page",curPage);//当前页码
        returnInfo.put("records",records);//总记录
        returnInfo.put("rows",rows);//当前记录
        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        returnInfo.put("returnMsg","查询成功！本次返回"+rows.size()+"条记录,总共"+records+"条记录！");
        return returnInfo;
    }

    /**
     * 根据项目id获取表中该项目数据的最早最晚时间
     * @param ajaxParam
     * @return
     */
    public JSONObject getTimeRange(String ajaxParam){
        JSONObject returnInfo = null;
        JSONObject paramJson = JSONObject.parseObject(ajaxParam);
        String sql = "com.raising.forward.construction.mapper.TbmTsDataMapper.getTimeRange";
        returnInfo = sqlSessionTemplate.selectOne(sql,paramJson);
        if(returnInfo == null){
            returnInfo = new JSONObject();
            returnInfo.put("max",null);
            returnInfo.put("min",null);
            return returnInfo;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        returnInfo.put("max",dateFormat.format( returnInfo.getTimestamp("max") ));
        returnInfo.put("min",dateFormat.format( returnInfo.getTimestamp("min") ));
        return returnInfo;
    }


    public JSONObject getRows(String ajaxParam){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = new JSONObject();
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        //数据校验
        String projectId = inquStatus.getString("projectId");
        inquStatus.put("projectId",Integer.parseInt(projectId));
        if(inquStatus.containsKey("startTime")){
            try {
                inquStatus.put("startTime",dateFormat.parse( inquStatus.getString("startTime") ));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(inquStatus.containsKey("endTime")){
            try {
                inquStatus.put("endTime",dateFormat.parse( inquStatus.getString("endTime") ));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        String querySql = "com.raising.forward.construction.mapper.TbmTsDataMapper.getTbmTsData";
        String countSql = "com.raising.forward.construction.mapper.TbmTsDataMapper.count";
        String entity = "";
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, querySql);
        paramInfo.put(BaseService.COUNT_SQL, countSql);
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        returnInfo = baseDao.query(paramInfo);

        //获取时间范围，做查询条件提示
        JSONObject timeRange = getTimeRange(inquStatus.toJSONString());
        returnInfo.put("max",timeRange.getString("max"));
        returnInfo.put("min",timeRange.getString("min"));

        JSONArray rows =  returnInfo.getJSONArray("rows");
        if(rows == null || rows.size() < 1){
            return returnInfo;
        }
        /*筛选查询数据*/
        Set<String> keySet = rows.getJSONObject(0).keySet();
        List<String> deleteKey = new ArrayList<>();
        JSONArray columnArr = paramInfo.getJSONArray("columnArr");
        for(String keyTemp : keySet){
            if(!columnArr.contains(keyTemp)){
                deleteKey.add(keyTemp);
            }
        }
        for(int i=0;i<rows.size();i++){
            JSONObject temp = rows.getJSONObject(i);
            temp.put("dt",dateFormat.format(new java.util.Date(temp.getLong("dt"))));
            for(int k = 0;k<deleteKey.size();k++){
                String deleteKeyName = deleteKey.get(k);
                temp.remove(deleteKeyName);
            }
        }
        return returnInfo;
    }









}
