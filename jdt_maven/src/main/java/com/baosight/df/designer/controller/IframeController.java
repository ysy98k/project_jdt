package com.baosight.df.designer.controller;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.df.metamanage.entity.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/df/designer/iframe.do")
public class IframeController {
	@Autowired
	private HttpServletRequest request;

	private SqlSessionTemplate sqlSessionTemplate;

	@RequestMapping(params = "method=queryPage", method = RequestMethod.POST)
	public @ResponseBody
	JSONObject queryPage(String ajaxParam) {
		JSONObject returnInfo = new JSONObject();
		PageInfo pageQuery = JSONObject.parseObject(ajaxParam, PageInfo.class);
		if (!StringUtils.isBlank(pageQuery.getPageEname())) {
			pageQuery.setTenant((String) request.getSession().getAttribute(
					Constants.SESSION_TENANT_KEY));
			List<JSONObject> resultArr = sqlSessionTemplate.selectList(
					"PageManage.queryPrecise", pageQuery);
			if (resultArr.size() > 0) {
				returnInfo = resultArr.get(0);
			}
		}
		return returnInfo;
	}

	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}

	@Resource
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
}
