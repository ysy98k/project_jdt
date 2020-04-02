package com.raising.forward.entity;

import com.baosight.common.basic.entity.BaseEntity;

import java.sql.Timestamp;

/**
 * @author ysy
 * @date 2018/3/26 10:52
 * @description 零位配置
 */
public class ZeroConfig extends BaseEntity {
    private Integer id;
    private float tbm_A;
    private float tbm_B;
    private float tbm_C;
    private float tbm_D;
    private float tbm_E;
    private float tbm_F;
    private float tbm_G;
    private float zero_LT_X;
    private float Zero_LT_Y;
    private float zero_LT_Z;
    private float zero_LT_RollAngle;
    private float zero_LT_PitchAngle;
    private float zero_LT_AzimuthAngle;
    private Integer sectionId;
    private java.sql.Timestamp updateTime;
    private String dbValue;
    private String dbName;
    private Integer clientId;
    private Integer projectId;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getTbm_A() {
        return tbm_A;
    }

    public void setTbm_A(float tbm_A) {
        this.tbm_A = tbm_A;
    }

    public float getTbm_B() {
        return tbm_B;
    }

    public void setTbm_B(float tbm_B) {
        this.tbm_B = tbm_B;
    }

    public float getTbm_C() {
        return tbm_C;
    }

    public void setTbm_C(float tbm_C) {
        this.tbm_C = tbm_C;
    }

    public float getTbm_D() {
        return tbm_D;
    }

    public void setTbm_D(float tbm_D) {
        this.tbm_D = tbm_D;
    }

    public float getTbm_E() {
        return tbm_E;
    }

    public void setTbm_E(float tbm_E) {
        this.tbm_E = tbm_E;
    }

    public float getTbm_F() {
        return tbm_F;
    }

    public void setTbm_F(float tbm_F) {
        this.tbm_F = tbm_F;
    }

    public float getTbm_G() {
        return tbm_G;
    }

    public void setTbm_G(float tbm_G) {
        this.tbm_G = tbm_G;
    }

    public float getZero_LT_X() {
        return zero_LT_X;
    }

    public void setZero_LT_X(float zero_LT_X) {
        this.zero_LT_X = zero_LT_X;
    }

    public float getZero_LT_Y() {
        return Zero_LT_Y;
    }

    public void setZero_LT_Y(float zero_LT_Y) {
        Zero_LT_Y = zero_LT_Y;
    }

    public float getZero_LT_Z() {
        return zero_LT_Z;
    }

    public void setZero_LT_Z(float zero_LT_Z) {
        this.zero_LT_Z = zero_LT_Z;
    }

    public float getZero_LT_RollAngle() {
        return zero_LT_RollAngle;
    }

    public void setZero_LT_RollAngle(float zero_LT_RollAngle) {
        this.zero_LT_RollAngle = zero_LT_RollAngle;
    }

    public float getZero_LT_PitchAngle() {
        return zero_LT_PitchAngle;
    }

    public void setZero_LT_PitchAngle(float zero_LT_PitchAngle) {
        this.zero_LT_PitchAngle = zero_LT_PitchAngle;
    }

    public float getZero_LT_AzimuthAngle() {
        return zero_LT_AzimuthAngle;
    }

    public void setZero_LT_AzimuthAngle(float zero_LT_AzimuthAngle) {
        this.zero_LT_AzimuthAngle = zero_LT_AzimuthAngle;
    }

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getDbValue() {
        return dbValue;
    }

    public void setDbValue(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
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
