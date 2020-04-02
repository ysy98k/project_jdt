package com.raising.backstage.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.common.NewBaseService;
import com.raising.ccs.ResourceService;
import com.raising.forward.mapper.DaoUtil;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.baosight.common.utils.DateUtils.String2Date;
import static com.baosight.common.utils.DateUtils.date2String;

/**
 * 项目Service
 */

@Service("projectService")
public class ProjectService extends NewBaseService{

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);






    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SqlSessionTemplate sqlSession;

    @Autowired
    private DaoUtil daoUtil;

    @Autowired
    private ResourceService resourceService;





    /**
     * 根据条件，查询项目信息。针对表格返回
     * @param ajaxParam
     * @return
     */
    public JSONObject getProject(String ajaxParam){
        JSONObject paramInfo = JSON.parseObject(ajaxParam);

        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        String querySql = "com.raising.forward.mapper.ProjectInfoMapper.getRowsForBackstage";
        String countSql = "com.raising.forward.mapper.ProjectInfoMapper.countForBackstage";
        String daoEntity = "com.raising.forward.entity.ProjectInfo";
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, querySql);
        paramInfo.put(BaseService.COUNT_SQL, countSql);
        paramInfo.put(BaseService.DAO_ENTITY, daoEntity);
        JSONObject returnInfo = daoUtil.query(paramInfo);
        JSONArray rows = returnInfo.getJSONArray("rows");
        if(rows != null && rows.size() > 0){
            for(int i = 0;i<rows.size();i++){
                JSONObject temp = rows.getJSONObject(i);
                String createTime = temp.getString("createTime");
                String startTimeStr = temp.getString("startTime");
                String endTimeStr = temp.getString("endTime");
                if(!StringUtils.isNullOrEmpty(createTime)){
                    temp.put("createTime", date2String("yyyy-MM-dd HH:mm:ss", new Date(temp.getLong("createTime"))));
                }
                if(!StringUtils.isNullOrEmpty(startTimeStr)){
                    temp.put("startTime", date2String("yyyy-MM-dd HH:mm:ss", new Date(Long.parseLong(startTimeStr))));
                }
                if(!StringUtils.isNullOrEmpty(endTimeStr)){
                    temp.put("endTime", date2String("yyyy-MM-dd HH:mm:ss", new Date(Long.parseLong(endTimeStr))));
                }
            }
        }
        return returnInfo;
    }


    /**
     *增加项目
     */
    @Transactional
    public JSONObject addProjects(String ajaxParam) throws Exception {
        JSONObject paramInfo = JSON.parseObject(ajaxParam);
        JSONArray dataArray = paramInfo.getJSONObject("detail").getJSONArray("resultRow");
        for(int i=0;i<dataArray.size();i++){
            JSONObject dataJson = dataArray.getJSONObject(i);
            //将时间字符串转换为Date类型
            String startTimeStr = dataJson.getString("startTime");
            String endTimeStr = dataJson.getString("endTime");
            String createTimeStr = dataJson.getString("createTime");
            if(!StringUtils.isNullOrEmpty(startTimeStr) ){
                dataJson.put("startTime", String2Date(new String(startTimeStr), "yyyy-MM-dd HH:mm:ss"));
            }
            if(!StringUtils.isNullOrEmpty(endTimeStr) ){
                dataJson.put("endTime", String2Date(new String(endTimeStr), "yyyy-MM-dd HH:mm:ss"));
            }
            dataJson.put("createTime", String2Date(new String(createTimeStr), "yyyy-MM-dd HH:mm:ss"));
            Date date = new java.util.Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dataJson.put("updateTime",String2Date(new String(simpleDateFormat.format(date)), "yyyy-MM-dd HH:mm:ss"));
        }
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        String insertSql = "com.raising.forward.mapper.ProjectInfoMapper.addRows";
        String daoEntity = "com.raising.forward.entity.ProjectInfo";
        paramInfo.put(BaseService.DAO_ENTITY,daoEntity);
        paramInfo.put("update",false);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.RESULT_BLOCK,"detail");
        paramInfo.put(BaseService.INSERT_SQL, insertSql);
        JSONObject returnInfo = new JSONObject();

        returnInfo = daoUtil.insert(paramInfo);

        if (Constants.EXECUTE_SUCCESS.equals(returnInfo.getString("status"))) {
            JSONArray projectArray = returnInfo.getJSONObject("detail").getJSONArray("resultRow");
            resourceService.addResource(returnInfo,projectArray);
        }
        return returnInfo;
    }

    /**
     * 删除项目     *
     */
    public JSONObject deleteProjects(String ajaxParam) throws Exception {
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONArray dataArray = paramInfo.getJSONObject("result").getJSONArray("resultRow");
        for(int i=0;i<dataArray.size();i++){
            JSONObject temp = dataArray.getJSONObject(i);
            temp.remove("createTime");
        }
        String deleteSql = "com.raising.forward.mapper.ProjectInfoMapper.delete";
        String entity = "com.raising.forward.entity.ProjectInfo";

        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        paramInfo.put("update", false);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.DELETE_SQL, deleteSql);
        paramInfo.put(BaseService.RESULT_BLOCK, "result");
        paramInfo.put(BaseService.DAO_ENTITY,entity);
        JSONObject returnInfo = null;

        returnInfo = daoUtil.delete(paramInfo);

        /* 根据状态判断是否需要删除资源 */
        if (Constants.EXECUTE_SUCCESS.equals(returnInfo.getString("status"))) {
            StringBuffer resoutceName = new StringBuffer();
            JSONArray resourceArray = returnInfo.getJSONObject("result").getJSONArray("resultRow");
            resourceService.deleteResource(returnInfo,resourceArray);
        }
        return returnInfo;
    }

    /**
     * 更新     *
     * @param
     * @return 输出JSONObject
     */
    public JSONObject updateProjects(String ajaxParam) throws Exception {
        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);

        //更改之前，判断是否更改采集器名称，同时修改时间格式。
        //changedCollectorNames,用来存放采集器被修改的记录集合。key保存要删的资源。value保存要新增的资源
        Map<String,JSONObject> changedCollectorNames = new HashMap<>();
        JSONArray dataArray = paramInfo.getJSONObject("detail").getJSONArray("resultRow");
        for(int i=0;i<dataArray.size();i++){
            JSONObject dataJson = dataArray.getJSONObject(i);
            if(!dataJson.getString("collectorName").equals(dataJson.getString("collectorName2"))){
                changedCollectorNames.put(dataJson.getString("collectorName2"),dataJson);
            }
            //将时间字符串转换为Date类型
            String startTimeStr = dataJson.getString("startTime");
            String endTimeStr = dataJson.getString("endTime");
            if(!StringUtils.isNullOrEmpty(startTimeStr) ){
                dataJson.put("startTime", String2Date(new String(startTimeStr), "yyyy-MM-dd HH:mm:ss"));
            }
            if(!StringUtils.isNullOrEmpty(endTimeStr) ){
                dataJson.put("endTime", String2Date(new String(endTimeStr), "yyyy-MM-dd HH:mm:ss"));
            }
            String createTimeStr = dataJson.getString("createTime");
            dataJson.put("createTime", String2Date(new String(createTimeStr), "yyyy-MM-dd HH:mm:ss"));
            Date date = new java.util.Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dataJson.put("updateTime",String2Date(new String(simpleDateFormat.format(date)), "yyyy-MM-dd HH:mm:ss"));
        }
        String updateSql = "com.raising.forward.mapper.ProjectInfoMapper.update";
        String daoEntity = "com.raising.forward.entity.ProjectInfo";
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.UPDATE_SQL, updateSql);
        paramInfo.put(BaseService.RESULT_BLOCK, "detail");
        paramInfo.put(BaseService.DAO_ENTITY,daoEntity);
        JSONObject returnInfo = daoUtil.update(paramInfo);
        //资源操作
        if (changedCollectorNames.size() > 0 && Constants.EXECUTE_SUCCESS.equals(returnInfo.getString("status"))) {
            //封装数据
            JSONArray deleteArray = new JSONArray();
            JSONArray addArray = new JSONArray();
            Set<String> deleteResource =  changedCollectorNames.keySet();
            for (String resource: deleteResource) {
                JSONObject tempObj = new JSONObject();
                tempObj.put("collectorName",resource);
                deleteArray.add(tempObj);
                addArray.add(changedCollectorNames.get(resource));
            }
            //操作资源，现删除，在新增
            resourceService.deleteResource(returnInfo,deleteArray);
            resourceService.addResource(returnInfo,addArray);
            redisTemplate.delete("upload_check_project");
            String returnMsg = returnInfo.getString("returnMsg");
            if(returnMsg.indexOf("资源删除成功资源新增成功")  > 0){
                returnMsg = returnMsg.substring(0,returnMsg.indexOf("！")+1) + "原先的资源已更新";
                returnInfo.put("returnMsg",returnMsg );
            }
        }
        return returnInfo;
    }


    /**
     * 根据参数，获取项目列表。
     * @param querJson
     * @return
     */
    public List<JSONObject> getProjectList(JSONObject querJson){
        //数据封装
        if(querJson == null){
            return null;
        }
        String querySql = "com.raising.forward.mapper.ProjectInfoMapper.getRows";
        //查询结果
        List<JSONObject> list = this.sqlSessionTemplate.selectList(querySql, querJson);
        return list;
    }

    /**
     * 得到项目状态
     */
    public String getStatusCode(){
        JSONObject paramJson = new JSONObject();
        paramJson.put("tenant",request.getSession().getAttribute("tenant"));
        String sql = "com.raising.forward.mapper.ProjectInfoMapper.getRows";
        List<JSONObject> objects = this.sqlSession.selectList(sql, paramJson);
        if(objects == null){
            return null;
        }
        //遍历，得到status 不为空的值
        for(int i=0;i<objects.size();i++){
            JSONObject temp = objects.get(i);
            if(!StringUtils.isNullOrEmpty(temp.getString("status"))){
                return temp.getString("status");
            }
        }
        return null;
    }

    /**
     * 得到盾构信息
     */
    public List<JSONObject> getTbmInfo(){
        String sql = "com.raising.forward.mapper.TbmInfoMapper.getRows";
        JSONObject paramJson = new JSONObject();
        paramJson.put("tenant",request.getSession().getAttribute("tenant"));
        List<JSONObject> array = sqlSession.selectList(sql,paramJson);
        return  array;
    }

    public List<JSONObject> getSections(){

        String sql = "SectionManage.query";
        JSONObject paramJson = new JSONObject();
        paramJson.put("tenant",request.getSession().getAttribute("tenant"));
        List<JSONObject> array = sqlSession.selectList(sql,paramJson);
        return  array;
    }

    public JSONObject initSelect(){
        JSONObject returnInfo = new JSONObject();
        List<String> supervisorList = new ArrayList<>();
        List<String> buildUnitList = new ArrayList<>();
        String sql = "com.raising.forward.mapper.ProjectInfoMapper.getInitSelect";
        JSONObject paramJson = new JSONObject();
        paramJson.put("tenant",request.getSession().getAttribute("tenant"));
        List<JSONObject> array = sqlSession.selectList(sql,paramJson);
        if(array == null || array.size() < 1){
            returnInfo.put("haveData",Constants.EXECUTE_FAIL);
            returnInfo.put("supervisorList",supervisorList);
            returnInfo.put("buildUnitList",buildUnitList);
            return returnInfo;
        }
        for(int i =0;i<array.size();i++){
            JSONObject projectTemp = array.get(i);
            String supervisor = projectTemp.getString("supervisor");
            String buildUnit = projectTemp.getString("buildUnit");
            if(!StringUtils.isNullOrEmpty(supervisor) && !supervisorList.contains(supervisor)){
                supervisorList.add(supervisor);
            }
            if(!StringUtils.isNullOrEmpty(buildUnit) && !buildUnitList.contains(buildUnit)){
                buildUnitList.add(buildUnit);
            }
        }
        Collections.sort(supervisorList);
        Collections.sort(buildUnitList);
        returnInfo.put("haveData",Constants.EXECUTE_SUCCESS);
        returnInfo.put("supervisorList",supervisorList);
        returnInfo.put("buildUnitList",buildUnitList);
        return returnInfo;
    }




}
