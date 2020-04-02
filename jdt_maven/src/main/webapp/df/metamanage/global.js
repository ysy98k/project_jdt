$(document).ready(function () {
        baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog',
            'bxalert', 'bxvalidate'], function () {
            initWidgets();
            var gridOption = {
                caption: '国际化配置',
                colNames: ['', '资源键'],
                colModel: [
                    {
                        name: 'messageId',
                        index: 'messageId',
                        width: 60,
                        hidden: true
                    }, {
                        name: 'messageKey',
                        index: 'messageKey',
                        width: 100,
                        editable: false,
                        sortable: true
                    }],
                sorttable: true,
                sortname: 'messageKey',
                sortorder: 'desc',
                caption: "国际化配置",
                jsonReader: {
                    id: "messageId",
                    repeatitems: false
                },
                subGrid: true,
                subGridRowExpanded: showChildGrid,
                subGridOptions: {
                    plusicon: "ace-icon fa fa-plus",
                    minusicon: "ace-icon fa fa-minus"
                },
            };

            var option = {
                queryParam: {},
                dataPattern: "url",
                url: "/df/metamanage/global.do?method=querykey",
                showMsgOpt: {
                    showMsgId: "alertdiv"
                },
                gridOption: gridOption,
                callBackFunct: gridCallback,
                navGridOption: {
                    edit: true,
                    editicon: 'ace-icon fa fa-pencil blue',
                    editfunc: updateRec,
                    add: true,
                    addicon: 'ace-icon fa fa-plus-circle purple',
                    addfunc: insertRec,
                    del: true,
                    delicon: 'ace-icon fa fa-trash-o red',
                    delfunc: deleteRec,
                    search: false,
                    refresh: false
                }
            };

            $("#messageContent").bxgrid(option);
        });

        $(window).on('resize.jqGrid', function () {
            if($("#table_messageContent").length>0)
                $("#table_messageContent").jqGrid('setGridWidth', $(".page-content").width());
        });

        var $select = $('div[id="template"] #detail_messageLan');
        for (var key in lanMap) {
            var $option = $("<option value='" + key + "'>" + lanMap[key] + "</option>");
            $select.append($option);
        }
    }
);


var messageKeyStore = {};

var lanMap = {
    "zh_CN": "中文",
    "en_US": "英文",
    "ja_JP": "日文"
};

var gridCallback = function (data) {
    //先对data进行判定
    if (data != null && data != undefined) {
        var keys = "";
        for (var i = 0; i < data.rows.length; i++) {
            keys += "'" + data.rows[i].messageKey + "',";
        }
        keys = keys.substring(0, keys.length - 1);
        var paramJsonObj = {};
        paramJsonObj.inqu_status = {
            messageKey: keys
        };
        var callback = {
            onSuccess: function (paramJsonObj) {
                messageKeyStore = {};
                if (paramJsonObj != null && paramJsonObj != undefined) {
                    for (var i = 0; i < paramJsonObj.rows.length; i++) {
                        var key = paramJsonObj.rows[i].messageKey;
                        if (messageKeyStore[key] == undefined) {
                            messageKeyStore[key] = [];
                        }
                        messageKeyStore[key].push(paramJsonObj.rows[i]);
                    }
                }

            }
        };
        AjaxCommunicator.ajaxRequest('/df/metamanage/global.do?method=querybatch',
            'POST', paramJsonObj, callback);
    }
};

var showChildGrid = function (parentRowID, parentRowKey) {
    var selectData = $("#messageContent").bxgrid('rawMethodCall', 'getRowData', parentRowKey);
    if (selectData != null && messageKeyStore[selectData.messageKey] != undefined) {
        var records = messageKeyStore[selectData.messageKey];
        //_fillChildDiv(records, "detail", parentRowID);
        fillChildGrid(records, parentRowID);
    }
};

var fillChildGrid = function (data, parentRowID) {
    var $column = $("#subgrid_column").clone(true);
    $("#" + parentRowID).append($column);
    $column.show();
    for (var i = 0; i < data.length; i++) {
        var record = data[i];
        var $row = $("#subgrid_row").clone();
        for (var key in record) {
            var value = record[key];
            $("#" + parentRowID).append($row);
            $row.find("label[for='detail_" + key + "']").empty();
            if (key === "messageEnable") {
                var icon = "";
                if (value) {
                    icon = "fa-check green";
                } else {
                    icon = "fa-close red";
                }
                $row.find("label[for='detail_" + key + "']").append($('<i style="margin-left: 15px;" class="ace-icon fa ' + icon + ' bigger-130"></i>'));
            } else if (key === "messageLan") {
                $row.find("label[for='detail_" + key + "']").text(value + "[" + lanMap[value] + "]");
            } else {
                $row.find("label[for='detail_" + key + "']").text(value);
            }
        }
        $row.show();
    }
};

