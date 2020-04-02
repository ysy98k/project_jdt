package com.raising.thread;

import com.alibaba.fastjson.JSONObject;
import com.util.TsdbUtil;

import java.util.concurrent.Callable;

public class GetTsdbTimeRangeData implements Callable<JSONObject> {

    private Integer instanceId;
    private Integer propertyId;
    private Long sTime;
    private Long endTime;
    private Integer interval;
    private Long cursor_mask;

    public GetTsdbTimeRangeData(){

    }
    public GetTsdbTimeRangeData(Integer instanceId,Integer propertyId,int limit,long sTime,long endTime,int interval,long cursor_mask){
        this.instanceId = instanceId;
        this.propertyId = propertyId;
        this.sTime = sTime;
        this.endTime = endTime;
        this.interval = interval;
        this.cursor_mask = cursor_mask;
    }

    @Override
    public JSONObject call() throws Exception {
        JSONObject timeRangeData = TsdbUtil.getTimeRangeData(this.instanceId, this.propertyId, this.sTime, this.endTime, this.interval, this.cursor_mask);
        timeRangeData.put("instanceId",this.instanceId);
        return timeRangeData;
    }
}
