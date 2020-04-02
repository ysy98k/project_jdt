var zTreeNodes;
var setting = {
    view: {
        showLine: false,
        showIcon: false,
        addDiyDom: addDiyDom
    },
    data: {
        key:{
            name:"groupName"
        },
        simpleData: {
            enable: true,
            idKey: "groupId",
            pIdKey: "parentId"
        }
    }
};

$(function () {
    //初始化数据
    init();
    //初始化一级节点展开
    var treeNode = $.fn.zTree.getZTreeObj("dataTree");
    var a1 = 1 ;
})

/**
 * 自定义DOM节点
 */
function addDiyDom(treeId, treeNode) {
    var spaceWidth = 15;
    var liObj = $("#" + treeNode.tId);
    var aObj = $("#" + treeNode.tId + "_a");
    liObj.attr("data-groupId",treeNode.groupId);
    var switchObj = $("#" + treeNode.tId + "_switch");
    var icoObj = $("#" + treeNode.tId + "_ico");
    var spanObj = $("#" + treeNode.tId + "_span");
    aObj.attr('title', '');
    aObj.append('<div class="diy swich"></div>');
    var div = $(liObj).find('div').eq(0);
    switchObj.remove();
    spanObj.remove();
    icoObj.remove();
    div.append(switchObj);
    div.append(spanObj);
    var spaceStr = "<span style='height:1px;display: inline-block;width:" + (spaceWidth * treeNode.level) + "px'></span>";
    switchObj.before(spaceStr);

    var editStr = '';
    editStr += '<div class="diy">' + (treeNode.displayName == null ? ' ' : treeNode.displayName) + '</div>';
    editStr += '<div class="diy">' + (treeNode.groupDescription == null ? ' ' : treeNode.groupDescription ) + '</div>';
    editStr += '<div class="diy operator">' + formatHandle(treeNode) + '</div>';
    aObj.append(editStr);
}
/**
 * 查询数据
 */
function query(data) {
    var rows = data;
    var parentArr = [];
    var groupNamesArr = [];//当前用户所在组集合
    var parentId = null;
    var parentGroupName = null;
    //获得当前登录用户的角色集合
    var groupNamesStr = window.groupNames;

    if(groupNamesStr.indexOf(",") > 0){
        groupNamesArr = groupNamesStr.split(",");
    }else{
        groupNamesArr.push(groupNamesStr);
    }
    if(groupNamesArr.indexOf("admingroup") >= 0){
        parentId = rows[0].groupId == 1? rows[1].groupId:rows[0].groupId;//当前用户所在组的id设为parentId。
        parentGroupName = rows[0].groupId == 1? rows[1].groupName:rows[0].groupName;//当前用户所在组的id设为parentId。
        $("#dataTree").attr("data-parentId",parentId);
        $("#dataTree").attr("data-parentGroupName",parentGroupName);

    }else{
        //过滤用户角色集合
        for(var i=0;i<rows.length;i++){
            //如果是admingroup组的人。不过滤。
            if(groupNamesArr.indexOf(rows[i].groupName) >= 0 ){
                var temp = {};
                temp.groupName = rows[i].groupName;
                temp.groupId = rows[i].groupId;
                parentArr.push(temp);
                rows.splice(i, 1);
                i --;
            }
        }
        var parentIdsArr = [];
        var parentGroupNamesArr = [];
        for(var i=0;i<parentArr.length;i++){
            parentIdsArr.push(parentArr[i].groupId);
            parentGroupNamesArr.push(parentArr[i].groupName);
        }
        $("#dataTree").attr("data-parentId",parentIdsArr.join(","));
        $("#dataTree").attr("data-parentGroupName",parentGroupNamesArr.join(","));
    }



    zTreeNodes = rows;
    //初始化树
    var treeObj = $.fn.zTree.init($("#dataTree"), setting, zTreeNodes);
    var nodeList = treeObj.getNodes();//一节节点List
    for(var i = 0; i < nodeList.length; i++) { //设置节点展开第二级节点
        treeObj.expandNode(nodeList[i], true, false, true);
    }

    //添加表头
    var li_head = ' <li class="head">' +
                        '<a>' +
                            '<div class="diy">角色名</div>' +
                            '<div class="diy">角色显示名</div>' +
                            '<div class="diy">角色描述</div>' +
                            '<div class="diy">操作</div>' +
                        '</a>' +
                '</li>';
    var rows = $("#dataTree").find('li');
    if (rows.length > 0) {
        rows.eq(0).before(li_head)
    } else {
        $("#dataTree").append(li_head);
        $("#dataTree").append('<li ><div style="text-align: center;line-height: 30px;" >无符合条件数据</div></li>')
    }
}

