package com.raising.rest;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;

import com.raising.backstage.service.ProjectService;
import com.raising.forward.service.PropertiesValue;
import com.raising.rest.sdk.utils.encoder.BASE64Decoder;
import com.raising.rest.service.DataUploadService;
import com.raising.rest.service.StoreService;


import org.apache.commons.codec.binary.Base64;
import org.glassfish.grizzly.http.util.HttpUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * 处理上传数据，ZeroConfig,DesignLine,Station等数据
 * 对数据库操作。
 */
@Component
@Path("/raising")
public class DataUpload {

    private static final Logger logger = LoggerFactory.getLogger(DataUpload.class);

    //失败code
    private static final int ERRCODE_FAIL = 500 ;
    @Autowired
    private DataUploadService dataUploadService;

    @Autowired
    private StoreService storeService;



    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ProjectService projectService;


    @Test
    public void test1() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("E:/test.dat"));
        while (true){
            String line = reader.readLine();
            if(line == null || "".equals(line)){
                break;
            }
            URL url = new URL("http://127.0.0.1:9999/jdt/rest/api/raising/upload");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(10000000);
            urlConnection.setReadTimeout(1000000);
            urlConnection.connect();


            if(!StringUtils.isNullOrEmpty(line)){
                PrintWriter printWriter = new PrintWriter(urlConnection.getOutputStream());
                printWriter.write(line);
                printWriter.flush();
            }
            InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream(),"utf-8");
            BufferedReader reader2 = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String temp = null;
            while( (temp=reader2.readLine()) != null){
                stringBuffer.append(temp);
            }
            System.out.println("----------"+temp);
            reader2.close();
            inputStreamReader.close();
            urlConnection.disconnect();
        }
    }



    /**
     * 处理数据上传接口
     * @param paramStr
     * @return
     */
    @POST
    @Path("/upload")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject upload(String paramStr) {
        JSONObject returnInfo = new JSONObject();//设置总的返回结果
        JSONArray paramArray = JSONArray.parseArray(paramStr);
        String projectName = null;
        Map<String,JSONObject> clientTableNamesMap = new HashMap<>();//用来做表明校验
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

    /**
     * 数据上传结果检验接口，获得查询表的列
     * @return
     */
    @POST
    @Path("/upload/test/getColumns")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject getColumns(JSONObject paramJson){
        JSONObject returnInfo = new JSONObject();
        String tableName = paramJson.getString("tableName");
        List<String> columns = dataUploadService.getColumns(tableName);
        String timeColumn = null;
        String tableTimeColumnStr = PropertiesValue.CLIENT_TABLE_TIMECOLUMN;
        JSONArray timeColumnsArr =  JSONArray.parseArray(tableTimeColumnStr);
        for(int i=0;i<timeColumnsArr.size();i++){
            JSONObject temp = timeColumnsArr.getJSONObject(i);
            if(tableName.equals(temp.getString("tableName"))){
                timeColumn = temp.getString("columnName");
            }
        }
        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        returnInfo.put("columns",columns);
        returnInfo.put("timeColumn",timeColumn);
        return returnInfo;
    }

    /**
     * 数据上传结果检验接口，根据条件，查询表中数据
     * @param ajaxParam
     * @return
     */
    @POST
    @Path("upload/test/getRows")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject getRows(String ajaxParam) throws UnsupportedEncodingException {
        String temp = URLDecoder.decode(ajaxParam, "UTF-8");
        temp = temp.substring(temp.indexOf("=")+1);
        JSONObject paramJson = JSONObject.parseObject(temp);
        JSONObject returnInfo = dataUploadService.getRows(paramJson);
        return returnInfo;
    }

    @POST
    @Path("/uploadTsdb")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject uploadTSDB(String paramStr){
        return null;
    }
    @POST
    @Path("/SB")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject update(@RequestParam("uploadFile") MultipartFile file)
    {
        System.out.println("a");
        return null;
    }
}
