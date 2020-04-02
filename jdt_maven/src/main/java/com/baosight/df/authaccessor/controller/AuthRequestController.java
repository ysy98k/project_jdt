package com.baosight.df.authaccessor.controller;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.dao.RestDao;
import com.baosight.df.metamanage.entity.PageInfo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lusongkai on 2017/3/14.
 */
@Controller
@RequestMapping("/authaccessor/authrequest.do")
public class AuthRequestController {

    @Autowired
    public RestDao dao;

    @Value("${aas.host}")
    private String host;

    @Value("${aas.rest_service_name}")
    private String serviceName;

    protected SqlSessionTemplate sqlSessionTemplate;

    private String pageUrl;

    private String pageType;

    @RequestMapping(value = "")
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response){
        String str = null;
        try {
            HttpSession session = request.getSession(false);
            String pageName = request.getParameter("pageName");
            if(queryPageName(session,pageName) == true){
                if(queryUserAuth(session,pageName) == true){
                    session.setAttribute(pageName,true);
                    if(pageType.equals("externLink")){  //如果为外部链接的话
                        str = "redirect:http://"+request.getServerName()+":"+request.getServerPort()+pageUrl;
                    }else{
                        str = "redirect:"+pageUrl;
                    }
                }else{
                    str="/df/error/unauthorized";  //没有权限访问
                }
            }else{
                str="/df/error/resourceNotExist";      //没有对应的页面资源
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ModelAndView(str);
    }

    /*
    查询当前租户的pageManage表，判断当前页面资源是否存在，如果存在返回true
     */
    public boolean queryPageName(HttpSession session,String pageName){
        boolean result;
        PageInfo pageInfo = new PageInfo();
        String tenant = (String)session.getAttribute("tenant");
        pageInfo.setTenant(tenant);
        pageInfo.setPageEname(pageName);
        List<JSONObject> resultArr = new ArrayList<JSONObject>();
        resultArr =sqlSessionTemplate.selectList("PageManage.queryPrecise",pageInfo);
        if(resultArr.isEmpty()){
            result = false;
        }else{
            pageUrl = (String)resultArr.get(0).get("pagePath");
            pageType = (String)resultArr.get(0).get("pageType");
            result = true;
        }
        return result;
    }

    /*
    查询当前用户是否具有当前页面的浏览权限，如果有，返回true
     */
    public boolean queryUserAuth(HttpSession session,String pageName){
        boolean result;
        dao.setHost(host);
        dao.setServiceName(serviceName);
        JSONObject mapIn = new JSONObject();
        mapIn.put("token", session.getAttribute("token").toString());
        JSONObject returnInfo = dao.invoke("get", "/api/permission/JDT/" + pageName + "/get", mapIn);
        result = (boolean) returnInfo.get("permission");
        return result;
    }

    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    @Resource
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

}
