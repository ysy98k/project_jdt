package com.raising.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.dao.RestDao;
import com.raising.backstage.entity.SectionManage;
import com.raising.backstage.service.SectionManageService;
import com.raising.forward.entity.DesignLine;
import com.raising.forward.entity.ZeroConfig;
import com.raising.forward.mapper.ZeroConfigMapper;
import com.raising.forward.service.d.DesignLineService;
import com.raising.forward.service.d.ZeroConfigService;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;

/**
 * 租户创建时的回调函数所使用的rest服务
 *
 * @author yetianqi
 */
@Component
@Path("/raising")
public class RestTrans {

    private static final Logger logger = LoggerFactory
            .getLogger(RestTrans.class);

    @Context
    HttpServletRequest request;
    @Context
    HttpServletResponse response;

    @Autowired
    private RestDao dao;

    @Value("${postgres.host}")
    private String url;

    @Value("${jdt.database.instance}")
    private String instance;

    @Value("${postgres.user}")
    private String userName;
    @Value("${postgres.password}")
    private String password;

    @Value("${aas.host}")
    private String aasAddress;

    @Value("${aas.rest_service_name}")
    private String aasAppPath;

    @Value("${service.name}")
    private String serviceName;

    @Autowired
    protected SqlSessionTemplate sqlSessionTemplate;

    @GET
    @Path("/DesignLine")
    @Produces(MediaType.APPLICATION_JSON)
    public String UpdateDesignLine() {
        JSONObject generalObj = new JSONObject();//设置总的返回结果
        generalObj.put("status", "ok");
        return generalObj.toJSONString();
    }

    @Autowired
    private DesignLineService designLineService;




    @Autowired
    private SectionManageService sectionManageService;

    @POST
    @Path("/design")
    @Produces(MediaType.APPLICATION_JSON)


    public String transDesignLineData(JSONObject jsb) throws Exception {

        String collectionName = jsb.getString("name");
        String tenantName = "raising";
        request.getSession().getAttribute("tenant");

        JSONArray jsonArray = jsb.getJSONArray("data");
        SectionManage daoEntity = new SectionManage();

        daoEntity.setCollectorName(collectionName);
        daoEntity.setSectionName("");
        HashMap queryMap = new HashMap();
        queryMap.put("tenant", tenantName);
        queryMap.put("ascDesc", null);
        queryMap.put("filedName", null);
        daoEntity.fromMap(queryMap);

        request.getRemoteAddr();

        Integer curPage = 1;
        Integer curRowNum = 10;

        String querySql = "SectionManage.query";

        List<JSONObject> list = this.sqlSessionTemplate.selectList(querySql, daoEntity, new RowBounds((curPage.intValue() - 1) * curRowNum.intValue(), curRowNum.intValue()));

        if (list.size()==0)
        {
            JSONObject generalObj = new JSONObject();//设置总的返回结果

            generalObj.put("status", "采集器名称错误，请检查采集器名称");
            return generalObj.toJSONString();
        }
        String sectionId = list.get(0).getString("sectionId");
        Integer sid = Integer.parseInt(sectionId);
        if (designLineService.validate(sid,tenantName)!=0)
            designLineService.delete(sid,tenantName);

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String x = jsonObject.getString("x");
            float a = Float.parseFloat(x);
            String y = jsonObject.getString("y");
            float b = Float.parseFloat(y);
            String z = jsonObject.getString("z");
            float c = Float.parseFloat(z);

			/*String section_id = jsonObject.getString("section_id");
            Integer s_id = Integer.parseInt(section_id);*/
            String mileage = jsonObject.getString("designmileage");
            float mlg = Float.parseFloat(mileage);
            String amuzith = jsonObject.getString("amuzith");
            float az = Float.parseFloat(amuzith);
            String slope = jsonObject.getString("slope");
            float sp = Float.parseFloat(slope);
            String dist = jsonObject.getString("dist");
            float dt = Float.parseFloat(dist);
            String mapmileage = jsonObject.getString("mapmileage");
            float mm = Float.parseFloat(mapmileage);


            DesignLine designLine = new DesignLine();
            designLine.setX(a);
            designLine.setY(b);
            designLine.setZ(c);
			/*designLine.setCookieId(s_id);*/
            designLine.setDesignmileage(mlg);
            designLine.setAmuzith(az);
            designLine.setSlope(sp);
            designLine.setDist(dt);
            designLine.setMapmileage(mm);

            if (list != null) {
                designLine.setCookieId(sid);
                designLineService.insert(designLine,tenantName);
            }

        }

