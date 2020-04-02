

$(document).ready(function () {


    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'],
        function () {
            $("#queryarea").bxdiv();
            var gridOption = {
                primaryRowKey: "projectId",
                caption: false,
                colNames: ['项目Id', '项目名称', '采集器名称','采集器对比名称',  '方向','总环数','施工单位','项目业主','区间名称', '详细信息','创建时间'],
                colModel: [{
                    name: 'projectId',
                    index: 'projectId',
                    width: 10,
                    hidden: true,
                    forbidCopy: true
                },
                    {
                        name: 'projectName',
                        index: 'projectName',
                        width: 50,
                        editable: true,
                        sortable: true,
                        editrules: {
                            required: true,
                        }
                    },
                    {
                        name: 'collectorName',
                        index: 'collectorName',
                        width: 50,
                        editable: true,
                        forbidCopy: true
                    },
                    {
                        name: 'collectorName2',
                        width: 10,
                        hidden: true,
                        forbidCopy: true,
                        forbidExport:true,
                        readOnly:true,
                        formatter: function (value, grid, row, state) {
                            return row.collectorName;
                        }
                    },
                    {
                        name: 'tunnelDrection',
                        index: 'tunnelDrection',
                        width: 20,
                        editable: true,
                        sortable: true,
                        editrules: {
                            required: true,
                        },
                        edittype:'select',
                        editoptions: {value: "左:左; 右:右"}
                    },
                    {
                        name: 'ringTotal',
                        index: 'ringTotal',
                        width: 25,
                        editable: true,
                        sortable: true,
                        editrules: {
                            required: true,
                            custom_func: function(val,name){
                                var num =  Number(val);
                                if(isNaN(val) ){
                                    return [false, "列【" + name + "】的值【" + val+ "】格式出错：格式应该为数字", ""];
                                }
                                return [true,""];
                            }

                        }
                    },
                    {
                        name: 'buildUnit',
                        index: 'buildUnit',
                        width: 50,
                        editable: true,
                        sortable: true,
                        editrules: {
                            required: true,
                        }
                    },
                    {
                        name: 'sectionOwner',
                        index: 'sectionOwner',
                        width: 50,
                        editable: false,
                        readOnly:true,
                        sortable: true,
                        forbidCopy: true
                    },{
                        name: 'sectionName',
                        index: 'sectionName',
                        width: 50,
                        editable: true,
                        editrules: {
                            required: true
                        },
                        edittype:'select',
                        editoptions: {value: getSection()}
                    },{
                        name: 'projectDetail',
                        width: 50,
                        readOnly : true,
                        forbidExport:true,
                        formatter: function (value, grid, row, state) {
                            var projectDetailext =  row.projectId == undefined ? "编辑" : "项目详细信息";
                            var btnReset =
                                "<div style='display: inline-block'>" +
                                "<div class='btn btn-skinColor btn-xs' style='height:21px;border:0'>" +
                                "<a href='javascript:editProjectDetail(\""+grid.rowId+"\",\""+row.projectId+"\")'><font color='#FFFFFF'>"+projectDetailext+"</font>" +
                                "</div>" +
                                "</div>"

                            return btnReset;
                        }
                    },
                    {
                        name: 'createTime',
                        index: 'createTime',
                        width: 50,
                        editable: true,
                        sortable: true,
                        editrules: {
                            required: true,
                            custom: true,
                            custom_func: createTimeCheck
                        },
                        editoptions: {
                            size:100,maxlengh:200,length:150,
                            dataInit:function(element){
                                element.value = isNullOrEmptyOrUndefiend(element.value) == true ? moment().format('YYYY-MM-DD HH:mm:ss') : element.value;
                                //日期控件初始化
                                $(element).daterangepicker(getDateRangPickerOption(element.value,element.value), function(start, end, label) {
                                    var s  = start.format('YYYY-MM-DD HH:mm:ss');
                                    element.value = s;
                                });

                            }
                        }
                    }
                ],
                sortorder: 'asc',
                height: 351,
                jsonReader: {
                    id: "projectId",
                    repeatitems: false
                }
            };
            var defaultData = [{
                label: "全部",
                value: ""
            }];
            $("#inqu_status-pageType").bxcombobox({
                dataPattern: 'ccs',
                ccsId: "metadata.pageType",
                async: false,
                data: defaultData
            });
            var option = {
                queryParam: {},
                dataPattern: "url",
                url: "/raising/backstage/project/getProjects.do",
                showMsgOpt: {
                    showMsgId: "alertdiv"
                },
                gridOption: gridOption
            };
            $("#jqGrid").bxgrid(option);

        });

    initTime();
    initSelectQuery();
    getSection();
    initProjectDetailSelect();
});


