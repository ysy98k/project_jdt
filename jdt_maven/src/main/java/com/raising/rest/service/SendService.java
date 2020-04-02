package com.raising.rest.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.raising.forward.service.PropertiesValue;
import com.util.TsdbUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;

/**
 * 数据转发Service
 * 转发 tsdb 、关系数据
 */
@Service("sendService")
public class SendService {

    private static final Logger logger = LoggerFactory.getLogger(SendService.class);

    /**
     * 获得实例信息
     * @param paramJson
     */
    public JSONArray getTSDBInstanceInfo(JSONObject paramJson)  {
        JSONArray arr = paramJson.getJSONArray("data");
        JSONObject data = arr.getJSONObject(0);
        String collectorName = paramJson.getString("collectorName");
        Set<String> key =data.keySet();//key 是 sp1 ,MR_Ring_Number，格式。key中有个time值，此值无用
        JSONArray tagArr = new JSONArray();
        JSONArray instanceArr = null;

        for(String ketTemp : key){
            if("dt".equals(ketTemp)){
                continue;
            }
            tagArr.add(collectorName+"_"+ketTemp);
        }
        String url = "http://"+PropertiesValue.AAS_ADRESS+"/tsdbrest/api/instance";
        HttpPost post = new HttpPost(url);
        HttpEntity entity = new StringEntity(tagArr.toJSONString(),"utf-8");
        post.setEntity(entity);
        //String request = TsdbUtil.request(post);
        try {

            JSONObject retrunJson = TsdbUtil.getInstancesArr(tagArr);
            instanceArr = retrunJson.getJSONArray("data");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return instanceArr;
    }

    public void  sendToTSDB(JSONObject paramJson,JSONArray instanceArr){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONArray dataArr = paramJson.getJSONArray("data");
        JSONArray tsdbData = new JSONArray();
        String collectorName = paramJson.getString("collectorName");


        for(int i=0;i<instanceArr.size();i++){
            try{
                JSONObject instance = instanceArr.getJSONObject(i);
                String instanceName = instance.getString("instance_name");
                String tageName = instanceName.substring(collectorName.length()+1);
                for(int k =0;k<dataArr.size();k++){
                    JSONObject dataJson = dataArr.getJSONObject(k);
                    Object value = dataJson.get(tageName);
                    String time = dataJson.getString("dt");

                    JSONObject tsdbProperty = new JSONObject();
                    tsdbProperty.put("property_id",instance.getJSONArray("properties").getJSONObject(0).getInteger("id"));
                    tsdbProperty.put("type",instance.getJSONArray("properties").getJSONObject(0).getInteger("type"));
                    tsdbProperty.put("quality",192);
                    tsdbProperty.put("timestamp",dateFormat.parse(time).getTime());

                    /*if(tsdbProperty.getInteger("property_id") < 6 && value.toString().indexOf(".") >=0 ){
                        logger.error("mts----------","instanceName:"+instanceName+"：value:"+value);
                        System.out.println("mts----------instanceName:"+instanceName+"：value:"+value);
                    }*/

                    if(tsdbProperty.getInteger("property_id") >= 6){
                        BigDecimal temp = new BigDecimal(value.toString());
                        BigDecimal noZeros = temp.stripTrailingZeros();
                        String result = noZeros.toPlainString();
                        tsdbProperty.put("value",result);
                    }else if(tsdbProperty.getInteger("property_id") > 1){
                        tsdbProperty.put("value",value);
                    }else if(tsdbProperty.getInteger("property_id") == 1){
                        if(value instanceof  Number){
                            int intValue = Integer.parseInt(value.toString());
                            tsdbProperty.put("value",intValue == 0 ? false : true);
                        }else{
                            tsdbProperty.put("value",value);
                        }

                    }


                    JSONArray tsdbProperties = new JSONArray();
                    tsdbProperties.add(tsdbProperty);

                    JSONObject tsdb = new JSONObject();
                    tsdb.put("instance_id",instance.getInteger("instance_id"));
                    tsdb.put("property_values",tsdbProperties);

                    tsdbData.add(tsdb);
                    if(tageName.equals("D0010")){
                        System.out.println("D0010---"+tsdb);
                    }
                }

            }catch (Exception e){
                continue;
            }
        }
        String url = "http://"+ PropertiesValue.AAS_ADRESS+"/tsdbrest/api/record";
        HttpPost post = new HttpPost(url);
        post.setHeader(HttpHeaders.AUTHORIZATION, TsdbUtil.getHeader());
        System.out.println(tsdbData.toJSONString());
        HttpEntity entity = new StringEntity(tsdbData.toJSONString(),"utf-8");
        post.setEntity(entity);
        String request = TsdbUtil.request(post);
        System.out.println("result-----------"+request);
    }



}






























