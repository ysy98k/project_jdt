package com.raising.forward.service.ProgressManage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

@Service(value ="progressAnalysisService" )
public class ProgressAnalysisService extends NewBaseService {

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
        String type = paramInfo.getString("type");//查询数据的类型，日，月，季度
        if(StringUtils.isNullOrEmpty(startTime)){
            Timestamp compareTime = getCompareTime(paramInfo,type);
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
        //根据type(日月季，指定时间查询)。选择不同的查询起始时间。（当指定时间查询时指定终止时间）
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
        List<JSONObject> returnList =  operatorResult(ringNumsData,type,paramInfo.getInteger("curRowNum"));//处理数据库的查询结果。处理成前台显示
        int curPage = paramInfo.getInteger("curPage");
        int curRowNum = paramInfo.getInteger("curRowNum");
        int totalPage = (returnList.size()%curRowNum ==0)?returnList.size()/curRowNum:(returnList.size()/curRowNum+1);//总页数
        int index = (curPage -1)*curRowNum;
        int toIndex =  returnList.size() >= curRowNum*curPage ? curRowNum*curPage : returnList.size();
        List<JSONObject> rows = returnList.subList(index, toIndex);
        rows = getTotal(rows);

        returnInfo.put("total",totalPage);
        returnInfo.put("page",paramInfo.getInteger("curPage"));
        returnInfo.put("records",returnList.size());
        returnInfo.put("rows",rows);
        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        returnInfo.put("returnMsg","查询成功！本次返回"+rows.size()+"条记录，总共"+returnList.size()+"条记录！");
        return returnInfo;
    }

