package com.raising.backstage.service.privilege.projectManage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.common.NewBaseService;
import com.raising.forward.service.PropertiesValue;
import com.util.ExcelUtil;
import com.util.UploadUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.baosight.common.utils.DateUtils.String2Date;

/**
 * 报警变量管理Service
 */
@Service
public class AlarmVariableManagementService extends NewBaseService {

    public JSONObject getRows(String ajaxParam){
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = new JSONObject();
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        String querySql = "com.raising.forward.construction.mapper.AlarmCodeMapper.getRows";
        String countSql = "com.raising.forward.construction.mapper.AlarmCodeMapper.getCount";
        String entity = "com.raising.backstage.entity.projectManage.AlarmCode";
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, querySql);
        paramInfo.put(BaseService.COUNT_SQL, countSql);
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        returnInfo = daoUtil.query(paramInfo);
        JSONArray rows = returnInfo.getJSONArray("rows");
        if(rows == null || rows.size() < 1){
            return returnInfo;
        }
        for(int i =0;i<rows.size();i++){
            JSONObject temp = rows.getJSONObject(i);
            int alarmType = temp.getInteger("alarmType");
            if(1 == alarmType){
                temp.put("alarmType","设备报警");
            }else if(2 == alarmType){
                temp.put("alarmType","风险报警");
            }else if(3 == alarmType){
                temp.put("alarmType","导向报警");
            }
        }
        return returnInfo;
    }

    @Transactional
    public JSONObject addRows(String ajaxParam) throws Exception {
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONObject allData = paramInfo.getJSONObject("detail").getJSONArray("resultRow").getJSONObject(0);
        JSONArray dataArray = allData.getJSONArray("alarmCodeArr");
        for(int i=0;i<dataArray.size();i++){
            JSONObject dataJson = dataArray.getJSONObject(i);
            dataJson.put("alarmType",dataJson.getInteger("alarmType"));
            dataJson.put("alarmCode",dataJson.getInteger("alarmCode"));

        }
        String insertSql = "com.raising.forward.construction.mapper.AlarmCodeMapper.addRow";
        String entity = "com.raising.backstage.entity.projectManage.AlarmCode";
        paramInfo.put(BaseService.RESULT_BLOCK,"detail");
        paramInfo.put(BaseService.INSERT_SQL, insertSql);
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        JSONObject returnInfo = daoUtil.insert(paramInfo);
        return returnInfo;
    }

    @Transactional
    public JSONObject updateRows(String ajaxParam) throws Exception {
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONArray dataArray = paramInfo.getJSONObject("detail").getJSONArray("resultRow");
        for(int i=0;i<dataArray.size();i++){
            JSONObject dataJson = dataArray.getJSONObject(i);
            dataJson.put("alarmType",dataJson.getInteger("alarmType"));
            dataJson.put("alarmCode",dataJson.getInteger("alarmCode"));
            dataJson.put("alarmCodeId",dataJson.getInteger("alarmCodeId"));
        }
        String updateSql = "com.raising.forward.construction.mapper.AlarmCodeMapper.updateRow";
        String entity = "com.raising.backstage.entity.projectManage.AlarmCode";
        paramInfo.put(BaseService.RESULT_BLOCK,"detail");
        paramInfo.put(BaseService.UPDATE_SQL, updateSql);
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        JSONObject returnInfo = daoUtil.update(paramInfo);
        return returnInfo;
    }

    @Transactional
    public JSONObject delete(String ajaxParam) throws Exception {
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONArray dataArray = paramInfo.getJSONObject("result").getJSONArray("resultRow");
        for(int i=0;i<dataArray.size();i++){
            JSONObject dataJson = dataArray.getJSONObject(i);
            dataJson.put("alarmCodeId",dataJson.getIntValue("alarmCodeId"));
        }
        String deleteSql = "com.raising.forward.construction.mapper.AlarmCodeMapper.deleteRow";
        String entity = "com.raising.backstage.entity.projectManage.AlarmCode";
        paramInfo.put(BaseService.DELETE_SQL, deleteSql);
        paramInfo.put(BaseService.RESULT_BLOCK, "result");
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        JSONObject returnInfo = daoUtil.delete(paramInfo);
        return returnInfo;
    }

    /**
     * 根据项目Id，获取所有数据
     * @param paramJson
     * @return
     */
    public List<JSONObject> getDataByProjectIds(JSONObject paramJson){
        String sql = "com.raising.forward.construction.mapper.AlarmCodeMapper.getAllData";
        List<JSONObject> data = sqlSessionTemplate.selectList(sql, paramJson);
        return data;
    }

    /**
     * 根据项目id，获取最大Id
     * @param paramJson
     * @return
     */
    public Integer getMaxId(JSONObject paramJson){
        String sql = "com.raising.forward.construction.mapper.AlarmCodeMapper.getMaxId";
        Integer max = sqlSessionTemplate.selectOne(sql, paramJson);
        return max;
    }



}
