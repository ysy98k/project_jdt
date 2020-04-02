package com.raising.forward.controller.constructionManagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.common.BaseController;
import com.util.CSVUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/raising/forward/construction/reportTable")
public class ReportTableController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ReportTableController.class);



    @RequestMapping(value = "/getRows.do",method = RequestMethod.POST)
    public JSONObject getAlarmInfo(String ajaxParam){
        JSONObject returnInfo = new JSONObject();

        JSONObject paramJson = JSONObject.parseObject(ajaxParam);

        if("ring".equals(paramJson.getString("type"))){
            try {
                returnInfo = jDisDataService.getReportData(paramJson);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                returnInfo.put("status", Constants.EXECUTE_FAIL);
            }
            JSONObject timeObj = jRingDataService.getTaskTimeForRingNum(paramJson.getInteger("projectId"), paramJson.getInteger("MR_Ring_Num"));
            if(timeObj != null){
                returnInfo.put("assembleTime",timeObj.getString("assembleTime"));
                returnInfo.put("tunnellingTime",timeObj.getString("tunnellingTime"));
                returnInfo.put("shuDownTime",timeObj.getString("shuDownTime"));
            }
        }else if("soilRing".equals(paramJson.getString("type"))){
            //先获取环宽
            List<JSONObject> settingData = jSettingService.getData(paramJson.getInteger("projectId"), "RingLen");
            try{
                JSONObject data = settingData.get(0);
                Integer ringLen = data.getInteger("value");
                paramJson.put("ringLen",ringLen);
            }catch (Exception e){//如果不能正常取到，那么设1200默认值
                paramJson.put("ringLen",1200);
            }
            //获取行程数据
            try {
                returnInfo = jDisDataService.getReportSoilData(paramJson);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                returnInfo.put("status", Constants.EXECUTE_FAIL);
            }

        }else if("soilRings".equals(paramJson.getString("type"))){
            //获取环数据
            returnInfo = jRingDataService.getSoilReport(paramJson.getInteger("projectId"), paramJson.getInteger("startRingNum"),
                    paramJson.getInteger("endRingNum"));
        }else if("segment".equals(paramJson.getString("type"))){
            returnInfo = dMeasureResultService.getReportSegementData(paramJson);
        }
        return returnInfo;
    }


    @RequestMapping(value = "/downCSV.do",method = RequestMethod.GET)
    public void downExcel(HttpServletRequest request, HttpServletResponse response){
        JSONArray result = new JSONArray();
        try {
            String ajaxParam = request.getParameter("ajaxParam");
            ajaxParam = URLDecoder.decode(ajaxParam, "UTF-8");
            JSONObject paramJson = JSONObject.parseObject(ajaxParam);
            List<Object> columnNamesList = (List<Object>)paramJson.get("columnNamesList");
            List<List<Object>> data = new ArrayList<>();

            if("soilRings".equals(paramJson.getString("type"))) {
                //获取环数据
                JSONObject returnInfo = jRingDataService.getSoilReport(paramJson.getInteger("projectId"), paramJson.getInteger("startRingNum"),
                        paramJson.getInteger("endRingNum"));
                List<Object> ringsList = (List<Object>)returnInfo.get("ringsList");
                List<Object> ring_dumping_volume_total = (List<Object>)returnInfo.get("ring_Dumping_volume_Total");
                List<Object> lowerLimit = (List<Object>)returnInfo.get("lowerLimit");
                List<Object> upperLimit = (List<Object>)returnInfo.get("upperLimit");
                List<Object> ring_dumping_volume_dTotal = (List<Object>)returnInfo.get("ring_Dumping_volume_DTotal");
                for(int i=0;i<ringsList.size();i++){
                    List<Object> row = new ArrayList<>();
                    row.add(ringsList.get(i));
                    row.add(ring_dumping_volume_total.get(i));
                    row.add(lowerLimit.get(i));
                    row.add(upperLimit.get(i));
                    row.add(ring_dumping_volume_dTotal.get(i));
                    data.add(row);
                }
            }else if( "segment".equals(paramJson.getString("type")) ){
                JSONObject sement = dMeasureResultService.getReportSegementData(paramJson);
                JSONArray dataList =  sement.getJSONArray("dataList");
                for(int i=0;i<dataList.size();i++){
                    JSONObject temp = dataList.getJSONObject(i);
                    List<Object> row = new ArrayList<>();
                    row.add(temp.getString("MR_Ring_Number"));
                    row.add(temp.getString("MR_Act_A8X"));
                    row.add(temp.getString("MR_Act_A8Y"));
                    row.add(temp.getString("MR_Act_A8Z"));
                    row.add(temp.getString("MR_Des_A8Mileage"));
                    row.add(temp.getString("MR_Des_A8X"));
                    row.add(temp.getString("MR_Des_A8Y"));
                    row.add(temp.getString("MR_Des_A8Z"));
                    row.add(temp.getString("MR_Act_A8HD"));
                    row.add(temp.getString("MR_Act_A8VD"));
                    data.add(row);
                }
            }
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((new Date().getTime() + ".csv").getBytes(), "utf-8"));
            CSVUtils.createCSVFile(response.getOutputStream(),columnNamesList,data);
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
}
