var info = new Vue({
    el:"#father",
    data:{
        totalDataList:[],
        showDataList:[]
    }
})

$(document).ready(function () {
    var param = {inqu_status:{}};
    getGrid(param);

    //下拉框初始化
    $('.chosen-select').chosen({
        allow_single_deselect : true
    });

    $(window).on('resize.chosen', function() {
        $('.chosen-select').next().css({
            'width' : '210px'
        });
    }).trigger('resize.chosen');

    initSelect();

});

function getGrid(param){
    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'], function () {
        $("#queryarea").bxdiv();
        var gridOption = {
            primaryRowKey: "tbmId",
            colNames: ['tbmId', '盾构机厂家', '盾构机名称', '生产时间', '拥有单位', '盾构机尺寸', '盾构机类型', '铰接类型', '导向系统版本', '联系人', '电话', '盾构状态'],
            colModel: [
                {
                    name: 'tbmId',
                    index: 'tbmId',
                    width: 5,
                    hidden: true,
                    forbidCopy: false
                }, {
                    name: 'factory',
                    index: 'factory',
                    editable: true,
                    width: 80,
                    editrules: {
                        required: true,
                    },
                    editoptions: {
                        dataInit:function(element){
                            var a1 = element.value;
                            var a2 = a1.substring(a1.indexOf(">")+1,a1.lastIndexOf("</a>"));
                            a2 == "undefined" ? element.value ="" : element.value = a2;
                        }
                    },
                    formatter: function (value, grid, row, state) {
                        var btnReset =
                            "<a href='javascript:jumpPage(\""+row.tbmId+"\",\""+row.tbmName+"\")'>"+value+"</a>";
                        if(state == undefined && !isNullOrEmptyOrUndefiend(row.factory)){//如果是复制
                            return row.factory;
                        }
                        if(state == undefined){
                            return "";
                        }
                        return btnReset;
                    }

                }, {
                    name: 'tbmName',
                    index: 'tbmName',
                    editable: true,
                    width: 50,
                    editrules: {
                        required: true,
                    },
                    editoptions: {
                        dataInit:function(element){
                            var a1 = element.value;
                            var a2 = a1.substring(a1.indexOf(">")+1,a1.lastIndexOf("</a>"));
                            a2 == "undefined" ? element.value ="" : element.value = a2;
                        }
                    },
                    formatter: function (value, grid, row, state) {
                        var btnReset =
                            "<a href='javascript:jumpPage(\""+row.tbmId+"\",\""+row.tbmName+"\")'>"+value+"</a>";
                        if(state == undefined && !isNullOrEmptyOrUndefiend(row.tbmName)){
                            return row.tbmName;
                        }
                        if(state == undefined){
                            return "";
                        }
                        return btnReset;
                    }
                }, {
                    name: 'produceTime',
                    index: 'produceTime',
                    editable: true,
                    width: 30,
                    sortable: true,
                    editrules: {
                        required: true,
                        custom: true,
                        custom_func: checkGridDate
                    },
                    editoptions: {
                        dataInit:function(element){
                            element.value = isNullOrEmptyOrUndefiend(element.value) == true ? moment().format('YYYY-MM-DD') : element.value;
                            //日期控件初始化
                            $(element).datetimepicker({
                                language: 'zh-CN',//显示中文
                                format: 'yyyy-mm-dd',//显示格式
                                autoclose: true,//选中自动关闭
                                todayBtn: false,//显示今日按钮
                                todayHighlight: 1,//今天高亮
                                minView:2
                            }).on('changeDate', function(ev){
                                var a1 = ev.date.valueOf();
                                element.value = getDay(parseInt(a1));
                            });

                        }
                    }
                }, {
                    name: 'owner',
                    index: 'owner',
                    editable: true,
                    width: 30,
                    forbidCopy: false
                }, {
                    name: 'diameter',
                    index: 'diameter',
                    editable: true,
                    editrules: {
                        required: true,
                    },
                    width: 40
                },
                {name:'tbmType', index:'tbmType',width: 30, editable: true, edittype:'select', editoptions: {value: getTbmType() }}
                , {
                    name: 'hingeType',
                    index: 'hingeType',
                    editable: true,
                    width: 30,
                    edittype:'select', editoptions: {value: "主动:主动; 被动:被动"}
                }, {
                    name: 'rmsVersion',
                    index: 'rmsVersion',
                    editable: true,
                    width: 60
                }, {
                    name: 'contacts',
                    index: 'contacts',
                    editable: true,
                    width: 30
                }, {
                    name: 'phone',
                    index: 'phone',
                    editable: true,
                    width: 40,
                    editrules: {
                        required: false,
                        custom: true,
                        custom_func: checkGridMobile
                    },
                }, {
                    name: 'tbmStatus',
                    index: 'tbmStatus',
                    width: 30,
                    editable: false,readOnly:true,
                    forbidCopy: false

            }],
            sortable: false,
            height:315,
            caption: "盾构机信息",
            jsonReader: {
                id: "tbmId",
                repeatitems: false
            }
        };

        var option = {
            queryParam: param,
            dataPattern: "url",
            url: "/raising/forward/tbmManage/getTbmInfos.do",
            gridOption: gridOption,
            showMsgOpt: {
                showMsgId: "alertdiv"
            }
        };

        $("#grid").bxgrid(option);
    });
}

