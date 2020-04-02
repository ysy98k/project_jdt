<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>请假流程示例</title>
</head>
<body>
<%@ include file="/bxui/bxuihead.jsp" %>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/example/workflow/leave.js"></script>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css"/>
<%
    String taskId = (String) request.getParameter("taskId");
    String processDefId = (String) request.getParameter("processDefId");
%>
<div class="jqgridFrame contentFrame">
    <div class="page-content contentFrame">
        <div class="page-header" style="height: 50px;">
            <h1 class="pull-left">
                请假流程示例
                <small><i class="ace-icon fa fa-angle-double-right"></i>
                    普通表单示例——模拟请假流程
                </small>
            </h1>
        </div>

        <%
            if (null != taskId) {

        %>
        <input type="hidden" id="taskId" value="<%=taskId%>"/>
        <%
            }
        %>

        <%
            if (null != processDefId) {

        %>
        <input type="hidden" id="processDefId" value="<%=processDefId%>"/>
        <%
            }
        %>


        <div class="row">
            <div class="col-xs-12">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right"
                               for="days"> 请假天数 </label>

                        <div class="col-sm-9">
                            <input type="text" id="days"
                                   class="col-xs-10 col-sm-5"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label no-padding-right"
                               for="reason"> 请假事由 </label>

                        <div class="col-sm-9">
                            <textarea class="form-control" id="reason"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-md-offset-3 col-md-9">
                            <div class="row no-margin" style="width: 34%">
                                <button class="btn btn-info btn-block btn-inline" type="button" onclick="submitForm();">
                                    <i class="ace-icon fa fa-check bigger-110"></i> 提交
                                </button>
                                <button class="btn btn-info btn-block btn-inline no-margin" type="button" onclick="javascript:history.back();">
                                    <i class="ace-icon fa fa-check bigger-110"></i> 返回
                                </button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>

    </div>
</div>

</body>
</html>

