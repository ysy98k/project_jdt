package com.baosight.df.metamanage.service;


import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.JsonUtils;
import com.baosight.df.example.entity.CompanyInfo;
import com.baosight.df.metamanage.entity.GlobalMessage;

@Service
public class GlobalMessageService extends BaseService {
    @Autowired
    private HttpServletRequest request;

    /**
     * 查询
     *
     * @param 输入JSONObject
     * @return 输出JSONObject
     */

    public JSONObject querybatch(JSONObject paramInfo) {
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        if (inquStatus == null) {
            inquStatus = new JSONObject();
        }
        inquStatus
                .put("tenant",
                        request.getSession().getAttribute(
                                Constants.SESSION_TENANT_KEY));
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, "GlobalMessage.querybatch");
        paramInfo.put(BaseService.COUNT_SQL, "GlobalMessage.countbatch");
        paramInfo.put(BaseService.DAO_ENTITY,
                "com.baosight.df.metamanage.entity.GlobalMessage");
        JSONObject returnInfo = super.query(paramInfo);
        return returnInfo;
    }

    public JSONObject querykey(JSONObject paramInfo) {
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        if (inquStatus == null) {
            inquStatus = new JSONObject();
        }
        inquStatus
                .put("tenant",
                        request.getSession().getAttribute(
                                Constants.SESSION_TENANT_KEY));
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, "GlobalMessage.querykey");
        paramInfo.put(BaseService.COUNT_SQL, "GlobalMessage.countkey");
        paramInfo.put(BaseService.DAO_ENTITY,
                "com.baosight.df.metamanage.entity.GlobalMessage");
        JSONObject returnInfo = super.query(paramInfo);
        return returnInfo;
    }

    public JSONObject query(JSONObject paramInfo) {
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        if (inquStatus == null) {
            inquStatus = new JSONObject();
        }
        inquStatus
                .put("tenant",
                        request.getSession().getAttribute(
                                Constants.SESSION_TENANT_KEY));
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, "GlobalMessage.query");
        paramInfo.put(BaseService.COUNT_SQL, "GlobalMessage.count");
        paramInfo.put(BaseService.DAO_ENTITY,
                "com.baosight.df.metamanage.entity.GlobalMessage");
        JSONObject returnInfo = super.query(paramInfo);
        return returnInfo;
    }

    /**
     * 插入
     *
     * @param 输入JSONObject
     * @return 输出JSONObject
     */

    public JSONObject insert(JSONObject paramInfo) {
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        if (inquStatus == null) {
            inquStatus = new JSONObject();
        }
        inquStatus
                .put("tenant",
                        request.getSession().getAttribute(
                                Constants.SESSION_TENANT_KEY));
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.INSERT_SQL, "GlobalMessage.insert");
        paramInfo.put(BaseService.RESULT_BLOCK, "detail");
        paramInfo.put(BaseService.DAO_ENTITY,
                "com.baosight.df.metamanage.entity.GlobalMessage");
        JSONObject returnInfo = super.insert(paramInfo);
        return returnInfo;
    }

    /**
     * 删除
     *
     * @param 输入JSONObject
     * @return 输出JSONObject
     */

    public JSONObject delete(JSONObject paramInfo) {
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        if (inquStatus == null) {
            inquStatus = new JSONObject();
        }
        inquStatus
                .put("tenant",
                        request.getSession().getAttribute(
                                Constants.SESSION_TENANT_KEY));
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.DELETE_SQL, "GlobalMessage.delete");
        paramInfo.put(BaseService.RESULT_BLOCK, "result");
        paramInfo.put(BaseService.DAO_ENTITY,
                "com.baosight.df.metamanage.entity.GlobalMessage");
        JSONObject returnInfo = super.delete(paramInfo);
        return returnInfo;
    }

    /**
     * 更新
     *
     * @param 输入JSONObject
     * @return 输出JSONObject
     */

    public JSONObject update(JSONObject paramInfo) {
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        if (inquStatus == null) {
            inquStatus = new JSONObject();
        }
        inquStatus
                .put("tenant",
                        request.getSession().getAttribute(
                                Constants.SESSION_TENANT_KEY));
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.UPDATE_SQL, "GlobalMessage.update");
        paramInfo.put(BaseService.RESULT_BLOCK, "detail");
        paramInfo.put(BaseService.DAO_ENTITY,
                "com.baosight.df.metamanage.entity.GlobalMessage");
        JSONObject returnInfo = super.update(paramInfo);
        return returnInfo;
    }

    /**
     * 查询单条记录
     *
     * @param 输入JSONObject
     * @return 输出JSONObject
     */
    public JSONObject queryOne(JSONObject paramInfo) {
        JSONObject returnInfo = new JSONObject();
        HashMap queryMap = JsonUtils.toHashMap(paramInfo);
        GlobalMessage global = new GlobalMessage();
        global.fromMap(queryMap);
        global.setTenant((String) request.getSession().getAttribute(
                Constants.SESSION_TENANT_KEY));
        List pageInfoLst = sqlSessionTemplate.selectList("GlobalMessage.query",
                global);
        if (pageInfoLst.size() > 0) {
            JSONObject globalMessage = (JSONObject) pageInfoLst.get(0);
            returnInfo.put("detail", globalMessage);
        }
        return returnInfo;
    }
}
