/**
 * 地图查询js。
 */
var data = null;
var provinceArr = null;
//var map = new BMap.Map("allmap",{mapType: BMAP_HYBRID_MAP});
var map = new BMap.Map("allmap");
//zTree setting
var setting = {

    data: {
        simpleData: {
            enable: true,
            idKey: "fdItemId",
            pIdKey: "fdParentId"
        },
        key:{
            name:"fdItemName"
        }
    },
    view: {
        dblClickExpand: false,//双击节点时，是否自动展开父节点的标识
        showLine: false,//是否显示节点之间的连线
        showIcon: true,
        fontCss: { 'color': 'white', 'font-weight': 'normal' },//字体样式函数
        selectedMulti: false, //设置是否允许同时选中多个节点
        fontCss:{
            "height":"24px",
            "line-height":"24px",
            "color":"white"
        }
    },
    callback: {
        beforeClick: function (treeId, treeNode) {
            //code
        },
        onClick: function (event, treeId, treeNode) {
            zTree = $.fn.zTree.getZTreeObj("lineTree");
            if ($(event.target).hasClass('ico_close') || $(event.target).hasClass('ico_open')) {
                zTree.expandNode(treeNode);//如果是父节点，则展开该节点
            } else {
                var levelNum = treeNode.fdLevelNum;
                var itemName = treeNode.fdItemName;
                if(1 == levelNum ){
                    map.centerAndZoom(itemName, 8);
                    var data1 = data;
                    var provinceArr1 = provinceArr;
                    if(data1 == null || provinceArr1 == null ){
                        return;
                    }
                    drawProvince(data1,provinceArr1,map);
                }else if(2 == levelNum){
                    var data1 = data;
                    if(data1 == null ){
                        return;
                    }
                    map.centerAndZoom(itemName, 12);
                    drawCity(data1,map);
                }else if(3 == levelNum){
                    var data1 = data;
                    if(data1 == null ){
                        return;
                    }
                    var levelCode = treeNode.fdLevelCode;//线路ccsId。

                    for(var i =0;i<data1.length;i++){
                        if(levelCode != data1[i].ccsId){//如果不是同一个线路，直接跳过
                            continue;
                        }
                        //得到线路信息
                        var selectedLine = data1[i];
                        drawLine(selectedLine,map);
                        break;
                    }
                }
            }
        },
        onDblClick: function (event,treeId, treeNode) {
            //如果双击项目，则跳转
            var levelNum = treeNode.fdLevelNum;
            var itemName = treeNode.fdItemName;
            if(3 == levelNum){
                var data1 = data;
                if(data1 == null ){
                    return;
                }
                var levelCode = treeNode.fdLevelCode;//线路ccsId。

                for(var i =0;i<data1.length;i++){
                    if(levelCode != data1[i].ccsId){//如果不是同一个线路，直接跳过
                        continue;
                    }
                    //得到线路信息
                    var selectProject = data1[i].projectInfo;
                    parent.document.getElementById("contentFrame").src = toolkitPath+"/raising/frame.do";
                    $("[data-menucode='mapInfo']", parent.document).attr({class:"hover tabSelColor"});
                    $("[data-menucode='mapInfo'] a", parent.document).attr({class:"dropdown-toggle"});

                    $.cookie('selected_name',selectProject.projectName, {path: "/"});
                    $.cookie('selected_id',selectProject.projectId, {path: "/"});
                    $.cookie('selected_collection',selectProject.collectorName,{path:"/"});
                    $.cookie('totalMileage',selectProject.endMileage,{path:"/"});
                    $.cookie('totalLength',selectProject.totalLength,{path:"/"});
                    $.cookie('tbmName',selectProject.tbmName,{path:"/"});
                    $.cookie('ringTotal',selectProject.ringTotal,{path:"/"});
                }
            }
        }
    }
};

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
    $.ajax({
        "url":toolkitPath + '/raising/forward/map/getLines.do',
        "type":"post",
        "data":{"ajaxParam":JSON.stringify(paramJsonObj)},
        "dataType":"json",
        "success":function (JsonObj) {
            paramJsonObj = JsonObj.returnInfo;
            data = paramJsonObj;
            for (var i = 0; i < paramJsonObj.length; i++) {
                var temp = paramJsonObj[i];//携带了 盾构机，项目，区间信息
                if (temp.tunnelLine.length > 1) {
                    var province = temp.province;
                    var city = temp.city;
                    fillProvinceAndCity( province, city, temp);
                }
            }
            drawProvince(data,map);
        }
    })

    initMap(map);


    initStatisticalInfo();

    getOnLineTest();
    getAlarmTest();
    progressTest();
})

