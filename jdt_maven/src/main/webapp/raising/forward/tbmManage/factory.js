
var info = new Vue({
    el:"#father",
    data:{
        title:"厂内交货",
        rows:[],
        isUpload:false
    }
})



$(document).ready(function () {
    initDomHeight();
    var left=document.getElementById('sTitle').offsetLeft;
    document.getElementById('template').style.left=left + 80;
    $("#template").removeClass("hide");
    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxcombobox', 'bxtree', 'bxcomboboxtree'], function () {
        showTree();
    });


});

function initDomHeight(){
    var fatherHeight = $("#father").height();
    var headHeight = $("#header").outerHeight(true);
    var contentHeight = fatherHeight - headHeight;
    $("#treeDiv").height(contentHeight);
    $("#treeDiv2").height(contentHeight-10);
}

//数据上传
function dataUpload(){
    if(info.isUpload == true){
        alertDiv("提示","文件上传中，请等待。");
        return;
    }
    //初始化
    var button = [{
        text: "完成",
        "class" : "btn btn-skinColor btn-xs",
        "id":"uploadButton",
        click : function () {
            factoryUpload();
            //uploadFile();
        }
    }]
    var title = "文件上传";
    var dialogOpt = {
        title: title,
        buttons:button,
        width: '650px'
    };
    $("#formDiv").bxdialog(dialogOpt);

    $('#createTime').datetimepicker({
        language: 'zh-CN',//显示中文
        format: 'yyyy-mm-dd',//显示格式
        autoclose: true,//选中自动关闭
        todayBtn: false,//显示今日按钮
        todayHighlight: 1,//今天高亮
        minView:2
    }).on('changeDate', function(ev){
        var a1 = ev.date.valueOf();
        $("#createTime").val(getMyDate(parseInt(a1)));
    });
}


function  factoryUpload() {
    var brokerage = $("#brokerage").val();
    var brokeragePhone = $("#brokeragePhone").val();
    var contacts = $("#contacts").val();
    var contactsPhone = $("#contactsPhone").val();
    var createTime = $("#createTime").val();
    var file = document.getElementById("file").files[0];
    if(brokerage == "" || brokeragePhone == "" ||  contacts == "" || contactsPhone == "" || createTime == ""|| file == undefined  ){
        alertDiv("提示","红星标记数据项不可以为空！");
        return;
    }
    var fileName = file.name;
    var prefix  = fileName.substring(0,fileName.indexOf("."));
    var suffix = fileName.substring(fileName.indexOf("."));
    if("厂内交货" != prefix){
        alertDiv("提示","上传的压缩文件名必须为厂内交货");
        return;
    }
    if(suffix != ".zip"){
        alertDiv("提示","文件格式不正确，只能上传zip文件，上传失败");
        return;
    }
    if(file.size.toFixed(1) > 50*1024*1024){
        alertDiv("提示","文件大小应不超过50M，上传失败");
        return;
    }

    if(!checkMobile(brokeragePhone) || !checkMobile(contactsPhone)){
        alertDiv("提示","电话必须是11位数字");
        return;
    }
    $("#type").val("厂内交货");
    var option = {
        url: toolkitPath+'/raising/forward/tbmManage/factory/dataUpload.do', //这里写你的url
        type : 'POST',
        datatype:'json',//这里是返回类型，一般是json,text
        data:{"tbmName":$.cookie("selected_tbmName"),"tbmId":$.cookie("selected_tbmId")},
        clearForm: true,//提交后是否清空
        success : function(data) {
            info.isUpload = false;
            $("button[data-dismiss='modal'][class='bootbox-close-button close']").click();
            if(data.status == "0"){
                alertDiv("提示",data.message);
                getTable($.cookie("selected_tbmName"),info.title);
                //请求成功以后，发起转换文件请求。
            }else{
                alertDiv("提示",data.message);
            }
        } ,
        error:function(data){
            alertDiv("提示","页面请求失败");
        },
        uploadProgress: function (event, position, total, percentComplete) {
            percentVal = percentComplete + '%';
            $("#progressBack").attr("data-percent",percentVal);
            $("#progress").css("width",percentVal);
        }

    };
    $("#factoryForm").ajaxSubmit(option);
    $("#formDiv").bxdialog("close");
    bootbox.dialog({
        title: "厂内交货文件上传 ",
        message:"",
        message: "<div class='progress' id='progressBack' data-percent='0%'>\n" +
        "<div class='progress-bar' id='progress' style='width:0%;'></div></div>"

    });

    return false;
}



function download() {
    var len = $("input[name = 'record']:checked").length ;
    if(len < 1){
        alertDiv("提示","至少选择一份文件下载。");
        return;
    }
    /*var fileName = $("input[name = 'record']:checked").eq(0).parent().next("td").html();*/
    var fileNames = [];
    $("input[name = 'record']:checked").each(function () {
        var fileName = $(this).parent().next("td").html();
        fileNames.push(fileName);
    })

    var param = {"factoryType":info.title,"fileNames":fileNames,"tbmName":$.cookie("selected_tbmName")};
    var paramStr = JSON.stringify(param);
    window.location.href =toolkitPath +'/raising/forward/tbmManage/factory/downloadFile.do?ajaxParam='+encodeURIComponent(encodeURIComponent(paramStr));

    $("[name='record']").removeAttr("checked");//取消全选
}

