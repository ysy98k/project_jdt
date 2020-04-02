
<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.baosight.aas.auth.Constants" %>
<%@ page import="com.baosight.sys.controller.LoginController" %>
<%@ page import="java.util.Properties" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.net.InetAddress" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
	<title>力信盾构大数据平台</title>

	<%
		String toolkitPath = request.getContextPath();
		String skinName = (String) request.getSession().getAttribute("skinName");
		if (skinName == null || skinName.equals(""))
			skinName = "blue-skin";

		String path = request.getContextPath();
		//loginName:name@test
		String loginName=(String)request.getSession().getAttribute("loginName");
		String userName = (String)request.getSession().getAttribute(Constants.SESSION_USERNAME_KEY);
		String tenant = (String)request.getSession().getAttribute(Constants.SESSION_TENANT_KEY);
		String groupDisplayNames = (String)request.getSession().getAttribute(Constants.SESSION_GROUPDISPLAYNAME_KEY);
		String groupName = (String)request.getSession().getAttribute(Constants.SESSION_GROUPNAME_KEY);
		String username=(String)request.getSession().getAttribute("username");
		String userid=request.getSession().getAttribute("userId") + "";
		String token = (String)request.getSession().getAttribute("token");
	%>
	<script>
		var httpServerPath = window.location.protocol + "//" + window.location.host + "<%=toolkitPath%>";
		var toolkitPath = "<%=toolkitPath%>";
		document.getElementsByTagName( "body")[0].className += " " + "<%=skinName%>";
	</script>

	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta name="description" content="platForm Frameset" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
	<link type="text/css" rel="stylesheet" href="<%=toolkitPath%>/bxui/bootstrap/dist/css/bootstrap.css"></link>
	<link type="text/css" rel="stylesheet" href="<%=toolkitPath%>/bxui/jquery-ui/css/jquery-ui.css"></link>
	<link type="text/css" rel="stylesheet" href="<%=toolkitPath%>/bxui/aceadmin/style/css/font-awesome.css"></link>
	<link type="text/css" rel="stylesheet" href="<%=toolkitPath%>/bxui/aceadmin/style/css/ace.css"></link>
	<link type="text/css" rel="stylesheet" href="<%=toolkitPath%>/bxui/bxwidget/css/bx-skin.css"></link>

	<style>
		li[data-menucode="chartShow"]{
			display: none!important;
		}
	</style>

</head>

<body class="black-skin">
<iframe id="temp" src="https://bd.sh-raising.com/cvscfgsvr/console/main.html?token=<%=token%>" class="hide" style="display: none;"></iframe>


<%--<%@ include file="/bxui/bxuihead.jsp" %>--%>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery/dist/jquery.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery-ui/js/jquery-ui.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/easyui-plugin/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/aceadmin/js/ace-extra.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/aceadmin/js/ace-elements.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/aceadmin/js/ace.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/other/js/bxcommon.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxdialog.js"></script>
<script type="text/javascript" src="<%=path%>/df/console/frameTop.js"></script>
<script type="text/javascript">
    /*与cvs集成的时候，需要在sessionStorage加上这3个属性*/
    window.sessionStorage.setItem("username",'<%=userName%>');
    window.sessionStorage.setItem("tenant",'<%=tenant%>');
    window.sessionStorage.setItem("groupDisplayNames",'<%=groupDisplayNames%>');

    //获取框架配置
    var frame_bigLogoIcon;
    var frame_frontPagePath;
    var frame_frontPageType;
    var frame_logoIcon;
    var frame_logoText;
    var frame_skinName;
    var frame_pageName;
    $.ajax({
        url:toolkitPath+"/df/metamanage/frameSetting.do?method=queryFrontFrame",
        type:"post",
        data:{"ajaxParam":"{}"},
        dataType:"json",
        async:false,
        success:function (paramJsonObj) {
            if(typeof(paramJsonObj.skinName)!="undefined"){
                document.body.className=paramJsonObj.skinName;
                frame_skinName=paramJsonObj.skinName;
                frame_bigLogoIcon=paramJsonObj.bigLogoIcon;
                frame_logoIcon=paramJsonObj.logoIcon;
                frame_logoText=paramJsonObj.logoText;
                frame_pageName=paramJsonObj.pageCode;

                if(paramJsonObj.pagePath!=undefined){
                    frame_frontPagePath=paramJsonObj.pagePath;
                    frame_frontPageType=paramJsonObj.pageType;
                }else{
                    frame_frontPagePath="";
                    frame_frontPageType="";
                }
            }
        }
    });

