var BxChartScatter = (function () {

    function BxChartScatter(type, actions, newSerieOpt) {

        function BxChartScatter() {

        }

        BxChartScatter.prototype = new BxChartCommon(type, actions);

        BxChartScatter.prototype.defaultInitOption = function () {
            var defaultInitOption = {
                dataPattern: 'local',
                isOvertime: true
            };
            return defaultInitOption;
        };

        BxChartScatter.prototype.showPreviewCore = function () {
            var id = this.getPreviewId();
            var option = this.getOption("isOvertime") ? this.prepareOption() : this.getOptions();
            this.setOption("isOvertime", false);
            $("#" + id).bxchartsscatter(option);
        };

        BxChartScatter.prototype.showCore = function () {
            var id = this.getPreviewId();
            var option = this.prepareOption();
            $("#" + id).bxchartsscatter(option);
        };

        BxChartScatter.prototype.getConfigViewCore = function ($configView) {
            var bxcharts = this;
            var chartOpt = bxcharts.getOptions()["chartOption"];
            bxcharts.getConfigViewsControl("chartId", $configView).text(bxcharts.getId());
            bxcharts.setWidgetSize($configView);
            var allAtttibute = bxcharts.getConfigViewsControl("chartAttribute", $configView).find("[data-level]");
            $.each(allAtttibute, function (index, item) {
                bxcharts.setAttributeFromOption(chartOpt, $(this));
                $(this).change(function () {
                    bxcharts.setOptionValue(chartOpt, $(this));
                    bxcharts.setOption("option", chartOpt);
                    bxcharts.refresh();
                });
            });
        };

        BxChartScatter.prototype.getDataConfigViewCore = function ($dataConfigView) {
            var bxchart = this;
            // 获取图表数据绑定信息
            bxchart.initDataConfigView($dataConfigView, newSerieOpt);
        };

        BxChartScatter.prototype.prepareOption = function () {
            var dataSource = this.getOption("dataSource");
            var chartOption = this.getOption("chartOption");

            // set series
            var submitSeries = chartOption.series = [];
            var submitLegend = chartOption.legend.data = [];
            if (dataSource.dynaSeries) {
                var seriesJsonStr = DesignerUtils.queryViaComboTree(dataSource.dynaSeriesData.data);
                try {
                    var seriesJson = JSON.parse(seriesJsonStr);
                    var seriesDataMap = {};
                    $.each(seriesJson[0], function (key, value) {
                        seriesDataMap[key] = [eval("[" + value + "]")];
                        submitLegend.push(key);
                    });
                    for (var i = 1; i < seriesJson.length; i++) {
                        var tabRow = seriesJson[i];
                        $.each(submitLegend, function (index, item) {
                            seriesDataMap[item].push(eval("[" + tabRow[item] + "]"));
                        })
                    }
                    var markPoint = dataSource.dynaSeriesData.markPoint && {
                            data: [{type: "max", name: "最大值"}, {type: "min", name: "最小值"}]
                        };
                    var markLine = dataSource.dynaSeriesData.markLine && {data: [{type: "average", name: "平均值"}]};
                    $.each(seriesDataMap, function (key, value) {
                        var seriesOpt = deepCopy(newSerieOpt);
                        seriesOpt.name = key;
                        seriesOpt.data = value;
                        seriesOpt.markPoint = markPoint;
                        seriesOpt.markLine = markLine;
                        submitSeries.push(seriesOpt);
                    })
                } catch (e) {
                    submitSeries = [];
                    submitLegend = [];
                }

            } else {
                $.each(dataSource.seriesMap, function (key, value) {
                    var series = deepCopy(value);
                    var dataX = eval("[" + DesignerUtils.queryViaComboTree(series.dataX) + "]");
                    var dataY = eval("[" + DesignerUtils.queryViaComboTree(series.dataY) + "]");
                    var dataList = [];
                    for (var i = 0; i < dataX.length; i++) {
                        if (dataY[i] == undefined) break;
                        dataList.push([dataX[i], dataY[i]]);
                    }
                    series.data = dataList;
                    delete series["dataX"];
                    delete series["dataY"];
                    submitSeries.push(series);
                    submitLegend.push(series.name || "新建系列");
                });
            }
            return this.getOptions();
        };

        return new BxChartScatter();
    }

    return BxChartScatter;
})();

