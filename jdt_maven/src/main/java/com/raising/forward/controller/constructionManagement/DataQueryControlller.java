package com.raising.forward.controller.constructionManagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.RedisUtils;
import com.baosight.common.utils.StringUtils;
import com.common.NewDownloadExcelController;

import com.raising.forward.service.j.JDisDataService;
import com.raising.forward.service.j.JMileageDataService;
import com.raising.forward.service.j.JRingDataService;
import com.raising.forward.service.j.JTimeDataService;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/raising/forward/construction/dataQuery")
public class DataQueryControlller extends NewDownloadExcelController {

    private static final Logger logger = LoggerFactory.getLogger(DataQueryControlller.class);

    private static  String columnArr1 = "SP1,SP2,SP3,SP4";
    private static  String columnArr2 = "CRpm,CTor";
    private static  String columnArr3 = "C1EmC,C2EmC,C3EmC,C4EmC,C5EmC,C6EmC,C1Tor,C2Tor,C3Tor,C4Tor,C5Tor,C6Tor";
    private static  String columnArr4 = "JN,J1P,J2P,J3P,J4P,J1S,J2S,J3S,J4S,J1V,J2V,J3V,J4V";
    private static  String columnArr5 = "SCRpm,SCTor,SCOilFP,SCOilP,SCOilBP";
    private static  String columnArr6 = "HJ1S,HJ2S,HJ3S,HJ4S";
    private static  String columnArr7 = "BOF1P,BOF2P,BOF3P,BOF4P,BOF5P,BOF6P,BOF7P,BOF8P,BOB1P,BOB2P,BOB3P,BOB4P,BOB5P,BOB6P,BOB7P,BOB8P";
    private static  String columnArr8 = "G1P,G2P,G3P,G4P,G1LM,G12LM,G3LM,G4LM,G1Total,G2Total,G3Total,G4Total";
    private static  String columnArr9 = "E1P,E2P,E1LM,E2LM,FO1P,FO2P,FO3P,FO4P";
    private static  String columnArr10 = "waterT,waterBL,waterP";
    private static  String columnArr11 = "BenTkBL,MixtureBL,StosteBL";
    private static  String columnArr12 = "Air1P,Air2P,Air3P,Air4P";
    private static  String columnArr13 = "MR_Ring_Num";

    private static  String namesArr1 = "开挖面土压（上）(MPa),开挖面土压（下）(MPa),开挖面土压（左）(MPa),开挖面土压（右）(MPa)";
    private static  String namesArr2 = "刀盘转速(rpm),刀盘扭距(kNm)";
    private static  String namesArr3 = "No.1刀盘电动机电流(A),No.2刀盘电动机电流(A),No.3刀盘电动机电流(A),No.4刀盘电动机电流(A),No.5刀盘电动机电流(A),No.6刀盘电动机电流(A),No.1刀盘电动机扭矩(kN*m),No.2刀盘电动机扭矩(kN*m),No.3刀盘电动机扭矩(kN*m),No.4刀盘电动机扭矩(kN*m),No.5刀盘电动机扭矩(kN*m),No.6刀盘电动机扭矩(kN*m)";
    private static  String namesArr4 = "盾构千斤顶总推力(KN),盾构分区压力（上）(MPa),盾构分区压力（右）(MPa),盾构分区压力（下）(MPa),盾构分区压力（左）(MPa),1号行程传感器,2号行程传感器,3号行程传感器,4号行程传感器,No.1盾构千斤顶速度(mm/min),No.5盾构千斤顶速度(mm/min),No.9盾构千斤顶速度(mm/min),No.13盾构千斤顶速度(mm/min)";
    private static  String namesArr5 = "螺旋机转速(min^-1),螺旋机扭矩(KN·M),螺旋机压力（前）(MPa),螺旋机土压(中)(MPa),螺旋机土压(后)(MPa)";
    
    private static  String namesArr6 = "1#铰接千斤顶的行程,2#铰接千斤顶的行程,3#铰接千斤顶的行程,4#铰接千斤顶的行程";
    
