package com.raising.forward.service.group.progress;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupProgressService {

    /**
     * 获得周报数据
     * @param paramJson
     * @param projects
     * @return
     */
    public JSONObject getData(JSONObject paramJson,List<JSONObject> projects,List<JSONObject> ringData){
        JSONObject returnJson = new JSONObject();
        List<JSONObject> result = new ArrayList<>();


        //将ringData数据分类，存储在projectId 做key .的集合中
        Map<Integer,List<JSONObject>> projectMap = new HashMap<>();
        for(int i=0;i<ringData.size();i++){
            JSONObject temp = ringData.get(i);
            Integer projectIdTemp =  temp.getInteger("projectId");
            if( projectMap.containsKey(projectIdTemp)  ){
                List<JSONObject> projectData = projectMap.get(projectIdTemp);
                projectData.add(temp);
            }else{
                List<JSONObject> projectData = new ArrayList<>();
                projectData.add(temp);
                projectMap.put(projectIdTemp,projectData);
            }
        }
        Set<Integer> projectIds = projectMap.keySet();
        for(Integer projectId : projectIds ){
            JSONObject row = null;
            String projectName = "";
            Integer weekTotal = 0;
            Integer ringTotal = 0;
            double progress = 0.0;
            //得到projectName，ringTotal信息
            for(int i=0;i<projects.size();i++){
                JSONObject projectTemp = projects.get(i);
                if((int)projectId == projectTemp.getIntValue("projectId")){
                    projectName = projectTemp.getString("projectName");
                    ringTotal = projectTemp.getInteger("ringTotal");
                    break;
                }
            }
            List<JSONObject> projectData = projectMap.get(projectId);
            if("week".equals(paramJson.getString("type"))){
                row = getWeekRow(paramJson,projectData);
                row.put("projectName",projectName);
                row.put("ringTotal",ringTotal);
                row.put("progress", Math.round(row.getDoubleValue("endRingNums")*100/ringTotal)+"%");
            }else if("month".equals(paramJson.getString("type"))){
                row = getMonthRow(paramJson, projectData);
                row.put("projectName",projectName);
                row.put("ringTotal",ringTotal);
                row.put("progress", Math.round(row.getDoubleValue("endRingNums")*100/ringTotal)+"%");
            }
            result.add(row);

        }
        returnJson.put("status", Constants.EXECUTE_SUCCESS);
        returnJson.put("list",result);
        return returnJson;
    }

    private JSONObject getWeekRow(JSONObject paramJson,List<JSONObject> projectData){
        Long monday = paramJson.getLongValue("monday");
        Long tuesday = paramJson.getLongValue("tuesday");
        Long wednesday = paramJson.getLongValue("wednesday");
        Long thursday = paramJson.getLongValue("thursday");
        Long friday = paramJson.getLongValue("friday");
        Long saturday = paramJson.getLongValue("saturday");
        Long sunday = paramJson.getLongValue("sunday");

        Integer mondayNums = 0;
        Integer tuesdayNums = 0;
        Integer wednesdayNums = 0;
        Integer thursdayNums = 0;
        Integer fridayNums = 0;
        Integer saturdayNums = 0;
        Integer sundayNums = 0;
        Integer startRingNums = 0;
        Integer endRingNums = 0;
        Integer weekTotal = 0;

        JSONObject row = new JSONObject();
        for(int i=0;i<projectData.size();i++){
            JSONObject ring = projectData.get(i);
            Long dt = ring.getDate("dt").getTime();
            if(monday <= dt && dt < tuesday){
                for(int e=i;e<projectData.size();e++){
                    JSONObject ring2 = projectData.get(e);

                    Long dt2 = ring2.getDate("dt").getTime();
                    if(dt2 >= tuesday){
                        mondayNums =  projectData.get(e-1).getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                        i = e-1;
                        break;
                    }else if(e == (projectData.size()-1) ){
                        mondayNums = ring2.getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                        i = e;
                    }
                }

            }else if(tuesday <= dt && dt < wednesday){
                for(int e=i;e<projectData.size();e++){
                    JSONObject ring2 = projectData.get(e);

                    Long dt2 = ring2.getDate("dt").getTime();
                    if(dt2 >= wednesday){
                        tuesdayNums =  projectData.get(e-1).getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                        i = e-1;
                        break;
                    }else if(e == (projectData.size()-1) ){
                        tuesdayNums = ring2.getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                        i = e;
                    }
                }
            }else if(wednesday <= dt && dt < thursday){
                for(int e=i;e<projectData.size();e++){
                    JSONObject ring2 = projectData.get(e);

                    Long dt2 = ring2.getDate("dt").getTime();
                    if(dt2 >= thursday){
                        wednesdayNums =  projectData.get(e-1).getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                        i = e-1;
                        break;
                    }else if(e == (projectData.size()-1) ){
                        wednesdayNums = ring2.getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                        i = e;
                    }
                }
            }else if(thursday <= dt && dt < friday){
                for(int e=i;e<projectData.size();e++){
                    JSONObject ring2 = projectData.get(e);

                    Long dt2 = ring2.getDate("dt").getTime();
                    if(dt2 >= friday){
                        thursdayNums =  projectData.get(e-1).getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                        i = e-1;
                        break;
                    }else if(e == (projectData.size()-1) ){
                        thursdayNums = ring2.getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                        i = e;
                    }
                }
            }else if(friday <= dt && dt < saturday){
                for(int e=i;e<projectData.size();e++){
                    JSONObject ring2 = projectData.get(e);

                    Long dt2 = ring2.getDate("dt").getTime();
                    if(dt2 >= saturday){
                        fridayNums =  projectData.get(e-1).getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                        i = e-1;
                        break;
                    }else if(e == (projectData.size()-1) ){
                        fridayNums = ring2.getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                        i = e;
                    }
                }
            }else if(saturday <= dt && dt < sunday){
                for(int e=i;e<projectData.size();e++){
                    JSONObject ring2 = projectData.get(e);

                    Long dt2 = ring2.getDate("dt").getTime();
                    if(dt2 >= sunday){
                        saturdayNums =  projectData.get(e-1).getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                        i = e-1;
                        break;
                    }else if(e == (projectData.size()-1) ){
                        saturdayNums = ring2.getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                        i = e;
                    }
                }
            }else if(sunday <= dt && dt < (sunday+24*60*60*1000)){
                for(int e=i;e<projectData.size();e++){
                    JSONObject ring2 = projectData.get(e);

                    Long dt2 = ring2.getDate("dt").getTime();
                    if(dt2 >= (sunday+24*60*60*1000)){
                        sundayNums =  projectData.get(e-1).getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                        i = e-1;
                        break;
                    }else if(e == (projectData.size()-1) ){
                        sundayNums = ring2.getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                        i = e;
                    }
                }
            }
        }
        weekTotal = sundayNums+saturdayNums+fridayNums+thursdayNums+wednesdayNums+tuesdayNums+mondayNums;
        row.put("mondayNums",mondayNums);
        row.put("tuesdayNums",tuesdayNums);
        row.put("wednesdayNums",wednesdayNums);
        row.put("thursdayNums",thursdayNums);
        row.put("fridayNums",fridayNums);
        row.put("saturdayNums",saturdayNums);
        row.put("sundayNums",sundayNums);

        startRingNums = projectData.get(0).getInteger("MR_Ring_Num");
        endRingNums = projectData.get(projectData.size()-1).getInteger("MR_Ring_Num");
        row.put("endRingNums",endRingNums);
        row.put("weekTotal","("+startRingNums+" - "+endRingNums+") 累计" +weekTotal);
        return row;
    }

    private JSONObject getMonthRow(JSONObject paramJson,List<JSONObject> projectData){
        Long oneWeek = paramJson.getLongValue("oneWeekStart");
        Long twoWeek = paramJson.getLongValue("twoWeekStart");
        Long threeWeek = paramJson.getLongValue("threeWeekStart");
        Long fourWeek = paramJson.getLongValue("fourWeekStart");
        Long fiveWeek = paramJson.getLong("fiveWeekStart");
        Long endTime =  paramJson.getTimestamp("endTime").getTime();


        Integer oneNums = 0;
        Integer twoNums = 0;
        Integer threeNums = 0;
        Integer fourNums = 0;
        Integer fiveNums = 0;
        Integer startRingNums = 0;
        Integer endRingNums = 0;
        Integer monthTotal = 0;

        JSONObject row = new JSONObject();
        for(int i=0;i<projectData.size();i++){
            JSONObject ring = projectData.get(i);
            Long dt = ring.getDate("dt").getTime();
            if(oneWeek <= dt && dt < twoWeek){
                for(int e=i;e<projectData.size();e++){
                    JSONObject ring2 = projectData.get(e);
                    Long dt2 = ring2.getDate("dt").getTime();
                    if(dt2 >= twoWeek){
                        oneNums =  projectData.get(e-1).getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                        i = e-1;
                        break;
                    }else if(e == (projectData.size()-1) ){
                        oneNums = ring2.getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                        i = e;
                    }
                }

            }else if(twoWeek <= dt && dt < threeWeek){
                for(int e=i;e<projectData.size();e++){
                    JSONObject ring2 = projectData.get(e);

                    Long dt2 = ring2.getDate("dt").getTime();
                    if(dt2 >= threeWeek){
                        twoNums =  projectData.get(e-1).getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                        i = e-1;
                        break;
                    }else if(e == (projectData.size()-1) ){
                        twoNums = ring2.getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                        i = e;
                    }
                }
            }else if(threeWeek <= dt && dt < fourWeek){
                for(int e=i;e<projectData.size();e++){
                    JSONObject ring2 = projectData.get(e);

                    Long dt2 = ring2.getDate("dt").getTime();
                    if(dt2 >= fourWeek){
                        threeNums =  projectData.get(e-1).getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                        i = e-1;
                        break;
                    }else if(e == (projectData.size()-1) ){
                        threeNums = ring2.getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                        i = e;
                    }
                }
            }
            if(fiveWeek == null){
                if(fourWeek <= dt && dt < endTime){
                    for(int e=i;e<projectData.size();e++){
                        JSONObject ring2 = projectData.get(e);

                        Long dt2 = ring2.getDate("dt").getTime();
                        if(dt2 >= endTime){
                            fourNums =  projectData.get(e-1).getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                            i = e-1;
                            break;
                        }else if(e == (projectData.size()-1) ){
                            fourNums = ring2.getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                            i = e;
                        }
                    }
                }
            }else{
                if(fiveWeek <= dt && dt < endTime){
                    for(int e=i;e<projectData.size();e++){
                        JSONObject ring2 = projectData.get(e);

                        Long dt2 = ring2.getDate("dt").getTime();
                        if(dt2 >= endTime){
                            fiveNums =  projectData.get(e-1).getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                            i = e-1;
                            break;
                        }else if(e == (projectData.size()-1) ){
                            fiveNums = ring2.getInteger("MR_Ring_Num") - ring.getInteger("MR_Ring_Num");
                            i = e;
                        }
                    }
                }
            }
        }

        monthTotal = oneNums+twoNums+threeNums+fourNums+fiveNums;
        row.put("oneNums",oneNums);
        row.put("twoNums",twoNums);
        row.put("threeNums",threeNums);
        row.put("fourNums",fourNums);
        if(fiveWeek != null){
            row.put("fiveNums",fiveNums);
        }
        startRingNums = projectData.get(0).getInteger("MR_Ring_Num");
        endRingNums = projectData.get(projectData.size()-1).getInteger("MR_Ring_Num");
        row.put("endRingNums",endRingNums);
        row.put("monthTotal","("+startRingNums+" - "+endRingNums+") 累计" +monthTotal);

        return row;
    }



}
