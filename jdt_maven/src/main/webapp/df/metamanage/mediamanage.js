$(document).ready(function () {
    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'],
        function () {
            $("#queryarea").bxdiv();
            var gridOption = {
                primaryRowKey: "mediaId",
                caption: false,
                colNames: ['多媒体序列号', '文件名', '描述','文件类型', '操作'],
                colModel: [{
                    name: 'mediaId',
                    index: 'mediaId',
                    width: 10,
                    hidden: true,
                    forbidCopy: true
                },
                    {
                        name: 'mediaName',
                        index: 'mediaName',
                        width: 50,
                        editable: false,
                        sortable: true,
                        editrules: {
                            required: true
                        }
                    },
                    {
                        name: 'mediaDesc',
                        index: 'mediaDesc',
                        width: 50,
                        editable: true,
                        sortable: false,
                        /* editrules: {
                         required: true
                         }*/
                    },
                    {
                        name: 'mediaType',
                        index: 'mediaType',
                        width: 50,
                        edittype: 'ccs',
                        editoptions: 'metadata.mediaType',
                        editable: true,
                        sortable: true,
                       /* editrules: {
                            required: true,
                            custom: true,
                            custom_func: bxgrid_stringCheck

                        }*/
                    },
                    {
                        name: 'opt',
                        index: 'opt',
                        width: 45,
                        sortable: false,
                        readOnly: true,
                        forbidExport: true,
                        formatter: function (value, grid, row, state) {
                            var btnReset = "<div style='text-align:center'><input class='btn btn-block'  type='button' value='上&nbsp&nbsp&nbsp传' "
                                + "style='width:70px;border:none;'"
                                + "onclick='upLoadMedia("
                                + "\""+ row.mediaType + "\"" +','+"\""+row.mediaId+"\""+','+"\""+row.mediaName+"\","+grid.rowId+ ");"
                                +"'/></div>";
                            return btnReset;
                        }
                    }],
                sortorder: 'asc',
                height:351,
                jsonReader: {
                    id: "mediaId",
                    repeatitems: false
                }
            };
            var defaultData = [{
                label: "全部",
                value: ""
            }];
            $("#inqu_status-mediaType").bxcombobox({
                dataPattern: 'ccs',
                ccsId: "metadata.mediaType",
                async: false,
                data: defaultData
            });
            var option = {
                queryParam: {},
                dataPattern: "url",
                url: "/df/metamanage/mediaManage.do?method=query",
                showMsgOpt: {
                    showMsgId: "alertdiv"
                },
                gridOption: gridOption
            };
            $("#jqGrid").bxgrid(option);

        });
});

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
        saveData.mediaCreator =  $("#hidden-userName").val();
        saveData.mediaCreateTime =  DateUtil.convertDateToStr("yyyy-MM-dd HH:mm:ss",new Date());
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
    AjaxCommunicator.ajaxRequest('/df/metamanage/mediaManage.do?method=insert', 'POST', paramJsonObj, callback);
}

function deleteRec() {
    var selectArray = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if (selectArray.length == 0) {
        alertDiv("提示", "请至少勾选一条记录进行操作！");
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
    AjaxCommunicator.ajaxRequest('/df/metamanage/mediaManage.do?method=delete', 'POST', paramJsonObj, callback);
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
        saveData.mediaModifier =  $("#hidden-userName").val();
        saveData.mediaModitime = DateUtil.convertDateToStr("yyyy-MM-dd HH:mm:ss",new Date());
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
    AjaxCommunicator.ajaxRequest('/df/metamanage/mediaManage.do?method=update', 'POST', paramJsonObj, callback);
}

function on_query_click() {
    var queryParam = new Object();
    $("#queryarea").bxdiv('setQueryFromDiv', queryParam, "inqu_status");
    $("#jqGrid").bxgrid("option", "queryParam", queryParam);
    $("#jqGrid").bxgrid("query");
}

/**
 * 上传
 */
function upLoadMedia(type,id,name, rowid){
    //没保存不让上传
    if(!$("#jqGrid").bxgrid("whetherValidRow",rowid)){
        return;
    }
    var fileId = "mediaResources";
    var exgPicture = /\.((jpg)|(bmp)|(gif)|(tif)|(png)|(psd)|(tiff)|(cpx)|(ico)|(jpeg)|(eps))$/i;
    var exgAudio = /\.(mp3)$/i;
    var exgVideo = /\.(mp4)$/i;
    var buttons = [
        {
            html: "<i class='ace-icon fa fa-check bigger-110'></i>&nbsp;导入",
            class: "btn btn-skinColor btn-xs",
            click: function () {
                var fileObj = document.getElementById(fileId);
                var length = fileObj.files.length;
                if(length == 0){
                    $(this).dialog("close");
                    alertDiv("警告", "请导入文件！");
                    return;
                }
                var tempFileName = fileObj.files[0].name;
                if(type == "picture"){
                    if(!exgPicture.test(tempFileName))
                    {
                        $(this).dialog("close");
                        alertDiv("警告", "不支持的图片文件格式！");
                        return;
                    }
                }
                if(type == "audio"){
                    if(!exgAudio.test(tempFileName))
                    {
                        $(this).dialog("close");
                        alertDiv("警告", "请导入正确的音频文件，只支持mp3格式的文件");
                        return;
                    }
                }
                if(type == "video"){
                    if(!exgVideo.test(tempFileName))
                    {
                        $(this).dialog("close");
                        alertDiv("警告", "请导入正确的视频文件，只支持mp4格式的文件");
                        return;
                    }
                }
                $.ajaxFileUpload(
                        {
                            url: toolkitPath+"/df/metamanage/mediaManage.do?method=upload&type="+type+"&id="+id+"&name="+name, //用于文件上传的服务器端请求地址
                            secureuri: false, //是否需要安全协议，一般设置为false
                            fileElementId: fileId, //文件上传域的ID
                            dataType: 'json', //返回值类型 一般设置为json
                            success: function (paramJsonObj)  //服务器成功响应处理函数
                            {
                                var showMsgOpt = {
                                    showMsgId: "alertdiv",
                                    status: paramJsonObj.status,
                                    showMsg: paramJsonObj.returnMsg
                                };

                                $("#jqGrid").bxgrid("option", "showMsgOpt", showMsgOpt);
                                $("#jqGrid").bxgrid("query");
                            },
                            error: function (data, status, e)//服务器响应失败处理函数
                            {
                                alertDiv("警告", "系统错误，请联系管理员！");
                            }
                        }
                );
                $(this).dialog("close");
                $("#jqGrid").bxgrid("query");
            }
        }
    ];
    uploadDiv({
        title: "导入文件",
        buttons: buttons,
        fileId: fileId,
        no_icon: 'ace-icon fa fa-download',
        btn_choose: "点击导入文件页面",
    });
}



