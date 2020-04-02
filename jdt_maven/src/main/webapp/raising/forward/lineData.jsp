<%--
  Created by IntelliJ IDEA.
  User: ysy
  Date: 2018/7/2
  Time: 9:48
  Description: 
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.baosight.aas.auth.Constants" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <title>线型数据</title>
    <%
        String path = request.getContextPath();
        String tenantid = request.getSession().getAttribute(Constants.SESSION_TENANTID_KEY) + "";
        String first = java.net.URLDecoder.decode(request.getParameter("firstLevel"),"UTF-8");
        String second = java.net.URLDecoder.decode(request.getParameter("secondLevel"),"UTF-8");
    %>

    <meta name="description" content="platForm Frameset"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <style >
        body{
            background-color: white;
        }
        .ui-jqgrid-lastTh{
            float: right;
            margin: -2px -2px -2px 0;
            height: 42px !important;
        }
        .changecolor{
            background: #2184C1;
            background-color: #2184C1;
        }
        .tableTile{
            line-height: 24px;
            font-size: 15px;
            margin-top: 7px;
        }
        th{
            background-color: #F1F1F1;
            line-height: 24px!important;
            font-size: 13px;
            font-weight:700!important;
            text-align: center!important;
        }
        character-style{
            line-height: 24px;
            font-size: 13px;
            font-weight:700
        }
        #drawingTableBody td,#lineBody td{
            text-align:center;
            background-color: #FFFFFF;
            font-size: 13px;

        }
        .btn.btn-sm.btn-primary{
            font-size: 13px;
            padding:2px!important;
            margin: 0px!important;
            border-bottom: 0px!important;
            border-top: 0px!important;
        }
        /*设置Jqgird表格样式*/
        .ui-jqgrid-sortable {text-align: center;}
        .ui-jqgrid tr.jqgrow td {font-size:13px; }
    </style>
</head>
<body>
<%@ include file="/bxui/bxuihead.jsp"%>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.form.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/raphael.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.mousewheel.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raisingui/jquery.serializejson.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-form.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/selectLine.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/lineData.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/easyui-plugin/js/jquery.easyui.min.js"></script>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/easyui-plugin/css/easyui.css"/>


