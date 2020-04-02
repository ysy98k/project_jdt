

$(function(){


    $.ajax({
        url:toolkitPath+"/raising/forward/construction/tbmInfo/getTbmInfo.do",
        type:"get",
        data:{"ajaxParam":$.cookie("selected_id")},
        success:function(data){
            if(data != undefined && data != null){
               for(name in data){
                   $("#"+name).html(data[name]);
               }
            }
        }
    })
})



function getTbmRecord(){
    var projectId = $.cookie("selected_id");
    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'], function () {
        $("#queryarea").bxdiv();
        var gridOption = {
            primaryRowKey: "tbmId",
            colNames: ['projectId', '区间', '项目', '盾构机', '状态', '当前环', '项目总长度', '开始时间', '结束时间', '工期'],
            colModel: [
                {name:'projectId', index:'projectId',width: 30, hidden: true, forbidCopy:false},
                {name:'sectionName', index:'section',width: 30,editable: false},
                {name:'projectName', index:'projectName',width: 30,editable: false},
                {name:'tbmName', index:'tbmName',width: 30,editable: false},
                {name:'status', index:'status',width: 30,editable: false},
                {name:'currentRing', index:'currentRing',width: 30,editable: false},
                {name:'totalLength', index:'totalLength',width: 30,editable: false},
                {name:'startTime', index:'startTime',width: 30,editable: false},
                {name:'endTime', index:'endTime',width: 30,editable: false},
                {name:'constructionPeroid', index:'constructionPeroid',width: 30,editable: false}
                ],
            sortable: false,
            height:315,
            caption: false
        };

        var option = {
            queryParam: {"projectId":projectId},
            dataPattern: "url",
            url: "/raising/forward/construction/tbmInfo/getConstructionPeroid.do",
            gridOption: gridOption,
            showMsgOpt: {
                showMsgId: "alertdiv"
            }
        };

        $("#tbmRecord").bxgrid(option);
    });
}



