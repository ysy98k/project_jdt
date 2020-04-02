package com.baosight.df.designer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/df/designer/designer.do")
public class DesignerController {

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView init(HttpServletRequest request,
			HttpServletResponse response) {
		return new ModelAndView("/df/designer/designer");
	}

    @RequestMapping(params = "method=query", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject query(String ajaxParam) {
        JSONObject returnInfo = null;
        return returnInfo;
    }

}
