

$(document).ready(function () {
    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'],
        function () {
            $("#queryarea").bxdiv();
            var gridOption = {
                caption: false,
                colNames: ['日期','白班/环','夜班/环','当天累计/环','当天设计/环','当天进度', '累计','完成进度','备注'],
                colModel: [
                    {name:'date', index:'date',width: 40,readyOnly: true},
                    {name:'work', index:'work',width: 40,readyOnly: true},
                    {name:'nightWork', index:'nightWork',width: 40,readyOnly: true},
                    {name:'grandTotalWork', index:'grandTotalWork',width: 40,readyOnly: true},
                    {name:'designWork', index:'designWork',width: 40,readyOnly: true},
                    {name:'currentProgress', index:'progress',width: 40,readyOnly: true},
                    {name:'grandTotal', index:'grandTotal',width: 40,readyOnly: true},
                    {name:'completionSchedule', index:'completionSchedule',width: 40,readyOnly: true},
                    {name:'remark', index:'remark',width: 40,readyOnly: true}
                ],
                sortorder: 'asc',
                height: 331
            };
            var option = {
                queryParam: {"projectId":$.cookie("selected_id"),"type":"day"},
                dataPattern: "url",
                url: "/raising/forward/progressManage/currentProgress/getRows.do",
                showMsgOpt: {
                    showMsgId: "alertdiv"
                },
                callBackFunct:function(data){
                    var rows = [];
                    if(data.status == "0"){
                        rows = data.rows;
                    }
                    initCharts(rows,"myCharts1");
                },
                gridOption: gridOption
            };
            $("#jqGrid").bxgrid(option);
        });
    init();
    $("#inqu_status-timeType").change(function(){
        if($(this).val() == "day"){
            $(".one").show();
            $(".two").hide();
            $(".three").hide();

        }else if($(this).val() == "month"){
            $(".one").hide();
            $(".two").show();
            $(".three").hide();
            if($("#jqGrid2").html() == ""){
                baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'],
                    function () {
                        $("#queryarea").bxdiv();
                        var gridOption = {
                            caption: false,
                            colNames: ['日期','当月累计/环','当月设计/环','当月进度', '累计','完成进度','备注'],
                            colModel: [
                                {name:'date', index:'date',width: 40,readyOnly: true},
                                {name:'grandTotalWork', index:'grandTotalWork',width: 40,readyOnly: true},
                                {name:'designWork', index:'designWork',width: 40,readyOnly: true},
                                {name:'currentProgress', index:'progress',width: 40,readyOnly: true},
                                {name:'grandTotal', index:'grandTotal',width: 40,readyOnly: true},
                                {name:'completionSchedule', index:'completionSchedule',width: 40,readyOnly: true},
                                {name:'remark', index:'remark',width: 40,readyOnly: true}
                            ],
                            sortorder: 'asc',
                            height: 331
                        };
                        var option = {
                            queryParam: {"projectId":$.cookie("selected_id"),"type":"month"},
                            dataPattern: "url",
                            url: "/raising/forward/progressManage/currentProgress/getRows.do",
                            showMsgOpt: {
                                showMsgId: "alertdiv"
                            },
                            callBackFunct:function(data){
                                var rows = [];
                                if(data.status == "0"){
                                    rows = data.rows;
                                }
                                initCharts(rows,"myCharts2");
                            },
                            gridOption: gridOption
                        };
                        $("#jqGrid2").bxgrid(option);
                    });
            }
        }else if($(this).val() == "season"){
            $(".one").hide();
            $(".two").hide();
            $(".three").show();
            if($("#jqGrid3").html() == ""){
                baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'],
                    function () {
                        $("#queryarea").bxdiv();
                        var gridOption = {
                            caption: false,
                            colNames: ['日期','本季度累计/环','本季度设计/环','本季度进度', '累计','完成进度','备注'],
                            colModel: [
                                {name:'date', index:'date',width: 40,readyOnly: true},
                                {name:'grandTotalWork', index:'grandTotalWork',width: 40,readyOnly: true},
                                {name:'designWork', index:'designWork',width: 40,readyOnly: true},
                                {name:'currentProgress', index:'progress',width: 40,readyOnly: true},
                                {name:'grandTotal', index:'grandTotal',width: 40,readyOnly: true},
                                {name:'completionSchedule', index:'completionSchedule',width: 40,readyOnly: true},
                                {name:'remark', index:'remark',width: 40,readyOnly: true}
                            ],
                            sortorder: 'asc',
                            height: 331
                        };
                        var option = {
                            queryParam: {"projectId":$.cookie("selected_id"),"type":"season"},
                            dataPattern: "url",
                            url: "/raising/forward/progressManage/currentProgress/getRows.do",
                            showMsgOpt: {
                                showMsgId: "alertdiv"
                            },
                            callBackFunct:function(data){
                                var rows = [];
                                if(data.status == "0"){
                                    rows = data.rows;
                                }
                                initCharts(rows,"myCharts3");
                            },
                            gridOption: gridOption
                        };
                        $("#jqGrid3").bxgrid(option);
                    });
            }
        }
    })
});

