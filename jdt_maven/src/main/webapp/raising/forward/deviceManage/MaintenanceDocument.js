$(document).ready(function () {
    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxcombobox'], function () {

        //分页显示
        var imageGridOption = {
            caption: false,

            colNames: ['编号', '文件名称', '上传用户', '上传文件日期','文件类型','归档类型','说明','操作'],
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
            ]
            //caption: "保养文件"
        };
        var imageOption = {
            queryParam: {"inqu_status": {"cookieId": $.cookie('selected_id')}},
            gridOption: imageGridOption,
            dataPattern: "local",
           // url: "/raising/forward",
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

function makeGraph(){
    //初始化
    baosightRequire.requireFunct(['bxdiv', 'bxdialog'], function() {
        var dialog = $("#upanCreate")
            .removeClass('hide')
            .dialog(
                {
                    close: function (event, ui) {
                        //onpopupclose();
                    },
                    width: 700,
                    height: 430,
                    modal: true,
                    title: "<div class='widget-header'><h4 class='smaller'><i class='ace-icon fa fa-exclamation-triangle red'></i> "
                    + "附件归档信息" + "</h4></div>",
                    title_html: true,
                    buttons: [{
                        text: "保存文件",
                        "class" : "btn btn-skinColor btn-xs",
                        click : function () {
                            sectionDetailConfirmButtonClick(rowId);
                        }
                    }]
                });
    })
}