/**
 * 根据权限展示功能按钮
 * @param treeNode
 * @returns {string}
 */
function formatHandle(treeNode) {
    var htmlStr = '';
    htmlStr += '<div class="icon_div bigWidth"><a class="operatorButton eighty  layui-btn layui-btn-primary layui-btn-xs" lay-event="edit" title="角色成员管理" href="javascript:editUser(\'' + treeNode.tId + '\')">角色成员管理</a></div>';
    htmlStr += '<div class="icon_div bigWidth"><a class="operatorButton eighty layui-btn layui-btn-primary layui-btn-xs"  lay-event="edit"  title="角色授权管理" href="javascript:editResource(\'' + treeNode.tId + '\')">角色授权管理</a></div>';

    /*htmlStr += '<div class="icon_div bigWidth"><a class="operatorButton eighty layui-btn layui-btn-primary layui-btn-xs" lay-event="edit"  title="注册默认资源" href="javascript:register(\'' + treeNode.tId + '\')">注册默认资源</a></div>';*/
    htmlStr += '<div class="icon_div"><a class="operatorButton layui-btn layui-btn-primary layui-btn-xs" style="background-color: #87B87F" lay-event="edit" title="添加" href="javascript:addSector(\'' + treeNode.tId + '\')">添加</a></div>';
    htmlStr += '<div class="icon_div"><a class="operatorButton layui-btn layui-btn-primary layui-btn-xs" style="background-color: #2184C1" lay-event="edit"  title="修改" href="javascript:update(\'' + treeNode.tId + '\')">修改</a></div>';
    htmlStr += '<div class="icon_div"><a class="operatorButton layui-btn layui-btn-primary layui-btn-xs" style="background-color: #D15B47" lay-event="del" title="删除" href="javascript:del(\'' + treeNode.tId + '\')">删除</a></div>';

    return htmlStr;
}

function init(){
    $.ajax({
        "url":toolkitPath+ "/raising/backstage/privilege/roleManagement/getRows.do",
        "type":"post",
        "dataType":"json",
        success:function (data) {
            if(data.status == "0"){
                //过滤用户所在的角色记录，并将所在角色记录的groupId,groupName保存在data-parentId，parentGroupName中
                var rows = data.rows;
                query(rows);
            }
        },
        error:function () {
        }
    })
}

function queryWithCondition(){
    var queryParam = {
        'groupNameLike': $("#inqu_status-name").val() == "" ? null : $("#inqu_status-name").val(),
        'displayName': $("#inqu_status-display_name").val() == "" ? null : $("#inqu_status-display_name").val()
    };
    var str = "";
    if(!isNullOrEmptyOrUndefiend(queryParam.groupNameLike) || !isNullOrEmptyOrUndefiend(queryParam.displayName)){
        str = JSON.stringify(queryParam);
    }
    $.ajax({
        "url":toolkitPath+ "/raising/backstage/privilege/roleManagement/getRows.do",
        "type":"post",
        "data":{"ajaxParam":str},
        "dataType":"json",
        success:function (data) {
            if(data.status == "0"){
                var rows = data.rows;
                query(rows);
            }
        },
        error:function () {
        }
    })
}

/**
 * 新增按钮
 */
function add(){
    if($("#insert").length>0){
        baosightRequire.requireFunct(['bxdialog'],function(){
            alertDiv("提示","一次只能插入一条记录！");
        })
        return;
    }
    var spaceWidth = 15;
    var htmlStr = "<li id='insert' class='level0' tabindex='0' hidefocus='true' treenode style='background-color: rgb(237, 237, 237)'>" +
                    "<a id='insert_1_a' class='level0' treenode_a onclick='' target='_blank' style='' title=''>" +
                        "<div class='diy swich'>" +
                            "<span style='height:1px;display: inline-block;width:0px'></span>" +
                            "<span id='insert_1_switch' title='' class='button level0 switch noline_docu' treenode_switch=''></span>" +
                            "<span id='insert_1_span' class='node_name'><input id='insert_groupName' style='height=19px;line-height: 21px;' /></span>" +
                        "</div>"+
                        "<div class='diy'><input id='insert_displayName' style='height=19px;line-height: 21px;' /></div>" +
                        "<div class='diy'><input id='insert_groupDescription' style='height=19px;line-height: 21px;width: 80%;' /></div>"+
                        "<div class='diy'></div>"+
                    "</a>"
                +"</li>"
    $("li[class='head']").after(htmlStr);
}

