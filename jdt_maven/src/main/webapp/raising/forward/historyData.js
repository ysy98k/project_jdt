//树的js
var dataObj;

$(document).ready(function () {
    initDomHeight();

    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxcombobox', 'bxtree', 'bxcomboboxtree'], function () {
        showTree();

        /*showPayStatus();*/
        $("#queryarea").bxdiv();
        var gridOption = {
            colNames: ['变量名称', '单位', '点名', '数据质量', '数据值', '更新时间'],
            colModel: [
                {
                    name: 'name',
                    index: 'name',
                    width: 60
                },  {
                    name: 'desc',
                    index: 'desc',
                    width: 60
                },{
                    name: 'tag',
                    index: 'tag',
                    width: 60,
                    editable: true
                }, {
                    name: 'quality',
                    index: 'quality',
                    width: 90
                }, {
                    name: 'tgvalue',
                    index: 'tgvalue',
                    width: 90
                }, {
                    name: 'timestamp',
                    index: 'timestamp',
                    width: 90
                }],
            /*data: global_grid_data,*/
            sortable: true,
            sortname: 'id',
            sortorder: 'desc',
            caption: "历史数据",
            multiselect: false,
            onSelectRow: function (id) {
            },
            onCellSelect: function (rowid, iCol, cellcontent, e) {
                var buttons = [
                    {
                        text: '确认',
                        "class": "btn btn-primary btn-xs red",
                        click: function () {
                            $("#dialog-edit").bxdialog('close');
                        }
                    }
                ];
                var dialogOpt = {
                    title: "<i class='ace-icon fa fa-exclamation-triangle red'></i>  单元格点击事件",
                    dataPattern: 'text',
                    content: "单元格点击事件<br/>当前行号：" + rowid + "<br/>当前列索引：" + iCol + "<br/>单元格内容：" + cellcontent,
                    buttons: buttons
                };
                $("#dialog-edit").bxdialog(dialogOpt);
            }
        };

        var option = {
            queryParam: {},
            dataPattern: "url",
            url: "/raising/forward/historyData.do?method=query",
            showMsgOpt: {
                showMsgId: "alertdiv"
            },
            gridOption: gridOption
        };
        $("#grid").bxgrid(option);
        $("#detail").bxdiv();

        /*************************************设置pds数据源********************************************/
        var now = new Date();
        var grid_data = new Array();
        var callback = {
            onSuccess: function (gridData) {
                if (gridData.status == 0) {
                    var A050100 = gridData.dataValue[0].data;
                    var A050200 = gridData.dataValue[1].data;
                    var A050300 = gridData.dataValue[2].data;
                    var time = gridData.time;
                    for (var i = 0; i < time.length; i++) {
                        var oneGridData = {};
                        var oneTime = time[i];
                        oneGridData.time = oneTime;
                        oneGridData.A050100 = A050100[i];
                        oneGridData.A050200 = A050200[i];
                        oneGridData.A050300 = A050300[i];
                        grid_data.push(oneGridData);
                    }
                }
            }
        };


        /*$("#gridImage").bxgrid(imageOption);*/
    });


});

function initDomHeight(){
    var fatherHeight = $("#father").height();
    var headHeight = $("#header").outerHeight(true);
    var contentHeight = fatherHeight - headHeight;
    $("#treeDiv").height(contentHeight);
    $("#treeDiv2").height(contentHeight-10);
}


function showTree() {

    var handleCheckFunct = function (option) {
        var children = option.core.data.children;
        if (children != undefined && children != null && children.length > 0) {
            var oneChild = children[0];
            oneChild.state.checked = true;
        }
    };

    var option = {
        checkbox: {
            keep_selected_style: false,
            tie_selection: false,
            three_state: false,
            cascade: 'undetermined'
        },
        "plugins": ["themes", "wholerow"]
    };

    $("#bxtree3").bxtree({
        //ccsId: "provar."+$.cookie("selected_collection"),
        type:"post",
        ccsId: "provar.RBXS0018",
        showText: '变量列表',
        option: option,
        dataPattern: 'ccs',
        preHandlerFunct: handleCheckFunct
    });



}


function clk() {
    var dataArray = $("#grid").bxgrid("rawMethodCall", "getGridParam", "data");
    var selectArray = $("#grid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if (selectArray.length == 0) {
        alertDiv("提示", "请至少勾选一条记录进行操作！")
        return;
    }
    for (var i = 0; i < selectArray.length; i++) {
        var status = $("#grid").bxgrid('rawMethodCall', "saveRow", selectArray[i]);
        if (status == false) {
            return;
        }
        var saveData = $("#grid").bxgrid('rawMethodCall', "getRowData", selectArray[i]);
        dataArray.push(saveData);
    }
    $("#grid").bxgrid("refreshLocalData", dataArray);
}

jQuery(function ($) {
    $("#inqu_status-queryTime").datepicker({
        autoclose: true,
        todayHighlight: true,
        showOtherMonths: true,
        selectOtherMonths: false,
        changeMonth: true,
        changeYear: true,
        //isRTL:true,

        showButtonPanel: true,
        beforeShow: function () {
            //change button colors
            var datepicker = $(this).datepicker("widget");
            setTimeout(function () {
                var buttons = datepicker.find('.ui-datepicker-buttonpane')
                    .find('button');
                buttons.eq(0).addClass('btn btn-xs');
                buttons.eq(1).addClass('btn btn-xs btn-success');
                buttons.wrapInner('<span class="bigger-110" />');
            }, 0);
        }

    });
});

var collectionName = $.cookie("selected_collection") + "_";


jQuery(function ($) {
    $('#inqu_status-dateTimepicker1').datetimepicker({
        format: 'yyyy-mm-dd hh:ii:00'
    });
    $('#inqu_status-dateTimepicker2').datetimepicker({
        format: 'yyyy-mm-dd hh:ii:00'
    });

    $('#inqu_status-dateTimepicker1').datetimepicker().next().on(ace.click_event, function () {
        $(this).prev().focus();
    });
    $('#inqu_status-dateTimepicker2').datetimepicker().next().on(ace.click_event, function () {
        $(this).prev().focus();
    });
});

function on_query_click() {
    var collectorName = $.cookie("selected_collection");
    var tagName = collectionName + $("#bxtree3").bxtree("getSelected")[0].id;
    var pageNode = $("#bxtree3").bxtree("getSelected")[0].id;
    var tagIndex = $("#bxtree3").bxtree("getSelected")[0].parents;
    var selectName = $("#bxtree3").bxtree("getSelected")[0].text;
    var page = $("#grid").bxgrid("rawMethodCallMore", "getGridParam", "page");
    var rowNum = $("#grid").bxgrid("rawMethodCallMore", "getGridParam", "rowNum");
    var treeNode = tagIndex[1]+"."+tagIndex[0]+"."+pageNode;

    //var queryParam = {inqu_status: {"tagName": tagName,"collectorName":$.cookie("selected_collection"), "name": selectName, "pageNode":pageNode, "tagIndex": tagIndex, "page": page, "rowNum": rowNum}};
    var queryParam = {inqu_status: {"tagName": tagName,"collectorName":collectorName, "name": selectName, "pageNode":pageNode, "treeNode":treeNode, "page": page, "rowNum": rowNum}};

    $("#queryarea").bxdiv('setQueryFromDiv', queryParam, "inqu_status");

    $("#grid").bxgrid("option", "queryParam", queryParam);
    $("#grid").bxgrid("query");
}

