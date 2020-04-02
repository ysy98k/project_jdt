var vueInfo = new Vue({
    el:"#baba",
    data:{
        serviceList:[],
        selectService:""
    }
})

var setting = {
    check: {
        enable: true
    },
    data: {
        simpleData: {
            enable: true,
            idKey: "menuId",
            pIdKey: "parentCode"
        },
        key:{
            name:"dispName"
        }
    },
    view:{
        fontCss:{
            "height":"24px",
            "line-height":"24px"
        }
    }
};


$(document).ready(
    function () {
        //1024下报警条显示
        var groupNamesStr = window.parent.groupNames;
        var groupNameArr = groupNamesStr.split(",");
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
                    var arr= [];
                    if(groupNameArr.indexOf("admingroup") < 0){//如果不是管理员组。则只显示两条记录
                        var temp1 = {"serviceCode":"JDT","serviceName":"菜单资源"};
                        var temp2 = {"serviceCode":"PROP","serviceName":"项目资源"};
                        vueInfo.serviceList.push(temp1);
                        vueInfo.serviceList.push(temp2);
                    }else{
                        for(var i =0;i<data.length;i++){
                            var value = data[i].ccsId.split(".")[1];
                            var temp = {};
                            temp.serviceCode = data[i].ccsId.split(".")[1];
                            temp.serviceName = data[i].ccsStr;
                            vueInfo.serviceList.push(temp);
                        }
                    }
                }
            }
        })
        vueInfo.selectService = "PROP";
        showGrid();
        

    });

function serviceChange(){
    var service = vueInfo.selectService;
    if("JDT" == service){
        showTree();
    }else{
        showGrid();
    }
}
function showTreeInit(){
    window.parent.document.getElementById("inpopup2").style.width="470px";
    window.parent.document.getElementById("popupwindow").parentNode.style.width="500px";
    $("#searchButtonDiv").hide();
    $("#jqGrid").hide();
    $("#resourceNameDiv").hide();
    $("#zTreeDiv").show();
}

function showTree(){
    showTreeInit();
    $.ajax({
        url: toolkitPath + "/raising/backstage/privilege/roleResource/getResourceSelectRows.do",
        type:"post",
        data:{"ajaxParam":JSON.stringify({"type":"tree","service":"JDT"})},
        dataType:"json",
        success:function(data){
            if(data.status == "0"){
                var arr = data.dataList;
                $.fn.zTree.init($("#treeDemo"), setting, arr);
            }
        },
        error:function (data) {}
    })
}

function showGridInit(){
    window.parent.document.getElementById("inpopup2").style.width="870px";
    window.parent.document.getElementById("popupwindow").parentNode.style.width="900px";
    $("#searchButtonDiv").show();

    $("#zTreeDiv").hide();
    $("#jqGrid").show();
    $("#resourceNameDiv").show();
}

