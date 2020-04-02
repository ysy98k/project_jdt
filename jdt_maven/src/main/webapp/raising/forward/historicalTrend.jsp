<%--
  Created by IntelliJ IDEA.
  User: ysy
  Date: 2018/3/5
  Time: 15:25
  Description: 
--%>
<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="com.baosight.aas.auth.Constants" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <title>历史趋势</title>
    <%
        String path = request.getContextPath();
        String tenantid = request.getSession().getAttribute(Constants.SESSION_TENANTID_KEY) + "";
        String first = java.net.URLDecoder.decode(request.getParameter("firstLevel"),"UTF-8");
        String second = java.net.URLDecoder.decode(request.getParameter("secondLevel"),"UTF-8");
        String token = request.getSession().getAttribute("token").toString();
    %>

    <meta name="description" content="platForm Frameset"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>

    <style>
        .highcharts-axis-resizer {
            stroke: #eee;
        }
        .highcharts-axis-resizer:hover {
            stroke: #ccc;
        }
        html,body,#father{
            height:100%;
        }
        .tableTile{
            line-height: 24px;
            font-size: 15px;
            padding-top: 7px;
            background-color: #2184C1!important;
            height:30px;
            min-height: 28px!important;
        }
        #treeDiv{
            max-width: 340px;
        }
    </style>
    <link rel="stylesheet" href="<%=path%>/bxui/daterangepicker/css/daterangepicker.css"/>
</head>

<body>
<%@ include file="/bxui/bxuihead.jsp" %>
<script type="text/javascript" src="<%=path%>/bxui/daterangepicker/js/moment.min.js"></script>
<script type="text/javascript" src="<%=path%>/bxui/daterangepicker/js/daterangepicker.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=path%>/bxui/baosight-require.js"></script>
<script src="<%=toolkitPath%>/bxui/highcharts/highstock.js"></script>
<script src="<%=toolkitPath%>/bxui/highcharts/exporting.js"></script>
<script src="<%=toolkitPath%>/bxui/highcharts/highcharts-zh_CN.js"></script>






<div class="page-content" id="father" style="position:inherit;min-height:600px">
    <!-- /sectionController:settings.box -->
    <div class="page-header" id="header">
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
    <div class="row" id="baba">
        <div class="col-sm-3" id="treeDiv">
            <div class="widget-box widget-color-blue" id="treeDiv2" style="overflow:scroll;overflow-x:hidden">
                <div class="widget-header tableTile">
                    <div><span class="widget-title lighter smaller">掘进变量</span></div>
                </div>

                <div class="widget-body">
                    <div class="widget-main padding-8 bluetree">
                        <div id="bxtree3"></div>
                    </div>
                </div>

            </div>
        </div>

        <div id="queryarea" class="col-xs-9">
            <div id="timeDiv" class="input-group" style="float: right">
                <button type="button" class="btn btn-default pull-right" id="daterange-btn" >
                    <i class="fa fa-calendar"></i>
                    <span>时间</span>
                    <i class="fa fa-caret-down"></i>
                </button>
            </div>

            <div id="container"  style="min-width:400px;"></div>
        </div>
    </div>
</div>

<!-- ----------------------------------信息弹出框---------------------------------------------------------->
<div id="dialog-message" class="hide">
    <p class="bigger-110 bolder center grey">
        <br/>
        <b id="dialogInfo"></b>
    </p>
</div>
<div id="detail" class="hide" style="overflow: hidden">
    <div type="text" id = "div_city" name = "city" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
    <div type="text" id = "div_line" name = "line" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
    <div type="text" id = "div_program" name = "program" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
</div>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/selectLine.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/historicalTrend.js"></script>
</body>

</html>