/**
 * 查询按钮点击事件
 */
function on_query_click() {
    var queryParam = new Object();
    $("#queryarea").bxdiv('setQueryFromDiv', queryParam, "inqu_status");
    $("#jqGrid").bxgrid("option", "queryParam", queryParam);
    var sectionName = queryParam.inqu_status.sectionName;
    var tbmName = queryParam.inqu_status.tbmName;
    var projectName = queryParam.inqu_status.projectName;
    var collectorName = queryParam.inqu_status.collectorName;
    if(isNullOrEmptyOrUndefiend(sectionName) && isNullOrEmptyOrUndefiend(tbmName)
        && isNullOrEmptyOrUndefiend(projectName) && isNullOrEmptyOrUndefiend(collectorName)){
    }else{//如果输入框有值。则忽略下拉框的值。
        queryParam.inqu_status.sectionCcsId = null;
        queryParam.inqu_status.sectionOwner = null;
        queryParam.inqu_status.ccsSectionType = null;

        queryParam.inqu_status.factory = null;
        queryParam.inqu_status.rmsVersion = null;
        queryParam.inqu_status.tbmOwner = null;
        queryParam.inqu_status.tbmCcsType = null;

        queryParam.inqu_status.supervisor = null;
        queryParam.inqu_status.buildUnit = null;
    }
    $("#jqGrid").bxgrid("query");
}

/**
 * 新增/复制按钮点击事件
 */
function addAndCopy() {
    $("#jqGrid").bxgrid("addAndCopy");
}

/**
 * 保存按钮点击事件
 */
function saveRec() {
    var insertOrUpdate = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'insertOrUpdate');
    if (insertOrUpdate == "insert") {
        insertRec();
    } else {
        updateRec();
    }
}


/**
 * 新增
 */
function insertRec() {
    var selectArray = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if (selectArray.length == 0) {
        alertDiv("提示", "请至少勾选一条记录进行操作！")
        return;
    }
    var paramJsonObj = new Object();
    paramJsonObj.detail = {};
    paramJsonObj.detail.resultRow = [];
    //对数据做校验，处理。填充入参。
    for (var i = 0; i < selectArray.length; i++) {
        var sectionId = $("#"+selectArray[i]).find("select[name='sectionName']").val();
        var status = $("#jqGrid").bxgrid('rawMethodCall', "saveRow", selectArray[i]);
        if (status == false) {
            return;
        }
        var saveData = $("#jqGrid").bxgrid('rawMethodCall', "getRowData", selectArray[i]);
        //处理时间列的值
        saveData.createTime = saveData.createTime.trim();
        //处理saveData对象，项目详细属性的值。
        var divObj = document.createElement("div");
        divObj.innerHTML = saveData.projectDetail;
        var length = $(divObj).find("font").length;
        if(length < 2){
            alertDiv("提示","填写项目详细信息。");
            return;
        }
        var projectDetailStr = divObj.getElementsByTagName("font")[1].innerHTML;
        var projectDetailJson = $.parseJSON( projectDetailStr);
        var check = checkProjectDetail(projectDetailJson);
        if( check.status == false){
            alertDiv("提示",check.message);
            return ;
        }
        saveData.sectionId = sectionId;
        saveData = setObject(saveData,projectDetailJson);
        paramJsonObj.detail.resultRow.push(saveData);
    }

    var callback = {
        onSuccess: function (paramJsonObj) {
            var showMsgOpt = {
                showMsgId: "alertdiv",
                status: paramJsonObj.status,
                showMsg: paramJsonObj.returnMsg
            };
            $("#jqGrid").bxgrid("option", "showMsgOpt", showMsgOpt);
            $("#jqGrid").bxgrid("query");
        }






    };
    AjaxCommunicator.ajaxRequest('/raising/backstage/project/addProjects.do', 'POST', paramJsonObj, callback,true);
}

