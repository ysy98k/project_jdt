package com.raising.forward.service.materialConsumption;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.common.NewBaseService;
import com.util.DateUtils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(value = "materialConsumptionService")
public class MaterialConsumptionService extends NewBaseService {

    /**
     * 获得记录
     * @param paramInfo
     * @return
     * @throws ParseException
     */
    public JSONObject getRows(JSONObject paramInfo) throws ParseException {
        JSONObject returnInfo = new JSONObject();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        paramInfo.put("projectId",paramInfo.getInteger("projectId"));

        String startTime = paramInfo.getString("startTime");
        String endTime = paramInfo.getString("endTime");
        String materialType = paramInfo.getString("materialType");
        String dateType = paramInfo.getString("type");//查询数据的类型，日，月，季度
        String unit = paramInfo.getString("unit");
        String attribute = materialType;//存放消耗量的属性名。
        double theory = paramInfo.getDoubleValue("theory");

        if(StringUtils.isNullOrEmpty(startTime)){
            Timestamp compareTime = getCompareTime(paramInfo,dateType);
            paramInfo.put("startTime",compareTime);
        }else{
            Date date = dateFormat.parse(startTime);
            Timestamp t = new Timestamp(date.getTime());
            paramInfo.put("startTime",t);
        }
        if(!StringUtils.isNullOrEmpty(endTime)){
            Date date = dateFormat.parse(endTime);
            Timestamp t = new Timestamp(date.getTime());
            paramInfo.put("endTime",t);
        }

        String sql = "com.raising.forward.j.mapper.JRingData.getARDataAndProjectInfo";
        //查询时，为保证最早一天完整囊括白班，夜班。会查询最近11天的数。并抛弃最早一天的记录
        List<JSONObject> ringNumsData = sqlSessionTemplate.selectList(sql, paramInfo);//查询经过时间筛选后的记录。最近十天或者起始时间规定时间
        if(ringNumsData == null || ringNumsData.size() < 1){
            returnInfo.put("total",1);
            returnInfo.put("page",paramInfo.getInteger("curPage"));
            returnInfo.put("records",0);
            returnInfo.put("rows",new JSONArray());
            returnInfo.put("status", Constants.EXECUTE_SUCCESS);
            returnInfo.put("returnMsg","查询成功！本次返回0条记录，总共0条记录！");
            return returnInfo;
        }

        List<JSONObject> returnList =  operatorResult(ringNumsData,dateType,attribute,theory,unit);
        int curPage = paramInfo.getInteger("curPage");
        int curRowNum = paramInfo.getInteger("curRowNum");
        int totalPage = (returnList.size()%curRowNum ==0)?returnList.size()/curRowNum:(returnList.size()/curRowNum+1);//总页数
        int index = (curPage -1)*curRowNum;
        int toIndex =  returnList.size() >= curRowNum*curPage ? curRowNum*curPage : returnList.size();
        List<JSONObject> rows = returnList.subList(index, toIndex);

        returnInfo.put("total",totalPage);
        returnInfo.put("page",paramInfo.getInteger("curPage"));
        returnInfo.put("records",returnList.size());
        returnInfo.put("rows",rows);
        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        returnInfo.put("returnMsg","查询成功！本次返回"+rows.size()+"条记录，总共"+returnList.size()+"条记录！");
        return returnInfo;
    }

    /**
     * 环处理
     * @return
     */
    public JSONObject operatorRingData(JSONObject paramInfo){

        JSONObject returnInfo = new JSONObject();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String materialType = paramInfo.getString("materialType");
        String unit = paramInfo.getString("unit");
        //获得单位，每环理论消耗值，以及存放消耗量的属性名
        double theory = paramInfo.getDoubleValue("theory");

        //数据校验
        String querySql = "com.raising.forward.j.mapper.JRingData.getArData";
        String countSql = "com.raising.forward.j.mapper.JRingData.getCount";
        String entity = "com.raising.forward.entity.j.JRingData";
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, querySql);
        paramInfo.put(BaseService.COUNT_SQL, countSql);
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        returnInfo = baseDao.query(paramInfo);

