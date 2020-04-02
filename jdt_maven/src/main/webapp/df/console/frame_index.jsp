<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<html lang="en">
<head>
	<title>宝信大数据应用开发平台-管理控制台</title>
	<%
		String path = request.getContextPath();
	%>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta name="description" content="platForm Frameset" />
	<meta name="viewport"
		  content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
	<%--
        <script type="text/javascript" src="<%=path%>/bxui/baosight-require.js"></script>
    --%>
	<link rel="stylesheet"  href="<%=path%>/df/console/css/style-frame-index.css" />
	<link rel="stylesheet" href="<%=path%>/bxui/other/css/style-frame-inner.css" />
	<style>
		body,html{
			height:90%;
		}
		[v-cloak]{ display: none; }

	</style>
	<link rel="stylesheet" href="<%=path%>/raisingui/busy-load/app.min.css">
</head>

<body id="boy"  style="overflow: auto;background: url('<%=path%>/raising/image/ListBackground.jpg');backgound-size:100% 100%; background-repeat: no-repeat; background-attachment: fixed;margin-top: 0px" >

    <div class="col-xs-12 searchArea" style="margin-top:0px;margin-bottom:15px;padding-top:5px;background:#DFEAFC; " >
        <%--<div id="alertdiv" class="alertdiv"></div>--%>
        <!-- PAGE CONTENT BEGINS -->
        <div class="row" >
            <%--<div class="col-sm-3 col-xs-12" >
            </div>--%>
            <div class="col-sm-3 col-xs-12 " style="margin-right: 10px;padding-left:5px;">
                <label id="cityLabel" for="city" class="labelThree" style="padding-right: 20px;font-weight: bolder;font-size: 16px;color:#2184C1" ></label>
                <div type="text" id="city" name="city" <%--data-bxwidget="bxcombobox" data-bxtype="string" --%> style="width:60%;float: right"></div>
            </div>
            <div class="col-sm-3 col-xs-12 " style="margin-right: 10px;">
                <label id="lineLabel" for="line" class="labelThree" style="font-weight: bold;font-size: 16px;color:#2184C1"></label>
                <div type="text" id="line" name="line" data-bxwidget="bxcombobox" data-bxtype="string" style="width:60%;float: right"></div>
            </div>
            <div class="col-sm-3 col-xs-12 " style="margin-right: 10px;">
                <label id="projectNameLabel" class="labelThree" style="font-weight: bold;font-size: 16px;color:#2184C1" ></label>
                <%--<input type="text" id="inqu_status-sectionName" data-bxwidget class="inputThree"/>--%>
            </div>
            <div id="search"  style=";float: right;width:100px;margin-right: 43px;">

            </div>
        </div><!-- /.row -->

    </div>

    <div class="row" id="list-section" style="margin: 25px 25px 0px 25px;">
        <div class="col-md-3" style="margin-bottom: 10px;width:20%;" v-cloak v-for="data in showDataList">
            <div class="widget-box widget-color-blue light-border ui-sortable-handle">
                <div class="widget-header">
                        <div class="widget-title smaller" >
                            <a class="backstage-link" onclick="selectProject(this)" herf="#"  style="color: #ffffff;cursor: pointer;font-size: large;height: 10px" menuePara=""
                               v-bind:collector="data.collectorName" v-bind:projectId="data.projectId"
                               v-bind:totalMileage="data.digLength" v-bind:totalLength="data.totalLength"
                                v-bind:tbmName="data.tbmName" v-bind:ringTotal="data.ringTotal">
                                <center>
                                    <span v-if="data.projectName.length <= 11">{{data.projectName}}</span>
                                    <span id="pName" v-else v-bind:title="data.projectName">{{data.projectName.substring(0,9)}}...</span>
                                    <br/><span style="font-size: small">{{data.tbmName}}</span>
                                </center>
                            </a>
                        </div>

                    <div class="widget-toolbar"></div>
               </div>

                <div class="widget-body">
                    <div class="widget-main padding-6" id="list-backstage">
                        <div class="alert alert-info" >
                            <center>
                                <table>
                                    <tr>{{data.createTime}}<tr/>
                                    <tr>
                                        <td>当前环/总环：</td>
                                        <td v-if="data.quality=='192'">{{data.currentRing}}/{{data.ringTotal}}</td>
                                        <td v-else><span style="color: #bc5b10;">{{data.currentRing}}</span>/{{data.ringTotal}}</td>
                                    </tr>
                                    <tr><td>掘进距离(m)：</td><td>{{data.digLength}}</td></tr>
                                    <tr><td>切口里程(m)：</td><td>{{data.cutMileage}}</td></tr>
                                </table>
                            </center>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="explain" style="position:fixed;bottom:0px;right:0px;left:0px;color: #bc5b10;text-align: right;background-color:#D9EDF7;">
        说明：褐色数据代表采集的数据可能存在问题
    </div>

