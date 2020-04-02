

$(document).ready(function () {


    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'],
        function () {
            $("#queryarea").bxdiv();
            var gridOption = {
                primaryRowKey: "mobileId",
                caption: false,
                colNames: ['mobileId', '名字', '平台用户','绑定','电话','组名','可用'],
                colModel: [{
                    name: 'mobileId',
                    index:"mobileId",
                    width: 5,
                    hidden: true,
                    forbidCopy: false
                },{
                        name: 'name',
                        index: 'name',
                        width: 40,
                        editable: true,
                        sortable: true,
                        editrules: {
                            required: true,
                        }
                    },
                {
                    name: 'userName',
                    index: 'userName',
                    width: 30, editable: true, edittype:'select', editoptions: {value: getUserNames() }
                },
                {name:'bind', index:'bind',width: 30,editable: false,readOnly:true
                    ,formatter: function (value, grid, row, state) {
                        if(row.bind == true || row.bind == "true"){
                            return "绑定";
                        }else{

                            return "未绑定";
                        }
                    }
                },
                {name:'phone', index:'phone',width: 40,editable: true,editrules: {required: true,custom_func: checkGridMobile}},
                {name:'groupName', index:'groupName',width: 50,editable: false,readOnly:true},
                {name:'enable', index:'enable',width: 50,editable: true,edittype:'select', editoptions: {value: "true:可用; false:不可用"}
                    ,formatter: function (value, grid, row, state) {
                        if(row.enable == true || row.enable == "true"){
                            return "可用";
                        }else{
                            return "不可用";
                        }
                    }
                }
                ],
                sortorder: 'asc',
                height: 351,
                jsonReader: {
                    id: "mobileId",
                    repeatitems: false
                }
            };
            var defaultData = [{
                label: "全部",
                value: ""
            }];
            $("#inqu_status-pageType").bxcombobox({
                dataPattern: 'ccs',
                ccsId: "metadata.pageType",
                async: false,
                data: defaultData
            });
            var option = {
                queryParam: {},
                dataPattern: "url",
                mtype:"get",
                url: "/raising/backstage/mobile/getRows.do",
                showMsgOpt: {
                    showMsgId: "alertdiv"
                },
                gridOption: gridOption
            };
            $("#jqGrid").bxgrid(option);


        });
    $("#inqu_status-projectName").keypress(function () {
        onKeyQuery();
    });
    $("#inqu_status-pageCname").keypress(function () {
        onKeyQuery();
    });


});

function getUserNames() {
    var returnStr = "";
    $.ajax({
        async: false,
        type: "post",
        url: toolkitPath +"/raising/backstage/mobile/getUsers.do",
        success: function (data) {
            if(data.length > 0) {
                for (i = 0; i < data.length; i++) {
                    if (i < data.length - 1) {
                        returnStr += data[i].userId+"_"+data[i].tenantid+"_"+data[i].groupName + ":" + data[i].userName + ";";
                    } else {
                        returnStr += data[i].userId+"_"+data[i].tenantid+"_"+data[i].groupName + ":" + data[i].userName;
                    }
                }
            }
        }
    });
    return returnStr;

}

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
    //对数据做校验，处理。填充入参。
    for (var i = 0; i < selectArray.length; i++) {
        var userName = $("#"+selectArray[i]).find("option:selected").val();
        var enable = $("#"+selectArray[i]).find("select[name='enable']").val();
        var status = $("#jqGrid").bxgrid('rawMethodCall', "saveRow", selectArray[i]);
        if (status == false) {
            return;
        }
        var saveData = $("#jqGrid").bxgrid('rawMethodCall', "getRowData", selectArray[i]);
        saveData.userName =userName;
        saveData.enable = enable;
        delete saveData.bind;
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
    AjaxCommunicator.ajaxRequest('/raising/backstage/mobile/add.do', 'POST', paramJsonObj, callback,true);
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
    AjaxCommunicator.ajaxRequest('/raising/backstage/mobile/delete.do', 'POST', paramJsonObj, callback,true);
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
        var userName = $("#"+selectArray[i]).find("option:selected").val();
        var enable = $("#"+selectArray[i]).find("select[name='enable']").val();
        var status = $("#jqGrid").bxgrid('rawMethodCall', "saveRow", selectArray[i]);
        if (status == false) {
            return;
        }
        var saveData = $("#jqGrid").bxgrid('rawMethodCall', "getRowData", selectArray[i]);
        saveData.userName =userName;
        saveData.enable = enable;
        delete saveData.bind;
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
    AjaxCommunicator.ajaxRequest('/raising/backstage/mobile/update.do', 'POST', paramJsonObj, callback,true);
}











