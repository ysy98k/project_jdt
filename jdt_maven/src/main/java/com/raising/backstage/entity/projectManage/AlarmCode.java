package com.raising.backstage.entity.projectManage;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.entity.BaseEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 报警变量实体类
 */
public class AlarmCode extends BaseEntity implements Serializable {

    private Integer alarmCodeId;
    private String alarmName;
    private String alarmNameStr;
    private Integer alarmType;
    private Integer alarmCode;
    private Integer alarmLevel;

    private List<Integer> alarmTypesArr;
    private List<JSONObject> alarmCodeArr;


    public Integer getAlarmCodeId() {
        return alarmCodeId;
    }

    public void setAlarmCodeId(Integer alarmCodeId) {
        this.alarmCodeId = alarmCodeId;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getAlarmNameStr() {
        return alarmNameStr;
    }

    public void setAlarmNameStr(String alarmNameStr) {
        this.alarmNameStr = alarmNameStr;
    }

    public Integer getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(Integer alarmType) {
        this.alarmType = alarmType;
    }

    public Integer getAlarmCode() {
        return alarmCode;
    }

    public void setAlarmCode(Integer alarmCode) {
        this.alarmCode = alarmCode;
    }

    public Integer getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(Integer alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public List<Integer> getAlarmTypesArr() {
        return alarmTypesArr;
    }

    public void setAlarmTypesArr(List<Integer> alarmTypesArr) {
        this.alarmTypesArr = alarmTypesArr;
    }

    public List<JSONObject> getAlarmCodeArr() {
        return alarmCodeArr;
    }

    public void setAlarmCodeArr(List<JSONObject> alarmCodeArr) {
        this.alarmCodeArr = alarmCodeArr;
    }
}
