package com.baosight.df.metamanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.df.metamanage.service.MediaManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by lusongkai on 2017/12/6.
 */
@Controller
@RequestMapping("/df/metamanage/mediaManage.do")
public class MediaManageController {

    private static final Logger logger = LoggerFactory.getLogger(MediaManageController.class);

    @Autowired
    private MediaManageService mediaManageService;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "")
    public ModelAndView init(HttpServletRequest request,
                             HttpServletResponse response) {
        return new ModelAndView("/df/metamanage/mediamanage");
    }

    @RequestMapping(params = "method=query", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject query(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = mediaManageService.query(ajaxParamObj);
        return returnInfo;
    }

    @RequestMapping(params = "method=insert", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject insert(String ajaxParam){
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = mediaManageService.insert(ajaxParamObj);
        return returnInfo;
    }

    @RequestMapping(params = "method=upload", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject upload(@RequestParam(value = "mediaResources", required = false) MultipartFile file){
        JSONObject mapIn = new JSONObject();
        mapIn.put("type", request.getParameter("type"));
        mapIn.put("id", request.getParameter("id"));
        mapIn.put("name", request.getParameter("name"));
        JSONObject returnObj = new JSONObject();
        try {
            returnObj = mediaManageService.upload(file,mapIn);
        } catch (Exception e) {
            e.printStackTrace();
            returnObj.put("returnMsg", "上传失败，请修改后重新操作！");
            returnObj.put("status", Constants.EXECUTE_FAIL);
        }
        return returnObj;
    }

    @RequestMapping(params = "method=delete", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject delete(String ajaxParam){
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = mediaManageService.delete(ajaxParamObj);
        return returnInfo;
    }

    @RequestMapping(params = "method=update", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject update(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = mediaManageService.update(ajaxParamObj);
        return returnInfo;
    }


}
