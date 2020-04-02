package com.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.baosight.common.basic.dao.RestDao;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.baosight.xinsight.pds.tsdb.client.*;
import com.baosight.xinsight.pds.tsdb.client.exceptions.*;


import com.raising.forward.service.PropertiesValue;
import com.raising.thread.GetTsdbTimeRangeData;
import org.apache.commons.codec.binary.Base64;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.http.HttpHeaders;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.*;


public class TsdbUtil {

    public static CloseableHttpClient httpclient = HttpClientBuilder.create().build();



    public TsdbUtil(){}

    /**
     * 获取int类型数据
     * @param tagObj
     * @return
     */
    public static String getValue(JSONObject  tagObj){
        String value = tagObj.getString("value");
        if(value.indexOf(".")>0){
            return value.substring(0,value.indexOf("."));
        }
        return value;
    }

    /**
     * 根据点名，获取所有点数据。没有权限过滤
     * @param instanceNames
     * @return
     * @throws IOException
     */
    public static JSONObject getInstanceOfRest(JSONArray instanceNames) throws IOException {
        //先获取tagId集合.,根据tagId集合获取数据集合
        JSONObject returnInfo = new JSONObject();
        //第一步获取实例集合
        String tagsUrl = "http://"+PropertiesValue.AAS_ADRESS+"/tsdbrest/api/instance";
        String tagsStr = getConnectionResponse(tagsUrl,"POST", instanceNames.toJSONString());
        JSONObject instanceJson = JSONObject.parseObject(tagsStr);
        if(!Constants.EXECUTE_SUCCESS.equals(instanceJson.getString("errcode"))){//如果查询失败。返回
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("message","获取tag点失败");
            return returnInfo;
        }
        JSONArray instanceRecords = instanceJson.getJSONArray("records");
        if(instanceRecords.size() < 1){
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("message","没有tag点");
            return returnInfo;
        }
        //第一步获取快照
        Map<Integer,JSONObject> instanceMap = new HashMap<>();
        JSONArray instancesParam = new JSONArray();
        for(int i=0; i<instanceRecords.size(); i++){
            JSONObject tagRecord = instanceRecords.getJSONObject(i);
            JSONArray properties = tagRecord.getJSONArray("properties");
            if(properties.size()<1){
                continue;
            }
            JSONObject instance = new JSONObject();
            JSONArray property_ids = new JSONArray();
            property_ids.add(properties.getJSONObject(0).getInteger("id"));
            instance.put("property_ids",property_ids);
            instance.put("instance_id",tagRecord.getInteger("instance_id"));
            instancesParam.add(instance);
            instanceMap.put(tagRecord.getInteger("instance_id"),tagRecord);
        }
        String recordUrl = "http://"+PropertiesValue.AAS_ADRESS+"/tsdbrest/api/record/snapshot";
        String resultStr = getConnectionResponse(recordUrl,"POST",  instancesParam.toJSONString());
        JSONObject resultJson = JSONObject.parseObject(resultStr);
        if(!Constants.EXECUTE_SUCCESS.equals(resultJson.getString("errcode"))){
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("message","获取数据失败");
            return returnInfo;
        }
        JSONArray dataArray = resultJson.getJSONArray("records");
        //第三步，处理结果
        for(int i = 0;i<dataArray.size();i++){
            JSONObject dataJson = dataArray.getJSONObject(i);
            JSONArray propertieValues = dataJson.getJSONArray("property_values");
            if(propertieValues.size() < 1){
                continue;
            }
            JSONObject propertieValue = propertieValues.getJSONObject(0);
            int instance_id = dataJson.getInteger("instance_id");
            JSONObject instance = instanceMap.get(instance_id);
            String instanceName = instance.getString("instance_name");
            dataJson.put("instanceName",instanceName);
            dataJson.put("tagName",instanceName.substring(instanceName.indexOf("_")+1));
            dataJson.put("collectorName",instanceName.substring(0,instanceName.indexOf("_")));
            dataJson.put("value",propertieValue.getString("value"));
            dataJson.put("quality",propertieValue.getInteger("quality"));
            dataJson.put("timestamp",propertieValue.getLong("timestamp"));
        }

        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        returnInfo.put("dataArray",dataArray);
        return returnInfo;
    }




