<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/9/26
  Time: 9:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>区间段信息管理</title>

    <style>
        [role = 'gridcell']{
            font-size: 13px;
        }  
    </style>

</head>
<body>
<%@ include file="/bxui/bxuihead.jsp" %>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/easyui-plugin/css/easyui.css"/>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/easyui-plugin/js/jquery.easyui.min.js"></script>

<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.form.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/tbmManage/selectTbm.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/tbmManage/tbmResume.js"></script>

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
                    盾构机履历
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
            </div>

            <div class="col-xs-12">
                <div id="jqGrid"></div>
            </div>
            <!-- /.col -->
        </div>
        <!-- /.row -->
    </div>

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
</html>
