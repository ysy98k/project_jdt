package com.raising.forward.entity.j;

import com.baosight.common.basic.entity.BaseEntity;

import java.sql.Timestamp;


public class JDisData extends BaseEntity {


    private Integer projectId;

    private Integer  MR_Ring_Num;

    private float MR_Des_A1Mileage;

    private Integer CurMS;

    private Timestamp dt;

    private String collectorName;//select

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getMR_Ring_Num() {
        return MR_Ring_Num;
    }

    public void setMR_Ring_Num(Integer MR_Ring_Num) {
        this.MR_Ring_Num = MR_Ring_Num;
    }

    public float getMR_Des_A1Mileage() {
        return MR_Des_A1Mileage;
    }

    public void setMR_Des_A1Mileage(float MR_Des_A1Mileage) {
        this.MR_Des_A1Mileage = MR_Des_A1Mileage;
    }

    public Integer getCurMS() {
        return CurMS;
    }

    public void setCurMS(Integer curMS) {
        CurMS = curMS;
    }

    public Timestamp getDt() {
        return dt;
    }

    public void setDt(Timestamp dt) {
        this.dt = dt;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName;
    }
}