    public static String getConnectionResponse(String urlStr,String requestType,String writerStr ) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod(requestType);
        String plainCredentials = "admin@raising:"+PropertiesValue.ADMIN_PASSWORD;
        String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));
        urlConnection.setRequestProperty("Authorization", "Basic " + base64Credentials);
        urlConnection.setRequestProperty("Content-Type","application/json");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setConnectTimeout(100000);
        urlConnection.setReadTimeout(100000);
        urlConnection.connect();


        if("POST".equalsIgnoreCase(requestType) && !StringUtils.isNullOrEmpty(writerStr)){
            PrintWriter printWriter = new PrintWriter(urlConnection.getOutputStream());
            printWriter.write(writerStr);
            printWriter.flush();
        }
        InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream(),"utf-8");
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        while( (temp=reader.readLine()) != null){
            stringBuffer.append(temp);
        }
        reader.close();
        inputStreamReader.close();
        urlConnection.disconnect();
        return stringBuffer.toString();
    }

    public static String request( HttpUriRequest request){
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(request);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("responseCode"+response.toString());
        return resultString;
    }



    /**
     * 构造Basic Auth认证头信息
     *
     * @return
     */
    public static String getHeader() {
        String auth =  "admin@raising:"+PropertiesValue.ADMIN_PASSWORD;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
        String authHeader = "Basic " + new String(encodedAuth);
        return authHeader;
    }

    /**
     * 根据根据实例名称查询实例信息 http://example.domain.com/tsdbrest/api/instance
     * @param InstancesParamArr
     * @return
     * @throws IOException
     */
    public static JSONObject getInstancesArr(JSONArray InstancesParamArr) throws IOException {
        JSONObject returnInfo = new JSONObject();
        //第一步实例Id与属性Id
        String tagsUrl = "http://"+PropertiesValue.AAS_ADRESS+"/tsdbrest/api/instance";
        String tagsStr = getConnectionResponse(tagsUrl,"POST", InstancesParamArr.toJSONString());
        JSONObject tagsJson = JSONObject.parseObject(tagsStr);
        if(!Constants.EXECUTE_SUCCESS.equals(tagsJson.getString("errcode"))){//如果查询失败。返回
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("message","获取tag点失败");
            return returnInfo;
        }
        JSONArray tagRecords = tagsJson.getJSONArray("records");
        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        returnInfo.put("data",tagRecords);
        return returnInfo;
    }

    /**
     *历史记录区间范围查询 tsdbrest/api/record/range/instanceId/propertyId
     * @param instanceId
     * @param propertyId
     * @param sTime
     * @param endTime
     * @param interval
     * @param cursor_mask 小于0是为*
     * @return
     * @throws IOException
     */
    public static JSONObject getTimeRangeData(Integer instanceId,Integer propertyId,long sTime,long endTime,int interval,long cursor_mask) throws IOException {
        JSONObject retrunInfo = new JSONObject();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuffer urlBuffer = new StringBuffer("http://");
        urlBuffer.append(PropertiesValue.AAS_ADRESS);
        urlBuffer.append("/tsdbrest/api/record/range/");
        urlBuffer.append(instanceId);
        urlBuffer.append("/");
        urlBuffer.append(propertyId);
        urlBuffer.append("?limit=1000");
        urlBuffer.append("&start_time=");
        urlBuffer.append(sTime);
        urlBuffer.append("&end_time=");
        urlBuffer.append(endTime);
        urlBuffer.append("&interval=");
        urlBuffer.append(interval);
        if(cursor_mask < 0){
            urlBuffer.append("&cursor_mask=*");
        }else{
            urlBuffer.append("&cursor_mask=");
            urlBuffer.append(cursor_mask);
        }
        String recordsStr = getConnectionResponse(urlBuffer.toString(),"GET",null);
        JSONObject recordJson = JSONObject.parseObject(recordsStr);
        if(!Constants.EXECUTE_SUCCESS.equals(recordJson.getString("errcode"))){
            retrunInfo.put("status",Constants.EXECUTE_FAIL);
            return retrunInfo;
        }
        JSONArray records = recordJson.getJSONArray("records");
        for(int i=0;i<records.size();i++){
            JSONObject temp = records.getJSONObject(i);
            temp.put("dateTime",dateFormat.format(temp.getLong("timestamp")));
        }
        recordJson.put("status",Constants.EXECUTE_SUCCESS);
        return recordJson;
    }



    /**
     rest  接口获取数据结束
     */
    public String rest(String urlStr,String requestType,String writerStr ) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod(requestType);
        String plainCredentials = "admin@raising:rms56807268";
        String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));
        urlConnection.setRequestProperty("Authorization", "Basic " + base64Credentials);
        urlConnection.setRequestProperty("Content-Type","application/json");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setConnectTimeout(10000);
        urlConnection.setReadTimeout(10000);
        urlConnection.connect();


        if("POST".equalsIgnoreCase(requestType) && !StringUtils.isNullOrEmpty(writerStr)){
            PrintWriter printWriter = new PrintWriter(urlConnection.getOutputStream());
            printWriter.write(writerStr);
            printWriter.flush();
        }
        InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream(),"utf-8");
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        while( (temp=reader.readLine()) != null){
            stringBuffer.append(temp);
        }
        reader.close();
        inputStreamReader.close();
        urlConnection.disconnect();
        return stringBuffer.toString();
    }



    @Test
    public void test() throws Exception {


        //CloseableHttpClient build = HttpClientBuilder.create().build();

        // 创建用户信息
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
                = new UsernamePasswordCredentials("admin@raising", "rms56807268");
        provider.setCredentials(AuthScope.ANY, credentials);
        // 创建客户端的时候进行身份验证
        CloseableHttpClient build = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
        HttpGet request = new HttpGet("http://116.236.141.14:83/aas/api/session");
        // 将验证信息放入到 Header
        //request.setHeader(HttpHeaders.AUTHORIZATION, getHeader());

        CloseableHttpResponse response = build.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode == 200){
            System.out.println("200");
        }else{
            System.out.println(statusCode);
        }


    }


    @Test
    public void test1() throws PdsException, ParseException, IOException {
        String url = "https://bd.sh-raising.com/tsdbrest/api/record";
        String url2 = "http://116.236.141.14:83/tsdbrest/api/record";

        JSONArray arr = new JSONArray();
        JSONArray propertyArr = new JSONArray();
        JSONObject json = new JSONObject();
        JSONObject property = new JSONObject();
        property.put("value",3);
        property.put("timestamp",1574991790000L);
        property.put("type",6);
        property.put("property_id",6);
        property.put("quality",1);
        propertyArr.add(property);
        json.put("instance_id",1886);
        json.put("property_values",propertyArr);

        arr.add(json);

        String post = rest(url2, "POST", arr.toJSONString());
        System.out.println("mts--------"+post);
    }

    @Test
    public void test2(){
        List< GetTsdbTimeRangeData> callableList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("--------------------------------"+dateFormat.format(new Date()));
        GetTsdbTimeRangeData thread1 = new GetTsdbTimeRangeData(8611,6,100,1557590400000L,1557936000000L,345600,-1);
        GetTsdbTimeRangeData thread2 = new GetTsdbTimeRangeData(8611,6,100,1557590400000L,1557936000000L,1,-1);
        GetTsdbTimeRangeData thread3 = new GetTsdbTimeRangeData(8611,6,100,1557590400000L,1557936000000L,1,-1);
        callableList.add(thread1);
        callableList.add(thread2);
        callableList.add(thread3);
        ExecutorService exec = Executors.newFixedThreadPool(3);
        try {
            List<Future<JSONObject>> futures = exec.invokeAll(callableList, 5, TimeUnit.SECONDS);
            for(int i=0;i<futures.size();i++){
                Future<JSONObject> future = futures.get(i);
                if(future.isDone()){
                    System.out.println("------------"+i+"----------done");
                    try {
                        JSONObject jsonObject = future.get();
                        String instanceId = jsonObject.getString("instanceId");
                        System.out.println("------------"+i+"----------done"+instanceId);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("------------"+i+"----------nodone");
                }

            }
            exec.shutdown();
            for(int i=0;i<futures.size();i++){
                Future<JSONObject> future = futures.get(i);
                if(future.isDone()){
                    System.out.println("------------"+i+"----------done");
                    try {
                        JSONObject jsonObject = future.get();
                        String instanceId = jsonObject.getString("instanceId");
                        System.out.println("------------"+i+"----------done"+instanceId);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("------------"+i+"----------nodone");
                }

            }
            System.out.println("--------------------------------"+dateFormat.format(new Date()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }








}