function getAlarmTest(){
    var str = "";
    var data = [
        {"projectName":"杭临左线","tbmName":"小松1#","alarmNum":10},
        {"projectName":"孙东区间右线","tbmName":"小松2#","alarmNum":8},
        {"projectName":"正定左线","tbmName":"广州中船53#","alarmNum":6},
        {"projectName":"仙湖站到莲塘站右线","tbmName":"铁建重工2#","alarmNum":4},
        {"projectName":"铜湘右线","tbmName":"中交天和天神吧太奇","alarmNum":2}

    ];
    for(var i=0;i<data.length;i++){
        str += "<tr>" ;
        str += data[i].projectName.length > 8 ?
                "<td title='"+data[i].projectName+"'>"+data[i].projectName.substring(0,6)+"..</td>" :
                "<td>"+data[i].projectName+"</td>";
        str += data[i].tbmName.length > 7 ?
            "<td title='"+data[i].tbmName+"'>"+data[i].tbmName.substring(0,5)+"..</td>" :
            "<td>"+data[i].tbmName+"</td>" ;

        str += "<td>"+data[i].alarmNum+"</td>" +
            "</tr>";
    }
    $("#alarmTable").append(str);
}

function getOnLineTest(){
    var str = "";
    var color = ["#CC0000","#FF3300","#FF9900","#FF00FF","#3333FF","#0099FF","#006666","#660000"];
    var data = [
        {"cnName":"北京","tbmNum":10,"connectNum":8,"rate":"80%"},
        {"cnName":"上海","tbmNum":10,"connectNum":7,"rate":"70%"},
        {"cnName":"广州","tbmNum":10,"connectNum":6,"rate":"60%"},
        {"cnName":"东京","tbmNum":8,"connectNum":4,"rate":"40%"},
        {"cnName":"吉隆坡","tbmNum":6,"connectNum":3,"rate":"50%"}

    ];
    for(var i=0;i<data.length;i++){
        var colorTemp = color[i];
        str += "<tr>" +
            "<td style='padding-top: 5px;'>"+"<span style='display:inline-block;color:white;height: 20px;width:21px;font:bold Open Sans;background-color: "+colorTemp+"'>"+(i+1)+"</span>"+"</td>" +
            "<td>"+data[i].cnName+"</td>" +
            "<td>"+data[i].tbmNum+"</td>" +
            "<td>"+data[i].connectNum+"</td>" +
            "<td>"+data[i].rate+"</td>" +
            "</tr>";
    }
    $("#onLineTable").append(str);
}

function progressTest(){
    var str = "";
    var data = [
        {"projectName":"杭临左线","progress":"100%"},
        {"projectName":"一二三四五六七八九十","progress":"80%"},
        {"projectName":"杭临右线","progress":"60%"},
        {"projectName":"杭临左线","progress":"40%"},
        {"projectName":"杭临左线","progress":"20%"}


    ];
    for(var i=0;i<data.length;i++){
        str += "<div>" ;
        str +=  data[i].projectName.length > 7 ?
            "<div class='progressName' title='"+data[i].projectName+"'>" :
            "<div class='progressName'>";
        str += data[i].projectName.length > 7 ? data[i].projectName.substring(0,5)+".." : data[i].projectName ;

            str += "</div>";
            str += "<div class='progress' >";
            str += "<span class='blue'  style='width: "+data[i].progress+";'><span>"+data[i].progress+"</span></span>";
            str += "</div>";
        str += "</div>";

    }
    $("#progressDiv").append(str);
}

