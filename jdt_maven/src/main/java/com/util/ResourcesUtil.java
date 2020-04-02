package com.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.raising.backstage.entity.SectionManage;
import com.raising.forward.service.PropertiesValue;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 获得用户权限下的Resources。
 */
public class ResourcesUtil {
    //所属服务
    private static  final String SERVICE = "PROP";

    private static final Logger logger = LoggerFactory.getLogger(ResourcesUtil.class);


    /**
     * 返回JSOnArray类型 resources 。值有可能为null.
     * @param token
     * @param groupName
     * @return
     */
    public static JSONArray getResources(String token,String groupName){
        JSONArray resultArray = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try{
            String path = PropertiesValue.AAS_API +"/usergroup/"+groupName+"/resource";
            path+="?service="+SERVICE+"&token="+token+"&limit=200";

            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.connect();
            if (conn.getResponseCode() == 200) {
                //获得服务器返回的字节流
                is = conn.getInputStream();
                //内存输出流,适合数据量比较小的字符串 和 图片
                baos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                byte[] result = baos.toByteArray();

                JSONObject jsonObject  = JSONObject.parseObject(new String(result, "utf-8"));
                resultArray = jsonObject.getJSONArray("resources");
                is.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }finally {
            try {
                if(baos != null) {
                    baos.close();
                }
                if(is != null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return resultArray;
        }

    }

    public static List<String> getCollectorNameResouces(String token,String groupName){
        List<String> collectorNames = new ArrayList<String>();
        JSONArray resouces =  getResources(token,groupName);
        if(resouces == null || resouces.size() < 1){
            return null;
        }
        for(int i=0;i<resouces.size();i++){
            JSONObject resouceTemp = resouces.getJSONObject(i);
            String collectorName = resouceTemp.getString("name");
            collectorNames.add(collectorName);
        }
        return collectorNames;
    }
}
