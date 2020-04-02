<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>项目信息管理</title>
    <style>
        .dropdown-menu{
            z-index: 1051!important;
        }
        .bootstrap-timepicker{
            left: 10px;
        }
    </style>
</head>
<body>
<%@ include file="/bxui/bxuihead.jsp" %>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/daterangepicker/css/daterangepicker.css"/>
<link href="<%=toolkitPath%>/bxui/aceadmin/style/css/ace.css" rel="stylesheet" type="text/css">
<link href="<%=toolkitPath%>/bxui/date-time/css/bootstrap-timepicker.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/moment.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/daterangepicker.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/error/assets/js/uncompressed/date-time/bootstrap-timepicker.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>

<script type="text/javascript" src="<%=toolkitPath%>/bxui/easyui-plugin/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/loadsave.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/error/assets/js/uncompressed/date-time/bootstrap-timepicker.js"></script>
<script src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css"/>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/easyui-plugin/css/easyui.css"/>


<div class="jqgridFrame contentFrame">
    <div class="page-content contentFrame">
        <!-- /sectionController:settings.box -->
        <div class="page-header" style="height: 50px;">
            <h1 class="pull-left">
                项目信息管理
                <small><i class="ace-icon fa fa-angle-double-right"></i>
                    对项目信息管理进行注册和相关配置
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
                               for="inqu_status-sectionCcsId">线路</label>
                        <select id="inqu_status-sectionCcsId" data-bxwidget class="inputThree">
                            <option value="">全部</option>
                        </select>
                    </div>
                    <div class="col-sm-2 col-xs-12">
                        <label class="labelThree"
                               for="inqu_status-sectionOwner">业主</label>
                        <select id="inqu_status-sectionOwner" data-bxwidget class="inputThree">
                            <option value="">全部</option>
                        </select>
                    </div>

                    <div class="col-sm-2 col-xs-12">
                        <label class="labelThree"
                               for="inqu_status-ccsSectionType">区间类型</label>
                        <select id="inqu_status-ccsSectionType" data-bxwidget class="inputThree">
                            <option value="">全部</option>
                        </select>
                    </div>
                    <div class="col-sm-2 col-xs-12">
                        <label class="labelThree"
                               for="inqu_status-supervisor">监理单位</label>
                        <select id="inqu_status-supervisor" data-bxwidget class="inputThree">
                            <option value="">全部</option>
                        </select>
                    </div>
                    <div class="col-sm-2 col-xs-12">
                        <label class="labelThree"
                               for="inqu_status-buildUnit">施工单位</label>
                        <select id="inqu_status-buildUnit" data-bxwidget class="inputThree">
                            <option value="">全部</option>
                        </select>
                    </div>
                </div>
                <div class="row">

                    <div class="col-sm-2 col-xs-12">
                        <label class="labelThree"
                               for="inqu_status-factory">厂家</label>
                        <select id="inqu_status-factory" data-bxwidget class="inputThree">
                            <option value="">全部</option>
                        </select>
                    </div>
                    <div class="col-sm-2 col-xs-12">
                        <label class="labelThree"
                               for="inqu_status-tbmOwner">拥有单位</label>
                        <select id="inqu_status-tbmOwner" data-bxwidget class="inputThree">
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
                               for="inqu_status-tbmCcsType">盾构机类型</label>
                        <select id="inqu_status-tbmCcsType" data-bxwidget class="inputThree">
                            <option value="">全部</option>
                        </select>
                    </div>

                </div>
                <div class="row">
                    <div class="col-sm-2 col-xs-12">
                        <label class="labelThree"
                               for="inqu_status-projectName">项目名称</label>
                        <input type="text" id="inqu_status-projectName" data-bxwidget class="inputThree" onkeyup="projectNameKeyUp(this)" />
                    </div>
                    <div class="col-sm-2 col-xs-12">
                        <label class="labelThree"
                               for="inqu_status-collectorName">采集器名称</label>
                        <input type="text" id="inqu_status-collectorName" data-bxwidget class="inputThree" onkeyup="collectorNameKeyUp(this)"  />
                    </div>
                    <div class="col-sm-2 col-xs-12">
                        <label class="labelThree"
                               for="inqu_status-sectionName">区间名称</label>
                        <input type="text" id="inqu_status-sectionName" data-bxwidget class="inputThree" onkeyup="sectionNameKeyUp(this)"/>
                    </div>
                    <div class="col-sm-2 col-xs-12">
                        <label class="labelThree"
                               for="inqu_status-tbmName">盾构机名称</label>
                        <input type="text" id="inqu_status-tbmName" data-bxwidget class="inputThree" onkeyup="tbmNameKeyUp(this)"/>
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
                    <!-- 和上div同行，位于行右 -->
                    <div class="col-sm-6 col-md-6 col-xs-12 no-padding">
                        <div class="col-sm-4 col-md-3 col-xs-6">
                            <button class="btn btn-sm btn-block" onclick="on_grantresource_click();">
                                <div class="ace-icon fa fa-key"></div>
                                <span>注册资源</span>
                            </button>
                        </div>
                        <div class="col-sm-3 col-md-3 col-xs-6">
                            <button class="btn btn-sm btn-block" onclick="clearSelect();">
                                <div class="ace-icon fa fa-search"></div>
                                <span>清空查询条件</span>
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
    <%--详细信息--%>
    <div id="editProjectDetail" class="hide row" style="margin:0px;">
        <form class="form-horizontal" role="form">
            <div class="form-group hide" style="margin:0px 0px 15px 0px;">
                <div class="col-sm-9">
                    <input type="text" id="projectId" name="projectId" value="" class=".input-large" />
                </div>
                <div class="col-sm-9">
                    <input type="text" id="sectionId" name="sectionId" value="" class=".input-large" />
                </div>
            </div>
            <div class="form-group" style="margin:0px 0px 15px 0px;">
                <div class="col-sm-6" style="float: left;">
                    <label class="col-sm-4 control-label no-padding-right" for="tbmId">盾构机名称<span style="color: red">*</span> </label>
                    <input class="col-sm-7" style="margin-left: 10px;" type="text" id="tbmId" name="tbmId" value="" class=".input-large" />

                </div>
                <div class="col-sm-6">
                    <label class="col-sm-4 control-label no-padding-right" for="status"> 项目状态<span style="color: red">*</span> </label>
                    <select class="col-sm-7 easyui-combobox" style="margin-left: 10px;padding:3px 5px;"  name="status" id="status"></select>
                </div>
            </div>
            <div class="form-group" style="margin:0px 0px 15px 0px;">
                <div class="col-sm-6" style="float: left;">
                    <label class="col-sm-4 control-label no-padding-right" for="startMileage">起始里程(m)<span style="color: red">*</span> </label>
                    <input class="col-sm-7" style="margin-left: 10px;" type="text" id="startMileage" name="startMileage" value="" class=".input-large" />
                </div>
                <div class="col-sm-6">
                    <label class="col-sm-4 control-label no-padding-right" for="endMileage"> 结束里程(m)<span style="color: red">*</span> </label>
                    <input class="col-sm-7" style="margin-left: 10px;" type="text" id="endMileage" name="endMileage" value="" class=".input-large" />
                </div>
            </div>
            <div class="form-group" style="margin:0px 0px 15px 0px;">
                <div class="col-sm-6" style="float: left;">
                    <label class="col-sm-4 control-label no-padding-right" for="totalLength">总长度<span style="color: red">*</span> </label>
                    <input class="col-sm-7" style="margin-left: 10px;" type="text" id="totalLength" name="totalLength" value="" class=".input-large" />
                </div>
                <div class="col-sm-6">
                    <label class="col-sm-4 control-label no-padding-right" for="geologyInfo"> 地质信息 </label>
                    <input class="col-sm-7" style="margin-left: 10px;" type="text" id="geologyInfo" name="geologyInfo" value="" class=".input-large" />
                </div>
            </div>
            <div class="form-group" style="margin:0px 0px 15px 0px;">
                <div class="col-sm-6" style="float: left;">
                    <label class="col-sm-4 control-label no-padding-right" for="projectSituation">项目概况 </label>
                    <input class="col-sm-7" style="margin-left: 10px;" type="text" id="projectSituation" name="projectSituation" value="" class=".input-large" />
                </div>
                <div class="col-sm-6">
                    <label class="col-sm-4 control-label no-padding-right" for="projectLocation"> 项目地点 </label>
                    <input class="col-sm-7" style="margin-left: 10px;" type="text" id="projectLocation" name="projectLocation" value="" class=".input-large" />
                </div>
            </div>
            <div class="form-group" style="margin:0px 0px 15px 0px;">
                <div class="col-sm-6" style="float: left;">
                    <label class="col-sm-4 control-label no-padding-right" for="startTime">开始时间<span style="color: red">*</span> </label>
                    <input class="col-sm-7" style="margin-left: 10px;" type="text" id="startTime" name="startTime" value="" class=".input-large" />
                </div>
                <div class="col-sm-6">
                    <label class="col-sm-4 control-label no-padding-right" for="endTime"> 结束时间<span style="color: red">*</span> </label>
                    <input class="col-sm-7" style="margin-left: 10px;" type="text" id="endTime" name="endTime" value="" class=".input-large" />
                </div>
            </div>
            <div class="form-group" style="margin:0px 0px 15px 0px;">
                <div class="col-sm-6" style="float: left;">
                    <label for="dayShiftStart" class="col-sm-4 control-label no-padding-right">白班开始时间<span style="color: red">*</span> </label>
                    <div class="input-group bootstrap-timepicker col-sm-7">
                        <input id="dayShiftStart" type="text" class="form-control"   />
                        <span class="input-group-addon"> <i class="fa fa-clock-o bigger-110"></i></span>
                    </div>
                </div>
                <div class="col-sm-6">
                    <label class="col-sm-4 control-label no-padding-right" for="dayShiftEnd"> 白班结束时间<span style="color: red">*</span> </label>
                    <div class="input-group bootstrap-timepicker col-sm-7">
                        <input id="dayShiftEnd" type="text" class="form-control" />
                        <span class="input-group-addon"> <i class="fa fa-clock-o bigger-110"></i></span>
                    </div>
                </div>
            </div>
            <div class="form-group" style="margin:0px 0px 15px 0px;">
                <div class="col-sm-6" style="float: left;">
                    <label class="col-sm-4 control-label no-padding-right" for="supervisor">监理单位 </label>
                    <input class="col-sm-7" style="margin-left: 10px;" type="text" id="supervisor" name="supervisor" value="" class=".input-large" />
                </div>
                <div class="col-sm-6" style="float: left;">
                    <label class="col-sm-4 control-label no-padding-right" for="templateName">模板名称<span style="color: red">*</span>  </label>
                    <select class="col-sm-7 easyui-combobox" style="margin-left: 10px;padding:3px 5px;"  name="templateName" id="templateName"></select>
                </div>
            </div>
        </form>
        <div id="confrmEdit" class="hide row" style="margin:0px;">确定保存么？</div>
    </div>

</div>
<script type="text/javascript" src="<%=toolkitPath%>/raising/backstage/projectManage.js" />
<script type="text/javascript">
    function initCol(colNames,colModels) {
        colNames.push('操作');
        var  classification

    }

</script>

</body>

</html>

