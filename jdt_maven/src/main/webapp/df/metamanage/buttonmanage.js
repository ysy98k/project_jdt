$(document).ready(function () {
    baosightRequire.requireFunct(['bxtree', 'bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxvalidate', 'bxcombobox'],
        function () {
    	    $("#queryarea").bxdiv();
    	    showTree();
            $("#inqu_status-pageName").attr("readonly", true);
            var gridOption = {
                caption: false,
                primaryRowKey:"buttonId",
                colNames: ['页面号', '按钮序列号', '按钮ID', '按钮显示名', '按钮所属页面英文名', '按钮所属页面中文名', '按钮所属页面路径'],
                colModel: [
                    {
                        name: 'pageId',
                        index: 'pageId',
                        width: 50,
                        hidden: true
                    },
                    {
                        name: 'buttonId',
                        index: 'buttonId',
                        width: 50,
                        hidden: true,
                        forbidCopy : true
                    },
                    {
                        name: 'buttonName',
                        index: 'buttonName',
                        width: 80,
                        sortable: true,
                        editrules:{
							required : true,
                            custom:true,
                            custom_func:bxgrid_englishCheck
						}
                    },
                    {
                        name: 'buttonDisplayname',
                        index: 'buttonDisplayname',
                        width: 80,
                        sortable: false,
                        editable: true,
                        editrules:{
							required : true,
                            custom:true,
                            custom_func:bxgrid_stringCheck
						}
                    },
                    {
                        name: 'pageEname',
                        index: 'pageEname',
                        width: 80,
                        readOnly : true,
                        sortable: false
                    },
                    {
                        name: 'pageCname',
                        index: 'pageCname',
                        width: 80,
                        readOnly : true,
                        sortable: false
                    },
                    {
                        name: 'pagePath',
                        index: 'pagePath',
                        width: 150,
                        readOnly : true,
                        sortable: false,
                    }
                ],
                sortorder: 'desc',
                jsonReader: {
                    id: "buttonId",
                    repeatitems: false
                }
            };

            var option = {
                queryParam: {},
                dataPattern: "url",
                url: "/df/metamanage/buttonManage.do?method=query",
                showMsgOpt: {
                    showMsgId: "alertdiv"
                },
                gridOption: gridOption
            };
            $("#jqGrid").bxgrid(option);
        });
    $("#inqu_status-buttonName").keypress(function () {
        onKeyQuery();
    });
    $("#inqu_status-buttonDisplayname").keypress(function () {
        onKeyQuery();
    });
    $("#inqu_status-pageEname").keypress(function () {
        onKeyQuery();
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
    var nodeList = $("#pagetree").bxtree("getSelected");
    if ((nodeList.length > 0 && nodeList[0].id == "0-#") || nodeList.length == 0) {
        alertDiv("提示", "请在左侧树中选择页面后，再进行该操作");
        return;
    }
    var selectArray = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if(selectArray.length == 0){
       alertDiv("提示","请至少勾选一条记录进行操作！")
       return;	
    }
    var arrayNode = nodeList[0].id.split("-");
    var paramJsonObj = new Object();
    paramJsonObj.detail = {};
    paramJsonObj.detail.resultRow = []; 
    for(var i=0;i<selectArray.length;i++){
        var status = $("#jqGrid").bxgrid('rawMethodCall',"saveRow",selectArray[i]);
        if(status == false){
            return;
        }
    	var saveData = $("#jqGrid").bxgrid('rawMethodCall',"getRowData",selectArray[i]);  
    	saveData.pageId = arrayNode[0];
    	saveData.pageEname = arrayNode[1];
    	paramJsonObj.detail.resultRow.push(saveData);
    }		
	var callback = {
		onSuccess : function(paramJsonObj) {
			var showMsgOpt = {
					showMsgId : "alertdiv",
					status : paramJsonObj.status,
					showMsg : paramJsonObj.returnMsg
				};
			$("#jqGrid").bxgrid("option","showMsgOpt",showMsgOpt);
			$("#jqGrid").bxgrid("query");
		}
	};
	AjaxCommunicator.ajaxRequest('/df/metamanage/buttonManage.do?method=insert','POST', paramJsonObj, callback);
}

function deleteRec() {
	var selectArray = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if(selectArray.length == 0){
       alertDiv("提示","请至少勾选一条记录进行操作！")
       return;	
    }
	var buttons = [ {
		html : "<i class='ace-icon fa fa-check bigger-110'></i>&nbsp; 是",
		"class" : "btn btn-skinColor btn-xs",
		click : function() {
			deleteOK();
			$(this).dialog("close");
		}
	}, {
		html : "<i class='ace-icon fa fa-times bigger-110'></i>&nbsp; 否",
		"class" : "btn btn-xs",
		click : function() {
			$(this).dialog("close");
		}
	} ];
	confirmDiv("确认删除", "数据删除后将不可恢复，一般情况下程序会自动删除相关资源以及页面的关联，如未删除，请自行手工删除，是否确定删除？", buttons);
}

function deleteOK() {
    var paramJsonObj = {};
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
    AjaxCommunicator.ajaxRequest('/df/metamanage/buttonManage.do?method=delete', 'POST', paramJsonObj, callback);
}

function updateRec() {
	    var nodeList = $("#pagetree").bxtree("getSelected");
	    if (nodeList.length > 0 && nodeList[0].id == "0-#") {
	        alertDiv("提示", "请在左侧树中选择页面后，再进行该操作");
	        return;
	    }
        var selectArray = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
	    if(selectArray.length == 0){
	       alertDiv("提示","请至少勾选一条记录进行操作！")
	       return;	
	    }
	    var arrayNode = nodeList[0].id.split("-");
		var paramJsonObj = new Object();
        paramJsonObj.detail = {};
        paramJsonObj.detail.resultRow = []; 
        for(var i=0;i<selectArray.length;i++){
            var status = $("#jqGrid").bxgrid('rawMethodCall',"saveRow",selectArray[i]);
            if(status == false){
                return;
            }
        	var saveData = $("#jqGrid").bxgrid('rawMethodCall',"getRowData",selectArray[i]);   
        	saveData.pageId = arrayNode[0];
        	saveData.pageEname = arrayNode[1];
        	paramJsonObj.detail.resultRow.push(saveData);
        }			
		var callback = {
			onSuccess : function(paramJsonObj) {
				var showMsgOpt = {
						showMsgId : "alertdiv",
						status : paramJsonObj.status,
						showMsg : paramJsonObj.returnMsg
					};
					
				$("#jqGrid").bxgrid("option","showMsgOpt",showMsgOpt);
				$("#jqGrid").bxgrid("query");				
			}
		};
		AjaxCommunicator.ajaxRequest('/df/metamanage/buttonManage.do?method=update','POST', paramJsonObj, callback);
}

function on_query_click() {
    var queryParam = new Object();
    $("#queryarea").bxdiv('setQueryFromDiv', queryParam, "inqu_status");
    $("#jqGrid").bxgrid("option", "queryParam", queryParam);
    $("#jqGrid").bxgrid("query");
}

function onKeyQuery() {
    var e = window.event || arguments.callee.caller.arguments[0];
    var keyCode = e.keyCode || e.which; // 按键的keyCode
    if (keyCode == 13) {
        on_query_click();
    }
}

function showTree() {
    var option = {
        'core': {
            'data': {
                'url': function (node) {
                    return httpServerPath + '/df/metamanage/buttonManage.do?method=querySubTree';
                },
                'data': function (node) {
                    var nodeId = node.id;
                    return {
                        'parentCode': nodeId
                    };
                }
            },
            "check_callback": true
        },
        "dnd": {
            is_draggable: true
        }
        ,
        plugins: ["state", "dnd", "wholerow"]
    };
    var treeOption = {
        dataPattern: "url",
        custom: true,
        sortorder: 'asc',
        option: option
    };
    var nodeState=false;//树节点是否被选中
    $("#pagetree").bxtree(treeOption)
        .on("select_node.jstree", function (event, obj) {
            clickNode(obj.node);
            on_query_click();
            nodeState=true;
        }).on('state_ready.jstree', function(e, data){
            if(!nodeState){
                var inst = data.instance;
                var obj = inst.get_node(e.target.firstChild.firstChild.lastChild);
                inst.select_node(obj);
            }
            nodeState=false;
        });
}

function clickNode(node) {
    var nodeId = node.id;
    var arrayNode = nodeId.split("-");
    if (arrayNode[1] == "#") {// 点击的是系统节点
        $("#inqu_status-pageName").val("");
        $("#inqu_status-pageName").attr("readonly", false);
    } else {// 点击的是项目节点
        $("#inqu_status-pageName").val(arrayNode[1]);
        $("#inqu_status-pageName").attr("readonly", true);
    }
}