</script>
<link rel="stylesheet" href="<%=path%>/df/console/css/style-frameTop.css" />
<div class="noNavber"></div>
<div id="navbar" class="navbar navbar-default navbar-collapse h-navbar" mouseOver="" mouseOut="">
	<script type="text/javascript">
        try{ace.settings.check('navbar' , 'fixed')}catch(e){}
	</script>
	<div class="navbar-container" id="navbar-container">
		<!-- #section:basics/sidebar.mobile.toggle -->

		<button class="pull-right navbar-toggle collapsed" type="button" data-toggle="collapse" data-target=".sidebar">
			<span class="sr-only">Toggle sidebar</span>

			<span class="icon-bar"></span>

			<span class="icon-bar"></span>

			<span class="icon-bar"></span>
		</button>
		<div class="pull-left navButtonText">
			<a class="navbar-brand" onClick="reload()" title="首页" style="cursor: pointer">
				<small><img id="logoIcon" src="" style="max-height: 25px" onClick="reload()"></small>
			</a>
		</div>

		<div class="pull-left logoDiv"><a onClick="reload()" title="首页" style="cursor: pointer">
			<span class="navButtonText" id="logoText"></span></a>
		</div>
		<div class="pull-right upButton" role="navigation">
			<ul class="nav ace-nav">
				<li style="width: 30px;background-color: #5a5959">
					<a data-toggle="dropdown" onclick="hideNavbar();" href="#" class="dropdown-toggle" >
						<i class="ace-icon fa fa-chevron-up"></i>
					</a>
				</li>
			</ul>
		</div>
		<div class="pull-right"
			 role="navigation" id="userControl">
			<ul class="nav ace-nav">
				<li class="xInsightGray">
					<a data-toggle="dropdown" href="#" class="dropdown-toggle" >
						<span class="user-info"><%=loginName%></span>
						<%--<i class="ace-icon fa fa-caret-down"></i>--%>
					</a>

					<ul class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-close">
						<li class="divider"></li>
						<li>
							<a href="#" onClick="updatePW();" ><i class="ace-icon fa fa-edit"></i>修改密码</a>
						</li>
						<li>
							<a href="#" onClick="openWin('show');" ><i class="ace-icon fa fa-lock"></i>锁定屏幕</a>
						</li>
						<li>
							<a href="<%=path%>/logout" ><i class="ace-icon fa fa-sign-out"></i>退出登录</a>
						</li>

					</ul>
				</li>
			</ul>
		</div>
		<%--<c:if test="${sessionScope.groupDisplayNames eq '管理员组'}">
			<div class="pull-right" role="navigation">
				<ul class="nav ace-nav">
					<li class="xInsightGray">
						<a data-toggle="dropdown" onclick="readyToOpenForm('<%=path%>/df/console/frame.do','self');" href="#" class="dropdown-toggle" >
							<span class="user-info">后台管理</span>
						</a>
					</li>
				</ul>
			</div>
		</c:if>--%>

	</div>
	<!-- /.navbar-container -->
</div>
<script>
    var groupName = "<%=groupName%>";
    $.ajax({
        url:toolkitPath+"/raising/froward/frameTop/init.do",
        type:"get",
        data:{"groupname":groupName},
        dataType:"json",
        async:false,
        success:function (data) {
            if(true == data.containFrame){
                var str = "<div class=\"pull-right\" role=\"navigation\">\n" +
                    			"<ul class=\"nav ace-nav\">\n" +
                    				"<li class=\"xInsightGray\">\n" +
                    					"<a data-toggle=\"dropdown\" onclick=\"readyToOpenForm('<%=path%>/df/console/frame.do','self');\" href=\"#\" class=\"dropdown-toggle\" >\n" +
                    						"<span class=\"user-info\">后台管理</span>\n" +
                    					"</a>\n" +
                    				"</li>\n" +
                    			"</ul>\n" +
                    		"</div>";
                $("#navbar-container").append(str);
			}
        }
    });

</script>

