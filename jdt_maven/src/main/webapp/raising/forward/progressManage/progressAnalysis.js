

$(document).ready(function () {
    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'],
        function () {
            $("#queryarea").bxdiv();
            var gridOption = {
                caption: false,
                colNames: ['日期','当天完成/环','掘进时间/h','拼装时间/h','停机时间/h','每环掘进时间/h', '每环拼装时间/h','每环停机时间/h',"简图",'备注'],
                colModel: [
                    {name:'date', index:'date',width: 40,readyOnly: true},
                    {name:'work', index:'work',width: 45,readyOnly: true},
                    {name:'tunnellingDate', index:'tunnellingDate',width: 40,readyOnly: true},
                    {name:'assembleDate', index:'assembleDate',width: 40,readyOnly: true},
                    {name:'shutDownDate', index:'shutDownDate',width: 40,readyOnly: true},
                    {name:'drivingTimePerRing', index:'drivingTimePerRing',width: 60,readyOnly: true},
                    {name:'installationTimePerRing', index:'installationTimePerRing',width: 60,readyOnly: true},
                    {name:'outageTimePerLoop', index:'outageTimePerLoop',width: 60,readyOnly: true},
                    {name:'diagram', index:'diagram',width: 40,readyOnly: true,
                        formatter: function (value, grid, row, state) {
                            var width = grid.colModel.width-10;
                            var proportionValue =  value.split(":");
                            var tunnelling = proportionValue[0];
                            var assemble = proportionValue[1];
                            var shutDown = proportionValue[2];
                            var tunnellingWidth =parseInt(width*tunnelling/10);
                            var assembleWidth =parseInt(width*assemble/10);
                            var shutDownWidth =parseInt(width*shutDown/10);
                            var str =
                            "<div>" +
                                "<div style='display: inline-block; background-color: #008B00;width:"+tunnellingWidth+"px;height: 10px;'></div>"+
                                "<div style='display: inline-block; background-color: #009ACD;width:"+assembleWidth+"px;height: 10px;'></div>"+
                                "<div style='display: inline-block; background-color: #CC0000;width: "+shutDownWidth+"px;height: 10px;'></div>"+
                            "</div>"
                            return str;
                        }
                    },
                    {name:'remark', index:'remark',width: 40,readyOnly: true},
                ],
                sortorder: 'asc',
                height: 331
            };
            var option = {
                queryParam: {"projectId":$.cookie("selected_id"),"type":"day"},
                dataPattern: "url",
                url: "/raising/forward/progressManage/progressAnalysis/getRows.do",
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
        }else if($(this).val() == "month"){
            $(".one").hide();
            $(".two").show();
            if($("#jqGrid2").html() == ""){
                baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'],
                    function () {
                        $("#queryarea").bxdiv();
                        var gridOption = {
                            caption: false,
                            colNames: ['日期','当月完成/环','掘进时间/h','拼装时间/h','停机时间/h','每环掘进时间/h', '每环拼装时间/h','每环停机时间/h',"简图",'备注'],
                            colModel: [
                                {name:'date', index:'date',width: 40,readyOnly: true},
                                {name:'work', index:'work',width: 40,readyOnly: true},
                                {name:'tunnellingDate', index:'tunnellingDate',width: 40,readyOnly: true},
                                {name:'assembleDate', index:'assembleDate',width: 40,readyOnly: true},
                                {name:'shutDownDate', index:'shutDownDate',width: 40,readyOnly: true},
                                {name:'drivingTimePerRing', index:'drivingTimePerRing',width: 40,readyOnly: true},
                                {name:'installationTimePerRing', index:'installationTimePerRing',width: 40,readyOnly: true},
                                {name:'outageTimePerLoop', index:'outageTimePerLoop',width: 40,readyOnly: true},
                                {name:'diagram', index:'diagram',width: 40,readyOnly: true},
                                {name:'remark', index:'remark',width: 40,readyOnly: true},
                            ],
                            sortorder: 'asc',
                            height: 331
                        };
                        var option = {
                            queryParam: {"projectId":$.cookie("selected_id"),"type":"month"},
                            dataPattern: "url",
                            url: "/raising/forward/progressManage/progressAnalysis/getRows.do",
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
    var works1 = [];
    var works2 = [];
    var works3 = [];
    for(var i =0;i<rows.length;i++){
        dates.push(rows[i].date);
        works1.push(rows[i].tunnellingDate);
        works2.push(rows[i].assembleDate);
        works3.push(rows[i].shutDownDate);
    }

    var myChart = echarts.init(document.getElementById(idName));
    option = {
        legend: {
            data:[                      //----图例内容
                    {name:'掘进时间',textStyle:{color:'#008B00'}},
                    {name:'拼装时间',textStyle:{color:'#009ACD'}},
                    {name:'停机时间',textStyle:{color:'#CC0000'}}
                ]
        },
        dataZoom: [
            {
                type: 'slider',//图表下方的伸缩条
                show : true,  //是否显示
                realtime : true,  //
                start : 0,  //伸缩条开始位置（1-100），可以随时更改
                end : 100,  //伸缩条结束位置（1-100），可以随时更改
            },
            {
                type: 'inside',  //鼠标滚轮
                realtime : true,
                //还有很多属性可以设置，详见文档
            }
        ],
        tooltip: {},
        xAxis: {type: 'category',data:dates},
        yAxis: {},
        series: [
            {type: 'bar',name:"掘进时间",data: works1,barGrap:'50%',barCategoryGap:'50%',barMaxWidth:20,itemStyle:{color:"#008B00"}},
            {type: 'bar',name:"拼装时间",data: works2,barGrap:'50%',barCategoryGap:'50%',barMaxWidth:20,itemStyle:{color:"#009ACD"}},
            {type: 'bar',name:"停机时间",data: works3,barGrap:'50%',barCategoryGap:'50%',barMaxWidth:20,itemStyle:{color:"#CC0000"}}
        ]
    };

    myChart.setOption(option);
}








