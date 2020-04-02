var BxChartMap = (function () {

    function BxChartMap(type, actions, newSerieOpt) {

        var beautifyObj;

        function BxChartMap() {

        };

        BxChartMap.prototype = new BxChartCommon(type, actions);

        BxChartMap.prototype.defaultInitOption = function () {
            var defaultInitOption = {
                dataPattern: "local",
                isOvertime: true
            };
            return defaultInitOption;
        };

        BxChartMap.prototype.showPreviewCore = function () {
            var id = this.getPreviewId();
            var option = this.getOption("isOvertime") ? this.prepareOption() : this.getOptions();
            this.setOption("isOvertime", false);
            $("#" + id).bxchartsmap(option);
        };

        BxChartMap.prototype.showCore = function () {
            var id = this.getPreviewId();
            var option = this.prepareOption();
            $("#" + id).bxchartsmap(option);
        };

        BxChartMap.prototype.getConfigViewCore = function ($configView) {
            var bxcharts = this;
            var chartOpt = bxcharts.getOptions()["chartOption"];
            bxcharts.getConfigViewsControl("chartId", $configView).text(bxcharts.getId());
            bxcharts.setWidgetSize($configView);
            var allAtttibute = bxcharts.getConfigViewsControl("chartAttribute", $configView).find("[data-level]");
            $.each(allAtttibute, function (index, item) {
                bxcharts.setAttributeFromOption(chartOpt, $(this));
                $(this).change(function () {
                    bxcharts.setOptionValue(chartOpt, $(this));
                    bxcharts.refresh();
                });
            });
        };

        BxChartMap.prototype.getDataConfigViewCore = function ($dataConfigView) {
            var bxchart = this;
            // 获取图表数据绑定信息
            bxchart.initDataConfigView($dataConfigView, newSerieOpt);

        };

        BxChartMap.prototype.prepareOption = function () {
            var dataSource = this.getOption("dataSource");
            var chartOption = this.getOption("chartOption");
            var axisList = DesignerUtils.queryViaComboTree(dataSource.axisData).split(",");

            // set series
            var submitSeries = chartOption["series"] = [];
            var submitLegend = chartOption["legend"]["data"] = [];
            if (dataSource.dynaSeries) {
                var seriesJsonStr = DesignerUtils.queryViaComboTree(dataSource.dynaSeriesData.data);
                try {
                    var seriesJson = JSON.parse(seriesJsonStr);
                    var seriesDataMap = {};
                    $.each(seriesJson[0], function (key, value) {
                        seriesDataMap[key] = [value];
                        submitLegend.push(key);
                    });
                    for (var i = 1; i < seriesJson.length; i++) {
                        var tabRow = seriesJson[i];
                        $.each(submitLegend, function (index, item) {
                            seriesDataMap[item].push(tabRow[item]);
                        })
                    }
                    $.each(seriesDataMap, function (key, value) {
                        var seriesOpt = deepCopy(newSerieOpt);
                        seriesOpt.name = key;
                        seriesOpt.data = [];
                        $.each(axisList, function (index, item) {
                            seriesOpt.data.push({
                                name: item,
                                value: value[index]
                            });
                        });
                        submitSeries.push(seriesOpt);
                    })
                }catch(e) {
                    submitSeries = [];
                    submitLegend = [];
                }

            } else {
                var seriesMap = dataSource.seriesMap;
                $.each(seriesMap, function (key, value) {
                    var series = deepCopy(value);
                    var valueList = eval("[" + DesignerUtils.queryViaComboTree(series.data) + "]");
                    series.data = [];
                    $.each(axisList, function (index, item) {
                        series.data.push({
                            name: item,
                            value: valueList[index]
                        });
                    });
                    submitSeries.push(series);
                    submitLegend.push(series.name || "新建系列");
                });
            }

            return this.getOptions();
        };


        return new BxChartMap();
    }

    return BxChartMap;
})();

