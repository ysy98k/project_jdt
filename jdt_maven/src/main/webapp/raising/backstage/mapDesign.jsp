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
        body, html{width: 100%;height: 100%;margin:0;font-family:"微软雅黑";}
        #allmap {width: 100%; height:500px; overflow: hidden;}
        #result {width:100%;font-size:12px;}
        dl,dt,dd,ul,li{
            margin:0;
            padding:0;
            list-style:none;
        }
        p{font-size:12px;}
        dt{
            font-size:14px;
            font-family:"微软雅黑";
            font-weight:bold;
            border-bottom:1px dotted #000;
            padding:5px 0 5px 5px;
            margin:5px 0;
        }
        dd{
            padding:5px 0 0 5px;
        }
        li{
            line-height:28px;
        }
    </style>

    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=SVXG7z0f9r0gEwvMbjG0v2G7x1w4FEzz"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.js"></script>
    <link rel="stylesheet" href="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.css" />
    <!--加载检索信息窗口-->
    <script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.js"></script>
    <link rel="stylesheet" href="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.css" />

    <title>地图查询</title>
</head>
<body>
<%@ include file="/bxui/bxuihead.jsp" %>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jstree/dist/jstree.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxtree.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/loadsave.js"></script>
<script src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css"/>
<div class="contentFrame">
<div class="row">
    <div class="col-sm-7 col-md-4 col-xs-12">
        <div class="col-sm-2 col-md-2" style="height:36px;line-height:36px;text-align: right;" >
            <label  for="suggestId" style="height: 36px">地名</label>
        </div>
        <div class="col-sm-6 col-md-6 ">
            <input type="text" id="suggestId" style="width: 100%;" />

        </div>
        <div class="col-sm-2 col-md-2  no-padding-left-1024">
            <button class="btn btn-sm btn-block" onclick="chaXun();">
                <div class="ace-icon fa fa-search"></div>
                <span id="search">查询</span>
            </button>
        </div>
    </div>
    <div id="searchResultPanel" style="border:1px solid #C0C0C0;width:150px;height:auto; display:none;"></div>
    <div class="col-sm-5 col-md-3 col-xs-12 no-padding">
        <div class="col-sm-1 col-md-8 hidden">
            <input type="text" id="sectionId"/>
        </div>

        <div class="col-sm-4 col-md-2 col-xs-6 no-padding-left-1024">
            <button class="btn btn-sm btn-block" onclick="drawDesignLine();">
                <span id="draw">绘制</span>
            </button>
        </div>
        <div class="col-sm-2 col-md-2 col-xs-6 no-padding-left-1024">
            <button class="btn btn-sm btn-block" onclick="saveLine();" >
                <span id="save">保存</span>
            </button>
        </div>
        <div class="col-sm-2 col-md-2 col-xs-6 no-padding-left-1024">
            <button class="btn btn-sm btn-block" onclick="clearLine();" >
                <span id="clear">清除</span>
            </button>
        </div>

    </div>

</div>

<div id="allmap" style="overflow:hidden;zoom:1;position:relative;">
    <div id="map" style="height:100%;-webkit-transition: all 0.5s ease-in-out;transition: all 0.5s ease-in-out;"></div>
