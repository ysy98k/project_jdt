package com.raising.forward.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class PropertiesValue {

    public static String TEMP = "124.74.252.133/aas";

    public static String AAS_ADRESS ;//192.168.0.10:83

    public static String AAS_APP_PATH;//aas

    public static String SERVICE_NAME;//JDT

    public static String CCS_ADRESS;//192.168.0.10:83

    public static String CCS_APP_PATH;//ccs

    public static String CVS_ADRESS;//192.168.0.10:83

    public static String CVS_CONFIG_SERVICE_NAME;//aasfgsvr

    public static String PDS_ADRESS;//10.25.10.12:8080

    public static String PDS_GATEWAY_HOST;//10.25.10.12:10006

    public static String AAS_LOGIN_PATH;//192.168.0.10:83/aas

    public static String AAS_LOGIN_NAME = "admin@raising";

    public static String AAS_LOGIN_PASSWORD = "admin";

    public static String AAS_API;//http://192.168.0.10:83/aas/api

    public static  String CCS_QUERY_ITEM;//http://192.168.0.10:83/ccs/api/query/item

    public static String CVS_PATH;//http://192.168.0.10:83/aasfgsvr

    public static String TSDB_SERVER_HOST;//192.168.1.88

    public static String TSDB_SERVER_PORT;//192.168.1.88

    public static String TSDB_APP_PATH;//tsdbcfgsvr

    public static String UPLOAD_FILE_PATH;// /raising/upload_file

    public static String TEMP_FILE_PATH;//admin账号密码

    public static String CLIENT_TABLE_HEADER;//数据上传表分类

    public static String CLIENT_TABLE_TIMECOLUMN;//数据上传表，时间列字段名。

    public static String ADMIN_PASSWORD;//admin账号密码

    public static String DATA_SYNC_TABLE_NAME;//数据同步表名，不包含dis mileage表






    @Value("${aas.host}")
    public void setAasAdress(String aasAdress) {
        PropertiesValue.AAS_ADRESS = aasAdress;
        if(PropertiesValue.AAS_APP_PATH != null){
            PropertiesValue.AAS_LOGIN_PATH = PropertiesValue.AAS_ADRESS + "/" + PropertiesValue.AAS_APP_PATH;
            PropertiesValue.AAS_API = "http://"+PropertiesValue.AAS_ADRESS + "/" + PropertiesValue.AAS_APP_PATH+ "/api";
        }
    }


    @Value("${aas.rest_service_name}")
    public void setAasAppPath(String aasAppPath) {
        PropertiesValue.AAS_APP_PATH = aasAppPath;
        if(PropertiesValue.AAS_ADRESS != null){
            PropertiesValue.AAS_LOGIN_PATH = PropertiesValue.AAS_ADRESS + "/" + PropertiesValue.AAS_APP_PATH;
            PropertiesValue.AAS_API = "http://"+PropertiesValue.AAS_ADRESS + "/" + PropertiesValue.AAS_APP_PATH+ "/api";
        }
    }


    @Value("${service.name}")
    public void setServiceName(String serviceName) {
        PropertiesValue.SERVICE_NAME = serviceName;
    }


    @Value("${ccs.host}")
    public void setCcsAdress(String ccsAdress) {
        PropertiesValue.CCS_ADRESS = ccsAdress;
        if(PropertiesValue.CCS_APP_PATH != null){
            PropertiesValue.CCS_QUERY_ITEM = "http://"+PropertiesValue.CCS_ADRESS + "/" + PropertiesValue.CCS_APP_PATH+"/api/query/item/";

        }
    }


    @Value("${ccs.rest_service_name}")
    public void setCcsAppPath(String ccsAppPath) {
        PropertiesValue.CCS_APP_PATH = ccsAppPath;
        if(PropertiesValue.CCS_ADRESS != null){
            PropertiesValue.CCS_QUERY_ITEM = "http://"+PropertiesValue.CCS_ADRESS + "/" + PropertiesValue.CCS_APP_PATH+"/api/query/item/";
        }
    }


    @Value("${cvs.host}")
    public void setCvsAdress(String cvsAdress) {
        PropertiesValue.CVS_ADRESS = cvsAdress;
        if(PropertiesValue.CVS_CONFIG_SERVICE_NAME != null){
            PropertiesValue.CVS_PATH = "http://"+PropertiesValue.CVS_ADRESS + "/"+ PropertiesValue.CVS_CONFIG_SERVICE_NAME;
        }
    }


    @Value("${cvs.config_service_name}")
    public void setCvsConfigName(String cvsConfigName) {
        PropertiesValue.CVS_CONFIG_SERVICE_NAME = cvsConfigName;
        if(PropertiesValue.CVS_ADRESS != null){
            PropertiesValue.CVS_PATH = "http://"+PropertiesValue.CVS_ADRESS + "/"+ PropertiesValue.CVS_CONFIG_SERVICE_NAME;
        }
    }


    @Value("${pds.host}")
    public void setPdsAdress(String pdsAdress) {
        PropertiesValue.PDS_ADRESS = pdsAdress;
    }


    @Value("${pds.gateway_host}")
    public void setPdsGatewayHost(String pdsGatewayHost) {
        PropertiesValue.PDS_GATEWAY_HOST = pdsGatewayHost;
    }

    @Value("${tsdb_server.host}")
    public  void setTsdbServerHost(String tsdbServerHost) {
        PropertiesValue.TSDB_SERVER_HOST = tsdbServerHost;
    }

    @Value("${tsdb_server.port}")
    public  void setTsdbServerPort(String tsdbServerPort) {
        PropertiesValue.TSDB_SERVER_PORT = tsdbServerPort;
    }

    @Value("${tsdb.rest_service_name}")
    public void setTsdbRestServiceName(String tsdbRestServiceName) {
        PropertiesValue.TSDB_APP_PATH = tsdbRestServiceName;

    }

    @Value("${raising_upload_file_path}")
    public void setUploadFilePath(String uploadFilePath){
        PropertiesValue.UPLOAD_FILE_PATH = uploadFilePath;
    }

    @Value("${raising_temp_file_path}")
    public void setTempFilePath(String tempFilePath){
        PropertiesValue.TEMP_FILE_PATH = tempFilePath;
    }

    @Value("${client_table_header}")
    public void setClientTableHeader(String clientTableHeader){
        PropertiesValue.CLIENT_TABLE_HEADER = clientTableHeader;
    }

    @Value("${cilent_table_timeColumn}")
    public void setClientTableTimeColumn(String clientTableTimeColumn){
        PropertiesValue.CLIENT_TABLE_TIMECOLUMN = clientTableTimeColumn;
    }

    @Value("${admin.password}")
    public void setAdminPassword(String adminPassword){
        PropertiesValue.ADMIN_PASSWORD = adminPassword.trim();
    }


    @Value("${data_sync_table_name}")
    public void setDataSyncTableName(String dataSyncTableName){
        PropertiesValue.DATA_SYNC_TABLE_NAME = dataSyncTableName;
    }

































}
