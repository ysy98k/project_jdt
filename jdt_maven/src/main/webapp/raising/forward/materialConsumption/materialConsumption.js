

$(document).ready(function () {
    showGrid();

    init();
    //耗材下拉框改变事件
    $("#inqu_status-materialType").change(function(){
        query();
    })

    //时间下拉框改变事件
    $("#inqu_status-timeType").change(function(){
        showGrid();
    })
});

/**
 *
 */
function query(){
    var queryParam = new Object();
    var type = $("#inqu_status-timeType").val();
    var gridId = null;
    var data = getSettingCode();
    var materialType = data.materialType;
    var settingCode = data.settingCode;
    var settingUnitCode = data.settingUnitCode;
    var projectId = parseInt($.cookie("selected_id"));
    if("ring" == type){
        var startRing = parseInt($('#inqu_status-startRingNum').val());
        var endRing = parseInt($('#inqu_status-endRingNum').val());
        queryParam.materialType = materialType;
        queryParam.settingCode = settingCode;
        queryParam.settingUnitCode = settingUnitCode;
        queryParam.projectId = projectId;
        queryParam.type = "ring";
        queryParam.inqu_status = {};
        queryParam.inqu_status.projectId = projectId;
        if(!isInt(startRing) || !isInt(endRing) ){
            queryParam.inqu_status.startRingNum = startRing;
            queryParam.inqu_status.endRingNum = endRing;
        }
        gridId = "jqGrid4";
    }else{
        queryParam.materialType = materialType;
        queryParam.settingCode = settingCode;
        queryParam.settingUnitCode = settingUnitCode;
        queryParam.projectId = projectId;

        var sT = new Date($('#inqu_status-startTime').val());
        var eT = new Date($('#inqu_status-endTime').val());
        if(sT < eT ){
            queryParam.startTime = $('#inqu_status-startTime').val();
            queryParam.endTime = $('#inqu_status-endTime').val();
        }
        if("day" == type){
            queryParam.type = "day";
            gridId = "jqGrid";
        }else if("month" == type){
            queryParam.type = "month";
            gridId = "jqGrid2";
        }else if("season" == type){
            queryParam.type = "season";
            gridId = "jqGrid3";
        }else{
            queryParam.type = "day";
            gridId = "jqGrid";
        }
    }

    $("#"+gridId).bxgrid("option", "queryParam", queryParam);
    $("#"+gridId).bxgrid("query");
}
/**
 * 查询按钮点击事件
 */
function on_query_click() {
    var queryParam = new Object();
    var type = $("#inqu_status-timeType").val();
    var gridId = null;
    var data = getSettingCode();
    var materialType = data.materialType;
    var settingCode = data.settingCode;
    var settingUnitCode = data.settingUnitCode;
    var projectId = parseInt($.cookie("selected_id"));
    if("ring" == type){
        var startRing = parseInt($('#inqu_status-startRingNum').val());
        var endRing = parseInt($('#inqu_status-endRingNum').val());
        if(!isInt(startRing) || !isInt(endRing) ){
            alertDiv("提示","请输入起始和结束环");
            return;
        }
        queryParam.materialType = materialType;
        queryParam.settingCode = settingCode;
        queryParam.settingUnitCode = settingUnitCode;
        queryParam.projectId = projectId;
        queryParam.type = "ring";
        queryParam.inqu_status = {};
        queryParam.inqu_status.startRingNum = startRing;
        queryParam.inqu_status.endRingNum = endRing;
        queryParam.inqu_status.projectId = projectId;
        gridId = "jqGrid4";
    }else{
        queryParam.materialType = materialType;
        queryParam.settingCode = settingCode;
        queryParam.settingUnitCode = settingUnitCode;
        queryParam.projectId = projectId;
        queryParam.startTime = $('#inqu_status-startTime').val();
        queryParam.endTime = $('#inqu_status-endTime').val();
        var sT = new Date(queryParam.startTime);
        var eT = new Date(queryParam.endTime);
        if(sT >= eT ){
            alertDiv("提示","起始与结束时间不可以相同,且起始时间必须小于结束时间");
            return;
        }

        if("day" == type){
            queryParam.type = "day";
            gridId = "jqGrid";
        }else if("month" == type){
            queryParam.type = "month";
            gridId = "jqGrid2";
        }else if("season" == type){
            queryParam.type = "season";
            gridId = "jqGrid3";
        }else{
            queryParam.type = "day";
            gridId = "jqGrid";
        }
    }
    $("#"+gridId).bxgrid("option", "queryParam", queryParam);
    $("#"+gridId).bxgrid("query");
}


