

$(document).ready(function () {

    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'],
        function () {
            $("#queryarea").bxdiv();
            var gridOption = {
                primaryRowKey: "alarmCodeId",
                caption: false,
                colNames: ['报警变量Id', '报警变量','报警提示信息', '变量类型','变量code'],
                colModel: [{
                    name: 'alarmCodeId',
                    index: 'alarmCodeId',
                    width: 10,
                    hidden: true,
                    forbidCopy: true
                },
                {
                    name: 'alarmName',
                    index: 'alarmName',
                    width: 50,
                    editable: true,
                    sortable: true,
                    editrules: {
                        required: true,
                    }
                },
                {
                    name: 'alarmNameStr',
                    index: 'alarmNameStr',
                    width: 50,
                    editable: true,
                    sortable: true,
                    editrules: {
                        required: true,
                    }
                },

                {
                    name: 'alarmType',
                    index: 'alarmType',
                    width: 20,
                    editable: true,
                    sortable: true,
                    editrules: {
                        required: true,
                    },
                    edittype:'select',
                    editoptions: {value:"1:设备报警;2:风险源报警;3:导向报警"}
                },

                {
                    name: 'alarmCode',
                    index: 'alarmCode',
                    width: 50,
                    editable: true,
                    forbidCopy: false
                }
                ],
                sortorder: 'asc',
                height: 351,
                jsonReader: {
                    id: "alarmCodeId",
                    repeatitems: false
                }
            };
            var defaultData = [{
                label: "全部",
                value: ""
            }];
            var option = {
                queryParam: {},
                dataPattern: "url",
                url: "/raising/backstage/projectManage/alarmVariableManagement/getRows.do",
                showMsgOpt: {
                    showMsgId: "alertdiv"
                },
                gridOption: gridOption
            };
            $("#jqGrid").bxgrid(option);
        });

});


/**
 * 查询按钮点击事件
 */
function on_query_click() {
    var queryParam = new Object();
    $("#queryarea").bxdiv('setQueryFromDiv', queryParam, "inqu_status");
    $("#jqGrid").bxgrid("option", "queryParam", queryParam);
    $("#jqGrid").bxgrid("query");
}

/**
 * 新增/复制按钮点击事件
 */
function addAndCopy() {
    $("#jqGrid").bxgrid("addAndCopy");
}

/**
 * 保存按钮点击事件
 */
function saveRec() {
    var insertOrUpdate = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'insertOrUpdate');
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
    var selectArray = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if (selectArray.length == 0) {
        alertDiv("提示", "请至少勾选一条记录进行操作！")
        return;
    }
    var paramJsonObj = new Object();
    paramJsonObj.detail = {};
    paramJsonObj.detail.resultRow = [];
    paramJsonObj.detail.resultRow[0] = {};
    paramJsonObj.detail.resultRow[0].alarmCodeArr = [];
    //对数据做校验，处理。填充入参。
    for (var i = 0; i < selectArray.length; i++) {
        var alarmType = $("#"+selectArray[i]).find("select[name='alarmType']").val();
        var status = $("#jqGrid").bxgrid('rawMethodCall', "saveRow", selectArray[i]);
        if (status == false) {
            return;
        }
        var saveData = $("#jqGrid").bxgrid('rawMethodCall', "getRowData", selectArray[i]);
        saveData.alarmType = alarmType;
        paramJsonObj.detail.resultRow[0].alarmCodeArr.push(saveData);
    }

    var callback = {
        onSuccess: function (paramJsonObj) {
            var showMsgOpt = {
                showMsgId: "alertdiv",
                status: paramJsonObj.status,
                showMsg: paramJsonObj.returnMsg
            };
            $("#jqGrid").bxgrid("option", "showMsgOpt", showMsgOpt);
            $("#jqGrid").bxgrid("query");
        }
    };

    AjaxCommunicator.ajaxRequest('/raising/backstage/projectManage/alarmVariableManagement/add.do', 'POST', paramJsonObj, callback,true);
}

/**
 * 删除
 */
function deleteRec() {
    var selectArray = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if (selectArray.length == 0) {
        alertDiv("提示", "请至少勾选一条记录进行操作！")
        return;
    }
    var buttons = [{
        html: "<i class='ace-icon fa fa-check bigger-110'></i>&nbsp; 是",
        "class": "btn btn-skinColor btn-xs",
        click: function () {
            deleteOK();
            $(this).dialog("close");
        }
    }, {
        html: "<i class='ace-icon fa fa-times bigger-110'></i>&nbsp; 否",
        "class": "btn btn-xs",
        click: function () {
            $(this).dialog("close");
        }
    }];
    confirmDiv("确认删除", "数据删除后将不可恢复，一般情况下程序会自动删除相关资源，如未删除，请自行手工删除，是否确定删除？", buttons);
}

function deleteOK() {
    var paramJsonObj = new Object();
    $("#jqGrid").bxgrid('setInfoFromGrid', paramJsonObj, 'result');
    var callback = {
        onSuccess: function (paramJsonObj) {
            var showMsgOpt = {
                showMsgId: "alertdiv",
                status: paramJsonObj.status,
                showMsg: paramJsonObj.returnMsg
            };
            $("#jqGrid").bxgrid("option", "showMsgOpt", showMsgOpt);
            $("#jqGrid").bxgrid("query");
        }
    };
    AjaxCommunicator.ajaxRequest('/raising/backstage/projectManage/alarmVariableManagement/delete.do', 'POST', paramJsonObj, callback,true);
}

