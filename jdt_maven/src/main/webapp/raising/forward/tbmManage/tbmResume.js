var sequence = 0;


$(document).ready(function () {

    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'],
        function () {
            var gridOption = {
                primaryRowKey: "id",
                caption: false,
                colNames: [ 'id','时间','类型', '经手人'
                    ,'经手人电话', '联系人','联系人电话','备注'],
                colModel: [
                    {
                        name: 'id',
                        index: 'id',
                        width: 1,
                        hidden: true,
                        forbidCopy: false
                    },{
                        name: 'createTime',
                        index: 'createTime',
                        width: 40,
                        editable: false,
                        sortable: true
                    },
                    {
                        name: 'type',
                        index: 'type',
                        width: 40,
                        editable: false,
                        sortable: true
                    },
                    {
                        name: 'brokerage',
                        index: 'brokerage',
                        width: 30,
                        editable: false
                    },
                    {
                        name: 'brokeragePhone',
                        index: 'brokeragePhone',
                        width: 40,
                        editable: false
                    },
                    {
                        name: 'contacts',
                        index: 'contacts',
                        width: 30,
                        editable: false
                    },
                    {
                        name: 'contactsPhone',
                        index: 'contactsPhone',
                        width: 40,
                        editable: false
                    },
                    {
                        name: 'remarks',
                        index: 'remarks',
                        width: 40,
                        editable: false
                    }
                ],
                sortname:"序列",
                rownumbers: true,
                multiselect:false,
                sortorder: 'asc',
                height: 351,
                jsonReader: {
                    id: "id",
                    repeatitems: false
                }/*,
                onSortCol:function (index,iCol,sortorder) {
                    sequence = 0;
                }*/
            };
            var defaultData = [{
                label: "全部",
                value: ""
            }];

            var option = {
                queryParam: {"inqu_status":{"tbmId":$.cookie('selected_tbmId')}},
                dataPattern: "url",
                url: "/raising/forward/tbmManage/tbmResume/getRows.do",
                showMsgOpt: {
                    showMsgId: "alertdiv"
                },
                gridOption: gridOption,
                navGridOption: {
                    download: false,
                    downloadParam: {
                        downloadUrl: "/sectionManage.do/download.do"
                    },
                    upload: false,
                    uploadParam: {
                        uploadUrl: "/sectionManage.do?method=insertForUpload"
                    }
                }
            };
            $("#jqGrid").bxgrid(option);


        });


})
