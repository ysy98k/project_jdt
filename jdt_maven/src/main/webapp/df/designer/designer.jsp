<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>可视化布局系统</title>
    <link href="css/bootstrap-combined.min.css" rel="stylesheet">
</head>

<body style="min-height: 100%; cursor: auto;" class="edit">
<%@ include file="/bxui/bxuihead.jsp" %>
<script type="text/javascript"
        src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript"
        src="<%=toolkitPath%>/bxui/jseditor/ace.js"></script>
<!--[if lt IE 9]>
<script src="<%=toolkitPath%>/df/designer/js/html5shiv.js"></script>
<![endif]-->
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/designer.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/jquery.layout-1.4.4.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/jquery.layout-simple.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/htmlutil.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/loadsave.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/codeEditor.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/dataSourceEditor.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/jquery.contextMenu.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/evol-colorpicker.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/container/container.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/controllers/widgets.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/interaction/tab.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/controllers/js/bxwidgetcommon.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/controllers/js/chart/bxchartscommon.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/controllers/js/chart/bar/bxchartsbar.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/controllers/js/chart/line/bxchartsline.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/controllers/js/chart/pie/bxchartspie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/controllers/js/chart/gauge/bxchartsgauge.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/controllers/js/chart/map/bxchartsmap.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/controllers/js/chart/radar/bxchartsradar.js"></script>
<script type="text/javascript"
        src="<%=toolkitPath%>/df/designer/controllers/js/chart/scatter/bxchartsscatter.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/jquery.shCircleLoader.js"></script>

<link rel="stylesheet" href="<%=toolkitPath%>/df/designer/css/layoutit.css">
<link rel="stylesheet" href="<%=toolkitPath%>/df/designer/css/layout-default-latest.css">
<link rel="stylesheet" href="<%=toolkitPath%>/df/designer/css/evol-colorpicker.css"/>
<link rel="stylesheet" href="<%=toolkitPath%>/df/designer/css/designer.css"/>
<link rel="stylesheet" href="<%=toolkitPath%>/df/designer/css/jquery.shCircleLoader.css"/>