/**
 * 删除
 */
function deleteRec() {
    var selectArray = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if (selectArray.length == 0) {
        alertDiv("提示", "请至少勾选一条记录进行操作！")
        return;
    }
    var buttons = [{
        html: "<i class='ace-icon fa fa-check bigger-110'></i>&nbsp; 是",
        "class": "btn btn-skinColor btn-xs",
        click: function () {
            deleteOK();
            $(this).dialog("close");
        }
    }, {
        html: "<i class='ace-icon fa fa-times bigger-110'></i>&nbsp; 否",
        "class": "btn btn-xs",
        click: function () {
            $(this).dialog("close");
        }
    }];
    confirmDiv("确认删除", "数据删除后将不可恢复，一般情况下程序会自动删除相关资源，如未删除，请自行手工删除，是否确定删除？", buttons);
}

function deleteOK() {
    var paramJsonObj = new Object();
    $("#jqGrid").bxgrid('setInfoFromGrid', paramJsonObj, 'result');
    var callback = {
        onSuccess: function (paramJsonObj) {
            var showMsgOpt = {
                showMsgId: "alertdiv",
                status: paramJsonObj.status,
                showMsg: paramJsonObj.returnMsg
            };

            $("#jqGrid").bxgrid("option", "showMsgOpt", showMsgOpt);
            $("#jqGrid").bxgrid("query");
        }
    };
    AjaxCommunicator.ajaxRequest('/raising/backstage/project/deleteProject.do', 'POST', paramJsonObj, callback,true);
}

/**
 * 修改
 */
function updateRec() {
    var selectArray = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if (selectArray.length == 0) {
        alertDiv("提示", "请至少勾选一条记录进行操作！")
        return;
    }
    var paramJsonObj = new Object();
    paramJsonObj.detail = {};
    paramJsonObj.detail.resultRow = [];
    for (var i = 0; i < selectArray.length; i++) {
        var sectionId = $("#"+selectArray[i]).find("select[name='sectionName']").val();
        var status = $("#jqGrid").bxgrid('rawMethodCall', "saveRow", selectArray[i]);
        if (status == false) {
            return;
        }
        var saveData = $("#jqGrid").bxgrid('rawMethodCall', "getRowData", selectArray[i]);
        //处理时间列的值
        saveData.createTime = saveData.createTime.trim();
        saveData.sectionId = sectionId;
        //处理saveData对象ccsId属性的值
        var divObj = document.createElement("div");
        divObj.innerHTML = saveData.projectDetail;
        var length = $(divObj).find("font").length;
        if(length == 2){
            var projectDetailStr = divObj.getElementsByTagName("font")[1].innerHTML;
            var projectDetailJson = $.parseJSON( projectDetailStr);
            var check = checkProjectDetail(projectDetailJson);
            if( check.status == false){
                alertDiv("提示",check.message);
                return ;
            }
           saveData =  setObject(saveData,projectDetailJson);
        }
        paramJsonObj.detail.resultRow.push(saveData);
    }
    var callback = {
        onSuccess: function (paramJsonObj) {
            var showMsgOpt = {
                showMsgId: "alertdiv",
                status: paramJsonObj.status,
                showMsg: paramJsonObj.returnMsg
            };

            $("#jqGrid").bxgrid("option", "showMsgOpt", showMsgOpt);
            $("#jqGrid").bxgrid("query");
        }
    };
    AjaxCommunicator.ajaxRequest('/raising/backstage/project/updateProject.do', 'POST', paramJsonObj, callback,true);
}

