package com.raising.forward.service.d;

import com.alibaba.fastjson.JSONObject;
import com.common.NewBaseService;
import com.raising.forward.entity.ZeroConfig;
import com.raising.forward.mapper.ZeroConfigMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ysy
 * @date 2018/3/26 17:19
 * @description
 */
@Service
public class ZeroConfigService extends NewBaseService {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ZeroConfigMapper zeroConfigMapper;
    //乘以1000
    private static final String[] MULTIPLY_BY_1000 =
            {"TBM_A","TBM_B","Zero_LT_X","Zero_LT_Y","Zero_LT_Z","TBM_Z1_Correct","TBM_Z2_Correct","TBM_Z3_Correct","TBM_Z4_Correct"
                    ,"TBM_J1_Correct","TBM_J2_Correct","TBM_J3_Correct","TBM_J4_Correct"
                    ,"TBM_Z_Radius","TBM_J_Radius"};
    //弧度转变角度
    private static final String[] RADIANS_CHANGE_INTO_ANGLES  =
            {"TBM_Z1_Position","TBM_Z2_Position","TBM_Z3_Position","TBM_Z4_Position"
                    ,"TBM_J1_Position","TBM_J2_Position","TBM_J3_Position","TBM_J4_Position"
                    ,"Zero_LT_RollAngle","Zero_LT_PitchAngle","Zero_LT_AzimuthAngle"};
    //启用
    private static final String[] ENABLE = {"TBM_Z1_Enable","TBM_Z2_Enable","TBM_Z3_Enable","TBM_Z4_Enable","TBM_J1_Enable"
            ,"TBM_J1_Enable","TBM_J2_Enable","TBM_J3_Enable","TBM_J4_Enable"};

    public JSONObject queryJson() {
        JSONObject returnInfo = null;
        String tenant = this.request.getSession().getAttribute("tenant").toString();
        int SID = 0;
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals("selected_id")) {
                SID = Integer.parseInt(c.getValue());
            }
        }
        if(SID < 1){
            return null;
        }
        List<ZeroConfig> queryInfo = zeroConfigMapper.query(SID);
        if (queryInfo.size() == 0) {
            return returnInfo;
        }
        returnInfo =  opeartorZeroCofig(queryInfo);
        return returnInfo;
    }



    public void insert(ZeroConfig zeroConfig){
        zeroConfigMapper.insert(zeroConfig);
    }
    public void update(ZeroConfig zeroConfig){
        zeroConfigMapper.update(zeroConfig);
    }

    /**
     * 对查询数据做处理。参数必不为null或者.size() = 0
     * @param queryInfo
     * @return
     */
    private JSONObject opeartorZeroCofig(List<ZeroConfig> queryInfo){
        JSONObject dataJson = new JSONObject();
        List<String> columns = new ArrayList<>();
        for(int i = 0;i<MULTIPLY_BY_1000.length;i++){
            columns.add(MULTIPLY_BY_1000[i]);
        }
        for(int i = 0;i<RADIANS_CHANGE_INTO_ANGLES.length;i++){
            columns.add(RADIANS_CHANGE_INTO_ANGLES[i]);
        }
        for(int i = 0;i<ENABLE.length;i++){
            columns.add(ENABLE[i]);
        }
        //对直接操作的dbName 使用setDbValueByDbName统一处理
        for(int i = 0;i<columns.size();i++){
            setDbValueByDbName(queryInfo,dataJson,columns.get(i));
        }
        //对需要换算的dbName，单独处理
        float tbm_b = getDbValueByDbName(queryInfo,"TBM_B");
        float tbm_c = getDbValueByDbName(queryInfo,"TBM_C");
        float tbm_e = getDbValueByDbName(queryInfo,"TBM_E");
        float tbm_f = getDbValueByDbName(queryInfo,"TBM_F");
        float tbm_g = getDbValueByDbName(queryInfo,"TBM_G");
        Float bValue = null;
        if(tbm_b != -1 && tbm_c != -1){
            bValue = tbm_b+ tbm_c*1000;
        }else if(tbm_b != -1 && tbm_c == -1){
            bValue = tbm_b;
        }else if(tbm_b == -1 && tbm_c != -1){
            bValue = tbm_c*1000;
        }else {
            bValue = null;
        }
        //dataJson.put("TBM_B",bValue *1000);
        dataJson.put("TBM_C",tbm_e*1000 < 0 ? null : tbm_e*1000);
        dataJson.put("TBM_D",tbm_c*1000 < 0 ? null : tbm_c*1000);
        dataJson.put("TBM_E",tbm_f*1000 < 0 ? null : tbm_f*1000);
        dataJson.put("TBM_F",tbm_g*1000 < 0 ? null : tbm_g*1000);
        //dataJson.put("TBM_G","");
        return dataJson;
    }

    /**
     * 根据dbName,将处理后的数据放入dataJson中
     * @param queryInfo
     * @param dataJson
     * @param dbName
     */
    private void setDbValueByDbName(List<ZeroConfig> queryInfo,JSONObject dataJson,String dbName){
        DecimalFormat df   = new DecimalFormat("0.0000");
        for(int j=0;j<queryInfo.size();j++){
            ZeroConfig zeroConfig = queryInfo.get(j);
            if(dbName.equals(zeroConfig.getDbName())){//如果queryInfo有此dbName值

                if(Arrays.asList(MULTIPLY_BY_1000).contains(dbName)){//如果要乘以1000的
                    dataJson.put(zeroConfig.getDbName(),Float.parseFloat(zeroConfig.getDbValue())*1000);
                    break;
                }else if(Arrays.asList(RADIANS_CHANGE_INTO_ANGLES).contains(dbName)){//如果要将弧度转变为角度的
                    double angle =  (Double.parseDouble(zeroConfig.getDbValue()) * 180 )/Math.PI;
                    String angleStr = df.format(angle);
                    dataJson.put(zeroConfig.getDbName(),angleStr);
                    break;
                }else if(Arrays.asList(ENABLE).contains(dbName)){//如果是是否启用
                    String dbValueStr = zeroConfig.getDbValue();
                    String enable = "0".equals(dbValueStr) || "1".equals(dbValueStr) ? dbValueStr : null;
                    dataJson.put(dbName,enable);
                    break;
                }
            }else {//如果queryInfo没有此dbName值，则置为null
                dataJson.put(dbName,null);
            }
        }

    }

    /**
     * 指定dbName名字。从零位配置 集合中获取ZeroConfig 值为dbName的 dbValue值
     * @param queryInfo
     * @param dbName
     * @return
     */
    private float getDbValueByDbName(List<ZeroConfig> queryInfo,String dbName){
        for(int j=0;j<queryInfo.size();j++){
            ZeroConfig zeroConfig = queryInfo.get(j);
            if(dbName.equals(zeroConfig.getDbName())){
                return Float.parseFloat(zeroConfig.getDbValue());
            }
        }
        return  -1;
    }


    public List<JSONObject> getDataByProjectIds(JSONObject paramJson){
        String sql = "com.raising.forward.mapper.ZeroConfigMapper.getAllData";
        List<JSONObject> objects = sqlSessionTemplate.selectList(sql, paramJson);
        if(objects == null){
            objects = new ArrayList<JSONObject>();
        }
        return objects;
    }

    public Integer getMaxId(JSONObject paramJson){
        String sql = "com.raising.forward.mapper.ZeroConfigMapper.getMaxId";
        Integer id = sqlSessionTemplate.selectOne(sql,paramJson);
        return id;
    }

}
