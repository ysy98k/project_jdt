<%@ page import="java.util.Enumeration" %>
<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>角色信息管理</title>
<%
	String path = request.getContextPath();
	String curpath=(String)request.getParameter("showtype");

%>

	<!--引入文件： 1、zTree默认css样式  2、jquery  3、zTree js-->
	<link href="<%=path%>/raisingui/zTree/zTreeStyle.css" rel="stylesheet">



	<style>
		/*searchArea*/
		.searchArea{
			margin-bottom: 20px;
		}
		/*searchArea end*/


		/*按钮*/
		.icon_div {
			display: inline-block;
			/*height: 25px;*/
			height: 100%;
			width:50px;
			text-align: center;
		}
		.bigWidth{
			display: inline-block;
			width: 92px!important;

		}
		.operatorButton{
			display: inline-block;
			width: 40px;
			/*height: 20px;*/
			height: 100%;
			cursor: pointer;
			color: white!important;
			padding-left: 3px!important;
			line-height: 24px!important;
		}
		.eighty{
			width: 80px!important;
			background-color: #EE7600!important;

		}
		/*end--按钮*/

		/*ztree表格*/
		.ztree {
			padding: 0;
			border: 2px solid #CDD6D5;
		}

		.ztree li a {
			vertical-align: middle;
			height: 30px;
		}

		.ztree li > a {
			width: 100%;
		}

		.ztree li > a,
		.ztree li a.curSelectedNode {
			padding-top: 0px;
			background: none;
			height: auto;
			border: none;
			cursor: default;
			opacity: 1;
		}

		.ztree li ul {
			padding-left: 0px
		}

		.ztree div.diy span {
			line-height: 30px;
			vertical-align: middle;
		}

		.ztree div.diy {
			height: 100%;
			min-height:34px;
			width: 20%;
			line-height: 30px;
			/*
			border-top: 1px dotted #ccc;
			border-left: 1px solid #eeeeee;
			*/
			border-top: 1px solid rgb(225, 225, 225);
			border-left: 1px solid rgb(225, 225, 225);
			text-align: center;
			display: inline-block;
			box-sizing: border-box;
			color: #6c6c6c;
			/*font-family: "SimSun";*/
			font-family: "Open Sans";
			font-size: 13px;
			font-weight: 400;
			overflow: hidden;
		}

		.ztree div.diy:first-child {
			text-align: left;
			text-indent: 10px;
			border-left: none;
		}
		.ztree div.diy:last-child {
			text-align: left;
			padding-left: 20px;
			width: 40%;
		}

		.ztree .head {
			background: #2184c1;
		}

		.ztree .head div.diy {
			border-top: none;
			border-right: 1px solid #CDD2D4;
			color: #fff;
			/*font-family: "Microsoft YaHei";*/
			font-family: "Open Sans";
			font-size: 14px;
		}
		.ztree .head div.diy:last-child {
			text-align: center;
		}
		/*表格按钮默认展开*/
		/*end--ztree表格*/

	</style>

</head>
<body class="no-skin">
<%@ include file="/bxui/bxuihead.jsp" %>
<%--<script src="<%=path%>/raisingui/jquery/jquery-1.4.4.min.js"></script>--%>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script src="<%=path%>/bxui/util/jsUtil.js"></script>
<script>
	var groupNames = "<%=request.getSession().getAttribute("groupNames")%>"
	var currentUserIdStr = "<%=request.getSession().getAttribute("userId")%>"
	var currentUserId = Number(currentUserIdStr);//用来记录操作人
