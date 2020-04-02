package com.raising.forward.service;

import com.alibaba.fastjson.JSONObject;
import com.raising.backstage.controller.SectionManageController;
import com.raising.forward.entity.*;
import com.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.util.List;

import static com.util.ExcelUtil.getCellFormatValue;

@Service
public class SurveyInfoService {

    private static final Logger logger = LoggerFactory.getLogger(SectionManageController.class);
    private String reportTimes;
    private Date reportDate;
    private Engineering engineering;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public JSONObject getData(int selectedid, String reportTime) {

        JSONObject returnInfo = new JSONObject();
        List<PatrolInfo> patrolInfoList = new ArrayList<>();
        List<String> sheetList = new ArrayList<>();
        List<List> reportSheetList = new ArrayList<>();
        ReportRelation reportRelation = null;
        String tenant = (String) request.getSession().getAttribute("tenant");
        List<ReportRelation> reportRelations;
        List<JSONObject> ProjectInfos = getProjectByID(selectedid, tenant, sqlSessionTemplate);

        if (ProjectInfos.isEmpty()) {
            returnInfo.put("msg", "当前项目不存在！");
            return returnInfo;
        }
        JSONObject project = ProjectInfos.get(0);
        String projectName = (String) project.get("projectname");
        int projectID = (Integer) project.get("projectid");
        System.out.println("项目名称: " + projectName);
        Engineering engineering = getEngineeringByProjectID(projectID, tenant, sqlSessionTemplate);
        if (engineering == null) {
            returnInfo.put("msg", "没有此项目的数据，请插入数据！");
            return returnInfo;
        } else {
            reportRelations = getReportRelation(engineering.getEngineeringUUID(), null, null, tenant, sqlSessionTemplate);
            if (reportTime == null) {
                reportRelation = getLastReportRelationByEngUUID(engineering.getEngineeringUUID(), tenant, sqlSessionTemplate);
            } else {
                List<ReportRelation> oneReportRelation = getReportRelation(engineering.getEngineeringUUID(), reportTime, null, tenant, sqlSessionTemplate);
                if (oneReportRelation.size() == 1) {
                    reportRelation = oneReportRelation.get(0);
                }
            }

            List<ResultsTable> resultTables = getResultTableListByResultTableUUID(reportRelation.getResultTableUUID(), tenant, sqlSessionTemplate);

            for (ResultsTable resultTable : resultTables) {
                sheetList.add(resultTable.getResultsTableName());
                String resultID = resultTable.getResultId();
                if (resultTable.getResultsTableName().contains("巡视")) {
                    patrolInfoList = getPatrolSheetByResultTableID(resultID);
                    for (PatrolInfo patrolInfo : patrolInfoList) {
                        patrolInfo.setSheetName(resultTable.getResultsTableName());
                    }
                    System.out.println(patrolInfoList);
                } else {
                    List<ReportData> reportDatas = getReportSheetByResultID(resultID, tenant, sqlSessionTemplate);
                    if (!reportDatas.isEmpty()) {
                        reportSheetList.add(reportDatas);
                    }
                }
            }
        }

        List<PointAttribute> pointAttributes = getPointAttribute(engineering.getEngineeringUUID(), null, tenant, sqlSessionTemplate);

        returnInfo = getResultData(reportSheetList, pointAttributes, returnInfo);
        returnInfo.put("sheetlist", sheetList);
        returnInfo.put("patrolInfoList", patrolInfoList);
        returnInfo.put("reportSheetList", reportSheetList);
        returnInfo.put("engineering", engineering);
        returnInfo.put("reportTimes", reportRelation.getReportTime());
        returnInfo.put("reportDate", reportRelation.getReportDate());
        returnInfo.put("reportRelations", reportRelations);
        returnInfo.put("pointAttributes", pointAttributes);
        return returnInfo;
    }

