package com.baosight.df.metamanage.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.dao.RestDao;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.RedisUtils;
import com.baosight.common.utils.StringUtils;
import com.baosight.df.metamanage.entity.PageInfo;
import com.baosight.df.metamanage.entity.TenantMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.apache.commons.codec.binary.Base64.encodeBase64String;

@Service
@Transactional
public class TenantMenuService extends BaseService {


	@Autowired
	private RedisUtils redisUtils;

	@Autowired
	private HttpServletRequest request;
	@Autowired
	public RestDao dao;

	@Value("${aas.host}")
	private String host;

	@Value("${aas.rest_service_name}")
	private String serviceName;

	@Autowired
	private RestDao cvsDao;

	@Value("${cvs.host}")
	private String cvsHost;

	@Value("${cvs.config_service_name}")
	private String cvsServiceName;

	@Value("${jdt.menu.cache}")
	private String menuCache;

	private static final Comparator<TenantMenu> COMPARATOR = new Comparator<TenantMenu>() {
		public int compare(TenantMenu o1, TenantMenu o2) {
			return o1.compareTo(o2);// 运用TenantMenu类的compareTo方法比较两个对象
		}
	};

	/**
	 * 更新
	 *
	 * @param
	 * @return 输出JSONObject
	 */

	public JSONObject updateTenantMenu(JSONObject paramInfo) {
		paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
		JSONObject returnInfo = new JSONObject();
		JSONObject inquStatus = new JSONObject();
		inquStatus
				.put("tenant",
						request.getSession().getAttribute(
								Constants.SESSION_TENANT_KEY));
		paramInfo.put("inqu_status", inquStatus);
		paramInfo.put(BaseService.UPDATE_SQL, "TenantMenu.update");
		paramInfo.put(BaseService.RESULT_BLOCK, "detail");
		paramInfo.put(BaseService.DAO_ENTITY,
				"com.baosight.df.metamanage.entity.TenantMenu");
		returnInfo = super.update(paramInfo);
		return returnInfo;
	}

	/**
	 * 修改父节点
	 *
	 * @param
	 * @return 输出JSONObject
	 */
	public JSONObject updateParent(JSONObject paramInfo) {
		JSONObject returnInfo = new JSONObject();
		paramInfo
				.put("tenant",
						request.getSession().getAttribute(
								Constants.SESSION_TENANT_KEY));
		try {
			sqlSessionTemplate.selectList("TenantMenu.updateParent", paramInfo);
			returnInfo.put("status", 0);
		} catch (Exception e) {
			returnInfo.put("status", -1);
			returnInfo.put("returnMsg", "传入参数传递有误，请重新尝试！");
			e.printStackTrace();
		}
		return returnInfo;
	}

	/**
	 * 查询树
	 *
	 * @param
	 * @return 输出List<JSONObject>
	 */
	public JSONArray querySubTree(JSONObject paramInfo) {

		JSONArray menuLstarray = new JSONArray();
		if (paramInfo.get("treeparentId").equals("#")) {
			JSONObject menu = new JSONObject();
			menu.put("id", "0-#");
			menu.put("parent", "#");
			menu.put("children", true);
			menu.put("text", "菜单展示");
			menuLstarray.add(menu);
		} else {
			paramInfo.put(
					"tenant",
					request.getSession().getAttribute(
							Constants.SESSION_TENANT_KEY));
			List<JSONObject> menuLst = sqlSessionTemplate.selectList(
					"TenantMenu.queryTree", paramInfo);
			if (menuLst != null && menuLst.size() > 0) {
				for (int j = 0; j < menuLst.size(); j++) {
					JSONObject menu = menuLst.get(j);
					menu.put("id",
							menu.get("menuCode") + "-" + menu.get("level"));
					menu.put("parent", paramInfo.get("treeparentId"));
					menu.put("children", true);
					menuLstarray.add(menu);
				}
			}
		}
		return menuLstarray;
	}