//注册资源按钮
function on_grantresource_click() {
    var selectArray = $("#jqGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
    if (selectArray.length < 1) {
        alertDiv("记录数错误", "请至少选择一条记录进行授权！");
        return;
    }
    //创建对象入参，
    var selectedResource = [];
    //封装对象入参
    for (var i = 0; i < selectArray.length; i++) {
        var insertResource = {};
        var selectData = $("#jqGrid").bxgrid("getResotreRowDataById", selectArray[i]);
        if (selectData == undefined) {
            continue;
        }
        insertResource.name = selectData.collectorName;
        insertResource.display_name = selectData.projectName;
        insertResource.description = "";
        insertResource.service = 'PROP';
        selectedResource.push(insertResource);
    }
    var paramJsonObj = {};
    paramJsonObj.selectedResource = selectedResource;
    var callback = {
        onSuccess: function (paramJsonObj) {
            var showMsgOpt = {
                showMsgId: "alertdiv",
                status: paramJsonObj.status,
                showMsg: paramJsonObj.returnMsg
            };
            $("#jqGrid").bxgrid("option", "showMsgOpt", showMsgOpt);
            $("#jqGrid").bxgrid("query");
        }
    };
    AjaxCommunicator.ajaxRequest('/df/metamanage/pageManage.do?method=grantResource', 'POST', paramJsonObj, callback,true);

};


/**
 * 项目详细信息点击事件
 * @param rowId
 * @param projectId
 */
function editProjectDetail(rowId,projectId){
    $("#projectId").val("");
    $("#startMileage").val("");
    $("#endMileage").val("");
    $("#totalLength").val("");
    $("#geologyInfo").val("");
    $("#projectSituation").val("");
    $("#projectLocation").val("");
    $("#supervisor").val("");
    $("#startTime").val("");
    $("#endTime").val("");
    $("#dayShiftStart").val("07:00");
    $("#dayShiftEnd").val("19:00");
    //初始化表单。先判断是否编辑过
    var fontNum = $("#"+rowId).find("[aria-describedby='table_jqGrid_projectDetail']").eq(0).find("font").length;
    if(fontNum > 1){
        var projectDetailStr = $("#"+rowId).find("[aria-describedby='table_jqGrid_projectDetail']").eq(0).find("font[class='hide']").eq(0).html();
        var projectDetail = JSON.parse(projectDetailStr);
        $("#projectId").val(projectDetail.projectId);
        $("#tbmId").combobox('select',projectDetail.tbmId);
        $("#status").combobox('select',projectDetail.status);
        $("#templateName").combobox('select',projectDetail.templateName);
        $("#startMileage").val(projectDetail.startMileage);
        $("#endMileage").val(projectDetail.endMileage);
        $("#totalLength").val(projectDetail.totalLength);
        $("#geologyInfo").val(projectDetail.geologyInfo);
        $("#projectSituation").val(projectDetail.projectSituation);
        $("#projectLocation").val(projectDetail.projectLocation);
        $("#supervisor").val(projectDetail.supervisor);
        $("#startTime").val(projectDetail.startTime);
        $("#endTime").val(projectDetail.endTime);
        $("#dayShiftStart").val(projectDetail.dayShiftStart);
        $("#dayShiftEnd").val(projectDetail.dayShiftEnd);
    }else {//没有编辑过
        if (projectId != "undefined" && projectId != "null") { //修改
            //根据获取的区间段信息，填充表单
            var callback = {
                onSuccess: function (data) {
                    if (data.status == 0) {
                        var dataJson = data.rows[0];
                        $("#projectId").val(dataJson.projectId);
                        $("#tbmId").combobox('select',dataJson.tbmId);
                        $("#status").combobox('select',dataJson.status);
                        $("#templateName").combobox('select',dataJson.templateName);
                        $("#startMileage").val(dataJson.startMileage);
                        $("#endMileage").val(dataJson.endMileage);
                        $("#totalLength").val(dataJson.totalLength);
                        $("#geologyInfo").val(dataJson.geologyInfo);
                        $("#projectSituation").val(dataJson.projectSituation);
                        $("#projectLocation").val(dataJson.projectLocation);
                        $("#supervisor").val(dataJson.projectLocation);
                        if(!isNullOrEmptyOrUndefiend(dataJson.startTime)){
                            $("#startTime").val(dataJson.startTime);
                        }
                        if(!isNullOrEmptyOrUndefiend(dataJson.endTime)){
                            $("#endTime").val(dataJson.endTime);
                        }
                        $("#dayShiftStart").val(dataJson.dayShiftStart);
                        $("#dayShiftEnd").val(dataJson.dayShiftEnd);
                    } else {
                        alertDiv("提示", data.message);
                    }
                }
            }
            //访问后台，获取区间段详细信息
            var paramJson = {"inqu_status":{"projectId":projectId}};
            AjaxCommunicator.ajaxRequest('/raising/backstage/project/getProjects.do', 'POST', paramJson, callback,true);
        }
    }
    //初始化
    var button = [{
        text: "完成",
        "class" : "btn btn-skinColor btn-xs",
        click : function () {
            projectDetailSaveButtonClick(rowId);
        }
    }]
    var title = "项目详细信息";
    var dialogOpt = {
        title: title,
        buttons:button,
        width: '700px'
    };
    //显示对话框
    $("#editProjectDetail").bxdialog(dialogOpt);
}


/**
 * 项目详细信息，完成按钮点击事件
 * @param rowId
 */
function projectDetailSaveButtonClick(rowId){
    var button = [{
        text: "确认",
        "class" : "btn btn-skinColor btn-xs",
        click : function () {
            sectionDetailConfirmButtonClick(rowId);
        }
    },{
        text: "取消",
        "class" : "btn btn-skinColor btn-xs",
        click : function () {
            $("#confrmEdit").bxdialog('close');
        }
    }]
    var title = "项目详细信息";
    var dialogOpt = {
        title: title,
        buttons:button,
        width: '200px'
    };
    //显示对话框
    $("#confrmEdit").bxdialog(dialogOpt);
}

/**
 * 项目详细信息，保存确认对话框点击事件
 */
function sectionDetailConfirmButtonClick(rowId){
    //先关闭确认对话框
    $("#confrmEdit").bxdialog('close');
    //数据校验
    var projectDetailJson = {}
    projectDetailJson.tbmId = $("#tbmId").combobox("getValue");
    projectDetailJson.status = $("#status").combobox("getValue");
    projectDetailJson.templateName = $("#templateName").combobox("getValue");
    projectDetailJson.startMileage = $("#startMileage").val();
    projectDetailJson.endMileage = $("#endMileage").val();
    projectDetailJson.totalLength = $("#totalLength").val();
    projectDetailJson.geologyInfo = $("#geologyInfo").val();
    projectDetailJson.projectSituation = $("#projectSituation").val();
    projectDetailJson.projectLocation = $("#projectLocation").val();
    projectDetailJson.supervisor = $("#supervisor").val();
    projectDetailJson.startTime = $("#startTime").val();
    projectDetailJson.endTime = $("#endTime").val();
    projectDetailJson.dayShiftStart = $("#dayShiftStart").val();
    projectDetailJson.dayShiftEnd = $("#dayShiftEnd").val();
    var check = checkProjectDetail(projectDetailJson);
    if( check.status == false){
        alertDiv("提示",check.message);
        return ;
    }

    projectDetailJson.projectId = $("#projectId").val();
    //修改单元格的值。
    //先删除
    $("#"+rowId).find("[ aria-describedby='table_jqGrid_projectDetail']").eq(0).find("font[class='hide']").eq(0).remove();
    var projectDetailJsonStr = JSON.stringify(projectDetailJson);
    $("#"+rowId).find("[aria-describedby='table_jqGrid_projectDetail']").eq(0).append("<font class='hide'>"+projectDetailJsonStr+"</font>");
    $("#"+rowId).find("[aria-describedby='table_jqGrid_projectDetail']").eq(0).find("font[color='#FFFFFF']").eq(0).html("项目详细信息");
    $("#editProjectDetail").bxdialog("close");
    alertDiv("信息","编辑项目详细信息成功，请点击表格上方保存按钮保存！");

}

/**
 * 数据校验
 */
function checkProjectDetail(projectDetailJson){
    var returnJson = {}
    if(  projectDetailJson.status == "" ||projectDetailJson.startMileage == "" || isNullOrEmptyOrUndefiend(projectDetailJson.templateName)
        || projectDetailJson.endMileage == "" || projectDetailJson.totalLength == "" || projectDetailJson.tbmId == ""
        || projectDetailJson.dayShiftStart == "" || projectDetailJson.dayShiftEnd == ""
        || isNullOrEmptyOrUndefiend(projectDetailJson.startTime)  || isNullOrEmptyOrUndefiend(projectDetailJson.endTime) ){
        returnJson.status = false;
        returnJson.message = "被红色标记的数据项不可以为空，且格式正确。";
        return returnJson;
    }
    var reDateTime = /^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1])) (?:(?:[0-2][0-3])|(?:[0-1][0-9])):[0-5][0-9]:[0-5][0-9]$/;
    if(projectDetailJson.startTime.trim() != ""){
        if(!reDateTime.test(projectDetailJson.startTime.trim())){
            returnJson.status = false;
            returnJson.message = "时间格式出错！格式为2000-01-01 00:00:00";
            return returnJson;
        }
    }
    if(projectDetailJson.endTime.trim() != ""){
        if(!reDateTime.test(projectDetailJson.endTime.trim())){
            returnJson.status = false;
            returnJson.message = "时间格式出错！格式为2000-01-01 00:00:00";
            return returnJson;
        }
    }
    var startStr = projectDetailJson.dayShiftStart.substring(0,projectDetailJson.dayShiftStart.indexOf(":"));
    var endStr  = projectDetailJson.dayShiftEnd.substring(0,projectDetailJson.dayShiftEnd.indexOf(":"));
    if(Number(endStr) <= Number(startStr)){
        returnJson.status = false;
        returnJson.message = "白班结束时间必须大于白班起始时间";
        return returnJson;
    }
    var totalLength =  Number(projectDetailJson.totalLength);
    var startMileage =  Number(projectDetailJson.startMileage);
    var endMileage =  Number(projectDetailJson.endMileage);
    var tbmId =  Number(projectDetailJson.tbmId);
    if(isNaN(totalLength) || isNaN(startMileage)|| isNaN(endMileage) || isNaN(tbmId)){
        returnJson.status = false;
        returnJson.message = "被红色标记的数据项不可以为空，且格式正确。";
        return returnJson;
    }
    returnJson.status = true;
    return returnJson;

}

