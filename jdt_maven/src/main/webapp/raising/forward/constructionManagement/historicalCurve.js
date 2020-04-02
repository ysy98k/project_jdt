
$(document).ready(function () {
    init();
    initTime();

    var selectType = $("li[class='layui-this']").html();
    var data =  getColumn(selectType);
    var condition = {
        "projectId":parseInt($.cookie("selected_id")),
        "lineArr":data.lineArr,
        "type":"ring",
        "getRing":"1",
        "collectorName":$.cookie("selected_collection")
        };
    ShenXianYiZhi(selectType,data.columnArr,data.lineArr,condition,"管理行程");


    //最上侧列表点击事件
    $("ul[class='layui-tab-title'] li").click(function(event){
        var type = event.currentTarget.innerHTML;

        var data = getColumn(type);
        var columnArr = data.columnArr;
        var lineArr = data.lineArr;

        var queryParam = new Object();
        var xName = null;
        if($('#inqu_status-startTime').val() != "" || $('#inqu_status-endTime').val() != ""){
            xName = "时间";
            queryParam.type="time";
            var startTimeStr = $('#inqu_status-startTime').val() == "" ? null : $('#inqu_status-startTime').val();
            var endTimeStr = $('#inqu_status-endTime').val() == "" ? null : $('#inqu_status-endTime').val();
            if(startTimeStr == endTimeStr){
                alertDiv("提示","起始与结束时间不可以相同！");
                return;
            }
            var startTime = new Date(startTimeStr);
            var endTime = new Date(endTimeStr);
            getTimeData(type,columnArr,lineArr,startTime.getTime(),endTime.getTime());
            return;
        }
        if("-1" != $("select[name='ringNum']").val()){
            xName = "管理行程";
            queryParam.type="ring";
            queryParam.MR_Ring_Num = parseInt($("select[name='ringNum']").val());
            queryParam.collectorName = $.cookie("selected_collection");
        }else if($("#inqu_status-startMileage").val() != "" || $("#inqu_status-endMileage").val() != ""){
            xName = "里程";
            queryParam.type="mileage";
            queryParam.startMileage = $('#inqu_status-startMileage').val() == "" ? null : parseFloat($('#inqu_status-startMileage').val());
            queryParam.endMileage = $('#inqu_status-endMileage').val() == "" ? null : parseFloat($('#inqu_status-endMileage').val());
            queryParam.collectorName = $.cookie("selected_collection");
        }
        queryParam.projectId = parseInt($.cookie("selected_id"));
        queryParam.getRing = "2";
        queryParam.lineArr = lineArr;
        ShenXianYiZhi(type,columnArr,lineArr,queryParam,xName);

    })
})

