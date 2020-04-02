package com.raising.forward.entity;

import com.baosight.common.basic.entity.BaseEntity;


public class CoordinateInfo extends BaseEntity {

    private Integer coordinateInfoId;

    private Integer lineId;

    private Float x;

    private Float y;

    private Float z;

    private Float designmileage;

    public Integer getCoordinateInfoId() {
        return coordinateInfoId;
    }

    public void setCoordinateInfoId(Integer coordinateInfoId) {
        this.coordinateInfoId = coordinateInfoId;
    }

    public Integer getLineId() {
        return lineId;
    }

    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getZ() {
        return z;
    }

    public void setZ(Float z) {
        this.z = z;
    }

    public Float getDesignmileage() {
        return designmileage;
    }

    public void setDesignmileage(Float designmileage) {
        this.designmileage = designmileage;
    }
}
