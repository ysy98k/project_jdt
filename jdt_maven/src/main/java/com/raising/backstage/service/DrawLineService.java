package com.raising.backstage.service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.common.NewBaseService;
import com.raising.forward.service.ProjectForwardService;
import com.util.GeoPoint;
import com.util.*;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.util.GeometryHelper;
import com.util.GeoPoly;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


@Service
public class DrawLineService extends NewBaseService{

    private static final Logger logger = LoggerFactory.getLogger(DrawLineService.class);

    private static final double OFFSET = 20.00 / 6400000 * 180 / 3.14;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    protected ProjectForwardService projectForwardService;



    public JSONObject saveLine(String ajaxParam) {

        JSONObject ajaxJsonObj = JSONObject.parseObject(ajaxParam);
        int sectionId = ajaxJsonObj.getInteger("sectionId");
        JSONArray jsonArray = ajaxJsonObj.getJSONArray("data");
        double[] lng = new double[jsonArray.size()];
        double[] lat = new double[jsonArray.size()];
        String mapCoordinateCenter = "";
        for (int i = 0; i < jsonArray.size(); i++) {
            lng[i] = jsonArray.getJSONObject(i).getDouble("lng");
            lat[i] = jsonArray.getJSONObject(i).getDouble("lat");
            String x = String.valueOf(lng[i]);
            String y = String.valueOf(lat[i]);
            mapCoordinateCenter += x + "," + y + ";";
        }
        GeoPoly[] geoPoly = GeometryHelper.getParallel(lng, lat, OFFSET, OFFSET);
        ArrayList leftGeoPoly = geoPoly[0].get_PtArray();
        ArrayList rightGeoPoly = geoPoly[1].get_PtArray();

        int len = leftGeoPoly.size();
        String mapCoordinateLeft = "";
        for (int i = 0; i < len; i++) {

            GeoPoint leftGeoPoint = (GeoPoint) leftGeoPoly.get(i);
            String x = String.valueOf(leftGeoPoint.getX());
            String y = String.valueOf(leftGeoPoint.getY());
            mapCoordinateLeft += x + "," + y + ";";
        }

        String mapCoordinateRight = "";
        for (int i = 0; i < len; i++) {
            GeoPoint rightGeoPoint = (GeoPoint) rightGeoPoly.get(i);
            String x = String.valueOf(rightGeoPoint.getX());
            String y = String.valueOf(rightGeoPoint.getY());
            mapCoordinateRight += x + "," + y + ";";
        }
        JSONObject lineInfoJson = new JSONObject();
        lineInfoJson.put("tenant",request.getSession().getAttribute("tenant"));
        lineInfoJson.put("sectionId", sectionId);
        lineInfoJson.put("mapCoordinateCenter", mapCoordinateCenter);
        lineInfoJson.put("mapCoordinateRight", mapCoordinateRight);
        lineInfoJson.put("mapCoordinateLeft", mapCoordinateLeft);

        String lineInfoSql = "SectionManage.update";
        int result = this.sqlSessionTemplate.update(lineInfoSql, lineInfoJson);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", result);
        jsonObject.put("leftPoint", leftGeoPoly);
        jsonObject.put("rightPoint", rightGeoPoly);
        return jsonObject;
    }

