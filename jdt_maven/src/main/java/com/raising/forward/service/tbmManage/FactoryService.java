package com.raising.forward.service.tbmManage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException;
import com.baosight.common.basic.dao.RestDao;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.RedisUtils;
import com.baosight.common.utils.StringUtils;
import com.raising.forward.entity.tbmManage.TbmServiceInfo;
import com.raising.forward.service.PropertiesValue;
import com.util.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipInputStream;


/**
 * 厂内发货
 */
@Service("factoryService")
public class FactoryService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(FactoryService.class);




    @Autowired
    private RestDao dao;


    public JSONObject getFileTable(String ajaxParam)  {
        JSONObject returnInfo = new JSONObject();
        List<JSONObject> dataList = new LinkedList<>();
        JSONObject ajaxJson = JSONObject.parseObject(ajaxParam);
        String factoryType = ajaxJson.getString("factoryType");
        String tbmName = ajaxJson.getString("tbmName");


        String path = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"factory"+File.separator+tbmName+File.separator+"厂内交货"+File.separator+factoryType;
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
            String fileSize = FileUtils.getFileSize(temp);
            String fileTime = FileUtils.getTime(temp);
            json.put("fileName",temp.getName());
            json.put("fileSize", fileSize);
            json.put("time", fileTime);
            dataList.add(json);
        }
        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        returnInfo.put("dataList",dataList);
        return returnInfo;
    }

    /**
     * 上传压缩文件。保存在指定目录下
     * 将符合条件的文件，转换成pdf文件。
     * 不符合条件的文件，放入提示集合
     * @param file
     * @param tbmName
     * @return
     * @throws Exception
     */
    public JSONObject dataUpload( File file, String tbmName,String token) {
        JSONObject returnInfo = new JSONObject();
        dao.setHost(PropertiesValue.CCS_ADRESS);
        dao.setServiceName(PropertiesValue.CCS_APP_PATH);

        //文件大小，格式校验
        List<String> factoryType = new ArrayList<>();
        JSONObject param = new JSONObject();
        param.put("token",token);
        JSONObject get = dao.invoke("get", "/api/query/item/factory", param);
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

        List<String> failName = null;
        try {
            failName = operatorZip(file,factoryType,tbmName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(failName.size() < 1) {
            returnInfo.put("status", Constants.EXECUTE_SUCCESS);
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
            logger.debug("debugMessage:"+failNameStr+ "未成功保存");
        }

        return returnInfo;
    }


    private List<String> operatorZip(File file,List<String> factoryType,String tbmName) throws IOException {
        List<String> failName = new ArrayList<>();
        String tbmNamePath = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"factory"+File.separator+tbmName;
        FileUtils.deleteFolder(tbmNamePath);//先删除旧文件
        //ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream(), Charset.forName("GBK"));
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file), Charset.forName("GBK"));
        java.util.zip.ZipEntry zipEntry;


        Long progress = 0L;
        while ((zipEntry = zipInputStream.getNextEntry()) != null){
            String zipEntryName = zipEntry.getName();
            String outPath = zipEntryName.replaceAll("\\+", "/");
            boolean checkResult =  checkUploadFile(outPath,factoryType,failName);
            if(checkResult == false){//如果通过校验
                continue;
            }
            //判断文件父目录是否存在
            String DirectoryPath = tbmNamePath +File.separator+outPath.substring(0,outPath.lastIndexOf("/"));
            File file1 = new File(DirectoryPath);
            if(!file1.exists()){
                file1.mkdirs();
            }
            String currentFilePath = tbmNamePath + File.separator + outPath;
            if(new File(currentFilePath).isDirectory()){//如果当前文件是目录，则跳过
                continue;
            }
            OutputStream outputStream = null;
            outputStream = new FileOutputStream(currentFilePath);
            byte[] bytes = new byte[zipInputStream.available()];
            int len;
            //当read的返回值为-1，表示碰到当前项的结尾，而不是碰到zip文件的末尾
            while ((len = zipInputStream.read(bytes)) > 0) {
                progress = progress + len;
                outputStream.write(bytes, 0, len);
            }
            outputStream.flush();
            outputStream.close();
            //必须调用closeEntry()方法来读入下一项
            zipInputStream.closeEntry();
        }
        zipInputStream.close();
        return failName;
    }

    private boolean checkUploadFile(String outPath,List<String> factoryType,List<String> failName){
        if(outPath.indexOf("/")<0){//如果压缩目录下的一级子目录包含文件，则跳过
            failName.add(outPath);
            return false;
        }
        String oneLevel = outPath.substring(0,outPath.indexOf("/"));
        if(!"厂内交货".equals(oneLevel)){//如果一级子目录不是厂内交货则跳过
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

    public void convertFileToPdf(String tbmName){
        String factoryPath = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"factory"+File.separator+tbmName+File.separator+"厂内交货";
        String filePath = factoryPath + File.separator + "beingConverted.txt";
        File convertFile = new File(filePath);//用来做转换标示
        File parentFile = convertFile.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        //convertFile写1
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(convertFile));
            writer.write("1");
            writer.flush();
            writer.close();

            String factoryDirectoryPath = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"factory"+File.separator+tbmName+File.separator+"厂内交货";
            File factory = new File(factoryDirectoryPath);
            if(!factory.exists()){
                return;
            }
            File[] files = factory.listFiles();
            if(files.length < 1){
                return;
            }

            String fileType =  ".doc|.docx|.csv|.ppt|.pptx";
            String excel = ".xls|.xlsx";
            for(int i =0;i<files.length;i++){
                File tempFile = files[i];//厂内交货下的一级子目录。文件夹对象
                if(tempFile.isFile()){//过滤文件
                    continue;
                }
                File[] files2 = tempFile.listFiles();
                if(files2.length < 1){
                    continue;
                }
                String directoryName  = tempFile.getName();//二级目录名
                for( int k = 0;k<files2.length;k++){
                    File sourceFile = files2[k];
                    if(sourceFile.isDirectory()){//如果是文件夹，则跳过
                        continue;
                    }
                    //如果该文件不是doc类型。但是内容为空，则不转换
                    String fileName = sourceFile.getName();
                    String postfix =fileName.substring(fileName.lastIndexOf(".")+1);

                    if(fileType.indexOf(postfix.toLowerCase()) >= 0 ){  //如果匹配，则转换
                        String directory = factoryDirectoryPath + File.separator + "pdf" + File.separator + directoryName;
                        String outputFileName = fileName.replaceAll("." + postfix, ".pdf");
                        String outPath = directory + File.separator + outputFileName;
                        try {
                            Office2Pdf.converterFile(sourceFile, outPath, sourceFile.getCanonicalPath());
                        }catch (OpenOfficeException e){//如果出现转换异常，视为被转换文件为空导致的转换异常。新建一个空pdf文件。处理异常
                            File file = new File(outPath);
                            if(!file.exists()){
                                file.createNewFile();
                            }
                        }
                    }else if(excel.indexOf(postfix.toLowerCase()) >= 0){//如果是excel。则转换为Html。
                        String directory = factoryDirectoryPath + File.separator + "pdf" + File.separator + directoryName;
                        String outputFileName = fileName.replaceAll("." + postfix, ".html");
                        String outPath = directory + File.separator + outputFileName;
                        try {
                            ExcelUtil.convertExceltoHtml(sourceFile.getCanonicalPath(),outPath);
                        }catch (Exception e){//如果出现转换异常，视为被转换文件为空导致的转换异常。新建一个空文件。处理异常

                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //convertFile写0
            try {
                writer = new BufferedWriter(new FileWriter(convertFile));
                writer.write("0");
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }




    public void download(String tbmName,JSONArray fileNames,String factoryType,HttpServletResponse response) throws IOException {
        List<String> paths = new ArrayList<>();
        for(int i=0;i<fileNames.size();i++){
            String fileName = fileNames.getString(i);
            //String path = request.getSession().getServletContext().getRealPath("WEB-INF/uploadfiles/factory/"+tbmName+"/厂内交货/"+factoryType+"/"+fileName);
            String path = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"factory"+File.separator+tbmName+File.separator+"厂内交货"+File.separator+factoryType+File.separator+fileName;
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

    public JSONObject deleteFiles(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject ajaxJson = JSONObject.parseObject(ajaxParam);
        String tbmName = ajaxJson.getString("tbmName");
        String factoryType = ajaxJson.getString("factoryType");
        JSONArray fileNames = ajaxJson.getJSONArray("fileNames");
        if(StringUtils.isNullOrEmpty(tbmName) || StringUtils.isNullOrEmpty(factoryType) || fileNames == null || fileNames.size() < 1){
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("message","执行失败");
            return returnInfo;
        }
        for(int i=0;i<fileNames.size();i++){
            String fileName = fileNames.getString(i);
            String path = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"factory"+File.separator+tbmName+File.separator+"厂内交货"+File.separator+factoryType+File.separator+fileName;
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

    /**
     * 文件预览
     * @param ajaxParam
     * @param response
     * @throws IOException
     */
    public void previewFile(String ajaxParam,HttpServletResponse response) throws IOException {
        ajaxParam = URLDecoder.decode(ajaxParam, "UTF-8");
        JSONObject ajaxJson = JSONObject.parseObject(ajaxParam);
        String factoryType = ajaxJson.getString("factoryType");
        String tbmName = ajaxJson.getString("tbmName");
        String fileName = ajaxJson.getString("fileName");


        //String filePath =request.getSession().getServletContext().getRealPath("WEB-INF/uploadfiles/factory/"+tbmName+"/厂内交货");
        String filePath =PropertiesValue.UPLOAD_FILE_PATH+File.separator+"factory"+File.separator+tbmName+File.separator+"厂内交货";
        String resouceFilePath = filePath + File.separator + factoryType + File.separator+ fileName;
        String postfix = fileName.substring(fileName.lastIndexOf(".") );
        String prefix = fileName.substring(0,fileName.lastIndexOf("."));
        String images = ".jpg|.gif|.jpeg|.png|.bmp";
        String excel = ".xls|.xlsx";
        String pdf = ".pdf";
        File file = null;
        if(images.indexOf(postfix.toLowerCase()) >= 0){
            response.setContentType("image/jpeg");
            file = new File(filePath+File.separator+factoryType+File.separator+fileName);
        }else if(excel.indexOf(postfix.toLowerCase()) >= 0){//如果文件时excel
            response.setContentType("text/html; charset=utf-8");
            String htmlFileName = fileName.replaceAll(postfix,".html");
            String pdfPath = filePath + File.separator + "pdf" + File.separator+factoryType+File.separator+htmlFileName;
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
            String pdfPath = filePath + File.separator + "pdf" + File.separator+factoryType+File.separator+pdfFileName;
            file = new File(pdfPath);
            if(!file.exists()){//如果pdf文件不存在
                //Process process = Office2Pdf.startOpenOffice();//开启openOffice进程
                Office2Pdf.converterFile(new File(resouceFilePath),file.getCanonicalPath(),"");
                //Office2Pdf.office2pdf(file.getCanonicalPath());
                //Office2Pdf.closeOpenOffice(process);//关闭进程
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
        //String factoryPath = request.getSession().getServletContext().getRealPath("WEB-INF/uploadfiles/factory/" + tbmName + "/厂内交货");
        String factoryPath =PropertiesValue.UPLOAD_FILE_PATH+File.separator+"factory"+File.separator+tbmName+File.separator+"厂内交货";
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


    public void downloadTemplet(HttpServletResponse response) throws IOException {
        String path =PropertiesValue.UPLOAD_FILE_PATH+File.separator+"factory"+File.separator+"厂内交货.zip";
        UploadUtil.download(response,"厂内交货.zip",path);

    }







}

