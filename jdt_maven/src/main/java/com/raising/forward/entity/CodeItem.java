package com.raising.forward.entity;

import java.io.Serializable;

/**
 * 字典类，对应aasccs库中字典表。
 */
public class CodeItem implements Serializable {

    private Integer fdItemId;

    private String fdItemCode;

    private String fdItemName;

    private String fdItemDesc;

    private String fdLevelCode;

    private String fdLevelNum;

    private String fdParentId;

    private String fdSystemId;

    private String fdTenantId;

    public Integer getFdItemId() {
        return fdItemId;
    }

    public void setFdItemId(Integer fdItemId) {
        this.fdItemId = fdItemId;
    }

    public String getFdItemCode() {
        return fdItemCode;
    }

    public void setFdItemCode(String fdItemCode) {
        this.fdItemCode = fdItemCode;
    }

    public String getFdItemName() {
        return fdItemName;
    }

    public void setFdItemName(String fdItemName) {
        this.fdItemName = fdItemName;
    }

    public String getFdItemDesc() {
        return fdItemDesc;
    }

    public void setFdItemDesc(String fdItemDesc) {
        this.fdItemDesc = fdItemDesc;
    }

    public String getFdLevelCode() {
        return fdLevelCode;
    }

    public void setFdLevelCode(String fdLevelCode) {
        this.fdLevelCode = fdLevelCode;
    }

    public String getFdLevelNum() {
        return fdLevelNum;
    }

    public void setFdLevelNum(String fdLevelNum) {
        this.fdLevelNum = fdLevelNum;
    }

    public String getFdParentId() {
        return fdParentId;
    }

    public void setFdParentId(String fdParentId) {
        this.fdParentId = fdParentId;
    }

    public String getFdSystemId() {
        return fdSystemId;
    }

    public void setFdSystemId(String fdSystemId) {
        this.fdSystemId = fdSystemId;
    }

    public String getFdTenantId() {
        return fdTenantId;
    }

    public void setFdTenantId(String fdTenantId) {
        this.fdTenantId = fdTenantId;
    }
}
