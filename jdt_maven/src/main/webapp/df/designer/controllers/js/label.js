(function (WidgetObj, type) {
    function Label() { // html节点的操作对象的定位
    }

    var actions = {
        "type": "label",
        "actions": [
            {
                "name": "OnClick",
                "params": "()",
                "tooltip": "控件上发生鼠标点击时触发"
            },
            {
                "name": "OnDblClick",
                "params": "()",
                "tooltip": "在控件上发生鼠标双击时触发"
            },
            {
                "name": "OnMouseOver",
                "params": "()",
                "tooltip": "当鼠标指针移动到控件上时触发"
            },
            {
                "name": "OnMouseOut",
                "params": "()",
                "tooltip": "当鼠标指针移动到出控件时触发"
            }]
    };

    function showLabel(widget, id) {
        var $Label = $("#" + id);
        $Label.width(widget.getOptions()["width"]);
        var labelText = DesignerUtils.queryViaComboTree(widget.getOption("labelDsSetting"));
        $Label.text(labelText);
        if(isAvailable(widget.getOption("option").style)){
            $Label.css(widget.getOption("option").style);
        }
    }

    Label.prototype = new BxWidgetCommon(type, actions);

    Label.prototype.initOption = function () {
        var option = {
            labelDsSetting: "标签",
            option: {
                "labelText": "",
                "style": {
                    "color": "#393939",
                    "text-align": "left"
                }
            }
        };
        this.setOptions(option);
        this.showPreviewCore();
    };

    Label.prototype.getValue = function () {
        return $("#" + this.getId()).text();
    };

    Label.prototype.showPreviewCore = function () {
        showLabel(this, this.getPreviewId());
    };

    Label.prototype.showCore = function () {
        showLabel(this, this.getId());
    };

    Label.prototype.getConfigViewCore = function ($configView) {
        var $this = this;
        var labelOpt = $this.getOptions()["option"];
        $this.getConfigViewsControl("labelId", $configView).text($this.getId());
        /*设置input宽度*/
        var widthArribute = $this.getConfigViewsControl("width", $configView);
        widthArribute.val($("#" + $this.getId()).width());
        widthArribute.change(function () {
            $this.getOptions().width = $(this).val();
            $this.refresh();
        });

        var attributeList = $this.getConfigViewsControl("attribute", $configView).find("[data-attribute]");
        $.each(attributeList, function (index, item) {
            $this.setAttributeFromOption(labelOpt, $(this));
            $(this).change(function () {
                $this.setOptionValue(labelOpt, $(this));
                $this.setOption("option", labelOpt);
                $this.refresh();
            });
        });

        $this.getFontStyle($this, $configView);
        var $fontColorInput = $this.getConfigViewsControl("fontColor", $configView);
        if(isAvailable(labelOpt.style)){
            $fontColorInput.colorpicker({color: labelOpt.style.color});
        }
        var styleList = $this.getConfigViewsControl("styleAttr", $configView).find("[data-attribute]");
        $.each(styleList, function (index, item) {
            $this.setAttributeFromOption(labelOpt, $(this));
            if (labelOpt.style[$(this).data("attribute")] == "继承") {
                delete labelOpt.style[$(this).data("attribute")];
            }
            $(this).change(function () {
                $this.setOptionValue(labelOpt, $(this));
                $this.setOption("option", labelOpt);
                if ($(this).val() == "none") {
                    delete labelOpt.style[$(this).data("attribute")];
                    $("#" + $this.getId()).attr("style", "width:100%");
                }
                $("#" + $this.getId()).css(labelOpt.style);
                $this.refresh();
            });
        });
        $("#" + $this.getId()).css(labelOpt.style);
    };

    Label.prototype.getDataConfigViewCore = function ($dataConfigView) {
        var label = this;
        $dataConfigView.find(".treeInput_arround").remove();
        label.initCombTrees($dataConfigView);
        var labelDatasource = label.getConfigViewsControl("labelDsSetting", $dataConfigView);
        labelDatasource.change(function () {
            label.dataSourceExecute($dataConfigView);
            label.refresh();
        });
        labelDatasource.val(label.getOptions()["labelDsSetting"]);
    };

    Label.prototype.bindCore = function (eventname, callback) {
        if (eventname != "Init")
            $("#" + this.getId()).attr(eventname, callback.name + '();');
    };

    Label.prototype.dataSourceExecute = function ($dataConfigView) {
        var label = this;
        var labelDatasource = label.getConfigViewsControl("labelDsSetting", $dataConfigView).val();
        if(! DesignerUtils.checkComboTreeValue(labelDatasource)) labelDatasource = "";
        label.getOptions()["labelDsSetting"] = labelDatasource;
    };

    Widget.registerType(type, Label);
})(WidgetObject, "label");