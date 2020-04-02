

$(function(){
    initTime();
    //初始化下拉框
    $('.chosen-select').chosen({
        allow_single_deselect : true
    });
    $(window).on('resize.chosen', function() {
        $('.chosen-select').next().css({
            'width' : '210px'
        });
    }).trigger('resize.chosen');


    var projectId = $.cookie("selected_id");
    var inqu_status = {"projectId":projectId,"collectorName":$.cookie("selected_collection")};
    tableConvert("ring",inqu_status);
})


function download(type){
    var obj = {};
    obj.curRowNum = 1000;
    obj.downloadColumnDesc = null;
    obj.curPage = 1;
    obj.columnArr = null;
    obj.downloadColumn = null;
    obj.columnImageMap = {};
    obj.inqu_status = {"projectId":$.cookie("selected_id"),"collectorName":$.cookie("selected_collection")};
    obj.type = type;
    obj.columnSelectMap = {};
    obj.mts_column = [];

    if(type == "ring"){
        obj.downloadColumnDesc = "环号";
        obj.downloadColumn = "MR_Ring_Num";
        var temp = getColNamesAndModel(type);
        removeArrVal(temp.column,"projectId");
        obj.columnArr = temp.column;
        $("input[name='ring-checkbox']:checked").each(function(){
            var a = $(this).next("span").html().trim();;
            obj.mts_column.push(a);
        })
        var minRing = $("#startRingNum").val();
        var maxRing = $("#endRingNum").val();
        if(isInt(minRing) && isInt(maxRing)){
            obj.inqu_status.startRingNum = parseInt(minRing);
            obj.inqu_status.endRingNum = parseInt(maxRing);
        }else{
            alertDiv("提示","起始与结束环不可以为空");
            return;
        }
    }else if(type == "time"){
        obj.downloadColumnDesc = "时间";
        obj.downloadColumn = "dt";
        var temp = getColNamesAndModel(type);
        removeArrVal(temp.column,"projectId");
        obj.columnArr = temp.column;
        $("input[name='time-checkbox']:checked").each(function(){
            var a = $(this).next("span").html().trim();;
            obj.mts_column.push(a);
        })
        var startTime = $('#inqu_status-startTime').val().trim();
        var endTime = $('#inqu_status-endTime').val().trim();
        if(isNullOrEmptyOrUndefiend(startTime) || isNullOrEmptyOrUndefiend(endTime)){
            alertDiv("提示","起始与结束时间不可以为空");
            return;
        }else{
            obj.inqu_status.startTime = startTime;
            obj.inqu_status.endTime = endTime;
        }
    }else if(type == "mileage"){
        obj.downloadColumnDesc = "里程";
        obj.downloadColumn = "MR_Des_A1Mileage";
        var temp = getColNamesAndModel(type);
        removeArrVal(temp.column,"projectId");
        obj.columnArr = temp.column;
        $("input[name='mileage-checkbox']:checked").each(function(){
            var a = $(this).next("span").html().trim();;
            obj.mts_column.push(a);
        })

        var startMileage = $("#startMileage").val();
        var endMileage = $("#endMileage").val();
        if(isNumber(startMileage) && isNumber(endMileage)){
            obj.inqu_status.startMileage = Number(startMileage);
            obj.inqu_status.endMileage = Number(endMileage);
        }else {
            alertDiv("提示","起始与结束里程不可以为空");
            return;
        }

    }else if(type == "trip"){
        obj.downloadColumnDesc = "管理行程";
        obj.downloadColumn = "CurMS";
        var temp = getColNamesAndModel(type);
        removeArrVal(temp.column,"projectId");
        obj.columnArr = temp.column;
        $("input[name='trip-checkbox']:checked").each(function(){
            var a = $(this).next("span").html().trim();;
            obj.mts_column.push(a);
        })
        var ringNum = $("#ringNum").val();
        if(isInt(ringNum)){
            obj.inqu_status.MR_Ring_Num = parseInt(ringNum);
        }else{
            alertDiv("提示","请选择环号");
            return;
        }
    }



    var str = JSON.stringify(obj);
    window.location.href = toolkitPath + "/raising/forward/construction/dataQuery/downExcel.do?ajaxParam="+encodeURIComponent(encodeURIComponent(str));


    var count = "请等待：0%";
    var customElement = $("<div>", {
        id: "countdown",
        css: { "font-size" : "50px" }, text: count
    });
    //$("#father").busyLoad("show", {containerClass:"zIndexLoad",custom: customElement});
   /* window.setTimeout(function(){
        var timer = window.setInterval(function(){
            $.ajax({
                type:'get',
                dataType:'json',
                url: toolkitPath+"/raising/forward/construction/dataQuery/getProgress.do",
                success: function(data) {
                    if(data.status != "-1"){
                        console.log(data.progress);
                        count = "请等待："+data.progress;
                        customElement.text(count);

                        if(data.progress == "100%"){
                            window.clearInterval(timer);
                            $("#father").busyLoad("hide");
                        }
                    }else{
                        window.clearInterval(timer);
                        $("#father").busyLoad("hide");
                    }
                },
                error:function(data){
                    window.clearInterval(timer);
                    $("#father").busyLoad("hide");
                }
            });
        },1000);
    },200);*/

}


