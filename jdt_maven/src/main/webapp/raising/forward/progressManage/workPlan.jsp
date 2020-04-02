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
    </style>

</head>
<body>
<%@ include file="/bxui/bxuihead.jsp" %>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/daterangepicker/css/daterangepicker.css"/>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/moment.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/daterangepicker.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/selectLine.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/progressManage/workPlan.js"></script>
<input type="hidden" value="<%=tenantid%>" id="tenantid">
<div class="page-content" style="position:inherit;min-height:600px">
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
        <div class="col-xs-12 searchArea" id="queryarea">

            <div id="alertdiv" class="alertdiv"></div>
            <div class="row">
                <div class="col-sm-6 col-md-6 col-xs-12 no-padding">
                    <div class="col-md-3"></div>
                    <div class="col-sm-3 col-md-3 col-xs-6 no-padding-left-1024">
                        <button class="btn btn-sm btn-block" onclick="addAndCopy();">
                            <div class="ace-icon fa fa-plus"></div>
                            <span>新增/复制</span>
                        </button>
                    </div>
                    <div class="col-sm-3 col-md-2 col-xs-6 no-padding-left-1024">
                        <button class="btn  btn-sm btn-block" onclick="saveRec();">
                            <div class="ace-icon fa fa-save"></div>
                            <span>保存</span>
                        </button>
                    </div>
                    <div class="col-sm-3 col-md-2 col-xs-6 no-padding-left-1024">
                        <button class="btn btn-sm btn-danger width-100" onclick="deleteRec();">
                            <div class="ace-icon fa fa-trash-o"></div>
                            <span>删除</span>
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-xs-12">
            <div id="jqGrid"></div>
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
