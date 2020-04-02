<%@ page import="com.baosight.aas.auth.Constants" %>
<%@ page import="java.net.URLEncoder" %><%--
  Created by IntelliJ IDEA.
  User: ysy
  Date: 2018/10/25
  Time: 16:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <%
        String path = request.getContextPath();
        //loginName:name@test
        String loginName=(String)request.getSession().getAttribute("loginName");
        String userName = (String)request.getSession().getAttribute(Constants.SESSION_USERNAME_KEY);
        String tenant = (String)request.getSession().getAttribute(Constants.SESSION_TENANT_KEY);
        String groupDisplayNames = (String)request.getSession().getAttribute(Constants.SESSION_GROUPDISPLAYNAME_KEY);
        String username=(String)request.getSession().getAttribute("username");
        String userid=request.getSession().getAttribute("userId") + "";

        String token = (String)request.getSession().getAttribute("token");

    %>
    <style>
        body{
             margin: 0px;
         }
        iframe{
            border-width:0px;
        }
    </style>
</head>

<body>
<%--<%@ include file="/bxui/bxuihead.jsp"%>--%>
<script type="text/javascript" src="<%=path%>/bxui/jquery.min.js"></script>
<script type="text/javascript">
    function cvsSysLogin(username, lessee, password) {
        var url = "https://bd.sh-raising.com/cvscfgsvr/bpd?action=sysLogin&lessee=" + lessee + "&username=" + username +"&password=" + password;
        $.ajax({
            url: url,
            async: false,
            type: "POST",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function(result) {
                console.log("get cvs main.html success");
                sessionStorage.setItem("getMainHtml",1);
            },
            error: function(xhr, status, error) {
                console.log("get cvs main.html failed");
                sessionStorage.setItem("getMainHtml",0)
            }
        });
    }

    cvsSysLogin("admin","raising","admin");
</script>
<%--<iframe id="temp" src="https://bd.sh-raising.com/cvscfgsvr/console/main.html?token=<%=token%>" class="hide"></iframe>--%>
<%

    Cookie cookie = new Cookie("selected_collection", URLEncoder.encode(request.getAttribute("collectorName").toString(), "UTF-8"));  // 创建cookie对象// 创建cookie对象
    Cookie cookie1 = new Cookie("totalLength", URLEncoder.encode(request.getAttribute("totalLength").toString(), "UTF-8"));
    Cookie cookie2 = new Cookie("totalMileage", URLEncoder.encode(request.getAttribute("totalMileage").toString(), "UTF-8"));
    Cookie cookie3 = new Cookie("selected_name", URLEncoder.encode(request.getAttribute("collectorName").toString(), "UTF-8"));
    Cookie cookie4 = new Cookie("ringTotal", URLEncoder.encode(request.getAttribute("ringTotal").toString(), "UTF-8"));
    Cookie cookie5 = new Cookie("tbmName", URLEncoder.encode(request.getAttribute("tbmName").toString(), "UTF-8"));
    Cookie cookie6 = new Cookie("modeName", URLEncoder.encode(request.getAttribute("modeName").toString(), "UTF-8"));
    Cookie cookie7 = new Cookie("selected_name", URLEncoder.encode(request.getAttribute("selected_name").toString(), "UTF-8"));





    cookie.setMaxAge(60*60*24);                     // 保存一天，单位秒
    cookie.setPath("/");
    cookie1.setMaxAge(60*60*24);                     // 保存一天，单位秒
    cookie1.setPath("/");
    cookie2.setMaxAge(60*60*24);                     // 保存一天，单位秒
    cookie2.setPath("/");
    cookie3.setMaxAge(60*60*24);                     // 保存一天，单位秒
    cookie3.setPath("/");
    cookie4.setMaxAge(60*60*24);                     // 保存一天，单位秒
    cookie4.setPath("/");
    cookie5.setMaxAge(60*60*24);                     // 保存一天，单位秒
    cookie5.setPath("/");
    cookie6.setMaxAge(60*60*24);                     // 保存一天，单位秒
    cookie6.setPath("/");
    cookie7.setMaxAge(60*60*24);                     // 保存一天，单位秒
    cookie7.setPath("/");
    // 注意是通过response对象的addCookie方法设置cookie
    response.addCookie(cookie);
    response.addCookie(cookie1);
    response.addCookie(cookie2);
    //response.addCookie(cookie3);
    response.addCookie(cookie4);
    response.addCookie(cookie5);
    response.addCookie(cookie6);
    response.addCookie(cookie7);
    // 添加cookie
%>
<iframe class="c" src="<%=request.getAttribute("cvsurl")%>" srcolling="no" style="height: 1600px;width: 100%">
<%-- 取出所有cookie --%>
<%--<%--%>
    <%--// 注意是通过request对象进行取得，返回的是一个Cookie数组--%>
    <%--Cookie[] cookies = request.getCookies();        // 获取所有的cookie--%>

    <%--// 取出所有的cookie--%>
    <%--// 通过cookie的getName和getValue方法--%>
    <%--for (int i = 0; i < cookies.length; i++) {--%>
<%--%>--%>
<%--<%=cookies[i].getName() + "-->" + cookies[i].getValue()%>--%>
<%--<%--%>
    <%--}--%>
<%--%>--%>

</body>

</html>