    public JSONObject saveAndReportData(File file, int selectedid) {
        JSONObject returnInfo = new JSONObject();
        Workbook workbook = ExcelUtil.readExcel(file);
        Sheet sheet;
        List<String> sheetList = new ArrayList<>();
        List<List> reportSheetList = new ArrayList<>();
        List<PatrolInfo> patrolInfoList = new ArrayList<>();
        ReportRelation reportRelation = null;
        boolean checkEngineering = false;
        boolean checkReportRelation = false;
        boolean checkResultTable = false;


        String tenant = (String) request.getSession().getAttribute("tenant");

        List<JSONObject> ProjectInfos = getProjectByID(selectedid, tenant, sqlSessionTemplate);
        if (ProjectInfos.isEmpty()) {
            returnInfo.put("msg", "当前项目不存在！");
            return returnInfo;
        }
        JSONObject project = ProjectInfos.get(0);
        String projectName = (String) project.get("projectname");
        int projectID = (Integer) project.get("projectid");
        engineering = new Engineering();
        for (int i = 1; i < workbook.getNumberOfSheets(); i++) {
            sheet = workbook.getSheetAt(i);
            String sheetname = sheet.getSheetName();
            if (sheetname.contains("附页")) {
                engineering = getEngineeringInfo(sheet, engineering);
                if (!engineering.getProject().equals(projectName)) {
                    returnInfo.put("msg", "传入文件项目与当前项目不符合，请确认！");
                    return returnInfo;
                }
                // get engineer by id
                // Engineering engineeringInDB = getEngineeringByProject(engineering.getProject(), tenant, sqlSessionTemplate);
                Engineering engineeringInDB = getEngineeringByProjectID(projectID, tenant, sqlSessionTemplate);
                if (engineeringInDB == null) {
                    engineering.setEngineeringUUID();
                    engineering.setProjectID(projectID);
                    if (engineering.getEngineeringUUID() != null) {
                        int result = insertEngineering(engineering, tenant, sqlSessionTemplate);
                        if (result > 0) {
                            checkEngineering = true;
                            System.out.println("insert success");
                        } else {
                            returnInfo.put("msg", "传入文件的附页项目信息格式有误，请确认！");
                            return returnInfo;
                        }
                    }
                } else {
                    if (engineeringInDB.getEngineeringName() == null) {
                        engineering.setOldEngineeringUUID(engineeringInDB.getEngineeringUUID());
                        int result = updateEngineering(engineering, tenant, sqlSessionTemplate);
                        if (result < 0) {
                            returnInfo.put("msg", "传入文件的附页项目信息格式有误，请确认！");
                            return returnInfo;
                        }
                    } else {
                        engineering = engineeringInDB;
                    }
                    checkEngineering = true;
                }
            }

            if (sheetname.contains("管线") || sheetname.contains("管片") || sheetname.contains("地表") || sheetname.contains("建筑")) {
                List<ReportSheet> reportSheetInfoList = new ArrayList<>();
                setReportSheet(reportSheetInfoList, sheet, tenant, sqlSessionTemplate);
                System.out.println(reportSheetInfoList);
                reportSheetList.add(reportSheetInfoList);
            }

            if (sheetname.contains("巡视")) {
                patrolInfoList = getPatrolSheet(sheet);
            }

            if (!sheetname.contains("封面") && !sheetname.contains("附页") && !sheetname.contains("成果")) {
                sheetList.add(sheetname);
            }
        }
        if (checkEngineering) {
            List<ReportRelation> reportRelations = getReportRelation(engineering.getEngineeringUUID(), reportTimes, reportDate, tenant, sqlSessionTemplate);
            if (reportRelations.isEmpty()) {
                reportRelation = new ReportRelation();
                reportRelation.setResultTableUUID();
                if (reportRelation.getResultTableUUID() != null) {
                    reportRelation.setEngineeringUUID(engineering.getEngineeringUUID());
                    reportRelation.setReportTime(reportTimes);
                    reportRelation.setReportDate(reportDate);
                    int result = insertReportRelation(reportRelation);
                    if (result > 0) {
                        checkReportRelation = true;
                        System.out.println("insert report relation success");
                    } else {
                        System.out.println("insert report relation failure");
                    }
                }
            } else {
                System.out.println("ReportRelation already in DB. So data is already in DB");
                reportRelation = reportRelations.get(0);
                checkReportRelation = false;
            }
        } else {
            System.out.println("No Engineering in DB and Can not insert it");
        }

        if (checkReportRelation) {
            int result = prepareInsertResultTable(reportRelation.getResultTableUUID(), sheetList, tenant);
            if (result > 0) {
                checkResultTable = true;
                System.out.println("ResultTable insert success");
            } else if (result == 0) {
                checkResultTable = true;
                System.out.println("Do not need insert ResultTable");
            } else {
                System.out.println("ResultTable insert failure");
            }
        } else {
            System.out.println("No reportRelation in DB and Can not insert it");
        }

        if (checkResultTable) {
            int insertPatrolSheetResult = prepareInsertPatrolSheet(reportRelation, patrolInfoList, tenant);
            if (insertPatrolSheetResult > 0) {
                System.out.println("PatrolSheet insert success");
            } else if (insertPatrolSheetResult == 0) {
                System.out.println("Do not need insert patrolSheet");
            } else {
                System.out.println("PatrolSheet insert failure");
            }

            int insertReportSheetResult = prepareInsertReportSheet(reportRelation, reportSheetList, tenant);
            if (insertReportSheetResult > 0) {
                System.out.println("ReportSheet insert success");
            } else {
                System.out.println("ReportSheet insert failure");
            }
        } else {
            System.out.println("No checkResultTable in DB and Can not insert it");
        }

        List<PointAttribute> pointAttributes = getPointAttribute(engineering.getEngineeringUUID(), null, tenant, sqlSessionTemplate);
        List<List> allReportDate = new ArrayList<>();
        List<ResultsTable> resultTables = getResultTableListByResultTableUUID(reportRelation.getResultTableUUID(), tenant, sqlSessionTemplate);

        for (ResultsTable resultTable : resultTables) {
            if (!resultTable.getResultsTableName().contains("巡视")) {
                List<ReportData> reportDatas = getReportSheetByResultID(resultTable.getResultId(), tenant, sqlSessionTemplate);
                if (!reportDatas.isEmpty()) {
                    allReportDate.add(reportDatas);
                }
            }
        }
        List<ReportRelation> reportRelations = getReportRelation(engineering.getEngineeringUUID(), null, null, tenant, sqlSessionTemplate);

        getResultData(allReportDate, pointAttributes, returnInfo);
        returnInfo.put("sheetlist", sheetList);
        returnInfo.put("patrolInfoList", patrolInfoList);
        returnInfo.put("reportSheetList", allReportDate);
        returnInfo.put("engineering", engineering);
        returnInfo.put("reportTimes", reportTimes);
        returnInfo.put("reportDate", reportDate);
        returnInfo.put("reportRelations", reportRelations);
        returnInfo.put("pointAttributes", pointAttributes);
        return returnInfo;
    }

