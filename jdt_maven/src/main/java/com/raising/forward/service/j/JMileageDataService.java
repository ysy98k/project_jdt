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

@Service
public class JMileageDataService extends NewBaseService {
    /**
     * 历史曲线。获取曲线展示数据
     * @param ajaxParam
     * @return
     */
    public JSONObject getData(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);

        String sql = "com.raising.forward.j.mapper.JMileageData.getArData";

        List<JSONObject> objectList = sqlSessionTemplate.selectList(sql, paramInfo);

        JSONArray lineArr = paramInfo.getJSONArray("lineArr");
        List<String> xAxis  = new ArrayList<>();//行程
        for(int i=0;i<lineArr.size();i++){
            List<String> data = new ArrayList<>();

            String columnName = lineArr.getString(i);
            for(int k = 0;k<objectList.size();k++){
                JSONObject temp = objectList.get(k);
                String arDataStr = temp.getJSONObject("ARData").getString("value");
                JSONObject arData = JSONObject.parseObject(arDataStr);
                data.add(arData.getString(columnName));
                if(i == 0){
                    xAxis.add(temp.getString("MR_Des_A1Mileage"));
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

    public JSONObject getMileageRange(String ajaxParam){
        JSONObject paramJson = JSONObject.parseObject(ajaxParam);
        String sql = "com.raising.forward.j.mapper.JMileageData.getMileageRangeData";
        JSONObject selectOne = sqlSessionTemplate.selectOne(sql, paramJson);
        if(selectOne == null){
            selectOne = new JSONObject();
            selectOne.put("max",0);
            selectOne.put("min",0);
        }
        return selectOne;
    }

    public JSONObject getRows(String ajaxParam){
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = new JSONObject();
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        //数据校验
        String projectId = inquStatus.getString("projectId");
        inquStatus.put("projectId",Integer.parseInt(projectId));
        String querySql = "com.raising.forward.j.mapper.JMileageData.getArDataForGrid";
        String countSql = "com.raising.forward.j.mapper.JMileageData.count";
        String entity = "com.raising.forward.entity.j.JMileageData";
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, querySql);
        paramInfo.put(BaseService.COUNT_SQL, countSql);
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        returnInfo = baseDao.query(paramInfo);

        if(Constants.EXECUTE_FAIL.equals(returnInfo.getString("status")) && "查询出错，请检查SQL语句！".equals(returnInfo.getString("returnMsg"))){
            //如果查询失败，且提示sql错误。则认为数据库没有此表
            returnInfo.put("returnMsg","里程数据没有上传，请先上传里程数据。");
            returnInfo.put("max","");
            returnInfo.put("min","");
            return returnInfo;

        }
        JSONObject mileageRange = getMileageRange(inquStatus.toJSONString());
        returnInfo.put("max",mileageRange.getString("max"));
        returnInfo.put("min",mileageRange.getString("min"));


        JSONArray rows =  returnInfo.getJSONArray("rows");
        if(rows == null || rows.size() < 1){
            return returnInfo;
        }
        /*筛选查询数据*/
        JSONArray columnArr = paramInfo.getJSONArray("columnArr");
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        for(int i=0;i<rows.size();i++){
            JSONObject temp = rows.getJSONObject(i);
            Set<String> keySet = temp.keySet();
            String arDataStr = temp.getJSONObject("ARData").getString("value");
            JSONObject arData = JSONObject.parseObject(arDataStr);
            for(int k=0;k<columnArr.size();k++){
                String columnStr = columnArr.getString(k);
                if(!keySet.contains(columnStr)){//如果要查的数据，temp对象中没有。就从temp的ARData属性中取
                    temp.put(columnStr,decimalFormat.format(arData.getDoubleValue(columnStr)));
                }

            }
        }
        return returnInfo;
    }

    /**
     * 根据项目Id，获取所有数据
     * @param paramJson
     * @return
     */
    public List<JSONObject> getDataByProjectIds(JSONObject paramJson){
        String sql = "com.raising.forward.j.mapper.JMileageData.getAllData";
        List<JSONObject> data = sqlSessionTemplate.selectList(sql, paramJson);
        return data;
    }

    /**
     * 根据项目id，获取最大Id
     * @param paramJson
     * @return
     */
    public Integer getMaxId(JSONObject paramJson){
        String sql = "com.raising.forward.j.mapper.JMileageData.getMaxId";
        Integer max = sqlSessionTemplate.selectOne(sql, paramJson);
        return max;
    }
}
