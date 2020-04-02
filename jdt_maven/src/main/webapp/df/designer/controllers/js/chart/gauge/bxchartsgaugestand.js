(function (WidgetObj, type) {
    function BxChartGaugeStand() { // html节点的操作对象的定位
    }

    var newSerieOpt = {};

    var actions = {
        "type": type,
        "actions": []
    };

    BxChartGaugeStand.prototype = new BxChartGauge(type, actions, newSerieOpt);

    BxChartGaugeStand.prototype.initOption = function () {
        var defalutInitOption = this.defaultInitOption();
        var option = {
            dataSource: {
                polarData: {
                    name: '完成率',
                    value: "50"
                }
            },
            chartOption: {
                title: {
                    text: "完成率",
                    subtext: "",
                    x: "center",
                    y: "top"
                },
                tooltip: {formatter: "{a} <br/>{b} : {c}%"},
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
                series: [{
                    name: '业务指标',
                    type: 'gauge',
                    detail: {
                        formatter: '{value}%'
                    },
                    data: [{}]
                }]
            }

        };
        $.extend(defalutInitOption, option);
        this.setOptions(defalutInitOption);
    };

    Widget.registerType(type, BxChartGaugeStand);
})(WidgetObject, "chartGauge");