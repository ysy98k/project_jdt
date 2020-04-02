<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/1/11
  Time: 14:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.baosight.aas.auth.Constants" %>
<html>
<head>
    <title>工作计划</title>
    <%
        String path = request.getContextPath();
        String tenantid = request.getSession().getAttribute(Constants.SESSION_TENANTID_KEY) + "";
        String first = java.net.URLDecoder.decode(request.getParameter("firstLevel"),"UTF-8");
        String second = java.net.URLDecoder.decode(request.getParameter("secondLevel"),"UTF-8");
    %>
    <style>
        .ui-jqgrid-sortable {text-align: center;}
        .ui-jqgrid tr.jqgrow td {
            font-size:13px;
            text-align: center!important;
        }
        .ui-jqgrid-lastTh{
            float: right;
            margin: -2px -2px -2px 0;
            height: 42px !important;
        }
        #queryarea{
            margin-bottom: 5px;
        }
        .lala{
            min-width: 150px;
            height: 37px;
            text-align: center;
            text-align-last: center;
        }
        .di{
            margin: 0px 10px 5px 10px;
        }
    </style>

</head>
<body>
<%@ include file="/bxui/bxuihead.jsp" %>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/daterangepicker/css/daterangepicker.css"/>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/moment.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/daterangepicker.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raisingui/echarts/echarts.common.min.js"></script>

<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/selectLine.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/progressManage/currentProgress.js"></script>
<input type="hidden" value="<%=tenantid%>" id="tenantid">
<div class="page-content" style="min-height:600px;min-width: 1500px;">
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
        <div class="col-xs-12 col-sm-12 col-md-7  searchArea" id="queryarea">

            <div id="alertdiv" class="alertdiv"></div>
            <div class="row" >

                <div class="row" style="margin-left: 0px;margin-bottom: 10px;">
                    <div class="col-xs-12 col-sm-6 col-md-9  no-padding">
                        <div class="col-sm-3 col-xs-12">
                            <select id="inqu_status-timeType" data-bxwidget class="inputThree lala">
                                <option value="day">日</option>
                                <option value="month">月</option>
                                <option value="season">季</option>
                            </select>
                        </div>
                        <div class="col-sm-9 col-xs-12">
                            <label class="labelThree" for="inqu_status-startTime" style="margin-right: 20px;">日期</label>
                            <input type="text" id="inqu_status-startTime" data-bxwidget class="inputThree"/>
                            <label class="labelThree di"  >至</label>
                            <input type="text" id="inqu_status-endTime" data-bxwidget class="inputThree"/>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-6 col-md-3 no-padding">
                        <div class="col-xs-12 col-sm-12 col-md-6  no-padding-left-1024">
                            <button class="btn btn-sm btn-block" onclick="on_query_click();">
                                <div class="ace-icon fa fa-search"></div>
                                <span>查询</span>
                            </button>
                        </div>
                    </div>
                </div>

                <div class="col-xs-12 col-sm-12 col-md-12 one">
                    <div id="jqGrid"></div>
                </div>
                <div class="col-xs-12 col-sm-12 col-md-12 two">
                    <div  id="jqGrid2"></div>
                </div>
                <div class="col-xs-12 col-sm-12 col-md-12 three">
                    <div id="jqGrid3"></div>
                </div>

            </div>

        </div>
        <div class="col-xs-12 col-md-5 one">
            <div id="myCharts1" style="min-width: 500px;height:550px;"></div>
        </div>
        <div class="col-xs-12 col-md-5 two" style="display:none;">
            <div id="myCharts2" style="min-width: 500px;height:550px;"></div>
        </div>
        <div class="col-xs-12 col-md-5 three" style="display:none;">
            <div id="myCharts3" style="min-width: 500px;height:550px;"></div>
        </div>
        <!-- /.col -->
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
