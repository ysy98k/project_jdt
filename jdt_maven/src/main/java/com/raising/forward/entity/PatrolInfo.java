package com.raising.forward.entity;

import java.util.Date;

public class PatrolInfo {
    private String SheetName;
    private String patrolContent;
    private String patrolResult;
    private String remark;
    private int placeMark;

    public PatrolInfo() {

    }

    public String getSheetName() {
        return SheetName;
    }

    public void setSheetName(String sheetName) {
        SheetName = sheetName;
    }

    public String getPatrolContent() {
        return patrolContent;
    }

    public void setPatrolContent(String patrolContent) {
        this.patrolContent = patrolContent;
    }

    public String getPatrolResult() {
        return patrolResult;
    }

    public void setPatrolResult(String patrolResult) {
        this.patrolResult = patrolResult;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getPlaceMark() {
        return placeMark;
    }

    public void setPlaceMark(int placeMark) {
        this.placeMark = placeMark;
    }
}