	/**
	 * 批量查询菜单权限
	 *
	 * @param
	 * @return 输出JSONObject
	 */
	public JSONObject queryRest(JSONObject paramInfo) {
		dao.setHost(host);
		dao.setServiceName(serviceName);
		String api = "/api/permission/JDT";

		JSONObject mapIn = new JSONObject();


		paramInfo
				.put("tenant",
						request.getSession().getAttribute(
								Constants.SESSION_TENANT_KEY));
		List<JSONObject> menuLst = sqlSessionTemplate.selectList(
				"TenantMenu.queryLink", paramInfo);

		String pageInfo = "";
		ArrayList pageInfoArray = new ArrayList();
		for (int i = 0; i < menuLst.size(); i++) {
			String page = menuLst.get(i).get("linkPath").toString();
			if (page != null && (!page.equals(""))){
				pageInfo += page + ",";
			}
			if(pageInfo.length()<5000){//为防止溢出，限制字符串长度不超过5000
				if(i==menuLst.size()-1)
					pageInfoArray.add(pageInfo);
				continue;
			}else {//字符串长度超过5000后，分段塞入
				pageInfoArray.add(pageInfo);
				pageInfo="";
			}

		}
		JSONObject returnInfo = new JSONObject();
		JSONObject result = new JSONObject();

		for (int ii = 0; ii < pageInfoArray.size(); ii++) {
			String pages = pageInfoArray.get(ii).toString();
			mapIn.put("token",
					request.getSession().getAttribute(Constants.SESSION_TOKEN_KEY));
			mapIn.put("name", pages.substring(0, pages.length() - 1));
			returnInfo = dao.invoke("get", api, mapIn);
			if (returnInfo.get("errcode").equals("0")) {
				result.put("status", 0);
			} else {
				result.put("status", -1);
				result.put("returnMsg", returnInfo.get("errinfo"));
				return result;
			}

			JSONArray recordArray = returnInfo.getJSONArray("permission");
			for (int j = 0; j < recordArray.size(); j++) {
				if (recordArray.getJSONObject(j).get("GET").equals(true))
					result.put(recordArray.getJSONObject(j).get("name").toString(),
							recordArray.getJSONObject(j).get("GET"));
			}
		}

		return result;
	}

	/**
	 * 查询所有，生成菜单项
	 *
	 * @param
	 * @return 输出JSONObjectF
	 */
	public JSONObject queryHomeAndBackgroundMenu(JSONObject paramInfo) {
		JSONObject returnInfo = new JSONObject();
		String menuType = paramInfo.getString("menuType");
		String menuLeftKey = com.baosight.aas.auth.Constants.JDT_REDIS_ROOT + ":"  + com.baosight.aas.auth.Constants.JDT_REDIS_MENU_ROOT + ":" + request.getSession().getAttribute(
				Constants.SESSION_TENANT_KEY) + ":" + request.getSession().getAttribute(
				Constants.SESSION_USERNAME_KEY) + ":" + com.baosight.aas.auth.Constants.JDT_REDIS_MENU_LEFT;
		String menuTopKey = com.baosight.aas.auth.Constants.JDT_REDIS_ROOT + ":"  + com.baosight.aas.auth.Constants.JDT_REDIS_MENU_ROOT + ":" + request.getSession().getAttribute(
				Constants.SESSION_TENANT_KEY) + ":" + request.getSession().getAttribute(
				Constants.SESSION_USERNAME_KEY) + ":" + com.baosight.aas.auth.Constants.JDT_REDIS_MENU_TOP;
		String menuKey = "";
		if("leftMenu".equals(menuType)){
			menuKey = menuLeftKey;
		}else{
			menuKey = menuTopKey;
		}
		if("true".equals(menuCache)){
			String menusFromRedis = redisUtils.get(menuKey);
			if(!org.apache.commons.lang.StringUtils.isBlank(menusFromRedis)){
				returnInfo.put("authMenus", JSONArray.parseArray(menusFromRedis));
				return returnInfo;
			}
		}
		paramInfo
				.put("tenant",
						request.getSession().getAttribute(
								Constants.SESSION_TENANT_KEY));

		// 获取有权限的菜单
		JSONObject result = queryRest(paramInfo);
		if (result.getIntValue("status") != 0) {
			return result;
		}

		List<JSONObject> menuLst = sqlSessionTemplate.selectList(
				"TenantMenu.queryHomeAndBackgroundMenu", paramInfo);

		if (menuLst != null && menuLst.size() > 0) {
			// 节点列表（散列表，用于临时存储节点对象）
			LinkedHashMap nodeList = new LinkedHashMap();
			// 根据结果集构造节点列表（存入散列表）
			ArrayList menuArray = new ArrayList();
			String cvsMenuPageCodes ="";
			JSONObject cvsResult = new JSONObject();
			for (int i = 0; i < menuLst.size(); i++) {
				JSONObject menuNode = menuLst.get(i);

				TenantMenu menu = new TenantMenu();
				menu.fromMap(menuLst.get(i));
				menuArray.add(menu);
				nodeList.put(StringUtils.toString(menuNode.get("menuCode")),
						menuNode);

				String parentCode = StringUtils.toString(menuNode
						.get("parentCode"));
				String linkType = StringUtils.toString(menuNode
						.get("linkType"));
				JSONArray authMenus = returnInfo.getJSONArray("authMenus");
				if (parentCode.equals("0") && linkType.equals("treeNode")) {
					if (authMenus == null) {
						authMenus = new JSONArray();
						returnInfo.put("authMenus", authMenus);
					}
					authMenus.add(menuNode);
				}else if(parentCode.equals("0") && linkType.equals("leafNode")){
					if (whetherHasLinkPathPermission(result, menuNode.get("linkPath")
							.toString()) != null) {
						String[] linkPathStr = menuNode.getString("linkPath")
								.split(",");
						if ( linkPathStr.length > 1) {
							setPagePathAndPageType(
									menuNode,
									whetherHasLinkPathPermission(result,
											menuNode.get("linkPath").toString()));
						}
						if (authMenus == null) {
							authMenus = new JSONArray();
							returnInfo.put("authMenus", authMenus);
						}
						authMenus.add(menuNode);
					}
				}
			}

			Collections.sort(menuArray, COMPARATOR);// 用Comparator对menuArray进行排序

			LinkedHashMap nodeListFromLevel = new LinkedHashMap();
			for (int m = 0; m < menuArray.size(); m++) {
				TenantMenu menu = new TenantMenu();
				menu = (TenantMenu) menuArray.get(m);
				nodeListFromLevel.put(
						StringUtils.toString(menu.toMap().get("menuCode")),
						menu.toMap());
			}

			// 构造多叉树
			Set entrySet = new TreeSet();
			entrySet = nodeListFromLevel.entrySet();

			for (Iterator it = entrySet.iterator(); it.hasNext();) {
				String json = JSON.toJSONString((HashMap) ((Map.Entry) it
						.next()).getValue());
				JSONObject menu = JSON.parseObject(json);
				JSONObject node = ((JSONObject) nodeList.get(menu.get(
						"menuCode").toString()));

				JSONObject parent = ((JSONObject) nodeList.get(menu.get(
						"parentCode").toString()));

				JSONArray array = new JSONArray();
				if (parent != null && parent.getString("linkType").equals("treeNode")) {
					array = parent.getJSONArray("subMenus");
				}
				if (whetherHasLinkPathPermission(result, menu.get("linkPath")
						.toString()) != null
						|| node.get("subMenus") != null) {
					if (array == null) {
						array = new JSONArray();
						parent.put("subMenus", array);
					}
					array.add(node);
					String[] linkPathStr = node.getString("linkPath")
							.split(",");
					if ("leafNode".equals(node.getString("linkType"))
							&& linkPathStr.length > 1) {
						setPagePathAndPageType(
								node,
								whetherHasLinkPathPermission(result,
										menu.get("linkPath").toString()));
					}
				}
				if(StringUtils.toString(node.get("pageType"))!=null && StringUtils.toString(node.get
						("pageType")).equals("cvsLink")){
					cvsMenuPageCodes += node.get("linkPath")+"|";
				}
			}

			if (cvsMenuPageCodes != null && !cvsMenuPageCodes.equals("")){
				cvsResult = queryCvsRest(cvsMenuPageCodes);
			}
			JSONArray topMenuArray = returnInfo.getJSONArray("authMenus");
			JSONArray tempTopMenuArray = new JSONArray();
			setMenuPagePathRecursion(tempTopMenuArray,topMenuArray,cvsResult);
			if("true".equals(menuCache)) {
				redisUtils.set(menuKey, tempTopMenuArray.toJSONString());
				redisUtils.expire(menuKey, 604800);//一周7天
			}
			returnInfo.put("authMenus", tempTopMenuArray);
		}

		return returnInfo;
	}

