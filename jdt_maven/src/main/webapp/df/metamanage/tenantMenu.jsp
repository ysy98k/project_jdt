<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<html lang="zh-cn" style="height: 100%">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="author" content="ruiye@baosight.com">
<title>菜单配置</title>
<style type="text/css" id="scriptCss">
.dispContent {
	height: 100%;
}
</style>
</head>
<body>
	<%@ include file="/bxui/bxuihead.jsp"%>
	<script type="text/javascript"
		src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
	<script type="text/javascript"
		src="<%=toolkitPath%>/df/metamanage/tenantMenu.js"></script>
    <link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css" />
	<div class="jqgridFrame contentFrame" style="height: 100%">
		<div class="page-content contentFrame">
			<!-- /sectionController:settings.box -->
			<div class="page-header" style="height: 50px;">
				<h1 class="pull-left">
					菜单配置 <small><i class="ace-icon fa fa-angle-double-right"></i>提供上侧、左侧两种类型的菜单信息配置，实现查询、增加、删除、修改及排序等功能</small>
				</h1>
				<div id="menuType" style="float: right"></div>
			</div>
			<!-- /.page-header -->
			<input type="hidden" id="menuLevel" />
			<div class="row" style="height: 90%">
				<div class="col-xs-12" style="height: 100%">
					<div class="row">
						<div class="col-sm-2 overflow-ie11">
							<div class="row">
								<div id="menutree"></div>
							</div>
						</div>
						<div class="col-sm-10">
							<div class="row">
								<div class="col-xs-12">
									<div id="alertdiv" class="alertdiv"></div>
									<div class="row">
										<div class="col-sm-4">
											<label class="labelThree control-label no-padding-right"
												   for="inqu_status-parentCode">父菜单名</label> <input
												type="hidden" id="inqu_status-parentCode" data-bxwidget
												class="inputThree" /> <input type="text" id="parentName"
																			 data-bxwidget class="inputThree" />
										</div>
										<div class="col-sm-4">
											<label class="labelThree control-label no-padding-right"
												   for="inqu_status-dispName">菜单名称</label> <input type="text"
																								  id="inqu_status-dispName" data-bxwidget class="inputThree" />
										</div>
										<div class="col-sm-4">
											<label class="labelThree control-label no-padding-right"
												   for="inqu_status-menuCode">菜单代码</label> <input type="text"
																								  id="inqu_status-menuCode" data-bxwidget class="inputThree" />
										</div>
									</div>
									<div class="row">
										<div class="col-sm-2"></div>
										<div class="col-sm-2  col-xs-6 no-padding-right-1024">
											<button class="btn btn-sm pull-right btn-block"
													onclick="addAndCopy();">
												<div class="ace-icon fa fa-plus"></div>
												<span>新增/复制</span>
											</button>
										</div>
										<div class="col-sm-2 col-xs-6 no-padding-right-1024">
											<button class="btn btn-sm pull-right btn-block"
													onclick="saveRec();">
												<div class="ace-icon fa fa-save"></div>
												<span>保存</span>
											</button>
										</div>
										<div class="col-sm-2  col-xs-6 no-padding-right-1024">
											<button class="btn btn-sm pull-right btn-danger width-100"
													onclick="deleteRec();">
												<div class="ace-icon fa fa-trash-o"></div>
												<span>删除</span>
											</button>
										</div>
                                        <div class="col-sm-2  col-xs-6 no-padding-right-1024">
                                            <button class="btn btn-sm pull-right btn-block"
                                                    onclick="publish();">
                                                <div class="ace-icon fa fa-play"></div>
                                                <span>发布</span>
                                            </button>
                                        </div>
										<div class="col-sm-2  col-xs-6">
											<button class="btn btn-sm pull-right btn-block"
													onclick="on_query_click(1);">
												<div class="ace-icon fa fa-search"></div>
												<span>查询</span>
											</button>
										</div>
									</div>
								</div>
								<div class="col-xs-12">
									<div id="jqGrid"></div>
								</div>
							</div>
							<!-- /.col -->
						</div>
					</div>
				</div>
			</div>
			<!-- /.row -->
		</div>
	</div>
	
	<!-- ----------------------------------信息弹出框---------------------------------------------------------->
	<div id="dialog-update-message" class="hide"></div>
	<div id="dialog-delect-message" class="hide"></div>
</body>
</html>