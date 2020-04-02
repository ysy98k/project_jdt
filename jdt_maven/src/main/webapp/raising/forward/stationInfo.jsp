<%--
  Created by IntelliJ IDEA.
  User: ysy
  Date: 2018/6/1
  Time: 10:29
  Description: 
--%>
<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="com.baosight.aas.auth.Constants" %>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <title>系统设置</title>
    <%
        String path = request.getContextPath();
        String tenantid = request.getSession().getAttribute(Constants.SESSION_TENANTID_KEY) + "";
        String first = java.net.URLDecoder.decode(request.getParameter("firstLevel"),"UTF-8");
        String second = java.net.URLDecoder.decode(request.getParameter("secondLevel"),"UTF-8");
    %>

    <meta name="description" content="platForm Frameset"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
</head>

<body>
<%@ include file="/bxui/bxuihead.jsp" %>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/raphael.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.mousewheel.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=path%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/selectLine.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/stationInfo.js"></script>
<input type="hidden" value="<%=tenantid%>" id="tenantid">
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css"/>
<div class="page-content" style="position:inherit;min-height:100%">
    <div class="page-header">
        <h5>
            <%=first%>
            <small>
                &nbsp;
                <i class="ace-icon fa fa-angle-double-right"></i>
                &nbsp;&nbsp;
                <%=second%>
            </small>
        </h5>
        <div style="float: right;margin-top: -20px">
            <a href="javascript:insertPage()" ><span id = "cityname"></span><span id="linemessage"></span><span id="programmessage"></span></a>
        </div>
    </div><!-- /.page-header -->
    <div class="row">
        <div class="col-md-12">
            <div id="gridImage" style="height: 100%"></div>
        </div>
    </div>
</div>
<div id="detail" class="hide" style="overflow: hidden">
    <div type="text" id = "div_city" name = "city" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
    <div type="text" id = "div_line" name = "line" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
    <div type="text" id = "div_program" name = "program" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
</div>
</body>
</html>
