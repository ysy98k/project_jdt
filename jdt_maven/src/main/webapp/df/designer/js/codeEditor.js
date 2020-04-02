var editor_modified;
var currentScript;
var CodeEditor = (function () {

    var PAGE_FUNCS = [
        {
            type: "snippet",
            meta: "sys",
            caption: "sys.HandlePage",
            snippet: "sys.HandlePage(${1:pagename}, ${2:oldtags}, ${3:newtags}, ${4:pageparam})",
            score: 1,
            desc: '[函数]\nHandlePage(pagename, oldtags, newtags, pageparam) \n   [描述]\n在主视图中根据不同的参数打开页面 \n'
            +
            '[参数]\npagename 画面名，字符串 \noldtags 被替换的变量名称，字符串/数组，可以不设 \nnewtags 替换的变量名称，字符串/数组，可以不设 \npageparam 页面参数，字符串',
            docHTML: 'function'
        }
    ];

    function saveScript(pageId, value) {
        storageUtils.saveJS(pageId, value);
    }

    var CodeEditor = {
        init: function (pageId, actions) {
            this.scriptid = pageId;
            var d_width = 800;
            var d_height = 500;
            var d_toolbar_height = 28;
            var codeEditorObj = this;
            var container = document.createElement('div');
            container.id = 'codeEditorDialog';
            container.style.width = d_width + 'px';
            container.style.height = d_height + 'px';
            container.style.padding = '0px'
            var toolbar = document.createElement('div');
            toolbar.style.width = d_width + 'px';
            toolbar.style.height = d_toolbar_height + 'px';
            toolbar.style.align = 'center';
            container.appendChild(toolbar);
            var div = document.createElement('div');
            div.id = 'editor';
            div.style.width = d_width + 'px';
            div.style.height = (d_height - d_toolbar_height) + 'px';
            container.appendChild(div);

            $(container).appendTo($(document.body));
            $("#codeEditorDialog").dialog({
                modal: true,
                resizable: false,
                width: 'auto',
                height: 'auto',
                closeOnEscape: false,
                title: '事件编辑器 ',
                beforeClose: function (event, ui) {
                    codeEditorObj.onClose();
                    $(container).remove();
                }
            });

            var jqToolbar = $(toolbar);
            // showControls
            function showControls() {
                // 控件选择框
                $("<select id='codeEditorContSelect' style='width:32%; margin: .0em; ' ></select>").appendTo(jqToolbar);
                // 事件选择框
                $("<select id='codeEditorActionSelect' style='width:32%; margin: .0em; ' ></select>").appendTo(jqToolbar);
                $("<button id='btnCodeEditorformat' class='btn btn-white btn-warning btn-xs'style='width:12%;height: 100%;'>格式化</button>").appendTo(jqToolbar);
                // 查找按钮
                $("<button id='btnCodeEditorFind' class='btn btn-white btn-warning btn-xs'style='width:12%;height: 100%;'>查找</button>").appendTo(jqToolbar);
                // 替换按钮
                $("<button id='btnCodeEditorReplace' class='btn btn-white btn-warning btn-xs' style='width:12%;height: 100%; '>替换</button>").appendTo(jqToolbar);
                // 设定css
                $("button[id^='btnCodeEditor']").css("vertical-align", "top");
            }

            function showActionSelect(type) {
                function showAction(item) {//生成事件
                    var noMatch = true;
                    for (var j = 0; j < item.actions.length; j++) {
                        var action = item.actions[j];
                        var option = $("<option value='" + action.name + "@" + type
                            + "' title='" + action.tooltip + "'>" + action.name + "</option>")
                            .appendTo($("#codeEditorActionSelect"));
                        // 如果当前对象已经绑定事件，选中(指向)当前事件所在的代码行
                        if (noMatch) {
                            var funcName = "";
                            funcName += currCell.name + "_" + action.name;
                            var range = editor.find(funcName, {
                                skipCurrent: false,
                                wrap: true,
                                regExp: false,
                                caseSensitive: false,
                                wholeWord: true
                            });
                            noMatch = !range && funcName;
                            editor.clearSelection();
                            if (!noMatch) {
                                //editor.addSelectionMarker(range);
                                option.attr("selected", "selected");
                                editor.navigateTo(range.start.row, 0);
                            }
                        }
                    }
                }

                $("#codeEditorActionSelect").empty();

                for (var i = 0; i < actions.length; i++) {
                    var item = actions[i];
                    if ('default' == item.type || type == item.type) {
                        showAction(item);
                    }
                }
                $("#codeEditorActionSelect").selectmenu("refresh");
                if (type != "none")
                    $("#codeEditorActionSelect").selectmenu({
                        select: function (event, ui) {
                            // 寻找editsession中没有当前选中控件的选中动作
                            // 如果有，定位到那里
                            // 没有，着新增一条    对象名_动作 = function(e){}
                            var type = ui.item.value.split("@")[1];
                            var params;
                            for (var i = 0; i < actions.length; i++) {
                                if (type == actions[i].type || 'default' == actions[i].type) {
                                    var action = actions[i].actions;
                                    for (var j = 0; j < action.length; j++) {
                                        if (ui.item.label == action[j].name) {
                                            params = action[j].params;
                                            break;
                                        }
                                    }
                                }
                            }
                            var funcName = "";
                            funcName += currCell.name + "_" + ui.item.label;
                            var range = editor.find(funcName, {
                                skipCurrent: false,//是否不搜索当前行
                                wrap: true,//搜索到文档底部是否回到顶端
                                regExp: false,//搜索内容是否是正则表达式
                                caseSensitive: true,//是否匹配大小写搜索
                                wholeWord: true//是否匹配整个单词搜素
                            });
                            var noMatch = !range && funcName;
                            editor.clearSelection();
                            if (!noMatch) {
                                editor.navigateTo(range.start.row, 0);
                            }
                            else {
                                params = /^\(/.test(params) ? params : "(" + params;
                                params = /\)$/.test(params) ? params : params + ")";
                                editor.navigateTo(editor.session.getLength(), 0);
                                editor.insert("function " + funcName + params + "{\n\n}\n\n");
                                editor.navigateTo(editor.session.getLength() - 4, 0);
                                editor.focus();
                            }
                        }
                    });

                // 为每一项加上tooltips
                var lang = ace.require("ace/lib/lang");
                for (var i = 0; i < $("#codeEditorActionSelect").children().length; i++) {
                    var item = $("#codeEditorActionSelect").children()[i];
                    var menuitem = $("#codeEditorActionSelect-menu").children()[i];
                    $(menuitem).attr("title", lang.escapeHTML($(item).attr("title")).replace(/\n/g, "<br>"));
                    $(menuitem).tooltip({
                        position: {
                            my: "left top",
                            at: "right+5 top-5"
                        },
                        content: function () {
                            return $(this).attr("title");
                        }
                    });
                }
            }

            var beautifyObj;
            baosightRequire.requireFunct(['js_beautify'],function(beautify) {
                beautifyObj = beautify;
                $("#btnCodeEditorformat").click();
            });

            $("#editor").css("border-top", "1px solid #DDD");
            var editor = ace.edit("editor");
            editor.session.setMode("ace/mode/javascript");
            editor.setTheme("ace/theme/twilight");
            editor.setFontSize(14);//字体大小
            //editor.setOption("wrap", "free");//自动换行,设置为off关闭
            //editor.setHighlightActiveLine(false);
            //editor.setTheme("ace/theme/terminal");
            //editor.setTheme("ace/theme/chrome");
            editor.setShowPrintMargin(false)
            editor.$blockScrolling = Infinity;
            ace.config.loadModule('ace/ext/language_tools', function (module) {
                ace.config.loadModule("ace/autocomplete", function (module) {
                    module.Autocomplete.startCommand.bindKey = "Alt-/"; //修改代码提示快捷键为 eclipse 默认快捷键 alt+/
                });
                editor.setOptions({
                    enableSnippets: true,
                    enableBasicAutocompletion: true,
                    enableLiveAutocompletion: true
                });
                //// 代码补全
                //var completer = {
                //    getCompletions: function (editor, session, pos, prefix, callback) {
                //        if (prefix.length === 0) {
                //            return callback(null, []);
                //        } else {
                //            return callback(null, funcs);
                //        }
                //    },
                //    getDocTooltip: function (item) {
                //        if (item.type == "snippet" && item.docHTML == "function") {
                //            item.docHTML = [
                //                "<b>", lang.escapeHTML(item.caption), "</b>", "<hr/>",
                //                lang.escapeHTML(item.desc)
                //            ].join("");
                //        }
                //    }
                //};
                //module.addCompleter(completer);

                // 添加 ctrl+s 快捷键，使可以保存已经编辑的代码
                editor.commands.addCommand({
                    name: "saveCurrentScript",
                    exec: function (editor) {
                        //sessionStorage.setItem(codeEditorObj.scriptid, editor.session.getValue());
                        saveScript(codeEditorObj.scriptid, editor.session.getValue());
                    },
                    bindKey: "Ctrl-S"
                });
            });
            //var pagescript = sessionStorage.getItem(this.scriptid);//从缓存中获取当前页面的脚本
            var pagescript = storageUtils.getJS(this.scriptid);
            if (pagescript != null)
                editor.session.setValue(pagescript);

            editor.focus();

            var cells = new Array();
            var model = widgetsUtils.getControllerList();
            for (var i in model) {//获取对象
                if (!model[i])
                    continue;
                var cell = {
                    "name": i,
                    "type": model[i].getType()
                };
                cells.push(cell);
            }
            if (typeof(i) == "undefined") {
                sessionStorage.setItem("curobj", null);
                cells = [{"name": "无对象", "type": "none"}];
            }
            var currCell = undefined;
            if (sessionStorage.getItem("curobj") != null)
                currCell = JSON.parse(sessionStorage.getItem("curobj"));
            showControls();

            for (var i = 0; i < cells.length; i++) {//生成对象
                var cell = cells[i];
                $("<option value='" + cell.name + "@" + cell.type + "'>" + cell.name + "</option>").appendTo($("#codeEditorContSelect"));
                if (i == 0 && currCell == null) {
                    currCell = cell;
                }
            }

            $("#codeEditorContSelect").selectmenu({//选择对象，生成此对象可用的事件
                select: function (event, ui) {
                    var name = ui.item.value.split("@")[0];
                    var type = ui.item.value.split("@")[1];
                    for (var i = 0; i < cells.length; i++) {
                        if (name == cells[i].name) {
                            currCell = cells[i];
                            break;
                        }
                    }
                    $("#codeEditorActionSelect").selectmenu({
                        select: undefined
                    });
                    showActionSelect(type);
                    $(this).blur();
                }
            }).selectmenu("menuWidget").css("max-height", d_height - 100 + "px");

            $("#codeEditorActionSelect").selectmenu().selectmenu("menuWidget").css("max-height", d_height - 100 + "px");

            if (currCell != undefined) {//打开编辑框时，根据所选对象显示相关事件
                showActionSelect(currCell.type);
                $("#codeEditorContSelect").selectmenu("refresh");
                $("#codeEditorContSelect").val(currCell.name + "@" + currCell.type);
                $("#codeEditorContSelect").selectmenu("refresh");
            }
            //生成下拉按钮
            $(".ui-icon-triangle-1-s").addClass("fa fa-caret-down").removeClass("ui-icon-triangle-1-s")

            $("#btnCodeEditorFind").button()
                .click(function (event) {
                        ace.config.loadModule("ace/ext/searchbox", function (e) {
                            e.Search(editor);
                            $("input[placeholder='Search for']").attr("placeholder", "查找");
                            $("[action='toggleRegexpMode']").attr("title", "使用正则表达式");
                            $("[action='toggleCaseSensitive']").attr("title", "大小写敏感");
                            $("[action='toggleWholeWords']").attr("title", "整词搜索");
                        });
                    }
                );
            $("#btnCodeEditorformat").button().click(function (event) {
                var editorContext=editor.session.getValue();
                editor.session.setValue (beautifyObj.js_beautify(editorContext));
            });
            $("#btnCodeEditorReplace").button().click(function (event) {
                ace.config.loadModule("ace/ext/searchbox", function (e) {
                    e.Search(editor, true);
                    $("input[placeholder='Replace with']").attr("placeholder", "替换为");
                    $("[action='replaceAndFindNext']").attr("title", "替换并且查找下一个");
                    $("[action='replaceAll']").attr("title", "替换所有");

                    $("input[placeholder='Search for']").attr("placeholder", "查找");
                    $("[action='toggleRegexpMode']").attr("title", "使用正则表达式");
                    $("[action='toggleCaseSensitive']").attr("title", "大小写敏感");
                    $("[action='toggleWholeWords']").attr("title", "整词搜索");
                });
            });
        },
        onClose: function (cancel) {
            var editor = ace.edit("editor");
            editor.commands.removeCommand({
                name: "saveCurrentScript"
            });

            $("#codeEditorContSelect").selectmenu("destroy");
            $("#codeEditorActionSelect").selectmenu("destroy");
            //sessionStorage.setItem(this.scriptid, editor.session.getValue());
            saveScript(this.scriptid, editor.session.getValue());
            editor_modified = true;
        },
        scriptid: "",
        saveEvent: function () {
            if (true) {
                //var eventCont=sessionStorage.getItem("demo1_eventCode");
                var eventCont = storageUtils.getJS(pageId);
                //var oldEventCont = JSON.parse(sessionStorage.getItem("oldEventCont"));
                //if (oldEventCont != null) {
                //    for (var m = 0; m < oldEventCont.length; m++) {
                //        if ($("#" + oldEventCont[m].id).length > 0) {
                //            $("#" + oldEventCont[m].id).removeAttr(oldEventCont[m].action);
                //        }
                //    }
                //}
                if (eventCont.match("function") != null) {
                    var eventArray = eventCont.match(/function\s+([^\(]+)/g);
                    for (var i = 0; i < eventArray.length; i++) {
                        eventArray[i] = eventArray[i].replace("function", "").trim();
                        var eventObj = new Object();
                        eventObj.id = eventArray[i].split("_")[0];
                        eventObj.action = eventArray[i].split("_")[1];
                        if ($("#" + eventObj.id).length > 0) {
                            $("#" + eventObj.id).attr(eventObj.action, eventArray[i] + "();");
                        }
                        eventArray[i] = eventObj;
                    }
                }
                //sessionStorage.setItem("oldEventCont", JSON.stringify(eventArray));
            }
        }
    };
    return CodeEditor;
})();


