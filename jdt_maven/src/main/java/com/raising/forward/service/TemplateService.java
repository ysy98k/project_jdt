package com.raising.forward.service;

import com.alibaba.fastjson.JSONObject;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("templateService")
public class TemplateService {
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public JSONObject getTemplateName(String collectorName)
    {
        String sql1 = "com.raising.forward.mapper.ProjectInfoMapper.getTemplate";

        String sql2 = "com.raising.forward.mapper.ProjectInfoMapper.getTbmName";
//        Object o = new Object();

        JSONObject obj = new JSONObject();
        obj.put("collectorName",collectorName);


//        obj = JSON.parseObject(phone);
        JSONObject result = sqlSessionTemplate.selectOne(sql1,obj);
        String tbmId= result.getString("tbmId");
        int ftbmId = Integer.parseInt(tbmId);
        JSONObject obj2 = new JSONObject();
        obj2.put("tbmId",ftbmId);
        JSONObject tbmName = sqlSessionTemplate.selectOne(sql2,obj2);
        String tn=tbmName.getString("tbmName");
        result.put("tbmName",tn);
        return result;
    }
}
