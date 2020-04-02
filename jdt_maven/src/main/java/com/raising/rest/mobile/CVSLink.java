package com.raising.rest.mobile;


import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;


import com.raising.ccs.ResourceService;
import com.raising.forward.service.PropertiesValue;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.io.IOException;
import java.util.List;


/**
 * 获取公共CVS链接接口
 */
@Component
@Path("/raising")
public class CVSLink {

    private static final Logger logger = LoggerFactory.getLogger(CVSLink.class);
    //租户名称
    private static final String TENANT_NAME = "raising";


    @Autowired
    protected SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    protected ResourceService resourceService;

    /**
     *获取公共CVS链接方法
     * 入参token,group,modename,section_id
     * @param paramJson
     * @return
     */
    @POST
    @Path("/cvslink")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject getCVSLink(JSONObject paramJson){
        //返回值
        JSONObject returnInfo = new JSONObject();
        //判断权限
        String token = paramJson.getString("token");
        String group = paramJson.getString("group");
        Integer projectId = paramJson.getInteger("project_id");
        boolean checkResult = checkAuthority(token,group,projectId);
        if(checkResult == false){
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("message","return fail！");
            return returnInfo;
        }

        //创建连接实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try{
            //先登录
            String loginUrl = PropertiesValue.CVS_PATH + "/bpd?action=login&lessee=raising&username=admin&password=admin";
            HttpPost loginPost = new HttpPost(loginUrl);
            loginPost.addHeader("Content-type","application/json;charset=utf-8");
            RequestConfig loginRequestConfig =
                    RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
            loginPost.setConfig(loginRequestConfig);

            CloseableHttpResponse loginResponse = httpClient.execute(loginPost);
            try{
                if(loginResponse.getEntity() != null){
                    //如果登录成功，则获取cvs连接
                    String cvsUrl =  PropertiesValue.CVS_PATH + "/bpd?action=getShareLinkByParams";
                    StringBuffer sb = new StringBuffer(cvsUrl);
                    String modeName = paramJson.getString("modename");
                    String pageNme = Base64.encodeBase64String(modeName.getBytes());
                    sb.append("&pagename="+pageNme);

                    HttpGet cvsGet = new HttpGet(sb.toString());
                    RequestConfig cvdRequestConfig =
                            RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
                    HttpResponse cvsResponse = httpClient.execute(cvsGet);
                    if(cvsResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                        String cvsReslut = EntityUtils.toString(cvsResponse.getEntity(),"utf-8");
                        JSONObject cvsJson = JSONObject.parseObject(cvsReslut);
                        JSONObject urlJson = cvsJson.getJSONObject("msg");
                        String temp = urlJson.getString(modeName);
                        String s = temp.replaceFirst("http", "https");
                        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
                        returnInfo.put("url",s);
                        return  returnInfo;
                    }
                }
            }finally {
                loginResponse.close();
            }

        }catch (Exception e){
            logger.error(e.getMessage());
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("message","return fail！");
            return returnInfo;
        }finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        returnInfo.put("status",Constants.EXECUTE_FAIL);
        returnInfo.put("message","return fail！");
        return returnInfo;
    }


    @GET
    @Path("/cvsPage")
    @Produces(MediaType.APPLICATION_JSON)
    public ModelAndView frame(@QueryParam("collectorName") String collectorName,@QueryParam("token") String token,@QueryParam("group") String group,@QueryParam("project_id") Integer projectId) {
        JSONObject returnInfo = new JSONObject();
        //判断权限

//        boolean checkResult = checkAuthority(token,group,projectId);
//        if(checkResult == false){
//            returnInfo.put("status",Constants.EXECUTE_FAIL);
//            returnInfo.put("message","return fail！");
//            return returnInfo;
//        }

        //创建连接实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try{
            //先登录
            String loginUrl = PropertiesValue.CVS_PATH + "/bpd?action=login&lessee=raising&username=admin&password=admin";
            HttpPost loginPost = new HttpPost(loginUrl);
            loginPost.addHeader("Content-type","application/json;charset=utf-8");
            RequestConfig loginRequestConfig =
                    RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
            loginPost.setConfig(loginRequestConfig);

            CloseableHttpResponse loginResponse = httpClient.execute(loginPost);
            try{
                if(loginResponse.getEntity() != null){
                    //如果登录成功，则获取cvs连接
                    String cvsUrl =  PropertiesValue.CVS_PATH + "/bpd?action=getShareLinkByParams";
                    StringBuffer sb = new StringBuffer(cvsUrl);
                    String modeName ="mobileTmpl";
                    String pageNme = Base64.encodeBase64String(modeName.getBytes());
                    sb.append("&pagename="+pageNme);

                    HttpGet cvsGet = new HttpGet(sb.toString());
                    RequestConfig cvdRequestConfig =
                            RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
                    HttpResponse cvsResponse = httpClient.execute(cvsGet);
                    if(cvsResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                        String cvsReslut = EntityUtils.toString(cvsResponse.getEntity(),"utf-8");
                        JSONObject cvsJson = JSONObject.parseObject(cvsReslut);
                        JSONObject urlJson = cvsJson.getJSONObject("msg");
                        String temp = urlJson.getString(modeName);
                        String s = temp.replaceFirst("http", "https");
//                        map.addAttribute("cvsurl",s);
//                        map.addAttribute("collectorName",collectorName);
                        //resp.sendRedirect("http://baidu.com/downloadRequestElecCont.action?contNo="+contNo);
//                        //req.getRequestDispatcher("/raising/forward/cvsPage").forward(req, resp);
                    }
                }
            }finally {
                loginResponse.close();
            }

        }catch (Exception e){
            logger.error(e.getMessage());
//        }finally {
//            try {
//                httpClient.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

         ModelAndView mv = new ModelAndView("cvsPage");
        return mv;
    }

    /**
     * 判断有无权限
     *  查找该组下是否有sectionId对应的采集器名称，有则返回true
     */
    private boolean checkAuthority(String token,String groupName,Integer projectId){
        //根据secionId获得采集器名字
        String collectorName = getCollectorName(projectId);
        if(collectorName == null){
            return false;
        }
        //获取资源
        List<String> collectorNameResouces = resourceService.getCollectionNames(groupName);
        if(collectorNameResouces.contains(collectorName)){
            return true;
        }
        return false;
    }

    /**
     * 得到采集器名称
     * @param projectId
     * @return
     */
    private String getCollectorName(Integer projectId){
        JSONObject paramJson = new JSONObject();
        paramJson.put("projectId",projectId);
        paramJson.put("tenant",TENANT_NAME);
        String sql = "com.raising.forward.mapper.ProjectInfoMapper.getRows";
        List<JSONObject> list = this.sqlSessionTemplate.selectList(sql, paramJson);
        if(!(list.size() > 0)){
            return  null;
        }else{
            return list.get(0).getString("collectorName");
        }
    }


}
