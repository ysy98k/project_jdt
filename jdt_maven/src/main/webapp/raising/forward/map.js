/***
 * 地图查询js。
 */
var data = null;
var provinceArr = null;
var map = new BMap.Map("allmap");

/**
 * 自定义地图覆盖物
 */
//1.定义自定义覆盖物构造函数
function customOverlay( dataObj){
    this._dataObj = dataObj;
}

// 2.继承API的BMap.Overlay
customOverlay.prototype = new BMap.Overlay();
//3.初始化自定义覆盖物
customOverlay.prototype.initialize = function(map){
    this._map = map;
    var div = this._div = document.createElement("div");
    $(div).css({
        "position":"fixed",
        //"zIndex":BMap.Overlay.getZIndex(this._point.lat),
        "height":"235px",
        "width":"200px",
        "right":"300px",
        "bottom":"250px",
        "fontSize":"12px"
    });
    $(div).append("<div class=\"widget-box\">\n" +
        "        <div class=\"mts-widget-header\">\n" +
        "            盾构机统计信息\n" +
        "        </div>\n" +
        "        <div class=\"widget-body\">\n" +
        "            <div class=\"widget-main\">\n" +
        "                <p class=\"alert alert-info\">\n" +
        "                    <span>接入总数&nbsp;&nbsp;<span id=\"totalNum\"></span>&nbsp;&nbsp;台</span>\n" +
        "                </p>\n" +
        "                <p class=\"alert alert-success\">\n" +
        "                    <span>土压数量&nbsp;&nbsp;<span id=\"shieldNum\"></span>&nbsp;&nbsp;台</span>\n" +
        "                </p>\n" +
        "                <p class=\"alert alert-info\">\n" +
        "                    <span>泥水数量&nbsp;&nbsp;<span id=\"slurryNum\"></span>&nbsp;&nbsp;台</span>\n" +
        "                </p>\n" +
        "                <p class=\"alert alert-success\">\n" +
        "                    <span>TBM数量&nbsp;&nbsp;<span id=\"tbmNum\"></span>&nbsp;&nbsp;台</span>\n" +
        "                </p>\n" +
        "                <p id='pushBench' class=\"alert alert-info\">\n" +
        "                    <span>顶管数量&nbsp;&nbsp;<span id=\"pushBenchNum\"></span>&nbsp;&nbsp;台</span>\n" +
        "                </p>\n" +
        "            </div>\n" +
        "        </div>\n" +
        "    </div>");
    // 将div添加到覆盖物容器中
    map.getPanes().labelPane.appendChild(div);
    // 需要将div元素作为方法的返回值，当调用该覆盖物的show、
    // hide方法，或者对覆盖物进行移除时，API都将操作此元素
    return div;

}

//3、绘制覆盖物
// 实现绘制方法
customOverlay.prototype.draw = function(){
    // 根据地理坐标转换为像素坐标，并设置给容器
    var map = this._map;
    //var pixel = map.pointToOverlayPixel(this._point);
    var width = document.body.clientWidth;
    var height = document.body.clientHeight;
    this._div.style.left = width - 300 + "px";
    this._div.style.top  = height - 300 + "px";
    var dataObj = this._dataObj;
    $("#totalNum").html(dataObj.totalNum);
    $("#shieldNum").html(dataObj.shieldNum);
    $("#slurryNum").html(dataObj.slurryNum);
    $("#tbmNum").html(dataObj.tbmNum);
    if(dataObj.pushBenchNum > 0){
        this._div.style.height = "255px";
        this._div.style.width = "250px";
        $("#pushBench").show();
        $("#pushBenchNum").html(dataObj.pushBenchNum);
    }else{
        $("#pushBench").hide();
        this._div.style.height = "235px";
        this._div.style.width = "200px";
    }
}
//显示方法
customOverlay.prototype.show = function () {
    if(this._div){
        this._div.style.display = "";
    }
}
//隐藏方法
customOverlay.prototype.hide = function(){
    if(this._div){
        this._div.style.display = "none";
    }
}