/**
 * 保存按钮
 */
function saveRole(){
    var group = {};
    group.name = $("#insert_groupName").val();
    group.display_name = $("#insert_displayName").val();
    group.description = $("#insert_groupDescription").val();
    group.parentId = $("#dataTree").attr("data-parentId").split(",")[0];

    if(isNullOrEmptyOrUndefiend(group.name) || isNullOrEmptyOrUndefiend(group.display_name) ){
        baosightRequire.requireFunct(['bxdiv','bxdialog','bxalert'], function () {
            alertDiv("提示","角色名和角色显示名不可以为空");
        })

        return;
    }
    if(group.name.indexOf("_default") >=0 &&  (group.name.length - group.name.lastIndexOf("_default") == 8 )){
        baosightRequire.requireFunct(['bxdiv','bxdialog'], function () {
            alertDiv("提示","角色名不可以用_default结尾。");
        })
        return;
    }

    $.ajax({
        "url":toolkitPath+ "/raising/backstage/privilege/roleManagement/addOrUpdateRows.do",
        "type":"post",
        "data":{"ajaxParam":JSON.stringify(group)},
        "dataType":"json",
        success:function (data) {
            if(data.errcode == "0"){
                $("#alertdiv").removeClass("alert-danger");
                $("#alertdiv").addClass("alertdiv alert-info alert auto-hide width-100");
            }else{
                $("#alertdiv").addClass("alertdiv alert-danger alert auto-hide width-100");
            }
            $("#alertdiv").attr("title",data.message);
            $("#alertdiv").html(data.message);
            var rows = data.rows;
            query(rows);
        },
        error:function () {
        }
    })
}

/**
 * 给记录添加子角色
 * @param id
 */
function addSector(id){
    var parentId = $("#"+id).attr("data-groupId");
    $("#form_groupName").attr('readonly',false);
    //初始化
    baosightRequire.requireFunct(['bxdiv', 'bxdialog'], function() {
        var button = [{
            text: "完成",
            "class" : "btn btn-skinColor btn-xs",
            click : function () {
                var checkResult = saveOrUpdate(parentId,true);
                if("false" != checkResult){
                    $(this).bxdialog('close');
                }

            }
        }]
        var title = "新增角色信息";
        var dialogOpt = {
            title: title,
            buttons:button,
            width: '400px'
        };
        $("#addForm")[0].reset();
        //显示对话框
        $("#addForm").bxdialog(dialogOpt);
    })

}

function update(id){
    var  treeObj  = $.fn.zTree.getZTreeObj("dataTree");
    var treeNode = treeObj.getNodeByTId(id);
    $("#form_groupName").val(treeNode.groupName);
    $("#form_displayName").val(treeNode.displayName);
    $("#form_groupDescription").val(treeNode.groupDescription);
    $("#form_groupName").attr('readonly',true);
    //初始化
    baosightRequire.requireFunct(['bxdiv', 'bxdialog'], function() {
        var button = [{
            text: "完成",
            "class" : "btn btn-skinColor btn-xs",
            click : function () {
                saveOrUpdate(treeNode.groupId,false);
                $(this).bxdialog('close');
            }
        }]
        var title = "新增角色信息";
        var dialogOpt = {
            title: title,
            buttons:button,
            width: '400px'
        };
        //显示对话框
        $("#addForm").bxdialog(dialogOpt);
    })
}

/**
 *
 * @param rowId
 */
function saveOrUpdate(parentId,save){
    //数据校验
    var group = {};
    group.name = $("#form_groupName").val();
    group.display_name = $("#form_displayName").val();
    group.description = $("#form_groupDescription").val();
    if(save){
        group.parentId = parentId;
    }
    if(isNullOrEmptyOrUndefiend(group.name) || isNullOrEmptyOrUndefiend(group.display_name) || isNullOrEmptyOrUndefiend(group.description) ){
        alertDiv("提示","红星标记项不可以为空");
        return "false";
    }
    if(save && group.name.indexOf("_default") >=0 && (group.name.length - group.name.lastIndexOf("_default") == 8 )){
        alertDiv("提示","角色名不可以用_default结尾。");
        return "false";
    }
    if(!save){
        group.groupId =parentId;//
        delete group.groupName;
    }
    $.ajax({
        "url":toolkitPath+ "/raising/backstage/privilege/roleManagement/addOrUpdateRows.do",
        "type":"post",
        "data":{"ajaxParam":JSON.stringify(group)},
        "dataType":"json",
        success:function (data) {
            if(data.errcode == "0"){
                $("#alertdiv").removeClass("alert-danger");
                $("#alertdiv").addClass("alertdiv alert-info alert auto-hide width-100");
            }else{
                $("#alertdiv").addClass("alertdiv alert-danger alert auto-hide width-100");
            }
            $("#alertdiv").attr("title",data.message);
            $("#alertdiv").html(data.message);
            var rows = data.rows;
            query(rows);
        },
        error:function () {
        },
        complete:function(){
            $("#addForm")[0].reset();//清空form表单
        }
    })


}


