<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="com.baosight.aas.auth.Constants" %>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <title>监测布点图</title>
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
<script type="text/javascript" src="<%=toolkitPath%>/bxui/raphael.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.mousewheel.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-form.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/selectLine.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/lineData.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/aceadmin/js/ace-elements.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/easyui-plugin/js/jquery.easyui.min.js"></script>
<script src="<%=toolkitPath%>/bxui/pdfjs-2.0.943-dist/build/pdf.js"></script>
<script src="<%=toolkitPath%>/bxui/pdfjs-2.0.943-dist/build/pdf.worker.js"></script>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/easyui-plugin/css/easyui.css"/>
<link href="<%=toolkitPath%>/bxui/aceadmin/style/css/font-awesome.css" rel="stylesheet" type="text/css">
<link type="text/css" rel="stylesheet" href="surveyGraph.css"/>


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
        <div style="float: right;margin-top: -20px">
            <a href="javascript:insertPage()"><span id="cityname"></span><span id="linemessage"></span><span
                    id="programmessage"></span></a>
        </div>
    </div><!-- /.page-header -->

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

    <!--  内容   -->
    <div style="height: 750px">
        <div class="alert alert-danger" id="errorAlert" style="display: none">
            <button type="button" class="close" onclick="hideMessage()">
                <i class="ace-icon fa fa-times"></i>
            </button>

            <strong id="message">
            </strong>
        </div>


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
                                    onclick="uploadZip(document.getElementById('id-input-file-2'))">确定
                            </button>
                            <button data-bb-handler="cancel" type="button" class="btn btn-default"
                                    onclick="cancelUpload()">
                                取消
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="ui-widget-overlay ui-front" id="uploadBlackGround" style="z-index: 1049; display: none;"></div>
        <div class="row" id="uploadDIV" style="margin-left: 1px;">
            <div style="display: inline-block;">
                <button class="btn btn-primary" onclick="openBootBox()">上传</button>
            </div>
            &nbsp;
            <div style="display: inline-block;">
                <button id="showDelete" class="btn btn-danger" onclick="showDelete()">删除</button>
            </div>

        </div>
        <div class="row" id="deleteDIV" style="margin-left: 1px; display: none">
            <div id="checkAll" style="display: inline-block;">
                <button class="btn btn-sm btn-primary" onclick="allChecked()">全选</button>
            </div>
            &nbsp;
            <div id="checkNone" style="display: inline-block;">
                <button class="btn btn-sm btn-primary" onclick="clearCheckBox()">反选</button>
            </div>
            &nbsp;
            <div id="doDelete" style="display: inline-block;">
                <button class="btn btn-sm btn-danger" onclick="doDelete()">删除</button>
            </div>
            &nbsp;
            <div id="cancelDelete" style="display: inline-block;">
                <button class="btn btn-sm btn-warning" onclick="cancelDelete()">取消</button>
            </div>
        </div>
        <div class="row" id="showpdf" style="margin-top: 10px;display: none">
            <div class="col-sm-6" style="width: 300px;height: 555px">
                <div class="widget-box widget-color-blue2">
                    <div class="widget-header" style="padding: 15px 0px 0px 10px;">
                        <h4 id="pdflabel" class="widget-title lighter smaller" style="margin-top: 20px">选择布点图</h4>
                    </div>

                    <div class="widget-body" style="height: 555px">
                        <div class="widget-main padding-8" id="allFiles" style=" height:555px; overflow-y:scroll;">
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-sm-6">
                <iframe id="pdfContainer" src="" style='width: 1200px;height: 600px' charset="utf-8"
                        scroll='no'>
                </iframe>
            </div>
        </div>
        <div class="bootbox modal fade bootbox-confirm in" tabindex="-1" role="dialog" id="deleteCheck"
             style="display: none; padding-right: 17px;margin-top: 50px">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-body">
                        <button type="button" class="bootbox-close-button close" data-dismiss="modal" aria-hidden="true"
                                style="margin-top: -10px;" onclick="cancelCheck()">×
                        </button>
                        <div class="bootbox-body" id="deleteFilesLabel">确定删除选中的文件？</div>
                    </div>
                    <div class="modal-footer">
                        <button data-bb-handler="cancel" type="button" class="btn btn-default" onclick="cancelCheck()">
                            取消
                        </button>
                        <button data-bb-handler="confirm" type="button" class="btn btn-primary" onclick="deleteRight()">
                            确定
                        </button>
                    </div>
                </div>
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
<script type="text/javascript" src="surveyGraph.js"></script>



</html>
