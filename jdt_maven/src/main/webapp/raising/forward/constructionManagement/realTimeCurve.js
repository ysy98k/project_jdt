$(function(){
    initTree();
    show();
})


function show(){
    //得到选中数组。
    var nameArr = [];
    var codeArr = [];
    $("i[class~='icon-check-box-cicre']").each(function(){
        var codeObj = $(this).parent().parent();
        var code = $(codeObj).attr("data-id");
        if(!isNumber(code)){
            var text = codeObj[0].childNodes[1];
            nameArr.push(text.textContent);
            codeArr.push(code);
        }
    });
    //判断数组长度
    if(codeArr.length > 16){
        alertDiv("提示","最多显示16条数据");
        return;
    }else if(codeArr.length < 1) {
        return;
    }
    //得到InstanceId
    var instanceResult = getInstanceId(codeArr,nameArr);
    if(instanceResult.length<1){//如果是空的,
        showChart(nameArr,codeArr,[],[]);
        return;
    }
    //得到展示数据
    var result = [];
    var endTime = new Date().getTime();
    var startTime = endTime - 60*60*1000;
    (function(result,len){
        for(var i=0;i<instanceResult.length;i++){
            //var worker = new Worker("getPreviousHour.js");
            var worker = new Worker("historicalCurve2.js");
            var getRecordURL = "https://bd.sh-raising.com/tsdbrest/api/record/range/"  + instanceResult[i].instance_id + "/" + instanceResult[i].propertyId;
            var param = {};
            param.tagCode = instanceResult[i].instance_name.substring(instanceResult[i].instance_name.indexOf("_")+1);
            param.getRecordURL = getRecordURL;
            param.startTime = startTime;
            param.endTime = endTime;

            worker.postMessage(JSON.stringify(param));//向worker发送数据
            worker.onmessage = function(evt){           //接收worker传过来的数据函数
                var str = evt.data;
                var showData =  JSON.parse(str);
                var code = showData.code;
                var timArr = showData.timArr;
                if(timArr != null && timArr.length > 0 ){
                    result.xAxis;
                    if(result.xAxis != undefined && result.xAxis.length > timArr.length){
                    }else{
                        result.xAxis = timArr;
                    }
                }
                if(showData.dataArr.length > 0){
                    result[code] = showData.dataArr;
                }
                len--;
                if(len == 0){
                    //如果所有请求完毕。则展示数据。
                    showChart(nameArr,codeArr,result,instanceResult);
                }
            }
        }
    })(result,instanceResult.length);
}

