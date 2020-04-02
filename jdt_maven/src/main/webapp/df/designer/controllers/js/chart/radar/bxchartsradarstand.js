(function (WidgetObj, type) {
    function BxChartRadarStand() { // html节点的操作对象的定位
    }

    var initSerieOpt = {
        name: "",
        value: ""
    };

    var actions = {
        "type": type,
        "actions": []
    };

    BxChartRadarStand.prototype = new BxChartRadar(type, actions, initSerieOpt);

    BxChartRadarStand.prototype.initOption = function () {
        var defalutInitOption = this.defaultInitOption();
        var option = {
            dataSource: {
                polarData: {
                    name: "销售,管理,信息技术,客服,研发,市场",
                    value: "6000,16000,30000,38000,52000,25000"
                },
                seriesMap: {
                    "series1": {
                        id: "series1",
                        name: '预算分配',
                        value: "4300, 10000, 28000, 35000, 50000, 19000"

                    },
                    "series2": {
                        id: "series2",
                        name: '实际开销',
                        value: "5000, 14000, 28000, 31000, 42000, 21000"
                    }
                },
                dynaSeries: false,
                dynaSeriesData: {data:""}
            },
            chartOption: {
                title: {
                    text: '预算 vs 开销',
                    subtext: '纯属虚构',
                    x: "left",
                    y: "top"
                },
                tooltip: {trigger: 'axis'},
                legend: {
                    orient: 'vertical',
                    x: 'right',
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
                polar: [{indicator: []}],
                calculable: true,
                series: [{
                    name:'预算 vs 开销',
                    type: 'radar',
                    data: []
                }]
            }
        };
        $.extend(defalutInitOption, option);
        this.setOptions(defalutInitOption);
    };

    Widget.registerType(type, BxChartRadarStand);
})(WidgetObject, "chartRadar");