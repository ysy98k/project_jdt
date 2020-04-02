package com.raising.forward.service.tbmManage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException;
import com.baosight.common.basic.dao.RestDao;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.raising.forward.service.PropertiesValue;
import com.util.*;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipInputStream;


/**
 * 厂内发货
 */
@Service("startManageService")
public class StartManageService {



    private static final Logger logger = LoggerFactory.getLogger(StartManageService.class);




    @Autowired
    private RestDao dao;

    /**
     * 得到二级菜单的
     * @param ajaxParam
     * @return
     */
    public JSONObject getSecondFileTable(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        List<JSONObject> dataList = new LinkedList<>();
        JSONObject ajaxJson = JSONObject.parseObject(ajaxParam);
        String secondLevel = ajaxJson.getString("secondLevel");
        String tbmName = ajaxJson.getString("tbmName");

        //String path =request.getSession().getServletContext().getRealPath(FILE_PATH+"/"+tbmName+"/"+secondLevel+".zip");
        String path =PropertiesValue.UPLOAD_FILE_PATH+File.separator+"startManage"+File.separator+tbmName+File.separator+secondLevel+".zip";

        File file = new File(path);
        if(!file.exists()){
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("dataList",dataList);
            return returnInfo;
        }else{
            JSONObject json = new JSONObject();
            json.put("fileName",file.getName());
            json.put("fileSize", com.util.FileUtils.getFileSize(file));
            json.put("time",com.util.FileUtils.getTime(file));
            json.put("filePath", path);
            dataList.add(json);
        }
        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        returnInfo.put("dataList",dataList);
        return returnInfo;
    }

    public JSONObject getFileTable(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        List<JSONObject> dataList = new LinkedList<>();
        JSONObject ajaxJson = JSONObject.parseObject(ajaxParam);
        String secondLevel = ajaxJson.getString("secondLevel");
        String threeLevel = ajaxJson.getString("threeLevel");
        String tbmName = ajaxJson.getString("tbmName");

        /*String path =request.getSession().getServletContext().getRealPath(FILE_PATH+"/"+tbmName+"/"+secondLevel+"/"+threeLevel);*/
        String path = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"startManage"+File.separator+tbmName+File.separator+secondLevel+File.separator+threeLevel;


        File file = new File(path);
        if(!file.isDirectory()){
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("dataList",dataList);
            return returnInfo;
        }
        File[] files = file.listFiles();
        if(files == null || files.length < 1 ){
            returnInfo.put("status",Constants.EXECUTE_SUCCESS);
            returnInfo.put("dataList",dataList);
            return returnInfo;
        }
        for(int i =0;i<files.length;i++){
            JSONObject json = new JSONObject();
            File temp = files[i];
            json.put("fileName",temp.getName());
            json.put("fileSize", com.util.FileUtils.getFileSize(temp));
            json.put("time",com.util.FileUtils.getTime(temp));
            dataList.add(json);
        }
        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        returnInfo.put("dataList",dataList);
        return returnInfo;
    }

