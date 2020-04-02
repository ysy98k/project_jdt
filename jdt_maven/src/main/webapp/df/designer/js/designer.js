var isEdit = true;
var configViewContainer = ".ui-layout-east .ui-layout-center #attributeSettingContent";
var configDataViewContainer = ".ui-layout-east .ui-layout-center #dataSettingContent";

var currentDocument = null;
var timerSave = 500; //HJHJ 由于2s还有可能使得缓存更新跟不上操作速度，所以，改成1.5s。
var stopsave = 0;
var startdrag = 0;
var currenteditor = null;//当前编辑的行
var ROLLBACK_LIMIT = 10;
var DEF_DEMO_HEIGHT = 400;
var DEF_DEMO_FOOT_HEIGHT = 150;


function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)return unescape(r[2]);
    return null;
}
var pageId = GetQueryString("pagename");


//根据步骤将demo的数据保存到本地存储中
var layouthistory = [];
var currentStep = 0;
var currentHtml = ""; //保存当前页面html
var currentCss = {};
function saveLayout(state) {
    // demo在重新插入html并且添加sortable事件后，getSaveContent返回值会改变
    // 统一用currentHtml来判断当前页面是否改变
    // 由于图表的html会不停变化，所以删除preview，只判断option是否相同
    widgetsUtils.saveControllers();//saveControllers()中会保存option 到页面，要先执行
    var saveContent = widgetsUtils.getSaveContent();
    if (currentHtml == saveContent && !state) return;
    layouthistory = storageUtils.saveLayout(pageId, saveContent, currentCss, currentStep);
    currentHtml = saveContent;
    currentStep = 0;
    $('#undo').attr("disabled", layouthistory.length < 2);
    $('#redo').attr("disabled", true);
}

function undoLayout() {
    if (layouthistory.length - currentStep < 2) {
        return false;
    }
    var currentLayout = layouthistory[layouthistory.length - currentStep - 2];
    $('.demo').html(currentLayout.html);
    pageCssUtils.loadCss(currentLayout.css);
    currentStep++;
    $('#undo').attr("disabled", layouthistory.length - currentStep < 2);
    $('#redo').attr("disabled", false);
    return true;
}

function redoLayout() {
    if (!currentStep) {
        return false;
    }
    var currentLayout = layouthistory[layouthistory.length - currentStep];
    $('.demo').html(currentLayout.html);
    pageCssUtils.loadCss(currentLayout.css);
    currentStep--;
    $('#undo').attr("disabled", false);
    $('#redo').attr("disabled", !currentStep);
    return true;
}

$(window).resize(function () {
    fitDemoHeight();
    $("body").css("min-height", $(window).height() - 90);
});

function restoreData() {//从本地缓存中恢复数据到demo区域
    window.sessionStorage.removeItem("curobj");
    layouthistory = storageUtils.getLayoutList(pageId);
    var currentLayout = layouthistory[layouthistory.length - 1];
    currentCss = currentLayout.css;
    $(".demo").html(currentLayout.html);
    $('#undo').attr("disabled", layouthistory.length < 2);
    $('#redo').attr("disabled", true);
    widgetsUtils.load();
    pageCssUtils.loadCss(currentLayout.css);
    addDemoSortable();
    currentHtml = widgetsUtils.getSaveContent();
}

/*
 * 添加中间组件的排序效果
 * 动态添加的DOM元素要重新绑定排序事件
 * */
