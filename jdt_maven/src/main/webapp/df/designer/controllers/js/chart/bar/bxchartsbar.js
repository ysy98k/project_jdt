var BxChartBar = (function () {

    function BxChartBar(type, actions, newSerieOpt) {

        function BxChartBar() {

        };

        BxChartBar.prototype = new BxChartCommon(type, actions);

        BxChartBar.prototype.defaultInitOption = function () {
            var defaultInitOption = {
                dataPattern: "local",
                isOvertime: true,
                queryResult: {
                    hasQueryBtn: true,
                    queryBtn: "none"
                },
                dataValueType: "fact",
                serieslist: {},
                conditionlist: {
                    instance: {},
                    time: {},
                    chartRelate: {}
                },
                condition: [
                    {
                        item: '',
                        value: [],
                        label: [],
                        generateInstance: true,
                        selectInstance: 'none',
                        selectType: 'selected'
                    },
                    {
                        item: 'time',
                        value: [],
                        type: 'date',
                        returnFormatter: 'yyyy-MM-dd',
                        intervalsStr: 'day',
                        selectStart: 'none',
                        selectEnd: 'none',
                        intervalsType: 'intervalsStr',
                        selectIntervals: 'none'
                    }
                ]
            };
            return defaultInitOption;
        };

        BxChartBar.prototype.showPreviewCore = function () {
            var id = this.getPreviewId();
            var option = this.getOption("isOvertime") ? this.prepareOption() : this.getOptions();
            this.setOption("isOvertime", false);
            $("#" + id).bxchartsbar(option);
        };

        BxChartBar.prototype.showCore = function () {
            var id = this.getId();
            var option = this.prepareOption();
            $("#" + id).bxchartsbar(option);
        };

        BxChartBar.prototype.getConfigViewCore = function ($configView) {
            var bxchartsbar = this;
            var chartOpt = bxchartsbar.getOptions()["chartOption"];
            bxchartsbar.getConfigViewsControl("chartId", $configView).text(bxchartsbar.getId());
            bxchartsbar.setWidgetSize($configView);
            var allAtttibute = bxchartsbar.getConfigViewsControl("chartAttribute", $configView).find("[data-level]");
            $.each(allAtttibute, function (index, item) {
                bxchartsbar.setAttributeFromOption(chartOpt, $(this));
                $(this).change(function () {
                    bxchartsbar.setOptionValue(chartOpt, $(this));
                    bxchartsbar.refresh();
                });
            });
        };

        BxChartBar.prototype.getDataConfigViewCore = function ($dataConfigView) {
            var bxchartsbar = this;
            // 获取图表数据绑定信息
            bxchartsbar.initDataConfigView($dataConfigView, newSerieOpt);
        };

        BxChartBar.prototype.prepareOption = function () {
            // set axis
            var dataSource = this.getOption("dataSource");
            var chartOption = this.getOption("chartOption");
            var axis = DesignerUtils.queryViaComboTree(dataSource.axisData);
            if (this.getOption("chartRelate").ydataItem == 'dataValue') {
                chartOption.xAxis[0].data = axis.split(",");
            } else {
                chartOption.yAxis[0].data = axis.split(",");
            }

            // set series
            var submitSeries = chartOption["series"] = [];
            var submitLegend = chartOption["legend"]["data"] = [];
            if (dataSource.dynaSeries) {
                var seriesJsonStr = DesignerUtils.queryViaComboTree(dataSource.dynaSeriesData.data);
                try {
                    var seriesJson = JSON.parse(seriesJsonStr);
                    var seriesDataMap = {};
                    $.each(seriesJson[0], function (key, value) {
                        seriesDataMap[key] = [parseFloat(value)];
                        submitLegend.push(key);
                    });
                    for (var i = 1; i < seriesJson.length; i++) {
                        var tabRow = seriesJson[i];
                        $.each(submitLegend, function (index, item) {
                            seriesDataMap[item].push(parseFloat(tabRow[item]));
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
                }catch(e) {
                    submitSeries = [];
                    submitLegend = [];
                }

            } else {
                var seriesMap = dataSource.seriesMap;
                $.each(seriesMap, function (key, value) {
                    var series = deepCopy(value);
                    series.data = eval("[" + DesignerUtils.queryViaComboTree(series.data) + "]");
                    submitSeries.push(series);
                    submitLegend.push(series.name || "新建系列");
                });
            }

            return this.getOptions();
        };

        return new BxChartBar();
    }

    return BxChartBar;
})();

