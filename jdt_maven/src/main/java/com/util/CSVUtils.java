package com.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.List;

public class CSVUtils {

    /**
     * CSV文件生成方法
     * @param head
     * @param dataList
     * @return
     */
    public static void createCSVFile(OutputStream out,List<Object> head, List<List<Object>> dataList) {

        BufferedWriter csvWtriter = null;
        try {
            // GB2312使正确读取分隔符","
            csvWtriter = new BufferedWriter(new OutputStreamWriter(out, "GB2312"), 1024);
            // 写入文件头部
            writeRow(head, csvWtriter);
            // 写入文件内容
            for (List<Object> row : dataList) {
                writeRow(row, csvWtriter);
            }
            csvWtriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvWtriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 写一行数据方法
     * @param row
     * @param csvWriter
     * @throws IOException
     */
    private static void writeRow(List<Object> row, BufferedWriter csvWriter) throws IOException {
        // 写入文件头部
        for (Object data : row) {
            StringBuffer sb = new StringBuffer();
            String rowStr = sb.append("\"").append(data).append("\",").toString();
            csvWriter.write(rowStr);
        }
        csvWriter.newLine();
    }


    /**
     * CSV文件生成方法
     * @param head
     * @return
     */
    public static void downloadJqgridData(OutputStream out,List<String> head,List<String> columnsList, JSONArray data) {

        BufferedWriter csvWtriter = null;
        try {
            // GB2312使正确读取分隔符","
            csvWtriter = new BufferedWriter(new OutputStreamWriter(out, "GB2312"), 1024);
            // 写入文件头部
            writeGqgridHead(head, csvWtriter);
            // 写入文件内容
            StringBuffer sb = new StringBuffer();
            for(int i =0;i<data.size();i++){
                writeJqgridRow(data.getJSONObject(i),columnsList, csvWtriter,sb);
            }

            csvWtriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvWtriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 写头
     * @param row
     * @param csvWriter
     * @throws IOException
     */
    private static void writeGqgridHead(List<String> row, BufferedWriter csvWriter) throws IOException {
        // 写入文件头部
        for (Object data : row) {
            StringBuffer sb = new StringBuffer();
            String rowStr = sb.append("\"").append(data).append("\",").toString();
            csvWriter.write(rowStr);
        }
        csvWriter.newLine();
    }
    /**
     * 写一行数据方法
     * @param row
     * @param csvWriter
     * @throws IOException
     */
    private static void writeJqgridRow(JSONObject row,List<String> columnsList, BufferedWriter csvWriter,StringBuffer sb) throws IOException {

        // 写入文件头部
        for (String column : columnsList) {
            sb.setLength(0);
            String rowStr = sb.append("\"").append( row.getString(column) == null?"":row.getString(column)).append("\",").toString();
            csvWriter.write(rowStr);
        }
        csvWriter.newLine();
    }


    public static class ConstantsUtil {

        //aasccs数据源名称。用于查询其中的字典表信息
        public static final String AASCCS_DATA_SOURCE = "aasccsDataSource";
        //jdt数据源
        public static final String JDT_DATA_SOURCE = "dataSource";

        public static final String SCHEMA_NAME = "raisng";


    }
}