function del(id){
    baosightRequire.requireFunct([ 'bxdiv', 'bxdialog'], function() {
        var buttons = [{
            html: "<i class='ace-icon fa fa-check bigger-110'></i>&nbsp; 是",
            "class": "btn btn-skinColor btn-xs",
            click: function () {
                deleteOK(id);
                $(this).dialog("close");
            }
        }, {
            html: "<i class='ace-icon fa fa-times bigger-110'></i>&nbsp; 否",
            "class": "btn btn-xs",
            click: function () {
                $(this).dialog("close");
            }
        }];
        confirmDiv("确认删除", "数据删除后将不可恢复，同时删除角色关联权限，当存在用户关联此角色或者存在用户关联此角色的后代角色时，不可删除，是否确定删除？", buttons);
    })

}

function deleteOK(id) {
    var groupId = $("#"+id).attr("data-groupId");
    var  treeObj  = $.fn.zTree.getZTreeObj("dataTree");
    var treeNode = treeObj.getNodeByTId(id);

    var groupIds = [];
    var groupNames = [];
    groupIds = getAllChildrenNodes(treeNode,groupIds,"groupId");
    groupNames = getAllChildrenNodes(treeNode,groupNames,"groupName");
    $.ajax({
        "url":toolkitPath+ "/raising/backstage/privilege/roleManagement/deleteRows.do",
        "type":"post",
        "data":{"ajaxParam":JSON.stringify({"groupIds":groupIds, "groupNames":groupNames})},
        "dataType":"json",
        success:function (data) {
            if(data.status == "0"){
                $("#alertdiv").removeClass("alert-danger");
                $("#alertdiv").addClass("alertdiv alert-info alert auto-hide width-100");
            }else{
                $("#alertdiv").addClass("alertdiv alert-danger alert auto-hide width-100");
            }
            $("#alertdiv").attr("title",data.message);
            $("#alertdiv").html(data.message);
            var rows = data.rows;
            query(rows);
        },
        error:function () {
        }
    })
}

/**
 * 注册默认资源
 * @param tId
 */
function  register(tId){
    var  treeObj  = $.fn.zTree.getZTreeObj("dataTree");
    var treeNode = treeObj.getNodeByTId(tId);
    var groupName = treeNode.groupName;
    paramJsonObj = {"groupName":groupName};
    var callback = {
        onSuccess : function(paramJsonObj) {
            if(paramJsonObj.status == "0"){
                $("#alertdiv").removeClass("alert-danger");
                $("#alertdiv").addClass("alertdiv alert-info alert auto-hide width-100");
            }else{
                $("#alertdiv").addClass("alertdiv alert-danger alert auto-hide width-100");
            }
            $("#alertdiv").attr("title",paramJsonObj.message);
            $("#alertdiv").html(paramJsonObj.message);

        }
    };
    AjaxCommunicator.ajaxRequest( '/raising/backstage/privilege/roleManagement/register.do','POST', paramJsonObj, callback,true);

}

function getAllChildrenNodes(treeNode,result,field){
    if("groupName" == field){
        result.push(treeNode.groupName);
    }else if("groupId" == field){
        result.push(treeNode.groupId);
    }
    if (treeNode.isParent) {
        var childrenNodes = treeNode.children;
        if (childrenNodes) {
            for (var i = 0; i < childrenNodes.length; i++) {
                if(childrenNodes[i].isParent){
                    result = getAllChildrenNodes(childrenNodes[i], result);
                }else{
                    if("groupName" == field){
                        result.push(childrenNodes[i].groupName);
                    }else if("groupId" == field){
                        result.push(childrenNodes[i].groupId);
                    }
                    //result.push(childrenNodes[i].groupName);
                }
            }
        }
    }
    return result;
}



function editUser(tId) {
    windowopen(toolkitPath + "/raising/backstage/privilege/roleUser/getPage.do", '角色成员管理 ',tId);
}



