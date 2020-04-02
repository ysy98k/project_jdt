$(document).ready(
    function () {
        //1024下报警条显示
        var parentGroupName = window.parent.$("#dataTree").attr("data-parentGroupName");
        var curHeight = document.documentElement.clientHeight;
        if (curHeight < 645) {
            $(".page-content").css("padding-top", "0px");
            $(".alertdiv").css("margin-bottom", "10px");
        }
        $.ajax({
            url:toolkitPath+'/raising/ccs/newCCShandler/combobox.do?ajaxParam=service',
            method:"get",
            dataType:"json",
            async:false,
            success:function(data){
                if(data != null && data != undefined){

                    var optionStr = "";
                    if("admingroup" != parentGroupName ){//如果不是管理员组。则只显示两条记录
                        optionStr = "<option value='JDT'>菜单资源</option>" +
                                    "<option value='PROP'>项目资源</option>";
                    }else{
                        for(var i =0;i<data.length;i++){
                            var value = data[i].ccsId.split(".")[1];
                            optionStr += "<option value='"+value+"'>"+data[i].ccsStr+"</option>";
                        }
                    }
                    var selectStr = "<select id=\"inqu_status-service_select\" name=\"undefined_select\" style=\"width:100%\" class=\"form-control\">"+optionStr+"</select>";

                    $("#inqu_status-service").append(selectStr);

                }
            }
        })
        var colNames = ["资源名",'资源显示名', '资源描述'];
        var colMode = [
                {name: 'name',index: 'name',width: 70},
                {
                    name: 'display_name',
                    index: 'display_name',
                    width: 70
                },
                {
                    name: 'description',
                    index: 'description',
                    width: 70
                }];

        baosightRequire.requireFunct(['bxgrid','bxcombobox'], function () {

            /********************************拥有资源*********************************************/
            var groupresourcegridOption = {
                colNames: colNames,
                colModel: colMode,
                sorttable: true,
                sortname: 'name',
                sortorder: 'asc',
                caption: false
            };

            var groupresourceoption = {
                queryParam: {
            		'name':'',
                    'selectgroup': $("#inqu_status-groupname").val(),
                    'service':   $("#inqu_status-service_select").val()
                },
                dataPattern: "url",
                url : "/raising/backstage/privilege/roleResource/getResourcesRows.do",
                gridOption: groupresourcegridOption,
                navGridOption : {
    				  edit: false,
    				 add: true,
		                addicon: 'ace-icon fa fa-plus-circle purple',
		                addfunc: insertResource,
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
            $("#jqGrid").bxgrid(groupresourceoption);  
            
        });

    });

function on_query_click() {
    var parentGroupName = window.parent.$("#dataTree").attr("data-parentGroupName");
    var queryParam = {
        'selectgroup': $("#inqu_status-groupname").val(),
        'service':   $("#inqu_status-service_select").val(),
        'name':$("#inqu_status-name").val()
    };
    $("#jqGrid").bxgrid("option", "queryParam", queryParam);
    $("#jqGrid").bxgrid("query");
}


/**********************************************操作资源****************************************************/

function windowopen(url,name) {
	var windowopen2 = parent.window.windowopen2 ;
	windowopen2(url,name);
}
function insertResource(){
	windowopen(toolkitPath +　"/raising/backstage/privilege/roleResource/getResourcePage.do","资源信息管理");
}

function deleteResource(){
	var resourcename = "";
	var selectId = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
	for(var i = 0;i < selectId.length; i++){
		var selectData = $("#jqGrid").bxgrid('rawMethodCall', 'getRowData', selectId[i]);
		resourcename += selectData.name;
		if(i < selectId.length-1){
			resourcename += ",";
		}
	}
	var paramObj = new Object();
	paramObj.name = resourcename;
	paramObj.service = $("#inqu_status-service_select").val();
	paramObj.selectgroup = $("#inqu_status-groupname").val();
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
	AjaxCommunicator.ajaxRequest('/raising/backstage/privilege/roleResource/deleteRows.do', 'POST',paramObj, callback,true);
}

function confirmResource(resourceArray){
	var paramObj = new Object();
    paramObj.resource = resourceArray;
    paramObj.selectgroup = $("#inqu_status-groupname").val();
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
	AjaxCommunicator.ajaxRequest('/raising/backstage/privilege/roleResource/bindResource.do', 'POST',paramObj, callback,true);
}

function deletePage() {
	var resourcename = "";
	var selectId = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
	for(var i = 0;i < selectId.length; i++){
		var selectData = $("#jqGrid").bxgrid('rawMethodCall', 'getRowData', selectId[i]);
		resourcename += selectData.name;
		if(i < selectId.length-1){
			resourcename += ",";
		}
	}
	
    dialogMessage("确认删除", "数据删除后将不可恢复，<br>是否确定删除对资源"+resourcename+"的授权？");
}


function dialogMessage(title, centext) {
    $("#dialogInfo").html(centext);
    var dialog = $("#dialog-message").removeClass('hide').dialog({
        modal: true,
        title:  title ,
        title_html: true,
        buttons: [{
            text: "确认",
            "class": "btn btn-skinColor btn-xs",
            click: function() {
            	deleteResource();
                $(this).dialog("close");
            }
        }]
    });
}