/**
 * 查询按钮点击事件
 */
function on_query_click(){
    var queryParam = new Object();
    $("#queryarea").bxdiv('setQueryFromDiv', queryParam, "inqu_status");
    $("#grid").bxgrid("option", "queryParam", queryParam);
    $("#grid").bxgrid("query");

}

/**
 * 新增/复制按钮点击事件
 */
function addAndCopy() {
    $("#grid").bxgrid("addAndCopy");

}

/**
 * 保存按钮点击事件
 */
function saveRec() {
    var insertOrUpdate = $("#grid").bxgrid('rawMethodCall', 'getGridParam', 'insertOrUpdate');
    if (insertOrUpdate == "insert") {
        insertRec();
    } else {
        updateRec();
    }
}

/**
 * 新增
 */
function insertRec() {
    var selectArray = $("#grid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if (selectArray.length == 0) {
        alertDiv("提示", "请至少勾选一条记录进行操作！")
        return;
    }
    var paramJsonObj = new Object();
    paramJsonObj.detail = {};
    paramJsonObj.detail.resultRow = [];
    //对数据做校验，处理。填充入参。
    for (var i = 0; i < selectArray.length; i++) {
        //var tbmType = $("#"+selectArray[i]).find("option:selected").val();
        var tbmType = $("#"+selectArray[i]).find("select[name='tbmType']").val();
        var status = $("#grid").bxgrid('rawMethodCall', "saveRow", selectArray[i]);
        if (status == false) {
            return;
        }
        var saveData = $("#grid").bxgrid('rawMethodCall', "getRowData", selectArray[i]);
        saveData.tbmName = saveData.tbmName.substring(saveData.tbmName.indexOf(">")+1,saveData.tbmName.lastIndexOf("</a>"));
        saveData.factory = saveData.factory.substring(saveData.factory.indexOf(">")+1,saveData.factory.lastIndexOf("</a>"));
        if(tbmType != undefined && tbmType != null && tbmType != "" ){
            saveData.ccsType =tbmType;
        }
        if(checkProjectDetail(saveData) == false){
            alertDiv("提示","请正确书写盾构信息");
            return;
        }
        delete saveData.tbmType;
        paramJsonObj.detail.resultRow.push(saveData);
    }
    var callback = {
        onSuccess: function (paramJsonObj) {
            var showMsgOpt = {
                showMsgId: "alertdiv",
                status: paramJsonObj.status,
                showMsg: paramJsonObj.returnMsg
            };
            $("#grid").bxgrid("option", "showMsgOpt", showMsgOpt);
            $("#grid").bxgrid("query");
            initSelect();
        }

    };
    AjaxCommunicator.ajaxRequest('/raising/forward/tbmManage/addTbm.do', 'POST', paramJsonObj, callback,true);
}

/**
 * 修改
 */