    public JSONObject dataUpload( File file, String tbmName,String directory,String token)  {
        JSONObject returnInfo = new JSONObject();
        dao.setHost(PropertiesValue.CCS_ADRESS);
        dao.setServiceName(PropertiesValue.CCS_APP_PATH);


        List<String> factoryType = new ArrayList<>();
        JSONObject param = new JSONObject();
        param.put("token",token);
        JSONObject get = dao.invoke("get", "/api/query/item/startManage.first", param);
        if("0".equals(get.getString("errCode"))){
            JSONObject ccs = get.getJSONObject("ccs");
            JSONArray children = ccs.getJSONArray("children");
            for(int i = 0;i<children.size();i++){
                JSONObject tempChildren = children.getJSONObject(i);
                factoryType.add(tempChildren.getString("label"));
            }
        }else{
            returnInfo.put("status", Constants.EXECUTE_FAIL);
            returnInfo.put("message","获取CCS数据失败,文件保存失败！");
            return  returnInfo;
        }

        //解压zip文件并保存
        String pathTemp =PropertiesValue.UPLOAD_FILE_PATH+File.separator+"startManage"+File.separator+tbmName;
        List<String> failName = new ArrayList<>();

        ZipInputStream zipInputStream = null;
        try {
            zipInputStream = new ZipInputStream(new FileInputStream(file), Charset.forName("GBK"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        java.util.zip.ZipEntry zipEntry ;
        boolean check = false;
        try {
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String zipEntryName = zipEntry.getName();
                String outPath = zipEntryName.replaceAll("\\+", "/");
                boolean checkResult = checkUploadFile(outPath, directory, factoryType, failName);
                if (checkResult == false) {//如果通过校验
                    continue;
                }
                check = true;
                String DirectoryPath = pathTemp + "/" + outPath.substring(0, outPath.lastIndexOf("/"));
                File file1 = new File(DirectoryPath);
                if (!file1.exists()) {
                    file1.mkdirs();
                }
                String pathName = pathTemp + "/" + outPath;
                if (new File(pathName).isDirectory()) {
                    continue;
                }
                OutputStream outputStream = new FileOutputStream(pathName);//***
                byte[] bytes = new byte[zipInputStream.available()];
                int len;
                //当read的返回值为-1，表示碰到当前项的结尾，而不是碰到zip文件的末尾
                while ((len = zipInputStream.read(bytes)) > 0) {
                    outputStream.write(bytes, 0, len);
                }
                outputStream.flush();
                outputStream.close();
                //必须调用closeEntry()方法来读入下一项
                zipInputStream.closeEntry();
            }
            zipInputStream.close();
        }catch (IOException e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        if(check == true){//如果上传压缩文件通过校验，则保存此压缩文件。否则不保存
            //保存zip文件
            String zipPath = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"startManage"+File.separator+tbmName+File.separator+file.getName();
            File zipFile = new File(zipPath);
            FileUtils.moveFile(file.getAbsolutePath(),zipPath);
        }

        if(failName.size() < 1) {
            returnInfo.put("status", Constants.EXECUTE_SUCCESS);
            returnInfo.put("message", "文件保存成功");
        }else {
            StringBuffer failNameStr = new StringBuffer("");
            for(int i=0;i<failName.size();i++){
                String s = failName.get(i);
                failNameStr.append(s);
                if(i < failName.size() - 2){
                    failNameStr.append(",");
                }
            }
            returnInfo.put("status", "1");
            returnInfo.put("message",failNameStr+ "未成功保存");
        }

        return returnInfo;
    }

    private boolean checkUploadFile(String outPath,String directory,List<String> factoryType,List<String> failName){
        if(outPath.indexOf("/")<0){//如果压缩目录下的一级子目录包含文件，则跳过
            failName.add(outPath);
            return false;
        }
        String oneLevel = outPath.substring(0,outPath.indexOf("/"));
        if(!directory.equals(oneLevel)){//如果一级子目录不是用户选择的目录，则跳过
            if(failName.contains(oneLevel+"文件夹")){
                return false;
            }
            failName.add(oneLevel+"文件夹");
            return false;
        }
        //判断是否符合保存规则
        String secondLevel = null;
        secondLevel = outPath.substring(outPath.indexOf("/")+1);
        if("".equals(secondLevel)){//如果是压缩目录下的一级子目录厂内交货目录，则跳过
            return false;
        }
        if(secondLevel.indexOf("/")< 0){//如果是压缩目录下的二级子目录，但不是文件夹。则不容过校验，并保存文件名
            failName.add(secondLevel);
            return false;
        }
        secondLevel =  secondLevel.substring(0,secondLevel.indexOf("/"));
        if(!factoryType.contains(secondLevel)){//如果是压缩目录下的二级子目录，但是文件夹名字不符合规则，则不通过校验，并保存文件夹名。
            if(failName.contains(secondLevel+"文件夹")){
                return false;
            }
            failName.add(secondLevel+"文件夹");
            return false;
        }
        return true;
    }

    public void download(String ajaxParam,HttpServletResponse response) throws IOException {
        JSONArray fileNames = null;
        String tbmName = "";
        String secondLevel = "";
        String threeLevel = "";
        boolean downAllZip = false;
        try {
            String temp = URLDecoder.decode(ajaxParam, "UTF-8");
            JSONObject ajaxJson = JSONObject.parseObject(temp);
            tbmName = ajaxJson.getString("tbmName");
            fileNames = ajaxJson.getJSONArray("fileNames");
            secondLevel = ajaxJson.getString("secondLevel");
            threeLevel = ajaxJson.getString("threeLevel");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(fileNames == null || fileNames.size() < 1  || StringUtils.isNullOrEmpty(tbmName)){
            return;
        }
        if(StringUtils.isNullOrEmpty(secondLevel)||StringUtils.isNullOrEmpty(threeLevel)){
            downAllZip = true;
        }

        if(downAllZip == true){//如果下载整个二级目录
            String fileName = fileNames.getString(0);
            //String path = request.getSession().getServletContext().getRealPath(FILE_PATH+"/"+tbmName+"/"+fileName);
            String path = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"startManage"+File.separator+tbmName+File.separator+fileName;
            UploadUtil.download(response,fileName,path);
        }else{//如果下载三级目录文件
            List<String> paths = new ArrayList<>();
            for(int i=0;i<fileNames.size();i++){
                String fileName = fileNames.getString(i);
                //String path = request.getSession().getServletContext().getRealPath(FILE_PATH+"/"+tbmName+"/"+secondLevel+"/"+threeLevel+"/"+fileName);
                String path = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"startManage"+File.separator+tbmName+File.separator+secondLevel+File.separator+threeLevel+File.separator+fileName;
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

    }


    public JSONObject deleteFiles(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject ajaxJson = JSONObject.parseObject(ajaxParam);
        String tbmName = ajaxJson.getString("tbmName");
        String secondLevel = ajaxJson.getString("secondLevel");
        String threeLevel = ajaxJson.getString("threeLevel");
        JSONArray fileNames = ajaxJson.getJSONArray("fileNames");
        boolean deleteAllZip = false;
        if(StringUtils.isNullOrEmpty(tbmName) || fileNames == null || fileNames.size() < 1){
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("message","执行失败");
            return returnInfo;
        }
        if(StringUtils.isNullOrEmpty(threeLevel)){
            deleteAllZip = true;
        }
        if(deleteAllZip == true){//如果删除文件夹，则删除全部
//          String zipPath = request.getSession().getServletContext().getRealPath(FILE_PATH + "/" + tbmName + "/" + fileNames.getString(0));
            String zipPath = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"startManage"+File.separator+tbmName+File.separator+fileNames.getString(0);


            boolean deleteZip = com.util.FileUtils.deleteFolder(zipPath);
            if (deleteZip == false ) {
                returnInfo.put("status", Constants.EXECUTE_FAIL);
                returnInfo.put("message", "删除" + fileNames.getString(0) + "失败！");
                return returnInfo;
            }
        }else {
            for (int i = 0; i < fileNames.size(); i++) {
                String fileName = fileNames.getString(i);
                //String path = request.getSession().getServletContext().getRealPath(FILE_PATH + "/" + tbmName + "/" + secondLevel + "/" + threeLevel + "/" + fileName);
                String path = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"startManage"+File.separator+tbmName+File.separator+secondLevel+File.separator+threeLevel+File.separator+fileName;

                boolean deleteResult = com.util.FileUtils.deleteFolder(path);
                if (deleteResult == false) {
                    returnInfo.put("status", Constants.EXECUTE_FAIL);
                    returnInfo.put("message", "删除" + fileName + "失败！");
                    return returnInfo;
                }
            }
        }

        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        return returnInfo;
    }

    public JSONArray getTree(String tbmName,JSONArray menuArr){
        JSONArray returnArray = new JSONArray();

        //String path = request.getSession().getServletContext().getRealPath(FILE_PATH + "/" + tbmName);
        String path = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"startManage"+File.separator+tbmName;

        File file = new File(path);

        File[] files =  file.listFiles();
        List<String> fileNames = new ArrayList<>();
        if(files == null ||files.length < 1){
            return returnArray;
        }else{
            for(int i =0;i<files.length;i++){
                String fileName = files[i].getName();
                if(fileName.indexOf(".zip") != -1){
                    continue;
                }
                fileNames.add(fileName);
            }
            //遍历节点集合，剔除文件系统中没有上传的节点
            for(int i=0;i<menuArr.size();i++){
                JSONObject temp = menuArr.getJSONObject(i);
                String text = temp.getString("text");
                if(fileNames.contains(text)) {
                    returnArray.add(temp);
                }

            }
        }
        return returnArray;
    }


    public void downloadTemplet(HttpServletResponse response) throws IOException {
        //String path = request.getSession().getServletContext().getRealPath("WEB-INF/uploadfiles/startManage/templet.zip");
        String path = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"startManage"+File.separator+"首次始发.zip";
        UploadUtil.download(response,"首次始发.zip",path);

    }


    public void convertFileToPdf(String tbmName,String secondLevel){


        String secondPath = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"startManage" + File.separator + tbmName + File.separator +secondLevel;
        String filePath = secondPath + File.separator + "beingConverted.txt";
        File convertFile = new File(filePath);//用来做转换标示
        File parentFile = convertFile.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(convertFile));
            writer.write("1");
            writer.flush();
            writer.close();

            File factory = new File(secondPath);
            File[] files = factory.listFiles();
            if (files.length < 1) {
                return;
            }

            String fileType = ".doc|.docx|.csv";
            String excel = ".xls|.xlsx";
            for (int i = 0; i < files.length; i++) {
                File tempFile = files[i];//首次始发的一级目录，文件夹对象
                if (tempFile.isFile()) {//过滤文件
                    continue;
                }
                File[] files2 = tempFile.listFiles();
                if (files2.length < 1) {
                    continue;
                }
                String directoryName = tempFile.getName();//二级目录名，1.数据库备份
                for (int k = 0; k < files2.length; k++) {//要转换的文件对象
                    File sourceFile = files2[k];
                    if (sourceFile.isDirectory()) {//如果是文件夹，则跳过
                        continue;
                    }
                    //如果该文件不是doc类型。但是内容为空，则不转换
                    String fileName = sourceFile.getName();//文件名
                    String postfix = fileName.substring(fileName.lastIndexOf(".") + 1);

                    if (fileType.indexOf(postfix.toLowerCase()) >= 0) {  //如果匹配，则转换
                        String directory = secondPath + File.separator + "pdf" + File.separator + directoryName;//首次始发/pdf/1.数据库备份
                        String pdfFileName = fileName.replaceAll("." + postfix, ".pdf");//pdf文件名
                        String pdfPath = directory + File.separator + pdfFileName;//pdf文件路径
                        try {
                            Office2Pdf.converterFile(sourceFile, pdfPath, sourceFile.getCanonicalPath());
                        } catch (OpenOfficeException e) {//如果出现转换异常，视为被转换文件为空导致的转换异常。新建一个空pdf文件。处理异常
                            File file = new File(pdfPath);
                            if (!file.exists()) {
                                file.createNewFile();
                            }
                        }
                    } else if (excel.indexOf(postfix.toLowerCase()) >= 0) {//如果是excel。则转换为Html。
                        String directory = secondPath + File.separator + "pdf" + File.separator + directoryName;//首次始发/pdf/1.数据库备份
                        String htmlFileName = fileName.replaceAll("." + postfix, ".html");
                        String outPath = directory + File.separator + htmlFileName;
                        try {
                            ExcelUtil.convertExceltoHtml(sourceFile.getCanonicalPath(), outPath);
                        } catch (Exception e) {//如果出现转换异常，视为被转换文件为空导致的转换异常。新建一个空文件。处理异常

                        }
                    }
                }
            }

            writer = new BufferedWriter(new FileWriter(convertFile));
            writer.write("0");
            writer.flush();
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
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
        String secondLevel = ajaxJson.getString("secondLevel");
        String threeLevel = ajaxJson.getString("threeLevel");
        String fileName = ajaxJson.getString("fileName");


//        String tbmPath =request.getSession().getServletContext().getRealPath(FILE_PATH+"/"+tbmName);
        String tbmPath =PropertiesValue.UPLOAD_FILE_PATH+File.separator+"startManage"+File.separator+tbmName;

        String resouceFilePath = tbmPath + File.separator + secondLevel + File.separator+ threeLevel+File.separator+fileName;
        String postfix = fileName.substring(fileName.lastIndexOf(".") );

        String images = ".jpg|.gif|.jpeg|.png|.bmp";
        String excel = ".xls|.xlsx";
        String pdf = ".pdf";
        File file = null;
        if(images.indexOf(postfix.toLowerCase()) >= 0){
            response.setContentType("image/jpeg");
            file = new File(resouceFilePath);
        }else if(".txt".indexOf(postfix.toLowerCase()) >= 0){
            response.setContentType("text/html; charset=utf-8");
            file = new File(resouceFilePath);
        }else if(excel.indexOf(postfix.toLowerCase()) >= 0){//如果文件时excel
            response.setContentType("text/html; charset=utf-8");
            String htmlFileName = fileName.replaceAll(postfix,".html");
            String pdfPath = tbmPath + File.separator+secondLevel + File.separator + "pdf" +File.separator+ threeLevel +File.separator +htmlFileName;
            file = new File(pdfPath);
            if(!file.exists()){//如果html文件不存在
                try {
                    ExcelUtil.convertExceltoHtml(resouceFilePath,file.getCanonicalPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }else if(pdf.indexOf(postfix.toLowerCase()) >= 0){
            response.setContentType("application/pdf");
            file = new File(resouceFilePath);
        }else{//如果不是文件，则转换成PDF格式
            response.setContentType("application/pdf");
            String pdfFileName = fileName.replaceAll(postfix,".pdf");
            String pdfPath = tbmPath + File.separator+secondLevel + File.separator + "pdf" +File.separator+ threeLevel +File.separator +pdfFileName;
            file = new File(pdfPath);
            if(!file.exists()){//如果pdf文件不存在
                Office2Pdf.converterFile(new File(resouceFilePath),file.getCanonicalPath(),"");
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
        String secondLevel = ajaxJson.getString("secondLevel");
//      String secondPath = request.getSession().getServletContext().getRealPath(FILE_PATH +"/" + tbmName + "/"+secondLevel);
        String secondPath = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"startManage"+File.separator+tbmName+File.separator+secondLevel;

        String filePath = secondPath + File.separator + "beingConverted.txt";
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
}

