<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="com.baosight.aas.auth.Constants" %>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <title>统计分析</title>
    <%
        String path = request.getContextPath();
        String tenantid = request.getSession().getAttribute(Constants.SESSION_TENANTID_KEY) + "";
        String first = java.net.URLDecoder.decode(request.getParameter("firstLevel"),"UTF-8");
        String second = java.net.URLDecoder.decode(request.getParameter("secondLevel"),"UTF-8");
    %>

    <meta name="description" content="platForm Frameset"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <style>
        body{
            position:inherit;
            min-height:600px;
            background-color: white!important;
        }
    </style>
</head>

<body>
<%@ include file="/bxui/bxuihead.jsp" %>
<%--<script type="text/javascript" src="<%=path%>/bxui/baosight-require.js"></script>--%>
<input type="hidden" value="<%=tenantid%>" id="tenantid">
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css"/>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.form.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/raphael.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.mousewheel.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/selectLine.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/lineData.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-form.js"></script>

<link rel="stylesheet" href="<%=path%>/bxui/daterangepicker/css/daterangepicker.css"/>
<script type="text/javascript" src="<%=path%>/bxui/daterangepicker/js/daterangepicker.js"></script>


<script type="text/javascript" src="<%=toolkitPath%>/bxui/aceadmin/js/ace-elements.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/easyui-plugin/js/jquery.easyui.min.js"></script>


<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<!--
<script type="text/javascript" src="<%=path%>/bxui/daterangepicker/js/moment.min.js"></script>
-->
<script type="text/javascript" src="<%=toolkitPath%>/bxui/highcharts/highstock.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/highcharts/exporting.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/highcharts/highcharts-zh_CN.js"></script>


<script type="text/javascript" src="summaryInfo.js"></script>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/easyui-plugin/css/easyui.css"/>
<link href="<%=toolkitPath%>/bxui/aceadmin/style/css/font-awesome.css" rel="stylesheet" type="text/css">


<div class="page-content">
    <!-- /sectionController:settings.box -->
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
            <a href="javascript:insertPage()"><span id="cityname"></span><span id="linemessage"></span><span
                    id="programmessage"></span></a>
        </div>
    </div><!-- /.page-header -->



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

    <!--  内容   -->
    <!--  折线图弹出框   -->

    <div class="col-md-8 col-md-offset-2 widget-container-col ui-sortable" id="lineDataGraph"
         style="position: absolute; display: none;z-index:1050;">
        <!-- #sectionController:custom/widget-box -->
        <div class="widget-box ui-sortable-handle">
            <div class="widget-header">
                <h5 class="widget-title"></h5>

                <!-- #sectionController:custom/widget-box.toolbar -->
                <div class="widget-toolbar">
                    <a href="#" onclick="hideBootBox()">
                        <i class="ace-icon fa fa-times"></i>
                    </a>
                </div>

                <!-- /sectionController:custom/widget-box.toolbar -->
            </div>
            <!-- 折线图表 -->
            <div class="widget-body">
                <div class="input-group" style="margin-top: 2px; margin-right:2px; float: right">
                    <button type="button" class="btn btn-default pull-right" id="daterange-btn">
                        <i class="fa fa-calendar"></i>
                        <span>时间</span>
                        <i class="fa fa-caret-down"></i>
                    </button>
                </div>
                <div class="widget-main">
                    <div id="container"></div>
                </div>
            </div>
        </div>
        <!-- /sectionController:custom/widget-box -->
    </div>
    <div class="ui-widget-overlay ui-front" id="lineDataGraphGround" style="z-index: 1049; display: none;"></div>

    <!--  内容   -->
    <div style="margin-left: 1px;">
        <div class="alert alert-danger" id="errorAlert" style="display: none">
            <button type="button" class="close" onclick="hideMessage()">
                <i class="ace-icon fa fa-times"></i>
            </button>

            <strong id="message">
            </strong>
        </div>
        <div style="display: inline-block;">
            <button class="btn btn-primary" onclick="makeGraph()">生成图表</button>
        </div>
        &nbsp;
        <div style="display: inline-block;">
            <button class="btn btn-sm btn-warning" onclick="clearCheckBox()">清空</button>
        </div>
        <div style="display: inline-block;">
            &nbsp;&nbsp;
            <input name="form-field-radio" class="ace" type="radio" checked="checked" value="cumulativeVariation">
            <span class="lbl">&nbsp;&nbsp;累计变化量</span>&nbsp;&nbsp;
            <input name="form-field-radio" class="ace" type="radio" value="changeRate">
            <span class="lbl">&nbsp;&nbsp;变化速率</span>
        </div>
    </div>
    <div>
        <div class="tabbable" id="sheetTable" style="margin-top: 10px;">
            <ul class="nav nav-tabs padding-12 tab-color-blue background-blue" id="myTab">
            </ul>
            <div class="tab-content" id="myTabinfo"></div>
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
</html>
