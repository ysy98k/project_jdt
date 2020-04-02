$(document).ready(function () {
    getLastData();
});

function getLastData() {
    var selectid = $.cookie('selected_id');
    if (selectid != null) {
        $.ajax({
            type: 'get',
            url: toolkitPath + '/raising/forward/summaryInfo/getAllLastPointsData.do?selected_id=' + selectid,
            cache: false,
            processData: false,
            contentType: false
        }).success(function (allData) {
            if (allData != null) {
                if (allData.msg != null) {
                    showMessage(allData.msg)
                } else {
                    $("#J_TbData").empty();
                    $("#myTab").empty();
                    $("#myTabinfo").empty();
                    getTitle(allData);
                    getTab(allData);
                }
            }
        }).error(function () {
            showMessage("数据库中没有数据，请上传!");
        });
    }
}

function getTitle(allData) {
    if (allData != null) {
        console.info(allData.sheetlist);
        for (var i = 1; i < allData.sheetlist.length; i++) {
            if (allData.sheetlist[i].indexOf("巡视") === -1) {
                if (i === 1) {
                    var $trTemp = $("<li class='active' onclick='clearCheckBox()'></li>");
                } else {
                    $trTemp = $("<li class='' onclick='clearCheckBox()'></li>");
                }
                $trTemp.append("<a data-toggle='tab' href='#" + allData.sheetlist[i] + "'>" + allData.sheetlist[i] + "</a>");
                $trTemp.appendTo("#myTab");
            }
        }
    }
}

function getTab(allData) {
    if (allData != null) {
        var reportSheetList = allData.reportSheetList;
        var $trTemp = $("<div id='" + reportSheetList[0][0].sheetName + "' class='tab-pane active' >" +
            "<table class='table table-striped table-bordered table-hover' style='margin: 0 auto;'>" +
            "<thead class='thin-border-bottom'>" +
            "<tbody id='R_Data" + 0 + "'></tbody></thead></table><div>");
        $trTemp.appendTo("#myTabinfo");
        getReportDataRow(reportSheetList, 0);

        for (var i = 1; i < reportSheetList.length; i++) {
            $trTemp = $("<div id='" + reportSheetList[i][0].sheetName + "' class='tab-pane'>" +
                "<table class='table table-striped table-bordered table-hover' style='margin: 0 auto;'>" +
                "<thead class='thin-border-bottom'>" +
                "<tbody id='R_Data" + i + "'></tbody></thead></table><div>");
            $trTemp.appendTo("#myTabinfo");
            getReportDataRow(reportSheetList, i);
        }
    }
}


