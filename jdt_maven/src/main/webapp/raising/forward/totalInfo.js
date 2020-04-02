var info = new Vue({
    el:"#father",
    data:{
        totalDataList:[],
        showDataList:[],
    }
})

$(document).ready(function () {
    initDom();
    //下拉框初始化
    $('.chosen-select').chosen({
        allow_single_deselect : true
    });

    $(window).on('resize.chosen', function() {
        $('.chosen-select').next().css({
            'width' : '210px'
        });
    }).trigger('resize.chosen');
    $("#tableContent").busyLoad("show");
    $.ajax({
        url:toolkitPath + "/raising/totalInfo/getTotalInfos.do",
        type:"get",
        dataType:"json",
        success:function (data) {
            $("#tableContent").busyLoad("hide");
            if(data ==null){
                return;
            }
            info.totalDataList = data.dataList;
            info.showDataList = data.dataList;
            var cityArray = data.cityArray;
            var str = getCityOption(cityArray);
            $("#citySelect").append(str);
            $('.chosen-select').trigger("chosen:updated")
        },
        error:function(){
            $("#tableContent").busyLoad("hide");
        }
    })


});

function initDom(){
    var fathHeight = $("#father").outerHeight(true);
    var son1Height = $("#son1").outerHeight(true);
    var totalHeight = fathHeight - son1Height-3;
    $("#total").height(totalHeight);
    var totalGrandson1Height = $("#totalGrandson1").outerHeight(true);
    var explainHeight = $("#explain").outerHeight(true);
    var totalGrandson2 = totalHeight - totalGrandson1Height-1 -explainHeight;
    $("#totalGrandson2").height(totalGrandson2);

}

/**
 * 施工状态点击事件
 * @param obj
 */
function constructorClick(obj){
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
            numArr.push(checkedInputs.eq(i).val()); // 将文本框的值添加到数组中
        }
    }
    var isAll = $.inArray("all",numArr);
    if(isAll > -1){//如果点击全部
        info.showDataList = info.totalDataList;
        return;
    }

    var showDataList = [];
    for(var i=0;i<info.totalDataList.length;i++){
        var data = info.totalDataList[i];
        var contain = $.inArray(data.status,numArr);
        if(contain > -1){
            showDataList.push(data);
        }
    }
    info.showDataList = showDataList;
}

/**
 * 通讯状态改变事件
 */
function communicationChange(){
    var cityValue = $("#citySelect").val();
    var communicationValue = $("#communication").val();
    var showDataList = [];
    for(var i=0;i<info.totalDataList.length;i++){
        var data = info.totalDataList[i];
        var a = data.cityCCSId.indexOf(cityValue);
        var b = data.communicationState.indexOf(communicationValue);
        if(data.cityCCSId.indexOf(cityValue) >=0 && data.communicationState.indexOf(communicationValue) >= 0){
            showDataList.push(data);
        }
    }
    info.showDataList = showDataList;
}

/**
 * 城市改变事件
 */
function cityChange(){
    var cityValue = $("#citySelect").val();
    var communicationValue = $("#communication").val();
    var showDataList = [];
    for(var i=0;i<info.totalDataList.length;i++){
        var data = info.totalDataList[i];
        var a = data.cityCCSId.indexOf(cityValue);
        var b = data.communicationState.indexOf(communicationValue);
        if(data.cityCCSId.indexOf(cityValue) >=0 && data.communicationState.indexOf(communicationValue) >= 0){
            showDataList.push(data);
        }
    }
    info.showDataList = showDataList;
}

function selecteProject(obj){

    parent.document.getElementById("contentFrame").src =toolkitPath+"/raising/frame.do";
    $("[data-menucode='totalInfo']", parent.document).attr({class:"hover tabSelColor"});
    $("[data-menucode='totalInfo'] a", parent.document).attr({class:"dropdown-toggle"});

    /*$("[data-menucode='totalInfo']", parent.document).attr({class:"hover tabSelColor"});
    $("[data-menucode='totalInfo'] a", parent.document).attr({class:"dropdown-toggle"});
    $("[data-menucode='chartShow']", parent.document).attr({class:"hover tabSelColor active"});
    $("[data-menucode='chartShow'] a", parent.document).attr({class:"dropdown-toggle tabSel"});*/
    var selectName = $(obj).attr("data-projectName");
    var collectorName = $(obj).attr("data-collector");
    var projectId = $(obj).attr("data-id");
    var totalLength = $(obj).attr("data-totalLength");
    var totalMileage = $(obj).attr("data-totalMileage");
    var tbmName = $(obj).attr("data-tbmName");
    var ringTotal = $(obj).attr("data-ringTotal");
    $.cookie('selected_name',selectName, {path: "/"});
    $.cookie('selected_id',projectId, {path: "/"});
    $.cookie('selected_collection',collectorName,{path:"/"});
    $.cookie('totalLength',totalLength,{path:"/"});
    $.cookie('totalMileage',totalMileage,{path:"/"});
    $.cookie('tbmName',tbmName,{path:"/"});
    $.cookie('ringTotal',ringTotal,{path:"/"});
}

function getCityOption(cityArray){
    if(cityArray == null || cityArray.length < 1){
        return;
    }
    var str = "";
    for(var i=0;i<cityArray.length;i++){
        str += "<option value='"+cityArray[i].value+"'>"+cityArray[i].label+"</option>"
    }
    return str;
}



