package com.raising.forward.service.d;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.JsonUtils;
import com.baosight.common.utils.StringUtils;
import com.baosight.df.metamanage.entity.PageInfo;
import com.common.NewBaseService;
import com.raising.forward.entity.DesignLine;
import com.raising.forward.mapper.DesignLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.mybatis.spring.SqlSessionTemplate;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class DesignLineService extends BaseService {

    @Value("${designer.delivery_pkg}")
    private String delivery_pkg;


    @Autowired
    protected SqlSessionTemplate sqlSessionTemplate;

    @Autowired(required = false)
    private DesignLineMapper designLineMapper;


    /**
     * 查询     *
     * @param
     * @return 输出JSONObject
     */
    public JSONObject query(JSONObject paramInfo) {
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        String cookieId=inquStatus.getString("cookieId");
        if(StringUtils.isNullOrEmpty(cookieId)){
            return null;
        }
        int cid=Integer.parseInt(cookieId);
        inquStatus.put("cookieId",cid);
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, "DesignLine.query");
        paramInfo.put(BaseService.COUNT_SQL, "DesignLine.count");
        paramInfo.put(BaseService.DAO_ENTITY,
                "com.raising.forward.entity.DesignLine");
        JSONObject returnInfo = super.query(paramInfo);
        JSONArray rows = returnInfo.getJSONArray("rows");
        if(rows == null || rows.size() < 1){
            return returnInfo;
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        for(int i = 0;i<rows.size();i++){
            JSONObject object = rows.getJSONObject(i);
            object.put("x",decimalFormat.format(object.getDoubleValue("x") ));
            object.put("y",decimalFormat.format(object.getDoubleValue("y") ));
            object.put("z",decimalFormat.format(object.getDoubleValue("z") ));

        }
        return returnInfo;
    }

    public JSONArray queryAllById(Integer projectId,String tenant){
        String sql = "DesignLine.query";
        JSONObject paramJson = new JSONObject();
        paramJson.put("cookieId",projectId);
        List<JSONObject> objects = sqlSessionTemplate.selectList(sql, paramJson);
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        for(int i = 0;i<objects.size();i++){
            JSONObject  temp = objects.get(i);
            temp.put("x",decimalFormat.format(temp.getDoubleValue("x") ));
            temp.put("y",decimalFormat.format(temp.getDoubleValue("y") ));
            temp.put("z",decimalFormat.format(temp.getDoubleValue("z") ));
        }
        JSONArray array = JSONArray.parseArray(JSONObject.toJSONString(objects));
        return array;
    }


    public void insert(DesignLine designLine,String tenant) {
        designLineMapper.insert(designLine,tenant);
    }

    public Integer validate(Integer cookieId,String tenant) {
        return designLineMapper.validate(cookieId,tenant);
    }

    public void delete(Integer cookieId,String tenant) {
        designLineMapper.delete(cookieId,tenant);
    }


    public List<JSONObject> getDataByProjectIds(JSONObject param){
        String sql = "DesignLine.getAllData";
        List<JSONObject> objects = sqlSessionTemplate.selectList(sql, param);
        if(objects == null){
            objects = new ArrayList<JSONObject>();
        }
        return objects;
    }

    public Integer getMaxId(JSONObject param){
        String sql = "DesignLine.getMaxId";
        Integer id = sqlSessionTemplate.selectOne(sql,param);
        id = id == null ? 0 : id;
        return id;
    }

}



