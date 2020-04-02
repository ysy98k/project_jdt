(function (WidgetObj, type) {
    function BxChartLineStack() { // html节点的操作对象的定位
    }

    var newSerieOpt={
                name:'',
                type:'line',
                data:"",
                stack: '总和',
                markPoint : false,
                markLine : false
            };
    
    var actions = {
        "type": type,
        "actions": []
    };

    BxChartLineStack.prototype = new BxChartLine(type, actions,newSerieOpt);

    BxChartLineStack.prototype.initOption = function () {
        var defalutInitOption = this.defaultInitOption();
        var option = {
            dataSource: {
                axisData: "周一,周二,周三,周四,周五,周六,周日",
                seriesMap: {
                    "series1": {
                        id:'series1',
                        name: '邮件营销',
                        type: 'line',
                        stack: '总和',
                        data: "120, 132, 101, 134, 90, 230, 210",
                        "markPoint": false,
                        "markLine": false
                    },
                    "series2": {
                        id:'series2',
                        name: '联盟广告',
                        type: 'line',
                        stack: '总和',
                        data: "220, 182, 191, 234, 290, 330, 310",
                        "markPoint": false,
                        "markLine": false
                    },
                    "series3": {
                        id:'series3',
                        name: '视频广告',
                        type: 'line',
                        stack: '总和',
                        data: "150, 232, 201, 154, 190, 330, 410",
                        "markPoint": false,
                        "markLine": false
                    },
                    "series4": {
                        id:'series4',
                        name: '直接访问',
                        type: 'line',
                        stack: '总和',
                        data: "320, 332, 301, 334, 390, 330, 320",
                        "markPoint": false,
                        "markLine": false
                    },
                    "series5":{
                        id:'series5',
                        name: '搜索引擎',
                        type: 'line',
                        stack: '总和',
                        data: "820, 932, 901, 934, 1290, 1330, 1320",
                        "markPoint": false,
                        "markLine": false
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
                    text: '堆积折线图',
                    subtext: '',
                    x: 'left',
                    y: 'bottom'
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: [],
                    x: 'center',
                    y: 'top'
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
                    boundaryGap: false,
                    name:'',
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
    Widget.registerType(type, BxChartLineStack);
})(WidgetObject, "chartLineT");