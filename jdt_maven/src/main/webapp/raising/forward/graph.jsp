<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="com.baosight.aas.auth.Constants" %>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <title>掘进监控</title>
    <%
        String path = request.getContextPath();
        String tenantid = request.getSession().getAttribute(Constants.SESSION_TENANTID_KEY) + "";
    %>

    <meta name="description" content="platForm Frameset"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>

</head>

<body>
<%@ include file="/bxui/bxuihead.jsp" %>
<%--<script type="text/javascript" src="<%=path%>/bxui/baosight-require.js"></script>--%>
<input type="hidden" value="<%=tenantid%>" id="tenantid">
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css"/>
<div class="page-content" style="position:inherit;min-height:600px">
    <!-- /sectionController:settings.box -->
    <div class="page-header">
        <h1>
            监测管理
            <small>
                <i class="ace-icon fa fa-angle-double-right"></i>
                图表
            </small>
        </h1>
        <div style="float: right;margin-top: -20px">
            <a href="javascript:insertPage()"><span id="cityname"></span><span id="linemessage"></span><span
                    id="programmessage"></span></a>
        </div>
    </div><!-- /.page-header -->

    <iframe id="cvsPage" width="100%" style="min-height: 790px" src="">
    </iframe>

    <div id="detail" class="hide" style="overflow: hidden">
        <div type="text" id="div_city" name="city" data-bxwidget="bxcombobox" data-bxtype="string"
             style="margin:15px 10px 15px 10px">
        </div>
        <div type="text" id="div_line" name="line" data-bxwidget="bxcombobox" data-bxtype="string"
             style="margin:15px 10px 15px 10px">
        </div>
        <div type="text" id="div_program" name="program" data-bxwidget="bxcombobox" data-bxtype="string"
             style="margin:15px 10px 15px 10px">
        </div>
    </div>
</div>

<!-- /.page-content -->

<!-- ----------------------------------信息弹出框---------------------------------------------------------->
<div id="dialog-message" class="hide">
    <p class="bigger-110 bolder center grey">
        <br/>
        <b id="dialogInfo"></b>
    </p>
</div>
</body>

<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-form.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxdialog.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxcombobox.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/loadsave.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/selectLine.js"></script>
<script type="text/javascript">
    var getCvsUrl = {
        onSuccess: function (dataJson) {
            if (dataJson.status == "0") {
                if (dataJson.cvsUrl == null || dataJson.cvsUrl == "") {
                    $("#cvsPage").attr("src", toolkitPath + "/404.jsp");
                } else {
                    $("#cvsPage").attr("src", dataJson.cvsUrl);
                }
            } else {
                $("#cvsPage").attr("src", toolkitPath + "/404.jsp");
            }
        }
    };
    var collectorName = $.cookie('selected_collection');
    var ajaxParam = {"collectorName": collectorName};
    AjaxCommunicator.ajaxRequest('/raising/forward/sectionSetting.do?method=getCvsUrl', 'GET', ajaxParam, getCvsUrl,true);
</script>


</html>
