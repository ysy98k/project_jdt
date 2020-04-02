package com.raising.forward.entity;

import com.baosight.common.basic.entity.BaseEntity;

import java.util.UUID;

public class PointAttribute extends BaseEntity {
    private String pointID;
    private String engineeringID;
    private String point;
    private String ringLocation;
    private float initialHeight;
    private float cumulativeVariationControlValue;
    private float changeRateControlValue;
    private String levelOfRisk;

    public String getPointID() {
        return pointID;
    }

    public void setPointID(String pointID) {
        this.pointID = pointID;
    }

    public void setNewPointID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        this.pointID = uuid;
    }

    public String getEngineeringID() {
        return engineeringID;
    }

    public void setEngineeringID(String engineeringID) {
        this.engineeringID = engineeringID;
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

    public String getLevelOfRisk() {
        return levelOfRisk;
    }

    public void setLevelOfRisk(String levelOfRisk) {
        this.levelOfRisk = levelOfRisk;
    }
}
