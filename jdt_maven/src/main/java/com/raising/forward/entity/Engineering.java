package com.raising.forward.entity;

import com.baosight.common.basic.entity.BaseEntity;

import java.util.UUID;

public class Engineering extends BaseEntity {
    private String engineeringUUID;  // 工程UUID

    private int projectID;

    private String engineeringName;  // 工程名称

    private String project;  // 区间名称

    private String constructionCompany; // 施工单位

    private String supervisionCompany;  // 监理单位

    private String monitoringCompany; // 监测单位

    private String instrumentModel;  // 仪器型号

    public Engineering() {

    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getMonitoringCompany() {
        return monitoringCompany;
    }

    public void setMonitoringCompany(String monitoringCompany) {
        this.monitoringCompany = monitoringCompany;
    }


    public String getEngineeringUUID() {
        return engineeringUUID;
    }

    public void setOldEngineeringUUID(String uuid) {
        this.engineeringUUID = uuid;
    }

    public void setEngineeringUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        this.engineeringUUID = uuid;
    }

    public String getEngineeringName() {
        return engineeringName;
    }

    public void setEngineeringName(String engineeringName) {
        this.engineeringName = engineeringName;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getConstructionCompany() {
        return constructionCompany;
    }

    public void setConstructionCompany(String constructionCompany) {
        this.constructionCompany = constructionCompany;
    }

    public String getSupervisionCompany() {
        return supervisionCompany;
    }

    public void setSupervisionCompany(String supervisionCompany) {
        this.supervisionCompany = supervisionCompany;
    }

    public String getInstrumentModel() {
        return instrumentModel;
    }

    public void setInstrumentModel(String instrumentModel) {
        this.instrumentModel = instrumentModel;
    }
}
