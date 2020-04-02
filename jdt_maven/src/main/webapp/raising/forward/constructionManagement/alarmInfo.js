
$(document).ready(function () {
    initTime();
    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'], function () {
        $("#queryarea").bxdiv();
        var queryParam = {"inqu_status":{"projectId":$.cookie("selected_id"),"alarmTypesArr":[1,2,3]}};
        var gridOption = {
            primaryRowKey: "alarmId",
            caption: "报警信息",
            colNames: ['alarmId','盾构机名称', '报警信息', '报警时间', '报警类别'],
            colModel: [
                {
                    name: 'alarmId',
                    index: 'alarmId',
                    width: 5,
                    hidden: true,
                    forbidCopy: false
                },{name:'tbmName', index:'tbmName',width: 30},
                {name:'alarmStr', index:'alarmStr',width: 30},
                {name:'startTime', index:'startTime',width: 30},
                {name:'alarmType', index:'alarmType',width: 30}],
            sortable: false,
            height:315
        };

        var option = {
            queryParam: queryParam,
            dataPattern: "url",
            url: "/raising/forward/construction/alarmInfo/getRows.do",
            gridOption: gridOption,
            showMsgOpt: {
                showMsgId: "alertdiv"
            }
        };
        $("#grid").bxgrid(option);
    });
})

function initTime(){
    $("#historyTime").hide();
    $("#historyOperatorButton").hide();
    $("#historyGridDiv").hide();

    $('#inqu_status-startTime').daterangepicker({
        "singleDatePicker": true,
        "showDropdowns": true,
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
    $('#inqu_status-endTime').daterangepicker({
        "singleDatePicker": true,
        "showDropdowns": true,
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
}


function alarmTypeClick(obj){
    //修改input标签的状态
    var checked = $(obj).find("input").first().attr("checked");
    if(checked == undefined || checked == null){
        if(obj.id == "all"){
            //如果点击全部按钮，则重置所有标签，取消被选中状态
            $("#constructorDiv").find("input").removeAttr("checked");
            $("#constructorDiv").find("label[id !='all']").removeClass("active");
        }else{
            //如果点击不是全部按钮，则重置全部标签，取消被选中状态
            $("#constructorDiv").find("label[id ='all']").removeClass("active");
            $("#constructorDiv").find("input[value ='all']").removeAttr("checked");
        }
        $(obj).find("input").first().attr("checked","checked");

    }else{
        $(obj).find("input").first().removeAttr("checked");
    }
    var checkedInputs = $("#constructorDiv").find("input[checked='checked']");
    var numArr = []
    if(checkedInputs.length == 0){
        return;
    }else{
        for (var i = 0; i < checkedInputs.length; i++) {
            numArr.push(parseInt(checkedInputs.eq(i).val())); // 将文本框的值添加到数组中
        }
    }


    var queryParam = new Object();
    $("#queryarea").bxdiv('setQueryFromDiv', queryParam, "inqu_status");

    queryParam.inqu_status = {"projectId":$.cookie("selected_id"),"alarmTypesArr":numArr};
    var isAll = $.inArray(-1,numArr);
    if(isAll > -1){//如果点击全部
        queryParam.inqu_status = {"projectId":$.cookie("selected_id"),"alarmTypesArr":[1,2,3]};
    }
    queryParam.curPage = 1;
    queryParam.curRowNum = 10;
    var historyShow = $("#gridDiv").is(':hidden');
    if(historyShow == true){
        $("#historyGrid").bxgrid("option", "queryParam", queryParam);
        $("#historyGrid").bxgrid("query");
    }else{
        $("#grid").bxgrid("option", "queryParam", queryParam);
        $("#grid").bxgrid("query");
    }

}


function infoHistoryConvert(obj){
    var content = $(obj).html();
    if("报警历史记录" == content){
        $(obj).html("报警信息");
        $("#gridDiv").hide();
        $("#historyGridDiv").show();
        $("#historyTime").show();
        $("#historyOperatorButton").show();
        baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'], function () {
            $("#queryarea").bxdiv();
            var queryParam = {"inqu_status":{"projectId":$.cookie("selected_id"),"alarmTypesArr":[1,2,3]}};
            var gridOption = {
                primaryRowKey: "alarmId",
                caption: "报警历史信息",
                colNames: ['alarmId','盾构机名称', '报警信息', '报警开始时间','报警解决时间', '报警类别'],
                colModel: [
                    {
                        name: 'alarmId',
                        index: 'alarmId',
                        width: 5,
                        hidden: true,
                        forbidCopy: false
                    },{name:'tbmName', index:'tbmName',width: 30},
                    {name:'alarmStr', index:'alarmStr',width: 30},
                    {name:'startTime', index:'startTime',width: 30},
                    {name:'endTime', index:'endTime',width: 30},
                    {name:'alarmType', index:'alarmType',width: 30}],
                sortable: false,
                height:315
            };

            var option = {
                queryParam: queryParam,
                dataPattern: "url",
                url: "/raising/forward/construction/alarmInfo/getHistoryRows.do",
                gridOption: gridOption,
                showMsgOpt: {
                    showMsgId: "alertdiv"
                }
            };
            $("#historyGrid").bxgrid(option);
        });

    }else{
        $("#gridDiv").show();
        $("#historyTime").hide();
        $("#historyOperatorButton").hide();
        $("#historyGridDiv").hide();
        $(obj).html("报警历史记录");
    }
}

function on_query_click(obj){
    var queryParam = new Object();
    queryParam.inqu_status = {};
    queryParam.inqu_status.projectId = $.cookie("selected_id");
    queryParam.inqu_status.startTime = $('#inqu_status-startTime').val();
    queryParam.inqu_status.endTime = $('#inqu_status-endTime').val();

    var checkedInputs = $("#constructorDiv").find("input[checked='checked']");
    var numArr = []
    if(checkedInputs.length == 0){
        numArr = [1,2,3];
    }else{
        for (var i = 0; i < checkedInputs.length; i++) {
            numArr.push(parseInt(checkedInputs.eq(i).val())); // 将文本框的值添加到数组中
        }
    }
    queryParam.inqu_status.alarmTypesArr = numArr;
    var isAll = $.inArray(-1,numArr);
    if(isAll > -1){//如果点击全部
        queryParam.inqu_status = {"projectId":$.cookie("selected_id"),"alarmTypesArr":[1,2,3]};
    }
    if(queryParam.inqu_status.startTime == queryParam.inqu_status.endTime){
        alertDiv("提示","起始与结束时间不可以相同！");
        return;
    }
    $("#historyGrid").bxgrid("option", "queryParam", queryParam);
    $("#historyGrid").bxgrid("query");
    //$(obj).css("background-color","rgb(33, 132, 193)");
    $("button[class='btn btn-sm btn-block']").css("background-color","rgb(33, 132, 193)");
}

function exportExcel(){
    var queryParam = {};
    queryParam.inqu_status = {};
    queryParam.inqu_status.projectId = $.cookie("selected_id");
    queryParam.downloadColumnDesc ='盾构机名称,报警信息,报警开始时间,报警解决时间,报警类别';
    queryParam.downloadColumn = "tbmName,alarmStr,startTime,endTime,alarmType";
    queryParam.columnImageMap = {};
    queryParam.columnSelectMap = {};
    queryParam.curPage = 1;
    queryParam.curRowNum = 1000;
    var url =toolkitPath+ "/raising/forward/construction/alarmInfo/downloadExcel.do?ajaxParam="+encodeURIComponent(encodeURIComponent(JSON.stringify(queryParam)));
    window.location.href = url;
}


























