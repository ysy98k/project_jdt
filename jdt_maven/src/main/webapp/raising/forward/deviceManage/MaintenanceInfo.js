var localData = [{
    id: "021",
        name: "台灯",
    tradeday: "2015-12-21",
    amount: "2100",
    phone: "0230721",
    desc: "2007-12-03",
    status: 'pay',
    area: 2
}, {
    id: "022",
        name: "显示器",
        tradeday: "2015-12-22",
        amount: "2200",
        phone: "0230722",
        desc: "2007-12-03",
        status: 'pay',
        area: 2
}, {
    id: "023",
        name: "话筒",
        tradeday: "2015-12-23",
        amount: "2300",
        phone: "0230723",
        desc: "2007-12-03",
        status: 'notpay',
        area: 2
}];


$(document).ready(function () {
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



