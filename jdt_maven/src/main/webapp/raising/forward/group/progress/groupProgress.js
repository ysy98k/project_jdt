var info = new Vue({
    el:"#father",
    data:{
        yearOption:[],
        weekOption:[]
    }
})
Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
}

$(function(){

    initSelect();
})
//初始化
function initSelect(){
    //初始化下拉框
    $('.chosen-select').chosen({
        allow_single_deselect : true
    });
    $(window).on('resize.chosen', function() {
        $('.chosen-select').next().css({
            'width' : '210px'
        });
    }).trigger('resize.chosen');
    //初始化周
    getAllYear();
    getAllWeek(new Date());
    //初始化月
    getAllYearForMonth();
    $("#week_week").children("option:last-child").attr("selected","selected");
    $('.chosen-select').trigger("chosen:updated");
    //年改变事件
    $("#week_year").change(function(){
        var a = $("#week_year").val();
        getAllWeek($("#week_year").val());
        $("#week_week").children("option:first-child").attr("selected","selected");
        $('.chosen-select').trigger("chosen:updated");
    })
}

function queryWeek(obj){
    $(obj).css( "background-color","rgb(33, 132, 193)!important" );
    //obj.attr("disabled");
    var paramJson = {};
    var yearStr = $("#week_year").val();
    var weekStr = $("#week_week").val();
    var weekStart = parseInt(weekStr.split("-")[0]);
    var weekEnd = parseInt(weekStr.split("-")[1]);
    var monday = new Date(weekStart);
    var tuesday = monday.setDate(monday.getDate()+1);
    var wednesday = monday.setDate(monday.getDate()+1) ;
    var thursday = monday.setDate(monday.getDate()+1) ;
    var firday = monday.setDate(monday.getDate()+1) ;
    var saturday = monday.setDate(monday.getDate()+1) ;
    var sunday = monday.setDate(monday.getDate()+1) ;
    var paramJson = {};
    paramJson.type = "week";
    paramJson.monday = weekStart;
    paramJson.tuesday = tuesday;
    paramJson.wednesday = wednesday;
    paramJson.thursday = thursday;
    paramJson.friday = firday;
    paramJson.saturday = saturday;
    paramJson.sunday = sunday;
    paramJson.startTime = weekStart;
    paramJson.endTime = monday.setDate(monday.getDate()+1)-1;

    var columns = new Array();
    var index_0 = [{
        "title": $("#week_year option:selected").text() + $("#week_week option:selected").text() +"进度周报",
        "halign":"center",
        "align":"center",
        "colspan": 11
    }];
    var index_1 = [
            {
                "field": 'projectName',
                "title": '项目',
                "valign":"middle",
                "align":"center",
                "rowspan": 2
            },
            {
                "title": '周一'
            },
            {
                "title": '周二'
            },
            {
                "title": '周三'
            },
            {
                "title": '周四'
            },
            {
                "title": '周五'
            },
            {
                "title": '周六'
            },
            {
                "title": '周日'
            },
            {
                "field": 'weekTotal',
                "title": '周累计（环）',
                "valign":"middle",
                "align":"center",
                "rowspan": 2
            },
            {
                "field": 'ringTotal',
                "title": '项目总长（环）',
                "valign":"middle",
                "align":"center",
                "rowspan": 2
            },
            {
                "field": 'progress',
                "title": '进度',
                "valign":"middle",
                "align":"center",
                "rowspan": 2
            }
        ];
    var index_2 = [
        {
            "field": 'mondayNums',
            "title": new Date(weekStart).getDate()+'号'
        },
        {
            "field": 'tuesdayNums',
            "title":  new Date(tuesday).getDate()+'号'
        },
        {
            "field": 'wednesdayNums',
            "title": new Date(wednesday).getDate()+'号'
        },
        {
            "field": 'thursdayNums',
            "title": new Date(thursday).getDate()+'号'
        },
        {
            "field": 'fridayNums',
            "title": new Date(firday).getDate()+'号'
        },
        {
            "field": 'saturdayNums',
            "title": new Date(saturday).getDate()+'号'
        },
        {
            "field": 'sundayNums',
            "title": new Date(sunday).getDate()+'号'
        }
    ]
    columns.push(index_0);
    columns.push(index_1);
    columns.push(index_2);

    var height = $("#tableContent").height()-100;
    $('#week_table').bootstrapTable("destroy").bootstrapTable({
        url: toolkitPath +  '/raising/forward/statisticalAnalysis/progress/groupProgress/getRows.do',         //请求后台的URL（*）
        method: 'get',
        cache: false,
        queryParams: {"ajaxParam":JSON.stringify(paramJson)},//传递参数（*）
        responseHandler: function(data){
            if(data.status != "0" || data.list.length < 1){
                return [];
            }
            return data.list;
        },
        pagination: true,                   //是否显示分页（*）
        pageNumber:1,                       //初始化加载第一页，默认第一页
        pageSize: 10,                       //每页的记录行数（*）
        sidePagination: "client",           //分页方式：client客户端分页，server服务端分页（*）
        pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
        height: height,
        columns: columns


    });

    /*$.ajax({
        "url":toolkitPath + "/raising/forward/construction/reportTable/getRows.do",
        "type":"post",
        "data":{"ajaxParam":JSON.stringify(paramJson)},
        "dataType":"json",
        "success":function(data){
            if("0" == data.status){

            }
        }
    });*/
}