<div class="ui-layout-north">
    <div class="navbar navbar-fixed-top">
        <div class="container-fluid">
            <div class="pull-left">
                <a class="navbar-brand">
                    <small><img
                            src="<%=toolkitPath%>/df/console/images/logo-small.png"></small>
                </a>
            </div>

            <div class="pull-left">
                <a class="navbar-brand"> <span class="navText">WLS页面组件设计服务</span></a>
            </div>
            <div class="nav-collapse center" id="menu-layoutit">
                <div class="btn-group btn-corner">
                    <button class="btn border-white btn-sm" title="数据源编辑器" id="dataSource">
                        <i class="icon-only ace-icon fa fa-database"></i>
                    </button>
                </div>

                <div class="btn-group btn-corner">
                    <button class="btn border-white btn-sm" href="#undo" id="undo">
                        <i class="icon-only ace-icon fa fa-undo" title="撤销"></i>
                    </button>
                    <button class="btn border-white btn-sm" href="#redo" id="redo">
                        <i class="icon-only ace-icon fa fa-repeat" title="重做"></i>
                    </button>
                    <button class="btn border-white btn-sm" href="#clear" id="clear">
                        <i class="icon-only ace-icon fa fa-trash-o" title="清空"></i>
                    </button>
                </div>

                <div class="btn-group btn-corner" style="margin-right: 30px">
                    <button class="btn border-white btn-sm" title="事件编辑器" id="bindEvent">
                        <i class="icon-only ace-icon fa fa-code"></i>
                    </button>
                </div>

                <div class="btn-group btn-corner">
                    <button class="btn border-white btn-sm" id="btnSave" title="保存">
                        <i class="icon-only ace-icon fa fa-floppy-o"></i>
                    </button>
                    <button class="btn border-white btn-sm" href="#preview" id="preview">
                        <i class="icon-only ace-icon fa fa-eye" title="预览"></i>
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="ui-layout-west">
    <div class="sidebar-nav ">
        <div id="accordion" class="accordion-style2" style="display: none">
            <div class="group">
                <h3 class="accordion-header"
                    title="在这里设置你的栅格布局, 栅格总数默认为12, 用空格分割每列的栅格值">布局设置</h3>

                <div>
                    <ul class="nav nav-list accordion-group">
                        <li style="display: list-item;" class="rows" id="estRows">
                            <div class="lyrow ui-draggable">
                                <a href="#close" class="remove label label-important"><i
                                        class="ace-icon fa fa-trash-o"></i>&nbsp;删除</a> <span
                                    class="drag label"><i class="ace-icon fa fa-arrows"></i>&nbsp;拖动</span>

                                <div class="preview">
                                    <input value="12" type="text" maxlength="23">
                                </div>
                                <div class="view">
                                    <div class="row-fluid clearfix ">
                                        <div class="span12 column"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="lyrow ui-draggable">
                                <a href="#close" class="remove label label-important"><i
                                        class="ace-icon fa fa-trash-o"></i>&nbsp;删除</a> <span
                                    class="drag label"><i class="ace-icon fa fa-arrows"></i>&nbsp;拖动</span>

                                <div class="preview">
                                    <input value="6 6" type="text" maxlength="23">
                                </div>
                                <div class="view">
                                    <div class="row-fluid clearfix">
                                        <div class="span6 column"></div>
                                        <div class="span6 column"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="lyrow ui-draggable">
                                <a href="#close" class="remove label label-important"><i
                                        class="ace-icon fa fa-trash-o"></i>&nbsp;删除</a> <span
                                    class="drag label"><i class="ace-icon fa fa-arrows"></i>&nbsp;拖动</span>

                                <div class="preview">
                                    <input value="8 4" type="text" maxlength="23">
                                </div>
                                <div class="view">
                                    <div class="row-fluid clearfix">
                                        <div class="span8 column"></div>
                                        <div class="span4 column"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="lyrow ui-draggable">
                                <a href="#close" class="remove label label-important"><i
                                        class="ace-icon fa fa-trash-o"></i>&nbsp;删除</a> <span
                                    class="drag label"><i class="ace-icon fa fa-arrows"></i>&nbsp;拖动</span>

                                <div class="preview">
                                    <input value="4 8" type="text" maxlength="23">
                                </div>
                                <div class="view">
                                    <div class="row-fluid clearfix">
                                        <div class="span4 column"></div>
                                        <div class="span8 column"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="lyrow ui-draggable">
                                <a href="#close" class="remove label label-important"><i
                                        class="ace-icon fa fa-trash-o"></i>&nbsp;删除</a> <span
                                    class="drag label"><i class="ace-icon fa fa-arrows"></i>&nbsp;拖动</span>

                                <div class="preview">
                                    <input value="4 4 4" type="text" maxlength="23">
                                </div>
                                <div class="view">
                                    <div class="row-fluid clearfix">
                                        <div class="span4 column"></div>
                                        <div class="span4 column"></div>
                                        <div class="span4 column"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="lyrow ui-draggable">
                                <a href="#close" class="remove label label-important"><i
                                        class="ace-icon fa fa-trash-o"></i>&nbsp;删除</a> <span
                                    class="drag label"><i class="ace-icon fa fa-arrows"></i>&nbsp;拖动</span>

                                <div class="preview">
                                    <input value="2 6 4" type="text" maxlength="23">
                                </div>
                                <div class="view">
                                    <div class="row-fluid clearfix">
                                        <div class="span2 column"></div>
                                        <div class="span6 column"></div>
                                        <div class="span4 column"></div>
                                    </div>
                                </div>
                            </div>
                            <%--<div class="lyrow ui-draggable">--%>
                            <%--<a href="#close" class="remove label label-important"><i--%>
                            <%--class="ace-icon fa fa-trash-o"></i>&nbsp;删除</a> <span--%>
                            <%--class="drag label"><i class="ace-icon fa fa-arrows"></i>&nbsp;拖动</span>--%>

                            <%--<div class="preview">--%>
                            <%--<input type="hidden" value="custom">--%>
                            <%--<input value="自定义布局" readonly="readonly" type="text">--%>
                            <%--</div>--%>
                            <%--<div class="view">--%>
                            <%--<div class="row-fluid clearfix ">--%>
                            <%--</div>--%>
                            <%--</div>--%>
                            <%--</div>--%>

                        </li>
                    </ul>
                </div>
            </div>

            <div class="group">
                <h3 class="accordion-header" title="交互组件">交互组件</h3>
                <div>
                    <ul class="nav nav-list accordion-group">
                        <li style="display: list-item;" class="rows">
                            <div class="lyrow lyrow-tab ui-draggable left-box">
                                <a href="#close" class="remove label label-important">
                                    <i class="ace-icon fa fa-trash-o"></i>&nbsp;删除
                                </a>
                                <span class="drag label"><i class="ace-icon fa fa-arrows"></i>&nbsp;拖动</span>
                                <div class="preview" title="Tab页"
                                     style="background-image: url('img/htmlicon/tab.png');height: 70px">
                                    <span class="title">Tab页</span>
                                </div>
                                <div class="view">
                                    <div class="tabbable">
                                        <ul class="nav nav-tabs">
                                            <li>
                                                <a class="tabs-plus"><i class="ace-icon fa fa-plus"></i></a>
                                            </li>
                                        </ul>
                                        <div class="tab-content"></div>
                                    </div>
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <!-- #accordion -->
    </div>
    <!-- ./span -->