function getColumn(type){
    var data = {};
    var columnArr = [];
    var lineArr = [];
    switch (type){
        case "土压":
            columnArr = ['开挖面土压（上）(MPa)','开挖面土压（下）(MPa)','开挖面土压（左）(MPa)','开挖面土压（右）(MPa)'];
            lineArr = ["SP1","SP2","SP3","SP4"];
            break;
        case "刀盘":
            columnArr = ['转速(rpm)','扭距(kNm)'];
            lineArr = ["CRpm","CTor"];
            break;
        case "主驱动电机":
            columnArr = ['No.1刀盘电动机电流(A)','No.2刀盘电动机电流(A)','No.3刀盘电动机电流(A)',
                'No.4刀盘电动机电流(A)','No.5刀盘电动机电流(A)','No.6刀盘电动机电流(A)',
                'No.1刀盘电动机扭矩(kN*m)','No.2刀盘电动机扭矩(kN*m)','No.3刀盘电动机扭矩(kN*m)',
                'No.4刀盘电动机扭矩(kN*m)','No.5刀盘电动机扭矩(kN*m)','No.6刀盘电动机扭矩(kN*m)'];
            lineArr = ["C1EmC","C2EmC","C3EmC","C4EmC","C5EmC","C6EmC","C1Tor","C2Tor","C3Tor","C4Tor","C5Tor","C6Tor"];
            break;
        case "推进":
            columnArr = ['盾构千斤顶总推力(KN)',
                    '盾构分区压力（上）(MPa)','盾构分区压力（右）(MPa)','盾构分区压力（下）(MPa)','盾构分区压力（左）(MPa)',
                    '1号行程传感器','2号行程传感器','3号行程传感器','4号行程传感器',
                    'No.1盾构千斤顶速度(mm/min)','No.5盾构千斤顶速度(mm/min)','No.9盾构千斤顶速度(mm/min)','No.13盾构千斤顶速度(mm/min)'];
            lineArr = ['JN',
                    "J1P","J2P","J3P","J4P",
                    "J1S","J2S","J3S","J4S",
                    "J1V","J2V","J3V","J4V"
                    ];
            break;
        case "螺旋输送机":
            columnArr = ['螺旋机转速(min^-1)','螺旋机扭矩(KN·M)','螺旋机压力（前）(MPa)','螺旋机土压(中)(MPa)','螺旋机土压(后)(MPa)'];
            lineArr = ["SCRpm","SCTor","SCOilFP","SCOilP","SCOilBP"];
            break;
        case "铰接":
            columnArr = ['1#铰接千斤顶的行程','2#铰接千斤顶的行程','3#铰接千斤顶的行程','4#铰接千斤顶的行程'];
            lineArr = ["HJ1S","HJ2S","HJ3S","HJ4S"];
            break;
        case "盾尾密封":
            columnArr = [
                '1#前仓油脂压力(MPa)','2#前仓油脂压力(MPa)','3#前仓油脂压力(MPa)','4#前仓油脂压力(MPa)',
                '5#前仓油脂压力(MPa)','6#前仓油脂压力(MPa)','7#前仓油脂压力(MPa)','8#前仓油脂压力(MPa)',
                '1#后仓油脂压力(MPa)','2#后仓油脂压力(MPa)','3#后仓油脂压力(MPa)','4#后仓油脂压力(MPa)',
                '5#后仓油脂压力(MPa)','6#后仓油脂压力(MPa)','7#后仓油脂压力(MPa)','8#后仓油脂压力(MPa)'
                ];
            lineArr = [
                "BOF1P","BOF2P","BOF3P","BOF4P",
                "BOF5P","BOF6P","BOF7P","BOF8P",
                "BOB1P","BOB2P","BOB3P","BOB4P",
                "BOB5P","BOB6P","BOB7P","BOB8P"
                ];
            break;
        case "注浆":
            columnArr = [
                    '注浆注入口1压力(MPa)','注浆注入口12压力(MPa)','注浆注入口3压力(MPa)','注浆注入口4压力(MPa)',
                    '注浆管1注浆流量(L/min)','注浆管2注浆流量(L/min)','注浆管3注浆流量(L/min)','注浆管4注浆流量(L/min)',
                    '注浆管1注入浆量累计值(L)','注浆管2注入浆量累计值(L)','注浆管3注入浆量累计值(L)','注浆管4注入浆量累计值(L)'
                    ];
            lineArr = [
                "G1P","G2P","G3P","G4P",
                "G1LM","G2LM","G3LM","G4LM",
                "G1Total","G12Total","G3Total","G4Total"
                ];
            break;
        case "渣土改良":
            columnArr = [
                '膨润土管2#压力(MPa)','膨润土管2#压力(MPa)','膨润土管1#流量 (L/min)','膨润土管2#流量 (L/min)',
                '泡沫管1#压力(MPa)','泡沫管2#压力(MPa)','泡沫管3#压力(MPa)','泡沫管4#压力(MPa)'
                ];
            lineArr = [
                    "E1P","E2P","E1LM","E2LM",
                    "FO1P","FO2P","FO3P","FO4P"
                    ];
            break;
        case "工业水系统":
            columnArr = ['工业水温度(℃)','工业水液位','工业水压力(MPa)'];
            lineArr = ["waterT","waterBL","waterP"];
            break;
        case "液压站":
            columnArr = ['膨润土液位(%)','混合液罐液位(%)','原液液位(%)'];
            lineArr = ["BenTkBL","MixtureBL","StosteBL"];
            break;
        case "压缩空气":
            columnArr = ['空气管1#压力(MPa)','空气管2#压力(MPa)','空气管3#压力(MPa)','空气管4#压力(MPa)'];
            lineArr = ["Air1P","Air2P","Air3P","Air4P"];
            break;
        default:
            columnArr = ['开挖面土压（上）(MPa)','开挖面土压（下）(MPa)','开挖面土压（左）(MPa)','开挖面土压（右）(MPa)'];
            lineArr = ["SP1","SP2","SP3","SP4"];
            break;
    }
    data.columnArr = columnArr;
    data.lineArr = lineArr;
    return data;

}

