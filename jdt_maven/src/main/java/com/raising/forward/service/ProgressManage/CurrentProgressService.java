package com.raising.forward.service.ProgressManage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.common.NewBaseService;
import com.raising.rest.sdk.utils.DateUtil;
import com.util.DateUtils;
import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.baosight.common.utils.DateUtils.date2String;


@Service
public class CurrentProgressService extends NewBaseService{


    public JSONObject getRows(JSONObject paramInfo,JSONArray workPlanRows) throws ParseException {
        JSONObject returnInfo = new JSONObject();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        paramInfo.put("projectId",paramInfo.getInteger("projectId"));
        paramInfo.put("tenant",request.getSession().getAttribute("tenant"));
        String startTime = paramInfo.getString("startTime");
        String endTime = paramInfo.getString("endTime");
        String type = paramInfo.getString("type");//查询数据的类型，日，月，季度
        if(StringUtils.isNullOrEmpty(startTime)){
            Timestamp compareTime = getCompareTime(paramInfo,type);//如果起始时间为空，则查询前10天（包括当天），前十月（包括当月），以及所有季度的数据
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
        String sql = "com.raising.forward.progressManage.mapper.currentProgressMapper.getRows";
        //查询时，为保证最早一天完整囊括白班，夜班。会查询最近11天的数。并抛弃最早一天的记录
        List<JSONObject> ringNumsData = sqlSessionTemplate.selectList(sql, paramInfo);//查询经过时间筛选后的记录。最近十天或者起始时间规定时间
        if(ringNumsData == null || ringNumsData.size() < 1){
            returnInfo.put("total",1);
            returnInfo.put("page",paramInfo.getInteger("curPage"));
            returnInfo.put("records",0);
            returnInfo.put("rows",new JSONArray());
            returnInfo.put("status",Constants.EXECUTE_SUCCESS);
            returnInfo.put("returnMsg","查询成功！本次返回0条记录，总共0条记录！");
            return returnInfo;
        }
        List<JSONObject> returnList =  operatorResult(ringNumsData,workPlanRows,type);
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
     * 根据类型（日月季），获取数据的起始时间
     * 获得表中最新记录十天前的时间
     * @param ajaxJson
     * @return
     */
    private Timestamp getCompareTime(JSONObject ajaxJson,String type){
        String sql = "com.raising.forward.progressManage.mapper.currentProgressMapper.getCompareTime";
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
     * 操作结果集合
     * @param ringNumsData
     * @param workPlanRows
     * @return
     */
    private List<JSONObject> operatorResult(List<JSONObject> ringNumsData,JSONArray workPlanRows,String type){
        int grandTotal = 0;//累计
        List<JSONObject> rowsList = getRowsList(ringNumsData,type); //存放前台显示表中每行记录
        int initRingNum = ringNumsData.get(0).getInteger("ringNum");//初始环号。

        for(int i=0;i<rowsList.size();i++){//填充前台显示表中每行记录，每一个元素对应一行记录
            JSONObject oneRow = rowsList.get(i);

            Long start = oneRow.getLong("start");//当前天，白班的起始时间
            Long end = oneRow.getLong("end");//当前天，白班的结束时间，夜班的起始时间
            //Long nightEnd = oneRow.getLong("nightEnd");//当前天，夜班的结束时间。
            String DateStr = oneRow.getString("date");
            int work = 0;//（月，季度）工作量 ，（天）白班工作量
            int nightWork = 0;//夜班工作量
            int currentDayGrandTotal = 0;//当天（月，季度）累计环
            int currentRowDesign = 0;//当天(月，季度)设计环
            String currentProgressStr = "";//当天（月，季度）进度
            String completionSchedule = "";//完成进度

            Map<String, Integer> oneRowWork = getOneRowWork(initRingNum, start, end, ringNumsData,type);
            work = oneRowWork.get("work");
            nightWork = oneRowWork.get("nightWork");
            initRingNum = oneRowWork.get("initRingNum");

            currentDayGrandTotal = work+nightWork;//当天（月，季度）累计环
            Map<String, Object> designMap = null;
            try {
                designMap = getDesign(currentDayGrandTotal,initRingNum, DateStr, workPlanRows, oneRow,type);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            currentRowDesign = (Integer) designMap.get("currentRowDesign");
            currentProgressStr = (String) designMap.get("currentProgressStr");
            completionSchedule = (String) designMap.get("completionSchedule");

            grandTotal += currentDayGrandTotal;
            oneRow.put("work",work);//白班量
            if("day".equals(type)){
                oneRow.put("nightWork",nightWork);//夜班量
            }
            oneRow.put("grandTotalWork",currentDayGrandTotal);//当天累计
            oneRow.put("designWork",currentRowDesign);//当天设计
            oneRow.put("currentProgress",currentProgressStr+"%");//当天进度
            oneRow.put("grandTotal",grandTotal);//累计
            oneRow.put("completionSchedule",completionSchedule+"%");//完成进度


        }
        return rowsList;
    }

    /**
     *  获取前台显示每一条记录集合。集合内一个元素为前台显示的一条记录。
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
                    //long nightEnd = timeJson.getLongValue("nightEnd");

                    dayObj.put("date",format.format(ringDate));
                    dayObj.put("start",dayStart);
                    dayObj.put("end",dayEnd);
                    //dayObj.put("nightEnd",nightEnd);
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
     * 获取每天的，白班，夜班推进环数,并更新下一天的初始环数
     */
    private Map<String,Integer> getOneRowWork(int initRingNum ,Long start,Long end,List<JSONObject> ringNumsData,String type){
        int work = 0;//白班工作量
        int nightWork = 0;//夜班工作量

        for(int j=0;j<ringNumsData.size();j++){//遍历推进环数信息，获得每天的白班，夜班推进环数
            JSONObject ringObj = ringNumsData.get(j);
            Timestamp ringDate = ringObj.getTimestamp("ringDate");
            long ringTime = ringDate.getTime();
            if(("month".equals(type) || "season".equals(type) ) && ringTime >= end){
                break;
            }else if("day".equals(type) && ringTime >= (start+ 86400000)){
                break;
            }else if(start <= ringTime && ringTime < end){
                work = ringObj.getInteger("ringNum") - initRingNum;
                if(j<ringNumsData.size()-1){
                    long nextTime =   ringNumsData.get(j+1).getTimestamp("ringDate").getTime();
                    if(nextTime >= end){
                        initRingNum = ringObj.getInteger("ringNum");//更新初始化环号信息，用于计算夜班工作量 = 夜班结束环-当天白班结束环
                    }
                }
                if(j == ringNumsData.size()-1){
                    initRingNum = ringObj.getInteger("ringNum");
                }
                continue;
            }else if(end <= ringTime && ringTime < (start+ 86400000) && "day".equals(type)){
                nightWork = ringObj.getInteger("ringNum") - initRingNum;
                if(j<ringNumsData.size()-1){
                    long nextTime =   ringNumsData.get(j+1).getTimestamp("ringDate").getTime();
                    if(nextTime >= (start+ 86400000)){
                        initRingNum = ringObj.getInteger("ringNum");//更新初始化环号信息，用于计算白班工作量 = 当天白班结束环-昨天夜班结束环
                    }
                }
                if(j == ringNumsData.size()-1){
                    initRingNum = ringObj.getInteger("ringNum");
                }
                continue;
            }
        }
        Map<String,Integer> resultMap = new HashMap<>();
        resultMap.put("work",work);
        resultMap.put("nightWork",nightWork);
        resultMap.put("initRingNum",initRingNum);
        return resultMap;
    }

    /**
     * 获得当前设计环，当天进度，完成进度
     * @param grandTotalWork
     * @param DateStr
     * @param workPlanRows
     * @param oneRow
     * @return
     */
    private Map<String,Object> getDesign(int grandTotalWork,int initRingNum,String DateStr,JSONArray workPlanRows,JSONObject oneRow,String type) throws ParseException {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM");

        Map<String,Object> resultMap = new HashMap<>();
        int currentRowDesign = 0;//每行记录的工作计划量 列
        String currentProgressStr = "";//每行记录的完成进度量 列
        String completionSchedule = "";//每行记录的累计进度量 列

        int ringTotal = oneRow.getInteger("ringTotal");
        if(workPlanRows == null || workPlanRows.size() < 1){//获得当前设计环
            String projectStartTime = oneRow.getString("projectStartTime");
            String projectEndTime = oneRow.getString("projectEndTime");
            int sub = 0;
            long startTime = 0;
            long endTime = 0;

            startTime = dateFormat.parse(projectStartTime).getTime();
            endTime = dateFormat.parse(projectEndTime).getTime();

            if("day".equals(type)){
                sub = DateUtils.getTimeLimitForProject(projectStartTime, projectEndTime);//工期
            }else if("month".equals(type)){
                sub = DateUtils.getMonthSub(startTime, endTime);
            }else if("season".equals(type)){
                sub = DateUtils.getSeasonSub(startTime, endTime);
            }
            currentRowDesign = (ringTotal%sub == 0)?ringTotal/sub : (ringTotal/sub+1);
            double currentSchedule =  ((double)grandTotalWork*100)/currentRowDesign;
            currentProgressStr = decimalFormat.format(currentSchedule);
        }else{
            for(int j=0;j<workPlanRows.size();j++){//遍历工作计划集合，获取当前天的设计环，与当天进度
                JSONObject workPlanObj = workPlanRows.getJSONObject(j);
                String planTime = workPlanObj.getString("planTime");//计划时间节点
                Integer schedule = workPlanObj.getInteger("schedule");//计划工期
                Integer ringNum = workPlanObj.getInteger("ringNum");//计划完成至环数
                if("day".equals(type)){//判断当前日期是否在此计划节点之内
                    long daySub = DateUtils.getDaySub(DateStr, planTime);
                    if(daySub<0){
                        continue;
                    }
                    if(j == 0){
                        currentRowDesign = (ringNum%schedule == 0) ? ringNum/schedule : (ringNum/schedule +1);
                    }else{
                        Integer previousDayRingNum =  workPlanRows.getJSONObject(j-1).getInteger("ringNum");//上一个计划完成至环数
                        Integer zheCiJiHuaHuanShu = ringNum - previousDayRingNum;
                        currentRowDesign = (zheCiJiHuaHuanShu%schedule == 0) ? zheCiJiHuaHuanShu/schedule : (zheCiJiHuaHuanShu/schedule +1);
                    }
                    double currentSchedule =  ((double)grandTotalWork*100)/currentRowDesign;
                    currentProgressStr = decimalFormat.format(currentSchedule);//当天（月，季度）进度
                    break;
                }else if("month".equals(type)){
                    Date dateParse = dateFormat2.parse(DateStr);
                    Date parse1 = dateFormat2.parse(planTime);


                    long sub = DateUtils.getMonthSub(dateParse.getTime(),parse1.getTime());
                    if(sub != 0 ){
                        continue;
                    }
                    Integer previousRingNum = null;//上一个计划完成至环数
                    Integer thisMoth = null;
                    if(j == 0){
                        previousRingNum = workPlanRows.getJSONObject(0).getInteger("ringNum");
                    }else{
                        previousRingNum = workPlanRows.getJSONObject(j-1).getInteger("ringNum");
                    }
                    for(int k=j;k<workPlanRows.size();k++){
                        String nextPlanTime = workPlanRows.getJSONObject(k).getString("planTime");//计划时间节点
                        Date parse2 = dateFormat2.parse(nextPlanTime);
                        long subNext = DateUtils.getMonthSub(dateParse.getTime(),parse2.getTime());
                        if(subNext > 0){
                            thisMoth = workPlanRows.getJSONObject(k-1).getInteger("ringNum");
                            break;
                        }
                    }
                    thisMoth = thisMoth == null?workPlanRows.getJSONObject(workPlanRows.size() -1).getInteger("ringNum") : thisMoth;
                    currentRowDesign = thisMoth - previousRingNum;
                    //schedule = DateUtils.getDayOfMonth(dateParse.getTime());//工期 = 本月天数
                    //currentRowDesign = (zheCiJiHuaHuanShu%schedule == 0) ? zheCiJiHuaHuanShu/schedule : (zheCiJiHuaHuanShu/schedule +1);
                    double currentSchedule =  ((double)grandTotalWork*100)/currentRowDesign;
                    currentProgressStr = decimalFormat.format(currentSchedule);//当天（月，季度）进度
                    break;
                }else if("season".equals(type)){
                    Date parse1 = null;
                    parse1 = dateFormat2.parse(planTime);
                    String planSeasonStr = formatDate(parse1, type);
                    if(planSeasonStr.compareToIgnoreCase(DateStr) != 0){
                        continue;
                    }
                    Integer previousRingNum = null;//上一个计划完成至环数
                    Integer thisSeason = null;//这个季度结束，计划完成至环数
                    if(j == 0){
                        previousRingNum = workPlanRows.getJSONObject(0).getInteger("ringNum");
                    }else{
                        previousRingNum = workPlanRows.getJSONObject(j-1).getInteger("ringNum");
                    }
                    for(int k=j;k<workPlanRows.size();k++){
                        String nextPlanTime = workPlanRows.getJSONObject(k).getString("planTime");//计划时间节点
                        Date parse2 = dateFormat2.parse(nextPlanTime);
                        String nextPlanTimeStr = formatDate(parse2, type);
                        if(nextPlanTimeStr.compareToIgnoreCase(DateStr) > 0){
                            thisSeason = workPlanRows.getJSONObject(k-1).getInteger("ringNum");
                            break;
                        }
                    }
                    thisSeason = thisSeason == null?workPlanRows.getJSONObject(workPlanRows.size() -1).getInteger("ringNum") : thisSeason;
                    currentRowDesign = thisSeason - previousRingNum;
                    double currentSchedule =  ((double)grandTotalWork*100)/currentRowDesign;
                    currentProgressStr = decimalFormat.format(currentSchedule);//当天（月，季度）进度
                    break;
                }


            }
        }
        double t1 = ((initRingNum*1.0)/ringTotal)*100;//完成进度。等于完成环数/总环数
        completionSchedule = decimalFormat.format(t1);
        resultMap.put("currentRowDesign",currentRowDesign);
        resultMap.put("currentProgressStr",currentProgressStr == "" ? "0" : currentProgressStr);
        resultMap.put("completionSchedule",completionSchedule);
        return resultMap;
    }

    /**
     * 获得某一天白班，夜班时间段
     * @param currentDate
     * @return
     */
    private JSONObject getTime(Date currentDate,String dayShiftStart,String dayShiftEnd,String type){
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
            cal.set(i1,i2,i3,dayShiftEndHour,dayShiftEndMinute,00);//白班结束时间，夜班起始时间
            long dayEnd = cal.getTime().getTime();
            cal.set(i1,i2,i3+1,dayShiftStartHour,dayShiftStartMinute,00);//夜班结束时间
            long nightEnd = cal.getTime().getTime();
            returnJson.put("start",dayStart);
            returnJson.put("end",dayEnd);

        }

        return  returnJson;
    }

    private String formatDate(Date date ,String type){
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
