var global_grid_data = [{
    id: "001",
    name: "电脑",
    tradeday: "2015-12-01",
    amount: "100",
    phone: "0230701",
    desc: "2007-12-03",
    status: 'notpay',
    area: 0
}, {
    id: "002",
    name: "台灯",
    tradeday: "2015-12-02",
    amount: "200",
    phone: "0230702",
    desc: "2007-12-03",
    status: 'notpay',
    area: 0
}, {
    id: "003",
    name: "显示器",
    tradeday: "2015-12-03",
    amount: "300",
    phone: "0230703",
    desc: "2007-12-03",
    status: 'pay',
    area: 1
}, {
    id: "004",
    name: "话筒",
    tradeday: "2015-12-04",
    amount: "400",
    phone: "0230704",
    desc: "2007-12-03",
    status: 'pay',
    area: 0
}, {
    id: "005",
    name: "打印机",
    tradeday: "2015-12-05",
    amount: "500",
    phone: "0230705",
    desc: "2007-12-03",
    status: 'notpay',
    area: 2
}, {
    id: "006",
    name: "音响",
    tradeday: "2015-12-06",
    amount: "600",
    phone: "0230706",
    desc: "2007-12-03",
    status: 'pay',
    area: 2
}, {
    id: "007",
    name: "手机",
    tradeday: "2015-12-07",
    amount: "700",
    phone: "0230707",
    desc: "2007-12-03",
    status: 'pay',
    area: 2
}, {
    id: "008",
    name: "服务器",
    tradeday: "2015-12-08",
    amount: "800",
    phone: "0230708",
    desc: "2007-12-03",
    status: 'notpay',
    area: 0
}, {
    id: "009",
    name: "打印机",
    tradeday: "2015-12-09",
    amount: "900",
    phone: "0230709",
    desc: "2007-12-03",
    status: 'pay',
    area: 0
}, {
    id: "010",
    name: "电脑",
    tradeday: "2015-12-10",
    amount: "1000",
    phone: "0230710",
    desc: "2007-12-03",
    status: 'pay',
    area: 1
}, {
    id: "011",
    name: "台灯",
    tradeday: "2015-12-11",
    amount: "1100",
    phone: "0230711",
    desc: "2007-12-03",
    status: 'notpay',
    area: 1
}, {
    id: "012",
    name: "显示器",
    tradeday: "2015-12-12",
    amount: "1200",
    phone: "0230712",
    desc: "2007-12-03",
    status: 'pay',
    area: 1
}, {
    id: "013",
    name: "话筒",
    tradeday: "2015-12-13",
    amount: "1300",
    phone: "0230713",
    desc: "2007-12-03",
    status: 'pay',
    area: 1
}, {
    id: "014",
    name: "打印机",
    tradeday: "2015-12-14",
    amount: "1400",
    phone: "0230714",
    desc: "2007-12-03",
    status: 'pay',
    area: 1
}, {
    id: "015",
    name: "音响",
    tradeday: "2015-12-15",
    amount: "1500",
    phone: "0230715",
    desc: "2007-12-03",
    status: 'notpay',
    area: 2
}, {
    id: "016",
    name: "手机",
    tradeday: "2015-12-16",
    amount: "1600",
    phone: "0230716",
    desc: "2007-12-03",
    status: 'notpay',
    area: 2
}, {
    id: "017",
    name: "服务器",
    tradeday: "2015-12-17",
    amount: "1700",
    phone: "0230717",
    desc: "2007-12-03",
    status: 'pay',
    area: 2
}, {
    id: "018",
    name: "打印机",
    tradeday: "2015-12-18",
    amount: "1800",
    phone: "0230718",
    desc: "2007-12-03",
    status: 'pay',
    area: 2
}, {
    id: "019",
    name: "打印机",
    tradeday: "2015-12-19",
    amount: "1900",
    phone: "0230719",
    desc: "2007-12-03",
    status: 'notpay',
    area: 2
}, {
    id: "020",
    name: "电脑",
    tradeday: "2015-12-20",
    amount: "2000",
    phone: "0230720",
    desc: "2007-12-03",
    status: 'notpay',
    area: 2
}, {
    id: "021",
    name: "台灯",
    tradeday: "2015-12-21",
    amount: "2100",
    phone: "0230721",
    desc: "2007-12-03",
    status: 'pay',
    area: 2
}, {
    id: "022",
    name: "显示器",
    tradeday: "2015-12-22",
    amount: "2200",
    phone: "0230722",
    desc: "2007-12-03",
    status: 'pay',
    area: 2
}, {
    id: "023",
    name: "话筒",
    tradeday: "2015-12-23",
    amount: "2300",
    phone: "0230723",
    desc: "2007-12-03",
    status: 'notpay',
    area: 2
}];