function on_query_click(){
    var type = $("li[class='layui-this']").html();
    var data = getColumn(type);
    var columnArr = data.columnArr;
    var lineArr = data.lineArr;

    var queryParam = new Object();
    var xName = null;
    if($('#inqu_status-startTime').val() != "" || $('#inqu_status-endTime').val() != ""){
        xName = "时间";
        queryParam.type="time";
        var startTimeStr = $('#inqu_status-startTime').val() == "" ? null : $('#inqu_status-startTime').val();
        var endTimeStr = $('#inqu_status-endTime').val() == "" ? null : $('#inqu_status-endTime').val();
        if(startTimeStr == endTimeStr){
            alertDiv("提示","起始与结束时间不可以相同！");
            return;
        }
        var startTime = new Date(startTimeStr);
        var endTime = new Date(endTimeStr);
        getTimeData(type,columnArr,lineArr,startTime.getTime(),endTime.getTime());
        return;
    }
    if("-1" != $("select[name='ringNum']").val()){
        xName = "管理行程";
        queryParam.type="ring";
        queryParam.MR_Ring_Num = parseInt($("select[name='ringNum']").val());
        queryParam.collectorName = $.cookie("selected_collection");
    }else if($("#inqu_status-startMileage").val() != "" || $("#inqu_status-endMileage").val() != ""){
        xName = "里程";
        queryParam.type="mileage";
        queryParam.startMileage = $('#inqu_status-startMileage').val() == "" ? null : parseFloat($('#inqu_status-startMileage').val());
        queryParam.endMileage = $('#inqu_status-endMileage').val() == "" ? null : parseFloat($('#inqu_status-endMileage').val());
        queryParam.collectorName = $.cookie("selected_collection");
    }else{
        alertDiv("提示","请选择查询条件");
        return;
    }
    queryParam.projectId = parseInt($.cookie("selected_id"));
    queryParam.getRing = "2";
    queryParam.lineArr = lineArr;

    ShenXianYiZhi(type,columnArr,lineArr,queryParam,xName);
}

function ShenXianYiZhi(type,columnArr,lineArr,condition,xName){
    var result = null;

    $.ajax({
        "url":toolkitPath+"/raising/forward/construction/historicalCurve/getData.do",
        "type":"get",
        "data":{"ajaxParam":JSON.stringify(condition)},
        "async":false,
        "success":function (data) {
            if(data.status == '0'){
                result = data;
                if("1" == condition.getRing){//如果是第一次查询，设置查询条件区间

                    $("#minMileage").html("最小:"+data.minMileage );
                    $("#maxMileage").html("最大:"+data.maxMileage);
                    var ringArr = data.ringNums;
                    var str = "";
                    for(var i=0;i<ringArr.length;i++){
                        str += "<option value='"+ringArr[i]+"' >"+ringArr[i]+"</option>";
                    }
                    $("select[name='ringNum']").append(str);
                    $("select[name='ringNum']").val(data.currentRingNum);
                }
            }
        }
    })

    showChart(type,columnArr,lineArr,xName,result);
}