<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
	<script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
	</script>

	<!-- #section:basics/sidebar -->
	<div id="sidebar" class="sidebar h-sidebar navbar-collapse collapse">
		<script type="text/javascript">
            try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
		</script>
		<div class="pull-left bigLogo">
			<a class="navbar-brand" onClick="reload()" style="cursor:pointer" title="首页">
				<small><img id="bigLogoIcon" src=""></small>
			</a>
		</div>

		<ul  class="nav nav-list menu"></ul>

		<!-- #section:basics/sidebar.layout.minimize -->
		<div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
			<i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
		</div>

		<!-- /section:basics/sidebar.layout.minimize -->
		<script type="text/javascript">
            //动态生成菜单
            var param = new Object();
            param={"menuType":"topMenu","fieldName":"menuOrder","ascDesc":"asc"};
            $.ajax({
                url:toolkitPath+"/df/metamanage/tenantMenu.do?method=queryHomeAndBackgroundMenu",
                type:"post",
                data:{"ajaxParam":JSON.stringify(param)},
                dataType:"json",
                success:function(paramJsonObj) {
                    var authMenus = paramJsonObj.authMenus;
                    genMenu(authMenus);

                    if(document.body.clientWidth> 992){
                        $(".nav-list").children("li:first").children().addClass("tabSel");
                        $(".nav-list").children("li:first").addClass("active");
                    }
                }
            });


            //1-2级菜单的递归算法
            function genMenu(menus){
                for(var i=0;i<menus.length;i++){
                    var iconPath="";
                    if(menus[i].iconType!="noneIcon"){
                        iconPath=menus[i].iconPath;
                    }

                    var subMenu="";
                    var subMenuArrow="";
                    var subMenuCss="";
                    var subLink="";
                    if(menus[i].parentCode=='0') {//1级菜单
                        if (menus[i].subMenus != null) {
                            subMenuArrow = "<b class='arrow fa fa-angle-down'></b>";
                            subMenuCss = "class='dropdown-toggle'";
                            subMenu = "<b class='arrow'></b><ul class='submenu'></ul>";
                            var menuDiv = "<li class='hover tabSelColor' data-menuCode=" + menus[i].menuCode + ">" +
                                "<a href='' class='dropdown-toggle'><i class='menu-icon fa " + iconPath + "'></i>" +
                                "<span class='menu-text'>" + menus[i].dispName + "</span>" + subMenuArrow +
                                "</a>" + subMenu + "</li>";
                        } else {
                            if (menus[i].linkType == 'leafNode') {
                                var subLink = "onClick='readyToOpenForm(" + '\"' + menus[i].pagePath + '\"' + ',\"' + menus[i].linkParam + '\"' + ',\"' + menus[i].parentCode +"?firstLevel="+menus[i].parentName+"&secondLevel="+menus[i].dispName+"\")'";
                            }
                            var menuDiv = "<li class='hover tabSelColor' data-menuCode=" + menus[i].menuCode + ">" +
                                "<a href='' class='dropdown-toggle' " + subLink + "><i class='menu-icon fa " + iconPath + "'></i>" +
                                "<span class='menu-text'>" + menus[i].dispName + "</span>" + subMenuArrow +
                                "</a>" + subMenu + "</li>";
                        }
                        $(".nav-list").append(menuDiv);
                    }else {
                        if(menus[i].subMenus!=null){
                            subMenu="<b class='arrow'></b><ul class='submenu'></ul>";
                        }else{
                            if (menus[i].linkType == 'leafNode') {
                                var subLink = "onClick='readyToOpenForm(" + '<%=path%>\"' + menus[i].pagePath + '\"' + ',\"' + menus[i].linkParam + '\"' + ',\"' + menus[i].parentCode +"?firstLevel="+menus[i].parentName+"&secondLevel="+menus[i].dispName+ "\")'";
                            }
                        }

                        if (menus[i].menuLevel == 0) {//1级菜单
                            if (menus[i].iconType == "customIcon") {
                                var icon = "<%=path%>/df/console/images/" + iconPath;
                                var menuDiv = "<li class='hover tabSelColor' data-menuCode=" + menus[i].menuCode + ">" +
                                    "<a href='' class='dropdown-toggle' " + subLink + "><i class='menu-icon'><img class='topIcon' src=" + icon + "></i>" +
                                    "<span class='menu-text'>" + menus[i].dispName + "</span>" + subMenuArrow +
                                    "</a>" + subMenu + "</li>";
                            } else {
                                var menuDiv = "<li class='hover tabSelColor' data-menuCode=" + menus[i].menuCode + ">" +
                                    "<a href='' class='dropdown-toggle' " + subLink + "><i class='menu-icon fa " + iconPath + "'></i>" +
                                    "<span class='menu-text'>" + menus[i].dispName + "</span>" + subMenuArrow +
                                    "</a>" + subMenu + "</li>";
                            }
                            $(".nav-list").append(menuDiv);
                        } else {//2级菜单
                            if (menus[i].iconType == "customIcon") {
                                var icon = "<%=path%>/df/console/images/" + iconPath;
                                var subMenuDiv = "<li class='hover subSelColor' id=menuParent_" + menus[i].parentCode + i + ">" +
                                    "<a href='' class='dropdown-toggle' " + subLink + ">" +
                                    "<i class='menu-icon'><img class='subIcon' src=" + icon + "></i>" + menus[i].dispName + subMenuArrow +
                                    "</a>" + subMenu + "</li>";
                            } else {
                                var subMenuDiv = "<li class='hover subSelColor' id=menuParent_" + menus[i].parentCode + i + ">" +
                                    "<a href='' class='dropdown-toggle' " + subLink + ">" +
                                    "<i class='menu-icon fa " + iconPath + "'></i>" + menus[i].dispName + subMenuArrow +
                                    "</a>" + subMenu + "</li>";
                            }

                            if (i == 0) {
                                $("#sidebar-collapse").prev().children(":last").find("ul:last").append(subMenuDiv);
                            } else {
                                var j = (i - 1).toString();
                                $("#menuParent_" + menus[i].parentCode + j).after(subMenuDiv);
                            }
                        }
                    }
                    if(menus[i].subMenus!=null){
                        genMenu(menus[i].subMenus);
                    }
                }
            }
		</script>
		<script type="text/javascript">
            try {
                ace.settings.check('sidebar', 'collapsed')
            } catch (e) {
            }
            var State=1;//上菜单是否固定，0固定，1浮动
            $(document).ready(function(){
                //$(".main-content").css("background-color","rgb(37,46,60)");//iframe切换的时候需要增加底色效果
                if(frame_frontPagePath!=undefined && frame_frontPagePath!="")
                    readyToOpenForm(frame_frontPagePath,"embed");
                else{
                    var last_gritter;
                    if(last_gritter) $.gritter.remove(last_gritter);
                    last_gritter = $.gritter.add({
                        title: '操作失败！',
                        text: '请确认“系统设置”中的“首页设置”是否配置正确！',
                        class_name: 'gritter-error gritter-center'
                    });
                    return;
                }

                //首页ifram循环刷新菜单栏自动切换定位(轨道增加的需求)
                function iframeFresh(){
                    var i = Math.round(Math.random()*9+1);
                    $(".subSelColor a")[i].click();
                    var iframe_attr = $(window.frames["contentFrame"]).attr("data-parentcode");
                    $("li.tabSelColor").each(function(){
                        if(iframe_attr == $(this).attr("data-menuCode")){
                            $(this).click();
                        }
                    })
                }

                //动态加载样式
                $("#logoText").html(frame_logoText);
                $("#logoIcon").attr("src",frame_logoIcon);
                $("#bigLogoIcon").attr("src",frame_bigLogoIcon);
                $("a").bind('dragstart', function(evt){return false;})//所有链接拖动时不显示详细url信息
            });

            function readyToOpenForm(node,openLink,parentCode){
                if(openLink=="embed") {
                    document.getElementById("contentFrame").src = node;
                    document.getElementById("contentFrame").setAttribute("data-parentCode",parentCode);
                }
                else if(openLink=="self"){
                    location.href =node;
                }else{
                    window.open (node);
                }
				//改变菜单颜色
				var currentNode = $("a[onclick *= '"+node+"']")[0];
                if(currentNode!=undefined &&!$(currentNode).hasClass("tabSel")){
                    $("li[class='hover tabSelColor active']").find("a").eq(0).removeClass("tabSel");
                    $("li[class='hover tabSelColor active']").removeClass("active");
                    $(currentNode).addClass("tabSel");
                    $(currentNode.parentNode).addClass("active");
                }
                //$(this).addClass("tabSel");
                /*if($(".navbar-toggle").css("display")=="none") {
                    $(this).addClass("active");
                }*/
            }



            $(".subSelColor").bind().click(function(e){
                if($(this).children("ul").length==0){
                    if($(".subSelColor").children().hasClass("subtabSel")){
                        $(".subSelColor").children().removeClass("subtabSel");
                    }
                    $(this).children().addClass("subtabSel");
                }
            });

            $("a[data-toggle='dropdown']").click(function(){
                if( $(this.parentNode).hasClass("open") ){
                    $(this.parentNode).removeClass("open");
				}else{
                    $(this.parentNode).addClass("open");
				}
			})

            function hideNavbar(){
                if(State==1){
                    $('#navbar').animate({
                            top: -45
                        },
                        500,function () {
                            $("#navbar").css("display","none");
                        }
                    );
                }
            }

            function showNavbar(){
                if(State==1){
                    $("#navbar").css("display","block");
                    $('#navbar').animate({
                            top: 0
                        },
                        500);
                }
            }

            function reload(){
                location.reload();
            }
            $("iframe").height($("#sidebar").height());
		</script>
	</div>
	<div class="arrowdown" onclick="showNavbar();"></div>

	<!-- /section:basics/sidebar -->
	<div class="main-content" style="-webkit-overflow-scrolling:touch;overflow:auto;">
		<!-- #section:basics/content.breadcrumbs -->
		<iframe id="contentFrame" class="contentFrame" src="" data-parentCode="" style="overflow:scroll"></iframe>
	</div>
	<!-- /.main-content -->


	<a href="#" id="btn-scroll-up"
	   class="btn-scroll-up btn btn-sm btn-inverse"> <i
			class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
	</a>