//初始化地图
function initMap(map){
    map.setMinZoom(5);//设置最小显示级别
    map.centerAndZoom("西安市", 6);
    //map.enableScrollWheelZoom();//启用地图滚轮放大缩小
    var styleJson = getMapStyleJson();
    //取消兴趣点显示（餐饮店名，娱乐店名，乡镇名）
    map.setMapStyle({
        styleJson:[{
            "featureType": "village",
            "elementType": "labels",
            "stylers": {
                "visibility": "off"
            }
        }, {
            "featureType": "town",
            "elementType": "labels",
            "stylers": {
                "visibility": "off"
            }
        }, {
            "featureType": "district",
            "elementType": "labels",
            "stylers": {
                "visibility": "on"
            }
        }, {
            "featureType": "entertainmentlabel",
            "elementType": "labels",
            "stylers": {
                "visibility": "off"
            }
        }, {
            "featureType": "hotellabel",
            "elementType": "labels",
            "stylers": {
                "visibility": "off"
            }
        }, {
            "featureType": "restaurantlabel",
            "elementType": "labels",
            "stylers": {
                "visibility": "off"
            }
        }, {
            "featureType": "shoppinglabel",
            "elementType": "labels",
            "stylers": {
                "visibility": "off"
            }
        }, {
            "featureType": "shoppinglabel",
            "elementType": "labels.icon",
            "stylers": {
                "visibility": "off"
            }
        }, {
            "featureType": "hotellabel",
            "elementType": "labels.icon",
            "stylers": {
                "visibility": "off"
            }
        }, {
            "featureType": "restaurantlabel",
            "elementType": "labels.icon",
            "stylers": {
                "visibility": "off"
            }
        }, {
            "featureType": "entertainmentlabel",
            "elementType": "labels.icon",
            "stylers": {
                "visibility": "off"
            }
        }, {
            "featureType": "medicallabel",
            "elementType": "labels.icon",
            "stylers": {
                "visibility": "on"
            }
        }, {
            "featureType": "estatelabel",
            "elementType": "labels.icon",
            "stylers": {
                "visibility": "off"
            }
        }, {
            "featureType": "estatelabel",
            "elementType": "labels",
            "stylers": {
                "visibility": "off"
            }
        }, {
            "featureType": "poilabel",
            "elementType": "labels",
            "stylers": {
                "visibility": "off"
            }
        }, {
            "featureType": "poilabel",
            "elementType": "labels.icon",
            "stylers": {
                "visibility": "off"
            }
        }]
    });
}

//初始化五个绝对定位DIV
function initStatisticalInfo(){
    //地区概览DIV
    $.ajax({
        "url":toolkitPath + '/raising/forward/projectManage/getLineTreeJson.do',
        "type":"get",
        "dataType":"json",
        "success":function (data) {
            if(data.status == "0" ){
               zTreeObj = $.fn.zTree.init($("#lineTree"), setting, data.data);
            }
        }
    })
    //盾构厂家占比DIV
    $.ajax({
        "url":toolkitPath + '/raising/forward/map/getDivInfo.do',
        "type":"get",
        "data":{"ajaxParam":JSON.stringify(new Object())},
        "dataType":"json",
        "success":function (data) {
            if(data.status == "0" ){
                /*drawPie(data.factoryList);
                onLine(data.onLineList);
                progress(data.progressList);*/

            }
        }
    })
}

//画线路视角图
function drawLine(lineObj,map){
    var pMap = map;
    var temp = lineObj;
    if( isNullOrEmptyOrUndefiend(temp.tunnelLine )){//如果此项目没有绘制线路，则跳转至城市，并提示
        pMap.centerAndZoom(lineObj.city,11);
        alertDiv("提示","选中项目并没有绘制线路");
        return;
    }

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
    pMap.addOverlay(completedLine);

    var tunnelPoint = [];
    var tunnelLine = temp.tunnelLine[1]._PtArray;
    for (var m = 0; m < tunnelLine.length; m++) {
        var x = tunnelLine[m].x;
        var y = tunnelLine[m].y;
        var point = new BMap.Point(x, y);
        tunnelPoint.push(point);
    }
    var completingLine = new BMap.Polyline(tunnelPoint, {//绘制未掘进线段
        strokeColor: "#31FCFF",
        strokeWeight: 3,
        strokeOpacity: 0.5,
        strokeStyle: "dashed"
    })
    pMap.addOverlay(completingLine);

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

    pMap.centerAndZoom(tbmPiont, 16);
}

//画城市地图
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

