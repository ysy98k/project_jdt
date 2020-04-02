<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body class="devpreview sourcepreview">
<%@ include file="/bxui/bxuihead.jsp" %>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jseditor/ace.js"></script>
<!--[if lt IE 9]>
<script src="<%=toolkitPath%>/df/designer/js/html5shiv.js"></script>
<![endif]-->
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/viewpage.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/htmlutil.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/loadsave.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/controllers/widgets.js"></script>

<script type="text/javascript"
        src="<%=toolkitPath%>/df/designer/controllers/js/bxwidgetcommon.js"></script>
<script type="text/javascript"
        src="<%=toolkitPath%>/df/designer/controllers/js/chart/bxchartscommon.js"></script>
<script type="text/javascript"
        src="<%=toolkitPath%>/df/designer/controllers/js/chart/bar/bxchartsbar.js"></script>
<script type="text/javascript"
        src="<%=toolkitPath%>/df/designer/controllers/js/chart/line/bxchartsline.js"></script>
<script type="text/javascript"
        src="<%=toolkitPath%>/df/designer/controllers/js/chart/pie/bxchartspie.js"></script>

<script type="text/javascript"
        src="<%=toolkitPath%>/df/designer/controllers/js/chart/gauge/bxchartsgauge.js"></script>
<script type="text/javascript"
        src="<%=toolkitPath%>/df/designer/controllers/js/chart/map/bxchartsmap.js"></script>
<script type="text/javascript"
        src="<%=toolkitPath%>/df/designer/controllers/js/chart/radar/bxchartsradar.js"></script>
<script type="text/javascript"
        src="<%=toolkitPath%>/df/designer/controllers/js/chart/scatter/bxchartsscatter.js"></script>

<link rel="stylesheet" href="<%=toolkitPath%>/df/designer/css/bootstrap-combined.min.css">
<link rel="stylesheet" href="<%=toolkitPath%>/df/designer/css/viewpage.css">
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/dataSourceEditor.js"></script>
<style id="demoCss" type="text/css"></style>
<div class="demo runState" style="min-height: 100%;"></div>
<!-- ----------------------------------信息弹出框---------------------------------------------------------->
<div id="dialog-delect-message" class="hide"></div>
</body>
</html>
