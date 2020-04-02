package com.raising.forward.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.BaseController;
import com.raising.forward.service.CodeItemService;
import com.raising.forward.service.TotalInfoService;
import com.util.CSVUtils;
import com.util.MultipleDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 信息总览Controller
 */
@Controller("totalInfoController")
@RequestMapping("/raising/totalInfo")
public class TotalInfoController extends BaseController {

    @Autowired
    private CodeItemService codeItemService;

    @Autowired
    private TotalInfoService totalInfoService;

    /**
     * 得到城市信息
     * @param
     * @return
     */
    @RequestMapping(value = "getTotalInfos.do",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getTotalInfos(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        JSONObject returnInfo = null;
        String groupNames = request.getSession().getAttribute("groupNames").toString();
        //String token = request.getSession().getAttribute("token").toString();
        List<String> collectionNames = resourceService.getCollectionNames(groupNames);
        returnInfo = totalInfoService.getTotalInfos(collectionNames);
        List<JSONObject> dataList = (List<JSONObject>) returnInfo.get("dataList");
        if(dataList == null || dataList.size() < 1){
            return new JSONObject();
        }
        List<String> ccsIdList = (List<String>)returnInfo.get("ccsIdList");
        returnInfo.remove("ccsIdList");
        JSONObject cityRows = new JSONObject();
        try {
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            //获得查询条件信息ccsIds
            cityRows = codeItemService.getLineCondition2(ccsIdList);

            JSONObject one = dataList.get(0);
            Map<String,String> tbmCCSId = codeItemService.getCodeNameFromSystemCode(one.getString("tbmCCSId"));
            //查询盾构机类型
            for(int i =0;i<dataList.size();i++){
                JSONObject temp = dataList.get(i);
                String tbmType = tbmCCSId.get(temp.getString("tbmCCSId"));
                temp.put("tbmType",tbmType);
            }
        } finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        JSONArray cityArray = cityRows.getJSONArray("cityArray");
        returnInfo.put("cityArray",cityArray);
        return returnInfo;
    }



}
