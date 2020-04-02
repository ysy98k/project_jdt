$(document).ready(

    function () {
        var selectGroupId =  window.parent.$("#inpopup").attr("data-selectGroupId");
        var selectGroupName =  window.parent.$("#inpopup").attr("data-selectGroupName");
        $("#inqu_status-groupname").val(selectGroupName);
        //1024下报警条显示

        var curHeight = document.documentElement.clientHeight;
        if (curHeight < 645) {
            $(".page-content").css("padding-top", "0px");
            $(".alertdiv").css("margin-bottom", "10px");
        }
        baosightRequire.requireFunct(['bxgrid'], function () {
        	                 
            /********************************拥有成员*********************************************/           
            var groupresourcegridOption = {
                colNames: ['roleMemberId','userId','用户名','显示名', '用户描述','电话','邮箱','工号'],
                colModel: [
                {
                    name: 'roleMemberId',
                    index: 'roleMemberId',
                    hidden: true
                },
                {
                    name: 'userId',
                    index: 'userId',
                    hidden: true
                },
                {
                    name: 'username',
                    index: 'username',
                    width: 60
                }, 
                {
                    name: 'displayName',
                    index: 'displayName',
                    width: 60
                },
                {
                    name: 'description',
                    index: 'description',
                    width: 60
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
                    width: 60
                }],
                sorttable: true,
                sortname: 'username',
                sortorder: 'asc',
                caption: false
            };

            var groupuseroption = {
                queryParam: {
                    inqu_status:{groupId:selectGroupId}
                },
                dataPattern: "url",
                url : "/raising/backstage/privilege/roleUser/getRows.do",
                gridOption: groupresourcegridOption,
                navGridOption : {
              	  edit: false,
		                add: true,
		                addicon: 'ace-icon fa fa-plus-circle purple',
		                addfunc: insertUser,
		                del: true,
		                delicon: 'ace-icon fa fa-trash-o red',
		                delfunc: deletePage,
                        search:false,
                        refresh:false
				},
                showMsgOpt: {
                    showMsgId: "alertdiv"
                }
            };
            $("#jqGrid").bxgrid(groupuseroption);  
            
        });

    }
    );

function on_query_click() {
    var selectGroupId =  window.parent.$("#inpopup").attr("data-selectGroupId");
    var showMsgOpt = {
        showMsgId: "alertdiv",
        status: null,
        showMsg: null
    };

    $("#jqGrid").bxgrid("option", "showMsgOpt", showMsgOpt);
    var queryParam = {
        inqu_status:{"username":$("#inqu_status-username").val(),"groupId":selectGroupId},
        condition:true
    };
    $("#jqGrid").bxgrid("option", "queryParam", queryParam);
    $("#jqGrid").bxgrid("query");
}


/**********************************************操作资源****************************************************/
function windowopen(url,name)
{
	var windowopen2 = parent.window.windowopen2;
	windowopen2(url,name);
}

function insertUser(){
	windowopen(toolkitPath +　"/raising/backstage/privilege/roleUser/getUserSelect.do","用户信息管理");
}

function deleteUser(){
    var currentGroupIds =  window.parent.$("#dataTree").attr("data-parentId");
    var currentGroupNames = window.parent.$("#dataTree").attr("data-parentgroupname");
    var groupIdsArr = currentGroupIds.split(",");
    var groupNamesArr = currentGroupNames.split(",");
    var babaId = groupIdsArr[0];
    var babaName = groupNamesArr[0];


	var selectId = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    var paramObj = new Object();
    paramObj.currentUserGroupId = babaId;
    paramObj.currentUserGroupName = babaName;
    paramObj.selectGroupName = $("#inqu_status-groupname").val();
    paramObj.result = {};
    paramObj.result.resultRow = [];

    for(var i = 0;i < selectId.length; i++){
		var selectData = $("#jqGrid").bxgrid('rawMethodCall', 'getRowData', selectId[i]);
		var temp = {};
		temp.roleMemberId = selectData.roleMemberId;
		temp.userId = selectData.userId;
		temp.username = selectData.username;
		temp.modifyPersonId = selectData.roleMemberId;
        paramObj.result.resultRow.push(temp);
	}
	var callback = {
		onSuccess : function(paramJsonObj) {
			var showMsgOpt = {
				showMsgId : "alertdiv",
				status : paramJsonObj.status,
				showMsg :paramJsonObj.returnMsg
			};
			$("#jqGrid").bxgrid("option", "showMsgOpt", showMsgOpt);	
			$("#jqGrid").bxgrid("refresh");
		}
	}
	AjaxCommunicator.ajaxRequest('/raising/backstage/privilege/roleUser/delete.do', 'POST',paramObj, callback,true);
}


function confirmUser(userArray){
    if(userArray== null || userArray.length < 1){
        return;
    }
	var paramObj = new Object();
    var roleMembers = [];
	paramObj.detail = {};
    paramObj.detail.resultRow = [];

	var selectGroupId = window.parent.$("#inpopup").attr("data-selectGroupId");
    for(var i =0;i<userArray.length;i++){
        var temp = {};
        temp.userId = userArray[i];
        temp.groupId = selectGroupId;
        paramObj.detail.resultRow.push(temp);
    }
	var callback = {
		onSuccess : function(paramJsonObj) {	
			var showMsgOpt = {
				showMsgId : "alertdiv",
				status : paramJsonObj.status,
				showMsg : paramJsonObj.returnMsg
			};
			$("#jqGrid").bxgrid("option", "showMsgOpt", showMsgOpt);	
			$("#jqGrid").bxgrid("refresh");
		}
	}
	AjaxCommunicator.ajaxRequest('/raising/backstage/privilege/roleUser/bindUser.do', 'POST',paramObj, callback,true);
}

function deletePage() {
    var username = "";
	var selectId = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
	for(var i = 0;i < selectId.length; i++){
		var selectData = $("#jqGrid").bxgrid('rawMethodCall', 'getRowData', selectId[i]);
		username += selectData.username;
		if(i < selectId.length-1){
			username += ",";
		}
	}
    dialogMessage("确认删除", "数据删除后将不可恢复，<br>是否确定从角色中删除用户"+username+"？");
}


function dialogMessage(title, centext) {
    $("#dialogInfo").html(centext);
    var dialog = $("#dialog-message").removeClass('hide').dialog({
        modal: true,
        title: title,
        title_html: true,
        buttons: [{
            text: "确认",
            "class": "btn btn-skinColor btn-xs",
            click: function() {
            	deleteUser();
                $(this).dialog("close");
            }
        }]
    });
}


