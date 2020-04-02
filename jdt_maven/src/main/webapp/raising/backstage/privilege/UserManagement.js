var info = new Vue({
    el:"#detail",
    data:{
        groupNamesStr:"",
        message:"yiasd"
    }
})

$(document).ready(function() {

    //1024下报警条显示
    var curHeight = document.documentElement.clientHeight;
    if (curHeight < 645) {
        $(".page-content").css("padding-top", "0px");
        $(".alertdiv").css("margin-bottom", "10px");
    }
    $("#messageDiv").hide();

    $('#detail-username').change(function (event) {
        if ($('#detail-displayName').val() == "")
            $('#detail-displayName').val($('#detail-username').val());
    });
    //初始画新增表单。


    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxvalidate','bxtimepicker'],
    function() {
        initWidgets();
        var colNames = ['用户ID', '用户名', '用户显示名', '用户描述','用户角色集合','电话','邮箱','工号','有效期','操作'];
        var colModels = [{
          name: 'userId',
          index: 'userId',
          width: 60,
          hidden: true
	    },
        {
          name: 'username',
          index: 'username',
          width: 50
	    },
	    {
          name: 'displayName',
          index: 'displayName',
          width: 50
	    },
	    {
          name: 'description',
          index: 'description',
          width: 90
	    },
        {
            name: 'groupNamesStr',
            index: 'groupNamesStr',
            width: 90/*,
            hidden: true*/
        },
        {
            name: 'telephone',
            index: 'telephone',
            width: 60
        },
        {
            name: 'email',
            index: 'email',
            width: 60
        },
        {
            name: 'employeeId',
            index: 'employeeId',
            width: 40
        },
        {
            name : 'validity',
            index : 'validity',
            width : 70
        },
        {
            name : 'operator',
            index : 'operator',
            width : 100,
            formatter : function(value, grid, row, state) {
                var btnReset =
                    "<div style='text-align:center;display: inline-block;min-width: 145px'>" +
                        "<div class='btn btn-skinColor btn-xs' style='height:21px;border:0;margin-right: 5px;float:left'>" +
                            "<a href='javascript:setValidity("+ "\""+ row.userId+ "\");'><font color='#FFFFFF'>设置有效期</font></a>" +
                        "</div>" +
                    "</div>";
                return btnReset;
            }
        }];
        

        
        var gridOption = {
        	colNames:colNames,
        	colModel:colModels,
        	sorttable: true,
            sortname: 'uid',
            sortorder: 'asc',
            caption: false,
            height:395,
            jsonReader: {
              id: "uid",
              repeatitems: false
            }
         };

        var option = {
            queryParam: {inqu_status:{groupNames : groupNames}},
            dataPattern : "url",
            url: "/raising/backstage/privilege/userManagement/getRows.do",
            gridOption: gridOption,
            showMsgOpt: {
                showMsgId: "alertdiv"
            },
            navGridOption : {
				edit: false,
                editicon: 'ace-icon fa fa-pencil blue',
                editfunc: updatePage,
                add: false,
                addicon: 'ace-icon fa fa-plus-circle purple',
                addfunc: insertPage,
                del: false,
                delicon: 'ace-icon fa fa-trash-o red',
                delfunc: deletePage,
                search:false,
                refresh:false
			}
        };

        $("#jqGrid").bxgrid(option);
    });
    info.groupNamesStr = "14585214784845152" ;
});

function showErrorMessage() {
	var dialogOpts = {
			   buttons: {
			      "Ok": function() { } ,
			      "Cancel": function() {}
			   }
			}
			$("#dialog-message").dialog(dialogOpts);
}

