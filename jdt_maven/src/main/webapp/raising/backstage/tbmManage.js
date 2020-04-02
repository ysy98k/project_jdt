

$(document).ready(function () {
    initQuery();

    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'],
        function () {
            $("#queryarea").bxdiv();
            var gridOption = {
                primaryRowKey: "tbmId",
                caption: false,
                colNames: ['盾构机Id', '盾构机名称', '盾构机厂家','盾构机类型','铰接类型','联系人','电话','所属单位','导向系统版本', '盾构机直径','生产时间','创建时间'],
                colModel: [{
                    name: 'tbmId',
                    index: 'tbmId',
                    width: 5,
                    hidden: true,
                    forbidCopy: false
                },
                    {
                        name: 'tbmName',
                        index: 'tbmName',
                        width: 40,
                        editable: true,
                        sortable: true,
                        editrules: {
                            required: true,
                        }
                    },
                    {
                        name: 'factory',
                        index: 'factory',
                        width: 70,
                        editable: true
                    },
                    {name:'tbmType', index:'tbmType',width: 30, editable: true, edittype:'select', editoptions: {value: getTbmType() }},
                    {name:'hingeType', index:'hingeType',width: 30,editable: true,edittype:'select', editoptions: {value: "主动:主动; 被动:被动"}},
                    {name:'contacts', index:'contacts',width: 40,editable: true},
                    {name:'phone', index:'phone',width: 50,editable: true},
                    {name:'owner', index:'owner',width: 50,editable: true,editrules: {required: true}},
                    {name:'rmsVersion', index:'rmsVersion',width: 50,editable: true,editrules: {required: true}},
                    {
                        name: 'diameter',
                        index: 'diameter',
                        width: 25,
                        editable: true,
                        sortable: true,
                        editrules: {
                            required: true,
                        }
                    },
                    {
                        name: 'produceTime',
                        index: 'produceTime',
                        width: 30,
                        editable: true,
                        sortable: true,
                        editrules: {
                            required: true,
                            custom: true,
                            custom_func: createDateCheck
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
                    {
                        name: 'createTime',
                        index: 'createTime',
                        width: 50,
                        editable: true,
                        sortable: true,
                        editrules: {
                            required: true,
                            custom: true,
                            custom_func: createTimeCheck
                        },
                        editoptions: {
                            size:100,maxlengh:200,length:150,
                            dataInit:function(element){
                                element.value = isNullOrEmptyOrUndefiend(element.value) == true ? moment().format('YYYY-MM-DD HH:mm:ss') : element.value;
                                //日期控件初始化
                                $(element).daterangepicker(getDateRangPickerOption(element.value,element.value), function(start, end, label) {
                                    var s  = start.format('YYYY-MM-DD HH:mm:ss');
                                    element.value = s;
                                });

                            }
                        }
                    }
                ],
                sortorder: 'asc',
                height: 351,
                jsonReader: {
                    id: "tbmId",
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
                url: "/raising/backstage/tbm/getTbmInfos.do",
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

/**
 * 初始化查询条件
 */
function initQuery(){
    $.ajax({
        async: false,
        type: "get",
        url: toolkitPath +"/raising/backstage/tbm/initQuery.do",
        data:{"tbmCode":"tbmtype"},
        success: function (data) {
            var tbmTypesList =  data.tbmTypesList;
            if(data.haveData == "0"){//如果返回了数据
                var ownersList =  data.ownersList;
                var rmsVersionsList =  data.rmsVersionsList;
                var factorysList =  data.factorysList;
                for (var i = 0; i < factorysList.length; i++) {
                    $("#inqu_status-factory").append("<option value='"+factorysList[i]+"'>"+factorysList[i]+"</option>");
                }
                for (var i = 0; i < ownersList.length; i++) {
                    $("#inqu_status-owner").append("<option value='"+ownersList[i]+"'>"+ownersList[i]+"</option>");
                }
                for (var i = 0; i < rmsVersionsList.length; i++) {
                    $("#inqu_status-rmsVersion").append("<option value='"+rmsVersionsList[i]+"'>"+rmsVersionsList[i]+"</option>");
                }
            }
            for (var i = 0; i < tbmTypesList.length; i++) {
                $("#inqu_status-ccsType").append("<option value='"+tbmTypesList[i].ccsId+"'>"+tbmTypesList[i].ccsStr+"</option>");
            }
        }
    });

}

function getTbmType(code) {
    var returnStr = "";
    $.ajax({
        async: false,
        type: "post",
        url: toolkitPath +"/raising/backstage/tbm/getTbmType.do",
        data:{"code":"tbmtype"},
        success: function (data) {
            for (var i = 0; i < data.length; i++) {
                if (i < data.length - 1) {
                    returnStr += data[i].ccsId + ":" + data[i].ccsStr + ";";
                } else {
                    returnStr += data[i].ccsId + ":" + data[i].ccsStr;
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
        var tbmType = $("#"+selectArray[i]).find("option:selected").val();
        var status = $("#jqGrid").bxgrid('rawMethodCall', "saveRow", selectArray[i]);
        if (status == false) {
            return;
        }
        var saveData = $("#jqGrid").bxgrid('rawMethodCall', "getRowData", selectArray[i]);
        //处理时间列的值
        saveData.createTime = saveData.createTime.trim();
        if(tbmType != null && tbmType != "" && tbmType != undefined){
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
            $("#jqGrid").bxgrid("option", "showMsgOpt", showMsgOpt);
            $("#jqGrid").bxgrid("query");
        }
    };
    AjaxCommunicator.ajaxRequest('/raising/backstage/tbm/addTbmInfos.do', 'POST', paramJsonObj, callback,true);
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
    AjaxCommunicator.ajaxRequest('/raising/backstage/tbm/deleteTbmInfos.do', 'POST', paramJsonObj, callback,true);
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
        var tbmType = $("#"+selectArray[i]).find("option:selected").val();
        var status = $("#jqGrid").bxgrid('rawMethodCall', "saveRow", selectArray[i]);
        if (status == false) {
            return;
        }
        var saveData = $("#jqGrid").bxgrid('rawMethodCall', "getRowData", selectArray[i]);

        //处理时间列的值
        saveData.createTime = saveData.createTime.trim();
        if(tbmType != null && tbmType != "" && tbmType != undefined){
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

            $("#jqGrid").bxgrid("option", "showMsgOpt", showMsgOpt);
            $("#jqGrid").bxgrid("query");
        }
    };
    AjaxCommunicator.ajaxRequest('/raising/backstage/tbm/updateTbmInfos.do', 'POST', paramJsonObj, callback,true);
}

//检验生产日期单元格
function createDateCheck(val,name){
    //验证时间格式为：2012-01-31忽略前后空格。
    var reDateTime = /^(\d{4})\-(\d{2})\-(\d{2})$/;
    if(!reDateTime.test(val.trim()) ){
        return [false, "列【" + name + "】的值【" + val+ "】格式出错：格式应该为2000-01-01", ""];
    }
    return [true,""];
}

//检验时间单元格
function createTimeCheck(val,name){
    //验证时间格式为：2012-01-31 09:00:22,忽略前后空格。
    var reDateTime = /^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1])) (?:(?:[0-2][0-3])|(?:[0-1][0-9])):[0-5][0-9]:[0-5][0-9]$/;
    if(!reDateTime.test(val.trim()) ){
        return [false, "列【" + name + "】的值【" + val+ "】格式出错：格式应该为2000-01-01 00:00:00", ""];
    }
    return [true,""];
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








