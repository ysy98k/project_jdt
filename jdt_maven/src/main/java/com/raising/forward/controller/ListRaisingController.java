package com.raising.forward.controller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStreamReader;



import java.net.URL;

import java.net.URLConnection;






@Controller
@RequestMapping("/raising/")
public class ListRaisingController {



    @RequestMapping(value = "list.do")
    public String guidingPicture(Model model) {

        model.addAttribute("mileage1", "");
		/*model.addAttribute("mileage2",mso2.get("value"));
		model.addAttribute("mileage3",mso3.get("value"));
		model.addAttribute("mileage4",mso4.get("value"));*/

        return "/TBD/Section/frame_index";
    }
//发送get请求
    public static void get(String urlStr) throws Exception
    {

        URL url = new URL(urlStr);
        URLConnection urlConnection = url.openConnection(); // 打开连接

        System.out.println(urlConnection.getURL().toString());

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8")); // 获取输入流
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null)
        {
            sb.append(line + "\n");
        }
        br.close();
        System.out.println(sb.toString());
    }

//发送post请求
    public static void httpPostWithJSON(String url, String json)
    {
        // 创建默认的httpClient实例
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try
        {
            // 创建httppost
            HttpPost httppost = new HttpPost(url);

            httppost.addHeader("Content-type", "application/json; charset=utf-8");
            System.out.println("executing request " + httppost.getURI());

            // 向POST请求中添加消息实体
            StringEntity se = new StringEntity(json, "UTF-8");
            httppost.setEntity(se);
            System.out.println("request parameters " + json);

            // 执行post请求
            CloseableHttpResponse response = httpclient.execute(httppost);
            try
            {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                // 打印响应状态
                /*System.out.println(response.getStatusLine());*/
                if (entity != null)
                {
                    // 打印响应内容
                    System.out.println("--------------------------------------");
                    /*System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));*/
                    System.out.println("--------------------------------------");
                    HttpGet request = new HttpGet("http://124.74.252.133/cvscfgsvr/bpd?action=getShareLinkByParams&pagename=aHN0ejAwMDE%3D");
                    HttpResponse response2 = httpclient.execute(request);
                    if (response2.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        /**读取服务器返回过来的json字符串数据**/
                        String strResult = EntityUtils.toString(response2.getEntity());

                    }
                    /*URL url2 = new URL("http://124.74.252.133/cvscfgsvr/bpd?action=getShareLinkByParams&pagename=aHN0ejAwMDE%3D");
                    URLConnection urlConnection = url2.openConnection(); // 打开连接

                    System.out.println(urlConnection.getURL().toString());

                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8")); // 获取输入流
                    String line = null;
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    br.close();
                    System.out.println(sb.toString());*/
                    /*get("http://124.74.252.133/cvscfgsvr/bpd?action=getShareLinkByParams&pagename=aHN0ejAwMDE%3D");*/
                }
            }
            finally
            {
                response.close();
            }
        }
        catch (Exception e)
        {
            System.out.println("executing httpPostWithJSON error: " + e.getMessage());
        }
        finally
        {
            // 关闭连接,释放资源
            try
            {
                httpclient.close();
            }
            catch (IOException e)
            {
                System.out.println("executing httpPostWithJSON error: " + e.getMessage());
            }
        }
    }


}
