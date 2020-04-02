package com.raising.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.raising.forward.service.PropertiesValue;
import com.util.HttpUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;

/**
 * 注册tsdb
 */
@Component
@Path("/raising/register")
public class RegisterTSDB {


    @POST
    @Path("/tag")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject tag(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject param = JSONObject.parseObject(ajaxParam);
        String collectorName = param.getString("collectorName");
        JSONArray tags  = param.getJSONArray("tags");
        if(collectorName == null || tags == null || param.size() < 1){
            returnInfo.put("status", Constants.EXECUTE_FAIL);
            returnInfo.put("message","所传数据无内容");
            return returnInfo;
        }
        //查询添加tag点的tagId
        String addTag = "http://"+PropertiesValue.AAS_ADRESS+"/tsdbrest/api/tag?collector_name="+ collectorName;
        String get = HttpUtils.postRequestSendJSON(addTag,HttpUtils.getHeader(),tags.toJSONString());
        JSONObject result = JSONObject.parseObject(get);
        return result;
    }














}