//子格明细显示
//function _fillChildDiv(jsonData, blockName, parentRowID) {
//    for (var i = 0; i < jsonData.length; i++) {
//        var $div = $('div[id="template"]');
//        var $klon = $div.clone(true).prop('id', 'fill' + i);
//        $klon.each(function () {
//            //不可以编辑状态
//            $(this).find("[data-bxwidget]").each(function () {
//                $(this).attr("disabled", "true");
//            });
//        });
//        //移除删除按钮
//        $klon.find("button[id='deleteBtn']").parent().remove();
//        $klon.show();
//        $("#" + parentRowID).append($klon);
//
//        //填充数据
//        var keyArray = jsonData[i];
//        for (var attribute in keyArray) {
//            var attributeValue = keyArray[attribute];
//            _setWidgetValue($klon.find("#" + blockName + "-" + attribute), attributeValue);
//        }
//    }
//}

//新增
function insertRec() {
    $("#detailExceptKeyDiv").find('div[id^=detail]').each(function () {
        $(this).remove();
    });
    $("#detail").find("input[id='detail_messageKey']").val("");
    //这里为detail增加一个记录的div，始终存在，且不允许删除
    var $div = $('div[id="template"]');
    var $klon = $div.clone(true).prop('id', 'detail0');
    
  //区分name用于验证模块
    $klon.find("[data-bxwidget]").each(function (){
    	$(this).attr("id",$(this).attr("id") + "0");
    	if($(this).attr("name")!=undefined){//messageId没有定义name属性
    		$(this).attr("name",$(this).attr("name") + "0");
    	}
    });
    
    $klon.show();
    $("#detailExceptKeyDiv").append($klon);

    var buttons = [{
        text: "保存",
        "class": "btn btn-skinColor btn-xs",
        click: function () {
            if ($("#detail").bxvalidate("validate")) {
                if (ifDuplicateInPage($("#detailExceptKeyDiv"), "select[id^='detail_messageLan']")) {//如果重复
                    var alertButtons = [{
                        text: '了解',
                        "class": "btn btn-skinColor btn-xs",
                        click: function () {
                            $("#dialog-update-message").bxdialog('close');
                        }
                    }];
                    var alertDialogOpt = {
                        title: "<i class='ace-icon fa fa-warning orange'></i>  新增警告",
                        dataPattern: 'text',
                        content: "页面中有重复数据，请检查。",
                        buttons: alertButtons
                    };
                    $("#dialog-update-message").bxdialog(alertDialogOpt);
                } else {
                    var paramJsonObjInsert = new Object();
                    var paramJsonObjUpdate = new Object();
                    //批量填充数据
                    setInfoFromDiv($("#detail"), paramJsonObjInsert, paramJsonObjUpdate, 'detail',
                        'messageKey');
                    var callback = {
                        onSuccess: function (paramJsonObjInsert) {
                            $("#detail").dialog("close");
                            var alertButtons = [{
                                text: '了解',
                                "class": "btn btn-skinColor btn-xs",
                                click: function () {
                                    $("#dialog-update-message").bxdialog('close');
                                }
                            }];
                            if (paramJsonObjInsert.status != 0) {//插入失败
                                var alertDialogOpt = {
                                    title: "<i class='ace-icon fa fa-warning orange'></i>  新增警告",
                                    dataPattern: 'text',
                                    content: "插入失败，与已有数据重复！",
                                    buttons: alertButtons
                                };
                                $("#dialog-update-message").bxdialog(alertDialogOpt);
                            } else {//插入成功才需要query和refresh
                                var alertDialogOpt = {
                                    title: "<i class='ace-icon fa fa-warning orange'></i>  成功提示",
                                    dataPattern: 'text',
                                    content: "记录成功插入。",
                                    buttons: alertButtons
                                };
                                $("#dialog-update-message").bxdialog(alertDialogOpt);
                                $("#messageContent").bxgrid("query");
                                $("#messageContent").bxgrid("refresh");
                            }
                        }
                    };
                    AjaxCommunicator.ajaxRequest(
                        '/df/metamanage/global.do?method=insert', 'POST',
                        paramJsonObjInsert, callback);
                }
            }
        }
    }];

    var title = "新增记录";
    var dialogOpt = {
        title: title,
        buttons: buttons,
        width: "700px"
    };
    $("#detail").bxdialog(dialogOpt);
}

