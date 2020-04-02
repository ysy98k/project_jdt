package com.baosight.df.metamanage.service;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.entity.BaseEntity;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lusongkai on 2017/12/6.
 */
@Service
@Transactional
public class MediaManageService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(MediaManageService.class);

    @Autowired
    private HttpServletRequest request;

    public JSONObject query(JSONObject mapIn){
        JSONObject inquStatus = mapIn.getJSONObject("inqu_status");
        mapIn.put("inqu_status", inquStatus);
        mapIn.put(BaseService.QUERY_BLOCK, "inqu_status");
        mapIn.put(BaseService.QUERY_SQL, "MediaManage.query");
        mapIn.put(BaseService.COUNT_SQL, "MediaManage.count");
        mapIn.put(BaseService.DAO_ENTITY,
                "com.baosight.df.metamanage.entity.MediaManage");
        JSONObject returnInfo = super.query(mapIn);
        return returnInfo;
    }

    public JSONObject insert(JSONObject mapIn){
        JSONObject inquStatus = mapIn.getJSONObject("inqu_status");
        mapIn.put("inqu_status", inquStatus);
        mapIn.put(BaseService.QUERY_BLOCK, "inqu_status");
        mapIn.put(BaseService.INSERT_SQL, "MediaManage.insert");
        mapIn.put(BaseService.RESULT_BLOCK, "detail");
        mapIn.put(BaseService.DAO_ENTITY,
                "com.baosight.df.metamanage.entity.MediaManage");
        JSONObject returnInfo = super.insert(mapIn);
        return returnInfo;
    }

    public JSONObject upload(MultipartFile file,JSONObject mapIn) throws Exception{
        String type = mapIn.getString("type");
        String id = mapIn.getString("id");
        String name = mapIn.getString("name");
        name = new String(name.getBytes("iso8859-1"), "utf-8");
        JSONObject returnObj = new JSONObject();
        String dir = System.getProperty("catalina.home").replace("\\", File.separator);
        String tenantId = (String)request.getSession().getAttribute("tenant");
        String dirPicture = dir+File.separator+"wls"+File.separator+"mediamanage"+File.separator+"picture"+File.separator+tenantId;
        String dirAudio = dir+File.separator+"wls"+File.separator+"mediamanage"+File.separator+"audio"+File.separator+tenantId;
        String dirVideo = dir+File.separator+"wls"+File.separator+"mediamanage"+File.separator+"video"+File.separator+tenantId;
        String fileName = file.getOriginalFilename();
        String prefix=fileName.substring(fileName.lastIndexOf(".")).toLowerCase(); //带点的后缀名
        if(type.equals("picture")){
            returnObj = saveFiles(file,dirPicture,id,name,prefix);
        }else if(type.equals("audio")){
            returnObj = saveFiles(file,dirAudio,id,name,prefix);
        }else if(type.equals("video")){
            returnObj = saveFiles(file,dirVideo,id,name,prefix);
        }
        return returnObj;
    }

    public JSONObject saveFiles(MultipartFile file,String dirName,String id,String name,String suffixName) throws Exception{
        String result = createDir(dirName);                      //创建picture，audio和video三个文件夹
        if(result.equals("alreadyExist")){
            createDir(dirName+File.separator+name);              //创建资源名文件夹
            String resourceName = name+suffixName;
            String tenantId = (String)request.getSession().getAttribute("tenant");
            emptyFile(dirName+File.separator+name);
            String path = dirName+File.separator+name+File.separator+resourceName;
            InputStream fileInputStream = file.getInputStream();
            OutputStream outputStream = new FileOutputStream(path);
            byte[] bytes = new byte[1024];
            while((fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes);
            }
            fileInputStream.close();
            outputStream.close();
            return uploadUpdate(tenantId+File.separator+name + File.separator + resourceName,id);
        }else if(result.equals("success"))
            return saveFiles(file,dirName,id,name,suffixName);
        else {
            JSONObject returnObj = new JSONObject();
            returnObj.put("returnMsg", "初始化资源目录失败，请检查后重新操作！");
            returnObj.put("status", Constants.EXECUTE_FAIL);
            return returnObj;
        }
    }

    public JSONObject uploadUpdate(String path,String id) throws Exception{
        JSONObject returnObj = new JSONObject();
        String insertSql = "MediaManage.uploadUpdate";
        String classStr = "com.baosight.df.metamanage.entity.MediaManage";
        BaseEntity daoEntity;
        try {
            Class classObj = Class.forName(classStr);
            daoEntity = (BaseEntity) classObj.newInstance();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            daoEntity = new BaseEntity();
        }
        Map map = new HashMap<>();
        map.put("mediaId",id);
        map.put("mediaPath",path);
        map.put("tenant", request.getSession().getAttribute(Constants.SESSION_TENANT_KEY));
        daoEntity.fromMap(map);
        sqlSessionTemplate.insert(insertSql, daoEntity);
        JSONObject returnInfo = returnObj;
        returnInfo.put("status", Constants.EXECUTE_SUCCESS);
        returnInfo.put("returnMsg", "成功上传了一个资源！");
        return returnInfo;
    }

    public JSONObject delete(JSONObject mapIn) {
        mapIn.put(BaseService.QUERY_BLOCK, "inqu_status");
        JSONObject inquStatus = mapIn.getJSONObject("inqu_status");
        mapIn.put("inqu_status", inquStatus);
        mapIn.put(BaseService.DELETE_SQL, "MediaManage.delete");
        mapIn.put(BaseService.RESULT_BLOCK, "result");
        mapIn.put(BaseService.DAO_ENTITY,
                "com.baosight.df.metamanage.entity.MediaManage");
        JSONObject returnInfo = super.delete(mapIn);
        JSONObject resultBlock = (JSONObject) mapIn.get("result");
        List deleteRows = resultBlock.getJSONArray(RESULT_Row);
        String dir = System.getProperty("catalina.home").replace("\\", File.separator);
        String tenantId = (String)request.getSession().getAttribute("tenant");
        String dirPicture = dir+File.separator+"wls"+File.separator+"mediamanage"+File.separator+"picture"+File.separator+tenantId;
        String dirAudio = dir+File.separator+"wls"+File.separator+"mediamanage"+File.separator+"audio"+File.separator+tenantId;
        String dirVideo = dir+File.separator+"wls"+File.separator+"mediamanage"+File.separator+"video"+File.separator+tenantId;
        for(int i=0;i<deleteRows.size();i++){
            JSONObject tempRow = (JSONObject) deleteRows.get(i);
            String mediaName = tempRow.getString("mediaName");
            String mediaType = tempRow.getString("mediaType");
            if(mediaType.equals("picture"))
                deleteDir(dirPicture + File.separator + mediaName);
            else if(mediaType.equals("audio"))
                deleteDir(dirAudio+File.separator+mediaName);
            else if(mediaType.equals("video"))
                deleteDir(dirVideo+File.separator+mediaName);
        }
        return returnInfo;
    }

    public JSONObject update(JSONObject mapIn) {
        mapIn.put(BaseService.QUERY_BLOCK, "inqu_status");
        JSONObject inquStatus = mapIn.getJSONObject("inqu_status");
        mapIn.put("inqu_status", inquStatus);
        mapIn.put(BaseService.UPDATE_SQL, "MediaManage.update");
        mapIn.put(BaseService.RESULT_BLOCK, "detail");
        mapIn.put(BaseService.DAO_ENTITY,
                "com.baosight.df.metamanage.entity.MediaManage");
        JSONObject returnInfo = super.update(mapIn);
        return returnInfo;
    }

    public String createDir(String dirName){
        File dir = new File(dirName);
        if (dir.exists()) {
            logger.info("创建目录" + dirName + "失败，目标目录已经存在");
            return "alreadyExist";
        }
        if (!dirName.endsWith(File.separator)) {
            dirName = dirName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
            return "success";
        } else {
            logger.info("创建目录" + dirName + "失败！");
            return "failure";
        }
    }

    /**
     * 删除整个资源名目录
     * @param dirName
     * @return
     */
    public boolean deleteDir(String dirName){
        boolean flag = true;
        File file = new File(dirName);
        if (!file.exists()) {
            flag = false;
            return flag;
        }
        if (!file.isDirectory()) {
            flag = false;
            return flag;
        }
        emptyFile(dirName);
        file.delete();//最后删除空文件夹
        return flag;
    }

    /**
     * 清空文件夹
     */
    public void emptyFile(String dirName){
        File file = new File(dirName);
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (dirName.endsWith(File.separator)) {
                temp = new File(dirName + tempList[i]);
            } else {
                temp = new File(dirName + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                deleteDir(dirName + File.separator + tempList[i]);//先删除文件夹里面的文件
                File myFilePath = new File(dirName + File.separator + tempList[i]);//再删除空文件夹
                myFilePath.delete();
            }
        }
    }




}
