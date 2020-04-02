<%--
  Created by IntelliJ IDEA.
  User: ysy
  Date: 2018/8/8
  Time: 11:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.baosight.aas.auth.Constants" %>
<html>
<head>
    <title>保养记录</title>
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
        .bai{
            background-color: rgb(255, 255, 255);
        }
        .labelStyle{
            width: 30%;text-align: right;
            margin-right: 10px;
        }
        .divStyle{
            width:60%;text-align: left;;display: inline-block;
        }
        .datetimepicker table tr td span {
            width: 30%;
        }
    </style>

</head>

<body class="bai">
<%@ include file="/bxui/bxuihead.jsp" %>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/daterangepicker/css/daterangepicker.css"/>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/moment.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/daterangepicker.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raisingui/echarts/echarts.common.min.js"></script>

<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/selectLine.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/deviceManage/MaintenanceRecord.js"></script>

<input type="hidden" value="<%=tenantid%>" id="tenantid" />
<div id="father" class="page-content" style="position:inherit;min-height:600px">
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
            <a href="javascript:insertPage()" ><span id = "cityname"></span><span id="linemessage"></span><span id="programmessage"></span></a>
        </div>
    </div>
    <!-- 内容-->
    <div class="row" >
        <div class="col-xs-12 col-sm-6 col-md-9  no-padding">
            <%--<div class="col-md-1"></div>--%>
            <div class="col-sm-2 col-xs-12">
                组合信息
                <select id="inqu_status-timeType" data-bxwidget class="inputThree lala">
                    <option value="day">所有组合</option>
                    <option value="month">刀盘/刀具</option>
                </select>
            </div>
            <div class="col-sm-2 col-xs-12">
                部件信息
                <select id="inqu_status-part" data-bxwidget class="inputThree lala">
                    <option value="day">所有部件</option>
                </select>
            </div>
            <div class="col-sm-4 col-xs-12">
                <label class="labelThree" for="inqu_status-startTime">日期</label>
                <input type="text" id="inqu_status-startTime" data-bxwidget class="inputThree"/>
                <label class="labelThree di"  >至</label>
                <input type="text" id="inqu_status-endTime" data-bxwidget class="inputThree"/>
            </div>
            <div class="col-xs-12 col-sm-2  no-padding">
                <div class="col-xs-12 col-sm-12 col-md-8  no-padding-left-1024">
                    <button class="btn btn-sm btn-block" onclick="on_query_click();">
                        <div class="ace-icon fa fa-search"></div>
                        <span>查询</span>
                    </button>
                </div>
            </div>
                <div class="col-xs-12 col-sm-2 no-padding">
                    <div class="col-xs-12 col-sm-12  no-padding-left-1024">
                        <button class="btn btn-sm btn-block" onclick="importPages()">
                            <div class="ace-icon fa fa-download"></div>
                            <span>生成报表</span>
                        </button>
                    </div>
                </div>
        </div>

    </div>


</div>

<!-- ----------------------------------信息弹出框---------------------------------------------------------->
<div id="updateConfirm" class="hide">
    <span>确认修改么？</span>
</div>
<div class="alertDiv hide"></div>



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
