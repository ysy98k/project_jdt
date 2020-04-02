package com.raising.forward.service.j;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.common.NewBaseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class JSettingService extends NewBaseService {

    /**
     * 根据projectId,name获取数据。name可为null。
     * @param projectId
     * @param name
     * @return
     */
    public List<JSONObject> getData(Integer projectId,String name){
        JSONObject paramJson = new JSONObject();
        paramJson.put("projectId",projectId);
        paramJson.put("name",name);

        String sql = "com.raising.forward.j.mapper.JSetting.getData";
        List<JSONObject> objects = sqlSessionTemplate.selectList(sql, paramJson);
        return objects;
    }

    /**
     * 根据项目Id，获取所有数据
     * @param paramJson
     * @return
     */
    public List<JSONObject> getDataByProjectIds(JSONObject paramJson){
        String sql = "com.raising.forward.j.mapper.JSetting.getAllData";
        List<JSONObject> data = sqlSessionTemplate.selectList(sql, paramJson);
        return data;
    }

    /**
     * 根据项目id，获取最大Id
     * @param paramJson
     * @return
     */
    public Integer getMaxId(JSONObject paramJson){
        String sql = "com.raising.forward.j.mapper.JSetting.getMaxId";
        Integer max = sqlSessionTemplate.selectOne(sql, paramJson);
        return max;
    }



}
