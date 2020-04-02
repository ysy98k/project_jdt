var chartsChordOption_stand = {
    dataPattern: 'local',
    chartOption: {
        title: {
            text: '测试数据',
            subtext: 'From d3.js',
            x: 'right',
            y: 'bottom'
        },
        tooltip: {
            trigger: 'item',
            formatter: function(params) {
                if (params.indicator2) { // is edge
                    return params.value.weight;
                } else { // is node
                    return params.name
                }
            }
        },
        toolbox: {
            show: true,
            feature: {
                restore: {
                    show: true
                },
                magicType: {
                    show: true,
                    type: ['force', 'chord']
                },
                saveAsImage: {
                    show: true
                }
            }
        },
        legend: {
            x: 'left',
            data: ['group1', 'group2', 'group3', 'group4']
        },
        series: [{
            type: 'chord',
            sort: 'ascending',
            sortSub: 'descending',
            showScale: true,
            showScaleText: true,
            data: [{
                name: 'group1'
            },
            {
                name: 'group2'
            },
            {
                name: 'group3'
            },
            {
                name: 'group4'
            }],
            itemStyle: {
                normal: {
                    label: {
                        show: false
                    }
                }
            },
            matrix: [[11975, 5871, 8916, 2868], [1951, 10048, 2060, 6171], [8010, 16145, 8090, 8045], [1013, 990, 940, 6907]]
        }]
    }
}