    private void setReportSheet(List<ReportSheet> reportSheetInfoList, Sheet sheet, String tenant, SqlSessionTemplate sqlSessionTemplate) {
        String sheetName = sheet.getSheetName();
        List<PointAttribute> pointAttributes = new ArrayList<>();
        System.out.println("In get  Sheet and sheet name is: " + sheetName);
        int rownum = sheet.getPhysicalNumberOfRows();
        String flag = null;
        for (int i = 0; i < rownum; i++) {
            Row row = sheet.getRow(i);
            int colnum = row.getPhysicalNumberOfCells();
            List<String> saveData = new ArrayList<>();
            for (int j = 0; j < colnum; j++) {
                String cellData = (String) getCellFormatValue(row.getCell(j));
                if (j == 0 && cellData.contains("点号")) {
                    flag = "begin";
                }
                if (flag != null && flag.equals("begin") && i > 4) {
                    saveData.add(cellData);
                }
                if (j == 0 && cellData.contains("监理单位")) {
                    flag = "end";
                }
            }
            if (i > 4 && flag != null && flag.equals("begin")) {
                ReportSheet reportSheet = new ReportSheet();
                PointAttribute pointAttribute = new PointAttribute();
                reportSheet.setSheetName(sheetName);
                getReportSheet(reportSheet, pointAttribute, saveData, sheetName);
                pointAttributes.add(pointAttribute);
                reportSheetInfoList.add(reportSheet);
            }
        }

        List<PointAttribute> pointAttributesInDB = getPointAttribute(engineering.getEngineeringUUID(), null, tenant, sqlSessionTemplate);
        List<PointAttribute> doInsertpointAttributes = new ArrayList<>();
        List<PointAttribute> doUpdatepointAttributes = new ArrayList<>();
        for (PointAttribute pointAttribute : pointAttributes) {
            boolean hasPoint = false;
            if (pointAttributesInDB.isEmpty()) {
                pointAttribute.setNewPointID();
                doInsertpointAttributes.add(pointAttribute);
            } else {
                for (PointAttribute pointAttributeInDB : pointAttributesInDB) {
                    if (pointAttribute.getPoint().equals(pointAttributeInDB.getPoint())) {
                        pointAttribute.setPointID(pointAttributeInDB.getPointID());
                        hasPoint = true;
                        if (!pointAttribute.getLevelOfRisk().equals(pointAttributeInDB.getLevelOfRisk())) {
                            doUpdatepointAttributes.add(pointAttribute);
                        }
                    }
                }
                if (!hasPoint) {
                    pointAttribute.setNewPointID();
                    doInsertpointAttributes.add(pointAttribute);
                }
            }
        }

        if (!doInsertpointAttributes.isEmpty()) {
            int result = insertPointAttribute(doInsertpointAttributes, tenant, sqlSessionTemplate);
            System.out.println("insertPointAttribute: " + result);
        }

        if (!doUpdatepointAttributes.isEmpty()) {
            int result = updatePointAttribute(doUpdatepointAttributes, tenant, sqlSessionTemplate);
            System.out.println("updatePointAttribute: " + result);
        }
    }

