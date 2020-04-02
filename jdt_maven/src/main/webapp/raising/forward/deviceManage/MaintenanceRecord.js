


$(document).ready(function () {
    init();
    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxcombobox'], function () {

        //分页显示
        var imageGridOption = {
            primaryRowKey: "stationId",
            caption: false,

            colNames: ['编号', '状态', '部件', '保养项目','上次完成时间','下次计划时间','计划到期时间','周期'],
            colModel: [
                {
                    name: 'dbName',
                    index: 'dbName',
                    width: 60
                },
                {
                    name: 'pointX',
                    index: 'pointX',
                    width: 60
                }, {
                    name: 'pointY',
                    index: 'pointY',
                    width: 60,
                }, {
                    name: 'pointZ',
                    index: 'pointZ',
                    width: 60
                }, {
                    name: 'updateTime',
                    index: 'updateTime',
                    width: 60
                },{
                    name: 'nextPlanTime',
                    index: 'nextPlanTime',
                    width: 60
                },
                {
                    name: 'plannedExpirationDate',
                    index: 'plannedExpirationDate',
                    width: 60
                }, {
                    name: 'cycle',
                    index: 'cycle',
                    width: 60,
                }
            ],
            jsonReader: {
                id: "stationId",
                repeatitems: false
            },
            caption: "保养信息"
        };
        var imageOption = {
            queryParam: {"inqu_status": {"cookieId": $.cookie('selected_id')}},
            gridOption: imageGridOption,
            dataPattern: "url",
            url: "/raising/forward",
            navGridOption: {
                download: true,
                downloadParam: {
                    downloadUrl: "/raising/forward/stationConfig.do/downloadExcel.do"
                }
            }
        };
        $("#grid").bxgrid(imageOption);
    });
});


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