//导出csv文件
function exportCSV(type){
    if(type == "week"){
        var weekStr = $("#week_week").val();
        var weekStart = parseInt(weekStr.split("-")[0]);
        var monday = new Date(weekStart);
        var tuesday = monday.setDate(monday.getDate()+1);
        var wednesday = monday.setDate(monday.getDate()+1) ;
        var thursday = monday.setDate(monday.getDate()+1) ;
        var firday = monday.setDate(monday.getDate()+1) ;
        var saturday = monday.setDate(monday.getDate()+1) ;
        var sunday = monday.setDate(monday.getDate()+1) ;
        var index_0 = [$("#week_year option:selected").text() + $("#week_week option:selected").text() +"进度周报"];
        var index_1 = ["项目","周一","周二","周三","周四","周五","周六","周日","周累计（环）","项目总长（环）","进度"];
        var index_2 = [new Date(weekStart).getDate()+'号',new Date(tuesday).getDate()+'号',new Date(wednesday).getDate()+'号'
                ,new Date(thursday).getDate()+'号',new Date(firday).getDate()+'号',new Date(saturday).getDate()+'号'
                ,new Date(sunday).getDate()+'号'];

        var str = "";
        str = str + index_0[0]+"\n";
        str = str + index_1.join(",")+"\n";
        str = str + ","+index_2.join(",") + "\n";

        var allTableData = $('#week_table').bootstrapTable('getData');
        for(var i=0;i<allTableData.length;i++){
            var temp = allTableData[i];
            str = str + temp.projectName+","+temp.mondayNums+","+temp.tuesdayNums+","+temp.wednesdayNums+","+temp.thursdayNums+","
            +temp.fridayNums+","+temp.saturdayNums+","+temp.sundayNums+"," +temp.weekTotal+","+temp.ringTotal+","+temp.progress+"\n";

        }

        var aaaa = "data:text/csv;charset=utf-8,\ufeff" + str;
        var link = document.createElement("a");
        link.setAttribute("href", aaaa);
        var date = new Date();
        var filename = date.format("yyyy-MM-dd HH:mm:ss");
        link.setAttribute("download", filename + ".csv");
        link.click();


    }else if(type == "month"){
        var year =  parseInt($("#month_year").val());
        var month = parseInt($("#month_month").val());
        var week_array = [];
        var i = 0;
        var start = new Date(year, month-1, 1); // 得到当月第一天
        var end = new Date(year, month, 0); // 得到当月最后一天
        var start_day = start.getDay(); // 当月第一天是周几
        console.log(start.format("yyyy-MM-dd"), end.format("yyyy-MM-dd")); // 每月的起始日期
        switch(start_day){
            case 0: i = 1; break;
            case 1: i = 1; break;
            case 2: i = 7; break;
            case 3: i = 6; break;
            case 4: i = 5; break;
            case 5: i = 4; break;
            case 6: i = 3; break;
        }

        while ( new Date(year, month-1, i) <= end) {
            var s = new Date(year, month-1, i);
            var e = new Date(year, month-1, i + 6);
            week_array.push( [s.getTime(),s.format("dd")+"日 - "+ e.format("dd")+"日"])
            i += 7;
        }

        var index_0 = [year + "年" +month+"月" +"进度月报"];
        var index_1 = ["项目","第一周","第二周","第三周","第四周","月累计（环）","项目总长（环）","进度"];
        var index_2 = [week_array[0][1]+'(环)',week_array[1][1]+'(环)',week_array[2][1]+'(环)',week_array[3][1]+'(环)'];
        if(week_array.length == 5){//如果这月有五个星期
            index_1.splice(5, 0, '第五周');
            index_2.push(week_array[4][1]+'(环)');
        }

        var str = "";
        str = str + index_0[0]+"\n";
        str = str + index_1.join(",")+"\n";
        str = str + ","+index_2.join(",") + "\n";

        var allTableData = $('#month_table').bootstrapTable('getData');
        for(var i=0;i<allTableData.length;i++){
            var temp = allTableData[i];
            if(week_array.length == 5){
                str = str + temp.projectName+","+temp.oneNums+","+temp.twoNums+","+temp.threeNums+","+temp.fourNums+","
                    +temp.fiveNums+","+temp.monthTotal+","+temp.ringTotal+","+temp.progress+"\n";
            }else{
                str = str + temp.projectName+","+temp.oneNums+","+temp.twoNums+","+temp.threeNums+","+temp.fourNums+","
                    +temp.monthTotal+","+temp.ringTotal+","+temp.progress+"\n";
            }
        }
        var aaaa = "data:text/csv;charset=utf-8,\ufeff" + str;
        var link = document.createElement("a");
        link.setAttribute("href", aaaa);
        var date = new Date();
        var filename = date.format("yyyy-MM-dd HH:mm:ss");
        link.setAttribute("download", filename + ".csv");
        link.click();
    }
}