$(document).ready(function () {
    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxcombobox'], function () {

        showPayStatus();

        var gridOption = {
            colNames: ['ID', '物品', '交易日期', '交易金额', '电话', '描述', '状态', '区域'],
            colModel: [
                {
                    name: 'id',
                    index: 'id',
                    width: 60
                }, {
                    name: 'name',
                    index: 'name',
                    width: 60,
                    editable: true
                }, {
                    name: 'tradeday',
                    index: 'tradeday',
                    width: 90
                }, {
                    name: 'amount',
                    index: 'amount',
                    width: 90
                }, {
                    name: 'phone',
                    index: 'phone',
                    width: 90
                }, {
                    name: 'desc',
                    index: 'desc',
                    width: 90
                },
                {
                    name: 'status',
                    index: 'status',
                    width: 90,
                    edittype: 'ccs',
                    editoptions: 'payment.status'
                }
                , {
                    name: 'area',
                    index: 'area',
                    width: 100,
                    editable: true,
                    edittype: 'select',
                    editrules: {
                        required: true
                    },
                    formatter: 'select',
                    editoptions: {value: {0: '上海', 1: '苏州', 2: '北京'}}
                }],
            data: global_grid_data,
            sortable: true,
            sortname: 'id',
            sortorder: 'desc',
            caption: "物品买卖交易",
            onSelectRow: function (id) {
			    /*var buttons = [
		   			{
		   				text: '确认',
		   				"class" : "btn btn-primary btn-xs red",
		   				click: function() {
		   					$("#dialog-edit").bxdialog('close');
		   				}
		   			}
		   		];
		   		var dialogOpt = {
		   			title :"<i class='ace-icon fa fa-exclamation-triangle red'></i>  行编辑事件",
		   			dataPattern: 'text',
		   			content : "行编辑事件，这里进行行触发操作",
		   			buttons : buttons
		   		};
		   		$("#dialog-edit").bxdialog(dialogOpt);
		   		$("#grid").bxgrid('rawMethodCall', 'editRow', id,true);*/
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

        showGroupGrid();
        showAddRowGrid();

        var option = {
            queryParam: {},
            gridOption: gridOption,
            dataPattern: 'local'
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

        var paramObj = {
            dataPattern: "pds",
            condition: [
                {
                    item: 'process',
                    value: ['A050100', 'A050200', 'A050300'],
                    label: ['原料', '焦化', '烧结'],
                    generateInstance: true,
                    order: 1
                },
                {
                    item: 'time',
                    value: DateUtil.getTimeLstByInterval(DateUtil.getWholeDay(new Date(now.getTime() - 1000 * 60 * 60 * 24 * 10)).getTime(), DateUtil.getWholeDay(new Date(now.getTime())).getTime(), 24 * 60 * 60 * 1000),
                    type: 'date',
                    returnFormatter: 'yyyy-MM-dd HH:mm:ss',
                    intervals: 24 * 60 * 60 * 1000,
                    order: 2
                }, {
                    item: 'energyId',
                    value: ['E2101'],
                    label: ['电'],
                    generateAttribute: true,
                    order: 1
                }, {
                    item: 'propertyId',
                    value: ['D'],
                    label: ['使用量'],
                    generateAttribute: true,
                    order: 2
                }, {
                    item: 'timegrandid',
                    value: ['day'],
                    label: ['日'],
                    generateAttribute: true,
                    order: 3
                }
            ],
            chartRelate: {
                legendItem: 'process'
            }
        };

        //  AjaxCommunicator.ajaxRequest("/charthandler",'POST', paramObj, callback);
        var bxgridOption = {
            dataPattern: 'local',
            gridOption: {
                colNames: ['时间戳', '原料', '焦化', '烧结'],
                colModel: [
                    {
                        name: 'time',
                        index: 'time',
                        width: 60,
                        editable: true
                    },
                    {
                        name: 'A050100',
                        index: 'A050100',
                        width: 60,
                        editable: true
                    }, {
                        name: 'A050200',
                        index: 'A050200',
                        width: 60,
                        editable: true
                    }, {
                        name: 'A050300',
                        index: 'A050300',
                        width: 60,
                        editable: true
                    }],
                caption: "工序时序数据——pds数据源模式",
                data: grid_data,
                jsonReader: {
                    id: "time",
                    repeatitems: false
                }
            }
        };
        $("#pdsgrid").bxgrid(bxgridOption);


        var image_grid_data = [{
            key1: "香蕉",
            key2: "https://www.baidu.com/img/bd_logo1.png"
        }, {
            key1: "苹果",
            key2: "http://www.51hlife.com/images/b2b/hsh_logo.png"
        },{
            key1: "橘子",
            key2: "http://technet.ccdomain.com/drupal/themes/garland/logo.png"
        }];


        var imageGridOption = {
            colNames: ['物品', '链接'],
            colModel: [
                {
                    name: 'key1',
                    index: 'key1',
                    width: 60
                }, {
                    name: 'key2',
                    index: 'key2',
                    width: 60,
                    image:true,
                    formatter:'link',
                    formatoptions:{
                        target:'_blank'
                    }
                }],
            data: image_grid_data,
            sortable: true,
            caption: "物品链接"
        };
        var imageOption = {
            queryParam: {},
            gridOption: imageGridOption,
            dataPattern: 'local',
            navGridOption: {
                download: true,
                downloadParam: {
                    downloadUrl: "/df/example/bxgridDemo.do/download.do"
                }
            }
        };
        $("#gridImage").bxgrid(imageOption);
    });
});

function clk(){
    var dataArray = $("#grid").bxgrid("rawMethodCall","getGridParam","data");
    var selectArray = $("#grid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if(selectArray.length == 0){
        alertDiv("提示","请至少勾选一条记录进行操作！")
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

function getSelects() {
    return {shanghai: '上海', jiangsu: '江苏'};
}

function insertOpt() {
    $("#detail").bxdiv("cleanPopDiv");
    var buttons = [
        {
            text: "保存",
            "class": "btn btn-primary btn-xs",
            click: function () {
                $("#detail").dialog("close");
                dialogMessage("说明", "触发新增事件！");
            }
        }];

    var title = "新增记录";
    var dialogOpt = {
        title: title,
        buttons: buttons
    };
    $("#detail").bxdialog(dialogOpt);
}

function showPayStatus() {
    $("#detail-status").bxcombobox({
        ccsId: "payment.status", // 表示调用ccs服务时，需要查询的分类代码ID，返回为该分类代码对应的子项
        async: false,
        dataPattern: "ccs",
        classAttr: "col-xs-10 col-sm-10"
    });
}

function updateOpt() {
    $("#detail").bxdiv("cleanPopDiv");
    var selectId = $("#grid").bxgrid('rawMethodCall', 'getGridParam', 'selrow');
    var selectData = $("#grid").bxgrid('rawMethodCall', 'getRowData', selectId);
    var paramJsonObj = new Object();
    paramJsonObj = {detail: selectData};
    $("#detail").bxdiv('fillPopDiv', paramJsonObj, 'detail');
    var buttons = [
        {
            text: "保存",
            "class": "btn btn-primary btn-xs",
            click: function () {
                $("#detail").dialog("close");
                dialogMessage("说明", "触发修改事件！");
            }
        }];

    var title = "修改记录";
    var dialogOpt = {
        title: title,
        buttons: buttons
    };
    $("#detail").bxdialog(dialogOpt);
}

function deleteOpt() {
    $("#griddetail").text("触发删除事件！");
    var buttons = [
        {
            text: "确定",
            "class": "btn btn-primary btn-xs",
            click: function () {
                $("#griddetail").dialog("close");
            }
        }];
    var title = "删除记录";
    var dialogOpt = {
        title: title,
        buttons: buttons
    };
    $("#griddetail").bxdialog(dialogOpt);
}

function dialogMessage(title, centext) {
    $("#dialog-message").html(centext);
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
                    "class": "btn btn-primary btn-xs",
                    click: function () {
                        $(this).dialog("close");
                    }
                }]
            });
}