function insertPage() {
    $('#detail-groupId').next(".combo").show();
    $("#groupNamesStr").hide();

    $("#required").html("*");
    $("#detail-username").attr("disabled", false);
    $("#password").show();
    $("#confirm_password").show();

    $("#detail-groupId").find("option").eq(0).prop("selected",true);
    $("#detail").bxdiv("cleanPopDiv");

    
    var buttons = [{
        text: "保存",
        "class": "btn btn-skinColor btn-xs",
        click: function() {
        	var username = $('#detail-username').val();
        	var disp = $('#detail-displayName').val();
        	var groupId = $("#detail-groupId").combobox("getValue");
            var v = $('#detail-groupId').combobox('getText');

        	if(disp==""){
        		$('#detail-displayName').val(username);
        	}      
        	var password=$('#thepassword').val();
        	$('#detail-password').val(password);

        	if(isNullOrEmptyOrUndefiend(groupId)){
                $('#detail-groupId').parent().parent().addClass("has-error");
            }

            if ($("#detail").bxvalidate("validate")) {
                var paramJsonObj = new Object();
                paramJsonObj.currentUserId = window.currentUserId;
                var $this = $(this);
                $("#detail").bxdiv('setInfoFromDiv', paramJsonObj, 'detail');
                paramJsonObj.detail.resultRow[0].parentGroupId = $("#parentGroupId").html();
                paramJsonObj.detail.resultRow[0].telephone = paramJsonObj.detail.resultRow[0].telephone.trim() ;
                paramJsonObj.detail.resultRow[0].email = paramJsonObj.detail.resultRow[0].email.trim() ;
                paramJsonObj.detail.resultRow[0].groupId = groupId;
                var callback = {
                    onSuccess: function(paramJsonObj) {
                        if(isNumber(paramJsonObj.groupId)){//ruguo1
                            $("#detail-groupId").find("option[value*='_default']").attr("value",paramJsonObj.groupId);
                            var groupData =  $("#detail-groupId ").combobox('getData');
                            groupData[0].groupId = paramJsonObj.groupId;
                            $("#detail-groupId ").combobox('loadData',groupData);
                            $("#detail-groupId ").combobox('select',groupData[0].groupId);
                            //$("#detail-groupId ").combobox('loadData');
                        }
                        var showMsgOpt = {
                            showMsgId: "alertdiv",
                            status: paramJsonObj.status,
                            showMsg: paramJsonObj.returnMsg
                        };

                        $("#jqGrid").bxgrid("option", "showMsgOpt", showMsgOpt);
                        $("#jqGrid").bxgrid("query");
                        if(showMsgOpt.status ==0)
                        {
                        	$this.dialog("close");
                        }
                    }
                };
                AjaxCommunicator.ajaxRequest('/raising/backstage/privilege/userManagement/insert.do', 'POST', paramJsonObj, callback,true);
            }

        }
    }];

    var title = "新增账号";

    var dialogOpt = {
        title: title,
        buttons: buttons,
        width: '320px'
    };

    $("#detail").bxdialog(dialogOpt);
    var paramJsonObj = {
        detail: {
            uid: '',
            username: '',
            password: '',
            desc: ''
        }
    };
    $("#detail").bxdiv('fillPopDiv', paramJsonObj, 'detail');
    $("#detail-username").attr("readonly", false);
}

function deletePage() {
	var resourcename = "";
	var selectId = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if(selectId.length == 0){
        alertDiv("提示","请至少勾选一条记录进行操作！")
        return;
    }
	for(var i = 0;i < selectId.length; i++){
		var selectData = $("#jqGrid").bxgrid('rawMethodCall', 'getRowData', selectId[i]);
		resourcename += selectData.username;
		if(i < selectId.length-1){
			resourcename += ",";
		}
	}
	
    dialogMessage("确认删除", "数据删除后将不可恢复，<br>是否确定删除用户"+resourcename+"？");
}

function deleteOK() {
    var paramJsonObj = new Object();

    $("#jqGrid").bxgrid('setInfoFromGrid', paramJsonObj, 'detail');

    var callback = {
        onSuccess: function(paramJsonObj) {
            var showMsgOpt = {
                showMsgId: "alertdiv",
                status: paramJsonObj.status,
                showMsg: paramJsonObj.returnMsg
            };

            $("#jqGrid").bxgrid("option", "showMsgOpt", showMsgOpt);
            $("#jqGrid").bxgrid("query");

        }
    };
    AjaxCommunicator.ajaxRequest('/raising/backstage/privilege/userManagement/delete.do', 'POST', paramJsonObj, callback,true);

}

