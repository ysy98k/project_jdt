(function (WidgetObj, type) {
    function BxProgressBar() { // html节点的操作对象的定位
    }

    var actions = {};
    var createProgressBar = function (event, ui) {
        $(this).addClass('progress progress-striped active')
            .children(0).addClass('progress-bar progress-bar-skin').removeClass('ui-widget-header');
    };

    function show(widget, id) {
        var controlObj = $("#" + id);
        var options = widget.getOptions();
        var $label = controlObj.parent().find("label");
        $label.text(options["labelText"]);
        $label.width(options["labelWidth"]);
        options.option.value = parseFloat(DesignerUtils.queryViaComboTree(options["progressbarDsSetting"]));
        require(["bxprogressbar"], function () {
            controlObj.bxprogressbar(options);
        });
    }

    BxProgressBar.prototype = new BxWidgetCommon(type, actions);

    BxProgressBar.prototype.initOption = function () {
        var bxprogressbar = this;
        var options = {
            labelText: '标签',
            labelWidth: '',
            progressbarDsSetting: "28",
            option: {
                value: 0
            }
        };
        bxprogressbar.setOptions(options);
    };
    BxProgressBar.prototype.getValue = function (id) {
        return $("#" + this.getId()).attr("aria-valuenow");
    };

    BxProgressBar.prototype.showPreviewCore = function () {
        show(this, this.getPreviewId());
    };

    BxProgressBar.prototype.showCore = function () {
        show(this, this.getId());
    };

    BxProgressBar.prototype.getConfigViewCore = function ($configView) {
        var bxprogressbar = this;
        var bxlabel = $("#" + bxprogressbar.getId()).parent().find("label");
        bxprogressbar.getConfigViewsControl("widgetId", $configView).text(bxprogressbar.getId());
        /*设置标签文字*/
        var labelText = bxprogressbar.getConfigViewsControl("labelText", $configView);
        labelText.val(bxprogressbar.getOptions()["labelText"]);
        labelText.change(function () {
            bxprogressbar.getOptions()["labelText"] = labelText.val();
            bxprogressbar.refresh();
            bxprogressbar.getConfigViewsControl("labelWidth", $configView).val(bxlabel.width());
        });

        /*设置标签宽度*/
        var labelWidth = bxprogressbar.getConfigViewsControl("labelWidth", $configView);
        labelWidth.val(bxlabel.width());
        labelWidth.change(function () {
            if ($("div[data-id='" + bxprogressbar.getId() + "']").width() - $(this).val() - 12 < 0) {
                alert("标签宽度不可大于进度条控件当前宽度(" + ($("div[data-id='" + bxprogressbar.getId() + "']").width() - 12) + ")");
            }
            else {
                bxprogressbar.getOptions().labelWidth = $(this).val();
                bxprogressbar.refresh();
            }
            bxprogressbar.getConfigViewsControl("labelWidth", $configView).val(bxlabel.width());
        });

    };

    BxProgressBar.prototype.getDataConfigViewCore = function ($dataConfigView) {
        var bxprogressbar = this;
        $dataConfigView.find(".treeInput_arround").remove();
        bxprogressbar.initCombTrees($dataConfigView);
        var bxprogressbarDatasource = bxprogressbar.getConfigViewsControl("progressbarDsSetting", $dataConfigView);
        bxprogressbarDatasource.change(function () {
            bxprogressbar.dataSourceExecute($dataConfigView);
            bxprogressbar.refresh();
        });
        bxprogressbarDatasource.val(bxprogressbar.getOptions()["progressbarDsSetting"]);
    };

    BxProgressBar.prototype.bindCore = function (eventname, callback) {

    };

    BxProgressBar.prototype.dataSourceExecute = function ($dataConfigView) {
        var bxprogressbar = this;
        var bxprogressbarDatasource = bxprogressbar.getConfigViewsControl("progressbarDsSetting", $dataConfigView).val();
        if (!DesignerUtils.checkComboTreeValue(bxprogressbarDatasource)) bxprogressbarDatasource = "";
        bxprogressbar.getOptions()["progressbarDsSetting"] = bxprogressbarDatasource;
        bxprogressbar.getOptions()["option"].value = parseFloat(DesignerUtils.queryViaComboTree(bxprogressbar.getOptions()["progressbarDsSetting"]));
    };

    Widget.registerType(type, BxProgressBar);
})(WidgetObject, "bxprogressbar");