function on_query_click(tableType){
    var projectId = $.cookie("selected_id");
    var inqu_status = {"projectId":projectId,"collectorName":$.cookie("selected_collection")};
    if(tableType == "ring"){
        var minRing = $("#startRingNum").val();
        var maxRing = $("#endRingNum").val();
        if(isInt(minRing)){
            inqu_status.startRingNum = parseInt(minRing);
        }
        if(isInt(maxRing)){
            inqu_status.endRingNum = parseInt(maxRing);
        }
        tableConvert(tableType,inqu_status);
    }else if(tableType == "time"){
        var startTime = $('#inqu_status-startTime').val().trim();
        var endTime = $('#inqu_status-endTime').val().trim();
        if(!isNullOrEmptyOrUndefiend(startTime)){
            inqu_status.startTime = startTime;
        }
        if(!isNullOrEmptyOrUndefiend(endTime)){
            inqu_status.endTime = endTime;
        }
        tableConvert(tableType,inqu_status);
    }else if(tableType == "mileage"){
        var startMileage = $("#startMileage").val();
        var endMileage = $("#endMileage").val();
        if(isNumber(startMileage)){
            inqu_status.startMileage = Number(startMileage);
        }
        if(isNumber(endMileage)){
            inqu_status.endMileage = Number(endMileage);
        }
        tableConvert(tableType,inqu_status);
    }else if(tableType == "trip"){
        var ringNum = $("#ringNum").val();
        if(isInt(ringNum)){
            inqu_status.MR_Ring_Num = parseInt(ringNum);
        }
        tableConvert(tableType,inqu_status);
    }

}



function tableConvert(tableType,inqu_status){
    inqu_status = inqu_status == undefined ? {"projectId":$.cookie("selected_id"),"collectorName":$.cookie("selected_collection")} :inqu_status;
    var data = getColNamesAndModel(tableType);
    var colNames = data.names;
    var colModel = data.models;
    var gridId = null;
    var queryareaId = null;
    var alertDivId = null;
    var columnArr =data.column;

    if(tableType == "ring"){
        gridId = "tbmRecord1";
        queryareaId = "queryarea1";
        alertDivId = "alertdiv1";
    }else if(tableType == "time"){
        gridId = "tbmRecord2";
        queryareaId = "queryarea2";
        alertDivId = "alertdiv2";
    }else if(tableType == "mileage"){
        gridId = "tbmRecord3";
        queryareaId = "queryarea3";
        alertDivId = "alertdiv3";
    }else if(tableType == "trip"){
        gridId = "tbmRecord4";
        queryareaId = "queryarea4";
        alertDivId = "alertdiv4";
    }
    var parent = $("#"+gridId).parent();
    $(parent).html("");
    $(parent).append("<div id='"+gridId+"'></div>");
    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'], function () {
        $("#"+queryareaId).bxdiv();

        var gridOption = {
            primaryRowKey: "projectId",
            colNames: colNames,
            colModel:colModel,
            sortable: false,
            height:315,
            caption: false
        };

        var option = {
            queryParam: {"type":tableType,"inqu_status":inqu_status,"columnArr":columnArr},
            dataPattern: "url",
            url: "/raising/forward/construction/dataQuery/getRows.do",
            callBackFunct:function(data){
                fillCondition(tableType,data,inqu_status);
            },
            gridOption: gridOption,
            showMsgOpt: {
                showMsgId: alertDivId+""
            },
            navGridOption: {
                download: false,
                downloadParam: {
                    downloadUrl: "/raising/forward/construction/dataQuery/downExcel.do"
                }
            }
        };
        $("#"+gridId).bxgrid(option);
    });
}