</body>
<%@ include file="/bxui/bxuihead.jsp"%>
<script type="text/javascript" src="<%=toolkitPath%>/raisingui/busy-load/app.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/vue/vue.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=path%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=path%>/bxui/util/jsUtil.js"></script>
<script type="text/JavaScript">
    function initExplain(){
        var listWidth = $("#list-section").outerWidth(true);
        $("#explain").css("padding-right","15px");
        $("#explain").width(listWidth-15);

    }

    function getScrollWidth() {
        var noScroll, scroll, oDiv = document.createElement("DIV");
        oDiv.style.cssText = "position:absolute;top:10px;width:100px;height:100px; overflow:hidden;";
        noScroll = document.getElementById("boy").appendChild(oDiv).clientWidth;
        oDiv.style.overflowY = "scroll";
        scroll = oDiv.clientWidth;
        document.body.removeChild(oDiv);
        return noScroll-scroll;
    }
	var info = new Vue({
		el:"#list-section",
		data:{
		    allDataList:[],
		    showDataList:[]
		}
	})
    

    info.$watch('showDataList',function (nval, oval) {
        initExplain();
    })


    var codeName ;
    var city = [] ;
    var line = [];

    $(document).ready(function() {
        $("#boy").busyLoad("show",{
            spinner: "pump",
			background: "rgba(0, 0, 0, 0.39)"
		});

        var curHeight = document.documentElement.scrollHeight;
        $("#myCarousel").height(curHeight - 220);
        $(".figure1").animate({
            opacity:'1'
        },1000,function(){
            $(".carousel-caption").animate({
                left:'0',
                opacity:'1'
            },200);
        });
        //添加搜索元素。
        $("#cityLabel").html("城市：");
        $("#lineLabel").html("线路：");
        $("#projectNameLabel").html("项目：")
        $("#projectNameLabel").after("" +
            "<input type=\"text\" id=\"inqu_status-projectName\" data-bxwidget class=\"inputThree\"/>")
        $("#search").append(
            "<button class='btn btn-sm btn-block' onclick='search();'>" +
            "<div class='ace-icon fa fa-search'></div>\n" +
            "<span style='font: 15px;'>查询</span>\n" +
            "</button>"
        )

        //默认查询所有列表信息
        var callback = {
            onSuccess: function (paramJsonObj) {
                $("#boy").busyLoad("hide");
                info.allDataList = paramJsonObj.detailsArray;
                info.showDataList = paramJsonObj.detailsArray;
                //给条件框需要用到的变量赋值
                codeName = paramJsonObj.codeName;
                city = paramJsonObj.codeName.cityArray;

                //填充下拉列表框，并绑定事件
                baosightRequire.requireFunct(['bxcombobox'], function() {
                    $("#city").bxcombobox({
                        dataPattern: 'local',
                        data :city,
                        async : false,
                        select : function(event, ui) {
                        }
                    });
                    $("#city_select").prepend("<option value='' selected=selected>全部</option>")

                    $("#line").bxcombobox({
                        dataPattern: 'local',
                        data : line,
                        async : false,
                        select : function(event, ui) {
                        }
                    });
                    $("#line_select").prepend("<option value='' selected=selected>全部</option>")

                    //城市下拉列表框，更改事件
                    $("#city_select").change(function () {
                        var citySelected = $("#city_select").val();
                        var showDataList = [];
                        for(var i = 0;i<info.allDataList.length;i++){
                            var data = info.allDataList[i];
                            var temp = data.ccsId.indexOf(citySelected);
                            if(temp >= 0){
                                showDataList.push(data);
                            }
                        }
                        //显示数据
                        info.showDataList = showDataList;

                        //填充线路列表框
                        line = codeName[citySelected];
                        if(citySelected == ""){
                            line = [];
                        }
                        $("#line").bxcombobox({
                            dataPattern: 'local',
                            data : line,
                            async : false,
                            select : function(event, ui) {
                            }
                        });
                        $("#line_select").prepend("<option value='' selected=selected>全部</option>");
                    })

                    //线路下拉列表框，更改事件
                    $("#line_select").change(function(){
                        var lineSelected = $("#line_select").val();
                        if(lineSelected == ""){//如果选择全部，则查询城市下的列表信息
                            lineSelected = $("#city_select").val();
                        }
                        var showDataList = [];
                        for(var i = 0;i<info.allDataList.length;i++){
                            var data = info.allDataList[i];
                            var temp = data.ccsId.indexOf(lineSelected);
                            if(temp >= 0){
                                showDataList.push(data);
                            }
                        }
                        //显示数据
                        info.showDataList = showDataList;
                    })

                    //鼠标移入事件
                    // $("#pName").mouseover(function (data) {
                    //     $("#pName").attr("title",data.projectName);
                    // }).mouseout(function () {
                    //     console.log("鼠标移除");
                    // });

                });


            }
        };
        var paramJsonObj = {};
        AjaxCommunicator.ajaxRequest('/raising/forward/listQuery/getRows.do', 'POST', paramJsonObj, callback,true);


    });

