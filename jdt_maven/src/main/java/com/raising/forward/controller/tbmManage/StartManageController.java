package com.raising.forward.controller.tbmManage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.common.BaseController;
import com.raising.forward.entity.tbmManage.TbmServiceInfo;
import com.raising.forward.service.CodeItemService;
import com.raising.forward.service.PropertiesValue;
import com.raising.forward.service.tbmManage.StartManageService;
import com.util.CSVUtils;
import com.util.MultipleDataSource;
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
import java.io.*;

/**
 * 始发管理
 */
@Controller("startManageController")
@RequestMapping("/raising/forward/tbmManage/startManage")
public class StartManageController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(StartManageController.class);




    @Autowired
    private StartManageService service;

    @Autowired
    private CodeItemService codeItemService;

    @RequestMapping(value="/getSecondFileTable.do",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getSecondFileTable(String ajaxParam){
        JSONObject returnInfo = service.getSecondFileTable(ajaxParam);
        return returnInfo;
    }

    @RequestMapping(value = "/getFileTable.do",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getFileTable(String ajaxParam){
        JSONObject returnInfo = service.getFileTable(ajaxParam);
        return returnInfo;
    }

    @RequestMapping(value = "/dataUpload.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject dataUpload(@RequestParam("file") MultipartFile file,String tbmName,String directory,String secondLevel,TbmServiceInfo tbmServiceInfo){
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

        param.put("type","start");
        param.put("filePath",tempFile.getAbsolutePath());
        param.put("tbmName",tbmName);
        param.put("directory",directory);
        param.put("tbmServiceInfo",tbmServiceInfo);
        param.put("secondLevel",secondLevel);
        param.put("token",request.getSession().getAttribute("token"));
        redisTemplate.convertAndSend("tbmUploadFileChannel",param);

        return returnInfo;

    }

    @RequestMapping(value="/downloadFile.do",method = RequestMethod.GET)
    @ResponseBody
    public void download(String ajaxParam, HttpServletResponse response){
        try {
            service.download(ajaxParam,response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


    @RequestMapping(value="deleteFile.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject deleteFile(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        returnInfo = service.deleteFiles(ajaxParam);
        return returnInfo;
    }

    @RequestMapping(value="getTree.do",method = RequestMethod.GET)
    @ResponseBody
    public JSONArray getLineTree(String ajaxParam, HttpServletRequest request) {
        JSONObject ajaxParamObj = new JSONObject();

        ajaxParamObj.put("fdParentId", request.getParameter("fdParentId"));
        ajaxParamObj.put("treeparentId", request.getParameter("treeparentId"));
        ajaxParamObj.put("tbmName", request.getParameter("tbmName"));

        JSONArray returnInfo;
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            returnInfo = codeItemService.getStartManageSubTree(ajaxParamObj);
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        if (ajaxParamObj.get("treeparentId").equals("0-#")) {//打开的节点是始发管理节点，则读取文件夹。得到始发管理下的子节点
            returnInfo = service.getTree(ajaxParamObj.getString("tbmName"),returnInfo);

        }
        return returnInfo;
    }

    @RequestMapping(value = "downloadTemplet.do",method = RequestMethod.GET)
    @ResponseBody
    public void downloadTemplet(HttpServletResponse response){
        try {
            service.downloadTemplet(response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @RequestMapping(value = "convertFileToPDF.do",method = RequestMethod.GET)
    public void convertFileToPdf(String ajaxParam){
        JSONObject ajaxJson = JSONObject.parseObject(ajaxParam);
        String tbmName = ajaxJson.getString("tbmName");
        String secondLevel = ajaxJson.getString("secondLevel");

        String secondPath = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"startManage" + File.separator + tbmName + File.separator +secondLevel;
        String filePath = secondPath + File.separator + "beingConverted.txt";
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
            service.convertFileToPdf(tbmName,secondLevel);
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

    @RequestMapping(value = "checkPreview.do",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject checkPreview(String ajaxParam){
        JSONObject returnInfo = null;
        try {
            returnInfo = service.checkPreview(ajaxParam);
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
            service.previewFile(ajaxParam, response);
        } catch (IOException e) {
            logger.error( e.getMessage());
        }

    }
}
