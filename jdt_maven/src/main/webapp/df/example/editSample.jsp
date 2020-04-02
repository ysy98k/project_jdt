<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<html lang="zh-cn" style="height: 100%">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>组件代码实例编辑</title>
<style type="text/css" id="scriptCss">
.dispContent {
	height: 100%;
}
</style>
<%
	String chart = (String) request.getParameter("chart");
	String detail = (String) request.getParameter("detail");
%>
<script type="text/javascript">
    var chartType = '<%=chart%>';
    var detailType = '<%=detail%>';
</script>
</head>
<body style="height: 100%">
	<%@ include file="/bxui/bxuihead.jsp"%>
	<script type="text/javascript"
		src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
	<script type="text/javascript"
		src="<%=toolkitPath%>/bxui/jseditor/ace.js"></script>
	<script type="text/javascript"
		src="<%=toolkitPath%>/df/example/editSample.js"></script>
    <link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css" />
	<div class="jqgridFrame contentFrame" style="height: 100%">
		<div class="page-content contentFrame">
			<!-- /sectionController:settings.box -->
			<div class="page-header" style="height: 50px;">
				<div class="row">
					<div class="col-xs-9">
						<h1 class="pull-left">
							实例在线编辑 <small><i
								class="ace-icon fa fa-angle-double-right"></i>
								主要用于bx组件的在线编辑，测试，保存，同时也可以作为Web在线编辑工具</small>
						</h1>
					</div>
					<div class="col-xs-3">
						<div class="row">		
						    <div class="col-xs-6">
						          <button class="btn btn-sm pull-right btn-block"
									style="width: 100%" onclick="submitCode();">
									<div class="ace-icon fa fa-refresh"></div>
									<span>提交代码</span>
								</button>
						    </div>
						    <div class="col-xs-6">
						        <button class="btn btn-sm pull-right btn-block"
									style="width: 100%" onclick="returnBack();">
									<div class="ace-icon fa fa-backward"></div>
									<span>返回</span>
								</button>
						    </div>											
						</div>
					</div>
				</div>
			</div>
			<!-- /.page-header -->

			<div class="row">
				<div class="col-xs-12">
					<div class="row">
						<div class="col-sm-4" style="height: 500px">
							<fieldset class="colBorder">
								<legend class="colLegend">javaScript</legend>
								<div class="editframe">
									<div class="editContent">
										<a href="#" onclick="autoResize()"
											class="ace-icon fa fa-expand" id="jsicon-resize"></a>&nbsp;&nbsp;<font
											color="#428bca">按F2可以最大化、还原编辑框</font>
										<pre id="jscontent"
											style="height: 100%; width: 100%; border-top: none; border-radius: 0px"></pre>
									</div>
								</div>
							</fieldset>
						</div>
						<div class="col-sm-8" style="height: 505px">
							<fieldset class="colBorderOther">
								<legend class="colLegend">示例展示 </legend>
								<div class="dispframe">
									<div class="dispContent" id="dispArea"></div>
								</div>
							</fieldset>
						</div>
					</div>
				</div>
			</div>
			<!-- /.row -->


		</div>
	</div>
</body>
</html>