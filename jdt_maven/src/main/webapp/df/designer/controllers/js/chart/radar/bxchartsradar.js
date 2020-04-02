var BxChartRadar = (function () {

    function BxChartRadar(type, actions, newSerieOpt) {

        function BxChartRadar() {

        };

        BxChartRadar.prototype = new BxChartCommon(type, actions);

        BxChartRadar.prototype.defaultInitOption = function () {
            var defaultInitOption = {
                dataPattern: 'local',
                isOvertime: true
            };
            return defaultInitOption;
        };

        BxChartRadar.prototype.showPreviewCore = function () {
            var id = this.getPreviewId();
            var option = this.getOption("isOvertime") ? this.prepareOption() : this.getOptions();
            this.setOption("isOvertime", false);
            $("#" + id).bxchartsradar(option);
        };

        BxChartRadar.prototype.showCore = function () {
            var id = this.getPreviewId();
            var option = this.prepareOption();
            $("#" + id).bxchartsradar(option);
        };

        BxChartRadar.prototype.getConfigViewCore = function ($configView) {
            var bxcharts = this;
            var chartOpt = bxcharts.getOptions()["chartOption"];
            bxcharts.getConfigViewsControl("chartId", $configView).text(bxcharts.getId());
            bxcharts.setWidgetSize($configView);
            var allAtttibute = bxcharts.getConfigViewsControl("chartAttribute", $configView).find("[data-level]");
            $.each(allAtttibute, function (index, item) {
                bxcharts.setAttributeFromOption(chartOpt, $(this));
                $(this).change(function () {
                    bxcharts.setOptionValue(chartOpt, $(this));
                    if($(this).attr('data-attribute')=='text')
                        chartOpt.series[0].name=$(this).val();
                    bxcharts.setOption("option", chartOpt);
                    bxcharts.refresh();
                });
            });
        };

        BxChartRadar.prototype.getDataConfigViewCore = function ($dataConfigView) {
            var bxchart = this;
            // 获取图表数据绑定信息
            bxchart.initDataConfigView($dataConfigView, newSerieOpt);
        };

        BxChartRadar.prototype.prepareOption = function () {
            var dataSource = this.getOption("dataSource");
            var chartOption = this.getOption("chartOption");

            // set polar
            var pNameList = DesignerUtils.queryViaComboTree(dataSource.polarData.name).split(",");
            var pValueList = DesignerUtils.queryViaComboTree(dataSource.polarData.value).split(",");
            chartOption.polar[0].indicator = [];
            $.each(pNameList, function (index, item) {
                chartOption.polar[0].indicator.push({
                    text: item,
                    max: parseInt(pValueList[index] || 0)
                });
            });

            // set series
            var submitSeries = chartOption.series[0].data = [];
            var submitLegend = chartOption.legend.data = [];
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
                        seriesOpt.value = value;
                        submitSeries.push(seriesOpt);
                    })
                }catch(e) {
                    submitSeries = [];
                    submitLegend = [];
                }
            }else{
                var seriesMap = dataSource.seriesMap;
                $.each(seriesMap, function (key, value) {
                    var series = deepCopy(value);
                    series.value = eval("[" + DesignerUtils.queryViaComboTree(series.value) + "]");
                    submitSeries.push(series);
                    submitLegend.push(series.name || "新建系列");
                });
            }
            return this.getOptions();
        };

        return new BxChartRadar();
    }

    return BxChartRadar;
})();

