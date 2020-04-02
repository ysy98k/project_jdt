var vue  = new Vue({
    el:"#father",
    data:{
        soil_curMS:[],
        soil_Dumping_volume_Total:[],
        soil_Dumping_volume_DTotal:[],
        soil_BeltRpm:[],
        soil_SP1:[],
        soil_ringsList:[],
        soil_ring_Dumping_volume_Total:[],
        soil_lowerLimit:[],
        soil_upperLimit:[],
        soil_ring_Dumping_volume_DTotal:[],
        segment_dataList:[]
    }
})

$(function(){
    //初始化下拉框
    $('.chosen-select').chosen({
        allow_single_deselect : true
    });
    $(window).on('resize.chosen', function() {
        $('.chosen-select').next().css({
            'width' : '210px'
        });
    }).trigger('resize.chosen');

    init();

    getData("ring");

    $("#ringNum").change(function(){
        $("#ring").removeAttr("data-show");
        getData("ring");
    })
    $("#soilRingNum").change(function(){
        $("#soilRing").removeAttr("data-show");
        getData("soilRing");
    })
})


function getData(type){
    //$("#father").busyLoad("show",{ spinner: "accordion"});
    $("#father").busyLoad("show");

    if(type== "ring"){//环片
        var show = $("#ring").attr("data-show");
        if(show == "false"){
            $("#father").busyLoad("hide");
            return;
        }
        var selectNum = $("#ringNum").val();
        var paramJson = {};
        paramJson.projectId = parseInt($.cookie("selected_id"));
        paramJson.collectorName = $.cookie("selected_collection");
        paramJson.MR_Ring_Num = parseInt(selectNum);
        paramJson.type = "ring";

        $.ajax({
            "url":toolkitPath + "/raising/forward/construction/reportTable/getRows.do",
            "type":"post",
            "data":{"ajaxParam":JSON.stringify(paramJson)},
            "dataType":"json",
            "success":function(data){
                showRing(data);
            },
            "error":function(){
                console.log("err");
            },
            "complete":function(){
                $("#father").busyLoad("hide");
                $("#ring").attr("data-show",false);
            }
        })

    }else if(type == "soilRing"){//排土量
        var show = $("#soilRing").attr("data-show");
        if(show == "false"){
            $("#father").busyLoad("hide");
            return;
        }

        var selectNum = $("#soilRingNum").val();
        var paramJson = {};
        paramJson.projectId = parseInt($.cookie("selected_id"));
        paramJson.collectorName = $.cookie("selected_collection");
        paramJson.MR_Ring_Num = parseInt(selectNum);
        paramJson.curMsArr = vue.soil_curMS;

        paramJson.type = "soilRing";
        $.ajax({
            "url":toolkitPath + "/raising/forward/construction/reportTable/getRows.do",
            "type":"post",
            "data":{"ajaxParam":JSON.stringify(paramJson)},
            "dataType":"json",
            "success":function(data){
                showSoilRing(data);
            },
            "error":function(){

            },
            "complete":function(){
                $("#father").busyLoad("hide");
                $("#soilRing").attr("data-show",false);
            }
        })


    }else if(type == "soilRings"){
        var show = $("#soilRings").attr("data-show");
        if(show == "false"){
            $("#father").busyLoad("hide");
            return;
        }

        var startRing = $("#startRing").val();
        var endRing = $("#endRing").val();
        var paramJson = {};
        if(isInt(startRing) && isInt(endRing)){
            paramJson.startRingNum = parseInt(startRing);
            paramJson.endRingNum = parseInt(endRing);
        }

        paramJson.projectId = parseInt($.cookie("selected_id"));
        paramJson.type = "soilRings";
        $.ajax({
            "url":toolkitPath + "/raising/forward/construction/reportTable/getRows.do",
            "type":"post",
            "data":{"ajaxParam":JSON.stringify(paramJson)},
            "dataType":"json",
            "success":function(data){
                showSoilRings(data);
            },
            "error":function(){

            },
            "complete":function(){
                $("#father").busyLoad("hide");
                $("#soilRings").attr("data-show",false);
            }
        })
    }else if(type == "segment"){//管片
        var show = $("#segment").attr("data-show");
        if(show == "false"){
            $("#father").busyLoad("hide");
            return;
        }
        var startRing = $("#segmentStartRing").val();
        var endRing = $("#segmentEndRing").val();
        var paramJson = {};
        if(isInt(startRing) && isInt(endRing)){
            paramJson.startRing = parseInt(startRing);
            paramJson.endRing = parseInt(endRing);
        }

        paramJson.projectId = parseInt($.cookie("selected_id"));
        paramJson.type = "segment";
        $.ajax({
            "url":toolkitPath + "/raising/forward/construction/reportTable/getRows.do",
            "type":"post",
            "data":{"ajaxParam":JSON.stringify(paramJson)},
            "dataType":"json",
            "success":function(data){
                showSegment(data);
            },
            "error":function(){

            },
            "complete":function(){
                $("#father").busyLoad("hide");
                $("#segment").attr("data-show",false);
            }
        })
    }
}

