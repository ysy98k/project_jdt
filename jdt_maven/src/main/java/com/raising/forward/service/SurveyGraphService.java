package com.raising.forward.service;

import com.alibaba.fastjson.JSONObject;
import com.raising.forward.controller.tbmManage.FaultTreatController;
import com.raising.forward.entity.Engineering;
import com.raising.forward.entity.ProjectInfo;
import com.raising.rest.sdk.utils.encoder.BASE64Encoder;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Service
public class SurveyGraphService {

    private SurveyInfoService surveyInfoService = new SurveyInfoService();

    private static final Logger logger = LoggerFactory.getLogger(FaultTreatController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public JSONObject uploadxls(MultipartFile multipartfile, int selectID) {
        JSONObject returnInfo = new JSONObject();
        Engineering engineering = engineeringCheck(selectID);
        if (engineering != null) {
            String fileName = multipartfile.getOriginalFilename();
            String path = PropertiesValue.UPLOAD_FILE_PATH + "/surveyGraph/" + engineering.getEngineeringUUID() + "/" + fileName;
            logger.info("File save path: " + path);
            File newFile = new File(path);
            File fileParent = newFile.getParentFile();
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            try {
                multipartfile.transferTo(newFile);
            } catch (IOException e) {
                logger.error("Exception: " + e);
            }
            if (newFile.isFile()) {
                List<String> pdfFiles = getFileNames(engineering);
                returnInfo.put("files", pdfFiles);
                returnInfo.put("result", "上传文件成功");
                returnInfo.put("path", path);
                returnInfo.put("fileName", fileName);
            } else {
                returnInfo.put("result", "上传文件失败");
            }
        } else {
            returnInfo.put("msg", "当前项目不存在或还未上传测点信息！");
            return returnInfo;
        }
        return returnInfo;
    }

    public JSONObject getProjectLastPDF(int selectID) {
        JSONObject returnInfo = new JSONObject();
        Engineering engineering = engineeringCheck(selectID);
        if (engineering != null) {
            List<String> pdfFiles = getFileNames(engineering);
            String lastFilePath = PropertiesValue.UPLOAD_FILE_PATH + "/surveyGraph/" + engineering.getEngineeringUUID() + "/" + pdfFiles.get(0);
            returnInfo.put("fileName", pdfFiles.get(0));
            returnInfo.put("files", pdfFiles);
            returnInfo.put("lastFilePath", lastFilePath);
        } else {
            returnInfo.put("msg", "请上传布点图！");
            return returnInfo;
        }
        return returnInfo;
    }

    public JSONObject deleteFiles(int selectID, List<String> fileNames) {
        JSONObject returnInfo = new JSONObject();
        Engineering engineering = engineeringCheck(selectID);
        List<String> errorDeleteFiles = new ArrayList<>();
        if (engineering != null) {
            for (String fileName : fileNames) {
                String path = PropertiesValue.UPLOAD_FILE_PATH + "/surveyGraph/" + engineering.getEngineeringUUID() + "/" + fileName;
                File file = new File(path);
                if (file.isFile()) {
                    file.delete();
                } else {
                    errorDeleteFiles.add(fileName);
                }
            }
            List<String> pdfFiles = getFileNames(engineering);
            if (pdfFiles.isEmpty()) {
                returnInfo.put("msg", "没有文件！");
                return returnInfo;
            }
            String lastFilePath = PropertiesValue.UPLOAD_FILE_PATH + "/surveyGraph/" + engineering.getEngineeringUUID() + "/" + pdfFiles.get(0);
            returnInfo.put("fileName", pdfFiles.get(0));
            returnInfo.put("files", pdfFiles);
            returnInfo.put("lastFilePath", lastFilePath);
            if (!errorDeleteFiles.isEmpty()) {
                returnInfo.put("errorDeleteFiles", errorDeleteFiles);
            }

        } else {
            returnInfo.put("msg", "当前项目不存在！");
            return returnInfo;
        }
        return returnInfo;
    }

    public JSONObject getProjectPDF(int selectID, String fileName) {
        JSONObject returnInfo = new JSONObject();
        Engineering engineering = engineeringCheck(selectID);
        if (engineering != null) {
            String path = PropertiesValue.UPLOAD_FILE_PATH + "/surveyGraph/" + engineering.getEngineeringUUID() + "/" + fileName;
            System.out.println(path);
            File file = new File(path);
            if (file.isFile()) {
                returnInfo.put("file", path);
                returnInfo.put("fileName", fileName);
            } else {
                returnInfo.put("msg", "文件不存在！");
                return returnInfo;
            }
        } else {
            returnInfo.put("msg", "当前项目不存在！");
            return returnInfo;
        }
        return returnInfo;
    }

    private Engineering engineeringCheck(int selectID) {
        String tenant = (String) request.getSession().getAttribute("tenant");
        List<JSONObject> ProjectInfos = surveyInfoService.getProjectByID(selectID, tenant, sqlSessionTemplate);
        if (ProjectInfos.isEmpty()) {
            return null;
        }
        JSONObject project = ProjectInfos.get(0);
        String projectName = (String) project.get("projectname");
        int projectID = (Integer) project.get("projectid");

        Engineering engineering = surveyInfoService.getEngineeringByProjectID(projectID, tenant, sqlSessionTemplate);
        if (engineering == null) {
            Engineering newEngineering = new Engineering();
            newEngineering.setEngineeringUUID();
            newEngineering.setProject(projectName);
            newEngineering.setProjectID(projectID);
            int result = surveyInfoService.insertEngineering(newEngineering, tenant, sqlSessionTemplate);
            if (result > 0) {
                return newEngineering;
            } else {
                return null;
            }
        }
        return engineering;
    }

    private List<String> getFileNames(Engineering engineering) {
        List<String> pdfFiles = new ArrayList<>();
        String directoryPath = PropertiesValue.UPLOAD_FILE_PATH + "/surveyGraph/" + engineering.getEngineeringUUID();
        File file = new File(directoryPath);
        File[] fs = file.listFiles();
        if (fs != null) {
            for (File f : fs) {
                if (!f.isDirectory())
                    pdfFiles.add(f.getName());
            }
        }
        return pdfFiles;
    }


    public void showPDF(String filePath, HttpServletResponse response) {
        File file = new File(filePath);
        try {
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            byte[] bytes = new byte[inputStream.available()];
            int len;
            response.reset();
//            response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(file.getName(), "UTF-8"));
//            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition",
                    "inline; filename=" + java.net.URLEncoder.encode(file.getName(), "UTF-8"));
            OutputStream outputStream = response.getOutputStream();
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
