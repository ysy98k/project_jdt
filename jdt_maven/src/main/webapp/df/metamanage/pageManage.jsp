<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>页面信息管理</title>

</head>
<body>
<%@ include file="/bxui/bxuihead.jsp" %>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/loadsave.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/metamanage/pageManage.js"></script>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css"/>

<div class="jqgridFrame contentFrame">
    <div class="page-content contentFrame">
        <!-- /sectionController:settings.box -->
        <div class="page-header" style="height: 50px;">
            <h1 class="pull-left">
                页面信息管理
                <small><i class="ace-icon fa fa-angle-double-right"></i>
                    对页面信息进行注册和相关配置，以便关联菜单并配置页面权限
                </small>
            </h1>

        </div>
        <!-- /.page-header -->
        <div class="row">
            <div class="col-xs-12 searchArea" id="queryarea">

                <div id="alertdiv" class="alertdiv"></div>
                <div class="row">
                    <div class="col-sm-4 col-xs-12">
                        <label class="labelThree"
                               for="inqu_status-pageEname">页面英文名</label>
                        <input type="text" id="inqu_status-pageEname" data-bxwidget class="inputThree"/>
                    </div>
                    <div class="col-sm-4 col-xs-12">
                        <label class="labelThree"
                               for="inqu_status-pageCname">页面中文名</label>
                        <input type="text" id="inqu_status-pageCname" data-bxwidget class="inputThree"/>
                    </div>
                    <div class="col-sm-4 col-xs-12">
                        <label class="labelThree"
                               for="inqu_status-pageCname">页面类型</label>
                        <div type="text" id="inqu_status-pageType" data-bxwidget="bxcombobox" class="inputThree"></div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-6 col-md-6 col-xs-12 no-padding">
                        <div class="col-sm-4 col-md-3 col-xs-6 no-padding-right-1024">
                            <button class="btn btn-sm btn-block" onclick="importPages()">
                                <div class="ace-icon fa fa-download"></div>
                                <span>导入设计页</span>
                            </button>
                        </div>
                        <div class="col-sm-4 col-md-3 col-xs-6 no-padding-right-1024">
                            <button class="btn btn-sm btn-block" onclick="exportPages()">
                                <div class="ace-icon fa fa-upload"></div>
                                <span>导出设计页</span>
                            </button>
                        </div>
                        <div class="col-sm-4 col-md-3 col-xs-6">
                            <button class="btn btn-sm btn-block" onclick="on_grantresource_click();">
                                <div class="ace-icon fa fa-key"></div>
                                <span>注册资源</span>
                            </button>
                        </div>
                    </div>
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
                        <div class="col-sm-3 col-md-2 col-xs-6 no-padding-left-1024">
                            <button class="btn btn-sm btn-block" onclick="on_query_click();">
                                <div class="ace-icon fa fa-search"></div>
                                <span>查询</span>
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
<!-- page-content -->

</body>
</html>