function showRing(data){
    var ringArr = data.ringsNumList;
    var x = data.xAxis;
    var JN = data.JN;
    var JV = data.JV;
    var CTor = data.CTor;
    var CRpm = data.CRpm;
    var SP1 = data.SP1;
    var SCRpm = data.SCRpm;
    var SCTor = data.SCTor;
    var MR_Act_A1HD = data.MR_Act_A1HD;
    var MR_Act_A1VD = data.MR_Act_A1VD;
    var J1SBefore = data.J1SBefore;
    var J2SBefore = data.J2SBefore;
    var J3SBefore = data.J3SBefore;
    var J4SBefore = data.J4SBefore;
    var J1SAfter = data.J1SAfter;
    var J2SAfter = data.J2SAfter;
    var J3SAfter = data.J3SAfter;
    var J4SAfter = data.J4SAfter;
    var assembleTime = data.assembleTime;
    var tunnellingTime = data.tunnellingTime;
    var shuDownTime = data.shuDownTime;
    showLineCharts("JN",x,"总推力(MPa)",JN);
    showLineCharts("JV",x,"千斤顶平均速度(mm/min)",JV);
    showLineCharts("CTor",x,"刀盘扭矩(kN·m)",CTor);
    showLineCharts("CRpm",x,"刀盘转速(r/min)",CRpm);
    showLineCharts("SP1",x,"上部土压(MPa)",SP1);
    showLineCharts("SCRpm",x,"螺旋机转速(r/min)",SCRpm);
    showLineCharts("SCTor",x,"螺旋机扭矩(kN·m)",SCTor);
    showLineCharts("MR_Act_A1",x,"盾构机首姿态(mm)",{"MR_Act_A1HD":MR_Act_A1HD,"MR_Act_A1VD":MR_Act_A1VD});
    showLineCharts("timeChart",["停机","掘进","拼装"],"掘进时间(h)",[shuDownTime,tunnellingTime,assembleTime]);
    $("#J1SBefore").html(J1SBefore);
    $("#J2SBefore").html(J2SBefore);
    $("#J3SBefore").html(J3SBefore);
    $("#J4SBefore").html(J4SBefore);
    $("#J1SAfter").html(J1SAfter);
    $("#J2SAfter").html(J2SAfter);
    $("#J3SAfter").html(J3SAfter);
    $("#J4SAfter").html(J4SAfter);
    if(ringArr != undefined || ringArr != null){
        var str = "";
        for(var i=0;i<ringArr.length;i++){
            str += "<option value='"+ringArr[i]+"' >"+ringArr[i]+"</option>";
        }
        $("#ringNum").html("");
        $("#ringNum").append(str);
        $('.chosen-select').trigger("chosen:updated")
    }
    $("#ringNum").val(data.MR_Ring_Num+"");

}

