var jsEditorContent;
var importJs;
var detailType;
var bxuiFunction;
var beautifyObj;
$(document).ready(function() {	
    importJs = "charts" + chartType + "demo.js";
    detailType = "charts" + chartType.substring(0, 1).toUpperCase() + chartType.substring(1)　+ "Option_" + detailType;
    bxuiFunction = "bxcharts" + chartType;	
    baosightRequire.requireFunct(['js_beautify'],function(beautify) {
    	beautifyObj = beautify;
	    jsEditorContent = ace.edit("jscontent");
	    jsEditorContent.setTheme("ace/theme/twilight");
	    jsEditorContent.renderer.setScrollMargin(10, 10);
	    jsEditorContent.getSession().setMode("ace/mode/javascript");	    
	    var onload = function () {
            executeImport();
        };   
	    var node = document.createElement('script');
	    node.type = "text/javascript";
	    $(node).load(onload).bind('readystatechange', function () {
            if (node.readyState == 'loaded') {
                onload();
            }
        });
	    document.getElementsByTagName('head')[0].appendChild(node);
        node.src = toolkitPath + '/df/example/charts/' + importJs;
    });
});

function autoResize() {
    var dom = ace.require("ace/lib/dom");
    dom.toggleCssClass(document.body, "fullScreen");
    dom.toggleCssClass(jsEditorContent.container, "fullScreen-editor");
    jsEditorContent.resize();
}

//add command to all new editor instaces
ace.require("ace/commands/default_commands").commands.push({
    name: "Toggle Fullscreen",
    bindKey: "F2",
    exec: function(editor) {
        var dom = ace.require("ace/lib/dom");
        dom.toggleCssClass(document.body, "fullScreen");
        dom.toggleCssClass(editor.container, "fullScreen-editor");
        editor.resize();
    }
});

function executeImport() {
    var bxImport = "bxcharts" + chartType;  
    var contentStr = "option=" + JSON.stringify(eval(detailType));
    contentStr = beautifyObj.js_beautify(contentStr);
    jsEditorContent.setValue(contentStr);
    jsEditorContent.getSession().setScrollTop(0);
    jsEditorContent.clearSelection();
    submitCode();
   
}

function submitCode() {
    var contentStr = jsEditorContent.getValue();
    var firstLeftBracket = contentStr.indexOf("{");
    var lastRightBracket = contentStr.lastIndexOf("}");
    var subContentStr = contentStr.substring(firstLeftBracket, lastRightBracket + 1);
    var option = eval('(' + subContentStr + ')');
    
    var bxuiCall = "$('#dispArea')." + bxuiFunction + "(" + JSON.stringify(option) + ")";
    baosightRequire.requireFunct(['bxcharts',bxuiFunction],function(beautify) {
       eval(bxuiCall);
    });
}

function returnBack(){
	window.history.back(-1); 
}