function showGrid(){
    var type = $("#inqu_status-timeType").val();
    var data = getSettingCode();
    var materialType = data.materialType;
    var settingCode = data.settingCode;
    var settingUnitCode = data.settingUnitCode;
    var projectId = parseInt($.cookie("selected_id"));
    if(type == "day"){
        $(".one").show();
        $(".two").hide();
        $(".three").hide();
        $(".four").hide();
        $("#timeCondition").show();
        $("#ringCondition").hide();
        if($("#jqGrid").html() == ""){
            baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'],
                function () {
                    $("#queryarea").bxdiv();
                    var gridOption = {
                        caption: false,
                        colNames: ['日期','单位','理论消耗','实际消耗','超耗量','理论累计', '实际累计','总超耗量','备注'],
                        colModel: [
                            {name:'date', index:'date',width: 40,readyOnly: true},
                            {name:'unit', index:'unit',width: 40,readyOnly: true},
                            {name:'theoreticalConsumption', index:'theoreticalConsumption',width: 40,readyOnly: true},
                            {name:'actualConsumption', index:'actualConsumption',width: 40,readyOnly: true},
                            {name:'excess', index:'excess',width: 40,readyOnly: true},
                            {name:'theoreticalAccumulation', index:'theoreticalAccumulation',width: 40,readyOnly: true},
                            {name:'actualAccumulation', index:'actualAccumulation',width: 40,readyOnly: true},
                            {name:'totalExcess', index:'totalExcess',width: 40,readyOnly: true},
                            {name:'remark', index:'remark',width: 40,readyOnly: true},
                        ],
                        sortorder: 'asc',
                        height: 331
                    };
                    var option = {
                        queryParam: {"projectId":projectId,"type":"day","inqu_status":{"projectId":projectId},
                            "materialType":materialType,"settingCode":settingCode,"settingUnitCode":settingUnitCode},
                        dataPattern: "url",
                        url: "/raising/forward/materialConsumption/materialConsumption/getRows.do",
                        showMsgOpt: {
                            showMsgId: "alertdiv"
                        },
                        callBackFunct:function(data){
                            var rows = [];
                            if(data.status == "0"){
                                rows = data.rows;
                            }
                            initCharts(rows,"myCharts1");
                        },
                        gridOption: gridOption
                    };
                    $("#jqGrid").bxgrid(option);
                });
        }

    }else if(type == "month"){
        $(".one").hide();
        $(".two").show();
        $(".three").hide();
        $(".four").hide();
        $("#timeCondition").show();
        $("#ringCondition").hide();
        if($("#jqGrid2").html() == ""){
            baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'],
                function () {
                    $("#queryarea").bxdiv();
                    var gridOption = {
                        caption: false,
                        colNames: ['日期','单位','理论消耗','实际消耗','超耗量','理论累计', '实际累计','总超耗量','备注'],
                        colModel: [
                            {name:'date', index:'date',width: 40,readyOnly: true},
                            {name:'unit', index:'unit',width: 40,readyOnly: true},
                            {name:'theoreticalConsumption', index:'theoreticalConsumption',width: 40,readyOnly: true},
                            {name:'actualConsumption', index:'actualConsumption',width: 40,readyOnly: true},
                            {name:'excess', index:'excess',width: 40,readyOnly: true},
                            {name:'theoreticalAccumulation', index:'theoreticalAccumulation',width: 40,readyOnly: true},
                            {name:'actualAccumulation', index:'actualAccumulation',width: 40,readyOnly: true},
                            {name:'totalExcess', index:'totalExcess',width: 40,readyOnly: true},
                            {name:'remark', index:'remark',width: 40,readyOnly: true},
                        ],
                        sortorder: 'asc',
                        height: 331
                    };
                    var option = {
                        queryParam: {"projectId":projectId,"type":"month","inqu_status":{"projectId":projectId},
                            "materialType":materialType,"settingCode":settingCode,"settingUnitCode":settingUnitCode},
                        dataPattern: "url",
                        url: "/raising/forward/materialConsumption/materialConsumption/getRows.do",
                        showMsgOpt: {
                            showMsgId: "alertdiv"
                        },
                        callBackFunct:function(data){
                            var rows = [];
                            if(data.status == "0"){
                                rows = data.rows;
                            }
                            initCharts(rows,"myCharts2");
                        },
                        gridOption: gridOption
                    };
                    $("#jqGrid2").bxgrid(option);
                });
        }
    }else if(type == "season"){
        $(".one").hide();
        $(".two").hide();
        $(".three").show();
        $(".four").hide();
        $("#timeCondition").show();
        $("#ringCondition").hide();
        if($("#jqGrid3").html() == ""){
            baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'],
                function () {
                    $("#queryarea").bxdiv();
                    var gridOption = {
                        caption: false,
                        colNames: ['日期','单位','理论消耗','实际消耗','超耗量','理论累计', '实际累计','总超耗量','备注'],
                        colModel: [
                            {name:'date', index:'date',width: 40,readyOnly: true},
                            {name:'unit', index:'unit',width: 40,readyOnly: true},
                            {name:'theoreticalConsumption', index:'theoreticalConsumption',width: 40,readyOnly: true},
                            {name:'actualConsumption', index:'actualConsumption',width: 40,readyOnly: true},
                            {name:'excess', index:'excess',width: 40,readyOnly: true},
                            {name:'theoreticalAccumulation', index:'theoreticalAccumulation',width: 40,readyOnly: true},
                            {name:'actualAccumulation', index:'actualAccumulation',width: 40,readyOnly: true},
                            {name:'totalExcess', index:'totalExcess',width: 40,readyOnly: true},
                            {name:'remark', index:'remark',width: 40,readyOnly: true},
                        ],
                        sortorder: 'asc',
                        height: 331
                    };
                    var option = {
                        queryParam: {"projectId":projectId,"type":"season","inqu_status":{"projectId":projectId},
                            "materialType":materialType,"settingCode":settingCode,"settingUnitCode":settingUnitCode},
                        dataPattern: "url",
                        url:"/raising/forward/materialConsumption/materialConsumption/getRows.do",
                        showMsgOpt: {
                            showMsgId: "alertdiv"
                        },
                        callBackFunct:function(data){
                            var rows = [];
                            if(data.status == "0"){
                                rows = data.rows;
                            }
                            initCharts(rows,"myCharts3");
                        },
                        gridOption: gridOption
                    };
                    $("#jqGrid3").bxgrid(option);
                });
        }
    }else if(type == "ring"){
        $(".one").hide();
        $(".two").hide();
        $(".three").hide();
        $(".four").show();
        $("#timeCondition").hide();
        $("#ringCondition").show();

        if($("#jqGrid4").html() == ""){
            baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'],
                function () {
                    $("#queryarea").bxdiv();
                    var gridOption = {
                        caption: false,
                        colNames: ['环','单位','理论消耗','实际消耗','超耗量','理论累计', '实际累计','总超耗量','备注'],
                        colModel: [
                            {name:'MR_Ring_Num', index:'date',width: 40,readyOnly: true},
                            {name:'unit', index:'unit',width: 40,readyOnly: true},
                            {name:'theoreticalConsumption', index:'theoreticalConsumption',width: 40,readyOnly: true},
                            {name:'actualConsumption', index:'actualConsumption',width: 40,readyOnly: true},
                            {name:'excess', index:'excess',width: 40,readyOnly: true},
                            {name:'theoreticalAccumulation', index:'theoreticalAccumulation',width: 40,readyOnly: true},
                            {name:'actualAccumulation', index:'actualAccumulation',width: 40,readyOnly: true},
                            {name:'totalExcess', index:'totalExcess',width: 40,readyOnly: true},
                            {name:'remark', index:'remark',width: 40,readyOnly: true},
                        ],
                        sortorder: 'asc',
                        height: 331
                    };

                    var option = {
                        queryParam: {"inqu_status":{"projectId":projectId},"projectId":projectId,"type":"ring",
                            "materialType":materialType,"settingCode":settingCode,"settingUnitCode":settingUnitCode},
                        dataPattern: "url",
                        url: "/raising/forward/materialConsumption/materialConsumption/getRows.do",
                        showMsgOpt: {
                            showMsgId: "alertdiv"
                        },
                        callBackFunct:function(data){
                            var rows = [];
                            if(data.status == "0"){
                                rows = data.rows;
                            }
                            initCharts(rows,"myCharts4");
                        },
                        gridOption: gridOption
                    };
                    $("#jqGrid4").bxgrid(option);
                });
        }
    }
}

