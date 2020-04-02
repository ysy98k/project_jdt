(function (WidgetObj, type) {

    var bxtree;
    var contextmenu_state = false;

    var creatNode = function (data) {
        var inst = $.jstree.reference(data.reference),
            obj = inst.get_node(data.reference);
        inst.create_node(obj, {}, "last", function (new_node) {
            setTimeout(function () {
                inst.edit(new_node);
            }, 0);
            var optionData = bxtree.getOptions()["data"];
            var newNode = {value: new_node.id, label: 'New node'}
            if (new_node.parent == bxtree.getId()) {
                optionData.push(newNode);
            } else {
                addNode(optionData);
            }
            function addNode(node) {
                for (var i = 0; i < node.length; i++) {
                    var value = node[i].value;
                    var children = node[i].children;
                    var label = node[i].label;
                    if (value == new_node.parent) {
                        if (children == undefined) {
                            node[i].children = [];
                        }
                        node[i].children.push(newNode);
                    } else {
                        if (children != undefined) {
                            addNode(children);
                        }
                    }
                }
            }
        });
    };

    var removeNode = function (data) {
        var inst = $.jstree.reference(data.reference),
            obj = inst.get_node(data.reference);

        var optionData = bxtree.getOptions()["data"];
        if (obj.id == bxtree.getId()) {
            alert("根节点不可删除");
            return;
        } else {
            delNode(optionData);
            if (inst.is_selected(obj)) {
                inst.delete_node(inst.get_selected());
            }
            else {
                inst.delete_node(obj);
            }
        }
        function delNode(node) {
            for (var i = 0; i < node.length; i++) {
                var value = node[i].value;
                var children = node[i].children;
                var label = node[i].label;
                if (value == obj.id) {
                    node.splice(i, 1)
                } else {
                    if (children != undefined) {
                        delNode(children);
                    }
                }
            }
        }
    };
    var success = 1;

    function unique(newValue, opt) {
        for (var m = 0; m < opt.length; m++) {
            var value = opt[m].value;
            var children = opt[m].children;
            if (value != newValue) {
                if (children != undefined && success == 1) {
                    success = 1;
                    unique(newValue, children);
                }
            } else {
                success = 0;
                return;
            }
        }
    }

    function reValue(oldValue, newValue, opt) {
        for (var i = 0; i < opt.length; i++) {
            var value = opt[i].value;
            var children = opt[i].children;
            if (value == oldValue) {
                opt[i].value = newValue;
            } else {
                if (children != undefined) {
                    reValue(oldValue, newValue, children);
                }
            }
        }
    }

    function rename(nodeid, newname, opt) {
        for (var i = 0; i < opt.length; i++) {
            var value = opt[i].value;
            var children = opt[i].children;
            if (value == nodeid) {
                opt[i].label = newname;
            } else {
                if (children != undefined) {
                    rename(nodeid, newname, children);
                }
            }
        }
    }

    bxTree = {
        actionsToEvent: {
            "Ready": "ready.jstree",
            "AfterOpen": "after_open.jstree",
            "AfterClose": "after_close.jstree",
            "Refresh": "refresh.jstree",
            "OnSelect": "select_node.jstree",
            "OnHover": "hover_node.jstree",
            "OnChanged": "changed.jstree",
            "OnMove": "move_node.jstree"
        },
        actions: {
            "type": type,
            "actions": [
                {
                    "name": "Ready",
                    "params": "",
                    "tooltip": "所有节点完成加载后触发"
                },
                {
                    "name": "AfterOpen",
                    "params": "",
                    "tooltip": "打开节点后触发"
                },
                {
                    "name": "AfterClose",
                    "params": "",
                    "tooltip": "关闭节点后触发"
                },
                {
                    "name": "Refresh",
                    "params": "",
                    "tooltip": "更新树时触发"
                },
                {
                    "name": "OnSelect",
                    "params": "event, obj",
                    "tooltip": "在控件上选中节点触发"
                },
                {
                    "name": "OnHover",
                    "params": "",
                    "tooltip": "在控件上移到节点上时触发"
                },
                {
                    "name": "OnChanged",
                    "params": "event, data",
                    "tooltip": "当选择改变后触发"
                },
                {
                    "name": "OnMove",
                    "params": "event, data",
                    "tooltip": "当移动选择的节点时触发"
                }
            ]
        },
        initOption: function () {
            bxtree = this;
            var dataObj = [
                {
                    'value': 'node_1',
                    'label': '树节点1',
                    'children': [{
                        'label': '叶子1', 'value': 'j2_1', 'children': [{
                            'value': 'j3_1',
                            'label': '末端'
                        }]
                    }, {'value': 'j2_2', 'label': '叶子2'}]
                },
                {
                    'value': 'node_2',
                    'label': '树节点2',
                    'children': [{'value': 'j2_3', 'label': '叶子3'}]
                }
            ];
            this.setOptions({
                dataPattern: 'local',
                ccsCfg:'',
                selectNode: {
                    enable: true,
                    id: "",
                    text: ""
                },
                async: false,
                showText: '树根',
                data: dataObj,
                option: {
                    'core': {
                        'multiple': true,
                        'strings': {
                            'Loading ...': 'waiting...'
                        },
                        'themes': {
                            'name': 'default',
                            'stripes': false,
                            'responsive': true
                        },
                        "check_callback": true
                    },
                    "checkbox": {
                        "three_state": false
                    },
                    "plugins": ["themes", "wholerow", "contextmenu"],
                    "contextmenu": {
                        "items": {
                            "create": {
                                "separator_before": true,
                                "separator_after": true,
                                "_disabled": false,
                                "label": "增加树节点",
                                "icon": "fa fa-plus",
                                "action": creatNode
                            },
                            /*"rename" : {
                             "separator_before"	: false,
                             "separator_after"	: false,
                             "_disabled"			: false,
                             "label"				: "重命名",
                             "icon"				: "fa fa-pencil",
                             "action"			: renameNode
                             },*/
                            "remove": {
                                "separator_before": false,
                                "separator_after": false,
                                "_disabled": false,
                                "label": "删除树节点",
                                "icon": "fa fa-trash-o",
                                "action": removeNode
                            },
                            "ccp": null
                        }
                    }
                }
            });
        },
        getValue: function () {
            var selected = "";
            $.each($("#" + this.getId()).bxtree("getSelected"), function (index, item) {
                selected += item.id + ",";
            });
            return selected.substr(0, selected.length - 1);
        },
        showPreviewCore: function ($dataConfigView) {
            bxtree = this;
            var id = bxtree.getPreviewId();
            var option = bxtree.getOptions();
            option.option.contextmenu.items.create.action = creatNode;
            option.option.contextmenu.items.remove.action = removeNode;
            if (option.dataPattern != "local") {
                option = bxtree.prepareOption();
            }
            $("#" + id).bxtree(option).bind("rename_node.jstree", function (e, data) {
                var optionData = bxtree.getOptions()["data"];
                rename(data.node.id, data.text, optionData);
            });
        },
        showCore: function () {
            bxtree = this;
            var id = bxtree.getId();
            var option = bxtree.getOptions();
            if (option.option.plugins.indexOf("contextmenu") > -1)
                option.option.plugins.splice(option.option.plugins.indexOf("contextmenu"), 1);
            if (option.dataPattern != "local") {
                option = bxtree.prepareOption();
            }
            $("#" + id).bxtree(option);
        },
        getConfigViewCore: function ($configView) {
            bxtree = this;
            var treeOpt = this.getOptions()["option"];
            bxtree.getConfigViewsControl("treeId", $configView).text(bxtree.getId());
            var heightArribute = bxtree.getConfigViewsControl("treeHeight", $configView);
            heightArribute.val($("#" + bxtree.getId()).height());
            heightArribute.change(function () {
                treeOpt.height = $(this).val();
                $("#" + bxtree.getId()).css("min-height", $(this).val() + "px");
                bxtree.setOption("option", treeOpt);
                bxtree.refresh();
            });
            var cores = bxtree.getConfigViewsControl("coreattribute", $configView).find("input");
            $.each(cores, function (index, item) {
                $(this).change(function () {
                    treeOpt.core[$(this).data("attribute")] = $(this).val();
                    bxtree.setOption("option", treeOpt);
                    bxtree.refresh();
                });
            });
            var plugins = bxtree.getConfigViewsControl("plugins", $configView).find("input[type=checkbox]");
            $.each(plugins, function (index, item) {
                if (treeOpt.plugins.indexOf($(this).data("attribute")) > -1) {
                    if ($(this).data("attribute") == "checkbox") {
                        bxtree.getConfigViewsControl("checkboxplugin", $configView).show();
                    }
                    $(this).context.checked = true;
                } else {
                    if ($(this).data("attribute") == "checkbox") {
                        bxtree.getConfigViewsControl("checkboxplugin", $configView).hide();
                    }
                    $(this).context.checked = false;
                }
                $(this).change(function () {
                    if ($(this).data("attribute") == "checkbox") {//复选框要拿出来单独判断
                        if ($(this).context.checked == true) {
                            bxtree.getConfigViewsControl("checkboxplugin", $configView).show();
                        } else {
                            bxtree.getConfigViewsControl("checkboxplugin", $configView).hide();
                        }
                    }
                    if ($(this).context.checked) {
                        if (treeOpt.plugins.indexOf($(this).data("attribute")) == -1) {
                            treeOpt.plugins.push($(this).data("attribute"));
                        }
                    } else {
                        if (treeOpt.plugins.indexOf($(this).data("attribute")) > -1) {
                            treeOpt.plugins.splice(treeOpt.plugins.indexOf($(this).data("attribute")), 1);
                        }
                    }
                    bxtree.setOption("option", treeOpt);
                    bxtree.refresh();
                });
            });
            var checkboxplugins = bxtree.getConfigViewsControl("checkboxplugin", $configView).find("input[type=checkbox]");
            $.each(checkboxplugins, function (index, item) {
                bxtree.setAttributeFromOption(treeOpt, $(this));
                $(this).change(function () {
                    if (treeOpt.checkbox == null || treeOpt.checkbox == undefined) {
                        treeOpt.checkbox = {};
                    }
                    treeOpt.checkbox[$(this).data("attribute")] = $(this).context.checked;
                    bxtree.setOption("option", treeOpt);
                    bxtree.refresh();
                });
            });
        },
        getDataConfigViewCore: function ($dataConfigView) {
            var attributeList = $dataConfigView.find("[data-attribute]");
            bxtree = this;
            if (bxtree.getOptions()["selectNode"].enable) {
                if ($("#" + bxtree.getId()).bxtree("getSelected").length > 0) {
                    var selId = $("#" + bxtree.getId()).bxtree("getSelected")[0].id;
                    var selText = $("#" + bxtree.getId()).bxtree("getSelected")[0].text;
                    bxtree.getOptions()["selectNode"].id = selId;
                    bxtree.getOptions()["selectNode"].text = selText;
                    bxtree.getConfigViewsControl("treeValue", $dataConfigView).val(selId);
                    bxtree.getConfigViewsControl("treeLable", $dataConfigView).val(selText);
                } else {
                    bxtree.getOptions()["selectNode"].id = "";
                    bxtree.getOptions()["selectNode"].text = "";
                    bxtree.getConfigViewsControl("treeValue", $dataConfigView).val("");
                    bxtree.getConfigViewsControl("treeLable", $dataConfigView).val("");
                }
            }
            /*生成下拉框树*/
            bxtree.initTreeInput(bxtree, $dataConfigView);
            var ccsCfgInput = bxtree.getConfigViewsControl("ccsCfg", $dataConfigView);
            if (isAvailable(bxtree.getOptions()["ccsCfg"])) {
                ccsCfgInput.val(bxtree.getOptions()["ccsCfg"]);
                if(! DesignerUtils.checkComboTreeValue(ccsCfgInput.val())){
                    ccsCfgInput.val('');
                    bxtree.setOption("ccsCfg", ccsCfgInput.val());
                }
            }
            $.each(attributeList, function (index, item) {
                /*$(this).val(bxtree.getOptions()[$(this).data("attribute")]);*/
                bxtree.setAttributeFromOption(bxtree.getOptions(), $(this));
                if ($(this).data("attribute") == "dataPattern") {
                    if ($(this).val() == "ccs") {
                        bxtree.getConfigViewsControl("ccsIdDiv", $dataConfigView).show();
                        bxtree.getConfigViewsControl("localDiv", $dataConfigView).hide();
                    } else {
                        bxtree.getConfigViewsControl("ccsIdDiv", $dataConfigView).hide();
                        bxtree.getConfigViewsControl("localDiv", $dataConfigView).show();
                    }
                }

                $(this).change(function () {
                    if ($(this).data("attribute") == "dataPattern") {
                        if ($(this).val() == "ccs") {
                            bxtree.getConfigViewsControl("ccsIdDiv", $dataConfigView).show();
                            bxtree.getConfigViewsControl("localDiv", $dataConfigView).hide();
                            bxtree.getOptions()["option"].plugins.splice(bxtree.getOptions()["option"].plugins.indexOf("contextmenu"), 1);
                            bxtree.getOptions()["selectNode"].enable=false;
                        } else {
                            bxtree.getConfigViewsControl("ccsIdDiv", $dataConfigView).hide();
                            bxtree.getConfigViewsControl("localDiv", $dataConfigView).show();
                            bxtree.getOptions()["option"].plugins.push("contextmenu");
                            bxtree.getConfigViewsControl("treeValue", $dataConfigView).val("");
                            bxtree.getConfigViewsControl("treeLable", $dataConfigView).val("");
                            bxtree.getOptions()["selectNode"].enable=true;
                        }
                    }
                    if ($(this).data("attribute") == "id") {
                        if ($("#" + bxtree.getId()).bxtree("getSelected").length == 1) {
                            if ($("#" + bxtree.getId()).bxtree("getSelected")[0].parent == "#") {
                                alert("树根节点的值不可编辑");
                                bxtree.getConfigViewsControl("treeValue", $dataConfigView).val(bxtree.getOptions()["selectNode"].id);
                                return;
                            }
                            var oldValue = $("#" + bxtree.getId()).bxtree("getSelected")[0].id;
                            unique(bxtree.getConfigViewsControl("treeValue", $dataConfigView).val(), bxtree.getOptions()["data"]);
                            if (success == 0) {
                                alert("树节点的值不可以重复");
                                bxtree.getConfigViewsControl("treeValue", $dataConfigView).val(oldValue);
                                success = 1;
                            }
                            else {
                                reValue(oldValue, bxtree.getConfigViewsControl("treeValue", $dataConfigView).val(), bxtree.getOptions()["data"]);
                            }
                        }else if($("#" + bxtree.getId()).bxtree("getSelected").length < 1){
                            alert("请选择一个树节点，再进行修改");
                            bxtree.getConfigViewsControl("treeValue", $dataConfigView).val(bxtree.getOptions()["selectNode"].id);
                        }else {
                            alert("修改树节点有且只能唯一,如需配置“复选框”，请修改完成后再配置");
                            bxtree.getConfigViewsControl("treeValue", $dataConfigView).val(bxtree.getOptions()["selectNode"].id);
                        }

                    }
                    if ($(this).data("attribute") == "text") {
                        if ($("#" + bxtree.getId()).bxtree("getSelected").length == 1) {
                            if ($("#" + bxtree.getId()).bxtree("getSelected")[0].parent == "#") {
                                bxtree.getOptions()["showText"] = bxtree.getConfigViewsControl("treeLable", $dataConfigView).val();
                            } else {
                                rename($("#" + bxtree.getId()).bxtree("getSelected")[0].id, bxtree.getConfigViewsControl("treeLable", $dataConfigView).val(), bxtree.getOptions()["data"]);
                            }
                        }else if($("#" + bxtree.getId()).bxtree("getSelected").length < 1){
                            alert("请选择一个树节点，再进行修改");
                            bxtree.getConfigViewsControl("treeLable", $dataConfigView).val(bxtree.getOptions()["selectNode"].text);
                        }
                        else {
                            alert("修改树节点有且只能唯一,如需配置“复选框”，请修改完成后再配置");
                            bxtree.getConfigViewsControl("treeLable", $dataConfigView).val(bxtree.getOptions()["selectNode"].text);
                        }
                    }

                    bxtree.setOption($(this).data("attribute"), $(this).val());
                    bxtree.refresh();
                });
            });
        },
        prepareOption: function (){
            var option = this.getOptions();
            var ccsId = DesignerUtils.queryViaComboTree(option.ccsCfg);
            option.ccsId = ccsId;
            return this.getOptions();
        },
        initTreeInput:function (bxtree, $dataConfigView) {
            var ccsObj = [];
            $.each(dataSourceditor.getDSOptions(), function (key, value) {
                switch (value.ds_type) {
                    case "ccs":
                        ccsObj.push({value: "*{" + key + "}", label: key});
                        break;
                }
            });

            var ccsTreeData = [{
                label: "ccs数据源",
                state: {opened: true},
                value: "<无可用的ccs配制>",
                children: ccsObj
            }];
            treeInputUtils.createTreeInput(bxtree.getConfigViewsControl("ccsCfg", $dataConfigView), ccsTreeData);
        },
        bindCore: function (eventname, callback) {
            var $this = this;
            if (this._properties.actionsToEvent[eventname]) {
                require(["bxtree"], function () {
                    $($this.getView()).find("#" + $this.getId()).on($this._properties.actionsToEvent[eventname], function (event, obj) {
                        callback.call(arguments);
                    });
                });
            }
        }
    };

    Widget.extend(type, bxTree);
})(WidgetObject, "bxtree");