</div>
<div class="container-fluid ui-layout-center">
    <div class="row-fluid">
        <!--/span-->
        <style id="demoCss" type="text/css"></style>
        <div class="demo ui-sortable editState" style="min-height: 304px;">
            <span style="display: none" id="dataSourceOption"></span>
        </div>
        <!-- end demo -->
        <!--/span-->
        <div id="download-layout">
            <div class="container-fluid"></div>
        </div>
    </div>
    <!--/row-->
</div>
<!--/.fluid-container-->
<div class="ui-layout-east">
    <div class="ui-layout-center">
        <div class="tabbable">
            <ul class="nav nav-tabs" id="settingTab">
                <li class="active"><a data-toggle="tab"
                                      href="#attributeSetting">属性框 </a></li>

                <li id="DSTab" class="hide"><a data-toggle="tab" href="#dataSetting">数据源绑定框 </a></li>
            </ul>
            <div class="tab-content">
                <div id="attributeSetting" class="tab-pane in active">
                    <div class="ui-widget-event">
                        <div id="eventSettingContent">
                            <button class="btn btn-xs btn-block" onclick="openCodeditor();">
                                <i class="ace-icon fa fa-plus-circle"></i> 绑定事件 <i
                                    class="ace-icon fa fa-code"></i>
                            </button>
                        </div>
                        <div id="attributeSettingContent"></div>
                    </div>
                </div>
                <div id="dataSetting" class="tab-pane">
                    <div id="dataSettingContent"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- ----------------------------------pop div---------------------------------------------------------->
<form id="detail" class="hide">
    <input type="hidden" id="detail-pageId" data-bxwidget
           data-bxtype="number"/>

    <div class="row rowspace">
        <div class="col-sm-12">
            <div class="form-group">
                <label class="col-sm-4 control-label no-padding-right labelfont"
                       for="detail-pageEname"><font style="color: red">* </font>
                    页面英文名 </label>

                <div class="col-sm-8">
                    <input type="text" id="detail-pageEname" name="detailpageEname"
                           class="col-sm-12" data-bxwidget data-bxtype="string"/>
                </div>
            </div>
        </div>
    </div>
    <div class="row rowspace">
        <div class="col-sm-12">
            <div class="form-group">
                <label class="col-sm-4 control-label no-padding-right labelfont"
                       for="detail-pageCname"><font style="color: red">* </font>
                    页面中文名 </label>

                <div class="col-sm-8">
                    <input type="text" id="detail-pageCname" name="detailpageCname"
                           class="col-sm-12" data-bxwidget data-bxtype="string"/>
                </div>
            </div>
        </div>
    </div>

    <div class="row rowspace">
        <div class="col-sm-12">
            <div class="form-group">
                <label class="col-sm-4 control-label no-padding-right labelfont"
                       for="detail-pageType"><font style="color: red">* </font>页面类型</label>

                <div class="col-sm-8">
                    <select id="detail-pageType" name="detailpageType"
                            class="col-sm-12 no-padding-left"
                            data-bxwidget="select" data-bxtype="string" required>
                        <option value="designPage">设计器页面</option>
                    </select>
                </div>
            </div>
        </div>
    </div>
    <div class="row rowspace">
        <div class="col-sm-12">
            <div class="form-group">
                <label class="col-sm-4 control-label no-padding-right labelfont"
                       for="detail-pagePath">页面路径 </label>

                <div class="col-sm-8">
                    <input type="text" id="detail-pagePath" name="detailpagePath"
                           class="col-sm-12" data-bxwidget data-bxtype="string"
                           value="路径自动生成" readonly="true"/>
                </div>
            </div>
        </div>
    </div>
    <div class="row rowLastSpace">
        <div class="col-sm-12">
            <div class="form-group">
                <label class="col-sm-4 control-label no-padding-right labelfont"
                       for="detail-pageDesc">页面描述 </label>

                <div class="col-sm-8">
                    <input type="text" id="detail-pageDesc" name="detailpageDesc"
                           class="col-sm-12" data-bxwidget data-bxtype="string"/>
                </div>
            </div>
        </div>
    </div>
