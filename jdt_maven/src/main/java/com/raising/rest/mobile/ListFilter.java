package com.raising.rest.mobile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;

import com.common.RequestHolder;
import com.raising.ccs.ResourceService;
import com.raising.forward.service.CodeItemService;
import com.raising.forward.service.ListQueryService;
import com.util.CSVUtils;
import com.util.MultipleDataSource;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 获取列表数据接口
 */
@Component
@Path("/raising")
public class ListFilter {
    @Autowired
    protected SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private CodeItemService codeItemService;

    @Autowired
    private ListQueryService listQueryService;

    @Autowired
    protected ResourceService resourceService;



    /**
     * 获取列表数据接口
     * @param paramJson
     * @return
     */
    @POST
    @Path("/filter")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject getRows(JSONObject paramJson){
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        //返回值
        JSONObject returnInfo = new JSONObject();
        JSONArray resultArray = new JSONArray();
        //获取权限控制下的资源
//        String token = paramJson.getString("token");
//        String group = paramJson.getString("group");
//        List<String> collectorNames = ResourcesUtil.getCollectorNameResouces(token, group);
        String mld="";
        JSONArray detailsArray = null;

        HttpServletRequest currentRequest = RequestHolder.getCurrentRequest();
        String groupNames  =  currentRequest.getSession().getAttribute("groupNames").toString();
        List<String> collectionNames = resourceService.getCollectionNames(groupNames);
        detailsArray = listQueryService.getProjet(mld,collectionNames);
        //获得查询条件信息ccsIds
        List<String> ccsIdsList = new ArrayList<>();
        for(Object obj : detailsArray){
            JSONObject tempObj = (JSONObject)obj;
            ccsIdsList.add(tempObj.getString("ccsId"));
        }
        //更换数据源，从aasccs库中的字典表中，查询ccsIds对应的值
        JSONObject codeName;
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            codeName = codeItemService.getLineCondition2(ccsIdsList);
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }

        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        returnInfo.put("listInfo",resultArray);
        return returnInfo;
    }


}