    private void getReportSheet(ReportSheet reportSheet, PointAttribute pointAttribute, List saveData, String sheetName) {
        if (sheetName.contains("管线") || sheetName.contains("管片")) {
            SavePointAttribute(pointAttribute, sheetName, saveData);

            reportSheet.setPoint((String) saveData.get(0));
            reportSheet.setHeight(Float.parseFloat((String) saveData.get(3)));
            reportSheet.setChangeQuantity(Float.parseFloat((String) saveData.get(4)));
            reportSheet.setChangeRate(Float.parseFloat((String) saveData.get(5)));
            reportSheet.setCumulativeVariation(Float.parseFloat((String) saveData.get(6)));
            reportSheet.setRemarks((String) saveData.get(10));
        }
        if (sheetName.contains("建筑")) {
            SavePointAttribute(pointAttribute, sheetName, saveData);

            reportSheet.setPoint((String) saveData.get(0));
            reportSheet.setChangeQuantity(Float.parseFloat((String) saveData.get(3)));
            reportSheet.setChangeRate(Float.parseFloat((String) saveData.get(4)));
            reportSheet.setCumulativeVariation(Float.parseFloat((String) saveData.get(5)));
            reportSheet.setRemarks((String) saveData.get(9));
        }
        if (sheetName.contains("地表")) {
            SavePointAttribute(pointAttribute, sheetName, saveData);

            reportSheet.setPoint((String) saveData.get(0));
            reportSheet.setChangeQuantity(Float.parseFloat((String) saveData.get(3)));
            reportSheet.setCumulativeVariation(Float.parseFloat((String) saveData.get(4)));
            reportSheet.setChangeRate(Float.parseFloat((String) saveData.get(5)));
            reportSheet.setFormationLossRate(Float.parseFloat((String) saveData.get(6)));
            reportSheet.setRemarks((String) saveData.get(10));
        }
    }

    private void SavePointAttribute(PointAttribute pointAttribute, String sheetName, List saveData) {
        pointAttribute.setEngineeringID(engineering.getEngineeringUUID());
        pointAttribute.setPoint((String) saveData.get(0));
        pointAttribute.setRingLocation((String) saveData.get(1));
        pointAttribute.setInitialHeight(Float.parseFloat((String) saveData.get(2)));
        if (!sheetName.contains("建筑")) {
            pointAttribute.setChangeRateControlValue(Float.parseFloat(((String) saveData.get(7)).substring(1)));
            pointAttribute.setCumulativeVariationControlValue(Float.parseFloat(((String) saveData.get(8)).substring(1)));
            pointAttribute.setLevelOfRisk((String) saveData.get(9));
        } else {
            pointAttribute.setChangeRateControlValue(Float.parseFloat(((String) saveData.get(6)).substring(1)));
            pointAttribute.setCumulativeVariationControlValue(Float.parseFloat(((String) saveData.get(7)).substring(1)));
            pointAttribute.setLevelOfRisk((String) saveData.get(8));
        }
    }

    private JSONObject getResultData(List<List> reportSheetList, List<PointAttribute> pointAttributes, JSONObject returnInfo) {
        List<ResultData> resultDatas = new ArrayList<>();
        for (List<ReportData> reportSheets : reportSheetList) {
            String sheetName = reportSheets.get(0).getSheetName();
            ResultData resultData = new ResultData();
            resultData.setSheetName(sheetName);
            ReportData reportDataCQ = getMaxPointValue(reportSheets, "changeQuantity");
            resultData.setMaxCQPoint(reportDataCQ.getPoint());
            resultData.setMaxCQdata(reportDataCQ.getChangeQuantity());
            ReportData reportDataCV = getMaxPointValue(reportSheets, "cumulativeVariation");
            resultData.setMaxCVPoint(reportDataCV.getPoint());
            resultData.setMaxCVdata(reportDataCV.getCumulativeVariation());

            for (PointAttribute pointAttribute : pointAttributes) {
                if (pointAttribute.getPoint().contains(reportDataCQ.getPoint())) {
                    resultData.setChangeRateControlValue(pointAttribute.getChangeRateControlValue());
                    resultData.setCumulativeVariationControlValue(pointAttribute.getCumulativeVariationControlValue());
                }
            }

            if (sheetName.contains("地表")) {
                ReportData reportDataFL = getMaxPointValue(reportSheets, "formationLossRate");
                resultData.setMaxFLPoint(reportDataFL.getPoint());
                resultData.setMaxFLdata(reportDataFL.getFormationLossRate());
            }
            resultDatas.add(resultData);
        }
        returnInfo.put("resultDatas", resultDatas);
        return returnInfo;
    }

