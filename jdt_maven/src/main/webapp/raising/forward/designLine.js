
$(document).ready(function () {
    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxcombobox'], function () {
        var callback = {
            onSuccess: function (paramJsonObj) {
                var data = paramJsonObj.rows;

                var hline = new DesignLine("linePhoto1", data, 0); // 绘制平曲线
                var vline = new DesignLine("linePhoto2", data, 1); // 绘制竖曲线
            }

        };

        var paramJsonObj = {"inqu_status": {"cookieId": $.cookie('selected_id')}, "curPage": 1, "curRowNum": 10000};
        AjaxCommunicator.ajaxRequest('/raising/forward/designLine.do?method=query', 'POST', paramJsonObj, callback,true);

        var imageGridOption = {
            primaryRowKey: "projectId",
            caption: false,

            colNames: ['x', 'y', 'z', '里程'],
            colModel: [
                {
                    name: 'x',
                    index: 'x',
                    width: 60
                }, {
                    name: 'y',
                    index: 'y',
                    width: 60,
                }, {
                    name: 'z',
                    index: 'z',
                    width: 60
                }, {
                    name: 'designmileage',
                    index: 'designmileage',
                    width: 60
                }
            ],
            sortorder: 'asc',
            jsonReader: {
                id: "projectId",
                repeatitems: false
            },
            caption: "设计线"
        };

        var imageOption = {
            queryParam: {"inqu_status": {"cookieId": $.cookie('selected_id')}},
            gridOption: imageGridOption,
            dataPattern: "url",
            url: "/raising/forward/designLine.do?method=query",
            navGridOption: {
                download: true,
                downloadParam: {
                    downloadUrl: "/raising/forward/designLine.do/downloadExcel.do"
                }
            }
        };
        $("#gridImage").bxgrid(imageOption);
    });
});