</form>
<!-- ----------------------------------数据源编辑器---------------------------------------------------------->
<div id="dataSourceEditor" class="hide" style="overflow-x:hidden ">
    <div class="row">
        <div class="col-sm-7">
            <h4 class="header blue lighter smaller">
                数据源列表
                <span class="pull-right inline" style="border-left: 1px solid #D5E3EF;width: 90px;padding-left: 10px;">
                    <a href="javascript:void(0);" id="addDS" title="新增"><i class="ace-icon fa fa-plus"></i></a>
                    <a href="javascript:void(0);" id="confirmDS" title="确认"><i class="ace-icon fa fa-check"></i></a>
                    <a href="javascript:void(0);" id="deleteDS" title="删除"><i class="ace-icon fa fa-trash-o"></i></a>
                </span>
            </h4>
            <div class="col-xs-12">
                <div id="dataSourseGrid" style="width:100%"></div>
            </div>
        </div><!-- ./span -->
        <div class="col-sm-5">
            <h4 class="header blue lighter smaller">
                数据源参数配置
            </h4>
            <div class="row">
                <div class="col-xs-12" id="data_detail" style="display: none">
                    <div class="row">
                        <div class="col-sm-12">
                            <label for="dsName" title="数据源名称"
                                   class="col-sm-4 no-padding smaller-20"> 数据源名称</label><label
                                id="dsName" class="col-sm-8 no-padding smaller-20"></label>
                        </div>
                    </div>
                    <div class="hr hr-dotted hr8"></div>
                    <div id="sqldataScfg" style="display: none">
                        <div class="row">
                            <div class="col-sm-12">
                                <label title="sql模板名称" class="smaller-20 col-sm-4 no-padding">sql模板名称</label>
                                <input type="text" id='sqldataCode' data-attribute="sqlCode" data-level="sqlCode"
                                       class="col-sm-7"/>
                                <a href="javascript:void(0);" id="refreshSqlCol" title="刷新sql列" style="margin-left:5px"><i
                                        class="middle ace-icon fa fa-refresh"></i></a>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-12">
                                <label title="sqlCol" class="smaller-20 col-sm-4 no-padding">sql列预览</label>
                                <textarea type="text" readonly="readonly" id='sqlCol' data-attribute="sqlCol"
                                          data-level="sqlCol" class="col-sm-8 smaller-90"
                                          style="padding-left: 4px;max-width: 306px"></textarea>
                            </div>
                        </div>
                        <div class="hr hr-dotted hr8"></div>
                        <div class="row">
                            <div class="col-sm-12">
                                <button class="btn btn-xs btn-block" id="addsqlDataBtn">
                                    <i class="ace-icon fa fa-plus"></i>添加参数
                                </button>
                            </div>
                        </div>
                        <div id="sqloptionAttribute" style="display: none">
                            <div class="hr hr-dotted hr8"></div>
                            <div class="row">
                                <div class=" col-sm-12">
                                    <label title="参数名" class="smaller-20 col-sm-4 no-padding">参数名</label><input
                                        placeholder="如：参数名" class="col-sm-8" type="text"
                                        data-attribute="optionName" data-level="optionName"/>
                                </div>
                            </div>
                            <div class="row">
                                <div class=" col-sm-12">
                                    <label title="参数值" class="smaller-20 col-sm-4 no-padding">参数值</label>
                                    <input type="text" class="col-sm-8 treeInput" placeholder="如：参数值"
                                           data-attribute="optionValue" data-level="optionValue"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="pdsdataScfg" style="display: none">
                        <div class="row">
                            <div class=" col-sm-12">
                                <label title="查询算法" class="smaller-20 col-sm-4 no-padding">查询算法</label><select
                                    class="col-sm-8" data-attribute="dataValueType" data-level="dataValueType">
                                <option value="interpolation">插值查询</option>
                                <option value="snapshot">snapshot</option>
                            </select>
                            </div>
                        </div>
                        <div id="pdsAttribute">
                            <div class="row">
                                <div class=" col-sm-12">
                                    <label title="实例值关联组件" class="smaller-20 col-sm-4 no-padding">实例名称</label>
                                    <input type="text" class="col-sm-8 treeInput" placeholder="请选择或输入"
                                           data-attribute="instance" data-level="instance"/>
                                </div>
                            </div>
                            <div class="row">
                                <div class=" col-sm-12">
                                    <label title="属性值关联组件" class="smaller-20 col-sm-4 no-padding">属性名称</label>
                                    <input type="text" class="col-sm-8 treeInput" placeholder="请选择或输入"
                                           data-attribute="attribute" data-level="attribute" data-type="select"/>
                                </div>
                            </div>
                            <div class="hr hr-dotted hr8"></div>
                            <div class="interpolationOptions">
                                <div class="row">
                                    <div class=" col-sm-12">
                                        <label title="开始时间关联组件" class="smaller-20 col-sm-4 no-padding">起始时间</label>
                                        <div id="pdsdataS_startTime" data-bxwidget="bxtimepicker"
                                             class="selectinput input-group"></div>
                                        <input type="text" class="col-sm-8 treeInput"
                                               data-time="pdsdataS_startTime_dateInput" data-attribute="startTime"
                                               data-level="startTime"/>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class=" col-sm-12">
                                        <label title="结束时间关联组件" class="smaller-20 col-sm-4 no-padding">结束时间</label>
                                        <div id="pdsdataS_endTime" data-bxwidget="bxtimepicker"
                                             class="selectinput input-group"></div>
                                        <input type="text" class="col-sm-8 treeInput"
                                               data-time="pdsdataS_endTime_dateInput" data-attribute="endTime"
                                               data-level="endTime"/>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class=" col-sm-12">
                                        <label title="时间间隔是具体的毫秒值"
                                               class="smaller-20 col-sm-4 no-padding">时间间隔</label>
                                        <input id="pdsDSintervals" type="text" class="col-sm-8 treeInput"
                                               placeholder="请选择或输入" data-attribute="intervals" data-level="intervals"/>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class=" col-sm-12">
                                    <label title="时间返回格式是对查询出来的时间进行格式化，它的格式：时间段之间“-”分隔，yyyy-MM-dd HH:mm:ss从左到右依次代表：年-月-日 时:分:秒"
                                           class="smaller-20 col-sm-4 no-padding">时间返回格式</label>
                                    <input placeholder="如:yyyy-MM-dd HH:mm:ss" type="text" class="col-sm-8"
                                           data-attribute="returnFormatter" data-level="returnFormatter"/>
                                </div>
                            </div>
                            <div class="interpolationOptions">
                                <div class="hr hr-dotted hr8"></div>
                                <div class="row">
                                    <div class=" col-sm-12">
                                        <label title="用于限制查询数据返回的个数，最大记录数不可超过改数值，只支持数字"
                                               class="smaller-20 col-sm-4 no-padding">记录返回个数</label>
                                        <input id="limit" type="text" class="col-sm-8 treeInput" data-attribute="limit"
                                               data-level="limit"/>
                                    </div>
                                    <div class=" col-sm-12">
                                        <label title="如为5，那么从开始时间查询出的第5个值开始返回，只支持数字"
                                               class="smaller-20 col-sm-4 no-padding">位移个数</label>
                                        <input id="offset" type="text" class="col-sm-8 treeInput"
                                               data-attribute="offset" data-level="offset"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="ccsdataScfg" style="display: none">
                        <div class="row">
                            <div class=" col-sm-12">
                                <label title="ccsId关联组件" class="smaller-20 col-sm-4 no-padding">ccsId</label>
                                <input type="text" class="col-sm-8" data-attribute="ccsId" data-level="ccsId"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- ----------------------------------信息弹出框---------------------------------------------------------->
<div id="dialog-delect-message" class="hide">
</div>

</body>
</html>
