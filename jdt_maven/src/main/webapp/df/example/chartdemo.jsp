<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="author" content="hujinhua@baosight.com">
<title>图表组件pds数据源示例</title>

</head>
<body>
	<%@ include file="/bxui/bxuihead.jsp"%>
	<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
	<script type="text/javascript"
		src="<%=toolkitPath%>/df/example/chartdemo.js"></script>
    <link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css" />
	<link rel="stylesheet"
		href="<%=toolkitPath%>/bxui/bxwidget/css/bx-skin.css" /> 
	<div class="jqgridFrame contentFrame">
		<div class="page-content contentFrame">
			<!-- /sectionController:settings.box -->
			<div class="page-header" style="height: 50px;">
				<h1 class="pull-left">
					图表组件示例 
				</h1>
			</div>
			<div class="row">
				<div class="col-sm-12" id="query">				
					<div class="row">
						<div class="col-xs-12" style="padding: 0px">
							<div id="bardemostandard" style="width: 100%; height: 400px"></div>
						</div>
						<div class="col-xs-12" style="padding-left: 50px">
							<div id="bardemohorizontal" style="width: 90%; height: 400px"></div>
						</div>
						<div class="col-xs-12" style="padding-left: 50px">
							<div id="linedemostandard" style="width: 100%; height: 400px"></div>
						</div>
						<div class="col-xs-12" style="padding-left: 50px">
							<div id="piedemostandard" style="width: 100%; height: 400px"></div>
						</div>
						<div class="col-xs-12" style="padding-left: 50px">
							<div id="piedemotime" style="width: 100%; height: 400px"></div>
						</div>
						<div class="col-xs-12" style="padding-left: 50px">
							<div id="gaugedemo" style="width: 100%; height: 400px"></div>
						</div>
					</div>			

				</div>
			</div>

		</div>
	</div>

	
</body>
</html>