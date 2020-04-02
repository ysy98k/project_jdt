<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>bxgrid</title>

</head>

<body>

<%@ include file="/bxui/bxuihead.jsp" %>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript"
        src="<%=toolkitPath%>/df/bxwidget/table/bxgriddemo.js"></script>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css"/>
<div class="page-content">

    <div class="page-header">
        <h1>
            bxgrid
            <small><i class="ace-icon fa fa-angle-double-right"></i>
                bxgrid表格组件
            </small>
        </h1>
    </div>


    <div class="row">
        <div class="col-xs-12">

            <div id="grid"></div>

        </div>

    </div>

    <div class="row">
        <div class="col-xs-12">
            <div id="groupGrid"></div>
        </div>
    </div>
    
    <div class="row">
        <div class="col-xs-12">
            <div id="groupAddRow"></div>
        </div>
    </div>

    <div class="row">
        <div class="col-xs-12">
            <div id="gridImage"></div>
        </div>
    </div>

    <div id="griddetail" class="hide"></div>

</div>
<!-- ----------------------------------pop div---------------------------------------------------------->
<form id="detail" class="hide">
    <input type="hidden" id="detail-id" data-bxwidget data-bxtype="number"/>

    <div class="row rowspace">
        <div class="col-sm-12">
            <div class="form-group">
                <label class="col-sm-4 control-label no-padding-right labelfont"
                       for="detail-name">物品 </label>

                <div class="col-sm-8">
                    <input type="text" id="detail-name" name="detaillname"
                           class="col-xs-10 col-sm-10" data-bxwidget data-bxtype="string" required/>
                </div>
            </div>
        </div>
    </div>
    <div class="row rowspace">
        <div class="col-sm-12">
            <div class="form-group">
                <label class="col-sm-4 control-label no-padding-right labelfont"
                       for="detail-tradeday">交易日期 </label>

                <div class="col-sm-8">
                    <input type="text" id="detail-tradeday" name="detailtradeday"
                           class="col-xs-10 col-sm-10" data-bxwidget data-bxtype="string"/>
                </div>
            </div>
        </div>
    </div>
    <div class="row rowspace">
        <div class="col-sm-12">
            <div class="form-group">
                <label class="col-sm-4 control-label no-padding-right labelfont"
                       for="detail-amount"> 交易金额 </label>

                <div class="col-sm-8">
                    <input type="text" id="detail-amount" name="detailamount"
                           class="col-xs-10 col-sm-10" data-bxwidget data-bxtype="number"/>
                </div>
            </div>
        </div>
    </div>
    <div class="row rowspace">
        <div class="col-sm-12">
            <div class="form-group">
                <label class="col-sm-4 control-label no-padding-right labelfont"
                       for="detail-phone">电话</label>

                <div class="col-sm-8">
                    <input type="text" id="detail-phone" name="detailphone"
                           class="col-xs-10 col-sm-10" data-bxwidget data-bxtype="number"/>
                </div>
            </div>
        </div>
    </div>
    <div class="row rowLastSpace">
        <div class="col-sm-12">
            <div class="form-group">
                <label class="col-sm-4 control-label no-padding-right labelfont"
                       for="detail-desc"> 描述 </label>

                <div class="col-sm-8">
                    <input type="text" id="detail-desc" name="detaildesc"
                           class="col-xs-10 col-sm-10" data-bxwidget data-bxtype="string"/>
                </div>
            </div>
        </div>
    </div>
    <div class="row rowLastSpace">
        <div class="col-sm-12">
            <div class="form-group">
                <label class="col-sm-4 control-label no-padding-right labelfont"
                       for="detail-status"> 状态 </label>

                <div class="col-sm-8" id="detail-status" name="detailstatus" data-bxwidget="bxcombobox"
                     data-bxtype="string">

                </div>
            </div>
        </div>
    </div>
    <div class="row rowLastSpace">
        <div class="col-sm-12">
            <div class="form-group">
                <label class="col-sm-4 control-label no-padding-right labelfont"
                       for="detail-area"> 区域 </label>

                <div class="col-sm-8">
                    <input type="text" id="detail-area" name="detailarea"
                           class="col-xs-10 col-sm-10" data-bxwidget data-bxtype="string"/>
                </div>
            </div>
        </div>
    </div>
</form>
<!-- ----------------------------------信息弹出框---------------------------------------------------------->
<div id="dialog-message" class="hide">
    <p class="bigger-110 bolder center grey">
        <br/>
        <b id="dialogInfo"></b>
    </p>
</div>
<div id="dialog-edit" class="hide">
</div>
</body>
</html>
