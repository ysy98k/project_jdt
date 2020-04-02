package com.raising.forward.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.controller.DownloadExcelController;
import com.raising.forward.service.d.DMeasureResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

/**
 * 测量结果
 */
@Controller
@RequestMapping("/raising/forward/measureResult.do")
public class MeasureResultController extends DownloadExcelController {
    private static final Logger logger = LoggerFactory.getLogger(MeasureResultController.class);

    @Autowired
    private DMeasureResultService dMeasureResultService;

    @RequestMapping(params = "method=query",method = RequestMethod.POST)
    public @ResponseBody
    JSONObject queryMeasureResult(String ajaxParam){
        JSONObject jsonObject = JSON.parseObject(ajaxParam);
        JSONObject result = dMeasureResultService.queryMeasureResult(jsonObject);
        return result;
    }

    @RequestMapping(value="downloadExcel.do",method = RequestMethod.GET)
    @ResponseBody
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response){
        try {
            String ajaxParam = request.getParameter("ajaxParam");
            ajaxParam = URLDecoder.decode(ajaxParam, "UTF-8");
            JSONObject queryResult = queryMeasureResult(ajaxParam);
            JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
            ajaxParamObj.put("downloadData", queryResult.getJSONArray("rows"));
            download(request, response, ajaxParamObj.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
    }
}
