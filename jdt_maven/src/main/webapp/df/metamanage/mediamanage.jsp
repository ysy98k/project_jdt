<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String userName = (String) request.getSession().getAttribute("username");
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <title>多媒体管理</title>

</head>
<body>
<%@ include file="/bxui/bxuihead.jsp" %>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/loadsave.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/metamanage/mediamanage.js"></script>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css"/>

<div class="jqgridFrame contentFrame">
  <div class="page-content contentFrame">
    <!-- /sectionController:settings.box -->
    <div class="page-header" style="height: 50px;">
      <h1 class="pull-left">
        多媒体管理
        <small><i class="ace-icon fa fa-angle-double-right"></i>
          提供多媒体资源的查询和上传
        </small>
      </h1>

    </div>
    <!-- /.page-header -->
    <div class="row">
      <div class="col-xs-12 searchArea" id="queryarea">

        <div id="alertdiv" class="alertdiv"></div>
        <div class="row">
            <div class="col-sm-5 col-xs-12 col-1024-6">
              <div class="col-sm-6 col-xs-12 col-1024-6">
                <label class="labelThree"
                       for="inqu_status-mediaName">文件名</label>
                <input type="text" id="inqu_status-mediaName" data-bxwidget class="inputThree"/>
              </div>
              <div class="col-sm-6 col-xs-12 col-1024-6">
                <label class="labelThree"
                       for="inqu_status-mediaType">文件类型</label>
                <div type="text" id="inqu_status-mediaType" data-bxwidget="bxcombobox" class="inputThree"></div>
              </div>
            </div>

            <div class="col-sm-7 col-xs-12 col-1024-6">
                <div class="col-sm-3 col-1024-4 col-xs-12 pull-left no-padding-right">
                    <button class="btn btn-sm btn-block" onclick="addAndCopy();">
                        <div class="ace-icon fa fa-plus"></div>
                        <span>新增/复制</span>
                    </button>
                </div>
                <div class="col-sm-9 col-1024-8 col-xs-12 pull-left no-padding">
                    <div class="col-sm-4 col-1024-4 col-xs-12 pull-left  no-padding-right">
                        <button class="btn  btn-sm btn-block" onclick="saveRec();">
                            <div class="ace-icon fa fa-save"></div>
                            <span>保存</span>
                        </button>
                    </div>
                    <div class="col-sm-4 col-1024-4 col-xs-12 pull-left  no-padding-right">
                        <button class="btn btn-sm btn-danger width-100" onclick="deleteRec();">
                            <div class="ace-icon fa fa-trash-o"></div>
                            <span>删除</span>
                        </button>
                    </div>
                    <div class="col-sm-4 col-1024-4 col-xs-12 pull-right  no-padding-right">
                        <button class="btn btn-sm btn-block" onclick="on_query_click();">
                            <div class="ace-icon fa fa-search"></div>
                            <span>查询</span>
                        </button>
                    </div>
                </div>
            </div>
        </div>

      </div>

      <div class="col-xs-12">
        <div id="jqGrid"></div>
      </div>
      <!-- /.col -->
        <input type="hidden" id="hidden-userName" data-bxwidget data-bxtype="string" value="<%=userName%>">
    </div>
    <!-- /.row -->
  </div>
</div>
<!-- page-content -->

</body>
</html>

