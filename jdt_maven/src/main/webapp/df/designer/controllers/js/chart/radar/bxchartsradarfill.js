(function (WidgetObj, type) {
    function BxChartRadarFill() { // html节点的操作对象的定位
    }

    var initSerieOpt = {
        name: "",
        value: ""
    };

    var actions = {
        "type": type,
        "actions": []
    };

    BxChartRadarFill.prototype = new BxChartRadar(type, actions, initSerieOpt);

    BxChartRadarFill.prototype.initOption = function () {
        var defalutInitOption = this.defaultInitOption();
        var option = {
            dataSource: {
                polarData: {
                    name: "进攻,防守,体能,速度,力量,技巧",
                    value: "100,100,100,100,100,100"
                },
                seriesMap: {
                    "series1": {
                        id: "series1",
                        name: '舍普琴科',
                        value: "97, 42, 88, 94, 90, 86"

                    },
                    "series2": {
                        id: "series2",
                        name: '罗纳尔多',
                        value: "97, 32, 74, 95, 88, 92"
                    }
                },
                dynaSeries: false,
                dynaSeriesData: {data:""}
            },
            chartOption: {
                title: {
                    text: '罗纳尔多 vs 舍普琴科',
                    subtext: '完全实况球员数据',
                    x: 'left',
                    y: 'top'
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    x: 'center',
                    y: 'bottom',
                    data: []
                },
                toolbox: {
                    show: true,
                    feature: {
                        dataView: {
                            show: true
                        },
                        saveAsImage: {
                            show: true
                        }
                    }
                },
                calculable: true,
                polar: [{
                    indicator: [],
                    radius: 130
                }],
                series: [{
                    name:'罗纳尔多 vs 舍普琴科',
                    type: 'radar',
                    itemStyle: {normal: {areaStyle: {type: 'default'}}},
                    data: []
                }]
            }
        };
        $.extend(defalutInitOption, option);
        this.setOptions(defalutInitOption);
    };

    Widget.registerType(type, BxChartRadarFill);
})(WidgetObject, "chartRadarF");