//界面数据是否重复的检测
function ifDuplicateInPage(mainPage, duplicateParam) {
    var lanArrayUnique = [];
    var count = 0;
    mainPage.find(duplicateParam).each(function () {
        var lanTemp = $(this).val();
        count++;
        if ($.inArray(lanTemp, lanArrayUnique) == -1) {//=-1表示不存在
            lanArrayUnique.push(lanTemp);
        }
    });
    if (lanArrayUnique.length < count) {//有重复
        return true;
    }
    return false;
}


function deleteRec() {
    dialogMessage("确认删除", "数据删除后将不可恢复，是否确定删除");
}

function dialogMessage(title, centext) {
    $("#dialogInfo").html(centext);
    var dialog = $("#dialog-message")
        .removeClass('hide')
        .dialog(
            {
                modal: true,
                title: "<div class='widget-header'><h4 class='smaller'><i class='ace-icon fa fa-exclamation-triangle red'></i> "
                + title + "</h4></div>",
                title_html: true,
                buttons: [{
                    text: "确认",
                    "class": "btn btn-skinColor btn-xs",
                    click: function () {
                        deleteOK(SUBGRID, new Object);//SUBGRID：按key删除
                        $(this).dialog("close");
                    }
                }]
            });
}


var DETAILGRID = "detailGrid";
var SUBGRID = "subGrid";
//删除
function deleteOK(mark, deleteRow) {
    var paramJsonObj = new Object();
    if (mark == SUBGRID) {
        deleteSubRecordsByKey("messageContent", paramJsonObj, 'result');
    } else if (mark == DETAILGRID) {
        deleteDetailRecord("detail", paramJsonObj, 'result', deleteRow);
    }
    var callback = {
        onSuccess: function (paramObjDelete) {
            if (mark == SUBGRID) {//执行删除后立马要刷新数据,detail页面的删除跟随update和insert的保存键一起刷新
                var showMsgOpt = {
                    showMsgId: "alertdiv",
                    status: paramObjDelete.status,
                    showMsg: paramObjDelete.returnMsg
                };
                $("#messageContent").bxgrid("option", "showMsgOpt", showMsgOpt);
                $("#messageContent").bxgrid("query");
                $("#messageContent").bxgrid("refresh");
            }
        }
    };
    AjaxCommunicator.ajaxRequest(
        '/df/metamanage/global.do?method=delete', 'POST',
        paramJsonObj, callback);
}

//按key进行删除
function deleteSubRecordsByKey(mainPageId, paramJsonObj, blockName) {//blockName='result'
    var block = paramJsonObj[blockName];
    if (!isAvailable(block)) {
        block = new Object();
        paramJsonObj[blockName] = block;
    }
    var resultRow = new Array();
    var selectIdArray = $("#" + mainPageId).bxgrid('rawMethodCall', 'getGridParam',
        'selarrrow');
    for (var i = 0; i < selectIdArray.length; i++) {
        //var status = $("#" + mainPageId).bxgrid('rawMethodCall', "saveRow", selectIdArray[i]);
        var selectData = $("#" + mainPageId).bxgrid('rawMethodCall', 'getRowData',selectIdArray[i]);
        if (selectData != null && messageKeyStore[selectData.messageKey] != undefined) {
            //找到某个key对应的所有记录
            var records = messageKeyStore[selectData.messageKey];
            for (var j = 0; j < records.length; j++) {
                resultRow.push(records[j]);
            }
        }
    }
    block["resultRow"] = resultRow;
}

