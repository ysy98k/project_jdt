var info = new Vue({
    el:"#tbmForm",
    data:{
        tbmId:"",
        tbmName:"",
        factory:"",
        tbmType:"2",
        diameter:"",
        produceTime:"",
        typeList:[]
    }
})

function getMyDate(str) {
    var oDate = new Date(str),
        oYear = oDate.getFullYear(),
        oMonth = oDate.getMonth()+1,
        oDay = oDate.getDate(),
        oTime = oYear +'-'+ addZero(oMonth) +'-'+ addZero(oDay) ;
    return oTime;
}

//补零操作
function addZero(num){
    if(parseInt(num) < 10){
        num = '0'+num;
    }
    return num;
}

$(function(){
    $("[tagexpr]").each(function () {
        var oldValue = $(this).attr("tagexpr");
        var a1 = oldValue.substring(0,oldValue.indexOf("."));
        var a2 = oldValue.substring(oldValue.indexOf("_")+1);
        var a3 = a1+"."+"lineTest" + "_" + a2;
        $(this).attr("tagexpr",a3);
    })
    getTbmInfo();
    $('.date-picker').datetimepicker({
        language: 'zh-CN',//显示中文
        format: 'yyyy-mm-dd',//显示格式
        autoclose: true,//选中自动关闭
        todayBtn: false,//显示今日按钮
        todayHighlight: 1,//今天高亮
        minView:2
    }).on('changeDate', function(ev){
        var a1 = ev.date.valueOf();
        info.produceTime = getMyDate(parseInt(a1));



    });

})

function getTbmInfo(){
    var projectId = $.cookie("selected_id");
    var param = {"inqu_status":{"tbmId":projectId,"code":"tbmtype"}};//tbmId只是传输数据的幌子。这样写非常方便
    $.ajax({
        type: "get",
        url : toolkitPath+'/raising/forward/tbmManage/getTbmInfo.do',
        data:{"ajaxParam":JSON.stringify(param)},
        dataType:"json",
        success: function (data) {
            if(data.status == "0"){
                info.typeList = data.tbmTypeList;
                var temp = data.rows[0];
                info.tbmId = temp.tbmId;
                info.tbmName = temp.tbmName;
                info.factory = temp.factory;
                info.tbmType = temp.ccsType;
                info.diameter = temp.diameter;
                info.produceTime = temp.produceTime;

            }
        }
    });

}

function update(){
    if(info.tbmName == "" || info.produceTime == ""){
        alertDiv("提示","被红星标注列的值不可以为空");
        return;
    }
    baosightRequire.requireFunct([ 'bxdialog'],function () {
        var button = [{
            text: "确认",
            "class" : "btn btn-skinColor btn-xs",
            click : function () {
                $("#updateConfirm").bxdialog('close');
                var param = {};
                param.tbmId = info.tbmId;
                param.tbmName = info.tbmName;
                param.factory = info.factory;
                param.ccsType = info.tbmType;
                param.diameter = info.diameter;
                param.produceTime = info.produceTime;
                var obj = {"detail":{"resultRow":[]}};
                obj.detail.resultRow.push(param);
                $.ajax({
                    type: "POST",
                    url : toolkitPath+'/raising/forward/tbmManage/updateTbmInfo.do',
                    data:{"ajaxParam":JSON.stringify(obj)},
                    dataType:"json",
                    success: function (data) {
                        if(data.status == "0"){
                            alertDiv("提示","修改成功");
                        }else{
                            alertDiv("提示","修改失败");
                        }
                    }
                });
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
            width: '200px'
        };
        //显示对话框
        $("#updateConfirm").bxdialog(dialogOpt);
    });



}

