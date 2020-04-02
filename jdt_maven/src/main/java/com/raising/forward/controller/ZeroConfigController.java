package com.raising.forward.controller;

import com.alibaba.fastjson.JSONObject;
import com.raising.forward.service.d.ZeroConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/raising/forward")
public class ZeroConfigController {

    @Autowired
    private ZeroConfigService zeroConfigService;

    @RequestMapping(value = "zeroInfo.do")
    public ModelAndView frame(ModelMap map) {
        JSONObject returnInfo = new JSONObject();
        returnInfo = zeroConfigService.queryJson();
        map.addAttribute("zeroInfo", returnInfo);
        return new ModelAndView("/raising/forward/ZeroInfo");
    }
}
