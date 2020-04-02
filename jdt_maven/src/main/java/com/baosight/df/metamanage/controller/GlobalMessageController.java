package com.baosight.df.metamanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.baosight.df.metamanage.service.GlobalMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/df/metamanage/global.do")
public class GlobalMessageController {

    @Autowired
    private GlobalMessageService service;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView init(HttpServletRequest request,
                             HttpServletResponse response) {
        return new ModelAndView("/df/metamanage/global");
    }

    @RequestMapping(params = "method=query", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject query(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = service.query(ajaxParamObj);
        return returnInfo;
    }

    @RequestMapping(params = "method=querykey", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject querykey(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = service.querykey(ajaxParamObj);
        return returnInfo;
    }

    @RequestMapping(params = "method=querybatch", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject querybatch(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = service.querybatch(ajaxParamObj);
        return returnInfo;
    }

    @RequestMapping(params = "method=insert", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject insert(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = service.insert(ajaxParamObj);
        return returnInfo;
    }

    @RequestMapping(params = "method=delete", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject delete(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = service.delete(ajaxParamObj);
        return returnInfo;
    }

    @RequestMapping(params = "method=update", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject update(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = service.update(ajaxParamObj);
        return returnInfo;
    }

    @RequestMapping(params = "method=queryOne", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject queryOne(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = service.queryOne(ajaxParamObj);
        return returnInfo;
    }

}
