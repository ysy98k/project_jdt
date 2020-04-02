(function (WidgetObj, type) {
    function BxChartLineSmooth() { // html节点的操作对象的定位
    }
    
    var newSerieOpt={
                name:'',
                type:'line',
                data:"",
                markPoint : false,
                markLine : false,
                smooth: true,
		        itemStyle: {
		                normal: {
		                    lineStyle: {
		                        shadowColor: 'rgba(0,0,0,0.4)'
		                    }
		                }
		            }
            };
	
    var actions = {
	        "type": type,
	        "actions": [
	        ]
	    };
    
    BxChartLineSmooth.prototype = new BxChartLine(type, actions,newSerieOpt);

    BxChartLineSmooth.prototype.initOption = function () {
    	var defalutInitOption = this.defaultInitOption();
        var option = {
            dataSource: {
                axisData: "0,10,20,30,40,50,60,70,80",
                seriesMap: {
                    "series1":{
                        id:'series1',
                        name: '高度(km)与气温(°C)变化关系',
                        type: 'line',
                        smooth: true,
                        itemStyle: {
                            normal: {
                                lineStyle: {
                                    shadowColor: 'rgba(0,0,0,0.4)'
                                }
                            }
                        },
                        data: "15, -50, -56.5, -46.5, -22.1, -2.5, -27.7, -55.7, -76.5",
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
                    text: '高度(km)与气温(°C)变化关系',
                    subtext: '平滑折线图',
                    x: 'left',
                    y: 'top'
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
                calculable: true,
                xAxis: [{
                    type: 'value',
                    name:'°C'
                }],
                yAxis: [{
                    type: 'category',
                    name:'km',
                    axisLine: {
                        onZero: false
                    },
                    boundaryGap: false,
                    data: []
                }],
                series: []
            },
            "chartRelate" : {
                "xdataItem" : 'dataValue',
                "ydataItem" : '',
                "legendItem" : ''
            }
        };
        $.extend(defalutInitOption, option);
        this.setOptions(defalutInitOption);
    };
    
    Widget.registerType(type, BxChartLineSmooth);
})(WidgetObject, "chartLineS");