	/*递归设置菜单的pagePath信息*/
	protected void setMenuPagePathRecursion(JSONArray tempTopMenuArray,JSONArray upMenuArray,JSONObject cvsResult){
		String toolkitPath = request.getContextPath();
		for (int j = 0; j < upMenuArray.size(); j++) {
			if (upMenuArray.getJSONObject(j).get("subMenus") != null) {
				JSONArray oneMenuArray = (JSONArray) upMenuArray.getJSONObject(j).get("subMenus");
				for (int k = 0;k < oneMenuArray.size();k++) {
					if(oneMenuArray.getJSONObject(k).getString("pageType")!=null && (oneMenuArray.getJSONObject(k)
							.getString("pageType").equals("localLink") || oneMenuArray.getJSONObject(k)
							.getString("pageType").equals("designPage"))){
						oneMenuArray.getJSONObject(k).put("pagePath",toolkitPath+oneMenuArray.getJSONObject(k).getString("pagePath"));
					}
					else if(oneMenuArray.getJSONObject(k).getString("pageType")!=null && oneMenuArray.getJSONObject
							(k).getString("pageType").equals("cvsLink")){
						String pageCodeName = oneMenuArray.getJSONObject(k).getString("linkPath");
						if(cvsResult.getString("cvsErrorMsg")!=null){
							//cvs接口返回异常
							oneMenuArray.getJSONObject(k).put("pagePath",toolkitPath+"/bxui/other/jsp/errorinfo" +
									".jsp?errCode=181000&detailMessage="+cvsResult.getString("cvsErrorMsg"));
						}else{
							if(cvsResult.getString("code").equals("0")){
								JSONObject cvsMenus = (JSONObject) cvsResult.get("msg");
								if(cvsMenus.getString(pageCodeName)==null || cvsMenus.getString(pageCodeName)
										.equals("")){
									//cvs接口返回成功，页面不存在为空
									oneMenuArray.getJSONObject(k).put("pagePath",
											toolkitPath+"/bxui/other/jsp/errorinfo" +
													".jsp?errCode=181002&detailMessage=没有cvs画面");
								}else{
									//cvs接口返回成功，页面存在返回url
									oneMenuArray.getJSONObject(k).put("pagePath", cvsMenus.getString
											(pageCodeName));
								}
							}
							else{
								//cvs接口返回失败
								oneMenuArray.getJSONObject(k).put("pagePath",
										toolkitPath+"/bxui/other/jsp/errorinfo.jsp?errCode=181001&detailMessage="+cvsResult.get("msg"));
							}
						}
					}
				}
				if(0 == upMenuArray.getJSONObject(j).getInteger("menuLevel")){
					tempTopMenuArray.add(upMenuArray.getJSONObject(j));
				}
			}else if(upMenuArray.getJSONObject(j).getString("linkType").equals("leafNode") && upMenuArray
					.getJSONObject(j).getString("parentCode").equals("0")){
				JSONObject obj = upMenuArray.getJSONObject(j);
				if(obj.getString("pageType")!=null && (obj.getString("pageType").equals("localLink") || obj
						.getString("pageType").equals("designPage"))){
					obj.put("pagePath", toolkitPath + obj.getString("pagePath"));
				} else if(obj.getString("pageType")!=null && obj.getString("pageType").equals("cvsLink")){
					String pageCodeName = obj.getString("linkPath");
					if(cvsResult.getString("cvsErrorMsg")!=null){
						//cvs接口返回异常
						obj.put("pagePath", toolkitPath + "/bxui/other/jsp/errorinfo" +
								".jsp?errCode=181000&detailMessage=" + cvsResult.getString("cvsErrorMsg"));
					}else{
						if(cvsResult.getString("code").equals("0")){
							JSONObject cvsMenus = (JSONObject) cvsResult.get("msg");
							if(cvsMenus.getString(pageCodeName)==null || cvsMenus.getString(pageCodeName)
									.equals("")){
								//cvs接口返回成功，页面不存在为空
								obj.put("pagePath",
										toolkitPath + "/bxui/other/jsp/errorinfo" +
												".jsp?errCode=181002&detailMessage=没有cvs画面");
							}else{
								//cvs接口返回成功，页面存在返回url
								obj.put("pagePath", cvsMenus.getString
										(pageCodeName));
							}
						}
						else{
							//cvs接口返回失败
							obj.put("pagePath",
									toolkitPath+"/bxui/other/jsp/errorinfo.jsp?errCode=181001&detailMessage="+cvsResult.get("msg"));
						}
					}
				}
				tempTopMenuArray.add(upMenuArray.getJSONObject(j));
			}
			if(null != upMenuArray.getJSONObject(j).getJSONArray("subMenus")){
				setMenuPagePathRecursion(tempTopMenuArray,upMenuArray.getJSONObject(j).getJSONArray("subMenus"),cvsResult);
			}
		}
	}

