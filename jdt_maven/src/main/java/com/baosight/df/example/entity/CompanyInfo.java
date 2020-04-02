package com.baosight.df.example.entity;

import java.util.Map;

import com.baosight.common.basic.entity.BaseEntity;
import com.baosight.common.utils.NumberUtils;
import com.baosight.common.utils.StringUtils;

public class CompanyInfo extends BaseEntity {

	private Long companyId;
	private String companyCode;
	private String companyName;
	private String companyDesc;
	private String areaCode;

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyDesc() {
		return companyDesc;
	}

	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public void fromMap(Map map) {
		super.fromMap(map);
		setCompanyId(NumberUtils.toLong(map.get("companyId")));
		setCompanyCode(StringUtils.toString(map.get("companyCode")));
		setCompanyName(StringUtils.toString(map.get("companyName")));
		setCompanyDesc(StringUtils.toString(map.get("companyDesc")));
		setAreaCode(StringUtils.toString(map.get("areaCode")));
	}

	public Map toMap() {
		Map map = super.toMap();
		map.put("companyId", companyId);
		map.put("companyCode", companyCode);
		map.put("companyName", companyName);
		map.put("companyDesc", companyDesc);
		map.put("areaCode", areaCode);
		return map;
	}

}