function showSoilRing(data){

    var MR_Ring_Num = data.MR_Ring_Num;
    var ringArr = data.ringsNumList;
    vue.soil_curMS = data.curMsList == undefined ? [] :data.curMsList ;
    vue.soil_Dumping_volume_Total = data.Dumping_volume_Total == undefined ? [] :data.Dumping_volume_Total ;
    vue.soil_Dumping_volume_DTotal = data.Dumping_volume_DTotal == undefined ? [] :data.Dumping_volume_DTotal ;;
    vue.soil_BeltRpm = data.BeltRpm == undefined ? [] :data.BeltRpm ;;
    vue.soil_SP1 = data.SP1 == undefined ? [] :data.SP1 ;;

    if(data.curMsList == null || data.curMsList.length < 12){
        var len = 12 - (data.curMsList == null ? 0 : data.curMsList);
        for(var i=0;i<len;i++){
            vue.soil_curMS.push(null);
            vue.soil_Dumping_volume_Total.push(null);
            vue.soil_Dumping_volume_DTotal.push(null);
            vue.soil_BeltRpm.push(null);
            vue.soil_SP1.push(null);
        }
    }
    //展示单环信息
    showSoilChart("单环");
    if(ringArr != undefined || ringArr != null){
        var str = "";
        for(var i=0;i<ringArr.length;i++){
            str += "<option value='"+ringArr[i]+"' >"+ringArr[i]+"</option>";
        }
        $("#soilRingNum").html("");
        $("#soilRingNum").append(str);
        $('.chosen-select').trigger("chosen:updated")
    }
    $("#soilRingNum").val(isNullOrEmptyOrUndefiend(MR_Ring_Num)? "-1" : MR_Ring_Num+"");


}

function soilRingsSearch(){
    var startRing = $("#startRing").val();
    var endRing = $("#endRing").val();
    if(!isInt(startRing) || !isInt(endRing)){
        alertDiv("提示","请输入起始和结束环");
        return;
    }
    $("#soilRings").removeAttr("data-show");
    getData("soilRings");
}

function showSoilRings(data){
    vue.soil_ringsList = data.ringsList;
    vue.soil_ring_Dumping_volume_Total = data.ring_Dumping_volume_Total;
    vue.soil_lowerLimit = data.lowerLimit;
    vue.soil_upperLimit = data.upperLimit;
    vue.soil_ring_Dumping_volume_DTotal = data.ring_Dumping_volume_DTotal;

    if(data.ringsList == null || data.ringsList.length < 10){
        var len = 10 - (data.ringsList == null ? 0 : data.ringsList);
        for(var i=0;i<len;i++){
            vue.soil_ringsList.push(null);
            vue.soil_ring_Dumping_volume_Total.push(null);
            vue.soil_lowerLimit.push(null);
            vue.soil_upperLimit.push(null);
            vue.soil_ring_Dumping_volume_DTotal.push(null);
        }
    }
    showSoilChart("多环");

}

function segmentSearch(){
    var startRing = $("#segmentStartRing").val();
    var endRing = $("#segmentEndRing").val();
    if(!isInt(startRing) || !isInt(endRing)){
        alertDiv("提示","请输入起始和结束环");
        return;
    }
    $("#segment").attr("data-show",true);
    getData("segment");
}


function showSegment(data){
    vue.segment_dataList = data.dataList;
    if(data.dataList == null || data.dataList.length < 10){
        var len = 10 - (data.dataList == null ? 0 : data.dataList);
        for(var i=0;i<len;i++){
            vue.segment_dataList.push(new Object());
        }
    }

    showSegementChart(data.ringsList,data.horizontalLine,data.verticalLine,data.pieData);
}


