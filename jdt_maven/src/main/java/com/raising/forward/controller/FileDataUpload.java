package com.raising.forward.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.raising.backstage.service.ProjectService;
import com.raising.rest.DataUpload;
import com.raising.rest.service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.GZIPInputStream;

@Controller
@RequestMapping("/raising/forward")
public class FileDataUpload {
    private static final Logger logger = LoggerFactory.getLogger(DataUpload.class);
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private StoreService storeService;

    @RequestMapping(value = "/uploadFile.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject multiImport(@RequestParam("uploadFile") MultipartFile file) throws IOException {
        JSONObject returnInfo = new JSONObject();//设置总的返回结果
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(file.getBytes());
        String suffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
        try {
            byte[] buffer = new byte[1024];
            int n;
            if (suffix.equals(".gz")) {
                GZIPInputStream gzipInputStream = new GZIPInputStream(in);
                while ((n = gzipInputStream.read(buffer)) >= 0)
                    out1.write(buffer, 0, n);
                gzipInputStream.close();
            } else if (suffix.equals(".dat")) {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
                while ((n = bufferedInputStream.read(buffer)) >= 0)
                    out1.write(buffer, 0, n);
                bufferedInputStream.close();
            }
        } catch (IOException e) {
            return null;
        }
        System.out.println(out1.toString());
        JSONArray paramArray = JSONArray.parseArray(out1.toString());
        out1.flush();
        out1.close();
        String projectName = null;
        Map<String, JSONObject> clientTableNamesMap = new HashMap<>();//用来做表明校验
        for(int i=0;i<paramArray.size();i++){
            JSONObject paramJson = paramArray.getJSONObject(i);
            JSONObject extra = paramJson.getJSONObject("extra");
            projectName = extra.getString("proName");
            paramJson.put("proName",extra.getString("proName"));
            paramJson.put("clientTableName",extra.getString("name"));
            paramJson.put("keyWorld",extra.getString("keyWorld"));
            paramJson.put("timeColumn",extra.getString("timeColumn"));
            paramJson.put("realtime",extra.getBooleanValue("realtime"));
            paramJson.put("tableName",null);
            if(!extra.getBooleanValue("realtime")){//如果不是实时数据
                clientTableNamesMap.put(extra.getString("name"),paramJson);
            }
        }

        try {
            //项目名校验
            JSONObject project = (JSONObject)redisTemplate.opsForHash().get("upload_check_project", projectName);
            if(project == null){
                JSONObject querJson  =  new JSONObject();
                querJson.put("projectNameEq",projectName);
                List<JSONObject> projectList = projectService.getProjectList(querJson);
                if (projectList == null || projectList.size()==0){//数据校验不成功
                    returnInfo.put("code", Constants.EXECUTE_FAIL);
                    returnInfo.put("message", "项目名称错误，该项目名称在数据库中不存在，请检查项目名称！");
                    return returnInfo;
                }
                Integer projectId = projectList.get(0).getInteger("projectId");
                String collectorName = projectList.get(0).getString("collectorName");
                if(projectId == null || projectId <= 0){
                    returnInfo.put("code", Constants.EXECUTE_FAIL);
                    returnInfo.put("message", "项目名称错误，该项目名称在数据库中不存在，请检查项目名称！");
                    return returnInfo;
                }
                for(int i=0;i<paramArray.size();i++){
                    JSONObject paramJson = paramArray.getJSONObject(i);
                    paramJson.put("projectId",projectId);
                    paramJson.put("collectorName",collectorName);
                }

                JSONObject projectJson = new JSONObject();
                projectJson.put("projectId",projectId);
                projectJson.put("collectorName",collectorName);
                redisTemplate.opsForHash().put("upload_check_project", projectName,projectJson);
            }else{
                for(int i=0;i<paramArray.size();i++){
                    JSONObject paramJson = paramArray.getJSONObject(i);
                    paramJson.put("projectId",project.getInteger("projectId"));
                    paramJson.put("collectorName",project.getString("collectorName"));
                }
            }
            //表名校验
            if(clientTableNamesMap.size() > 0){//如果是关系型数据表
                //1.先做数据校验，如果数据校验通过，将数据发给消息中间件，并返回成功。如果校验不通过，返回错误
                //表名校验,并获取服务端表名
                Set<String> clientTableNamesSet = clientTableNamesMap.keySet();
                for(String clientTableName : clientTableNamesSet){
                    JSONObject paramJson = clientTableNamesMap.get(clientTableName);
                    String serverTable = (String)redisTemplate.opsForHash().get("upload_check_table", clientTableName);
                    if(StringUtils.isNullOrEmpty(serverTable)){
                        JSONObject checkTable = storeService.checkAndGetServerTableName(paramJson);
                        if(Constants.EXECUTE_FAIL.equals(checkTable.getString("code"))){
                            return checkTable;
                        }else{//放入缓存。下次不用在校验
                            serverTable =  checkTable.getString("tableName");
                            paramJson.put("tableName",serverTable);
                            redisTemplate.opsForHash().put("upload_check_table", clientTableName,serverTable);
                        }
                    }else{
                        paramJson.put("tableName",serverTable);
                    }
                }
            }
            //2.数据校验完毕,返回结果,将数据发送消息中间件处理
            redisTemplate.convertAndSend("dataUploadChannel",paramArray);
            returnInfo.put("code", Constants.EXECUTE_SUCCESS);
            returnInfo.put("message", "upload success!");
        } catch (Exception e) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String format = dateFormat.format(date);
            //如果失败，则回滚
            returnInfo.put("code", Constants.EXECUTE_FAIL);
            returnInfo.put("message", "出错了，请检查上传数据格式或联系管理员！");
            logger.error("mts_" + format + ":" + e.getMessage());
            e.printStackTrace();
            return returnInfo;
        }
        return returnInfo;
    }

}
