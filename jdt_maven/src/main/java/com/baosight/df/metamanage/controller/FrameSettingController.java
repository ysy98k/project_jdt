package com.baosight.df.metamanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.dao.RestDao;
import com.baosight.df.metamanage.service.FrameSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/df/metamanage/frameSetting.do")
public class FrameSettingController {

	@Autowired
	private FrameSettingService service;
	@Autowired
	private HttpServletRequest request;

    @Autowired
    private RestDao dao;

    @Value("${aas.host}")
    private String host;

    @Value("${aas.rest_service_name}")
    private String serviceName;

	@RequestMapping(value = "")
	public ModelAndView frame_manage(HttpServletRequest request,
			HttpServletResponse response) {

		return new ModelAndView("/df/metamanage/frameSetting");
	}

	@RequestMapping(params = "method=updateFrameSetting", method = RequestMethod.POST)
	public @ResponseBody
	JSONObject updateFrameSetting(String ajaxParam) {
		JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
		JSONObject returnInfo = service.updateFrameSetting(ajaxParamObj);
        request.getSession().setAttribute("skinName", ajaxParamObj.getString("skinName"));
		return returnInfo;
	}
	

	@RequestMapping(params = "method=queryFrame", method = RequestMethod.POST)
	public @ResponseBody
	JSONObject queryFrame(String ajaxParam) {
        dao.setHost(host);
        dao.setServiceName(serviceName);
        service.dao = dao;

		JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
		JSONObject returnInfo = service.queryFrame(ajaxParamObj);

		if(returnInfo.getString("skinName")!=null){
            request.getSession().setAttribute("skinName", returnInfo.getString("skinName"));
        }else{
            request.getSession().setAttribute("skinName","black-skin");
            service.insertFrameSetting(ajaxParamObj);
        }
		return returnInfo;
	}

    @RequestMapping(params = "method=queryFrontFrame", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject queryFrontFrame(String ajaxParam) {
        dao.setHost(host);
        dao.setServiceName(serviceName);
        service.dao = dao;
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = service.queryFrontFrame(ajaxParamObj);

        if(returnInfo.getString("skinName")!=null){
            request.getSession().setAttribute("skinName", returnInfo.getString("skinName"));
        }else{
            request.getSession().setAttribute("skinName","black-skin");
            service.insertFrameSetting(ajaxParamObj);
        }
        return returnInfo;
    }

    @RequestMapping(params = "method=queryFrameSetting", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject queryFrameSetting(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = service.queryFrameSetting(ajaxParamObj);
        return returnInfo;
    }

    @RequestMapping(value="/queryLogo.do",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject queryLogo(){
        JSONObject returnInfo = service.getLogo();
        return returnInfo;
    }
}
