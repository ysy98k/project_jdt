<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="com.baosight.aas.auth.Constants"%>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>报警管理</title>
    <%
        String path = request.getContextPath();
        String tenantid=request.getSession().getAttribute(Constants.SESSION_TENANTID_KEY) + "";
    %>

    <meta name="description" content="platForm Frameset" />
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <%@ include file="/bxui/bxuihead.jsp"%>
    <link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css" />
</head>
<body>

    <input type="hidden" value="<%=tenantid%>" id="tenantid">

    <div class="page-content" style="position:inherit;min-height:600px">
        <!-- /sectionController:settings.box -->
        <div class="page-header">
            <h1>
                报警管理
                <small>
                    <i class="ace-icon fa fa-angle-double-right"></i>
                    报警画面
                </small>
            </h1>
            <!--右侧下拉列表 -->
            <div style="float:right;width: 300px;margin-top: -25px">
                <select class="form-control" id="form-field-select-1">
                </select>
            </div>
        </div><!-- /.page-header -->
        <iframe id="cvsPage" width="100%" style="min-height: 790px"  src="http://www.baidu.com">

        </iframe>
    </div>
    <%--发送请求，从数据库总读取数据，填充右侧下拉列表框--%>
    <script type="text/javascript">
        var callback = {
            onSuccess: function (paramJsonObj) {
                var data = paramJsonObj;

                for (var i = 0; i < data.length; i++) {
                    $("#form-field-select-1").append("<option value='" + data[i].sectionId + "' url='"+data[i].cvsUrl +"' collection='"+data[i].collectorName +"'>" + data[i].sectionName + "</option>");
                }

                if ($.cookie('selected_name')!=null) {
                    $("#form-field-select-1").val($.cookie('selected_id'));
                    var strVal = $("#form-field-select-1").find("option:selected").attr("url");
                    $("#cvsPage").attr("src", strVal);

                }
                $("#form-field-select-1").change(function () {
                    alert("aaaaa");
                    var strVal = $("#form-field-select-1").find("option:selected").attr("url");
                    $("#cvsPage").attr("src", strVal);
                    var strVal = $("#form-field-select-1").find("option:selected").attr("collection");
                    $.cookie('selected_collection',strVal, {path: "/"});
                    var strVal = $("#form-field-select-1").val();
                    $.cookie('selected_id',strVal, {path: "/"});
                });
            }


        };
        $(function(){
            var paramJsonObj = {};
            AjaxCommunicator.ajaxRequest('/ListQuery.do?method=query', 'POST', paramJsonObj, callback);
            var h = $('#contentFrame', parent.document).height();
            $("#cvsPage").height(h - 110);
        })

    </script>
</body>
</html>