    private ReportData getMaxPointValue(List<ReportData> reportSheets, String dataType) {
        ReportData maxData = reportSheets.get(0);
        for (ReportData reportData : reportSheets) {
            float maxDataValue = 0;
            float currentValue = 0;
            if (dataType.contains("changeQuantity")) {
                maxDataValue = maxData.getChangeQuantity();
                currentValue = reportData.getChangeQuantity();
            }
            if (dataType.contains("cumulativeVariation")) {
                maxDataValue = maxData.getCumulativeVariation();
                currentValue = reportData.getCumulativeVariation();
            }
            if (dataType.contains("formationLossRate")) {
                maxDataValue = maxData.getFormationLossRate();
                currentValue = reportData.getFormationLossRate();
            }
            if (maxDataValue > 0 && currentValue > 0) {
                if (maxDataValue < currentValue) {
                    maxData = reportData;
                }
            } else if (maxDataValue < 0 && currentValue < 0) {
                if (currentValue < maxDataValue) {
                    maxData = reportData;
                }
            } else if (maxDataValue > 0 && currentValue < 0) {
                if (maxDataValue + currentValue < 0) {
                    maxData = reportData;
                }
            } else {
                if (maxDataValue + currentValue > 0) {
                    maxData = reportData;
                }
            }
        }
        return maxData;
    }

    private Engineering getEngineeringInfo(Sheet sheet, Engineering engineering) {
        System.out.println("In get EngineeringInfo Sheet and sheet name is: " + sheet.getSheetName());
        int rownum = sheet.getPhysicalNumberOfRows();
        String flag = "flag";
        Date date;
        for (int i = 0; i < rownum; i++) {
            Row row = sheet.getRow(i);
            int colnum = row.getPhysicalNumberOfCells();
            for (int j = 0; j < colnum; j++) {
                Cell cell = row.getCell(j);
                String cellData = (String) getCellFormatValue(cell);
                if (cellData != null) {
                    if (cellData.contains("工程名称")) {
                        flag = cellData;
                    }
                    if (flag.contains("工程名称") && j != 0) {
                        engineering.setEngineeringName(cellData);
                        flag = "flag";
                    }
                    if (cellData.contains("工程地点")) {
                        flag = cellData;
                    }
                    if (flag.contains("工程地点") && j != 0) {
                        engineering.setProject(cellData);
                        flag = "flag";
                    }
                    if (cellData.contains("施工单位")) {
                        flag = cellData;
                    }
                    if (flag.contains("施工单位") && j != 0) {
                        engineering.setConstructionCompany(cellData);
                        flag = "flag";
                    }
                    if (cellData.contains("监理单位")) {
                        flag = cellData;
                    }
                    if (flag.contains("监理单位") && j != 0) {
                        engineering.setSupervisionCompany(cellData);
                        flag = "flag";
                    }
                    if (cellData.contains("监测单位")) {
                        flag = cellData;
                    }
                    if (flag.contains("监测单位") && j != 0) {
                        engineering.setMonitoringCompany(cellData);
                        flag = "flag";
                    }
                    if (cellData.contains("监测日期")) {
                        flag = cellData;
                    }
                    if (flag.contains("监测日期") && j != 0) {
                        date = cell.getDateCellValue();
                        reportDate = date;
                        System.out.println(date);
                        flag = "flag";
                    }
                    if (cellData.contains("仪器型号")) {
                        flag = cellData;
                    }
                    if (flag.contains("仪器型号") && j != 0) {
                        engineering.setInstrumentModel(cellData);
                        flag = "flag";
                    }
                    if (cellData.contains("监测次数")) {
                        flag = cellData;
                    }
                    if (flag.contains("监测次数") && j != 0) {
                        Double d = cell.getNumericCellValue();
                        DecimalFormat df = new DecimalFormat("#");
                        String value = df.format(d);
                        reportTimes = value;
                        flag = "flag";
                    }
                    if (cellData.contains("报送单位")) {
                        System.out.println(engineering);
                        return engineering;
                    }
                }
            }
        }
        System.out.println("没有监测内容数据");
        return null;
    }