	// 设置node的pagePath和pageType，因为当node关联多个页面后，pagePath与pageType为空，需要再次设置
	private void setPagePathAndPageType(JSONObject node, String pageEname) {
		if (pageEname == null) {
			return;
		}
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageEname(pageEname);
		pageInfo.setTenant((String) request.getSession().getAttribute(
				Constants.SESSION_TENANT_KEY));
		List<JSONObject> pageLst = sqlSessionTemplate.selectList(
				"PageManage.queryPrecise", pageInfo);
		if (pageLst.size() > 0) {
			JSONObject onePage = pageLst.get(0);
			node.put("pageType", onePage.getString("pageType"));
			node.put("pagePath", onePage.getString("pagePath"));
			node.put("linkPath", pageEname);
		}
	}

	/*
	 * 当一个菜单关联多个页面的时候，判断是否有权限，如果有权限，只返回第一个匹配的页面号（针对轨道重庆5号线的需求改造）
	 */
	private String whetherHasLinkPathPermission(JSONObject result,
												String linkPathStr) {
		String[] linkPathArray = linkPathStr.split(",");
		for (int i = 0; i < linkPathArray.length; i++) {
			String oneLinkPath = linkPathArray[i];
			if (result.containsKey(oneLinkPath)) {
				return oneLinkPath;
			}
		}
		return null;
	}

