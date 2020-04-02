<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.baosight.common.message.XinsightResourceBundleMessageSource" %>
<%@ page import="com.baosight.common.SpringContextUtil" %>
<%@ page import="java.util.Locale" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>异常页面</title>
    <%
        XinsightResourceBundleMessageSource messageSource = SpringContextUtil.getBean("messageSource");
        String errCode = (String) request.getParameter("errCode");
        String detailMessage = new String(request.getParameter("detailMessage").getBytes("iso-8859-1"), "utf-8");
        Object[] detail = new Object[1];
        detail[0] = detailMessage;
        String errInfo = messageSource.getMessage(String.valueOf(errCode), detail, Locale.CHINESE);
    %>
</head>
<body>
<%@ include file="/bxui/bxuihead.jsp" %>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<div class="error-container">
    <div class="well">
        <h1 class="grey lighter smaller">
            <span class="blue bigger-125">
                <i class="ace-icon fa fa-random"></i>
                异常信息
            </span>
            错误码：<%=errCode%>
        </h1>

        <hr/>
        <h3 class="lighter smaller">
            错误信息：<%=errInfo%>
        </h3>

        <div class="space"></div>

        <hr/>
        <div class="space"></div>
    </div>
</div>

</body>
</html>

