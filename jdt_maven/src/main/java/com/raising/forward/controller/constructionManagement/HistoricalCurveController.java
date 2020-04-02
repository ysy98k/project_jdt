package com.raising.forward.controller.constructionManagement;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.raising.forward.service.j.JDisDataService;
import com.raising.forward.service.j.JMileageDataService;
import com.raising.forward.service.j.JRingDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 历史曲线
 */
@RestController
@RequestMapping("/raising/forward/construction/historicalCurve")
public class HistoricalCurveController {

    private static final Logger logger = LoggerFactory.getLogger(HistoricalCurveController.class);



    @Autowired
    private JMileageDataService jMileageDataService;

    @Autowired
    private JRingDataService jRingDataService;

    @Autowired
    private JDisDataService jDisDataService;


    @RequestMapping(value = "/getData.do",method = RequestMethod.GET)
    public JSONObject getData(String ajaxParam){
        JSONObject returnInfo = null;
        JSONObject paramJson = JSONObject.parseObject(ajaxParam);

        switch (paramJson.getString("type")){
            case "mileage":
                try {
                    returnInfo = jMileageDataService.getData(ajaxParam);
                }catch (Exception e){
                    returnInfo = new JSONObject();
                    returnInfo.put("status", Constants.EXECUTE_FAIL);
                }
                break;
            case "ring":
                try {
                    returnInfo = jDisDataService.getData(ajaxParam);
                }catch (Exception e){
                    returnInfo = new JSONObject();
                    returnInfo.put("status", Constants.EXECUTE_FAIL);
                }
                break;
            default:
                break;
        }
        if("1".equals(paramJson.getString("getRing"))){
            try{
                JSONObject mileageData =  jMileageDataService.getMileageRange(ajaxParam);
                returnInfo.put("maxMileage",mileageData.getString("max"));
                returnInfo.put("minMileage",mileageData.getString("min"));
            }catch (Exception e){
                returnInfo.put("maxMileage","");
                returnInfo.put("minMileage","");
            }

        }
        return returnInfo;
    }

}
