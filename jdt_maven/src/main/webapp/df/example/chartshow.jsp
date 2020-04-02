<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<html lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>图表组件展示</title>
<%
	String type = (String) request.getParameter("type");
%>
<script type="text/javascript">
    var type = '<%=type%>';
</script>
<style type="text/css">
    .thumbnail{
        background-color:#fff !important;
    }

    .thumbnail:hover, .thumbnail:focus, .thumbnail.active{
        width: 150%;
        position: absolute;
        z-index: 10;
    }

    .thumbnail .caption{
        padding:0px 10px !important;
        color: #595C66 !important;
        font-family: "microsoft yahei", simhei, Geneva, Arial, Helvetica, sans-serif;
    }

    .thumbnail h3{
        font-family:"microsoft yahei",simhei, Geneva, Arial, Helvetica, sans-serif;
        font-size: 16px;
    }

    .thumbnail p{
        font-size: 10px;
    }

</style>
</head>
<body>
	<%@ include file="/bxui/bxuihead.jsp"%>
	<script type="text/javascript"
		src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
	<script type="text/javascript"
		src="<%=toolkitPath%>/bxui/jseditor/ace.js"></script>
	<script type="text/javascript"
		src="<%=toolkitPath%>/df/example/chartshow.js"></script>
    <link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css" />
	<div class="jqgridFrame contentFrame">
		<div class="page-content contentFrame">
			<!-- /sectionController:settings.box -->
			<div class="page-header" style="height: 50px;">
				<h1 class="pull-left">
					<div id="title"></div>
				</h1>
			</div>
			<div id="showDiv"></div>
		</div>
	</div>
</body>
</html>