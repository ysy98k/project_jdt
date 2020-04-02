package com.raising.rest.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.raising.forward.service.ProjectForwardService;
import com.raising.forward.service.PropertiesValue;
import com.raising.forward.service.SectionService;
import com.raising.forward.service.tbmManage.TbmResumeService;
import com.raising.thread.DataUploadInsert;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.FutureTask;

/**
 * 数据上传存储Service
 */
@Service("storeService")
public class StoreService {


    //租户名称
    //private static final String TENANT_NAME = "raising";
    //单记录更新模式
    private static final String MODE_SINGLE="update_insert";
    //整表更新模式
    private static final String MODE_TOTAL="delete_insert";
    //追加更新模式
    private static final String MODE_ADD="insert";
    //成功code
    private static final int ERRCODE_SUCCESS = 0;



    @Autowired
    protected SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private TbmResumeService tbmResumeService;

    @Autowired
    private ProjectForwardService projectForwardService;

    @Transactional
    public JSONObject saveData(JSONObject paramJson) throws Exception {
        JSONObject returnInfo = new JSONObject();//设置总的返回结果
        Integer projectId = paramJson.getInteger("projectId");
        //第二步：判断模式
        String commond = paramJson.getString("command");
        //第三步：根据不同的模式，调用不同的sql语句。并将Map传入其中，操作数据库
        if (MODE_SINGLE.equals(commond)) {//如果是单记录更新模式
            returnInfo = modeSingle(paramJson,projectId);
        } else if (MODE_TOTAL.equals(commond)) {//如果是整表更新模式
            returnInfo = modeTotal(paramJson,projectId);
        } else if (MODE_ADD.equals(commond)) {//如果是追加更新模式
            returnInfo = modeAdd (paramJson,projectId);
        }
        //第四步：返回结果
        return returnInfo;
    }




    /**
     * 单记录更新模式
     * @param paramJson
     * @param projectId
     * @throws Exception
     */
    public JSONObject modeSingle(JSONObject paramJson,Integer projectId) throws Exception {
        JSONObject returnInfo = new JSONObject();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ArrayList<String> columnNames = getColumnNames(paramJson,false);//存放列名
        //读取关键字，作为数据校验的列名
        String compareCloumn = paramJson.getString("keyWorld");
        //封装数据至map
        Map paramMap = new HashMap();
        //paramMap.put("tenant",TENANT_NAME);
        paramMap.put("tableName", paramJson.getString("tableName"));
        paramMap.put("columnNames",columnNames);
        JSONArray dataArray = paramJson.getJSONArray("data");
        //循环数据，判断增加或更新
        for(int i=0;i<dataArray.size();i++){
            JSONObject dataObj = dataArray.getJSONObject(i);
            boolean update = false;//用来判断是增加还是修改
            ArrayList oneRowValues = new ArrayList();//存放一行的数据
            for(int j =0; j<columnNames.size();j++){
                if("pro_id".equals(columnNames.get(j))){
                    oneRowValues.add(j,projectId);
                    continue;
                }
                if(compareCloumn.equals(columnNames.get(j))){
                    //根据关键字，作为
                    //如果表中存在记录，则修改。否则新增
                    String oneRowValue =  dataObj.getString(columnNames.get(j));
                    JSONObject queryJson = new JSONObject();
                    queryJson.put("projectId",projectId);
                    queryJson.put("keyName",compareCloumn);
                    queryJson.put("keyValue",oneRowValue);
                    //queryJson.put("keyValue",parse);
                    queryJson.put("columnNames",columnNames);
                    //queryJson.put("tenant",TENANT_NAME);
                    queryJson.put("tableName", paramJson.getString("tableName"));
                    List<JSONObject> jsonObjects =
                            this.sqlSessionTemplate.selectList("com.raising.rest.DataUpload.getRows", queryJson);
                    if(jsonObjects != null && jsonObjects.size() > 0){
                        update = true;
                    }
                    oneRowValues.add(j,oneRowValue);
                    continue;
                }
                String oneRowValue =  dataObj.getString(columnNames.get(j));
                oneRowValues.add(j,oneRowValue);
            }
            //操作数据库
            int result = -1;
            int historyResult = -1;
            if(update == false){
                //放入值集合
                ArrayList<ArrayList> columnValues = new ArrayList();//存放所有行的数据
                columnValues.add(oneRowValues);
                paramMap.put("columnValues",columnValues);
                paramMap.put("tableName", paramJson.getString("tableName"));

                if(columnValues.contains("MR_Ring_Num")){
                    int a = 1;
                }

                result = this.sqlSessionTemplate.insert("com.raising.rest.DataUpload.addRow", paramMap);
                //对history表进行操作
                if (result > 0) {
                    paramMap.put("tableName", paramJson.getString("tableName") + "_history");
                    Date date = new java.util.Date(System.currentTimeMillis());
                    columnNames.add("timestamp");
                    oneRowValues.add(simpleDateFormat.format(date));
                    historyResult = this.sqlSessionTemplate.insert("com.raising.rest.DataUpload.addRow", paramMap);
                }
                paramMap.remove("columnValues");
            }else{
                paramMap.put("pro_id", projectId);
                //paramMap.put("dbName", dataObj.getString("dbName"));
                paramMap.put("keyName", compareCloumn);
                paramMap.put("keyValue", dataObj.getString(compareCloumn));
                paramMap.put("oneRow",oneRowValues);
                paramMap.put("tableName", paramJson.getString("tableName"));
                result = this.sqlSessionTemplate.update("com.raising.rest.DataUpload.updateRow", paramMap);
                //对history表进行操作
                if (result > 0) {
                    paramMap.put("tableName", paramJson.getString("tableName") + "_history");
                    Date date = new java.util.Date(System.currentTimeMillis());
                    columnNames.add("timestamp");
                    oneRowValues.add(simpleDateFormat.format(date));
                    ArrayList<ArrayList> columnValues = new ArrayList();//存放所有行的数据
                    columnValues.add(oneRowValues);
                    paramMap.put("columnValues",columnValues);
                    historyResult = this.sqlSessionTemplate.insert("com.raising.rest.DataUpload.addRow", paramMap);
                }
                paramMap.remove("oneRow");
            }
            columnNames.remove("timestamp");
            //对结果判断，失败则抛出异常回滚
            if(result <= 0 || historyResult <= 0){
                throw new Exception();
            }
        }
        returnInfo.put("errcode", ERRCODE_SUCCESS);
        returnInfo.put("message", "upload success!");
        return returnInfo;

    }

