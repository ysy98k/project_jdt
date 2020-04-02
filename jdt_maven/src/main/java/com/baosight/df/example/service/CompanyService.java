package com.baosight.df.example.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.JsonUtils;
import com.baosight.df.example.entity.CompanyInfo;

@Service
public class CompanyService extends BaseService {
	@Autowired
	private HttpServletRequest request;

	/**
	 * 查询
	 * 
	 * @param paramInfo
	 * @return 输出JSONObject
	 */

	public JSONObject query(JSONObject paramInfo) {
		JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
		paramInfo.put("inqu_status", inquStatus);
		paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
		paramInfo.put(BaseService.QUERY_SQL, "CompanyInfo.query");
		paramInfo.put(BaseService.COUNT_SQL, "CompanyInfo.count");
		paramInfo.put(BaseService.DAO_ENTITY,
				"com.baosight.df.example.entity.CompanyInfo");
		JSONObject returnInfo = super.query(paramInfo);
		return returnInfo;
	}

	/**
	 * 插入
	 * 
	 * @param paramInfo
	 * @return 输出JSONObject
	 */

	public JSONObject insert(JSONObject paramInfo) {
		paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
		JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
		paramInfo.put("inqu_status", inquStatus);
		paramInfo.put(BaseService.INSERT_SQL, "CompanyInfo.insert");
		paramInfo.put(BaseService.RESULT_BLOCK, "detail");
		paramInfo.put(BaseService.DAO_ENTITY,
				"com.baosight.df.example.entity.CompanyInfo");
		JSONObject returnInfo = super.insert(paramInfo);
		return returnInfo;
	}

	/**
	 * 删除
	 * 
	 * @param paramInfo
	 * @return 输出JSONObject
	 */

	public JSONObject delete(JSONObject paramInfo) {
		paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
		JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
		paramInfo.put("inqu_status", inquStatus);
		paramInfo.put(BaseService.DELETE_SQL, "CompanyInfo.delete");
		paramInfo.put(BaseService.RESULT_BLOCK, "result");
		paramInfo.put(BaseService.DAO_ENTITY,
				"com.baosight.df.example.entity.CompanyInfo");
		JSONObject returnInfo = super.delete(paramInfo);
		return returnInfo;
	}

	/**
	 * 更新
	 * 
	 * @param paramInfo
	 * @return 输出JSONObject
	 */

	public JSONObject update(JSONObject paramInfo) {
		paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
		JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
		paramInfo.put("inqu_status", inquStatus);
		paramInfo.put(BaseService.UPDATE_SQL, "CompanyInfo.update");
		paramInfo.put(BaseService.RESULT_BLOCK, "detail");
		paramInfo.put(BaseService.DAO_ENTITY,
				"com.baosight.df.example.entity.CompanyInfo");
		JSONObject returnInfo = super.update(paramInfo);
		return returnInfo;
	}

	/**
	 * 查询单条记录
	 * 
	 * @param paramInfo
	 * @return 输出JSONObject
	 */
	public JSONObject queryOne(JSONObject paramInfo) {
		JSONObject returnInfo = new JSONObject();
		HashMap queryMap = JsonUtils.toHashMap(paramInfo);
		CompanyInfo company = new CompanyInfo();
		company.fromMap(queryMap);
		company.setTenant((String) request.getSession().getAttribute(
				Constants.SESSION_TENANT_KEY));
		List pageInfoLst = sqlSessionTemplate.selectList("CompanyInfo.query",
				company);
		if (pageInfoLst.size() > 0) {
			JSONObject companyInfo = (JSONObject) pageInfoLst.get(0);
			returnInfo.put("detail", companyInfo);
		}
		return returnInfo;
	}
}