//获得年月下拉框
function getAllYear(){
    var date = new Date();
    var currentYear =  parseInt(date.getFullYear());
    var currentMonth = parseInt(date.getMonth()) + 1;
    var str = "";
    for(var i=9;i>=0;i--){
        for(var k=1;k<13;k++){
            if(i == 0 && k == currentMonth) {//如果是当前年,当前月
                var temp = "<option value='"+(currentYear - i)+"-"+k+"' selected>"+ (currentYear - i)+"年 - "+k+ "月</option>";
            }else{
                var temp = "<option value='"+(currentYear - i)+"-"+k+"'>"+ (currentYear - i)+"年 - "+k+ "月</option>";
            }
            str += temp;
        }
    }
    $("#week_year").append(str);
}

//获得所有星期。 now_month 是 “2019-6”格式
function getAllWeek(now_month){
    var index=1;
    var str = "";

    var week_array = [];//存放一个数组。第一个元素是值，第二个元素是显示值
    var today = new Date(Date.parse(now_month));
    var year = today.getFullYear();
    var month = today.getMonth();
    var i = 0;

    var start = new Date(year, month, 1); // 得到当月第一天
    var end = new Date(year, month+1, 0); // 得到当月最后一天
    var start_day = start.getDay(); // 当月第一天是周几
    console.log(start.format("yyyy-MM-dd"), end.format("yyyy-MM-dd")); // 每月的起始日期
    switch(start_day){
        case 0: i = 1; break;
        case 1: i = 1; break;
        case 2: i = 7; break;
        case 3: i = 6; break;
        case 4: i = 5; break;
        case 5: i = 4; break;
        case 6: i = 3; break;
    }

    while ( new Date(year, month, i) <= end) {
        var s = new Date(year, month, i);
        var e = new Date(year, month, i + 6);
        week_array.push( [s.getTime()+"-"+e.getTime(),s.format("MM/dd")+" - "+ e.format("MM/dd")])
        i += 7;
    }

    for(var i =0;i<week_array.length;i++){
        if(i == 0){
            var temp = "<option value='"+week_array[i][0]+"' selected>第"+(i+1)+"周（" +week_array[i][1]+"）</option>";
        }else{
            var temp = "<option value='"+week_array[i][0]+"' >第"+(i+1)+"周（" +week_array[i][1]+"）</option>";
        }
        str += temp;
    }
    $("#week_week").html("");
    $("#week_week").append(str);
}



function getAllYearForMonth(){
    var date = new Date();
    var currentYear =  parseInt(date.getFullYear());
    var currentMonth = parseInt(date.getMonth()) + 1;
    var str = "";
    for(var i=9;i>=0;i--){
        if(i == 0 ) {//如果是当前年,当前月
            var temp = "<option value='"+(currentYear - i)+"' selected>"+ (currentYear - i)+"年</option>";
        }else{
            var temp = "<option value='"+(currentYear - i)+"'>"+ (currentYear - i)+"年</option>";
        }
        str += temp;
    }
    $("#month_month").val(currentMonth);
    $("#month_year").append(str);
}

