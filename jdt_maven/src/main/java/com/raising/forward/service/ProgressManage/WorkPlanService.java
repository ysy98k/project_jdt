package com.raising.forward.service.ProgressManage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.common.NewBaseService;
import com.util.DateUtils;
import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.baosight.common.utils.DateUtils.String2Date;
import static com.baosight.common.utils.DateUtils.date2String;

@Service
@Transactional
public class WorkPlanService extends NewBaseService {


    public JSONObject getRows(String ajaxParam){
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = new JSONObject();
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        String querySql = "com.raising.forward.progressManage.mapper.workPlanMapper.getRows";
        String countSql = "com.raising.forward.progressManage.mapper.workPlanMapper.count";
        String entity = "com.raising.forward.entity.progressManage.WorkPlan";
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put("curRowNum",paramInfo.getInteger("curRowNum")-2 );//留两行，用来显示起始和结束时间
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
        for(int i=0;i<detailsArray.size();i++){
            JSONObject temp = detailsArray.getJSONObject(i);
            Date startDate = new Date(temp.getLong("startTime"));
            Date endDate = new Date(temp.getLong("endTime"));
            Date planDate = new Date(temp.getLong("planTime"));
            String startTime = date2String("yyyy-MM-dd",startDate);
            String endTime = date2String("yyyy-MM-dd",endDate);
            String planTime = date2String("yyyy-MM-dd",planDate);
            temp.put("startTime",startTime);
            temp.put("endTime",endTime);
            temp.put("planTime",planTime);
        }
        JSONObject info = detailsArray.getJSONObject(0);
        String sTime = info.getString("startTime");
        String eTime = info.getString("endTime");
        long daySub = DateUtils.getTimeLimitForProject(sTime, eTime);//计算工期
        Integer ringTotal =  info.getInteger("ringTotal");
        JSONObject firstRow = new JSONObject();
        firstRow.put("projectId",info.getInteger("projectId"));
        firstRow.put("planTime",sTime);
        firstRow.put("startTime",sTime);
        firstRow.put("project",true);
        firstRow.put("schedule","");
        firstRow.put("ringNum","");
        firstRow.put("remark","");
        JSONObject endRow = new JSONObject();
        endRow.put("projectId",info.getInteger("projectId"));
        endRow.put("planTime",eTime);
        endRow.put("startTime",sTime);
        endRow.put("project",true);
        endRow.put("end",true);
        endRow.put("schedule",daySub);
        endRow.put("ringNum",ringTotal);
        endRow.put("remark","");
        detailsArray.add(0,firstRow);
        detailsArray.add(endRow);
        return returnInfo;
    }


    public JSONObject addRow(String ajaxParam) throws Exception {
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONArray dataArray = paramInfo.getJSONObject("detail").getJSONArray("resultRow");
        for(int i=0;i<dataArray.size();i++){
            JSONObject dataJson = dataArray.getJSONObject(i);
            //将时间字符串转换为Date类型
            String startTimeStr = null;
            String planTimeStr = dataJson.getString("planTime");
            JSONObject previousRow = getPreviousRow(dataJson);
            if(previousRow == null){
                startTimeStr = dataJson.getString("startTime");
            }else{
                startTimeStr = previousRow.getString("planTimeStr");
            }
            long daySub = DateUtils.getDaySub(startTimeStr, planTimeStr);//计算工期
            dataJson.put("planTime", String2Date(new String(planTimeStr), "yyyy-MM-dd"));
            dataJson.put("schedule", daySub);
            dataJson.put("ringNum", dataJson.getInteger("ringNum"));
            dataJson.put("projectId", dataJson.getInteger("projectId"));
        }
        String insertSql = "com.raising.forward.progressManage.mapper.workPlanMapper.addRow";
        String entity = "com.raising.forward.entity.progressManage.WorkPlan";
        paramInfo.put(BaseService.RESULT_BLOCK,"detail");
        paramInfo.put(BaseService.INSERT_SQL, insertSql);
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        JSONObject returnInfo = daoUtil.insert(paramInfo);
        return returnInfo;
    }


