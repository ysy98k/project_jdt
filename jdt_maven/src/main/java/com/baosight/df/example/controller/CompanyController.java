package com.baosight.df.example.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.baosight.df.example.service.CompanyService;

@Controller
@RequestMapping("/df/example/company.do")
public class CompanyController {

	@Autowired
	private CompanyService service;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView init(HttpServletRequest request,
			HttpServletResponse response) {
		return new ModelAndView("/df/example/company");
	}

	@RequestMapping(params = "method=query", method = RequestMethod.POST)
	public @ResponseBody
	JSONObject query(String ajaxParam) {
		JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
		JSONObject returnInfo = service.query(ajaxParamObj);
		return returnInfo;
	}

	@RequestMapping(params = "method=insert", method = RequestMethod.POST)
	public @ResponseBody
	JSONObject insert(String ajaxParam) {
		JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
		JSONObject returnInfo = service.insert(ajaxParamObj);
		return returnInfo;
	}

	@RequestMapping(params = "method=delete", method = RequestMethod.POST)
	public @ResponseBody
	JSONObject delete(String ajaxParam) {
		JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
		JSONObject returnInfo = service.delete(ajaxParamObj);
		return returnInfo;
	}

	@RequestMapping(params = "method=update", method = RequestMethod.POST)
	public @ResponseBody
	JSONObject update(String ajaxParam) {
		JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
		JSONObject returnInfo = service.update(ajaxParamObj);
		return returnInfo;
	}

	@RequestMapping(params = "method=queryOne", method = RequestMethod.POST)
	public @ResponseBody
	JSONObject queryOne(String ajaxParam) {
		JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
		JSONObject returnInfo = service.queryOne(ajaxParamObj);
		return returnInfo;
	}

}
