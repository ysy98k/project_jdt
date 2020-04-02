<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>国际化配置</title>
</head>
<body>
<%@ include file="/bxui/bxuihead.jsp" %>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript"
        src="<%=toolkitPath%>/df/metamanage/global.js"></script>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css" />

<%--<link href="~/Content/styles/easyui.css" rel="stylesheet"/>--%>
<%--<link href="~/Content/styles/icon.css" rel="stylesheet"/>--%>
<%--<script src="~/Scripts/jquery-1.7.1.js"></script>--%>
<%--<script src="~/Scripts/Webscript/jquery.easyui.min.js"></script>--%>
<%--<script src="~/Scripts/Webscript/easyui-lang-zh_CN.js"></script>--%>

<div class="jqgridFrame contentFrame">
    <div class="page-content contentFrame">
        <!-- /sectionController:settings.box -->
        <div class="page-header" style="height: 50px;">
            <h1 class="pull-left">
                国际化配置
                <small>
                    <i class="ace-icon fa fa-angle-double-right"></i>
                  对国际化信息进行相关配置
                </small>
            </h1>

        </div>
        <!-- /.page-header -->


        <div class="row">

            <div class="col-xs-12 searchArea" id="queryarea">

                <div id="alertdiv" class="alertdiv">
                </div>

                <div class="col-sm-3">
                    <label class="labelThree"
                           for="inqu_status-messageKey">资源键</label>
                    <input type="text" id="inqu_status-messageKey" data-bxwidget class="inputThree"/>
                </div>

                <div class="col-sm-3">
                    <label class="labelThree"
                           for="inqu_status-messageValue">资源值</label>
                    <input type="text" id="inqu_status-messageValue" data-bxwidget class="inputThree"/>
                </div>

                <div class="col-sm-2 col-xs-6 pull-right"style="margin-bottom: 5px">
                    <button class="btn btn-sm pull-right btn-block" onclick="on_query_click();" style="width: 100%">
                        <div class="ace-icon fa fa-search"></div>
                        <span>查询</span>
                    </button>
                </div>
            </div>

            <div class="col-xs-12">
                <div id="messageContent"></div>
            </div>
            <!-- /.col -->

        </div>
        <!-- /.row -->

    </div>
</div>
<!-- page-content -->

<!-- ----------------------------------信息弹出框---------------------------------------------------------->
<div id="dialog-update-message" class="hide">
</div>
<div id="dialog-message" class="hide">
    <p class="bigger-110 bolder center grey">
        <br/>
        <b id="dialogInfo"></b>
    </p>
</div>

<!-- ----------------------------------pop div---------------------------------------------------------->
<form class="hide" id="detail" style="min-height:250px;min-width:520px;">
    <!--key行-->
    <div class="row rowspace">
        <div class="col-sm-12 no-padding-right">
		    <label class="col-sm-2 control-label no-padding-right label-font-left"
		           for="detail_messageKey"><font style="color:red">*</font>资源键 </label>
		
		    <div class="col-sm-9">
			    <div class="form-group">
			        <input type="text" id="detail_messageKey" name="detailmessageKey"
			               class="col-xs-12 col-sm-12 global-input" data-bxwidget data-bxtype="string" required/>
			   	</div>
		    </div>
		    <div class="col-sm-1 no-padding-left">
		        <button id="addBtn" type="button" style="margin-top: 5px"
		                class="ace-icon fa fa-plus-circle purple"></button>
		    </div>
           		
        </div>
    </div>

    <!--除key行-->
    <div class="row rowspace" id="detailExceptKeyDiv">
        <div class="col-sm-12">
            <div class="form-group">
                <label class="col-sm-2 control-label label-font-left" text-align="center"
                       for="detail_messageKey">是否启用 </label>
                <label class="col-sm-3 control-label label-font-left"
                       for="detail_messageLan">所属语言 </label>
                <label class="col-sm-3 control-label label-font-left"
                       for="detail_messageValue"><font style="color:red">*</font>资源值 </label>
                <label class="col-sm-3 control-label label-font-left"
                       for="detail_messageDesc">资源描述 </label>
                <label class="col-sm-1 control-label label-font-left"
                       for="deleteBtn"> </label>
            </div>

        </div>
    </div>


    <!-- 统一模板 -->
    <div class="row rowspace" id="template" style="display:none">
        <div class="col-sm-12">
            <input type="hidden" id="detail_messageId" data-bxwidget data-bxtype="number" data-bxauto/>

            <div class="col-sm-2 " style="padding-top: 4px; padding-right: 0;padding-left: 25px;">
                <input id="detail_messageEnable" name="detailmessageEnable" type="checkbox"
                       class="ace ace-switch ace-switch-6 green" style="margin-top: 0" checked="checked"
                       data-bxwidget="checkbox" data-bxtype="string" />
                <span class="lbl"></span>
                <%--<option value="true">是</option>--%>
                <%--<option value="false">否</option>--%>
            </div>
            <div class="col-sm-3">
                <select id="detail_messageLan" name="detailmessageLan"
                        class="col-sm-12 no-padding-right"
                        data-bxwidget="select" data-bxtype="string">
                    <%--<option value="zh_CN">中文</option>--%>
                    <%--<option value="en_US">英文</option>--%>
                    <%--<option value="ja_JP">日文</option>--%>
                </select>
            </div>
            <div class="col-sm-3">
                <div class="form-group">
                    <input type="text" id="detail_messageValue" name="detailmessageValue"
                           class="col-xs-12 col-sm-12 global-input" data-bxwidget data-bxtype="string" required/>
                </div>
            </div>
            <div class="col-sm-3">
                     <input type="text" id="detail_messageDesc" name="detailmessageDesc"
                       class="col-xs-12 col-sm-12 global-input" data-bxwidget data-bxtype="string"/>
            </div>
            <div class="col-sm-1 no-padding-left" style="height: 30px;">
                <button id="deleteBtn" style="margin-top: 5px" type="button"
                        class="ace-icon fa fa-trash-o red"></button>
            </div>
        </div>
    </div>
</form>

<div class="row rowspace" id="subgrid_column" style="display: none;">
    <div class="col-sm-12">
        <div class="form-group">
            <label class="col-sm-3 control-label label-font-left" text-align="center"
                   for="detail_messageKey">是否启用 </label>
            <label class="col-sm-3 control-label label-font-left"
                   for="detail_messageLan">所属语言 </label>
            <label class="col-sm-3 control-label label-font-left"
                   for="detail_messageValue">资源值 </label>
            <label class="col-sm-3 control-label label-font-left"
                   for="detail_messageDesc">资源描述 </label>
        </div>
    </div>
</div>

<div class="row rowspace" id="subgrid_row" style="display: none;">
    <div class="col-sm-12">
        <div class="form-group">
            <label class="col-sm-3 control-label label-font-left"
                   for="detail_messageEnable"></label>
            <label class="col-sm-3 control-label label-font-left"
                   for="detail_messageLan"></label>
            <label class="col-sm-3 control-label label-font-left" style="word-wrap:break-word;word-break:break-all; "
                   for="detail_messageValue"></label>
            <label class="col-sm-3 control-label label-font-left" style="word-wrap:break-word;word-break:break-all; "
                   for="detail_messageDesc"></label>
        </div>
    </div>
</div>

</body>
</html>

