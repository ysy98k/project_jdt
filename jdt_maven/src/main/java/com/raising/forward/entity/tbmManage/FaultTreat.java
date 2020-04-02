package com.raising.forward.entity.tbmManage;

import com.baosight.common.basic.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class FaultTreat extends BaseEntity {

    private Integer faultId;

    private Integer tbmId;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createTime;

    private String productName;

    private String rseason;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date warrantyTime;//质保到期时间

    private String brokerage;//经手人

    private String brokeragePhone;

    private String contacts;//联系人

    private String contactsPhone;

    private String processMode;

    private String place;

    public Integer getFaultId() {
        return faultId;
    }

    public void setFaultId(Integer faultId) {
        this.faultId = faultId;
    }

    public Integer getTbmId() {
        return tbmId;
    }

    public void setTbmId(Integer tbmId) {
        this.tbmId = tbmId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRseason() {
        return rseason;
    }

    public void setRseason(String rseason) {
        this.rseason = rseason;
    }

    public Date getWarrantyTime() {
        return warrantyTime;
    }

    public void setWarrantyTime(Date warrantyTime) {
        this.warrantyTime = warrantyTime;
    }

    public String getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(String brokerage) {
        this.brokerage = brokerage;
    }

    public String getBrokeragePhone() {
        return brokeragePhone;
    }

    public void setBrokeragePhone(String brokeragePhone) {
        this.brokeragePhone = brokeragePhone;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }

    public String getProcessMode() {
        return processMode;
    }

    public void setProcessMode(String processMode) {
        this.processMode = processMode;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
