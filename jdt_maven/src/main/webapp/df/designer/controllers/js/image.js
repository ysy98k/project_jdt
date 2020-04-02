/**
 * Created by chenxi on 2017/11/14.
 */
(function (WidgetObj, type) {
    function Image() { // html节点的操作对象的定位
    }

    var actions = {
        "type": "image",
        "actions": [
            {
                "name": "OnClick",
                "params": "()",
                "tooltip": "控件上发生鼠标点击时触发"
            }
        ]
    };

    Image.prototype = new BxWidgetCommon(type, actions);

    Image.prototype.initOption = function () {
        var option = {
            option: {
                height: "",
                width: ""
            },
            align: "center",
            src: "./data/default_picture.jpg"
        };
        this.setOptions(option);
    };

    function showImage(widget, id) {
        var $image = $("#" + id);
        $image.get(0).src = widget.getOption("src");
        var option = widget.getOption("option");
        for (var key in option) {
            $image.css(key, option[key]);
        }
        var align = widget.getOption("align") || "center";
        $image.parent().css("text-align", align);
    }

    Image.prototype.showPreviewCore = function () {
        showImage(this, this.getPreviewId());
    };

    Image.prototype.showCore = function () {
        showImage(this, this.getId());
    };

    Image.prototype.getConfigViewCore = function ($configView) {
        var $this = this;
        var imgId = $this.getId();
        var imgOpt = $this.getOption("option");
        $this.getConfigViewsControl("widgetId", $configView).text(imgId);

        //图片属性
        $this.getConfigViewsControl("imgWidth", $configView).val(imgOpt.width || "auto")
            .change(function () {
                if(parseInt($(this).val()) <5) $(this).val("5%");
                imgOpt.width = $(this).val() == "auto" ? "" : $(this).val();
                $this.refresh();
            });

        $this.getConfigViewsControl("imgHeight", $configView).val(imgOpt.height || "auto")
            .change(function () {
                if(parseInt($(this).val()) <5) $(this).val("5%");
                imgOpt.height = $(this).val() == "auto" ? "" : $(this).val();
                $this.refresh();
            });

        $this.getConfigViewsControl("align", $configView).val($this.getOption("align"))
            .change(function () {
                $this.setOption("align", $(this).val());
                $this.refresh();
            });
    };

    Image.prototype.getDataConfigViewCore = function ($dataConfigView) {
        var $this = this;
        $dataConfigView.find(".treeInput_arround").remove();
        $this.initCombTrees($dataConfigView);
        $this.getConfigViewsControl("src", $dataConfigView).val($this.getOption("src"))
            .change(function () {
                $this.setOption("src", $(this).val());
                $this.refresh();
            });
    };

    Image.prototype.bindCore = function (eventname, callback) {
        if (eventname != "Init")
            $('#' + this.getId()).attr(eventname, callback.name + '();');
    };

    // overwrite
    Image.prototype.initCombTrees = function ($configView) {
        var imgObj = DesignerUtils.getMediaList("picture");
        $configView.find(".treeInput").each(function () {
            treeInputUtils.createTreeInput($(this), imgObj);
        });
    };


    Widget.registerType(type, Image);
})(WidgetObject, "image");