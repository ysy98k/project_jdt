(function (WidgetObj, type) {
    function BxComboBox() {
    }

    var actions = {
        "type": "bxcombobox",
        "actions": [
            {
                "name": "OnChange",
                "params": "()",
                "tooltip": "选项变化事件"
            }
        ]
    };

    BxComboBox.prototype = new BxWidgetCommon(type, actions);

    BxComboBox.prototype.initOption = function () {
        var bxcombobox = this;
        var optionSubId = "bxcombobox" + DesignerUtils.randomNumber();
        var optionId = bxcombobox.getId() + "$$" + optionSubId;
        var dataObj = [
            {
                'id': optionId,
                'value': 'value',
                'label': '选项',
                'subId': optionSubId
            }
        ];
        this.setOptions({
            labelText: '标签',
            ccsCfg: '',
            sqlValue: '',
            sqlLabel: '',
            labelWidth: '',
            dataPattern: 'local',
            data: dataObj,
            async: false,
            option:{
                multiple:false
            },
            sqldata: {
                label: '',
                value: ''
            }
        });
    };

    BxComboBox.prototype.getValue = function () {
        return $("#" + this.getId() + "_select").val();
    };

    function show(widget, id) {
        var controlObj = $("#" + id);
        var $label = controlObj.parent().parent().find("label");
        $label.text(widget.getOptions()["labelText"]);
        $label.width(widget.getOptions()["labelWidth"]);
        var options = widget.getOptions();
        if (options.dataPattern != "local") {
            options = widget.prepareOption(options.dataPattern);
        }
        $("#" + id).bxcombobox(options);
    };

    BxComboBox.prototype.showPreviewCore = function () {
        show(this, this.getPreviewId());
    };

    BxComboBox.prototype.showCore = function () {
        show(this, this.getId());
    };

    BxComboBox.prototype.getConfigViewCore = function ($configView) {
        var bxcombobox = this;
        var widgetOpt = bxcombobox.getOptions();
        bxcombobox.getConfigViewsControl("selectId", $configView).text(bxcombobox.getId());
        var bxlabel = $("#" + bxcombobox.getId()).parent().parent().find("label");
        /*设置标签文字*/
        var labelText = bxcombobox.getConfigViewsControl("labelText", $configView);
        labelText.val(bxcombobox.getOptions()["labelText"]);
        labelText.change(function () {
            bxcombobox.getOptions().labelText = labelText.val();
            bxcombobox.refresh();
            bxcombobox.getConfigViewsControl("labelWidth", $configView).val(bxlabel.width());
        });

        /*设置宽度*/
        var labelWidth = bxcombobox.getConfigViewsControl("labelWidth", $configView);
        labelWidth.val(bxlabel.width());
        labelWidth.change(function () {
            bxcombobox.getOptions().labelWidth = $(this).val();
            bxcombobox.refresh();
        });

        var multiple = bxcombobox.getConfigViewsControl("multiple", $configView);
        multiple.prop("checked", widgetOpt.option.multiple);
        multiple.change(function () {
            bxcombobox.setOptionValue(widgetOpt.option, $(this));
            bxcombobox.setOption("option", widgetOpt.option);
            bxcombobox.refresh();
        });
    };

    BxComboBox.prototype.getDataConfigViewCore = function ($dataConfigView) {
        var bxcombobox = this;
        var widgetOpt = bxcombobox.getOptions();
        bxcombobox.getConfigViewsControl("selectId", $dataConfigView).text(bxcombobox.getId());
        var dataPatternInput = bxcombobox.getConfigViewsControl("dataPattern", $dataConfigView);
        var dataPattern = bxcombobox.getOptions()[dataPatternInput.data("attribute")];
        dataPatternInput.val(dataPattern);
        //显示当前数据来源
        bxcombobox.getConfigViewsControl("localDiv", $dataConfigView).hide();
        bxcombobox.getConfigViewsControl("sqlDiv", $dataConfigView).hide();
        bxcombobox.getConfigViewsControl("ccsDiv", $dataConfigView).hide();
        bxcombobox.getConfigViewsControl(dataPattern + "Div", $dataConfigView).show();
        dataPatternInput.unbind().change(function () {
            var curDataPattern = $(this).val();
            bxcombobox.getConfigViewsControl("localDiv", $dataConfigView).hide();
            bxcombobox.getConfigViewsControl("sqlDiv", $dataConfigView).hide();
            bxcombobox.getConfigViewsControl("ccsDiv", $dataConfigView).hide();
            bxcombobox.getConfigViewsControl(curDataPattern + "Div", $dataConfigView).show();
            bxcombobox.setOption(dataPatternInput.data("attribute"), $(this).val());
            bxcombobox.refresh();
        });
        /*生成下拉框树*/
        BxComboBox.prototype.initTreeInput(bxcombobox, $dataConfigView);

        /*设置local选项*/
        bxcombobox.initOptionBtn($dataConfigView);
        bxcombobox.getConfigViewsControl("addDataBtn", $dataConfigView).unbind().bind("click", function () {
            bxcombobox.addElement($dataConfigView);
            bxcombobox.refresh();
        });
        /*设置ccsId的值*/
        var ccsCfgInput = bxcombobox.getConfigViewsControl("ccsCfg", $dataConfigView);
        if (isAvailable(bxcombobox.getOptions()["ccsCfg"])) {
            ccsCfgInput.val(bxcombobox.getOptions()["ccsCfg"]);
            if(! DesignerUtils.checkComboTreeValue(ccsCfgInput.val())){
                ccsCfgInput.val('');
                bxcombobox.setOption("ccsCfg", ccsCfgInput.val());
            }
        }
        ccsCfgInput.change(function () {
            bxcombobox.setOption("ccsCfg", $(this).val());
            bxcombobox.refresh();
        });
        /*设置sql的值*/
        var sqlInputlist = bxcombobox.getConfigViewsControl("sqlDiv", $dataConfigView).find("[data-attribute]");
        $.each(sqlInputlist, function (index, item) {
            bxcombobox.setAttributeFromOption(widgetOpt, $(this));
            if(! DesignerUtils.checkComboTreeValue($(this).val())){
                $(this).val('');
                bxcombobox.setOptionValue(widgetOpt, $(this));
                bxcombobox.setOptions(widgetOpt);
            }
            $(this).change(function () {
                bxcombobox.setOptionValue(widgetOpt, $(this));
                bxcombobox.setOptions(widgetOpt);
                bxcombobox.refresh();
            });
        });
    };

    BxComboBox.prototype.prepareOption = function (type) {
        var option = this.getOptions();
        if (type == "ccs") {
            var ccsId = DesignerUtils.queryViaComboTree(option.ccsCfg);
            option.ccsId = ccsId;
        } else if (type == "sql") {
            var label = DesignerUtils.queryViaComboTree(option.sqlLabel);
            var value = DesignerUtils.queryViaComboTree(option.sqlValue);
            option.sqldata.label = label;
            option.sqldata.value = value;
        }

        return this.getOptions();
    };

    BxComboBox.prototype.initTreeInput = function (bxcombobox, $dataConfigView) {
        var sqlObj = [], ccsObj = [];
        $.each(dataSourceditor.getDSOptions(), function (key, value) {
            switch (value.ds_type) {
                case "sql":
                    var sqlOpt = value.options;
                    var column = [];
                    var sqlHead = "${" + key + ".";
                    $.each(sqlOpt.sqlCol.split(","), function (index, item) {
                        if (item != "")
                            column.push({value: sqlHead + item + "}", label: item});
                        else
                            column = null;
                    });
                    var sqlObjVal;
                    if (column != null) {
                        sqlObj.push({
                            label: key,
                            value: sqlObjVal,
                            state: {opened: false},
                            children: column
                        });
                    }

                    break;
                case "ccs":
                    ccsObj.push({value: "*{" + key + "}", label: key});
                    break;
            }
        });

        var sqlTreeData = [{
            label: "sql数据源",
            state: {opened: true},
            value: "<无可用的sql配制>",
            children: sqlObj
        }];
        var ccsTreeData = [{
            label: "ccs数据源",
            state: {opened: true},
            value: "<无可用的ccs配制>",
            children: ccsObj
        }];
        treeInputUtils.createTreeInput(bxcombobox.getConfigViewsControl("sqlLabel", $dataConfigView), sqlTreeData);
        treeInputUtils.createTreeInput(bxcombobox.getConfigViewsControl("sqlValue", $dataConfigView), sqlTreeData);
        treeInputUtils.createTreeInput(bxcombobox.getConfigViewsControl("ccsCfg", $dataConfigView), ccsTreeData);
    };


    BxComboBox.prototype.initOptionBtn = function ($dataConfigView) {//初始化增加值的按钮区域
        var bxcombobox = this;
        var optionList = bxcombobox.getOptions()["data"];
        var addDataBtn = bxcombobox.getConfigViewsControl("addDataBtn", $dataConfigView).parent();
        var optionListArray = [];
        for (var i = 0; i < optionList.length; i++) {
            optionListArray.push(optionList[i]);
            bxcombobox.generateOptionBtn($dataConfigView, optionList[i]);
        }
        bxcombobox.getConfigViewsControl("optionList", $dataConfigView).val(JSON.stringify(optionListArray));
    };


    BxComboBox.prototype.generateOptionBtn = function ($dataConfigView, generateObj) {//封装生成按钮的代码
        var bxcombobox = this;
        var addDataBtn = bxcombobox.getConfigViewsControl("addDataBtn", $dataConfigView).parent();
        var bxComboBoxOptButton = "<div data-attribute='bxComboBoxOption' class='col-sm-12 btn btn-xs btn-yellow' id='" + generateObj.id + "' style='margin:0 15px 5px 15px;width: 86.9%'><span>" + generateObj.label + ":" + generateObj.value + "</span><i class='ace-icon fa fa-times pull-right'data-dismiss='alert'style='margin-top:3px'></i></div>"
        addDataBtn.before(bxComboBoxOptButton);
        bxcombobox.bindOptionBtnDeleteClick(generateObj.subId, $dataConfigView);
        bxcombobox.bindOptionBtnClick(generateObj.subId, $dataConfigView);
    };

    BxComboBox.prototype.bindOptionBtnDeleteClick = function (optionSubId, $dataConfigView) {//封装点击按钮删除的代码
        var bxcombobox = this;
        bxcombobox.getConfigViewsControl(optionSubId, $dataConfigView).children("i").unbind().bind("click", function () {// 绑定删除选项按钮事件
            var optionListArray = JSON.parse(bxcombobox.getConfigViewsControl("optionList", $dataConfigView).val());
            bxcombobox.getConfigViewsControl(optionSubId, $dataConfigView).unbind();
            bxcombobox.deleteElement(optionSubId, optionListArray, $dataConfigView);
        });
    };

    BxComboBox.prototype.bindOptionBtnClick = function (optionSubId, $dataConfigView) {//封装点击按钮的代码
        var bxcombobox = this;
        bxcombobox.getConfigViewsControl(optionSubId, $dataConfigView).unbind().bind("click", function () {// 绑定点击按钮事件
            var button = $(this);
            var optionListArray = JSON.parse(bxcombobox.getConfigViewsControl("optionList", $dataConfigView).val());
            var index = bxcombobox.functiontofindIndexByKeyValue(optionListArray, 'id', bxcombobox.getId() + "$$" + optionSubId);
            var clickOpt = optionListArray[index];
            bxcombobox.getConfigViewsControl("optionName", $dataConfigView).val(clickOpt.label);
            bxcombobox.getConfigViewsControl("optionName", $dataConfigView).unbind().change(function () {
                var curOptionListArray = JSON.parse(bxcombobox.getConfigViewsControl("optionList", $dataConfigView).val());
                curOptionListArray[index].label = $(this).val();
                button.find(">span").text(curOptionListArray[index].label + ":" + curOptionListArray[index].value);
                bxcombobox.getOptions()["data"] = curOptionListArray;
                bxcombobox.getConfigViewsControl("optionList", $dataConfigView).val(JSON.stringify(curOptionListArray));
                bxcombobox.refresh();
            });
            bxcombobox.getConfigViewsControl("optionValue", $dataConfigView).val(clickOpt.value);
            bxcombobox.getConfigViewsControl("optionValue", $dataConfigView).unbind().change(function () {
                var curOptionListArray = JSON.parse(bxcombobox.getConfigViewsControl("optionList", $dataConfigView).val());
                curOptionListArray[index].value = $(this).val();
                button.find(">span").text(curOptionListArray[index].label + ":" + curOptionListArray[index].value);
                bxcombobox.getOptions()["data"] = curOptionListArray;
                bxcombobox.getConfigViewsControl("optionList", $dataConfigView).val(JSON.stringify(curOptionListArray));
                bxcombobox.refresh();
            });
            bxcombobox.getConfigViewsControl("optionAttribute", $dataConfigView).show();
        });
    };

    BxComboBox.prototype.addElement = function ($dataConfigView) {// 增加元素
        var bxcombobox = this;
        var optionSubId = "option" + DesignerUtils.randomNumber();
        var optionId = bxcombobox.getId() + "$$" + optionSubId;
        var optionListArray = JSON.parse(bxcombobox.getConfigViewsControl("optionList", $dataConfigView).val());
        var generateObj = {
            'id': optionId,
            'label': '选项',
            'value': 'value',
            'subId': optionSubId
        };
        bxcombobox.generateOptionBtn($dataConfigView, generateObj);
        optionListArray.push(generateObj);
        bxcombobox.getConfigViewsControl("optionList", $dataConfigView).val(JSON.stringify(optionListArray));
        bxcombobox.getOptions()["data"] = optionListArray;
        bxcombobox.refresh();
        bxcombobox.getConfigViewsControl(generateObj.subId, $dataConfigView).trigger("click");
    };


    BxComboBox.prototype.deleteElement = function (optionSubId, optionListArray, $dataConfigView) {//删除元素
        var bxcombobox = this;
        var deleteIndex = bxcombobox.functiontofindIndexByKeyValue(optionListArray, 'id', bxcombobox.getId() + "$$" + optionSubId);
        optionListArray.splice(deleteIndex, 1);
        bxcombobox.getConfigViewsControl("optionList", $dataConfigView).val(JSON.stringify(optionListArray));
        bxcombobox.getOptions()["data"] = optionListArray;
        bxcombobox.getConfigViewsControl("optionAttribute", $dataConfigView).hide();
        bxcombobox.refresh();
    };

    BxComboBox.prototype.bindCore = function (eventname, callback) {
        if (eventname != "Init")
            $("#" + this.getId()).attr(eventname, callback.name + '();');
    };

    Widget.registerType(type, BxComboBox);
})(WidgetObject, "bxcombobox");