function showLineCharts(id,xData,title,data){
    var series = [];
    var color = ['#0AFF00'];
    if(id == "timeChart"){
        var one =
        {
            type: 'bar',
            name:title,
            barGrap:'50%',
            barCategoryGap:'50%',
            barMaxWidth:10,
            data: data,
            itemStyle:{color:"#008B00"}
        }
        series.push(one);
    }else if(id == "MR_Act_A1"){
        var one = {};
        one.data = data.MR_Act_A1HD;
        one.type = 'line';
        one.name = "水平偏差";
        series.push(one);
        var two = {};
        two.data = data.MR_Act_A1VD;
        two.type = 'line';
        two.name = "垂直偏差";
        series.push(two);
        color.push('#23FDE2');
        legend = {
            data:[
                {
                    name: "水平",
                    textStyle: {
                        color: '#FFFFFF'
                    }
                }, {
                    name: "垂直",
                    textStyle: {
                        color: '#FFFFFF'
                    }
                }
            ]

        };
    }else{
        var one = {};
        one.data = data;
        one.type = 'line';
        one.name = title;
        series.push(one);
    }
    var option = {
        xAxis: {
            type: 'category',
            data: xData,
            nameTextStyle:{
                color:'#FFFFFF'
            },
            axisLine:{
                lineStyle:{
                    color:'#FFFFFF'
                }
            }
        },
        tooltip : {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow',
                label: {
                    show: false
                }
            }
        },
        title: {
            left: 'center',
            text: title,
            textStyle:{
                color:'#FFFFFF'
            }
        },
        color:color,
        yAxis: {
            type: 'value',
            axisLine:{
                lineStyle:{
                    color:'#FFFFFF'
                }
            }
        },
        series: series
    };
    var myChart = echarts.init(document.getElementById(id));
    myChart.setOption(option);

}
//展示排土量折线图
function showSoilChart(type){
    var id = null;
    var x = null;
    var title = null;
    var series = [];
    var color = null;
    var legend = {"right":0,y:50,x:630,orient: 'vertical',textStyle:{color:'white'}};
    if(type == "单环"){
        id = "soilRingChart"
        x = vue.soil_curMS;
        title = "单环排土量";
        color = ["#0AFF00","#13C7FF","#FF6011","#FFA9AC"];
        legend.data = ["累计体积（m3）","理论体积（m3）","皮带机转速（m/s）","土压（MPa）"];
        var temp1 = {"name":"累计体积（m3）","type":"line","data":vue.soil_Dumping_volume_Total};
        var temp2 = {"name":"理论体积（m3）","type":"line","data":vue.soil_Dumping_volume_DTotal};
        var temp3 = {"name":"皮带机转速（m/s）","type":"line","data":vue.soil_BeltRpm};
        var temp4 = {"name":"土压（MPa）","type":"line","data":vue.soil_SP1};
        series.push(temp1);
        series.push(temp2);
        series.push(temp3);
        series.push(temp4);
    }else if(type == "多环"){
        id = "soilRingsChart";
        x = vue.soil_ringsList;
        title = "多环排土量";
        color = ['#0AFF00'];
        legend = null;
        var one =
            {
                type: 'bar',
                name:title,
                barGrap:'50%',
                barCategoryGap:'50%',
                barMaxWidth:10,
                data: vue.soil_ring_Dumping_volume_Total,
                itemStyle:{color:"#008B00"}
            }
        series.push(one);
    }

    var option = {
        legend:legend,
        xAxis: {
            type: 'category',
            data: x,
            nameTextStyle:{
                color:'#FFFFFF'
            },
            axisLine:{
                lineStyle:{
                    color:'#FFFFFF'
                }
            }
        },
        tooltip : {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow',
                label: {
                    show: false
                }
            }
        },
        title: {
            left: 'center',
            text: title,
            textStyle:{
                color:'#FFFFFF'
            }
        },
        color:color,
        yAxis: {
            type: 'value',
            axisLine:{
                lineStyle:{
                    color:'#FFFFFF'
                }
            }
        },
        series: series
    };
    if(type == "单环"){
        option.grid = { right:150};
    }
    var myChart = echarts.init(document.getElementById(id));
    myChart.setOption(option);
}