/**
 * 修改
 */
function updateRec() {
    var selectArray = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if (selectArray.length == 0) {
        alertDiv("提示", "请至少勾选一条记录进行操作！")
        return;
    }
    var paramJsonObj = new Object();
    paramJsonObj.detail = {};
    paramJsonObj.detail.resultRow = [];
    for (var i = 0; i < selectArray.length; i++) {
        var alarmType = $("#"+selectArray[i]).find("select[name='alarmType']").val();
        var status = $("#jqGrid").bxgrid('rawMethodCall', "saveRow", selectArray[i]);
        if (status == false) {
            return;
        }
        var saveData = $("#jqGrid").bxgrid('rawMethodCall', "getRowData", selectArray[i]);
        saveData.alarmType = alarmType;
        paramJsonObj.detail.resultRow.push(saveData);
    }
    var callback = {
        onSuccess: function (paramJsonObj) {
            var showMsgOpt = {
                showMsgId: "alertdiv",
                status: paramJsonObj.status,
                showMsg: paramJsonObj.returnMsg
            };

            $("#jqGrid").bxgrid("option", "showMsgOpt", showMsgOpt);
            $("#jqGrid").bxgrid("query");
        }
    };
    AjaxCommunicator.ajaxRequest('/raising/backstage/projectManage/alarmVariableManagement/update.do', 'POST', paramJsonObj, callback,true);
}

/**
 * 复选框点击事件
 */
function alarmCheckClick(){
    var arr = [];
    $.each($('input[name="form-field-checkbox"]:checkbox:checked'),function(){
        arr.push(parseInt( $(this).val() ));
    });
    if(arr.length == 0){
        arr = [1,2,3];
    }

    var queryParam = new Object();
    $("#queryarea").bxdiv('setQueryFromDiv', queryParam, "inqu_status");

    queryParam.inqu_status = {"alarmTypesArr":arr};
    queryParam.curPage = 1;
    queryParam.curRowNum = 10;
    $("#jqGrid").bxgrid("option", "queryParam", queryParam);
    $("#jqGrid").bxgrid("query");
}

/**
 * 模板下载
 */
function exportExcel(){
    var queryParam = {};
    queryParam.downloadColumnDesc ='报警变量,报警提示信息,变量类型,变量code';
    queryParam.downloadColumn = "alarmName,alarmNameStr,alarmType,alarmCode";
    queryParam.columnImageMap = {};
    queryParam.columnSelectMap = {};
    queryParam.curPage = 1;
    queryParam.curRowNum = 1000;
    var url =toolkitPath+ "/raising/backstage/projectManage/alarmVariableManagement/download.do?ajaxParam="+encodeURIComponent(encodeURIComponent(JSON.stringify(queryParam)));
    window.location.href = url;
    //window.location.href =toolkitPath +'/raising/backstage/projectManage/alarmVariableManagement/download.do';
}

/**
 * 数据校验
 */
function checkProjectDetail(projectDetailJson){
    var returnJson = {}
    if(  projectDetailJson.status == "" ||projectDetailJson.startMileage == "" || isNullOrEmptyOrUndefiend(projectDetailJson.templateName)
        || projectDetailJson.endMileage == "" || projectDetailJson.totalLength == "" || projectDetailJson.tbmId == ""
        || projectDetailJson.dayShiftStart == "" || projectDetailJson.dayShiftEnd == "" ){
        returnJson.status = false;
        returnJson.message = "被红色标记的数据项不可以为空，且格式正确。";
        return returnJson;
    }
    var reDateTime = /^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1])) (?:(?:[0-2][0-3])|(?:[0-1][0-9])):[0-5][0-9]:[0-5][0-9]$/;
    if(projectDetailJson.startTime.trim() != ""){
        if(!reDateTime.test(projectDetailJson.startTime.trim())){
            returnJson.status = false;
            returnJson.message = "时间格式出错！格式为2000-01-01 00:00:00";
            return returnJson;
        }
    }
    if(projectDetailJson.endTime.trim() != ""){
        if(!reDateTime.test(projectDetailJson.endTime.trim())){
            returnJson.status = false;
            returnJson.message = "时间格式出错！格式为2000-01-01 00:00:00";
            return returnJson;
        }
    }
    var startStr = projectDetailJson.dayShiftStart.substring(0,projectDetailJson.dayShiftStart.indexOf(":"));
    var endStr  = projectDetailJson.dayShiftEnd.substring(0,projectDetailJson.dayShiftEnd.indexOf(":"));
    if(Number(endStr) <= Number(startStr)){
        returnJson.status = false;
        returnJson.message = "白班结束时间必须大于白班起始时间";
        return returnJson;
    }
    var totalLength =  Number(projectDetailJson.totalLength);
    var startMileage =  Number(projectDetailJson.startMileage);
    var endMileage =  Number(projectDetailJson.endMileage);
    var tbmId =  Number(projectDetailJson.tbmId);
    if(isNaN(totalLength) || isNaN(startMileage)|| isNaN(endMileage) || isNaN(tbmId)){
        returnJson.status = false;
        returnJson.message = "被红色标记的数据项不可以为空，且格式正确。";
        return returnJson;
    }
    returnJson.status = true;
    return returnJson;

}











