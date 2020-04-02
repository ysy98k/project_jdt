package com.raising.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.dao.RestDao;
import com.baosight.common.utils.RedisUtils;
import com.baosight.xinsight.redis.RedisUtil;
import com.google.gson.JsonObject;
import com.raising.forward.service.PropertiesValue;
import com.raising.rest.mobile.UserBind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Component
@Path("/raising")
public class PawChange {
    private String api = "/api/user";

    private String apiCheckString = "/api/session";

    @Value("${aas.rest_service_name}")
    private String serviceName;

    @Value("${aas.host}")
    private String host;

    @Autowired
    private UserBind userBind;

    @Autowired
    private RestDao dao;

    @Autowired
    private RedisUtils redisUtils;

    @POST
    @Path("/pawChange")
    @Produces (MediaType.APPLICATION_JSON)
     public JSONObject pawChange(String ajaxParam){
        JSONObject result = new JSONObject();
        JSONObject jsb = JSON.parseObject(ajaxParam);
        String username = jsb.getString("username");
        String paw = jsb.getString("password");
        //判断redis中token是否有效
        String rdtoken = jsb.getString("token");
        String temp=redisUtils.get(rdtoken);

        if ("1".equals( temp)) {
            String token = userBind.getToken("admin", PropertiesValue.ADMIN_PASSWORD, "raising");
            dao.setHost(host);
            dao.setServiceName(serviceName);
//       JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
            JSONObject mapIn = new JSONObject();
//        HttpSession session = this.request.getSession();
//        String token = session.getAttribute("token").toString();
//        String username = ajaxParamObj.getString("username");
            mapIn.put("token", token);
            mapIn.put("password", paw);
            JSONObject returnInfo = dao.invoke("put", api + "/" + username, mapIn);
            String  status = returnInfo.getString("errcode");
            if(status.equals("0")) result.put("success","密码修改成功");

        }
        else
        {
            result.put("error","token不存在或失效");
        }
        return result;
    }
}