function getSettingCode(){
    var data = {};
    var material = $("#inqu_status-materialType").val();
    if(material == "GTotal"){
        data.materialType = "GTotal";
        data.settingCode = "Gtotal_theory";
        data.settingUnitCode = "G_unit";
    }else if(material == "Etotal"){
        data.materialType = "ETotal";
        data.settingCode = "Etotal_theory";
        data.settingUnitCode = "E_unit";
    }else if(material == "FOTotal"){
        data.materialType = "FOTotal";
        data.settingCode = "FOtotal_theory";
        data.settingUnitCode = "FO_unit";
    }else{
        data.materialType = "GTotal";
        data.settingCode = "Gtotal_theory";
        data.settingUnitCode = "G_unit";
    }
    return data;
}

/**
 * 导出
 */
function exportPages() {
    var chartId = null;
    if($("#inqu_status-timeType").val() == "season"){
        chartId = "jqGrid3";
    }else if( $("#inqu_status-timeType").val() == "month" ){
        chartId = "jqGrid2";
    }else {
        chartId = "jqGrid";
    }
    var table = $("#"+chartId).find("table[class='ui-pg-table']")[1];
    var page = $(table).find("td[dir='ltr']")[0];
    var curPage =  $(page).find("span").html();
    var rowNum = $(table).find("td[dir='ltr']")[1];
    var curRowNum = $(rowNum).find("select").val();

    var queryParam = new Object();
    if($('#inqu_status-startTime').val() != $('#inqu_status-endTime').val()){
        queryParam.startTime = $('#inqu_status-startTime').val();
        queryParam.endTime = $('#inqu_status-endTime').val();
    }
    var data = getSettingCode();
    var materialType = data.materialType;
    var settingCode = data.settingCode;
    var settingUnitCode = data.settingUnitCode;
    var projectId = parseInt($.cookie("selected_id"));

    if("ring" == $("#inqu_status-timeType").val()){
        queryParam.downloadColumnDesc ='环号,单位,理论消耗,实际消耗,超耗量,理论累计,实际累计,总超耗量';
        queryParam.downloadColumn = "MR_Ring_Num,unit,theoreticalConsumption,actualConsumption,excess,theoreticalAccumulation,actualAccumulation,totalExcess";
        queryParam.inqu_status ={"projectId":projectId};
    }else{
        queryParam.downloadColumnDesc ='日期,单位,理论消耗,实际消耗,超耗量,理论累计,实际累计,总超耗量';
        queryParam.downloadColumn = "date,unit,theoreticalConsumption,actualConsumption,excess,theoreticalAccumulation,actualAccumulation,totalExcess";
    }

    queryParam.columnImageMap = {};
    queryParam.columnSelectMap = {};
    queryParam.curPage = curPage;
    queryParam.curRowNum = curRowNum;
    queryParam.projectId = projectId;
    queryParam.materialType = materialType;
    queryParam.settingCode = settingCode;
    queryParam.settingUnitCode = settingUnitCode;
    queryParam.type = $("#inqu_status-timeType").val();
    var url =toolkitPath+ "/raising/forward/materialConsumption/materialConsumption/download.do?ajaxParam="+encodeURIComponent(encodeURIComponent(JSON.stringify(queryParam)));
    window.location.href = url;


}

