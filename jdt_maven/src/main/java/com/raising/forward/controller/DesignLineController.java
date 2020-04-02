package com.raising.forward.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.controller.DownloadExcelController;
import com.raising.forward.service.d.DesignLineService;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

/**
 * @author ysy
 * @date 2018/2/7 13:31
 * @description
 */
@Controller
@RequestMapping("/raising/forward/designLine.do")
public class DesignLineController extends DownloadExcelController {
    private static final Logger logger = LoggerFactory.getLogger(DesignLineController.class);

    @Value("${designer.delivery_pkg}")
    private String delivery_pkg;

    @Autowired
    protected SqlSessionTemplate sqlSessionTemplate;
    @Autowired
    private DesignLineService service;


    @Value("${aas.host}")
    private String aasAddress;

    @Value("${aas.rest_service_name}")
    private String aasAppPath;

    @Value("${service.name}")
    private String serviceName;

    @RequestMapping(params = "method=query", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject query(String ajaxParam) {


        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);


        JSONObject returnInfo = service.query(ajaxParamObj);
        return returnInfo;
    }

    @RequestMapping(value="downloadExcel.do",method = RequestMethod.GET)
    @ResponseBody
    public void downloadExcel(HttpServletRequest request,HttpServletResponse response){
        try {
            String ajaxParam = request.getParameter("ajaxParam");
            ajaxParam = URLDecoder.decode(ajaxParam, "UTF-8");
            Cookie[] cookies = request.getCookies();
            Integer projectId  = null;
            for (Cookie c : cookies) {
                if (c.getName().equals("selected_id")) {
                    projectId = Integer.parseInt(c.getValue());
                }
            }
            JSONArray result = service.queryAllById(projectId,request.getSession().getAttribute("tenant").toString());
            JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
            ajaxParamObj.put("downloadData", result);
            download(request, response, ajaxParamObj.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
    }
}
