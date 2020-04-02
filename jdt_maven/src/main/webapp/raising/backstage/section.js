var designurl = '../designer/designer.do?pagename=';
var secId ;
$(document).ready(function () {
    initSelect();

    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'],
        function () {
            $("#queryarea").bxdiv();
            var gridOption = {
                primaryRowKey: "sectionId",
                caption: false,
                colNames: ['区间序列号', '区间名称','业主','创建时间', '区间类型','城市线路','地图线路'],
                colModel: [{
                    name: 'sectionId',
                    index: 'sectionId',
                    width: 10,
                    hidden: true,
                    forbidCopy: true
                },
                    {
                        name: 'sectionName',
                        index: 'sectionName',
                        width: 50,
                        editable: true,
                        sortable: true,
                        editrules: {
                            required: true,
                        }
                    },
                    {
                        name: 'owner',
                        index: 'owner',
                        width: 50,
                        editable: true,
                        sortable: true,
                        editrules: {
                            required: true,
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
                    },
                    {
                        name: 'ccsSectionType',
                        index: 'ccsSectionType',
                        width: 50,
                        editable: true,
                        edittype:'select',
                        editrules: {
                            required: true
                        },
                        editoptions: {value: getSectionType()}
                    },
                    {
                        name: 'ccsId',
                        index: 'ccsId',
                        width: 50,
                        readOnly : true,
                        editrules: {
                            required: true,
                        },
                        formatter: function (value, grid, row, state) {

                            value = value == undefined ? "" : value;
                            var ccsIdText = value == "" ? "编辑" : value.substring(value.indexOf("_")+1);
                            var ccsIdCode = value == "" ? "" : value.substring(0,value.indexOf("_"));
                            if(isNullOrEmptyOrUndefiend(row.sectionId)){
                                ccsIdText = "编辑";
                                ccsIdCode = "";
                            }
                            var btnReset =
                                "<span class='hidden'>"+ccsIdCode+"</span>"+
                                "<div style='display: inline-block'>" +
                                "<div class='btn btn-skinColor btn-xs' style='height:21px;border:0'>" +
                                "<a href='javascript:editCcsId(\""+grid.rowId+"\",\""+ccsIdCode+"\")'><font color='#FFFFFF'>"+ccsIdText+"</font>" +
                                "</div>" +
                                "</div>"
                            return btnReset;
                        }
                    },
                {
                name: 'drawLine',
                width: 50,
                readOnly : true,
                forbidExport:true,
                formatter: function (value, grid, row, state) {
                    if(isNullOrEmptyOrUndefiend(row.sectionId)){
                        return "";
                    }
                    var sectionDetailext =  row.sectionId == undefined ? "绘制" : "绘制";
                    var btnReset =
                        "<div style='display: inline-block'>" +
                        "<div class='btn btn-skinColor btn-xs' style='height:21px;border:0'>" +
                        "<a href='javascript:editMap(\""+grid.rowId+"\",\""+row.sectionId+"\")'><font color='#FFFFFF'>"+sectionDetailext+"</font>" +
                        "</div>" +
                        "</div>"

                    return btnReset;
            }
            }
                ],

                sortorder: 'asc',
                height: 351,
                jsonReader: {
                    id: "sectionId",
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
                url: "/raising/backstage/sectionManage.do?method=query",
                showMsgOpt: {
                    showMsgId: "alertdiv"
                },
                gridOption: gridOption,
                navGridOption: {
                    download: false,
                    downloadParam: {
                        downloadUrl: "/raising/backstage/sectionManage.do/download.do"
                    },
                    upload: false,
                    uploadParam: {
                        uploadUrl: "/raising/backstage/sectionManage.do?method=insertForUpload"
                    }
                }
            };
            $("#jqGrid").bxgrid(option);

        });
    $("#inqu_status-sectionName").keypress(function () {
        onKeyQuery();
    });
    $("#inqu_status-pageCname").keypress(function () {
        onKeyQuery();
    });

});


function designRow(rowId) {
    //没保存不跳转
    if (!$("#jqGrid").bxgrid("whetherValidRow", rowId)) {
        return;
    }
    var selectData = $("#jqGrid").bxgrid('rawMethodCall', 'getRowData', rowId);
    window.open(designurl + selectData.sectionEname);
}

function addAndCopy() {
    $("#jqGrid").bxgrid("addAndCopy");
}

function saveRec() {
    var insertOrUpdate = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'insertOrUpdate');
    if (insertOrUpdate == "insert") {
        insertRec();
    } else {
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
        var tbmType = $("#"+selectArray[i]).find("option:selected").val();
        var status = $("#jqGrid").bxgrid('rawMethodCall', "saveRow", selectArray[i]);
        if (status == false) {
            return;
        }
        var saveData = $("#jqGrid").bxgrid('rawMethodCall', "getRowData", selectArray[i]);
        saveData.ccsSectionType = tbmType;
        if(isNullOrEmptyOrUndefiend(tbmType)){
            alertDiv("提示","需要选择区间类型。");
            return;
        }
        //处理时间列的值
        saveData.createTime = saveData.createTime.trim();
        //处理saveData对象ccsId属性的值
        var divObj = document.createElement("div");
        divObj.innerHTML = saveData.ccsId;
        saveData.ccsId = divObj.getElementsByTagName("span")[0].innerHTML;
        if(saveData.ccsId.split(".").length != 3){
            alertDiv("提示","需要选择线路。");
            return;
        }
        delete saveData.drawLine;
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
    AjaxCommunicator.ajaxRequest('/raising/backstage/sectionManage.do?method=insert', 'POST', paramJsonObj, callback,true);
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
    confirmDiv("确认删除", "数据删除后将不可恢复，是否确定删除？", buttons);
}

function deleteOK() {
    var paramJsonObj = new Object();
    var arr = [];

    $("#jqGrid").bxgrid('setInfoFromGrid', paramJsonObj, 'result');
    var rows = paramJsonObj.result.resultRow;//只携带sectionId删除，省去日期转换的麻烦
    for(var i =0;i<rows.length;i++){
        var data = rows[i];
        var temp = {"sectionId":data.sectionId};
        arr.push(temp);
    }
    paramJsonObj.result.resultRow = arr;
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
    AjaxCommunicator.ajaxRequest('/raising/backstage/sectionManage.do?method=delete', 'POST', paramJsonObj, callback,true);
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
        var tbmType = $("#"+selectArray[i]).find("option:selected").val();
        var status = $("#jqGrid").bxgrid('rawMethodCall', "saveRow", selectArray[i]);
        if (status == false) {
            return;
        }
        var saveData = $("#jqGrid").bxgrid('rawMethodCall', "getRowData", selectArray[i]);
        saveData.ccsSectionType = tbmType;
        //处理时间列的值
        saveData.createTime = saveData.createTime.trim();
        //处理saveData对象ccsId属性的值
        var divObj = document.createElement("div");
        divObj.innerHTML = saveData.ccsId;
        saveData.ccsId = divObj.getElementsByTagName("span")[0].innerHTML;
        //插入入参数组前，做数据校验。
        if(saveData.ccsId.split(".").length != 3){
            alertDiv("提示","需要选择线路。");
            return;
        }
        delete saveData.drawLine;
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
    AjaxCommunicator.ajaxRequest('/raising/backstage/sectionManage.do?method=update', 'POST', paramJsonObj, callback,true);
}

function on_query_click() {
    var queryParam = new Object();
    $("#queryarea").bxdiv('setQueryFromDiv', queryParam, "inqu_status");
    $("#jqGrid").bxgrid("option", "queryParam", queryParam);
    if(!isNullOrEmptyOrUndefiend(queryParam.inqu_status.sectionName)){//如果区间名称不为空，则指按照区间名称模糊查询
        queryParam.inqu_status.ccsId = null;
        queryParam.inqu_status.owner = null;
        queryParam.inqu_status.ccsSectionType = null;
    }
    $("#jqGrid").bxgrid("query");
}

function onKeyQuery() {
    var e = window.event || arguments.callee.caller.arguments[0];
    var keyCode = e.keyCode || e.which; // 按键的keyCode
    if (keyCode == 13) {
        on_query_click();
    }
}


/**
 * 点击编辑按钮，编辑CCSID
 * @param row 当前点击行的对象
 * @param ccsId 当前CCSID
 */
function editCcsId(rowId,ccsId){
    if(! rowId > 1){
        return alertDiv("提示","出错了！");
    }
    var button = [{
        text: "确认",
        "class" : "btn btn-skinColor btn-xs",
        click : function () {
            var ccsIdShow = $("#ccsIdShow").val();
            var ccsIdHide = $("#ccsIdHide").val();
            var cityHide = $("#cityHide").val();
            //数据校验
            var strArray = ccsIdHide.split(".");
            if(strArray.length != 3){
                alertDiv("提示","需要选择线路。");
                return;
            };
            //判断是否设置默认业主
            var owner = $("#"+rowId).find("input[name='owner']").eq(0).val();
            if(isNullOrEmptyOrUndefiend(owner)){
                $("#"+rowId).find("input[name='owner']").eq(0).val(cityHide+"地铁");
            }
            //设置bxgrid隐藏的ccsId拼音
            $("#"+rowId).find("span").eq(0).html(ccsIdHide);
            //设置bxgrid显示的值
            $("#"+rowId).find("font").eq(0).html(ccsIdShow);
            //重置CCSID DIV内容
            $("#ccsIdShow").val("");
            $("#ccsIdHide").val("");
            $("#cityHide").val("");
            $("#edit").bxdialog('close');
            alertDiv("信息","编辑城市线路成功，请点击表格上方保存按钮保存！");
        }
    }]
    var title = "线路选择";
    var dialogOpt = {
        title: title,
        buttons:button,
        width: '500px'
    };
    //显示对话框
    $("#edit").bxdialog(dialogOpt);
    showTree(rowId,ccsId);
}

/**
 * CCSID树，点击事件
 * @param node
 */
function clickNode(node,rowId) {
    var nodeId=node.id;
    //判断选中的node类型
    if(nodeId.indexOf("line") != -1){//如果是线路

        var cityId = node.parent+"_anchor";
        var cityHtml = $("a[id ^= '"+cityId+"']").html();
        var cityText = cityHtml.substring(cityHtml.indexOf("</i>")+4);//城市中文名
        var provinceId = node.parents[1]+"_anchor";
        var provinceHtml = $("a[id ^= '"+provinceId+"']").html();
        var provinceText = provinceHtml.substring(provinceHtml.indexOf("</i>")+4);//省份中文名
        var lineText = node.text;//线路的中文名
        var ccsId = nodeId.substring(nodeId.indexOf("-")+1);//ccsId的 code

        var temp = cityText;
        if(cityText.lastIndexOf("市")>0){
            temp = cityText.substring(0,cityText.lastIndexOf("市"));
        }
        var result = provinceText + cityText + lineText;
        //更改右端DIV显示
        $("#ccsIdShow").val(result);
        //隐藏ccsId 拼音
        $("#ccsIdHide").val(ccsId);
        $("#cityHide").val(temp);
    }else {
        //如果是根节点。或者其他非线路节点。
        return;
    }
}
//URL 获取树的方法。
function showTree(rowId,ccsId) {
    var option = {
        'core': {
            'data': {
                'url': function (node) {
                    return httpServerPath+'/raising/backstage/sectionManage.do?method=getLineTree';
                },
                'data': function (node) {
                    var nodeId = node.id;
                    var nodeArray = nodeId.split("-");

                    if (nodeId == "#") {// 点击的是根节点
                        return {
                            'fdParentId': '0',
                            'treeparentId': nodeId,
                        };
                    } else {// 点击的是子节点
                        return {
                            'fdParentId': nodeArray[0],
                            'treeparentId': nodeId,
                        };
                    }
                }
            },
            "check_callback": true
        },
        "dnd": {
            is_draggable: true
        },
        plugins: ["state","dnd","wholerow"]
    };
    var treeOption = {
        dataPattern:"url",
        custom:true,
        sortorder : 'asc',
        option:option
    };
    var nodeState=false;//树节点是否被选中
    $("#bxtree3").bxtree(treeOption)
        .on("select_node.jstree",function (event, obj) {
            clickNode(obj.node,rowId);
            //选中节点以后，CCSID光标放在最后，便于观察CCSID值的变化
            var inputObj = document.getElementById("ccsIdShow");
            moveEnd(inputObj);
            nodeState=true;
        })
        .on('state_ready.jstree', function(e, data){
            $("#ccsIdShow").val("");
            $("#ccsIdHide").val("");
            //初始化弹窗CCSID 输入框的值。
            var currentCCSIDHide = $("#"+rowId).find("span").eq(0).html();
            if(currentCCSIDHide.split(".").length == 3){
                var currentCCSIDShow = $("#"+rowId).find("font").eq(0).html();
                $("#ccsIdShow").val(currentCCSIDShow);
                $("#ccsIdHide").val(currentCCSIDHide);
            }else{
                $("#ccsIdShow").val("");
                $("#ccsIdHide").val("");
            }
        });

}

//设置CCSID输入框的光标
function moveEnd(inputObj) {
    inputObj.focus();
    var len = inputObj.value.length;
    if (document.selection) {
        var sel = inputObj.createTextRange();
        sel.moveStart('character', len);
        sel.collapse();
        sel.select();
    } else if (typeof inputObj.selectionStart == 'number'
        && typeof inputObj.selectionEnd == 'number') {
        inputObj.selectionStart = inputObj.selectionEnd = len;
    }
}

/**
 * 编辑区间段详细信息
 */
function editMap(rowId,sectionId) {
    //children.window.location.reload();
    var title = "地图线路";
    var dialogOpt = {
        title: title,
        // buttons:button,
        width: '1000px'
    };
    //显示对话框
    $("#showMap").bxdialog(dialogOpt);
    $.cookie("map_section_id",sectionId);
    var url = toolkitPath + "/raising/backstage/mapDesign.jsp?"+Math.random();
    $("#mapDesign").attr("src",url);

}

function getSectionType() {
    var returnStr = "";
    $.ajax({
        async: false,
        type: "get",
        url: toolkitPath +'/raising/backstage/sectionManage.do?method=getSectionType',
        data:{"code":"sectionType"},
        success: function (data) {
            var  sectionTypeArr = data.sectionTypesList;
            for (var i = 0; i < sectionTypeArr.length; i++) {
                if (i < sectionTypeArr.length - 1) {
                    returnStr += sectionTypeArr[i].ccsId + ":" + sectionTypeArr[i].ccsStr + ";";
                } else {
                    returnStr += sectionTypeArr[i].ccsId + ":" + sectionTypeArr[i].ccsStr;
                }
            }
        }
    });
    return returnStr;
}

function initSelect(){

    $.ajax({
        async: false,
        type: "get",
        url: toolkitPath +'/raising/backstage/sectionManage.do?method=initSelect',
        data:{"code":"sectionType"},
        success: function (data) {
            var sectionTypesArr = data.sectionTypesList;
            if(data.haveData == "0"){
                var ownersArr = data.ownersList;
                var linesArr = data.linesList;
                for (var i = 0; i < ownersArr.length; i++) {
                    $("#inqu_status-owner").append("<option value='"+ownersArr[i]+"'>"+ownersArr[i]+"</option>");
                }
                for (var i = 0; i < linesArr.length; i++) {
                    $("#inqu_status-ccsId").append("<option value='"+linesArr[i].ccsId+"'>"+linesArr[i].line+"</option>");
                }
            }
            for (var i = 0; i < sectionTypesArr.length; i++) {
                $("#inqu_status-ccsSectionType").append("<option value='"+sectionTypesArr[i].ccsId+"'>"+sectionTypesArr[i].ccsStr+"</option>");
            }
        }
    });

}
