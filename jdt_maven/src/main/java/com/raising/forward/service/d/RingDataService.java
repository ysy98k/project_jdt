package com.raising.forward.service.d;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.entity.GridData;
import com.common.NewBaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
@Service
public class RingDataService extends NewBaseService {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    @Autowired
    private HttpServletRequest request;


    public JSONObject queryRingData(JSONObject jsonObject){

        JSONObject inqu_status = jsonObject.getJSONObject("inqu_status");
        String cookieId=inqu_status.getString("cookieId");
        int projectId=Integer.parseInt(cookieId);
        if(projectId < 1){
            return null;
        }
        JSONObject queryJson = new JSONObject();
        queryJson.put("projectId",projectId);
        queryJson.put("tenant",request.getSession().getAttribute("tenant"));
        Integer curPage = (Integer)jsonObject.get("curPage");
        Integer curRowNum = (Integer)jsonObject.get("curRowNum");
        PageInfo pageResult;
        try {
            if (curPage == null || curRowNum == null) {
                curRowNum = Integer.valueOf(1000);
                curPage = Integer.valueOf(1);
            }
            PageHelper.startPage(curPage.intValue(), curRowNum.intValue());
            String querySql = "com.raising.forward.mapper.ringData.queryRingData";
            List<JSONObject> jsonObjects = this.sqlSessionTemplate.selectList(querySql,queryJson,new RowBounds((curPage.intValue() - 1) * curRowNum.intValue(), curRowNum.intValue()));
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(jsonObjects != null && jsonObjects.size() != 0){
                for(int i = 0;i<jsonObjects.size();i++){

                    Timestamp date = jsonObjects.get(i).getTimestamp("RD_CreatTime");
                    String RD_CreatTime = sdf.format(date);
                    jsonObjects.get(i).put("RD_CreatTime",RD_CreatTime);
                }
            }

            pageResult = new PageInfo(jsonObjects);
            pageResult.setList(jsonObjects);
        }catch (Exception var16) {
            var16.printStackTrace();
            jsonObject.put("status", "-1");
            jsonObject.put("returnMsg", "查询出错，请检查SQL语句！");
            return jsonObject;
        }
        Object generalArr = new ArrayList();
        String countSql = "com.raising.forward.mapper.ringData.count";
        generalArr = this.sqlSessionTemplate.selectList(countSql, queryJson);
        int generalSize = 0;
        if (generalArr != null && ((List)generalArr).size() > 0) {
            generalSize = ((Integer)((List)generalArr).get(0)).intValue();
        }

        GridData outGridSource = new GridData(generalSize, pageResult.getList());
        outGridSource.fillGridAttribute(jsonObject);
        JSONObject outJSONObj = JSONObject.parseObject(JSON.toJSONString(outGridSource));
        outJSONObj.put("status", "0");
        outJSONObj.put("returnMsg", "查询成功！本次返回" + pageResult.getList().size() + "条记录，总共" + generalSize + "条记录！");
        return outJSONObj;
    }



}




