var chartsMapOption_stand = {
    dataPattern: 'local',
    chartOption: {
        tooltip: {
            trigger: 'item',
            formatter: '{b}'
        },
        series: [
            {
                name: '中国',
                type: 'map',
                mapType: 'china',
                selectedMode : 'multiple',
                label: {
                    normal: {
                        show: true
                    },
                    emphasis: {
                        show: true
                    }
                },
                data:[
                    {name:'广东', selected:true}
                ]
            }
        ]
    }
}


var chartsMapOption_iphone = {
    dataPattern: 'local',
    chartOption : {
        title: {
            text: 'iphone销量',
            subtext: '纯属虚构',
            left: 'center'
        },
        tooltip: {
            trigger: 'item'
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data:['iphone3','iphone4','iphone5']
        },
        visualMap: {
            min: 0,
            max: 2500,
            left: 'left',
            top: 'bottom',
            text: ['高','低'],           // 文本，默认为数值文本
            calculable: true
        },
        toolbox: {
            show: true,
            orient: 'vertical',
            left: 'right',
            top: 'center',
            feature: {
                dataView: {readOnly: false},
                restore: {},
                saveAsImage: {}
            }
        },
        series: [
            {
                name: 'iphone3',
                type: 'map',
                mapType: 'china',
                roam: false,
                label: {
                    normal: {
                        show: true
                    },
                    emphasis: {
                        show: true
                    }
                },
                data:[
                    {name: '北京',value: Math.round(Math.random()*1000) },
                    {name: '天津',value: Math.round(Math.random()*1000) },
                    {name: '上海',value: Math.round(Math.random()*1000) },
                    {name: '重庆',value: Math.round(Math.random()*1000) },
                    {name: '河北',value: Math.round(Math.random()*1000) },
                    {name: '河南',value: Math.round(Math.random()*1000) },
                    {name: '云南',value: Math.round(Math.random()*1000) },
                    {name: '辽宁',value: Math.round(Math.random()*1000) },
                    {name: '黑龙江',value: Math.round(Math.random()*1000) },
                    {name: '湖南',value: Math.round(Math.random()*1000) },
                    {name: '安徽',value: Math.round(Math.random()*1000) },
                    {name: '山东',value: Math.round(Math.random()*1000) },
                    {name: '新疆',value: Math.round(Math.random()*1000) },
                    {name: '江苏',value: Math.round(Math.random()*1000) },
                    {name: '浙江',value: Math.round(Math.random()*1000) },
                    {name: '江西',value: Math.round(Math.random()*1000) },
                    {name: '湖北',value: Math.round(Math.random()*1000) },
                    {name: '广西',value: Math.round(Math.random()*1000) },
                    {name: '甘肃',value: Math.round(Math.random()*1000) },
                    {name: '山西',value: Math.round(Math.random()*1000) },
                    {name: '内蒙古',value: Math.round(Math.random()*1000) },
                    {name: '陕西',value: Math.round(Math.random()*1000) },
                    {name: '吉林',value: Math.round(Math.random()*1000) },
                    {name: '福建',value: Math.round(Math.random()*1000) },
                    {name: '贵州',value: Math.round(Math.random()*1000) },
                    {name: '广东',value: Math.round(Math.random()*1000) },
                    {name: '青海',value: Math.round(Math.random()*1000) },
                    {name: '西藏',value: Math.round(Math.random()*1000) },
                    {name: '四川',value: Math.round(Math.random()*1000) },
                    {name: '宁夏',value: Math.round(Math.random()*1000) },
                    {name: '海南',value: Math.round(Math.random()*1000) },
                    {name: '台湾',value: Math.round(Math.random()*1000) },
                    {name: '香港',value: Math.round(Math.random()*1000) },
                    {name: '澳门',value: Math.round(Math.random()*1000) }
                ]
            },
            {
                name: 'iphone4',
                type: 'map',
                mapType: 'china',
                label: {
                    normal: {
                        show: true
                    },
                    emphasis: {
                        show: true
                    }
                },
                data:[
                    {name: '北京',value: Math.round(Math.random()*1000) },
                    {name: '天津',value: Math.round(Math.random()*1000) },
                    {name: '上海',value: Math.round(Math.random()*1000) },
                    {name: '重庆',value: Math.round(Math.random()*1000) },
                    {name: '河北',value: Math.round(Math.random()*1000) },
                    {name: '安徽',value: Math.round(Math.random()*1000) },
                    {name: '新疆',value: Math.round(Math.random()*1000) },
                    {name: '浙江',value: Math.round(Math.random()*1000) },
                    {name: '江西',value: Math.round(Math.random()*1000) },
                    {name: '山西',value: Math.round(Math.random()*1000) },
                    {name: '内蒙古',value: Math.round(Math.random()*1000) },
                    {name: '吉林',value: Math.round(Math.random()*1000) },
                    {name: '福建',value: Math.round(Math.random()*1000) },
                    {name: '广东',value: Math.round(Math.random()*1000) },
                    {name: '西藏',value: Math.round(Math.random()*1000) },
                    {name: '四川',value: Math.round(Math.random()*1000) },
                    {name: '宁夏',value: Math.round(Math.random()*1000) },
                    {name: '香港',value: Math.round(Math.random()*1000) },
                    {name: '澳门',value: Math.round(Math.random()*1000) }
                ]
            },
            {
                name: 'iphone5',
                type: 'map',
                mapType: 'china',
                label: {
                    normal: {
                        show: true
                    },
                    emphasis: {
                        show: true
                    }
                },
                data:[
                    {name: '北京',value: Math.round(Math.random()*1000) },
                    {name: '天津',value: Math.round(Math.random()*1000) },
                    {name: '上海',value: Math.round(Math.random()*1000) },
                    {name: '广东',value: Math.round(Math.random()*1000) },
                    {name: '台湾',value: Math.round(Math.random()*1000) },
                    {name: '香港',value: Math.round(Math.random()*1000) },
                    {name: '澳门',value: Math.round(Math.random()*1000) }
                ]
            }
        ]
    }
}