    private static  String namesArr7 = "1#前仓油脂压力(MPa),2#前仓油脂压力(MPa),3#前仓油脂压力(MPa),4#前仓油脂压力(MPa),5#前仓油脂压力(MPa),6#前仓油脂压力(MPa),7#前仓油脂压力(MPa),8#前仓油脂压力(MPa),1#后仓油脂压力(MPa),2#后仓油脂压力(MPa),3#后仓油脂压力(MPa),4#后仓油脂压力(MPa),5#后仓油脂压力(MPa),6#后仓油脂压力(MPa),7#后仓油脂压力(MPa),8#后仓油脂压力(MPa)";
    
    private static  String namesArr8 = "注浆注入口1压力(MPa),注浆注入口2压力(MPa),注浆注入口3压力(MPa),注浆注入口4压力(MPa),注浆管1注浆流量(L/min),注浆管2注浆流量(L/min),注浆管3注浆流量(L/min),注浆管4注浆流量(L/min),注浆管1注入浆量累计值(L),注浆管2注入浆量累计值(L),注浆管3注入浆量累计值(L),注浆管4注入浆量累计值(L)";
    
    private static  String namesArr9 = "1号膨润土压力(MPa),2号膨润土压力(MPa),膨润土管1#流量 (L/min),膨润土管2#流量 (L/min),泡沫管1#压力(MPa),泡沫管2#压力(MPa),泡沫管3#压力(MPa),泡沫管4#压力(MPa)";
    private static  String namesArr10 = "工业水温度(℃),工业水液位,工业水压力(MPa)";
    private static  String namesArr11 = "膨润土液位(%),混合液罐液位(%),原液液位(%)";
    private static  String namesArr12 = "空气管1#压力(MPa),空气管2#压力(MPa),空气管3#压力(MPa),空气管4#压力(MPa)";
    private static  String namesArr13 = "环号";


    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private JTimeDataService jTimeDataService;

    @Autowired
    private JMileageDataService jMileageDataService;

    @Autowired
    private JRingDataService jRingDataService;

    @Autowired
    private JDisDataService jDisDataService;

    @RequestMapping(value = "/getRows.do",method = RequestMethod.POST)
    public JSONObject getRows(String ajaxParam){
        JSONObject paramJson = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = null;

        switch (paramJson.getString("type")){
            case "mileage":
                returnInfo = jMileageDataService.getRows(ajaxParam);
                break;
            case "trip":
                returnInfo = jDisDataService.getRows(ajaxParam);
                break;
            case "ring":
                returnInfo = jRingDataService.getRows(ajaxParam);
                break;
            case "time":
                returnInfo = jTimeDataService.getData(ajaxParam);
                break;
            default:
                break;
        }

        return returnInfo;
    }

