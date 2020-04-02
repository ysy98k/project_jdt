<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="org.apache.shiro.subject.Subject"%>
<%@ page import="org.apache.shiro.SecurityUtils"%>
<%@ page import="java.util.Enumeration" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户信息管理</title>
<%
	String path = request.getContextPath();
%>
    <style>
        .contentFrame{
        'padding-top':'0px'
        }
        body{
            height: 100%;
        }
        .textbox.combo{
            margin-left: 0px!important;
        }
    </style>


</head>
<body class="no-skin">
	<%@ include file="/bxui/bxuihead.jsp"%>
    <script>
        var groupNames = "<%=request.getSession().getAttribute("groupNames")%>"
        var currentUserIdStr = "<%=request.getSession().getAttribute("userId")%>"
        var currentUserId = Number(currentUserIdStr);//用来记录操作人
        var h =  document.documentElement.clientHeight;
        document.getElementsByTagName('body')[0].style.height = h+"px";
    </script>
    <link rel="stylesheet" href="<%=toolkitPath%>/bxui/daterangepicker/css/daterangepicker.css"/>
    <script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/moment.min.js"></script>
    <script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/daterangepicker.js"></script>
    <script type="text/javascript" src="<%=toolkitPath%>/bxui/vue/vue.min.js"></script>
    <script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
    <script type="text/javascript" src="<%=toolkitPath%>/bxui/easyui-plugin/js/jquery.easyui.min.js"></script>

    <script src="<%=path%>/bxui/util/jsUtil.js"></script>

    <link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css" />
    <link rel="stylesheet" href="<%=toolkitPath%>/bxui/easyui-plugin/css/easyui.css"/>


    <%
		int currentmenu = 0;
	%>




    <div class="jqgridFrame contentFrame">
		<div class="page-content contentFrame" style="border: none;">
            <div class="page-header" style="height: 50px;margin-top: 8px;">
                <h1 class="pull-left">
                    用户管理
                    <small><i class="ace-icon fa fa-angle-double-right"></i>
                        增加、修改、删除用户信息
                    </small>
                </h1>

            </div>

			<div class="row" >
				<div class="col-xs-12 searchArea">
					<div id="alertdiv" class="alertdiv"></div>

                    <div class="row">
                        <div class="col-sm-4 col-1024-12  no-padding">
                            <div class="col-sm-6 col-1024-6">
                                <label
                                        class="col-sm-5 col-xs-4 col-1024-3 no-padding-right"
                                        for="inqu_status-username">用户名</label>
                                <input type="text" id="inqu_status-username"
                                       class="col-sm-7 col-xs-8 col-1024-9" />
                            </div>

                            <div class="col-sm-6 col-1024-6">
                                <label
                                        class="col-sm-5 col-xs-4 col-1024-3 no-padding-right"
                                        for="inqu_status-displayName">显示名</label>
                                <input type="text" id="inqu_status-displayName"
                                       class="col-sm-7 col-xs-8 col-1024-9" />
                            </div>
                        </div>
                        <div class="col-sm-8 col-1024-12 col-xs-12 pull-right no-padding">
                            <div class="col-sm-2 col-xs-6 separate">
                                <button class="btn btn-sm pull-right btn-block"
                                        onclick="on_query_click();">
                                    <div class="ace-icon fa fa-search"></div>
                                    <span>查询</span>
                                </button>
                            </div>
                            <div class="col-sm-2 col-xs-6">
                                <button class="btn btn-sm pull-right btn-block" onclick="insertPage();">
                                    <div class="ace-icon fa fa-plus"></div>
                                    <span>新增</span>
                                </button>
                            </div>
                            <div class="col-sm-2 col-xs-6">
                                <button class="btn btn-sm pull-right btn-block" onclick="updatePage();">
                                    <div class="ace-icon fa fa-pencil-square-o"></div>
                                    <span>修改</span>
                                </button>
                            </div>
                            <div class="col-sm-2 col-xs-6">
                                <button class="btn btn-sm pull-right btn-danger width-100" onclick="deletePage();">
                                    <div class="ace-icon fa fa-trash-o"></div>
                                    <span>删除</span>
                                </button>
                            </div>
                            <div class="col-sm-2 col-xs-6">
                                <button class="btn btn-sm pull-right btn-block"
                                        onclick="abandon();">
                                    <div class="ace-icon fa fa-times-circle-o"></div>
                                    <span>禁用</span>
                                </button>
                            </div>
                            <div class="col-sm-2 col-xs-6">
                                <button class="btn btn-sm pull-right btn-block"
                                        onclick="keepValidity();">
                                    <div class="ace-icon fa fa-check-circle-o"></div>
                                    <span>永久有效</span>
                                </button>
                            </div>
                        </div>
                    </div>
				</div>


				<div class="col-xs-12" style="min-height: 600px;">
					<div id="jqGrid" style="width: 100%; height: 95%; margin-top: 2px;"></div>
				</div>
			</div>
		</div>
	</div>


	<!-- -----------------------------insert pop div---------------------------------------------------------->
	<form id="detail" class="hide" style="overflow: hidden;">
		<input type="hidden" id="detail-uid" data-bxwidget
			data-bxtype="number" data-bxauto />
        <%--<input type="text" class="hide" id="detail-parentGroupId"  value="" data-bxwidget data-bxtype="number" data-bxauto />--%>
        <span id="parentGroupId" class="hide"></span>
        <span id="parentGroupName" class="hide"></span>
		<div class="row rowspace">


			<div class="col-sm-12">
				<div class="form-group">
					<label class="col-sm-4 control-label no-padding-right labelfont"
						for="detail-username"><i style="color: red" id="required">* </i> 用户名
					</label>
					<div class="col-sm-7">
						<input type="text" id="detail-username" name="detailusername"
							class="col-xs-12 col-sm-12" data-bxwidget data-bxtype="string" />
					</div>
				</div>
			</div>
		</div>
		<div class="row rowspace">
			<div class="col-sm-12">
				<div class="form-group">
					<label class="col-sm-4 control-label no-padding-right labelfont"
						for="detail-displayName"><font style="color: red">* </font> 显示名 </label>
					<div class="col-sm-7">
						<input type="text" id="detail-displayName"
							name="detaildisplayName" class="col-xs-12 col-sm-12"
							data-bxwidget data-bxtype="string" />
					</div>
				</div>
			</div>
		</div>
        <div class="row rowspace">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="col-sm-4 control-label no-padding-right labelfont" for="detail-groupId"><font style="color: red">* </font> 角色 </label>
                    <div class="col-sm-7">
                        <select id="detail-groupId" name="detail-groupId"  class="easyui-combobox hide"
                                data-options="valueField:'groupId',textField:'groupName'" >
                        </select>
                        <div id="groupNamesStr" style="padding-top: 5px;">
                            <span v-if="groupNamesStr.length <= 12">{{groupNamesStr}}</span>
                            <span v-else v-bind:title="groupNamesStr">{{groupNamesStr.substring(0,9)}}...</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row rowspace">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="col-sm-4 control-label no-padding-right labelfont"
                           for="detail-description"> 用户简介 </label>
                    <div class="col-sm-7">
                        <input type="text" id="detail-description"
                               name="detaildescription" class="col-xs-12 col-sm-12"
                               data-bxwidget data-bxtype="string" />
                    </div>
                </div>
            </div>
        </div>
        <div class="row rowspace">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="col-sm-4 control-label no-padding-right labelfont"
                           for="detail-telephone"><span style="color: red">* </span> 电话
                    </label>
                    <div class="col-sm-7">
                        <input type="text" id="detail-telephone"
                               name="detailtelephone" class="col-xs-12 col-sm-12"
                               data-bxwidget data-bxtype="string" />
                    </div>
                </div>
            </div>
        </div>
        <div class="row rowspace">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="col-sm-4 control-label no-padding-right labelfont"
                           for="detail-email"> 邮箱
                    </label>
                    <div class="col-sm-7">
                        <input type="text" id="detail-email"
                               name="detailemail" class="col-xs-12 col-sm-12"
                               data-bxwidget data-bxtype="string" />
                    </div>
                </div>
            </div>
        </div>
        <div class="row rowspace">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="col-sm-4 control-label no-padding-right labelfont"
                           for="detail-employeeid"> 工号
                    </label>
                    <div class="col-sm-7">
                        <input type="text" id="detail-employeeid"
                               name="detailemployeeid" class="col-xs-12 col-sm-12"
                               data-bxwidget data-bxtype="string" />
                    </div>
                </div>
            </div>
        </div>

		<div class="row rowspace" id="password" style="display:block">
			<div class="col-sm-12">
				<div class="form-group">
					<label class="col-sm-4 control-label no-padding-right labelfont"
						for="detail-password"><font style="color: red">* </font> 密码 </label>
					<div class="col-sm-7">
						<input type="hidden" id="detail-password" name="detailpassword"
							class="col-xs-12 col-sm-12" data-bxwidget data-bxtype="string" />
						<input type="password" id="thepassword" name="thepassword"
							class="col-xs-12 col-sm-12" data-bxwidget data-bxtype="string" />
					</div>
				</div>
			</div>
		</div>
		<div class="row rowLastSpace" id="confirm_password" style="display:block">
			<div class="col-sm-12">
				<div class="form-group">
					<label class="col-sm-4 control-label no-padding-right labelfont"
						for="detail-password_confirm"><font style="color: red">* </font> 确认密码
					</label>
					<div class="col-sm-7">
						<input type="hidden" id="detail-password_confirm"
							name="detailpassword_confirm" class="col-xs-12 col-sm-12"
							data-bxwidget data-bxtype="string" /> <input type="password"
							id="thepasswordconfirm" name="thepasswordconfirm"
							class="col-xs-12 col-sm-12" data-bxwidget data-bxtype="string" />
					</div>
				</div>
			</div>
		</div>

	</form>
    <!-- ----------------------------------设置有效期弹出框---------------------------------------------------------->
    <div id="validity" class="hide" style="overflow: hidden;">
        <div class="row rowspace">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="col-sm-3 control-label labelfont"
                           for="validity-time"> 有效期
                    </label>
                    <div id="validity-time" class="col-sm-9 input-group" data-bxwidget="bxtimepicker">
                    </div>
                </div>
            </div>
        </div>
    </div>
	<!-- ----------------------------------信息弹出框---------------------------------------------------------->
	<div id="dialog-update-message" class="hide"></div>
	<div id="dialog-message" class="hide">
		<p class="bigger-110 bolder center grey">
			<br /> <b id="dialogInfo"></b>
		</p>
	</div>
    <script type="text/javascript" src="<%=toolkitPath%>/raising/backstage/privilege/UserManagement.js"></script>
</body>
</html>