	/**
	 * 查询记录
	 *
	 * @param
	 * @return 输出JSONObject
	 */
	public JSONObject queryTenantMenu(JSONObject paramInfo) {
		paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
		JSONObject inquStatus = new JSONObject();
		inquStatus
				.put("tenant",
						request.getSession().getAttribute(
								Constants.SESSION_TENANT_KEY));
		if (paramInfo.get("parentCode") != null)
			inquStatus.put("parentCode", paramInfo.get("parentCode"));
		if (paramInfo.get("parentName") != null)
			inquStatus.put("parentName", paramInfo.get("parentName"));
		if (paramInfo.get("dispName") != null)
			inquStatus.put("dispName", paramInfo.get("dispName"));
		if (paramInfo.get("menuCode") != null)
			inquStatus.put("menuCode", paramInfo.get("menuCode"));
		if (paramInfo.get("menuType") != null)
			inquStatus.put("menuType", paramInfo.get("menuType"));
		paramInfo.put("inqu_status", inquStatus);
		paramInfo.put(BaseService.QUERY_SQL, "TenantMenu.query");
		paramInfo.put(BaseService.COUNT_SQL, "TenantMenu.queryCount");
		paramInfo.put(BaseService.DAO_ENTITY,
				"com.baosight.df.metamanage.entity.TenantMenu");
		JSONObject returnInfo = super.query(paramInfo);
		return returnInfo;
	}

	/**
	 * 插入记录
	 *
	 * @param
	 * @return 输出JSONObject
	 */
	public JSONObject insertTenantMenu(JSONObject paramInfo) {
		paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
		JSONObject inquStatus = new JSONObject();
		inquStatus
				.put("tenant",
						request.getSession().getAttribute(
								Constants.SESSION_TENANT_KEY));
		paramInfo.put("inqu_status", inquStatus);
		paramInfo.put(BaseService.INSERT_SQL, "TenantMenu.insert");
		paramInfo.put(BaseService.RESULT_BLOCK, "detail");
		paramInfo.put(BaseService.DAO_ENTITY,
				"com.baosight.df.metamanage.entity.TenantMenu");
		JSONObject returnInfo = super.insert(paramInfo);
		return returnInfo;
	}

	/**
	 * 删除
	 *
	 * @param
	 * @return 输出JSONObject
	 */

	public JSONObject deleteTenantMenu(JSONObject paramInfo) {
		JSONObject returnInfo = new JSONObject();
		JSONObject resultBlock = (JSONObject) paramInfo.get("result");
		List<Object> deleteRows = resultBlock.getJSONArray("resultRow");
		if (deleteRows == null || deleteRows.size() == 0) {
			returnInfo.put("status", 0);
			returnInfo.put("returnMsg", "删除了0条记录！");
			return returnInfo;
		}
		for (int i = 0; i < deleteRows.size(); i++) {
			JSONObject tempRow = (JSONObject) deleteRows.get(i);
			tempRow.put(
					"tenant",
					request.getSession().getAttribute(
							Constants.SESSION_TENANT_KEY));
			List<JSONObject> menuLst = sqlSessionTemplate.selectList(
					"TenantMenu.queryLeafCount", tempRow);
			if (menuLst != null && menuLst.size() > 0) {
				returnInfo.put("status", -1);
				returnInfo.put("returnMsg", "删除出错，此记录已被其它项目所引用，无法删除！");
				return returnInfo;
			}
		}
		paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
		JSONObject inquStatus = new JSONObject();
		inquStatus
				.put("tenant",
						request.getSession().getAttribute(
								Constants.SESSION_TENANT_KEY));
		paramInfo.put("inqu_status", inquStatus);
		paramInfo.put(BaseService.DELETE_SQL, "TenantMenu.delete");
		paramInfo.put(BaseService.RESULT_BLOCK, "result");
		paramInfo.put(BaseService.DAO_ENTITY,
				"com.baosight.df.metamanage.entity.TenantMenu");

		returnInfo = super.delete(paramInfo);
		returnInfo.put("status", 0);
		return returnInfo;
	}

	public JSONObject getDownloadData(JSONObject paramInfo) {
		paramInfo.put(
				"tenant",
				request.getSession().getAttribute(
						Constants.SESSION_TENANT_KEY));
		List<JSONObject> menuLst = sqlSessionTemplate.selectList(
				"TenantMenu.queryHomeAndBackgroundMenu", paramInfo);
		JSONObject obj = new JSONObject();
		obj.put("rows",menuLst);
		return obj;
	}


	public JSONObject insertTenantMenuForUpload(JSONObject paramInfo) {
		paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
		JSONObject inquStatus = new JSONObject();
		inquStatus
				.put("tenant",
						request.getSession().getAttribute(
								Constants.SESSION_TENANT_KEY));
		paramInfo.put("inqu_status", inquStatus);
		paramInfo.put(BaseService.INSERT_SQL, "TenantMenu.insertForUpload");
		paramInfo.put(BaseService.RESULT_BLOCK, "detail");
		paramInfo.put(BaseService.DAO_ENTITY,
				"com.baosight.df.metamanage.entity.TenantMenu");
		JSONObject returnInfo = super.insertForUpload(paramInfo);
		return returnInfo;
	}

