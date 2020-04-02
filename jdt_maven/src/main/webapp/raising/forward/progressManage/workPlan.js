

$(document).ready(function () {
    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'],
        function () {
            $("#queryarea").bxdiv();
            var gridOption = {
                primaryRowKey: "planId",
                caption: false,
                colNames: ['planId','projectId','project','end','startTime','时间', '工期（天）','完成至环数','备注'],
                colModel: [
                    {name:'planId', index:'planId',width: 40,hidden: true,forbidCopy: false},
                    {name:'projectId', index:'projectId',width: 40,hidden: true,forbidCopy: true},
                    {name:'project', index:'project',width: 40,hidden: true,forbidCopy: false,editrules: {required: false}},//用来判断是否是第一行和最后一行记录
                    {name:'end', index:'end',width: 40,hidden: true,forbidCopy: false,editrules: {required: false}},//用来判断是否是最后一行记录
                    {name:'startTime', index:'startTime',width: 40,hidden: true,forbidCopy: true,editrules: {required: false}},//用来记录当前项目的开始时间，用来计算工期
                    {name:'planTime', index:'planTime',width: 40,editable: true,
                        sortable: true,
                        editrules: {
                            required: true,
                            custom: true,
                            custom_func: checkGridDate
                        },
                        editoptions: {
                            size:100,maxlengh:200,length:150,
                            dataInit:function(element){
                                element.value = isNullOrEmptyOrUndefiend(element.value) == true ? moment().format('YYYY-MM-DD') : element.value;
                                //日期控件初始化
                                $(element).daterangepicker(getDateOption(element.value,element.value), function(start, end, label) {
                                    var s  = start.format('YYYY-MM-DD');
                                    element.value = s;
                                });
                            }
                        }
                    },
                    {name:'schedule', index:'schedule',width: 50,editable: false,readOnly:true},
                    {name:'ringNum', index:'ringNum',width: 50,editable: true,editrules: {required: false}},
                    {name:'remark', index:'remark',width: 50,editable: true,editrules: {required: false}}
                ],
                sortorder: 'asc',
                height: 351,
                //rownumbers:true,
                jsonReader: {
                    id: "tbmId",
                    repeatitems: false
                }
            };
            var defaultData = [{
                label: "全部",
                value: ""
            }];
            var option = {
                queryParam: {"inqu_status":{"projectId":$.cookie("selected_id")}},
                dataPattern: "url",
                url: "/raising/forward/progressManage/workPlan/getRows.do",
                showMsgOpt: {
                    showMsgId: "alertdiv"
                },
                gridOption: gridOption
            };
            $("#jqGrid").bxgrid(option);
            //$("#jqGrid").jqGrid('setLabel','rn', '序号', {'text-align':'center','vertical-align':'middle'},'');

        });
});

/**
 * 查询按钮点击事件
 */
function on_query_click() {
    var queryParam = new Object();
    $("#queryarea").bxdiv('setQueryFromDiv', queryParam, "inqu_status");
    $("#jqGrid").bxgrid("option", "queryParam", queryParam);
    if(!isNullOrEmptyOrUndefiend(queryParam.inqu_status.tbmName)){//如果区间名称不为空，则指按照区间名称模糊查询
        queryParam.inqu_status.factory = null;
        queryParam.inqu_status.owner = null;
        queryParam.inqu_status.rmsVersion = null;
        queryParam.inqu_status.ccsType = null;
    }
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
    //对数据做校验，处理。填充入参。
    for (var i = 0; i < selectArray.length; i++) {
        var status = $("#jqGrid").bxgrid('rawMethodCall', "saveRow", selectArray[i]);
        if (status == false) {
            return;
        }
        var startTime = $("tr[role='row'][id='1']").children().get(6).innerHTML;
        if(isNullOrEmptyOrUndefiend(startTime) || "&nbsp;" == startTime){
            alertDiv("提示","项目起始时间为空，请先修改第一行的时间");
            return;
        }
        var saveData = $("#jqGrid").bxgrid('rawMethodCall', "getRowData", selectArray[i]);
        //处理时间列的值
        saveData.planTime = saveData.planTime.trim();
        saveData.startTime = startTime;
        saveData.projectId = $.cookie("selected_id");
        if(saveData.project != 'true' || saveData.end == 'true'){
            if(!isNumber(saveData.ringNum)){
                alertDiv("提示","环号必须是数字");
                return;
            }
        }
        if(isNullOrEmptyOrUndefiend(startTime)){
            return;
        }
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
    AjaxCommunicator.ajaxRequest('/raising/forward/progressManage/workPlan/addRow.do', 'POST', paramJsonObj, callback,true);
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
    confirmDiv("确认删除", "数据删除后将不可恢复，是否确定删除？", buttons);
}

function deleteOK() {
    var paramJsonObj = new Object();
    $("#jqGrid").bxgrid('setInfoFromGrid', paramJsonObj, 'result');
    var rowsArr = paramJsonObj.result.resultRow;
    for(var i =0;i<rowsArr.length;i++){
        var temp =  rowsArr[i];
        if(temp.project == "true"){
            alertDiv("提示","第一行和最后一行为项目记录，不可以删除。");
            return;
        }
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
    AjaxCommunicator.ajaxRequest('/raising/forward/progressManage/workPlan/deleteRows.do', 'POST', paramJsonObj, callback,true);
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
        var status = $("#jqGrid").bxgrid('rawMethodCall', "saveRow", selectArray[i]);
        if (status == false) {
            return;
        }
        var saveData = $("#jqGrid").bxgrid('rawMethodCall', "getRowData", selectArray[i]);
        //处理时间列的值
        saveData.planTime = saveData.planTime.trim();
        if(saveData.project != 'true' || saveData.end == 'true'){
            if(!isNumber(saveData.ringNum)){
                alertDiv("提示","环号必须是数字");
                return;
            }
        }
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
    AjaxCommunicator.ajaxRequest('/raising/forward/progressManage/workPlan/updateRow.do', 'POST', paramJsonObj, callback,true);
}









