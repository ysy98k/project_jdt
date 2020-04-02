package com.baosight.df.example.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.controller.DownloadExcelController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created by ruiye on 2017/12/7.
 */
@Controller
@RequestMapping("/df/example/bxgridDemo.do")
public class bxgridDemoController extends DownloadExcelController {
    private static final Logger logger = LoggerFactory.getLogger(bxgridDemoController.class);
    @Autowired
    private HttpServletRequest request;

    @RequestMapping(params = "method=query", method = RequestMethod.POST)
    public JSONObject query(String ajaxParam) {
        JSONObject returnInfo = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject row1 = new JSONObject();
        row1.put("key1","香蕉");
        row1.put("key2","https://www.baidu.com/img/bd_logo1.png");
        JSONObject row2 = new JSONObject();
        row2.put("key1","苹果");
        row2.put("key2","http://www.51hlife.com/images/b2b/hsh_logo.png");
        JSONObject row3 = new JSONObject();
        row3.put("key1","橘子");
        row3.put("key2","http://technet.ccdomain.com/drupal/themes/garland/logo.png");
        array.add(row1);
        array.add(row2);
        array.add(row3);
        returnInfo.put("rows", array);
        returnInfo.put("status",0);
        returnInfo.put("total",3);
        returnInfo.put("records",3);
        returnInfo.put("returnMsg","查询成功，本次返回3条记录！");
        return returnInfo;
    }
    @RequestMapping(value = "download.do", method = RequestMethod.GET)
    public String downloadExcel(HttpServletRequest request,
                                HttpServletResponse response) throws IOException {

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
        return null;
    }


}
