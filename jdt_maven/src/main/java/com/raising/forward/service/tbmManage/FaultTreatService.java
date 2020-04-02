package com.raising.forward.service.tbmManage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.raising.forward.mapper.DaoUtil;
import com.raising.forward.service.PropertiesValue;
import com.util.*;
import org.apache.commons.io.FilenameUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.baosight.common.utils.DateUtils.String2Date;
import static com.baosight.common.utils.DateUtils.date2String;

@Service
@Transactional
public class FaultTreatService {

    private static final Integer FILE_SIZE = 20000000;//20M

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private DaoUtil daoUtil;

    /**
     * 查询记录
     * @param ajaxParam
     * @return
     */
    public JSONObject query(String ajaxParam)  {
        JSONObject queryJson = new JSONObject();
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = new JSONObject();
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        //数据校验
        String tbmId = inquStatus.getString("tbmId");
        inquStatus.put("tbmId",Integer.parseInt(tbmId));
        String querySql = "com.raising.forward.mapper.FaultTreat.query";
        String countSql = "com.raising.forward.mapper.FaultTreat.count";
        String entity = "com.raising.forward.entity.tbmManage.FaultTreat";
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, querySql);
        paramInfo.put(BaseService.COUNT_SQL, countSql);
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        returnInfo = daoUtil.query(paramInfo);