</script>
<link rel="stylesheet" href="<%=path%>/raisingui/layui/css/layui.css" media="all">
	<div class="jqgridFrame contentFrame">
		<div class="page-content contentFrame" style="border: none;">
			<div class="page-header" style="height: 50px;">
				<h1 class="pull-left">
					角色信息管理
					<small><i class="ace-icon fa fa-angle-double-right"></i>
						增加、修改、删除角色信息
					</small>
				</h1>
			</div>

			<div class="row">
				<div class="col-xs-12 searchArea">
					<div id="alertdiv" class="alertdiv"></div>
					<div class="col-sm-5 no-padding">
						<div class="col-sm-6 col-1024-6 no-padding-left-1024">
							<div class="row">
								<div class="form-group">
									<label
											class="col-sm-4 col-1024-4 col-xs-4 no-padding-right"
											for="inqu_status-name">角色名</label>
									<input type="text" id="inqu_status-name" class="col-xs-8 col-1024-6 col-sm-7" />
								</div>
							</div>
						</div>
						<div class="col-sm-6 col-1024-6 no-padding-left-1024">
							<div class="row">
								<div class="form-group">
									<label
											class="col-xs-4 col-1024-6 col-sm-5 no-padding-right"
											for="inqu_status-display_name">角色显示名</label>
									<input type="text" id="inqu_status-display_name" class="col-xs-8 col-1024-6 col-sm-7" />
								</div>
							</div>
						</div>
					</div>
					<div class="col-sm-6  col-1024-7 col-xs-12 pull-right no-padding-right-1024">
						<div class="col-sm-3 col-xs-6 separate no-padding-left-1024">
							<button class="btn btn-sm pull-right btn-block"
									onclick="queryWithCondition();">
								<div class="ace-icon fa fa-search"></div>
								<span>查询</span>
							</button>
						</div>
						<div class="col-sm-3 col-xs-6 no-padding-right-1024">
							<button class="btn btn-sm pull-right btn-block no-textShadow-1024" onclick="add();">
								<div class="ace-icon fa fa-plus"></div>
								<span>新增</span>
							</button>
						</div>
						<div class="col-sm-3 col-xs-6 no-padding-right-1024">
							<button class="btn btn-sm pull-right btn-block" onclick="saveRole();">
								<div class="ace-icon fa fa-save"></div>
								<span>保存</span>
							</button>
						</div>
					</div>
				</div>
				<!-- /.col -->
			</div>

			<div class="layer">
				<div id="tableMain">
					<ul id="dataTree" class="ztree" data-parentId="">

					</ul>
				</div>
			</div>
		</div>
	</div>

	<!-- -->
	<div id="addFormDiv" class="hide row" style="margin:0px;">
		<form id="addForm" class="form-horizontal" role="form">
			<div class="col-sm-12" style="float: left;padding:0px 0px 15px 0px;">
				<label class="col-sm-3 control-label no-padding-right" for="form_groupName">角色名<span style="color: red">*</span> </label>
				<input class="col-sm-7" style="margin-left: 10px;" type="text" id="form_groupName" name="form_groupName" value="" class=".input-large" />
			</div>
			<div class="col-sm-12" style="float: left;padding:0px 0px 15px 0px;">
				<label class="col-sm-3 control-label no-padding-right" for="form_displayName" style="padding-left: 10px;">角色显示名<span style="color: red">*</span> </label>
				<input class="col-sm-7" style="margin-left: 10px;" type="text" id="form_displayName" name="form_displayName" value="" class=".input-large" />
			</div>
			<div class="col-sm-12" style="float: left;padding:0px 0px 15px 0px;">
				<label class="col-sm-3 control-label no-padding-right" for="form_groupDescription">角色描述<span style="color: red">*</span> </label>
				<input class="col-sm-7" style="margin-left: 10px;" type="text" id="form_groupDescription" name="form_groupDescription" value="" class=".input-large" />
			</div>

		</form>
		<div id="confrmEdit" class="hide row" style="margin:0px;">确定保存么？</div>
	</div>

	<!-- ----------------------------------信息弹出框---------------------------------------------------------->	
	<div id="dialog-update-message" class="hide">
	</div>
	<div id="dialog-message" class="hide">
		<p class="bigger-110 bolder center grey">
			<br/>
			<b id="dialogInfo"></b>
		</p>
	</div>

	
	<div id="popupwindow" class="hide">
		<iframe  frameborder="0"  id="inpopup" src="" width=870 height=580></iframe>
		<iframe  frameborder="0"  id="inpopup2" style="display:none" src="" width=870 height=580></iframe>
	</div>

	<script src="<%=path%>/raisingui/zTree/jquery.ztree.all.js"></script>
	<script type="text/javascript" src="<%=toolkitPath%>/raising/backstage/privilege/RoleManagement.js"></script>
</body>
<script type="text/javascript">
if('<%=curpath%>'=='jdt'){
	$('#topNav').css('display','none');
	$('.contentFrame').css('padding-top','0px');
}
</script>
</html>

