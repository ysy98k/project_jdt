$(document).ready(function () {
    baosightRequire.requireFunct(['bxdialog'], function () {
        var paramJsonObj = {};
        if(isAvailable($("#taskId").val())){
            paramJsonObj.taskId = $("#taskId").val();
            var callback = {
                onSuccess: function (returnObj) {
                    if(returnObj.status == "0"){
                        $("#days").val(returnObj.days);
                        $("#reason").val(returnObj.reason);
                    }
                }
            };
            AjaxCommunicator.ajaxRequest('/df/example/leavewk.do?method=queryData', 'POST', paramJsonObj, callback);
        }
    });
});

function submitForm() {
    var paramJsonObj = {};
    if(isAvailable($("#taskId").val())){
        paramJsonObj.taskId = $("#taskId").val();
    }
    if(isAvailable($("#processDefId").val())){
        paramJsonObj.processDefId = $("#processDefId").val();
    }
    paramJsonObj.days =  $("#days").val();
    paramJsonObj.reason =  $("#reason").val();
    var callback = {
        onSuccess: function (returnObj) {
            if(returnObj.status == "0"){
                alertDiv("提示", returnObj.returnMsg);
            }
        }
    };
    AjaxCommunicator.ajaxRequest('/df/example/leavewk.do?method=submit', 'POST', paramJsonObj, callback);
}