</div>
</div>
</body>
</html>
<script type="text/javascript">
    // 百度地图API功能
    var oldLine = [];
    var newline = [];
    var map = new BMap.Map('map');



    var overlays = [];
    var overlaycomplete = function(e){
        overlays.push(e.overlay);
    };

    var styleOptions = {
        strokeColor:"red",    //边线颜色。
        fillColor:"red",      //填充颜色。当参数为空时，圆形将没有填充效果。
        strokeWeight: 3,       //边线的宽度，以像素为单位。
        strokeOpacity: 0.8,	   //边线透明度，取值范围0 - 1。
        fillOpacity: 0.6,      //填充的透明度，取值范围0 - 1。
        strokeStyle: 'solid' //边线的样式，solid或dashed。
    }

    $(document).ready(function () {
        window.setTimeout("initLine()",2000);

    });

    function initLine() {
        var sectionId = $.cookie("map_section_id");
        /*console.log("initLine");
        var sectionId =null;
        var m = this.sectionId;*/
        if(isNumber(sectionId)){
            $("#sectionId").val(sectionId);
        }else{
            return;
        }
        var paramJsonObj = {};
        paramJsonObj.sectionId = sectionId;
        var callback= {
            onSuccess:function (paramJsonObj) {
                var city = paramJsonObj.city;
                map.centerAndZoom(city, 16);
                map.enableScrollWheelZoom();
                map.enableInertialDragging();
                map.enableContinuousZoom();
                var size = new BMap.Size(10, 20);
                map.addControl(new BMap.CityListControl({
                    anchor: BMAP_ANCHOR_TOP_LEFT,
                    offset: size,
                }));

                var len = paramJsonObj.mapCoordinateCenter.length;
                if(len > 1 ){
                    for(var i = 0;i< len;i++){
                        var x = paramJsonObj.mapCoordinateCenter[i].x;
                        var y = paramJsonObj.mapCoordinateCenter[i].y;
                        var point = new BMap.Point(x,y);
                        oldLine.push(point);
                    }
                }

                var centerLine = new BMap.Polyline(oldLine,{strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});
                map.addOverlay(centerLine);

                setTimeout(function(){
                    map.panTo(oldLine[0]);
                }, 1000);
            }
        };
        AjaxCommunicator.ajaxRequest('/raising/backstage/drawLine.do?method=query', 'POST', paramJsonObj, callback,true)
    }

    function getLine(e) {
        map.clearOverlays();
        var centerLine = new BMap.Polyline(oldLine,{strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});
        map.addOverlay(centerLine);
        var poi  = new BMap.Point(e.point.lng,e.point.lat);
        newline.push(poi);
        var polyline = new BMap.Polyline(newline,{strokeColor:"red", strokeWeight:2, strokeOpacity:0.5});
        map.addOverlay(polyline);
    }

    function drawDesignLine() {
        if(document.getElementById("draw").innerHTML == "绘制"){
            map.addEventListener("click",getLine);
            document.getElementById("draw").innerHTML = "取消绘制";
        }
        else if(document.getElementById("draw").innerHTML == "取消绘制"){
            map.removeEventListener("click",getLine);
            document.getElementById("draw").innerHTML="绘制";
        }
    }

    function revokLine() {
        if (document.getElementById("draw").innerHTML == "取消绘制") {
            map.clearOverlays();
            var centerLine = new BMap.Polyline(oldLine,{strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});
            map.addOverlay(centerLine);
            newline.pop();
            var polyline = new BMap.Polyline(newline, {strokeColor: "red", strokeWeight: 2, strokeOpacity: 0.5});
            map.addOverlay(polyline);
        }
    }

    map.addEventListener("rightclick",revokLine);

    function saveLine() {
        var sectionId = $("#sectionId").val();
        var paramJsonObj = {};
        paramJsonObj.sectionId = sectionId;
        paramJsonObj.data = newline;
        var callback= {
            onSuccess:function (paramJsonObj) {
                if(newline.length != 0){
                    oldLine = newline;
                }
                map.clearOverlays();
                newline = [];
                var centerLine = new BMap.Polyline(oldLine,{strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});
                map.addOverlay(centerLine);

            }
        };
        AjaxCommunicator.ajaxRequest('/raising/backstage/drawLine.do?method=save', 'POST', paramJsonObj, callback,true)
        }

    function clearLine() {
        map.clearOverlays();
        var centerLine = new BMap.Polyline(oldLine,{strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});
        map.addOverlay(centerLine);
        newline=[];
    }



    var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
        {"input" : "suggestId"
            ,"location" : map
        });

    ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
        var str = "";
        var _value = e.fromitem.value;
        var value = "";
        if (e.fromitem.index > -1) {
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

        value = "";
        if (e.toitem.index > -1) {
            _value = e.toitem.value;
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
        G("searchResultPanel").innerHTML = str;
    });

    var myValue;
    ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
        var _value = e.item.value;
        myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;

        setPlace();
    });

    function setPlace(){
        map.clearOverlays();    //清除地图上所有覆盖物
        function myFun(){
            var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
            map.centerAndZoom(pp, 18);
            map.addOverlay(new BMap.Marker(pp));    //添加标注
        }
        var local = new BMap.LocalSearch(map, { //智能搜索
            onSearchComplete: myFun
        });
        local.search(myValue);
    }
    function G(id) {
        return document.getElementById(id);
    }

    function chaXun(){
        var city = document.getElementById("suggestId").value;
        if(city != ""){
            map.centerAndZoom(city,11);      // 用城市名设置地图中心点
        }
    }

</script>
