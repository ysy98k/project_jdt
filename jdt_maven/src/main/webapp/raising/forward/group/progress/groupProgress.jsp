<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/1/11
  Time: 14:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.baosight.aas.auth.Constants" %>
<!DOCTYPE html>
<html>
<head>
    <title>进度分析</title>
    <link rel="stylesheet" href="https://unpkg.com/bootstrap-table@1.15.2/dist/bootstrap-table.min.css">
    <style>
        body{
            background-color: white!important;
        }
        table{
            font-size: 13px;
        }

        .th-inner{
            font-size: 15px;
        }
        thead tr th{
            background: repeat-x #F2F2F2;
            background-image: linear-gradient(to bottom,#f8f8f8 0,#ececec 100%);
        }
        th[rowspan]{
            background: repeat-x #F2F2F2;
            background-image: linear-gradient(to bottom,#f8f8f8 0,#ececec 100%);
        }
    </style>


</head>
<body>
<%@ include file="/bxui/bxuihead.jsp" %>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/vue/vue.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>

<script type="text/javascript" src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-form.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-elementui.js"></script>
<script src="<%=toolkitPath%>/bxui/bootstrap/dist/js/bootstrap.js"></script>
<script src="<%=toolkitPath%>/raisingui/bootstrapTable/bootstrap-table.js"></script>
<script src="<%=toolkitPath%>/raisingui/bootstrapTable/bootstrap-table-zh-CN.min.js"></script>



<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/group/progress/groupProgress.js"></script>

<div class="page-content" style="position:inherit;min-height:600px;padding-bottom: 8px;">
    <div class="page-header">
        <h5>
            进度分析
            <small>
                &nbsp;
                <i class="ace-icon fa fa-angle-double-right"></i>
                &nbsp;&nbsp;
                集团进度
            </small>
        </h5>
    </div><!-- /.page-header -->


    <div class="row" id="father" style="margin: 0px;">
        <div class="tabbable">
            <ul class="nav nav-tabs" id="myTab">
                <li class="active">
                    <a data-toggle="tab" href="#week" >
                        <i class="green ace-icon fa fa-home bigger-120"></i>
                        周报表
                    </a>
                </li>
                <li>
                    <a data-toggle="tab" href="#month" >
                        <i class="green ace-icon fa fa-camera-retro bigger-120"></i>
                        月报表
                    </a>
                </li>
            </ul>

            <div id="toolbar"></div>
            <div id="tableContent" class="tab-content" style="height: 800px;">

                <div id="week" class="tab-pane in active">

                    <div class="col-xs-12" id="queryarea1">
                        <div class="col-md-3" style="padding-top: 5px;max-width: 300px;">
                            <label for="week_year" style=""><span>年：</span></label>
                            <select class="chosen-select" id="week_year">
                            </select>
                        </div>

                        <div class="col-md-3" style="padding-top: 5px;">
                            <label for="week_week" style=""><span>周：</span></label>
                            <select class="chosen-select" style="padding-top: 5px;max-width: 80px;"  id="week_week">
                            </select>
                        </div>

                        <div class="col-md-1 btnDefault">
                            <button class="btn btn-sm btn-block" onclick="queryWeek(this);" >
                                <div class="ace-icon fa fa-search"></div>
                                <span>查询</span>
                            </button>
                        </div>
                        <div class="col-md-1 btnDefault">
                            <button class="btn btn-sm btn-block" onclick="exportCSV('week');" >
                                <div class="ace-icon fa fa-download"></div>
                                <span>导出</span>
                            </button>
                        </div>
                    </div>

                    <div class="col-xs-12" style="margin: 50px 0px 20px 0px">
                        <table id="week_table">
                        </table>
                    </div>


                </div>

                <div id="month" class="tab-pane">
                    <div class="col-xs-12" id="queryarea2">
                        <div class="col-md-3" style="padding-top: 5px;max-width: 300px;">
                            <label for="week_year" style=""><span>年：</span></label>
                            <select class="chosen-select" id="month_year">
                            </select>
                        </div>

                        <div class="col-md-3" style="padding-top: 5px;">
                            <label for="week_week" style=""><span>周：</span></label>
                            <select class="chosen-select" style="padding-top: 5px;max-width: 80px;"  id="month_month">
                                <option value="1">1月</option>
                                <option value="2">2月</option>
                                <option value="3">3月</option>
                                <option value="4">4月</option>
                                <option value="5">5月</option>
                                <option value="6">6月</option>
                                <option value="7">7月</option>
                                <option value="8">8月</option>
                                <option value="9">9月</option>
                                <option value="10">10月</option>
                                <option value="11">11月</option>
                                <option value="12">12月</option>
                            </select>
                        </div>

                        <div class="col-md-1 btnDefault">
                            <button class="btn btn-sm btn-block" onclick="queryMonth(this);" >
                                <div class="ace-icon fa fa-search"></div>
                                <span>查询</span>
                            </button>
                        </div>
                        <div class="col-md-1 btnDefault">
                            <button class="btn btn-sm btn-block" onclick="exportCSV('month');" >
                                <div class="ace-icon fa fa-download"></div>
                                <span>导出</span>
                            </button>
                        </div>
                    </div>

                    <div class="col-xs-12" style="margin: 50px 0px 20px 0px">
                        <table id="month_table">
                        </table>
                    </div>

                </div>


            </div>
        </div>
    </div>
</div>

</body>
</html>