function getColNamesAndModel(type){
    var namesArr = [];
    var names = null;
    var models = null;
    var column = [];

    switch (type){
        case "time":
            $("input[name='time-checkbox']:checked").each(function(){
                var a = $(this).next("span").html().trim();;
                namesArr.push(a);
            })
            names = ['projectId', '时间'];
            models = [
                {name:"projectId",width: 30,readOnly:true,hidden:true},
                {name:"dt",width: 30,readOnly:true}
            ];
            column = ['projectId', 'dt'];
            break;
        case "ring":
            $("input[name='ring-checkbox']:checked").each(function(){
                var a = $(this).next("span").html().trim();;
                namesArr.push(a);
            })
            names = ['projectId', '环号'];
            models = [
                {name:"projectId",width: 30,readOnly:true,hidden:true},
                {name:"MR_Ring_Num",width: 30,readOnly:true}
            ];
            column = ['projectId', 'MR_Ring_Num'];
            break;
        case "trip":
            $("input[name='trip-checkbox']:checked").each(function(){
                var a = $(this).next("span").html().trim();;
                namesArr.push(a);
            })
            names = ['projectId', '行程'];
            models = [
                {name:"projectId",width: 30,readOnly:true,hidden:true},
                {name:"CurMS",width: 30,readOnly:true}
            ];
            column = ['projectId', 'CurMS'];
            break;
        case "mileage":
            $("input[name='mileage-checkbox']:checked").each(function(){
                var a = $(this).next("span").html().trim();;
                namesArr.push(a);
            })
            names = ['projectId', '里程'];
            models = [
                {name:"projectId",width: 30,readOnly:true,hidden:true},
                {name:"MR_Des_A1Mileage",width: 30,readOnly:true}
            ];
            column = ['projectId', 'MR_Des_A1Mileage'];
            break;
        default:
            $("input[name='ring-checkbox']:checked").each(function(){
                var a = $(this).next("span").html().trim();;
                namesArr.push(a);
            })
            names = ['projectId', '时间'];
            models = [
                {name:"projectId",width: 30,readOnly:true,hidden:true},
                {name:"dt",width: 30,readOnly:true}
            ];
            column = ['projectId', 'dt'];
            break;
    }

    for(var i=0;i<namesArr.length;i++){
        var temp = namesArr[i];
        var data = piPei(temp);
        names.push.apply(names,data.columnArr);
        models.push.apply(models,data.lineArr);
        column.push.apply(column,data.column);
    }
    var data= {"names":names,"models":models,"column":column};
    return data;

}

