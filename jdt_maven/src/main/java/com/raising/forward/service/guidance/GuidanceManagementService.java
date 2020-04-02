package com.raising.forward.service.guidance;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.common.NewBaseService;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 导向管理Service
 */
@Service
public class GuidanceManagementService extends NewBaseService {

    /**
     * 获得导向管理界面数据
     * @param ajaxParam
     * @return
     */
    public JSONObject getGuidanceData(String ajaxParam,String hingeType,Integer ringTotal){
        JSONObject returnInfo = new JSONObject();
        JSONObject paramJson = new JSONObject();
        Integer projectId = Integer.parseInt(ajaxParam);
        paramJson.put("projectId",projectId);
        String sql = "com.raising.forward.mapper.measureResult.getDuidanceData";
        List<JSONObject> dataList = sqlSessionTemplate.selectList(sql, paramJson);
        if(dataList == null || dataList.size() < 1 ){//如果表里没数据，直接返回。(* ￣3)(ε￣ *)
            return returnInfo;
        }
        List<Double> xAxis = new ArrayList<>();
        List<Double> incisionHorizontalDeviationsList = new ArrayList<>();
        List<Double> incisionVerticalDeviationsList = new ArrayList<>();
        List<Double> horizontalTrendsList = new ArrayList<>();
        List<Double> veticalTrendsList = new ArrayList<>();

        DecimalFormat two = new DecimalFormat("0.00");
        DecimalFormat three = new DecimalFormat("0.000");
        DecimalFormat four = new DecimalFormat("0.0000");
        for(int i=0;i<dataList.size();i++){
            JSONObject temp = dataList.get(i);
            temp.put("incisionHorizontalDeviation",Math.round(temp.getDoubleValue("incisionHorizontalDeviation")*1000));
            temp.put("incisionVerticalDeviation",Math.round(temp.getDoubleValue("incisionVerticalDeviation")*1000));
            temp.put("activeArticulationHorizontalDeviation",Math.round(temp.getDoubleValue("activeArticulationHorizontalDeviation")*1000));
            temp.put("activeArticulationVerticalDeviation",Math.round(temp.getDoubleValue("activeArticulationVerticalDeviation")*1000));
            temp.put("passiveArticulationHorizontalDeviation",Math.round(temp.getDoubleValue("passiveArticulationHorizontalDeviation")*1000));
            temp.put("passiveArticulationVerticalDeviation",Math.round(temp.getDoubleValue("passiveArticulationVerticalDeviation")*1000));
            temp.put("shieldTailHorizontalDeviation",Math.round(temp.getDoubleValue("shieldTailHorizontalDeviation")*1000));
            temp.put("shieldVerticalDeviation",Math.round(temp.getDoubleValue("shieldVerticalDeviation")*1000));
            temp.put("horizontalTrend",Math.round(temp.getDoubleValue("horizontalTrend")*1000));/*水平趋向*/
            temp.put("veticalTrend",Math.round(temp.getDoubleValue("veticalTrend")*1000));/*垂直趋向*/
            //滚动角俯仰角保留四位小数
            temp.put("rollingAngle",four.format(temp.getDoubleValue("rollingAngle")*57.29578));
            temp.put("pitchAngle",four.format(temp.getDoubleValue("pitchAngle")*57.29578));

            temp.put("mileage",three.format(temp.getDoubleValue("mileage")));
            temp.put("drivingLength",three.format(temp.getDoubleValue("drivingLength")));

            Double mileage = temp.getDouble("mileage");
            Double incisionHorizontalDeviation = temp.getDouble("incisionHorizontalDeviation");
            Double incisionVerticalDeviation = temp.getDouble("incisionVerticalDeviation");
            Double horizontalTrend = temp.getDouble("horizontalTrend");
            Double veticalTrend = temp.getDouble("veticalTrend");
            xAxis.add(mileage);
            incisionHorizontalDeviationsList.add(incisionHorizontalDeviation);
            incisionVerticalDeviationsList.add(incisionVerticalDeviation);
            horizontalTrendsList.add(horizontalTrend);//前盾趋势变化曲线
            veticalTrendsList.add(veticalTrend);//前盾趋势变化曲线
        }

        JSONObject newData = dataList.get(dataList.size()-1);
        if("主动".equals(hingeType)){
            newData.put("articulationHorizontalDeviation",newData.getDouble("activeArticulationHorizontalDeviation"));
            newData.put("articulationVerticalDeviation",newData.getDouble("activeArticulationVerticalDeviation"));
        }else if("被动".equals(hingeType)){
            newData.put("articulationHorizontalDeviation",newData.getDouble("passiveArticulationHorizontalDeviation"));
            newData.put("articulationVerticalDeviation",newData.getDouble("passiveArticulationVerticalDeviation"));
        }
        int ringNumber = newData.getInteger("ringNumber");
        double progress = ((ringNumber*1.0)/ringTotal)*100;//完成进度。等于完成环数/总环数
        returnInfo.put("newData",newData);
        returnInfo.put("progress",two.format(progress));
        returnInfo.put("xAxis",xAxis);
        returnInfo.put("incisionHorizontalDeviationsList",incisionHorizontalDeviationsList);
        returnInfo.put("incisionVerticalDeviationsList",incisionVerticalDeviationsList);
        returnInfo.put("horizontalTrendsList",horizontalTrendsList);
        returnInfo.put("veticalTrendsList",veticalTrendsList);
        returnInfo.put("status", Constants.EXECUTE_SUCCESS);
        return returnInfo;
    }





















}
