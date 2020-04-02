var localData = [
    {
        name: "厦门地铁2号线2标#",
        project: "青博区间左线",
        distance: "1700",
        tbmNum: "广州中船34#",
        tbmParameter: "",
        startTime: "2018-08-16 00:00:00",
        endTime: "2019-03-10 00:00:00",
        schedule: "2"
    },{
        name: "厦门地铁2号线2标#",
        project: "青博区间左线",
        distance: "200",
        tbmNum: "广州中船50#",
        tbmParameter: "2018-07-14 00:00:00",
        startTime: "",
        endTime: "",
        schedule: ""
    }
];

$(function(){
    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxcombobox'], function () {
        var gridOption = {
            colNames: ['区间名称', '项目名称', "区间距离(m）", '盾构号', "盾构参数", '始发日期', "贯通日期","工期(D)"],
            colModel: [
                {
                    name: 'name',
                    index: 'name',
                    width: 60
                }, {
                    name: 'project',
                    index: 'project',
                    width: 60,
                    editable: true
                }, {
                    name: 'distance',
                    index: 'distance',
                    width: 60,
                    editable: true
                },  {
                    name: 'tbmNum',
                    index: 'tbmNum',
                    width: 60,
                    editable: true
                },  {
                    name: 'tbmParameter',
                    index: 'tbmParameter',
                    width: 60,
                    editable: true
                },  {
                    name: 'startTime',
                    index: 'startTime',
                    width: 60,
                    editable: true
                },  {
                    name: 'endTime',
                    index: 'endTime',
                    width: 60,
                    editable: true
                }, {
                    name: 'schedule',
                    index: 'schedule',
                    width: 60,
                    editable: true
                }],
            data: localData,
            caption: false,
            height: 351
        };

        var option = {
            queryParam: {},
            gridOption: gridOption,
            dataPattern: 'local'
        };
        $("#grid").bxgrid(option);

    });
})