function piPei(type){
    var columnArr = null;
    var lineArr = null;
    var column = null;
    switch (type){
        case "土压":
            columnArr = ['开挖面土压（上）(MPa)','开挖面土压（下）(MPa)','开挖面土压（左）(MPa)','开挖面土压（右）(MPa)'];
            lineArr = [
                {name:"SP1",width: 30,readOnly:true},
                {name:"SP2",width: 30,readOnly:true},
                {name:"SP3",width: 30,readOnly:true},
                {name:"SP4",width: 30,readOnly:true}
                ];
            column = ["SP1","SP2","SP3","SP4"];
            break;
        case "刀盘":
            columnArr = ['刀盘转速(rpm)','刀盘扭距(kNm)'];
            lineArr = [
                {name:"CRpm",width: 30,readOnly:true},
                {name:"CTor",width: 30,readOnly:true}
                ];
            column = ["CRpm","CTor"];
            break;
        case "主驱动电机":
            columnArr = ['No.1刀盘电动机电流(A)','No.2刀盘电动机电流(A)','No.3刀盘电动机电流(A)',
                'No.4刀盘电动机电流(A)','No.5刀盘电动机电流(A)','No.6刀盘电动机电流(A)',
                'No.1刀盘电动机扭矩(kN*m)','No.2刀盘电动机扭矩(kN*m)','No.3刀盘电动机扭矩(kN*m)',
                'No.4刀盘电动机扭矩(kN*m)','No.5刀盘电动机扭矩(kN*m)','No.6刀盘电动机扭矩(kN*m)'];
            lineArr = [
                {name:"C1EmC",width: 30,readOnly:true},
                {name:"C2EmC",width: 30,readOnly:true},
                {name:"C3EmC",width: 30,readOnly:true},
                {name:"C4EmC",width: 30,readOnly:true},
                {name:"C5EmC",width: 30,readOnly:true},
                {name:"C6EmC",width: 30,readOnly:true},
                {name:"C1Tor",width: 30,readOnly:true},
                {name:"C2Tor",width: 30,readOnly:true},
                {name:"C3Tor",width: 30,readOnly:true},
                {name:"C4Tor",width: 30,readOnly:true},
                {name:"C5Tor",width: 30,readOnly:true},
                {name:"C6Tor",width: 30,readOnly:true}
            ];
            column = ["C1EmC","C2EmC","C3EmC","C4EmC","C5EmC","C6EmC","C1Tor","C2Tor","C3Tor","C4Tor","C5Tor","C6Tor"];
            break;
        case "推进":
            columnArr = ['盾构千斤顶总推力(KN)',
                '盾构分区压力（上）(MPa)','盾构分区压力（右）(MPa)','盾构分区压力（下）(MPa)','盾构分区压力（左）(MPa)',
                '1号行程传感器','2号行程传感器','3号行程传感器','4号行程传感器',
                'No.1盾构千斤顶速度(mm/min)','No.5盾构千斤顶速度(mm/min)','No.9盾构千斤顶速度(mm/min)','No.13盾构千斤顶速度(mm/min)'];
            lineArr = [
                {name:"JN",width: 30,readOnly:true},
                {name:"J1P",width: 30,readOnly:true},
                {name:"J2P",width: 30,readOnly:true},
                {name:"J3P",width: 30,readOnly:true},
                {name:"J4P",width: 30,readOnly:true},
                {name:"J1S",width: 30,readOnly:true},
                {name:"J2S",width: 30,readOnly:true},
                {name:"J3S",width: 30,readOnly:true},
                {name:"J4S",width: 30,readOnly:true},
                {name:"J1V",width: 30,readOnly:true},
                {name:"J2V",width: 30,readOnly:true},
                {name:"J3V",width: 30,readOnly:true},
                {name:"J4V",width: 30,readOnly:true}
            ];
            column = ['JN',
                "J1P","J2P","J3P","J4P",
                "J1S","J2S","J3S","J4S",
                "J1V","J2V","J3V","J4V"
            ];
            break;
        case "螺旋输送机":
            columnArr = ['螺旋机转速(min^-1)','螺旋机扭矩(KN·M)','螺旋机压力（前）(MPa)','螺旋机土压(中)(MPa)','螺旋机土压(后)(MPa)'];
            lineArr = [
                {name:"SCRpm",width: 30,readOnly:true},
                {name:"SCTor",width: 30,readOnly:true},
                {name:"SCOilFP",width: 30,readOnly:true},
                {name:"SCOilP",width: 30,readOnly:true},
                {name:"SCOilBP",width: 30,readOnly:true}
            ];
            lineArr = ["SCRpm","SCTor","SCOilFP","SCOilP","SCOilBP"];
            break;
        case "铰接":
            columnArr = ['1#铰接千斤顶的行程','2#铰接千斤顶的行程','3#铰接千斤顶的行程','4#铰接千斤顶的行程'];
            lineArr =[
                {name:"HJ1S",width: 30,readOnly:true},
                {name:"HJ2S",width: 30,readOnly:true},
                {name:"HJ3S",width: 30,readOnly:true},
                {name:"HJ4S",width: 30,readOnly:true}
            ];
            column = ["HJ1S","HJ2S","HJ3S","HJ4S"];
            break;
        case "盾尾密封":
            columnArr = [
                '1#前仓油脂压力(MPa)','2#前仓油脂压力(MPa)','3#前仓油脂压力(MPa)','4#前仓油脂压力(MPa)',
                '5#前仓油脂压力(MPa)','6#前仓油脂压力(MPa)','7#前仓油脂压力(MPa)','8#前仓油脂压力(MPa)',
                '1#后仓油脂压力(MPa)','2#后仓油脂压力(MPa)','3#后仓油脂压力(MPa)','4#后仓油脂压力(MPa)',
                '5#后仓油脂压力(MPa)','6#后仓油脂压力(MPa)','7#后仓油脂压力(MPa)','8#后仓油脂压力(MPa)'
            ];
            lineArr = [
                {name:"BOF1P",width: 30,readOnly:true},
                {name:"BOF2P",width: 30,readOnly:true},
                {name:"BOF3P",width: 30,readOnly:true},
                {name:"BOF4P",width: 30,readOnly:true},
                {name:"BOF5P",width: 30,readOnly:true},
                {name:"BOF6P",width: 30,readOnly:true},
                {name:"BOF7P",width: 30,readOnly:true},
                {name:"BOF8P",width: 30,readOnly:true},
                {name:"BOB1P",width: 30,readOnly:true},
                {name:"BOB2P",width: 30,readOnly:true},
                {name:"BOB3P",width: 30,readOnly:true},
                {name:"BOB4P",width: 30,readOnly:true},
                {name:"BOB5P",width: 30,readOnly:true},
                {name:"BOB6P",width: 30,readOnly:true},
                {name:"BOB7P",width: 30,readOnly:true},
                {name:"BOB8P",width: 30,readOnly:true}
            ];
            column = [
                "BOF1P","BOF2P","BOF3P","BOF4P",
                "BOF5P","BOF6P","BOF7P","BOF8P",
                "BOB1P","BOB2P","BOB3P","BOB4P",
                "BOB5P","BOB6P","BOB7P","BOB8P"
            ];
            break;
        case "注浆":
            columnArr = [
                '注浆注入口1压力(MPa)','注浆注入口2压力(MPa)','注浆注入口3压力(MPa)','注浆注入口4压力(MPa)',
                '注浆管1注浆流量(L/min)','注浆管2注浆流量(L/min)','注浆管3注浆流量(L/min)','注浆管4注浆流量(L/min)',
                '注浆管1注入浆量累计值(L)','注浆管2注入浆量累计值(L)','注浆管3注入浆量累计值(L)','注浆管4注入浆量累计值(L)'
            ];
            lineArr = [
                {name:"G1P",width: 30,readOnly:true},
                {name:"G2P",width: 30,readOnly:true},
                {name:"G3P",width: 30,readOnly:true},
                {name:"G4P",width: 30,readOnly:true},
                {name:"G1LM",width: 30,readOnly:true},
                {name:"G2LM",width: 30,readOnly:true},
                {name:"G3LM",width: 30,readOnly:true},
                {name:"G4LM",width: 30,readOnly:true},
                {name:"G1Total",width: 30,readOnly:true},
                {name:"G2Total",width: 30,readOnly:true},
                {name:"G3Total",width: 30,readOnly:true},
                {name:"G4Total",width: 30,readOnly:true}
            ];
            lineArr = [
                "G1P","G2P","G3P","G4P",
                "G1LM","G2LM","G3LM","G4LM",
                "G1Total","G2Total","G3Total","G4Total"
            ];
            break;
        case "渣土改良":
            columnArr = [
                '1号膨润土压力(MPa)','2号膨润土压力(MPa)','膨润土管1#流量 (L/min)','膨润土管2#流量 (L/min)',
                '泡沫管1#压力(MPa)','泡沫管2#压力(MPa)','泡沫管3#压力(MPa)','泡沫管4#压力(MPa)'
            ];
            lineArr = [
                {name:"E1P",width: 30,readOnly:true},
                {name:"E2P",width: 30,readOnly:true},
                {name:"E1LM",width: 30,readOnly:true},
                {name:"E2LM",width: 30,readOnly:true},
                {name:"FO1P",width: 30,readOnly:true},
                {name:"FO2P",width: 30,readOnly:true},
                {name:"FO3P",width: 30,readOnly:true},
                {name:"FO4P",width: 30,readOnly:true}
            ];
            lineArr = [
                "E1P","E2P","E1LM","E2LM",
                "FO1P","FO2P","FO3P","FO4P"
            ];
            break;
        case "工业水系统":
            columnArr = ['工业水温度(℃)','工业水液位','工业水压力(MPa)'];
            lineArr = [
                {name:"waterT",width: 30,readOnly:true},
                {name:"waterBL",width: 30,readOnly:true},
                {name:"waterP",width: 30,readOnly:true}
            ];
            lineArr = ["waterT","waterBL","waterP"];
            break;
        case "液压站":
            columnArr = ['膨润土液位(%)','混合液罐液位(%)','原液液位(%)'];
            lineArr =[
                {name:"BenTkBL",width: 30,readOnly:true},
                {name:"MixtureBL",width: 30,readOnly:true},
                {name:"StosteBL",width: 30,readOnly:true}
            ];
            lineArr = ["BenTkBL","MixtureBL","StosteBL"];
            break;
        case "压缩空气":
            columnArr = ['空气管1#压力(MPa)','空气管2#压力(MPa)','空气管3#压力(MPa)','空气管4#压力(MPa)'];
            lineArr = [
                {name:"Air1P",width: 30,readOnly:true},
                {name:"Air2P",width: 30,readOnly:true},
                {name:"Air3P",width: 30,readOnly:true},
                {name:"Air4P",width: 30,readOnly:true}
                ];
            lineArr = ["Air1P","Air2P","Air3P","Air4P"];
            break;
        case "环号":
            columnArr = ['环号'];
            lineArr = [{name:"MR_Ring_Num",width: 30,readOnly:true}];
            column = ["MR_Ring_Num"];
            break;
        default:
            columnArr = ['开挖面土压（上）(MPa)','开挖面土压（下）(MPa)','开挖面土压（左）(MPa)','开挖面土压（右）(MPa)'];
            lineArr = [
                {name:"SP1",width: 30,readOnly:true},
                {name:"SP2",width: 30,readOnly:true},
                {name:"SP3",width: 30,readOnly:true},
                {name:"SP4",width: 30,readOnly:true}
            ];
            column = ["SP1","SP2","SP3","SP4"];
            break;
    }
    var data= {"columnArr":columnArr,"lineArr":lineArr,"column":column};
    return data;
}

