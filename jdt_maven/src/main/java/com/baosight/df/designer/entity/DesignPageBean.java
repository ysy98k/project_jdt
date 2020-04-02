package com.baosight.df.designer.entity;

import com.baosight.common.basic.entity.BaseEntityBean;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DesignPageBean extends BaseEntityBean {
    private String htmlstring;
    private String jsstring;
    private String cssstring;
    private String author;
    private String pagename;

    private String page_cname;
    private String page_desc;
    private String delivery_pkg;

    private String importJs = "";

    public DesignPageBean() {

    }

    public DesignPageBean(String pagename) {
        this.pagename = pagename;
    }

    public String getHtmlstring() {
        return htmlstring;
    }

    public void setHtmlstring(String htmlstring) {
        this.htmlstring = htmlstring;
    }

    public String getJsstring() {
        return jsstring;
    }

    public void setJsstring(String jsstring) {
        this.jsstring = jsstring;
    }

    public String getPagename() {
        return pagename;
    }

    public void setPagename(String pagename) {
        this.pagename = pagename;
    }

    public String getCssstring() {
        return cssstring;
    }

    public void setCssstring(String cssstring) {
        this.cssstring = cssstring;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPage_cname() {
        return page_cname;
    }

    public void setPage_cname(String page_cname) {
        this.page_cname = page_cname;
    }

    public String getPage_desc() {
        return page_desc;
    }

    public void setPage_desc(String page_desc) {
        this.page_desc = page_desc;
    }

    public String getDelivery_pkg() {
        return delivery_pkg;
    }

    public void setDelivery_pkg(String delivery_pkg) {
        this.delivery_pkg = delivery_pkg;
    }

    public String getImportJs() {
        return importJs;
    }

    public void setImportJs(String importJs) {
        this.importJs = importJs;
    }
}