function showGroupGrid() {

    var gridOption = {
        colNames: ['ID', '物品', '交易日期', '交易金额', '电话', '描述', '状态', '区域'],
        colModel: [
            {
                name: 'id',
                index: 'id',
                width: 60
            }, {
                name: 'name',
                index: 'name',
                width: 60,
                editable: true
            }, {
                name: 'tradeday',
                index: 'tradeday',
                width: 90
            }, {
                name: 'amount',
                index: 'amount',
                width: 90
            }, {
                name: 'phone',
                index: 'phone',
                width: 90
            }, {
                name: 'desc',
                index: 'desc',
                width: 90
            },
            {
                name: 'status',
                index: 'status',
                width: 90,
                edittype: 'ccs',
                editoptions: 'payment.status'
            }
            , {
                name: 'area',
                index: 'area',
                width: 100,
                editable: true,
                //edittype: 'select',
                //editrules: {
                //    required: true
                //},
                //formatter: 'select',
                //editoptions: {value: {0: '上海', 1: '苏州', 2: '北京'}}
            }],
        data: global_grid_data,
        sortable: true,
        sortname: 'id',
        sortorder: 'desc',
        caption: "物品买卖交易",
        grouping: true,
        groupingView: {
            groupField: ["area"],
            groupColumnShow: [false],
            groupText: ["<span style='cursor:pointer;' onclick='$(this).prev().click();'><b>{0}</b></span>"],
            groupOrder: ["asc"],
            groupSummary: [false],
            groupCollapse: true,
            plusicon: "ace-icon fa fa-plus",
            minusicon: "ace-icon fa fa-minus"
        }
    };

    var option = {
        queryParam: {},
        gridOption: gridOption,
        dataPattern: 'local',
        navGridOption: {
            edit: true,
            editicon: 'ace-icon fa fa-pencil blue',
            editfunc: updateOpt,
            add: true,
            addicon: 'ace-icon fa fa-plus-circle purple',
            addfunc: insertOpt,
            del: true,
            delicon: 'ace-icon fa fa-trash-o red',
            delfunc: deleteOpt,
            search: true,
            searchicon: 'ace-icon fa fa-search orange',
            refresh: true,
            refreshicon: 'ace-icon fa fa-refresh green'
        }

    };
    baosightRequire.requireFunct(['bxgrid'], function () {
        $("#groupGrid").bxgrid(option);
    });

}



