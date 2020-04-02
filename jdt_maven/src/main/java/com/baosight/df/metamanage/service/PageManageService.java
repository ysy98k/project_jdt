package com.baosight.df.metamanage.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.JsonUtils;
import com.baosight.df.metamanage.entity.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class PageManageService extends BaseService {

	@Value("${designer.delivery_pkg}")
	private String delivery_pkg;

	@Autowired
	private HttpServletRequest request;

	/**
	 * 查询
	 *
	 * @param
	 * @return 输出JSONObject
	 */

	public JSONObject query(JSONObject paramInfo) {
		JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
		paramInfo.put("inqu_status", inquStatus);
		paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
		paramInfo.put(BaseService.QUERY_SQL, "PageManage.query");
		paramInfo.put(BaseService.COUNT_SQL, "PageManage.count");
		paramInfo.put(BaseService.DAO_ENTITY,
				"com.baosight.df.metamanage.entity.PageInfo");
		JSONObject returnInfo = super.query(paramInfo);
		return returnInfo;
	}

	/**
	 * 插入
	 *
	 * @param
	 * @return 输出JSONObject
	 */

	public JSONObject insert(JSONObject paramInfo) {
		paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
		JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
		paramInfo.put("inqu_status", inquStatus);
		boolean update = paramInfo.getBoolean("update");
		if(update == true){
			paramInfo.put(BaseService.INSERT_SQL, "PageManage.insertForUpload");
		}else{
			paramInfo.put(BaseService.INSERT_SQL, "PageManage.insert");
		}
		paramInfo.put(BaseService.RESULT_BLOCK, "detail");
		paramInfo.put(BaseService.DAO_ENTITY,
				"com.baosight.df.metamanage.entity.PageInfo");
		JSONObject returnInfo = new JSONObject();
		JSONArray newRow = new JSONArray();
		JSONArray resultRow = paramInfo.getJSONObject("detail").getJSONArray(
				"resultRow");
		for (int i = 0; i < resultRow.size(); i++) {
			JSONObject resultOne = resultRow.getJSONObject(i);
			if ("designPage".equals(resultOne.getString("pageType"))) {
				resultOne.put("pagePath","/df/designer/viewpage.do?pagename=" + resultOne.getString("pageEname"));
				JSONObject designOne = new JSONObject();
				designOne.put("pageName", resultOne.getString("pageEname"));
				designOne.put("htmlString", "");
				designOne.put("jsString", "");
				designOne.put("cssString", "");
				designOne.put("author", "");
				designOne.put("deliveryPkg", delivery_pkg);
				newRow.add(designOne);
			}
		}
		if(update == true){
			returnInfo = super.insertForUpload(paramInfo);
		}else{
			returnInfo = super.insert(paramInfo);
		}
		if (Constants.EXECUTE_FAIL.equals(returnInfo.getString("status"))) {
			return returnInfo;
		}
		if (newRow.size() > 0) {
			JSONObject designParam = new JSONObject();
			try {
				designParam = (JSONObject) JsonUtils.deepClone(paramInfo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			designParam.getJSONObject("detail").put("resultRow", newRow);
			designParam.put(BaseService.INSERT_SQL,
					"PageManage.insertDesignPage");
			designParam.put(BaseService.DAO_ENTITY,
					"com.baosight.df.metamanage.entity.DesignPage");
			JSONObject returnInfo2 = super.insert(designParam);
			if (Constants.EXECUTE_FAIL.equals(returnInfo2.getString("status"))) {
				return returnInfo2;
			}
		}
		return returnInfo;
	}

	/**
	 * 删除
	 *
	 * @param
	 * @return 输出JSONObject
	 */

	public JSONObject delete(JSONObject paramInfo) {
		paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
		JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
		paramInfo.put("inqu_status", inquStatus);
		paramInfo.put(BaseService.DELETE_SQL, "PageManage.delete");
		paramInfo.put(BaseService.RESULT_BLOCK, "result");
		paramInfo.put(BaseService.DAO_ENTITY,
				"com.baosight.df.metamanage.entity.PageInfo");
		JSONObject returnInfo = super.delete(paramInfo);
		if (Constants.EXECUTE_SUCCESS.equals(returnInfo.getString("status"))) {
			JSONArray newRow = new JSONArray();
			JSONArray resultRow = paramInfo.getJSONObject("result")
					.getJSONArray("resultRow");
			for (int i = 0; i < resultRow.size(); i++) {
				JSONObject resultOne = resultRow.getJSONObject(i);
				if ("designPage".equals(resultOne.getString("pageType"))) {
					JSONObject designOne = new JSONObject();
					designOne.put("pageName", resultOne.getString("pageEname"));
					designOne.put("htmlString", "");
					designOne.put("jsString", "");
					designOne.put("cssString", "");
					designOne.put("author", "");
					newRow.add(designOne);
				}
			}
			if (newRow.size() > 0) {
				JSONObject designParam = new JSONObject();
				try {
					designParam = (JSONObject) JsonUtils.deepClone(paramInfo);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				designParam.getJSONObject("result").put("resultRow", newRow);
				designParam.put(BaseService.DELETE_SQL,
						"PageManage.deleteDesignPage");
				designParam.put(BaseService.DAO_ENTITY,
						"com.baosight.df.metamanage.entity.DesignPage");
				JSONObject returnInfo2 = super.delete(designParam);
				if (Constants.EXECUTE_FAIL.equals(returnInfo2
						.getString("status"))) {
					return returnInfo2;
				}
			}
		}
		return returnInfo;
	}

	public JSONObject deleteDesignPage(JSONObject paramInfo) {
		paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
		JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
		paramInfo.put("inqu_status", inquStatus);
		paramInfo.put(BaseService.DELETE_SQL, "PageManage.deletedesign");
		paramInfo.put(BaseService.RESULT_BLOCK, "result");
		paramInfo.put(BaseService.DAO_ENTITY,
				"com.baosight.df.metamanage.entity.PageInfo");
		JSONObject returnInfo = super.delete(paramInfo);
		return returnInfo;
	}

	public JSONObject deleteReportPage(JSONObject paramInfo) {
		paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
		JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
		paramInfo.put("inqu_status", inquStatus);
		paramInfo.put(BaseService.DELETE_SQL, "PageManage.deletereport");
		paramInfo.put(BaseService.RESULT_BLOCK, "result");
		paramInfo.put(BaseService.DAO_ENTITY,
				"com.baosight.df.metamanage.entity.PageInfo");
		JSONObject returnInfo = super.delete(paramInfo);
		return returnInfo;
	}

	/**
	 * 更新
	 *
	 * @param
	 * @return 输出JSONObject
	 */

	public JSONObject update(JSONObject paramInfo) {
		paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
		JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
		paramInfo.put("inqu_status", inquStatus);
		paramInfo.put(BaseService.UPDATE_SQL, "PageManage.update");
		paramInfo.put(BaseService.RESULT_BLOCK, "detail");
		paramInfo.put(BaseService.DAO_ENTITY,
				"com.baosight.df.metamanage.entity.PageInfo");
		JSONObject returnInfo = super.update(paramInfo);
		return returnInfo;
	}

	/**
	 * 查询单条记录
	 *
	 * @param
	 * @return 输出JSONObject
	 */
	public JSONObject queryOne(JSONObject paramInfo) {
		JSONObject returnInfo = new JSONObject();
		HashMap queryMap = JsonUtils.toHashMap(paramInfo);
		PageInfo page = new PageInfo();
		page.fromMap(queryMap);
		page.setTenant((String) request.getSession().getAttribute(
				Constants.SESSION_TENANT_KEY));
		List pageInfoLst = sqlSessionTemplate.selectList("PageManage.query",
				page);
		if (pageInfoLst.size() > 0) {
			JSONObject pageInfo = (JSONObject) pageInfoLst.get(0);
			returnInfo.put("detail", pageInfo);
		}
		return returnInfo;
	}

}
