function initComponent() {

    loadWidgets.load().done(function (list) {
        if (type == "preview") {
            restoreFromLocalStorage();
        }
        else {
            restoreData();
        }
    });
}

function changeStructure(e, t) {
    $(".demo ." + e).removeClass(e).addClass(t)
}

function cleanHtml(e) {
    $(e).parent().append($(e).children().clone(true));
}

function removeClean(html) {//下载
    var t = $("div.demo");
    t.find(".preview, .configuration, .drag, .remove").remove();
    t.find(".lyrow").addClass("removeClean");
    t.find(".box-element").addClass("removeClean");
    t.find(".box-element").addClass("element-padding");
    //t.find(".lyrow .lyrow .lyrow .lyrow .lyrow .removeClean").each(function () {
    //    cleanHtml(this)
    //});
    //t.find(".lyrow .lyrow .lyrow .lyrow .removeClean").each(function () {
    //    cleanHtml(this)
    //});
    //t.find(".lyrow .lyrow .lyrow .removeClean").each(function () {
    //    cleanHtml(this)
    //});
    //t.find(".lyrow .lyrow .removeClean").each(function () {
    //    cleanHtml(this)
    //});
    //t.find(".lyrow .removeClean").each(function () {
    //    cleanHtml(this)
    //});
    //t.find(".removeClean").each(function () {
    //    cleanHtml(this)
    //});
    //t.find(".removeClean").remove();
    t.find(".lyrow").css("height", "");
    $(".demo .column").removeClass("ui-sortable");
    $(".demo .row-fluid").removeClass("clearfix").children().removeClass("column");
    if ($(".demo .container").length > 0) {
        changeStructure("row-fluid", "row")
    }
}
function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)return unescape(r[2]);
    return null;
}
var pageId = GetQueryString("pagename");
var type = GetQueryString("type");
var currentStep = GetQueryString("currentStep");
var importJs = GetQueryString("importJs");

function restoreData() {//从本地缓存中恢复数据到demo区域
    var node = document.createElement('script');
    node.type = "text/javascript";
    var onload = function () {
        var currentLayout = storageUtils.getLayout(pageId, currentStep);
        $(".demo").html(currentLayout.html);
        pageCssUtils.loadCss(currentLayout.css);
        widgetsUtils.load();
        removeClean();
    };
    $(node).load(onload).bind('readystatechange', function () {
        if (node.readyState == 'loaded') {
            onload();
        }
    });
    document.getElementsByTagName('head')[0].appendChild(node);
    node.src = "loadsave.do?method=publishjs&pagename=" + pageId;
}

function restoreFromLocalStorage() {
    var node = document.createElement('script');
    node.type = "text/javascript";
    document.getElementsByTagName('head')[0].appendChild(node);
    node.text = " //# sourceURL=" + window.location.origin + window.location.pathname + "/" + pageId + ".js \n" + storageUtils.getJS(pageId);
    var currentLayout = storageUtils.getLayout(pageId, currentStep);
    var importJsArray = [];
    if (isAvailable(importJs)) {
        /*读取需要require进来的js文件*/
        importJsArray = importJs.split(",");
    }
    importJsArray.push("bxdialog");
    baosightRequire.requireFunct(importJsArray, function () {
        $(".demo").html(currentLayout.html);
        pageCssUtils.loadCss(currentLayout.css);
        widgetsUtils.load();
        removeClean();
        changeTabAndBxgrid();
    });
}

function changeTabAndBxgrid() {
    //tab页面切换
    $(".demo").on("click", ".tabbable>.nav a[href]", function (e) {
        e.preventDefault();
        e.stopPropagation(); // 阻止事件冒泡
        $(this).closest("ul").children(".active").removeClass("active");
        $(this).tab("show");
        for (var id in widgetsUtils.getBxgridList()) {
            $("#" + id).bxgrid("rawMethodCall", "setGridWidth", $("#" + id).parent().parent().width());
        }
        for (var id in widgetsUtils.getBxchartList()) {
            widgetsUtils.getControllerList()[id].refresh();
        }
    });
}

$(document).ready(function () {
    function setBackGround() {
        var backgroundColor = pageCssUtils.getCss('.demo', 'background-color');
        if (backgroundColor) {
            $(".demo").css('background-color', backgroundColor);
        }
    }

    function init() {
        initComponent();
        $(document).attr("title", pageId);
    }

    if (type == "preview") {
        //setBackGround();
        init();
    } else {
        storageUtils.loadFromDB(pageId, function (importJs) {
            //setBackGround();
            if (isAvailable(importJs)) {//如果有需要导入的bxui文件，这里负责统一导入
                var importJsArray = importJs.split(",");
                importJsArray.push("bxdialog");
                baosightRequire.requireFunct(importJsArray, function () {
                    init();
                    changeTabAndBxgrid();
                });
            } else {
                var paramJsonObj = {};
                paramJsonObj.pageId = pageId;
                var callback = {
                    onSuccess: function (paramJsonObj) {
                        var importJsArray = paramJsonObj.importJs.split(",");
                        importJsArray.push("bxdialog");
                        baosightRequire.requireFunct(importJsArray, function () {
                            init();
                            changeTabAndBxgrid();
                        });
                    }
                };
                AjaxCommunicator.ajaxRequest('/df/designer/loadsave.do?method=getRequireJsArray', 'POST', paramJsonObj, callback);
            }
        });
    }
});