function setObject(saveData,paramJson){
    saveData.status = paramJson.status;
    saveData.tbmId = paramJson.tbmId;
    saveData.templateName = paramJson.templateName;
    saveData.startMileage = paramJson.startMileage;
    saveData.endMileage = paramJson.endMileage ;
    saveData.totalLength = paramJson.totalLength;
    saveData.supervisor = paramJson.supervisor;
    saveData.dayShiftStart = paramJson.dayShiftStart;
    saveData.dayShiftEnd = paramJson.dayShiftEnd;
    if(paramJson.geologyInfo != ""){
        saveData.geologyInfo = paramJson.geologyInfo;
    }
    if(paramJson.projectSituation !=""){
        saveData.projectSituation = paramJson.projectSituation;
    }
    if(paramJson.projectLocation != ""){
        saveData.projectLocation = paramJson.projectLocation;
    }
    if(paramJson.startTime.trim() != ""){
        saveData.startTime = paramJson.startTime;
    }
    if(paramJson.endTime.trim() != "" ){
        saveData.endTime = paramJson.endTime;
    }
    delete saveData.projectDetail;
    return saveData;
}

function initSelectQuery() {
    $.ajax({
        url:toolkitPath+'/raising/backstage/project/initSelect.do',
        data:{'sectionCode':'sectionType',"tbmCode":"tbmtype"},
        type:"get",
        dataType:"json",
        success:function (data) {
            if(data.status == "0"){
                var sectionTypesList =  data.sectionTypesList;
                var linesList =  data.linesList;
                var sectionOwnersList =  data.sectionOwnersList;
                var tbmTypeList =  data.tbmTypeList;
                var factorysList =  data.factorysList;
                var tbmOwnersList =  data.tbmOwnersList;
                var rmsVersionsList =  data.rmsVersionsList;
                var supervisorList =  data.supervisorList;
                var buildUnitList =  data.buildUnitList;
                for (var i = 0; i < sectionTypesList.length; i++) {
                    $("#inqu_status-ccsSectionType").append("<option value='"+sectionTypesList[i].ccsId+"'>"+sectionTypesList[i].ccsStr+"</option>");
                }
                for (var i = 0; i < linesList.length; i++) {
                    $("#inqu_status-sectionCcsId").append("<option value='"+linesList[i].ccsId+"'>"+linesList[i].line+"</option>");
                }
                for (var i = 0; i < sectionOwnersList.length; i++) {
                    $("#inqu_status-sectionOwner").append("<option value='"+sectionOwnersList[i]+"'>"+sectionOwnersList[i]+"</option>");
                }
                for (var i = 0; i < tbmTypeList.length; i++) {
                    $("#inqu_status-tbmCcsType").append("<option value='"+tbmTypeList[i].ccsId+"'>"+tbmTypeList[i].ccsStr+"</option>");
                }
                for (var i = 0; i < factorysList.length; i++) {
                    $("#inqu_status-factory").append("<option value='"+factorysList[i]+"'>"+factorysList[i]+"</option>");
                }
                for (var i = 0; i < tbmOwnersList.length; i++) {
                    $("#inqu_status-tbmOwner").append("<option value='"+tbmOwnersList[i]+"'>"+tbmOwnersList[i]+"</option>");
                }
                for (var i = 0; i < rmsVersionsList.length; i++) {
                    $("#inqu_status-rmsVersion").append("<option value='"+rmsVersionsList[i]+"'>"+rmsVersionsList[i]+"</option>");
                }

                for (var i = 0; i < supervisorList.length; i++) {
                    $("#inqu_status-supervisor").append("<option value='"+supervisorList[i]+"'>"+supervisorList[i]+"</option>");
                }
                for (var i = 0; i < buildUnitList.length; i++) {
                    $("#inqu_status-buildUnit").append("<option value='"+buildUnitList[i]+"'>"+buildUnitList[i]+"</option>");
                }

            }
        }
    })
}

