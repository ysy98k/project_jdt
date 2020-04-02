(function (WidgetObj, type) {
    function BxChartLineStand() { // html节点的操作对象的定位
    }
    
    var newSerieOpt={
                name:'',
                type:'line',
                data:"",
                markPoint : false,
                markLine : false
            };

    var actions = {
	        "type": type,
	        "actions": [
	        ]
	    };

    BxChartLineStand.prototype = new BxChartLine(type, actions,newSerieOpt);

    BxChartLineStand.prototype.initOption = function () {
    	var defalutInitOption = this.defaultInitOption();
        var option = {
            dataSource: {
                axisData: "1月,2月,3月,4月,5月,6月,7月,8月,9月,10月,11月,12月",
                seriesMap: {
                    "series1": {
                        id:'series1',
                        "name": "蒸发量",
                        "type": "line",
                        "data": "2, 4.9, 7, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20, 6.4, 3.3",
                        "markPoint": {
                            "data": [{
                                "type": "max",
                                "name": "最大值"
                            }, {
                                "type": "min",
                                "name": "最小值"
                            }]
                        },
                        "markLine": {
                            "data": [{
                                "type": "average",
                                "name": "平均值"
                            }]
                        }
                    },
                    "series2": {
                        id:'series2',
                        "name": "降水量",
                        "type": "line",
                        "data": "2.6, 5.9, 9, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6, 2.3",
                        "markPoint": {
                            "data": [{
                                "type": "max",
                                "name": "最大值"
                            }, {
                                "type": "min",
                                "name": "最小值"
                            }]
                        },
                        "markLine": {
                            "data": [{
                                "type": "average",
                                "name": "平均值"
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
            "chartOption": {
                "title": {
                    "text": "某地区蒸发量和降水量",
                    "subtext": "标准折线图",
                    "x": "left",
                    "y": "top"
                },
                "tooltip": {
                    "trigger": "axis"
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
                "legend": {
                    "data": [],
                    "x": "center",
                    "y": "bottom"
                },
                "calculable": true,
                "xAxis": [{
                    "type": "category",
                    "name":'',
                    "data": []
                }],
                "yAxis": [{
                    "name":'',
                    "type": "value"
                }],
                "series": []
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

    Widget.registerType(type, BxChartLineStand);
})(WidgetObject, "chartLine");