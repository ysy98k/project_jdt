package com.raising.forward.controller.materialConsumption;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.common.controller.DownloadExcelController;
import com.baosight.common.utils.StringUtils;
import com.raising.forward.controller.progressManage.ProgressAnalysisController;
import com.raising.forward.service.j.JSettingService;
import com.raising.forward.service.materialConsumption.MaterialConsumptionService;
import com.util.CSVUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/raising/forward/materialConsumption/materialConsumption")
public class MaterialConsumptionController extends DownloadExcelController {

    private static final Logger logger = LoggerFactory.getLogger(MaterialConsumptionController.class);

    @Autowired
    private MaterialConsumptionService materialConsumptionService;

    @Autowired
    private JSettingService jSettingService;

    @RequestMapping(value = "getRows.do",method = RequestMethod.POST)
    public JSONObject getRows(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject paramJson = JSONObject.parseObject(ajaxParam);

        //查询setting表获得单位，理论消耗值
        List<JSONObject> settingData = jSettingService.getData(paramJson.getInteger("projectId"), null);
        Double theory = null;
        String unit = "";
        Double defaultTheory = null;
        String defaultUnit = "";
        String settingCode = paramJson.getString("settingCode");
        String settingUnitCode = paramJson.getString("settingUnitCode");

        for(int i=0;i<settingData.size();i++){
            JSONObject temp = settingData.get(i);
            if(settingCode.equals(temp.getString("name"))){
                theory = temp.getDoubleValue("value");
            }
            if(settingUnitCode.equals(temp.getString("name"))){
                unit = temp.getString("value");
            }
            if(settingCode.equals(temp.getString("name")) && -1 == temp.getInteger("projectId") ){
                defaultTheory = temp.getDoubleValue("value");
            }
            if(settingUnitCode.equals(temp.getString("name")) && -1 == temp.getInteger("projectId") ){
                defaultUnit = temp.getString("value");
            }
        }
        paramJson.put("theory",theory ==null ? defaultTheory : theory );
        paramJson.put("unit", StringUtils.isNullOrEmpty(unit) ? defaultUnit : unit);
        //查询显示表格数据
        try {
            if("ring".equals(paramJson.getString("type"))){
                returnInfo = materialConsumptionService.operatorRingData(paramJson);
            }else{
                returnInfo = materialConsumptionService.getRows(paramJson);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return returnInfo;
    }

    @RequestMapping(value = "download.do",method = RequestMethod.GET)
    public void download(String ajaxParam,HttpServletRequest request, HttpServletResponse response){
        JSONArray result = new JSONArray();
        try {
            ajaxParam = URLDecoder.decode(ajaxParam, "UTF-8");
            JSONObject ajaxJson = JSONObject.parseObject(ajaxParam);
            String[] columnName = ajaxJson.getString("downloadColumnDesc").split(",");
            String[] columnCode = ajaxJson.getString("downloadColumn").split(",");
            List<String> columnNamesList = Arrays.asList(columnName);
            List<String> columnsList = Arrays.asList(columnCode);

            JSONObject dataJson = getRows(ajaxParam);
            result = dataJson.getJSONArray("rows");

            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((new Date().getTime() + ".csv").getBytes(), "utf-8"));
            CSVUtils.downloadJqgridData(response.getOutputStream(),columnNamesList,columnsList,result);
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }



}