function addDemoSortable() {
    //.demo自排序，且可以排到.column中
    $(".demo").sortable({
        connectWith: ".column",
        opacity: .35,//当排序时助手（helper）的不透明度。从 0.01 到 1
        handle: ".drag",
        items: "> div:not(.sortable-disabled)",
        scroll: true,
        cursor: "move",
        cursorAt: {left: 40, top: 20},
        forcePlaceholderSize: true,
        start: function (e, t) {
            if (!startdrag) stopsave++;
            startdrag = 1;
        },
        sort: function (e, t) {
            //t.item.width('20%');
            t.item.css("max-width", 140).css("max-height", 30).css("overflow", "hidden");
        },
        stop: function (e, t) {
            //t.item.width('100%');
            t.item.css("max-width", '').css("max-height", '').css("overflow", '');
            if (stopsave > 0) stopsave--;
            startdrag = 0;
            DesignerUtils.fitSubWidgets(t.item);
            fitDemoHeight();
        }

    }).disableSelection();

    //.column中的组件自排序
    $(".demo .column").sortable({
        opacity: .35,
        connectWith: ".column",//列表中的项目需被连接的其他 sortable 元素的选择器
        handle: ".drag",
        items: "> div:not(.sortable-disabled)",
        cursor: "move",
        cursorAt: {left: 40, top: 20},
        forceHelperSize: true, forcePlaceholderSize: true, grid: [50, 20], tolerance: "pointer", helper: "original",
        zIndex: 1000000,
        start: function (e, t) {
            if (!startdrag) stopsave++;
            startdrag = 1;
        },
        sort: function (e, t) {
            t.item.width('20%');
            var data_id = t.item.data('id');
            if (data_id && data_id != 'control') {
                widgetsUtils.getControllerList()[data_id].refresh();
                if (data_id.indexOf('bxgrid') == 0) {
                    $("#" + data_id).bxgrid("rawMethodCall", "setGridWidth", t.item.width());
                }

            }
        },
        stop: function (e, t) {
            t.item.width('100%');
            DesignerUtils.fitSubWidgets(t.item);
            if (stopsave > 0) stopsave--;
            startdrag = 0;
            fitDemoHeight();
        }

    }).disableSelection();//因为他们要拖动，所以尽量设置他们的文字不能选择
}

/*
 * 添加左边组件的拖动效果
 * */
function addWidgetsDraggable() {
    function invalidDrag() {
        var buttons = [
            {
                text: '确认',
                "class": "btn btn-sm btn-block  no-padding",
                click: function () {
                    $("#dialog-delect-message").bxdialog('close');
                }
            }
        ];
        var dialogOpt = {
            title: "<i class='ace-icon fa fa-exclamation-triangle red'></i>  提示",
            dataPattern: 'text',
            content: "请将组件拖曳到布局框中，布局框是从“布局设置”栏中获取！",
            buttons: buttons,
            timeout: 1500
        };
        $("#dialog-delect-message").bxdialog(dialogOpt);
    }

    $(".sidebar-nav .lyrow").draggable({
        connectToSortable: ".demo, .colunm",//删除后无法定位到中间的区域
        helper: "clone",//复制移动
        handle: ".drag",//除非鼠标在指定的元素上按下才可以拖拽
        start: function (e, t) {//当拖拽开始时触发
            if (!startdrag) stopsave++;
            startdrag = 1;
        },
        drag: function (e, t) {//拖拽期间当鼠标移动时触发
            t.helper.width('100%');

        },
        stop: function (e, t) {//当拖拽停止时触发
            t.helper.find(".column").addClass("Outermost");
            if (t.helper.parent().hasClass('demo') || t.helper.parent().hasClass('column')) {
                addDemoSortable();
                $(configViewContainer).empty();//先清空右边属性区域内容
                $(configDataViewContainer).empty();
                checkDSContent();
                if (stopsave > 0) stopsave--;
                startdrag = 0;
                t.helper.find(".column").each(function (index, obj) {
                    var $div = $("<legend class='column-title sortable-disabled hidden'></legend>"); //column-display
                    $(obj).append($div);
                    $(obj).addClass("theme-default");
                    $div.unbind("drag");
                });
                fitDemoHeight();
            } else {
                invalidDrag();
            }
        }
    });

    //tab页面特殊处理
    $(".sidebar-nav .lyrow.lyrow-tab").draggable({
        connectToSortable: ".demo, .colunm",//删除后无法定位到中间的区域
        helper: "clone",//复制移动
        handle: ".preview, .drag",//除非鼠标在指定的元素上按下才可以拖拽
        start: function (e, t) {//当拖拽开始时触发
            if (!startdrag) stopsave++;
            startdrag = 1;
        },
        drag: function (e, t) {//拖拽期间当鼠标移动时触发
            t.helper.width('100%');

        },
        stop: function (e, t) {//当拖拽停止时触发
            if (t.helper.parent().hasClass('demo') || t.helper.parent().hasClass('column')) {
                $(configViewContainer).empty();//先清空右边属性区域内容
                $(configDataViewContainer).empty();
                checkDSContent();
                if (stopsave > 0) stopsave--;
                startdrag = 0;

                var id = "tab_" + DesignerUtils.randomNumber();
                var $tab = t.helper.find(".tabbable").attr("id", id);
                $tab.children(".nav").prepend(
                    '<li class="active"><a href="#' + id + '_0" data-toggle="tab">新标签页</a></li>'
                );
                $tab.children(".tab-content").prepend(
                    '<div class="tab-pane active" id="' + id + '_0"><div class="span12 column"></div></div>'
                );
                addDemoSortable();
                fitDemoHeight();
            } else {
                invalidDrag();
            }
        }
    });

    $(".sidebar-nav .box").draggable({
        connectToSortable: ".column",
        helper: "clone",
        handle: ".preview, .drag",
        start: function (e, t) {
            if (!startdrag) stopsave++;
            startdrag = 1;
        },
        drag: function (e, t) {
            t.helper.width('100%');
        },
        stop: function (e, t) {
            if (t.helper.parent().hasClass('column')) {

                $(configViewContainer).empty();//先清空右边属性区域内容
                $(configDataViewContainer).empty();
                widgetsUtils.create(t.helper);
                checkDSContent();
                $(t.helper).tooltip();
                if ($(t.helper).find(".tabbable").length > 0) { //tab页
                    $(t.helper).children(".demoview").remove();
                    $(t.helper).children(".view").attr("style", "");
                    addDemoSortable();
                }
                if (stopsave > 0) stopsave--;
                startdrag = 0;
                fitDemoHeight();
            } else {
                invalidDrag();
            }
        }
    });
}

