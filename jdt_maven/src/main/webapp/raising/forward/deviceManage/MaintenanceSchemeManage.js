var localData = [{
    bId: "021",
    name: "中交天和盾构保养方案"
}, {
    bId: "022",
    name: "铁建重工盾构保养方案"
}];

$(function(){
    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxcombobox'], function () {
        var gridOption = {
            colNames: ['编号', '方案名称', "操作"],
            colModel: [
                {
                    name: 'bId',
                    index: 'bId',
                    width: 60
                }, {
                    name: 'name',
                    index: 'name',
                    width: 60,
                    editable: true
                }, {
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