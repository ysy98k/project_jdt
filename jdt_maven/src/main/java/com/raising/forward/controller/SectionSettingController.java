package com.raising.forward.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.dao.RestDao;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.baosight.df.metamanage.service.FrameSettingService;
import com.common.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/raising/forward/sectionSetting.do")
public class SectionSettingController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(SectionSettingController.class);

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

        return new ModelAndView("/raising/forward/sectionSetting");
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

        if (returnInfo.getString("skinName") != null) {
            request.getSession().setAttribute("skinName", returnInfo.getString("skinName"));
        } else {
            request.getSession().setAttribute("skinName", "blue-skin");
            service.insertFrameSetting(ajaxParamObj);
        }
        returnInfo.put("pagePath", "/raising/forward/sectionSetting.do");
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

        if (returnInfo.getString("skinName") != null) {
            request.getSession().setAttribute("skinName", returnInfo.getString("skinName"));
        } else {
            request.getSession().setAttribute("skinName", "blue-skin");
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

    @RequestMapping(params = "method=getCvsUrl",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getCvsUrl(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject projectJson = projectService.getProject(ajaxParam);
        JSONArray rows = projectJson.getJSONArray("rows");
        if(rows == null || rows.size() < 0){
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("message","查询失败，找不到项目");
            return returnInfo;
        }
        JSONObject jsonObject = rows.getJSONObject(0);
        String templateName = jsonObject.getString("templateName");
        if(StringUtils.isNullOrEmpty(templateName)){
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("message","模板名为空");
            return returnInfo;
        }
        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        returnInfo.put("templateName",templateName);
        return returnInfo;
    }
}
