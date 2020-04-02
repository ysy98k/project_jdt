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
    <title>项目信息</title>
    <%
        String path = request.getContextPath();
        String tenantid = request.getSession().getAttribute(Constants.SESSION_TENANTID_KEY) + "";
        String first = java.net.URLDecoder.decode(request.getParameter("firstLevel"),"UTF-8");
        String second = java.net.URLDecoder.decode(request.getParameter("secondLevel"),"UTF-8");
    %>

    <meta name="description" content="platForm Frameset"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <style>
        .labelStyle{
            width: 30%;text-align: right;
            margin-right: 10px;
        }
        .spanS{
            width: 50%;
            max-width: 250px;
        }
        .inputS{
            width: 100%;
        }
        .bai{
            background-color: rgb(255, 255, 255);
        }
        .divStyle{
            width:60%;text-align: left;;display: inline-block;
        }
        .marginB{
            margin-bottom: 10px;
        }
        .dayShift{
            width: 30%!important;text-align: right;margin-top: 7px;
        }
    </style>
</head>

<body class="bai">
<%@ include file="/bxui/bxuihead.jsp" %>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/date-time/css/bootstrap-datetimepicker.css">
<link href="<%=toolkitPath%>/bxui/aceadmin/style/css/ace.css" rel="stylesheet" type="text/css">
<link href="<%=toolkitPath%>/bxui/date-time/css/bootstrap-timepicker.css" rel="stylesheet" type="text/css">