</div>
<!-- /.main-container -->


<!-- 修改密码弹出框 -->
<div id="detail" class="hide" style="overflow:hidden">
	<input type="hidden" id="detail-tid" data-bxwidget data-bxtype="number" data-bxauto />
	<div class="row rowspace">
		<div class="col-sm-12">
			<div class="form-group">
				<label class="col-sm-4 control-label no-padding-right labelfont"
					   for="loginName"> 账户 </label>
				<div class="col-sm-8">
					<input type="text" id="loginName"
						   class="col-xs-10 col-sm-10" data-bxwidget data-bxtype="string" value="<%=loginName%>"/>
				</div>
			</div>
		</div>
	</div>
	<div class="row rowspace">
		<div class="col-sm-12">
			<div class="form-group">
				<label class="col-sm-4 control-label no-padding-right labelfont"
					   for="detail-password"> 新密码 </label>
				<div class="col-sm-8">
					<input type="password" id="detail-password"
						   class="col-xs-10 col-sm-10" data-bxwidget data-bxtype="string" />
					<input type="hidden" id="detail-username" data-bxwidget data-bxtype="string" value="<%=username%>">
					<input type="hidden" id="detail-uid" data-bxwidget data-bxtype="number" value="<%=userid%>">
					<input type="hidden" id="detail-desc" data-bxwidget data-bxtype="string" value="">
				</div>
			</div>
		</div>
	</div>

	<div class="row rowLastSpace">
		<div class="col-sm-12">
			<div class="form-group">
				<label class="col-sm-4 control-label no-padding-right labelfont"
					   for="surePassword"> 确认密码 </label>
				<div class="col-sm-8">
					<input type="password" id="surePassword"
						   class="col-xs-10 col-sm-10" data-bxwidget data-bxtype="string" />
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 信息弹出框 -->
<div id="dialog-message" class="hide">
	<p class="bigger-110 bolder center grey">
		<br/>
		<b id="dialogInfo"></b>
	</p>
