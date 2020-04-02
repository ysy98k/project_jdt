
<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.baosight.aas.auth.Constants" %>
<%@ page import="com.baosight.sys.controller.LoginController" %>
<%@ page import="java.util.Properties" %>
<%@ page import="java.io.InputStream" %>


<html>
<head>
    <title>宝信大数据应用开发平台</title>
    <%
        String path = request.getContextPath();

        //loginName:name@test
        String loginName=(String)request.getSession().getAttribute("loginName");
    %>

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="description" content="platForm Frameset" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
</head>

<body class="black-skin">
<%@ include file="/bxui/bxuihead.jsp"%>
<script type="text/javascript" src="<%=path%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=path%>/bxui/jquery/dist/jquery-migrate-1.1.0.js"></script>
<script type="text/javascript">
    //获取框架配置
    var frame_pagePath;
    var frame_pageType;
    var frame_logoIcon;
    var frame_logoText;
    var frame_skinName;
    var frame_pageName;
    var callback = {
        onSuccess : function(paramJsonObj) {
            if(typeof(paramJsonObj.skinName)!="undefined"){
                document.body.className=paramJsonObj.skinName;
                frame_skinName=paramJsonObj.skinName;
                frame_logoIcon=paramJsonObj.logoIcon;
                frame_logoText=paramJsonObj.logoText;
                frame_pageName=paramJsonObj.pageCode;

                if(paramJsonObj.pagePath!=undefined){
                    frame_pagePath=paramJsonObj.pagePath;
                    frame_pageType=paramJsonObj.pageType;
                }else{
                    frame_pagePath="";
                    frame_pageType="";
                }
            }
        }
    };
    AjaxCommunicator.ajaxRequest(
        '/raising/forward/tbmManage/jumpPage.do',
        'POST', {}, callback);
</script>
<link rel="stylesheet" href="<%=path%>/df/console/css/style-frame.css"/>
<div class="noNavber"></div>

