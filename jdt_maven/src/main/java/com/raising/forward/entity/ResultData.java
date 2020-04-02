package com.raising.forward.entity;

import com.baosight.common.basic.entity.BaseEntity;

public class ResultData extends BaseEntity {
    private String sheetName;
    private String maxCQPoint;
    private float maxCQdata;
    private String maxCVPoint;
    private float maxCVdata;
    private String maxFLPoint;
    private float maxFLdata;
    private float cumulativeVariationControlValue;
    private float changeRateControlValue;

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getMaxCQPoint() {
        return maxCQPoint;
    }

    public void setMaxCQPoint(String maxCQPoint) {
        this.maxCQPoint = maxCQPoint;
    }

    public float getMaxCQdata() {
        return maxCQdata;
    }

    public void setMaxCQdata(float maxCQdata) {
        this.maxCQdata = maxCQdata;
    }

    public String getMaxCVPoint() {
        return maxCVPoint;
    }

    public void setMaxCVPoint(String maxCVPoint) {
        this.maxCVPoint = maxCVPoint;
    }

    public float getMaxCVdata() {
        return maxCVdata;
    }

    public void setMaxCVdata(float maxCVdata) {
        this.maxCVdata = maxCVdata;
    }

    public String getMaxFLPoint() {
        return maxFLPoint;
    }

    public void setMaxFLPoint(String maxFLPoint) {
        this.maxFLPoint = maxFLPoint;
    }

    public float getMaxFLdata() {
        return maxFLdata;
    }

    public void setMaxFLdata(float maxFLdata) {
        this.maxFLdata = maxFLdata;
    }

    public float getCumulativeVariationControlValue() {
        return cumulativeVariationControlValue;
    }

    public void setCumulativeVariationControlValue(float cumulativeVariationControlValue) {
        this.cumulativeVariationControlValue = cumulativeVariationControlValue;
    }

    public float getChangeRateControlValue() {
        return changeRateControlValue;
    }

    public void setChangeRateControlValue(float changeRateControlValue) {
        this.changeRateControlValue = changeRateControlValue;
    }
}
