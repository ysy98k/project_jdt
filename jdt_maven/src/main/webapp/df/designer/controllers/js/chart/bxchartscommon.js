var BxChartCommon = (function () {

    function BxChartCommon(type, actions) {

        function BxChartCommon() {
        }

        BxChartCommon.prototype = new BxWidgetCommon(type, actions);

        // 获取图表数据绑定信息
        BxChartCommon.prototype.initDataConfigView = function ($dataConfigView, newSerieOpt) {
            var bxchart = this;
            var chartOpt = bxchart.getOptions()["chartOption"];
            var dataSource = bxchart.getOption("dataSource");
            if (!isAvailable(dataSource)) {//为了兼容升级时不报错
                dataSource = {
                    axisData: [],
                    polarData: {
                        name: "",
                        value: ""
                    },
                    seriesMap: {},
                    dynaSeries: false
                };
                bxchart.setOption("dataSource", dataSource);
            }
            //获取坐标轴点数据
            var $axisData = bxchart.getConfigViewsControl("axisData", $dataConfigView);
            if ($axisData.length > 0) {
                //检查内容是否存在
                if (!DesignerUtils.checkComboTreeValue(dataSource.axisData)) dataSource.axisData = "";
                $axisData.val(dataSource.axisData);
                $axisData.change(function () {
                    dataSource.axisData = this.value;
                    bxchart.setOption("isOvertime", true);
                    bxchart.refresh();
                });
            }
            //获取极坐标数据
            var polarData = dataSource.polarData;
            if (polarData) {
                //检查内容是否存在
                if (!DesignerUtils.checkComboTreeValue(polarData.name)) polarData.name = "";
                bxchart.getConfigViewsControl("polarName", $dataConfigView).val(polarData.name)
                    .change(function () {
                        dataSource.polarData.name = $(this).val();
                        bxchart.setOption("isOvertime", true);
                        bxchart.refresh();
                    });
                //检查内容是否存在
                if (!DesignerUtils.checkComboTreeValue(polarData.value)) polarData.value = "";
                bxchart.getConfigViewsControl("polarValue", $dataConfigView).val(polarData.value)
                    .change(function () {
                        dataSource.polarData.value = $(this).val();
                        bxchart.setOption("isOvertime", true);
                        bxchart.refresh();
                    });
            }

            //初始化系列
            var seriesMap = dataSource.seriesMap || {};
            $.each(seriesMap, function (key, value) {
                bxchart.addSeries(value, $dataConfigView);
            });

            //获取饼图，雷达图数据
            var allseries0 = bxchart.getConfigViewsControl("series0Attr", $dataConfigView).find("[data-level]");
            $.each(allseries0, function (index, item) {
                bxchart.setDataAttrFromOption($(this), chartOpt.series[0]);
                $(this).change(function () {
                    bxchart.setDataOptionValue($(this), chartOpt.series[0]);
                    bxchart.setOption("isOvertime", true);
                    bxchart.refresh();
                });
            });

            //绑定增加系列按钮事件，点击增加系列进行操作处理
            bxchart.getConfigViewsControl("addSeriesBtn", $dataConfigView).unbind().bind("click", function () {
                var serieId = bxchart.addSeries(newSerieOpt, $dataConfigView);
                //绑定系列，点击获取系列信息
                bxchart.getConfigViewsControl(serieId, $dataConfigView).trigger("click");
                bxchart.refresh();
            });

            // 动态系列按钮事件
            bxchart.getConfigViewsControl("dynaSeriesOn", $dataConfigView).on("click", function () {
                dataSource.dynaSeries = true;
                $dataConfigView.find(".fixed-only").slideUp(200);
                $dataConfigView.find(".dyna-only").slideDown(500);
                bxchart.setOption("isOvertime", true);
                bxchart.refresh();
            });
            bxchart.getConfigViewsControl("dynaSeriesOff", $dataConfigView).on("click", function () {
                dataSource.dynaSeries = false;
                $dataConfigView.find(".dyna-only").slideUp(200);
                $dataConfigView.find(".fixed-only").slideDown(500);
                bxchart.setOption("isOvertime", true);
                bxchart.refresh();
            });
            if (dataSource.dynaSeries) {
                $dataConfigView.find(".fixed-only").hide();
            } else {
                $dataConfigView.find(".dyna-only").hide();
            }

            // 动态系列修改事件
            if (dataSource.dynaSeriesData) {
                bxchart.getConfigViewsControl("dynaSeriesData", $dataConfigView)
                    .val(dataSource.dynaSeriesData.data)
                    .change(function () {
                        dataSource.dynaSeriesData.data = this.value;
                        bxchart.setOption("isOvertime", true);
                        bxchart.refresh();
                    });
                $dataConfigView.find(".dyna-only input[type='checkbox']").each(function () {
                    var level = $(this).data('level');
                    $(this).context.checked = dataSource.dynaSeriesData[level];
                    $(this).change(function () {
                        dataSource.dynaSeriesData[level] = $(this).context.checked;
                        bxchart.setOption("isOvertime", true);
                        bxchart.refresh();
                    })
                });
            }

            // 初始化下拉树输入框
            $dataConfigView.find(".treeInput_arround").remove();
            bxchart.initCombTrees($dataConfigView);

        };

        // 增加系列按钮
        BxChartCommon.prototype.addSeries = function (seriesOpt, $dataConfigView) {
            var bxchart = this;
            var seriesMap = bxchart.getOption("dataSource")["seriesMap"];
            // 初始化id
            var seriesId = seriesOpt.id || "series" + DesignerUtils.randomNumber();

            // 添加系列元素
            var series = "<div data-attribute='series'class='col-sm-12 btn btn-xs btn-yellow' id='" + this.getId() + "$$" + seriesId
                + "' data-seriesid='" + seriesId + "' style='margin:0 15px 5px 15px;width: 86.9%'><span>" + (seriesOpt.name || "新建系列")
                + "</span><i class='ace-icon fa fa-times pull-right'data-dismiss='alert'style='margin-top:3px'></i></div>";
            bxchart.getConfigViewsControl("addSeriesBtn", $dataConfigView).parent().before(series);
            seriesMap[seriesId] = deepCopy(seriesOpt);
            seriesMap[seriesId]["id"] = seriesId;

            // 删除按钮事件
            bxchart.getConfigViewsControl(seriesId, $dataConfigView).children("i").bind("click", function () {
                $(this).parent().unbind();
                bxchart.delDataAttr($(this).parent().data("seriesid"), $dataConfigView);
            });

            // 系列元素单击事件
            bxchart.getConfigViewsControl(seriesId, $dataConfigView).bind("click", function () {
                var curBtn = $(this);
                var curOpt = seriesMap[curBtn.data("seriesid")];
                var $seriesAttribute = bxchart.getConfigViewsControl("seriesAttribute", $dataConfigView);
                $.each($seriesAttribute.find("[data-level]"), function (index, item) {
                    bxchart.setDataAttrFromOption($(this), curOpt);
                    $(this).unbind("change").bind("change", function () {
                        bxchart.setDataOptionValue($(this), curOpt);// 从组件option中设置当前系列对象的值
                        if ($(this).data("attribute") == "name")
                            curBtn.children("span").text($(this).val());
                        bxchart.setOption("isOvertime", true);
                        bxchart.refresh();
                    });
                });
                bxchart.getConfigViewsControl("seriesAttribute", $dataConfigView).show();
            });

            return seriesId;
        };

        // 删除系列或是属性对象
        BxChartCommon.prototype.delDataAttr = function (id, $dataConfigView) {
            var bxchart = this;
            delete bxchart.getOption("dataSource")["seriesMap"][id];
            bxchart.setOption("isOvertime", true);
            bxchart.getConfigViewsControl("seriesAttribute", $dataConfigView).hide();
            bxchart.refresh();
        };

        BxChartCommon.prototype.setDataAttrFromOption = function ($attributeObj, dataObj) {// 从组件option中设置当前系列属性框的值
            if (dataObj) {
                var dateLevel = $attributeObj.data("level");
                var dataAttr = dateLevel.split(".")[1];
                //检查内容是否存在
                if (!DesignerUtils.checkComboTreeValue(dataObj[dataAttr])) dataObj[dataAttr] = "";
                this.setDataAttrValue($attributeObj, dataObj[dataAttr]);
            }
        };

        BxChartCommon.prototype.setDataAttrValue = function ($attributeObj, value) {// 设置属性框的值
            if ($attributeObj.attr("type") == "checkbox") {
                if (value != undefined && value.data != undefined)
                    $attributeObj.context.checked = true;
                else if (value)
                    $attributeObj.context.checked = true;
                else
                    $attributeObj.context.checked = false;

            }
            else {
                if (value instanceof Array) {
                    var arrayStr = this.convertArrayValuetoStr(value);
                    arrayStr = arrayStr.substring(1, arrayStr.length - 1);
                    $attributeObj.val(arrayStr);
                } else {
                    $attributeObj.val(value);
                }
            }
        };

        /*数组转字符串*/
        BxChartCommon.prototype.convertArrayValuetoStr = function (valueArray) {
            var valueStr = "[";
            for (var i = 0; i < valueArray.length; i++) {
                if (valueArray[i] instanceof Array) {
                    valueStr += this.convertArrayValuetoStr(valueArray[i]);
                } else {
                    valueStr += valueArray[i];
                }
                if (i < valueArray.length - 1) {
                    valueStr += ",";
                }
            }
            valueStr += "]";
            return valueStr;
        };


        BxChartCommon.prototype.setDataOptionValue = function ($attributeObj, dataObj) {// 将$attributeObj值设置到dataObj属性中
            if (dataObj) {
                var dateLevel = $attributeObj.data("level");
                var dataAttr = dateLevel.split(".")[1];
                if (dataObj[dataAttr] != null && dataObj[dataAttr] != undefined) {
                    dataObj[dataAttr] = this.getDataAttrValue($attributeObj, dataAttr);
                } else {
                    dataObj[dataAttr] = "";
                }
            }
        };

        BxChartCommon.prototype.getDataAttrValue = function ($attributeObj, dataAttr) {// 从系列属性框得到值
            if ($attributeObj.attr("type") == "checkbox") {
                if ($attributeObj.context.checked) {
                    if (dataAttr == "markPoint") {
                        var markPoint = {
                            "data": [{
                                "type": "max",
                                "name": "最大值"
                            }, {
                                "type": "min",
                                "name": "最小值"
                            }]
                        };
                        return markPoint;
                    } else if (dataAttr == "markLine") {
                        var markLine = {
                            "data": [{
                                "type": "average",
                                "name": "平均值"
                            }]
                        };
                        return markLine;
                    }
                    else
                        return true;
                } else {
                    return false;
                }
            } else {
                if (/^(radius)$/.test(dataAttr)) {
                    return DesignerUtils.queryViaComboTree($attributeObj.val()).split(",");
                }
                return $attributeObj.val();

                /*
                 if (/^((value)|(label)|(center)|(radius))$/.test(dataAttr))
                 return DesignerUtils.queryViaComboTree($attributeObj.val()).split(",");
                 if (dataAttr == 'data')
                 return eval("[" + DesignerUtils.queryViaComboTree($attributeObj.val()) + "]");
                 return DesignerUtils.queryViaComboTree($attributeObj.val());
                 */
            }
        };


        /* 初始化组合下拉框 */
        BxChartCommon.prototype.initCombTrees = function ($configView) {
            var widgetsObj = [], sqlObj = [], sqlTabObj = [], pdsObj = [], funcObj = [];

            var typeList = /^((input)|(bxtree)|(bxcombobox)|(label)|(bxtimepicker)|(textarea)|(radio)|(checkbox)|(bxprogressbar)|(switch))$/;
            $.each(DesignerUtils.getWidgetsSelectObj(), function (index, item) {
                if (typeList.test(item.type))
                    widgetsObj.push({value: "#{" + item.name + "}", label: item.name});
            });

            $.each(dataSourceditor.getDSOptions(), function (key, value) {
                switch (value.ds_type) {
                    case "sql":
                        var sqlOpt = value.options;
                        var column = [];
                        var sqlHead = "${" + key + ".";
                        $.each(sqlOpt.sqlCol.split(","), function (index, item) {
                            column.push({value: sqlHead + item + "}", label: item});
                        });
                        sqlObj.push({
                            label: key,
                            value: sqlHead + (column.length > 0 ? "wlsUnusedValue}" : "id}"),
                            state: {opened: false},
                            children: column
                        });
                        sqlTabObj.push({
                            label: key,
                            value: "${" + key + "}"
                        });
                        break;
                    case "pds":
                        pdsObj.push({
                            label: key,
                            state: {opened: false},
                            children: [
                                {value: "%{" + key + ".v}", label: "值"},
                                {value: "%{" + key + ".t}", label: "时间"}
                            ]
                        });
                        break;
                }
            });

            $.each(DesignerUtils.getCusFuncList(), function (index, item) {
                funcObj.push({value: "FUNC{" + item + "}", label: item});
            });

            var treeData = [{
                label: "页面组件",
                state: {opened: false},
                value: "<无可用的组件>",
                children: widgetsObj
            }, {
                label: "sql",
                state: {opened: false},
                value: "<无可用的sql配制>",
                children: sqlObj
            }, {
                label: "pds",
                state: {opened: false},
                value: "<无可用的pds配制>",
                children: pdsObj
            }, {
                label: "自定义函数",
                state: {opened: false},
                value: "<无可用的函数>",
                children: funcObj
            }];

            $configView.find(".treeInput:not(.treeInput-tab)").each(function () {
                treeInputUtils.createTreeInput($(this), treeData);
            });

            var tabTreeData = [{
                label: "sql",
                state: {opened: false},
                value: "<无可用的sql配制>",
                children: sqlTabObj
            }, {
                label: "自定义函数",
                state: {opened: false},
                value: "<无可用的函数>",
                children: funcObj
            }];
            $configView.find(".treeInput-tab").each(function () {
                treeInputUtils.createTreeInput($(this), tabTreeData);
            });
        };

        return new BxChartCommon();
    }

    return BxChartCommon;
})();