        JSONArray detailsArray = (JSONArray)returnInfo.get("rows");//获得每条记录相关联的文件名称
        String tbmName = inquStatus.getString("tbmName");
        if(detailsArray.size() < 1){
            return returnInfo;
        }
        for(int i = 0;i<detailsArray.size();i++){
            JSONObject tempObj = detailsArray.getJSONObject(i);
            tempObj.put("createTime", date2String("yyyy-MM-dd", new Date(tempObj.getLong("createTime"))));
            tempObj.put("warrantyTime", date2String("yyyy-MM-dd", new Date(tempObj.getLong("warrantyTime"))));
            String id = tempObj.getString("faultId");
            String path = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"faultTreat"+File.separator+tbmName+File.separator+id;
            File file = new File(path);
            if(!file.exists() || !file.isDirectory()){
                continue;//如果没有关联文件，或者文件不是目录,跳转下一个
            }
            File[] files = file.listFiles();
            if(files.length > 0){
                for(int j = 0;j<files.length;j++){
                    File file1 = files[j];
                    if(file1.isDirectory() && ("pdf".equals(file1.getName()) || "odt".equals(file1.getName()))){
                        continue;
                    }else {
                        tempObj.put("fileName", file1.getName());//无论文件多少，只显示一个文件
                        break;
                    }
                }
            }
        }
        return returnInfo;
    }

    /**
     * 新增记录
     * @param ajaxParam
     * @return
     */
    public JSONObject insert(String ajaxParam) throws Exception {
        JSONObject ajaxJson = JSONObject.parseObject(ajaxParam);
        JSONArray dataArray = ajaxJson.getJSONObject("detail").getJSONArray("resultRow");
        for(int i=0;i<dataArray.size();i++){
            JSONObject dataJson = dataArray.getJSONObject(i);
            //将时间字符串转换为Date类型
            String createTimeStr = dataJson.getString("createTime");
            String warrantyTimeStr = dataJson.getString("warrantyTime");
            dataJson.put("createTime", String2Date(new String(createTimeStr), "yyyy-MM-dd"));
            dataJson.put("warrantyTime", String2Date(new String(warrantyTimeStr), "yyyy-MM-dd"));
            dataJson.put("tbmId", dataJson.getInteger("tbmId"));
        }
        String insertSql = "com.raising.forward.mapper.FaultTreat.insert";
        String entity = "com.raising.forward.entity.tbmManage.FaultTreat";
        ajaxJson.put(BaseService.RESULT_BLOCK,"detail");
        ajaxJson.put(BaseService.INSERT_SQL, insertSql);
        ajaxJson.put(BaseService.DAO_ENTITY, entity);
        JSONObject returnInfo = daoUtil.insert(ajaxJson);
        return returnInfo;
    }

    /**
     * 修改记录
     * @param ajaxParam
     * @return
     */
    public JSONObject update(String ajaxParam) throws Exception {
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONArray dataArray = paramInfo.getJSONObject("detail").getJSONArray("resultRow");
        for(int i=0;i<dataArray.size();i++){
            JSONObject dataJson = dataArray.getJSONObject(i);
            //将时间字符串转换为Date类型
            String createTimeStr = dataJson.getString("createTime");
            String warrantyTimeStr = dataJson.getString("warrantyTime");
            dataJson.put("createTime", String2Date(new String(createTimeStr), "yyyy-MM-dd"));
            dataJson.put("warrantyTime", String2Date(new String(warrantyTimeStr), "yyyy-MM-dd"));
            dataJson.put("faultId",dataJson.getIntValue("faultId"));
            dataJson.put("tbmId",dataJson.getIntValue("tbmId"));
        }
        String updateSql = "com.raising.forward.mapper.FaultTreat.update";
        String entity = "com.raising.forward.entity.tbmManage.FaultTreat";
        paramInfo.put(BaseService.UPDATE_SQL, updateSql);
        paramInfo.put(BaseService.RESULT_BLOCK, "detail");
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        JSONObject returnInfo = daoUtil.update(paramInfo);
        return returnInfo;
    }

    /**
     * 删除记录
     * @param ajaxParam
     * @return
     */
    public JSONObject delete(String ajaxParam) throws Exception {
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONArray dataArray = paramInfo.getJSONObject("result").getJSONArray("resultRow");
        List<String> faultIds = new ArrayList<>();//如果删除成功，则根据此id集合。删除记录关联的文件
        for(int i=0;i<dataArray.size();i++){
            JSONObject dataJson = dataArray.getJSONObject(i);
            dataJson.put("faultId",dataJson.getIntValue("faultId"));
            faultIds.add(dataJson.getString("faultId"));
        }
        String deleteSql = "com.raising.forward.mapper.FaultTreat.delete";
        String entity = "com.raising.forward.entity.tbmManage.FaultTreat";
        paramInfo.put(BaseService.DELETE_SQL, deleteSql);
        paramInfo.put(BaseService.RESULT_BLOCK, "result");
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        JSONObject returnInfo = null;
        returnInfo = daoUtil.delete(paramInfo);
        if(Constants.EXECUTE_SUCCESS.equals(returnInfo.getString("status"))){
            String tbmName = paramInfo.getString("tbmName");
            for(int i=0;i<faultIds.size();i++){
                String faultId = faultIds.get(i);
                String path = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"faultTreat"+File.separator+tbmName+File.separator+faultId;
                FileUtils.deleteFolder(path);
            }
        }
        return returnInfo;
    }

    public JSONObject queryFile(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONArray rows = new JSONArray();
        JSONObject ajaxJson = JSONObject.parseObject(ajaxParam);

        String tbmName = ajaxJson.getString("tbmName");
        Integer faultId = ajaxJson.getInteger("faultId");

        String path = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"faultTreat"+File.separator+tbmName+File.separator+faultId;
        File file = new File(path);
        if(!file.exists() || !file.isDirectory()){

        }else {
            File[] files = file.listFiles();
            if (files != null || files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    JSONObject tempObj = new JSONObject();
                    File file1 = files[i];
                    if(file1.isDirectory() && ("pdf".equals(file1.getName()) || "odt".equals(file1.getName()))){//如果是pdf,odt文件夹，则忽略。
                        continue;
                    }
                    tempObj.put("fileName", file1.getName());
                    rows.add(tempObj);
                }
            }
        }
        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        returnInfo.put("page",1);
        returnInfo.put("returnMsg","查询成功！本次返回"+rows.size()+"条记录，总共"+rows.size()+"条记录");
        returnInfo.put("records",rows.size());
        returnInfo.put("rows",rows);
        return returnInfo;
    }

    /**
     * 上传文件
     * @param file
     * @param tbmName
     * @return
     * @throws Exception
     */
    public JSONObject dataUpload(File file, String tbmName,String faultId)  {
        JSONObject returnInfo = new JSONObject();

        String oldFileName = file.getName();//文件原名

        String path = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"faultTreat"+File.separator+tbmName+File.separator+faultId;
        File targetFile = new File(path,oldFileName);

        targetFile.mkdirs();
        file.renameTo(targetFile);
        returnInfo.put("status", Constants.EXECUTE_SUCCESS);
        returnInfo.put("message", "文件保存成功");
        return returnInfo;
    }

    /**
     * 下载文件
     * @param ajaxParam
     * @param response
     * @throws IOException
     */
    public void download(String ajaxParam,HttpServletResponse response) throws IOException {
        JSONArray fileNames = null;
        String tbmName = "";
        String faultId = "";
        try {
            String temp = URLDecoder.decode(ajaxParam, "UTF-8");
            JSONObject ajaxJson = JSONObject.parseObject(temp);
            tbmName = ajaxJson.getString("tbmName");
            fileNames = ajaxJson.getJSONArray("fileNames");
            faultId = ajaxJson.getString("faultId");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(fileNames == null || fileNames.size() < 1 || StringUtils.isNullOrEmpty(tbmName) || StringUtils.isNullOrEmpty(faultId)){
            return;
        }

        List<String> paths = new ArrayList<>();
        for(int i=0;i<fileNames.size();i++){
            String fileName = fileNames.getString(i);
            String path = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"faultTreat"+File.separator+tbmName+File.separator+faultId+File.separator+fileName;
            paths.add(path);
        }
        if(paths.size() == 1){
            String path = paths.get(0);
            String fileName = fileNames.getString(0);
            File sourceFile = new File(path);
            if(sourceFile.isFile()){//如果是文件，直接下载
                UploadUtil.download(response,fileName,path);
            }else if(sourceFile.isDirectory()){//如果是目录
                File zipTemp = File.createTempFile(fileName,".zip");
                ZipUtil.compress(path, zipTemp.getPath());
                UploadUtil.download(response,zipTemp.getName(),zipTemp.getPath());
                zipTemp.delete();
            }
        }else{//如果下载文件不止一个。则压缩下载。
            UploadUtil.downloadFiles(response,fileNames,paths);
        }

    }

    /**
     * 删除文件
     * @param ajaxParam
     * @return
     */
    public JSONObject deleteFile(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject ajaxJson = JSONObject.parseObject(ajaxParam);
        String tbmName = ajaxJson.getString("tbmName");
        JSONArray fileNames = ajaxJson.getJSONArray("fileNames");
        String faultId = ajaxJson.getString("faultId");

        if(StringUtils.isNullOrEmpty(tbmName) || fileNames == null || fileNames.size() < 1  || StringUtils.isNullOrEmpty(faultId)){
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("message","执行失败");
            return returnInfo;
        }
        for(int i=0;i<fileNames.size();i++){
            String fileName = fileNames.getString(i);
            String path = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"faultTreat"+File.separator+tbmName+File.separator+faultId+File.separator+fileName;
            boolean deleteResult = FileUtils.deleteFolder(path);
            if(deleteResult == false){
                returnInfo.put("status",Constants.EXECUTE_FAIL);
                returnInfo.put("message","删除"+fileName+"失败！");
                return returnInfo;
            }
        }

        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        return returnInfo;
    }

    /***
     * 得到问题类型的ccsId
     * @return
     */
    public String getProblemType(){
        JSONObject paramJson = new JSONObject();
        paramJson.put("tenant",request.getSession().getAttribute("tenant"));
        String sql = "com.raising.forward.mapper.FaultTreat.query";
        List<JSONObject> objects = this.sqlSessionTemplate.selectList(sql, paramJson);
        if(objects == null){
            return null;
        }
        //遍历，得到status 不为空的值
        for(int i=0;i<objects.size();i++){
            JSONObject temp = objects.get(i);
            if(!StringUtils.isNullOrEmpty(temp.getString("processMode"))){
                return temp.getString("processMode");
            }
        }
        return null;

    }

    public void convertFileToPdf(String tbmName,String faultId)  {
        String faultPath = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"faultTreat"+File.separator+tbmName+File.separator+faultId;

        String pdfPath = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"faultTreat"+File.separator+tbmName+File.separator+faultId+File.separator+"pdf";
        File pdfDirFile = new File(pdfPath);
        if(!pdfDirFile.exists()){//如果此路径不存在，则创建该文件夹
            pdfDirFile.mkdirs();
        }
        String filePath = pdfPath + File.separator + "beingConverted.txt";
        File convetFile = new File(filePath);//用来做转换标示
        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(convetFile));
            writer.write("1");
            writer.flush();
            writer.close();

            File falut = new File(faultPath);
            if (!falut.exists()) {
                return;
            }
            File[] files = falut.listFiles();
            if (files.length < 1) {
                return;
            }
            String fileType = ".doc|.docx|.jpg|.gif|.jpeg|.png|.bmp|.txt";
            String excel = ".xls|.xlsx";
            for (int i = 0; i < files.length; i++) {
                File tempFile = files[i];//要转换的文件，其中包含不需要转换的pdf文件夹
                if (tempFile.isDirectory()) {//过滤文件夹，会自动过滤pdf文件夹
                    continue;
                }
                //如果该文件不是doc类型。但是内容为空，则不转换
                String fileName = tempFile.getName();
                String postfix = fileName.substring(fileName.lastIndexOf(".") + 1);

                if (fileType.indexOf(postfix.toLowerCase()) >= 0) {  //判断文件类型。
                    String directory = faultPath + File.separator + "pdf";
                    String odtDir = faultPath + File.separator + "odt";
                    String outputFileName = fileName.replaceAll("." + postfix, ".pdf");
                    String outPath = directory + File.separator + outputFileName;
                    File pdfFile = new File(outPath);
                    if (pdfFile.exists()) {//如果该文件已经转换成pdf文件，则跳过
                        continue;
                    }
                    try {
                        Office2Pdf.converterFile2(tempFile.getCanonicalPath(), outPath, odtDir);
                    } catch (OpenOfficeException e) {//如果出现转换异常，视为被转换文件为空导致的转换异常。新建一个空pdf文件。处理异常
                        File file = new File(outPath);
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                    }
                } else if (excel.indexOf(postfix.toLowerCase()) >= 0) {//如果是excel类型
                    String directory = faultPath + File.separator + "pdf";
                    String outputFileName = fileName.replaceAll("." + postfix, ".html");
                    String outPath = directory + File.separator + outputFileName;
                    try {
                        ExcelUtil.convertExceltoHtml(tempFile.getCanonicalPath(), outPath);
                    } catch (Exception e) {//如果出现转换异常，视为被转换文件为空导致的转换异常。新建一个空文件。处理异常

                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(convetFile));
                writer.write("0");
                writer.flush();
                writer.close();
            } catch (IOException e) {
            }

        }
    }


    /**
     * 检查后台是否正在转换Pdf文件。
     * @param ajaxParam
     * @return
     * @throws IOException
     */
    public JSONObject checkPreview(String ajaxParam) throws IOException {
        JSONObject returnInfo = new JSONObject();
        JSONObject ajaxJson = JSONObject.parseObject(ajaxParam);
        String tbmName = ajaxJson.getString("tbmName");
        String faultId = ajaxJson.getString("faultId");
        String factoryPath = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"faultTreat"+File.separator+tbmName+File.separator+faultId+File.separator+"pdf";
        String filePath = factoryPath + File.separator + "beingConverted.txt";
        File file = new File(filePath);//用来做转换标示
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String s = reader.readLine();
        if("0".equals(s)){
            returnInfo.put("status",Constants.EXECUTE_SUCCESS);
            return returnInfo;
        }
        returnInfo.put("status",Constants.EXECUTE_FAIL);
        returnInfo.put("message","文件正在转换，请等待");
        return returnInfo;
    }


    /**
     * 文件预览
     * @param ajaxParam
     * @param response
     * @throws IOException
     */
    public void previewFile(String ajaxParam,HttpServletResponse response) throws IOException {
        ajaxParam = URLDecoder.decode(ajaxParam, "UTF-8");
        JSONObject ajaxJson = JSONObject.parseObject(ajaxParam);

        String tbmName = ajaxJson.getString("tbmName");
        String fileName = ajaxJson.getString("fileName");
        String faultId = ajaxJson.getString("faultId");


        String filePath = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"faultTreat"+File.separator+tbmName+File.separator+faultId;
        String resouceFilePath = filePath + File.separator + fileName;//源文件
        String postfix = fileName.substring(fileName.lastIndexOf(".") );
        //String prefix = fileName.substring(0,fileName.lastIndexOf("."));
        String images = ".jpg|.gif|.jpeg|.png|.bmp";
        String excel = ".xls|.xlsx";
        File file = null;
        if(images.indexOf(postfix.toLowerCase()) >= 0){
            response.setContentType("image/jpeg");
            file = new File(resouceFilePath);
        }else if(excel.indexOf(postfix.toLowerCase()) >= 0){//如果文件时excel
            response.setContentType("text/html; charset=utf-8");
            String htmlFileName = fileName.replaceAll(postfix,".html");
            String pdfPath = filePath + File.separator + "pdf"+File.separator+htmlFileName;
            file = new File(pdfPath);
            if(!file.exists()){//如果html文件不存在
                try {
                    ExcelUtil.convertExceltoHtml(resouceFilePath,file.getCanonicalPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }else{//如果不是文件，则转换成PDF格式
            response.setContentType("application/pdf");
            String pdfFileName = fileName.replaceAll(postfix,".pdf");
            String pdfDirPath =  filePath + File.separator + "pdf";
            String odtDir = filePath + File.separator + "odt";
            File pdfDir = new File(pdfDirPath);
            if(!pdfDir.exists() || !pdfDir.isDirectory()){
                pdfDir.mkdirs();
            }
            String pdfPath = filePath + File.separator + "pdf"+File.separator+pdfFileName;
            file = new File(pdfPath);
            if(!file.exists()) {//如果pdf文件不存在
                Office2Pdf.converterFile2(resouceFilePath, file.getCanonicalPath(),odtDir);
            }
        }

        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = new byte[inputStream.available()];
        int len = 0;
        response.reset();

        response.setHeader("Content-Disposition",
                "inline; filename=" + java.net.URLEncoder.encode(file.getName(), "UTF-8"));
        OutputStream outputStream = response.getOutputStream();
        while ((len = inputStream.read(bytes)) != -1){
            outputStream.write(bytes,0,len);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();


    }



}


