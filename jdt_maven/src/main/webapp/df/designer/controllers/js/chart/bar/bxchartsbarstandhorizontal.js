(function (WidgetObj, type) {
    function BxChartBarStandHorizontal() { // html节点的操作对象的定位
    }
    
     var newSerieOpt={
                name:'',
                type:'bar',
                data:"",
                markPoint : false,
                markLine : false
            };
	
    var actions = {
	        "type": type,
	        "actions": [
	        ]
	    };
    
    BxChartBarStandHorizontal.prototype = new BxChartBar(type, actions,newSerieOpt);

    BxChartBarStandHorizontal.prototype.initOption = function () {
    	var defalutInitOption = this.defaultInitOption();
        var option = {
			dataSource: {
				axisData: "巴西,印尼,美国,印度,中国,世界人口",
				seriesMap: {
					"series1": {
						id: "series1",
						name: '2011年',
						type: 'bar',
						data: "18203, 23489, 29034, 104970, 131744, 630230",
						markPoint : false,
						markLine : false
					},
					"series2": {
						id: "series2",
						name: '2012年',
						type: 'bar',
						data: "19325, 23438, 31000, 121594, 134141, 681807",
						markPoint : false,
						markLine : false
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
		            text: '世界人口总量',
		            subtext: '数据来自网络',
					x: "left",
					y: "top"
		        },
		        tooltip: {
		            trigger: 'axis'
		        },
		        legend: {
		            data: [],
                    x: "center",
                    y: "bottom"
		        },
		        toolbox: {
				        show : true,
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
                    name:'万',
		            boundaryGap: [0, 0.01]
		        }],
		        yAxis: [{
		            type: 'category',
                    name:'',
		            data: []
		        }],
		        series: []
		    },
            chartRelate : {
                xdataItem : 'dataValue',
                ydataItem : '',
                legendItem : ''
            }
        };
        $.extend(defalutInitOption, option);
        this.setOptions(defalutInitOption);
    };
    Widget.registerType(type, BxChartBarStandHorizontal);
})(WidgetObject, "chartBar");