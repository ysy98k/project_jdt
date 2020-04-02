package com.util;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileUtils {

    /**
     * 根据地址，删除文件或者文件夹
     * @param sPath
     * @return
     */
    public static boolean deleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除文件
     * @param sPath
     * @return
     */
    private static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 删除目录
     * @param sPath
     * @return
     */
    private static boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 得到文件的大小 ...BT,..KB,...MB,..GB格式
     * @param file
     * @return
     */
    public static String getFileSize(File file){
        String size = "";
        if(file.exists() && file.isFile()){
            long fileS = file.length();
            DecimalFormat df = new DecimalFormat("#.00");
            if (fileS < 1024) {
                size = df.format((double) fileS) + "BT";
            } else if (fileS < 1048576) {
                size = df.format((double) fileS / 1024) + "KB";
            } else if (fileS < 1073741824) {
                size = df.format((double) fileS / 1048576) + "MB";
            } else {
                size = df.format((double) fileS / 1073741824) +"GB";
            }
        }else if(file.exists() && file.isDirectory()){
            size = "";
        }else{
            size = "0BT";
        }
        return size;
    }

    /**
     * 得到赏赐修改时间
     * @param file
     * @return
     */
    public static String getTime(File file){
        if(file.exists() && file.isFile()){
            Calendar cal = Calendar.getInstance();
            long time = file.lastModified();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            cal.setTimeInMillis(time);
            return formatter.format(cal.getTime());
        }
        return "";
    }

    /**

     * 移动文件

     * @param filePath 文件路径 -- 从哪里移动

     * @param destPath 目标路径 -- 移动到哪里

     */

    public static void moveFile(String filePath, String destPath){
        saveAsFile(new File(filePath), destPath);//拷贝
        deleteFolder(filePath);//删除
    }


    /**
     * 保存文件 -- 可以是文件，也可以是一个文件夹
     * @param file 文件对象 --从哪里拷贝
     * @param destPath 要保存的文件路径 -- 拷贝到哪里
     */
    public static void saveAsFile(File file, String destPath){
        if (file.isDirectory()) {
            //文件夹
            File[] files = file.listFiles();// 声明目录下所有的文件 files[];
            for (File filei : files) {// 遍历目录下所有的文件
                saveAsFile(filei,destPath+File.separator+filei.getName());// 把每个文件用这个方法进行迭代
            }
        } else {
            //文件
            FileInputStream in = null ;
            try {
                in = new FileInputStream(file);
                saveAsFile(in, destPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if(in != null){
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**

     * 保存文件 --只能处理单个文件，不能是文件夹
     * @param in 文件输入流对象
     * @param destPath 要保存的路径，含有文件后缀名
     */
    public static void saveAsFile(InputStream in, String destPath){
        FileOutputStream out = null;
        BufferedOutputStream Bout = null;
        try {
            byte[] buf = new byte[1024];
            File file = new File(destPath);
            if (!file.exists()) {
                (new File(file.getParent())).mkdirs();
            }
            out = new FileOutputStream(file);
            Bout = new BufferedOutputStream(out);
            int b;
            while ((b = in.read(buf)) != -1) {
                Bout.write(buf,0,b);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(Bout != null){
                try {
                    Bout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
