
$(document).ready(function () {
    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'],
        function () {
            $("#queryarea").bxdiv();
            var gridOption = {
                primaryRowKey: "faultId",
                caption: false,
                colNames: [ 'faultId','tbmId','时间', '产品名称','故障原因','质保到期时间', '经手人'
                            ,'经手人电话', '联系人','联系人电话','处理方式','附件','地点'],
                colModel: [
                    {
                        name: 'faultId',
                        index: 'faultId',
                        width: 1,
                        hidden: true,
                        forbidCopy: false
                    },
                    {
                        name: 'tbmId',
                        index: 'tbmId',
                        width: 1,
                        hidden: true,
                    },{
                        name: 'createTime',
                        index: 'createTime',
                        width: 40,
                        editable: true,
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
                                    element.value = getMyDate(parseInt(a1));
                                });
                            }
                        }
                    },  {
                        name: 'productName',
                        index: 'productName',
                        width: 50,
                        editable: true,
                        sortable: true,
                    },
                    {
                        name: 'rseason',
                        index: 'rseason',
                        width: 50,
                        editable: true,
                        sortable: true,
                    },
                    {
                        name: 'warrantyTime',
                        index: 'warrantyTime',
                        width: 40,
                        editable: true,
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
                                    element.value = getMyDate(parseInt(a1));
                                });

                            }
                        }
                    },
                    {
                        name: 'brokerage',
                        index: 'brokerage',
                        width: 30,
                        editable: true,
                        editrules: {
                            required: true
                        }
                    },
                    {
                        name: 'brokeragePhone',
                        index: 'brokeragePhone',
                        width: 40,
                        editable: true,
                        editrules: {
                            required: true,
                            custom: true,
                            custom_func: checkGridMobile
                        },
                    },
                    {
                        name: 'contacts',
                        index: 'contacts',
                        width: 30,
                        editable: true
                    },
                    {
                        name: 'contactsPhone',
                        index: 'contactsPhone',
                        width: 40,
                        editable: true,
                        editrules: {
                            required: true,
                            custom: true,
                            custom_func: checkGridMobile
                        },
                    },
                    {
                        name: 'processMode',
                        index: 'processMode',
                        width: 50,
                        editable: true,
                        editrules: {
                            required: true
                        },
                        edittype:'select',
                        editoptions: {value: getProblemtype()}
                    },
                    {
                        name: 'file',
                        width: 80,
                        readOnly : true,
                        forbidExport:true,
                        formatter: function (value, grid, row, state) {
                            var btnReset = null;

                            var fileName =  row.fileName;
                            if(fileName == undefined || fileName == null || fileName == ""){//如果没有关联文件
                               return "";
                            }else{
                                btnReset =
                                    "<div style='display: inline-block'>" +
                                        "<div class='btn btn-skinColor btn-xs' style='height:21px;border:0;margin-left: 5px;'>" +
                                            "<a href='javascript:previewFile(\""+row.fileName+"\",\""+row.faultId+"\")'><font color='#FFFFFF'>"+fileName+"</font>" +
                                        "</div>" +
                                        "<div class='btn btn-skinColor btn-xs' style='height:21px;border:0;margin-left: 5px;'>" +
                                            "<a href='javascript:attachment(\""+row.faultId+"\")'><font color='#FFFFFF'>更多</font>" +
                                        "</div>" +
                                    "</div>";
                            }

                            return btnReset;
                        }
                    },
                    {
                        name: 'place',
                        index: 'place',
                        width: 50,
                        editable: true
                    }
                ],

                sortorder: 'asc',
                height: 351,
                jsonReader: {
                    id: "faultId",
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
                queryParam: {"inqu_status":{"tbmId":$.cookie('selected_tbmId'),"tbmName":$.cookie('selected_tbmName')}},
                dataPattern: "url",
                url: "/raising/forward/tbmManage/faultTreat/query.do",
                showMsgOpt: {
                    showMsgId: "alertdiv"
                },
                gridOption: gridOption,
                navGridOption: {
                    download: false,
                    downloadParam: {
                        downloadUrl: "/sectionManage.do/download.do"
                    },
                    upload: false,
                    uploadParam: {
                        uploadUrl: "/sectionManage.do?method=insertForUpload"
                    }
                }
            };
            $("#jqGrid").bxgrid(option);

        });


});



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
        var processMode = $("#"+selectArray[i]).find("option:selected").val();
        var status = $("#jqGrid").bxgrid('rawMethodCall', "saveRow", selectArray[i]);
        if (status == false) {
            return;
        }
        var saveData = $("#jqGrid").bxgrid('rawMethodCall', "getRowData", selectArray[i]);
        saveData.processMode =processMode;
        saveData.createTime = saveData.createTime.trim();
        saveData.warrantyTime = saveData.warrantyTime.trim();
        saveData.tbmId = $.cookie("selected_tbmId");
        delete saveData.file;
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
    AjaxCommunicator.ajaxRequest('/raising/forward/tbmManage/faultTreat/insert.do', 'POST', paramJsonObj, callback,true);
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

    $("#jqGrid").bxgrid('setInfoFromGrid', paramJsonObj, 'result');
    paramJsonObj.tbmName = $.cookie("selected_tbmName");
    var jsonArray = paramJsonObj.result.resultRow;
    for(var i=0;i<jsonArray.length;i++){
        var temp = jsonArray[i];
        var record = {};
        record.faultId = temp.faultId;
        jsonArray[i] = record;
        //delete temp.file;
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
    AjaxCommunicator.ajaxRequest('/raising/forward/tbmManage/faultTreat/delete.do', 'POST', paramJsonObj, callback,true);
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
        var processMode = $("#"+selectArray[i]).find("option:selected").val();
        var status = $("#jqGrid").bxgrid('rawMethodCall', "saveRow", selectArray[i]);
        if (status == false) {
            return;
        }
        var saveData = $("#jqGrid").bxgrid('rawMethodCall', "getRowData", selectArray[i]);
        saveData.processMode =processMode;
        saveData.createTime = saveData.createTime.trim();
        saveData.warrantyTime = saveData.warrantyTime.trim();
        saveData.tbmId = $.cookie("selected_tbmId");
        delete saveData.file;
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
    AjaxCommunicator.ajaxRequest('/raising/forward/tbmManage/faultTreat/update.do', 'POST', paramJsonObj, callback,true);
}