<input type="hidden" value="<%=tenantid%>" id="tenantid">
<div class="page-content" id="page-content" style="position:inherit">
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
    <div class="row">
        <div class="col-md-7">
            <div class="row" style="margin-left: 1px">
                <div id="blueprintDiv" class="hide row" style="margin:0px;">
                    <form class="form-horizontal" id="blueprintForm" role="form" enctype="multipart/form-data" accept-charset="utf-8" onsubmit="document.charset='utf-8'">
                        <div class="form-group" style="margin:0px 0px 15px 0px;">
                            <div class="col-sm-12" style="float: left;">
                                <label class="col-sm-4 control-label no-padding-right" for="info">图纸说明<span style="color: red">*</span> </label>
                                <input class="col-sm-7" style="margin-left: 10px;" required="required" type="text" id="info" name="info" value="" class=".input-large" />
                                <input class="hide" id="projectName" name="projectName" value="" class=".input-large" />
                            </div>
                        </div>
                        <div class="form-group" style="margin:0px 0px 15px 0px;">
                            <div class="col-sm-12" style="float: left;">
                                <label class="col-sm-4 control-label no-padding-right" for="blueprint">选择文件(zip)<span style="color: red">*</span> </label>
                                <input class="col-sm-7" style="margin-left: 10px;" type="file" id="blueprint" name="blueprint" value="" required class=".input-large" />
                            </div>
                        </div>
                    </form>
                    <div id="confrmEdit" class="hide row" style="margin:0px;">确定</div>
                </div>
                <button class="btn btn-primary changecolor"  onclick="bulueprintButton()">蓝图上传</button>
                <button class="btn btn-primary" onclick="deletedata(1)">删除</button>
            </div>
            <div class="row">
                <div class="col-md-12 widget-container-col">
                <div class="widget-box widget-color-blue">
                    <!-- #sectionController:custom/widget-box.options -->
                    <div class="widget-header" style="background-color: #2184C1;">
                        <div class="tableTile" >
                        <span class="widget-title bigger lighter " >
                            蓝图信息
                        </span>
                        </div>
                    </div>

                    <!-- /sectionController:custom/widget-box.options -->
                    <div class="widget-body">
                        <div class="widget-main no-padding">
                            <table class="table table-striped table-bordered table-hover">
                                <thead class="thin-border-bottom">
                                <tr>
                                    <th>
                                        图纸名称
                                    </th>

                                    <th>
                                        图纸说明
                                    </th>
                                    <th>
                                        上传时间
                                    </th>
                                    <th>
                                        操作
                                    </th>
                                </tr>
                                </thead>

                                <tbody id="drawingTableBody">
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            </div>
            <div class="row" style="margin-left: 1px">
                <div id="lineDiv" class="hide row" style="margin:0px;">
                    <form class="form-horizontal" role="form" id="lineForm">
                        <div class="form-group" style="margin:0px 0px 15px 0px;">
                            <div class="col-sm-6" style="float: left;">
                                <label class="col-sm-4 control-label no-padding-right" for="personName">姓名<span style="color: red">*</span> </label>
                                <input class="col-sm-7" style="margin-left: 10px;" type="text" id="personName" name="personName" value="" class=".input-large" />
                            </div>
                            <div class="col-sm-6">
                                <label class="col-sm-4 control-label no-padding-right" for="telephone">联系方式<span style="color: red">*</span> </label>
                                <input class="col-sm-7" style="margin-left: 10px;" type="text" id="telephone" name="telephone" value="" class=".input-large" />
                            </div>
                        </div>
                        <div class="form-group" style="margin:0px 0px 15px 0px;">
                            <div class="col-sm-6" style="float: left;">
                                <label class="col-sm-4 control-label no-padding-right" for="personCompany">单位<span style="color: red">*</span></label>
                                <input class="col-sm-7" style="margin-left: 10px;" type="text" id="personCompany" name="personCompany" value="" class=".input-large" />
                            </div>
                            <div class="col-sm-6">
                                <label class="col-sm-4 control-label no-padding-right" for="personTitle">职位<span style="color: red">*</span></label>
                                <input class="col-sm-7" style="margin-left: 10px;" type="text" id="personTitle" name="personTitle" value="" class=".input-large" />
                            </div>
                        </div>
                        <div class="form-group" style="margin:0px 0px 15px 0px;">
                            <div class="col-sm-6">
                                <label class="col-sm-4 control-label no-padding-right" for="lineFile"> 文件(xlsx)<span style="color: red">*</span> </label>
                                <input class="col-sm-7" style="margin-left: 10px;" type="file" id="lineFile" name="lineFile"  class=".input-large" />
                            </div>
                            <div class="col-sm-6" style="float: left;">
                                <label class="col-sm-4 control-label no-padding-right" for="remarks">备注</label>
                                <input class="col-sm-7" style="margin-left: 10px;" type="text" id="remarks" name="remarks" value="" class=".input-large" />
                            </div>
                        </div>
                        <div class="form-group" style="margin:0px 0px 15px 0px;">
                            <div class="col-sm-6">
                                <label class="col-sm-4 control-label no-padding-right" for="drawing"> 图纸<span style="color: red">*</span> </label>
                                <input class="col-sm-7" style="margin-left: 10px;" type="text" id="drawing" name="drawing" value="" class=".input-large" />
                            </div>

                        </div>
                    </form>
                    <div id="lineConfirm" class="hide row" style="margin:0px;">确定</div>
                </div>
                <button class="btn btn-primary" onclick="lineButton()">线型上传</button>
                <button onclick="showCheckPage()" id="check" class="btn btn-primary">线型对比</button>
                <button class="btn btn-primary" onclick="deletedata(2)">删除</button>
            </div>
            <div class="row">
                <div class="col-md-12 widget-container-col">
                    <div class="widget-box widget-color-blue">
                        <!-- #sectionController:custom/widget-box.options -->
                        <div class="widget-header" style="background-color: #2184C1;">
                            <div class="tableTile">
                                <span class="widget-title bigger lighter tableTile">
                                    线型详情
                                </span>
                            </div>
                        </div>
                        <div class="widget-body">
                            <div class="widget-main no-padding">
                                <table class="table table-striped table-bordered table-hover">
                                    <thead class="thin-border-bottom">
                                    <tr>
                                        <th>
                                            <input type="checkbox" name="" value="" disabled />
                                        </th>
                                        <th>
                                            姓名
                                        </th>
                                        <th>
                                            单位
                                        </th>
                                        <th>
                                            职位
                                        </th>
                                        <th>
                                            联系方式
                                        </th>
                                        <th>
                                            图纸名称
                                        </th>
                                        <th>
                                            上传时间
                                        </th>
                                        <th>
                                            操作
                                        </th>
                                        <th>
                                            复核结果
                                        </th>
                                    </tr>
                                    </thead>

                                    <tbody id="lineBody">
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-5">
            <div id="alertdiv" class="alertdiv" style="height: 30px;padding:5px 15px;margin-bottom: 10px;"></div>
            <div id="gridImage"></div>
        </div>

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

<div id="checkPage" class="hide" style="overflow: hidden">

    <div class="row" style="margin-bottom: 3px;">
        <div class="col-xs-2" style="min-width: 200px;">
            <input id="id-button-borders" checked="" type="checkbox" class="ace ace-switch ace-switch-5"/>
            <span class="lbl middle"></span>
            <div style="display: inline-block;padding-top: 5px;"><span style="padding-left: 2px;">只显示超限记录</span></div>
        </div>
        <div class="col-xs-3" >
            复核结果：
            <select id="reviewSelect" onchange="reviewChange()">
                <option value="未复核">未复核</option>
                <option value="复核无误">复核无误</option>
                <option value="复核有误">复核有误</option>
            </select>
        </div>
        <%--<div class="col-xs-2" >
            <button class='btn btn-sm btn-primary' id="compareResult" onclick='downloadCompare()'>下载对比结果</button>
        </div>--%>
        <div class="area" style="float: right;margin-right: 50px">
            <button class='btn btn-sm btn-primary' id="compareResult" onclick='downloadCompare()'>下载对比结果</button>
            允许误差(mm)：
            <select id="errorSelect">
                <option value="1" selected>1</option>
                <option value="5">5</option>
                <option value="10">10</option>
                <option value="15">15</option>
            </select>
        </div>
    </div>
    <div class="row" style="padding-right: 50px;text-align: right;">
        <span id="prompt" style="color: red;"></span>
    </div>
    <div class="row">
        <div id="infodiv" class="alertdiv" style="height: 30px;padding:5px 15px;margin-bottom: 10px;"></div>


        <div class="col-xs-12">
            <div id="groupColGrid"></div>

        </div>
    </div>
</div>
</body>
</html>