    private List<PatrolInfo> getPatrolSheet(Sheet sheet) {
        System.out.println("In get patrol sheet and sheet name is: " + sheet.getSheetName());
        List<PatrolInfo> patrolInfos = new ArrayList<>();
        List<String> infoList = new ArrayList<>();
        String sheetName = sheet.getSheetName();
        int rownum = sheet.getPhysicalNumberOfRows();
        String flag = null;
        int mark = 0;
        for (int i = 0; i < rownum; i++) {
            Row row = sheet.getRow(i);
            int colnum = row.getPhysicalNumberOfCells();
            if (colnum != 0) {
                for (int j = 0; j < colnum; j++) {
                    String cellData = (String) getCellFormatValue(row.getCell(j));
                    if (cellData != null && !cellData.equals("")) {
                        // 输出表数据到达分类时开始进行数据写入集合
                        if (j == 0) {
                            flag = cellData;
                            if (!cellData.contains("分类")) {
                                mark = (mark / 10) * 10 + 10;
                            }
                        }
                        if (j != 0 && flag != null && !flag.contains("分类")) {
                            infoList.add(cellData);
                        }
                    }
                }
                if (flag != null && !flag.contains("分类")) {
                    PatrolInfo patrolInfo = new PatrolInfo();
                    mark = mark + 1;
                    patrolInfo.setSheetName(sheetName);
                    patrolInfo.setPatrolContent(infoList.get(0));
                    patrolInfo.setPatrolResult(infoList.get(1));
                    patrolInfo.setRemark(infoList.get(2));
                    patrolInfo.setPlaceMark(mark);
                    patrolInfos.add(patrolInfo);
                    infoList.clear();
                }
            }
        }
        return patrolInfos;
    }

    private int prepareInsertResultTable(String resultTableUUID, List<String> sheetList, String tenant) {
        List<ResultsTable> resultsTableList = new ArrayList<>();
        List<ResultsTable> resultTableInDB = getResultTableListByResultTableUUID(resultTableUUID, tenant, sqlSessionTemplate);
        int result;
        for (String sheetName : sheetList) {
            boolean flag = true;
            if (resultTableInDB != null) {
                for (ResultsTable resultsTable : resultTableInDB) {
                    String resultsTableName = resultsTable.getResultsTableName();
                    if (resultsTableName.contains(sheetName)) {
                        flag = false;
                    }
                }
            }
            if (flag) {
                ResultsTable resultsTable = new ResultsTable();
                resultsTable.setResultTableUUID(resultTableUUID);
                resultsTable.setResultId();
                resultsTable.setResultsTableName(sheetName);
                resultsTableList.add(resultsTable);
            }
        }
        if (!resultsTableList.isEmpty()) {
            result = insertResultTable(resultsTableList);
        } else {
            result = 0;
            System.out.println("表格已经存在");
        }
        return result;
    }

    private int prepareInsertPatrolSheet(ReportRelation reportRelation, List<PatrolInfo> patrolInfoList, String tenant) {
        List<ResultsTable> resultTableList = getResultTableListByResultTableUUID(reportRelation.getResultTableUUID(), tenant, sqlSessionTemplate);
        String resultTableID = null;
        int result = 0;
        for (ResultsTable resultsTable : resultTableList) {
            if (patrolInfoList.get(0).getSheetName().contains(resultsTable.getResultsTableName())) {
                resultTableID = resultsTable.getResultId();
            }
        }
        int count = checkPatrolSheetByResultTableID(resultTableID);
        System.out.println("Count of PatrolInfo: " + count);
        if (count < 1) {
            result = InsertPatrolSheet(resultTableID, patrolInfoList);
        }
        return result;
    }

    private int prepareInsertReportSheet(ReportRelation reportRelation, List<List> reportSheetList, String tenant) {
        String resultTableUUID = reportRelation.getResultTableUUID();
        List<ResultsTable> resultTableList = getResultTableListByResultTableUUID(resultTableUUID, tenant, sqlSessionTemplate);
        String resultTableID;
        int result = 0;

        for (ResultsTable resultsTable : resultTableList) {
            for (List<ReportSheet> reportSheets : reportSheetList) {
                if (reportSheets.get(0).getSheetName().contains(resultsTable.getResultsTableName())) {
                    resultTableID = resultsTable.getResultId();
                    result = insertReportSheet(resultTableID, reportSheets);
                }
            }
        }
        return result;
    }

    public ReportRelation getLastReportRelationByEngUUID(String engineeringUUID, String tenant, SqlSessionTemplate sqlSessionTemplate) {
        List<ReportRelation> reportRelations = getReportRelation(engineeringUUID, null, null, tenant, sqlSessionTemplate);
        ReportRelation tmp = reportRelations.get(0);
        for (ReportRelation current : reportRelations) {
            if (tmp.getReportDate().getTime() < current.getReportDate().getTime()) {
                tmp = current;
            }
        }
        return tmp;
    }


