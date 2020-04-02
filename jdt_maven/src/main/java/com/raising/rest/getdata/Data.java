package com.raising.rest.getdata;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.common.BaseController;
import com.raising.forward.service.PropertiesValue;
import org.junit.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.*;
import java.util.concurrent.*;

@Component
@Path("/raising")
public class Data extends BaseController {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @POST
    @Path("/getData")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject data(String ajaxParam){
        JSONObject paramJson = JSONObject.parseObject(ajaxParam);
        boolean continueGet = false;//继续获取数据
        JSONObject tables = paramJson.getJSONObject("tables");
        String username = paramJson.getString("username");
        String password = paramJson.getString("password");
        TreeMap<String,String> key = new TreeMap();
        key.put("username",username);
        key.put("password",password);
        Map map = (Map)redisTemplate.opsForValue().get(key.toString());
        if(map == null){
            return null;
        }
        List<GetData> threadsList = new ArrayList<>();
        List<Integer> projectIds = (List<Integer>)map.get("projectIds");
        //获取服务端所有表名
        JSONArray allTableNames = null;
        String allTableNamesStr = (String)redisTemplate.opsForValue().get("dataSyncTableNames");
        allTableNames = JSONArray.parseArray(allTableNamesStr);
        if(allTableNames == null){
            initTableName();
            allTableNamesStr = (String)redisTemplate.opsForValue().get("dataSyncTableNames");
            allTableNames = JSONArray.parseArray(allTableNamesStr);
        }
        //获取客户端所有表名
        Set<String> tableNames = new HashSet<>();
        List<String> disAndMileageTableNames = (ArrayList<String>)map.get("disAndMileageTableNames");
        Set<String> tableKey = tables.keySet();
        String[] strArr = new String[tableKey.size()];
        tableKey.toArray(strArr);

        tableNames.addAll(Arrays.asList(strArr));
        tableNames.addAll(disAndMileageTableNames);
        //每个表建一个线程获取数据
        for(String tableName : tableNames){
            //如果服务端没有此表名，则过滤
            if(!allTableNames.contains(tableName)){
                continue;
            }
            JSONObject table = tables.getJSONObject(tableName);
            if(table == null){
                table = new JSONObject();
                table.put("getData",true);
                table.put("clientMaxId",0);
                tables.put(tableName,table);
            }
            if(true == table.getBooleanValue("getData")){
                JSONObject param = new JSONObject();
                param.put("projectIds",projectIds);
                int clientMaxId = table.getIntValue("clientMaxId");
                if(clientMaxId > 0){
                    param.put("id",clientMaxId);
                }
                param.put("tableName",tableName);
                GetData getData = new GetData(tableName,param);
                threadsList.add(getData);
            }
        }

        //多线程获取数据
        ExecutorService exec = Executors.newFixedThreadPool(3);
        try {
            List<Future<JSONObject>> futures = exec.invokeAll(threadsList, 2, TimeUnit.MINUTES);
            for(Future<JSONObject> future:futures){
                JSONObject resultObject = future.get();
                String tableName = resultObject.getString("tableName");
                resultObject.remove("tableName");
                tables.put(tableName,resultObject);
                boolean getData = resultObject.getBooleanValue("getData");
                if(getData == true){//如果有一个表需要继续获取。那么就继续
                    continueGet = true;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }finally {
            exec.shutdown();
        }
        paramJson.remove("username");
        paramJson.remove("password");
        paramJson.put("continue",continueGet);
        paramJson.put("status", Constants.EXECUTE_SUCCESS);
        return paramJson;
    }

    class GetData implements Callable<JSONObject>{
        private String tableName;
        private JSONObject param;
        public GetData(String tableName,JSONObject param){
            this.tableName = tableName;
            this.param = param;
        }

        @Override
        public JSONObject call() {
            JSONObject returnInfo = new JSONObject();
            List<JSONObject> dataByProjectIds = new ArrayList<>();
            int clientMaxId = 0;
            Integer id = null;
            String idKey = null;
            if ("project".equals(tableName)) {
                dataByProjectIds = projectForwardService.getDataByProjectIds(this.param);
                id = projectForwardService.getMaxId(this.param);
                idKey = "pro_id";
            } else if ("t_section".equals(tableName)) {
                dataByProjectIds = sectionManageService.getDataByProjectIds(this.param);
                id = sectionManageService.getMaxId(this.param);
                idKey = "id";
            } else if ("tbm_info".equals(tableName)) {
                dataByProjectIds = tbmResumeService.getDataByProjectIds(this.param);
                id = tbmResumeService.getMaxId(this.param);
                idKey = "id";
            } else if ("t_alarm".equals(tableName)) {
                dataByProjectIds = alarmInfoService.getDataByProjectIds(this.param);
                id = alarmInfoService.getMaxId(this.param);
                idKey = "id";
            } else if ("t_alarm_code".equals(tableName)) {
                dataByProjectIds = alarmVariableManagementService.getDataByProjectIds(this.param);
                id = alarmVariableManagementService.getMaxId(this.param);
                idKey = "id";
            } else if ("d_coordinatelist".equals(tableName)) {
                dataByProjectIds = designLineService.getDataByProjectIds(this.param);
                id = designLineService.getMaxId(this.param);
                idKey = "table_increment_pk";
            } else if ("d_MeasureResult".equals(tableName)) {
                dataByProjectIds = dMeasureResultService.getDataByProjectIds(this.param);
                id = dMeasureResultService.getMaxId(this.param);
                idKey = "id";
            } else if ("d_Shield_Configure".equals(tableName)) {
                dataByProjectIds = zeroConfigService.getDataByProjectIds(this.param);
                id = zeroConfigService.getMaxId(this.param);
                idKey = "table_increment_pk";
            } else if ("d_Station_Configure".equals(tableName)) {
                dataByProjectIds = stationConfigService.getDataByProjectIds(this.param);
                id = stationConfigService.getMaxId(this.param);
                idKey = "table_increment_pk";
            }else if ("j_setting".equals(tableName)) {
                dataByProjectIds = jSettingService.getDataByProjectIds(this.param);
                id = jSettingService.getMaxId(this.param);
                idKey = "table_increment_pk";
            }  else if ("j_ringdata".equals(tableName)) {
                dataByProjectIds = jRingDataService.getDataByProjectIds(this.param);
                id = jRingDataService.getMaxId(this.param);
                idKey = "table_increment_pk";
            } else if (tableName.indexOf("j_disdata_") >= 0) {
                dataByProjectIds = jDisDataService.getDataByProjectIds(this.param);
                id = jDisDataService.getMaxId(this.param);
                idKey = "table_increment_pk";
            } else if (tableName.indexOf("j_mileagedata_") >= 0) {
                dataByProjectIds = jMileageDataService.getDataByProjectIds(this.param);
                id = jMileageDataService.getMaxId(this.param);
                idKey = "table_increment_pk";
            } else if ("p_work_plan".equals(tableName)) {
                dataByProjectIds = workPlanService.getDataByProjectIds(this.param);
                id = workPlanService.getMaxId(this.param);
                idKey = "id";
            }
            if (id != null && id > 0 && dataByProjectIds != null && dataByProjectIds.size() > 0) {
                clientMaxId = dataByProjectIds.get(dataByProjectIds.size() - 1).getInteger(idKey);
                if (id > clientMaxId) {
                    returnInfo.put("getData", true);
                } else {
                    returnInfo.put("getData", false);
                }
            } else {
                returnInfo.put("getData", false);
            }

            returnInfo.put("data", dataByProjectIds);
            returnInfo.put("clientMaxId", clientMaxId);
            returnInfo.put("tableName", tableName);
            return returnInfo;
        }
    }

    /**
     * 获取dis mileage表名
     */
    private  void initTableName(){
        String sql = "raising.db.getTableNames";
        //查询dis,mileage表名集合
        List<String> tableNames = sqlSessionTemplate.selectList(sql);
        String dataSyncTableName = PropertiesValue.DATA_SYNC_TABLE_NAME;
        JSONArray arr = JSONArray.parseArray(dataSyncTableName);
        arr.addAll(tableNames);
        redisTemplate.opsForValue().set("dataSyncTableNames",arr.toJSONString());
    }


}
