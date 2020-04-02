package com.raising.forward.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.common.controller.DownloadExcelController;
import com.baosight.common.utils.StringUtils;
import com.raising.forward.entity.LineData;
import com.raising.forward.service.guidance.LineDataService;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;


@Controller("lineDataController")
@RequestMapping("/raising/forward/lineData")
public class LineDataController extends DownloadExcelController {

    private static final Logger logger = LoggerFactory.getLogger(LineDataController.class);

    @Autowired
    private LineDataService lineDataService;

    @RequestMapping(value="/getTableData.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getTableData(String projectId){
        JSONObject returnInfo = new JSONObject();
        returnInfo = lineDataService.getTableData(projectId);
        return returnInfo;
    }

    @RequestMapping(value = "/getLineTable.do",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getLineTable(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        returnInfo = lineDataService.getLineTable(ajaxParam);
        return returnInfo;
    }


    @RequestMapping(value="/compare.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject compare(String ajaxParam){
        JSONObject returnInfo = lineDataService.compare(ajaxParam);
        return returnInfo;
    }
    /**
     * 蓝图上传
     * @param blueprint
     * @param info
     * @return
     */
    @RequestMapping(value="/blueprintUpload.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject blueprintUpload(@RequestParam("blueprint") MultipartFile blueprint, @RequestParam(required = false, defaultValue = "") String info,
                                       @RequestParam("projectName")String projectName){
        JSONObject returnInfo = new JSONObject();
        try {
            returnInfo = lineDataService.saveBlueprint(blueprint,info,projectName);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            returnInfo.put("status", Constants.EXECUTE_FAIL);
            returnInfo.put("message","项目上传失败！");
        }
        return returnInfo;
    }


    @RequestMapping(value="/lineUpload.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject lineUpload(@RequestParam("lineFile") MultipartFile lineFile, LineData lineData){
        JSONObject returnInfo = new JSONObject();
        try {
            returnInfo = lineDataService.saveLine(lineFile,lineData);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            returnInfo.put("status", Constants.EXECUTE_FAIL);
            returnInfo.put("message","项目上传失败");
        }
        return returnInfo;
    }

    @RequestMapping(value="/downloadFile.do",method = RequestMethod.GET)
    @ResponseBody
    public void download(HttpServletRequest request, HttpServletResponse response){
        String filePath = null;
        String fileName = request.getParameter("fileName");
        try {
            filePath = URLDecoder.decode(request.getParameter("filePath"), "UTF-8");
            fileName = URLDecoder.decode(request.getParameter("fileName"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(StringUtils.isNullOrEmpty(fileName) || StringUtils.isNullOrEmpty(filePath)){
            return;
        }
        try {
            lineDataService.download(fileName,filePath,response);
        } catch (IOException e) {

        }
    }

    @RequestMapping(value="/downloadLineInfo.do",method = RequestMethod.GET)
    @ResponseBody
    public void downloadLineInfo(HttpServletRequest request, HttpServletResponse response){
        Integer lineId = Integer.parseInt(request.getParameter("lineId"));

        if(lineId == null || lineId <1 ){
            return;
        }

        try {
            lineDataService.downloadLineInfo(lineId,response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value="/delete.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject delete(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        try {
            returnInfo = lineDataService.delete(ajaxParam);
        }catch (Exception e){
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("message","删除记录失败");
        }
        return returnInfo;
    }

    @RequestMapping(value="getDrawings.do",method = RequestMethod.GET)
    @ResponseBody
    public List<JSONObject> getDrawings(String projectId){
        return lineDataService.getDrawing(projectId);
    }

    @RequestMapping(value="review.do",method=RequestMethod.POST)
    @ResponseBody
    public String review(String ajaxParam){
        String review = lineDataService.review(ajaxParam);
        return review;
    }

    @RequestMapping(value="downloadCompareResult.do",method = RequestMethod.GET)
    @ResponseBody
    public void downloadCompareResult(String ajaxParam,HttpServletRequest request,HttpServletResponse response){
        try {
            //String ajaxParam = request.getParameter("ajaxParam");
            ajaxParam = URLDecoder.decode(ajaxParam, "UTF-8");
            JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
            Integer lineId = ajaxParamObj.getInteger("lineId");
            JSONArray result = lineDataService.downloadCompareResult(lineId);
            ajaxParamObj.put("downloadData", result);
            download(request, response, ajaxParamObj.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
    }

}