var row_grid_data = [{
    id2: "001",
    name: "电脑",
    tradeday: "2015-12-01",
    amount: "100",
    phone: "0230701",
    desc: "2007-12-03",
    status: 'notpay',
    area: 0
}, {
    id2: "002",
    name: "台灯",
    tradeday: "2015-12-02",
    amount: "200",
    phone: "0230702",
    desc: "2007-12-03",
    status: 'notpay',
    area: 0
}, {
    id2: "003",
    name: "显示器",
    tradeday: "2015-12-03",
    amount: "300",
    phone: "0230703",
    desc: "2007-12-03",
    status: 'pay',
    area: 1
}, {
    id2: "004",
    name: "话筒",
    tradeday: "2015-12-04",
    amount: "400",
    phone: "0230704",
    desc: "2007-12-03",
    status: 'pay',
    area: 0
}, {
    id2: "005",
    name: "打印机",
    tradeday: "2015-12-05",
    amount: "500",
    phone: "0230705",
    desc: "2007-12-03",
    status: 'notpay',
    area: 2
}, {
    id2: "006",
    name: "音响",
    tradeday: "2015-12-06",
    amount: "600",
    phone: "0230706",
    desc: "2007-12-03",
    status: 'pay',
    area: 2
}, {
    id2: "007",
    name: "手机",
    tradeday: "2015-12-07",
    amount: "700",
    phone: "0230707",
    desc: "2007-12-03",
    status: 'pay',
    area: 2
}, {
    id2: "008",
    name: "服务器",
    tradeday: "2015-12-08",
    amount: "800",
    phone: "0230708",
    desc: "2007-12-03",
    status: 'notpay',
    area: 0
}, {
    id2: "009",
    name: "打印机",
    tradeday: "2015-12-09",
    amount: "900",
    phone: "0230709",
    desc: "2007-12-03",
    status: 'pay',
    area: 0
}, {
    id2: "010",
    name: "电脑",
    tradeday: "2015-12-10",
    amount: "1000",
    phone: "0230710",
    desc: "2007-12-03",
    status: 'pay',
    area: 1
}, {
    id2: "011",
    name: "台灯",
    tradeday: "2015-12-11",
    amount: "1100",
    phone: "0230711",
    desc: "2007-12-03",
    status: 'notpay',
    area: 1
}, {
    id2: "012",
    name: "显示器",
    tradeday: "2015-12-12",
    amount: "1200",
    phone: "0230712",
    desc: "2007-12-03",
    status: 'pay',
    area: 1
}, {
    id2: "013",
    name: "话筒",
    tradeday: "2015-12-13",
    amount: "1300",
    phone: "0230713",
    desc: "2007-12-03",
    status: 'pay',
    area: 1
}, {
    id2: "014",
    name: "打印机",
    tradeday: "2015-12-14",
    amount: "1400",
    phone: "0230714",
    desc: "2007-12-03",
    status: 'pay',
    area: 1
}, {
    id2: "015",
    name: "音响",
    tradeday: "2015-12-15",
    amount: "1500",
    phone: "0230715",
    desc: "2007-12-03",
    status: 'notpay',
    area: 2
}, {
    id2: "016",
    name: "手机",
    tradeday: "2015-12-16",
    amount: "1600",
    phone: "0230716",
    desc: "2007-12-03",
    status: 'notpay',
    area: 2
}];


