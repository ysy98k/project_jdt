package com.raising.forward.controller;

import com.alibaba.fastjson.JSONObject;
import com.raising.backstage.controller.SectionManageController;
import com.raising.forward.service.SurveyGraphService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;


@Controller("SurveyGraphController")
@RequestMapping("/raising/forward/surveyGraph")
public class SurveyGraphController {
    private static final Logger logger = LoggerFactory.getLogger(SectionManageController.class);

    @Autowired
    private SurveyGraphService surveyGraphService;

    @RequestMapping(value = "/fileUpload.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject fileUpload(@RequestParam("file") MultipartFile multipartfile, @RequestParam("selected_id") int selectedid) {
        logger.info("selected id" + selectedid);
        JSONObject returnInfo = null;
        logger.info("in file upload");
        logger.info(multipartfile.getName());
        if (!multipartfile.isEmpty()) {
            returnInfo = surveyGraphService.uploadxls(multipartfile, selectedid);
        }
        return returnInfo;
    }

    @RequestMapping(value = "/lastPDF.do", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getLastPDF(@RequestParam("selected_id") int selectedid) {
        logger.info("selected id" + selectedid);
        logger.info("in file upload");
        JSONObject returnInfo = surveyGraphService.getProjectLastPDF(selectedid);
        return returnInfo;
    }

    @RequestMapping(value = "/getPDF.do", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getPDF(@RequestParam("selected_id") int selectedid, @RequestParam("filename") String fileName) {
        try {
            fileName = java.net.URLDecoder.decode(fileName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.info("decode error: " + e);
        }
        JSONObject returnInfo = surveyGraphService.getProjectPDF(selectedid, fileName);
        return returnInfo;
    }

    @RequestMapping(value = "/deleteFiles.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject deleteFiles(@RequestParam("selected_id") int selectedid, @RequestParam("fileNames") List<String> fileNames) {
        logger.info("in Delete Files");
        JSONObject returnInfo = surveyGraphService.deleteFiles(selectedid, fileNames);
        return returnInfo;
    }


    @RequestMapping(value = "/preShow.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject preShow(@RequestParam("path") String path, HttpServletRequest request) {
        logger.info("preShow pdf");
        JSONObject returnInfo = new JSONObject();

        try {
            StringBuffer serverPath = request.getRequestURL();
            int start = serverPath.indexOf("preShow.do");
            serverPath.replace(start, serverPath.length(), "showPDF.do");
            serverPath.append("?path=" + path);
            logger.info("预览地址：" + serverPath.toString());
            String urlPath = URLEncoder.encode(serverPath.toString(), "utf-8");
            returnInfo.put("data", urlPath);
        } catch (Exception e) {
            returnInfo.put("msg", "path error");
            e.printStackTrace();
        }
        return returnInfo;
    }


    @RequestMapping(value = "/showPDF.do", method = RequestMethod.GET)
    @ResponseBody
    public void showPDF(@RequestParam("path") String path, HttpServletResponse responses) {
        try {
            path = java.net.URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.info("decode error: " + e);
        }
        logger.info("in show pdf files: path: " + path);
        surveyGraphService.showPDF(path, responses);
    }
}