function updatePage() {
	var selectArray = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if(selectArray.length == 0){
        alertDiv("提示","请至少勾选一条记录进行操作！")
        return;
    }
	if(selectArray.length > 1){
		var buttons = [ 
			{
				text: '了解',
				"class" : "btn btn-skinColor btn-xs",
				click: function() {
					$("#dialog-update-message").bxdialog('close');
				} 
			}
		];
		var dialogOpt = {
			title : "<i class='ace-icon fa fa-warning orange'></i>  记录数错误",
			dataPattern: 'text',
			content : "记录数出错，只能选择一条数据进行修改！",
			buttons : buttons
		};
		$("#dialog-update-message").bxdialog(dialogOpt);
		return;
	}
	
    $("#required").html("");
    $("#detail").bxdiv("cleanPopDiv");
    
    var selectId = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selrow');
    var selectData = $("#jqGrid").bxgrid('rawMethodCall', 'getRowData', selectId);
    $('#detail-groupId').next(".combo").hide();
    info.groupNamesStr = selectData.groupNamesStr;
    $("#groupNamesStr").show();


    var title = '修改账号';
    var buttons = [{
        text: "保存",
        "class": "btn btn-skinColor btn-xs",
        click: function() {
        	//Copy password
        	var password=$('#thepassword').val();
        	$('#detail-password').val(password);

            if ($("#detail").bxvalidate("validate")) {
            	
            	if(password!="")
        		{            		
            		if(password!="_ThePasswordIsNotModified_")
            		{
            			showDialogMessage("确认更改密码", "您正在更改密码，请牢记新密码！");
            		}
        		}
            	
                var paramJsonObj = new Object();
                var $this = $(this);
                $("#detail").bxdiv('setInfoFromDiv', paramJsonObj, 'detail');
                var result = {"resultRow":[]};
                result.resultRow = paramJsonObj.detail.resultRow;
                result.resultRow[0].userId = selectData.userId;
                delete result.resultRow[0].validity;
                paramJsonObj.result = result;
                delete  paramJsonObj.detail;
                var callback = {
                    onSuccess: function(paramJsonObj) {
                        var showMsgOpt = {
                            showMsgId: "alertdiv",
                            status: paramJsonObj.status,
                            showMsg:  paramJsonObj.message
                        };
                        $("#jqGrid").bxgrid("option", "showMsgOpt", showMsgOpt);
                        $("#jqGrid").bxgrid("refresh");
                        $this.dialog("close");
                    }
                };
                AjaxCommunicator.ajaxRequest('/raising/backstage/privilege/userManagement/update.do', 'POST', paramJsonObj, callback,true);
            }
        }
    }];


    var dialogOpt = {
        title: title,
        buttons: buttons
    };

    $("#detail").bxdialog(dialogOpt);
    var paramJsonObj = {
        detail: selectData
    };
    $("#detail").bxdiv('fillPopDiv', paramJsonObj, 'detail');
    
    $("#detail-username").attr("disabled", true);
    $("#password").hide();
    $("#confirm_password").hide();
    
    $("#thepassword").val("_ThePasswordIsNotModified_");
    $("#thepasswordconfirm").val("_ThePasswordIsNotModified_");
    $("#detail-password").val("_ThePasswordIsNotModified_");
    $("#detail-password_confirm").val("_ThePasswordIsNotModified_");

}

function on_query_click() {
    var queryParam = {
        inqu_status:{
            'groupNames' : groupNames,
            'username': $("#inqu_status-username").val(),
            'displayName': $("#inqu_status-displayName").val()
        }
    };

    var showMsgOpt = {
        showMsgId: "alertdiv",
        status: null,
        showMsg: null
    };

    $("#jqGrid").bxgrid("option", "showMsgOpt", showMsgOpt);
    $("#jqGrid").bxgrid("option", "queryParam", queryParam);
    $("#jqGrid").bxgrid("query");
}

