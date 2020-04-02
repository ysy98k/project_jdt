(function (WidgetObj, type) {
    function BxChartBarStandHorizontalStack() { // html节点的操作对象的定位
    }

    var newSerieOpt = {
        name: '',
        type: 'bar',
        data: "",
        stack: '总和',
        itemStyle: {
            normal: {
                label: {
                    show: true,
                    position: 'insideRight'
                }
            }
        },
        markPoint: false,
        markLine: false
    };

    var actions = {
        "type": type,
        "actions": []
    };

    BxChartBarStandHorizontalStack.prototype = new BxChartBar(type, actions, newSerieOpt);

    BxChartBarStandHorizontalStack.prototype.initOption = function () {
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
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    position: 'insideRight'
                                }
                            }
                        },
                        data: "320, 302, 301, 334, 390, 330, 320",
                        markPoint: false,
                        markLine: false
                    },
                    "series2": {
                        id: "series2",
                        name: '邮件营销',
                        type: 'bar',
                        stack: '总和',
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    position: 'insideRight'
                                }
                            }
                        },
                        data: "120, 132, 101, 134, 90, 230, 210",
                        markPoint: false,
                        markLine: false
                    },
                    "series3": {
                        id: "series3",
                        name: '联盟广告',
                        type: 'bar',
                        stack: '总和',
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    position: 'insideRight'
                                }
                            }
                        },
                        data: "220, 182, 191, 234, 290, 330, 310",
                        markPoint: false,
                        markLine: false
                    },
                    "series4": {
                        id: "series4",
                        name: '视频广告',
                        type: 'bar',
                        stack: '总和',
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    position: 'insideRight'
                                }
                            }
                        },
                        data: "150, 212, 201, 154, 190, 330, 410",
                        markPoint: false,
                        markLine: false
                    },
                    "series5": {
                        id: "series5",
                        name: '搜索引擎',
                        type: 'bar',
                        stack: '总和',
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    position: 'insideRight'
                                }
                            }
                        },
                        data: "820, 832, 901, 934, 1290, 1330, 1320",
                        markPoint: false,
                        markLine: false
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
                stack: '总和',
                itemStyle: {
                    normal: {
                        label: {
                            show: true,
                            position: 'insideRight'
                        }
                    }
                }
            },
            chartOption: {
                title: {
                    text: '堆积条形图',
                    subtext: '',
                    x: 'center',
                    y: 'bottom'
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: { // 坐标轴指示器，坐标轴触发有效
                        type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
                legend: {
                    data: [],
                    x: "center",
                    y: "top"
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
                xAxis: [{
                    name: '',
                    type: 'value'
                }],
                yAxis: [{
                    type: 'category',
                    name: '',
                    data: []
                }],
                series: []
            },
            chartRelate: {
                xdataItem: 'dataValue',
                ydataItem: '',
                legendItem: ''
            }
        };
        $.extend(defalutInitOption, option);
        this.setOptions(defalutInitOption);
    };
    Widget.registerType(type, BxChartBarStandHorizontalStack);
})(WidgetObject, "chartBarT");