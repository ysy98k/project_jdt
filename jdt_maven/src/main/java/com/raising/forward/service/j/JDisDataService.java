package com.raising.forward.service.j;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.common.NewBaseService;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 数据查询
 */
@Service
public class JDisDataService extends NewBaseService {

    /**
     * 历史曲线
     * @param ajaxParam
     * @return
     */
    public JSONObject getData(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);

        Integer currentRingNum = null;
        if(paramInfo.getInteger("MR_Ring_Num") == null){
            List<Integer> ringNum = getRingNum(ajaxParam);//查询所有环
            returnInfo.put("ringNums",ringNum);
            if( ringNum.size() > 0){//如果是第一次查询。则默认显示最小环
                currentRingNum = ringNum.get(0);
                paramInfo.put("MR_Ring_Num",currentRingNum);
                returnInfo.put("currentRingNum",currentRingNum );
            }else{//如果没有数据
                returnInfo.put("currentRingNum",-1);
                returnInfo.put("status", Constants.EXECUTE_FAIL);
                return returnInfo;
            }
        }
        String sql = "com.raising.forward.j.mapper.JDisData.getDisData";
        List<JSONObject> objectList = sqlSessionTemplate.selectList(sql, paramInfo);
        JSONArray lineArr = paramInfo.getJSONArray("lineArr");
        List<String> xAxis  = new ArrayList<>();//行程

        for(int i=0;i<lineArr.size();i++){
            List<String> data = new ArrayList<>();

            String columnName = lineArr.getString(i);
            int xAxisStart = 0;

            for(int k = 0;k<objectList.size();k++){
                JSONObject temp = objectList.get(k);
                String arDataStr = temp.getJSONObject("ARData").getString("value");
                JSONObject arData = JSONObject.parseObject(arDataStr);
                data.add(arData.getString(columnName));
                if(i == 0 && k == 0){
                    xAxisStart = temp.getInteger("CurMS");
                    xAxis.add("0");
                }else if(i == 0){
                    xAxis.add( temp.getInteger("CurMS") - xAxisStart + "" );
                }
            }
            returnInfo.put(columnName,data);
            if(i == 0){
                returnInfo.put("xAxis",xAxis);//存放里程信息
            }
        }
        returnInfo.put("status", Constants.EXECUTE_SUCCESS);