//detail页面的统一delete
$(function () {
    $("#deleteBtn").click(function () {
        if ($("#detailExceptKeyDiv").children().length == 2) {//判定是否只剩一个
            var buttons = [{
                text: '了解',
                "class": "btn btn-skinColor btn-xs",
                click: function () {
                    $("#dialog-update-message").bxdialog('close');
                }
            }];
            var dialogOpt = {
                title: "<i class='ace-icon fa fa-warning orange'></i>  删除警告",
                dataPattern: 'text',
                content: "请至少保留一条数据",
                buttons: buttons
            };
            $("#dialog-update-message").bxdialog(dialogOpt);
            return;
        } else {
        	var deleteRow = $(this).parent().parent().parent();
            var messageId_value = deleteRow.find('input[id^="detail_messageId"]').attr("value");
            
        	//添加删除警告
        	var buttons = [{
                text: '确认',
                "class": "btn btn-skinColor btn-xs",
                click: function () {
                	$("#dialog-update-message").bxdialog('close');
                    if (messageId_value != undefined) {//如果value未定义，表示是空div，反之需要删除数据
                        deleteOK(DETAILGRID, deleteRow);
                    }
                    //删除div
                    deleteRow.remove();
                  
                }
            }];
            var dialogOpt = {
                title: "<i class='ace-icon fa fa-warning orange'></i>  确认删除",
                dataPattern: 'text',
                content: "数据删除后将不可恢复，是否确定删除",
                buttons: buttons
            };
            $("#dialog-update-message").bxdialog(dialogOpt);
            return;
        }
    });
});

//deletebtn真正删除数据的部分
function deleteDetailRecord(mainPageId, paramJsonObj, blockName, deleteRow) {
    var detailPage = $("#" + mainPageId);
    var widgetObj = detailPage;
    var block = paramJsonObj[blockName];//detail
    if (!isAvailable(block)) {
        block = new Object();
        paramJsonObj[blockName] = block;
    }
    var key_value = detailPage.find("input[id='detail_messageKey']").val();
    var resultRow = new Array();
    var oneRow = new Object();

    deleteRow.find("[data-bxwidget]").each(function () {
        var widgetIdArray = this.id.split('_');
        if (widgetIdArray.length == 2 && widgetIdArray[0] == "detail") {
        	attributeKey = widgetIdArray[1].substring(0,widgetIdArray[1].length-1);
            if ($(this).data("bxtype") == "number") {
                oneRow[attributeKey] = Number(_getWidgetValue($(this)));
            } else if ($(this).data("bxtype") == "boolean") {
                oneRow[attributeKey] = _getBoolean(_getWidgetValue($(this)));
            } else {
                oneRow[attributeKey] = String(_getWidgetValue($(this)));
            }
        }
    });
    oneRow['messageKey'] = key_value;
    resultRow.push(oneRow);
    block["resultRow"] = resultRow;
}


//编辑
function updateRec() {
    var selectArray = $("#messageContent").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if (selectArray.length > 1) {
        var buttons = [
            {
                text: '了解',
                "class": "btn btn-skinColor btn-xs",
                click: function () {
                    $("#dialog-update-message").bxdialog('close');
                }
            }
        ];

        var dialogOpt = {
            title: "<i class='ace-icon fa fa-warning orange'></i>  记录数错误",
            dataPattern: 'text',
            content: "记录数出错，只能选择一个资源键进行修改！",
            buttons: buttons
        };

        $("#dialog-update-message").bxdialog(dialogOpt);
        return;
    }

    var title = '修改记录';
    var buttons = [{
        text: "保存所有修改",
        "class": "btn btn-skinColor btn-xs",
        click: function () {
            if ($("#detail").bxvalidate("validate")) {//页面数据是否重复的检查
                if (ifDuplicateInPage($("#detailExceptKeyDiv"), "select[id^='detail_messageLan']")) {//如果重复
                    var alertButtons = [{
                        text: '了解',
                        "class": "btn btn-skinColor btn-xs",
                        click: function () {
                            $("#dialog-update-message").bxdialog('close');
                        }
                    }];
                    var alertDialogOpt = {
                        title: "<i class='ace-icon fa fa-warning orange'></i>  修改警告",
                        dataPattern: 'text',
                        content: "页面中有重复数据，请检查",
                        buttons: alertButtons
                    };
                    $("#dialog-update-message").bxdialog(alertDialogOpt);
                } else {
                    //编辑界面存在两种类型的记录
                    var paramJsonObjUpdate = new Object();
                    var paramJsonObjInsert = new Object();
                    var status = new Object();
                    //批量读取数据
                    setInfoFromDiv($("#detail"), paramJsonObjInsert, paramJsonObjUpdate, 'detail',
                        'messageKey');

                    var callbackUpdate = {
                        onSuccess: function (paramUpdate) {
                            status = paramUpdate.status;
                        }
                    };

                    var callbackInsert = {
                        onSuccess: function (paramInsert) {
                            status = status || paramInsert.status;
                        }
                    };

                    var ajaxUpdate = function (dfr) {
                        var dfr = $.Deferred();
                        AjaxCommunicator.ajaxRequest('/df/metamanage/global.do?method=update', 'POST',
                            paramJsonObjUpdate, callbackUpdate);
                        dfr.resolve();
                        return dfr.promise();
                    };

                    var ajaxInsert = function (dfr) {
                        var dfr = $.Deferred();
                        AjaxCommunicator.ajaxRequest('/df/metamanage/global.do?method=insert', 'POST',
                            paramJsonObjInsert, callbackInsert);
                        dfr.resolve();
                        return dfr.promise();
                    };

                    $.when(ajaxUpdate(), ajaxInsert()).done(function (showMsg) {//当插入和修改都执行完毕
                        if (status == 0) {//TODO
                            $("#messageContent").bxgrid("query");//插入或者修改成功，就要重新query
                        }
                        $("#messageContent").bxgrid("refresh");
                        $("#detail").dialog("close");
                    });
                }

            }
        }
    }];

    var dialogOpt = {
        title: title,
        buttons: buttons,
        width: "700px"
    };

    //显示原有记录
    var selectId = $("#messageContent").bxgrid('rawMethodCall', 'getGridParam', 'selrow');
    var selectData = $("#messageContent").bxgrid('rawMethodCall', 'getRowData', selectId);
    if (selectData != null && messageKeyStore[selectData.messageKey] != undefined) {
        //找到某个key对应的所有记录
        var records = messageKeyStore[selectData.messageKey];
        fillUpdateDiv(records, "detail", "detailExceptKeyDiv");
        $("#detail").bxdialog(dialogOpt);
    }
}