//展示管片姿态折线图
function showSegementChart(x,horizontalLine,verticalLine,pieData){
    var series = [];
    var horizontal = {"name":"水平偏差（mm）","type":"line","data":horizontalLine};
    var vertical = {"name":"垂直偏差（mm）","type":"line","data":verticalLine};
    series.push(horizontal);
    series.push(vertical);
    var lineOption = {
        legend:{y:25,data:["水平偏差（mm）","垂直偏差（mm）"],textStyle:{color:'white'}},
        xAxis: {
            type: 'category',
            data: x,
            nameTextStyle:{
                color:'#FFFFFF'
            },
            axisLine:{
                lineStyle:{
                    color:'#FFFFFF'
                }
            }
        },
        tooltip : {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow',
                label: {
                    show: false
                }
            }
        },
        title: {
            top:25,
            left: 'left',
            text: "管片姿态偏差",
            textStyle:{
                color:'#FFFFFF'
            }
        },
        color:["#0AFF00","#13C7FF"],
        yAxis: {
            type: 'value',
            axisLine:{
                lineStyle:{
                    color:'#FFFFFF'
                }
            }
        },
        series: series
    };
    var lineChart = echarts.init(document.getElementById("segmentLineChart"));
    lineChart.setOption(lineOption);

   var pieOption = {
        title : {
            text: '管片数据分类',
            x:'center',
            textStyle:{
                color:'#FFFFFF'
            }
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            type: 'scroll',
            orient: 'vertical',
            right: 10,
            top: 20,
            bottom: 20,
            data: ["偏差0<= |x| < 30","偏差30<= |x| < 50","偏差50<= |x| < 70","偏差70<= |x| < 100","偏差100<= |x|"],
            textStyle:{
                color:'#FFFFFF'
            }
        },
       color:["#30AB0D","#13C7FF","yellow","#FF6011","#C23531"],
        series : [
            {
                name: '姓名',
                type: 'pie',
                radius : '55%',
                center: ['40%', '50%'],
                data: pieData,
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
    var pieChart = echarts.init(document.getElementById("segmentPieChart"));
    pieChart.setOption(pieOption);
}

function exportCSV(type){
    if(type == "soilRings"){
        var startRing = $("#startRing").val();
        var endRing = $("#endRing").val();
        var paramJson = {};
        if(isInt(startRing) && isInt(endRing)){
            paramJson.startRingNum = parseInt(startRing);
            paramJson.endRingNum = parseInt(endRing);
        }
        paramJson.projectId = parseInt($.cookie("selected_id"));
        paramJson.type = "soilRings";
        paramJson.columnNamesList = ["环号","体积","设计下限","设计上限","理论体积"];
        var str = JSON.stringify(paramJson);
        window.location.href = toolkitPath + "/raising/forward/construction/reportTable/downCSV.do?ajaxParam="+encodeURIComponent(encodeURIComponent(str));

    }else if(type == "segment"){
        var startRing = $("#segmentStartRing").val();
        var endRing = $("#segmentEndRing").val();
        var paramJson = {};
        if(isInt(startRing) && isInt(endRing)){
            paramJson.startRing = parseInt(startRing);
            paramJson.endRing = parseInt(endRing);
        }

        paramJson.projectId = parseInt($.cookie("selected_id"));
        paramJson.type = "segment";
        paramJson.columnNamesList = ["环号","实测坐标X(m)","实测坐标Y(m)","实测坐标Z(m)",
            "里程","设计坐标X(m)","设计坐标Y(m)","设计坐标Z(m)","水平偏差(mm)","垂直偏差(mm)"];
        var str = JSON.stringify(paramJson);
        window.location.href = toolkitPath + "/raising/forward/construction/reportTable/downCSV.do?ajaxParam="+encodeURIComponent(encodeURIComponent(str));
    }
}


//下载整个页面。
function dowanload(){
    //下载body.。我肯定不下载Body。以后改
    html2canvas(document.body, {
        onrendered:function(canvas) {
            var contentWidth = canvas.width;
            var contentHeight = canvas.height;
            //一页pdf显示html页面生成的canvas高度;
            var pageHeight = contentWidth / 592.28 * 841.89;
            //未生成pdf的html页面高度
            var leftHeight = contentHeight;
            //pdf页面偏移
            var position = 0;
            //a4纸的尺寸[595.28,841.89]，html页面生成的canvas在pdf中图片的宽高
            var imgWidth = 595.28;
            var imgHeight = 592.28/contentWidth * contentHeight;

            var pageData = canvas.toDataURL('image/jpeg', 1.0);

            var pdf = new jsPDF('', 'pt', 'a4');

            //有两个高度需要区分，一个是html页面的实际高度，和生成pdf的页面高度(841.89)
            //当内容未超过pdf一页显示的范围，无需分页
            if (leftHeight < pageHeight) {
                pdf.addImage(pageData, 'JPEG', 0, 0, imgWidth, imgHeight );
            } else {
                while(leftHeight > 0) {
                    pdf.addImage(pageData, 'JPEG', 0, position, imgWidth, imgHeight)
                    leftHeight -= pageHeight;
                    position -= 841.89;
                    //避免添加空白页
                    if(leftHeight > 0) {
                        pdf.addPage();
                    }
                }
            }

            pdf.save('content.pdf');
        }
    })
}

//初始高度宽度
function init(){
    var baba = $("#tableContent").parent();
    var babaHeight = $(baba).height();
    var ulHeight = $("#myTab").height();
    $("#tableContent").height(babaHeight - ulHeight -35);
    var soilRingTableHeight = $("#soilRingTable").height();

    var maxHeight = document.body.clientHeight;
    if(maxHeight < 800){
        $("#segmentLineChart").height(185);
        $("#segmentPieChart").height(185);
    }

}