var last_gritter;
$(document).ready(function () {
    //editables on first profile page
    //图片控件设置
    $.fn.editable.defaults.mode = 'inline';
    $.fn.editableform.loading = "<div class='editableform-loading'><i class='ace-icon fa fa-spinner fa-spin fa-2x light-blue'></i></div>";
    $.fn.editableform.buttons = '<button type="submit" class="btn btn-info editable-submit"><i class="ace-icon fa fa-check"></i></button>' +
        '<button type="button" class="btn editable-cancel"><i class="ace-icon fa fa-times"></i></button>';

    baosightRequire.requireFunct(['bxtree', "bxcombobox", 'bxgrid', 'bxtimepicker',
        'bxdiv', 'bxalert', 'bxvalidate', 'bxdialog', 'bxcharts',
        'bxchartsbar', 'bxchartsline', 'bxchartspie', 'bxchartsgauge',
        'bxchartsmap', 'bxchartsradar', 'bxchartsscatter'], function () {
        //实例化一个加载效果
        var $loading = $("<div class='loading'></div>");
        $("div.sidebar-nav").prepend($loading);
        $loading.spin({
            length: 9
        });

        //初始化保存对话框
        initSaveDialog();

        addEvents();
        storageUtils.loadFromDB(pageId, function () {
            $("body").tooltip();
            loadWidgets.load().done(function (groupList) {
                //组件加载完成后移除加载效果
                $("div.sidebar-nav").find(".loading").spin("destroy").remove();
                $("#accordion").show();
                for (var i = 0; i < groupList.length; i++) {
                    var groupObject = groupList[i];
                    var $group = $("<div class='group'></div>");
                    var $title = $("<h3 class='accordion-header'></h3>");
                    $title.text(groupObject.name);
                    $title.attr("title", groupObject.tooltip || groupObject.name);
                    $group.append($title);

                    var $temp = $("<div></div>");
                    var $ul = $("<ul class='nav nav-list accordion-group'></ul>");
                    var $li = $("<li style='display: list-item;' class='rows'></li>");
                    var list = groupList[i].widgets;
                    for (var ii = 0; ii < list.length; ii++) {
                        var component = list[ii];
                        if (component != undefined)
                            $li.append(component);
                    }
                    $ul.append($li);
                    $temp.append($ul);
                    $group.append($temp);
                    $("#accordion").append($group);
                }

                $("#accordion").accordion({//左边层叠效果
                    collapsible: false,
                    heightStyle: "fill",
                    animate: 250,
                    header: ".accordion-header"
                }).sortable({
                    axis: "y",
                    handle: ".accordion-header",
                    stop: function (event, ui) {
                        // IE doesn't register the blur when sorting
                        // so trigger focusout handlers to remove .ui-state-focus
                        ui.item.children(".accordion-header").triggerHandler("focusout");
                    }
                });


                ContainerLoader.load();
                TabLoader.load();

                restoreData();//从本地缓存中恢复数据
                //TODO: 这里要调用resize()方法，否则页面不会出滚动条，要看一下resize()做了什么
                addWidgetsDraggable();
                $(window).resize();
                currentHtml = widgetsUtils.getSaveContent();

                setInterval(function () {//隔一段时间保存内容
                    if (stopsave) return;
                    stopsave++;
                    saveLayout();
                    stopsave--;
                }, timerSave);
            });

        });

    });
});