function editResource(tId) {
    var  treeObj  = $.fn.zTree.getZTreeObj("dataTree");
    var treeNode = treeObj.getNodeByTId(tId);
    var groupName = treeNode.groupName;
    if(groupName=='undefined') {
        alertDiv("提示", "群组不存在，请确认所选群组是否添加成功！");
    }else {
        windowopen(toolkitPath + "/raising/backstage/privilege/roleResource/getPage.do?&groupname=" + groupName, '角色授权管理',tId);
    }
}

function windowopen(url, name,tId) {
    var  treeObj  = $.fn.zTree.getZTreeObj("dataTree");
    var treeNode = treeObj.getNodeByTId(tId);
    var groupId = treeNode.groupId;
    var groupName = treeNode.groupName;

    $("#inpopup2").hide();
    $("#inpopup").show();

    $("#inpopup").attr('src', url);
    $("#inpopup").attr('data-selectGroupId', groupId);
    $("#inpopup").attr('data-selectGroupName', groupName);

    baosightRequire.requireFunct(['bxdiv', 'bxdialog'], function() {
        var dialog = $("#popupwindow")
            .removeClass('hide')
            .dialog(
                {
                    close: function (event, ui) {
                        onpopupclose();
                    },
                    width: 900,
                    height: 640,
                    modal: true,
                    title: "<div class='widget-header'><h4 class='smaller'><i class='ace-icon fa fa-exclamation-triangle red'></i> "
                    + name + "</h4></div>",
                    title_html: true,
                    buttons: []
                });
    })
}

function windowopen2(url, name) {
    document.getElementById("inpopup2").style.width="870px";
    document.getElementById("popupwindow").parentNode.style.width="900px";
    $("#inpopup").hide();
    $("#inpopup2").attr('src', url);
    $("#inpopup2").show();
    baosightRequire.requireFunct(['bxdiv', 'bxdialog'], function() {
        var dialog = $("#popupwindow").dialog(
                {
                    close: function (event, ui) {
                        onpopupclose();
                    },
                    title: "<div class='widget-header'><h4 class='smaller'><i class='ace-icon fa fa-exclamation-triangle red'></i> "
                    + name + "</h4></div>"
                });
    })

}

function windowopen3(name) {
    document.getElementById("inpopup2").style.width="870px";
    document.getElementById("popupwindow").parentNode.style.width="900px";
    $("#inpopup2").hide();
    $("#inpopup").show();
    baosightRequire.requireFunct(['bxdiv', 'bxdialog'], function() {
        var dialog = $("#popupwindow")
            .dialog(
                {
                    close: function (event, ui) {
                        onpopupclose();
                    },
                    title: "<div class='widget-header'><h4 class='smaller'><i class='ace-icon fa fa-exclamation-triangle red'></i> "
                    + name + "</h4></div>"
                });
    })
}


function on_query_click() {

    var showMsgOpt = {
        showMsgId: "alertdiv",
        status: null,
        showMsg: null
    };

    $("#jqGrid").bxgrid("option", "showMsgOpt", showMsgOpt);
    var queryParam = {
        'name': $("#inqu_status-name").val(),
        'display_name': $("#inqu_status-display_name").val()
    };
    $("#jqGrid").bxgrid("option", "queryParam", queryParam);
    $("#jqGrid").bxgrid("query");
}

function dialogMessage(title, centext) {
    $("#dialogInfo").html(centext);
    var dialog = $("#dialog-message")
        .removeClass('hide')
        .dialog(
            {
                modal: true,
                title: "<div class='widget-header'><h4 class='smaller'><i class='ace-icon fa fa-exclamation-triangle red'></i> "
                + title + "</h4></div>",
                title_html: true,
                buttons: [{
                    text: "确认",
                    "class": "btn btn-skinColor btn-xs",
                    click: function () {
                        deleteOK();
                        $(this).dialog("close");
                    }
                }]
            });
}

function showDialogMessage(title, centext) {
    $("#dialogInfo").html(centext);
    var dialog = $("#dialog-message")
        .removeClass('hide')
        .dialog(
            {
                modal: true,
                title: "<div class='widget-header'><h4 class='smaller'><i class='ace-icon fa fa-exclamation-triangle red'></i> "
                + title + "</h4></div>",
                title_html: true,
                buttons: [{
                    text: "确认",
                    "class": "btn btn-skinColor btn-xs",
                    click: function () {

                        $(this).dialog("close");
                    }
                }]
            });
}

function onpopupclose() {
    $("#inpopup").attr('src', '');
    $("#inpopup2").attr('src', '');
}