function getInstanceId(lineArr,nameArr){
    var result = [];
    var wareHose = {};
    var instancesArr = [];
    for(var i =0;i<lineArr.length;i++){
        var temp = lineArr[i];
        var instanceName = $.cookie("selected_collection") + "_" + temp;
        var instanceTemp = {"instance_name": instanceName};
        wareHose[instanceName] = nameArr[i];
        instancesArr.push(instanceTemp);
    }
    //var getInstanceURL = toolkitPath.replace("jdt", "tsdbrest/api/instance");
    var getInstanceURL = "https://bd.sh-raising.com/tsdbrest/api/instance";
    $.ajax({
        url: getInstanceURL,
        type: "post",
        data: JSON.stringify(instancesArr),
        contentType: 'application/json;charset=UTF-8',
        async: false,
        dataType: "json",
        beforeSend: function(req) {
            req.setRequestHeader('Authorization', "Basic " + new Base64().encode('tsdbDataUser@raising' + ":" + '123456'));
        },
        success: function (data) {
            if (Number(data.errcode) == 0) {
                var dataArr = data.records;
                for(var i =0;i<dataArr.length;i++){
                    var dataTemp = dataArr[i];
                    var resultTemp = {};
                    resultTemp.instance_name = dataTemp.instance_name;
                    resultTemp.instance_id = dataTemp.instance_id;
                    resultTemp.name = wareHose[resultTemp.instance_name];

                    var propertiesArr = dataTemp.properties;
                    if(propertiesArr.length > 0) {
                        resultTemp.propertyId = propertiesArr[0].id;
                        resultTemp.property_ids = [propertiesArr[0].id];
                        result.push(resultTemp);
                    }
                }
            }
        },
        error:function(){

        }
    })

    return result;

}
//展示折线图
function showChart(columnArr,lineArr,result,instanceResult){
    var color = [
        "#0AFF00","#30AB0D","#65BC90","#6AAB87",
        "#0D17FF","#13C7FF","#FF6011","#23FDE2",
        "#FF00E2","#CA28FF","#F4928F","#FF9DF5",
        "#FF0F09","#F47816","#FD976F","#FFA9AC"
    ];
    var legendData = [];
    var legend = {
        "data":legendData,
        "selected":{},
        right:0,
        x:1100,
        y:50,
        orient: 'vertical',
        textStyle:{
            color:'white'
        }

    };
    var xTime = result == null ? [] : result.xAxis;
    xTime = xTime == null || undefined ? [] :　xTime;
    var series = [];
    color.length = columnArr.length;
    for(var i=0;i<columnArr.length;i++){
        //var legendTemp = {"name":};//,"textStyle":{"color":'#FFFFFF'}
        legendData.push(columnArr[i]);
        if(i == 0){//设置只显示一个折线图，其他折线图默认隐藏
            legend.selected[columnArr[i]] = true;
        }else{
            legend.selected[columnArr[i]] = false;
        }
        var seriesTemp = {};
        seriesTemp.name = columnArr[i];
        seriesTemp.type = 'line';
        if(result!= null && result[lineArr[i]] != null){
            seriesTemp.data =  result[lineArr[i]];
        }else{
            seriesTemp.data = [];
        }
        seriesTemp.itemStyle = {"normal" : {"lineStyle":{"color":color[i]}}};
        /*seriesTemp.lineStyle={"width":1};*/
        series.push(seriesTemp);
    }

    var option = {
        textStyle:{
            color:'#FFFFFF'
        },
        title: {
            text: "数值",
            textStyle:{
                color:'#FFFFFF'
            }
        },
        grid:{
            right:200
        },
        tooltip : {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow',
                label: {
                    show: false
                }
            }
        },
        color:color,
        legend: legend,
        xAxis: {
            //type: 'time',
            type: 'category',
            name:"时间",
            data: xTime,
            splitLine: {
                show: false
            },
            nameTextStyle:{
                color:'#FFFFFF'
            },
            axisLine:{
                lineStyle:{
                    color:'#FFFFFF'
                }
            }
        },
        yAxis: {
            type: 'value',
            axisLine:{
                lineStyle:{
                    color:'#FFFFFF'
                }
            }
        },
        series: series
    };

    var chart = echarts.init(document.getElementById("myCharts"));
    chart.clear();
    chart.setOption(option);
    //实时刷新数据
    var requestArr = [];
    for(var i=0;i<instanceResult.length;i++){
       var temp = {};
        temp.instance_id = instanceResult[i].instance_id;
        temp.property_ids = instanceResult[i].property_ids;
        requestArr.push(temp);
    }
    var worker = new Worker("getSnapshot.js");
    var url = "https://bd.sh-raising.com/tsdbrest/api/record/snapshot" ;
    setInterval(function(){

        var param = {};
        param.url = url;
        param.arr = requestArr;

        worker.postMessage(JSON.stringify(param));//向worker发送数据
        worker.onmessage = function(evt){           //接收worker传过来的数据函数
            var str = evt.data;
            var response =  JSON.parse(str);

            var time =  response.time;
            var dataArr = response.dataArr;

            for(var i=0;i<dataArr.length;i++){
                var data =  dataArr[i];
                for(var k = 0;k<instanceResult.length;k++){
                    if(data.instanceId ==instanceResult[i].instance_id ){
                        var name = instanceResult[i].name;
                        for(var w=0;w<series.length;w++){
                            var seriesTemp =  series[w];
                            if(seriesTemp.name == name ){
                                var arr =  seriesTemp.data;
                                arr.shift();
                                arr.push(data.value);
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            if(xTime !=null ){
                xTime.shift();
                xTime.push(time);
            }
            //更新
            chart.setOption({
                xAxis: {
                    //type: 'time',
                    type: 'category',
                    name:"时间",
                    data: xTime,
                    splitLine: {
                        show: false
                    },
                    nameTextStyle:{
                        color:'#FFFFFF'
                    },
                    axisLine:{
                        lineStyle:{
                            color:'#FFFFFF'
                        }
                    }
                },
                series: series
            });
        }

    },5000);
}

//初始化上册树
function initTree(){
    var data = getData();
    new verTree({
        items:"#tree_list",
        type:"form",//list:表格展示 data:一般展示 form：表单展示
        data:data,
        parent:"parentId",
        params:"id",
        value:"name",
        name: "id",
        defaults:["1","SP1","SP2","SP3","SP4"]
    });
}

function getData(){
    
    var data = [
        {"id":"1", "name":"土压","parentId":"0", "levelType":1,
            "children":[
                {"name":"开挖面土压（上）(MPa)","parentId":"1","id":"SP1","levelType":2,"children":[]},
                {"name":"开挖面土压（下）(MPa)","parentId":"1","id":"SP2","levelType":2,"children":[]},
                {"name":"开挖面土压（左）(MPa)","parentId":"1","id":"SP3","levelType":2,"children":[]},
                {"name":"开挖面土压（右）(MPa)","parentId":"1","id":"SP4","levelType":2,"children":[]}
            ]
        },
        {"id":"2", "name":"刀盘","parentId":"0", "levelType":1,
            "children":[
                {"name":"刀盘转速(rpm)","parentId":"2","id":"CRpm","levelType":2,"children":[]},
                {"name":"刀盘扭距(kNm)","parentId":"2","id":"CTor","levelType":2,"children":[]}
            ]
        },
        {"id":"3", "name":"主驱动电机","parentId":"0", "levelType":1,
            "children":[
                {"name":"No.1刀盘电动机电流(A)","parentId":"3","id":"C1EmC","levelType":2,"children":[]},
                {"name":"No.2刀盘电动机电流(A)","parentId":"3","id":"C2EmC","levelType":2,"children":[]},
                {"name":"No.3刀盘电动机电流(A)","parentId":"3","id":"C3EmC","levelType":2,"children":[]},
                {"name":"No.4刀盘电动机电流(A)","parentId":"3","id":"C4EmC","levelType":2,"children":[]},
                {"name":"No.5刀盘电动机电流(A)","parentId":"3","id":"C5EmC","levelType":2,"children":[]},
                {"name":"No.6刀盘电动机电流(A)","parentId":"3","id":"C6EmC","levelType":2,"children":[]},
                {"name":"No.1刀盘电动机扭矩(kN*m)","parentId":"3","id":"C1Tor","levelType":2,"children":[]},
                {"name":"No.2刀盘电动机扭矩(kN*m)","parentId":"3","id":"C2Tor","levelType":2,"children":[]},
                {"name":"No.3刀盘电动机扭矩(kN*m)","parentId":"3","id":"C3Tor","levelType":2,"children":[]},
                {"name":"No.4刀盘电动机扭矩(kN*m)","parentId":"3","id":"C4Tor","levelType":2,"children":[]},
                {"name":"No.5刀盘电动机扭矩(kN*m)","parentId":"3","id":"C5Tor","levelType":2,"children":[]},
                {"name":"No.6刀盘电动机扭矩(kN*m)","parentId":"3","id":"C6Tor","levelType":2,"children":[]}
            ]
        },
        {"id":"4", "name":"推进","parentId":"0", "levelType":1,
            "children":[
                {"name":"盾构千斤顶总推力(KN)","parentId":"4","id":"JN","levelType":2,"children":[]},
                {"name":"盾构分区压力（上）(MPa)","parentId":"4","id":"J1P","levelType":2,"children":[]},
                {"name":"盾构分区压力（右）(MPa)","parentId":"4","id":"J2P","levelType":2,"children":[]},
                {"name":"盾构分区压力（下）(MPa)","parentId":"4","id":"J3P","levelType":2,"children":[]},
                {"name":"盾构分区压力（左）(MPa)","parentId":"4","id":"J4P","levelType":2,"children":[]},
                {"name":"1号行程传感器","parentId":"4","id":"J1S","levelType":2,"children":[]},
                {"name":"2号行程传感器","parentId":"4","id":"J2S","levelType":2,"children":[]},
                {"name":"3号行程传感器","parentId":"4","id":"J3S","levelType":2,"children":[]},
                {"name":"4号行程传感器","parentId":"4","id":"J4S","levelType":2,"children":[]},
                {"name":"No.1盾构千斤顶速度(mm/min)","parentId":"4","id":"J1V","levelType":2,"children":[]},
                {"name":"No.5盾构千斤顶速度(mm/min)","parentId":"4","id":"J2V","levelType":2,"children":[]},
                {"name":"No.9盾构千斤顶速度(mm/min)","parentId":"4","id":"J3V","levelType":2,"children":[]},
                {"name":"No.13盾构千斤顶速度(mm/min)","parentId":"4","id":"J4V","levelType":2,"children":[]}
            ]
        },
        {"id":"5", "name":"螺旋输送机","parentId":"0", "levelType":1,
            "children":[
                {"name":"螺旋机转速(min^-1)","parentId":"5","id":"SCRpm","levelType":2,"children":[]},
                {"name":"螺旋机扭矩(KN·M)","parentId":"5","id":"SCTor","levelType":2,"children":[]},
                {"name":"螺旋机压力（前）(MPa)","parentId":"5","id":"SCOilFP","levelType":2,"children":[]},
                {"name":"螺旋机压力（中）(MPa)","parentId":"5","id":"SCOilP","levelType":2,"children":[]},
                {"name":"螺旋机压力（后）(MPa)","parentId":"5","id":"SCOilBP","levelType":2,"children":[]}
            ]
        },
        {"id":"6", "name":"铰接","parentId":"0", "levelType":1,
            "children":[
                {"name":"1#铰接千斤顶的行程","parentId":"6","id":"HJ1S","levelType":2,"children":[]},
                {"name":"2#铰接千斤顶的行程","parentId":"6","id":"HJ2S","levelType":2,"children":[]},
                {"name":"3#铰接千斤顶的行程","parentId":"6","id":"HJ3S","levelType":2,"children":[]},
                {"name":"4#铰接千斤顶的行程","parentId":"6","id":"HJ4S","levelType":2,"children":[]}
            ]
        },
        {"id":"7", "name":"盾尾密封","parentId":"0", "levelType":1,
            "children":[
                {"name":"1#前仓油脂压力(MPa)","parentId":"7","id":"BOF1P","levelType":2,"children":[]},
                {"name":"2#前仓油脂压力(MPa)","parentId":"7","id":"BOF2P","levelType":2,"children":[]},
                {"name":"3#前仓油脂压力(MPa)","parentId":"7","id":"BOF3P","levelType":2,"children":[]},
                {"name":"4#前仓油脂压力(MPa)","parentId":"7","id":"BOF4P","levelType":2,"children":[]},
                {"name":"5#前仓油脂压力(MPa)","parentId":"7","id":"BOF5P","levelType":2,"children":[]},
                {"name":"6#前仓油脂压力(MPa)","parentId":"7","id":"BOF6P","levelType":2,"children":[]},
                {"name":"7#前仓油脂压力(MPa)","parentId":"7","id":"BOF7P","levelType":2,"children":[]},
                {"name":"8#前仓油脂压力(MPa)","parentId":"7","id":"BOF8P","levelType":2,"children":[]},

                {"name":"1#后仓油脂压力(MPa)","parentId":"7","id":"BOB1P","levelType":2,"children":[]},
                {"name":"2#后仓油脂压力(MPa)","parentId":"7","id":"BOB2P","levelType":2,"children":[]},
                {"name":"3#后仓油脂压力(MPa)","parentId":"7","id":"BOB3P","levelType":2,"children":[]},
                {"name":"4#后仓油脂压力(MPa)","parentId":"7","id":"BOB4P","levelType":2,"children":[]},
                {"name":"5#后仓油脂压力(MPa)","parentId":"7","id":"BOB5P","levelType":2,"children":[]},
                {"name":"6#后仓油脂压力(MPa)","parentId":"7","id":"BOB6P","levelType":2,"children":[]},
                {"name":"7#后仓油脂压力(MPa)","parentId":"7","id":"BOB7P","levelType":2,"children":[]},
                {"name":"8#后仓油脂压力(MPa)","parentId":"7","id":"BOB8P","levelType":2,"children":[]}
            ]
        },
        {"id":"8", "name":"注浆","parentId":"0", "levelType":1,
            "children":[
                {"name":"注浆注入口1压力(MPa)","parentId":"8","id":"G1P","levelType":2,"children":[]},
                {"name":"注浆注入口2压力(MPa)","parentId":"8","id":"G2P","levelType":2,"children":[]},
                {"name":"注浆注入口3压力(MPa)","parentId":"8","id":"G3P","levelType":2,"children":[]},
                {"name":"注浆注入口4压力(MPa)","parentId":"8","id":"G4P","levelType":2,"children":[]},

                {"name":"注浆管1注浆流量(L/min)","parentId":"8","id":"G1LM","levelType":2,"children":[]},
                {"name":"注浆管2注浆流量(L/min)","parentId":"8","id":"G2LM","levelType":2,"children":[]},
                {"name":"注浆管3注浆流量(L/min)","parentId":"8","id":"G3LM","levelType":2,"children":[]},
                {"name":"注浆管4注浆流量(L/min)","parentId":"8","id":"G4LM","levelType":2,"children":[]},

                {"name":"注浆管1注入浆量累计值(L)","parentId":"8","id":"G1Total","levelType":2,"children":[]},
                {"name":"注浆管2注入浆量累计值(L)","parentId":"8","id":"G2Total","levelType":2,"children":[]},
                {"name":"注浆管3注入浆量累计值(L)","parentId":"8","id":"G3Total","levelType":2,"children":[]},
                {"name":"注浆管4注入浆量累计值(L)","parentId":"8","id":"G4Total","levelType":2,"children":[]}

            ]
        },
        {"id":"9", "name":"渣土改良","parentId":"0", "levelType":1,
            "children":[
                {"name":"1号膨润土压力(MPa)","parentId":"9","id":"E1P","levelType":2,"children":[]},
                {"name":"2号膨润土压力(MPa)","parentId":"9","id":"E2P","levelType":2,"children":[]},

                {"name":"膨润土管1#流量 (L/min)","parentId":"9","id":"E1LM","levelType":2,"children":[]},
                {"name":"膨润土管2#流量 (L/min)","parentId":"9","id":"E2LM","levelType":2,"children":[]},

                {"name":"泡沫管1#压力(MPa)","parentId":"9","id":"FO1P","levelType":2,"children":[]},
                {"name":"泡沫管2#压力(MPa)","parentId":"9","id":"FO2P","levelType":2,"children":[]},
                {"name":"泡沫管3#压力(MPa)","parentId":"9","id":"FO3P","levelType":2,"children":[]},
                {"name":"泡沫管4#压力(MPa)","parentId":"9","id":"FO4P","levelType":2,"children":[]}
            ]
        },
        {"id":"10", "name":"工业水系统","parentId":"0", "levelType":1,
            "children":[
                {"name":"工业水温度(℃)","parentId":"10","id":"waterT","levelType":2,"children":[]},
                {"name":"工业水液位","parentId":"10","id":"waterBL","levelType":2,"children":[]},
                {"name":"工业水压力(MPa)","parentId":"10","id":"waterP","levelType":2,"children":[]}
            ]
        },
        {"id":"11", "name":"液压站","parentId":"0", "levelType":1,
            "children":[
                {"name":"膨润土液位(%)","parentId":"11","id":"BenTkBL","levelType":2,"children":[]},
                {"name":"混合液罐液位(%)","parentId":"11","id":"MixtureBL","levelType":2,"children":[]},
                {"name":"原液液位(%)","parentId":"11","id":"StosteBL","levelType":2,"children":[]}
            ]
        },
        {"id":"12", "name":"压缩空气","parentId":"0", "levelType":1,
            "children":[
                {"name":"空气管1#压力(MPa)","parentId":"12","id":"Air1P","levelType":2,"children":[]},
                {"name":"空气管2#压力(MPa)","parentId":"12","id":"Air2P","levelType":2,"children":[]},
                {"name":"空气管3#压力(MPa)","parentId":"12","id":"Air3P","levelType":2,"children":[]},
                {"name":"空气管4#压力(MPa)","parentId":"12","id":"Air4P","levelType":2,"children":[]}
            ]
        }
    ]
    return data;
}

