package com.raising.mq;

import com.alibaba.fastjson.JSONObject;
import com.raising.forward.entity.tbmManage.TbmServiceInfo;
import com.raising.forward.service.tbmManage.FactoryService;
import com.raising.forward.service.tbmManage.FaultTreatService;
import com.raising.forward.service.tbmManage.StartManageService;
import com.raising.forward.service.tbmManage.TbmResumeService;
import com.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.*;

/**
 * 技术服务管理，文件上传
 */
public class TBMUploadFileSubscriber implements MessageListener {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private FactoryService factoryService;

    @Autowired
    protected TbmResumeService tbmResumeService;

    @Autowired
    private StartManageService startManageService;

    @Autowired
    private FaultTreatService faultTreatService;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        RedisSerializer<?> serializer = redisTemplate.getValueSerializer();
        JSONObject param = (JSONObject)serializer.deserialize(message.getBody());

        TbmServiceInfo tbmServiceInfo = param.getObject("tbmServiceInfo", TbmServiceInfo.class);
        String filePath = param.getString("filePath");
        String tbmName = param.getString("tbmName");
        String token = param.getString("token");
        String type = param.getString("type");
        File file = new File(filePath);
        if("factory".equals(type)){
            JSONObject returnInfo = factoryService.dataUpload(file,tbmName,token);
            if("0".equals(returnInfo.getString("status"))){
                FileUtils.deleteFolder(filePath);
                tbmResumeService.addRow(tbmServiceInfo);
            }
            factoryService.convertFileToPdf(tbmName);

        }else if("start".equals(type)){
            String directory = param.getString("directory");
            String secondLevel = param.getString("secondLevel");
            JSONObject returnInfo = startManageService.dataUpload(file,tbmName,directory,token);
            if("0".equals(returnInfo.getString("status"))){
                tbmResumeService.addRow(tbmServiceInfo);
            }
            startManageService.convertFileToPdf(tbmName,secondLevel);
        }else if("fault".equals(type)){
            String faultId = param.getString("faultId");
            JSONObject returnInfo = faultTreatService.dataUpload(file,tbmName,faultId);
            faultTreatService.convertFileToPdf(tbmName,faultId);
        }





    }
}
