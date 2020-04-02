$(document).ready(
		function() {			
			baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog',
					'bxalert', 'bxvalidate'], function() {
				initWidgets();
				var gridOption = {
					caption : '企业信息管理',
					colNames : ['', '单位编码', '单位名称', '单位描述', '所属区县'],
					colModel : [
					{
						name : 'companyId',
						index : 'companyId',
						width : 60,
						hidden : true
					}, {
						name : 'companyCode',
						index : 'companyCode',
						width : 60,
						editable : true
					}, {
						name : 'companyName',
						index : 'companyName',
						width : 90,
						editable : true
					}, {
						name : 'companyDesc',
						index : 'companyDesc',
						width : 90,
						sortable : false,
						editable : true
					}, {
						name : 'areaCode',
						index : 'areaCode',
						width : 90,
						editable : true
					}],
					sorttable : true,
					sortname : 'companyCode',
					sortorder : 'desc',
					jsonReader : {
						id : "companyId",
						repeatitems : false
					}
				};

				var option = {
					queryParam : {},
					dataPattern : "url",
					url : "/df/example/company.do?method=query",
					showMsgOpt : {
						showMsgId : "alertdiv"
					},
					gridOption : gridOption,
					navGridOption : {
						edit: true,
		                editicon: 'ace-icon fa fa-pencil blue',
		                editfunc: updateRec,
		                add: true,
		                addicon: 'ace-icon fa fa-plus-circle purple',
		                addfunc: insertRec,
		                del: true,
		                delicon: 'ace-icon fa fa-trash-o red',
		                delfunc: deleteRec,
                        search:false,
                        refresh:false						
					}
				};

				$("#companyContent").bxgrid(option);
			});		

			$(window).on('resize.jqGrid', function () {
				$("#table_companyContent").jqGrid( 'setGridWidth', $(".page-content").width() );
		    });
			
		});

function insertRec() {

	$("#detail").bxdiv("cleanPopDiv");
	var buttons = [
			{
				text : "保存",
				"class" : "btn btn-primary btn-xs",
				click : function() {
					if ($("#detail").bxvalidate("validate")) {
						var paramJsonObj = new Object();
						$("#detail").bxdiv('setInfoFromDiv', paramJsonObj,
								'detail');
						var callback = {
							onSuccess : function(paramJsonObj) {
								var showMsgOpt = {
										showMsgId : "alertdiv",
										status : paramJsonObj.status,
										showMsg : paramJsonObj.returnMsg
									};									
								$("#companyContent").bxgrid("option","showMsgOpt",showMsgOpt);	                            
	                            $("#companyContent").bxgrid("query");
	                            $("#detail").dialog("close");
							}
						};
						AjaxCommunicator
								.ajaxRequest(
										'/df/example/company.do?method=insert',
										'POST', paramJsonObj, callback);
						
					}
				}
			} ];

	var title = "新增记录";
	var dialogOpt = {
		title : title,
		buttons : buttons
	};
	$("#detail").bxdialog(dialogOpt);
}

function deleteRec() {
	dialogMessage("确认删除","数据删除后将不可恢复，是否确定删除");
}

function deleteOK() {
	var paramJsonObj = new Object();
	$("#companyContent").bxgrid('setInfoFromGrid', paramJsonObj, 'result');
	var callback = {
		onSuccess : function(paramJsonObj) {
			var showMsgOpt = {
					showMsgId : "alertdiv",
					status : paramJsonObj.status,
					showMsg : paramJsonObj.returnMsg
				};
				
			$("#companyContent").bxgrid("option","showMsgOpt",showMsgOpt);
			$("#companyContent").bxgrid("query");
			
		}
	};
	AjaxCommunicator.ajaxRequest(
			'/df/example/company.do?method=delete', 'POST',
			paramJsonObj, callback);
}

function updateRec() {
	
	var selectArray = $("#companyContent").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');	
	if(selectArray.length > 1){
		var buttons = [ 
			{
				text: '了解',
				"class" : "btn btn-primary btn-xs",
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

	$("#detail").bxdiv("cleanPopDiv");
	var paramJsonObj = new Object();
	var selectId = $("#companyContent").bxgrid('rawMethodCall', 'getGridParam', 'selrow');
	var selectData = $("#companyContent").bxgrid('rawMethodCall', 'getRowData', selectId);
	paramJsonObj = selectData;

	var title = '修改记录';
	var buttons = [			
			{
				text : "保存",
				"class" : "btn btn-primary btn-xs",
				click : function() {

					if ($("#detail").bxvalidate("validate")) {
						var paramJsonObj = new Object();
						$("#detail").bxdiv('setInfoFromDiv', paramJsonObj,
								'detail');
						var callback = {
							onSuccess : function(paramJsonObj) {
								var showMsgOpt = {
										showMsgId : "alertdiv",
										status : paramJsonObj.status,
										showMsg : paramJsonObj.returnMsg
									};
									
								$("#companyContent").bxgrid("option","showMsgOpt",showMsgOpt);
								$("#companyContent").bxgrid("refresh");
								$("#detail").dialog("close");
								
							}
						};
						AjaxCommunicator.ajaxRequest(
										'/df/example/company.do?method=update',
										'POST', paramJsonObj, callback);
						$(this).dialog("close");
					}
				}
			}];

	var dialogOpt = {
		title : title,
		buttons : buttons
	};

	var queryRecBack = {
		onSuccess : function(paramJsonObj) {
			$("#detail").bxdiv('fillPopDiv', paramJsonObj, 'detail');
			$("#detail").bxdialog(dialogOpt);
		}
	};

	AjaxCommunicator.ajaxRequest(
			'/df/example/company.do?method=queryOne', 'POST',
			paramJsonObj, queryRecBack);

}

function on_query_click() {
	var queryParam = new Object();
	$("#queryarea").bxdiv('setQueryFromDiv', queryParam, "inqu_status");
	$("#companyContent").bxgrid("option", "queryParam", queryParam);
	$("#companyContent").bxgrid("query");
}

function initWidgets() {
	$("#queryarea").bxdiv();
	$("#detail").bxdiv();
	$("#detail").bxvalidate();
	var ruleOptionCustom = {
		rules : {
			detailcompanyCode : {
				required : true,
				englishCheck : true
			},
			detailcompanyName : {
				required : true,
				stringCheck : true
			},
			detailareaCode : {
				required : true,
				englishCheck : true
			}
		}
	};
	$("#detail").bxvalidate("option", "ruleOptionCustom", ruleOptionCustom);
}

function dialogMessage(title, centext) {	
    $("#dialogInfo").html(centext);
	var dialog = $("#dialog-message")
			.removeClass('hide')
			.dialog(
					{
						modal : true,
						title : "<div class='widget-header'><h4 class='smaller'><i class='ace-icon fa fa-exclamation-triangle red'></i> "
								+ title + "</h4></div>",
						title_html : true,
						buttons : [ {
							text : "确认",
							"class" : "btn btn-primary btn-xs",
							click : function() {
								deleteOK();
								$(this).dialog("close");
							}
						} ]
					});
}