function selectProject(obj){
    parent.document.getElementById("contentFrame").src ="<%=path%>/raising/frame.do";
    $("[data-menucode='infoWidgetShow']", parent.document).attr({class:"hover tabSelColor"});
    $("[data-menucode='infoWidgetShow'] a", parent.document).attr({class:"dropdown-toggle"});
    /*$("[data-menucode='infoWidgetShow']", parent.document).attr({class:"hover tabSelColor"});
    $("[data-menucode='infoWidgetShow'] a", parent.document).attr({class:"dropdown-toggle"});
    $("[data-menucode='chartShow']", parent.document).attr({class:"hover tabSelColor active"});
    $("[data-menucode='chartShow'] a", parent.document).attr({class:"dropdown-toggle tabSel"});*/
    var selected_id = $(obj).attr("projectId");
    var selected_name = $(obj).find("span:first").text();
    var selected_collection = $(obj).attr("collector");
    var totalLength = $(obj).attr("totalLength");
    var totalMileage = $(obj).attr("totalMileage");
    var tbmName = $(obj).attr("tbmName");
    var ringTotal = $(obj).attr("ringTotal");
    $.cookie('selected_name',selected_name, {path: "/"});
    $.cookie('selected_id',selected_id, {path: "/"});
    $.cookie('selected_collection',selected_collection,{path:"/"});
    $.cookie('totalLength',totalLength,{path:"/"});
    $.cookie('totalMileage',totalMileage,{path:"/"});
    $.cookie('tbmName',tbmName,{path:"/"});
    $.cookie('ringTotal',ringTotal,{path:"/"});
}



function search() {
        var projectName = $("#inqu_status-projectName").val();
		var showDataList = [];
		for(var i = 0;i<info.allDataList.length;i++){
			var data = info.allDataList[i];
			var temp = data.projectName.indexOf(projectName);
			if(temp >= 0){
				showDataList.push(data);
			}
		}
		//显示数据
		info.showDataList = showDataList;
    }


</script>
</html>
