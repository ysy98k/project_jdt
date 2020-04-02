function _get_basepath() {
    var a = window.location,
        b = a.pathname.split("/");
    return b.length >= 2 ? "/" + b[1] : ""
}
var appName = _get_basepath(),
prefix = document.scripts[document.scripts.length - 1].src.substring(0, document.scripts[document.scripts.length - 1].src.lastIndexOf("/") + 1),
    _baosight_ui_include_files = new Array("jquery/dist/jquery.js",
        /*"echarts3/echarts.js", "echarts3/map/china.js", "echarts3/theme/chalk.js", "echarts3/theme/dark.js",
        "echarts3/theme/essos.js", "echarts3/theme/halloween.js", "echarts3/theme/infographic.js",
        "echarts3/theme/purple-passion.js","echarts3/theme/roma.js", "echarts3/theme/vintage.js", "echarts3/theme/walden.js",
        "echarts3/theme/westeros.js", "echarts3/theme/wonderland.js", "echarts3/theme/macarons.js",*/

        "other/js/json.js",
        "bootstrap-plugin/js/bootbox.js", "bootstrap-plugin/js/bootstrap-editable.js",
        "aceadmin/js/ace-editable.js",
        "other/js/spin.js",
        "other/js/bxcommon.js",
        "jquery-ui/js/jquery-ui.js",
        "bootstrap/dist/js/bootstrap.js",
        "aceadmin/js/ace-extra.js",
        "aceadmin/js/ace-elements.js",
        "aceadmin/js/ace.js",
        "baosight-require.js"
    ),
    bxImportFile = function(a) {
        document.write('<script type="text/javascript" src="' + a + '"></script>')
    },
    bxImportCSS = function(a) {
        document.write('<link href="' + a + '" rel="stylesheet" type="text/css"/>')
    };
for (var _file in _baosight_ui_include_files) bxImportFile(prefix + _baosight_ui_include_files[_file]);
var _baosight_ui_include_css = new Array(
    "bootstrap/dist/css/bootstrap.css",
    "jquery-ui/css/jquery-ui.css",
    "jquery-plugin/css/jquery.gritter.css",
    "jqgrid/css/ui.jqgrid.css",
    "aceadmin/style/css/ace-fonts.css",
    "aceadmin/style/css/ace.css",
    "aceadmin/style/css/ace-skins.css",
    "aceadmin/style/css/ace-rtl.css",
    "aceadmin/style/css/ace.onpage-help.css",
    //"jstree-bootstrap-theme/dist/themes/custom/style.css",
    "bootstrap-plugin/css/bootstrap-editable.css",
    "bxwidget/css/bx-skin.css",
    "jquery-plugin/css/jquery.autocomplete.css"
);
for (var _file in _baosight_ui_include_css) bxImportCSS(prefix + _baosight_ui_include_css[_file]);

var baosightRequire = {
    requireFunct: function(a, b) {
        require(a, b)
    },
    importCSS: function(a, b) {
        $("<link>").attr({
            rel: "stylesheet",
            type: "text/css",
            href: a
        }).appendTo("head")
    }
};