    /**
     * 整表更新模式
     * @param paramJson
     * @param projectId
     * @return
     */
    public JSONObject modeTotal(JSONObject paramJson,Integer projectId) throws Exception {
        //采取列名和列值分别放入不同的list集合中。根据下标一一对应。
        //columnNames下标为0存放的列名，其对应的值，存放在oneRowValues下标0中
        JSONObject returnInfo = new JSONObject();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取列名
        ArrayList<String> columnNames = getColumnNames(paramJson,false);//存放列名,
        //获取行数据
        ArrayList<ArrayList> columnValues = getColumnValues(paramJson,projectId,columnNames,null);
        Map paramMap = new HashMap();
        //paramMap.put("tenant", TENANT_NAME);
        paramMap.put("pro_id", projectId);
        paramMap.put("tableName", paramJson.getString("tableName"));
        int delete = this.sqlSessionTemplate.delete("com.raising.rest.DataUpload.deleteRow", paramMap);

        if(columnNames == null || columnValues == null){//如果传来的数据data为空数组。执行删除，然后结束
            returnInfo.put("errcode", ERRCODE_SUCCESS);
            returnInfo.put("message", "upload success!");
            return returnInfo;
        }
        paramMap.put("columnNames",columnNames);
        paramMap.put("columnValues",columnValues);
        int result = this.sqlSessionTemplate.insert("com.raising.rest.DataUpload.addRow", paramMap);
        int resultOld = -1;
        if (result > 0) {
            paramMap.put("tableName", paramJson.getString("tableName") + "_history");
            Date date = new Date(System.currentTimeMillis());
            columnNames.add("timestamp");
            for(int i = 0;i<columnValues.size();i++){
                ArrayList oneRowValues = columnValues.get(i);
                oneRowValues.add(simpleDateFormat.format(date));
            }
            resultOld = this.sqlSessionTemplate.insert("com.raising.rest.DataUpload.addRow", paramMap);
        }
        if(result > 0 && resultOld > 0){
            returnInfo.put("errcode", ERRCODE_SUCCESS);
            returnInfo.put("message", "upload success!");
        }else{//如果失败，则抛出异常回滚
            throw new Exception();
        }
        return returnInfo;
    }