<input type="hidden" value="<%=tenantid%>" id="tenantid" />
<div id="father" class="page-content" style="position:inherit;min-height:450px">
    <!-- /sectionController:settings.box -->
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
    </div>
    <!-- 内容-->
    <div class="row">
        <div class="col-sm-12">
            <div class="widget-box">
                <div class="widget-header" style="padding-top: 13px;">
                    <h4 class="widget-title" >项目信息</h4>
                </div>

                <div class="widget-body" style="margin: 0 auto;max-width: 1100px;" >
                    <div class="widget-main no-padding">
                        <form id="projectForm"  >
                            <div class="form-group bai" style="margin:0px 0px 15px 0px;">
                                <div class="col-sm-6 marginB" style="float: left;">
                                    <label class="control-label no-padding-right labelStyle" for="projectName"  >项目名称<span style="color: red">*</span> </label>
                                    <span class="input-icon input-icon-right spanS" >
                                        <input type="text" id="projectName" name="projectName" v-model="projectName" class="inputS" /> <i class="ace-icon fa fa-leaf green"></i>
                                    </span>
                                </div>
                                <div class="col-sm-6 marginB">
                                    <label class="control-label no-padding-right labelStyle" for="sectionName">区间名称<span style="color: red">*</span> </label>
                                    <span class="input-icon input-icon-right spanS"  >
                                        <input type="text" id="sectionName" name="sectionName" v-model="sectionName" readonly class="inputS"/> <i class="ace-icon fa fa-leaf green"></i>
                                    </span>
                                </div>
                            </div>
                            <div class="form-group bai" style="margin:0px 0px 15px 0px;">
                                <div class="col-sm-6 marginB" style="float: left;">
                                    <label class="control-label no-padding-right labelStyle" for="tbmName">盾构机名称<span style="color: red">*</span> </label>
                                    <span class="input-icon input-icon-right spanS" >
                                        <input type="text" id="tbmName" v-model="tbmName" name="tbmName" readonly class="inputS"/> <i class="ace-icon fa fa-leaf green"></i>
                                    </span>
                                </div>
                                <div class="col-sm-6 marginB">
                                    <label class="control-label no-padding-right labelStyle" for="collectorName">采集器名称<span style="color: red">*</span> </label>
                                    <span class="input-icon input-icon-right spanS" >
                                        <input type="text" id="collectorName" name="collectorName" v-model="collectorName" class="inputS" readonly/> <i class="ace-icon fa fa-leaf green"></i>
                                    </span>
                                </div>

                            </div>
                            <div class="form-group bai" style="margin:0px 0px 15px 0px;">
                                <div class="col-sm-6 marginB" style="float: left;">
                                    <label class="control-label no-padding-right labelStyle" for="startMileage">起始里程(m)<span style="color: red">*</span> </label>
                                    <span class="input-icon input-icon-right spanS" >
                                        <input type="text" id="startMileage" name="startMileage" v-model="startMileage" class="inputS"/> <i class="ace-icon fa fa-leaf green"></i>
                                    </span>
                                </div>
                                <div class="col-sm-6 marginB">
                                    <label class="control-label no-padding-right labelStyle" for="endMileage">结束里程(m)<span style="color: red">*</span> </label>
                                    <span class="input-icon input-icon-right spanS" >
                                        <input type="text" id="endMileage" name="endMileage" v-model="endMileage" class="inputS"/> <i class="ace-icon fa fa-leaf green"></i>
                                    </span>

                                </div>
                            </div>
                            <div class="form-group" >
                                <div class="col-sm-6 marginB" style="float: left;">
                                    <label class="control-label no-padding-right labelStyle" for="totalLength">总长度<span style="color: red">*</span> </label>
                                    <span class="input-icon input-icon-right spanS" >
                                        <input type="text" id="totalLength" name="totalLength" v-model="totalLength" class="inputS"/> <i class="ace-icon fa fa-leaf green"></i>
                                    </span>
                                </div>
                                <div class="col-sm-6 marginB" >
                                    <label class="control-label no-padding-right labelStyle" for="ringTotal">总环数<span style="color: red">*</span> </label>
                                    <span class="input-icon input-icon-right spanS" >
                                        <input type="text" id="ringTotal" name="ringTotal" v-model="ringTotal" class="inputS"/> <i class="ace-icon fa fa-leaf green"></i>
                                    </span>
                                </div>
                            </div>
                            <div class="form-group" >
                                <div class="col-sm-6 marginB" style="float: left;">
                                    <label class="control-label no-padding-right labelStyle" for="projectSituation">项目概况</label>
                                    <span class="input-icon input-icon-right spanS" >
                                        <input type="text" id="projectSituation" name="projectSituation" v-model="projectSituation" class="inputS"/> <i class="ace-icon fa fa-leaf green"></i>
                                    </span>
                                </div>
                                <div class="col-sm-6 marginB">
                                    <label class="control-label no-padding-right labelStyle" for="projectLocation">项目地点</label>
                                    <span class="input-icon input-icon-right spanS" >
                                        <input type="text" id="projectLocation" name="projectLocation" v-model="projectLocation" class="inputS"/> <i class="ace-icon fa fa-leaf green"></i>
                                    </span>
                                </div>
                            </div>
                            <div class="form-group" style="margin:0px 0px 15px 0px;">
                                <div class="col-sm-6 marginB" style="float: left;">
                                    <label class="control-label no-padding-right labelStyle" for="startTime">开始时间</label>
                                    <span class="input-icon input-icon-right spanS" >
                                        <input type="text" id="startTime" name="startTime" v-model="startTime" class="inputS date-picker"/> <i class="ace-icon fa fa-leaf green"></i>
                                    </span>
                                </div>
                                <div class="col-sm-6 marginB">
                                    <label class="control-label no-padding-right labelStyle" for="endTime">结束时间</label>
                                    <span class="input-icon input-icon-right spanS" >
                                        <input type="text" id="endTime" name="endTime" v-model="endTime" class="inputS"/> <i class="ace-icon fa fa-leaf green"></i>
                                    </span>
                                </div>
                            </div>
                            <div class="form-group" style="margin:0px 0px 15px 0px;">
                                <div class="col-sm-6 marginB" style="float: left;">
                                    <label class="labelStyle" for="supervisor">监理单位</label>
                                    <span class="input-icon input-icon-right spanS" style="width: 300px;" >
                                        <input type="text" id="supervisor" name="supervisor" v-model="supervisor" class="inputS"/> <i class="ace-icon fa fa-leaf green"></i>
                                    </span>

                                </div>
                                <div class="col-sm-6 marginB">
                                    <label class="labelStyle" for="buildUnit"> 施工单位<span style="color: red">*</span> </label>
                                    <span class="input-icon input-icon-right spanS" >
                                        <input type="text" id="buildUnit" name="buildUnit" v-model="buildUnit"  class="inputS" /> <i class="ace-icon fa fa-leaf green"></i>
                                    </span>
                                </div>
                            </div>
                            <div class="form-group" style="margin:0px 0px 15px 0px;">
                                <div class="col-sm-6 marginB" style="float: left;">
                                    <label class="col-sm-4 control-label no-padding-right dayShift" for="dayShiftStart">白班开始时间<span style="color: red">*</span> </label>
                                    <div class="input-group bootstrap-timepicker " style="width: 251px;left: 12px;">
                                        <input id="dayShiftStart" type="text" class="form-control"/>
                                        <span class="input-group-addon"> <i class="fa fa-clock-o bigger-110"></i></span>
                                    </div>
                                </div>
                                <div class="col-sm-6 marginB">
                                    <label class="col-sm-4 control-label no-padding-right dayShift" for="dayShiftEnd">白班结束时间<span style="color: red">*</span> </label>
                                    <div class="input-group bootstrap-timepicker col-sm-7" style="width: 251px;left: 12px;">
                                        <input id="dayShiftEnd" type="text" class="form-control" />
                                        <span class="input-group-addon"> <i class="fa fa-clock-o bigger-110"></i></span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group" style="margin:0px 0px 15px 0px;" >
                                <div class="col-sm-6 marginB" style="float: left;">
                                    <label class="control-label no-padding-right labelStyle" for="geologyInfo">地质信息</label>
                                    <span class="input-icon input-icon-right spanS" >
                                        <input type="text" id="geologyInfo" name="geologyInfo" v-model="geologyInfo" class="inputS"/> <i class="ace-icon fa fa-leaf green"></i>
                                    </span>
                                </div>
                            </div>
                            <div class="clearfix form-actions bai" style="background-color:rgb(255, 255, 255);border-width: 0px;">
                                <div class="col-md-offset-6 col-md-6">
                                    <button type="button" class="btn btn-sm btn-success"  onclick="updateProject()">
                                        <i class="ace-icon fa fa-check bigger-110"></i> 修改
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- ----------------------------------信息弹出框---------------------------------------------------------->
<div id="updateConfirm" class="hide">
    <span>确认修改么？</span>
</div>
<div class="alertDiv hide"></div>

<div id="detail" class="hide" style="overflow: hidden">
    <div type="text" id = "div_city" name = "city" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
    <div type="text" id = "div_line" name = "line" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
    <div type="text" id = "div_program" name = "program" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
</div>

<script type="text/javascript" src="<%=toolkitPath%>/bxui/vue/vue.min.js"></script>
<script src="<%=toolkitPath%>/bxui/date-time/bootstrap-datetimepicker.js"></script>
<script src="<%=toolkitPath%>/bxui/date-time/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<%--
<script type="text/javascript" src="<%=toolkitPath%>/df/error/assets/js/uncompressed/date-time/bootstrap-timepicker.js"></script>
--%>

<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.form.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxdialog.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxcombobox.js"></script>
<script type="text/javascript" src="<%=path%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-form.js"></script>
<script src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/selectLine.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/projectManageForward.js"></script>

</body>
</html>
