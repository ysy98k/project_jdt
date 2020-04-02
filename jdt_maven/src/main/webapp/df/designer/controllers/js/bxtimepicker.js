(function (WidgetObj, type) {
    function Bxtimepicker() { // html节点的操作对象的定位
    }

    var actions = {};

    function show(widget, id) {
        var controlObj = $("#" + id);
        var $label = controlObj.parent().find("label");
        $label.text(widget.getOptions()["labelText"]);
        $label.width(widget.getOptions()["labelWidth"]);
        var options = widget.getOptions();
        $("#" + id).bxtimepicker(options);
    }

    Bxtimepicker.prototype = new BxWidgetCommon(type, actions);

    Bxtimepicker.prototype.initOption = function () {
        var option = {
            skin: 'default',
            dateFmt: 'yyyy-MM-dd HH:mm:ss',
            isShowClear: true,
            timepickerDsSetting: ""
        };
        this.setOptions({
            labelText: '标签',
            timeOption: option
        });
    };

    Bxtimepicker.prototype.getValue = function () {
        return $("#" + this.getId() + "_dateInput").val();
    };

    Bxtimepicker.prototype.showPreviewCore = function () {
        show(this, this.getPreviewId());
    };

    Bxtimepicker.prototype.showCore = function () {
        show(this, this.getId());
    };

    Bxtimepicker.prototype.getConfigViewCore = function ($configView) {
        var bxtimepicker = this;
        var bxlabel = $("#" + bxtimepicker.getId()).parent().find("label");
        var widgetOpt = bxtimepicker.getOptions()["timeOption"];
        bxtimepicker.getConfigViewsControl("widgetId", $configView).text(bxtimepicker.getId());

        /*设置标签文字*/
        var labelText = bxtimepicker.getConfigViewsControl("labelText", $configView);
        labelText.val(bxtimepicker.getOptions()["labelText"]);
        labelText.change(function () {
            bxtimepicker.getOptions()["labelText"] = labelText.val();
            bxtimepicker.refresh();
            bxtimepicker.getConfigViewsControl("labelWidth", $configView).val(bxlabel.width());
        });
        /*设置标签宽度*/
        var labelWidth = bxtimepicker.getConfigViewsControl("labelWidth", $configView);
        labelWidth.val(bxlabel.width());
        labelWidth.change(function () {
            bxtimepicker.getOptions().labelWidth = $(this).val();
            bxtimepicker.refresh();
        });
        var allAtttibute = bxtimepicker.getConfigViewsControl("attribute", $configView).find("[data-level]");
        $.each(allAtttibute, function (index, item) {
            bxtimepicker.setAttributeFromOption(widgetOpt, $(this));
            $(this).change(function () {
                bxtimepicker.setOptionValue(widgetOpt, $(this));
                bxtimepicker.setOption("timeOption", widgetOpt);
                bxtimepicker.refresh();
            });
        });
    };

    Bxtimepicker.prototype.getDataConfigViewCore = function ($dataConfigView) {
    };

    Bxtimepicker.prototype.bindCore = function (eventname, callback) {

    };

    Widget.registerType(type, Bxtimepicker);
})(WidgetObject, "bxtimepicker");