        return returnInfo;
    }


    public JSONObject getRows(String ajaxParam){
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = new JSONObject();
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        //数据校验
        String projectId = inquStatus.getString("projectId");
        inquStatus.put("projectId",Integer.parseInt(projectId));
        String querySql = "com.raising.forward.j.mapper.JDisData.getDisData";
        String countSql = "com.raising.forward.j.mapper.JDisData.count";
        String entity = "com.raising.forward.entity.j.JDisData";
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, querySql);
        paramInfo.put(BaseService.COUNT_SQL, countSql);
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        returnInfo = baseDao.query(paramInfo);
        if(Constants.EXECUTE_FAIL.equals(returnInfo.getString("status")) && "查询出错，请检查SQL语句！".equals(returnInfo.getString("returnMsg"))){
            //如果查询失败，且提示sql错误。则认为数据库没有此表
            returnInfo.put("returnMsg","里程数据没有上传，请先上传行程数据。");
            returnInfo.put("ringNums",new ArrayList<>());
            return returnInfo;

        }

        //获取环号信息
        List<Integer> ringNum = getRingNum(inquStatus.toJSONString());
        returnInfo.put("ringNums",ringNum);

        JSONArray rows =  returnInfo.getJSONArray("rows");
        if(rows == null || rows.size() < 1){
            return returnInfo;
        }
        /*筛选查询数据*/
        JSONArray columnArr = paramInfo.getJSONArray("columnArr");
        for(int i=0;i<rows.size();i++){
            JSONObject temp = rows.getJSONObject(i);
            Set<String> keySet = temp.keySet();
            String arDataStr = temp.getJSONObject("ARData").getString("value");
            JSONObject arData = JSONObject.parseObject(arDataStr);
            for(int k=0;k<columnArr.size();k++){
                String columnStr = columnArr.getString(k);
                if(!keySet.contains(columnStr)){//如果要查的数据，temp对象中没有。就从temp的ARData属性中取
                    temp.put(columnStr,arData.getString(columnStr));
                }

            }
        }
        return returnInfo;
    }


    /**
     * 获得环报表Data
     * @param paramJson
     * @return
     */
    public JSONObject getReportData(JSONObject paramJson){
        JSONObject returnInfo = new JSONObject();
        Integer MR_Ring_Num = paramJson.getInteger("MR_Ring_Num");
        if(MR_Ring_Num < 0){//如果小于0.则查出该项目所有环号
            List<Integer> ringsNum = getRingNum(paramJson.toJSONString());
            returnInfo.put("ringsNumList",ringsNum);
            if(ringsNum.size() < 1){
                return returnInfo;
            }
            paramJson.put("MR_Ring_Num",ringsNum.get(0));
        }

        String sql = "com.raising.forward.j.mapper.JDisData.getDisData";
        List<JSONObject> objectList = sqlSessionTemplate.selectList(sql, paramJson);

        List<String> xAxis  = new ArrayList<>();//行程
        List<String> JN = new ArrayList<>();
        List<String> JV = new ArrayList<>();
        List<String> CTor = new ArrayList<>();
        List<String> CRpm = new ArrayList<>();
        List<String> SP1 = new ArrayList<>();
        List<String> SCRpm = new ArrayList<>();
        List<String> SCTor = new ArrayList<>();
        List<String> MR_Act_A1HD = new ArrayList<>();//水平偏差
        List<String> MR_Act_A1VD = new ArrayList<>();//垂直偏差
        Integer J1SBefore = null;
        Integer J2SBefore = null;
        Integer J3SBefore = null;
        Integer J4SBefore = null;
        Integer J1SAfter = null;
        Integer J2SAfter = null;
        Integer J3SAfter = null;
        Integer J4SAfter = null;

        for(int k = 0;k<objectList.size();k++){
            JSONObject temp = objectList.get(k);

            String arDataStr = temp.getJSONObject("ARData").getString("value");
            JSONObject arData = JSONObject.parseObject(arDataStr);

            if( k ==0){
                J1SBefore = arData.getInteger("J1S");
                J2SBefore = arData.getInteger("J2S");
                J3SBefore = arData.getInteger("J3S");
                J4SBefore = arData.getInteger("J4S");
            }
            if(k == objectList.size() -1){
                J1SAfter = arData.getInteger("J1S");
                J2SAfter = arData.getInteger("J2S");
                J3SAfter = arData.getInteger("J3S");
                J4SAfter = arData.getInteger("J4S");
            }

            xAxis.add(temp.getString("CurMS"));
            JN.add(arData.getString("JN"));
            JV.add(arData.getString("JV"));
            CTor.add(arData.getString("CTor"));
            CRpm.add(arData.getString("CRpm"));
            SP1.add(arData.getString("SP1"));
            SCRpm.add(arData.getString("SCRpm"));
            SCTor.add(arData.getString("SCTor"));
            MR_Act_A1HD.add(arData.getString("MR_Act_A1HD"));
            MR_Act_A1VD.add(arData.getString("MR_Act_A1VD"));
        }

        returnInfo.put("xAxis",xAxis);
        returnInfo.put("JN",JN);
        returnInfo.put("JV",JV);
        returnInfo.put("CTor",CTor);
        returnInfo.put("CRpm",CRpm);
        returnInfo.put("SP1",SP1);
        returnInfo.put("SCRpm",SCRpm);
        returnInfo.put("SCTor",SCTor);
        returnInfo.put("MR_Act_A1HD",MR_Act_A1HD);
        returnInfo.put("MR_Act_A1VD",MR_Act_A1VD);
        returnInfo.put("J1SBefore",J1SBefore);
        returnInfo.put("J2SBefore",J2SBefore);
        returnInfo.put("J3SBefore",J3SBefore);
        returnInfo.put("J4SBefore",J4SBefore);
        returnInfo.put("J1SAfter",J1SAfter);
        returnInfo.put("J2SAfter",J2SAfter);
        returnInfo.put("J3SAfter",J3SAfter);
        returnInfo.put("J4SAfter",J4SAfter);
        returnInfo.put("MR_Ring_Num",paramJson.getInteger("MR_Ring_Num"));
        return returnInfo;
    }

    /**
     * 获得排土量报表data
     * @param paramJson
     * @return
     */
    public JSONObject getReportSoilData(JSONObject paramJson){
        JSONObject returnInfo = new JSONObject();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        Integer MR_Ring_Num = paramJson.getInteger("MR_Ring_Num");
        if(MR_Ring_Num < 0){//如果小于0.则查出该项目所有环号
            List<Integer> ringsNum = getRingNum(paramJson.toJSONString());
            returnInfo.put("ringsNumList",ringsNum);
            if(ringsNum.size() < 1){
                return returnInfo;
            }
            paramJson.put("MR_Ring_Num",ringsNum.get(0));
        }

        //查出所有信息
        String sql = "com.raising.forward.j.mapper.JDisData.getDisData";
        List<JSONObject> objectList = sqlSessionTemplate.selectList(sql, paramJson);


        //封装结果
        List<Integer> curMsList  = new ArrayList<>();//行程
        List<String> Dumping_volume_Total  = new ArrayList<>();//行程
        List<String> Dumping_volume_DTotal  = new ArrayList<>();//行程
        List<String> BeltRpm  = new ArrayList<>();//行程
        List<String> SP1  = new ArrayList<>();//行程

        Integer ringLen = paramJson.getInteger("ringLen");
        List<Integer> consume = new ArrayList<>();//工具
        for(int i=0;i<=ringLen;i+=100){
            curMsList.add(i);
            consume.add(i);
        }
        for(int i=0;i<objectList.size();i++){
            JSONObject temp = objectList.get(i);

            String arDataStr = temp.getJSONObject("ARData").getString("value");
            JSONObject arData = JSONObject.parseObject(arDataStr);

            Integer zero = consume.get(0);
            if(temp.getInteger("CurMS") >= zero ){//比较取出合理行程的数据
                consume.remove(0);
                Dumping_volume_Total.add(decimalFormat.format(arData.getDoubleValue("Dumping_volume_Total")) );//累计体积
                Dumping_volume_DTotal.add(decimalFormat.format(arData.getDoubleValue("Dumping_volume_DTotal"))  );//理论体积
                BeltRpm.add(decimalFormat.format(arData.getDoubleValue("BeltRpm")));//皮带机转速
                SP1.add(decimalFormat.format(arData.getDoubleValue("SP1")));//土压
                if(consume.size() < 1){
                    break;
                }
            }
        }
        //如果数据中行程数最大不超过1200。则填充空值
        for(int i=0;i<consume.size();i++){
            Dumping_volume_Total.add("");//累计体积
            Dumping_volume_DTotal.add("");//理论体积
            BeltRpm.add("");//皮带机转速
            SP1.add("");//土压
        }
        //如果数据中行程数最大不超过1200。则填充空值
        returnInfo.put("curMsList",curMsList);
        returnInfo.put("Dumping_volume_Total",Dumping_volume_Total);
        returnInfo.put("Dumping_volume_DTotal",Dumping_volume_DTotal);
        returnInfo.put("BeltRpm",BeltRpm);
        returnInfo.put("SP1",SP1);
        returnInfo.put("MR_Ring_Num",paramJson.getInteger("MR_Ring_Num"));
        return  returnInfo;
    }

    /**
     * 获取环号信息
     * @param ajaxParam
     * @return
     */
    public List<Integer> getRingNum(String ajaxParam){
        JSONObject paramJson = JSONObject.parseObject(ajaxParam);
        String sql = "com.raising.forward.j.mapper.JDisData.getTripRangeData";
        List<Integer> ringNums =  sqlSessionTemplate.selectList(sql,paramJson);
        if(ringNums == null){
            ringNums = new ArrayList<>();
        }
        return ringNums;

    }

    /**
     * 根据项目Id，获取所有数据
     * @param paramJson
     * @return
     */
    public List<JSONObject> getDataByProjectIds(JSONObject paramJson){
        String sql = "com.raising.forward.j.mapper.JDisData.getAllData";
        List<JSONObject> data = sqlSessionTemplate.selectList(sql, paramJson);
        return data;
    }

    /**
     * 根据项目id，获取最大Id
     * @param paramJson
     * @return
     */
    public Integer getMaxId(JSONObject paramJson){
        String sql = "com.raising.forward.j.mapper.JDisData.getMaxId";
        Integer max = sqlSessionTemplate.selectOne(sql, paramJson);
        return max;
    }
}