var chartsChordOption_multiple = {
    dataPattern: 'local',
    chartOption: {
        color: ['#FBB367', '#80B1D2', '#FB8070', '#CC99FF', '#B0D961', '#99CCCC', '#BEBBD8', '#FFCC99', '#8DD3C8', '#FF9999', '#CCEAC4', '#BB81BC', '#FBCCEC', '#CCFF66', '#99CC66', '#66CC66', '#FF6666', '#FFED6F', '#ff7f50', '#87cefa', ],
        title: {
            text: '中东地区的敌友关系',
            subtext: '数据来自财新网',
            sublink: 'http://international.caixin.com/2013-09-06/100579154.html',
            x: 'right',
            y: 'bottom'
        },
        toolbox: {
            show: true,
            feature: {
                restore: {
                    show: true
                },
                magicType: {
                    show: true,
                    type: ['force', 'chord']
                },
                saveAsImage: {
                    show: true
                }
            }
        },
        tooltip: {
            trigger: 'item',
            formatter: function(params) {
                if (params.name && params.name.indexOf('-') != -1) {
                    return params.name.replace('-', ' ' + params.seriesName + ' ')
                } else {
                    return params.name ? params.name: params.data.id
                }
            }
        },
        legend: {
            data: ['美国', '叙利亚反对派', '阿萨德', '伊朗', '塞西', '哈马斯', '以色列', '穆斯林兄弟会', '基地组织', '俄罗斯', '黎巴嫩什叶派', '土耳其', '卡塔尔', '沙特', '黎巴嫩逊尼派', '', '支持', '反对', '未表态'],
            orient: 'vertical',
            x: 'left'
        },
        series: [{
            "name": "支持",
            "type": "chord",
            "showScaleText": false,
            "clockWise": false,
            "data": [{
                "name": "美国"
            },
            {
                "name": "叙利亚反对派"
            },
            {
                "name": "阿萨德"
            },
            {
                "name": "伊朗"
            },
            {
                "name": "塞西"
            },
            {
                "name": "哈马斯"
            },
            {
                "name": "以色列"
            },
            {
                "name": "穆斯林兄弟会"
            },
            {
                "name": "基地组织"
            },
            {
                "name": "俄罗斯"
            },
            {
                "name": "黎巴嫩什叶派"
            },
            {
                "name": "土耳其"
            },
            {
                "name": "卡塔尔"
            },
            {
                "name": "沙特"
            },
            {
                "name": "黎巴嫩逊尼派"
            }],
            "matrix": [[0, 100, 0, 0, 0, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0], [10, 0, 0, 0, 0, 10, 10, 0, 10, 0, 0, 10, 10, 10, 10], [0, 0, 0, 10, 0, 0, 0, 0, 0, 10, 10, 0, 0, 0, 0], [0, 0, 100, 0, 0, 100, 0, 0, 0, 0, 100, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0], [0, 100, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0], [10, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 10, 0, 0], [0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 100, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 100, 0, 0, 0, 0, 0, 100, 0, 0, 0, 0, 0, 0, 0], [0, 100, 0, 0, 0, 100, 0, 100, 0, 0, 0, 0, 0, 0, 0], [0, 100, 0, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100], [0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0]]
        },
        {
            "name": "反对",
            "type": "chord",
            "insertToSerie": "支持",
            "data": [{
                "name": "美国"
            },
            {
                "name": "叙利亚反对派"
            },
            {
                "name": "阿萨德"
            },
            {
                "name": "伊朗"
            },
            {
                "name": "塞西"
            },
            {
                "name": "哈马斯"
            },
            {
                "name": "以色列"
            },
            {
                "name": "穆斯林兄弟会"
            },
            {
                "name": "基地组织"
            },
            {
                "name": "俄罗斯"
            },
            {
                "name": "黎巴嫩什叶派"
            },
            {
                "name": "土耳其"
            },
            {
                "name": "卡塔尔"
            },
            {
                "name": "沙特"
            },
            {
                "name": "黎巴嫩逊尼派"
            }],
            "matrix": [[0, 0, 100, 100, 0, 100, 0, 0, 100, 0, 0, 0, 0, 0, 0], [0, 0, 0, 10, 0, 0, 0, 0, 0, 10, 10, 0, 0, 0, 0], [10, 0, 0, 0, 0, 0, 10, 10, 10, 0, 0, 10, 10, 0, 10], [10, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 10, 0, 100, 0, 0, 0, 10, 10, 0, 0], [10, 0, 0, 0, 100, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 100, 0, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 100, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0], [10, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 0], [0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 100, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 100, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 100, 10, 0, 0, 0, 0, 0, 0], [0, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]]
        },
        {
            "name": "未表态",
            "type": "chord",
            "insertToSerie": "支持",
            "data": [{
                "name": "美国"
            },
            {
                "name": "叙利亚反对派"
            },
            {
                "name": "阿萨德"
            },
            {
                "name": "伊朗"
            },
            {
                "name": "塞西"
            },
            {
                "name": "哈马斯"
            },
            {
                "name": "以色列"
            },
            {
                "name": "穆斯林兄弟会"
            },
            {
                "name": "基地组织"
            },
            {
                "name": "俄罗斯"
            },
            {
                "name": "黎巴嫩什叶派"
            },
            {
                "name": "土耳其"
            },
            {
                "name": "卡塔尔"
            },
            {
                "name": "沙特"
            },
            {
                "name": "黎巴嫩逊尼派"
            }],
            "matrix": [[0, 0, 0, 0, 100, 0, 0, 100, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]]
        }]
    }
}

