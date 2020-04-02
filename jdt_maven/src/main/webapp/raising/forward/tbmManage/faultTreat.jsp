<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2018-07-02
  Time: 15:19
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>区间段信息管理</title>

</head>
<body>
<%@ include file="/bxui/bxuihead.jsp" %>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/date-time/css/bootstrap-datetimepicker.css">
<script src="<%=toolkitPath%>/bxui/date-time/bootstrap-datetimepicker.js"></script>
<script src="<%=toolkitPath%>/bxui/date-time/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/easyui-plugin/css/easyui.css"/>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.form.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/easyui-plugin/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/tbmManage/selectTbm.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/tbmManage/faultTreat.js"></script>


<div class="jqgridFrame contentFrame">
    <div class="page-content contentFrame">
        <!-- /sectionController:settings.box -->
        <div class="page-header" style="height: 25px;">
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
        <!-- /.page-header -->
        <div class="row">
            <div class="col-xs-12 searchArea" id="queryarea">

                <div id="alertdiv" class="alertdiv" style="padding: 10px 15px;"></div>
                <div class="row" style="margin-bottom: 5px;">
                    <div class="col-sm-8 col-md-8 col-xs-12 no-padding">
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
                            <button class="btn  btn-sm btn-block" onclick="attachment();">
                                <div class="ace-icon fa fa-save"></div>
                                <span>附件管理</span>
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
<!-- ----------------------------------信息弹出框---------------------------------------------------------->


<div id="fromDiv" class="hide" style="padding:6.5px 0px;">
    <div data-bxwidget="bxInputFile" class="uploadDiv ui-dialog-content ui-widget-content" style="overflow-x: hidden; width: auto; min-height: 35px; max-height: none; height: auto;" id="ui-id-1">
        <div class="row">
            <div class="col-sm-12">
                <div class="widget-body">
                    <div class="widget-main">
                        <div class="form-group">
                            <div class="col-xs-12">
                                <form id="factoryForm" enctype="multipart/form-data">
                                    <input class="hide" type="text" id="faultId" name="faultId" value="" />
                                    <input class="hide" type="text" id="tbmName" name="tbmName" value="" />
                                    <input multiple="" type="file" id="bxgridFileInput" name="Filedata" />
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix">
        <div class="ui-dialog-buttonset">
            <button type="button" class="btn btn-skinColor btn-xs" onclick="dataSubmit()">
                <i class="ace-icon fa fa-check bigger-110"></i>&nbsp; 上传
            </button>
        </div>
    </div>
</div>


<div id="attachmentManage"  class="page-content contentFrame hide" style="border: none;padding-bottom: 0">
    <!-- /section:settings.box -->
    <div class="page-header" style="height: 45px;">
        <h1 class="pull-left">
            附件管理 <small> <i class="ace-icon fa fa-angle-double-right"></i>
            上传、删除附件信息
        </small>
        </h1>

    </div>

    <div class="row" style="margin: 0px -15px 0px -15px;">

        <div class="col-xs-12 searchArea">

            <div id="alertdivInfo" class="alertdiv" style="padding: 7px 15px"></div>

            <div class="col-sm-3 col-md-3 col-xs-6 no-padding-left-1024">
                <button class="btn btn-sm btn-block" onclick="dataUpload();">
                    <div class="ace-icon fa fa-plus"></div>
                    <span>上传</span>
                </button>
            </div>
            <div class="col-sm-3 col-md-2 col-xs-6 no-padding-left-1024">
                <button class="btn btn-sm btn-danger width-100" onclick="deleteFile();">
                    <div class="ace-icon fa fa-trash-o"></div>
                    <span>删除</span>
                </button>
            </div>
            <div class="col-sm-3 col-md-2 col-xs-6 no-padding-left-1024">
                <button class="btn  btn-sm btn-block" onclick="download();">
                    <div class="ace-icon fa fa-save"></div>
                    <span>下载</span>
                </button>
            </div>
            <div class="col-sm-3 col-md-2 col-xs-6 no-padding-left-1024">
                <button class="btn  btn-sm btn-block" onclick="previewFile();">
                    <div class="ace-icon fa fa-save"></div>
                    <span>预览</span>
                </button>
            </div>

        </div>

        <input type="hidden" id="faultIdHide" />

        <div class="col-xs-12">
            <div id="attchmentGrid" style="width: 100%; height: 95%; margin-top: 5px"></div>
        </div>
    </div>
</div>


<div id="deleteConfirm" class="hide">
    <span>确定删除么？一旦删除，不可恢复</span>
</div>

<!--切换盾构机-->
<div id="detail" class="hide" >
    <div id="tbmDiv" style="margin:15px 10px 15px 10px">
        <label class="col-sm-4 control-label no-padding-right" style="margin-top:5px; " for="tbmNames">盾构机名称<span style="color: red">*</span> </label>
        <select class="col-sm-7 easyui-combobox" style="margin-left: 10px;padding:3px 5px;"  name="tbmNames" id="tbmNames"></select>
    </div>
    <div  style="margin:15px 10px 15px 23px">
        <span style="font-size: 8px;color: red;">(你可以通过输入盾构机名字首文字来精确查询盾构机)</span>
    </div>
</div>

</body>
<script type="text/javascript">
    function initCol(colNames,colModels) {
        colNames.push('操作');
        var  classification
    }




</script>
</html>

