$(document).ready(function() {
    tenantWithLockScreen = $("#valueSecond").val();
    usernameWithLockScreen = $("#valueThird").val();
});
var tenantWithLockScreen;
var usernameWithLockScreen;
function updatePW() {
    $("#navbar").css("display","none");
    var title = '修改密码';
    var buttons = [{
        text: "取消",
        "class": "btn btn-xs",
        click: function() {
            $(this).dialog("close");
        }
    },
        {
            text: "保存",
            "class": "btn btn-skinColor btn-xs",
            click: function() {
                if ($("#detail-password").val() == "" || $("#surePassword").val() == "" ) {
                    dialogMessage("确认密码信息", "密码框不能为空！");
                    return false;
                }
                if ($("#detail-password").val() != $("#surePassword").val()) {
                    dialogMessage("确认密码信息", "新密码不一致，请重新输入！");
                    return false;
                }
                var postParam = {
                    username: $("#detail-username").val(),
                    password: $("#detail-password").val(),
                };
                var $this = $(this);
                var callback = {
                    onSuccess: function(postData) {
                        if (postData.errcode == 0) {
                            $this.dialog("close");
                            window.location.href = toolkitPath + "/logout";
                        } else {
                            dialogMessage("操作失败", "" + postData.errcode + ":" + postData.errinfo);
                        }

                    }
                };
                AjaxCommunicator.ajaxRequest('/df/metamanage/user.do?method=modifyPassword', 'POST', postParam, callback);
            }
        }];

    var dialogOpt = {
        title: title,
        buttons: buttons
    };
    $("#detail").bxdialog(dialogOpt);

    $("#loginName").attr("readonly", true);
}

function dialogMessage(title, centext) {
    $("#dialogInfo").html(centext);
    var dialog = $("#dialog-message").removeClass('hide').dialog({
        modal: true,
        title: "<div class='widget-header'><h4 class='smaller'><i class='ace-icon fa fa-exclamation-triangle red'></i> " + title + "</h4></div>",
        title_html: true,
        buttons: [{
            text: "了解",
            "class": "btn btn-skinColor btn-xs",
            click: function() {
                $(this).dialog("close");
            }
        }]
    });
}

openWin = function(flag) {
    $("#navbar").css("display","none");
    var content = document.getElementById('content');
    var full = document.getElementById('full');
    if (flag == 'show') {
        content.style.display = 'block';
        full.style.display = 'block';
        lockDialog();
    } else {
        content.style.display = 'none';
        full.style.display = 'none';
    }
};

function lockDialog() { //弹出锁屏对话框
    var tempPassword;
    var title = '输入解锁密码';
    var buttons = [{
        text: "确定",
        "class": "btn btn-skinColor btn-xs",
        click: function() {
            tempPassword = $("#unlockPassword").val();
            var result = checkLockPwd(tempPassword);
            if (result == true) {
                $("#unlockPassword").val('');
                $(this).dialog("close");
                openWin('openWin');
            } else {
                $("#unlockPassword").val('');
                alertDiv("提示", "请输入正确的解锁密码！");
            }
        }
    }];

    var dialogOpt = {
        title: title,
        buttons: buttons
    };
    $("#unlock").bxdialog(dialogOpt);
    $(".ui-dialog-titlebar-close").hide(); //隐藏解锁对话框的“关闭”按钮
}

function checkLockPwd(tempPassword) { //检查解锁密码
    var tempTenant = tenantWithLockScreen;
    var tempUsername = usernameWithLockScreen;
    var password = tempPassword;
    var result;
    var paramJsonObj = {
        "tenant": tempTenant,
        "username": tempUsername,
        "password": password
    };

    var callBack = {
        onSuccess: function(paramJsonObj) {
            if (paramJsonObj.errcode == "0") {
                result = true;
            } else {
                result = false;
            }
        }
    };
    AjaxCommunicator.ajaxRequest('/df/metamanage/user.do?method=check', 'POST', paramJsonObj, callBack);
    return result;
}

document.onkeydown = function() { //禁用f5
    if (event.keyCode == 116) {
        event.keyCode = 0;
        event.returnValue = false;
    }
};

document.oncontextmenu = function() { //禁用右键刷新
    event.returnValue = false;
};