function deleteFile(){
    var button = [{
        text: "确定",
        "class" : "btn btn-skinColor btn-xs",
        click : function () {
            var len = $("input[name = 'record']:checked").length ;
            if(len < 1){
                alertDiv("提示","至少选择一条记录");
                return;
            }
            var fileNames = [];
            var indexArray = [];
            $("input[name = 'record']:checked").each(function () {
                indexArray.push($(this).val());
                var fileName = $(this).parent().next("td").html();
                fileNames.push(fileName);
            })
            var param = {"factoryType":info.title,"fileNames":fileNames,"tbmName":$.cookie("selected_tbmName")};
            $.ajax({
                type:"post",
                url:toolkitPath+'/raising/forward/tbmManage/factory/deleteFile.do',
                data:{"ajaxParam":JSON.stringify(param)},
                dataType:"json",
                success:function(data){
                    $("#deleteConfirm").bxdialog("close");
                    if(data.status = "0"){
                        indexArray.sort();
                        for(var i = indexArray.length - 1;i>= 0;i--){
                            info.rows.splice(indexArray[i],1);
                        }
                        alertDiv("提示","删除成功");
                        $("[name='record']").removeAttr("checked");//取消全选
                    }else{
                        alertDiv("提示",data.message);
                    }
                }
            })
        }
    }]
    var title = "确定删除";
    var dialogOpt = {
        title: title,
        buttons:button,
        width: '300px'
    };
    $("#deleteConfirm").bxdialog(dialogOpt);

}

function previewFile(e){
    var fileName= $(e).attr("data-fileName");
    if(fileName.indexOf(".") < 0){
        alertDiv("提示","文件夹不可以预览");
        return;
    }
    var postFix = fileName.substring(fileName.indexOf("."));
    var fileType = ".jpg|.gif|.jpeg|.png|.bmp|.csv|.doc|.docx|.ppt|.pptx|.xls|.xlsx|.pdf|.txt";
    if(fileType.indexOf(postFix.toLowerCase()) < 0){
        alertDiv("提示","该文件类型不支持预览");
        return;
    }


    var param = {};
    param.factoryType = info.title;
    param.fileName = fileName;
    param.tbmName = $.cookie("selected_tbmName");
    var img = ".jpg|.gif|.jpeg|.png|.bmp|.pdf";
    if(img.indexOf(postFix) < 0){
        $.ajax({
            type:"get",
            url:toolkitPath+'/raising/forward/tbmManage/factory/checkPreview.do',
            data:{"ajaxParam":JSON.stringify(param)},
            dataType:"json",
            //发送方式改为同步，避免弹出页面被浏览器拦截
            async: false,
            success:function(data){
                if(data.status == "-1"){
                    alertDiv("提示",data.message);
                    return;
                }
                window.open(toolkitPath+'/raising/forward/tbmManage/factory/preview.do?ajaxParam='+encodeURIComponent(encodeURIComponent(JSON.stringify(param))),
                    "_blank","top=200,left=200,height=600,width=800,status=yes,toolbar=1,menubar=no,location=no,scrollbars=yes");


            }
        })
    }else{
        window.open(toolkitPath+'/raising/forward/tbmManage/factory/preview.do?ajaxParam='+encodeURIComponent(encodeURIComponent(JSON.stringify(param))),
            "_blank","top=200,left=200,height=600,width=800,status=yes,toolbar=1,menubar=no,location=no,scrollbars=yes");
    }

}

function getTable(tbmName,factoryType){

    if(factoryType == "厂内交货" ){
        return;
    }
    if(factoryType == null || factoryType == "" ){
        alertDiv("提示","请选择节点");
        return;
    }
    var paramJsonObj = {};
    paramJsonObj.tbmName = tbmName;
    paramJsonObj.factoryType = factoryType;

    var xhr = $.ajax({
        type: "get",
        url:toolkitPath+'/raising/forward/tbmManage/factory/getFileTable.do',
        data: {"ajaxParam":JSON.stringify(paramJsonObj)},
        timeout: 20000,          // 设置超时时间 20s
        dataType: "json",
        success:function (paramJsonObj) {
            if(paramJsonObj.status == "0"){
                info.rows = paramJsonObj.dataList;
            }else{
                info.rows = paramJsonObj.dataList;
            }
        },
        complete: function (XMLHttpRequest,status) {
            if(status == 'timeout') {
                xhr.abort();    // 超时后中断请求
                alert("请求时间超过30s,请求中断，请刷新。")
                location.reload();
            }
        }
    })
}

function showTree() {
    var handleCheckFunct = function (option) {
        var children = option.core.data.children;
        if (children != undefined && children != null && children.length > 0) {
            var oneChild = children[0];
            oneChild.state.checked = true;
        }
    };

    var option = {
        checkbox: {
            keep_selected_style: false,
            tie_selection: false,
            three_state: false,
            cascade: 'undetermined'
        },
        "plugins": ["themes", "wholerow"]
    };

    $("#bigTree").bxtree({
        ccsId: "factory",
        showText: '变量列表',
        option: option,
        dataPattern: 'ccs',
        preHandlerFunct: handleCheckFunct
    }).on("select_node.jstree",function (event, obj) {
        var selectNode =  obj.node ;
        info.title = selectNode.text;
        getTable($.cookie("selected_tbmName"),selectNode.text);
        nodeState=true;
    });
}

function downloadTemplet() {
    window.location.href =toolkitPath +'/raising/forward/tbmManage/factory/downloadTemplet.do';
}



