package com.baosight.df.metamanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.dao.RestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/df/metamanage/user.do")
public class UserController {

	private String api = "/api/user";

	private String apiCheckString = "/api/session";

	@Value("${aas.host}")
	private String host;

	@Value("${aas.rest_service_name}")
	private String serviceName;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private RestDao dao;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView init(HttpServletRequest request,
			HttpServletResponse response) {

		return new ModelAndView("/aas/page/UserManagement");
	}

	//  url :"http://10.25.10.12:8080/aas/api/user"
	@RequestMapping(params = "method=modifyPassword", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject modifyPassword(String ajaxParam) {
        dao.setHost(host);
        dao.setServiceName(serviceName);
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject mapIn = new JSONObject();
        HttpSession session = this.request.getSession();
        String token = session.getAttribute("token").toString();
        String username = ajaxParamObj.getString("username");

        mapIn.put("token", token);
        mapIn.put("password", ajaxParamObj.get("password"));
        JSONObject returnInfo = dao.invoke("put", api+"/"+username, mapIn);
		return returnInfo;
	}

	@RequestMapping(params = "method=check", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject check(String ajaxParam) {
		dao.setHost(host);
		dao.setServiceName(serviceName);
		JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
		JSONObject mapIn = new JSONObject();
		HttpSession session = this.request.getSession();
		String token = session.getAttribute("token").toString();

		mapIn.put("token", token);
		mapIn.put("tenant", ajaxParamObj.get("tenant"));
		mapIn.put("username", ajaxParamObj.get("username"));
		mapIn.put("password", ajaxParamObj.get("password"));
		JSONObject returnInfo = dao.invoke("post", apiCheckString, mapIn);
		return returnInfo;
	}
}