function getReportDataRow(reportSheetList, i) {
    if (reportSheetList != null) {
        if (reportSheetList[i][0].sheetName.indexOf("地表") !== -1) {
            console.info(reportSheetList[i][0]);
            var $trTemp = $("<tr></tr>");
            $trTemp.append("<th align='center' style='width: 20px'><input type='checkbox' onchange='selectAll(" + i + ")' id='selectAll" + i + "' name='all" + i + "'/></th><th>测点编号</th><th>测点位置</th><th>初始高程(m)</th><th>本次变化量(mm)</th><th>累计变化量(mm)</th><th>变化速率(mm/d)</th><th>地表损失率(1/1000)</th><th>风险等级</th><th>测点备注</th>");
            $trTemp.appendTo("#R_Data" + i);
            for (var j = 0; j < reportSheetList[i].length; j++) {
                var checkBoxValue = reportSheetList[i][j].point;
                $trTemp = $("<tr></tr>");
                var pointName = reportSheetList[i][j].point;
                $trTemp.append("<td align='center' style='width: 20px' class='" + pointName + "'><input type='checkbox' id='checkbox' name='cb" + i + "' value='" + checkBoxValue + "'/></td>" +
                    "<td class='" + pointName + "'>" + pointName + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].ringLocation + "</td>" +
                    "<td class='" + pointName + "'>" + reportSheetList[i][j].initialHeight + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].changeQuantity + "</td>" +
                    "<td class='" + pointName + "'>" + reportSheetList[i][j].cumulativeVariation + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].changeRate + "</td>" +
                    "<td class='" + pointName + "'>" + reportSheetList[i][j].formationLossRate + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].levelOfRisk + "</td>" +
                    "<td class='" + pointName + "'>" + reportSheetList[i][j].remarks + "</td>");
                $trTemp.appendTo("#R_Data" + i);
                if (reportSheetList[i][j].levelOfRisk === "一级") {
                    $("." + pointName).css("background-color", "yellow");
                }
                if (reportSheetList[i][j].levelOfRisk === "二级") {
                    $("." + pointName).css("background-color", "orange");
                }
                if (reportSheetList[i][j].levelOfRisk === "三级") {
                    $("." + pointName).css({"background-color": "#B22222", "color": "white"});
                }
            }
        } else if (reportSheetList[i][0].sheetName.indexOf("建筑") !== -1) {
            $trTemp = $("<tr></tr>");
            $trTemp.append("<th align='center' style='width: 20px'><input type='checkbox' onchange='selectAll(" + i + ")' id='selectAll" + i + "' name='all" + i + "'/></th><th>测点编号</th><th>测点位置</th><th>初始高程(m)</th><th>本次变化量(mm)</th><th>变化速率(mm/d)</th><th>累计变化量(mm)</th><th>风险等级</th><th>测点备注</th>");
            $trTemp.appendTo("#R_Data" + i);
            for (j = 0; j < reportSheetList[i].length; j++) {
                $trTemp = $("<tr></tr>");
                checkBoxValue = reportSheetList[i][j].point;
                pointName = reportSheetList[i][j].point;
                $trTemp.append("<td align='center' style='width: 20px' class='" + pointName + "'><input type='checkbox' id='checkbox' name='cb" + i + "' value='" + checkBoxValue + "'/></td>" +
                    "<td class='" + pointName + "'>" + pointName + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].ringLocation + "</td>" +
                    "<td class='" + pointName + "'>" + reportSheetList[i][j].initialHeight + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].changeQuantity + "</td>" +
                    "<td class='" + pointName + "'>" + reportSheetList[i][j].changeRate + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].cumulativeVariation + "</td>" +
                    "<td class='" + pointName + "'>" + reportSheetList[i][j].levelOfRisk + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].remarks + "</td>");
                $trTemp.appendTo("#R_Data" + i);
                if (reportSheetList[i][j].levelOfRisk === "一级") {
                    $("." + pointName).css("background-color", "yellow");
                }
                if (reportSheetList[i][j].levelOfRisk === "二级") {
                    $("." + pointName).css("background-color", "orange");
                }
                if (reportSheetList[i][j].levelOfRisk === "三级") {
                    $("." + pointName).css({"background-color": "#B22222", "color": "white"});
                }
            }
        } else {
            $trTemp = $("<tr></tr>");
            $trTemp.append("<th align='center' style='width: 20px'><input type='checkbox' onchange='selectAll(" + i + ")' id='selectAll" + i + "' name='all" + i + "'/></th><th>测点编号</th><th>测点位置</th><th>初始高程(m)</th><th>本次高程(m)</th><th>本次变化量(mm)</th><th>变化速率(mm/d)</th><th>累计变化量(mm)</th><th>风险等级</th><th>测点备注</th>");
            $trTemp.appendTo("#R_Data" + i);
            for (j = 0; j < reportSheetList[i].length; j++) {
                $trTemp = $("<tr></tr>");
                checkBoxValue = reportSheetList[i][j].point;
                pointName = reportSheetList[i][j].point;
                $trTemp.append("<td align='center' style='width: 20px' class='" + pointName + "'><input type='checkbox' id='checkbox' name='cb" + i + "' value='" + checkBoxValue + "'/></td>" +
                    "<td class='" + pointName + "'>" + pointName + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].ringLocation + "</td>" +
                    "<td class='" + pointName + "'>" + reportSheetList[i][j].initialHeight + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].height + "</td>" +
                    "<td class='" + pointName + "'>" + reportSheetList[i][j].changeQuantity + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].changeRate + "</td>" +
                    "<td class='" + pointName + "'>" + reportSheetList[i][j].cumulativeVariation + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].levelOfRisk + "</td>" +
                    "<td class='" + pointName + "'>" + reportSheetList[i][j].remarks + "</td>");
                $trTemp.appendTo("#R_Data" + i);
                if (reportSheetList[i][j].levelOfRisk === "一级") {
                    $("." + pointName).css("background-color", "yellow");
                }
                if (reportSheetList[i][j].levelOfRisk === "二级") {
                    $("." + pointName).css("background-color", "orange");
                }
                if (reportSheetList[i][j].levelOfRisk === "三级") {
                    $("." + pointName).css({"background-color": "#B22222", "color": "white"});
                }
            }
        }
    }
}

function selectAll(i) {
    var id = "selectAll" + i;
    var isCheck = $("#" + id).is(':checked');
    var cbname = "cb" + i;
    $("input[name='" + cbname + "']").each(function () {
        this.checked = isCheck;       //循环赋值给每个复选框是否选中
    });
}

function clearCheckBox() {
    $("input[type='checkbox']").removeAttrs("checked");
}

function makeGraph() {
    var formData = new FormData();
    var points = [];
    var graphName = null;
    var sheetName = $("#myTab").children("li.active").children().html();
    formData.append("sheetName", sheetName);

    var radioVal = $("input[type='radio']:checked").val();
    if (radioVal == null) {
        showMessage("请选择需要的数据列！")
    } else {
        formData.append("column", radioVal);
        $("input:checkbox[type='checkbox']:checked").each(function () {
            var value = $(this).val();
            if (radioVal === "cumulativeVariation") {
                graphName = sheetName + "累计变化量";
            } else {
                graphName = sheetName + "变化速率";
            }

            if (value !== "on") {
                console.info(value);
                points.push(value);
            }
        });
        if (points.length > 0) {
            formData.append("points", points);
            var selectid = $.cookie('selected_id');
            if (selectid != null && selectid !== undefined) {
                formData.append("selected_id", selectid);
            }
            $.ajax({
                type: 'post',
                url: toolkitPath + '/raising/forward/summaryInfo/getPointsData.do',
                data: formData,
                cache: false,
                processData: false,
                contentType: false
            }).success(function (allData) {
                if (allData != null) {
                    console.info("图表名称为：" + graphName);
                    $("#lineDataGraph").show();
                    $("#lineDataGraphGround").show();
                    $("#lineTitle").html();
                    console.info(allData);
                    getLineGraph(allData, graphName);

                } else {
                    showMessage("没有数据！");
                }
            }).error(function () {
                showMessage("没有获得数据！");
            });
        } else {
            showMessage("请勾选需要的数据！");
        }
    }
}