// 创建Map实例
$(document).ready(function () {
    // 百度地图API功能
    var paramJsonObj = {};
    var callback= {
        onSuccess: function (JsonObj) {
            paramJsonObj = JsonObj.returnInfo;
            data = paramJsonObj;
            provinceArr = [];
            for (var i = 0; i < paramJsonObj.length; i++) {
                var temp = paramJsonObj[i];//携带了 盾构机，项目，区间信息
                if (temp.tunnelLine.length > 1) {
                    var province = temp.province;
                    var city = temp.city;
                    fillProvinceAndCity(provinceArr, province, city, temp);
                }
            }


            map.addEventListener("zoomend", function(type,target){//百度地图缩放监听事件
                var zoomNumber = map.getZoom();//缩放级别
                if(zoomNumber > 13 && status != "detailed"){//
                    status = "detailed";
                    draw(data,map);
                }else if(10< zoomNumber && zoomNumber <=13 && status != "city"){
                    status = "city";
                    drawCity(data,map);
                }else if(zoomNumber <=10 && status != "province"){
                    status = "province";
                    drawProvince(data,provinceArr,map);
                }
            });
            //js触发map的zoomend事件
            var ev = document.createEvent("HTMLEvents");
            ev.initEvent("zoomend", null, null);
            map.dispatchEvent(ev);
            //显示信息总览
            showTotalInfo(JsonObj);

        }
    }
    AjaxCommunicator.ajaxRequest('/raising/forward/map/getLines.do', 'POST', paramJsonObj, callback,true);
    initMap(map);
})

function initMap(map){
    var status = "detailed";
    map.setMinZoom(5);//设置最小显示级别
    map.enableScrollWheelZoom();//允许滚动滑轮实现地图缩放。
    map.enableContinuousZoom();//启用连续缩放效果
    var size = new BMap.Size(10, 20);
    map.addControl(new BMap.CityListControl({
        anchor: BMAP_ANCHOR_TOP_LEFT,
        offset: size
    }));

    map.centerAndZoom("西安市", 5);
}



function draw(paramJsonObj,map){
    var overlaysArr = map.getOverlays();
    for(var i = 0;i<overlaysArr.length;i++){
        var temp = overlaysArr[i];
        var identification = temp._div;
        if(identification != undefined && identification != null){
            continue;
        }
        map.removeOverlay(temp);
    }

    provinceArr = [];
    cityArr = [];
    for (var i = 0; i < paramJsonObj.length; i++) {
        var temp = paramJsonObj[i];//携带了 盾构机，项目，区间信息
        if (temp.tunnelLine.length > 1) {
            var province = temp.province;
            var city = temp.city;
            fillProvinceAndCity(provinceArr,province,city,temp);

            var tunnelFinishPoint = [];
            var tunnelFinishLine = temp.tunnelLine[0]._PtArray;
            for (var j = 0; j < tunnelFinishLine.length; j++) {
                var x = tunnelFinishLine[j].x;
                var y = tunnelFinishLine[j].y;
                var point = new BMap.Point(x, y);
                tunnelFinishPoint.push(point);
            }
            var completedLine = new BMap.Polyline(tunnelFinishPoint, {//绘制已经掘进完成的线段
                strokeColor: "#32c20d",
                strokeWeight: 3,
                strokeOpacity: 0.5
            });
            map.addOverlay(completedLine);

            var tunnelPoint = [];
            var tunnelLine = temp.tunnelLine[1]._PtArray;
            for (var m = 0; m < tunnelLine.length; m++) {
                var x = tunnelLine[m].x;
                var y = tunnelLine[m].y;
                var point = new BMap.Point(x, y);
                tunnelPoint.push(point);
            }
            var completingLine = new BMap.Polyline(tunnelPoint, {//绘制未掘进线段
                strokeColor: "#2f2973",
                strokeWeight: 3,
                strokeOpacity: 0.5,
                strokeStyle: "dashed"
            })
            map.addOverlay(completingLine);

            var firstPoint = new BMap.Point(tunnelFinishLine[0].x, tunnelFinishLine[0].y);//起始点
            var lastPoint = new BMap.Point(tunnelLine[tunnelLine.length - 1].x, tunnelLine[tunnelLine.length - 1].y);//结束点
            var tbmPiont = new BMap.Point(tunnelLine[0].x, tunnelLine[0].y);
            var tbmIcon = new BMap.Icon(toolkitPath+"/raising/image/tbm.gif", new BMap.Size(16, 16));
            var stationIcon = new BMap.Icon(toolkitPath+"/raising/image/station.png", new BMap.Size(24, 24));

            var startMarker = new BMap.Marker(firstPoint, {icon: stationIcon});
            var endMarker = new BMap.Marker(lastPoint, {icon: stationIcon});
            var tbmMarker = new BMap.Marker(tbmPiont, {icon: tbmIcon});

            var firStCircle = new BMap.Circle(firstPoint,30,{strokeColor:"blue", strokeWeight:0.1, strokeOpacity:0.01,fillOpacity:0.01}); //创建圆
            (function(temp){
                firStCircle.addEventListener("click", function(e){
                    var content = getConent(temp);
                    var infoWindow = new BMap.InfoWindow(content);  // 创建信息窗口对象
                    map.openInfoWindow(infoWindow,e.point); //开启信息窗口
                });
            })(temp);


            var lastCircle = new BMap.Circle(lastPoint,30,{strokeColor:"blue", strokeWeight:0.1, strokeOpacity:0.01,fillOpacity:0.01}); //创建圆
            (function(temp){
                lastCircle.addEventListener("click", function(e){
                    var content = getConent(temp);
                    var infoWindow = new BMap.InfoWindow(content);  // 创建信息窗口对象
                    map.openInfoWindow(infoWindow,e.point); //开启信息窗口
                });
            })(temp);
            map.addOverlay(tbmMarker);
            map.addOverlay(startMarker);
            map.addOverlay(endMarker);
            map.addOverlay(firStCircle);
            map.addOverlay(lastCircle);

            getShowContent(temp,map,tbmMarker);
            getShowContent(temp,map,startMarker);
            getShowContent(temp,map,endMarker);
        }
    }

}

