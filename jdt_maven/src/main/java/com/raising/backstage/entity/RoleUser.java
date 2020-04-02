package com.raising.backstage.entity;

import com.alibaba.fastjson.JSONArray;
import com.baosight.common.basic.entity.BaseEntity;

import java.util.List;

public class RoleUser extends BaseEntity {

    private Integer roleMemberId;
    private Integer userId;
    private Integer groupId;
    private Integer modifyPersonId;

    //sql查询用
    private String username;
    private List<Integer> groupIds;
    private JSONArray roleMembers;

    public Integer getRoleMemberId() {
        return roleMemberId;
    }

    public void setRoleMemberId(Integer roleMemberId) {
        this.roleMemberId = roleMemberId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getModifyPersonId() {
        return modifyPersonId;
    }

    public void setModifyPersonId(Integer modifyPersonId) {
        this.modifyPersonId = modifyPersonId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Integer> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<Integer> groupIds) {
        this.groupIds = groupIds;
    }

    public JSONArray getRoleMembers() {
        return roleMembers;
    }

    public void setRoleMembers(JSONArray roleMembers) {
        this.roleMembers = roleMembers;
    }
}