function initTime(){
    $("#startTime").daterangepicker(getDateRangPickerOption(), function(start, end, label) {
        var s  = start.format('YYYY-MM-DD HH:mm:ss');
        $("#startTime").val(s);
    });
    $("#endTime").daterangepicker(getDateRangPickerOption(), function(start, end, label) {
        var s  = start.format('YYYY-MM-DD HH:mm:ss');
        $("#endTime").val(s);
    });
}

function clearSelect(){
    $('#inqu_status-projectName').val("");
    $('#inqu_status-collectorName').val("");
    $('#inqu_status-sectionName').val("");
    $('#inqu_status-tbmName').val("");
    $("#inqu_status-sectionCcsId").val("");
    $("#inqu_status-sectionOwner").val("");
    $("#inqu_status-ccsSectionType").val("");
    $("#inqu_status-supervisor").val("");
    $("#inqu_status-buildUnit").val("");
    $("#inqu_status-factory").val("");
    $("#inqu_status-tbmOwner").val("");
    $("#inqu_status-rmsVersion").val("");
    $("#inqu_status-tbmCcsType").val("");

}

function getSection() {
    var returnStr = "";
    $.ajax({
        async: false,
        type: "get",
        url : toolkitPath+'/raising/backstage/project/getSections.do',
        dataType:"json",
        success: function (data) {
            for (i = 0; i < data.length; i++) {
                if (i < data.length - 1) {
                    returnStr += data[i].sectionId + ":" + data[i].sectionName + ";";
                } else {
                    returnStr += data[i].sectionId + ":" + data[i].sectionName;
                }
            }
        }
    });
    return returnStr;
}

