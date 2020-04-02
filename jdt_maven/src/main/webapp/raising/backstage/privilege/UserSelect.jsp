<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户查询</title>
</head>
<body class="no-skin">
	<%@ include file="/bxui/bxuihead.jsp"%>
	<script type="text/javascript"
		src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
	<script type="text/javascript"
		src="<%=toolkitPath%>/raising/backstage/privilege/UserSelect.js"></script>
	<link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css" />
    <%
        String type = request.getParameter("type");
        String curGroup = request.getParameter("curGroup");
        String curOrg = request.getParameter("curOrg");
    %>

	<div class="jqgridFrame contentFrame">
		<div class="page-content contentFrame" style="border: none;padding-bottom: 0">
			<!-- /section:settings.box -->
			<div class="page-header" style="height: 50px;">
				<h1 class="pull-left">
					用户信息管理 <small> <i class="ace-icon fa fa-angle-double-right"></i>
						查询用户信息
					</small>
				</h1>

			</div>



			<div class="row">

				<div class="col-xs-12 searchArea">

					<div id="alertdiv" class="alertdiv"></div>
                    <input type="hidden" id="curGroup" value=<%=curGroup%> />
                    <input type="hidden" id="curOrg" value=<%=curOrg%> />
					<div class="col-sm-4">
						<div class="row">
							<div class="form-group">
								<label
									class="searchLabel col-sm-4 control-label no-padding-right"
									for="inqu_status-name">用户名</label>
								<div class="col-sm-7 inputFrame">
									<input type="text" id="inqu_status-name" class="searchInput" />
								</div>
							</div>
						</div>
					</div>		
					<div class="col-sm-4">
						<div class="row">
							<div class="form-group">
								<label
									class="searchLabel col-sm-4 control-label no-padding-right"
									for="inqu_status-display_name">显示名</label>
								<div class="col-sm-7 inputFrame">
									<input type="text" id="inqu_status-display_name" class="searchInput" />
								</div>
							</div>
						</div>
					</div>		
					
					<div class="col-sm-4">
						<div class="row">
							<div class="col-sm-6">
								<button class="btn btn-sm btn-info btn-block pull-left searchBtn"
									style="width: 90%" onclick="on_query_click();">
									<div class="ace-icon fa fa-search"></div>
									<span>查询</span>
								</button>
							</div>
							<div class="col-sm-6">
								<button class="btn btn-sm btn-info btn-block pull-right searchBtn"
									style="width: 120%" onclick="confirmreturn('<%=type%>');">
									<div class="ace-icon fa fa-check"></div>
									<span>确认并关闭</span>
								</button>
							</div>
						</div>
					</div>
											
				</div>

				<div class="col-xs-12">
					<div id="jqGrid" style="width: 100%; height: 100%; margin-top: 5px"></div>
				</div>
			</div>
			<!-- /.row -->


		</div>
	</div>
	<!-- page-content -->

</body>
</html>