var chartsChordOption_football = {
    dataPattern: 'local',
    chartOption: {
        title: {
            text: '德国队效力联盟',
            x: 'right',
            y: 'bottom'
        },
        tooltip: {
            trigger: 'item',
            formatter: function(params) {
                if (params.indicator2) { // is edge
                    return params.indicator2 + ' ' + params.name + ' ' + params.indicator;
                } else { // is node
                    return params.name
                }
            }
        },
        toolbox: {
            show: true,
            feature: {
                restore: {
                    show: true
                },
                magicType: {
                    show: true,
                    type: ['force', 'chord']
                },
                saveAsImage: {
                    show: true
                }
            }
        },
        legend: {
            x: 'left',
            data: ['阿森纳', '拜仁慕尼黑', '多特蒙德']
        },
        series: [{
            type: 'chord',
            sort: 'ascending',
            sortSub: 'descending',
            showScale: false,
            itemStyle: {
                normal: {
                    label: {
                        rotate: true
                    }
                }
            },
            // 使用 nodes links 表达和弦图
            nodes: [{
                name: '默特萨克'
            },
            {
                name: '厄齐尔'
            },
            {
                name: '波多尔斯基'
            },
            {
                name: '诺伊尔'
            },
            {
                name: '博阿滕'
            },
            {
                name: '施魏因施泰格'
            },
            {
                name: '拉姆'
            },
            {
                name: '克罗斯'
            },
            {
                name: '穆勒'
            },
            {
                name: '格策'
            },
            {
                name: '胡梅尔斯'
            },
            {
                name: '魏登费勒'
            },
            {
                name: '杜尔姆'
            },
            {
                name: '格罗斯克罗伊茨'
            },
            {
                name: '阿森纳'
            },
            {
                name: '拜仁慕尼黑'
            },
            {
                name: '多特蒙德'
            }],
            links: [{
                source: '阿森纳',
                target: '默特萨克',
                weight: 0.9,
                name: '效力'
            },
            {
                source: '阿森纳',
                target: '厄齐尔',
                weight: 0.9,
                name: '效力'
            },
            {
                source: '阿森纳',
                target: '波多尔斯基',
                weight: 0.9,
                name: '效力'
            },
            {
                source: '拜仁慕尼黑',
                target: '诺伊尔',
                weight: 0.9,
                name: '效力'
            },
            {
                source: '拜仁慕尼黑',
                target: '博阿滕',
                weight: 0.9,
                name: '效力'
            },
            {
                source: '拜仁慕尼黑',
                target: '施魏因施泰格',
                weight: 0.9,
                name: '效力'
            },
            {
                source: '拜仁慕尼黑',
                target: '拉姆',
                weight: 0.9,
                name: '效力'
            },
            {
                source: '拜仁慕尼黑',
                target: '克罗斯',
                weight: 0.9,
                name: '效力'
            },
            {
                source: '拜仁慕尼黑',
                target: '穆勒',
                weight: 0.9,
                name: '效力'
            },
            {
                source: '拜仁慕尼黑',
                target: '格策',
                weight: 0.9,
                name: '效力'
            },
            {
                source: '多特蒙德',
                target: '胡梅尔斯',
                weight: 0.9,
                name: '效力'
            },
            {
                source: '多特蒙德',
                target: '魏登费勒',
                weight: 0.9,
                name: '效力'
            },
            {
                source: '多特蒙德',
                target: '杜尔姆',
                weight: 0.9,
                name: '效力'
            },
            {
                source: '多特蒙德',
                target: '格罗斯克罗伊茨',
                weight: 0.9,
                name: '效力'
            },

            // Ribbon Type 的和弦图每一对节点之间必须是双向边
            {
                target: '阿森纳',
                source: '默特萨克',
                weight: 1
            },
            {
                target: '阿森纳',
                source: '厄齐尔',
                weight: 1
            },
            {
                target: '阿森纳',
                source: '波多尔斯基',
                weight: 1
            },
            {
                target: '拜仁慕尼黑',
                source: '诺伊尔',
                weight: 1
            },
            {
                target: '拜仁慕尼黑',
                source: '博阿滕',
                weight: 1
            },
            {
                target: '拜仁慕尼黑',
                source: '施魏因施泰格',
                weight: 1
            },
            {
                target: '拜仁慕尼黑',
                source: '拉姆',
                weight: 1
            },
            {
                target: '拜仁慕尼黑',
                source: '克罗斯',
                weight: 1
            },
            {
                target: '拜仁慕尼黑',
                source: '穆勒',
                weight: 1
            },
            {
                target: '拜仁慕尼黑',
                source: '格策',
                weight: 1
            },
            {
                target: '多特蒙德',
                source: '胡梅尔斯',
                weight: 1
            },
            {
                target: '多特蒙德',
                source: '魏登费勒',
                weight: 1
            },
            {
                target: '多特蒙德',
                source: '杜尔姆',
                weight: 1
            },
            {
                target: '多特蒙德',
                source: '格罗斯克罗伊茨',
                weight: 1
            }]
        }]
    }
}