function initProjectDetailSelect(){
    $("#status").combobox({
        panelHeight:'auto',
        panelMaxHeight:200,
        panelMinHeight:100,
        width:178,
        method:"get",
        url : toolkitPath+'/raising/backstage/project/getProjectStatus.do?ajaxParam=prostatus',
        valueField : 'statusCode',
        textField : 'statusStr',
        editable:false,
        hasDownArrow:true,
        onLoadSuccess: function () { //数据加载完毕以后，默认选中第
            $("#status").next().css("border","1px solid #d5d5d5");
            $("#status").next().css("margin-left","10px");

        }
    });

    $("#tbmId").combobox({
        panelHeight:'auto',
        panelMaxHeight:200,
        panelMinHeight:100,
        method:'get',
        url : toolkitPath+'/raising/backstage/project/getTbmInfo.do',
        valueField : 'tbmId',
        textField : 'tbmName',
        width:178,
        editable:true,
        hasDownArrow:true,
        filter: function(q, row){
            var opts = $(this).combobox('options');
            return row[opts.textField].indexOf(q) == 0;
        },
        onLoadSuccess: function () { //数据加载完毕以后，默认选中第
            $(".textbox.combo").css("border","1px solid #d5d5d5");
            $(".textbox.combo").css("margin-left","10px");
        }
    });

    $("#templateName").combobox({
        panelHeight:'auto',
        panelMaxHeight:200,
        panelMinHeight:100,
        width:178,
        method:"get",
        url : toolkitPath+'/raising/backstage/project/getProjectStatus.do?ajaxParam=template',
        valueField : 'statusCode',
        textField : 'statusStr',
        editable:false,
        hasDownArrow:true,
        onLoadSuccess: function () { //数据加载完毕以后，默认选中第
            $("#status").next().css("border","1px solid #d5d5d5");
            $("#status").next().css("margin-left","10px");

        }
    });

    $('#dayShiftStart').timepicker({
        minuteStep : 1,
        showSeconds : false,
        showMeridian : false
    }).next().on(ace.click_event, function() {
        $(this).prev().focus();
    });

    $('#dayShiftEnd').timepicker({
        minuteStep : 1,
        showSeconds : false,
        showMeridian : false
    }).next().on(ace.click_event, function() {
        $(this).prev().focus();
    });
}

