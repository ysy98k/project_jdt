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
    <title>历史曲线</title>
    <%
        String path = request.getContextPath();
        String tenantid = request.getSession().getAttribute(Constants.SESSION_TENANTID_KEY) + "";
    %>

    <meta name="description" content="platForm Frameset"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <%--<link rel="stylesheet" href="<%=path%>/raisingui/vertree/css/treeIcon.css">
    <link rel="stylesheet" href="<%=path%>/raisingui/vertree/css/common.css">--%>
    <style>
        .bai{
            background-color:#FFFFFF;
        }
        #father{
            background: -webkit-linear-gradient(#105899,#3C84C5, #105899); /* Safari 5.1 - 6.0 */
            background: -o-linear-gradient(#105899,#3C84C5, #105899); /* Opera 11.1 - 12.0 */
            background: -moz-linear-gradient(#105899,#3C84C5, #105899); /* Firefox 3.6 - 15 */
            background: linear-gradient(#105899,#3C84C5, #105899);!important; /* 标准的语法 */
            color:white!important;
            min-height:600px;
            height: 100%;
            position:inherit;
        }
        /*头部*/
        .page-header{
            background-color: white;
            margin: -8px -20px 0px!important;
            padding:8px 20px 9px 20px;
        }
        .page-header h5,.page-header h5 small,.page-header div a{
            color:white!important;
        }
        .row{
            margin:0px!important;
        }
        /*tree*/
        ul[class=""]{
            position: absolute;
            z-index: 2;
            border: white solid 1px;
            /*background: linear-gradient(#105899,#3C84C5, #105899);!important; */
            background: #105899!important; /* 标准的语法 */
        }

    </style>

</head>

<body class="bai">
<%@ include file="/bxui/bxuihead.jsp" %>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/moment.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/echarts3/echarts.js"></script>

<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxdialog.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-elementui.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raisingui/vertree/verTree.js"></script>
<script src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>


<style>
    /*tree*/
    .blue{
        color: #48C275!important;
    }
</style>
<input type="hidden" value="<%=tenantid%>" id="tenantid" />
<div id="father" class="page-content" style="min-height: 864px;min-width: 1600px;" >
    <div class="page-header">
        <h5 style="color: rgb(38, 121, 181)!important;">
            施工管理管理
            <small style="color:rgb(153, 153, 153)!important;">
                &nbsp;
                <i class="ace-icon fa fa-angle-double-right"></i>
                &nbsp;&nbsp;
                实时曲线
            </small>
        </h5>
        <div style="float: right;margin-top: -15px;">
            <a href="javascript:insertPage()" style="color: rgb(38, 121, 181)!important;" ><span id = "cityname"></span><span id="linemessage"></span><span id="programmessage"></span></a>
        </div>
    </div>

    <!-- 内容-->
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12" style="margin-top:50px;">
            <div id="tree_list"></div>
        </div>

        <div class="col-xs-12 col-sm-12 col-md-12" style="margin-top:20px;">
            <div class="col-xs-12 col-sm-10 col-md-10  no-padding-left-1024" >
            </div>
            <div class="col-xs-12 col-sm-1 col-md-1  no-padding-left-1024" >
                <button class="btn btn-sm btn-block" onclick="show();" style="background-color: #48C275!important;border-color: #48C275!important;">
                    <div class="ace-icon fa fa-search"></div>
                    <span>查询</span>
                </button>
            </div>
        </div>
        <div class="col-xs-12 col-sm-12 col-md-12" style="margin-top:30px;">
            <div id="myCharts" style="min-height:600px;width: 1300px;"></div>
        </div>

    </div>
</div>

<!------------------------------------信息弹出框---------------------------------------------------------->




<div id="detail" class="hide" style="overflow: hidden">
    <div type="text" id = "div_city" name = "city" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
    <div type="text" id = "div_line" name = "line" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
    <div type="text" id = "div_program" name = "program" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
</div>

<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/selectLine.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/constructionManagement/realTimeCurve.js"></script>


</body>
</html>
