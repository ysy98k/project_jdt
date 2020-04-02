package com.baosight.df.metamanage.entity;

import com.baosight.common.basic.entity.BaseEntity;
import com.baosight.common.utils.NumberUtils;
import com.baosight.common.utils.StringUtils;

import java.util.Map;

public class TenantMenu extends BaseEntity implements Comparable<Object>{

	private String parentCode;//父菜单代码
    private String parentName;//父菜单名称
	private Long menuId;//菜单id
	private String menuType;//菜单类型（左侧，上侧）
	private String dispName;//菜单名称
	private String linkType;//节点类型（树，叶子）
	private String linkPath;//页面代码
	private String linkParam;//页面打开方式（弹出、更新、内嵌）
	private Long menuOrder;//页面排序
	private String iconType;//图标类型
	private String iconPath;//图标内容
	private String tenant;//租户名
	private String menuCode;//菜单代码
	private Long menuLevel;//菜单层级
    private String pagePath;//页面路径

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }
	/**
	 * @return the menuLevel
	 */
	public Long getMenuLevel() {
		return menuLevel;
	}

	/**
	 * @param menuLevel
	 *            the menuLevel to set
	 */
	public void setMenuLevel(Long menuLevel) {
		this.menuLevel = menuLevel;
	}

	/**
	 * @return the menuCode
	 */
	public String getMenuCode() {
		return menuCode;
	}

	/**
	 * @param menuCode
	 *            the menuCode to set
	 */
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	/**
	 * @return the parentCode
	 */
	public String getParentCode() {
		return parentCode;
	}

	/**
	 * @param parentCode
	 *            the parentCode to set
	 */
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	/**
	 * @return the menuId
	 */
	public Long getMenuId() {
		return menuId;
	}

	/**
	 * @param menuId
	 *            the menuId to set
	 */
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	/**
	 * @return the menuType
	 */
	public String getMenuType() {
		return menuType;
	}

	/**
	 * @param menuType
	 *            the menuType to set
	 */
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	/**
	 * @return the dispName
	 */
	public String getDispName() {
		return dispName;
	}

	/**
	 * @param dispName
	 *            the dispName to set
	 */
	public void setDispName(String dispName) {
		this.dispName = dispName;
	}

	/**
	 * @return the linkType
	 */
	public String getLinkType() {
		return linkType;
	}

	/**
	 * @param linkType
	 *            the linkType to set
	 */
	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	/**
	 * @return the linkPath
	 */
	public String getLinkPath() {
		return linkPath;
	}

	/**
	 * @param linkPath
	 *            the linkPath to set
	 */
	public void setLinkPath(String linkPath) {
		this.linkPath = linkPath;
	}

	/**
	 * @return the linkParam
	 */
	public String getLinkParam() {
		return linkParam;
	}

	/**
	 * @param linkParam
	 *            the linkParam to set
	 */
	public void setLinkParam(String linkParam) {
		this.linkParam = linkParam;
	}

	/**
	 * @return the menuOrder
	 */
	public Long getMenuOrder() {
		return menuOrder;
	}

	/**
	 * @param menuOrder
	 *            the menuOrder to set
	 */
	public void setMenuOrder(Long menuOrder) {
		this.menuOrder = menuOrder;
	}

	/**
	 * @return the iconType
	 */
	public String getIconType() {
		return iconType;
	}

	/**
	 * @param iconType
	 *            the iconType to set
	 */
	public void setIconType(String iconType) {
		this.iconType = iconType;
	}

	/**
	 * @return the iconPath
	 */
	public String getIconPath() {
		return iconPath;
	}

	/**
	 * @param iconPath
	 *            the iconPath to set
	 */
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	/**
	 * @return the tenant
	 */
	public String getTenant() {
		return tenant;
	}

	/**
	 * @param tenant
	 *            the tenant to set
	 */
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public void fromMap(Map map) {
		super.fromMap(map);
		setParentCode(StringUtils.toString(map.get("parentCode")));
        setParentName(StringUtils.toString(map.get("parentName")));
		setMenuId(NumberUtils.toLong(map.get("menuId")));
		setMenuType(StringUtils.toString(map.get("menuType")));
		setDispName(StringUtils.toString(map.get("dispName")));
		setLinkType(StringUtils.toString(map.get("linkType")));
		setLinkPath(StringUtils.toString(map.get("linkPath")));
		setLinkParam(StringUtils.toString(map.get("linkParam")));
		setMenuOrder(NumberUtils.toLong(map.get("menuOrder")));
		setIconType(StringUtils.toString(map.get("iconType")));
		setIconPath(StringUtils.toString(map.get("iconPath")));
		setMenuCode(StringUtils.toString(map.get("menuCode")));
		setMenuLevel(NumberUtils.toLong(map.get("menuLevel")));
        setPagePath(StringUtils.toString(map.get("pagePath")));
	}

	public Map toMap() {
		Map map = super.toMap();
		map.put("parentCode", parentCode);
        map.put("parentName", parentName);
		map.put("menuId", menuId);
		map.put("menuType", menuType);
		map.put("dispName", dispName);
		map.put("linkType", linkType);
		map.put("linkPath", linkPath);
		map.put("linkParam", linkParam);
		map.put("menuOrder", menuOrder);
		map.put("iconType", iconType);
		map.put("iconPath", iconPath);
		map.put("tenant", tenant);
		map.put("menuCode", menuCode);
		map.put("menuLevel", menuLevel);
        map.put("pagePath", pagePath);
		return map;
	}
	
    @Override
    public int compareTo(Object o) {
        if(this ==o){
            return 0;            
        }
        else if (o!=null && o instanceof TenantMenu) {   
        	TenantMenu tenantMenu = (TenantMenu) o;
            if(menuLevel.compareTo(tenantMenu.menuLevel)<0){
                return 1;
            }else if(menuLevel.compareTo(tenantMenu.menuLevel)==0){
                if(menuOrder.compareTo(tenantMenu.menuOrder)>0) {
                    return 1;
                }else if(menuOrder.compareTo(tenantMenu.menuOrder)==0){
                    return 0;
                }else {
                    return -1;
                }
            }else{
	            return -1;
	        }
	    }else{
	        return -1;
	    }
    }
}
