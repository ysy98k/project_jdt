var localData = [
    {
        name: "厦门地铁2号线2标#",
        project: "青博区间左线",
        status: "竣工",
        tbm: "广州中船34#",
        startTime: "2018-07-14 00:00:00",
        endTime: "2018-07-16 00:00:00"
    },{
        name: "铜湘区间",
        project: "中～井区间右线",
        status: "竣工",
        tbm: "广州中船50#",
        startTime: "2018-07-14 00:00:00",
        endTime: "2018-07-16 00:00:00"
    }
];

$(function(){
    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxcombobox'], function () {
        var gridOption = {
            colNames: ['区间名称', '所属项目', "状态", '盾构机', "计划开始时间", '计划竣工时间', "操作"],
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
                    name: 'status',
                    index: 'status',
                    width: 60,
                    editable: true
                },  {
                    name: 'tbm',
                    index: 'tbm',
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
                },  {
                    name: 'ope',
                    index: 'ope',
                    width: 90,
                    readOnly : true,
                    forbidExport:true,
                    formatter: function (value, grid, row, state) {
                        var projectDetailext =   "编辑";
                        var btnReset =
                            "<div style='display: inline-block'>" +
                            "<div class='btn btn-skinColor btn-xs' style='height:21px;border:0'>" +
                            "<a href='javascript:editProjectDetail()'><font color='#FFFFFF'>"+projectDetailext+"</font>" +
                            "</div>" +
                            "<div class='btn btn-skinColor btn-xs' style='height:21px;border:0;margin-left: 10px;background-color: red;'>" +
                            "<a href='javascript:editProjectDetail()'><font color='#FFFFFF'>删除</font>" +
                            "</div>" +
                            "</div>"
                        return btnReset;
                    }
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