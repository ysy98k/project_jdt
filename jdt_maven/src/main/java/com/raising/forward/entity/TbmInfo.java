package com.raising.forward.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baosight.common.basic.entity.BaseEntity;

import java.util.Date;

/**
 * 盾构机信息表
 */
public class TbmInfo extends BaseEntity {

    private Integer tbmId;

    private String tbmName;

    private String factory;

    private String ccsType;

    private String diameter;

    private String hingeType;//铰接类型

    private String contacts;

    private String phone;

    private String rmsVersion;

    private String owner;

    private Date produceTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String projectStatus;

    private String tbmStatus;

    private String tbmNameStrict;



    public Integer getTbmId() {
        return tbmId;
    }

    public void setTbmId(Integer tbmId) {
        this.tbmId = tbmId;
    }

    public String getTbmName() {
        return tbmName;
    }

    public void setTbmName(String tbmName) {
        this.tbmName = tbmName;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getCcsType() {
        return ccsType;
    }

    public void setCcsType(String ccsType) {
        this.ccsType = ccsType;
    }

    public String getDiameter() {
        return diameter;
    }

    public void setDiameter(String diameter) {
        this.diameter = diameter;
    }

    public String getHingeType() {
        return hingeType;
    }

    public void setHingeType(String hingeType) {
        this.hingeType = hingeType;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRmsVersion() {
        return rmsVersion;
    }

    public void setRmsVersion(String rmsVersion) {
        this.rmsVersion = rmsVersion;
    }

    public Date getProduceTime() {
        return produceTime;
    }

    public void setProduceTime(Date produceTime) {
        this.produceTime = produceTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getTbmStatus() {
        return tbmStatus;
    }

    public void setTbmStatus(String tbmStatus) {
        this.tbmStatus = tbmStatus;
    }

    public String getTbmNameStrict() {
        return tbmNameStrict;
    }

    public void setTbmNameStrict(String tbmNameStrict) {
        this.tbmNameStrict = tbmNameStrict;
    }
}
