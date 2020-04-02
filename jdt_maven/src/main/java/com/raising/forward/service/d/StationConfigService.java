package com.raising.forward.service.d;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.common.NewBaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.raising.forward.mapper.StationConfigMapper;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ysy
 * @date 2018/6/1 13:26
 * @description
 */
@Service
public class StationConfigService extends NewBaseService {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private StationConfigMapper stationConfigMapper;



    /**
     * 查询分页信息。
     * @param paramJson
     * @return
     */
    public JSONObject query(JSONObject paramJson) throws ParseException {
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject returnInfo = new JSONObject();
        JSONArray retunArray = new JSONArray();
        //封装查询参数
        String tenant = this.request.getSession().getAttribute("tenant").toString();
        int SID = 0;
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals("selected_id")) {
                SID = Integer.parseInt(c.getValue());
            }
        }
        if(SID < 1){
            return null;
        }
        paramJson.put("projectId",SID);
        paramJson.put("tenant",tenant);
        //设置分页数据
        Integer curPage = (Integer)paramJson.get("curPage");
        Integer curRowNum = (Integer)paramJson.get("curRowNum");
        if(curPage == null || curRowNum == null){
            curRowNum = 1000;
            curPage = 1;
        }
        //分页并封装返回结果
        String orderBy = "\"UpdateTime\"  desc";
        PageHelper.startPage(curPage, curRowNum,orderBy);
        List<JSONObject> result = stationConfigMapper.query(paramJson);
        PageInfo<JSONObject> page = new PageInfo<JSONObject>(result);

        for (JSONObject temp: result ) {
            temp.put("pointX",decimalFormat.format(temp.getFloatValue("pointX")) );
            temp.put("pointY",decimalFormat.format(temp.getFloatValue("pointY")) );
            temp.put("pointZ",decimalFormat.format(temp.getFloatValue("pointZ")) );
            temp.put("updateTime",dateFormat.format(dateFormat.parse(temp.getString("updateTime"))));
            retunArray.add(temp);
        }
        returnInfo.put("total",page.getPages());
        returnInfo.put("page",page.getPageNum());
        returnInfo.put("records",page.getPageSize());
        returnInfo.put("rows",retunArray);
        return returnInfo;
    }


    public List<JSONObject> getDataByProjectIds(JSONObject paramJson){
        String sql = "com.raising.forward.mapper.StationConfigMapper.getAllData";
        List<JSONObject> objects = sqlSessionTemplate.selectList(sql, paramJson);
        if(objects == null){
            objects = new ArrayList<JSONObject>();
        }
        return objects;
    }

    public Integer getMaxId(JSONObject paramJson){
        String sql = "com.raising.forward.mapper.StationConfigMapper.getMaxId";
        Integer id = sqlSessionTemplate.selectOne(sql,paramJson);
        return id;
    }
}