    /**
     * 追加更新模式
     * 此业务针对吊篮，不需要对history表操作
     * @param paramJson
     * @param projectId
     * @throws Exception
     */
    public JSONObject modeAdd (JSONObject paramJson,Integer projectId) throws Exception {
        JSONObject returnInfo = new JSONObject();
        //读取关键字，作为数据校验的列名
        String compareCloumn = paramJson.getString("timeColumnif");
        ArrayList<String> columnNames = getColumnNames(paramJson,false);
        Map paramMap = new HashMap();
        paramMap.put("tableName", paramJson.getString("tableName"));
        //paramMap.put("tenant", TENANT_NAME);
        paramMap.put("columnNames",columnNames);
        paramMap.put("projectId",projectId);//查询该项目的最新时间用
        Long tableUpdataTime;
        if (compareCloumn != null)
            tableUpdataTime = getLatestTime(paramMap,compareCloumn);//表中最新时间，用来和每行记录时间比较
        else
            tableUpdataTime = null;
        paramMap.remove("projectId");//用完舍弃
        ArrayList<ArrayList> columnValues = getColumnValues(paramJson,projectId,columnNames,tableUpdataTime);
        if(columnValues.size() < 1){
            returnInfo.put("errcode", ERRCODE_SUCCESS);
            returnInfo.put("message", "数据没有更新。原因：上传数据为空，或者所有上传数据的时间均早于数据库中该项目最新时间");
            return returnInfo;
        }
        paramMap.put("columnValues",columnValues);

        DataUploadInsert dataUploadInsert = new DataUploadInsert(this.sqlSessionTemplate,paramMap);
        FutureTask<Integer> future = new FutureTask<>(dataUploadInsert);
        new Thread(future).start();
        returnInfo.put("errcode", ERRCODE_SUCCESS);
        returnInfo.put("message", "upload success!");

        /*int result = this.sqlSessionTemplate.insert("com.raising.rest.DataUpload.addRow", paramMap);
        if(result > 0 ){
            returnInfo.put("errcode", ERRCODE_SUCCESS);
            returnInfo.put("message", "upload success!");
        }else{//如果失败则回滚
            throw new Exception();
        }*/
        return returnInfo;
    }

    /**
     * 查询表中最新时间
     */
    public Long getLatestTime(Map paramMap,String compareCloumn){
        Date tableUpdataTime = null;
        paramMap.put("orderBy","\""+compareCloumn+"\" desc");
        paramMap.put("pageSize",1);
        paramMap.put("pageIndex",0);
        List<JSONObject> resultJsons = this.sqlSessionTemplate.selectList("com.raising.rest.DataUpload.getRows", paramMap);
        if(resultJsons == null || resultJsons.size() < 1){
            return null;
        }
        JSONObject resultObj = resultJsons.get(0);
        String tableUpdataTimeStr = resultObj.getString(compareCloumn);
        try {
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            tableUpdataTime = simpleDateFormat.parse(tableUpdataTimeStr);//表中最新时间，用来和每行记录时间比较
        } catch (ParseException e) {
            e.printStackTrace();
        }
        paramMap.remove("orderBy");
        paramMap.remove("pageSize");
        paramMap.remove("pageIndex");
        return tableUpdataTime.getTime();
    }

    /**
     * 封装数据，得到列集合
     * checkTime 为true做时间校验，false，不做
     */
    public ArrayList<String> getColumnNames(JSONObject paramJson,Boolean checkTime){
        ArrayList<String> columnNames = new ArrayList();//存放列名,
        JSONArray dataArray = paramJson.getJSONArray("data");
        if(dataArray.size() < 1){
            return null;
        }
        //封装列
        String tableName = paramJson.getString("tableName");
        if("MeasureResult".equals(tableName)){//如果是这长表。那么就处理它一下。
            String getColumnSql = "com.raising.rest.DataUpload.getColumnName";
            Map paramMap = new HashMap();
            paramMap.put("raising","raising");
            paramMap.put("tableName","MeasureResult");
            List<String> columns = sqlSessionTemplate.<String>selectList(getColumnSql, paramMap);
            columns.remove("id");//id为表中主键。不会传此数据。此id由数据库自己生成。
            return (ArrayList<String>)columns ;


        }
        JSONObject dataObj = dataArray.getJSONObject(0);
        Set<String> columnNameSet = dataObj.keySet();
        for (String temp : columnNameSet) {
            String keyValue = dataObj.getString(temp);
            if("null".equalsIgnoreCase(keyValue) || StringUtils.isNullOrEmpty(keyValue)){//如果改key的value为null。则不插入
                continue;
            }
            columnNames.add(temp);
        }
        columnNames.add("pro_id");
        if("t_section".equals(tableName) || "tbm_info".equals(tableName)){
            columnNames.remove("pro_id");
        }
        if(checkTime){
            columnNames.add("timestamp");
        }
        return columnNames;
    }