    public void downloadTemp(HttpServletResponse response) {
        String filePath = PropertiesValue.UPLOAD_FILE_PATH + "/surveyInfo/Template.xlsx";
        File file = new File(filePath);
        try {
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            byte[] bytes = new byte[inputStream.available()];
            int len;
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(file.getName(), "UTF-8"));
            response.setContentType("application/octet-stream");
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


    // ------------------------- DB Work -------------------------
    // ------------------------- project -------------------------
    public List<JSONObject> getProjectByID(int id, String tenant, SqlSessionTemplate sqlSessionTemplate) {
        String LineSql = "com.raising.forward.mapper.ProjectInfoMapper.getProjectByID";
        System.out.println("Begin to get project");
        JSONObject paramInfo = new JSONObject();
        paramInfo.put("tenant", tenant);
        paramInfo.put("id", id);
        return sqlSessionTemplate.selectList(LineSql, paramInfo);
    }

    // ------------------------- Engineering -------------------------
    public Engineering getEngineeringByProjectID(int projectID, String tenant, SqlSessionTemplate sqlSessionTemplate) {
        String LineSql = "com.raising.forward.mapper.Engineering.getEngineering";
        System.out.println("Begin to get engineering");
        JSONObject paramInfo = new JSONObject();
        paramInfo.put("tenant", tenant);
        paramInfo.put("projectid", projectID);
        Engineering engineeringInDB = sqlSessionTemplate.selectOne(LineSql, paramInfo);
        System.out.println("get engineering from db: " + engineeringInDB);
        return engineeringInDB;
    }

    public int insertEngineering(Engineering engineering, String tenant, SqlSessionTemplate sqlSessionTemplate) {
        String LineSql = "com.raising.forward.mapper.Engineering.insertEngineering";
        System.out.println("Begin to inset engineering");
        JSONObject paramInfo = new JSONObject();
        paramInfo.put("tenant", tenant);
        paramInfo.put("engineering", engineering);
        return sqlSessionTemplate.insert(LineSql, paramInfo);
    }

    public int updateEngineering(Engineering engineering, String tenant, SqlSessionTemplate sqlSessionTemplate) {
        String LineSql = "com.raising.forward.mapper.Engineering.updateEngineering";
        System.out.println("Begin to update engineering");
        JSONObject paramInfo = new JSONObject();
        paramInfo.put("tenant", tenant);
        paramInfo.put("engineering", engineering);
        return sqlSessionTemplate.update(LineSql, paramInfo);
    }

    // ------------------------- Engineering -------------------------

    // ------------------------- ReportRelation -------------------------
    private int insertReportRelation(ReportRelation reportRelation) {
        String LineSql = "com.raising.forward.mapper.ReportRelation.insertReportRelation";
        System.out.println("Begin to inset ReportRelation");
        JSONObject paramInfo = new JSONObject();
        paramInfo.put("tenant", request.getSession().getAttribute("tenant"));
        paramInfo.put("reportRelation", reportRelation);
        return sqlSessionTemplate.insert(LineSql, paramInfo);
    }


    public List<ReportRelation> getReportRelation(String engineeringUUID, String reportTimes, Date reportDate, String tenant, SqlSessionTemplate sqlSessionTemplate) {
        String LineSql = "com.raising.forward.mapper.ReportRelation.getReportRelation";
        JSONObject paramInfo = new JSONObject();
        paramInfo.put("tenant", tenant);
        paramInfo.put("engineeringUUID", engineeringUUID);
        paramInfo.put("reportTimes", reportTimes);
        paramInfo.put("reportDate", reportDate);
        return sqlSessionTemplate.selectList(LineSql, paramInfo);
    }

    // ------------------------- ReportRelation -------------------------

    // ------------------------- ResultTable -------------------------
    public List<ResultsTable> getResultTableListByResultTableUUID(String resultTableUUID, String tenant, SqlSessionTemplate sqlSessionTemplate) {
        String LineSql = "com.raising.forward.mapper.ResultTable.getResultTableListByResultTableUUID";
        JSONObject paramInfo = new JSONObject();
        paramInfo.put("tenant", tenant);
        paramInfo.put("resultTableUUID", resultTableUUID);
        List<ResultsTable> resultTableInDB = sqlSessionTemplate.selectList(LineSql, paramInfo);
        System.out.println("resultTableInDB: " + resultTableInDB);
        return resultTableInDB;
    }

    private int insertResultTable(List resultsTableList) {
        String LineSql = "com.raising.forward.mapper.ResultTable.insertResultTable";
        System.out.println("Begin to inset result tables");
        JSONObject paramInfo = new JSONObject();
        paramInfo.put("tenant", request.getSession().getAttribute("tenant"));
        paramInfo.put("resultsTableList", resultsTableList);
        return sqlSessionTemplate.insert(LineSql, paramInfo);
    }

    // ------------------------- ResultTable -------------------------

    // ------------------------- PatrolSheet -------------------------
    private int InsertPatrolSheet(String resultTableID, List patrolInfoList) {
        String LineSql = "com.raising.forward.mapper.PatrolInfo.insertPatrolInfo";
        System.out.println("Begin to patrolInfo sheet");
        JSONObject paramInfo = new JSONObject();
        paramInfo.put("tenant", request.getSession().getAttribute("tenant"));
        paramInfo.put("patrolInfoList", patrolInfoList);
        paramInfo.put("resultTableID", resultTableID);
        return sqlSessionTemplate.insert(LineSql, paramInfo);
    }

    private int checkPatrolSheetByResultTableID(String resultTableID) {
        String LineSql = "com.raising.forward.mapper.PatrolInfo.checkPatrolSheetByResultTableID";
        System.out.println("Begin to patrolInfo sheet");
        JSONObject paramInfo = new JSONObject();
        paramInfo.put("tenant", request.getSession().getAttribute("tenant"));
        paramInfo.put("resultTableID", resultTableID);
        return sqlSessionTemplate.selectOne(LineSql, paramInfo);
    }

    private List<PatrolInfo> getPatrolSheetByResultTableID(String resultTableID) {
        String LineSql = "com.raising.forward.mapper.PatrolInfo.getPatrolSheetByResultTableID";
        System.out.println("Begin to patrolInfo sheet");
        JSONObject paramInfo = new JSONObject();
        paramInfo.put("tenant", request.getSession().getAttribute("tenant"));
        paramInfo.put("resultTableID", resultTableID);
        return sqlSessionTemplate.selectList(LineSql, paramInfo);
    }


    // ------------------------- PatrolSheet -------------------------

    // ------------------------- ReportSheet -------------------------
    private int insertReportSheet(String resultTableID, List<ReportSheet> reportSheets) {
        String LineSql = "com.raising.forward.mapper.ReportSheet.insertReportSheet";
        System.out.println("Begin to ReportSheet sheet");
        JSONObject paramInfo = new JSONObject();
        paramInfo.put("tenant", request.getSession().getAttribute("tenant"));
        paramInfo.put("resultTableID", resultTableID);
        paramInfo.put("reportSheets", reportSheets);
        return sqlSessionTemplate.insert(LineSql, paramInfo);
    }

    public List<ReportData> getReportSheetByResultID(String resultID, String tenant, SqlSessionTemplate sqlSessionTemplate) {
        String LineSql = "com.raising.forward.mapper.ReportData.getReportSheetByResultID";
        System.out.println("Begin to ReportSheet sheet");
        JSONObject paramInfo = new JSONObject();
        paramInfo.put("tenant", tenant);
        paramInfo.put("resultID", resultID);
        return sqlSessionTemplate.selectList(LineSql, paramInfo);
    }

    // ------------------------- PointAttribute -------------------------
    private List<PointAttribute> getPointAttribute(String engineeringID, String point, String tenant, SqlSessionTemplate sqlSessionTemplate) {
        String LineSql = "com.raising.forward.mapper.PointAttribute.getPointAttribute";
        System.out.println("Begin to get pointAttribute sheet");
        JSONObject paramInfo = new JSONObject();
        paramInfo.put("tenant", tenant);
        paramInfo.put("engineeringID", engineeringID);
        paramInfo.put("point", point);
        return sqlSessionTemplate.selectList(LineSql, paramInfo);
    }

    private int insertPointAttribute(List<PointAttribute> pointAttributes, String tenant, SqlSessionTemplate sqlSessionTemplate) {
        String LineSql = "com.raising.forward.mapper.PointAttribute.insertPointAttribute";
        System.out.println("Begin to insert pointAttribute sheet");
        JSONObject paramInfo = new JSONObject();
        paramInfo.put("tenant", tenant);
        paramInfo.put("pointAttributes", pointAttributes);
        return sqlSessionTemplate.insert(LineSql, paramInfo);
    }

    private int updatePointAttribute(List<PointAttribute> pointAttributes, String tenant, SqlSessionTemplate sqlSessionTemplate) {
        String LineSql = "com.raising.forward.mapper.PointAttribute.updatePointAttribute";
        System.out.println("Begin to update pointAttribute sheet");
        JSONObject paramInfo = new JSONObject();
        paramInfo.put("tenant", tenant);
        paramInfo.put("pointAttributes", pointAttributes);
        return sqlSessionTemplate.insert(LineSql, paramInfo);
    }
}