function updateRec() {
    var selectArray = $("#grid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if (selectArray.length == 0) {
        alertDiv("提示", "请至少勾选一条记录进行操作！")
        return;
    }
    var paramJsonObj = new Object();
    paramJsonObj.detail = {};
    paramJsonObj.detail.resultRow = [];
    for (var i = 0; i < selectArray.length; i++) {
        //var tbmType = $("#"+selectArray[i]).find("option:selected").val();
        var tbmType = $("#"+selectArray[i]).find("select[name='tbmType']").val();
        var status = $("#grid").bxgrid('rawMethodCall', "saveRow", selectArray[i]);
        if (status == false) {
            return;
        }
        var saveData = $("#grid").bxgrid('rawMethodCall', "getRowData", selectArray[i]);
        saveData.tbmName = saveData.tbmName.substring(saveData.tbmName.indexOf(">")+1,saveData.tbmName.lastIndexOf("</a>"));
        saveData.factory = saveData.factory.substring(saveData.factory.indexOf(">")+1,saveData.factory.lastIndexOf("</a>"));
        if(tbmType != undefined && tbmType != null && tbmType != "" ){
            saveData.ccsType =tbmType;
        }
        if(checkProjectDetail(saveData) == false){
            alertDiv("提示","请正确书写盾构信息");
            return;
        }
        delete saveData.tbmType;
        paramJsonObj.detail.resultRow.push(saveData);
    }
    var callback = {
        onSuccess: function (paramJsonObj) {
            var showMsgOpt = {
                showMsgId: "alertdiv",
                status: paramJsonObj.status,
                showMsg: paramJsonObj.returnMsg
            };
            $("#grid").bxgrid("option", "showMsgOpt", showMsgOpt);
            $("#grid").bxgrid("query");
            initSelect();
        }
    };
    AjaxCommunicator.ajaxRequest('/raising/forward/tbmManage/updateTbm.do', 'POST', paramJsonObj, callback,true);
}


/**
 * 盾构机厂家，盾构机名称点击事件
 * 跳转页面
 * @param tbmId
 * @param tbmName
 */
function jumpPage(tbmId,tbmName){
    parent.document.getElementById("contentFrame").src =toolkitPath+"/df/console/frame_tbm.jsp";
    $.cookie('selected_tbmId',tbmId, {path: "/"});
    $.cookie('selected_tbmName',tbmName, {path: "/"});

}

function initSelect(){
    $.ajax({
        "url":toolkitPath+'/raising/forward/tbmManage/getTbmInfosOption.do',
        "type":"get",
        "dataType":"json",
        success:function (data) {
            if(data.haveData == "-1"){
                return;
            }

            var factoryArr = data.factorysList;
            var ownerArr = data.ownersList;
            var statusArr  = data.statusList;

            $("#factoryOption").siblings().remove();
            $("#ownerOption").siblings().remove();
            $("#tbmStatusOption").siblings().remove();
            var factoryStr = getOption(factoryArr);
            $("#inqu_status-factory").append(factoryStr);
            var ownerStr = getOption(ownerArr);
            $("#inqu_status-owner").append(ownerStr);
            var statusStr = getOption(statusArr);
            $("#inqu_status-tbmStatus").append(statusStr);
            $('.chosen-select').trigger("chosen:updated");
        }

    })
}

function getTbmType(code) {
    var returnStr = "";
    $.ajax({
        async: false,
        type: "post",
        url: toolkitPath +"/raising/backstage/tbm/getTbmType.do",
        data:{"code":"tbmtype"},
        success: function (data) {
            for (i = 0; i < data.length; i++) {
                if(code == 1){
                    $("#inqu_status-ccsType").append("<option value='"+data[i].ccsId+"'>"+data[i].ccsStr+"</option>");
                }else{
                    if (i < data.length - 1) {
                        returnStr += data[i].ccsId + ":" + data[i].ccsStr + ";";
                    } else {
                        returnStr += data[i].ccsId + ":" + data[i].ccsStr;
                    }
                }
            }

        }
    });
    if(code == 1){

    }else {
        return returnStr;
    }
}

function getOption(data){
    if(data == null || data.length < 1){
        return;
    }
    var str = "";
    for(var i=0;i<data.length;i++){
        str += "<option value='"+data[i]+"'>"+data[i]+"</option>"
    }
    return str;
}

/**
 * 数据校验
 */
function checkProjectDetail(data){
    if( data.tbmName == "" ||data.factory == "" || data.ccsType == "" || data.diameter == "" ){
        return false;
    }
    return true;
}



