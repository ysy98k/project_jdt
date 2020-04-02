package com.raising.forward.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.BaseController;
import com.util.CSVUtils;
import com.util.MultipleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表查询
 */
@Controller
@RequestMapping("/raising/forward/listQuery")
public class ListQueryController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ListQueryController.class);


    /**
     * 查询主页显示的数据信息
     * 查询条件信息
     * 查询列表信息
     * @return
     */
    @RequestMapping(value = "/getRows.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject queryMainPageInfo(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        //查询列表信息
        JSONArray detailsArray = null;
        String groupNames = request.getSession().getAttribute("groupNames").toString();
        List<String> collectionNames = resourceService.getCollectionNames(groupNames);
        detailsArray = listQueryService.getProjet(ajaxParam,collectionNames);
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
        returnInfo.put("detailsArray",detailsArray);
        returnInfo.put("codeName",codeName);
        return returnInfo;
    }

    /**
     * 根据前台查询条件，查询列表信息
     * @param ajaxParam
     * @return
     */
    @RequestMapping(value = "/getDetails.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getDetails(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONArray resultArray = null;
        String groupNames = request.getSession().getAttribute("groupNames").toString();
        List<String> collectionNames = resourceService.getCollectionNames(groupNames);
        resultArray = listQueryService.getProjet(ajaxParam,collectionNames);
        returnInfo.put("rows",resultArray);
        return returnInfo;
    }


}
