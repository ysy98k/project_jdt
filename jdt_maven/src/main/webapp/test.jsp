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

    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak==SVXG7z0f9r0gEwvMbjG0v2G7x1w4FEzz"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/library/TextIconOverlay/1.2/src/TextIconOverlay_min.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/library/MarkerClusterer/1.2/src/MarkerClusterer_min.js"></script>

    <title>地图展示</title>


</head>

<body style="min-height: 864px;">
<%@ include file="/bxui/bxuihead.jsp" %>
<link href="<%=toolkitPath%>/raisingui/zTree/zTreeStyle.css" rel="stylesheet">
<link rel="stylesheet" href="./mapQuery.css">
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/aceadmin/style/css/font-awesome.min.css">

<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-form.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script  src="<%=toolkitPath%>/raisingui/zTree/jquery.ztree.core.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/loadsave.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raisingui/echarts/echarts.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>

<style type="text/css">

</style>

<div id="father" class="row" style="position:relative;padding: 0px;margin: 0px;height: 100%;min-height: 864px;/*background-image:url('../image/map/u0.png');*/">

    <%--城市列表--%>
    <div class="infoDiv"
         style="height: 50%;z-index:10;
         background: #044161;top:15px;left:15px; ">
        <div class="decorate-border decorate-border-leftTop" style="left:-4px;top:-4px;"></div>
        <div class="decorate-border decorate-border-rightTop" style="left:234px;top:-4px;"></div>
        <div class="decorate-border decorate-border-leftBottom" style="left:-4px;top:calc(100% - 15px);"></div>
        <div class="decorate-border decorate-border-rightBottom" style="left:234px;top:calc(100% - 15px);"></div>

        <div class="col-xs-12 infoTitle" >
            <i class="fa fa-home title_i" aria-hidden="true"></i>地区概览
        </div>
        <div class="col-xs-12" style="background-color: white;height:25px;">
            <i id="searchLineButton" onclick="searchLine()" class="fa fa-search" aria-hidden="true" style="color: #044161;"></i>
            <input  name="lineInput" style="width: 200px;border-width: 0px;height:25px;" />
            <i onClick="clearInput()" class="icon fa fa-remove"
               style="text-decoration:blink;cursor:pointer;height:19px;position:absolute;left:230px;top:7px;color: #044161;"></i>
        </div>
        <div id="lineTree"  class="ztree contentDiv" style="clear: both; height:calc(100% - 60px); overflow-y:auto;overflow-x: hidden;" >

        </div>
    </div>

    <%--盾构厂家占比--%>
    <div class="infoDiv" style="height: 40%;top:55%;z-index:10;background: #044161;left:15px;">
        <div class="decorate-border decorate-border-leftTop" style="left:-4px;top:-4px;"></div>
        <div class="decorate-border decorate-border-rightTop" style="left:234px;top:-4px;"></div>
        <div class="decorate-border decorate-border-leftBottom" style="left:-4px;top:calc(100% - 15px);"></div>
        <div class="decorate-border decorate-border-rightBottom" style="left:234px;top:calc(100% - 15px);"></div>

        <div class="col-xs-12 infoTitle"><i class="fa fa-pie-chart title_i" aria-hidden="true" style="color: #996633"></i>盾构厂家占比</div>
        <div id="tbmFactoryChart" class="contentDiv" style="min-height: 100px;min-width: 100px;"></div>
    </div>

    <div id="allmap" class="col-xs-12" style="height: 100%;min-height: 864px;"></div>

    <%--报警一览--%>
    <div class="infoDiv" style="height: 30%;top:15px;z-index:10;background: #044161;right:10px;">
        <div class="decorate-border decorate-border-leftTop" style="left:-4px;top:-4px;"></div>
        <div class="decorate-border decorate-border-rightTop" style="left:234px;top:-4px;"></div>
        <div class="decorate-border decorate-border-leftBottom" style="left:-4px;top:calc(100% - 15px);"></div>
        <div class="decorate-border decorate-border-rightBottom" style="left:234px;top:calc(100% - 15px);"></div>

        <div class="col-xs-12 infoTitle"><i class="fa fa-exclamation title_i" aria-hidden="true" style="color: red;"></i>报警一览</div>
        <div id="alarmDiv" class="contentDiv">
            <table id="alarmTable" class="mts_table">
                <thead class="mts_tr_head">
                <td style="width:120px;">项目名</td>
                <td style="width:100px;">盾构名</td>
                <td style="width:70px;">报警总数</td>
                </thead>

            </table>
        </div>
    </div>

    <%--在线情况--%>
    <div class="infoDiv" style="height: 30%;top:calc(30% + 30px);z-index:10;background: #044161;right:10px;">
        <div class="decorate-border decorate-border-leftTop" style="left:-4px;top:-4px;"></div>
        <div class="decorate-border decorate-border-rightTop" style="left:234px;top:-4px;"></div>
        <div class="decorate-border decorate-border-leftBottom" style="left:-4px;top:calc(100% - 15px);"></div>
        <div class="decorate-border decorate-border-rightBottom" style="left:234px;top:calc(100% - 15px);"></div>

        <div class="col-xs-12 infoTitle"><i class="fa fa-bluetooth-b title_i" aria-hidden="true" style="color: #00FF66"></i>在线情况</div>
        <div id="onLineDiv" class="contentDiv"  >
            <table id="onLineTable" class="mts_table">
                <thead class="mts_tr_head">
                <td style="width:40px;">排序</td>
                <td style="width:60px;">地区</td>
                <td style="width:70px;">盾构数量</td>
                <td style="width:70px;">接入数量</td>
                <td style="width:50px;">在线率</td>
                </thead>

            </table>
        </div>
    </div>

    <%--掘进进度--%>
    <div class="infoDiv" style="height: 30%;top:calc(60% + 45px);z-index:10;background: #044161;right:10px;">
        <div class="decorate-border decorate-border-leftTop" style="left:-4px;top:-4px;"></div>
        <div class="decorate-border decorate-border-rightTop" style="left:234px;top:-4px;"></div>
        <div class="decorate-border decorate-border-leftBottom" style="left:-4px;top:calc(100% - 15px);"></div>
        <div class="decorate-border decorate-border-rightBottom" style="left:234px;top:calc(100% - 15px);"></div>

        <div class="col-xs-12 infoTitle"><i class="fa fa-area-chart title_i" aria-hidden="true" style="color: #990099"></i>掘进进度</div>
        <div id="progressDiv" class="contentDiv" style="padding: 20px 10px;">
        </div>
    </div>
    <div style="clear:both;"></div>

</div>


</body>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/mapQuery.js" ></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/infoBox.js" ></script>

</html>