function queryMonth(){
    var year =  parseInt($("#month_year").val());
    var month = parseInt($("#month_month").val());
    var week_array = [];
    var i = 0;
    var start = new Date(year, month-1, 1); // 得到当月第一天
    var end = new Date(year, month, 0); // 得到当月最后一天
    var start_day = start.getDay(); // 当月第一天是周几
    console.log(start.format("yyyy-MM-dd"), end.format("yyyy-MM-dd")); // 每月的起始日期
    switch(start_day){
        case 0: i = 1; break;
        case 1: i = 1; break;
        case 2: i = 7; break;
        case 3: i = 6; break;
        case 4: i = 5; break;
        case 5: i = 4; break;
        case 6: i = 3; break;
    }

    while ( new Date(year, month-1, i) <= end) {
        var s = new Date(year, month-1, i);
        var e = new Date(year, month-1, i + 6);
        week_array.push( [s.getTime(),s.format("dd")+"日 - "+ e.format("dd")+"日"])
        i += 7;
    }

    var paramJson = {};
    paramJson.type = "month";
    paramJson.oneWeekStart = week_array[0][0];
    paramJson.twoWeekStart = week_array[1][0];
    paramJson.threeWeekStart = week_array[2][0];
    paramJson.fourWeekStart = week_array[3][0];
    paramJson.startTime = paramJson.oneWeekStart;
    paramJson.endTime = paramJson.fourWeekStart + 7*24*60*1000 -1;
    if(week_array.length == 5){
        paramJson.fiveWeekStart = week_array[4][0];
        paramJson.endTime = paramJson.fourWeekStart + 7*24*60*1000 -1;
    }




    var columns = new Array();
    var index_0 = [{
        "title": year + "年" +month+"月" +"进度月报",
        "halign":"center",
        "align":"center",
        "colspan": week_array.length == 5 ? 9 : 8
    }];
    var index_1 = [
        {
            "field": 'projectName',
            "title": '项目',
            "valign":"middle",
            "align":"center",
            "rowspan": 2
        },
        {
            "title": '第一周'
        },
        {
            "title": '第二周'
        },
        {
            "title": '第三周'
        },
        {
            "title": '第四周'
        },
        {
            "field": 'monthTotal',
            "title": '月累计（环）',
            "valign":"middle",
            "align":"center",
            "rowspan": 2
        },
        {
            "field": 'ringTotal',
            "title": '项目总长（环）',
            "valign":"middle",
            "align":"center",
            "rowspan": 2
        },
        {
            "field": 'progress',
            "title": '进度',
            "valign":"middle",
            "align":"center",
            "rowspan": 2
        }
    ];
    var index_2 = [
        {
            "field": 'oneNums',
            "title": week_array[0][1]+'(环)'
        },
        {
            "field": 'twoNums',
            "title":  week_array[1][1]+'(环)'
        },
        {
            "field": 'threeNums',
            "title": week_array[2][1]+'(环)'
        },
        {
            "field": 'fourNums',
            "title": week_array[3][1]+'(环)'
        }
    ]
    if(week_array.length == 5){//如果这月有五个星期
        index_1.splice(5, 0, { "title": '第五周'});
        index_2.push({
            "field": 'fiveNums',
            "title": week_array[4][1]+'号'
        });
    }

    columns.push(index_0);
    columns.push(index_1);
    columns.push(index_2);


    var height = $("#tableContent").height()-100;
    $('#month_table').bootstrapTable("destroy").bootstrapTable({
        url: toolkitPath +  '/raising/forward/statisticalAnalysis/progress/groupProgress/getRows.do',         //请求后台的URL（*）
        method: 'get',
        cache: false,
        queryParams: {"ajaxParam":JSON.stringify(paramJson)},//传递参数（*）
        responseHandler: function(data){
            if(data.status != "0" || data.list.length < 1){
                return [];
            }
            return data.list;
        },
        pagination: true,                   //是否显示分页（*）
        pageNumber:1,                       //初始化加载第一页，默认第一页
        pageSize: 10,                       //每页的记录行数（*）
        sidePagination: "client",           //分页方式：client客户端分页，server服务端分页（*）
        pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
        height: height,
        columns: columns


    });
}


