<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="com.baosight.aas.auth.Constants" %>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <title>监测报表</title>
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
        body{
            background-color: white!important;
        }
    </style>

</head>

<body>
<%@ include file="/bxui/bxuihead.jsp" %>
<%--<script type="text/javascript" src="<%=path%>/bxui/baosight-require.js"></script>--%>
<input type="hidden" value="<%=tenantid%>" id="tenantid">
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css"/>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.form.js"></script>
<!--
<script type="text/javascript" src="<%=toolkitPath%>/bxui/raphael.min.js"></script>
-->
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.mousewheel.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-form.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/selectLine.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/lineData.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/aceadmin/js/ace-elements.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/easyui-plugin/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="surveyInfo.js"></script>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/easyui-plugin/css/easyui.css"/>
<link type="text/css" rel="stylesheet" href="surveyInfo.css"/>
<link href="<%=toolkitPath%>/bxui/aceadmin/style/css/font-awesome.css" rel="stylesheet" type="text/css">
<div class="page-content" style="position:inherit;min-height:600px">
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
        <div id="select_line_div">
            <a href="javascript:insertPage()"><span id="cityname"></span><span id="linemessage"></span><span
                    id="programmessage"></span></a>
        </div>
    </div><!-- /.page-header -->
    <!--
    <iframe id="cvsPage" width="100%" style="min-height: 790px" src="">
    </iframe >
    -->

    <div id="detail" class="hide" style="overflow: hidden">
        <div type="text" id="div_city" name="city" data-bxwidget="bxcombobox" data-bxtype="string"
             style="margin:15px 10px 15px 10px">
        </div>
        <div type="text" id="div_line" name="line" data-bxwidget="bxcombobox" data-bxtype="string"
             style="margin:15px 10px 15px 10px">
        </div>
        <div type="text" id="div_program" name="program" data-bxwidget="bxcombobox" data-bxtype="string"
             style="margin:15px 10px 15px 10px">
        </div>
    </div>
    <!-- 文件上传框  -->
    <div class="uploadFilecontainer">
        <div class="bootbox modal fade bootbox-prompt in" id="uploadFile" tabindex="-1" role="dialog"
             style="display: none; margin-top: 50px;"
             aria-hidden="false">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="bootbox-close-button close" onclick="hideBootBox()"
                                data-dismiss="modal" aria-hidden="true">×
                        </button>
                        <h4 class="modal-title">文件上传</h4>
                    </div>

                    <div class="widget-body">
                        <div class="widget-body-inner" style="display: block; height: 100px">
                            <div class="widget-main">
                                <div class="form-group">
                                    <div class="col-xs-12">
                                        <!-- #sectionController:custom/file-input -->
                                        <label class="ace-file-input">
                                            <input id="id-input-file-2" type="file" name="file"
                                                   onchange="checkFile(document.getElementById('id-input-file-2'))">
                                            <span class="ace-file-container" id="changeSpan" data-title="选择">
                                                <span class="ace-file-name" id="fileName" data-title="请添加文件...">
                                                    <i class=" ace-icon fa fa-upload" id="uploadCoin"></i>
                                                </span>
                                            </span>
                                            <a class="remove" id="removeFile" onclick="deleteFile()" href="#">
                                                <i class=" ace-icon fa fa-times"></i>
                                            </a>
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button data-bb-handler="confirm" type="button" class="btn btn-primary"
                                onclick="submitUpload(document.getElementById('id-input-file-2'))">确定
                        </button>
                        <button data-bb-handler="cancel" type="button" class="btn btn-default" onclick="cancelUpload()">
                            取消
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="ui-widget-overlay ui-front" id="uploadBlackGround" style="z-index: 1049; display: none;"></div>

    <!-- 内容 -->
    <div class="row" style="margin-left: 1px;">
        <div class="alert alert-danger" id="errorAlert" style="display: none">
            <button type="button" class="close" onclick="hideMessage()">
                <i class="ace-icon fa fa-times"></i>
            </button>

            <strong id="message">
            </strong>
        </div>
        <div style="float:left; margin-left: 10px">
            <button class="btn btn-primary" onclick="openBootBox()">文件上传</button>
            <button class="btn btn-primary" onclick="downloadXlsTemplet()">下载模板</button>
        </div>
        <div style="float:right; width: 180px; margin-right: 10px">
            <select class="form-control" id="dateAndTimes" onchange="changeData()"></select>
        </div>
        <div id="alltables" style="display: none;">
            <div style="margin-top: 50px;">
                <table border="1" class="table table-striped table-bordered table-hover" style="margin:0px auto;">
                    <thead class="thin-border-bottom">
                    <tr>
                        <th>施工单位</th>
                        <td id="constructionCompany"></td>
                        <th>监理单位</th>
                        <td id="supervisionCompany"></td>
                    </tr>
                    <tr>
                        <th>监测单位</th>
                        <td id="monitoringCompany"></td>
                        <th>仪器型号</th>
                        <td id="instrumentModel"></td>
                    </tr>
                    </thead>
                </table>
            </div>

            <div class="tabbable" id="sheetTable" style="margin-top: 10px;">
                <ul class="nav nav-tabs padding-12 tab-color-blue background-blue" id="myTab4"></ul>
                <div class="tab-content" id="myTabinfo"></div>
            </div>
        </div>
    </div>
</div>


<!-- /.page-content -->

<!-- ----------------------------------信息弹出框---------------------------------------------------------->
<div id="dialog-message" class="hide">
    <p class="bigger-110 bolder center grey">
        <br/>
        <b id="dialogInfo"></b>
    </p>
</div>
</body>

<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-form.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxdialog.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxcombobox.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/loadsave.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/selectLine.js"></script>



</html>