        if(Constants.EXECUTE_SUCCESS.equals(returnInfo.getString("status"))){
            double theoreticalAccumulation = 0.0;//理论累计
            double actualCumulative = 0.0;//实际累计
            double totalExcessConsumption = 0.0;//总超耗量

            JSONArray rows = returnInfo.getJSONArray("rows");
            for(int i=0;i<rows.size();i++){
                JSONObject tempRow = rows.getJSONObject(i);
                tempRow.put("x",tempRow.getInteger("MR_Ring_Num"));
                String arDataStr = tempRow.getJSONObject("ARData").getString("value");
                JSONObject arData = JSONObject.parseObject(arDataStr);
                double value = arData.getDoubleValue(materialType);
                double excess = value - theory < 0 ? 0 : value - theory ;
                theoreticalAccumulation += theory;
                actualCumulative += value;
                totalExcessConsumption += excess;
                tempRow.put("unit",unit);//单位
                tempRow.put("theoreticalConsumption",decimalFormat.format(theory));//理论消耗
                tempRow.put("actualConsumption",decimalFormat.format(value));//实际消耗
                tempRow.put("excess",decimalFormat.format(excess));//超耗量
                tempRow.put("theoreticalAccumulation",decimalFormat.format(theoreticalAccumulation));//理论累计
                tempRow.put("actualAccumulation",decimalFormat.format(actualCumulative));//实际累计
                tempRow.put("totalExcess",decimalFormat.format(totalExcessConsumption));//总超耗量
            }
        }