    /***
     * 根据查询的数据，做处理。
     * @param ringNumsData
     * @param type
     * @return
     */
    private List<JSONObject> operatorResult(List<JSONObject> ringNumsData,String type,int curRowNum){
        List<JSONObject> rowsList = getRowsList(ringNumsData, type);//获得前台显示的每行记录
        if(rowsList.size() == curRowNum){
            rowsList.remove(rowsList.size()-1);
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        int initRingNum = ringNumsData.get(0).getInteger("ringNum");//初始环号。
        for(int i=0;i<rowsList.size();i++){//填充前台显示表中每行记录，每一个元素对应一行记录
            JSONObject oneRow = rowsList.get(i);

            Long start = oneRow.getLong("start");//当前记录的起始时间
            Long end = oneRow.getLong("end");//当前记录的结束时间

            int work = 0;
            double tunnellingDate  = 0;//掘进时长
            double assembleDate = 0;//拼装时长
            double shutDownDate = 0;//停机时间
            double drivingTimePerRing = 0.0;//每环掘进时长
            double installationTimePerRing = 0.0;//每环拼装时长
            double outageTimePerLoop = 0.0;//每环停机时长

            Map<String, Object> oneRowWork = getOneRowWork(initRingNum,start, end, ringNumsData);
            work = (Integer) oneRowWork.get("work");
            tunnellingDate = (double)oneRowWork.get("tunnellingDate");
            assembleDate = (double)oneRowWork.get("assembleDate");
            shutDownDate = (double)oneRowWork.get("shutDownDate");
            initRingNum = (int)oneRowWork.get("initRingNum");

            drivingTimePerRing = work == 0 ? 0 : tunnellingDate/work ;
            installationTimePerRing = work == 0 ? 0 : assembleDate/work ;
            outageTimePerLoop = work == 0 ? 0 : shutDownDate/work ;

            double totalTime = tunnellingDate+assembleDate+shutDownDate;
            int tunnelProportion = (int)Math.round(tunnellingDate*10/totalTime);
            int assembleProportion = (int)Math.round(assembleDate*10/totalTime);
            String diagram = tunnelProportion+":"+assembleProportion+":"+(10-tunnelProportion-assembleProportion);

            oneRow.put("date",oneRow.getString("date"));//当天累计
            oneRow.put("work",work);//当天设计
            oneRow.put("tunnellingDate",decimalFormat.format(tunnellingDate));//当天进度
            oneRow.put("assembleDate",decimalFormat.format(assembleDate) );//累计
            oneRow.put("shutDownDate",decimalFormat.format(shutDownDate));//完成进度
            oneRow.put("drivingTimePerRing",decimalFormat.format(drivingTimePerRing));//每环掘进时间(h)
            oneRow.put("installationTimePerRing",decimalFormat.format(installationTimePerRing));//每环拼装时间(h)
            oneRow.put("outageTimePerLoop",decimalFormat.format(outageTimePerLoop));//每环停机时间(h)
            oneRow.put("diagram",diagram);
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
                    long dayStart = timeJson.getLongValue("start");//当天白班起始时间
                    long dayEnd = dayStart+86400000;//当天夜班结束时间
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
    private Map<String,Object> getOneRowWork(int initRingNum, Long start, Long end, List<JSONObject> ringNumsData){
        int work = 0;//当天工作量
        double tunnellingDate = 0;//掘进时间
        double assembleDate = 0;//拼装时间
        double shutDownDate = 0;//停机时间

        for(int j=0;j<ringNumsData.size();j++){//遍历推进环数信息，获得每天的白班，夜班推进环数
            JSONObject ringObj = ringNumsData.get(j);
            Timestamp ringDate = ringObj.getTimestamp("ringDate");
            long ringTime = ringDate.getTime();
            if(ringTime >= end){
                break;
            }else if(start <= ringTime && ringTime < end){
                work = ringObj.getInteger("ringNum") - initRingNum;
                assembleDate +=  ringObj.getDoubleValue("assemblingTime")/3600;
                tunnellingDate +=  ringObj.getDoubleValue("tunnellingTime")/3600;//junjin时长
                double shutDown = (ringObj.getDoubleValue("ringTotalTime")/3600 - ringObj.getDoubleValue("tunnellingTime")/3600) > 0?
                        (ringObj.getDoubleValue("ringTotalTime")/3600 - ringObj.getDoubleValue("tunnellingTime")/3600) : 0;
                shutDownDate +=  shutDown;
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
            }
        }
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("work",work);
        resultMap.put("initRingNum",initRingNum);
        resultMap.put("tunnellingDate",tunnellingDate);
        resultMap.put("assembleDate",assembleDate);
        resultMap.put("shutDownDate",shutDownDate);
        return resultMap;
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
            cal.set(i1,i2,i3,dayShiftEndHour,dayShiftEndMinute,00);//白班结束时间，夜班起始时间
            long dayEnd = cal.getTime().getTime();
            cal.set(i1,i2,i3+1,dayShiftStartHour,dayShiftStartMinute,00);//夜班结束时间
            long nightEnd = cal.getTime().getTime();
            returnJson.put("start",dayStart);
            returnJson.put("end",dayEnd);

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

    private List<JSONObject> getTotal(List<JSONObject> rows){
        JSONObject totalRow = new JSONObject();
        DecimalFormat format = new DecimalFormat("0.00");
        int ringNum = 0;
        double totalTunnellingDate  = 0;//掘进时长
        double totalAssembleDate = 0;//拼装时长
        double totalShutDownDate = 0;//停机时间
        double totalDrivingTimePerRing = 0.0;//每环掘进时长
        double totalInstallationTimePerRing = 0.0;//每环拼装时长
        double totalOutageTimePerLoop = 0.0;//每环停机时长
        for(int i=0;i<rows.size();i++){
            JSONObject temp = rows.get(i);
            ringNum += temp.getIntValue("work");
            totalTunnellingDate += temp.getDoubleValue("tunnellingDate");
            totalAssembleDate += temp.getDoubleValue("assembleDate");
            totalShutDownDate += temp.getDoubleValue("shutDownDate");
            totalDrivingTimePerRing += temp.getDoubleValue("drivingTimePerRing");
            totalInstallationTimePerRing += temp.getDoubleValue("installationTimePerRing");
            totalOutageTimePerLoop += temp.getDoubleValue("outageTimePerLoop");
        }
        double totalTime = totalTunnellingDate+totalAssembleDate+totalShutDownDate;
        int tunnelProportion = (int)Math.round(totalTunnellingDate*10/totalTime);
        int assembleProportion = (int)Math.round(totalAssembleDate*10/totalTime);
        String diagram = tunnelProportion+":"+assembleProportion+":"+(10-tunnelProportion-assembleProportion);

        totalRow.put("date","累计");//当天累计
        totalRow.put("work",ringNum);//当天设计
        totalRow.put("tunnellingDate",format.format(totalTunnellingDate));
        totalRow.put("assembleDate",format.format(totalAssembleDate) );
        totalRow.put("shutDownDate",format.format(totalShutDownDate));
        totalRow.put("drivingTimePerRing",format.format(totalDrivingTimePerRing/rows.size()));//每环掘进时间(h)
        totalRow.put("installationTimePerRing",format.format(totalInstallationTimePerRing/rows.size()  ));//每环拼装时间(h)
        totalRow.put("outageTimePerLoop",format.format(totalOutageTimePerLoop/rows.size()  ));//每环停机时间(h)
        totalRow.put("diagram",diagram);//每环停机时间(h)
        rows.add(totalRow);
        return rows;
    }




}
