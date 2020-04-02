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
    <title>盾构机信息</title>
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
            background-color:#FFFFFF;
        }
        .ui-jqgrid tr.jqgrow td {
            font-size:13px;
            text-align: center!important;
        }
        a[role="link"]{
            display: none;
        }


    </style>

</head>

<body class="bai">
<%@ include file="/bxui/bxuihead.jsp" %>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/daterangepicker/css/daterangepicker.css"/>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/moment.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/daterangepicker.js"></script>

<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxdialog.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-elementui.js"></script>

<input type="hidden" value="<%=tenantid%>" id="tenantid" />
<div id="father" class="page-content" style="position:inherit;min-height:600px">
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
    <div class="row">

        <div class="col-xs-12 searchArea" id="queryarea">

            <div id="alertdiv" class="alertdiv" ></div>

            <div class="row">
                <div class="col-md-3">
                    <div data-toggle="buttons" id="constructorDiv"  class="btn-group btn-overlap btn-corner">
                        <label class="btn btn-sm btn-white btn-info" id="all" onclick="alarmTypeClick(this)">
                            <input type="checkbox" value="-1"/>
                            全部
                        </label>
                        <label class="btn btn-sm btn-white btn-info" id="prostatus.building"  onclick="alarmTypeClick(this)">
                            <input type="checkbox" value="1"/>
                            设备报警
                        </label>
                        <label class="btn btn-sm btn-white btn-info" id="prostatus.finished" onclick="alarmTypeClick(this)">
                            <input type="checkbox" value="2"/>
                            风险报警
                        </label>
                        <label class="btn btn-sm btn-white btn-info" id="prostatus.nostart"  onclick="alarmTypeClick(this)">
                            <input type="checkbox" value="3"/>
                            导向报警
                        </label>
                    </div>
                </div>
                <div class="col-md-3">
                    <div id="historyTime">
                        <label class="labelThree" for="inqu_status-startTime" style="margin-right: 20px;">日期</label>
                        <input type="text" id="inqu_status-startTime" data-bxwidget class="inputThree"/>
                        <label class="labelThree di"  >至</label>
                        <input type="text" id="inqu_status-endTime" data-bxwidget class="inputThree"/>
                    </div>
                </div>
                <div class="col-md-3">
                    <div id="historyOperatorButton">
                        <div class="col-xs-12 col-sm-6 col-md-5  no-padding-left-1024">
                            <button class="btn btn-sm btn-block" onclick="on_query_click(this);">
                                <div class="ace-icon fa fa-search"></div>
                                <span>查询</span>
                            </button>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-5  no-padding-left-1024">
                            <button class="btn btn-sm btn-block" onclick="exportExcel(this);">
                                <div class="ace-icon fa fa-upload"></div>
                                <span>导出</span>
                            </button>
                        </div>
                    </div>

                </div>
                <div class="col-md-3">
                    <div style="text-align: right;">
                        <button  class="btn btn-white btn-success" onclick="infoHistoryConvert(this)">报警历史记录</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xs-12" id="gridDiv">
            <div id="grid"></div>
        </div>
        <div class="col-xs-12" id="historyGridDiv">
            <div id="historyGrid"></div>
        </div>
    </div>
</div>

<!------------------------------------信息弹出框---------------------------------------------------------->
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

<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/selectLine.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/constructionManagement/alarmInfo.js"></script>


</body>
</html>