function hideBootBox() {
    $("#lineDataGraph").hide();
    $("#lineDataGraphGround").hide();
}

var highCharts = null;

function getLineGraph(allData, graphName) {
    var lineData = allData.seriesDatas;
    var datas = lineData[0].data;
    var dataLength = datas.length;
    var startTime = datas[0][0];
    var endTime = datas[dataLength - 1][0];
    initDateRangePicker(startTime, endTime);
    Highcharts.setOptions({//修改时区
        lang: {
            rangeSelectorZoom: "范围",
            rangeSelectorFrom: "开始时间",
            rangeSelectorTo: "结束时间",
            loading: '加载中...',
            shortMonths: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
            months: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
            weekdays: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
        },
        global: {
            timezoneOffset: -8 * 60
            //useUTC: false
        }
    });
    highCharts = Highcharts.stockChart('container', {
        rangeSelector: {
            buttons: [{
                type: 'day',
                count: 15,
                text: '15天'
            }, {
                type: 'month',
                count: 1,
                text: '1个月'
            }, {
                type: 'month',
                count: 3,
                text: '3个月'
            }, {
                type: 'month',
                count: 6,
                text: '半年'
            }, {
                type: 'year',
                count: 1,
                text: '一年'
            }, {
                type: 'all',
                text: '所有'
            }],
            selected: 5,
            inputBoxWidth: 100,
            inputDateFormat: '%Y-%m-%d',
            inputEditDateFormat: '%Y-%m-%d',
            inputEnabled: false
        },
        xAxis: {
            dateTimeLabelFormats: {
                day: "%Y-%m-%d",
                week: "%Y-%m",
                month: "%Y-%m",
                year: "%Y"
            }
        },
        tooltip: {
            dateTimeLabelFormats: {
                day: "%Y-%m-%d",
                week: "%Y-%m-%d",
                month: "%Y-%m",
                year: "%Y"
            }, split: false
        },
        navigator: {
            xAxis: {
                dateTimeLabelFormats: {
                    day: "%Y-%m-%d",
                    week: "%Y-%m",
                    month: "%Y-%m",
                    year: "%Y"
                }
            },
            series: lineData
        },
        legend: {
            enabled: true
        },
        title: {
            text: graphName
        },
        series: lineData
    });
}


function initDateRangePicker(startTime, endTime) {
    var daterange_btn_span = $('#daterange-btn span');
    var daterange_btn = $('#daterange-btn');

    //定义locale汉化插件
    var locale = {
        "format": 'YYYY-MM-DD',
        "separator": " - ",
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
    var options = {
        "timePicker": false,
        "timePicker24Hour": false,
        "timePickerSeconds": false,
        "timePickerIncrement": 1,
        'locale': locale,
        //汉化按钮部分
        ranges: {
            '最近7日': [moment().subtract(6, 'days'), moment()],
            '最近30日': [moment().subtract(29, 'days'), moment()],
            '本月': [moment().startOf('month'), moment().endOf('month')],
            '上月': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
            '上半年': [moment().startOf('year'), moment().subtract(6, 'month').startOf('month')],
            '下半年': [moment().subtract(6, 'month').startOf('month'), moment().endOf('year')],
            '今年': [moment().startOf('year'), moment().endOf('year')]
        },
        startDate: moment().subtract(29, 'days'),
        endDate: moment()
    };
    //初始化显示当前时间
    daterange_btn_span.html(timeStampToString(startTime) + ' - ' + timeStampToString(endTime));
    //日期控件初始化
    daterange_btn.daterangepicker(options, function (start, end) {
            console.info(start);
            console.info(end);
            daterange_btn_span.html(start.format('YYYY-MM-DD') + ' - ' + end.format('YYYY-MM-DD'));
        }
    );
    daterange_btn.on('apply.daterangepicker', function (evt, picker) {
        var startTime = moment(picker.startDate.toISOString()).unix() * 1000;
        var endTime = moment(picker.endDate.toISOString()).unix() * 1000;
        highCharts.xAxis[0].setExtremes(startTime, endTime);//设置hishstock 的显示时间

    });
}

function timeStampToString(timeStamp) {
    var d = new Date(timeStamp);    //根据时间戳生成的时间对象
    return (d.getFullYear()) + "-" + (d.getMonth() + 1) + "-" + (d.getDate());
}


function hideMessage() {
    $("#errorAlert").hide();
}

function showMessage(message) {
    $("#message").html(message);
    $("#errorAlert").show();

}