function getProblemtype() {
    var returnStr = "";
    $.ajax({
        async: false,
        type: "get",
        url : toolkitPath+'/raising/forward/tbmManage/faultTreat/getProblemType.do',
        dataType:"json",
        success: function (data) {
            for (i = 0; i < data.length; i++) {
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



var upload = false;
//数据上传
function dataUpload(){
    if(upload == true){
        alertDiv("提示","文件正在上传，请勿重复上传。");
        return;
    }
    var faultId = $("#faultIdHide").val();
    if(faultId == null || faultId == ""){
        alertDiv("提示","请先保存记录，在上传文件")
        return;
    }

    $("#faultId").val(faultId);

    $('#bxgridFileInput').ace_file_input({
        style : 'well',
        btn_choose : '上传文件',
        btn_change : null,
        no_icon : 'ace-icon fa fa-cloud-upload',
        droppable : true,
        thumbnail : 'small',//large | fit
        preview_error : function(filename, error_code) {
        }

    }).on('change', function() {
    });

    var title = "文件上传";
    var dialogOpt = {
        title: title,
        width: '350px'

    };
    $("#fromDiv").bxdialog(dialogOpt);

}


function dataSubmit() {
    $("#tbmName").val($.cookie("selected_tbmName"));
    var faultId = $("#faultIdHide").val();
    var file = document.getElementById("bxgridFileInput").files[0];
    var fileName = file.name;
    var suffix = fileName.substring(fileName.indexOf("."));
    var fileFilt = ".RAR|.ZIP|.PDF|.PDFX|.TXT|.XLSX|.DOCX|.JPEG|.JPG|.GIF|.PNG";
    if(fileFilt.indexOf(suffix.toLocaleUpperCase()) <=-1 ){
        alertDiv("提示","文件格式不正确，允许文件格式为.RAR|.ZIP|.PDF|.PDFX|.TXT|.XLSX|.DOCX|.JPEG|.JPG|.GIF|.PNG,文件保存失败！");
        return;
    }
    if(file.size.toFixed(1) > 20*1024*1024){
        alertDiv("提示","文件大小应不超过20M，上传失败");
        return;
    }
    var option = {
        url: toolkitPath+'/raising/forward/tbmManage/faultTreat/dataUpload.do', //这里写你的url
        type : 'POST',
        datatype:'json',//这里是返回类型，一般是json,text
        clearForm: true,//提交后是否清空
        success : function(data) {
            upload = false;
            $("button[data-dismiss='modal'][class='bootbox-close-button close']").click();//关闭文件上传进度条。
            $("a[class='remove']").eq(0).click();//触发删除文件按钮
            if(data.status != "-1"){
                alertDiv("提示",data.message);
                /*$("#jqGrid").bxgrid("query");*/
                $("#attchmentGrid").bxgrid("query");
                //请求成功以后，发起转换文件请求。
                var param = {"tbmName":$.cookie("selected_tbmName"),"faultId":faultId};
                AjaxCommunicator.ajaxRequest('/raising/forward/tbmManage/faultTreat/convertFileToPDF.do', 'GET', param,{onSuccess: function () {}},true);
            }else{
                alertDiv("提示",data.message);
            }
        } ,
        error:function(data){
            upload = false;
            alertDiv("提示","页面请求失败");
        },
        uploadProgress: function (event, position, total, percentComplete) {
            percentVal = percentComplete + '%';
            $("#progressBack").attr("data-percent",percentVal);
            $("#progress").css("width",percentVal);
            console.log(percentVal,total,position);
        }
    };
    $("#factoryForm").ajaxSubmit(option);
    upload = true;
    //formDivClose();//关闭上传文件框
    $("#fromDiv").bxdialog("close");
    return false;
}

function download() {
    var faultId = $("#faultIdHide").val();
    var fileNames = [];
    var selectArray = $("#attchmentGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if (selectArray.length == 0 ) {
        alertDiv("提示", "至少选择一条记录")
        return;
    }
    for(var i=0;i<selectArray.length;i++){
        var status = $("#attchmentGrid").bxgrid('rawMethodCall', "saveRow", selectArray[i]);
        if (status == false) {
            return;
        }
        var saveData = $("#attchmentGrid").bxgrid('rawMethodCall', "getRowData", selectArray[i]);
        var fileName = saveData.fileName;
        fileNames.push(fileName);
    }

    var param = {"faultId":faultId,"fileNames":fileNames,"tbmName":$.cookie("selected_tbmName")};
    var paramStr = JSON.stringify(param);
    window.location.href =toolkitPath +'/raising/forward/tbmManage/faultTreat/downloadFile.do?ajaxParam='+encodeURIComponent(encodeURIComponent(paramStr));

}

function deleteFile(){
    var faultId = $("#faultIdHide").val();
    var fileNames = [];
    var selectArray = $("#attchmentGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if (selectArray.length == 0 ) {
        alertDiv("提示", "至少选择一条记录")
        return;
    }
    for(var i=0;i<selectArray.length;i++){
        var status = $("#attchmentGrid").bxgrid('rawMethodCall', "saveRow", selectArray[i]);
        if (status == false) {
            return;
        }
        var saveData = $("#attchmentGrid").bxgrid('rawMethodCall', "getRowData", selectArray[i]);
        var fileName = saveData.fileName;
        fileNames.push(fileName);
    }

    var buttons = [{
        html: "<i class='ace-icon fa fa-check bigger-110'></i>&nbsp; 是",
        "class": "btn btn-skinColor btn-xs",
        click: function () {
            var param = {"faultId":faultId,"fileNames":fileNames,"tbmName":$.cookie("selected_tbmName")};
            var paramStr = JSON.stringify(param);

            $.ajax({
                type:"post",
                url:toolkitPath+'/raising/forward/tbmManage/faultTreat/deleteFile.do',
                data:{"ajaxParam":paramStr},
                dataType:"json",
                success:function(data){
                    if(data.status = "0"){
                        alertDiv("提示","删除成功");
                        $("#attchmentGrid").bxgrid("query");
                    }else{
                        alertDiv("提示",data.message);
                    }
                }
            })
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

function previewFile(fileName,faultId){
    var fileName = fileName;
    var faultId = faultId;
    if(isNullOrEmptyOrUndefiend(fileName) && isNullOrEmptyOrUndefiend(faultId)){
        var selectArray = $("#attchmentGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
        if (selectArray.length != 1) {
            //alert("提示", "只能操作一条记录");
            alertDiv("提示", "只能操作一条记录");
            return;
        }
        var status = $("#attchmentGrid").bxgrid('rawMethodCall', "saveRow", selectArray[0]);
        if (status == false) {
            return;
        }
        var saveData = $("#attchmentGrid").bxgrid('rawMethodCall', "getRowData", selectArray[0]);
        fileName = saveData.fileName;
        faultId = $("#faultIdHide").val();
    }

    if(fileName.indexOf(".") < 0) {
        alertDiv("提示", "文件夹不可以预览");
        return;
    }
    var postFix = fileName.substring(fileName.indexOf("."));
    var fileType = ".jpg|.gif|.jpeg|.png|.bmp|.doc|.docx|.txt|.xls|.xlsx";
    if (fileType.indexOf(postFix) < 0) {
        alertDiv("提示", "该文件类型不支持预览");
        return;
    }

    var param = {};
    param.faultId = faultId;
    param.fileName = fileName;
    param.tbmName = $.cookie("selected_tbmName");
    var img = ".jpg|.gif|.jpeg|.png|.bmp|.pdf";
    if(img.indexOf(postFix) < 0){
        $.ajax({
            type:"get",
            url:toolkitPath+'/raising/forward/tbmManage/faultTreat/checkPreview.do',
            data:{"ajaxParam":JSON.stringify(param)},
            dataType:"json",
            //发送方式改为同步，避免弹出页面被浏览器拦截
            async: false,
            success:function(data){
                if(data.status == "-1"){
                    alertDiv("提示",data.message);
                    return;
                }
                window.open(toolkitPath+'/raising/forward/tbmManage/faultTreat/preview.do?ajaxParam='+encodeURIComponent(encodeURIComponent(JSON.stringify(param))),
                    "_blank","top=200,left=200,height=600,width=800,status=yes,toolbar=1,menubar=no,location=no,scrollbars=yes");


            }
        })
    }else{
        window.open(toolkitPath+'/raising/forward/tbmManage/faultTreat/preview.do?ajaxParam='+encodeURIComponent(encodeURIComponent(JSON.stringify(param))),
            "_blank","top=200,left=200,height=600,width=800,status=yes,toolbar=1,menubar=no,location=no,scrollbars=yes");
    }

}

function attachment(faultId){
    var faultId = faultId;
    if(isNullOrEmptyOrUndefiend(faultId)) {
        var selectArray = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
        if (selectArray.length != 1) {
            alertDiv("提示", "只能操作一条记录")
            return;
        }
        var status = $("#jqGrid").bxgrid('rawMethodCall', "saveRow", selectArray[0]);
        if (status == false) {
            return;
        }
        var saveData = $("#jqGrid").bxgrid('rawMethodCall', "getRowData", selectArray[0]);
        faultId = saveData.faultId;
    }
    $("#faultIdHide").val(faultId);
    //初始化

    var title = "文件上传";
    var dialogOpt = {
        title: title,
        height:590,
        width: '700px'

    };
    $("#attachmentManage").bxdialog(dialogOpt);

    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'],
        function () {
            var gridOption = {
                primaryRowKey: "faultId",
                caption: false,
                colNames: [ '文件名'],
                colModel: [
                    {
                        name: 'fileName',
                        index: 'fileName',
                        width: 100

                    }
                ],
                sortorder: 'asc',
                height: 250,
                width:200,
                jsonReader: {
                    id: "faultId",
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
                queryParam: {"faultId":faultId,"tbmName":$.cookie('selected_tbmName')},
                dataPattern: "url",
                url: "/raising/forward/tbmManage/faultTreat/queryFile.do",
                showMsgOpt: {
                    showMsgId: "alertdivInfo"
                },
                gridOption: gridOption,
                navGridOption: {
                    download: false,
                    downloadParam: {
                        downloadUrl: "/sectionManage.do/download.do"
                    },
                    upload: false,
                    uploadParam: {
                        uploadUrl: "/sectionManage.do?method=insertForUpload"
                    }
                }
            };
            $("#attchmentGrid").bxgrid(option);

        });
}