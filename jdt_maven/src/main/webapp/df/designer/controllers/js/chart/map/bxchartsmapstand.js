(function (WidgetObj, type) {
    function BxChartMapStand() { // html节点的操作对象的定位
    }

    var newSerieOpt = {
        name: '',
        type: 'map',
        mapType: 'china',
        roam: false,
        itemStyle: {
            normal: {
                label: {show: true}
            },
            emphasis: {
                label: {show: true}
            }
        },
        data: ""
    };

    var actions = {
        "type": type,
        "actions": []
    };

    BxChartMapStand.prototype = new BxChartMap(type, actions, newSerieOpt);

    BxChartMapStand.prototype.initOption = function () {
        var defalutInitOption = this.defaultInitOption();
        var option = {
            dataSource: {
                axisData: "北京,天津,上海,重庆,河北,河南,云南,辽宁,黑龙江,湖南,安徽,"
                + "山东,新疆,江苏,浙江,江西,湖北,广西,甘肃,山西,内蒙古,陕西,吉林,"
                + "福建,贵州,广东,青海,西藏,四川,宁夏,海南,台湾,香港,澳门",
                seriesMap: {
                    "series1": {
                        id: "series1",
                        name: 'iphone5',
                        type: 'map',
                        mapType: 'china',
                        roam: false,
                        itemStyle: {
                            normal: {
                                label: {show: true}
                            },
                            emphasis: {
                                label: {show: true}
                            }
                        },
                        data: "286,455,492,333,379,290,221,223,143,393,396,"
                        + "369,570,379,430,253,293,393,283,210,341,253,299"
                        + "415,209,250,121,60,151,86,169,48,36,39"
                    },
                    "series2": {
                        id: "series2",
                        name: 'iphone6',
                        type: 'map',
                        mapType: 'china',
                        roam: false,
                        itemStyle: {
                            normal: {
                                label: {show: true}
                            },
                            emphasis: {
                                label: {show: true}
                            }
                        },
                        data: "661,724,856,763,523,660,456,532,135,699,348,"
                        + "753,530,778,887,663,625,555,583,690,561,893,889"
                        + "795,989,760,469,293,361,324,259,158,96,169"
                    },
                    "series3": {
                        id: "series3",
                        name: 'iphone7',
                        type: 'map',
                        mapType: 'china',
                        roam: false,
                        itemStyle: {
                            normal: {
                                label: {show: true}
                            },
                            emphasis: {
                                label: {show: true}
                            }
                        },
                        data: "1153,105,1032,963,869,790,666,753,254,787,693,"
                        + "963,860,529,999,856,1093,935,785,818,573,1063,863"
                        + "967,1125,861,570,308,971,693,1058,463,588,561"
                    }
                },
                dynaSeries: false,
                dynaSeriesData: {data:""}
            },
            chartOption: {
                title: {
                    text: 'iphone销量',
                    subtext: '标准地图',
                    x: 'center',
                    y: 'top'
                },
                toolbox: {show: false},
                tooltip: {trigger: 'item'},
                legend: {
                    orient: 'vertical',
                    x: 'left',
                    y: 'top',
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
                dataRange: {
                    min: 0,
                    max: 2500,
                    x: 'left',
                    y: 'bottom',
                    text: ['高', '低'],
                    // 文本，默认为数值文本
                    calculable: true
                },
                roamController: {show: false,},
                series: []
            }
        };
        $.extend(defalutInitOption, option);
        this.setOptions(defalutInitOption);
    };

    Widget.registerType(type, BxChartMapStand);
})(WidgetObject, "chartMap");