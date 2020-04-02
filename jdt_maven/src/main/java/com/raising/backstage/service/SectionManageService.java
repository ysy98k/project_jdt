package com.raising.backstage.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.entity.BaseEntity;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.JsonUtils;
import com.baosight.common.utils.StringUtils;
import com.baosight.df.metamanage.entity.PageInfo;
import com.raising.backstage.entity.SectionManage;
import com.util.MultipleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.baosight.common.utils.DateUtils.String2Date;

@Service
@Transactional
public class SectionManageService extends BaseService {
    private static final Logger logger = LoggerFactory
            .getLogger(SectionManageService.class);


    @Value("${designer.delivery_pkg}")
    private String delivery_pkg;

    @Autowired
    private HttpServletRequest request;

    /**
     * 查询
     *
     * @param
     * @return 输出JSONObject
     */

    public JSONObject query(JSONObject paramInfo) {
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, "SectionManage.query");
        paramInfo.put(BaseService.COUNT_SQL, "SectionManage.count");
        paramInfo.put(BaseService.DAO_ENTITY,
                "com.raising.backstage.entity.SectionManage");
        JSONObject returnInfo = super.query(paramInfo);
        return returnInfo;
    }

    public JSONObject  insert(JSONObject paramInfo){
        String entity = "com.raising.backstage.entity.SectionManage";
        paramInfo.put(BaseService.RESULT_BLOCK,"detail");
        paramInfo.put(BaseService.INSERT_SQL, "SectionManage.insert");
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        JSONObject returnInfo = super.insert(paramInfo);
        return returnInfo;

    }

    /**
     * 删除
     *
     * @param
     * @return 输出JSONObject
     */
    public JSONObject delete(JSONObject paramInfo) {
//        String sectionManageSql = "SectionManage.delete";
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.DELETE_SQL, "SectionManage.delete");
        paramInfo.put(BaseService.RESULT_BLOCK, "result");
        paramInfo.put(BaseService.DAO_ENTITY,
                "com.raising.backstage.entity.SectionManage");
        JSONObject returnInfo = super.delete(paramInfo);

        return returnInfo;
    }




    /**
     * 更新     *
     * @param
     * @return 输出JSONObject
     */
    public JSONObject update(JSONObject paramInfo) {
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.UPDATE_SQL, "SectionManage.update");
        paramInfo.put(BaseService.RESULT_BLOCK, "detail");
        paramInfo.put(BaseService.DAO_ENTITY,
                "com.raising.backstage.entity.SectionManage");
        JSONObject returnInfo = super.update(paramInfo);
        return returnInfo;
    }

    /**
     * 查询单条记录
     *
     * @param
     * @return 输出JSONObject
     */
    public JSONObject queryOne(JSONObject paramInfo) {
        JSONObject returnInfo = new JSONObject();
        HashMap queryMap = JsonUtils.toHashMap(paramInfo);
        PageInfo page = new PageInfo();
        page.fromMap(queryMap);
        page.setTenant((String) request.getSession().getAttribute(
                Constants.SESSION_TENANT_KEY));
        List pageInfoLst = sqlSessionTemplate.selectList("SectionManage.query",page);
        if (pageInfoLst.size() > 0) {
            JSONObject pageInfo = (JSONObject) pageInfoLst.get(0);
            returnInfo.put("detail", pageInfo);
        }
        return returnInfo;
    }

    public List<JSONObject> initSelect(){
        String sql = "SectionManage.query";
        JSONObject paramJson = new JSONObject();
        paramJson.put("tenant",request.getSession().getAttribute("tenant"));
        List<JSONObject> objects = sqlSessionTemplate.selectList(sql, paramJson);
        return objects;
    }

    /**
     * 根据项目Id，获取所有数据
     * @param paramJson
     * @return
     */
    public List<JSONObject> getDataByProjectIds(JSONObject paramJson){
        String sql = "SectionManage.getAllData";
        List<JSONObject> data = sqlSessionTemplate.selectList(sql, paramJson);
        List<Integer> sectionId = new ArrayList<>();
        for(int i=data.size()-1;i>=0;i--){
            JSONObject section = data.get(i);
            Integer id = section.getInteger("id");
            if(sectionId.contains(id)){
                data.remove(i);
                continue;
            }
            sectionId.add(id);
        }
        return data;
    }

    /**
     * 根据项目id，获取最大Id
     * @param paramJson
     * @return
     */
    public Integer getMaxId(JSONObject paramJson){
        String sql = "SectionManage.getMaxId";
        Integer max = sqlSessionTemplate.selectOne(sql, paramJson);
        return max;
    }




}