    /**
     * 前台，地图查询
     * @return
     */
    public JSONObject queryAllLine(List<String> collectionNames) {
        JSONObject returnJson = new JSONObject();
        List<JSONObject> proListResult = projectForwardService.getResourcesProject(collectionNames);
        List<JSONObject> dataList = new ArrayList<>();
        int totalNum = 0,shieldNum = 0,slurryNum = 0,tbmNum = 0,pushBenchNum=0;//记录信息总览信息

        //获取tsdb数据
        JSONArray tagNamesArray = new JSONArray();
        for(String collectorName : collectionNames){
            JSONObject obj1 = new JSONObject();
            JSONObject obj2 = new JSONObject();
            obj1.put("instance_name",collectorName+"_MR_Ring_Num");
            obj2.put("instance_name",collectorName+"_MR_Des_A1Mileage");
            tagNamesArray.add(obj1);
            tagNamesArray.add(obj2);
        }
        JSONObject tagJson = null;
        try {
            tagJson = TsdbUtil.getInstanceOfRest( tagNamesArray);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        JSONArray tagsArray  = null;
        if(tagJson != null && Constants.EXECUTE_SUCCESS.equals(tagJson.getString("status"))){
            tagsArray =  tagJson.getJSONArray("dataArray");
        }
        totalNum = proListResult.size();
        for (int i = 0; i < proListResult.size(); i++) {
            JSONObject projectJson = proListResult.get(i);
            String tbmCCSId = projectJson.getString("tbmCCSId");
            if("tbmtype.epb".equalsIgnoreCase(tbmCCSId)){
                shieldNum = shieldNum + 1;
            }else if("tbmtype.mwb".equalsIgnoreCase(tbmCCSId)){
                slurryNum = slurryNum + 1;
            }else if("tbmtype.opentbm".equalsIgnoreCase(tbmCCSId)
                    || "tbmtype.shield".equalsIgnoreCase(tbmCCSId)
                    || "tbmtype.combinedtbm".equalsIgnoreCase(tbmCCSId)){
                tbmNum = tbmNum + 1;
            }else if("tbmtype.pushbench".equalsIgnoreCase(tbmCCSId)){
                pushBenchNum += 1;
            }
            String mapCoordinateCenter = projectJson.getString("mapCoordinateCenter");
            if (StringUtils.isNullOrEmpty(mapCoordinateCenter)) {//如果该区间没有被绘制线路地图。则跳过。不将该区间显示。
                continue;
            }
            String ccsId = projectJson.getString("ccsId");
            JSONObject data = new JSONObject();
            data.put("ccsId", ccsId);
            data.put("projectInfo", projectJson);
            fillCoordinate(dataList, data, projectJson, tagsArray);
        }
        returnJson.put("returnInfo",dataList);
        returnJson.put("totalNum",totalNum);
        returnJson.put("shieldNum",shieldNum);
        returnJson.put("slurryNum",slurryNum);
        returnJson.put("tbmNum",tbmNum);
        returnJson.put("pushBenchNum",pushBenchNum);
        return returnJson;
    }


    public JSONObject queryLine(String ajaxParam){


        JSONObject ajaxJsonObj = JSONObject.parseObject(ajaxParam);

        int sectionId = ajaxJsonObj.getInteger("sectionId");
        String querySql = "SectionManage.query";
        JSONObject queryJson = new JSONObject();
        queryJson.put("sectionId",sectionId);
        queryJson.put("tenant",request.getSession().getAttribute("tenant"));
        JSONObject jsonObject = this.sqlSessionTemplate.selectOne(querySql,queryJson);
        String ccsId = jsonObject.getString("ccsId");
        String fdLevelCode = ccsId.substring(0, ccsId.lastIndexOf("."));
        String cityName;
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            cityName = codeItemDao.getFdItemName(fdLevelCode);
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        JSONObject sectionInfo = new JSONObject();
        sectionInfo.put("city",cityName);
        String mapCoordinateCenter = jsonObject.getString("mapCoordinateCenter");
        if(StringUtils.isNullOrEmpty(mapCoordinateCenter)){
            sectionInfo.put("mapCoordinateCenter","");
            sectionInfo.put("tunnelLine","");
        }else {
            String[] coordinateCenter = mapCoordinateCenter.split(";");
            JSONObject[] centerLine = new JSONObject[coordinateCenter.length];
            for (int i = 0; i < coordinateCenter.length; i++) {
                JSONObject tempJson = new JSONObject();
                String[] temp = coordinateCenter[i].split(",");
                for (int j = 0; j < temp.length; j++) {
                    if (temp.length > 1) {
                        tempJson.put("x", temp[0]);
                        tempJson.put("y", temp[1]);
                        centerLine[i] = tempJson;
                    }
                }
            }
            sectionInfo.put("mapCoordinateCenter", centerLine);
        }
        return sectionInfo;
    }

    private void fillCoordinate(List<JSONObject> returnObjects,JSONObject returnInfo,JSONObject projectJson,JSONArray tagsArray){
        String tunnelDrection = projectJson.getString("tunnelDrection");
        Integer ringTotal = projectJson.getInteger("ringTotal");
        String collectorName = projectJson.getString("collectorName");
        String mapCoordinateCenter = projectJson.getString("mapCoordinateCenter");
        if (StringUtils.isNullOrEmpty(mapCoordinateCenter)) {
            returnInfo.put("mapCoordinateCenter", "");
            returnInfo.put("tunnelLine", "");
            return;
        }
        String currentRing = null;
        if(tagsArray != null){
            for(int j = 0;j<tagsArray.size();j++){
                JSONObject tagObj = tagsArray.getJSONObject(j);
                if(collectorName.equals(tagObj.getString("collectorName")) && "MR_Ring_Num".equals(tagObj.getString("tagName")) ){
                    if("192".equals(tagObj.getString("quality"))){
                        String value = tagObj.getString("value");
                        if(value.indexOf(".")>0){
                            value = value.substring(0,value.indexOf("."));
                        }
                        currentRing = value;
                    }
                }
            }
        }
        String[] coordinateCenter = mapCoordinateCenter.split(";");
        JSONObject[] centerLine = new JSONObject[coordinateCenter.length];//中心线
        for (int m = 0; m < coordinateCenter.length; m++) {
            JSONObject tempJson = new JSONObject();
            String[] temp = coordinateCenter[m].split(",");
            for (int j = 0; j < temp.length; j++) {
                if (temp.length > 1) {
                    tempJson.put("x", temp[0]);
                    tempJson.put("y", temp[1]);
                    centerLine[m] = tempJson;
                }
            }
        }
        returnInfo.put("mapCoordinateCenter", centerLine);
        double[] centerPointX = new double[centerLine.length];
        double[] centerPointY = new double[centerLine.length];
        for (int j = 0; j < centerLine.length; j++) {
            try {
                centerPointX[j] = centerLine[j].getDouble("x");
                centerPointY[j] = centerLine[j].getDouble("y");
            }catch (Exception e){
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
        GeoPoly[] geoPoly = GeometryHelper.getParallel(centerPointX, centerPointY, OFFSET, OFFSET);
        GeoPoly geoCenter = new GeoPoly(centerPointX, centerPointY);
        GeoPoly geoLeft = geoPoly[0];
        GeoPoly geoRight = geoPoly[1];

        double lratio = 0.0;//设置默认断点，用来显示推进进度。默认推进进度为0.
        DecimalFormat df = new DecimalFormat("#.0");
        if(currentRing != null && ringTotal != null){
            lratio = (Double.parseDouble(currentRing)) / ringTotal;
            lratio = Double.parseDouble(df.format(lratio));
        }
        JSONObject projectInfo = returnInfo.getJSONObject("projectInfo");
        projectInfo.put("projectRate", lratio*100);
        if ("左".equals(tunnelDrection)) {
            GeoPoly[] geoLeftPoly = GeometryHelper.breakByLength(geoLeft, lratio);
            returnInfo.put("tunnelLine", geoLeftPoly);
        } else if ("右".equals(tunnelDrection)) {
            GeoPoly[] geoRightPoly = GeometryHelper.breakByLength(geoRight, lratio);
            returnInfo.put("tunnelLine", geoRightPoly);
        } else if ("中".equals(tunnelDrection)) {
            GeoPoly[] geoCenterPoly = GeometryHelper.breakByLength(geoCenter, lratio);
            returnInfo.put("tunnelLine", geoCenterPoly);
        }
        returnObjects.add(returnInfo);
    }





}
