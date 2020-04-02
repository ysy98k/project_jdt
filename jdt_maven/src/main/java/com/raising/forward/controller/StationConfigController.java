package com.raising.forward.controller;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.controller.DownloadExcelController;
import com.raising.forward.service.d.StationConfigService;
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
import java.text.ParseException;

/**
 * @author ysy
 * @date 2018/6/1 13:32
 * @description
 */
@Controller
@RequestMapping("/raising/forward/stationConfig.do")
public class StationConfigController extends DownloadExcelController {
    private static final Logger logger = LoggerFactory.getLogger(StationConfigController.class);

    @Autowired
    private StationConfigService stationConfigService;

    /**
     * 吊篮信息查询。
     * 分页显示
     * 会传当前页，和页码参数
     * @param ajaxParam
     * @return
     */
    @RequestMapping(params = "method=query", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject query(String ajaxParam){
        JSONObject paramJson = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = null;
        try {
            returnInfo = stationConfigService.query(paramJson);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return returnInfo;

    }

    @RequestMapping(value="downloadExcel.do",method = RequestMethod.GET)
    @ResponseBody
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response){
        try {
            String ajaxParam = request.getParameter("ajaxParam");
            ajaxParam = URLDecoder.decode(ajaxParam, "UTF-8");
            JSONObject queryResult = query(ajaxParam);
            JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
            ajaxParamObj.put("downloadData", queryResult.getJSONArray("rows"));
            download(request, response, ajaxParamObj.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
    }





}
