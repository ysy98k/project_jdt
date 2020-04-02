var codename;
var detailData;
$(document).ready(function () {
    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxcombobox'], function () {


        //分页显示
        var imageGridOption = {
            primaryRowKey: "projectId",
            colNames: ['环号','切口里程', '切口X坐标值', '切口Y坐标值','切口Z坐标值','切口水平偏差','切口垂直偏差','主动铰接里程','铰接X坐标','铰接Y坐标','铰接Z坐标','主动铰接水平偏差','主动铰接垂直偏差','被动铰接里程','铰接X坐标','铰接Y坐标','铰接Z坐标','被动铰接水平偏差','被动铰接垂直偏差','盾尾里程','盾尾X坐标','盾尾Y坐标','盾尾Z坐标','盾尾水平偏移','盾尾垂直偏移','前盾水平趋势','前盾垂直趋势','前盾滚动角','前盾俯仰角','前盾方位角','后盾水平趋势','后盾垂直趋势','后盾滚动角','后盾俯仰角','后盾方位角'],
            colModel: [
                {
                    name: 'MR_Ring_Number',
                    index: 'MR_Ring_Number',
                    width: 100
                },
                {
                    name: 'MR_Des_A1Mileage',
                    index: 'MR_Des_A1Mileage',
                    width: 100
                }, {
                    name: 'MR_Act_A1X',
                    index: 'MR_Act_A1X',
                    width: 100
                }, {
                    name: 'MR_Act_A1Y',
                    index: 'MR_Act_A1Y',
                    width: 100
                }, {
                    name: 'MR_Act_A1Z',
                    index: 'MR_Act_A1Z',
                    width: 100
                }, {
                    name: 'MR_Act_A1HD',
                    index: 'MR_Act_A1HD',
                    width: 100
                }, {
                    name: 'MR_Act_A1VD',
                    index: 'MR_Act_A1VD',
                    width: 100
                }, {
                    name: 'MR_Des_A3Mileage',
                    index: 'MR_Des_A3Mileage',
                    width: 100
                }, {
                    name: 'MR_Act_A3X',
                    index: 'MR_Act_A3X',
                    width: 100
                }, {
                    name: 'MR_Act_A3Y',
                    index: 'MR_Act_A3Y',
                    width: 100
                }, {
                    name: 'MR_Act_A3Z',
                    index: 'MR_Act_A3Z',
                    width: 100
                }, {
                    name: 'MR_Act_A3HD',
                    index: 'MR_Act_A3HD',
                    width: 100
                }, {
                    name: 'MR_Act_A3VD',
                    index: 'MR_Act_A3VD',
                    width: 100
                }, {
                    name: 'MR_Des_A5Mileage',
                    index: 'MR_Des_A5Mileage',
                    width: 100
                }, {
                    name: 'MR_Act_A5X',
                    index: 'MR_Act_A5X',
                    width: 100
                }, {
                    name: 'MR_Act_A5Y',
                    index: 'MR_Act_A5Y',
                    width: 100
                }, {
                    name: 'MR_Act_A5Z',
                    index: 'MR_Act_A5Z',
                    width: 100
                }, {
                    name: 'MR_Act_A5HD',
                    index: 'MR_Act_A5HD',
                    width: 100
                }, {
                    name: 'MR_Act_A5VD',
                    index: 'MR_Act_A5VD',
                    width: 100
                }, {
                    name: 'MR_Des_A9Mileage',
                    index: 'MR_Des_A9Mileage',
                    width: 100
                }, {
                    name: 'MR_Act_A9X',
                    index: 'MR_Act_A9X',
                    width: 100
                }, {
                    name: 'MR_Act_A9Y',
                    index: 'MR_Act_A9Y',
                    width: 100
                }, {
                    name: 'MR_Act_A9Z',
                    index: 'MR_Act_A9Z',
                    width: 100
                }, {
                    name: 'MR_Act_A9HD',
                    index: 'MR_Act_A9HD',
                    width: 100
                }, {
                    name: 'MR_Act_A9VD',
                    index: 'MR_Act_A9VD',
                    width: 100
                }, {
                    name: 'MR_F_HT',
                    index: 'MR_F_HT',
                    width: 100
                }, {
                    name: 'MR_F_VT',
                    index: 'MR_F_VT',
                    width: 100
                }, {
                    name: 'MR_F_RollAngle',
                    index: 'MR_F_RollAngle',
                    width: 100
                }, {
                    name: 'MR_F_PitchAngle',
                    index: 'MR_F_PitchAngle',
                    width: 100
                }, {
                    name: 'MR_F_AzimuthAngle',
                    index: 'MR_F_AzimuthAngle',
                    width: 100
                }, {
                    name: 'MR_B_HT',
                    index: 'MR_B_HT',
                    width: 100
                }, {
                    name: 'MR_B_VT',
                    index: 'MR_B_VT',
                    width: 100
                }, {
                    name: 'MR_B_RollAngle',
                    index: 'MR_B_RollAngle',
                    width: 100
                }, {
                    name: 'MR_B_PitchAngle',
                    index: 'MR_B_PitchAngle',
                    width: 100
                }, {
                    name: 'MR_B_AzimuthAngle',
                    index: 'MR_B_AzimuthAngle',
                    width: 100
                }],
            sortorder: 'asc',
            height:'340',
            autoScroll:false,
            shrinkToFit:false,
            jsonReader: {
                id: "projectId",
                repeatitems: false
            },
            caption: "测量结果"
        };

        var imageOption = {
            queryParam: {"inqu_status": {"cookieId": $.cookie('selected_id')}},
            gridOption: imageGridOption,
            dataPattern: "url",
            url: "/raising/forward/measureResult.do?method=query",
            navGridOption: {
                download: true,
                downloadParam: {
                    downloadUrl: "/raising/forward/measureResult.do/downloadExcel.do"
                }
            }
        };
        $("#gridImage").bxgrid(imageOption);
        $("#gridImage").find(".ui-jqgrid-bdiv").css({
            'overflow-x':'scroll'
        })
    });


});



