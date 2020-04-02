//树的js
var dataObj;
var highCharts = null;


var dataObj = {
    "title":null,
    "unit":null,
    "name":null,
    "instanceId":null,
    "propertyId":null
}

$(document).ready(function () {
    initDom();

    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxcombobox', 'bxtree', 'bxcomboboxtree'], function () {
        initDateRangePicker();
        showTree();
        Highcharts.setOptions({//修改时区
            global: {
                timezoneOffset: -8 * 60
                //useUTC: false
            }
        });
        highCharts =  Highcharts.stockChart('container', {
            lang:{
              loading:'loading加载中'
            },
            rangeSelector: {
                buttons: [{
                    type: 'minute',
                    count: 30,
                    text: '半小时'
                }, {
                    type: 'hour',
                    count: 1,
                    text: '1小时'
                }, {
                    type: 'day',
                    count: 1,
                    text: '1天'
                },{
                    type: 'all',
                    text: '所有'
                }],
                selected: 5,
                inputBoxWidth:150,
                inputDateFormat:'%Y-%m-%d %H:%M:%S',
                inputEditDateFormat:'%Y-%m-%d %H:%M:%S',
                inputEnabled:false
            },
            title: {
                text: '历史趋势'
            },
            plotOptions: {
                series: {
                    showInLegend: true
                }
            },
            series: [{
                type: 'line',
                id: '000001',
                data: []
            }]
        });

    });
});

function initDom(){
    //初始化tree的高度
    var fatherHeight = $("#father").height();
    var headHeight = $("#header").outerHeight(true);
    var contentHeight = fatherHeight - headHeight;
    $("#treeDiv").height(contentHeight);
    $("#treeDiv2").height(contentHeight-10);

    //初始化highstock的宽度,高度
    var babaWidth = $("#baba").outerWidth(true);
    var treeWidth = $("#treeDiv").width();
    if(treeWidth = 350){
        $("#queryarea").width(babaWidth - treeWidth - 10);
    }
    var cHright = contentHeight - 80 <= 480 ? 480 : contentHeight - 80;
    $("#container").height(cHright);
}

//显示左侧树
function showTree() {

    var handleCheckFunct = function (option) {
        var children = option.core.data.children;
        if (children != undefined && children != null && children.length > 0) {
            var oneChild = children[0];
            oneChild.state.checked = true;
        }
    };

    var option = {
        checkbox: {
            keep_selected_style: false,
            tie_selection: false,
            three_state: false,
            cascade: 'undetermined'
        },
        "plugins": ["themes", "wholerow"]
    };

    $("#bxtree3").bxtree({
        //ccsId: "provar."+$.cookie("selected_collection"),
        ccsId: "provar.RBXS0018",
        showText: '变量列表',
        option: option,
        dataPattern: 'ccs',
        preHandlerFunct: handleCheckFunct
    }).on("select_node.jstree",function (event, obj) {

        var selectNode = obj.node;
        var id = selectNode.id;
        var title = selectNode.text;
        var parrntArr = selectNode.parents;
        var ccsId = parrntArr[1] + "." + parrntArr[0] + "." + id;

        getUnit(ccsId);
        getInstanceId(id);
        if ( dataObj.instanceId == null || dataObj.propertyId == null) {
            alertDiv("提示", "该点没有数据");
            return;
        }
        var endTime = new Date().getTime();
        var startTime = endTime - 86400000;
        highCharts.showLoading("拼命加载中。。。。。");
        showData(startTime,endTime);        nodeState = true;

    });
}


