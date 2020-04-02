var BxChartPie = (function () {
    function BxChartPie(type, actions, newSerieOpt) {
        function BxChartPie() {
        };

        BxChartPie.prototype = new BxChartCommon(type, actions);

        BxChartPie.prototype.defaultInitOption = function () {
            var defaultInitOption = {
                isOvertime: true,
                "queryResult": {
                    "hasQueryBtn": true,
                    "queryBtn": "none"
                },
                "dataValueType": "fact",
                "serieslist": {},
                "conditionlist": {},
                "condition": [
                    {
                        "item": '',
                        "value": [],
                        "label": [],
                        "generateInstance": true,
                        "selectInstance": 'none',
                        "selectType": 'selected'
                    },
                    {
                        "item": 'time',
                        "value": [],
                        "type": 'date',
                        "returnFormatter": 'yyyy-MM-dd',
                        "intervalsStr": 'day',
                        "selectStart": 'none',
                        "selectEnd": '',
                        "intervalsType": 'intervalsStr',
                        "selectIntervals": 'none'
                    }
                ],
                "chartRelate": {
                    "dataItem": 'dataValue',
                    "legendItem": ''
                }
            };
            return defaultInitOption;
        };

        BxChartPie.prototype.showPreviewCore = function () {
            var id = this.getPreviewId();
            var option = this.getOption("isOvertime") ? this.prepareOption() : this.getOptions();
            this.setOption("isOvertime", false);

            var seriesElement0 = option.chartOption.series[0];
            if (seriesElement0 != undefined && seriesElement0 != null) {
                var radius = seriesElement0.radius;
                if (radius.length == 1) {
                    seriesElement0.radius = radius[0];
                }
            }
            $("#" + id).bxchartspie(option);
        };

        BxChartPie.prototype.showCore = function () {
            var id = this.getId();
            var option = this.prepareOption();
            var seriesElement0 = option.chartOption.series[0];
            if (seriesElement0 != undefined && seriesElement0 != null) {
                var radius = seriesElement0.radius;
                if (radius.length == 1) {
                    seriesElement0.radius = radius[0];
                }
            }
            $("#" + id).bxchartspie(option);
        };

        BxChartPie.prototype.getConfigViewCore = function ($configView) {
            var bxchartspie = this;
            var chartOpt = bxchartspie.getOptions()["chartOption"];
            bxchartspie.getConfigViewsControl("chartId", $configView).text(bxchartspie.getId());
            bxchartspie.setWidgetSize($configView);
            var allAtttibute = bxchartspie.getConfigViewsControl("chartAttribute", $configView).find("[data-level]");
            $.each(allAtttibute, function (index, item) {
                bxchartspie.setAttributeFromOption(chartOpt, $(this));
                $(this).change(function () {
                    bxchartspie.setOptionValue(chartOpt, $(this));
                    bxchartspie.setOption("option", chartOpt);
                    bxchartspie.refresh();
                });
            });
        };

        BxChartPie.prototype.getDataConfigViewCore = function ($dataConfigView) {
            var bxchartspie = this;
            // 获取图表数据绑定信息
            bxchartspie.initDataConfigView($dataConfigView, newSerieOpt);
        };

        BxChartPie.prototype.prepareOption = function () {
            // set axis
            var dataSource = this.getOption("dataSource");
            var chartOption = this.getOption("chartOption");

            // set series
            var submitSeries = chartOption["series"][0].data = [];
            var submitLegend = chartOption["legend"]["data"] = [];
            if (dataSource.dynaSeries) {
                var seriesJsonStr = DesignerUtils.queryViaComboTree(dataSource.dynaSeriesData.data);
                try {
                    var seriesJson = JSON.parse(seriesJsonStr);
                    $.each(seriesJson[0], function (key, value) {
                        submitSeries.push({name:key, value:value});
                        submitLegend.push(key);
                    });
                }catch(e) {
                    submitSeries = [];
                    submitLegend = [];
                }
            }else{
                var seriesMap = dataSource.seriesMap;
                $.each(seriesMap, function (key, value) {
                    var series = deepCopy(value);
                    series.value = parseFloat(DesignerUtils.queryViaComboTree(series.value).split(",")[0]) || 0;
                    submitSeries.push(series);
                    submitLegend.push(series.name || "新建系列");
                });
            }
            return this.getOptions();
        };

        return new BxChartPie();
    }

    return BxChartPie;
})();