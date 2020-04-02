<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>盾构机信息管理</title>

</head>
<body>
<%@ include file="/bxui/bxuihead.jsp" %>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/daterangepicker/css/daterangepicker.css"/>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/moment.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/daterangepicker.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jstree/dist/jstree.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxtree.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/easyui-plugin/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/loadsave.js"></script>
<script src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css"/>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/easyui-plugin/css/easyui.css"/>

<div class="jqgridFrame contentFrame">
    <div class="page-content contentFrame">
        <!-- /sectionController:settings.box -->
        <div class="page-header" style="height: 50px;">
            <h1 class="pull-left">
                盾构机信息管理
                <small><i class="ace-icon fa fa-angle-double-right"></i>
                    对盾构机信息进行注册和相关配置
                </small>
            </h1>

        </div>
        <!-- /.page-header -->
        <div class="row">
            <div class="col-xs-12 searchArea" id="queryarea">

                <div id="alertdiv" class="alertdiv"></div>

                <div class="row">
                    <div class="col-sm-2 col-xs-12">
                        <label class="labelThree"
                               for="inqu_status-tbmName">名称</label>
                        <input type="text" id="inqu_status-tbmName" data-bxwidget class="inputThree"/>
                    </div>
                    <div class="col-sm-2 col-xs-12">
                        <label class="labelThree"
                               for="inqu_status-factory">厂家</label>
                        <select id="inqu_status-factory" data-bxwidget class="inputThree">
                            <option value="">全部</option>
                        </select>
                    </div>
                    <div class="col-sm-2 col-xs-12">
                        <label class="labelThree"
                               for="inqu_status-owner">拥有单位</label>
                        <select id="inqu_status-owner" data-bxwidget class="inputThree">
                            <option value="">全部</option>
                        </select>
                    </div>
                    <div class="col-sm-2 col-xs-12">
                        <label class="labelThree"
                               for="inqu_status-rmsVersion">导向系统</label>
                        <select id="inqu_status-rmsVersion" data-bxwidget class="inputThree">
                            <option value="">全部</option>
                        </select>
                    </div>
                    <div class="col-sm-2 col-xs-12">
                        <label class="labelThree"
                               for="inqu_status-ccsType">盾构机类型</label>
                        <select id="inqu_status-ccsType" data-bxwidget class="inputThree">
                            <option value="">全部</option>
                        </select>
                    </div>

                </div>

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
<script type="text/javascript" src="<%=toolkitPath%>/raising/backstage/tbmManage.js" />
<script type="text/javascript">
    function initCol(colNames,colModels) {
        colNames.push('操作');
        var  classification

    }

</script>

</body>

</html>

