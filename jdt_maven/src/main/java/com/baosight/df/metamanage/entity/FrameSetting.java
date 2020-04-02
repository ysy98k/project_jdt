package com.baosight.df.metamanage.entity;

import java.util.Map;

import com.baosight.common.basic.entity.BaseEntity;
import com.baosight.common.utils.NumberUtils;
import com.baosight.common.utils.StringUtils;

public class FrameSetting extends BaseEntity {

	private String skinName;//皮肤名
	private String skinDesc;//皮肤描述
	private String logoIcon;//logo的图标
    private String bigLogoIcon;//logo的图标
	private String logoText;//logo的文字
	private String homepage;//首页页面代码
    private String frontHomePage;//上侧菜单首页页面代码
    private String tenant;//租户名
    private String pagePath;//页面路径

    public String getFrontHomePage() {
        return frontHomePage;
    }

    public void setFrontHomePage(String frontHomePage) {
        this.frontHomePage = frontHomePage;
    }

    public String getBigLogoIcon() {
        return bigLogoIcon;
    }

    public void setBigLogoIcon(String bigLogoIcon) {
        this.bigLogoIcon = bigLogoIcon;
    }

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    public String getLogoIcon() {
		return logoIcon;
	}

	public void setLogoIcon(String logoIcon) {
		this.logoIcon = logoIcon;
	}

	public String getLogoText() {
		return logoText;
	}

	public void setLogoText(String logoText) {
		this.logoText = logoText;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getTenant() {
        return tenant;
    }
    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

	public String getSkinName() {
		return skinName;
	}
	public void setSkinName(String skinName) {
		this.skinName = skinName;
	}

	public String getSkinDesc() {
		return skinDesc;
	}
	public void setSkinDesc(String pageCname) {
		this.skinDesc = skinDesc;
	}

	public void fromMap(Map map) {
		super.fromMap(map);
		setSkinName(StringUtils.toString(map.get("skinName")));
		setSkinDesc(StringUtils.toString(map.get("skinDesc")));
		setLogoIcon(StringUtils.toString(map.get("logoIcon")));
        setBigLogoIcon(StringUtils.toString(map.get("bigLogoIcon")));
		setLogoText(StringUtils.toString(map.get("logoText")));
		setHomepage(StringUtils.toString(map.get("homepage")));
        setFrontHomePage(StringUtils.toString(map.get("frontHomePage")));
        setTenant(StringUtils.toString(map.get("tenant")));
        setPagePath(StringUtils.toString(map.get("pagePath")));
	}

	public Map toMap() {
		Map map = super.toMap();
		map.put("skinName", skinName);
		map.put("skinDesc", skinDesc);
		map.put("logoIcon", logoIcon);
        map.put("bigLogoIcon", bigLogoIcon);
		map.put("logoText", logoText);
		map.put("homepage", homepage);
        map.put("frontHomePage", frontHomePage);
        map.put("tenant", tenant);
        map.put("pagePath",pagePath);
		return map;
	}

}
