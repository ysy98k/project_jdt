package com.raising.forward.service.j;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.common.NewBaseService;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class JRingDataService extends NewBaseService {

    public JSONObject getData(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);

        List<Integer> ringNum = getRingNum(ajaxParam);

        String sql = "com.raising.forward.j.mapper.JRingData.getArData";

        List<JSONObject> objectList = sqlSessionTemplate.selectList(sql, paramInfo);
        JSONArray lineArr = paramInfo.getJSONArray("lineArr");
        List<String> xAxis  = new ArrayList<>();//行程
        Integer currentRingNum = null;
        if(paramInfo.getInteger("MR_Ring_Num") == null && objectList.size() > 0){//如果是第一次查询。则默认显示最小环
            currentRingNum = objectList.get(0).getInteger("MR_Ring_Num");
        }
        for(int i=0;i<lineArr.size();i++){
            List<String> data = new ArrayList<>();

            String columnName = lineArr.getString(i);
            int xAxisStart = 0;

            for(int k = 0;k<objectList.size();k++){
                JSONObject temp = objectList.get(k);
                if(currentRingNum !=null && temp.getInteger("MR_Ring_Num") != currentRingNum){
                    continue;
                }
                String arDataStr = temp.getJSONObject("ARData").getString("value");
                JSONObject arData = JSONObject.parseObject(arDataStr);
                data.add(arData.getString(columnName));
                if(i == 0 && k == 0){
                    xAxisStart = temp.getInteger("CurMS");
                    xAxis.add("0");
                }else if(i == 0){
                    xAxis.add( temp.getInteger("CurMS") - xAxisStart + "" );
                }
            }
            returnInfo.put(columnName,data);
            if(i == 0){
                returnInfo.put("xAxis",xAxis);//存放里程信息
            }
        }
        returnInfo.put("status", Constants.EXECUTE_SUCCESS);
        if(paramInfo.getInteger("MR_Ring_Num") == null){
            returnInfo.put("currentRingNum",currentRingNum == null ? -1:currentRingNum);
        }
        return returnInfo;
    }


    public List<Integer> getRingNum(String ajaxParam){
        JSONObject paramJson = JSONObject.parseObject(ajaxParam);
        String sql = "com.raising.forward.j.mapper.JRingData.getRingNum";
        List<Integer> ringNums =  sqlSessionTemplate.selectList(sql,paramJson);
        if(ringNums == null){
            ringNums = new ArrayList<>();
        }
        return ringNums;

    }

    public JSONObject getRows(String ajaxParam){
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = new JSONObject();
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        //数据校验
        String projectId = inquStatus.getString("projectId");
        inquStatus.put("projectId",Integer.parseInt(projectId));

        String querySql = "com.raising.forward.j.mapper.JRingData.getArData";
        String countSql = "com.raising.forward.j.mapper.JRingData.getCount";
        String entity = "com.raising.forward.entity.j.JRingData";
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, querySql);
        paramInfo.put(BaseService.COUNT_SQL, countSql);
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        returnInfo = baseDao.query(paramInfo);
        //获取所有环号。做查询条件
        List<Integer> ringNumsList = getRingNum(inquStatus.toJSONString());

        if(ringNumsList == null || ringNumsList.size() < 1){
            returnInfo.put("maxRingNum","");
            returnInfo.put("minRingNum","");
        }else{
            returnInfo.put("maxRingNum",ringNumsList.get(ringNumsList.size() -1));
            returnInfo.put("minRingNum",ringNumsList.get(0));
        }


        JSONArray rows =  returnInfo.getJSONArray("rows");
        if(rows == null || rows.size() < 1){
            return returnInfo;
        }
        /*筛选查询数据*/
        JSONArray columnArr = paramInfo.getJSONArray("columnArr");
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        for(int i=0;i<rows.size();i++){
            JSONObject temp = rows.getJSONObject(i);
            Set<String> keySet = temp.keySet();
            String arDataStr = temp.getJSONObject("ARData").getString("value");
            JSONObject arData = JSONObject.parseObject(arDataStr);
            for(int k=0;k<columnArr.size();k++){
                String columnStr = columnArr.getString(k);
                if(!keySet.contains(columnStr)){//如果要查的数据，temp对象中没有。就从temp的ARData属性中取
                    temp.put(columnStr,decimalFormat.format(arData.getDoubleValue(columnStr)));
                }

            }
        }
        returnInfo.put("ringNums",ringNumsList);
        return returnInfo;
    }


    /**
     * 获得某一环的拼装时间，掘进时间，准备时间
     * @return
     */
    public JSONObject getTaskTimeForRingNum(Integer projectId,Integer ringNum){
        JSONObject returnInfo = new JSONObject();
        JSONObject paramJson = new JSONObject();
        paramJson.put("projectId",projectId);
        paramJson.put("MR_Ring_Num",ringNum);
        String sql = "com.raising.forward.j.mapper.JRingData.getTaskTime";
        List<JSONObject> list = sqlSessionTemplate.selectList(sql, paramJson);
        if(list.size() < 1){
            return null;
        }
        JSONObject data = list.get(0);
        double assembleDate =  data.getDoubleValue("assemblingTime")/3600;//拼装时长
        double tunnellingDate =  data.getDoubleValue("tunnellingTime")/3600;//掘进时长
        double shutDownDate =  data.getDoubleValue("ringTotalTime")/3600 - data.getDoubleValue("tunnellingTime")/3600;//停机时长

        returnInfo.put("assembleTime",assembleDate);
        returnInfo.put("tunnellingTime",tunnellingDate);
        returnInfo.put("shuDownTime",shutDownDate);
        return returnInfo;

    }

    /**
     * 获得 报表导出 排土量表格数据
     * @param projectId
     * @param startRing
     * @param endRing
     * @return
     */
    public JSONObject getSoilReport(Integer projectId,Integer startRing,Integer endRing){
        JSONObject returnInfo = new JSONObject();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        JSONObject paramJson = new JSONObject();
        paramJson.put("projectId",projectId);
        if(startRing != null){
            paramJson.put("startRingNum",startRing);
            paramJson.put("endRingNum",endRing);
        }
        String sql = "com.raising.forward.j.mapper.JRingData.getArData";
        List<JSONObject> objectList = sqlSessionTemplate.selectList(sql, paramJson);

        //封装数据
        List<Integer> ringsList = new ArrayList<>();
        List<String> ring_Dumping_volume_Total = new ArrayList<>();
        List<String> lowerLimit = new ArrayList<>();
        List<String> upperLimit = new ArrayList<>();
        List<String> ring_Dumping_volume_DTotal = new ArrayList<>();
        for(int i=0;i<objectList.size();i++){
            JSONObject temp = objectList.get(i);

            if( (endRing !=null && endRing <temp.getInteger("MR_Ring_Num")) || i == 9){
                break;
            }

            String arDataStr = temp.getJSONObject("ARData").getString("value");
            JSONObject arData = JSONObject.parseObject(arDataStr);
            Double dumping_volume_dTotal = arData.getDouble("Dumping_volume_DTotal");
            ringsList.add(temp.getInteger("MR_Ring_Num"));
            ring_Dumping_volume_Total.add(  decimalFormat.format(arData.getDoubleValue("Dumping_volume_Total"))  );//累计体积
            Double lowerTemp =  arData.getDouble("Dumping_volume_DTotal")== null ? null : arData.getDouble("Dumping_volume_DTotal")*0.9;
            Double uperTemp = arData.getDouble("Dumping_volume_DTotal")== null ? null : arData.getDouble("Dumping_volume_DTotal")*1.1 ;

            lowerLimit.add(lowerTemp == null ? "" : decimalFormat.format(lowerTemp));//下线
            upperLimit.add(uperTemp == null ? "" : decimalFormat.format(uperTemp));//上线
            ring_Dumping_volume_DTotal.add( decimalFormat.format(arData.getDoubleValue("Dumping_volume_DTotal")) );//理论体积
        }
        returnInfo.put("ringsList",ringsList);
        returnInfo.put("ring_Dumping_volume_Total",ring_Dumping_volume_Total);
        returnInfo.put("lowerLimit",lowerLimit);
        returnInfo.put("upperLimit",upperLimit);
        returnInfo.put("ring_Dumping_volume_DTotal",ring_Dumping_volume_DTotal);
        return  returnInfo;
    }

    /**
     * 大数据分析，获取多个项目的数据
     */
    public List<JSONObject> getRowsOfStatis(JSONObject paramJson){
        String sql = "com.raising.forward.j.mapper.JRingData.getRowsForStatis";
        List<JSONObject> data = sqlSessionTemplate.selectList(sql, paramJson);
        return data;
    }

    /**
     * 根据项目Id，获取所有数据
     * @param paramJson
     * @return
     */
    public List<JSONObject> getDataByProjectIds(JSONObject paramJson){
        String sql = "com.raising.forward.j.mapper.JRingData.getAllData";
        List<JSONObject> data = sqlSessionTemplate.selectList(sql, paramJson);
        return data;
    }

    /**
     * 根据项目id，获取最大Id
     * @param paramJson
     * @return
     */
    public Integer getMaxId(JSONObject paramJson){
        String sql = "com.raising.forward.j.mapper.JRingData.getMaxId";
        Integer max = sqlSessionTemplate.selectOne(sql, paramJson);
        return max;
    }


}
