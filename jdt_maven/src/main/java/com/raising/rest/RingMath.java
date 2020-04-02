package com.raising.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.xinsight.pds.tsdb.client.PdsClient;
import com.baosight.xinsight.pds.tsdb.client.PdsRecord;
import com.baosight.xinsight.pds.tsdb.client.Tag;
import com.baosight.xinsight.pds.tsdb.client.exceptions.PdsException;
import com.baosight.xinsight.pds.tsdb.client.exceptions.ValueTypeNotMatched;
import com.util.TsdbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.*;

@Component
@Path("/raising")
public class RingMath {

    private static final Logger logger = LoggerFactory.getLogger(RingMath.class);

    //todo:重构，改成http连接

    @POST
    @Path("/ringNum")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject getRaising(JSONObject paramJson){
        String collectionName = paramJson.getString("collectorName");
        Date paramDate = paramJson.getDate("currentTime");
        JSONObject returnInfo = new JSONObject();

        long currentTime = paramDate.getTime();
        long currentZero = currentTime/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset();
        long fourthTime = currentZero - 24*60*60*1000;
        long thirdTime = fourthTime - 24*60*60*1000;
        long secondTime = fourthTime - 24*60*60*1000;
        long firstTime = thirdTime - 24*60*60*1000;

        List<Long> timesList = new ArrayList<>();
        timesList.add(firstTime);
        timesList.add(secondTime);
        timesList.add(thirdTime);
        timesList.add(fourthTime);
        timesList.add(currentZero);
        timesList.add(currentTime);

        Integer instanceId = null;
        Integer propertyId = null;
        JSONArray records = null;

        JSONArray tagArr = new JSONArray();
        tagArr.add(collectionName+"_MR_Ring_Num");

        try {
            JSONObject instancesArr = TsdbUtil.getInstancesArr(tagArr);
            try {
                JSONArray data = instancesArr.getJSONArray("data");
                if (data == null || data.size() < 1) {
                    returnInfo.put("status", Constants.EXECUTE_FAIL);
                    returnInfo.put("message", "没有此tag点");
                    return returnInfo;
                }
                JSONObject instanceTemp = data.getJSONObject(0);
                instanceId = instanceTemp.getInteger("instance_id");
                JSONArray properties = instanceTemp.getJSONArray("properties");
                propertyId = properties.getJSONObject(0).getInteger("id");
            }catch (Exception e){
                returnInfo.put("status", Constants.EXECUTE_FAIL);
                returnInfo.put("message", "没有此tag点");
                return returnInfo;
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("message","出错了，获取tag点错误");
            return returnInfo;
        }
        try {
            JSONObject timeRangeData = TsdbUtil.getTimeRangeData(instanceId, propertyId, firstTime, currentTime, 0, 0);
            records = timeRangeData.getJSONArray("records");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = records.size() - 1;i>0;i--){
            Integer big = records.getJSONObject(i).getInteger("value");
            Integer small = records.getJSONObject(i-1).getInteger("value");
            Integer value = big - small;
            if(!returnInfo.containsKey("five")){
                returnInfo.put("five",value);
            }else if(!returnInfo.containsKey("four")){
                returnInfo.put("four",value);
            }else if(!returnInfo.containsKey("three")){
                returnInfo.put("three",value);
            }else if(!returnInfo.containsKey("two")){
                returnInfo.put("two",value);
            }else if(!returnInfo.containsKey("one")){
                returnInfo.put("one",value);
            }
        }
        if(!returnInfo.containsKey("five")){
            returnInfo.put("five",0);
        }
        if(!returnInfo.containsKey("four")){
            returnInfo.put("four",0);
        }
        if(!returnInfo.containsKey("three")){
            returnInfo.put("three",0);
        }
        if(!returnInfo.containsKey("two")){
            returnInfo.put("two",0);
        }
        if(!returnInfo.containsKey("one")){
            returnInfo.put("one",0);
        }
        if(!returnInfo.containsKey("currentRing")){
            returnInfo.put("currentRing",0);
        }
        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        return returnInfo;
    }
}
