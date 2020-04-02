package com.raising.forward.entity;

import java.util.Date;

public class ReportLineData {
    private String point;
    private Date reportDate;
    private float changeRate;
    private float cumulativeVariation;

    public ReportLineData() {
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public float getChangeRate() {
        return changeRate;
    }

    public void setChangeRate(float changeRate) {
        this.changeRate = changeRate;
    }

    public float getCumulativeVariation() {
        return cumulativeVariation;
    }

    public void setCumulativeVariation(float cumulativeVariation) {
        this.cumulativeVariation = cumulativeVariation;
    }
}
