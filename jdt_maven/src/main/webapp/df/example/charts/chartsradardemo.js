var chartsRadarOption_stand = {
    dataPattern: 'local',
    chartOption: {
        title: {
            text: '预算 vs 开销（Budget vs spending）',
            subtext: '纯属虚构'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            orient: 'vertical',
            x: 'right',
            y: 'bottom',
            data: ['预算分配（Allocated Budget）', '实际开销（Actual Spending）']
        },
        toolbox: {
            show: true,
            feature: {
                mark: {
                    show: true
                },
                dataView: {
                    show: true,
                    readOnly: false
                },
                restore: {
                    show: true
                },
                saveAsImage: {
                    show: true
                }
            }
        },
        polar: [{
            indicator: [{
                text: '销售（sales）',
                max: 6000
            },
            {
                text: '管理（Administration）',
                max: 16000
            },
            {
                text: '信息技术（Information Techology）',
                max: 30000
            },
            {
                text: '客服（Customer Support）',
                max: 38000
            },
            {
                text: '研发（Development）',
                max: 52000
            },
            {
                text: '市场（Marketing）',
                max: 25000
            }]
        }],
        calculable: true,
        series: [{
            name: '预算 vs 开销（Budget vs spending）',
            type: 'radar',
            data: [{
                value: [4300, 10000, 28000, 35000, 50000, 19000],
                name: '预算分配（Allocated Budget）'
            },
            {
                value: [5000, 14000, 28000, 31000, 42000, 21000],
                name: '实际开销（Actual Spending）'
            }]
        }]
    }
}

var chartsRadarOption_custom = {
    dataPattern: 'local',
    chartOption: {
        title: {
            text: '自定义雷达图'
        },
        legend: {
            data: ['图一','图二', '张三', '李四']
        },
        radar: [
            {
                indicator: [
                    { text: '指标一' },
                    { text: '指标二' },
                    { text: '指标三' },
                    { text: '指标四' },
                    { text: '指标五' }
                ],
                center: ['25%', '50%'],
                radius: 120,
                startAngle: 90,
                splitNumber: 4,
                shape: 'circle',
                name: {
                    formatter:'【{value}】',
                    textStyle: {
                        color:'#72ACD1'
                    }
                },
                splitArea: {
                    areaStyle: {
                        color: ['rgba(114, 172, 209, 0.2)',
                            'rgba(114, 172, 209, 0.4)', 'rgba(114, 172, 209, 0.6)',
                            'rgba(114, 172, 209, 0.8)', 'rgba(114, 172, 209, 1)'],
                        shadowColor: 'rgba(0, 0, 0, 0.3)',
                        shadowBlur: 10
                    }
                },
                axisLine: {
                    lineStyle: {
                        color: 'rgba(255, 255, 255, 0.5)'
                    }
                },
                splitLine: {
                    lineStyle: {
                        color: 'rgba(255, 255, 255, 0.5)'
                    }
                }
            },
            {
                indicator: [
                    { text: '语文', max: 150 },
                    { text: '数学', max: 150 },
                    { text: '英语', max: 150 },
                    { text: '物理', max: 120 },
                    { text: '化学', max: 108 },
                    { text: '生物', max: 72 }
                ],
                center: ['75%', '50%'],
                radius: 120
            }
        ],
        series: [
            {
                name: '雷达图',
                type: 'radar',
                itemStyle: {
                    emphasis: {
                        // color: 各异,
                        lineStyle: {
                            width: 4
                        }
                    }
                },
                data: [
                    {
                        value: [100, 8, 0.40, -80, 2000],
                        name: '图一',
                        symbol: 'rect',
                        symbolSize: 5,
                        lineStyle: {
                            normal: {
                                type: 'dashed'
                            }
                        }
                    },
                    {
                        value: [60, 5, 0.30, -100, 1500],
                        name: '图二',
                        areaStyle: {
                            normal: {
                                color: 'rgba(255, 255, 255, 0.5)'
                            }
                        }
                    }
                ]
            },
            {
                name: '成绩单',
                type: 'radar',
                radarIndex: 1,
                data: [
                    {
                        value: [120, 118, 130, 100, 99, 70],
                        name: '张三',
                        label: {
                            normal: {
                                show: true,
                                formatter:function(params) {
                                    return params.value;
                                }
                            }
                        }
                    },
                    {
                        value: [90, 113, 140, 30, 70, 60],
                        name: '李四',
                        areaStyle: {
                            normal: {
                                opacity: 0.9
                            }
                        }
                    }
                ]
            }
        ]
    }
}

var chartsRadarOption_mulitple = {
    dataPattern: 'local',
    chartOption: {
        title: {
            text: '多雷达图',
            subtext: '纯属虚构'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            x: 'center',
            data: ['某软件', '某主食手机', '某水果手机', '降水量', '蒸发量']
        },
        toolbox: {
            show: true,
            feature: {
                mark: {
                    show: true
                },
                dataView: {
                    show: true,
                    readOnly: false
                },
                restore: {
                    show: true
                },
                saveAsImage: {
                    show: true
                }
            }
        },
        calculable: true,
        polar: [{
            indicator: [{
                text: '品牌',
                max: 100
            },
            {
                text: '内容',
                max: 100
            },
            {
                text: '可用性',
                max: 100
            },
            {
                text: '功能',
                max: 100
            }],
            center: ['25%', 200],
            radius: 80
        },
        {
            indicator: [{
                text: '外观',
                max: 100
            },
            {
                text: '拍照',
                max: 100
            },
            {
                text: '系统',
                max: 100
            },
            {
                text: '性能',
                max: 100
            },
            {
                text: '屏幕',
                max: 100
            }],
            radius: 80
        },
        {
            indicator: (function() {
                var res = [];
                for (var i = 1; i <= 12; i++) {
                    res.push({
                        text: i + '月',
                        max: 100
                    });
                }
                return res;
            })(),
            center: ['75%', 200],
            radius: 80
        }],
        series: [{
            type: 'radar',
            tooltip: {
                trigger: 'item'
            },
            itemStyle: {
                normal: {
                    areaStyle: {
                        type: 'default'
                    }
                }
            },
            data: [{
                value: [60, 73, 85, 40],
                name: '某软件'
            }]
        },
        {
            type: 'radar',
            polarIndex: 1,
            data: [{
                value: [85, 90, 90, 95, 95],
                name: '某主食手机'
            },
            {
                value: [95, 80, 95, 90, 93],
                name: '某水果手机'
            }]
        },
        {
            type: 'radar',
            polarIndex: 2,
            itemStyle: {
                normal: {
                    areaStyle: {
                        type: 'default'
                    }
                }
            },
            data: [{
                name: '降水量',
                value: [2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 75.6, 82.2, 48.7, 18.8, 6.0, 2.3],
            },
            {
                name: '蒸发量',
                value: [2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 35.6, 62.2, 32.6, 20.0, 6.4, 3.3]
            }]
        }]
    }
}
