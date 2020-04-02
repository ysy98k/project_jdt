package com.baosight.df.license.controller;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.dao.RestDao;
import com.baosight.df.license.service.LicenseService;
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

/**
 * Created by xutingting on 2017/6/1.
 */
@Controller
@RequestMapping("/df/license/license.do")
public class LicenseController {
    private String api = "/api/license";

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RestDao dao;

    @Value("${aas.host}")
    private String serviceAddress;

    @Value("${aas.rest_service_name}")
    private String appPath;

    @Autowired
    LicenseService LicenseService;

    @RequestMapping(value = "")
    public ModelAndView init(HttpServletRequest request,
                             HttpServletResponse response) {
        dao.setHost(serviceAddress);
        dao.setServiceName(appPath);

        return new ModelAndView("/df/license/license");
    }

    @RequestMapping(params = "method=getLicense", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject queryRestCommon() {
        HttpSession session = this.request.getSession();
        String token = session.getAttribute("token").toString();

        JSONObject mapIn = new JSONObject();
        mapIn.put("token", token);
        JSONObject returnInfo = dao.invoke("get", api, mapIn);
        if(returnInfo.containsKey("deployMode") && returnInfo.get("deployMode").equals("product")){
            returnInfo.putAll(LicenseService.queryLicense(mapIn));
        }
        return returnInfo;
    }

}
