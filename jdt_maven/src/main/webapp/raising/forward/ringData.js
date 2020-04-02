
$(document).ready(function () {
    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxcombobox'], function () {
        //分页显示
        var imageGridOption = {
            primaryRowKey: "projectId",
            colNames: ['环号', '当前环点位号','正上盾尾间隙值', '正下盾尾间隙值', '正左盾尾间隙值','正右盾尾间隙值','末端面中心点坐标X','末端面中心点坐标Y','末端面中心点坐标Z','末端面中心点里程','末端面法向量坐标X','末端面法向量坐标Y','末端面法向量坐标Z','管片中心水平偏差','管片中心垂直偏差','创建时间'],
            colModel: [
                {
                    name: 'RD_RingNum',
                    index: 'RD_RingNum',
                    width: 80
                },
                {
                    name: 'RD_point',
                    index: 'RD_point',
                    width: 120
                },
                {
                    name: 'RD_up',
                    index: 'RD_up',
                    width: 120
                }, {
                    name: 'RD_down',
                    index: 'RD_down',
                    width: 120
                }, {
                    name: 'RD_left',
                    index: 'RD_left',
                    width: 120
                }, {
                    name: 'RD_right',
                    index: 'RD_right',
                    width: 120
                },  {
                    name: 'RD_X',
                    index: 'RD_X',
                    width: 130
                }, {
                    name: 'RD_Y',
                    index: 'RD_Y',
                    width: 130
                }, {
                    name: 'RD_Z',
                    index: 'RD_Z',
                    width: 130
                }, {
                    name: 'RD_Mileage',
                    index: 'RD_Mileage',
                    width: 130
                }, {
                    name: 'RD_Normal_X',
                    index: 'RD_Normal_X',
                    width: 130
                }, {
                    name: 'RD_Normal_Y',
                    index: 'RD_Normal_Y',
                    width: 130
                }, {
                    name: 'RD_Normal_Z',
                    index: 'RD_Normal_Z',
                    width: 130
                }, {
                    name: 'RD_Segment_HD',
                    index: 'RD_Segment_HD',
                    width: 130
                }, {
                    name: 'RD_Segment_VD',
                    index: 'RD_Segment_VD',
                    width: 130
                }, {
                    name: 'RD_CreatTime',
                    index: 'RD_CreatTime',
                    width: 180
                }],
            sortorder: 'asc',
            height:310,
            autoScroll:false,
            shrinkToFit:false,
            jsonReader: {
                id: "projectId",
                repeatitems: false
            },
            caption: "管片成型质量"
        };

        var imageOption = {
            queryParam: {"inqu_status": {"cookieId": $.cookie('selected_id')}},
            gridOption: imageGridOption,
            dataPattern: "url",
            url: "/raising/forward/ringData.do?method=query",
            navGridOption: {
                download: true,
                downloadParam: {
                    downloadUrl: "/raising/forward/ringData.do/downloadExcel.do"
                }
            }
        };
        $("#gridImage").bxgrid(imageOption);
        $("#gridImage").find(".ui-jqgrid-bdiv").css({
            'overflow-x':'scroll'
        })
    });


});



