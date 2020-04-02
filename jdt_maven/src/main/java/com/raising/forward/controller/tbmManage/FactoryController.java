package com.raising.forward.controller.tbmManage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.common.BaseController;
import com.raising.forward.entity.tbmManage.TbmServiceInfo;

import com.raising.forward.service.PropertiesValue;
import com.raising.forward.service.tbmManage.FactoryService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.Date;

/**
 * 厂内发货
 */
@Controller("factoryController")
@RequestMapping("/raising/forward/tbmManage/factory")
public class FactoryController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(FactoryController.class);



    @Autowired
    private FactoryService factoryService;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/getFileTable.do",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getFileTable(String ajaxParam){
        JSONObject returnInfo = factoryService.getFileTable(ajaxParam);
        return returnInfo;
    }

    @RequestMapping(value = "/dataUpload.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject dataUpload(@RequestParam("file") MultipartFile file, String tbmName, TbmServiceInfo tbmServiceInfo){
        JSONObject returnInfo = new JSONObject();
        returnInfo.put("status", Constants.EXECUTE_SUCCESS);
        returnInfo.put("message", "文件上传成功");
        JSONObject param = new JSONObject();
        File tempFile =  new File(PropertiesValue.TEMP_FILE_PATH+"/"+tbmName+"/" +file.getOriginalFilename());
        if(!tempFile.getParentFile().exists()){
            tempFile.getParentFile().mkdirs();
        }
        try {
            tempFile.createNewFile();
            file.transferTo(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        param.put("type","factory");
        param.put("filePath",tempFile.getAbsolutePath());
        param.put("tbmName",tbmName);
        param.put("tbmServiceInfo",tbmServiceInfo);
        param.put("token",request.getSession().getAttribute("token"));
        redisTemplate.convertAndSend("tbmUploadFileChannel",param);

        return returnInfo;
    }

    @RequestMapping(value = "convertFileToPDF.do",method = RequestMethod.GET)
    public void convertFileToPdf(String ajaxParam){
        JSONObject ajaxJson = JSONObject.parseObject(ajaxParam);
        String tbmName = ajaxJson.getString("tbmName");
        //String factoryPath = FACTORY_FILE_PATH + tbmName + "/厂内交货");
        String factoryPath = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"factory"+File.separator+tbmName+File.separator+"厂内交货";
        String filePath = factoryPath + File.separator + "beingConverted.txt";
        File file = new File(filePath);//用来做转换标示
        File parentFile = file.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("1");
            writer.flush();
            writer.close();
            factoryService.convertFileToPdf(tbmName);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }finally {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write("0");
                writer.flush();
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value="/downloadFile.do",method = RequestMethod.GET)
    @ResponseBody
    public void download(String ajaxParam, HttpServletResponse response){
        String factoryType = "";
        JSONArray fileNames = null;
        String tbmName = "";
        try {
            String temp = URLDecoder.decode(ajaxParam, "UTF-8");
            JSONObject ajaxJson = JSONObject.parseObject(temp);
            tbmName = ajaxJson.getString("tbmName");
            fileNames = ajaxJson.getJSONArray("fileNames");
            factoryType = ajaxJson.getString("factoryType");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(fileNames == null || fileNames.size() < 1 || StringUtils.isNullOrEmpty(factoryType)|| StringUtils.isNullOrEmpty(tbmName) ){
            return;
        }
        try {
            factoryService.download(tbmName,fileNames,factoryType,response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @RequestMapping(value="deleteFile.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject deleteFile(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        returnInfo = factoryService.deleteFiles(ajaxParam);
        return returnInfo;
    }

    @RequestMapping(value = "checkPreview.do",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject checkPreview(String ajaxParam){
        JSONObject returnInfo = null;
        try {
            returnInfo = factoryService.checkPreview(ajaxParam);
        } catch (IOException e) {
            logger.error(e.getMessage());
            returnInfo = new JSONObject();
            returnInfo.put("status",Constants.EXECUTE_SUCCESS);//如果出现异常，则返回成功。预览时再次转换。
        }
        return returnInfo;
    }


    @RequestMapping(value = "preview.do",method = RequestMethod.GET)
    @ResponseBody
    public void previewFile(String ajaxParam,HttpServletResponse response){
        JSONObject returnInfo = new JSONObject();
        try {
            factoryService.previewFile(ajaxParam, response);
        } catch (IOException e) {
           logger.error( e.getMessage());
        }

    }

    @RequestMapping(value = "downloadTemplet.do",method = RequestMethod.GET)
    @ResponseBody
    public void downloadTemplet(HttpServletResponse response){
        try {
            factoryService.downloadTemplet(response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
