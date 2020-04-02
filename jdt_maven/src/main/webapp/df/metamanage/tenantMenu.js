$(document).ready(function() {
    baosightRequire.requireFunct([ 'bxtree','bxgrid','bxdiv','bxdialog','bxalert', 'bxvalidate','bxcombobox'], function() {
        var gridOption = {
            primaryRowKey:"menuId",
            colNames : [ '菜单id','菜单名称','菜单代码','父菜单名称','父菜单代码','节点类型', '页面代码','页面打开方式', '图标类型', '图标内容','排序'],
            colModel : [
                {
                    name : 'menuId',
                    index : 'menuId',
                    width : 60,
                    forbidCopy : true,
                    hidden : true
                },{
                    name : 'dispName',
                    index : 'dispName',
                    editable : true,
                    sortable : true,
                    width : 90,
                    editrules:{
                        required : true,
                        custom:true,
                        custom_func:bxgrid_stringCheck
                    }
                },{
                    name : 'menuCode',
                    index : 'menuCode',
                    width : 90,
                    sortable : true,
                    editrules:{
                        required : true,
                        custom:true,
                        custom_func:bxgrid_englishCheck
                    }
                },{
                    name : 'parentName',
                    index : 'parentName',
                    sortable : false,
                    readOnly : true,
                    width : 80
                },{
                    name : 'parentCode',
                    index : 'parentCode',
                    sortable : false,
                    readOnly : true,
                    width : 80
                },{
                    name : 'linkType',
                    index : 'linkType',
                    width : 60,
                    editable : true,
                    sortable : false,
                    edittype: 'ccs',
                    editoptions:'bxmenu.linkType'
                },{
                    name : 'linkPath',
                    index : 'linkPath',
                    editable : true,
                    sortable : false,
                    width : 120
                },{
                    name : 'linkParam',
                    index : 'linkParam',
                    width : 80,
                    editable : true,
                    sortable : false,
                    edittype: 'ccs',
                    editoptions:'bxmenu.openType'
                }, {
                    name : 'iconType',
                    index : 'iconType',
                    width : 100,
                    editable : true,
                    sortable : false,
                    edittype: 'ccs',
                    editoptions:'bxmenu.iconType'
                },
                {
                    name : 'iconPath',
                    index : 'iconPath',
                    editable : true,
                    sortable : false,
                    width : 70
                }, {
                    name : 'menuOrder',
                    index : 'menuOrder',
                    editable : true,
                    sortable : true,
                    width : 40,
                    editrules:{
                        required : true,
                        number : true
                    }

                }],
            caption : false,
            sortorder : 'asc',
            jsonReader : {
                id : "menuId",
                repeatitems : false
            }
        };

        var option = {
            dataPattern: 'url',
            queryParam : {menuType:"leftMenu"},
            url : "/df/metamanage/tenantMenu.do?method=queryTenantMenu",
            showMsgOpt : {
                showMsgId : "alertdiv"
            },
            gridOption : gridOption,
            navGridOption: {
                download: true,
                downloadParam:{
                    downloadUrl : "/df/metamanage/tenantMenu.do/download.do"
                },
                upload: true,
                uploadParam:{
                    uploadUrl : "/df/metamanage/tenantMenu.do?method=insertTenantMenuForUpload",
                    uploadFixValueFunc : function(){
                        if(!isAvailable($("#menuType").bxcombobox("selectObj").val())){
                            alertDiv("提示","菜单类型不能为空！");
                            return undefined;
                        }
                        var uploadFixValue = {
                            menuType : $("#menuType").bxcombobox("selectObj").val()
                        }
                        return JSON.stringify(uploadFixValue);
                    }
                }
            }
        };
        $("#jqGrid").bxgrid(option);

        $("#menuType").bxcombobox({
            ccsId: "bxmenu.menuType",
            // 表示调用ccs服务时，需要查询的分类代码ID，返回为该分类代码对应的子项
            dataPattern: 'ccs',
            async: false,
            bind: [{
                "change": function(event, ui) {
                    var menutype = $("#menuType").bxcombobox("selectObj").val();
                    showTree(menutype);
                    $('#menutree').jstree().refresh();
                    on_query_click(0);
                }
            }]
        });
        $("#menuType").bxcombobox("selectObj").val("leftMenu");
        showTree("leftMenu");
    });
    $("#parentName").keypress(function(){
        onKeyQuery();
    });
    $("#inqu_status-menuCode").keypress(function(){
        onKeyQuery();
    });
    $("#inqu_status-dispName").keypress(function(){
        onKeyQuery();
    });
});