function saveDesignData() {
    storageUtils.update2DB(pageId, function (data) {
            if (last_gritter) $.gritter.remove(last_gritter);
            last_gritter = $.gritter.add({
                title: '保存成功！',
                text: '保存成功，请刷新页面！',
                class_name: 'gritter-success gritter-right'
            });
        }, function (data) {
            if (last_gritter) $.gritter.remove(last_gritter);
            last_gritter = $.gritter.add({
                title: '保存失败！',
                text: '保存失败，请联系系统管理员进行排查！',
                class_name: 'gritter-success gritter-right'
            });
        }
    );
}

function insertDesignPage(paramJsonObj) {
    var pagename = paramJsonObj.pageEname;
    storageUtils.create2DB(paramJsonObj, function (data) {//调用成功的回调函数
        if (last_gritter) $.gritter.remove(last_gritter);
        last_gritter = $.gritter.add({
            title: '保存成功！',
            text: '保存成功，新增设计页面：' + pagename,
            class_name: 'gritter-success gritter-right'
        });
        $("#detail").dialog("close");
        location.href = location.href + "?pagename=" + pagename;
    }, function (data) {//调用失败的回调函数
        if (last_gritter) $.gritter.remove(last_gritter);
        last_gritter = $.gritter.add({
            title: '保存失败！',
            text: '保存失败，请检查页面参数是否重复！',
            class_name: 'gritter-item-wrapper gritter-error'
        });
    });
}

function openCodeditor() {
    //$.getJSON("./js/actions.json", function (data) {
    //    var actions = data.control_actions;
    //    CodeEditor.init(pageId, actions);
    //});
    var actions = [
        {
            "type": "none",
            "actions": [
                {
                    "name": "无事件",
                    "params": "()",
                    "tooltip": "请先加载对象"
                }
            ]
        }
    ];
    var tempActions = {};
    for (var id in widgetsUtils.getControllerList()) {
        var controller = widgetsUtils.getControllerList()[id];
        var type = controller.getType();
        if (!tempActions[type]) {
            tempActions[type] = {};
            actions.push(controller.getActions());
        }
    }
    CodeEditor.init(pageId, actions);
}


function openDataSourceditor() {
    dataSourceditor.init(pageId);

    var dialogOpt = {
        resizable: false,
        width: 800,
        height: 605,
        modal: true,
        title: "<i class='ace-icon fa fa-database'></i>  数据源编辑器",
        close: function (event, ui) {
            dataSourceditor.saveDSOptions();
            var curWidget = window.sessionStorage.getItem("curobj");
            if (curWidget) widgetsUtils.showConfigView(JSON.parse(curWidget).name);
        },
        buttons: [{
            text: "关 闭",
            width: 100,
            height: 30,
            "class": "btn btn-block btn-xs",
            click: function () {
                dataSourceditor.saveDSOptions();
                var curWidget = window.sessionStorage.getItem("curobj");
                if (curWidget) widgetsUtils.showConfigView(JSON.parse(curWidget).name);
                $(this).dialog("close");
            }
        }]
    };
    $("#dataSourceEditor").bxdialog(dialogOpt);

}

function initSaveDialog() {
    $("#detail").bxdiv();
    $("#detail").bxvalidate();
    var ruleOptionCustom = {
        rules: {
            detailpageEname: {
                required: true,
                englishCheck: true
            },
            detailpageCname: {
                required: true,
                stringCheck: true
            },
            detailpageType: {
                required: true,
                englishCheck: true
            }
        }
    };
    $("#detail").bxvalidate("option", "ruleOptionCustom", ruleOptionCustom);
}

