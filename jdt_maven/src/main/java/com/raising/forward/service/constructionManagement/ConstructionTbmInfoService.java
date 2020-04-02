package com.raising.forward.service.constructionManagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.common.NewBaseService;
import com.util.DateUtils;
import com.util.TsdbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ConstructionTbmInfoService extends NewBaseService{
    private static final Logger logger = LoggerFactory.getLogger(ConstructionTbmInfoService.class);


    private static final   String[] attrStrs =
            {"TBM_Work_Type","TBM_Articulation_Type","TBM_Driving_Diameter","TBM_F_Diameter","TBM_M_Diameter",

                    "TBM_B_Diameter","TBM_B_Indiamete","TBM_E_Diameter","TBM_F_Length","TBM_B_Length",

                    "RingLen","TBM_J_Radius","TBM_J1_Position","TBM_J2_Position","TBM_J3_Position",

                    "TBM_J4_Position","TBM_A0_Length","TBM_Thickness","TBM_Clearance","TBM_Z_Radius",

                    "TBM_Z1_Position","TBM_Z2_Position","TBM_Z3_Position","TBM_Z4_Position","TBM_Name",

                    "TBM_Manufactor","TBM_Owner","TBM_Guidance","TBM_Maintenance","TBM_Manufactor_Telephone",

                    "TBM_Owner_Telephone","TBM_Guidance_Telephone","TBM_Responsible_Telephone","TBM_Maintenance_Telephone","TBM_Production_Date",

                    "TBM_Driving_Length","TBM_Max_Thrust","TBM_Max_Thrustpressure","TBM_Max_Tooltorque","TBM_Max_ScrewPressure",

                    "TBM_Ability_Dumpingsoil","TBM_Shoe_Length","TBM_Pressure_Current","TBM_Max_Screwtorque","TBM_Cutterhead_Speed",

                    "TBM_Ability_Hturn","TBM_Ability_Vturn","TBM_Max_Speed","TBM_Length","TBM_All_Length",

                    "TBM_Weight"};

    private static final List<String> attr = new ArrayList<String>(Arrays.asList(attrStrs));


    public JSONObject getInfo(String paramStr){
        JSONObject returnInfo = new JSONObject();
        JSONObject paramJson = new JSONObject();
        paramJson.put("projectId",Integer.parseInt(paramStr));
        String sql = "com.raising.forward.j.mapper.JSetting.getTbmRows";
        List<JSONObject> list = sqlSessionTemplate.selectList(sql, paramJson);
        if(list == null || list.size() < 1){
            return returnInfo;
        }
        Arrays.sort(attrStrs);
        for(int i=0;i<list.size();i++){
            JSONObject temp = list.get(i);

            if(attr.contains(temp.getString("name"))){
                returnInfo.put(temp.getString("name"),temp.getString("value"));
            }

        }
        return returnInfo;
    }

    /**
     * 获得盾构机履历
     * @param paramStr
     * @return
     */
    public JSONObject getTbmRecord(String paramStr){
        JSONObject returnInfo = new JSONObject();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject paramInfo = JSONObject.parseObject(paramStr);
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status") == null ? new JSONObject() : paramInfo.getJSONObject("inqu_status");
        //数据校验
        inquStatus.put("projectId",paramInfo.getInteger("projectId"));
        String querySql = "com.raising.forward.construction.mapper.TbmInfoMapper.getTbmRecord";
        String countSql = "com.raising.forward.construction.mapper.TbmInfoMapper.getTbmRecordCount";
        String entity = "com.raising.forward.entity.ProjectInfo";
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, querySql);
        paramInfo.put(BaseService.COUNT_SQL, countSql);
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        returnInfo = baseDao.query(paramInfo);
        JSONArray data = returnInfo.getJSONArray("rows");


        //获取tsdb数据
        JSONArray instanceNamesArray = new JSONArray();
        for(int i=0;i<data.size();i++){
            JSONObject dataTemp = data.getJSONObject(i);
            String collectorName = dataTemp.getString("collectorName");
            JSONObject obj1 = new JSONObject();
            obj1.put("instance_name",collectorName+"_MR_Ring_Num");
            instanceNamesArray.add(obj1);
        }
        JSONObject instanceJson = null;
        try {
            instanceJson = TsdbUtil.getInstanceOfRest(instanceNamesArray);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        for(int i =0;i<data.size();i++){
            JSONObject dataTemp = data.getJSONObject(i);
            String collectorName = dataTemp.getString("collectorName");

            if(instanceJson != null && Constants.EXECUTE_SUCCESS.equals(instanceJson.getString("status"))){
                JSONArray instanceArray =  instanceJson.getJSONArray("dataArray");
                if(instanceArray != null){
                    for(int j = 0;j<instanceArray.size();j++){
                        JSONObject instance = instanceArray.getJSONObject(j);
                        if(collectorName.equals(instance.getString("collectorName")) && "MR_Ring_Num".equals(instance.getString("tagName")) ){
                            String value = instance.getString("value");
                            if(value.indexOf(".")>0){
                                value = value.substring(0,value.indexOf("."));
                            }
                            dataTemp.put("currentRing",value);
                            dataTemp.put("quality",instance.getString("quality"));
                        }

                    }
                }
            }

            if("prostatus.nostart".equals(dataTemp.getString("status"))){
                dataTemp.put("status","未开始");
            }else if("prostatus.building".equals(dataTemp.getString("status"))){
                dataTemp.put("status","在建");
            }else if("prostatus.finished".equals(dataTemp.getString("status"))){
                dataTemp.put("status","竣工");
            }
            //设置工期
            if(dataTemp.getLong("startTime") != null){
                String startTime = dateFormat.format(new Date(dataTemp.getLong("startTime")));
                dataTemp.put("startTime",startTime);
            }
            if(dataTemp.getLong("endTime") != null){
                String endTime = dateFormat.format(new Date(dataTemp.getLong("endTime")));
                dataTemp.put("endTime",endTime);
            }
            if(dataTemp.getString("startTime") != null && dataTemp.getString("endTime") != null){
                long daySub = DateUtils.getDaySub(dataTemp.getString("startTime"), dataTemp.getString("endTime"));
                dataTemp.put("constructionPeroid",daySub);
            }

        }
        return returnInfo;
    }
}
