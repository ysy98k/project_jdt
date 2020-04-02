package com.raising.forward.entity;

import com.baosight.common.basic.entity.BaseEntity;

/**
 * @author ysy
 * @date 2018/2/7 11:28
 * @description
 */
public class DesignLine extends BaseEntity {
    private Integer id;
    private float x;
    private float y;
    private float z;
    private Integer cookieId;
    private float designmileage;
    private float amuzith;
    private float slope;
    private float dist;
    private float mapmileage;
    private Integer clientId;
    private Integer projectId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public Integer getCookieId() {
        return cookieId;
    }

    public void setCookieId(Integer cookieId) {
        this.cookieId = cookieId;
    }

    public float getDesignmileage() {
        return designmileage;
    }

    public void setDesignmileage(float designmileage) {
        this.designmileage = designmileage;
    }

    public float getAmuzith() {
        return amuzith;
    }

    public void setAmuzith(float amuzith) {
        this.amuzith = amuzith;
    }

    public float getSlope() {
        return slope;
    }

    public void setSlope(float slope) {
        this.slope = slope;
    }

    public float getDist() {
        return dist;
    }

    public void setDist(float dist) {
        this.dist = dist;
    }

    public float getMapmileage() {
        return mapmileage;
    }

    public void setMapmileage(float mapmileage) {
        this.mapmileage = mapmileage;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}
