<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2018-07-02
  Time: 15:19
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>报警变量管理</title>
    <style>
        .carry{
            margin-top: 5px;
        }
    </style>
</head>
<body>
<%@ include file="/bxui/bxuihead.jsp" %>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jstree/dist/jstree.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxtree.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/loadsave.js"></script>
<script src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/backstage/projectManage/alarmVariableManagement.js"></script>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css"/>

<div class="jqgridFrame contentFrame">
    <div class="page-content contentFrame">
        <!-- /sectionController:settings.box -->
        <div class="page-header" style="height: 50px;">
            <h1 class="pull-left">
                报警变量管理
                <small><i class="ace-icon fa fa-angle-double-right"></i>
                    对报警变量进行注册和相关配置
                </small>
            </h1>

        </div>
        <!-- /.page-header -->
        <div class="row" style="margin:0px -5px;">
            <div class="col-xs-12 searchArea" id="queryarea">

                <div id="alertdiv" class="alertdiv"></div>

                <div class="row">
                    <div class="col-sm-6 col-md-6 col-xs-12">
                        <div class="col-md-2 col-sm-3 carry">
                            <label>
                                <input name="form-field-checkbox" type="checkbox" value="1" class="ace" onclick="alarmCheckClick()">
                                <span class="lbl"> 设备报警</span>
                            </label>
                        </div>
                        <div class="col-md-2 col-sm-3 carry">
                            <label>
                                <input name="form-field-checkbox" type="checkbox" value="2" class="ace" onclick="alarmCheckClick()">
                                <span class="lbl"> 风险报警</span>
                            </label>
                        </div>
                        <div class="col-md-2 col-sm-3 carry">
                            <label>
                                <input name="form-field-checkbox" type="checkbox" value="3" class="ace" onclick="alarmCheckClick()">
                                <span class="lbl"> 导向报警</span>
                            </label>
                        </div>

                        <div class="col-sm-6 col-md-6 col-xs-12">
                            <label class="labelThree" for="inqu_status-alarmName" style="text-align: right;padding-right: 20px;" >报警变量名</label>
                            <input type="text" id="inqu_status-alarmName" data-bxwidget class="inputThree"/>
                        </div>
                    </div>

                    <div class="col-sm-6 col-md-6 col-xs-12 no-padding">
                        <div class="col-sm-3 col-md-2 col-xs-6 no-padding-left-1024">
                            <button class="btn btn-sm btn-block" onclick="on_query_click();">
                                <div class="ace-icon fa fa-search"></div>
                                <span>查询</span>
                            </button>
                        </div>
                        <div class="col-sm-3 col-md-2 col-xs-6 no-padding-left-1024">
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
                        <div class="col-sm-4 col-md-3 col-xs-6 no-padding-right-1024">
                            <button class="btn btn-sm btn-block" onclick="exportExcel()">
                                <div class="ace-icon fa fa-upload"></div>
                                <span>导出</span>
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
        <!-- /.row -->
    </div>


</div>


</body>
</html>