function showAddRowGrid(){
	        var gridOption = {
            colNames: [' ','ID','物品', '交易日期', '交易金额', '电话', '描述', '状态', '区域'],
            colModel: [
                {name:'myac',index:'', width:80, fixed:true, sortable:false, resize:false,
					formatter:'actions', //设置编辑和删除按钮
					formatoptions:{ 
						keys:false,
						delbutton: false,
						afterSave : function (rowid, response, options) {
						   alert(rowid);	
						   alert(response);
						   alert(options);
						}
						
					}
				},				
                {
                    name: 'id2',
                    index: 'id2',
                    editable: true,
                    width: 60
                },
                {
                    name: 'name',
                    index: 'name',
                    width: 60,
                    editable: true
                }, {
                    name: 'tradeday',
                    index: 'tradeday',
                    editable: true,
                    width: 90
                }, {
                    name: 'amount',
                    index: 'amount',
                    editable: true,
                    width: 90
                }, {
                    name: 'phone',
                    index: 'phone',
                    editable: true,
                    width: 90
                }, {
                    name: 'desc',
                    index: 'desc',
                    editable: true,
                    width: 90
                },
                {
                    name: 'status',
                    index: 'status',
                    width: 90,
                    editable: true,
                    edittype: 'ccs',
                    editoptions: 'payment.status'
                }
                , {
                    name: 'area',
                    index: 'area',
                    width: 100,
                    editable: true,
                    edittype: 'select',
                    editrules: {
                        required: true
                    },
                    formatter: 'select',
                    editoptions: {value: {0: '上海', 1: '苏州', 2: '北京'}}
                }],
            data: row_grid_data,
            sortable: true,
            sortname: 'id2',
            sortorder: 'asc',
            caption: "物品买卖交易"
        };
	    var option = {
            queryParam: {},
            gridOption: gridOption,
            dataPattern: 'local',
            navGridOption: {
                edit: false,              
                add: true,
                addicon: 'ace-icon fa fa-plus-circle purple',
                addfunc: addRow,
                del: true,
                delicon: 'ace-icon fa fa-trash-o red',
                delfunc: deleteRow,
                search: false,
                refresh: false,
            }
        };
        $("#groupAddRow").bxgrid(option);
}

/*点击*/
function addRow(){
	var num = $("#groupAddRow").bxgrid("rawMethodCall",'getDataIDs');
	var addIndex = (parseFloat(num[num.length-1])+1)+"";
	$("#table_groupAddRow").addRowData(addIndex, {
	    
	});
	$("#table_groupAddRow").jqGrid("editRow","11",{ 
	    keys : true, 
	    onsavefunc: function() {
	        alert ("edited"); 
	    }
	});
	jQuery.fn.fmatter.rowactions.call($("#jEditButton_3"),'edit');
	//alert(addIndex);
}

function deleteRow(){
	var num = $("#groupAddRow").bxgrid("rawMethodCall",'getDataIDs');
	alert(num);
}