        return returnInfo;
}

    /**
     * 根据查询的数据，做处理。
     * @param ringNumsData
     * @param type
     * @param attribute
     * @param theoreticalConsumption 一环理论消耗
     * @return
     */
    private List<JSONObject> operatorResult(List<JSONObject> ringNumsData,String type,String attribute,double theory,String unit){
        DecimalFormat format = new DecimalFormat("#.##");
        List<JSONObject> rowsList = getRowsList(ringNumsData, type);
        int initRingNum = ringNumsData.get(0).getInteger("ringNum");//初始环号。
        double consumption = 0.0;//累计消耗量
        double theoreticalAccumulation = 0.0;//理论累计
        double actualAccumulation = 0.0;//实际累计
        double totalExcess = 0.0;//总超耗量

        for(int i=0;i<rowsList.size();i++){//填充前台显示表中每行记录，每一个元素对应一行记录
            JSONObject oneRow = rowsList.get(i);

            Long start = oneRow.getLong("start");//当前记录的起始时间
            Long end = oneRow.getLong("end");//当前记录的结束时间

            double oneRowTheoreticalConsumption = 0.0;//一行理论消耗值
            double actualConsumption = 0.0;//实际消耗
            double excess = 0.0;//超耗量
            int work = 0;//环数

            Map<String, Object> oneRowWork = getOneRowData(start, end, ringNumsData, initRingNum, attribute);
            work = (int)oneRowWork.get("work");
            initRingNum = (int)oneRowWork.get("initRingNum");
            actualConsumption = (double)oneRowWork.get("actualConsumption");
            consumption += actualConsumption;
            oneRowTheoreticalConsumption = theory * work;
            excess = actualConsumption - oneRowTheoreticalConsumption < 0 ? 0 : actualConsumption - oneRowTheoreticalConsumption;
            theoreticalAccumulation += oneRowTheoreticalConsumption;
            actualAccumulation += actualConsumption;
            totalExcess += excess;

            oneRow.put("x",oneRow.getString("date"));//当天累计
            oneRow.put("unit",unit);//当天设计
            oneRow.put("theoreticalConsumption",format.format(oneRowTheoreticalConsumption));//理论消耗
            oneRow.put("actualConsumption",format.format(actualConsumption));//实际消耗
            oneRow.put("excess",format.format(excess));//超耗量
            oneRow.put("theoreticalAccumulation",format.format(theoreticalAccumulation));//理论累计
            oneRow.put("actualAccumulation",format.format(actualAccumulation));//实际累计
            oneRow.put("totalExcess",format.format(totalExcess));//总超耗量
        }
        return rowsList;
    }


    /**
     * 获取前台显示每一条记录集合。集合内一个元素为前台显示的一条记录。
     * @param ringNumsData
     * @param type
     * @return
     */
    private List<JSONObject> getRowsList(List<JSONObject> ringNumsData,String type){
        //查询时，为保证最早一天完整囊括白班，夜班。会查询最近11天的数。并抛弃最早一天。
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = null;
        List<JSONObject> rowsList = new ArrayList<>();
        for(int i=0;i<ringNumsData.size();i++){ //遍历值循环 填充dayList,获取每天白班夜班的时间集合。每一个
            JSONObject dataTemp = ringNumsData.get(i);
            Timestamp ringDate = dataTemp.getTimestamp("ringDate");

            String dayShiftStart = dataTemp.getString("dayShiftStart");//白班 时间数 7:00
            String dayShiftEnd = dataTemp.getString("dayShiftEnd");//夜班时间数 19:00
            if("day".equals(type)){
                currentDate = currentDate  == null ? ringDate : currentDate;
                long daySub = DateUtils.getTimeLimitForProject(currentDate.toString(), ringDate.toString());
                if(i == 0 || daySub > 1){
                    currentDate = ringDate;
                    JSONObject dayObj = new JSONObject();

                    JSONObject timeJson = getTime(ringDate,dayShiftStart,dayShiftEnd,type);
                    long dayStart = timeJson.getLongValue("start");
                    long dayEnd = timeJson.getLongValue("end");

                    dayObj.put("date",format.format(ringDate));
                    dayObj.put("start",dayStart);
                    dayObj.put("end",dayEnd);
                    dayObj.put("ringTotal",dataTemp.getInteger("ringTotal"));
                    dayObj.put("projectStartTime",dataTemp.getString("startTime"));
                    dayObj.put("projectEndTime",dataTemp.getString("endTime"));
                    rowsList.add(dayObj);
                }
            }else if("month".equals(type) || "season".equals(type)){
                currentDate = currentDate  == null ? ringDate : currentDate;
                int sub = 0;
                if("month".equals(type)){
                    sub = DateUtils.getMonthSub(currentDate.getTime(),ringDate.getTime());
                }else{
                    long ringTime = DateUtils.getCurrentQuarterStartTime(ringDate.getTime());
                    long currentTime = DateUtils.getNextQuarterStartTime(currentDate.getTime());
                    boolean b = DateUtils.sameQuaeter(ringTime, currentTime);
                    sub = b ==true ? 1 : 0;
                }
                if(i == 0 || sub > 0){
                    currentDate = ringDate;
                    JSONObject rowObj = new JSONObject();

                    JSONObject timeJson = getTime(ringDate,dayShiftStart,dayShiftEnd,type);
                    long start = timeJson.getLongValue("start");
                    long end = timeJson.getLongValue("end");
                    rowObj.put("date",formatDate(new Date(start),type));
                    rowObj.put("start",start);
                    rowObj.put("end",end);
                    rowObj.put("ringTotal",dataTemp.getInteger("ringTotal"));
                    rowObj.put("projectStartTime",dataTemp.getString("startTime"));
                    rowObj.put("projectEndTime",dataTemp.getString("endTime"));
                    rowsList.add(rowObj);
                }
            }
        }

        return  rowsList;
    }


    /**
     *
     * @param start
     * @param end
     * @param ringNumsData
     * @param initRingNum
     * @param attribute
     * @return
     */
    private Map<String,Object> getOneRowData(Long start, Long end, List<JSONObject> ringNumsData,int initRingNum,String attribute){
        double actualConsumption = 0.0;//实际消耗量
        int work = 0;

        for(int j=0;j<ringNumsData.size();j++){//遍历推进环数信息，获得每天的白班，夜班推进环数
            JSONObject ringObj = ringNumsData.get(j);
            long ringTime = ringObj.getTimestamp("ringDate").getTime();
            if(ringTime >= end){
                break;
            }else if(start <= ringTime){
                //出现开始记录，寻找结束记录
                for(int k=j;k<ringNumsData.size();k++){
                    JSONObject temp1 = ringNumsData.get(k);
                    long temp2 = temp1.getTimestamp("ringDate").getTime();
                    if(temp2 >= end){
                        work = temp1.getInteger("ringNum") - initRingNum;//工作量
                        initRingNum = temp1.getInteger("ringNum");//更新初始化环号信息
                        break;
                    }
                    if(k == ringNumsData.size()-1){
                        work = temp1.getInteger("ringNum") - initRingNum;//工作量
                        initRingNum = temp1.getInteger("ringNum");//更新初始化环号信息。
                    }
                    String temp3 =  temp1.getJSONObject("ARData").getString("value");
                    JSONObject tempJson = JSONObject.parseObject(temp3);
                    try{
                        double value = tempJson.getDoubleValue(attribute);
                        actualConsumption += value;
                    }catch (Exception e){
                        actualConsumption +=0.0;
                    }
                }
                break;
            }
        }
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("work",work);
        resultMap.put("initRingNum",initRingNum);
        resultMap.put("actualConsumption",actualConsumption);
        return resultMap;
    }



    /**
     * 根据类型（日月季），获取数据的起始时间
     * 获得表中最新记录十天前的时间
     * @param ajaxJson
     * @return
     */
    private Timestamp getCompareTime(JSONObject ajaxJson,String type){
        String sql = "com.raising.forward.j.mapper.JRingData.getCompareTime";
        JSONObject paramJson = new JSONObject();
        paramJson.put("projectId",ajaxJson.getInteger("projectId"));
        JSONObject result = sqlSessionTemplate.selectOne(sql, paramJson);
        if(result == null){
            return null;
        }
        Timestamp dt = result.getTimestamp("dt");
        String dayShiftStart = result.getString("dayShiftStart");
        String dayShiftEnd = result.getString("dayShiftEnd");
        JSONObject timeJson = getTime(dt,dayShiftStart,dayShiftEnd,type);
        long dayStart = timeJson.getLongValue("start");
        if("season".equals(type)){
            return null;
        }else{
            Long differenceDayTime = DateUtils.getDifferenceDayTime(dayStart, -9,type);
            return new Timestamp(differenceDayTime);
        }
    }

    /**
     * 获得某一天白班，夜班时间段
     * @param currentDate
     * @return
     */
    private JSONObject getTime(Date currentDate, String dayShiftStart, String dayShiftEnd, String type){
        JSONObject returnJson = new JSONObject();
        int dayShiftStartHour = 7;
        int dayShiftStartMinute = 0;
        int dayShiftEndHour = 19;
        int dayShiftEndMinute = 0;
        if(!StringUtils.isNullOrEmpty(dayShiftStart)){
            String[] split1 = dayShiftStart.split(":");
            String[] split2 = dayShiftEnd.split(":");
            dayShiftStartHour = Integer.parseInt(split1[0]);
            dayShiftStartMinute = Integer.parseInt(split1[1]);
            dayShiftEndHour = Integer.parseInt(split2[0]);
            dayShiftEndMinute = Integer.parseInt(split2[1]);
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int i1 = cal.get(Calendar.YEAR);
        int i2 = cal.get(Calendar.MONTH);
        int i3 = cal.get(Calendar.DAY_OF_MONTH);
        if("month".equals(type)){
            //cal.set(i1,i2,i3,dayShiftStartHour,dayShiftStartMinute,00);//白班起始时间
            cal.set(Calendar.DAY_OF_MONTH,1);
            cal.set(Calendar.HOUR_OF_DAY,dayShiftStartHour);
            cal.set(Calendar.MINUTE,dayShiftStartMinute);
            cal.set(Calendar.SECOND,0);
            long start = cal.getTimeInMillis();
            cal.set(Calendar.MONTH,i2+1);
            cal.set(Calendar.DAY_OF_MONTH,1);
            cal.set(Calendar.HOUR_OF_DAY,dayShiftStartHour);
            cal.set(Calendar.MINUTE,dayShiftStartMinute);
            long end = cal.getTimeInMillis();
            returnJson.put("start",start);
            returnJson.put("end",end);
        }else if("season".equals(type)){
            long currentQuarterStartTime = DateUtils.getCurrentQuarterStartTime(currentDate.getTime());
            cal.setTimeInMillis(currentQuarterStartTime);
            cal.set(Calendar.HOUR_OF_DAY,dayShiftStartHour);
            cal.set(Calendar.MINUTE,dayShiftStartMinute);
            cal.set(Calendar.SECOND,0);
            long start = cal.getTimeInMillis();
            long nextQuarterStartTime = DateUtils.getNextQuarterStartTime(currentDate.getTime());
            cal.setTimeInMillis(nextQuarterStartTime);
            cal.set(Calendar.HOUR_OF_DAY,dayShiftStartHour);
            cal.set(Calendar.MINUTE,dayShiftStartMinute);
            cal.set(Calendar.SECOND,0);
            long end = cal.getTimeInMillis();
            returnJson.put("start",start);
            returnJson.put("end",end);
        }else if("day".equals(type)){
            cal.set(i1,i2,i3,dayShiftStartHour,dayShiftStartMinute,00);//白班起始时间
            long dayStart = cal.getTime().getTime();
            //cal.set(i1,i2,i3,dayShiftEndHour,dayShiftEndMinute,00);//白班结束时间，夜班起始时间
            cal.set(i1,i2,i3+1,dayShiftStartHour,dayShiftStartMinute,00);//夜班结束时间
            long nightEnd = cal.getTime().getTime();
            returnJson.put("start",dayStart);
            returnJson.put("end",nightEnd);

        }

        return  returnJson;
    }

    /**
     * 格式化时间。前台表中日期列
     * @param date
     * @param type
     * @return
     */
    private  String formatDate(Date date ,String type){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String result = null;
        if("month".equals(type)){
            result = format.format(date);
        }else if("season".equals(type)){
            long currentQuarterStartTime = DateUtils.getCurrentQuarterStartTime(date.getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentQuarterStartTime);
            int i = calendar.get(Calendar.MONTH) + 1;
            int i1 = calendar.get(Calendar.YEAR);
            if(i >= 1 && i <= 3){
                result = i1+"——第一季度";
            }else if(i >= 4 && i <= 6){
                result = i1+"——第二季度";
            }else if(i >= 7 && i <= 9){
                result = i1+"——第三季度";
            }else if(i >= 10 && i <= 12){
                result = i1+"——第四季度";
            }
        }
        return result;
    }


}
