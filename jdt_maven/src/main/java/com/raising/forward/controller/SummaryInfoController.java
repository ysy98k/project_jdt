package com.raising.forward.controller;

import com.alibaba.fastjson.JSONObject;
import com.raising.backstage.controller.SectionManageController;
import com.raising.forward.service.SummaryInfoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.ArrayList;
import java.util.List;

@Controller("SummaryInfoController")
@RequestMapping("/raising/forward/summaryInfo")
public class SummaryInfoController {
    private static final Logger logger = LoggerFactory
            .getLogger(SectionManageController.class);
    @Autowired
    private SummaryInfoService summaryInfoService;

    @RequestMapping(value = "/getAllLastPointsData.do", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getAllLastPointsData(@RequestParam("selected_id") int selectedid) {
        JSONObject returnInfo;
        logger.info("in getAllLastPointsData");
        returnInfo = summaryInfoService.getAllLastPointsData(selectedid);
        return returnInfo;
    }


    @RequestMapping(value = "/getPointsData.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getPointsData(@RequestParam("points") List<String> points, @RequestParam("selected_id") int selectedid, @RequestParam("column") String column, @RequestParam("sheetName") String sheetName) {
        JSONObject returnInfo;
        logger.info("in getPointData");
        returnInfo = summaryInfoService.getPointsData(selectedid, points, column);
        return returnInfo;
    }

}
