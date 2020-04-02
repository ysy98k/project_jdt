package com.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;



/**
 * 上传工具类
 */

public class UploadUtil {

    private static final Integer FILE_SIZE = 10000000;

    /**
     * 上传压缩文件
     *
     * @param file
     * @param path
     * @return
     */
    public static JSONObject resolveCompressUploadFile(MultipartFile file, String path,Integer Mb) throws Exception {
        JSONObject returnInfo = new JSONObject();
        if (file.isEmpty()) {
            throw new Exception("上传文件不可以为空");
        }
        String oldFileName = file.getOriginalFilename();//文件原名
        String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀

        if (file.getSize() > (Mb*1000000)) {
            String message = "上传文件不得大于"+Mb+"M";
            throw new Exception(message);
        } else if (!prefix.equalsIgnoreCase("zip") && !prefix.equalsIgnoreCase("rar") && !prefix.equalsIgnoreCase("xlsx")) {
            throw new Exception("文件格式不正确，应当是压缩（.zip）文件或xlsx文件");
        }
        // 时间加后缀名保存
        String saveName = System.currentTimeMillis() + RandomUtils.nextInt(1000000) + "." + prefix;
        File targetFile = new File(path, saveName);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            returnInfo.put("status", Constants.EXECUTE_FAIL);
            returnInfo.put("message", "文件保存失败！");
            return returnInfo;
        }

        String filePath = path + File.separator + saveName;
        returnInfo.put("status", Constants.EXECUTE_SUCCESS);
        returnInfo.put("message", "文件保存成功");
        returnInfo.put("filePath", filePath);
        return returnInfo;

    }

    /**
     * 上传压缩文件
     *
     * @param file
     * @param path
     * @return
     */
    public static JSONObject fileUpload(MultipartFile file, String path, int fileSize) throws Exception {
        JSONObject returnInfo = new JSONObject();
        if (file.isEmpty()) {
            returnInfo.put("status", Constants.EXECUTE_FAIL);
            returnInfo.put("message", "文件保存失败！");
            return returnInfo;
        }
        String oldFileName = file.getOriginalFilename();//文件原名
        String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀

        if (file.getSize() > fileSize) {
            returnInfo.put("status", Constants.EXECUTE_FAIL);
            returnInfo.put("message", "文件大小超过" + fileSize / 1000000 + ",文件保存失败！");
            return returnInfo;
        } else if (!prefix.equalsIgnoreCase("zip") && !prefix.equalsIgnoreCase("rar") && !prefix.equalsIgnoreCase("xlsx")) {
            throw new Exception("文件格式不正确，应当是压缩（.zip）文件或xlsx文件");
        }
        File targetFile = new File(path);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            returnInfo.put("status", Constants.EXECUTE_FAIL);
            returnInfo.put("message", "文件保存失败！");
            return returnInfo;
        }
        returnInfo.put("status", Constants.EXECUTE_SUCCESS);
        returnInfo.put("message", "文件保存成功");
        return returnInfo;

    }

    public static JSONObject uploadxlsx(MultipartFile file, String path, int fileSize) throws Exception {
        JSONObject returnInfo = new JSONObject();
        if (file.isEmpty()) {
            returnInfo.put("status", Constants.EXECUTE_FAIL);
            returnInfo.put("message", "上传失败");
            return returnInfo;
        }
        String oldFileName = file.getOriginalFilename();//文件原名
        String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀

        if (file.getSize() > fileSize) {
            returnInfo.put("status", Constants.EXECUTE_FAIL);
            returnInfo.put("message", "文件大小超过" + fileSize / 1000000 + ",文件保存失败！");
            return returnInfo;
        } else if (!prefix.equalsIgnoreCase("xlsx")) {
            throw new Exception("文件格式不正确，应当为xlsx文件");
        }
        File targetFile = new File(path);
//        if(!targetFile.exists()){
//            targetFile.mkdirs();
//        }
//        try {
//            file.transferTo(targetFile);
//        } catch (IOException e) {
//            returnInfo.put("status", Constants.EXECUTE_FAIL);
//            returnInfo.put("message","文件保存失败！");
//            return  returnInfo;
//        }
        returnInfo.put("status", Constants.EXECUTE_SUCCESS);
        returnInfo.put("message", "文件保存成功");
        return returnInfo;
    }


    public static void download(HttpServletResponse response, String fileName, String filePath) throws IOException {

        /* 第一步:根据文件路径获取文件 */
        File file = new File(filePath);
        if (file.exists()) { // 文件存在
            /* 第二步：根据已存在的文件，创建文件输入流 */
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            /* 第三步：创建缓冲区，大小为流的最大字符数 */
            byte[] buffer = new byte[inputStream.available()]; // int available() 返回值为流中尚未读取的字节的数量
            /* 第四步：从文件输入流读字节流到缓冲区 */
            inputStream.read(buffer);
            /* 第五步： 关闭输入流 */
            inputStream.close();
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.replaceAll(" ", "").getBytes("utf-8"), "iso8859-1"));
            response.addHeader("Content-Length", "" + file.length());

            /* 第六步：创建文件输出流 */
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            /* 第七步：把缓冲区的内容写入文件输出流 */
            outputStream.write(buffer);
            /* 第八步：刷空输出流，并输出所有被缓存的字节 */
            outputStream.flush();
            /* 第九步：关闭输出流 */
            outputStream.close();
        }
    }


    /**
     * 下载多个文件
     * @param response
     * @param fileNames
     * @param filePath
     * @throws IOException
     */
    public static void downloadFiles(HttpServletResponse response, JSONArray fileNames, List<String> filePath) throws IOException {
        //设置压缩包的名字
        //解决不同浏览器压缩包名字含有中文时乱码的问题
       String zipName = "download.zip";
       response.setContentType("application/octet-stream");
       response.setHeader("Content-Disposition", "attachment;filename="+zipName);
       //设置压缩流，边压缩，边下载
        ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
        zipOutputStream.setMethod(ZipOutputStream.DEFLATED);//设置压缩方法

        DataOutputStream dataOutputStream = null;
        //循环将文件写入压缩流
        for(int i=0;i<filePath.size();i++){
            String path = filePath.get(i);
            String fileName = fileNames.getString(i);
            File file=new File(path);//要下载文件的路径

            zipOutputStream.putNextEntry(new ZipEntry(i+fileName));
            dataOutputStream = new DataOutputStream(zipOutputStream);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] b = new byte[fileInputStream.available()];
            int length = 0;
            while (  ( length = fileInputStream.read(b) ) != -1){
                dataOutputStream.write(b,0,length);
            }
            fileInputStream.close();
            zipOutputStream.closeEntry();
        }

        dataOutputStream.flush();
        dataOutputStream.close();
        zipOutputStream.close();


    }
}