/**
 * 查询按钮点击事件
 */
function on_query_click() {
    var queryParam = new Object();
    queryParam.startTime = $('#inqu_status-startTime').val();
    queryParam.endTime = $('#inqu_status-endTime').val();
    queryParam.projectId = $.cookie("selected_id");
    queryParam.type = "day";
    if(queryParam.startTime == queryParam.endTime){
        alertDiv("提示","起始与结束时间不可以相同！");
        return;
    }
    $("#jqGrid").bxgrid("option", "queryParam", queryParam);
    $("#jqGrid").bxgrid("query");
}

function init(){

    $('#inqu_status-startTime').daterangepicker({
        "singleDatePicker": true,
        "showDropdowns": true,
        "startDate": new Date(),
        "locale":{
            "format": "YYYY-MM-DD",
            "applyLabel": "确定",
            "cancelLabel": "取消",
            "weekLabel": "W",
            "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
            "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"]
        }
    }, function(start, end, label) {
        //var s  = start.format('YYYY-MM-DD');
        //$("#inqu_status-startTime").val(s);
    });
    $('#inqu_status-endTime').daterangepicker({
        "singleDatePicker": true,
        "showDropdowns": true,
        "startDate": new Date(),
        "locale":{
            "format": "YYYY-MM-DD",
            "applyLabel": "确定",
            "cancelLabel": "取消",
            "weekLabel": "W",
            "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
            "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"]
        }
    }, function(start, end, label) {
        //console.log("New date range selected: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') + ' (predefined range: ' + label + ')");
    });
}

function initCharts(rows,idName){
    var dates = [];
    var works = [];
    for(var i =0;i<rows.length;i++){
        dates.push(rows[i].date);
        works.push(rows[i].grandTotalWork);
    }

    var myChart = echarts.init(document.getElementById(idName));

    option = {
        tooltip : {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow',
                label: {
                    show: false
                }
            }
        },
        /*toolbox: {//工具，折线图，保存图片等功能
            show : true,
            feature : {
                mark : {show: true},
                magicType: {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },*/
        calculable : true,
        /*legend: {
            data:['woShiNiBaBa'/!*, 'Budget 2011'*!/],
            itemGap: 5
        },*/
        xAxis: [
            {
                type : 'category',
                data : dates
            }
        ],
        yAxis: [
            {
                type : 'value',
                name : '累计环'
            }
        ],
        /*dataZoom: [
            {
                id: 'dataZoomX',
                type: 'slider',
                //xAxisIndex: [0],
                filterMode: 'filter'
            },
            {
                type: 'inside',
                start: 94,
                end: 100
            }
        ],*/
        series : [
            {
                type: 'bar',
                barGrap:'50%',
                barCategoryGap:'50%',
                barMaxWidth:10,
                data: works,
                itemStyle:{color:"#008B00"}
            }

        ]
    };
    myChart.setOption(option);
}