function projectNameKeyUp(obj){
    var a = obj.value;
    if(a.length > 0){//如果其中有值，则设另外三个输入框只读
        $('#inqu_status-collectorName').attr("readonly","readonly");
        $('#inqu_status-sectionName').attr("readonly","readonly");
        $('#inqu_status-tbmName').attr("readonly","readonly");
    }else{
        $("#inqu_status-collectorName").removeAttr("readonly");
        $("#inqu_status-sectionName").removeAttr("readonly");
        $("#inqu_status-tbmName").removeAttr("readonly");
    }

}

function collectorNameKeyUp(obj){
    var a = obj.value;
    if(a.length > 0){//如果其中有值，则设另外三个输入框只读
        $('#inqu_status-projectName').attr("readonly","readonly");
        $('#inqu_status-sectionName').attr("readonly","readonly");
        $('#inqu_status-tbmName').attr("readonly","readonly");
    }else{
        $("#inqu_status-sectionName").removeAttr("readonly");
        $("#inqu_status-projectName").removeAttr("readonly");
        $("#inqu_status-tbmName").removeAttr("readonly");
    }
}

function sectionNameKeyUp(obj){
    var a = obj.value;
    if(a.length > 0){//如果其中有值，则设另外三个输入框只读
        $('#inqu_status-projectName').attr("readonly","readonly");
        $('#inqu_status-collectorName').attr("readonly","readonly");
        $('#inqu_status-tbmName').attr("readonly","readonly");
    }else{
        $("#inqu_status-collectorName").removeAttr("readonly");
        $("#inqu_status-projectName").removeAttr("readonly");
        $("#inqu_status-tbmName").removeAttr("readonly");
    }

}

function tbmNameKeyUp(obj){
    var a = obj.value;
    if(a.length > 0){//如果其中有值，则设另外三个输入框只读
        $('#inqu_status-collectorName').attr("readonly","readonly");
        $('#inqu_status-sectionName').attr("readonly","readonly");
        $('#inqu_status-projectName').attr("readonly","readonly");
    }else{
        $("#inqu_status-collectorName").removeAttr("readonly");
        $("#inqu_status-sectionName").removeAttr("readonly");
        $("#inqu_status-projectName").removeAttr("readonly");
    }

}



