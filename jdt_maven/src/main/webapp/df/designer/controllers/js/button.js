(function (WidgetObj, type) {
    function Button() { // html节点的操作对象的定位
    }

    var actions = {
        "type": type,
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

    function showButton(widget, id) {
        var $button = $("#" + id);
        $button.children('span').html('  ' + widget.getOption("showText"));
        $button.children('i').attr('class', 'ace-icon fa ' + (widget.getOption("btnIcon")));
        var option = widget.getOption("option");
        for (key in option) {
            $button.attr(key, option[key]);
        }
    }

    Button.prototype = new WidgetObj(type, actions);

    Button.prototype.initOption = function () {
        var option = {
            showText: "按钮",
            btnIcon: "fa-search",
            option: {
                'class': "btn btn-sm btn-block",
                'style': ""
            }
        };
        this.setOptions(option);
    };

    Button.prototype.showPreviewCore = function () {
        showButton(this, this.getPreviewId());
    };

    Button.prototype.showCore = function () {
        showButton(this, this.getId());
    };

    Button.prototype.getConfigViewCore = function ($configView) {
        var bxbutton = this;
        var attributeList = bxbutton.getConfigViewsControl("attribute", $configView).find("[data-attribute]");
        var widgetOpt = bxbutton.getOptions()["option"];
        bxbutton.getConfigViewsControl("buttonId", $configView).text(bxbutton.getId());
        var showTextArribute = bxbutton.getConfigViewsControl("showText", $configView);
        showTextArribute.val(bxbutton.getOptions()["showText"]);
        showTextArribute.change(function () {
            bxbutton.setOption("showText", showTextArribute.val());
            bxbutton.refresh();
        });
        var btnIconArribute = bxbutton.getConfigViewsControl("btnIcon", $configView);
        btnIconArribute.val(bxbutton.getOptions()["btnIcon"]);
        btnIconArribute.change(function () {
            bxbutton.setOption("btnIcon", btnIconArribute.val());
            bxbutton.refresh();
        });
        $.each(attributeList, function (index, item) {
            bxbutton.setAttributeFromOption(widgetOpt, $(this));
            $(this).change(function () {
                bxbutton.setOptionValue(widgetOpt, $(this));
                bxbutton.setOption("option", widgetOpt);
                bxbutton.refresh();
            });
        });
    };

    Button.prototype.getDataConfigViewCore = function ($dataConfigView) {

    };

    Button.prototype.bindCore = function (eventname, callback) {
        if (eventname != "Init")
            $("#" + this.getId()).attr(eventname, callback.name + '();');
    };
    Widget.registerType(type, Button);
})(WidgetObject, "button");