function initWidgets() {
    $("#detail").bxdiv();
    $("#validity").bxdiv();
    $("#detail").bxvalidate();
    $("#validity-time").bxtimepicker({
        defaultValue: "",
        option:{
            readOnly:true
        }
    });
    
    var ruleOptionCustom = {
        rules: {
            detailusername: {
                required: true,
                maxlength:32,
                englishCheck: true
            },
            detaildescription: {
                maxlength:1024
            },
            detaildisplayName: {
                required: true,
                maxlength:1024,
                stringCheck: true
            },
            detailgroupId: {
                required: true,
                maxlength:1024,
                stringCheck: true
            },
            thepassword: {
                required: true,
                maxlength:32,
                isPassword: true
            },
            detailtelephone:{
                required: true,
                maxlength:40
            },
            detailemail:{
                maxlength:64
            },
            detailemployeeid:{
                maxlength:64,
                englishCheck: true
            },
            /*detailvalidity:{
                required: true,
                maxlength:64
            },*/
            thepasswordconfirm: {
                required: true,
                equalTo: '#thepassword',
                maxlength:32,
                isPassword: true
            }           
        }
    };
    $("#detail").bxvalidate("option", "ruleOptionCustom", ruleOptionCustom);
    $.ajax({
        "url":toolkitPath+"/raising/backstage/privilege/userManagement/getProgenyGroup.do",
        "data":{"ajaxParam":groupNames},
        "type":"get",
        "dataType":"json",
        "success":function (data) {
            if(data.status = "0"){
                var arr = data.groupList;
                var groupNames = window.groupNames;
                var currentGroupArr = [];
                if(groupNames.indexOf(",") > 0 ){
                    currentGroupArr = groupNames.split(",");
                }else{
                    currentGroupArr.push(groupNames);
                }
                var currentGroupName = arr[0];
                var dataJsonArr = [];

                for(var i = 0;i<arr.length;i++){
                    if(currentGroupArr.indexOf(arr[i].groupName) >= 0 ){//如果当前下标对应的元素组名 同当前用户组名相同。则不显示
                        if(currentGroupName = arr[i].groupName ){//获得当前组的Id
                            var groupId = arr[i].groupId;
                            $("#parentGroupId").html(groupId)
                        }
                        continue;
                    }else{
                        var temp = {};
                        temp.groupId = arr[i].groupId;
                        temp.groupName = arr[i].groupName;
                        dataJsonArr.push(temp);
                    }
                }
                if(dataJsonArr.length < 1){
                    var temp = {};
                    temp.groupId = currentGroupName+"_default";
                    temp.groupName = currentGroupName+"_default";
                    dataJsonArr.push(temp);
                }

                $("#detail-groupId").combobox({
                    panelHeight:'auto',
                    panelMaxHeight:200,
                    panelMinHeight:100,
                    data :dataJsonArr,
                    valueField : 'groupId',
                    textField : 'groupName',
                    width:138,
                    editable:true,
                    hasDownArrow:true,
                    filter: function(q, row){
                        var opts = $(this).combobox('options');
                        return row[opts.textField].indexOf(q) == 0;
                    },
                });
                $("#detail-groupId ").combobox('select',dataJsonArr[0].groupId);

            }
        }
    })


}


function dialogMessage(title, centext) {
    $("#dialogInfo").html(centext);
    var dialog = $("#dialog-message").removeClass('hide').dialog({
        modal: true,
        title: "<div class='widget-header'><h4 class='smaller'><i class='ace-icon fa fa-exclamation-triangle red'></i> " + title + "</h4></div>",
        title_html: true,
        buttons: [{
            text: "确认",
            "class": "btn btn-skinColor btn-xs",
            click: function() {
                if(title=="确认删除"){
                    deleteOK();
                }
                else{
                    abandonOK();
                }
                $(this).dialog("close");
            }
        }]
    });
}

function showDialogMessage(title, centext) {
    $("#dialogInfo").html(centext);
    var dialog = $("#dialog-message").removeClass('hide').dialog({
        modal: true,
        title: "<div class='widget-header'><h4 class='smaller'><i class='ace-icon fa fa-exclamation-triangle red'></i> " + title + "</h4></div>",
        title_html: true,
        buttons: [{
            text: "确认",
            "class": "btn btn-skinColor btn-xs",
            click: function() {
               
                $(this).dialog("close");
            }
        }]
    });
}

//2016.8.29 密码重置
function resetUserPassword(username) {
	var buttons = [ 
		   			{
		   				text: '了解',
		   				"class" : "btn btn-skinColor btn-xs",
		   				click: function() {
		   					$("#dialog-update-message").bxdialog('close');
		   				} 
		   			}
		   		];
	if(name!=null && name!=undefined){
		var paramObj = {
		    	"username":username
		    };
		    var callback = {
		        onSuccess: function(paramJsonObj) {
		        	if(paramJsonObj.status=="0"){
		        		var dialogOpt = {
		        				title : "<i class='ace-icon fa fa-warning orange'></i>  提示",
		        				dataPattern: 'text',
		        				content : "用户"+username+"的密码重置成功，请至少五分钟后再使用新密码进行登录！",
		        				buttons : buttons
		        		};
                    $("#jqGrid").bxgrid("query");
		        	}else{
		        		var dialogOpt = {
		        				title : "<i class='ace-icon fa fa-warning orange'></i>  提示",
		        				dataPattern: 'text',
		        				content : "用户"+username+"的密码重置失败。",
		        				buttons : buttons
		        		};
		        	}
		        	$("#dialog-update-message").bxdialog(dialogOpt);
		        }
		    };
		    AjaxCommunicator.ajaxRequest('/aas/UserManagement.do?method=resetUserPassword', 
			                'POST', paramObj, callback);
	}else{
	    var dialogOpt = {
				title : "<i class='ace-icon fa fa-warning orange'></i>  提示",
				dataPattern: 'text',
				content : "用户"+username+"的参数为undefined或null，请检查。",
				buttons : buttons
		};
		$("#dialog-update-message").bxdialog(dialogOpt);
	}
    
}