function refreshPage(){
    $("#jqGrid").bxgrid("refresh");
}

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
    var selectArray = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if(selectArray.length == 0){
        alertDiv("提示","请至少勾选一条记录进行操作！")
        return;
    }
    var menuType=$("#menuType").bxcombobox("selectObj").val();
    var paramJsonObj = new Object();
    paramJsonObj.detail = {};
    paramJsonObj.detail.resultRow = [];
    for(var i=0;i<selectArray.length;i++){
        var status = $("#jqGrid").bxgrid('rawMethodCall',"saveRow",selectArray[i]);
        if(status == false){
            return;
        }
        var saveData = $("#jqGrid").bxgrid('rawMethodCall',"getRowData",selectArray[i]);
        saveData.menuType = menuType;
        saveData.menuLevel = parseInt($("#menuLevel").val());
        saveData.parentCode = $("#inqu_status-parentCode").val();
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
            $('#menutree').jstree().refresh();
            //$("#jqGrid").bxgrid("query");
        }
    };
    AjaxCommunicator
            .ajaxRequest(
            '/df/metamanage/tenantMenu.do?method=insertTenantMenu',
            'POST', paramJsonObj, callback);
}

function deleteRec() {
    var selectArray = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if(selectArray.length == 0){
        alertDiv("提示","请至少勾选一条记录进行操作！")
        return;
    }
    var buttons = [
        {
            text: '确认',
            "class" : "btn btn-block btn-xs red",
            click: function() {
                deleteOK();
                $("#dialog-delect-message").bxdialog('close');
            }
        }
    ];
    var dialogOpt = {
        title :"<i class='ace-icon fa fa-exclamation-triangle red'></i>  确认删除",
        dataPattern: 'text',
        content : "删除菜单前请确认无相关子菜单，数据删除后将不可恢复，是否确定删除！",
        buttons : buttons
    };
    $("#dialog-delect-message").bxdialog(dialogOpt);
}

function deleteOK() {
    var paramJsonObj = new Object();

    $("#jqGrid").bxgrid('setInfoFromGrid', paramJsonObj, 'result');
    var callback = {
        onSuccess : function(paramJsonObj) {
            var showMsgOpt = {
                showMsgId : "alertdiv",
                status : paramJsonObj.status,
                showMsg : paramJsonObj.returnMsg
            };

            $("#jqGrid").bxgrid("option","showMsgOpt",showMsgOpt);
            $("#menutree").jstree().refresh();
            //$("#jqGrid").bxgrid("query");
            $("#dialog-message").dialog("close");
        }
    };
    AjaxCommunicator.ajaxRequest(
            '/df/metamanage/tenantMenu.do?method=deleteTenantMenu', 'POST',
            paramJsonObj, callback);

}

function updateRec() {
    var selectArray = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if(selectArray.length == 0){
        alertDiv("提示","请至少勾选一条记录进行操作！")
        return;
    }
    var menuType=$("#menuType").bxcombobox("selectObj").val();
    var paramJsonObj = new Object();
    paramJsonObj.detail = {};
    paramJsonObj.detail.resultRow = [];
    for(var i=0;i<selectArray.length;i++){
        var status = $("#jqGrid").bxgrid('rawMethodCall',"saveRow",selectArray[i]);
        if(status == false){
            return;
        }
        var saveData = $("#jqGrid").bxgrid('rawMethodCall',"getRowData",selectArray[i]);
        saveData.menuType = menuType;
        saveData.menuLevel = parseInt($("#menuLevel").val());
        saveData.parentCode = $("#inqu_status-parentCode").val();
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
            $('#menutree').jstree().refresh();
            //$("#jqGrid").bxgrid("query");
        }
    };
    AjaxCommunicator
            .ajaxRequest(
            '/df/metamanage/tenantMenu.do?method=updateTenantMenu',
            'POST', paramJsonObj, callback);
}

