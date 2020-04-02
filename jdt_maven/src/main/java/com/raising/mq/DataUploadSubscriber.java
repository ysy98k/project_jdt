package com.raising.mq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.raising.rest.service.SendService;
import com.raising.rest.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import redis.clients.jedis.JedisPubSub;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 消息消费者
 */
public class DataUploadSubscriber implements MessageListener {

    @Autowired
    private StoreService storeService;

    @Autowired
    private SendService sendService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        try {
            RedisSerializer<?> serializer = redisTemplate.getValueSerializer();
            JSONArray paramArray = (JSONArray)serializer.deserialize(message.getBody());
            for(int i=0;i<paramArray.size();i++){
                JSONObject paramJson = paramArray.getJSONObject(i);
                if(paramJson.getBoolean("realtime")){//如果是实时数据
                    String proName = paramJson.getString("proName");
                    String collectorName = paramJson.getString("collectorName");
                    JSONArray arr = paramJson.getJSONArray("data");
                    JSONObject data = arr.getJSONObject(0);
                    Set<String> set = data.keySet();//TreeSet自然排序。
                    TreeSet<String> keySet = new TreeSet(set);
                    keySet.add(proName);
                    keySet.add(collectorName);
                    redisTemplate.delete(keySet.toString());
                    JSONArray tsdb_instance_arr = (JSONArray)redisTemplate.opsForValue().get(keySet.toString());
                    if(tsdb_instance_arr == null){
                        tsdb_instance_arr = sendService.getTSDBInstanceInfo(paramJson);
                        redisTemplate.opsForValue().set(keySet.toString(),tsdb_instance_arr,3, TimeUnit.DAYS);
                    }
                    sendService.sendToTSDB(paramJson,tsdb_instance_arr );
                }else{//如果是非实时数据
                    String clientTableName = paramJson.getString("clientTableName");
                    if("project".equals(clientTableName)){//如果是项目表，单独处理
                        storeService.saveProject(paramJson);
                    }else{
                        if("disdata".equals(clientTableName) || "mileagedata".equals(clientTableName)){
                            if(!storeService.existTable(paramJson)){//如果不存在表。则创建
                                storeService.createSequence(paramJson);
                                storeService.createTable(paramJson);
                                //更新disdata、mileagedata表名缓存
                                String allTableNamesStr = (String)redisTemplate.opsForValue().get("dataSyncTableNames");
                                if(!StringUtils.isNullOrEmpty(allTableNamesStr)){
                                    JSONArray arr = JSONArray.parseArray(allTableNamesStr);
                                    String tableName = paramJson.getString("tableName");
                                    arr.add(tableName);
                                    redisTemplate.opsForValue().set("dataSyncTableNames",arr.toJSONString());
                                }
                            }
                        }
                        //存储
                        storeService.saveData(paramJson);
                    }
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