    @RequestMapping(value = "/downExcel.do",method = RequestMethod.GET)
    public void downExcel(HttpServletRequest request, HttpServletResponse response){
        JSONArray result = new JSONArray();
        redisUtils.set(request.getSession().getAttribute("username").toString()+"_"+"downloadProgress","0");
        try {
            String ajaxParam = request.getParameter("ajaxParam");
            ajaxParam = URLDecoder.decode(ajaxParam, "UTF-8");
            JSONObject ajaxJson = JSONObject.parseObject(ajaxParam);

            util(ajaxJson);
            List<String> columnsList = (List<String>)ajaxJson.get("columnsList");
            List<String> columnNamesList = (List<String>)ajaxJson.get("columnNamesList");
            do{
                JSONObject data = getRows(ajaxJson.toJSONString());
                if(Constants.EXECUTE_SUCCESS.equals(data.getString("status"))){
                    JSONArray rows = data.getJSONArray("rows");
                    result.addAll(rows);
                    if(result.size() >= data.getInteger("records")){
                        break;
                    }
                }else{
                    break;
                }
                ajaxJson.put("curPage",ajaxJson.getInteger("curPage")+1);
            }while (true);
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((new Date().getTime() + ".csv").getBytes(), "utf-8"));
            CSVUtils.downloadJqgridData(response.getOutputStream(),columnNamesList,columnsList,result);
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    @RequestMapping(value = "/getProgress.do",method = RequestMethod.GET)
    public JSONObject getProgress(HttpServletRequest request){
        JSONObject returnInfo = new JSONObject();
        String username = request.getSession().getAttribute("username").toString();
        String progress = redisUtils.get(username + "_" + "downloadProgress");

        if(!StringUtils.isNullOrEmpty(progress)){
            returnInfo.put("status",Constants.EXECUTE_SUCCESS);
            Integer p = Integer.parseInt(progress);
            if(p >= 100){
                returnInfo.put("progress","100%");
                redisUtils.del(username + "_" + "downloadTotal");
                redisUtils.del(username + "_" + "downloadCurrent");
                redisUtils.del(username + "_" + "downloadProgress");
            }else{
                returnInfo.put("progress",p+"%");
            }
        }else{
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            redisUtils.del(username + "_" + "downloadTotal");
            redisUtils.del(username + "_" + "downloadCurrent");
        }

        return returnInfo;
    }

    private void util(JSONObject ajaxJson){
        String downloadColumn = ajaxJson.getString("downloadColumn");
        String downloadColumnDesc = ajaxJson.getString("downloadColumnDesc");
        JSONArray column = ajaxJson.getJSONArray("mts_column");
        StringBuffer codeTemp = new StringBuffer();
        StringBuffer nameTemp = new StringBuffer();
        for(int i=0;i<column.size();i++){
            String integer = column.getString(i);
            if(integer.equals("土压")){
                codeTemp.append(",").append(columnArr1);
                nameTemp.append(",").append(namesArr1);
            }else if(integer.equals("刀盘")){
                codeTemp.append(",").append(columnArr2);
                nameTemp.append(",").append(namesArr2);
            }else if(integer.equals("主驱动电机")){
                codeTemp.append(",").append(columnArr3);
                nameTemp.append(",").append(namesArr3);
            }else if(integer.equals("推进")){
                codeTemp.append(",").append(columnArr4);
                nameTemp.append(",").append(namesArr4);
            }else if(integer.equals("螺旋输送机")){
                codeTemp.append(",").append(columnArr5);
                nameTemp.append(",").append(namesArr5);
            }else if(integer.equals("铰接")){
                codeTemp.append(",").append(columnArr6);
                nameTemp.append(",").append(namesArr6);
            }else if(integer.equals("盾尾密封")){
                codeTemp.append(",").append(columnArr7);
                nameTemp.append(",").append(namesArr7);
            }else if(integer.equals("注浆")){
                codeTemp.append(",").append(columnArr8);
                nameTemp.append(",").append(namesArr8);
            }else if(integer.equals("渣土改良")){
                codeTemp.append(",").append(columnArr9);
                nameTemp.append(",").append(namesArr9);
            }else if(integer.equals("工业水系统")){
                codeTemp.append(",").append(columnArr10);
                nameTemp.append(",").append(namesArr10);
            }else if(integer.equals("液压站")){
                codeTemp.append(",").append(columnArr11);
                nameTemp.append(",").append(namesArr11);
            }else if(integer.equals("压缩空气")){
                codeTemp.append(",").append(columnArr12);
                nameTemp.append(",").append(namesArr12);
            }else if(integer.equals("环号")){
                codeTemp.append(",").append(columnArr13);
                nameTemp.append(",").append(namesArr13);
            }
        }
        downloadColumn = downloadColumn + codeTemp.toString();
        downloadColumnDesc = downloadColumnDesc+ nameTemp.toString();

        String[] split1 = downloadColumn.split(",");
        String[] split2 = downloadColumnDesc.split(",");
        List<String> columnsList = Arrays.asList(split1);
        List<String> columnNamesList = Arrays.asList(split2);
        ajaxJson.put("columnsList",columnsList);
        ajaxJson.put("columnNamesList",columnNamesList);
    }
}
