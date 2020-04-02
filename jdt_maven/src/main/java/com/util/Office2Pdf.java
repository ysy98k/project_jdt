package com.util;


import com.artofsolving.jodconverter.BasicDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.DocumentFormatRegistry;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.net.ConnectException;
import java.nio.file.Files;
import java.util.regex.Pattern;
import java.util.zip.ZipInputStream;

public class Office2Pdf {




    public static File office2pdf(String inputFilePath) {

        try {
            File inputFile = new File(inputFilePath);
            // 转换后的文件路径
            String outputFilePath_end = getOutputFilePath(inputFilePath);
            File outputFile = new File(outputFilePath_end);
            if (!inputFile.exists()) {
                return null;
            }
            if(outputFile.exists()){
                return outputFile;
            }
            return converterFile(inputFile, outputFilePath_end, inputFilePath);

        }catch (Exception e) {
            return null;
        }

    }

    public static File converterFile(File inputFile, String outputFilePath_end, String inputFilePath) throws IOException {
        File outputFile = new File(outputFilePath_end);
        // 假如目标路径不存在,则新建该路径
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }
        OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
        try {
            connection.connect();
            DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
            converter.convert(inputFile,outputFile);
            connection.disconnect();
        } catch (ConnectException e) {
            e.printStackTrace();
        }
        return outputFile;
    }

    public static File converterFile2(String inputFilePath, String outputFilePath_end,String odtDir) throws IOException {
        File inputFile = new File(inputFilePath);

        String postfix = getPostfix(inputFilePath);
        String odtPath = null;
        if("txt".equals(postfix)){//如果是txt文件。则转换成odt文件在转换
            String fileName = inputFile.getName();
            String odtFileName = fileName.replaceAll("." + getPostfix(fileName), ".odt");
            File file = new File(odtDir);
            if( !file.exists()){
                file.mkdirs();
            }
            odtPath = odtDir + File.separator + odtFileName;
            File odtFile  = new File(odtPath);

            FileUtils.copyFile(inputFile,new File(odtPath));
            inputFile = odtFile;
        }

        File outputFile = new File(outputFilePath_end);
        // 假如目标路径不存在,则新建该路径
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }

        OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
        try {
            connection.connect();
            DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
            converter.convert(inputFile,outputFile);
            connection.disconnect();
        } catch (ConnectException e) {
            e.printStackTrace();
        }finally {
            if(odtPath != null){
                com.util.FileUtils.deleteFolder(odtPath);
            }
        }
        return outputFile;
    }



    public static Process startOpenOffice() throws IOException {
        String openOfficeHome = getOfficeHome();
        String command = openOfficeHome+"/program/soffice.exe -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\"";
        Process p = Runtime.getRuntime().exec(command);//线程
        return p;
    }
    public static void closeOpenOffice(Process p){
        p.destroy();//切断线程
    }




    public static String getOutputFilePath(String inputFilePath) {
        String outputFilePath = inputFilePath.replaceAll("." + getPostfix(inputFilePath), ".pdf");
        return outputFilePath;
    }

    /**
     * 获取inputFilePath的后缀名，如："e:/test.pptx"的后缀名为："pptx"<br>
     *
     * @param inputFilePath
     * @return
     */
    public static String getPostfix(String inputFilePath) {
        return inputFilePath.substring(inputFilePath.lastIndexOf(".") + 1);
    }
    /**
     * 根据操作系统的名称，获取OpenOffice.org 3的安装目录<br>
     * 如我的OpenOffice.org 3安装在：C:/Program Files (x86)/OpenOffice.org 3<br>
     *
     * @return OpenOffice.org 3的安装目录
     */
    public static String getOfficeHome() {
        String osName = System.getProperty("os.name");
        if (Pattern.matches("Linux.*", osName)) {
            return "/opt/openoffice4";
        } else if (Pattern.matches("Windows.*", osName)) {
            return "D:/Program Files/OpenOffice 4";
        }
        return null;
    }



    @Test
    public void main() throws IOException {
        String t = getOfficeHome();
        String file1Path = "E:\\厂内交货\\测量数据\\系统软件部人员技能汇总表.docx";
        String path2 = "E:\\新建文本文档.txt";
        String path222 = "E:\\新建文本文档.odt";
        String path22 = "E:\\新建文本文档.pdf";
        String path5 = "C:\\Users\\Administrator\\Desktop\\厂内交货\\原始测量数据\\新建 Microsoft Office Excel 工作表.xlsx";

        File odtFile = new File(path222);
        FileUtils.copyFile(new File(path2),odtFile);
        File inputFile = odtFile;
        Office2Pdf.converterFile(inputFile,path22,"");

        if(1==1){
            return;
        }


        File file1 = new File(file1Path);
        File file2 = new File("");
        FileInputStream inputStream1 = new FileInputStream(file1);
        String path3 = "E:\\系统软件部人员技能汇总表.docx";
        String path4 = "E:\\系统软件部人员技能汇总表.pdf";
        File file3 = new File(path3);
        file3.createNewFile();
        File file4 = new File(path4);
        file4.createNewFile();
        OutputStream outputStream = new FileOutputStream(file3);
        IOUtils.copy(inputStream1,outputStream);
        IOUtils.closeQuietly(outputStream);
        office2pdf(path3);

        OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
        try {
            connection.connect();
            DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
            converter.convert(file3,file4);
            connection.disconnect();
            //p.destroy();//切断线程
        } catch (ConnectException e) {
            e.printStackTrace();
        }
    }

}
