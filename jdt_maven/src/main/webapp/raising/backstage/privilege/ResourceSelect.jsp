<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>资源信息管理</title>
<%
	String path = request.getContextPath();
%>
	<style>
		.th-checkbox{
			display: inline-block;
			vertical-align: text-top;
		}
		#zTreeContain{
			margin-left: 15px;
		}
		#zTreeDiv{
			margin-top: 10px;
			border: 1px solid  #2184c1;
			min-height: 415px;
		}
		.tableTile{
			margin: 0px 0px;
			background-color: rgb(33, 132, 193)!important;
			height: 38px;
			padding:7px 0px 0px 12px;
			line-height: 24px;
			color: white;
		}

		/*Ztree*/
		li[class^="leve"]{
			line-height:24px!important;
		}
		.node_name{
			line-height:24px!important;
			font-size: 13px!important;
			font-family: Microsoft YaHei!important
		}
		.ztree li span.button{
			margin-top: 3px!important;
		}
	</style>
	<link href="<%=path%>/raisingui/zTree/zTreeStyle.css" rel="stylesheet">
</head>
<body class="no-skin">
	<%@ include file="/bxui/bxuihead.jsp"%>
	<script type="text/javascript" src="<%=toolkitPath%>/bxui/vue/vue.min.js"></script>
	<script src="<%=toolkitPath%>/raisingui/zTree/jquery.ztree.all.js"></script>
	<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>

    <link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css" />
	<div class="jqgridFrame contentFrame" id="baba">
		<div class="page-content contentFrame" style="border: none;padding-bottom: 0">
			<!-- /section:settings.box -->
			<div class="page-header" style="height: 50px;">
				<h1 class="pull-left">
					资源信息管理 <small> <i class="ace-icon fa fa-angle-double-right"></i>
						查询资源信息
					</small>
				</h1>
			</div>

			<div class="row">

				<div class="col-xs-12 searchArea">

					<div id="alertdiv" class="alertdiv"></div>

					<div class="col-sm-4" >
						<div class="row" id="resourceNameDiv">
							<div class="form-group">
								<label
									class="searchLabel col-sm-4 control-label no-padding-right"
									for="inqu_status-name">资源名</label>
								<div class="col-sm-7 inputFrame">
									<input type="text" id="inqu_status-name" class="searchInput" />
								</div>
							</div>
						</div>
					</div>		
					<div class="col-sm-4 col-xs-7">
							<div class="row">
								<div class="form-group">
									<label
										class="searchLabel col-sm-4 col-xs-4 control-label no-padding-right"
										for="inqu_status-service">所属服务</label>
									<%--<div class="clearfix visible-xs"></div>--%>
									<div class="col-sm-7 col-xs-7 inputFrame" id="selectDiv" data-width="">
										<div id="inqu_status-service">
											<select name="undefined_select" id="inqu_status-service_select" style="width:100%" class="form-control"
													v-model="selectService" @change="serviceChange">
												<option v-bind:value="service.serviceCode" v-for="service in serviceList" >{{service.serviceName}}</option>
											</select>
										</div>
									</div>
								</div>
							</div>
					</div>		
					
					<div class="col-sm-4 col-xs-4">
						<div class="row">
							<div class="col-sm-6" id="searchButtonDiv">
								<button class="btn btn-sm btn-info btn-block pull-left searchBtn"
									style="width: 90%" onclick="on_query_click();">
									<div class="ace-icon fa fa-search"></div>
									<span>查询</span>
								</button>
							</div>
							<div class="col-sm-6 col-xs-11">
								<button class="btn btn-sm btn-info btn-block pull-right searchBtn"
									style="width: 120%" onclick="confirmreturn();">
									<div class="ace-icon fa fa-search"></div>
									<span>确认并返回</span>
								</button>
							</div>
						</div>
					</div>
											
				</div>

				<div class="col-xs-12">
					<div id="jqGrid" style="width: 100%; height: 100%; margin-top: 5px"></div>

					<div class="row" id="zTreeContain">
						<div class="zTreeDemoBackground center" id="zTreeDiv" style="width: 350px;">
							<div class="tableTile">
								<div style="line-height: 24px;font: 15px Open Sans;text-align: left!important;">
									<span id="sTitle" class="widget-title lighter smaller">菜单树</span>
								</div>
							</div>
							<ul id="treeDemo" class="ztree"></ul>
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>
	<script type="text/javascript" src="<%=toolkitPath%>/raising/backstage/privilege/ResourceSelect.js"></script>
</body>
</html>

