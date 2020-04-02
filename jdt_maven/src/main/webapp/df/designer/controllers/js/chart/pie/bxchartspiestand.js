(function (WidgetObj, type) {
    function BxChartPieStand() { // html节点的操作对象的定位
    }
    var initDataOpt={"value":0, "name":""};
    var actions = {
	        "type": type,
	        "actions": [
	        ]
	    };

    BxChartPieStand.prototype = new BxChartPie(type, actions,initDataOpt);

    BxChartPieStand.prototype.initOption = function () {
    	var defalutInitOption = this.defaultInitOption();
        var option = {
            dataSource: {
                seriesMap: {
                    "series1":{
                        id:"series1",
                        value: "335",
                        name: '焦化'

                    },
                    "series2":{
                        id:"series2",
                        value: "310",
                        name: '炼铁'

                    },
                    "series3":{
                        id:"series3",
                        value: "234",
                        name: '石灰'
                    },
                    "series4":{
                        id:"series4",
                        value: "135",
                        name: '轧材'
                    },
                    "series5":{
                        id:"series5",
                        value: "1548",
                        name: '能源中心'
                    }
                },
                dynaSeries: false,
                dynaSeriesData: {data:""}
            },
            "chartOption": {
				    "title" : {
				        "text": "某站点用户访问来源",
				        "subtext": "标准饼图",
				         "x": "center",
                         "y": "top"
				    },
				    "tooltip" : {
				        "trigger": "item",
				        "formatter": "{a} <br/>{b} : {c} ({d}%)"
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
				        "x" : "center",
				        "y": "bottom",
				        "data":[]
				    },   
				    "calculable" : true,
				    "series" : [
				        {
				            "name":"访问来源",
				            "type":"pie",
				            "radius" : "55%",
				            "data":[]
				        }
				    ]
            }
        };
        $.extend(defalutInitOption, option);
        this.setOptions(defalutInitOption);
    };

    Widget.registerType(type, BxChartPieStand);
})(WidgetObject, "chartPie");