function showChart(type,columnArr,lineArr,xName,result){
    var color = [
        "#0AFF00","#30AB0D","#65BC90","#6AAB87",
        "#0D17FF","#13C7FF","#FF6011","#23FDE2",
        "#FF00E2","#CA28FF","#F4928F","#FF9DF5",
        "#FF0F09","#F47816","#FD976F","#FFA9AC"
    ];
    var legendData = [];
    var legend = {
        "data":legendData,
        "selected":{},
        right:0,
        x:1100,
        y:50,
        orient: 'vertical',
        textStyle:{
            color:'white'
        }

    };

    var series = [];
    color.length = columnArr.length;
    for(var i=0;i<columnArr.length;i++){
        //var legendTemp = {"name":};//,"textStyle":{"color":'#FFFFFF'}
        legendData.push(columnArr[i]);
        if(i == 0){//设置只显示一个折线图，其他折线图默认隐藏
            legend.selected[columnArr[i]] = true;
        }else{
            legend.selected[columnArr[i]] = false;
        }
        var seriesTemp = {};
        seriesTemp.name = columnArr[i];
        seriesTemp.type = 'line';
        if(result!= null && result[lineArr[i]] != null){
            seriesTemp.data =  result[lineArr[i]];
        }else{
            seriesTemp.data = [];
        }
        seriesTemp.itemStyle = {"normal" : {"lineStyle":{"color":color[i]}}};
        series.push(seriesTemp);
    }

    var option = {
        textStyle:{
            color:'#FFFFFF'
        },
        title: {
            text: type,
            textStyle:{
                color:'#FFFFFF'
            }
        },
        grid:{
            right:200
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
        color:color,
        legend: legend,
        xAxis: {
            type: 'category',
            name:xName,
            data: result == null ? [] : result.xAxis,
            nameTextStyle:{
                color:'#FFFFFF'
            },
            axisLine:{
                lineStyle:{
                    color:'#FFFFFF'
                }
            }
        },
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

    var myChart = echarts.init(document.getElementById("myCharts"));
    myChart.clear();
    myChart.setOption(option);
}

function timeKeyUp(obj){
    var a = obj.value;
    if(a.length > 0){//如果其中有值，则设另外三个输入框只读

    }
}

function ringNumKeyChange(obj){
    var a = obj.value;
    if(a != "-1"){//如果其中有值，则设另外三个输入框只读
        $('#inqu_status-startTime').val("");
        $('#inqu_status-endTime').val("");
        $('#inqu_status-startMileage').val("");
        $('#inqu_status-endMileage').val("");
    }
}

function mileageKeyUp(obj){
    var a = obj.value;
    if(a.length > 0){//如果其中有值，则设另外三个输入框只读
        $('#inqu_status-startTime').val("");
        $('#inqu_status-endTime').val("");
        $("select[name='ringNum']").val("-1");
    }
}

function init(){
    var a = document.getElementById("inqu_status-startTime").offsetLeft;

    $("#minMileage").width($('#inqu_status-startMileage').width());
}

function initTime(){
    $("#historyTime").hide();
    $("#historyOperatorButton").hide();
    $("#historyGridDiv").hide();



    $('#inqu_status-startTime').daterangepicker({
        "singleDatePicker": true,
        "showDropdowns": true,
        "autoUpdateInput": false,
        //"startDate": new Date(),
        "timePicker": true,
        "timePicker24Hour": true,
        "timePickerSeconds": true,
        "locale":{
            "format": "YYYY-MM-DD HH:mm:ss",
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
        "autoUpdateInput": false,
        //"startDate": new Date(),
        "timePicker": true,
        "timePicker24Hour": true,
        "timePickerSeconds": true,
        "locale":{
            "format": "YYYY-MM-DD HH:mm:ss",
            "applyLabel": "确定",
            "cancelLabel": "取消",
            "weekLabel": "W",
            "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
            "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"]
        }
    }, function(start, end, label) {
    });

    $('#inqu_status-startTime').on('apply.daterangepicker', function(ev, picker) {
        $('#inqu_status-startTime').val(picker.startDate.format('YYYY-MM-DD HH:mm:ss '));
        $("select[name='ringNum']").val("-1");
        $('#inqu_status-startMileage').val("");
        $('#inqu_status-endMileage').val("");
    });
    $('#inqu_status-endTime').on('apply.daterangepicker', function(ev, picker) {
        $('#inqu_status-endTime').val(picker.startDate.format('YYYY-MM-DD HH:mm:ss '));
        $("select[name='ringNum']").val("-1");
        $('#inqu_status-startMileage').val("");
        $('#inqu_status-endMileage').val("");
    });
}

function getTimeData(type,columnArr,lineArr,startTime,endTime){
    //先获取instanceId和propertyId
    var idsArr = getInstanceId(lineArr);
    if(idsArr.length < 1){
        return;
    }
    var param = {};

    var result = [];
    (function(result,len){
        for(var i=0;i<idsArr.length;i++){
            var worker = new Worker("historicalCurve2.js");
            getRecordURL = "https://bd.sh-raising.com/tsdbrest/api/record/range/"  + idsArr[i].instance_id + "/" + idsArr[i].propertyId;
            param.tagCode = idsArr[i].instance_name.substring(idsArr[i].instance_name.indexOf("_")+1);
            param.getRecordURL = getRecordURL;
            param.startTime = startTime;
            param.endTime = endTime;

            worker.postMessage(JSON.stringify(param));//向worker发送数据
            worker.onmessage = function(evt){           //接收worker传过来的数据函数
                var str = evt.data;
                var showData =  JSON.parse(str);
                var code = showData.code;
                var timArr = showData.timArr;
                if(timArr != null && timArr.length > 0 ){
                    result.xAxis;
                    if(result.xAxis != undefined && result.xAxis.length > timArr.length){
                    }else{
                        result.xAxis = timArr;
                    }
                }
                if(showData.dataArr.length > 0){
                    result[code] = showData.dataArr;
                }
                len--;
                if(len == 0){
                    showChart(type,columnArr,lineArr,"时间",result);
                }
            }
        }
    })(result,lineArr.length);

}

function getInstanceId(lineArr){
    var result = [];
    var instancesArr = [];
    for(var i =0;i<lineArr.length;i++){
        var temp = lineArr[i];
        var instanceName = $.cookie("selected_collection") + "_" + temp;
        var instanceTemp = {"instance_name": instanceName};
        instancesArr.push(instanceTemp);
    }

    var getInstanceURL = "https://bd.sh-raising.com/tsdbrest/api/instance";
    $.ajax({
        url: getInstanceURL,
        type: "post",
        data: JSON.stringify(instancesArr),
        contentType: 'application/json;charset=UTF-8',
        async: false,
        dataType: "json",
        beforeSend: function(req) {
            req.setRequestHeader('Authorization', "Basic " + new Base64().encode('tsdbDataUser@raising' + ":" + '123456'));
        },
        success: function (data) {
            if (Number(data.errcode) == 0) {
                var dataArr = data.records;
                for(var i =0;i<dataArr.length;i++){
                    var dataTemp = dataArr[i];
                    var resultTemp = {};
                    resultTemp.instance_name = dataTemp.instance_name;
                    resultTemp.instance_id = dataTemp.instance_id;

                    var propertiesArr = dataTemp.properties;
                    if(propertiesArr.length > 0) {
                        resultTemp.propertyId = propertiesArr[0].id;
                        result.push(resultTemp);
                    }
                }
            }
        },
        error:function(){

        }
    })

    return result;

}


function Base64() {

    // private property
    _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    // public method for encoding
    this.encode = function(input) {
        var output = "";
        var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
        var i = 0;
        input = _utf8_encode(input);
        while (i < input.length) {
            chr1 = input.charCodeAt(i++);
            chr2 = input.charCodeAt(i++);
            chr3 = input.charCodeAt(i++);
            enc1 = chr1 >> 2;
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
            enc4 = chr3 & 63;
            if (isNaN(chr2)) {
                enc3 = enc4 = 64;
            } else if (isNaN(chr3)) {
                enc4 = 64;
            }
            output = output +
                _keyStr.charAt(enc1) + _keyStr.charAt(enc2) +
                _keyStr.charAt(enc3) + _keyStr.charAt(enc4);
        }
        return output;
    }

    // public method for decoding
    this.decode = function(input) {
        var output = "";
        var chr1, chr2, chr3;
        var enc1, enc2, enc3, enc4;
        var i = 0;
        input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
        while (i < input.length) {
            enc1 = _keyStr.indexOf(input.charAt(i++));
            enc2 = _keyStr.indexOf(input.charAt(i++));
            enc3 = _keyStr.indexOf(input.charAt(i++));
            enc4 = _keyStr.indexOf(input.charAt(i++));
            chr1 = (enc1 << 2) | (enc2 >> 4);
            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
            chr3 = ((enc3 & 3) << 6) | enc4;
            output = output + String.fromCharCode(chr1);
            if (enc3 != 64) {
                output = output + String.fromCharCode(chr2);
            }
            if (enc4 != 64) {
                output = output + String.fromCharCode(chr3);
            }
        }
        output = _utf8_decode(output);
        return output;
    }

    // private method for UTF-8 encoding
    _utf8_encode = function(string) {
        string = string.replace(/\r\n/g, "\n");
        var utftext = "";
        for (var n = 0; n < string.length; n++) {
            var c = string.charCodeAt(n);
            if (c < 128) {
                utftext += String.fromCharCode(c);
            } else if ((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            } else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }

        }
        return utftext;
    }

    // private method for UTF-8 decoding
    _utf8_decode = function(utftext) {
        var string = "";
        var i = 0;
        var c = c1 = c2 = 0;
        while (i < utftext.length) {
            c = utftext.charCodeAt(i);
            if (c < 128) {
                string += String.fromCharCode(c);
                i++;
            } else if ((c > 191) && (c < 224)) {
                c2 = utftext.charCodeAt(i + 1);
                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                i += 2;
            } else {
                c2 = utftext.charCodeAt(i + 1);
                c3 = utftext.charCodeAt(i + 2);
                string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                i += 3;
            }
        }
        return string;
    }
}















