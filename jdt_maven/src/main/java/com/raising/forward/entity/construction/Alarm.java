package com.raising.forward.entity.construction;

import com.baosight.common.basic.entity.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * 报警类
 */
public class Alarm extends BaseEntity {

    private Integer alarmId;
    private Date startTime;
    private Date endTime;
    private String alarmDetail;
    private Integer projectId;
    private Integer alarmCodeId;
    private Integer alarmCode;
    private String alarmStr;
    private Integer alarmType;
    private String alarmName;

    private List<Integer> alarmTypesArr;

    public Integer getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Integer alarmId) {
        this.alarmId = alarmId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getAlarmDetail() {
        return alarmDetail;
    }

    public void setAlarmDetail(String alarmDetail) {
        this.alarmDetail = alarmDetail;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getAlarmCodeId() {
        return alarmCodeId;
    }

    public void setAlarmCodeId(Integer alarmCodeId) {
        this.alarmCodeId = alarmCodeId;
    }

    public Integer getAlarmCode() {
        return alarmCode;
    }

    public void setAlarmCode(Integer alarmCode) {
        this.alarmCode = alarmCode;
    }

    public String getAlarmStr() {
        return alarmStr;
    }

    public void setAlarmStr(String alarmStr) {
        this.alarmStr = alarmStr;
    }

    public Integer getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(Integer alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public List<Integer> getAlarmTypesArr() {
        return alarmTypesArr;
    }

    public void setAlarmTypesArr(List<Integer> alarmTypesArr) {
        this.alarmTypesArr = alarmTypesArr;
    }
}
