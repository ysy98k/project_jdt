(function (WidgetObj, type) {
    function BxChartScatterStand() { // html节点的操作对象的定位
    }

    var initSerieOpt = {
        name: "",
        type: 'scatter',
        dataX: "",
        dataY: "",
        markPoint: false,
        markLine: false
    };

    var actions = {
        "type": type,
        "actions": []
    };

    BxChartScatterStand.prototype = new BxChartScatter(type, actions, initSerieOpt);

    BxChartScatterStand.prototype.initOption = function () {
        var defalutInitOption = this.defaultInitOption();
        var option = {
            dataSource: {
                seriesMap:{
                    "series1": {
                        id: "series1",
                        name: '女性',
                        type: 'scatter',
                        dataX: "161.2,167.5,159.5,157.0,155.8,170.0,159.1,166.0,176.2,160.2," +
                        "172.5,170.9,172.9,153.4,160.0,147.2,168.2,175.0,157.0,167.6,159.5," +
                        "175.0,166.8,176.5,170.2,174.0,173.0,179.9,170.5,160.0,154.4,162.0," +
                        "176.5,160.0,152.0,162.1,170.0,160.2,161.3,166.4,168.9,163.8,167.6," +
                        "160.0,161.3,167.6,165.1,160.0,170.0,157.5,167.6,160.7,163.2,152.4",
                        dataY: "51.6,59.0,49.2,63.0,53.6,59.0,47.6,69.8,66.8,75.2,55.2,54.2," +
                        "62.5,42.0,50.0,49.8,49.2,73.2,47.8,68.8,50.6,82.5,57.2,87.8,72.8," +
                        "54.5,59.8,67.3,67.8,47.0,46.2,55.0,83.0,54.4,45.8,53.6,73.2,52.1," +
                        "67.9,56.6,62.3,58.5,54.5,50.2,60.3,58.3,56.2,50.2,72.9,59.8,61.0," +
                        "69.1,55.9,46.5",
                        markPoint: {
                            data: [{
                                type: 'max',
                                name: '最大值'
                            },
                                {
                                    type: 'min',
                                    name: '最小值'
                                }]
                        },
                        markLine: {
                            data: [{
                                type: 'average',
                                name: '平均值'
                            }]
                        }
                    },
                    "series2": {
                        id: "series2",
                        name: '男性',
                        type: 'scatter',
                        dataX: "174.0,175.3,193.5,186.5,187.2,181.5,184.0,184.5,175.0,184.0," +
                        "180.0,177.8,192.0,176.0,174.0,184.0,192.7,171.5,173.0,176.0,176.0," +
                        "180.5,172.7,176.0,173.5,178.0,180.3,180.3,164.5,173.0,183.5,175.5," +
                        "188.0,189.2,172.8,170.0,182.0,170.0,177.8,184.2,186.7,171.4,172.7," +
                        "175.3,180.3,182.9,188.0,177.2,172.1,167.0,169.5,174.0,172.7,182.2," +
                        "164.1,163.0,171.5,184.2,174.0,174.0,177.0,186.0,167.0,171.8,182.0," +
                        "167.0,177.8,164.5,192.0,175.5",
                        dataY: "65.6,71.8,80.7,72.6,78.8,74.8,86.4,78.4,62.0,81.6,76.6,83.6," +
                        "90.0,74.6,71.0,79.6,93.8,70.0,72.4,85.9,78.8,77.8,66.2,86.4,81.8," +
                        "89.6,82.8,76.4,63.2,60.9,74.8,70.0,72.4,84.1,69.1,59.5,67.2,61.3," +
                        "68.6,80.1,87.8,84.7,73.4,72.1,82.6,88.7,84.1,94.1,74.9,59.1,75.6," +
                        "86.2,75.3,87.1,55.2,57.0,61.4,76.8,86.8,72.2,71.6,84.8,68.2,66.1," +
                        "72.0,64.6,74.8,70.0,101.6,63.2",
                        markPoint: {
                            data: [{
                                type: 'max',
                                name: '最大值'
                            },
                                {
                                    type: 'min',
                                    name: '最小值'
                                }]
                        },
                        markLine: {
                            data: [{
                                type: 'average',
                                name: '平均值'
                            }]
                        }
                    }
                },
                dynaSeries: false,
                dynaSeriesData: {
                    data:"",
                    markPoint: false,
                    markLine: false
                }
            },
            chartOption: {
                title: {
                    text: '男性女性身高体重分布',
                    subtext: '抽样调查来自: Heinz  2003',
                    x: 'left',
                    y: 'top'
                },
                tooltip: {
                    showDelay: 0,
                    formatter: function (params) {
                        if (params.value.length > 1) {
                            return params.seriesName + ' :<br/>' + params.value[0] + 'cm ' + params.value[1] + 'kg ';
                        } else {
                            return params.seriesName + ' :<br/>' + params.name + ' : ' + params.value + 'kg ';
                        }
                    },
                    axisPointer: {
                        show: true,
                        type: 'cross',
                        lineStyle: {
                            type: 'dashed',
                            width: 1
                        }
                    }
                },
                legend: {
                    data: [],
                    x: 'center',
                    y: 'bottom'
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
                xAxis: [{
                    type: 'value',
                    name: 'cm',
                    scale: true
                }],
                yAxis: [{
                    type: 'value',
                    name: 'kg',
                    scale: true
                }],
                series: []
            }
        };
        $.extend(defalutInitOption, option);
        this.setOptions(defalutInitOption);
    };

    Widget.registerType(type, BxChartScatterStand);
})(WidgetObject, "chartScat");