/**
 * 画城市显示图
 * @param data
 * @param cityArr
 * @param map
 */
function drawCity(data,map){
    var overlaysArr = map.getOverlays();
    for(var i = 0;i<overlaysArr.length;i++){
        var temp = overlaysArr[i];
        var identification = temp._div;
        if(identification != undefined && identification != null){
            continue;
        }
        map.removeOverlay(temp);
    }

    //绘制覆盖物
    for(var i=0;i<data.length;i++){
        var temp = data[i];//携带了 盾构机，项目，区间信息
        if (temp.tunnelLine.length > 1) {
            var tunnelLine = temp.tunnelLine[1]._PtArray;
            var tbmPiont = new BMap.Point(tunnelLine[0].x, tunnelLine[0].y);
            var tbmIcon = new BMap.Icon(toolkitPath+"/raising/image/tbm.gif", new BMap.Size(30, 30));

            var tbmCircle = new BMap.Circle(tbmPiont,40,{icon: tbmIcon,strokeColor:"blue", strokeWeight:0.1, strokeOpacity:0.1}); //创建圆
            tbmCircle.addEventListener("click", function(e){
                getShowContent(temp,map,tbmMarker);
            });

            var tbmMarker = new BMap.Marker(tbmPiont, {icon: tbmIcon});
            map.addOverlay(tbmMarker);
            map.addOverlay(tbmCircle);
            getShowContent(temp,map,tbmMarker);
        }

    }

}

/**
 * 画省份的显示图
 * @param data
 * @param provinceArr
 * @param map
 */
function drawProvince(data,provinceArr,map) {
    var overlaysArr = map.getOverlays();
    for(var i = 0;i<overlaysArr.length;i++){
        var temp = overlaysArr[i];
        var identification = temp._div;
        if(identification != undefined && identification != null){
            continue;
        }
        map.removeOverlay(temp);
    }
    var myGeo = new BMap.Geocoder();
    var objArr = [];
    //绘制覆盖物
    for (var i = 0; i < provinceArr.length; i++) {
        //获得百度经纬度地址。因为获得需要时间。避免因为延时,js变量保存for循环最后一个值。将其放在Arr里
        if (provinceArr[i].province != undefined && provinceArr[i].province != null) {
            var provinceObj = provinceArr[i];
            objArr.push(provinceObj);
            // 中文地址解析出百度经纬度地址
            var province = provinceObj.province;
            var city = provinceObj.city;
            (function(a){
                var province = a.province;
                var city = a.city;
                var position = province + city;
                myGeo.getPoint(position, function(point){
                    if (point) {
                        var tbmMarker = new BMap.Marker(point);
                        var label = new BMap.Label(a.number+"台",{offset:new BMap.Size(25,0)});
                        map.addOverlay(tbmMarker);
                        tbmMarker.setLabel(label);
                        tbmMarker.addEventListener("click",function(e){
                            map.centerAndZoom(city,11);
                        });
                    }
                },city);
            })(provinceObj)
        }
    }

}

function showTotalInfo(data){
    var myCompOverlay = new customOverlay(data);
    map.addOverlay(myCompOverlay);

}

