package com.raising.forward.controller.tbmManage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.common.BaseController;
import com.raising.forward.service.CodeItemService;

import com.raising.forward.service.PropertiesValue;
import com.raising.forward.service.tbmManage.FaultTreatService;
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

import javax.servlet.http.HttpServletResponse;
import java.io.*;

import java.util.List;
import java.util.Map;


/**
 * 故障处理
 */
@Controller("faultTreatController")
@RequestMapping("/raising/forward/tbmManage/faultTreat")
public class FaultTreatController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(FaultTreatController.class);

    @Autowired
    private FaultTreatService faultTreatService;

    @Autowired
    private CodeItemService codeItemService;


    @RequestMapping(value = "query.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject faultTreatQuery(String ajaxParam){

        JSONObject returnInfo = faultTreatService.query(ajaxParam);
        if(returnInfo == null){
            return null;
        }
        JSONArray detailsArray = (JSONArray)returnInfo.get("rows");
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            if(detailsArray.size() < 1){
                return returnInfo;
            }
            /*for(int i = 0;i<detailsArray.size();i++){
                JSONObject tempObj = detailsArray.getJSONObject(i);
                String problemtype = codeItemService.getCodeNameFromSystemAndItem(tempObj.getString("processMode"));
                tempObj.put("processMode",problemtype);
            }*/
            JSONObject one = detailsArray.getJSONObject(0);
            Map<String,String> processMode = codeItemService.getCodeNameFromSystemCode(one.getString("processMode"));
            for(int i = 0;i<detailsArray.size();i++){
                JSONObject tempObj = detailsArray.getJSONObject(i);
                String problemtype = processMode.get(tempObj.getString("tbmCCSId"));
                tempObj.put("processMode",problemtype);
            }
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return  returnInfo;
    }

    @RequestMapping(value = "insert.do",method = RequestMethod.POST)
    public @ResponseBody
    JSONObject insert(String ajaxParam){

        JSONObject returnInfo = null;
        try {
            returnInfo = faultTreatService.insert(ajaxParam);
        } catch (Exception e) {
            returnInfo = new JSONObject();
            returnInfo.put("status", Constants.EXECUTE_FAIL);
            returnInfo.put("returnMsg",e.getMessage());
            logger.error(e.getMessage());
        }
        return returnInfo;
    }

    @RequestMapping(value = "update.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject update(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        try {
            returnInfo = faultTreatService.update(ajaxParam);
        } catch (Exception e) {
            returnInfo.put("status", Constants.EXECUTE_FAIL);
            returnInfo.put("returnMsg",e.getMessage());
            logger.error(e.getMessage());
        }
        return returnInfo;
    }

    @RequestMapping(value = "delete.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject delete(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        try {
            returnInfo = faultTreatService.delete(ajaxParam);
        }catch (Exception e) {
            logger.error(e.getMessage());
            returnInfo.put("status", Constants.EXECUTE_FAIL);
            returnInfo.put("returnMsg","删除记录失败！");
            return  returnInfo;
        }
        return returnInfo;
    }

    @RequestMapping(value = "queryFile.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject queryFile(String ajaxParam){
        JSONObject returnInfo = faultTreatService.queryFile(ajaxParam);
        return returnInfo;
    }

    @RequestMapping(value = "/dataUpload.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject dataUpload(@RequestParam("Filedata") MultipartFile file, String tbmName,String faultId){
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

        param.put("type","fault");
        param.put("filePath",tempFile.getAbsolutePath());
        param.put("tbmName",tbmName);
        param.put("faultId",faultId);
        redisTemplate.convertAndSend("tbmUploadFileChannel",param);

        return returnInfo;
    }

    @RequestMapping(value="/downloadFile.do",method = RequestMethod.GET)
    @ResponseBody
    public void download(String ajaxParam, HttpServletResponse response){
        try {
            faultTreatService.download(ajaxParam,response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @RequestMapping(value="deleteFile.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject deleteFile(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        returnInfo = faultTreatService.deleteFile(ajaxParam);
        return returnInfo;
    }




    @RequestMapping(value="getProblemType.do",method = RequestMethod.GET)
    @ResponseBody
    public List<JSONObject> getProblemType(){
        String problemType = faultTreatService.getProblemType();
        if(StringUtils.isNullOrEmpty(problemType)){
            return null;
        }
        List<JSONObject> problemTypeList = null;
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            String systemCode = problemType.substring(0,problemType.indexOf("."));
            problemTypeList = codeItemService.getCodeListFromSystemCode(systemCode);
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return  problemTypeList;
    }

    @RequestMapping(value = "convertFileToPDF.do",method = RequestMethod.GET)
    public void convertFileToPdf(String ajaxParam){
        JSONObject ajaxJson = JSONObject.parseObject(ajaxParam);
        String tbmName = ajaxJson.getString("tbmName");
        String faultId = ajaxJson.getString("faultId");
        String pdfPath = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"faultTreat"+File.separator+tbmName+File.separator+faultId+File.separator+"pdf";
        File pdfDirFile = new File(pdfPath);
        if(!pdfDirFile.exists()){//如果此路径不存在，则创建该文件夹
            pdfDirFile.mkdirs();
        }
        String filePath = pdfPath + File.separator + "beingConverted.txt";
        File file = new File(filePath);//用来做转换标示
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("1");
            writer.flush();
            writer.close();
            faultTreatService.convertFileToPdf(tbmName,faultId);
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
            returnInfo = faultTreatService.checkPreview(ajaxParam);
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
            faultTreatService.previewFile(ajaxParam, response);
        } catch (IOException e) {
            logger.error( e.getMessage());
        }

    }
}

