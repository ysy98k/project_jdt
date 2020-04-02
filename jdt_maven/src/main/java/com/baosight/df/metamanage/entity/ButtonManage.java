package com.baosight.df.metamanage.entity;

import com.baosight.common.basic.entity.BaseEntity;

public class ButtonManage extends BaseEntity {
    private Integer buttonId;

    private String buttonName;

    private String buttonDisplayname;

    private Integer pageId;

    private String pageEname;

    private String pageCname;

    private String pagePath;

    private String pageName;

    public Integer getButtonId() {
        return buttonId;
    }

    public void setButtonId(Integer buttonId) {
        this.buttonId = buttonId;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName == null ? null : buttonName.trim();
    }

    public String getButtonDisplayname() {
        return buttonDisplayname;
    }

    public void setButtonDisplayname(String buttonDisplayname) {
        this.buttonDisplayname = buttonDisplayname == null ? null : buttonDisplayname.trim();
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public String getPageCname() {
        return pageCname;
    }

    public void setPageCname(String pageCname) {
        this.pageCname = pageCname;
    }

    public String getPageEname() {
        return pageEname;
    }

    public void setPageEname(String pageEname) {
        this.pageEname = pageEname;
    }

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
}