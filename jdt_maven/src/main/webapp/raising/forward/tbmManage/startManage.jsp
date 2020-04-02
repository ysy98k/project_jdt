<%--
  Created by IntelliJ IDEA.
  User: ysy
  Date: 2018/8/8
  Time: 11:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.baosight.aas.auth.Constants" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>始发管理</title>
    <%
        String path = request.getContextPath();
        String tenantid = request.getSession().getAttribute(Constants.SESSION_TENANTID_KEY) + "";
    %>

    <meta name="description" content="platForm Frameset"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <style>

        [v-cloak]{ display: none; }

        html,body,#father{
            height:100%;
        }

        .dataTable:last-child{
            border-bottom: 1px rgb(225, 225, 225) solid;
        }
        #template{
            font-size: 6px;
            color: white;
            position: absolute;
            margin-top: 4px;
        }
        #uploadButton{
            font-size: 6px;
            color: white;
            margin-right: 10px;
            margin-top:4px;
            float: right;
        }
        .tableTile{
            line-height: 24px;
            font-size: 15px;
            padding-top: 7px;
            background-color: #2184C1!important;
            height:30px;
            min-height: 28px!important;
        }
    </style>
</head>

<body style="background-color: white;">
<%@ include file="/bxui/bxuihead.jsp" %>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/easyui-plugin/css/easyui.css"/>
<input type="hidden" value="<%=tenantid%>" id="tenantid">
<div id="father" class="page-content" style="position:inherit;min-height:600px">
    <!-- /sectionController:settings.box -->
    <div class="page-header" id="header">
        <h5>
            技服管理
            <small>
                &nbsp;
                <i class="ace-icon fa fa-angle-double-right"></i>
                &nbsp;&nbsp;
                故障处理
            </small>
        </h5>
        <div style="float: right;margin-top: -20px;margin-right: 15px;">
            <a href="javascript:insertPage()" ><span id = "currentTbm"></span></a>
        </div>
    </div>

    <!-- 内容-->
    <div class="row">
        <div class="col-sm-3" id="treeDiv">
            <div class="widget-box widget-color-blue" id="treeDiv2" style="overflow:scroll;overflow-x:hidden">
                <div class="widget-header tableTile">
                    <div>
                        <span class="widget-title lighter smaller" id="sTitle">
                            始发管理
                            <a href="javascript:void(0)"  id="template" class="hide" onclick="downloadTemplet()">模板下载</a>
                            <a href="javascript:void(0)" id="uploadButton" style="" onclick="dataUpload()">文件上传</a>
                            <%--<div id="uploadButton" class="ace-icon fa fa-upload" style="float: right;margin-right: 20px;cursor:pointer;" onclick="dataUpload()"></div>--%>
                        </span>
                    </div>
                </div>

                <div class="widget-body">
                    <div class="widget-main padding-8 bluetree">
                        <div id="bigTree"></div>
                    </div>
                </div>

            </div>
        </div>

        <div id="queryarea" class="col-xs-8">
            <div class="widget-box widget-color-blue " style="float: left;margin-right: 10px;">
                <div class="widget-header tableTile">
                    <div>
                        <span class="widget-title bigger lighter">
                            <i class="ace-icon fa fa-table"></i>
                            {{title}}
                        </span>
                    </div>
                </div>
                <!-- /sectionController:custom/widget-box.options -->
                <div class="widget-body">

                </div>
                <div class="widget-main no-padding">
                    <table class="table table-striped table-bordered table-hover" style="table-layout:fixed;">
                        <thead>
                        <tr>
                            <td align="center" style="width: 5%;"><input type="checkbox" disabled="disabled"></td>
                            <td align="center" style="width: 35%;">名称</td>
                            <td align="center" style="width: 20%;">大小</td>
                            <td align="center" style="width: 35%;">上传时间</td>
                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="(row,index) in rows" class="dataTable">
                            <td align="center" ><input type="checkbox" name="record" v-bind:value="index"></td>
                            <td align="center" onclick="previewFile(this)" v-bind:data-fileName="row.fileName" >{{row.fileName}}</td>
                            <td align="center" >{{row.fileSize}}</td>
                            <td align="center">{{row.time}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <div id="page_table_jqGrid" class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr" style="height: 40px;">
                    <table cellspacing="0" cellpadding="0" border="0" class="ui-pg-table" style="width:100%;table-layout:fixed;height:100%;" role="row">
                        <tbody>
                        <tr>
                            <td style="width: 10px;"></td>
                            <td align="center" class="ui-pg-button ui-corner-all" title="" data-original-title="数据导出" style="width: 25px;">
                                <div class="ui-pg-div"><span class="ui-icon ace-icon fa fa-cloud-download" onclick="download()"></span></div>
                            </td>
                            <c:if test="${sessionScope.groupDisplayNames eq '管理员组'}">
                                <td align="center" class="ui-pg-button ui-corner-all" title="" id="del_table_jqGrid" data-original-title="删除所选记录" style="width: 25px;">
                                    <div class="ui-pg-div"><span class="ui-icon ace-icon fa fa-trash-o red" onclick="deleteFile()"></span></div>
                                </td>
                            </c:if>
                            <td>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>


<!-- ----------------------------------信息弹出框---------------------------------------------------------->

<div id="formDiv" class="hide">
    <form class="form-horizontal" role="form" id="startManageForm">
        <div class="form-group" style="margin:0px 0px 15px 0px;">
            <input type="text" name="type" id="type" value="" class="hide" />
            <div class="col-sm-6" style="float: left;">
                <label class="col-sm-4 control-label no-padding-right" for="brokerage">经手人<span style="color: red">*</span> </label>
                <input class="col-sm-7" style="margin-left: 10px;" type="text" id="brokerage" name="brokerage" value="" class=".input-large" />
            </div>
            <div class="col-sm-6">
                <label class="col-sm-4 control-label no-padding-right" for="brokeragePhone">经手人电话<span style="color: red">*</span> </label>
                <input class="col-sm-7" style="margin-left: 10px;" type="text" id="brokeragePhone" name="brokeragePhone" value="" class=".input-large" />
            </div>
        </div>
        <div class="form-group" style="margin:0px 0px 15px 0px;">
            <div class="col-sm-6" style="float: left;">
                <label class="col-sm-4 control-label no-padding-right" for="contacts">联系人<span style="color: red">*</span></label>
                <input class="col-sm-7" style="margin-left: 10px;" type="text" id="contacts" name="contacts" value="" class=".input-large" />
            </div>
            <div class="col-sm-6">
                <label class="col-sm-4 control-label no-padding-right" for="contactsPhone">联系人电话<span style="color: red">*</span></label>
                <input class="col-sm-7" style="margin-left: 10px;" type="text" id="contactsPhone" name="contactsPhone" value="" class=".input-large" />
            </div>
        </div>
        <div class="form-group" style="margin:0px 0px 15px 0px;">
            <div class="col-sm-6">
                <label class="col-sm-4 control-label no-padding-right" for="remarks">备注</label>
                <input class="col-sm-7" style="margin-left: 10px;" type="text" id="remarks" name="remarks"  class=".input-large" />
            </div>
            <div class="col-sm-6" style="float: left;">
                <label class="col-sm-4 control-label no-padding-right" for="createTime">时间<span style="color: red">*</span></label>
                <input class="col-sm-7" style="margin-left: 10px;" type="text" id="createTime" name="createTime" value="" class=".input-large" />
            </div>
        </div>
        <div class="form-group" style="margin:0px 0px 15px 0px;">
            <div class="col-sm-6">
                <label class="col-sm-4 control-label no-padding-right" for="file"> 文件(zip)<span style="color: red">*</span> </label>
                <input class="col-sm-7" style="margin-left: 10px;" type="file" id="file" name="file"  class=".input-large" />
            </div>
        </div>
    </form>
    <div id="lineConfirm" class="hide row" style="margin:0px;">确定</div>

</div>

<div id="deleteConfirm" class="hide">
    <span>确定删除么？一旦删除，不可恢复</span>
</div>

<div id="detail" class="hide" >
    <div id="tbmDiv" style="margin:15px 10px 15px 10px">
        <label class="col-sm-4 control-label no-padding-right" style="margin-top:5px; " for="tbmNames">盾构机名称<span style="color: red">*</span> </label>
        <select class="col-sm-7 easyui-combobox" style="margin-left: 10px;padding:3px 5px;"  name="tbmNames" id="tbmNames"></select>
    </div>
    <div  style="margin:15px 10px 15px 23px">
        <span style="font-size: 8px;color: red;">(你可以通过输入盾构机名字首文字来精确查询盾构机)</span>
    </div>
</div>

<script type="text/javascript" src="<%=toolkitPath%>/bxui/vue/vue.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.form.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=path%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-form.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/easyui-plugin/js/jquery.easyui.min.js"></script>
<script src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/tbmManage/selectTbm.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/tbmManage/startManage.js"></script>
</body>
</html>
