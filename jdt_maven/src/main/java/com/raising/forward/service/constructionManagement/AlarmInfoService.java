package com.raising.forward.service.constructionManagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.utils.StringUtils;
import com.common.NewBaseService;
import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AlarmInfoService extends NewBaseService {

    @Transactional
    public JSONObject getAlarmInfo(String ajaxParam) throws Exception {
        JSONObject returnInfo = new JSONObject();
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        Integer projectId = inquStatus.getInteger("projectId");

        operatorResource(projectId);


        String querySql = "com.raising.forward.construction.mapper.AlarmMapper.getAlarmInfo";
        String countSql = "com.raising.forward.construction.mapper.AlarmMapper.getAlarmCount";
        String entity = "com.raising.forward.entity.construction.Alarm";
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, querySql);
        paramInfo.put(BaseService.COUNT_SQL, countSql);
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        returnInfo = daoUtil.query(paramInfo);
        JSONArray rows = returnInfo.getJSONArray("rows");
        if(rows == null || rows.size() < 1){
            return returnInfo;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i =0;i<rows.size();i++){
            JSONObject temp = rows.getJSONObject(i);
            Long startTime = temp.getLong("startTime");
            int alarmType = temp.getInteger("alarmType");
            temp.put("startTime",simpleDateFormat.format(new Date(startTime)));
            if(1 == alarmType){
                temp.put("alarmType","设备报警");
            }else if(2 == alarmType){
                temp.put("alarmType","风险报警");
            }else if(3 == alarmType){
                temp.put("alarmType","导向报警");
            }
        }

        return returnInfo;
    }

    /**
     * 判断是否更新alarm表记录。如果需要更新，则更新。
     * @param projectId
     * @return
     */
    public void operatorResource(Integer projectId) throws Exception {
        String getBasicConfigSql = "com.raising.forward.construction.mapper.AlarmCodeMapper.getAlarm";
        JSONObject currencyJson = new JSONObject();
        currencyJson.put("projectId",projectId);

        List<JSONObject> resourcesList = sqlSessionTemplate.selectList(getBasicConfigSql, currencyJson);
        if(resourcesList != null && resourcesList.size() == 0){//如果源数据没有任何记录。则不需要更新alarm表。直接返回
            return;
        }
        boolean needUpdate = false;
        String alarmSql = "com.raising.forward.construction.mapper.AlarmMapper.getLastTime";
        Timestamp lastTime = sqlSessionTemplate.selectOne(alarmSql,currencyJson);

        if(lastTime == null ){
            needUpdate =true;
        }else{
            long lastTimeTime = lastTime.getTime();
            for(int i =0;i<resourcesList.size();i++){//通过对比d_alarm与t_alarm时间；判断是否要更新 t_alarm表
                JSONObject temp = resourcesList.get(i);
                long updateTime = temp.getTimestamp("updateTime").getTime();
                if(lastTimeTime < updateTime){
                    needUpdate = true;
                    break;
                }
            }
        }
        if(needUpdate == false){//如果不需要更新alarm表。直接返回
            return;
        }
        //如果需要更新
        List<JSONObject> addAlarmInfoList = new ArrayList<>();
        List<String> deleteUuidList = new ArrayList<>();
        List<String> addUuidList = new ArrayList<>();//此变量用来记录需要增加的报警记录
        String getEditAlarm = "com.raising.forward.construction.mapper.AlarmMapper.getEidtRows";
        List<String> editAlarmUuidList = sqlSessionTemplate.selectList(getEditAlarm, currencyJson);//获取需要编辑的alarm记录
        for(int i =0;i<resourcesList.size();i++){
            JSONObject object = resourcesList.get(i);
            if(editAlarmUuidList.contains(object.getString("alarmUuid"))){//如果alarm表中有。则判断是否更新。
                // 考虑有可能需要更新多条记录。所以不使用循环更新记录。更新记录的思路是。删除旧记录，添加新记录。
                //如果alarm表中有。则alarm表中记录为报警记录。判断resourcesList。是否有报警解除记录即uuid相同但anAlarm=2的记录。
                //resourcesList。以uuid、updaeTime排序。anAlarm=2的记录如果存在。updateTime应该大于anAlarm=1的记录。
                //如果anAlarm=2的记录存在。则在resourcesList的下标为i+1
                if(i+1 == resourcesList.size()){
                    continue;
                }
                JSONObject nextObject = resourcesList.get(i+1);
                if(object.getString("alarmUuid").equals(nextObject.getString("alarmUuid")) && 2 == nextObject.getInteger("anAlarm")){
                    //如果此报警记录被解除。即。如果存在报警结束记录。则新建一条更新alarm表中记录.
                    deleteUuidList.add(object.getString("alarmUuid"));
                    JSONObject updateObject = new JSONObject();
                    updateObject.put("startTime",new Date(object.getTimestamp("updateTime").getTime()));
                    updateObject.put("endTime",new Date(nextObject.getTimestamp("updateTime").getTime()));
                    updateObject.put("projectId",object.getInteger("projectId"));
                    updateObject.put("alarmCodeId",object.getInteger("alarmCodeId"));
                    updateObject.put("updateTime",new Date(nextObject.getTimestamp("updateTime").getTime()));
                    updateObject.put("alarmUuid",object.getString("alarmUuid"));
                    addAlarmInfoList.add(updateObject);
                }
            }else{//如果alarm表中没有。则添加
                //判断此报警信息，是否被解决
                if(!addUuidList.contains(object.getString("alarmUuid"))){//如果没有。则代表这是一条新的anAlarm=1的报警记录
                    JSONObject addObject = new JSONObject();
                    Date endTime = null;
                    if(i+1 < resourcesList.size()){
                        JSONObject nextObject = resourcesList.get(i+1);
                        if(object.getString("alarmUuid").equals(nextObject.getString("alarmUuid")) && 2 == nextObject.getInteger("anAlarm")){
                            endTime = new Date(nextObject.getTimestamp("updateTime").getTime());
                        }
                    }
                    addObject.put("startTime",new Date(object.getTimestamp("updateTime").getTime()));
                    addObject.put("endTime",endTime);
                    addObject.put("projectId",object.getInteger("projectId"));
                    addObject.put("alarmCodeId",object.getInteger("alarmCodeId"));
                    addObject.put("updateTime",new Date(object.getTimestamp("updateTime").getTime()));
                    if(endTime != null){
                        addObject.put("updateTime",endTime);
                        i = i+1;//如果下一行是历史记录。则下一行的信息已经被提取。为避免重复插入，所以i+1
                    }
                    addObject.put("alarmUuid",object.getString("alarmUuid"));
                    addAlarmInfoList.add(addObject);
                }else{//如果有。则这是一条报警解除记录。直接忽视
                }
            }
        }
        //填充好操作记录。先删除
        if(deleteUuidList.size() > 0){
            String deleteSql = "com.raising.forward.construction.mapper.AlarmMapper.deleteAlarm";
            JSONObject deleteJson = new JSONObject();
            deleteJson.put("projectId",projectId);
            deleteJson.put("deleteUuids",deleteUuidList);
            int delete = sqlSessionTemplate.delete(deleteSql, deleteJson);
            if(delete < 1){
                throw new Exception("更新报警信息失败！");
            }
        }
        //增加
        String insertSql = "com.raising.forward.construction.mapper.AlarmMapper.addAlarm";
        JSONObject insertJson = new JSONObject();
        insertJson.put("addAlarmInfoList",addAlarmInfoList);
        int insert = sqlSessionTemplate.insert(insertSql, insertJson);
        if(insert < 1){
            throw new Exception("更新报警信息失败！");
        }
    }

    @Transactional
    public JSONObject getHistory(String ajaxParam) throws Exception {
        JSONObject returnInfo = new JSONObject();
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        Integer projectId = paramInfo.getInteger("projectId");
        operatorResource(projectId);

        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTimeStr = inquStatus.getString("startTime");
        String endTimeStr = inquStatus.getString("endTime");
        if(!StringUtils.isNullOrEmpty(startTimeStr)){
            inquStatus.put("startTime",dateFormat.parse(startTimeStr));
        }
        if(!StringUtils.isNullOrEmpty(endTimeStr)){
            inquStatus.put("endTime",dateFormat.parse(endTimeStr));
        }

        String querySql = "com.raising.forward.construction.mapper.AlarmMapper.getAlarmHistory";
        String countSql = "com.raising.forward.construction.mapper.AlarmMapper.getAlarmHistoryCount";
        String entity = "com.raising.forward.entity.construction.Alarm";
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, querySql);
        paramInfo.put(BaseService.COUNT_SQL, countSql);
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        returnInfo = daoUtil.query(paramInfo);

        JSONArray rows = returnInfo.getJSONArray("rows");
        if(rows == null || rows.size() < 1){
            return returnInfo;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i =0;i<rows.size();i++){
            JSONObject temp = rows.getJSONObject(i);
            Long startTime = temp.getLong("startTime");
            Long endTime = temp.getLong("endTime");
            int alarmType = temp.getInteger("alarmType");
            temp.put("startTime",simpleDateFormat.format(new Date(startTime)));
            temp.put("endTime",simpleDateFormat.format(new Date(endTime)));
            if(1 == alarmType){
                temp.put("alarmType","设备报警");
            }else if(2 == alarmType){
                temp.put("alarmType","风险报警");
            }else if(3 == alarmType){
                temp.put("alarmType","导向报警");
            }
        }
        return returnInfo;
    }

    /**
     * 根据项目Id，获取所有数据
     * @param paramJson
     * @return
     */
    public List<JSONObject> getDataByProjectIds(JSONObject paramJson){
        String sql = "com.raising.forward.construction.mapper.AlarmMapper.getAllData";
        List<JSONObject> data = sqlSessionTemplate.selectList(sql, paramJson);
        return data;
    }

    /**
     * 根据项目id，获取最大Id
     * @param paramJson
     * @return
     */
    public Integer getMaxId(JSONObject paramJson){
        String sql = "com.raising.forward.construction.mapper.AlarmMapper.getMaxId";
        Integer max = sqlSessionTemplate.selectOne(sql, paramJson);
        return max;
    }













}