    /**
     * 封装数据，得到值集合
     * tableUpdataTime 为null不做时间校验，不为null,则以tableUpdateTime 为基准做时间校验
     */
    public ArrayList<ArrayList> getColumnValues(JSONObject paramJson,Integer projectId,ArrayList<String> columnNames,Long tableUpdataTime) throws ParseException {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //读取时间校验列名，如果tableUpdataTime为null则为null，反之有值
        String compareCloumn = paramJson.getString("timeColum");
        ArrayList<ArrayList> columnValues = new ArrayList();//存放所有行的数据

        JSONArray dataArray = paramJson.getJSONArray("data");
        if(dataArray.size() < 0){
            return null;
        }
        //封装值
        for (int i = 0; i < dataArray.size(); i++) {
            JSONObject dataObj = dataArray.getJSONObject(i);
            ArrayList oneRowValues = new ArrayList();//存放一行的数据
            //时间校验，如果不通过，则不放入值集合中
            if(tableUpdataTime != null){
                String compareTimeStr  = dataObj.getString(compareCloumn);
                Long compareTime = simpleDateFormat.parse(compareTimeStr).getTime();
                if(compareTime - tableUpdataTime <= 0 ){
                    continue;
                }
            }
            //填充oneRowValues
            for(int j =0; j<columnNames.size();j++){
                if("pro_id".equals(columnNames.get(j))){
                    oneRowValues.add(j,projectId);
                    continue;
                }
                if("timestamp".equals(columnNames.get(j))){
                    Date date = new Date(System.currentTimeMillis());
                    oneRowValues.add(j,simpleDateFormat.format(date));
                    continue;
                }
                String oneRowValue =  dataObj.getString(columnNames.get(j));
                oneRowValues.add(j,oneRowValue);
            }
            //放入值集合
            columnValues.add(oneRowValues);
        }
        return columnValues;
    }

    /**
     * 检查项目名称和表名是否存在
     * @param paramJson
     * @return
     */
    public JSONObject checkAndGetServerTableName(JSONObject paramJson){
        JSONObject returnInfo = new JSONObject();//设置总的返回结果
        String clientTableName = paramJson.getString("clientTableName");
        String collectorName = paramJson.getString("collectorName");
        if( "disdata".equals(clientTableName) || "mileagedata".equals(clientTableName) ){//如果是disdata 、mileagedata两张表，就不做校验。
            returnInfo.put("tableName","j_"+clientTableName+"_"+collectorName);
        }else{
            String serverTableName = null;
            String tableHeaderStr = PropertiesValue.CLIENT_TABLE_HEADER;
            JSONObject tableHeaderJson =  JSONObject.parseObject(tableHeaderStr);
            Set<String> tableHeaderSet = tableHeaderJson.keySet();
            for(String tableHeaderTemp : tableHeaderSet){
                //判断表名是否在表集合中
                JSONArray clientTableArray = tableHeaderJson.getJSONArray(tableHeaderTemp);
                if(clientTableArray.contains(clientTableName)){
                    serverTableName = tableHeaderTemp + "_" + clientTableName;
                    break;
                }
            }
            if(StringUtils.isNullOrEmpty(serverTableName)){
                returnInfo.put("code", Constants.EXECUTE_FAIL);
                returnInfo.put("message", "表名错误，"+paramJson.getString("tableName")+"表在数据库中不存在，请检查数据！");
                return returnInfo;
            }

            Map paramMap = new HashMap();
            paramMap.put("tableName", serverTableName);
            int result = this.sqlSessionTemplate.selectOne("com.raising.rest.DataUpload.checkTableName",paramMap);
            if(result != 1){
                returnInfo.put("code", Constants.EXECUTE_FAIL);
                returnInfo.put("message", "表名错误，"+paramJson.getString("tableName")+"表在数据库中不存在，请检查数据！");
                return returnInfo;
            }
            returnInfo.put("tableName",serverTableName);
        }
        returnInfo.put("status", Constants.EXECUTE_SUCCESS);
        return returnInfo;
    }







