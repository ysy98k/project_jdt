package com.raising.backstage.entity;

import com.baosight.common.basic.entity.BaseEntity;
import com.baosight.common.utils.StringUtils;

import java.util.List;

public class User extends BaseEntity {

    private Integer userId;
    private String username;
    private String password;
    private String displayName;
    private String description;
    private String telephone;
    private String email;
    private String employeeId;
    private Integer tenantId;
    private Integer openId;
    private Integer deleteFlag;

    private String validity;


    //sql查询用
    private List<Integer> userIds;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getOpenId() {
        return openId;
    }

    public void setOpenId(Integer openId) {
        this.openId = openId;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public String getValidity() {
        if(StringUtils.isNullOrEmpty(this.validity)){
             return  "9999-01-01 00:00:00";
        }else{
            return validity;
        }

    }

    public void setValidity(String validity) {
        if(StringUtils.isNullOrEmpty(validity)){
            this.validity = "9999-01-01 00:00:00";
        }else{
            this.validity = validity;
        }
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