	public JSONObject queryCvsRest(String ajaxParam) {
		JSONObject returnInfo = new JSONObject();
		cvsDao.setHost(cvsHost);
		cvsDao.setServiceName(cvsServiceName);

		JSONObject mapIn = new JSONObject();
		mapIn.put("token",
				request.getSession().getAttribute(Constants.SESSION_TOKEN_KEY));
		mapIn.put("action", "getShareLinkByParams");
		mapIn.put("pagename", encodeBase64String(ajaxParam.getBytes()));

		try{
			returnInfo = cvsDao.invoke("get", "/bpd", mapIn);
		}catch (Exception e){
			e.printStackTrace();
			returnInfo.put("cvsErrorMsg",e.getMessage());
		}

		return returnInfo;
	}


	/**
	 * 查询所有，生成菜单项
	 *
	 * @param
	 * @return 输出JSONObjectF
	 */
	public JSONObject queryHomeAndBackgroundMenuOfRest(JSONObject paramInfo,String username,String token) {
		JSONObject returnInfo = new JSONObject();
		String menuType = paramInfo.getString("menuType");
		String menuLeftKey = com.baosight.aas.auth.Constants.JDT_REDIS_ROOT + ":"  + com.baosight.aas.auth.Constants.JDT_REDIS_MENU_ROOT + ":"
				+  "raising:"+ username +":"
				+ com.baosight.aas.auth.Constants.JDT_REDIS_MENU_LEFT;
		String menuTopKey = com.baosight.aas.auth.Constants.JDT_REDIS_ROOT + ":"  + com.baosight.aas.auth.Constants.JDT_REDIS_MENU_ROOT + ":"
				+  "raising:"+ username + ":"
				+ com.baosight.aas.auth.Constants.JDT_REDIS_MENU_TOP;
		String menuKey = "";
		if("leftMenu".equals(menuType)){
			menuKey = menuLeftKey;
		}else{
			menuKey = menuTopKey;
		}
		if("true".equals(menuCache)){
			String menusFromRedis = redisUtils.get(menuKey);
			if(!org.apache.commons.lang.StringUtils.isBlank(menusFromRedis)){
				returnInfo.put("authMenus", JSONArray.parseArray(menusFromRedis));
				return returnInfo;
			}
		}
		paramInfo
				.put("tenant","raising");

		// 获取有权限的菜单
		JSONObject result = queryRestOfRest(paramInfo,token);
		if (result.getIntValue("status") != 0) {
			return result;
		}

		List<JSONObject> menuLst = sqlSessionTemplate.selectList(
				"TenantMenu.queryHomeAndBackgroundMenu", paramInfo);

		if (menuLst != null && menuLst.size() > 0) {
			// 节点列表（散列表，用于临时存储节点对象）
			LinkedHashMap nodeList = new LinkedHashMap();
			// 根据结果集构造节点列表（存入散列表）
			ArrayList menuArray = new ArrayList();
			String cvsMenuPageCodes ="";
			JSONObject cvsResult = new JSONObject();
			for (int i = 0; i < menuLst.size(); i++) {
				JSONObject menuNode = menuLst.get(i);

				TenantMenu menu = new TenantMenu();
				menu.fromMap(menuLst.get(i));
				menuArray.add(menu);
				nodeList.put(StringUtils.toString(menuNode.get("menuCode")),
						menuNode);

				String parentCode = StringUtils.toString(menuNode
						.get("parentCode"));
				String linkType = StringUtils.toString(menuNode
						.get("linkType"));
				JSONArray authMenus = returnInfo.getJSONArray("authMenus");
				if (parentCode.equals("0") && linkType.equals("treeNode")) {
					if (authMenus == null) {
						authMenus = new JSONArray();
						returnInfo.put("authMenus", authMenus);
					}
					authMenus.add(menuNode);
				}else if(parentCode.equals("0") && linkType.equals("leafNode")){
					if (whetherHasLinkPathPermission(result, menuNode.get("linkPath")
							.toString()) != null) {
						String[] linkPathStr = menuNode.getString("linkPath")
								.split(",");
						if ( linkPathStr.length > 1) {
							setPagePathAndPageType(
									menuNode,
									whetherHasLinkPathPermission(result,
											menuNode.get("linkPath").toString()));
						}
						if (authMenus == null) {
							authMenus = new JSONArray();
							returnInfo.put("authMenus", authMenus);
						}
						authMenus.add(menuNode);
					}
				}
			}

			Collections.sort(menuArray, COMPARATOR);// 用Comparator对menuArray进行排序

			LinkedHashMap nodeListFromLevel = new LinkedHashMap();
			for (int m = 0; m < menuArray.size(); m++) {
				TenantMenu menu = new TenantMenu();
				menu = (TenantMenu) menuArray.get(m);
				nodeListFromLevel.put(
						StringUtils.toString(menu.toMap().get("menuCode")),
						menu.toMap());
			}

			// 构造多叉树
			Set entrySet = new TreeSet();
			entrySet = nodeListFromLevel.entrySet();

			for (Iterator it = entrySet.iterator(); it.hasNext();) {
				String json = JSON.toJSONString((HashMap) ((Map.Entry) it
						.next()).getValue());
				JSONObject menu = JSON.parseObject(json);
				JSONObject node = ((JSONObject) nodeList.get(menu.get(
						"menuCode").toString()));

				JSONObject parent = ((JSONObject) nodeList.get(menu.get(
						"parentCode").toString()));

				JSONArray array = new JSONArray();
				if (parent != null && parent.getString("linkType").equals("treeNode")) {
					array = parent.getJSONArray("subMenus");
				}
				if (whetherHasLinkPathPermission(result, menu.get("linkPath")
						.toString()) != null
						|| node.get("subMenus") != null) {
					if (array == null) {
						array = new JSONArray();
						parent.put("subMenus", array);
					}
					array.add(node);
					String[] linkPathStr = node.getString("linkPath")
							.split(",");
					if ("leafNode".equals(node.getString("linkType"))
							&& linkPathStr.length > 1) {
						setPagePathAndPageType(
								node,
								whetherHasLinkPathPermission(result,
										menu.get("linkPath").toString()));
					}
				}
				if(StringUtils.toString(node.get("pageType"))!=null && StringUtils.toString(node.get
						("pageType")).equals("cvsLink")){
					cvsMenuPageCodes += node.get("linkPath")+"|";
				}
			}

			if (cvsMenuPageCodes != null && !cvsMenuPageCodes.equals("")){
				cvsResult = queryCvsRestOfRest(cvsMenuPageCodes,token);
			}
			JSONArray topMenuArray = returnInfo.getJSONArray("authMenus");
			JSONArray tempTopMenuArray = new JSONArray();
			setMenuPagePathRecursionOfRest(tempTopMenuArray,topMenuArray,cvsResult);
			if("true".equals(menuCache)) {
				redisUtils.set(menuKey, tempTopMenuArray.toJSONString());
				redisUtils.expire(menuKey, 604800);//一周7天
			}
			returnInfo.put("authMenus", tempTopMenuArray);
		}

		return returnInfo;
	}

