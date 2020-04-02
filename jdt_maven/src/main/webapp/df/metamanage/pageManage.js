var designurl = '../designer/designer.do?pagename=';

$(document).ready(function () {
    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'],
            function () {
                $("#queryarea").bxdiv();
                var gridOption = {
                    primaryRowKey: "pageId",
                    caption: false,
                    colNames: ['页面序列号', '页面英文名', '页面中文名', '页面类型', '页面路径', '页面描述', '操作'],
                    colModel: [{
                        name: 'pageId',
                        index: 'pageId',
                        width: 10,
                        hidden: true,
                        forbidCopy: true
                    },
                        {
                            name: 'pageEname',
                            index: 'pageEname',
                            width: 50,
                            sortable: true,
                            editrules: {
                                required: true,
                                custom: true,
                                custom_func: bxgrid_englishCheck
                            }
                        },
                        {
                            name: 'pageCname',
                            index: 'pageCname',
                            width: 50,
                            editable: true,
                            sortable: true,
                            editrules: {
                                required: true,
                                custom: true,
                                custom_func: bxgrid_stringCheck

                            }
                        },
                        {
                            name: 'pageType',
                            index: 'pageType',
                            width: 50,
                            edittype: 'ccs',
                            editoptions: 'metadata.pageType',
                            editable: true,
                            sortable: false,
                            editrules: {
                                required: true
                            }
                        },
                        {
                            name: 'pagePath',
                            index: 'pagePath',
                            width: 130,
                            sortable: false,
                            editable: true
                        },
                        {
                            name: 'pageDesc',
                            index: 'pageDesc',
                            width: 100,
                            sortable: false,
                            editable: true
                        },
                        {
                            name: 'opt',
                            index: 'opt',
                            width: 45,
                            sortable: false,
                            readOnly: true,
                            forbidExport: true,
                            formatter: function (value, grid, row, state) {// (状态)格式化程序
                                if (row.pageType == "designPage") {
                                    var btnReset = "<div style='text-align:center'><input class='btn btn-block'  type='button' value='设&nbsp&nbsp&nbsp计' "
                                            + "style='width:70px;border:none;'"
                                            + "onclick='designRow("
                                            + "\""
                                            + grid.rowId
                                            + "\");'/></div>";
                                    return btnReset;
                                } else {
                                    return "";
                                }

                            }
                        }],
                    sortorder: 'asc',
                    height:351,
                    jsonReader: {
                        id: "pageId",
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
                    url: "/df/metamanage/pageManage.do?method=query",
                    showMsgOpt: {
                        showMsgId: "alertdiv"
                    },
                    gridOption: gridOption,
                    navGridOption: {
                        download: true,
                        downloadParam: {
                            downloadUrl: "/df/metamanage/pageManage.do/download.do"
                        },
                        upload: true,
                        uploadParam: {
                            uploadUrl: "/df/metamanage/pageManage.do?method=insertForUpload"
                        }
                    }
                };
                $("#jqGrid").bxgrid(option);

            });
    $("#inqu_status-pageEname").keypress(function () {
        onKeyQuery();
    });
    $("#inqu_status-pageCname").keypress(function () {
        onKeyQuery();
    });
});


function designRow(rowId) {
    //没保存不跳转
    if(!$("#jqGrid").bxgrid("whetherValidRow",rowId)){
        return;
    }
    var selectData = $("#jqGrid").bxgrid('rawMethodCall', 'getRowData', rowId);
    window.open(designurl + selectData.pageEname);
}

function addAndCopy(){
    $("#jqGrid").bxgrid("addAndCopy");
}

function saveRec() {
    var insertOrUpdate = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'insertOrUpdate');
    if(insertOrUpdate == "insert"){
        insertRec();
    }else{
        updateRec();
    }
}

function insertRec() {
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
    AjaxCommunicator.ajaxRequest('/df/metamanage/pageManage.do?method=insert', 'POST', paramJsonObj, callback);
}

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
    AjaxCommunicator.ajaxRequest('/df/metamanage/pageManage.do?method=delete', 'POST', paramJsonObj, callback);
}

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
        if ("designPage" == saveData.pageType) {
            saveData.pagePath = "/df/designer/viewpage.do?pagename=" + saveData.pageEname;
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
    AjaxCommunicator.ajaxRequest('/df/metamanage/pageManage.do?method=update', 'POST', paramJsonObj, callback);
}