function init(){
    $('#inqu_status-startTime').daterangepicker({
        "singleDatePicker": true,
        "showDropdowns": true,
        "startDate": new Date(),
        "locale":{
            "format": "YYYY-MM-DD",
            "applyLabel": "确定",
            "cancelLabel": "取消",
            "weekLabel": "W",
            "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
            "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"]
        }
    }, function(start, end, label) {
    });
    $('#inqu_status-endTime').daterangepicker({
        "singleDatePicker": true,
        "showDropdowns": true,
        "startDate": new Date(),
        "locale":{
            "format": "YYYY-MM-DD",
            "applyLabel": "确定",
            "cancelLabel": "取消",
            "weekLabel": "W",
            "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
            "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"]
        }
    }, function(start, end, label) {
    });
}

function initCharts(rows,idName){
    var dates = [];
    var works = [];
    for(var i =0;i<rows.length;i++){
        dates.push(rows[i].x);
        works.push(rows[i].actualConsumption);
    }

    var myChart = echarts.init(document.getElementById(idName));

    option = {
        tooltip : {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow',
                label: {
                    show: false
                }
            }
        },
        toolbox: {//工具，折线图，保存图片等功能
            show : true,
            feature : {
                mark : {show: true},
                magicType: {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        calculable : true,
        legend: {
            data:['woShiNiBaBa', 'Budget 2011'],
            itemGap: 5
        },
        xAxis: [
            {
                type : 'category',
                data : dates
            }
        ],
        yAxis: [
            {
                type : 'value',
                name : '累计值'
            }
        ],
        series : [
            {
                type: 'bar',
                barGrap:'50%',
                barCategoryGap:'50%',
                barMaxWidth:10,
                data: works,
                itemStyle:{color:"#008B00"}
            }

        ]
    };
    myChart.setOption(option);
}








