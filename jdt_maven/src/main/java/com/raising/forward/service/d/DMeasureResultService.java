package com.raising.forward.service.d;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.entity.GridData;
import com.common.NewBaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;

@Service
public class DMeasureResultService extends NewBaseService {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    @Autowired
    private HttpServletRequest request;

    /**
     * 导向管理，测量结果表格
     * @param jsonObject
     * @return
     */
    public JSONObject queryMeasureResult(JSONObject jsonObject){

        JSONObject inqu_status = jsonObject.getJSONObject("inqu_status");
        String cookieId=inqu_status.getString("cookieId");
        int projectId=Integer.parseInt(cookieId);
        if(projectId < 1){
            return null;
        }
        JSONObject queryJson = new JSONObject();
        queryJson.put("projectId",projectId);
        Integer curPage = (Integer)jsonObject.get("curPage");
        Integer curRowNum = (Integer)jsonObject.get("curRowNum");
        PageInfo pageResult;
        try {
            if (curPage == null || curRowNum == null) {
                curRowNum = Integer.valueOf(1000);
                curPage = Integer.valueOf(1);
            }
            PageHelper.startPage(curPage.intValue(), curRowNum.intValue());
            String querySql = "com.raising.forward.mapper.measureResult.queryMeasureResult";
            List<JSONObject> jsonObjects = this.sqlSessionTemplate.selectList(querySql,queryJson,new RowBounds((curPage.intValue() - 1) * curRowNum.intValue(), curRowNum.intValue()));
            pageResult = new PageInfo(jsonObjects);
            pageResult.setList(jsonObjects);
        }catch (Exception var16) {
                var16.printStackTrace();
                jsonObject.put("status", "-1");
                jsonObject.put("returnMsg", "查询出错，请检查SQL语句！");
                return jsonObject;
            }
        Object generalArr = new ArrayList();
        String countSql = "com.raising.forward.mapper.measureResult.count";
        generalArr = this.sqlSessionTemplate.selectList(countSql, queryJson);
        int generalSize = 0;
        if (generalArr != null && ((List)generalArr).size() > 0) {
            generalSize = ((Integer)((List)generalArr).get(0)).intValue();
        }

        GridData outGridSource = new GridData(generalSize, pageResult.getList());
        outGridSource.fillGridAttribute(jsonObject);
        JSONObject outJSONObj = JSONObject.parseObject(JSON.toJSONString(outGridSource));
        outJSONObj.put("status", "0");
        outJSONObj.put("returnMsg", "查询成功！本次返回" + pageResult.getList().size() + "条记录，总共" + generalSize + "条记录！");
        return outJSONObj;
    }