	/**
	 * 批量查询菜单权限
	 *
	 * @param
	 * @return 输出JSONObject
	 */
	public JSONObject queryRestOfRest(JSONObject paramInfo,String token) {
		dao.setHost(host);
		dao.setServiceName(serviceName);
		String api = "/api/permission/JDT";

		JSONObject mapIn = new JSONObject();


		paramInfo.put("tenant","raising");
		List<JSONObject> menuLst = sqlSessionTemplate.selectList(
				"TenantMenu.queryLink", paramInfo);

		String pageInfo = "";
		ArrayList pageInfoArray = new ArrayList();
		for (int i = 0; i < menuLst.size(); i++) {
			String page = menuLst.get(i).get("linkPath").toString();
			if (page != null && (!page.equals(""))){
				pageInfo += page + ",";
			}
			if(pageInfo.length()<5000){//为防止溢出，限制字符串长度不超过5000
				if(i==menuLst.size()-1)
					pageInfoArray.add(pageInfo);
				continue;
			}else {//字符串长度超过5000后，分段塞入
				pageInfoArray.add(pageInfo);
				pageInfo="";
			}

		}
		JSONObject returnInfo = new JSONObject();
		JSONObject result = new JSONObject();

		for (int ii = 0; ii < pageInfoArray.size(); ii++) {
			String pages = pageInfoArray.get(ii).toString();
			mapIn.put("token",token);
			mapIn.put("name", pages.substring(0, pages.length() - 1));
			returnInfo = dao.invoke("get", api, mapIn);
			if (returnInfo.get("errcode").equals("0")) {
				result.put("status", 0);
			} else {
				result.put("status", -1);
				result.put("returnMsg", returnInfo.get("errinfo"));
				return result;
			}

			JSONArray recordArray = returnInfo.getJSONArray("permission");
			for (int j = 0; j < recordArray.size(); j++) {
				if (recordArray.getJSONObject(j).get("GET").equals(true))
					result.put(recordArray.getJSONObject(j).get("name").toString(),
							recordArray.getJSONObject(j).get("GET"));
			}
		}

		return result;
	}

	public JSONObject queryCvsRestOfRest(String ajaxParam,String token) {
		JSONObject returnInfo = new JSONObject();
		cvsDao.setHost(cvsHost);
		cvsDao.setServiceName(cvsServiceName);

		JSONObject mapIn = new JSONObject();
		mapIn.put("token",token);
		mapIn.put("action", "getShareLinkByParams");
		mapIn.put("pagename", encodeBase64String(ajaxParam.getBytes()));

		try{
			returnInfo = cvsDao.invoke("get", "/bpd", mapIn);
		}catch (Exception e){
			e.printStackTrace();
			returnInfo.put("cvsErrorMsg",e.getMessage());
		}

		return returnInfo;
	}

