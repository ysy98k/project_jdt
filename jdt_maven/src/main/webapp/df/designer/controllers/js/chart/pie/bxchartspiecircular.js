(function (WidgetObj, type) {
    function BxChartPieCircular() { // html节点的操作对象的定位
    }
    var initDataOpt={"value":0, "name":""};
    var actions = {
	        "type": type,
	        "actions": [
	        ]
	    };
	
    BxChartPieCircular.prototype = new BxChartPie(type, actions,initDataOpt);

    BxChartPieCircular.prototype.initOption = function () {
    	var defalutInitOption = this.defaultInitOption();
        var option = {
            dataSource: {
                seriesMap: {
                    "series1":{
                        id:"series1",
                        value: "335",
                        name: '直接访问'

                    },
                    "series2":{
                        id:"series2",
                        value: "310",
                        name: '邮件营销'

                    },
                    "series3":{
                        id:"series3",
                        value: "234",
                        name: '联盟广告'
                    },
                    "series4":{
                        id:"series4",
                        value: "135",
                        name: '视频广告'
                    },
                    "series5":{
                        id:"series5",
                        value: "1548",
                        name: '搜索引擎'
                    }
                },
                dynaSeries: false,
                dynaSeriesData: {data:""}
            },
		    chartOption : {
                "title" : {
                    "text": "某站点用户访问来源",
                    "subtext": "标准环形图",
                    "x": "center",
                    "y": "bottom"
                },
		        tooltip: {
		            trigger: 'item',
		            formatter: "{a} <br/>{b} : {c} ({d}%)"
		        },
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
		        calculable: true,
		        series: [{
		            name: '访问来源',
		            type: 'pie',
		            radius: ['50%', '70%'],
		            itemStyle: {
		                normal: {
		                    label: {
		                        show: false
		                    },
		                    labelLine: {
		                        show: false
		                    }
		                },
		                emphasis: {
		                    label: {
		                        show: true,
		                        position: 'center',
		                        textStyle: {
		                            fontSize: '30',
		                            fontWeight: 'bold'
		                        }
		                    }
		                }
		            },
		            data: []
		        }]
		    }
        };
        $.extend(defalutInitOption, option);
        this.setOptions(defalutInitOption);
    };
    Widget.registerType(type, BxChartPieCircular);
})(WidgetObject, "chartPieC");