<!-- /backstage:basics/navbar.layout -->
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>

    <!-- #backstage:basics/sidebar -->
    <div id="sidebar" class="sidebar responsive">
        <script type="text/javascript">
            try {
                ace.settings.check('sidebar', 'fixed')
            } catch (e) {
            }
        </script>


        <ul class="nav nav-list sideMenu">

        </ul>

        <!-- #backstage:basics/sidebar.layout.minimize -->
        <div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
            <i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left"
               data-icon2="ace-icon fa fa-angle-double-right"></i>
        </div>
        <%--
        --%>
        <!-- /backstage:basics/sidebar.layout.minimize -->
        <script type="text/javascript">
            //动态生成菜单
            var param = new Object();
            param = {"menuType": "tbmMenu", "fieldName": "menuOrder", "ascDesc": "asc"};
            var callbackMenu = {
                onSuccess: function (paramJsonObj) {
                    var authMenus = paramJsonObj.authMenus;
                    var menuAll = new Array();

                    //菜单定位
                    function getAllMenus(menus, menuAll) {
                        if (menus) {
                            $.each(menus, function (index, item) {
                                if (item.subMenus) {
                                    getAllMenus(item.subMenus, menuAll);
                                } else {
                                    menuAll.push(item);
                                }
                            });
                        }
                    }

                    getAllMenus(authMenus, menuAll);
                    genMenu(authMenus);

                    $(".sideMenu>li:first").addClass("open");
                    $(".sideMenu>li:first ul.submenu:first").show();

                    $("#autoComplete").autocomplete(menuAll, {
                        minChars: 2,                    //最少输入字符数
                        max: 12,
                        autoFill: false,                //是否选多个,用","分开
                        mustMatch: false,               //是否全匹配, 如数据中没有此数据,将无法输入
                        matchContains: true,            //是否全文搜索,否则只是前面作为标准
                        scrollHeight: 220,
                        width: 180,
                        multiple: false,
                        formatItem: function (row, i, max) {                    //显示格式
                            return "<span>" + row.dispName + "</span>";
                        },
                        formatMatch: function (row, i, max) {               //以什么数据作为搜索关键词,可包括中文,
                            return row.dispName;
                        },
                        formatResult: function (row) {                      //返回结果,选中之后input的填充字段
                            return row.dispName;
                        }
                    }).result(function (event, row, formatted) {
                        readyToOpenForm(row.pagePath, row.linkParam);
                        $("#" + row.menuId + "").click();
                        $("#" + row.menuId + "").parents(".submenu").show().parent("li").addClass("open");
                        $("#" + row.menuId + "").parents(".submenu").show().parent("li").siblings("li").removeClass("open").children(".submenu").hide();
                    });
                }
            };
            AjaxCommunicator.ajaxRequest(
                '/df/metamanage/tenantMenu.do?method=queryHomeAndBackgroundMenu',
                'POST', param, callbackMenu);

            //1-2级菜单的递归算法
            function genMenu(menus) {

                for (var i = 0; i < menus.length; i++) {
                    var iconPath = "";
                    if (menus[i].iconType != "noneIcon") {
                        iconPath = menus[i].iconPath;
                    }
                    if (menus[i].parentCode == '0') {//1级菜单
                        var submenuDiv = "";
                        if (menus[i].subMenus != null) {
                            submenuDiv = "<ul class='submenu'></ul>"
                        }
                        var menuDiv = "<li class='nav-list-title'>" +
                            "<a class='dropdown-toggle' ><i class='menu-icon fa " + iconPath + "'></i>" +
                            "<span class='menu-text'>" + menus[i].dispName + "</span><b class='arrow fa fa-angle-down'></b></a>" +
                            "<b class='arrow'></b>" + submenuDiv + "</li>";
                        $("ul.sideMenu").append(menuDiv);

                    } else {
                        var subMenu = "";
                        var subLink = "";
                        if (menus[i].subMenus != null) {
                            subMenu = "<b class='arrow fa fa-angle-down'></b></a><b class='arrow'></b><ul class='submenu'></ul>";
                        } else {
                            if (menus[i].linkType == 'leafNode') {
                                var subLink = "onClick='readyToOpenForm(" + '\"' + menus[i].pagePath + '\"' + ',\"' + menus[i].linkParam + "\")'";
                            }
                        }

                        if (menus[i].menuLevel == 1) {//2级菜单
                            var menuDiv = "<li class='clickThis tabSelColor' id='" + menus[i].menuId + "'><a href='' class='dropdown-toggle' " + subLink +
                                "><i class='menu-icon fa " + iconPath + "'></i>" +
                                "<span class='menu-text'>" + menus[i].dispName + "</span>" + subMenu + "</li>";
                            $("ul.sideMenu>li:last").find("ul.submenu:first").append(menuDiv);
                        } else {//3级菜单
                            var subMenuDiv = "<li class='clickThis subSelColor' id=" + menus[i].menuId + "><a href='' class='dropdown-toggle' " + subLink +
                                "><i class='menu-icon fa " + iconPath + "'></i>" + menus[i].dispName + subMenu + "</li>";
                            if (i == 0) {
                                $("#sidebar-collapse").prev().children(":last").find("ul:last").append(subMenuDiv);
                            } else {
                                var j = (i - 1);
                                $("#" + menus[j].menuId).after(subMenuDiv);
                            }
                        }
                    }
                    if (menus[i].subMenus != null) {
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
            var State = 0;//上菜单是否固定，0固定，1浮动
            $(document).ready(function () {
                if (frame_pagePath != undefined && frame_pagePath != "") {
                    $("ul[style ='display: block;'] ").find("a").eq(0).addClass("tabSel");//选择展开的ul，第一个a标签高亮显示
                    readyToOpenForm("<%=path%>" + frame_pagePath, "embed");
                }else {
                    var last_gritter;
                    if (last_gritter) $.gritter.remove(last_gritter);
                    last_gritter = $.gritter.add({
                        title: '操作失败！',
                        text: '请确认“系统设置”中的“首页设置”是否配置正确！',
                        class_name: 'gritter-error gritter-center'
                    });
                    return;
                }

                $("#logoText").html(frame_logoText);
                $("#logoIcon").attr("src", frame_logoIcon);

                //左菜单是否收缩
                if ($("#sidebar").hasClass("menu-min")) {
                    hasMenuMin();
                } else {
                    $(".main-content").css("padding-top", "0px");
                    $(".upButton").css("display", "none");
                }

                $("a").bind('dragstart', function (evt) {
                    return false;
                })//所有链接拖动时不现实详细url信息
                $("#sidebar").css("min-height", document.documentElement.scrollHeight);
            });

            function readyToOpenForm(node, openLink) {
                if (openLink == "embed")
                    document.getElementById("contentFrame").src = node;
                else if (openLink == "self") {
                    location.href = node;
                } else {
                    window.open(node);
                }

            }

            //左菜单收缩按钮点击效果
            $("#sidebar-collapse").bind().click(function () {
                if (!$("#sidebar").hasClass("menu-min")) {
                    hasMenuMin();
                } else {
                    State = 0;
                    $(".main-content").css("padding-top", "0px");
                    $("#navbar").css("display", "block");
                    $(".upButton").css("display", "none");
                    $('#navbar').animate({
                            top: 0
                        },
                        500);
                    $("#autoComplete").attr("placeholder", "输入菜单名");
                }
            });

            //左侧菜单点击展开收缩菜单子项
            $(".nav-list-title >a").bind().click(function (e) {
                e.stopPropagation();
                var parent = $(this).parent(".nav-list-title");
                if (parent.hasClass("open")) {
                    parent.removeClass("open");
                    parent.find("ul.submenu:first").hide();
                } else {
                    parent.addClass("open");
                    parent.find("ul.submenu:first").show();
                }
            });

            $(".tabSelColor").bind().click(function () {
                if ($(".tabSelColor").children().not(".submenu").hasClass("tabSel")) {
                    $(".tabSelColor").children().not(".submenu").removeClass("tabSel");
                    $(".tabSelColor").removeClass("active");
                }
                $(this).children().not(".submenu").addClass("tabSel");
            });

            $(".subSelColor").bind().click(function (e) {
                if ($(this).children("ul").length == 0) {
                    if ($(".subSelColor").children().hasClass("subtabSel")) {
                        $(".subSelColor").children().removeClass("subtabSel");
                    }
                    $(this).children().addClass("subtabSel");
                }
            });

            function hideNavbar() {
                if (State == 1) {
                    $('#navbar').animate({
                            top: -45
                        },
                        500, function () {
                            $("#navbar").css("display", "none");
                        }
                    );
                }
            }

            $(".noNavber").mouseenter(function () {
                if (State == 1) {
                    $("#navbar").css("display", "block");
                    $('#navbar').animate({
                            top: 0
                        },
                        500);
                }
            });

            function hasMenuMin() {
                //如果左菜单收缩，上菜单也收缩
                $('#navbar').animate({
                        top: -45
                    },
                    500, function () {
                        $("#navbar").css("display", "none");
                    });
                State = 1;
                //调整页面高度
                $(".main-content").css("padding-top", "0px");
                //隐藏菜单名搜索的占位字符
                $("#autoComplete").attr("placeholder", "");

                $(".upButton").css("display", "block");
            }

            $("iframe").height($("#sidebar").height());

        </script>
    </div>

    <!-- /backstage:basics/sidebar -->
    <div class="main-content" style="-webkit-overflow-scrolling:touch;overflow:auto;">
        <!-- #backstage:basics/content.breadcrumbs -->
        <iframe id="contentFrame" class="contentFrame" src="" style="padding-top:0;"></iframe>
    </div>
    <!-- /.main-content -->
    <a href="#" id="btn-scroll-up"
       class="btn-scroll-up btn btn-sm btn-inverse"> <i
            class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
    </a>
</div>
<!-- /.main-container -->

</body>
</html>

