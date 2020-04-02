package com.baosight.df.designer.entity;

import com.baosight.common.basic.entity.BaseEntityBean;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageBean extends BaseEntityBean {
	private String page_ename;
	private String page_cname;
	private String page_path;
	private String page_type;
	private String page_desc;

	public enum PageType {
		designPage, reportPage
	}

	public PageBean(String pagename, String pagecname, String pagedesc, PageType pageType) {
		page_ename = pagename;
		page_cname = pagecname;
		// page_path = "/df/designer/loadsave.do?method=publish&pagename=" +
		// pagename;
		switch (pageType) {
		case designPage:
			page_path = "/df/designer/viewpage.do?pagename=" + pagename;
			page_type = "designPage";
			break;
		case reportPage:
			page_path = "http://192.168.128.96:18080/report/rest/report.html?reportTemplateName=" + pagename;
			page_type = "reportPage";
			break;
		}

		page_desc = pagedesc;

	}

	public String getPage_ename() {
		return page_ename;
	}

	public void setPage_ename(String page_ename) {
		this.page_ename = page_ename;
	}

	public String getPage_cname() {
		return page_cname;
	}

	public void setPage_cname(String page_cname) {
		this.page_cname = page_cname;
	}

	public String getPage_path() {
		return page_path;
	}

	public void setPage_path(String page_path) {
		this.page_path = page_path;
	}

	public String getPage_type() {
		return page_type;
	}

	public void setPage_type(String page_type) {
		this.page_type = page_type;
	}

	public String getPage_desc() {
		return page_desc;
	}

	public void setPage_desc(String page_desc) {
		this.page_desc = page_desc;
	}

}
