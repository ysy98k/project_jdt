(function (WidgetObj, type) {
    function BxChartLineArea() { // html节点的操作对象的定位
    }
    
    var actions = {
	        "type": type,
	        "actions": [
	        ]
	    };
    
    var newSerieOpt={
                name:'',
                type:'line',
                data:"",
                markPoint : false,
                markLine : false,
                smooth: true,
                itemStyle: {
                        normal: {
                            areaStyle: {
                                type: 'default'
                            }
                        }
                    }
            };
	
    BxChartLineArea.prototype = new BxChartLine(type, actions,newSerieOpt);

    BxChartLineArea.prototype.initOption = function () {
    	var defalutInitOption = this.defaultInitOption();
        var option = {
            dataSource: {
                axisData: "周一,周二,周三,周四,周五,周六,周日",
                seriesMap: {
                    "series1": {
                        id:'series1',
                        name: '成交',
                        type: 'line',
                        smooth: true,
                        itemStyle: {
                            normal: {
                                areaStyle: {
                                    type: 'default'
                                }
                            }
                        },
                        data: "10, 12, 21, 54, 260, 830, 710",
                        "markPoint" : false,
                        "markLine" : false
                    },
                    "series2": {
                        id:'series2',
                        name: '预购',
                        type: 'line',
                        smooth: true,
                        itemStyle: {
                            normal: {
                                areaStyle: {
                                    type: 'default'
                                }
                            }
                        },
                        data: "30, 182, 434, 791, 390, 30, 10",
                        "markPoint" : false,
                        "markLine" : false
                    },
                    "series3": {
                        id:'series3',
                        name: '意向',
                        type: 'line',
                        smooth: true,
                        itemStyle: {
                            normal: {
                                areaStyle: {
                                    type: 'default'
                                }
                            }
                        },
                        data: "1320, 1132, 601, 234, 120, 90, 20",
                        "markPoint" : false,
                        "markLine" : false
                    }
                },
                dynaSeries: false,
                dynaSeriesData: {
                    data:"",
                    markPoint: false,
                    markLine: false
                }
            },
            chartOption : {
                title: {
                    text: '某楼盘销售情况',
                    subtext: '标准面积图',
                    x: 'left',
                    y: 'top'
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: [],
                    "x": "center",
                    "y": "bottom"
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
                    type: 'category',
                    name:'',
                    boundaryGap: false,
                    data: []
                }],
                yAxis: [{
                    name:'',
                    type: 'value'
                }],
                series: []
            },
            "chartRelate" : {
                "xdataItem" : '',
                "ydataItem" : 'dataValue',
                "legendItem" : ''
            }
        };
        $.extend(defalutInitOption, option);
        this.setOptions(defalutInitOption);
    };
    Widget.registerType(type, BxChartLineArea);
})(WidgetObject, "chartArea");