function initDateRangePicker() {
    //定义locale汉化插件
    var locale = {
        "format": 'YYYY-MM-DD HH:mm:ss',
        "separator": " —— ",
        "applyLabel": "确定",
        "cancelLabel": "取消",
        "fromLabel": "起始时间",
        "toLabel": "结束时间",
        "customRangeLabel": "自定义",
        "weekLabel": "W",
        "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
        "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        "firstDay": 1
    };
    var optionSet1 = {
        "timePicker":true,
        "timePicker24Hour": true,
        "timePickerSeconds":true,
        "timePickerIncrement":1,
        'locale': locale,
        //汉化按钮部分
        ranges: {
            '今日': [moment().format('YYYY-MM-DD HH:mm:ss'), moment().format('YYYY-MM-DD HH:mm:ss')],
            '昨日': [moment().subtract(1, 'days').format('YYYY-MM-DD HH:mm:ss'), moment().subtract(1, 'days').format('YYYY-MM-DD HH:mm:ss')],
            '最近7日': [moment().subtract(6, 'days').format('YYYY-MM-DD HH:mm:ss'), moment().format('YYYY-MM-DD HH:mm:ss')],
            '最近30日': [moment().subtract(29, 'days').format('YYYY-MM-DD HH:mm:ss'), moment().format('YYYY-MM-DD HH:mm:ss')],
            '本月': [moment().startOf('month').format('YYYY-MM-DD HH:mm:ss'), moment().endOf('month').format('YYYY-MM-DD HH:mm:ss')],
            '上月': [moment().subtract(1, 'month').startOf('month').format('YYYY-MM-DD HH:mm:ss'), moment().subtract(1, 'month').endOf('month').format('YYYY-MM-DD HH:mm:ss')]
        },
        startDate: moment().subtract(29, 'days').format('YYYY-MM-DD HH:mm:ss'),
        endDate: moment().format('YYYY-MM-DD HH:mm:ss')
    };
    //初始化显示当前时间
    $('#daterange-btn span').html(moment().subtract('hours', 1).format('YYYY-MM-DD HH:mm:ss') + ' - ' + moment().format('YYYY-MM-DD HH:mm:ss'));
    //日期控件初始化
    $('#daterange-btn').daterangepicker(optionSet1,function (start, end,label) {
        $('#daterange-btn span').html(start.format('YYYY-MM-DD HH:mm:ss') + ' - ' + end.format('YYYY-MM-DD HH:mm:ss'));
        }
    );
    $("#daterange-btn").on('apply.daterangepicker', function(evt, picker) {
        var startTime = moment(picker.startDate.toISOString()).unix()*1000;
        var endTime = moment(picker.endDate.toISOString()).unix()*1000;
        var t = $("div[class *='daterangepicker']").attr("class");
        $("div[class *='daterangepicker']").hide();

        highCharts.showLoading("拼命加载中。。。。。");
        showData(startTime, endTime);

        //highCharts.xAxis[0].setExtremes(startTime, endTime);//设置hishstock 的显示时间
    });
};

function showData(startTime,endTime){
    //var showData = getShowData(startTime,endTime);

    var worker = new Worker("historicalTrend2.js");

    var param = {};
    var getRecordURL = toolkitPath.replace("jdt", "tsdbrest/api/record/range");
    getRecordURL = getRecordURL + "/" + dataObj.instanceId + "/" + dataObj.propertyId;
    param.getRecordURL = getRecordURL;
    param.startTime = startTime;
    param.endTime = endTime;
    worker.postMessage(JSON.stringify(param));//向worker发送数据
    worker.onmessage = function(evt){           //接收worker传过来的数据函数
        var str = evt.data;
        var showData =  JSON.parse(str);
        highCharts.update({
            tooltip: {
                split: false,
                shared: true,
                valueSuffix: dataObj.unit
            },
            title: {
                text: dataObj.title
            },
            series: [{
                type: 'line',
                id: '000001',
                name: dataObj.name,
                data: showData
            }]
        })
        highCharts.hideLoading();
    }



}

function getUnit(ccsId){
    $.ajax({
        url:toolkitPath + "/raising/forward/historicalTrend/getUnit.do",
        type:"get",
        data:{"ajaxParam":ccsId},
        async:false,
        dataType:"json",
        success:function(data){
            if(data.status == '0' && !isNullOrEmptyOrUndefiend(data.unit)){
                dataObj.unit = data.unit;
            }else{
                dataObj.unit = null;
            }
        },
        error:function(){
            dataObj.unit = null;
        }
    })
}