    public List<String> getColumns(String tableName){
        String sql = "com.raising.rest.DataUpload.getColumnName";
        Map paramMap = new HashMap();
        paramMap.put("raising","raising");
        paramMap.put("tableName",tableName);
        List<String> columns = sqlSessionTemplate.selectList(sql, paramMap);
        return columns;
    }

    public JSONObject getRows(JSONObject paramJson){
        JSONObject returnInfo = new JSONObject();
        String timeColumnName = paramJson.getString("timeColumnName");
        //paramJson.put("tenant",TENANT_NAME);

        paramJson.put("projectId",paramJson.getInteger("projectId"));
        String querySql = "com.raising.rest.DataUpload.getRowsOfTest";
        String countSql = "com.raising.rest.DataUpload.getCountOfTest";
        //设置分页数据
        Integer curPage = (Integer)paramJson.get("curPage");
        Integer curRowNum = (Integer)paramJson.get("curRowNum");
        if(curPage == null || curRowNum == null){
            curRowNum = 1000;
            curPage = 1;
        }
        //分页并封装返回结果
        PageHelper.startPage(curPage, curRowNum);
        List<JSONObject> result = sqlSessionTemplate.selectList(querySql,paramJson);
        PageInfo<JSONObject> page = new PageInfo<JSONObject>(result);

        if(!StringUtils.isNullOrEmpty(timeColumnName)){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for(int i=0;i<result.size();i++){
                JSONObject temp =  result.get(i);
                if(temp.containsKey(timeColumnName)){
                    Timestamp timestamp = temp.getTimestamp(timeColumnName);
                    temp.put(timeColumnName,format.format(new Date(timestamp.getTime())));
                }
            }
        }

        int count = sqlSessionTemplate.selectOne(countSql, paramJson);
        returnInfo.put("total",page.getPages());
        returnInfo.put("page",page.getPageNum());
        returnInfo.put("records",page.getPageSize());
        returnInfo.put("rows",result);
        returnInfo.put("status","0");
        returnInfo.put("returnMsg","查询成功！本次返回" + page.getList().size() + "条记录，总共" + count + "条记录！");
        return returnInfo;
    }



    /**
     * 项目上传接口
     * @param paramJson
     * @return
     */
    public JSONObject saveProject(JSONObject paramJson){
        JSONObject returnInfo = new JSONObject();//设置总的返回结果
        JSONArray projectsArr = paramJson.getJSONArray("data");
        if(projectsArr == null || projectsArr.size() < 1){
            returnInfo.put("errcode", Constants.EXECUTE_FAIL);
            returnInfo.put("errmsg", "出错了，上传项目信息为空！");
            return returnInfo;
        }
        JSONObject projectJson = projectsArr.getJSONObject(0);
        String sectionName = projectJson.getString("section_name");
        String tbmName = projectJson.getString("tbm_name");
        JSONObject section = sectionService.getSectionByName(sectionName);
        JSONObject tbm = tbmResumeService.getTbmByName(tbmName);
        if(section == null || tbm == null){
            returnInfo.put("errcode", Constants.EXECUTE_FAIL);
            returnInfo.put("errmsg", "出错了，项目所依赖的区间或盾构机信息不存在，请确保区间或盾构机信息上传成功。");
            return returnInfo;
        }
        Integer sectionId = section.getInteger("sectionId");
        Integer tbmId = tbm.getInteger("tbmId");
        projectJson = convertProject(projectJson);
        projectJson.put("sectionId",sectionId);
        projectJson.put("tbmId",tbmId);
        int i = projectForwardService.addProject(projectJson);
        if(i <1){
            returnInfo.put("errcode", Constants.EXECUTE_FAIL);
            returnInfo.put("errmsg", "出错了，插入数据失败！请检查数据格式");
            return returnInfo;
        }
        returnInfo.put("errcode", ERRCODE_SUCCESS);
        returnInfo.put("message", "upload success!");
        return returnInfo;
    }

