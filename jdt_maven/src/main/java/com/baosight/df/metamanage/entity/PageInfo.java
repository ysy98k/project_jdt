package com.baosight.df.metamanage.entity;

import com.baosight.common.basic.entity.BaseEntity;
import com.baosight.common.utils.NumberUtils;
import com.baosight.common.utils.StringUtils;

import java.util.Map;

public class PageInfo extends BaseEntity {

	private Long pageId;
	private String pageEname;
	private String pageCname;
	private String pagePath;
	private String pageType;
	private String pageDesc;

	public Long getPageId() {
		return pageId;
	}

	public void setPageId(Long pageId) {
		this.pageId = pageId;
	}

	public String getPageEname() {
		return pageEname;
	}

	public void setPageEname(String pageEname) {
		this.pageEname = pageEname;
	}

	public String getPageCname() {
		return pageCname;
	}

	public void setPageCname(String pageCname) {
		this.pageCname = pageCname;
	}

	public String getPagePath() {
		return pagePath;
	}

	public void setPagePath(String pagePath) {
		this.pagePath = pagePath;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	public String getPageDesc() {
		return pageDesc;
	}

	public void setPageDesc(String pageDesc) {
		this.pageDesc = pageDesc;
	}

	public void fromMap(Map map) {
		super.fromMap(map);
		setPageId(NumberUtils.toLong(map.get("pageId")));
		setPageEname(StringUtils.toString(map.get("pageEname")));
		setPageCname(StringUtils.toString(map.get("pageCname")));
		setPagePath(StringUtils.toString(map.get("pagePath")));
		setPageType(StringUtils.toString(map.get("pageType")));
		setPageDesc(StringUtils.toString(map.get("pageDesc")));
	}

	public Map toMap() {
		Map map = super.toMap();
		map.put("pageId", pageId);
		map.put("pageEname", pageEname);
		map.put("pageCname", pageCname);
		map.put("pagePath", pagePath);
		map.put("pageType", pageType);
		map.put("pageDesc", pageDesc);
		return map;
	}

}
