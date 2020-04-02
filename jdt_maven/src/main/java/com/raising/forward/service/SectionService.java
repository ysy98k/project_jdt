package com.raising.forward.service;

import com.alibaba.fastjson.JSONObject;
import com.common.NewBaseService;
import org.springframework.stereotype.Service;

@Service("sectionService")
public class SectionService extends NewBaseService {

    /**
     * 根据区间名获取Section
     * @param sectionName
     * @return
     */
    public JSONObject getSectionByName(String sectionName){
        String sql = "SectionManage.query";
        JSONObject param = new JSONObject();
        param.put("tenant","raising");
        param.put("sectionNameStrict",sectionName);
        JSONObject section = sqlSessionTemplate.selectOne(sql, param);
        return section;
    }

}
