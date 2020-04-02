(function (WidgetObj, type) {
    function BxChartBarStack() { // html节点的操作对象的定位
    }

    var newSerieOpt = {
        name: '',
        type: 'bar',
        data: "",
        stack: '总和',
        markPoint: false,
        markLine: false
    };

    var actions = {
        "type": type,
        "actions": []
    };

    BxChartBarStack.prototype = new BxChartBar(type, actions, newSerieOpt);

    BxChartBarStack.prototype.initOption = function () {
        var defalutInitOption = this.defaultInitOption();
        var option = {
            dataSource: {
                axisData: "周一,周二,周三,周四,周五,周六,周日",
                seriesMap: {
                    "series1": {
                        id: "series1",
                        name: '直接访问',
                        type: 'bar',
                        stack: '总和',
                        data: "320, 332, 301, 334, 390, 330, 320"
                    },
                    "series2": {
                        id: "series2",
                        name: '邮件营销',
                        type: 'bar',
                        stack: '总和',
                        data: "120, 132, 101, 134, 90, 230, 210"
                    },
                    "series3": {
                        id: "series3",
                        name: '联盟广告',
                        type: 'bar',
                        stack: '总和',
                        data: "220, 182, 191, 234, 290, 330, 310"
                    },
                    "series4": {
                        id: "series4",
                        name: '视频广告',
                        type: 'bar',
                        stack: '总和',
                        data: "150, 232, 201, 154, 190, 330, 410"
                    },
                    "series5": {
                        id: "series5",
                        name: '搜索引擎',
                        type: 'bar',
                        stack: '总和',
                        data: "862, 1018, 964, 1026, 1679, 1600, 1570"
                    },
                    "series6": {
                        id: "series6",
                        name: '百度',
                        type: 'bar',
                        stack: '总和',
                        data: "620, 732, 701, 734, 1090, 1130, 1120"
                    },
                    "series7": {
                        id: "series7",
                        name: '谷歌',
                        type: 'bar',
                        stack: '总和',
                        data: "120, 132, 101, 134, 290, 230, 220"
                    },
                    "series8": {
                        id: "series8",
                        name: '必应',
                        type: 'bar',
                        stack: '总和',
                        data: "60, 72, 71, 74, 190, 130, 110"
                    },
                    "series9": {
                        id: "series9",
                        name: '其他',
                        type: 'bar',
                        stack: '总和',
                        data: "62, 82, 91, 84, 109, 110, 120"
                    }
                },
                dynaSeries: false,
                dynaSeriesData: {
                    data:"",
                    markPoint: false,
                    markLine: false
                }
            },
            seriesObj: {
                stack: '总和'
            },
            chartOption: {
                title: {
                    text: "某站点用户访问来源",
                    subtext: "",
                    x: "left",
                    y: "bottom"
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: { // 坐标轴指示器，坐标轴触发有效
                        type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
                    }
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
                legend: {
                    data: [],
                    x: 'center',
                    y: 'top'
                },
                calculable: true,
                xAxis: [{
                    type: 'category',
                    name: '',
                    data: []
                }],
                yAxis: [{
                    name: '',
                    type: 'value'
                }],
                series: []
            },
            chartRelate: {
                xdataItem: '',
                ydataItem: 'dataValue',
                legendItem: ''
            }
        };
        $.extend(defalutInitOption, option);
        this.setOptions(defalutInitOption);
    };

    Widget.registerType(type, BxChartBarStack);
})(WidgetObject, "chartColT");