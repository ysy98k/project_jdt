


$(document).ready(function () {
    $("#currentTbm").html("["+$.cookie("selected_tbmName")+"]");
    $("#tbmNames").combobox({
        panelMaxHeight:200,
        panelMinHeight:100,
        method:'get',
        url : toolkitPath+'/raising/forward/tbmManage/getTbmInfosSelect.do',
        valueField : 'tbmId',
        textField : 'tbmName',
        width:200,
        editable:true,
        hasDownArrow:true,
        filter: function(q, row){
            var opts = $(this).combobox('options');
            return row[opts.textField].indexOf(q) == 0;
        },
        onLoadSuccess: function () { //数据加载完毕以后，默认选中第
            $("#status").next().css("border","1px solid #d5d5d5");
            $("#status").next().css("margin-left","10px");

        }
    });

})


function insertPage() {
    var button = [{
        text: "确定",
        "class" : "btn btn-skinColor btn-xs",
        click : function () {
            var tbmId = $("#tbmNames").combobox("getValue");
            var tbmName = $("#tbmNames").combobox("getText");
            $("#detail").bxdialog('close');
            $.cookie('selected_tbmId',tbmId, {path: "/"});
            $.cookie('selected_tbmName',tbmName, {path: "/"});
            window.location.reload();

        }
    }]
    var title = "选择盾构机";
    var dialogOpt = {
        title: title,
        buttons:button,
        width: '400px'

    };
    $("#detail").bxdialog(dialogOpt);
    $("[type='button'][class='ui-dialog-titlebar-close']").bind("click",function () {//对话框关闭时，保证combox隐藏
        $("div[class='panel combo-p panel-htop']").css("display","none");
    })


}

/**
 * 技术服务管理点击事件
 */
function tbmManage() {
    parent.parent.document.getElementById("contentFrame").src= toolkitPath+"/raising/forward/tbmManage/tbmManage.jsp";
}