//设置有效期
function setValidity(userId) {
    $("#validity-time_dateInput").val("");
    var buttons = [
        {
            text: '保存',
            "class": "btn btn-skinColor btn-xs",
            click: function () {
                var paramObj = {
                    "result":{"resultRow":[{"userId": userId,"validity": $("#validity-time_dateInput").val()+":000"}]}
                 };
                var $this = $(this);
                var callback = {
                    onSuccess: function (paramJsonObj) {
                        var showMsgOpt = {
                            showMsgId: "alertdiv",
                            status: paramJsonObj.status,
                            showMsg: paramJsonObj.returnMsg.replace(/<br>/g, "，")
                        };

                        $("#jqGrid").bxgrid("option", "showMsgOpt", showMsgOpt);
                        $("#jqGrid").bxgrid("query");
                        $this.dialog("close");
                    }
                };
                AjaxCommunicator.ajaxRequest('/raising/backstage/privilege/userManagement/setValidity.do','POST', paramObj, callback,true);
            }
        }
    ];
    var dialogOpt = {
        title : "设置有效期",
        buttons : buttons
    };
    $("#validity").bxdialog(dialogOpt);

}
//批量禁用有效期
function abandon(){
    var resourcename = "";
    var selectId = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if(selectId.length == 0){
        alertDiv("提示","请至少勾选一条记录进行操作！")
        return;
    }
    for(var i = 0;i < selectId.length; i++){
        var selectData = $("#jqGrid").bxgrid('rawMethodCall', 'getRowData', selectId[i]);
        resourcename += selectData.username;
        if(i < selectId.length-1){
            resourcename += ",";
        }
    }

    dialogMessage("确认禁用", "有效期禁用后该用户的权限将失效，<br>是否确定禁用用户"+resourcename+"？");
}

function abandonOK() {
    var paramJsonObj = new Object();

    $("#jqGrid").bxgrid('setInfoFromGrid', paramJsonObj, 'result');
    var rowsArr = paramJsonObj.result.resultRow;
    for(var i=0;i<rowsArr.length;i++){
        var temp = {};
        temp.userId = rowsArr[i].userId;
        temp.validity = rowsArr[i].validity;
        rowsArr[i] = temp;
    }
    var callback = {
        onSuccess: function(paramJsonObj) {
            var showMsgOpt = {
                showMsgId: "alertdiv",
                status: paramJsonObj.status,
                showMsg: paramJsonObj.returnMsg
            };

            $("#jqGrid").bxgrid("option", "showMsgOpt", showMsgOpt);
            $("#jqGrid").bxgrid("query");

        }
    };
    AjaxCommunicator.ajaxRequest('/raising/backstage/privilege/userManagement/abandonValidity.do', 'POST', paramJsonObj, callback,true);

}

//批量设置永久有效期
function keepValidity(){
    var paramJsonObj = new Object();
    var selectId = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if(selectId.length == 0){
        alertDiv("提示","请至少勾选一条记录进行操作！")
        return;
    }

    $("#jqGrid").bxgrid('setInfoFromGrid', paramJsonObj, 'result');
    var rowsArr = paramJsonObj.result.resultRow;
    for(var i=0;i<rowsArr.length;i++){
        var temp = {};
        temp.userId = rowsArr[i].userId;
        temp.validity = rowsArr[i].validity;
        rowsArr[i] = temp;
    }

    var callback = {
        onSuccess: function(paramJsonObj) {
            var showMsgOpt = {
                showMsgId: "alertdiv",
                status: paramJsonObj.status,
                showMsg: paramJsonObj.returnMsg.replace(/<br>/g, "，")
            };

            $("#jqGrid").bxgrid("option", "showMsgOpt", showMsgOpt);
            $("#jqGrid").bxgrid("query");

        }
    };
    AjaxCommunicator.ajaxRequest('/raising/backstage/privilege/userManagement/keepValidity.do', 'POST', paramJsonObj, callback,true);
}
