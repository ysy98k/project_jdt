package com.raising.forward.entity;

import com.baosight.common.basic.entity.BaseEntity;

import java.util.Date;
import java.util.UUID;

public class ReportRelation extends BaseEntity {
    private String engineeringUUID;
    private Date reportDate;
    private String reportTime;
    private String resultTableUUID;

    public String getEngineeringUUID() {
        return engineeringUUID;
    }

    public void setEngineeringUUID(String engineeringUUID) {
        this.engineeringUUID = engineeringUUID;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getResultTableUUID() {
        return resultTableUUID;
    }

    public void setResultTableUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        this.resultTableUUID = uuid;
    }
}