function showTree(menutype) {
    var option = {
        'core': {
            'data': {
                'url': function (node) {
                    return httpServerPath+'/df/metamanage/tenantMenu.do?method=querySubTree';
                },
                'data': function (node) {
                    var nodeId = node.id;
                    var nodeArray = nodeId.split("-");

                    if (nodeId == "#") {// 点击的是根节点
                        return {
                            'parentCode': '0',
                            'treeparentId': nodeId,
                            'menuType': menutype
                        };
                    } else {// 点击的是子节点
                        clickNode(node);
                        return {
                            'parentCode': nodeArray[0],
                            'treeparentId': nodeId,
                            'menuType': menutype
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
    $("#menutree").bxtree(treeOption)
            .on("select_node.jstree",function (event, obj) {
                clickNode(obj.node);
                $("#inqu_status-dispName").val("");
                $("#inqu_status-menuCode").val("");
                on_query_click(0);
                nodeState=true;
            }).on("move_node.jstree",function (e, data) {
                var menuCode = data.node.id.split("-")[0];
                var parentCode = data.parent.split("-")[0];
                if(parentCode=='0'){
                    var menuLevel = 0;
                }else{
                    var menuLevel = parseInt(data.parent.split("-")[1])+1;
                }
                var paramJsonObj = {
                    menuCode: menuCode,
                    parentCode: parentCode,
                    menuLevel:menuLevel
                };
                var callback = {
                    onSuccess: function (paramJsonObj) {
                        if (paramJsonObj.status != "0") {
                            alert(paramJsonObj.returnMsg);
                        }
                        $('#menutree').jstree().refresh();
                    }
                };
                AjaxCommunicator.ajaxRequest('/df/metamanage/tenantMenu.do?method=updateParent', 'POST', paramJsonObj, callback);
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
    var nodeId=node.id;
    var nodeArray = nodeId.split("-");
    if (nodeId == "#") {// 点击的是系统节点
        $("#inqu_status-parentCode").val("0");
    } else {// 点击的是项目节点
        $("#inqu_status-parentCode").val(nodeArray[0]);
    }
    if(nodeArray[1]=="#")
        $("#menuLevel").val(0);
    else
        $("#menuLevel").val(parseInt(nodeArray[1])+1);

    $("#parentName").val(node.text);
}

function on_query_click(type) {
    var queryParam = new Object();
    var dispName=$("#inqu_status-dispName").val();
    var menuCode=$("#inqu_status-menuCode").val();
    var parentName=$("#parentName").val();
    var menuType=$("#menuType").bxcombobox("selectObj").val();
    var parentCode=$("#inqu_status-parentCode").val();
    if(menuType==null){
        menuType="leftMenu";
    }
    if(type==0||parentName=="菜单展示"){
        if(parentName=="菜单展示"){
            parentCode="0";
        }
        queryParam={"parentCode":parentCode,"dispName":dispName,"menuType":menuType,"menuCode":menuCode};
    }else{
        queryParam={"parentName":parentName,"dispName":dispName,"menuType":menuType,"menuCode":menuCode};
    }

    $("#jqGrid").bxgrid("option", "queryParam",queryParam );
    $("#jqGrid").bxgrid("query");
}

function onKeyQuery(){
    var e = window.event||arguments.callee.caller.arguments[0];
    var keyCode  =  e.keyCode||e.which; // 按键的keyCode
    if ( keyCode == 13) {
        on_query_click(1);
    }
}

function publish(){
    var callback = {
        onSuccess : function(paramJsonObj) {
            if(paramJsonObj.status==0)
                alertDiv("提示","发布成功!");
            else
                alertDiv("提示",paramJsonObj.returnMsg);
        }
    };
    AjaxCommunicator.ajaxRequest('/df/metamanage/tenantMenu.do?method=clearMenuCache', 'POST', {}, callback);
}