function getInstanceId(treeNodeId){
    var instanceName = $.cookie("selected_collection") + "_" + treeNodeId;
    var getInstanceURL = toolkitPath.replace("jdt", "tsdbrest/api/instance");
    var instancesArr = [];
    var instance1 = {"instance_name": instanceName};
    instancesArr.push(instance1);
    $.ajax({
        url: getInstanceURL,
        type: "post",
        data: JSON.stringify(instancesArr),
        contentType: 'application/json;charset=UTF-8',
        async: false,
        dataType: "json",
        success: function (data) {
            if (Number(data.errcode) == 0) {
                var dataArr = data.records;
                var instanceRecord = dataArr[0];
                dataObj.instanceId = instanceRecord.instance_id;
                if(dataObj.instanceId == undefined){
                    dataObj.instanceId = null;
                }
                var propertiesArr = instanceRecord.properties;
                if(propertiesArr.length > 0){
                    dataObj.propertyId = propertiesArr[0].id;
                }else{
                    dataObj.propertyId = null;
                }
            }
        },
        error:function(){
            dataObj.instanceId = null;
            dataObj.propertyId = null;
        }
    })

}

function getShowData(startTime,endTime){
    var getRecordURL = toolkitPath.replace("jdt", "tsdbrest/api/record/range");
    getRecordURL = getRecordURL + "/" + dataObj.instanceId + "/" + dataObj.propertyId;
    /*
    var endTime = new Date().getTime();
    var startTime= new Date(endTime -365*24*60*60*1000*1.5).getTime();//设置查询的起始时间为当前时间的 一年半前。*/

    var interval = Math.round((endTime - startTime)/10000);
    if(interval <= 5000 ){//如果小于5s。则使用查询原始值的方式查询
        interval = 0;
    }

    var cursor_mask = "*";
    var recordsArr = [];
    var condition = true;
    do {
        //43200000半天
        //7200000 2小时
        //300000  5分钟
        var ajaxObj = $.ajax({
            url: getRecordURL,
            type: "get",
            data: {
                "limit": "1000",
                "start_time": startTime,
                "end_time": endTime,
                "cursor_mask": cursor_mask,
                "interval": interval
            },
            dataType: "json",
            async: false,
            success: function (data) {
                condition = false;
                if (Number(data.errcode) == 0) {
                    cursor_mask = data.next_cursor_mask;
                    condition = data.records.length == 1000 ? true : false;
                    recordsArr = recordsArr.concat(data.records);
                }
            },
            error: function () {
                condition = false;
            }
        })
    } while (condition);

    var showData = new Array();
    for (var i = 0; i < recordsArr.length; i++) {
        var temp = [];
        temp[0] = recordsArr[i].timestamp;
        temp[1] = recordsArr[i].value;
        showData.push(temp);
    }
    return showData;
}

function backUp(){
    var paramJsonObj = {};
    paramJsonObj.ccsId = parrntArr[1] +"." + parrntArr[0] +"."+ id;
    paramJsonObj.collector = $.cookie("selected_collection");
    paramJsonObj.code = id;

    var xhr = $.ajax({
        type: "get",
        url:toolkitPath+'/raising/forward/historicalTrend/getDiagram.do',
        data: {"ajaxParam":JSON.stringify(paramJsonObj)},
        timeout: 30000,          // 设置超时时间 30s
        dataType: "json",
        beforeSend: function (xhr) {
            //$.showLoading();    // 数据加载成功之前，使用loading组件
        },
        success:function (paramJsonObj) {
            if(paramJsonObj.status == "0"){
                if(paramJsonObj.dataArray == null){
                    alertDiv("提示","该点没有数据");
                }
                var  data = paramJsonObj.dataArray;
                var unit = paramJsonObj.unit;
                var name = paramJsonObj.title;

                highCharts.update({
                    tooltip: {
                        split: false,
                        shared: true,
                        valueSuffix:unit
                    },
                    title: {
                        text: title
                    },
                    series: [{
                        type: 'line',
                        id: '000001',
                        name: name,
                        data: data
                    }]
                })

            }else{
                alertDiv("提示",paramJsonObj.message);
            }
        },
        error: function (textStatus) {
            console.error(textStatus);
        },
        complete: function (XMLHttpRequest,status) {
            highCharts.hideLoading();
            if(status == 'timeout') {
                xhr.abort();    // 超时后中断请求
                alert("请求时间超过30s,请求中断，请刷新。")
                location.reload();
            }
        }
    })
}