function fillProvinceAndCity(provinceArr,province,city,temp){
    if(provinceArr.indexOf(province) > -1){//如果包含
        for(var i =0;i<provinceArr.length;i++){
            var a = provinceArr[i];
            if(province == a.province){
                a.number = a.number +1;
            }
        }
    }else{//如果不包含
        var tunnelLine = temp.tunnelLine[1]._PtArray;
        //存放该省份第一个城市的名称。用作定位
        var provinceTemp = {"province":province,"city":city,"number":1,"x":tunnelLine[0].x,"y":tunnelLine[0].y};
        provinceArr.push(provinceTemp);
        provinceArr.push(province);
    }
}

function getShowContent(obj,map,marker){
    var content = getConent(obj);
    if(marker !=null) {
        marker.addEventListener("click", function (e) {
            var p = e.target;
            var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat)
            var infoWindow = new BMap.InfoWindow(content);  // 创建信息窗口对象
            map.openInfoWindow(infoWindow, point); //开启信息窗口
        });
    }
}

function getConent(obj) {
    var projectName = obj.projectInfo.projectName;
    var tbmFactory = obj.projectInfo.factory;
    var status = "";
    if("prostatus.nostart" == obj.projectInfo.status){
        status = "未开始";
    }else if("prostatus.building" == obj.projectInfo.status){
        status = "在建";
    }else {
        status = "竣工";
    }
    var projectRate = obj.projectInfo.projectRate + "%";
    var totalLength = obj.projectInfo.totalLength;
    var content = "<h4 align='center' style='padding:0.2em 0;color: dodgerblue;'><a href='javascript:jumpPage(\""+obj.projectInfo.projectId+"\")'>" + projectName + "</a></h4>"
        +"<p style=';margin-bottom:0px;'><span style='padding-left: 50px;color: rgb(77, 77, 77);font-family:SimHei;font-size:16px'>项目状态：</span><span style='padding-left: 3px;color: rgb(137, 157, 192);font-weight: bold'>"+status+"</span></p>"
        +"<p style=';margin-bottom:0px;'><span style='padding-left: 50px;color: rgb(77, 77, 77);font-family:SimHei;font-size:16px'>项目进度：</span><span style='padding-left: 3px;color: rgb(137, 157, 192);font-weight: bold'>"+projectRate+"</span></p>"
        +"<p style=';margin-bottom:0px;'><span style='padding-left: 50px;color: rgb(77, 77, 77);font-family:SimHei;font-size:16px'>项目总长：</span><span style='padding-left: 3px;color: rgb(137, 157, 192);font-weight: bold'>"+totalLength+"</span></p>"
        +"<p style=';margin-bottom:0px;'><span style='padding-left: 50px;color: rgb(77, 77, 77);font-family:SimHei;font-size:16px'>盾构厂家：</span><span style='padding-left: 3px;color: rgb(137, 157, 192);font-weight: bold'>"+tbmFactory+"</span></p>"
    ;
    return content;
}

function jumpPage(projectIdStr){
    for(var i = 0;i<data.length;i++){
        var projectInfo = data[i].projectInfo;
        if(projectInfo.projectId == parseInt(projectIdStr)){

            var projectName = projectInfo.projectName;
            var projectId = projectInfo.projectId;
            var collectorName = projectInfo.collectorName;
            var totalLength = projectInfo.totalLength;
            var tbmName = projectInfo.tbmName;
            var ringTotal = projectInfo.ringTotal;
            parent.document.getElementById("contentFrame").src = toolkitPath+"/raising/frame.do";
            /*$("[data-menucode='mapInfo']", parent.document).attr({class:"hover tabSelColor"});
            $("[data-menucode='mapInfo'] a", parent.document).attr({class:"dropdown-toggle"});
            $("[data-menucode='chartShow']", parent.document).attr({class:"hover tabSelColor active"});
            $("[data-menucode='chartShow'] a", parent.document).attr({class:"dropdown-toggle tabSel"});*/
            $.cookie('selected_name',projectName, {path: "/"});
            $.cookie('selected_id',projectId, {path: "/"});
            $.cookie('selected_collection',collectorName,{path:"/"});
            $.cookie('totalMileage',projectInfo.endMileage,{path:"/"});
            $.cookie('totalLength',totalLength,{path:"/"});
            $.cookie('tbmName',tbmName,{path:"/"});
            $.cookie('ringTotal',ringTotal,{path:"/"});
        }
    }

}








