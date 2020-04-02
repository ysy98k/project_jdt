<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="com.baosight.aas.auth.Constants"%>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<title>掘进监控</title>
	<%
		String path = request.getContextPath();
		String tenantid=request.getSession().getAttribute(Constants.SESSION_TENANTID_KEY) + "";
	%>

	<meta name="description" content="platForm Frameset" />
	<meta name="viewport"
		  content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
	<style>
		html,body,#father{
			height:100%;
			background-color:white!important;
		}

	</style>
</head>

<body>
<%@ include file="/bxui/bxuihead.jsp"%>
<%--<script type="text/javascript" src="<%=path%>/bxui/baosight-require.js"></script>--%>
<input type="hidden" value="<%=tenantid%>" id="tenantid">
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css" />
<div class="page-content" id="father"  style="position:inherit;min-height:600px">
	<!-- /sectionController:settings.box -->
	<div class="page-header" id="header" >
		<h5>
			监控管理
			<small>
				&nbsp;
				<i class="ace-icon fa fa-angle-double-right"></i>
				&nbsp;&nbsp;掘进监控
			</small>
		</h5>
		<%--<div style="margin-top: -20px;margin-right: 30px"><a href="javascript:showFullScreen()"><span>全屏</span></a></div>--%>
		<div style="float: right;margin-top: -20px">
			<a href="javascript:showFullScreen()" style="margin-right: 20px"><span>全屏</span></a>
			<a href="javascript:insertPage()" ><span id = "cityname"></span><span id="linemessage"></span><span id="programmessage"></span></a>
		</div>
	</div><!-- /.page-header -->
	<iframe id="cvsPage" width="100%" frameborder="no"  src="";scrolling="no">
	</iframe>
	<%--<iframe id="cvsPage" width="100%" frameborder="no" src="";scrolling="no">
    </iframe>--%>


	<div id="detail" class="hide" style="overflow: hidden">
		<div type="text" id = "div_city" name = "city" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
		</div>
		<div type="text" id = "div_line" name = "line" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
		</div>
		<div type="text" id = "div_program" name = "program" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
		</div>
	</div>
</div>

<!-- /.page-content -->

<!-- ----------------------------------信息弹出框---------------------------------------------------------->
<div id="dialog-message" class="hide">
	<p class="bigger-110 bolder center grey">
		<br/>
		<b id="dialogInfo"></b>
	</p>
</div>
</body>

<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-form.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxdialog.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxcombobox.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/loadsave.js"></script>
<script src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/selectLine.js"></script>
<script type="text/javascript">
    function initDomHeight(){
        var fatherHeight = $("#father").height();
        var headHeight = $("#header").outerHeight(true);
        var contentHeight = fatherHeight - headHeight;
        $("#cvsPage").height(contentHeight);

    }
    $(document).ready(function () {
        initDomHeight();
    });
    $(document).keydown(function(event){
        console.log(event.keyCode);
        if(event.keyCode==27)
            exitFullscreen()
    });

    function showFullScreen(){
        var elm = document.getElementById("cvsPage");
        launchFullscreen(elm );
    }
    // 全屏，退出按esc或参考参考参考注释代码写退出全屏按钮
    function launchFullscreen(element) {
        if (element.requestFullscreen) {
            element.requestFullscreen();
        } else if (element.mozRequestFullScreen) {
            element.mozRequestFullScreen();
        } else if (element.msRequestFullscreen) {
            element.msRequestFullscreen();//ie浏览器
        } else if (element.webkitRequestFullscreen) {
            element.webkitRequestFullScreen();//谷歌浏览器
        }
    }

    // 监听全屏事件webkitfullscreenchange是谷歌内核的事件；MSFullscreenChange是ie内核的事件
    document.addEventListener('webkitfullscreenchange', function fullscreenChange() {
        if (document.fullscreenEnabled ||
            document.webkitIsFullScreen ||
            document.mozFullScreen ||
            document.msFullscreenElement) {
            console.log('enter fullscreen');
            //可以在这里做一些全屏时的事
        } else {
            console.log('exit fullscreen');
            //可以在这里做一些退出全屏时的事
        }
    }, false);


    function exitFullscreen() {
        if (document.exitFullscreen) {
            document.exitFullscreen();
        } else if (
            document.msExitFullscreen){
            document.msExitFullscreen();
        } else if (document.mozCancelFullScreen) {
            document.mozCancelFullScreen();
        } else if (document.webkitExitFullscreen) {
            document.webkitExitFullscreen();
        }
    }




    var projectId = $.cookie('selected_id');
    var collectorName = $.cookie('selected_collection');
    if(isNullOrEmptyOrUndefiend(projectId)){
        $("#cvsPage").attr("src", toolkitPath+ "/404.jsp");
    }else{
        var getCvsUrl = {
            onSuccess: function (dataJson) {
                if(dataJson.status == "0") {
                    var templateName = dataJson.templateName;
                    //如果是第一次健在cvs页面,或者是加载不同的模板
                    var  pageName = templateName.substring(templateName.indexOf(".")+1);
                    var url = urlGenerator("template",collectorName,pageName);
                    $("#cvsPage").attr("src",url);
                }else {
                    $("#cvsPage").attr("src", toolkitPath+ "/404.jsp");
                }
            }
        };
        var ajaxParam = {"inqu_status":{"projectId":projectId}};
        AjaxCommunicator.ajaxRequest('/raising/forward/sectionSetting.do?method=getCvsUrl', 'GET', ajaxParam, getCvsUrl,true);
    }

    /**
     * 宝信提供，cvs画面复用的接口
     */
    function urlGenerator(oldTags,newTags,pageName) {
        var encodedPageName = window.btoa(encodeURIComponent(pageName));
        var replaceTagsVal = oldTags + "@@" + newTags + "@@" + pageName;
        var encodedreplaceTagsVal = window.btoa(encodeURIComponent(JSON.stringify(replaceTagsVal)));
        var prefix = window.location.protocol + "//" + window.location.host + ":" + window.location.port+"/cvscfgsvr/bpd?action=loadPage&pagename=" + encodedPageName + "&replaceTags=" + encodedreplaceTagsVal;
        return prefix;
    }

</script>




</html>
