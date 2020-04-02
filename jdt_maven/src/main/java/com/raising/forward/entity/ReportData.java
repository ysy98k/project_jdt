package com.raising.forward.entity;

import com.baosight.common.basic.entity.BaseEntity;

public class ReportData extends BaseEntity {
    private String SheetName; //表名
    private String point; //点号
    private String ringLocation;
    private float initialHeight;
    private float height; //本次高程
    private float changeQuantity; //本次变化
    private float changeRate;//变化速率
    private float cumulativeVariation; //累计变化
    private float formationLossRate; //地表损失
    private String remarks; //备注
    private String levelOfRisk;

    public String getSheetName() {
        return SheetName;
    }

    public void setSheetName(String sheetName) {
        SheetName = sheetName;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getRingLocation() {
        return ringLocation;
    }

    public void setRingLocation(String ringLocation) {
        this.ringLocation = ringLocation;
    }

    public float getInitialHeight() {
        return initialHeight;
    }

    public void setInitialHeight(float initialHeight) {
        this.initialHeight = initialHeight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getChangeQuantity() {
        return changeQuantity;
    }

    public void setChangeQuantity(float changeQuantity) {
        this.changeQuantity = changeQuantity;
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

    public float getFormationLossRate() {
        return formationLossRate;
    }

    public void setFormationLossRate(float formationLossRate) {
        this.formationLossRate = formationLossRate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLevelOfRisk() {
        return levelOfRisk;
    }

    public void setLevelOfRisk(String levelOfRisk) {
        this.levelOfRisk = levelOfRisk;
    }
}
