
var info = new Vue({
    el:"#projectForm",
    data:{
        projectId:"",
        projectName:"",
        sectionName:"",
        tbmName:"",
        collectorName:"",
        startMileage:"",
        endMileage:"",
        totalLength:"",
        ringTotal:"",
        projectSituation:"",
        projectLocation:"",
        geologyInfo:"",
        startTime:"",
        endTime:"",
        supervisor:"",
        buildUnit:"",
        dayShiftStart:"", // 此属性未与vue绑定显示
        dayShiftEnd:""       // 此属性未与vue绑定显示

    }
})


$(function(){
    getProjectInfo();
    $('#startTime').datetimepicker({
        language: 'zh-CN',//显示中文
        format: 'yyyy-mm-dd hh:ii:ss',//显示格式
        autoclose: true,//选中自动关闭
        todayBtn: false,//显示今日按钮
        todayHighlight: 1,//今天高亮
        minView:0
    }).on('changeDate', function(ev){
        var a1 = ev.date.valueOf();
        info.startTime = getMyDate(parseInt(a1));
    });
    $('#endTime').datetimepicker({
        language: 'zh-CN',//显示中文
        format: 'yyyy-mm-dd hh:ii:ss',//显示格式
        autoclose: true,//选中自动关闭
        todayBtn: false,//显示今日按钮
        todayHighlight: 1,//今天高亮
        minView:0
    }).on('changeDate', function(ev){
        var a1 = ev.date.valueOf();
        info.endTime = getMyDate(parseInt(a1));
    });

    $('#dayShiftStart').timepicker({
        minuteStep : 1,
        showSeconds : false,
        showMeridian : false
    }).next().on(ace.click_event, function() {
        $(this).prev().focus();
    });

    $('#dayShiftEnd').timepicker({
        minuteStep : 1,
        showSeconds : false,
        showMeridian : false
    }).next().on(ace.click_event, function() {
        $(this).prev().focus();
    });
})

function getProjectInfo(){
    var projectId = $.cookie("selected_id");
    $.ajax({
        type:"get",
        data:{"ajaxParam":JSON.stringify({"projectId":projectId})},
        url: toolkitPath+'/raising/forward/projectManage/getProject.do',
        dataType:"json",
        success:function(data){
            if(data.status == "0"){
                info.projectId = data.projectId;
                info.projectName = data.projectName;
                info.sectionName = data.sectionName;
                info.tbmName = data.tbmName;
                info.collectorName = data.collectorName;
                info.startMileage = data.startMileage;
                info.endMileage = data.endMileage;
                info.totalLength = data.totalLength;
                info.ringTotal = data.ringTotal;
                info.projectSituation = data.projectSituation;
                info.projectLocation = data.projectLocation;
                info.geologyInfo = data.geologyInfo;
                info.startTime = data.startTime;
                info.endTime = data.endTime;
                info.supervisor = data.supervisor;
                info.buildUnit = data.buildUnit;
                $("#dayShiftStart").val(data.dayShiftStart);
                $("#dayShiftEnd").val(data.dayShiftEnd);
                info.dayShiftStart = data.dayShiftStart;
                info.dayShiftEnd = data.dayShiftEnd;
            }
        }

    })
}

function updateProject(){
    var checkResult = checkProject();
    if(checkResult.status != true){
        alertDiv("提示",checkResult.message);
        return;
    }

    baosightRequire.requireFunct([ 'bxdialog'],function () {
        var button = [{
            text: "确认",
            "class" : "btn btn-skinColor btn-xs",
            click : function () {
                $("#updateConfirm").bxdialog('close');
                var param= info.$data;
                $.ajax({
                    type:"post",
                    data:{"ajaxParam":JSON.stringify(param)},
                    url: toolkitPath+'/raising/forward/projectManage/updateProject.do',
                    dataType:"json",
                    success:function(data){
                        if(data.status == "0"){
                            alertDiv("提示","修改成功");
                        }else{
                            alertDiv("提示","修改失败");
                        }
                    }
                })
            }
        },{
            text: "取消",
            "class" : "btn btn-skinColor btn-xs",
            click : function () {
                $("#updateConfirm").bxdialog('close');
            }
        }]
        var title = "提示";
        var dialogOpt = {
            title: title,
            buttons:button,
            width: '300px'
        };
        //显示对话框
        $("#updateConfirm").bxdialog(dialogOpt);
    });
}


function checkProject(){
    var returnJson = {}
    info.dayShiftStart = $("#dayShiftStart").val();
    info.dayShiftEnd = $("#dayShiftEnd").val();
    if( info.startMileage == "" || info.endMileage == "" || info.totalLength == "" || info.projectName == ""
        || info.buildUnit == "" || info.dayShiftStart == "" || info.dayShiftEnd == "" ){
        returnJson.status = false;
        returnJson.message = "被红色标记的数据项不可以为空，且格式正确。";
        return returnJson;
    }
    var reDateTime = /^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1])) (?:(?:[0-2][0-3])|(?:[0-1][0-9])):[0-5][0-9]:[0-5][0-9]$/;
    if(!isNullOrEmptyOrUndefiend(info.startTime) && info.startTime.trim() != ""){
        if(!reDateTime.test(info.startTime.trim())){
            returnJson.status = false;
            returnJson.message = "时间格式出错！格式为2000-01-01 00:00:00";
            return returnJson;
        }
    }
    if(!isNullOrEmptyOrUndefiend(info.endTime) && info.endTime.trim() != ""){
        if(!reDateTime.test(info.endTime.trim())){
            returnJson.status = false;
            returnJson.message = "时间格式出错！格式为2000-01-01 00:00:00";
            return returnJson;
        }
    }
    var startStr = info.dayShiftStart.substring(0,info.dayShiftStart.indexOf(":"));
    var endStr  = info.dayShiftEnd.substring(0,info.dayShiftEnd.indexOf(":"));
    if(Number(endStr) <= Number(startStr)){
        returnJson.status = false;
        returnJson.message = "白班结束时间必须大于白班起始时间";
        return returnJson;
    }
    var totalLength =  Number(info.totalLength);
    var startMileage =  Number(info.startMileage);
    var endMileage =  Number(info.endMileage);
    if(isNaN(totalLength) || isNaN(startMileage)|| isNaN(endMileage)){
        returnJson.status = false;
        returnJson.message = "被红色标记的数据项不可以为空，且格式正确。";
        return returnJson;
    }
    returnJson.status = true;
    return returnJson;

}