function fitDemoHeight() {
    $('.demo').css("min-height", DEF_DEMO_HEIGHT + "px");
    var demoMinHeight = $('.demo').height() + DEF_DEMO_FOOT_HEIGHT;
    $('.demo').css("min-height", demoMinHeight + "px");

}

/*
 * 使用冒泡机制
 * 统一绑定 demo 页面中的事件
 * 只在初始化时调一次即可
 * */
function addEvents() {
    //设计器菜单按钮事件
    $("#bindEvent").click(function () {
        openCodeditor();
    });
    $("#dataSource").click(function () {
        openDataSourceditor();
    });
    $("#btnSave").click(function () {//修改实现，点击保存前，先判断url地址是否有pagename,如果无，则弹出新建页面窗口
        saveLayout();
        if (pageId == null || pageId == undefined) {
            $("#detail").bxdiv();
            $("#detail").bxvalidate();
            $("#detail").bxdiv("cleanPopDiv");
            $("#detail-pageType option[value='designPage']").prop("selected", "selected");
            $("#detail-pagePath").val("路径自动生成");
            var buttons = [{
                text: "保存",
                "class": "btn btn-sm btn-block  no-padding",
                click: function () {
                    var pageCname = $("#detail-pageCname").val();
                    var pageEname = $("#detail-pageEname").val();
                    if (!pageCname) {
                        //如果没有输入中文名，用英文名代替
                        $("#detail-pageCname").val(pageEname);
                    }
                    if ($("#detail").bxvalidate("validate")) {
                        var paramJsonObj = new Object();
                        $("#detail").bxdiv('setInfoFromDiv', paramJsonObj, 'detail');
                        var pageType = paramJsonObj.detail.resultRow[0].pageType;
                        //DesignPage类型
                        insertDesignPage(paramJsonObj.detail.resultRow[0]);
                    }
                }
            }];
            var title = "输入新增页面信息";
            var dialogOpt = {
                title: title,
                buttons: buttons
            };
            $("#detail").bxdialog(dialogOpt);
        } else {
            saveDesignData();
        }
    });

    $('#undo').click(function () {
        stopsave++;
        if (undoLayout()) {
            addDemoSortable();
            widgetsUtils.load();
            fitDemoHeight();
            currentHtml = widgetsUtils.getSaveContent();
        }
        stopsave--;
    });
    $('#redo').click(function () {
        stopsave++;
        if (redoLayout()) {
            addDemoSortable();
            widgetsUtils.load();
            fitDemoHeight();
            currentHtml = widgetsUtils.getSaveContent();
        }
        stopsave--;
    });

    $("#clear").click(function (e) {
        var buttons = [
            {
                text: '确认',
                "class": "btn btn-sm btn-block  no-padding",
                click: function () {
                    $(".demo").empty();
                    window.sessionStorage.removeItem("curobj");
                    storageUtils.clean(pageId);
                    widgetsUtils.clearControllerList();
                    widgetsUtils.cleanConfigView();
                    layouthistory = [];
                    $("#dialog-delect-message").bxdialog('close');
                }
            }
        ];
        var dialogOpt = {
            title: "<i class='ace-icon fa fa-exclamation-triangle red'></i>  确认删除",
            dataPattern: 'text',
            content: "  数据删除后将不可恢复，是否确定删除！",
            buttons: buttons
        };
        $("#dialog-delect-message").bxdialog(dialogOpt);
        return;
    });

    $('#preview').click(function () {
        var allobj = widgetsUtils.getControllerList();
        var importJs = [];
        for (key in allobj) {
            var curObj = allobj[key];
            if ((curObj.getType() == "bxtimepicker" || curObj.getType() == "bxtree" || curObj.getType() == "bxgrid" ||
                curObj.getType() == "bxcombobox") && $.inArray(curObj.getType(), importJs) == -1) {
                importJs.push(curObj.getType());
            } else if ((curObj.getType() == "chartCol" || curObj.getType() == "chartColT" || curObj.getType() == "chartBar" ||
                curObj.getType() == "chartBarT") && $.inArray("bxchartsbar", importJs) == -1) {
                if ($.inArray("bxcharts", importJs) == -1) {
                    importJs.push("bxcharts");
                }
                importJs.push("bxchartsbar");
            } else if ((curObj.getType() == "chartLine" || curObj.getType() == "chartLineT" || curObj.getType() == "chartLineS" ||
                curObj.getType() == "chartArea" || curObj.getType() == "chartAreaT") && $.inArray("bxchartsline", importJs) == -1) {
                if ($.inArray("bxcharts", importJs) == -1) {
                    importJs.push("bxcharts");
                }
                importJs.push("bxchartsline");
            } else if ((curObj.getType() == "chartPie" || curObj.getType() == "chartPieC") && $.inArray("bxchartspie", importJs) == -1) {
                if ($.inArray("bxcharts", importJs) == -1) {
                    importJs.push("bxcharts");
                }
                importJs.push("bxchartspie");
            } else if (curObj.getType() == "chartGauge" && $.inArray("bxchartsgauge", importJs) == -1) {
                if ($.inArray("bxcharts", importJs) == -1) {
                    importJs.push("bxcharts");
                }
                importJs.push("bxchartsgauge");
            } else if (curObj.getType() == "chartMap" && $.inArray("bxchartsmap", importJs) == -1) {
                if ($.inArray("bxcharts", importJs) == -1) {
                    importJs.push("bxcharts");
                }
                importJs.push("bxchartsmap");
            } else if ((curObj.getType() == "chartRadar" || curObj.getType() == "chartRadarF") && $.inArray("bxchartsradar", importJs) == -1) {
                if ($.inArray("bxcharts", importJs) == -1) {
                    importJs.push("bxcharts");
                }
                importJs.push("bxchartsradar");
            } else if (curObj.getType() == "chartScat" && $.inArray("bxchartsscatter", importJs) == -1) {
                if ($.inArray("bxcharts", importJs) == -1) {
                    importJs.push("bxcharts");
                }
                importJs.push("bxchartsscatter");
            }
        }
        var importJsStr = "";
        for (var i = 0; i < importJs.length; i++) {
            if (i > 0) {
                importJsStr += ","
            }
            importJsStr += importJs[i]
        }
        window.open("viewpage.do?type=preview&pagename=" + pageId + "&currentStep=" + currentStep + "&importJs=" + importJsStr);
    });

    //布局设置
    //input为Html5新增事件，向输入框手动输入内容时触发
    //若IE兼容有问题可在events中加入"propertychange change"，目前没发现兼容问题
    $("#accordion").on("input", ".lyrow .preview input", function () {
        var getValue = $(this).val().replace(/^[0\s]|\b0|[^0-9\s]/g, "").replace(/\s{2,}/g, " ");
        $(this).val(getValue);

        var gridCount = 0;
        var divList = "";
        $.each(getValue.split(" ", 12), function (n, r) {
            if (!r) return;
            gridCount += parseInt(r);
            divList += '<div class="span' + r + ' column"></div>';
        });

        if (gridCount == 12) {
            $(this).closest(".lyrow").find(".row-fluid").html(divList);
            $(this).closest(".preview").prev().show();
            $(this).removeClass("error");
        } else {
            $(this).closest(".preview").prev().hide();
            $(this).addClass("error");
        }
    })


    /*****************************动态生成的元素事件***************************/
    //container的单击事件
    $(".demo").on("click", function (e) {
        e.stopPropagation(); // 阻止事件冒泡
        window.sessionStorage.removeItem("curobj");
        Container.showConfigView(this, $(configViewContainer));
        $(configDataViewContainer).empty();
        checkDSContent();
    });

    //布局框的行(Row)的单击事件
    $(".demo").on("click", '.column', function (e) {
        e.stopPropagation(); // 阻止事件冒泡
        window.sessionStorage.removeItem("curobj");
        Container.showColumnConfigView(this, $(configViewContainer));
        $(configDataViewContainer).empty();
        checkDSContent();
    });

    //布局框内的各组件的单击事件
    $(".demo").on("click", '.demoview > *', function (e) {
        //加入图片控件后，需要点击的默认行为来打开资源管理器
        //e.preventDefault();
        e.stopPropagation(); // 阻止事件冒泡
        var curobj = {
            "name": $(this).parent().parent().data("id"),
            "type": $(this).parent().parent().data("type")
        };
        window.sessionStorage.setItem("curobj", JSON.stringify(curobj));
        widgetsUtils.showConfigView(curobj.name);
        checkDSContent();
    });

    //删除按钮的单击事件
    $(".demo").on("click", ".lyrow .remove", function (e) {
        e.preventDefault();
        e.stopPropagation(); // 阻止事件冒泡
        var $html = $(this).parent();

        if ($html.data("id")) { //组件框删除动作
            widgetsUtils.removeController($html.data("id"));
        } else {
            $html.find('div[data-id]').each(function () {
                widgetsUtils.removeController($(this).data("id"));
            });
        }
        window.sessionStorage.removeItem("curobj");
        widgetsUtils.cleanConfigView();
        $html.remove();

        fitDemoHeight();
    });

    //tab页面切换
    $(".demo").on("click", ".tabbable>.nav a[href]", function (e) {
        e.preventDefault();
        e.stopPropagation(); // 阻止事件冒泡
        $(this).closest("ul").children(".active").removeClass("active");
        $(this).tab("show");
        DesignerUtils.fitSubWidgets($($(this).attr("href")));
        window.sessionStorage.removeItem("curobj");
        TabLoader.showConfigView(this, $(configViewContainer));
        $(configDataViewContainer).empty();
        checkDSContent();
    });

    //tab页面添加
    $(".demo").on("click", ".tabbable>.nav a.tabs-plus", function (e) {
        e.preventDefault();
        e.stopPropagation(); // 阻止事件冒泡
        $(this).parent().prevAll().removeClass("active");
        var thisTab = $(this).closest(".tabbable");
        var newId = thisTab.attr("id") + "_" + thisTab.children(".tab-content").children().length;
        $(this).closest("li").before(
            '<li><a href="#' + newId + '" data-toggle="tab">新标签页</a><span class="ace-icon fa fa-times"></span></li>'
        );
        thisTab.children(".tab-content").append(
            '<div class="tab-pane" id="' + newId + '"><div class="span12 column"></div></div>'
        );

        addDemoSortable();
        var $newNav = thisTab.find('a[href="#' + newId + '"]');
        $newNav.tab("show");
        window.sessionStorage.removeItem("curobj");
        TabLoader.showConfigView($newNav, $(configViewContainer));
        $(configDataViewContainer).empty();
        checkDSContent();
    });

    //tab删除
    $(".demo").on("click", ".tabbable>ul.nav>li>span", function (e) {
        e.preventDefault();
        e.stopPropagation(); // 阻止事件冒泡
        var closeid = $(this).siblings("a").attr("href");
        var $thisTab = $(this).closest(".tabbable");
        var tabId = $thisTab.attr("id");
        $(closeid).remove();
        $(this).closest("li").remove();
        var tabLength = $thisTab.children(".tab-content").children().length;
        for (var i = 1; i <= tabLength; i++) {
            $thisTab.find(">ul>li:nth-child(" + i + ")>a").attr("href", "#" + tabId + "_" + (i - 1));
            $thisTab.find(">.tab-content>.tab-pane:nth-child(" + i + ")").attr("id", tabId + "_" + (i - 1));
        }
        if ($thisTab.find(">ul>li.active").length < 1) {
            var $thisNav = $thisTab.find(">ul>li:first-child>a");
            $thisNav.tab("show");
            window.sessionStorage.removeItem("curobj");
            TabLoader.showConfigView($thisNav, $(configViewContainer));
            $(configDataViewContainer).empty();
            checkDSContent();
        }

    });

    //tab属性
    $(".demo").on("click", ".tabbable>.tab-content, .tabbable>.tab-content>.tab-pane>.column", function (e) {
        e.preventDefault();
        e.stopPropagation(); // 阻止事件冒泡
        var thisId = $(this).closest(".tab-content").children(".active").attr("id");
        var $thisNav = $(this).closest(".tabbable").find('a[href="#' + thisId + '"]');
        window.sessionStorage.removeItem("curobj");
        TabLoader.showConfigView($thisNav, $(configViewContainer));
        $(configDataViewContainer).empty();
        checkDSContent();
    });

}

//window.onbeforeunload = function(){return "onbeforeunload"}

/*
 * 检查数据源绑定框是否有内容
 * 是则显示，否则隐藏
 * */
function checkDSContent() {
    if ($("#dataSettingContent").children().length > 0) {
        $("#DSTab").removeClass("hide");
        return;
    }
    $("#DSTab").addClass("hide");
    $("#settingTab > li:first > a").tab("show");
}
