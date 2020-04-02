<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="author" content="hujinhua@baosight.com">
<title>高炉煤气模型预测</title>

</head>
<body>
	<%@ include file="/bxui/bxuihead.jsp"%>
	<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
	<script type="text/javascript"
		src="<%=toolkitPath%>/df/example/pdsdemo.js"></script>
    <link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css" />
	<link rel="stylesheet"
		href="<%=toolkitPath%>/bxui/bxwidget/css/bx-skin.css" /> 
	<div class="jqgridFrame contentFrame">
		<div class="page-content contentFrame">
			<!-- /sectionController:settings.box -->
			<div class="page-header" style="height: 50px;">
				<h1 class="pull-left">
					高炉煤气模型预测 <small><i class="ace-icon fa fa-angle-double-right"></i>
						高炉煤气模型预测 </small>
				</h1>
			</div>
			<div class="row">
				<div class="col-sm-12" id="query">
					<div class="row">
						<div class="col-sm-4">
							<label class="labelThree  control-label no-padding-right">预测起始时间</label>
							<input id="startTime" class="Wdate inputThree" type="text"
								onclick="changeStartDate();"/>
						</div>
						
						<div class="col-sm-4">
							<label class="labelThree  control-label no-padding-right">时间间隔(秒)</label>
							<input id="timeInterval" type="text"  class="inputThree"  />
						</div>	
					    <div class="col-sm-4">
							<label class="labelThree  control-label no-padding-right">刷新周期(秒)</label>
							<input id="cycle" type="text"  class="inputThree" />
						</div>					
					</div>
					<div class="row">
					    <div class="col-sm-4">
							<label class="labelThree  control-label no-padding-right">预测截止时间</label>
							<input id="endTime" class="Wdate inputThree" type="text"
								onclick="changeEndDate();" />
						</div>
					    <div class="col-sm-4">
					    </div>
						<div class="col-sm-4">						    
							<button class="btn btn-sm pull-right btn-block"
								style="width: 60%" onclick="on_query_click();">
								<div class="ace-icon fa fa-search"></div>
								<span>刷新</span>
							</button>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-12" style="padding: 0px">
							<div id="chartsdemo" style="width: 100%; height: 400px"></div>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-12" style="padding: 0px">
							<div id="griddemo" style="width: 100%; height: 400px"></div>
						</div>
					</div>

				</div>
			</div>

		</div>
	</div>
	
	 <div id="dialog-message" class="hide">			
	 </div>
	
</body>
</html>