</div>

</body>
<!-- 锁屏功能 -->
<style type="text/css">
	.block_overlay {
		display:none;
		position:absolute;
		top:0%;
		left:0%;
		width:100%;
		height:100%;
		background-color:gray;
		z-index:1049;
		filter:alpha(opacity=80);
		-moz-opacity:0.8;
		-khtml-opacity: 0.8;
		opacity: 0.8;
	}

	.div_content {
		display:none;
		position:absolute;
		top:25%;
		left:37.5%;
		width:25%;
		height:0%;
		z-index:1050;
		overflow:auto;
		background-color:white;
	}

</style>
<div id="content" class="div_content"></div>
<div id="full" class="block_overlay"></div>
<div id="unlock" class="hide" style="overflow:hidden">
	<div class="row rowLastSpace">
		<div class="col-sm-12">
			<div class="form-group">
				<label class="col-sm-4 control-label no-padding-right labelfont">
					解锁密码 </label>
				<div class="col-sm-8">
					<input type="password" id="unlockPassword"
						   class="col-xs-10 col-sm-10" data-bxwidget data-bxtype="string" />
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" id="valueFirst" value="<%=path%>"/>
<input type="hidden" id="valueSecond" value="<%=tenant%>"/>
<input type="hidden" id="valueThird" value="<%=userName%>"/>
</html>