	/*递归设置菜单的pagePath信息*/
	protected void setMenuPagePathRecursionOfRest(JSONArray tempTopMenuArray,JSONArray upMenuArray,JSONObject cvsResult){
		String toolkitPath = "jdt";
		for (int j = 0; j < upMenuArray.size(); j++) {
			if (upMenuArray.getJSONObject(j).get("subMenus") != null) {
				JSONArray oneMenuArray = (JSONArray) upMenuArray.getJSONObject(j).get("subMenus");
				for (int k = 0;k < oneMenuArray.size();k++) {
					if(oneMenuArray.getJSONObject(k).getString("pageType")!=null && (oneMenuArray.getJSONObject(k)
							.getString("pageType").equals("localLink") || oneMenuArray.getJSONObject(k)
							.getString("pageType").equals("designPage"))){
						oneMenuArray.getJSONObject(k).put("pagePath",toolkitPath+oneMenuArray.getJSONObject(k).getString("pagePath"));
					}
					else if(oneMenuArray.getJSONObject(k).getString("pageType")!=null && oneMenuArray.getJSONObject
							(k).getString("pageType").equals("cvsLink")){
						String pageCodeName = oneMenuArray.getJSONObject(k).getString("linkPath");
						if(cvsResult.getString("cvsErrorMsg")!=null){
							//cvs接口返回异常
							oneMenuArray.getJSONObject(k).put("pagePath",toolkitPath+"/bxui/other/jsp/errorinfo" +
									".jsp?errCode=181000&detailMessage="+cvsResult.getString("cvsErrorMsg"));
						}else{
							if(cvsResult.getString("code").equals("0")){
								JSONObject cvsMenus = (JSONObject) cvsResult.get("msg");
								if(cvsMenus.getString(pageCodeName)==null || cvsMenus.getString(pageCodeName)
										.equals("")){
									//cvs接口返回成功，页面不存在为空
									oneMenuArray.getJSONObject(k).put("pagePath",
											toolkitPath+"/bxui/other/jsp/errorinfo" +
													".jsp?errCode=181002&detailMessage=没有cvs画面");
								}else{
									//cvs接口返回成功，页面存在返回url
									oneMenuArray.getJSONObject(k).put("pagePath", cvsMenus.getString
											(pageCodeName));
								}
							}
							else{
								//cvs接口返回失败
								oneMenuArray.getJSONObject(k).put("pagePath",
										toolkitPath+"/bxui/other/jsp/errorinfo.jsp?errCode=181001&detailMessage="+cvsResult.get("msg"));
							}
						}
					}
				}
				if(0 == upMenuArray.getJSONObject(j).getInteger("menuLevel")){
					tempTopMenuArray.add(upMenuArray.getJSONObject(j));
				}
			}else if(upMenuArray.getJSONObject(j).getString("linkType").equals("leafNode") && upMenuArray
					.getJSONObject(j).getString("parentCode").equals("0")){
				JSONObject obj = upMenuArray.getJSONObject(j);
				if(obj.getString("pageType")!=null && (obj.getString("pageType").equals("localLink") || obj
						.getString("pageType").equals("designPage"))){
					obj.put("pagePath", toolkitPath + obj.getString("pagePath"));
				} else if(obj.getString("pageType")!=null && obj.getString("pageType").equals("cvsLink")){
					String pageCodeName = obj.getString("linkPath");
					if(cvsResult.getString("cvsErrorMsg")!=null){
						//cvs接口返回异常
						obj.put("pagePath", toolkitPath + "/bxui/other/jsp/errorinfo" +
								".jsp?errCode=181000&detailMessage=" + cvsResult.getString("cvsErrorMsg"));
					}else{
						if(cvsResult.getString("code").equals("0")){
							JSONObject cvsMenus = (JSONObject) cvsResult.get("msg");
							if(cvsMenus.getString(pageCodeName)==null || cvsMenus.getString(pageCodeName)
									.equals("")){
								//cvs接口返回成功，页面不存在为空
								obj.put("pagePath",
										toolkitPath + "/bxui/other/jsp/errorinfo" +
												".jsp?errCode=181002&detailMessage=没有cvs画面");
							}else{
								//cvs接口返回成功，页面存在返回url
								obj.put("pagePath", cvsMenus.getString
										(pageCodeName));
							}
						}
						else{
							//cvs接口返回失败
							obj.put("pagePath",
									toolkitPath+"/bxui/other/jsp/errorinfo.jsp?errCode=181001&detailMessage="+cvsResult.get("msg"));
						}
					}
				}
				tempTopMenuArray.add(upMenuArray.getJSONObject(j));
			}
			if(null != upMenuArray.getJSONObject(j).getJSONArray("subMenus")){
				setMenuPagePathRecursionOfRest(tempTopMenuArray,upMenuArray.getJSONObject(j).getJSONArray("subMenus"),cvsResult);
			}
		}
	}


}
