package com.raising.forward.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * aasccs t_user表Service
 */
@Service("userService")
public class UserService  {


    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;


    public List<JSONObject> getUsers2(JSONObject paramJson){
        String sql = "com.raising.forward.mapper.UserDao.getUsers2";
        List result = sqlSessionTemplate.selectList(sql,paramJson);
        return result;
    }
    public JSONObject getPhone(String phone)
    {
        String sql = "com.raising.forward.mapper.UserDao.getPhone";
//        Object o = new Object();

        JSONObject obj = new JSONObject();
        obj.put("phone",phone);
//        obj = JSON.parseObject(phone);
        JSONObject result = sqlSessionTemplate.selectOne(sql,obj);
        return result;
    }
}
