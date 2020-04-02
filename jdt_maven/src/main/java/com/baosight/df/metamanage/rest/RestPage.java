package com.baosight.df.metamanage.rest;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.df.metamanage.entity.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 查询页面的rest服务
 * 
 * @author yetianqi
 */
@Component
@Path("/page")
public class RestPage {

	private static final Logger logger = LoggerFactory
			.getLogger(RestPage.class);

	@Context
	HttpServletRequest request;
	@Context
	HttpServletResponse response;

	private SqlSessionTemplate sqlSessionTemplate;

	@GET
	@Path("/getPath")
	@Produces(MediaType.APPLICATION_JSON)
	public String getPath(@QueryParam("pageName") String pageName,@QueryParam("tenant") String tenant) {
		JSONObject returnObj = new JSONObject();
		PageInfo queryInfo = new PageInfo();
		queryInfo.setTenant(tenant);
		if(StringUtils.isBlank(pageName)){
			returnObj.put("status",Constants.EXECUTE_FAIL);
			return returnObj.toJSONString();
		}
		queryInfo.setPageEname(pageName);
		List<JSONObject> resultArr = sqlSessionTemplate.selectList("PageManage.queryPrecise", queryInfo);
		if(resultArr.size()>0){
			JSONObject pageResult = resultArr.get(0);
			returnObj.put("pagePath",pageResult.getString("pagePath"));
			returnObj.put("status",Constants.EXECUTE_SUCCESS);
		}else{
			returnObj.put("status",Constants.EXECUTE_FAIL);
		}
		return returnObj.toJSONString();
	}


	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}

	@Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
}
