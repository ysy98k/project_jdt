package com.raising.forward.controller;

import com.alibaba.fastjson.JSONObject;
import com.raising.backstage.controller.SectionManageController;
import com.raising.forward.service.SurveyInfoService;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;


@Controller("SurveyInfoController")
@RequestMapping("/raising/forward/surveyInfo")
public class SurveyInfoController {

    private static final Logger logger = LoggerFactory
            .getLogger(SectionManageController.class);

    @Autowired
    private SurveyInfoService surveyInfoService;

    @RequestMapping(value = "/fileUpload.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject fileUpload(@RequestParam("file") MultipartFile multipartfile, @RequestParam("selected_id") int selectedid) {
        System.out.println("selected id" + selectedid);
        JSONObject returnInfo;
        System.out.println("in file upload");
        File file = null;
        if (!multipartfile.isEmpty()) {
            CommonsMultipartFile commonsmultipartfile = (CommonsMultipartFile) multipartfile;
            DiskFileItem diskFileItem = (DiskFileItem) commonsmultipartfile.getFileItem();
            file = diskFileItem.getStoreLocation();
            System.out.println("转换之后的文件：" + file);
        }
        returnInfo = surveyInfoService.saveAndReportData(file, selectedid);
        return returnInfo;
    }

    @RequestMapping(value = "/getLastData.do", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getLastData(@RequestParam("selected_id") int selectedid) {
        System.out.println("in get last data");
        JSONObject returnInfo;
        returnInfo = surveyInfoService.getData(selectedid, null);
        return returnInfo;
    }

    @RequestMapping(value = "/getReportData.do", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getReportData(@RequestParam("selected_id") int selectedid, @RequestParam("reportTime") String reportTime) {
        System.out.println("in getReportData");
        JSONObject returnInfo;
        returnInfo = surveyInfoService.getData(selectedid, reportTime);
        return returnInfo;
    }

    @RequestMapping(value = "/downloadTemp.do", method = RequestMethod.GET)
    @ResponseBody
    public void downloadTemp(HttpServletResponse responses) {
        logger.info("download Template");
        surveyInfoService.downloadTemp(responses);
    }

}
