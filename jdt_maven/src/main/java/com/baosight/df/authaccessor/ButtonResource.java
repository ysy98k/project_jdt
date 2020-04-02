package com.baosight.df.authaccessor;

import com.alibaba.fastjson.JSONObject;

/**
 * @author hujinhua
 *         Histroy:
 *         2016/12/26 hujinhua Create
 */
public class ButtonResource {

    private int pageId;

    private int buttonId;

    private String pageName;

    private String buttonName;

    private String description;

    private String split = "_";

    public ButtonResource() {

    }

    public ButtonResource(JSONObject object) {
        setPageId(object.getInteger("pageId"));
        setPageName(object.getString("pageEname"));
        setButtonId(object.getInteger("buttonId"));
        setButtonName(object.getString("buttonName"));
        if (object.getString("buttonDisplayname") != null)
            setDescription(object.getString("buttonDisplayname"));
        else
            setDescription("");
    }

    public int getButtonId() {
        return buttonId;
    }

    public void setButtonId(int buttonId) {
        this.buttonId = buttonId;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    public String toResourceName() {
//        String str = pageId + "_" + buttonId + "_" + pageName + "_" + buttonName;
//        return "button" + split + pageId + split + buttonId;
        return "button" + split + pageName + split + buttonName;
    }

    public String toResourceDisplayName() {
//        return "button" + split + pageName + split + buttonName;
        return toResourceName();
    }

    public String toResourceDescription() {
//        return toResourceName() + split + toResourceDisplayName() + split + description;
        return toResourceName() + split + description;
    }

}