function fillUpdateDiv(jsonData, blockName, parentDivID) {
    $("#detailExceptKeyDiv").find('div[id^=detail]').each(function () {
        $(this).remove();
    });

    var parentDiv = $("#" + parentDivID);
    if (jsonData != undefined && jsonData != null && parentDiv != null) {
        //先填充key
        var keyValue = jsonData[0].messageKey;
        _setWidgetValue(parentDiv.parent().find("input[id='detail_messageKey']"), keyValue);

        //填充其他行的数据
        for (var i = 0; i < jsonData.length; i++) {
            //创建新div
            var $div = $('div[id="template"]');
            var $klon = $div.clone(true).prop('id', 'detail' + i);

            //区分name用于验证模块
            $klon.find("[data-bxwidget]").each(function (){
            	$(this).attr("id",$(this).attr("id") + i.toString());
            	if($(this).attr("name")!=undefined){//messageId没有定义name
            		$(this).attr("name",$(this).attr("name") + i.toString());
            	}
            });
            
            $klon.show();
            parentDiv.append($klon);

            //填充属性的值
            var keyArray = jsonData[i];
            for (var attribute in keyArray) {
                var attributeValue = keyArray[attribute];
                _setWidgetValue($klon.find("#" + blockName + "_" + attribute + i.toString()), attributeValue);
            }
            ;
        }
        ;
    }

}


$(function () {
    $("#addBtn").click(function () {
        //取出最后一个div的id并且增1
        var $div = $('div[id^="detail"]:last');
        var num = parseInt($div.prop("id").match(/\d+/g), 10) + 1;
        //不能用$div复制，否则会出现messageId重复，删除时也无法区分是否要调用sql的delete
        var $template = $('div[id="template"]');
        var $klon = $template.clone(true).prop('id', 'detail' + num);
        
        //区分name用于验证模块
        $klon.find("[data-bxwidget]").each(function (){
        	$(this).attr("id",$(this).attr("id") + num.toString());
        	if($(this).attr("name")!=undefined){//messageId没有定义name
        		$(this).attr("name",$(this).attr("name") + num.toString());
        	}
        });
        
        
        $klon.show();
        $("#detailExceptKeyDiv").append($klon);
        
        //验证
        var ruleOptionCustom = {};
        var rules = {};
        
        rules["detailmessageValue"+num]={
            required: true,
            stringCheck: true
        };
        rules["detailmessageDesc"+num]={
                required: false,
                stringCheck: false
        };
        
        ruleOptionCustom.rules = rules;
        $("#detail").bxvalidate("option", "ruleOptionCustom", ruleOptionCustom);
    });
});


