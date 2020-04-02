package com.raising.backstage.entity;

import com.baosight.common.basic.entity.BaseEntity;


public class Role extends BaseEntity {

    private Integer groupId;
    private String groupName;
    private String displayName;
    private String groupDescription;
    private Integer parentId;

    private Integer modifyPersonId;
    private Integer deleteFlag;


    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getModifyPersonId() {
        return modifyPersonId;
    }

    public void setModifyPersonId(Integer modifyPersonId) {
        this.modifyPersonId = modifyPersonId;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