function on_query_click() {
    var queryParam = new Object();
    $("#queryarea").bxdiv('setQueryFromDiv', queryParam, "inqu_status");
    $("#jqGrid").bxgrid("option", "queryParam", queryParam);
    $("#jqGrid").bxgrid("query");
}

function onKeyQuery() {
    var e = window.event || arguments.callee.caller.arguments[0];
    var keyCode = e.keyCode || e.which; // 按键的keyCode
    if (keyCode == 13) {
        on_query_click();
    }
}

function on_grantresource_click() {
    var selectArray = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if (selectArray.length < 1) {
        alertDiv("记录数错误", "请至少选择一条记录进行授权！");
        return;
    }

    var selectedResource = [];
    for (var i = 0; i < selectArray.length; i++) {
        var insertResource = {};
        var selectData = $("#jqGrid").bxgrid("getResotreRowDataById", selectArray[i]);
        if (selectData == undefined) {
            continue;
        }
        insertResource.name = selectData.pageEname;
        insertResource.display_name = selectData.pageCname;
        insertResource.description = selectData.pageDesc;
        insertResource.service = 'JDT';
        selectedResource.push(insertResource);
    }
    var paramJsonObj = {};
    paramJsonObj.selectedResource = selectedResource;
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
    AjaxCommunicator.ajaxRequest('/df/metamanage/pageManage.do?method=grantResource', 'POST', paramJsonObj, callback);
};

function importPages() {
    var fileId = "desingerPages";
    var excelReg = /(\.xml)$/;
    var buttons = [
        {
            html: "<i class='ace-icon fa fa-check bigger-110'></i>&nbsp;导入",
            class: "btn btn-skinColor btn-xs",
            click: function () {
                if (!excelReg.test($("#desingerPages").val())) {
                    $(this).dialog("close");
                    alertDiv("警告", "请导入 xml 文件");
                    return;
                }
                $.ajaxFileUpload(
                        {
                            url: toolkitPath + "/df/designer/fileserver.do?method=importDesinger", //用于文件上传的服务器端请求地址
                            secureuri: false, //是否需要安全协议，一般设置为false
                            fileElementId: fileId, //文件上传域的ID
                            dataType: 'json', //返回值类型 一般设置为json
                            success: function (data, status)  //服务器成功响应处理函数
                            {
                                if (data.status != 0) {
                                    alertDiv("警告", data.returnMsg);
                                    return;
                                }

                                var resultMsg = data.returnMsg;
                                if (data.countError) {
                                    resultMsg += '<br/>导入失败' + data.countError + '条页面信息！' +
                                            '<p><span style="color: #c43c35; cursor: pointer" onclick="$(' + "'#importErrorDetail'" + ').toggle(400)"> 详细信息>></span><br/>' +
                                            '<div id="importErrorDetail" style="display: none; color: #c43c35; max-height: 180px; overflow-y: auto"> ' + data.errorMsg + '</div></p>';
                                }
                                alertDiv("提示", resultMsg);
                                $("#jqGrid").bxgrid("query");
                            },
                            error: function (data, status, e)//服务器响应失败处理函数
                            {
                                alertDiv("警告", "系统错误，请联系管理员！");
                            }
                        }
                );
                $(this).dialog("close");
            }
        }
    ];
    uploadDiv({
        title: "导入设计页面",
        buttons: buttons,
        fileId: fileId,
        no_icon: 'ace-icon fa fa-download',
        btn_choose: "点击导入设计页面",
    });
}

function exportPages() {
    var selarrrow = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if (selarrrow.length < 1) {
        alertDiv("警告", "请选择要导出的设计器页面");
        return;
    }

    var pagelist = "";
    for (var i = 0; i < selarrrow.length; i++) {
        var rowid = selarrrow[i];
        var rowData = $("#jqGrid").bxgrid("getResotreRowDataById", rowid);
        if (rowData == undefined) {
            continue;
        }
        if ("designPage" != rowData.pageType) {
            alertDiv("警告", rowData.pageEname + "不是设计器页面");
            return;
        }
        pagelist += rowData.pageEname + ",";
    }
    downloadFile("/df/designer/fileserver.do/exportDesinger.do?pagelist=" + pagelist);
}