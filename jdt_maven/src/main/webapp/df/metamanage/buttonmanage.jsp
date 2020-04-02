<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>按钮信息管理</title>

</head>
<body>
<%@ include file="/bxui/bxuihead.jsp" %>
<script type="text/javascript"
        src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript"
        src="<%=toolkitPath%>/df/metamanage/buttonmanage.js"></script>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css" />

<div class="jqgridFrame contentFrame">
    <div class="page-content contentFrame">
        <!-- /sectionController:settings.box -->
        <div class="page-header" style="height: 50px;">
            <h1 class="pull-left">
                按钮信息管理
                <small><i class="ace-icon fa fa-angle-double-right"></i>
                    对按钮信息进行注册和相关配置，以便关联页面并配置按钮权限
                </small>
            </h1>

        </div>
        <!-- /.page-header -->
        <div class="row" style="height: 90%">
            <div class="col-xs-12" style="height: 100%">
                <div class="col-sm-2 hidden-y">
                    <div class="row">
                        <div id="pagetree"></div>
                    </div>
                </div>
                <div class="col-sm-10">
                    <div class="row">
                        <div class="col-xs-12 searchArea" id="queryarea">
                            <div id="alertdiv" class="alertdiv"></div>
                            <div class="row">
                                <div class="col-sm-4 col-xs-12">
                                    <label class="labelThree"
                                           for="inqu_status-pageName">所属页面</label>
                                    <input type="text" id="inqu_status-pageName" data-bxwidget class="inputThree"/>
                                </div>

                                <div class="col-sm-4 col-xs-12">
                                    <label class="labelThree"
                                           for="inqu_status-buttonName">按钮ID</label>
                                    <input type="text" id="inqu_status-buttonName" data-bxwidget
                                           class="inputThree"/>
                                </div>
                                <div class="col-sm-4 col-xs-12">
                                    <label class="labelThree"
                                           for="inqu_status-buttonDisplayname">按钮显示名</label>
                                    <input type="text" id="inqu_status-buttonDisplayname" data-bxwidget
                                           class="inputThree"/>
                                </div>
							</div>
							<div class="row">
								<div class="col-sm-4"></div>
                                <div class="col-sm-2  col-xs-6 pull-right">
                                    <button class="btn btn-sm pull-left btn-block"onclick="on_query_click();">
                                        <div class="ace-icon fa fa-search"></div>
                                        <span>查询</span>
                                    </button>
                                </div>
                                <div class="col-sm-2  col-xs-6 pull-right no-padding-right-1024">
                                    <button class="btn btn-sm pull-left btn-danger width-100"onclick="deleteRec();">
                                        <div class="ace-icon fa fa-trash-o"></div>
                                        <span>删除</span>
                                    </button>
                                </div>
                                <div class="col-sm-2  col-xs-6 pull-right no-padding-right-1024">
                                    <button class="btn btn-sm pull-left btn-block"onclick="saveRec();">
                                        <div class="ace-icon fa fa-save"></div>
                                        <span>保存</span>
                                    </button>
                                </div>
                                <div class="col-sm-2  col-xs-6 pull-right no-padding-right-1024">
                                    <button class="btn btn-sm pull-left btn-block"onclick="addAndCopy();">
                                        <div class="ace-icon fa fa-plus"></div>
                                        <span>新增/复制</span>
                                    </button>
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
        </div>
    </div>
</div>
</body>
</html>

