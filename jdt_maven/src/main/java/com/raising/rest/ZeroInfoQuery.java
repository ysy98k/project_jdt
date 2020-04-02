package com.raising.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.dao.RestDao;
import com.raising.backstage.entity.SectionManage;
import com.raising.forward.entity.ZeroConfig;
import com.raising.forward.mapper.ZeroConfigMapper;
import com.raising.forward.service.PropertiesValue;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
/**
 * @author ysy
 * @date 2018/5/11 14:34
 * @description
 */
@Component
@Path("/zero")
public class ZeroInfoQuery {
    private static final Logger logger = LoggerFactory
            .getLogger(com.baosight.df.metamanage.rest.RestPage.class);

    @Context
    HttpServletRequest request;
    @Context
    HttpServletResponse response;


    @Autowired
    private RestDao dao;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;


    @Autowired
    private ZeroConfigMapper zeroConfigMapper;

    @GET
    @Path("/configInfo")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONArray query(@QueryParam("token") String token,@QueryParam("groupName") String groupName) {
        JSONObject obj= new JSONObject();
        JSONArray returnInfo = new JSONArray();
        List<ZeroConfig> queryInfo=new ArrayList<>();

        try {
            /*String aasToken = request.getSession().getAttribute("token").toString();*/
            String aasToken = token;
            /*String groupNames = request.getSession().getAttribute("groupNames").toString();*/
            String groupNames = groupName;
            String path = PropertiesValue.AAS_API+"/usergroup/" + groupNames + "/resource";
            String service = "PROP";
            path += "?service=" + service + "&token=" + aasToken;

            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.connect();

            if (conn.getResponseCode() == 200) {
                //获得服务器返回的字节流
                InputStream is = conn.getInputStream();
                //内存输出流,适合数据量比较小的字符串 和 图片
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                byte[] result = baos.toByteArray();
                System.out.println(new String(result));
                String ccsinfo = result.toString();

                JSONObject jsonObject = JSONObject.parseObject(new String(result, "utf-8"));

                JSONArray Result = jsonObject.getJSONArray("resources");


                is.close();

                for (int i = 0; i < Result.size(); i++) {

                    JSONObject jo = Result.getJSONObject(i);
                    String name = jo.getString("name");

                    String collectorName = name;
                    String queryParam = collectorName + "_MR_Des_A1Mileage";
                    String queryRing = collectorName + "_MR_Ring_Num";
                    SectionManage daoEntity = new SectionManage();
                    daoEntity.setCollectorName(collectorName);
                    daoEntity.setSectionName("");
                    HashMap queryMap = new HashMap();
                    queryMap.put("tenant", "raising");
                    queryMap.put("ascDesc", null);
                    queryMap.put("filedName", null);
                    daoEntity.fromMap(queryMap);

                    request.getRemoteAddr();

                    Integer curPage = 1;
                    Integer curRowNum = 10;

                    String querySql = "SectionManage.query";

                    List<JSONObject> list = this.sqlSessionTemplate.selectList(querySql, daoEntity, new RowBounds((curPage.intValue() - 1) * curRowNum.intValue(), curRowNum.intValue()));
                    obj = list.get(0);
                    Integer sectionId =obj.getInteger("id");

                    queryInfo = zeroConfigMapper.query(sectionId);
     /*   ZeroConfig zeroConfig=new ZeroConfig();*/
                    if (queryInfo.size() == 0) {

                        return returnInfo;
                    }
                    queryInfo.get(0).setTbm_A(queryInfo.get(0).getTbm_A() * 1000);
                    queryInfo.get(0).setTbm_B((queryInfo.get(0).getTbm_B() + queryInfo.get(0).getTbm_C()) * 1000);
                    queryInfo.get(0).setTbm_D(queryInfo.get(0).getTbm_C() * 1000);
                    queryInfo.get(0).setTbm_C(queryInfo.get(0).getTbm_E() * 1000);
                    queryInfo.get(0).setTbm_E(queryInfo.get(0).getTbm_F() * 1000);
                    queryInfo.get(0).setTbm_F(queryInfo.get(0).getTbm_G() * 1000);
                    queryInfo.get(0).setZero_LT_X(queryInfo.get(0).getZero_LT_X()*1000);
                    queryInfo.get(0).setZero_LT_Y(queryInfo.get(0).getZero_LT_Y()*1000);
                    queryInfo.get(0).setZero_LT_Z(queryInfo.get(0).getZero_LT_Z()*1000);

                    returnInfo.add(queryInfo);

                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return returnInfo;
    }
}
