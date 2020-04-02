package com.raising.rest.mobile;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;

import com.baosight.common.utils.StringUtils;
import com.common.BaseController;
import com.raising.forward.service.PropertiesValue;

import com.util.CSVUtils;
import com.util.MultipleDataSource;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


/**
 * 用户身份校验及绑定openID
 */
@Component
@Path("/raising")
public class UserBind extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(UserBind.class);

    //租户名称
    private static final String TENANT_NAME = "raising";
    //已经绑定
    private static final int BOUND = 1;

    @Autowired
    private SqlSessionTemplate sqlSession;



    /**
     * 用户身份校验及绑定openID
     * @param paramJson
     * @return
     */
    @POST
    @Path("/userbind")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject getRows(JSONObject paramJson){
        //返回值
        JSONObject returnInfo = null;
        //根据查询参数，做不同处理
        if(paramJson.containsKey("phone")){
            returnInfo = bindingOpenId(paramJson);
        }else{
            //openId 校验
            returnInfo = checkOpenId(paramJson);
        }
        return returnInfo;
    }

    @POST
    @Path("/openid")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject getInfo(JSONObject paramJson){
        //返回值
        JSONObject returnInfo = new JSONObject();
        BufferedReader in = null;
        String appId = "wx9e496b5f727b2e95";
        String secret = "cfa20c022ea87897c99d9abb848ab9e6";
        String code = paramJson.getString("code");
        String url="https://api.weixin.qq.com/sns/jscode2session?appid="
                +appId+"&secret="+secret+"&js_code="+code+"&grant_type=authorization_code";
        try {
            URL weChatUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = weChatUrl.openConnection();
            // 设置通用的请求属性
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
//            return sb.toString();
            String opid = sb.toString();
            JSONObject op = JSONObject.parseObject(opid);
            String OP2 = op.getString("openid");
            returnInfo.put("openid",OP2);
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
        // 使用finally块来关闭输入流
	        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return returnInfo;
    }

    /**
     * 检查表中是否有传入的openId
     * @param paramJson
     * @return
     */
    private JSONObject checkOpenId(JSONObject paramJson){
        JSONObject returnInfo = new JSONObject();
        //查询mobile记录
        String openId = paramJson.getString("openID");
        JSONObject resultJsons = mobileService.login(openId);

        if(resultJsons != null ){
            String userName = null;
            String pwd = resultJsons.getString("pwd");
            //根据userId。获取平台用户名
            Integer userId = resultJsons.getInteger("userId");

            try{
                MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
                JSONObject userJson = new JSONObject();
                userJson.put("userId",userId);
                List<JSONObject> users2 = userService.getUsers2(userJson);
                if(users2 == null || users2.size() < 1){
                    returnInfo.put("returnMsg","联系管理员注册！");
                    return returnInfo;
                }
                userName = users2.get(0).getString("userName");

            }finally {
                //数据源切换回来，
                MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
            }
            //根据平台用户名，和平台用户密码登陆，获得token
            String token = getToken(userName, pwd, TENANT_NAME);
            if(StringUtils.isNullOrEmpty(token)){//如果token不合法
                returnInfo.put("status",Constants.EXECUTE_FAIL);
                returnInfo.put("returnMsg","bind fail！");
            }else {
                returnInfo.put("status", BOUND);
                returnInfo.put("token", token);
                returnInfo.put("group", resultJsons.getString("groupName"));
                returnInfo.put("message", "not need bind！");
            }
        }else{
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("returnMsg","bind fail！");
        }
        return returnInfo;
    }

    /**
     * 根据入参openId，修改入参phone对应记录的openId值。
     * @param paramJson
     * @return
     */
    private JSONObject bindingOpenId(JSONObject paramJson){
        //返回值
        JSONObject returnInfo = new JSONObject();
        String openId = paramJson.getString("openID");
        String phone = paramJson.getString("phone");
        String pwd = paramJson.getString("password");

        JSONObject getUserIdJson = new JSONObject();
        getUserIdJson.put("tenant",TENANT_NAME);
        getUserIdJson.put("phone",phone);
        String sql = "com.raising.backstage.entity.Mobile.getRows";
        List<JSONObject> objects = sqlSession.selectList(sql, getUserIdJson);
        if(objects.size() < 1){
            returnInfo.put("returnMsg","联系管理员注册！");
            return returnInfo;
        }
        Integer userId = objects.get(0).getInteger("userId");

        String userName = null;
        String groupName = null;
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            JSONObject userJson = new JSONObject();
            userJson.put("userId",userId);
            List<JSONObject> users2 = userService.getUsers2(userJson);
            if(users2 == null || users2.size() < 1){
                returnInfo.put("returnMsg","联系管理员注册！");
                return returnInfo;
            }
            userName = users2.get(0).getString("userName");
            groupName = users2.get(0).getString("groupName");
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        //根据token判断是否有此用户
        String token = getToken(userName, pwd, TENANT_NAME);
        if(token == null){
            returnInfo.put("returnMsg","联系管理员注册！");
            return returnInfo;
        }
        JSONObject mobileParam = new JSONObject();
        mobileParam.put("openId",openId);
        mobileParam.put("pwd",pwd);
        mobileParam.put("phone",phone);
        mobileParam.put("bind",true);
        returnInfo = mobileService.bindingOpenId(mobileParam);


        if(returnInfo.getString("status").equals(Constants.EXECUTE_SUCCESS)){
            returnInfo.put("token",token);
            returnInfo.put("userName",userName);
            returnInfo.put("groupName",groupName);
            returnInfo.put("message", "bind success!");
        }else{
            returnInfo.put("returnMsg","联系管理员注册！");
        }
        return returnInfo;
    }



    /**
     * 登录aas ，获得token
     */
    public String getToken(String userName,String password,String tenant){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String loginUrl = PropertiesValue.AAS_API+"/session";
        HttpPost loginPost = new HttpPost(loginUrl);
        //httppost配置
        //设置请求和传输超时时间
        RequestConfig requestConfig =
                RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
        loginPost.setConfig(requestConfig);
        loginPost.addHeader("Content-Type", "application/json");
        //设置json请求参数
        JSONObject paramJson = new JSONObject();
        paramJson.put("username",userName);
        paramJson.put("tenant",tenant);
        paramJson.put("password",password);
        String paramStr = JSONObject.toJSONString(paramJson);

        try {
            StringEntity entity = new StringEntity(paramStr);
            loginPost.setEntity(entity);
            //请求
            CloseableHttpResponse loginResponse = httpClient.execute(loginPost);
            if(loginResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String loginReslut = EntityUtils.toString(loginResponse.getEntity(),"utf-8");
                JSONObject loginResultJson = JSONObject.parseObject(loginReslut);
                String token = loginResultJson.getString("token");
                return token;
            }
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            return null;
        } catch (ClientProtocolException e) {
            logger.error(e.getMessage());
            return null;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
