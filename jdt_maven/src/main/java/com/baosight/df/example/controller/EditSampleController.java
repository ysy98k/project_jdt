package com.baosight.df.example.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/df/example/editSample.do")
public class EditSampleController {
	
	@RequestMapping(value = "",method = RequestMethod.GET)
	public ModelAndView editSample(HttpServletRequest request,
			HttpServletResponse response) {
		return new ModelAndView("/df/example/editSample");
	}

}