        JSONObject generalObj = new JSONObject();//设置总的返回结果

        generalObj.put("status", "ok");
        return generalObj.toJSONString();

    }


    @Autowired
    private ZeroConfigService zeroConfigService;

    @Autowired
    private ZeroConfigMapper zeroConfigMapper;


    @POST
    @Path("/zero")
    @Produces(MediaType.APPLICATION_JSON)


    public String transZeroConfigData(JSONObject jsb) throws Exception {

        String collectionName = jsb.getString("name");
        String tenantName = "raising";

        JSONArray jsonArray = jsb.getJSONArray("data");
        SectionManage daoEntity = new SectionManage();

        daoEntity.setCollectorName(collectionName);
        daoEntity.setSectionName("");
        HashMap queryMap = new HashMap();
        /*queryMap.put("tenant", "raising");*/
        queryMap.put("tenant",tenantName);
        queryMap.put("ascDesc", null);
        queryMap.put("filedName", null);
        daoEntity.fromMap(queryMap);

        Integer curPage = 1;
        Integer curRowNum = 10;

        String querySql = "SectionManage.query";

        List<JSONObject> list = this.sqlSessionTemplate.selectList(querySql, daoEntity, new RowBounds((curPage.intValue() - 1) * curRowNum.intValue(), curRowNum.intValue()));
        if (list.size()==0)
        {
            JSONObject generalObj = new JSONObject();//设置总的返回结果

            generalObj.put("status", "采集器名称错误，请检查采集器名称");
            return generalObj.toJSONString();
        }

        String sectionId = list.get(0).getString("sectionId");
        Integer sid = Integer.parseInt(sectionId);
      /*  if (!designLineService.validate(sid))
            designLineService.delete(sid);*/

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String TBM_A = jsonObject.getString("TBM_A");
            float a = Float.parseFloat(TBM_A);
            String TBM_B = jsonObject.getString("TBM_B");
            float b = Float.parseFloat(TBM_B);
            String TBM_C = jsonObject.getString("TBM_C");
            float c = Float.parseFloat(TBM_C);
            String TBM_D = jsonObject.getString("TBM_D");
            float d = Float.parseFloat(TBM_D);
            String TBM_E = jsonObject.getString("TBM_E");
            float e = Float.parseFloat(TBM_E);
            String TBM_F = jsonObject.getString("TBM_F");
            float f = Float.parseFloat(TBM_F);
            String TBM_G = jsonObject.getString("TBM_G");
            float g = Float.parseFloat(TBM_G);
            String Zero_LT_X = jsonObject.getString("Zero_LT_X");
            float h = Float.parseFloat(Zero_LT_X);
            String Zero_LT_Y = jsonObject.getString("Zero_LT_Y");
            float j = Float.parseFloat(Zero_LT_Y);
            String Zero_LT_Z = jsonObject.getString("Zero_LT_Z");
            float k = Float.parseFloat(Zero_LT_Z);
            String Zero_LT_RollAngle = jsonObject.getString("Zero_LT_RollAngle");
            float l = Float.parseFloat(Zero_LT_RollAngle);
            String Zero_LT_PitchAngle = jsonObject.getString("Zero_LT_PitchAngle");
            float m = Float.parseFloat(Zero_LT_PitchAngle);
            String Zero_LT_AzimuthAngle = jsonObject.getString("Zero_LT_AzimuthAngle");
            float n = Float.parseFloat(Zero_LT_AzimuthAngle);



            ZeroConfig zeroConfig = new ZeroConfig();
            zeroConfig.setTbm_A(a);
            zeroConfig.setTbm_B(b);
            zeroConfig.setTbm_C(c);
            zeroConfig.setTbm_D(d);
            zeroConfig.setTbm_E(e);
            zeroConfig.setTbm_F(f);
            zeroConfig.setTbm_G(g);
            zeroConfig.setZero_LT_X(h);
            zeroConfig.setZero_LT_Y(j);
            zeroConfig.setZero_LT_Z(k);
            zeroConfig.setZero_LT_RollAngle(l);
            zeroConfig.setZero_LT_PitchAngle(m);
            zeroConfig.setZero_LT_AzimuthAngle(n);
            zeroConfig.setSectionId(sid);

            if (list != null) {
                if (zeroConfigMapper.query(sid).size()==0)
                    zeroConfigService.insert(zeroConfig);
                else
                    zeroConfigService.update(zeroConfig);
            }
        }

        JSONObject generalObj = new JSONObject();//设置总的返回结果

        generalObj.put("status", "ok");
        return generalObj.toJSONString();

    }



}