    private JSONObject convertProject(JSONObject project){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(project.containsKey("pro_name")){
            project.put("projectName",project.getString("pro_name"));
        }
        if(project.containsKey("total_length")){
            project.put("totalLength",project.getDouble("total_length"));
        }
        if(project.containsKey("end_mileage")){
            project.put("endMileage",project.getDouble("end_mileage"));
        }
        if(project.containsKey("pro_situation")){
            project.put("projectSituation",project.getString("pro_situation"));
        }
        if(project.containsKey("start_mileage")){
            project.put("startMileage",project.getDouble("start_mileage"));
        }
        if(project.containsKey("pro_location")){
            project.put("projectLocation",project.getString("pro_location"));
        }
        if(project.containsKey("tunnel_direction")){
            project.put("tunnelDrection",project.getString("tunnel_direction"));
        }
        if(project.containsKey("template_name")){
            project.put("templateName",project.getString("template_name"));
        }
        if(project.containsKey("day_shift_start")){
            project.put("dayShiftStart",project.getString("day_shift_start"));
        }
        if(project.containsKey("day_shift_end")){
            project.put("dayShiftEnd",project.getString("day_shift_end"));
        }
        if(project.containsKey("update_time")){
            try {
                project.put("updateTime",dateFormat.parse(project.getString("update_time")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(project.containsKey("end_time")){
            try {
                project.put("endTime",dateFormat.parse(project.getString("end_time")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(project.containsKey("create_time")){
            try {
                project.put("createTime", dateFormat.parse(project.getString("create_time")) );
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(project.containsKey("start_time")){
            try {
                project.put("startTime",dateFormat.parse(project.getString("start_time")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(project.containsKey("collector_name")){
            project.put("collectorName",project.getString("collector_name"));
        }
        if(project.containsKey("ring_total")){
            project.put("ringTotal",project.getString("ring_total"));
        }
        if(project.containsKey("build_unit")){
            project.put("buildUnit",project.getString("build_unit"));
        }
        if(project.containsKey("geology_info")){
            project.put("geologyInfo",project.getString("geology_info"));
        }
        return project;
    }


    //判断是否存在里程与行程表
    public boolean existTable(JSONObject paramJson){
        JSONObject returnInfo = new JSONObject();
        String tableName = paramJson.getString("tableName");
        /*String collectionName = paramJson.getString("collectorName");
        //检查表。如果存在表则不创建
        String tableName2 = "j_"+tableName+"_"+collectionName;*/
        Map paramMap = new HashMap();
        paramMap.put("tableName", tableName);
        int result = this.sqlSessionTemplate.selectOne("com.raising.rest.DataUpload.checkTableName",paramMap);
        if(result > 0){
            return true;
        }else {
            return false;
        }
    }

    //创建里程或行程表序列
    @Transactional
    public void createSequence(JSONObject paramJson) throws Exception {
        JSONObject returnInfo = new JSONObject();
        String tableName = paramJson.getString("tableName");
        /*String collectionName = paramJson.getString("collectorName");
        //检查表。如果存在表则不创建
        String tableName2 = "j_"+tableName+"_"+collectionName;*/
        Map paramMap = new HashMap();
        paramMap.put("sequence",  tableName.toLowerCase());
        int result2 = this.sqlSessionTemplate.update("com.raising.rest.DataUpload.createSequence",paramMap);
        if (result2 < 0){
            throw new Exception();
        }
    }

    /**
     * disdata,mileagedata表上传时，先创建表
     */
    @Transactional
    public JSONObject createTable(JSONObject paramJson) throws Exception {
        JSONObject returnInfo = new JSONObject();
        /*JSONObject extra = paramJson.getJSONObject("extra");*/
        String tableName = paramJson.getString("tableName");
        String collectionName = paramJson.getString("collectorName");
        //检查表。如果存在表则不创建
       /* String tableName2 = "j_"+tableName+"_"+collectionName;*/
        Map paramMap = new HashMap();
        paramMap.put("tableName", tableName);
        paramMap.put("sequence",tableName.toLowerCase());
        int result1 = this.sqlSessionTemplate.update("com.raising.rest.DataUpload.createTable",paramMap);
        //建表成功后，创建主键与索引
        int update1 = this.sqlSessionTemplate.update("com.raising.rest.DataUpload.createProjectIdIndex", paramMap);
        int update2 = this.sqlSessionTemplate.update("com.raising.rest.DataUpload.createMileageIndex", paramMap);
        int update3 = this.sqlSessionTemplate.update("com.raising.rest.DataUpload.createRingNumIndex", paramMap);
        if( result1 < 0 || update1 <0 || update2 <0 || update3 < 0){
            throw new Exception();
        }

        //paramJson.put("tableName",tableName2);//替换表名
        returnInfo.put("status", Constants.EXECUTE_SUCCESS);
        return returnInfo;
    }
}