//批量读取数据
function setInfoFromDiv(detailPage, paramJsonObjInsert, paramJsonObjUpdate, blockName, commonParam) {
    var widgetObj = detailPage;//$("#detail");
    var blockInsert = paramJsonObjInsert[blockName]; //用于插入的记录
    var blockUpdate = paramJsonObjUpdate[blockName]; //用于更新的数据

    if (!isAvailable(blockInsert)) {
        blockInsert = new Object();
        paramJsonObjInsert[blockName] = blockInsert;
    }

    if (!isAvailable(blockUpdate)) {
        blockUpdate = new Object();
        paramJsonObjUpdate[blockName] = blockUpdate;
    }

    var resultRowInsert = new Array();
    var resultRowUpdate = new Array();

    var key_value = detailPage.find("input[id='detail_messageKey']").val();
    // 找到每一条记录
    $("#detailExceptKeyDiv").find("div[id^='detail']").each(function () {
        var oneRow = new Object();
        // 找到该记录下的参数值
        $(this).find("[data-bxwidget]").each(function () {
            var widgetIdArray = this.id.split('_');
            if (widgetIdArray.length == 2 && widgetIdArray[0] == blockName) {
            	attributeKey = widgetIdArray[1].substring(0,widgetIdArray[1].length-1);
            	if ($(this).data("bxtype") == "number") {
                    oneRow[attributeKey] = Number(_getWidgetValue($(this)));
                } else if ($(this).data("bxtype") == "boolean") {
                    oneRow[attributeKey] = _getBoolean(_getWidgetValue($(this)));
                } else {
                    oneRow[attributeKey] = String(_getWidgetValue($(this)));
                }
            }
        });
        oneRow['messageKey'] = key_value;

        var messageId_value = $(this).find('input[id^="detail_messageId"]').attr("value");
        if (messageId_value == undefined || messageId_value == "") {//新增
            resultRowInsert.push(oneRow);
        } else {//修改
            resultRowUpdate.push(oneRow);
        }
    });

    blockInsert["resultRow"] = resultRowInsert;
    blockUpdate["resultRow"] = resultRowUpdate;
}

//查询
function on_query_click() {
    var queryParam = new Object();
    $("#queryarea").bxdiv('setQueryFromDiv', queryParam, "inqu_status");
    $("#messageContent").bxgrid("option", "queryParam", queryParam);
    $("#messageContent").bxgrid("query");
}

function initWidgets() {
    $("#queryarea").bxdiv();
    $("#detail").bxdiv();
    $("#detail").bxvalidate();
    
    
    var ruleOptionCustom = {
        rules: {
            detailmessageKey: {
                required: true,
                englishCheck: true
            }/*,*/
//            detailmessageValue: {
//                required: true,
//                stringCheck: true
//            },
//            detailmessageDesc: {
//                required: false,
//                englishCheck: false
//            },
//            detailmessageLan: {
//                required: false,
//                englishCheck: false
//            },
//            detailmessageEnable: {
//                required: false,
//                englishCheck: false
//            }
        }
    };
    $("#detail").bxvalidate("option", "ruleOptionCustom", ruleOptionCustom);
}

function _getWidgetValue(widget) {//得到控件的值，这里判断了bxcombobox的情况
    if (widget.data("bxwidget") == "bxcombobox") {
        return widget.bxcombobox("selectObj").val();
    } else if (widget.data("bxwidget") == "checkbox") {
        return widget.is(':checked');
    } else {
        return widget.val();
    }
}


function _getBoolean(value) {
    if (value == "true") {
        return true;
    } else if (value == "false") {
        return false;
    } else {
        return Boolean(value);
    }
}

function _getBoolean(value) {
    if (value == "true") {
        return true;
    } else if (value == "false") {
        return false;
    } else {
        return Boolean(value);
    }
}

//设置控件的值，这里判断了bxcombobox的情况
function _setWidgetValue(widget, value) {
    if (widget.data("bxwidget") == "bxcombobox") {
        widget.bxcombobox("selectObj").val(String(value));
    } else if (widget.data("bxwidget") == "select") {
        widget.val(String(value));
    } else if (widget.data("bxwidget") == "checkbox") {
        if (value) {
            widget.attr("checked", "checked");
        } else {
            widget.removeAttr("checked");
        }
    } else {
        widget.val(value);
    }
}
