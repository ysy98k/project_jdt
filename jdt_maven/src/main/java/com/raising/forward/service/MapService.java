package com.raising.forward.service;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MapService {


    /**
     * 获得地图查询。三个DIV信息（厂家占比，连接数，项目进度）
     * @param projects
     * @param lineTree
     * @param ccsId
     * @return
     */
    public JSONObject getMapDivInfo(List<JSONObject> projects,List<JSONObject> lineTree,String ccsId){
        JSONObject returnInfo = new JSONObject();

        if(projects== null){
            return returnInfo;
        }
        //厂家占比变量
        Map<String,JSONObject> factoryMap = new HashMap<>();
        List<String> factoryMarkList = new ArrayList<>();
        //在线数量变量
        Map<String,JSONObject> onLineMap = new HashMap<>();
        List<String> onLineMarkList = new ArrayList<>();
        //进度变量
        List<JSONObject> progressList = new ArrayList<>();
        //获得原始数据
        for(int i=0;i<projects.size();i++){
            JSONObject projectTemp = projects.get(i);
            if(!StringUtils.isNullOrEmpty(ccsId) && projectTemp.getString("cityCCSId").indexOf(ccsId) < 0){
                //如果ccsId不为空，且当前项目的ccsId。无法匹配参数ccsId。
                // 即，当前项目不在 ccsId指定的省或市或线路。则跳过。
                continue;
            }
            //获得盾构机厂家占比
            opratorFactory(projectTemp,factoryMarkList,factoryMap);
            //获得在线数量
            int length = ccsId == null ? 1 : ccsId.split("\\.").length;
            getOnlineQuantity(projectTemp,onLineMarkList,onLineMap,length);
            //获得掘进进度
            getProjectProgress(projectTemp,progressList);
        }
        //对数据做处理
        List<JSONObject> onLineList = new ArrayList<> (onLineMap.values());
        //实现Comparator进行排序。以tbmNum倒序排序。
        Collections.sort(onLineList,new Comparator<JSONObject>(){
            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                return (o2.getInteger("tbmNum") - o1.getInteger("tbmNum"));
            }
        });
        //实现Comparator进行排序。以rate倒序排序。
        Collections.sort(progressList,new Comparator<JSONObject>(){
            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                return    o2.getString("rate").compareToIgnoreCase( o1.getString("rate") ) ;
            }
        });
        onLineList = onLineList.size() < 8 ? onLineList : onLineList.subList(0,8);
        progressList = progressList.size() < 8 ? progressList : progressList.subList(0,8);
        //将ccsId替换成中文地名或线路名
        addCNName(onLineList,lineTree,ccsId);

        returnInfo.put("status", Constants.EXECUTE_SUCCESS);
        returnInfo.put("factoryList", new ArrayList<> (factoryMap.values()));
        returnInfo.put("onLineList", onLineList);
        returnInfo.put("progressList", progressList);

        return returnInfo;
    }

    /**
     * 获得盾构机厂家占比
     * @param projectTemp   项目
     * @param factoryMarkList  盾构机厂家标记集合
     * @param map   值map
     */
    private void opratorFactory(JSONObject projectTemp,List<String> factoryMarkList,Map<String,JSONObject> map){
        String factory = projectTemp.getString("factory");
        if(factoryMarkList.contains(factory)){
            JSONObject temJson = map.get(factory);
            temJson.put("value",temJson.getIntValue("value")+1);
        }else{
            JSONObject tempJson = new JSONObject();
            tempJson.put("name",factory);
            tempJson.put("value",1);
            map.put(factory,tempJson);
            factoryMarkList.add(factory);
        }
    }

    /**
     * 获得在线数量
     * onLineMap保存值集合。以ccsId作key 。 json做值格式
     * @param projectTemp
     * @param onLineMarkList
     * @param onLineMap
     * @param length
     */
    private void getOnlineQuantity(JSONObject projectTemp,List<String> onLineMarkList,Map<String,JSONObject> onLineMap,int length){
        if(!"prostatus.building".equals(projectTemp.getString("status"))){//如果不是在建项目，则不做统计。
            return;
        }
        String cityCCSId = projectTemp.getString("cityCCSId");
        String markStr = null;
        if(length == 1){//显示省
            markStr = cityCCSId.split("\\.")[0];
        }else if(length == 2){//显示市
            markStr = cityCCSId.substring(0,cityCCSId.lastIndexOf("\\.")+1);
        }else {//显示线路
            markStr = cityCCSId;
        }

        //如果不包含该记录所在省的信息。
        if(!onLineMarkList.contains(markStr)){
            JSONObject data = new JSONObject();
            data.put("name",markStr);
            data.put("tbmNum",1);
            if("已连接".equals(projectTemp.getString("communicationState"))){
                data.put("connectNum",1);
            }else{
                data.put("connectNum",0);
            }
            onLineMap.put(markStr,data);
            onLineMarkList.add(markStr);
            return;
        }
        JSONObject row =  onLineMap.get(markStr);
        row.put("tbmNum",row.getIntValue("tbmNum")+1);
        if("已连接".equals(projectTemp.getString("communicationState"))){
            row.put("connectNum",row.getIntValue("connectNum")+1);
        }
    }

    /**
     * 获得掘进进度，排除非在建项目
     * 对每个在建项目，计算其进度，
     * @param projectTemp
     * @param progressList
     */
    private void getProjectProgress(JSONObject projectTemp,List<JSONObject> progressList){
        if(!"prostatus.building".equals(projectTemp.getString("status"))){//如果不是在建项目，则不做统计。
            return;
        }
        String projectName = projectTemp.getString("projectName");
        Integer ringTotal = projectTemp.getInteger("ringTotal");
        String ringNumStr =  projectTemp.getString("ringNum");
        Integer ringNum =  StringUtils.isNumeric(ringNumStr) == true ? Integer.parseInt(ringNumStr) : 0;

        //如果数据为特殊值不可以计算
        JSONObject data = new JSONObject();
        if(ringTotal == null || ringTotal == 0 || ringNum == null ){
            data.put("name",projectName);
            data.put("rate",0);
        }else{
            long rate =Math.round(ringNum *100.0 /ringTotal);
            data.put("name",projectName);
            data.put("rate",(int)rate+"%");
        }
        progressList.add(data);
    }

    private void addCNName(List<JSONObject> dataList,List<JSONObject> lineTree,String ccsId){
        for(int i=0;i<dataList.size();i++){
            JSONObject temp = dataList.get(i);
            String onLineStr = temp.getString("name");
            for(int k=0;k<lineTree.size();k++){
                JSONObject lineTreeTemp = lineTree.get(k);
                if(!StringUtils.isNullOrEmpty(ccsId) && lineTreeTemp.getString("fdLevelCode").indexOf(ccsId) < 0){
                    continue;
                }
                if(lineTreeTemp.getString("fdLevelCode").equals(onLineStr)){
                    temp.put("cnName",lineTreeTemp.getString("fdItemName"));
                    break;
                }
            }
            //如果是在线数，则计算在线率
            int connectNum = temp.getIntValue("connectNum");
            int tbmNum = temp.getIntValue("tbmNum");
            String rate = Math.round(connectNum*100.0/tbmNum) + "%";
            temp.put("rate",rate);
        }
    }


}
