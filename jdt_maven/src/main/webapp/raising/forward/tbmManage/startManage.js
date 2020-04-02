
var info = new Vue({
    el:"#father",
    data:{
        title:"始发管理",
        secondLevel:"",
        threeLevel:"",
        rows:[],
        isUpload:false
    }
})

$(document).ready(function () {
    initDomHeight();
    //更改模板下载超链接的位置
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
        "id":"mts_ok",
        click : function () {
            startManageUpload();
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

function  startManageUpload() {
    var result = check();
    if(result.status == false){
        alertDiv("提示",result.message);
        return;
    }

    $("#type").val(result.fileName);
    var buttons = [{
        html: "<i class='ace-icon fa fa-check bigger-110'></i>&nbsp; 是",
        "class": "btn btn-skinColor btn-xs",
        click: function () {
            $("[class='btn btn-skinColor btn-xs']").prop("disabled","disabled");
            $(this).dialog("close");

            var obj = document.getElementById("file");
            var fileName = obj.files[0].name;
            var prefix  = fileName.substring(0,fileName.indexOf("."));
            var option = {
                url: toolkitPath+'/raising/forward/tbmManage/startManage/dataUpload.do', //这里写你的url
                type : 'POST',
                datatype:'json',//这里是返回类型，一般是json,text
                data:{"tbmName":$.cookie("selected_tbmName"),"tbmId":$.cookie("selected_tbmId"),"directory":result.fileName,"secondLevel":prefix},
                clearForm: true,//提交后是否清空
                success : function(data) {
                    info.isUpload = false;
                    $("button[data-dismiss='modal'][class='bootbox-close-button close']").click();//关闭文件上传进度条。
                    if(data.status == "0"){
                        alertDiv("提示",data.message);
                        $("#formDiv").bxdialog("close");
                        $("[class='btn btn-skinColor btn-xs']").removeProp("disabled");
                        showTree();
                        //var param = {"tbmName":$.cookie("selected_tbmName"),"secondLevel":info.secondLevel};
                        //AjaxCommunicator.ajaxRequest('/raising/forward/tbmManage/startManage/convertFileToPDF.do', 'GET', param,{onSuccess:function () {}},true);
                    }else{
                        alertDiv("提示",data.message);
                        $("#formDiv").bxdialog("close");
                    }
                } ,
                error:function(data){
                    $("button[data-dismiss='modal'][class='bootbox-close-button close']").click();//关闭文件上传进度条。
                    info.isUpload = false;
                    alertDiv("提示","页面请求失败");
                    $("#formDiv").bxdialog("close");
                },
                uploadProgress: function (event, position, total, percentComplete) {
                    percentVal = percentComplete + '%';
                    $("#progressBack").attr("data-percent",percentVal);
                    $("#progress").css("width",percentVal);
                    console.log(percentVal,total,position);
                }
            };
            $("#startManageForm").ajaxSubmit(option);
            $("#formDiv").bxdialog("close");
            info.isUpload = true;
            bootbox.dialog({
                title: "始发管理文件上传 ",
                message:"",
                message: "<div class=\"progress\" id=\"progressBack\" data-percent=\"0%\"'>\n" +
                "<div class=\"progress-bar\" id=\"progress\" style=\"width:0%;\"></div></div>"

            });
            return false;
        }
    }, {
        html: "<i class='ace-icon fa fa-times bigger-110'></i>&nbsp; 否",
        "class": "btn btn-xs",
        click: function () {
            $(this).dialog("close");
            return;
        }
    }];
    confirmDiv("提示", "你当前上传的目录为 '"+result.fileName+"'，确定上传吗？", buttons);
    return false;
}


function download() {
    var len = $("input[name = 'record']:checked").length ;
    if(len < 1){
        alertDiv("提示","至少选择一份文件下载。");
        return;
    }
    var secondLevel =  info.secondLevel ;
    var threeLevel = info.threeLevel;
    var fileNames = [];
    $("input[name = 'record']:checked").each(function () {
        var fileName = $(this).parent().next("td").html();
        fileNames.push(fileName);
    })
    var param = {"secondLevel":secondLevel,"threeLevel":threeLevel,"fileNames":fileNames,"tbmName":$.cookie("selected_tbmName")};
    var paramStr = JSON.stringify(param);
    window.location.href =toolkitPath +'/raising/forward/tbmManage/startManage/downloadFile.do?ajaxParam='+encodeURIComponent(encodeURIComponent(paramStr));
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
            if(isNullOrEmptyOrUndefiend(info.secondLevel)){
                alertDiv("提示",info.secondLevel+"不能删除");
                return;
            }
            var fileNames = [];
            var indexArray = [];
            var secondLevel =  info.secondLevel ;
            var threeLevel = info.threeLevel;
            $("input[name = 'record']:checked").each(function () {
                indexArray.push($(this).val());
                var fileName = $(this).parent().next("td").html();
                fileNames.push(fileName);
            })
            var param = {"secondLevel":secondLevel,"threeLevel":threeLevel,"fileNames":fileNames,"tbmName":$.cookie("selected_tbmName")};
            $.ajax({
                type:"post",
                url:toolkitPath+'/raising/forward/tbmManage/startManage/deleteFile.do',
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


function getTable(tbmName,secondLevel,threeLevel){

    if(info.threeLevel == null || info.threeLevel == "" ){
        alertDiv("提示","请选择三级节点");
        return
    }
    var paramJsonObj = {};
    paramJsonObj.tbmName = tbmName;
    paramJsonObj.secondLevel = secondLevel;
    paramJsonObj.threeLevel = threeLevel;

    var xhr = $.ajax({
        type: "get",
        url:toolkitPath+'/raising/forward/tbmManage/startManage/getFileTable.do',
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
    var option = {
        'core': {
            'data': {
                'url': function (node) {
                    return httpServerPath+'/raising/forward/tbmManage/startManage/getTree.do';
                },
                'data': function (node) {
                    var nodeId = node.id;
                    var nodeArray = nodeId.split("-");
                    if (nodeId == "#") {// 点击的是根节点
                        return {
                            'fdParentId': '0','treeparentId': nodeId,'tbmName':$.cookie("selected_tbmName")
                        };
                    } else {// 点击的是子节点
                        return {
                            'fdParentId': nodeArray[0],'treeparentId': nodeId,'tbmName':$.cookie("selected_tbmName")
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
    $("#bigTree").bxtree(treeOption)
        .on("select_node.jstree",function (event, obj) {
            var selectNode =  obj.node ;
            var id = selectNode.id;
            var title = selectNode.text;
            var parrntArr = selectNode.parents;
            if(parrntArr.length == 1){
                info.title = "始发管理";
                info.secondLevel = "";
                info.threeLevel = "";
                return;
            }
            if(parrntArr.length == 2){
                info.title = title;
                info.secondLevel = title;
                info.threeLevel = "";
                getSecond($.cookie("selected_tbmName"),title);
                return ;
            }
            var secondId = parrntArr[0] + "_anchor";
            var secondText = $("#"+secondId).text();
            info.secondLevel = secondText;
            info.threeLevel = title;
            info.title = secondText+"—"+title;
            getTable($.cookie("selected_tbmName"),secondText,title);
        })

}

function getSecond(tbmName,secondLevel){

    if(info.secondLevel == null || info.secondLevel == "" ){
        alertDiv("提示","请选择三级节点");
        return
    }
    var paramJsonObj = {};
    paramJsonObj.tbmName = tbmName;
    paramJsonObj.secondLevel = secondLevel;

    var xhr = $.ajax({
        type: "get",
        url:toolkitPath+'/raising/forward/tbmManage/startManage/getSecondFileTable.do',
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

function downloadTemplet() {
    window.location.href =toolkitPath +'/raising/forward/tbmManage/startManage/downloadTemplet.do';
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
    param.secondLevel = info.secondLevel;
    param.threeLevel = info.threeLevel;
    param.fileName = fileName;
    param.tbmName = $.cookie("selected_tbmName");
    var img = ".jpg|.gif|.jpeg|.png|.bmp|.pdf";
    if(img.indexOf(postFix) < 0){
        $.ajax({
            type:"get",
            url:toolkitPath+'/raising/forward/tbmManage/startManage/checkPreview.do',
            data:{"ajaxParam":JSON.stringify(param)},
            dataType:"json",
            //发送方式改为同步，避免弹出页面被浏览器拦截
            async: false,
            success:function(data){
                if(data.status == "-1"){
                    alertDiv("提示",data.message);
                    return;
                }
                window.open(toolkitPath+'/raising/forward/tbmManage/startManage/preview.do?ajaxParam='+encodeURIComponent(encodeURIComponent(JSON.stringify(param))),
                    "_blank","top=200,left=200,height=600,width=800,status=yes,toolbar=1,menubar=no,location=no,scrollbars=yes");


            }
        })
    }else{
        window.open(toolkitPath+'/raising/forward/tbmManage/startManage/preview.do?ajaxParam='+encodeURIComponent(encodeURIComponent(JSON.stringify(param))),
            "_blank","top=200,left=200,height=600,width=800,status=yes,toolbar=1,menubar=no,location=no,scrollbars=yes");
    }

}


function check(){
    var result = {};
    result.message = "";
    result.status = false;
    result.fileName = "";

    var obj = document.getElementById("file");
    var file = obj.files[0];

    var brokerage = $("#brokerage").val();
    var brokeragePhone = $("#brokeragePhone").val();
    var contacts = $("#contacts").val();
    var contactsPhone = $("#contactsPhone").val();
    var createTime = $("#createTime").val();
    if(brokerage == "" || brokeragePhone == "" ||  contacts == "" || contactsPhone == "" || file == undefined || createTime == ""){
        result.message = "红星标记数据项不可以为空！";
        return result;
    }
    if(!checkMobile(brokeragePhone) || !checkMobile(contactsPhone)){
        result.message = "电话必须是11位数字！";
        return result;
    }

    var fileName = file.name;
    var prefix  = fileName.substring(0,fileName.indexOf("."));
    var suffix = fileName.substring(fileName.indexOf("."));
    if(suffix != ".zip"){
        result.message ="文件格式不正确，只能上传zip文件，上传失败";
        return;
    }
    if(file.size.toFixed(1) > 200*1024*1024){
        result.message = "文件大小应不超过200M，上传失败";
        return;
    }
    result.fileName = prefix;
    if(info.title != "始发管理"){//如果是选中二或三级节点上传，则默认上传至选中的二级目录
        if(info.secondLevel != prefix){
            result.message = "当前上传的目录是"+info.secondLevel+",上传的压缩文件必须与目录名相同";
        }else{
            result.status = true;
        }
    }else{//如果选中始发管理节点上传
        var len = 0;
        $("li[aria-level = 2]").each(function () {
            len++;
        })
        var temp = {};
        temp.one = "首次始发";
        temp.two = "二次始发";
        temp.three = "三次始发";
        temp.four = "四次始发";
        temp.five = "五次始发";
        var sourcesArr = ["首次始发","二次始发","三次始发","四次始发","五次始发","六次始发",
            "七次始发","八次始发","九次始发","十次始发","十一次始发","十二次始发","十三次始发","十四次始发",
            "十五次始发","十六次始发","十七次始发","十八次始发","十九次始发","二十次始发"];
        var index = -1;
        for(var i = 0;i<sourcesArr.length;i++){
            if(sourcesArr[i] == prefix){
                index = i;
                break;
            }
        }
        if(len == 0 && index > len){
            result.message = "你并未上传任何文件，现在只能上传"+temp.one+".zip";
        }else if(index == -1 || index > len){
            var maxFileName = sourcesArr[len];
            result.message = "上传文件名不合法，当前最多上传"+maxFileName;
        }else{
            result.status = true;
        }
    }


    return result;


}
