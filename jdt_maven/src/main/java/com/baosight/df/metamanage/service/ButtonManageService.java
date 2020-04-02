package com.baosight.df.metamanage.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.JsonUtils;
import com.baosight.df.metamanage.entity.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class ButtonManageService extends BaseService {
    @Autowired
    private HttpServletRequest request;

    /**
     * 查询
     *
     * @param paramInfo
     * @return 输出JSONObject
     */

    public JSONObject query(JSONObject paramInfo) {
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, "com.baosight.df.metamanage.dao.ButtonManageDao.query");
        paramInfo.put(BaseService.COUNT_SQL, "com.baosight.df.metamanage.dao.ButtonManageDao.count");
        paramInfo.put(BaseService.DAO_ENTITY, "com.baosight.df.metamanage.entity.ButtonManage");
        paramInfo.put("fieldName", "buttonId");
        paramInfo.put("ascDesc", "asc");

        JSONObject returnInfo = super.query(paramInfo);
        return returnInfo;
    }

    /**
     * 插入
     *
     * @param paramInfo
     * @return 输出JSONObject
     */

    public JSONObject insert(JSONObject paramInfo) {
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        JSONObject inquStatus = new JSONObject();
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.INSERT_SQL, "com.baosight.df.metamanage.dao.ButtonManageDao.insertSelective");
        paramInfo.put(BaseService.RESULT_BLOCK, "detail");
        paramInfo.put(BaseService.DAO_ENTITY,
                "com.baosight.df.metamanage.entity.ButtonManage");
        JSONObject returnInfo = super.insert(paramInfo);
        return returnInfo;
    }

    /**
     * 删除
     *
     * @param paramInfo
     * @return 输出JSONObject
     */

    public JSONObject delete(JSONObject paramInfo) {
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.DELETE_SQL, "com.baosight.df.metamanage.dao.ButtonManageDao.delete");
        paramInfo.put(BaseService.RESULT_BLOCK, "result");
        paramInfo.put(BaseService.DAO_ENTITY, "com.baosight.df.metamanage.entity.ButtonManage");
        JSONObject returnInfo = super.delete(paramInfo);
        return returnInfo;
    }

    /**
     * 更新
     *
     * @param paramInfo
     * @return 输出JSONObject
     */

    public JSONObject update(JSONObject paramInfo) {
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.UPDATE_SQL, "com.baosight.df.metamanage.dao.ButtonManageDao.updateByPrimaryKeySelective");
        paramInfo.put(BaseService.RESULT_BLOCK, "detail");
        paramInfo.put(BaseService.DAO_ENTITY, "com.baosight.df.metamanage.entity.ButtonManage");
        JSONObject returnInfo = super.update(paramInfo);
        return returnInfo;
    }

    /**
     * 查询单条记录
     *
     * @param paramInfo
     * @return 输出JSONObject
     */
    public JSONObject queryOne(JSONObject paramInfo) {
        JSONObject returnInfo = new JSONObject();
        HashMap queryMap = JsonUtils.toHashMap(paramInfo);
        PageInfo page = new PageInfo();
        page.fromMap(queryMap);
        page.setTenant((String) request.getSession().getAttribute(Constants.SESSION_TENANT_KEY));
        List pageInfoLst = sqlSessionTemplate.selectList("PageManage.query", page);
        if (pageInfoLst.size() > 0) {
            JSONObject pageInfo = (JSONObject) pageInfoLst.get(0);
            returnInfo.put("detail", pageInfo);
        }
        return returnInfo;
    }

    public JSONArray querySubTree(JSONObject paramInfo) {
        JSONArray menuLstarray = new JSONArray();
        if (paramInfo.get("parentCode").equals("#")) {
            JSONObject page = new JSONObject();
            page.put("id", "0-#");
            page.put("parent", "#");
            page.put("children", true);
            page.put("text", "页面列表");
            menuLstarray.add(page);
//        } else if (!paramInfo.get("parentCode").equals("0-#")) {
//            return menuLstarray;
        } else {
            paramInfo.put("tenant", request.getSession().getAttribute(Constants.SESSION_TENANT_KEY));
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPageType("localLink");
            pageInfo.setFieldName("pageId");
            pageInfo.setAscDesc("asc");
            pageInfo.setTenant((String) request.getSession().getAttribute(Constants.SESSION_TENANT_KEY));
            List<JSONObject> pageInfoLst = sqlSessionTemplate.selectList("PageManage.query", pageInfo);
            if (pageInfoLst != null && pageInfoLst.size() > 0) {
                for (int j = 0; j < pageInfoLst.size(); j++) {
                    JSONObject page = pageInfoLst.get(j);
                    page.put("id", page.getInteger("pageId") + "-" + page.getString("pageEname"));
                    page.put("text", String.format("%s(%s)", page.getString("pageCname"), page.getString("pageEname")));
                    page.put("parent", paramInfo.get("parentCode"));
                    page.put("children", false);
//                    page.put("children", true);
                    menuLstarray.add(page);
                }
            }
        }
        return menuLstarray;
    }
}