function showGrid(){
    showGridInit();
    var service = vueInfo.selectService;
    var selectGroup = parent.$("#inpopup")[0].contentWindow.document.getElementById("inqu_status-groupname").value;
    baosightRequire.requireFunct(['bxgrid', 'bxcombobox'], function () {
            var gridOption = {
                colNames: ['资源名', '资源显示名', '资源描述', '所属服务'],
                colModel: [{
                    name: 'name',
                    index: 'name',
                    label: "123",
                    width: 65
                }, {
                    name: 'display_name',
                    index: 'display_name',
                    width: 65
                }, {
                    name: 'description',
                    index: 'description',
                    sortable: false,
                    width: 65
                }, {
                    name: 'service',
                    index: 'service',
                    width: 60,
                    /*edittype: 'ccs',
                    editoptions: 'service',*/
                    sortable: false
                }],
                sortable: true,
                onCellSelect: function (rowid, iCol, cellcontent, e) {
                    var curCheckbox = $(this).find('#' + rowid + ' input[type=checkbox]');
                    if (isAvailable(curCheckbox) && curCheckbox.length > 0) {
                        if ($(e.target).is('input[type=checkbox]')) {
                            if ($(curCheckbox[0]).prop("checked") == false) {
                                $(this).jqGrid("saveRow", rowid);
                            } else {
                                $(this).editRow(rowid, true);
                            }
                        } else {
                            if ($(curCheckbox[0]).prop("checked")) {
                                $(this).editRow(rowid, true);
                                $(curCheckbox[0]).prop("checked", true);
                            }
                            $(this).jqGrid("setSelection", rowid, false);
                        }
                    }
                },
                sortname: 'name',
                sortorder: 'asc',
                caption: false
            };

            var option = {
                queryParam: {
                    'name': $("#inqu_status-name").val(),
                    'service': service,
                    "selectGroup":selectGroup
                },
                dataPattern: "url",
                url: "/raising/backstage/privilege/roleResource/getResourceSelectRows.do",
                gridOption: gridOption,
                navGridOption: {
                    add: false
                },
                showMsgOpt: {
                    showMsgId: "alertdiv"
                }
            };

            $("#jqGrid").bxgrid(option);

            // 页面授权管理增加全选功能
            $("#jqGrid").on("change", "td>input[type='checkbox']:not(.cbox,.th-checkbox)", function () {
                var rowid = $(this).closest("tr").attr("id");
                var rData = $("#jqGrid").bxgrid('rawMethodCall', 'getRowData', rowid);

                if ($(this).is(".row-selall>input")) {
                    // 全选框
                    rData.GET = rData.POST = rData.PUT = rData.DELETE = rData.selall;
                    $("#jqGrid").bxgrid('rawMethodCallMore', 'setRowData', rowid, rData);
                } else{
                    // 单个选择
                    var selall = (rData.GET == "true" && rData.POST == "true"
                    && rData.PUT == "true" && rData.DELETE == "true") ? "true" : "false";
                    if (rData.selall != selall) {
                        rData.selall = selall;
                        $("#jqGrid").bxgrid('rawMethodCallMore', 'setRowData', rowid, rData);
                    }
                }

            });

            $("#cb_table_jqGrid").click(function () {
                var selectArray = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
                if ($(this).prop("checked") == true) {
                    for (var i = 0; i < selectArray.length; i++) {
                        $("#jqGrid").bxgrid("rawMethodCall", "editRow", selectArray[i], true);
                    }
                } else {
                    for (var i = 0; i < selectArray.length; i++) {
                        $("#jqGrid").bxgrid("rawMethodCall", "saveRow", selectArray[i]);
                    }
                }
            });

            // 表头复选框单击事件： 不能绑定在表上，可能事件被拦截了
            $(".th-checkbox").click(function (e) {
                e.stopPropagation(); //阻止事件冒泡，important!
                var colID = $(this).attr("id");
                var isChecked = this.checked;
                $("." + colID+">input[type='checkbox']").each(function () {
                    if (this.checked != isChecked) this.click();
                });
            });


        });
}

function on_query_click() {
    var service = vueInfo.selectService;
    var selectGroup = parent.$("#inpopup")[0].contentWindow.document.getElementById("inqu_status-groupname").value;
    var queryParam = {
        'name': $("#inqu_status-name").val(),
        'service': service,
        "selectGroup":selectGroup
    };
    baosightRequire.requireFunct(['bxgrid', 'bxcombobox'], function () {
        $("#jqGrid").bxgrid("option", "queryParam", queryParam);
        $("#jqGrid").bxgrid("query");
    })
}

function confirmreturn() {
    var resourceArray = [];
    var  service = vueInfo.selectService;
    if("JDT" == service){
        var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
        var nodes = treeObj.getCheckedNodes(true);
        for(var i=0;i<nodes.length;i++){
            var resourceObj = {};
            /*if(nodes[i].isParent && nodes[i].linkPath != "tbmManage"
            && nodes[i].linkPath != "frame" && nodes[i].linkPath != "sectionInfo"){//如果是目录，则跳过
               continue;
            }*/
            if(nodes[i].isParent && nodes[i].level != 0){//如果是目录,且不是上册菜单目录,则跳过
                continue;
            }
            resourceObj.name = nodes[i].linkPath;
            resourceObj.service = "JDT";
            var action = {};
            action.GET = "true";
            action.POST = "true";
            action.PUT = "true";
            action.DELETE = "true";
            resourceObj.action = action;
            resourceArray.push(resourceObj);
        }
    }else{
        var selectId = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
        for (var i = 0; i < selectId.length; i++) {
            var selectData = $("#jqGrid").bxgrid('rawMethodCall', 'getRowData', selectId[i]);

            var resourceObj = {};
            resourceObj.name = selectData.name;
            resourceObj.service = selectData.service;
            var action = {};
            action.GET = "true";
            action.POST = "true";
            action.PUT = "true";
            action.DELETE = "true";
            resourceObj.action = action;
            resourceArray.push(resourceObj);
        }
    }
    var confirmResource = parent.$("#inpopup")[0].contentWindow.confirmResource;// window.opener.confirmResource;

    confirmResource(resourceArray);

    var windowopen3 = parent.window.windowopen3;
    windowopen3('角色授权管理');

}


