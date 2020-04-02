package com.baosight.df.metamanage.entity;

import com.baosight.common.basic.entity.BaseEntity;
import com.baosight.common.utils.NumberUtils;
import com.baosight.common.utils.StringUtils;

import java.util.Map;

public class DesignPage extends BaseEntity {

	private Long id;
	private String pageName;
	private String htmlString;
	private String jsString;
	private String cssString;
	private String author;
	private String deliveryPkg;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getHtmlString() {
		return htmlString;
	}

	public void setHtmlString(String htmlString) {
		this.htmlString = htmlString;
	}

	public String getJsString() {
		return jsString;
	}

	public void setJsString(String jsString) {
		this.jsString = jsString;
	}

	public String getCssString() {
		return cssString;
	}

	public void setCssString(String cssString) {
		this.cssString = cssString;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDeliveryPkg() {
		return deliveryPkg;
	}

	public void setDeliveryPkg(String deliveryPkg) {
		this.deliveryPkg = deliveryPkg;
	}


	public void fromMap(Map map) {
		super.fromMap(map);
		setId(NumberUtils.toLong(map.get("id")));
		setPageName(StringUtils.toString(map.get("pageName")));
		setHtmlString(StringUtils.toString(map.get("htmlString")));
		setJsString(StringUtils.toString(map.get("jsString")));
		setCssString(StringUtils.toString(map.get("cssString")));
		setAuthor(StringUtils.toString(map.get("author")));
		setDeliveryPkg(StringUtils.toString(map.get("deliveryPkg")));
	}

	public Map toMap() {
		Map map = super.toMap();
		map.put("id", id);
		map.put("pageName", pageName);
		map.put("htmlString", htmlString);
		map.put("jsString", jsString);
		map.put("cssString", cssString);
		map.put("author", author);
		map.put("deliveryPkg", deliveryPkg);
		return map;
	}

}