/*
* 填充条件数据
* */
function fillCondition(tableType,data,inqu_status){
    if(tableType == "ring"){
        var max = data.max;
        var min = data.min;
        $("#minRing").html("最小:"+ (isInt(data.minRingNum)  == false ? "" : data.minRingNum) );
        $("#maxRing").html("最大:"+ (isInt(data.maxRingNum) == false ?"" : data.maxRingNum ));

    }else if(tableType == "time"){
        var max = data.max;
        var min = data.min;
        $("#minTime").html("最早:"+ (isNullOrEmptyOrUndefiend(min)  == true ?"" : min) );
        $("#maxTime").html("最晚:"+ (isNullOrEmptyOrUndefiend(max) ==  true ?"" : max));
    }else if(tableType == "mileage"){
        var max = data.max;
        var min = data.min;
        $("#minMileage").html("最小:"+ (isNullOrEmptyOrUndefiend(min)  == true ?"" : min) );
        $("#maxMileage").html("最大:"+(isNullOrEmptyOrUndefiend(max) == true ?"" : max));
    }else if(tableType == "trip"){
        if(data.status != "0"){
            var option = "<option value='' >未选择</option>";
            $("#ringNum").html("");
            $("#ringNum").append(option);
            $('.chosen-select').trigger("chosen:updated");
            return;
        }
        var checkedNum = inqu_status.MR_Ring_Num == undefined ? "" : inqu_status.MR_Ring_Num;
        var ringNums = data.ringNums;
        var option = "<option value='' >未选择</option>"
        for(var i=0;i<ringNums.length;i++){
            var num = ringNums[i];
            if(checkedNum == (num+"")){
                option += "<option selected value='"+num+"' >"+num+"</option>";
            }else{
                option += "<option value='"+num+"' >"+num+"</option>";
            }

        }
        $("#ringNum").html("");
        $("#ringNum").append(option);
        $('.chosen-select').trigger("chosen:updated")
    }
}


function initTime(){

    $('#inqu_status-startTime').daterangepicker({
        "singleDatePicker": true,
        "showDropdowns": true,
        "autoUpdateInput": false,
        //"startDate": '2019-10-10 10:00:00',
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
        "startDate": new Date(),
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



