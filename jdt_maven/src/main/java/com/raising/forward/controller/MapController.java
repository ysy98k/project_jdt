package com.raising.forward.controller;

import com.alibaba.fastjson.JSONObject;
import com.common.BaseController;
import com.raising.forward.service.MapService;
import com.util.CSVUtils;
import com.util.MultipleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/raising/forward/map")
public class MapController extends BaseController {

    @Autowired
    private MapService mapService;

    @RequestMapping(value = "getLines.do",method = RequestMethod.POST)
    public JSONObject queryLine(String ajaxParam){
        String groupNames = request.getSession().getAttribute("groupNames").toString();
        List<String> collectionNames = resourceService.getCollectionNames(groupNames);
        JSONObject jsonObject = drawLineService.queryAllLine(collectionNames);
        List<JSONObject> dataList =  jsonObject.getObject("returnInfo", ArrayList.class);
        if(dataList == null || dataList.size() < 1){
            return jsonObject;
        }
        try {
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            for(int i = 0;i< dataList.size();i++){
                JSONObject temp =  dataList.get(i);
                String ccsId = temp.getString("ccsId");
                String cityCcsId = ccsId.substring(0, ccsId.lastIndexOf("."));
                String provinceCcsId = cityCcsId.substring(0, cityCcsId.lastIndexOf("."));
                String city = codeItemService.getCodeName(cityCcsId);
                String province = codeItemService.getCodeName(provinceCcsId);
                temp.put("province", province);
                temp.put("city", city);
            }
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return jsonObject;
    }

    @RequestMapping(value = "getDivInfo.do",method = RequestMethod.GET)
    public JSONObject getDivInfo(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject ajaxJson = JSONObject.parseObject(ajaxParam);
        String ccsId = ajaxJson.getString("ccsId");
        //先获取资源
        String groupNames = request.getSession().getAttribute("groupNames").toString();
        List<String> collectionNames = resourceService.getCollectionNames(groupNames);
        //根据资源，获取项目信息。
        JSONObject projectJson = totalInfoService.getTotalInfos(collectionNames);
        List<JSONObject> projects = projectJson.getObject("dataList",ArrayList.class);

        List<JSONObject> lineTree = new ArrayList<>();
        List<String> ccsIdsList = new ArrayList<>();
        for(int i=0;i<projects.size();i++){
            JSONObject projectTemp = projects.get(i);
            ccsIdsList.add(projectTemp.getString("cityCCSId"));
        }
        //获取所有线路列表集合。
        try {
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            lineTree = codeItemService.queryLineTree2(ccsIdsList);
        } finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        //获得
        returnInfo = mapService.getMapDivInfo(projects,lineTree,ccsId);

        return returnInfo;
    }
}