//画省份地图
function drawProvince(data,map) {
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
    var provinceArr = this.provinceArr;
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
//画盾构机厂家饼图
function drawPie(pieArr){
    var pieOption = {
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        // legend: {
        //     type: 'scroll',
        //     orient: 'vertical',
        //     right: 10,
        //     top: 20,
        //     bottom: 20,
        //     data: data.legendData,
        //     selected: data.selected
        // },
        series : [
            {
                name: '厂家信息',
                type: 'pie',
                radius : '55%',
                center: ['40%', '50%'],
                data: pieArr,
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };

    var pieChart = echarts.init(document.getElementById("tbmFactoryChart"));
    pieChart.setOption(pieOption);
}

//在线情况
function onLine(onLineArr){
    var color = ["#CC0000","#FF3300","#FF9900","#FF00FF","#3333FF","#0099FF","#006666","#660000"];
    var str = "";
    for(var i=0;i<onLineArr.length;i++){
        var dataTemp = onLineArr[i];
        var colorTemp = color[i];
        str += "<tr>" +
            "<td>"+(i+1)+"</td>" +
            "<td>"+dataTemp[i].cnName+"</td>" +
            "<td>"+dataTemp[i].tbmNum+"</td>" +
            "<td>"+dataTemp[i].connectNum+"</td>" +
            "<td>"+dataTemp[i].rate+"</td>" +
            "</tr>";
    }
    $("#onLineTable").append(str);

}

//掘进进度
function progress(progressArr){
    var str = "";
    for(var i=0;i<progressArr.length;i++){
        var temp = progressArr[i];
        str +=
                "<div>" +temp[i].name+"</div>" +
                "<div class='progress progress-striped pos-rel'>" +
                    "<div class='progress-bar progress-bar-success' style='width: 25%;' >"+"</div>"
                "</div>" +
                "<div>" +temp[i].rate+"</div>" ;

    }
    $("#progressDiv").append(str);

}

//根据输入框内容，查询lineTree，并展开匹配线路。如果没有匹配线路，则折叠树
function  searchLine(){
    var lineStr = $("input[name='lineInput']").val();
    if(isNullOrEmptyOrUndefiend(lineStr)){
        return;
    }
    //折叠所有节点
    var treeObj = $.fn.zTree.getZTreeObj("lineTree");
    treeObj.expandAll(false);
    //匹配节点
    var nodes = treeObj.getNodes();
    var nodeArr = treeObj.getNodesByParamFuzzy("fdItemName", lineStr, null);
    var firstNode = nodeArr.length < 1 ? null : nodeArr[0];
    if(firstNode != null){
        treeObj.selectNode(firstNode);
        $('#'+firstNode.tId+"_span").trigger('click');
    }

}
//清除input内容
function clearInput(){
    $("input[name='lineInput']").val("");
}

function showTotalInfo(data){
    var myCompOverlay = new customOverlay(data);
    map.addOverlay(myCompOverlay);

}

function fillProvinceAndCity(province,city,temp){
    this.provinceArr =  this.provinceArr == null ? [] : this.provinceArr;
    var provinceArr = this.provinceArr;
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
            $("[data-menucode='mapInfo']", parent.document).attr({class:"hover tabSelColor"});
            $("[data-menucode='mapInfo'] a", parent.document).attr({class:"dropdown-toggle"});


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

//获得 百度地图样式Json
function getMapStyleJson(){
    return [{
        "featureType": "village",
        "elementType": "labels",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "town",
        "elementType": "labels",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "district",
        "elementType": "labels",
        "stylers": {
            "visibility": "on"
        }
    }, {
        "featureType": "entertainmentlabel",
        "elementType": "labels",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "hotellabel",
        "elementType": "labels",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "restaurantlabel",
        "elementType": "labels",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "shoppinglabel",
        "elementType": "labels",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "shoppinglabel",
        "elementType": "labels.icon",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "hotellabel",
        "elementType": "labels.icon",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "restaurantlabel",
        "elementType": "labels.icon",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "entertainmentlabel",
        "elementType": "labels.icon",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "medicallabel",
        "elementType": "labels.icon",
        "stylers": {
            "visibility": "on"
        }
    }, {
        "featureType": "estatelabel",
        "elementType": "labels.icon",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "estatelabel",
        "elementType": "labels",
        "stylers": {
            "visibility": "off"
        }
    }];
}






