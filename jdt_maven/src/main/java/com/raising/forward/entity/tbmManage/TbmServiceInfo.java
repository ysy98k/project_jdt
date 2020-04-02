package com.raising.forward.entity.tbmManage;

import com.baosight.common.basic.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class TbmServiceInfo extends BaseEntity {

    private Integer id;

    private Integer tbmId;

    private String brokerage;//经手人

    private String brokeragePhone;

    private String contacts;//联系人

    private String contactsPhone;

    private String type;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createTime;

    private String remarks;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTbmId() {
        return tbmId;
    }

    public void setTbmId(Integer tbmId) {
        this.tbmId = tbmId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