    /**
     * 施工管理，报表导出，管片姿态功能
     */
    public JSONObject getReportSegementData(JSONObject paramJson){
        JSONObject returnInfo = new JSONObject();
        DecimalFormat decimalFormat = new DecimalFormat("#.###");

        String sql = "com.raising.forward.mapper.measureResult.getReportSegementData";
        List<JSONObject> data = sqlSessionTemplate.selectList(sql, paramJson);
        List<Integer> ringsList = new ArrayList<>();
        List<String> verticalLineData = new ArrayList<>();
        List<String> horizontalLineData = new ArrayList<>();
        List<JSONObject> pieData = new ArrayList<>();

        JSONObject green = new JSONObject();//0-30
        JSONObject blue = new JSONObject();//30-50
        JSONObject yellow = new JSONObject();//50-70
        JSONObject orange = new JSONObject();//70-100
        JSONObject red = new JSONObject();//100-
        green.put("name","偏差0<= |x| < 30");
        blue.put("name","偏差30<= |x| < 50");
        yellow.put("name","偏差50<= |x| < 70");
        orange.put("name","偏差70<= |x| < 100");
        red.put("name","偏差100<= |x|");
        pieData.add(green);
        pieData.add(blue);
        pieData.add(yellow);
        pieData.add(orange);
        pieData.add(red);

        if(data == null || data.size() <1){
            green.put("value",100);
            blue.put("value",0);
            yellow.put("value",0);
            orange.put("value",0);
            red.put("value",0);

            returnInfo.put("dataList",new ArrayList<>());
            returnInfo.put("ringsList",ringsList);
            returnInfo.put("verticalLine",verticalLineData);
            returnInfo.put("horizontalLine",horizontalLineData);
            returnInfo.put("pieData",pieData);
            return returnInfo;
        }


        Integer greenNum = 0;
        Integer blueNum = 0;
        Integer yellowNum = 0;
        Integer orangeNum = 0;
        Integer redNum = 0;

        for(int i=0;i<data.size();i++){
            JSONObject temp = data.get(i);
            ringsList.add(temp.getInteger("MR_Ring_Number"));
            temp.put("MR_Act_A8X",decimalFormat.format(temp.getDoubleValue("MR_Act_A8X")));
            temp.put("MR_Act_A8Y",decimalFormat.format(temp.getDoubleValue("MR_Act_A8Y")));
            temp.put("MR_Act_A8Z",decimalFormat.format(temp.getDoubleValue("MR_Act_A8Z")));
            temp.put("MR_Des_A8Mileage",decimalFormat.format(temp.getDoubleValue("MR_Des_A8Mileage")));
            temp.put("MR_Des_A8X",decimalFormat.format(temp.getDoubleValue("MR_Des_A8X")));
            temp.put("MR_Des_A8Y",decimalFormat.format(temp.getDoubleValue("MR_Des_A8Y")));
            temp.put("MR_Des_A8Z",decimalFormat.format(temp.getDoubleValue("MR_Des_A8Z")));
            temp.put("MR_Act_A8VD",Math.round(temp.getDoubleValue("MR_Act_A8VD")*1000));
            temp.put("MR_Act_A8HD",Math.round(temp.getDoubleValue("MR_Act_A8HD")*1000));

            verticalLineData.add(temp.getDoubleValue("MR_Act_A8VD")+"");
            horizontalLineData.add(temp.getDoubleValue("MR_Act_A8HD")+"");



            Double mr_act_a8HD = temp.getDouble("MR_Act_A8HD") == null ? 0 : temp.getDouble("MR_Act_A8HD");
            Double mr_act_a8VD = temp.getDouble("MR_Act_A8VD") == null ? 0 : temp.getDouble("MR_Act_A8VD");
            if(mr_act_a8HD >= 100 || mr_act_a8VD >= 100 || mr_act_a8HD <= -100 || mr_act_a8VD <= -100){
                redNum++;
            }else if(70 <= mr_act_a8HD && mr_act_a8HD < 100 ||
                    -100 < mr_act_a8HD && mr_act_a8HD <= -70 ||
                    70 <= mr_act_a8VD && mr_act_a8VD < 100 ||
                    -100 < mr_act_a8VD && mr_act_a8VD <=-70  ){
                orangeNum++;
            }else if(
                    50 <= mr_act_a8HD  || mr_act_a8HD <= -50 ||
                    50 <= mr_act_a8VD  ||mr_act_a8VD <=-50
                    ){
                yellowNum++;
            }else if(30 <= mr_act_a8HD  || mr_act_a8HD <= -30 ||
                    30 <= mr_act_a8VD  ||mr_act_a8VD <=-30){
                blueNum++;
            }else{
                greenNum++;
            }
        }
        Integer total = data == null ? 0 :data.size();
        String redRatio = decimalFormat.format((redNum*100.0/total));
        String orangeRatio = decimalFormat.format((orangeNum*100.0/total));
        String yellowRatio = decimalFormat.format((yellowNum*100.0/total));
        String blueRatio = decimalFormat.format((blueNum*100.0/total));
        Double greenRatio = 100 - Double.parseDouble(blueRatio) - Double.parseDouble(yellowRatio)  -
                Double.parseDouble(orangeRatio)- Double.parseDouble(redRatio);

        green.put("value",greenRatio);
        blue.put("value",blueRatio);
        yellow.put("value",yellowRatio);
        orange.put("value",orangeRatio);
        red.put("value",redRatio);

        returnInfo.put("dataList",data);
        returnInfo.put("ringsList",ringsList);
        returnInfo.put("verticalLine",verticalLineData);
        returnInfo.put("horizontalLine",horizontalLineData);
        returnInfo.put("pieData",pieData);

        return returnInfo;
    }

    public List<JSONObject> getDataByProjectIds(JSONObject paramJson){
        String sql = "com.raising.forward.mapper.measureResult.getAllData";
        List<JSONObject> objects = sqlSessionTemplate.selectList(sql, paramJson);
        if(objects == null){
            objects = new ArrayList<JSONObject>();
        }
        return objects;
    }

    public Integer getMaxId(JSONObject paramJson){
        String sql = "com.raising.forward.mapper.measureResult.getMaxId";
        Integer id = sqlSessionTemplate.selectOne(sql,paramJson);
        return id;
    }
}