//非缎带和弦图
var chartsChordOption_line = {
    dataPattern: 'local',
    chartOption: {
        title: {
            text: '德国队效力联盟',
            x: 'right',
            y: 'bottom'
        },
        tooltip: {
            trigger: 'item',
            formatter: function(params) {
                if (params.indicator2) { // is edge
                    return params.indicator2 + ' ' + params.name + ' ' + params.indicator;
                } else { // is node
                    return params.name
                }
            }
        },
        toolbox: {
            show: true,
            feature: {
                restore: {
                    show: true
                },
                magicType: {
                    show: true,
                    type: ['force', 'chord']
                },
                saveAsImage: {
                    show: true
                }
            }
        },
        legend: {
            x: 'left',
            data: ['阿森纳', '拜仁慕尼黑', '多特蒙德']
        },
        series: [{
            name: '德国队效力联盟',
            type: 'chord',
            sort: 'ascending',
            sortSub: 'descending',
            ribbonType: false,
            radius: '60%',
            itemStyle: {
                normal: {
                    label: {
                        rotate: true
                    }
                }
            },
            minRadius: 7,
            maxRadius: 20,
            // 使用 nodes links 表达和弦图
            nodes: [{
                name: '默特萨克'
            },
            {
                name: '厄齐尔'
            },
            {
                name: '波多尔斯基'
            },
            {
                name: '诺伊尔'
            },
            {
                name: '博阿滕'
            },
            {
                name: '施魏因施泰格'
            },
            {
                name: '拉姆'
            },
            {
                name: '克罗斯'
            },
            {
                name: '穆勒',
                symbol: 'star'
            },
            {
                name: '格策'
            },
            {
                name: '胡梅尔斯'
            },
            {
                name: '魏登费勒'
            },
            {
                name: '杜尔姆'
            },
            {
                name: '格罗斯克罗伊茨'
            },
            {
                name: '阿森纳'
            },
            {
                name: '拜仁慕尼黑'
            },
            {
                name: '多特蒙德'
            }],
            links: [{
                source: '阿森纳',
                target: '默特萨克',
                weight: 1,
                name: '效力'
            },
            {
                source: '阿森纳',
                target: '厄齐尔',
                weight: 1,
                name: '效力'
            },
            {
                source: '阿森纳',
                target: '波多尔斯基',
                weight: 1,
                name: '效力'
            },
            {
                source: '拜仁慕尼黑',
                target: '诺伊尔',
                weight: 1,
                name: '效力'
            },
            {
                source: '拜仁慕尼黑',
                target: '博阿滕',
                weight: 1,
                name: '效力'
            },
            {
                source: '拜仁慕尼黑',
                target: '施魏因施泰格',
                weight: 1,
                name: '效力'
            },
            {
                source: '拜仁慕尼黑',
                target: '拉姆',
                weight: 1,
                name: '效力'
            },
            {
                source: '拜仁慕尼黑',
                target: '克罗斯',
                weight: 1,
                name: '效力'
            },
            {
                source: '拜仁慕尼黑',
                target: '穆勒',
                weight: 1,
                name: '效力'
            },
            {
                source: '拜仁慕尼黑',
                target: '格策',
                weight: 1,
                name: '效力'
            },
            {
                source: '多特蒙德',
                target: '胡梅尔斯',
                weight: 1,
                name: '效力'
            },
            {
                source: '多特蒙德',
                target: '魏登费勒',
                weight: 1,
                name: '效力'
            },
            {
                source: '多特蒙德',
                target: '杜尔姆',
                weight: 1,
                name: '效力'
            },
            {
                source: '多特蒙德',
                target: '格罗斯克罗伊茨',
                weight: 1,
                name: '效力'
            }]
        }]
    }
}