    public JSONObject updateRow(String ajaxParam) throws Exception {
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONArray dataArray = paramInfo.getJSONObject("detail").getJSONArray("resultRow");
        for(int i=0;i<dataArray.size();i++){
            JSONObject dataJson = dataArray.getJSONObject(i);
            //将时间字符串转换为Date类型
            String startTimeStr = null;
            String planTimeStr = dataJson.getString("planTime");
            JSONObject previousRow = getPreviousRow(dataJson);
            if(previousRow == null){
                startTimeStr = dataJson.getString("startTime");
            }else{
                startTimeStr = previousRow.getString("planTimeStr");
            }
            long daySub = DateUtils.getTimeLimitForProject(startTimeStr, planTimeStr);//计算工期
            dataJson.put("planTime", String2Date(new String(planTimeStr), "yyyy-MM-dd"));
            dataJson.put("schedule", daySub);
            dataJson.put("ringNum", dataJson.getInteger("ringNum"));
        }
        String updateSql = "com.raising.forward.progressManage.mapper.workPlanMapper.updateRow";
        String entity = "com.raising.forward.entity.progressManage.WorkPlan";
        paramInfo.put(BaseService.UPDATE_SQL, updateSql);
        paramInfo.put(BaseService.RESULT_BLOCK, "detail");
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        JSONObject returnInfo = daoUtil.update(paramInfo);
        return returnInfo;
    }


    public JSONObject deleteRows(String ajaxParam) throws Exception {
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONArray dataArray = paramInfo.getJSONObject("result").getJSONArray("resultRow");
        for(int i=0;i<dataArray.size();i++){
            JSONObject dataJson = dataArray.getJSONObject(i);
            Integer planId = dataJson.getIntValue("planId");
            dataJson.clear();
            dataJson.put("planId",planId);
        }
        String deleteSql = "com.raising.forward.progressManage.mapper.workPlanMapper.deleteRow";
        String entity = "com.raising.forward.entity.progressManage.WorkPlan";
        paramInfo.put(BaseService.DELETE_SQL, deleteSql);
        paramInfo.put(BaseService.RESULT_BLOCK, "result");
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        JSONObject returnInfo = null;

        returnInfo = daoUtil.delete(paramInfo);
        return returnInfo;
    }

    public JSONObject getPreviousRow(JSONObject ajaxParam) throws ParseException {
        JSONObject paramJson = new JSONObject();
        String planTime = ajaxParam.getString("planTime");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = format.parse(planTime);
        paramJson.put("tenant",request.getSession().getAttribute("tenant"));
        paramJson.put("projectId",ajaxParam.getInteger("projectId"));
        paramJson.put("planTime",parse);
        paramJson.put("planId",ajaxParam.getInteger("planId"));
        String sql = "com.raising.forward.progressManage.mapper.workPlanMapper.getPervioueRow";
        JSONObject result = sqlSessionTemplate.selectOne(sql, paramJson);
        if(result != null){
            //long time = result.getTimestamp("planTime").getTime()+86400000;//上一条记录的时间加一天
            long time = result.getTimestamp("planTime").getTime();//上一条记录的时间加一天
            Date planDate = new Date(time);
            String planTimeStr = date2String("yyyy-MM-dd",planDate);
            result.put("planTimeStr",planTimeStr);
        }
        return result;
    }

    /**
     * 根据项目Id，获取所有数据
     * @param paramJson
     * @return
     */
    public List<JSONObject> getDataByProjectIds(JSONObject paramJson){
        String sql = "com.raising.forward.progressManage.mapper.workPlanMapper.getAllData";
        List<JSONObject> data = sqlSessionTemplate.selectList(sql, paramJson);
        return data;
    }

    /**
     * 根据项目id，获取最大Id
     * @param paramJson
     * @return
     */
    public Integer getMaxId(JSONObject paramJson){
        String sql = "com.raising.forward.progressManage.mapper.workPlanMapper.getMaxId";
        Integer max = sqlSessionTemplate.selectOne(sql, paramJson);
        return max;
    }


}
