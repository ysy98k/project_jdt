(function (WidgetObj, type) {
    function textarea() { // html节点的操作对象的定位
    }

    var actions = {
        "type": "textarea",
        "actions": [
            {
                "name": "OnFocus",
                "params": "()",
                "tooltip": "获得焦点事件"
            }, {
                "name": "OnKeyDown",
                "params": "()",
                "tooltip": "在输入框中按下键盘时触发"
            }
        ]
    };

    textarea.prototype = new BxWidgetCommon(type, actions);

    textarea.prototype.initOption = function () {
        var option = {
            css: {
                width: "100%",
                height: "55px"
            },
            attr: {},
            resize: false,
            txaDs: ""
        };
        this.setOptions(option);
    };

    textarea.prototype.getValue = function () {
        return $("#" + this.getId()).val();
    };

    function showTxa(widget) {
        var $textarea = $("#" + widget.getId());
        
        var attr = widget.getOption("attr");
        for (var key in attr) {
            $textarea.attr(key, attr[key]);
        }
        
        var css = widget.getOption("css");
        css["resize"] = widget.getOption("resize") ? "vertical" : "none";
        for (var key in css) {
            $textarea.css(key, css[key])
        }
        
        $textarea.val(DesignerUtils.queryViaComboTree(widget.getOption("txaDs")));
    }

    textarea.prototype.showPreviewCore = function () {
        showTxa(this);
    };

    textarea.prototype.showCore = function () {
        showTxa(this);
    };

    textarea.prototype.getConfigViewCore = function ($configView) {
        var $this = this;
        var txa_Opt = $this.getOptions();
        $this.getConfigViewsControl("txa_Id", $configView).text($this.getPreviewId());

        /*attribute*/
        var attributeList = $this.getConfigViewsControl("attribute", $configView).find("[data-attribute]");
        $.each(attributeList, function (index, item) {
            $this.setAttributeFromOption(txa_Opt, $(this));
            $(this).change(function () {
                $this.setOptionValue(txa_Opt, $(this));
                $this.refresh();
            });
        });

    };

    textarea.prototype.getDataConfigViewCore = function ($dataConfigView) {
        var $this = this;
        if(!DesignerUtils.checkComboTreeValue($this.getOption("txaDs")))
            $this.setOption("txaDs", "");

        $dataConfigView.find(".treeInput_arround").remove();
        $this.initCombTrees($dataConfigView);
        var txaDs = $this.getConfigViewsControl("txa_Ds", $dataConfigView);
        txaDs.val($this.getOption("txaDs")).change(function () {
            $this.setOption("txaDs", $(this).val());
            $this.refresh();
        });
    };

    textarea.prototype.bindCore = function (eventname, callback) {
        if (eventname != "Init")
            $('#' + this.getId()).attr(eventname, callback.name + '();');
    };

    Widget.registerType(type, textarea);
})(WidgetObject, "textarea");