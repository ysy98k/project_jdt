package com.raising.forward.controller;

import com.alibaba.fastjson.JSONObject;
import com.raising.forward.service.PropertiesValue;
import com.raising.forward.service.TemplateService;
import com.util.BtoaEncode;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLDecoder;
import java.net.URLEncoder;


@Controller
@RequestMapping("/raising/forward")
public class CvsPageControrller {
    @Autowired
    private BtoaEncode btoaEncode;

    @Autowired
    private TemplateService templateService;

    @RequestMapping(value = "cvsPage.do")
    public ModelAndView frame(@RequestParam("collectorName") String collectorName, ModelMap map) {
//        CloseableHttpClient httpClient = HttpClients.createDefault();
        try{

            JSONObject template = templateService.getTemplateName(collectorName);

           // String pageName = "mobileTmpl";
            String pageName2 = template.getString("templateName");
            String totalLength = template.getString("totalLength");

            String pageName = pageName2.substring(9,pageName2.length())+"_ztxt";
            String modeTag = "template";
            String encodeName = btoaEncode.botaEncodePassword(URLEncoder.encode(pageName, "UTF-8"));
            String replaceTagsVal = modeTag+"@@"+collectorName+"@@"+pageName;
            replaceTagsVal = "\""+replaceTagsVal+"\"";
            String encodeTg = btoaEncode.botaEncodePassword(URLEncoder.encode(replaceTagsVal));

            String url = "https://bd.sh-raising.com/cvscfgsvr/bpd?action=loadPage&pagename=" + encodeName + "&replaceTags=" + encodeTg;
            map.addAttribute("cvsurl", url);
            map.addAttribute("collectorName",collectorName);
            map.addAttribute("totalLength",totalLength);
            map.addAttribute("totalMileage",template.getString("totalMileage"));
            map.addAttribute("selected_name",template.getString("selected_name"));
            map.addAttribute("ringTotal",template.getString("ringTotal"));
            map.addAttribute("tbmName",template.getString("tbmName"));
            map.addAttribute("modeName",pageName2.substring(9,pageName2.length()));

            //先登录
//            String loginUrl = PropertiesValue.CVS_PATH + "/bpd?action=login&lessee=raising&username=admin&password=admin";
//            HttpPost loginPost = new HttpPost(loginUrl);
//            loginPost.addHeader("Content-type","application/json;charset=utf-8");
//            RequestConfig loginRequestConfig =
//                    RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
//            loginPost.setConfig(loginRequestConfig);
//
//            CloseableHttpResponse loginResponse = httpClient.execute(loginPost);
            try{
//                if(loginResponse.getEntity() != null){
//                    //如果登录成功，则获取cvs连接
//                    String cvsUrl =  PropertiesValue.CVS_PATH + "/bpd?action=getShareLinkByParams";
//                    StringBuffer sb = new StringBuffer(cvsUrl);
//                    String modeName ="mobileTmpl";
//                    String pageNme = Base64.encodeBase64String(modeName.getBytes());
//                    sb.append("&pagename="+pageNme);
//
//                    HttpGet cvsGet = new HttpGet(sb.toString());
//                    RequestConfig cvdRequestConfig =
//                            RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
//                    HttpResponse cvsResponse = httpClient.execute(cvsGet);
//                    if(cvsResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
//                        String cvsReslut = EntityUtils.toString(cvsResponse.getEntity(),"utf-8");
//                        JSONObject cvsJson = JSONObject.parseObject(cvsReslut);
//                        JSONObject urlJson = cvsJson.getJSONObject("msg");
//                        String temp = urlJson.getString(modeName);
//                        String s = temp.replaceFirst("http", "https");
//                        map.addAttribute("cvsurl",s);
//                        map.addAttribute("collectorName",collectorName);
//                        //resp.sendRedirect("http://baidu.com/downloadRequestElecCont.action?contNo="+contNo);
////                        //req.getRequestDispatcher("/raising/forward/cvsPage").forward(req, resp);
//                    }
//                }
            }finally {
//                loginResponse.close();
            }

        }catch (Exception e){
            e.printStackTrace();
          //  logger.error(e.getMessage());
//        }finally {
//            try {
//                httpClient.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }


//        map.addAttribute("cvsurl", "");
//        map.addAttribute("collectorName", "abc");

        return new ModelAndView("cvsPage");
    }


}
