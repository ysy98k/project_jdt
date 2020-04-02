package com.raising.forward.service;

import com.alibaba.fastjson.JSONObject;
import com.raising.forward.entity.*;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class SummaryInfoService {

    private SurveyInfoService surveyInfoService = new SurveyInfoService();

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;


    public JSONObject getAllLastPointsData(int selectedid) {
        JSONObject returnInfo = new JSONObject();
        List<String> sheetList = new ArrayList<>();
        List<List> reportSheetList = new ArrayList<>();
        String tenant = (String) request.getSession().getAttribute("tenant");

        List<JSONObject> ProjectInfos = surveyInfoService.getProjectByID(selectedid, tenant, sqlSessionTemplate);
        if (ProjectInfos.isEmpty()) {
            returnInfo.put("msg", "当前项目不存在！");
            return returnInfo;
        }
        JSONObject project = ProjectInfos.get(0);
        // String projectName = (String)project.get("projectname");
        int projectID = (Integer) project.get("projectid");
        Engineering engineering = surveyInfoService.getEngineeringByProjectID(projectID, tenant, sqlSessionTemplate);
        List<ReportRelation> reportRelations = surveyInfoService.getReportRelation(engineering.getEngineeringUUID(), null, null, tenant, sqlSessionTemplate);
        if (reportRelations.size() == 1) {
            List<ResultsTable> resultTables = surveyInfoService.getResultTableListByResultTableUUID(reportRelations.get(0).getResultTableUUID(), tenant, sqlSessionTemplate);
            for (ResultsTable resultTable : resultTables) {
                sheetList.add(resultTable.getResultsTableName());
                String resultID = resultTable.getResultId();
                if (!resultTable.getResultsTableName().contains("巡视")) {
                    List<ReportData> reportDatas = surveyInfoService.getReportSheetByResultID(resultID, tenant, sqlSessionTemplate);
//                    // not patrolInfoList
//                    // get reportSheets by points and report data is the last data.
//                    // table name,uuid
//                    String tableName = resultTable.getResultsTableName();
//
//                    List<ReportSheet> reportSheets = getLastAllReportSheetDataByResultID(resultID, tenant, sqlSessionTemplate);
                    if (!reportDatas.isEmpty()) {
                        reportSheetList.add(reportDatas);
                    }
                }
            }
        } else if (reportRelations.size() > 1) {
            List<ResultsTable> resultTables = surveyInfoService.getResultTableListByResultTableUUID(reportRelations.get(0).getResultTableUUID(), tenant, sqlSessionTemplate);
            for (ResultsTable resultTable : resultTables) {
                sheetList.add(resultTable.getResultsTableName());
                String resultID = resultTable.getResultId();
                if (!resultTable.getResultsTableName().contains("巡视")) {
                    String tableName = resultTable.getResultsTableName();
                    List<ReportSheet> reportDatas = getLastAllReportSheetDataByResultID(engineering.getEngineeringUUID(), tableName, tenant, sqlSessionTemplate);

                    // List<ReportSheet> reportSheets = getLastAllReportSheetDataByResultID(resultID, tenant, sqlSessionTemplate);
                    if (!reportDatas.isEmpty()) {
                        reportSheetList.add(reportDatas);
                    }
                }
            }
        } else {
            returnInfo.put("msg", "当前项目没有上传表格");
            return returnInfo;
        }

//        ReportRelation reportRelation = surveyInfoService.getLastReportRelationByEngUUID(engineering.getEngineeringUUID(), tenant, sqlSessionTemplate);
//
//        System.out.println("get last Date is : " + reportRelation.getReportDate());
//        List<ResultsTable> resultTables = surveyInfoService.getResultTableListByResultTableUUID(reportRelation.getResultTableUUID(), tenant, sqlSessionTemplate);
//
//        for (ResultsTable resultTable : resultTables) {
//            sheetList.add(resultTable.getResultsTableName());
//            String resultID = resultTable.getResultId();
//            if (!resultTable.getResultsTableName().contains("巡视")) {
//                // not patrolInfoList
//                // get reportSheets by points and report data is the last data.
//                // table name,uuid
//                String tableName = resultTable.getResultsTableName();
//
//                List<ReportSheet> reportSheets = getLastAllReportSheetDataByResultID(resultID, tenant, sqlSessionTemplate);
//
//
//                if (!reportSheets.isEmpty()) {
//                    reportSheetList.add(reportSheets);
//                }
//            }
//        }
        System.out.println(reportSheetList);
        returnInfo.put("sheetlist", sheetList);
        returnInfo.put("reportSheetList", reportSheetList);
        return returnInfo;
    }

    public JSONObject getPointsData(int selectedid, List<String> pointNames, String column) {
        JSONObject returnInfo = new JSONObject();
        List<SeriesData> seriesDatas = new ArrayList<>();
        String tenant = (String) request.getSession().getAttribute("tenant");
        List<JSONObject> ProjectInfos = surveyInfoService.getProjectByID(selectedid, tenant, sqlSessionTemplate);
        if (ProjectInfos.isEmpty()) {
            returnInfo.put("msg", "当前项目不存在！");
            return returnInfo;
        }
        JSONObject project = ProjectInfos.get(0);
        //String projectName = (String)project.get("projectname");
        int projectID = (Integer) project.get("projectid");
        Engineering engineering = surveyInfoService.getEngineeringByProjectID(projectID, tenant, sqlSessionTemplate);

        for (String point : pointNames) {
            List<List> lineDatas = new ArrayList<>();
            List<ReportLineData> reportLineDatas = getPointByPointNameAndEnginnering(engineering.getEngineeringUUID(), point, tenant, sqlSessionTemplate);
            for (ReportLineData reportLineData : reportLineDatas) {
                List<Object> lineData = new ArrayList<>();
                lineData.add(reportLineData.getReportDate().getTime());
                if (column.contains("changeRate")) {
                    lineData.add(reportLineData.getChangeRate());
                } else {
                    lineData.add(reportLineData.getCumulativeVariation());
                }
                lineDatas.add(lineData);
            }
            SeriesData seriesData = new SeriesData();
            seriesData.setName(point);
            seriesData.setData(lineDatas);
            seriesDatas.add(seriesData);
        }
        System.out.println(seriesDatas);
        returnInfo.put("seriesDatas", seriesDatas);
        return returnInfo;
    }

    private List<ReportSheet> getLastAllReportSheetDataByResultID(String engineeringUUID, String tableName, String tenant, SqlSessionTemplate sqlSessionTemplate) {
        String LineSql = "com.raising.forward.mapper.ReportData.getLastAllReportSheetDataByResultID";
        System.out.println("Begin to ReportSheet sheet");
        JSONObject paramInfo = new JSONObject();
        paramInfo.put("tenant", tenant);
        paramInfo.put("engineeringUUID", engineeringUUID);
        paramInfo.put("tableName", tableName);
        return sqlSessionTemplate.selectList(LineSql, paramInfo);
    }

    private List<ReportLineData> getPointByPointNameAndEnginnering(String EngineeringUUID, String point, String tenant, SqlSessionTemplate sqlSessionTemplate) {
        String LineSql = "com.raising.forward.mapper.ReportData.getPointByPointNameAndEnginnering";
        System.out.println("Begin to get point");
        JSONObject paramInfo = new JSONObject();
        paramInfo.put("tenant", tenant);
        paramInfo.put("EngineeringUUID", EngineeringUUID);
        paramInfo.put("point", point);
        return sqlSessionTemplate.selectList(LineSql, paramInfo);
    }
}
