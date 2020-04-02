$(document).ready(
    function () {
        //1024下报警条显示
        var selectGroupId =  window.parent.$("#inpopup").attr("data-selectGroupId");
        var curHeight = document.documentElement.clientHeight;
        if (curHeight < 645) {
            $(".page-content").css("padding-top", "0px");
            $(".alertdiv").css("margin-bottom", "10px");
        }
        baosightRequire.requireFunct(['bxgrid','bxcombobox'], function () {
 
            var gridOption = {
                colNames: ['id','用户名', '显示名', '用户描述','电话','邮箱','工号'],
                colModel: [
                    {
                        name: 'userId',
                        index: 'userId',
                        width: 60,
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
                caption: false,
                onSelectRow: function(id){				    
			   		$("#jqGrid").bxgrid('rawMethodCall', 'editRow', id,true);
			  }
            };

            var option = {
                queryParam: {
                    inqu_status:{selectGroupId : selectGroupId}
                },
                dataPattern : "url",
                url: "/raising/backstage/privilege/roleUser/getUserSelectRows.do",
                gridOption: gridOption,
                navGridOption: {
                	add: false
                },
                showMsgOpt: {
                    showMsgId: "alertdiv"
                }
            };
            $("#jqGrid").bxgrid(option);                      
        });

    });

function on_query_click() {
    var selectGroupId =  window.parent.$("#inpopup").attr("data-selectGroupId");
    var showMsgOpt = {
        showMsgId: "alertdiv",
        status: null,
        showMsg: null
    };
    $("#jqGrid").bxgrid("option", "showMsgOpt", showMsgOpt);
    var queryParam = {"condition":true};
    queryParam.inqu_status = {
        "selectGroupId":selectGroupId,
        'username': $("#inqu_status-name").val(),
        'displayName': $("#inqu_status-display_name").val()
    };

    $("#jqGrid").bxgrid("option", "queryParam", queryParam);
    $("#jqGrid").bxgrid("query");
}

function confirmreturn(){
	var userArray = [];
	var selectId = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
	for(var i = 0;i < selectId.length; i++){		
		var selectData = $("#jqGrid").bxgrid('rawMethodCall', 'getRowData', selectId[i]);
        userArray.push(selectData.userId);
	}
    var confirmUser = parent.$("#inpopup")[0].contentWindow.confirmUser;// window.opener.confirmResource;
    confirmUser(userArray);
    var windowopen3 = parent.window.windowopen3 ;
    windowopen3('角色成员管理');
}


