<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2018-07-02
  Time: 17:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
        body, html,#allmap {width: 100%;height: 100%;overflow: hidden;margin:0;font-family:"微软雅黑";}
    </style>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak==SVXG7z0f9r0gEwvMbjG0v2G7x1w4FEzz"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/library/TextIconOverlay/1.2/src/TextIconOverlay_min.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/library/MarkerClusterer/1.2/src/MarkerClusterer_min.js"></script>
    <title>地图展示</title>
    <style>
        p[class='alert alert-info'],p[class='alert alert-success']{
            padding: 5px;
            margin-bottom: 10px;
        }
        .mts-widget-header{
            text-align: center;
            font-size: 18px;
            background-color: #ffb432 !important;
            color: white;
            height: 35px;
            line-height:35px;
        }
    </style>
</head>

<body>
<%@ include file="/bxui/bxuihead.jsp"%>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-form.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxdialog.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxcombobox.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/loadsave.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>


<div id="allmap"></div>

</body>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/map.js" ></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/infoBox.js" ></script>
</html>