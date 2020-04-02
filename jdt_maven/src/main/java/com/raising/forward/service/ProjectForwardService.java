package com.raising.forward.service;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.common.NewBaseService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.baosight.common.utils.DateUtils.String2Date;


@Service("projectForwardService")
public class ProjectForwardService extends NewBaseService {

    private static final double OFFSET = 10.00 / 6400000 * 180 / 3.14;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SqlSessionTemplate sqlSession;

    public JSONObject getProjectInfo(String ajaxParam)  {
        JSONObject ajaxJson = JSONObject.parseObject(ajaxParam);
        Integer projectId = ajaxJson.getInteger("projectId");
        if(projectId == null){
            return null;
        }
        JSONObject paramJson = new JSONObject();
        paramJson.put("projectId",projectId);
        paramJson.put("tenant",request.getSession().getAttribute("tenant"));
        String sql = "com.raising.forward.mapper.ProjectInfoMapper.getRows";
        JSONObject returnInfo = sqlSession.selectOne(sql, paramJson);

        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTimeStr = returnInfo.getString("startTime");
        String endTimeStr = returnInfo.getString("endTime");
        try {
            if(!StringUtils.isNullOrEmpty(startTimeStr) ){
                Date parse = format.parse(startTimeStr);
                String format1 = format.format(parse);
                returnInfo.put("startTime",format1);
            }
            if(!StringUtils.isNullOrEmpty(endTimeStr) ){
                Date parse = format.parse(endTimeStr);
                String format1 = format.format(parse);
                returnInfo.put("endTime", format1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnInfo;
    }

    @Transactional
    public JSONObject updateProject(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject ajaxJson = JSONObject.parseObject(ajaxParam);
        //不修改sectionName。和collectorName
        ajaxJson.remove("sectionName");
        ajaxJson.remove("collectorName");
        ajaxJson.remove("tbmName");
        Integer projectId = ajaxJson.getInteger("projectId");
        if(projectId == null){
            returnInfo.put("status", Constants.EXECUTE_FAIL);
            returnInfo.put("message", "数据不合法，请检查非空验证");
            return returnInfo;
        }
        //将时间字符串转换为Date类型
        String startTimeStr = ajaxJson.getString("startTime");
        String endTimeStr = ajaxJson.getString("endTime");
        if(!StringUtils.isNullOrEmpty(startTimeStr) ){
            ajaxJson.put("startTime", String2Date(new String(startTimeStr), "yyyy-MM-dd HH:mm:ss"));
        }
        if(!StringUtils.isNullOrEmpty(endTimeStr) ){
            ajaxJson.put("endTime", String2Date(new String(endTimeStr), "yyyy-MM-dd HH:mm:ss"));
        }
        ajaxJson.put("startMileage", ajaxJson.getFloat("startMileage"));
        ajaxJson.put("endMileage", ajaxJson.getFloat("endMileage"));
        ajaxJson.put("totalLength",ajaxJson.getFloat("totalLength"));
        Date date = new java.util.Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ajaxJson.put("updateTime",String2Date(new String(simpleDateFormat.format(date)), "yyyy-MM-dd HH:mm:ss"));
        String sql = "com.raising.forward.mapper.ProjectInfoMapper.update";
        ajaxJson.put("tenant",request.getSession().getAttribute("tenant"));

        int update = sqlSession.update(sql, ajaxJson);
        if(update < 0){
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("message","修改失败");
        }else{
            returnInfo.put("status",Constants.EXECUTE_SUCCESS);
            returnInfo.put("message","修改成功");
        }

        return returnInfo;
    }

    /**
     * 根据条件，获取所有项目
     * @param param
     * @return
     */
    public JSONObject getProjectStatusByTbm(JSONObject param){
        String sql = "com.raising.forward.mapper.ProjectInfoMapper.getProjectStatusByTbm";
        List<JSONObject> returnInfo = sqlSession.selectList(sql, param);
        if(returnInfo == null || returnInfo.size() < 1){
            return new JSONObject() ;
        }
        return returnInfo.get(0);
    }

    /**
     * 根据采集器名，获取用户权限下的项目信息
     * @param collectorsNames
     * @return
     */
    public List<JSONObject> getResourcesProject(List<String> collectorsNames){
        List<JSONObject> returnList = null;
        //权限过滤
        if(collectorsNames == null || collectorsNames.size() < 1){
            return returnList;
        }
        JSONObject projectParam  = new JSONObject();
        projectParam.put("collectorNameList",collectorsNames);
        projectParam.put("tenant","raising");
        String projectInfoSql = "com.raising.forward.mapper.ProjectInfoMapper.getRowsWithResources";
        returnList =  this.sqlSessionTemplate.selectList(projectInfoSql,projectParam);
        return returnList;
    }

    @Transactional
    public int addProject(JSONObject project){
        String sql = "com.raising.forward.mapper.ProjectInfoMapper.addRows";
        project.put("tenant","raising");
        int insert = sqlSession.insert(sql, project);
        return insert;
    }

    /**
     * 根据项目Id，获取所有数据
     * @param paramJson
     * @return
     */
    public List<JSONObject> getDataByProjectIds(JSONObject paramJson){
        String sql = "com.raising.forward.mapper.ProjectInfoMapper.getAllData";
        List<JSONObject> data = sqlSessionTemplate.selectList(sql, paramJson);
        return data;
    }

    /**
     * 根据项目id，获取最大Id
     * @param paramJson
     * @return
     */
    public Integer getMaxId(JSONObject paramJson){
        String sql = "com.raising.forward.mapper.ProjectInfoMapper.getMaxId";
        Integer max = sqlSessionTemplate.selectOne(sql, paramJson);
        return max;
    }
























}
