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
    %>

    <meta name="description" content="platForm Frameset"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <style>
        .ui-jqgrid tr.ui-row-ltr td{
            font-size: 13px;
        }
    </style>
</head>

<body style="background-color: white;">
<%@ include file="/bxui/bxuihead.jsp" %>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/daterangepicker/css/daterangepicker.css"/>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/moment.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/daterangepicker.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/vue/vue.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<%--这里解决chosen下拉框和查询等按钮css冲突的问题--%>
<script type="text/javascript" src="<%=toolkitPath%>/df/error/assets/js/uncompressed/chosen.jquery.js"></script>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/jquery-plugin/css/chosen.css"/>
<%--这里解决chosen下拉框和查询等按钮css冲突的问题--%>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/loadsave.js"></script>
<script src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>
<style>
    .chosen-container>.chosen-single, [class*=chosen-container]>.chosen-single {
        line-height: 28px;
        height: 30px;
        box-shadow: none;
        background: #FAFAFA;
    }
    /*解决两个搜索图标问题*/
    .chosen-container-single .chosen-search::after{
        display: none;
    }
    .query_title_div{
        display:table-cell;
        text-align: right;
        padding: 4px 0px 0px 15px;
        min-width:50px;
        font-size: 15px;
        font-weight:400;
    }
    .shortDiv .chosen-container{
        width: 150px!important;
    }
</style>

<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/deviceManage/tbmAssetManage.js"></script>


<div id="father" class="page-content" style="position:inherit;min-height:600px">
    <div class="page-header">
        <h5>
            设备管理
            <small>
                &nbsp;
                <i class="ace-icon fa fa-angle-double-right"></i>
                &nbsp;&nbsp;
                盾构资产管理
            </small>
        </h5>
    </div>
    <!-- 内容-->
    <div class="page-content contentFrame">

        <div class="row">
            <div class="col-xs-12 searchArea" id="queryarea">

                <div id="alertdiv" class="alertdiv"></div>

                <div class="row">
                    <div class="col-md-3" id="factoryDiv">
                        <div style="display: inline-block">
                            盾构机厂家：
                        </div>
                        <select class="chosen-select" id="inqu_status-factory" data-bxwidget class="inputThree" >
                            <option id="factoryOption" value="">全部</option>
                        </select>
                    </div>


                    <div class="col-md-2 shortDiv" id="ownerDiv">
                        <div style="display: inline-block">
                            所有单位：
                        </div>
                        <select class="chosen-select" id="inqu_status-owner"  data-bxwidget class="inputThree">
                            <option id="ownerOption" value="">全部</option>
                        </select>
                    </div>


                    <div class="col-md-2 shortDiv" id="buildStatusDiv">
                        <div style="display: inline-block">
                            使用状态：
                        </div>
                        <select class="chosen-select" id="inqu_status-tbmStatus"   data-bxwidget class="inputThree">
                            <option id="tbmStatusOption" value="">全部</option>
                        </select>
                    </div>

                    <div class="col-sm-2">
                            <div style="display: inline-block">
                                盾构机名称：
                            </div>
                        <input type="text" id="inqu_status-tbmName" value="" style="width: 150px" data-bxwidget />
                    </div>

                    <div class="col-sm-1 col-md-1 col-xs-6 no-padding-left-1024" style="min-width: 100px;">
                        <button class="btn btn-sm btn-block" onclick="on_query_click();">
                            <div class="ace-icon fa fa-search"></div>
                            <span>查询</span>
                        </button>
                    </div>
                </div>

            </div>

            <div class="col-xs-12">
                <div id="grid"></div>
            </div>
            <!-- /.col -->
        </div>
        <!-- /.row -->
    </div>

</div>

<!-- ----------------------------------信息弹出框---------------------------------------------------------->
<%--<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/tbmManage/tbmManage.js"></script>--%>
</body>
</html>
