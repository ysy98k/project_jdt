package com.raising.forward.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baosight.common.basic.entity.BaseEntity;
import com.raising.backstage.entity.SectionManage;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 项目信息表
 */
public class ProjectInfo extends BaseEntity implements Serializable {

    private Integer projectId;

    private String projectName;

    private String buildUnit;

    private String supervisor;

    private Float totalLength;

    private String ringTotal;

    private Float startMileage;

    private Float endMileage;

    private String tunnelDrection;

    private Integer tbmId;

    private String collectorName;

    private String projectSituation;

    private String projectLocation;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date  endTime;

    private String status;

    private String geologyInfo;

    private Integer sectionId;

    private String templateName;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String dayShiftStart;

    private String dayShiftEnd;

    //权限过滤。其权限下的采集器集合
    private List<String> collectorNameList;

    //同section关联的字段
    private String sectionName;

    private String sectionOwner;

    private String sectionCcsId;

    private String ccsSectionType;

    //
    private String factory;

    private String tbmOwner;

    private String rmsVersion;

    private String tbmName;

    private String tbmCcsType;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getBuildUnit() {
        return buildUnit;
    }

    public void setBuildUnit(String buildUnit) {
        this.buildUnit = buildUnit;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public Float getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(Float totalLength) {
        this.totalLength = totalLength;
    }

    public String getRingTotal() {
        return ringTotal;
    }

    public void setRingTotal(String ringTotal) {
        this.ringTotal = ringTotal;
    }

    public Float getStartMileage() {
        return startMileage;
    }

    public void setStartMileage(Float startMileage) {
        this.startMileage = startMileage;
    }

    public Float getEndMileage() {
        return endMileage;
    }

    public void setEndMileage(Float endMileage) {
        this.endMileage = endMileage;
    }

    public String getTunnelDrection() {
        return tunnelDrection;
    }

    public void setTunnelDrection(String tunnelDrection) {
        this.tunnelDrection = tunnelDrection;
    }

    public Integer getTbmId() {
        return tbmId;
    }

    public void setTbmId(Integer tbmId) {
        this.tbmId = tbmId;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName;
    }

    public String getProjectSituation() {
        return projectSituation;
    }

    public void setProjectSituation(String projectSituation) {
        this.projectSituation = projectSituation;
    }

    public String getProjectLocation() {
        return projectLocation;
    }

    public void setProjectLocation(String projectLocation) {
        this.projectLocation = projectLocation;
    }

    public Date getStartTime() {
        if(this.startTime != null){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = df.format(this.startTime);
            try {
                return df.parse(format);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        if(this.endTime != null){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = df.format(this.endTime);
            try {
                return df.parse(format);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGeologyInfo() {
        return geologyInfo;
    }

    public void setGeologyInfo(String geologyInfo) {
        this.geologyInfo = geologyInfo;
    }

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Date getCreateTime() {
        if(this.createTime != null){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = df.format(this.createTime);
            try {
                return df.parse(format);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        if(this.updateTime != null){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = df.format(this.updateTime);
            try {
                return df.parse(format);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<String> getCollectorNameList() {
        return collectorNameList;
    }

    public void setCollectorNameList(List<String> collectorNameList) {
        this.collectorNameList = collectorNameList;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionOwner() {
        return sectionOwner;
    }

    public void setSectionOwner(String sectionOwner) {
        this.sectionOwner = sectionOwner;
    }

    public String getSectionCcsId() {
        return sectionCcsId;
    }

    public void setSectionCcsId(String sectionCcsId) {
        this.sectionCcsId = sectionCcsId;
    }

    public String getCcsSectionType() {
        return ccsSectionType;
    }

    public void setCcsSectionType(String ccsSectionType) {
        this.ccsSectionType = ccsSectionType;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getTbmOwner() {
        return tbmOwner;
    }

    public void setTbmOwner(String tbmOwner) {
        this.tbmOwner = tbmOwner;
    }

    public String getRmsVersion() {
        return rmsVersion;
    }

    public void setRmsVersion(String rmsVersion) {
        this.rmsVersion = rmsVersion;
    }

    public String getTbmName() {
        return tbmName;
    }

    public void setTbmName(String tbmName) {
        this.tbmName = tbmName;
    }

    public String getTbmCcsType() {
        return tbmCcsType;
    }

    public void setTbmCcsType(String tbmCcsType) {
        this.tbmCcsType = tbmCcsType;
    }

    public String getDayShiftStart() {
        return dayShiftStart;
    }

    public void setDayShiftStart(String dayShiftStart) {
        this.dayShiftStart = dayShiftStart;
    }

    public String getDayShiftEnd() {
        return dayShiftEnd;
    }

    public void setDayShiftEnd(String dayShiftEnd) {
        this.dayShiftEnd = dayShiftEnd;
    }
}
