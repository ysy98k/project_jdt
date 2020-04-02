
$(document).ready(function () {
    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxcombobox'], function () {

        //分页显示
        var imageGridOption = {
            primaryRowKey: "stationId",
            caption: false,

            colNames: ['名称', 'X(m)', 'Y(m)', 'Z(m)','更新时间'],
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
                }
            ],
            jsonReader: {
                id: "stationId",
                repeatitems: false
            },
            caption: "吊篮信息"
        };
        var imageOption = {
            queryParam: {"inqu_status": {"cookieId": $.cookie('selected_id')}},
            gridOption: imageGridOption,
            dataPattern: "url",
            url: "/raising/forward/stationConfig.do?method=query",
            navGridOption: {
                download: true,
                downloadParam: {
                    downloadUrl: "/raising/forward/stationConfig.do/downloadExcel.do"
                }
            }
        };
        $("#gridImage").bxgrid(imageOption);
    });
});

// points格式：[{mileage: 1, x: 1, y: 1, z: 1 }, {mileage: 1, x: 1, y: 1, z: 1 }, {mileage: 1, x: 1, y: 1, z: 1 }...]
// type: 0 表示绘制平曲线，1表示绘制竖曲线


