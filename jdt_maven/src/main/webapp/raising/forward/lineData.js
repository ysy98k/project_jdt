

// 蓝图上传按钮
function  bulueprintButton() {
    //初始化
    var button = [{
        text: "完成",
        "class" : "btn btn-skinColor btn-xs",
        click : function () {
            blueprintUpload();
        }
    }]
    var title = "蓝图上传";
    var dialogOpt = {
        title: title,
        buttons:button,
        width: '400px'
    };
    $("#blueprintDiv").bxdialog(dialogOpt);
}
//蓝图上传
function  blueprintUpload() {
    var info = $("#info").val();
    if(info == ""){
        alertDiv("提示","请填写正确的数据格式！");
        return;
    }
    $("#projectName").val($.cookie("selected_name"));
    var option = {
        url: toolkitPath+'/raising/forward/lineData/blueprintUpload.do', //这里写你的url
        type : 'POST',
        datatype:'json',//这里是返回类型，一般是json,text
        clearForm: true,//提交后是否清空
        success : function(data) {
            if(data.status == "0"){
                alertDiv("提示",data.message);
                initTable();
                $("#blueprintDiv").bxdialog("close");
            }else{
                alertDiv("提示",data.message);
            }
        } ,
        error:function(data){
            alertDiv("提示","页面请求失败");
        }
    };
    $("#blueprintForm").ajaxSubmit(option);
    return false;

}
// 线型上传按钮
function  lineButton() {
    //初始化蓝图下拉框
    $("#drawing").combobox({
        panelHeight:'auto',
        panelMaxHeight:200,
        panelMinHeight:100,
        method:'get',
        url : toolkitPath+'/raising/forward/lineData/getDrawings.do?projectId='+$.cookie("selected_id"),
        valueField : 'drawingId',
        textField : 'drawingName',
        width:150,
        editable:false,
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

    //初始化
    var button = [{
        id:"lineConfirmButton",
        text: "完成",
        "class" : "btn btn-skinColor btn-xs",
        click : function () {
            lineUpload();
        }
    }]
    var title = "线型上传";
    var dialogOpt = {
        title: title,
        buttons:button,
        width: '600px'
    };
    $("#lineDiv").bxdialog(dialogOpt);
}

//线型上传
function  lineUpload() {
    var personName = $("#personName").val();
    var telephone = $("#telephone").val();
    var drawingId = $("#drawing").combobox("getValue");
    var company = $("#personCompany").val();
    var personTitle = $("#personTitle").val();
    var file = document.getElementById("lineFile").value;
    var suffix = GetExtensionFileName(file);
    if(personName == "" || personName == "" ||  drawingId == "" || company == "" || personTitle == "" ){
        alertDiv("提示","红星标记数据项不可以为空！");
        return;
    }
    if(telephone.length != 11){
        alertDiv("提示","手机号长度不正确，请输入11位数字！");
        return;
    }
    if(telephone.length != 11){
        alertDiv("提示","手机号长度不正确，请输入11位数字！");
        return;
    }
    if(suffix != "xls" && suffix != "xlsx"){
        alertDiv("提示","文件格式不正确，只能上传xls或xlsx数据");
        return;
    }

    var option = {
        url: toolkitPath+'/raising/forward/lineData/lineUpload.do', //这里写你的url
        type : 'POST',
        datatype:'json',//这里是返回类型，一般是json,text
        data:{drawingId:drawingId,projectId:$.cookie("selected_id")},
        beforeSubmit:function (a,f,o) {
            $("#lineConfirmButton").attr("disabled", true);
        },
        /*clearForm: true,//提交后是否清空*/
        success : function(data) {
            $("#lineConfirmButton").attr("disabled", false);
            if(data.status == "0"){
                $("#lineForm")[0].reset();
                alertDiv("提示",data.message);
                initTable();
                $("#lineDiv").bxdialog("close");
            }else{
                alertDiv("提示",data.message);

            }
        } ,
        error:function(data){
            $("#lineConfirmButton").attr("disabled", false);
            alertDiv("提示","页面请求失败");
        }
    };
    $("#lineForm").ajaxSubmit(option);
    return false;

}
//下载按钮
function download(fileName,filePath) {
    if(filePath == "" || fileName ==""){
        alertDiv("提示","请选择下载文件！");
        return;
    }
    filePath= filePath.replace(/\//g,'\\');
    //下载
    var callback = {
        onSuccess: function () {
            var a  = 1;
        }
    }
    var param = {"filePath":filePath,"fileName":fileName};
    var paramStr = JSON.stringify(param);

    window.location.href =toolkitPath +'/raising/forward/lineData/downloadFile.do?filePath='+encodeURIComponent(encodeURIComponent(filePath))+"&fileName="+encodeURIComponent(encodeURIComponent(fileName));


}

function downloadLineInfo(lineId){
    var regPos = /^[0-9]+$/ ;
    if(!regPos.test(lineId)){
        alertDiv("提示","请选择文件！");
        return;
    }
    window.location.href =toolkitPath +'/raising/forward/lineData/downloadLineInfo.do?lineId='+lineId;

}


$(document).ready(function () {
    var projectId = $.cookie('selected_id');
    baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxcombobox'], function () {
        //分页显示
        var imageGridOption = {
            primaryRowKey: "projectId",
            caption: false,
            colNames: ['x', 'y', 'z', '里程'],
            colModel: [
                {
                    name: 'x',
                    index: 'x',
                    align:'center',
                    width: 60
                }, {
                    name: 'y',
                    index: 'y',
                    align:'center',
                    width: 60,
                }, {
                    name: 'z',
                    index: 'z',
                    align:'center',
                    width: 60
                }, {
                    name: 'designmileage',
                    index: 'designmileage',
                    align:'center',
                    width: 60
                }
            ],
            sortorder: 'asc',
            jsonReader: {
                id: "projectId",
                repeatitems: false
            },
            height:'325',
            caption: "线型信息"
        };

        var imageOption = {
            queryParam: {"inqu_status": {"cookieId": projectId}},
            gridOption: imageGridOption,
            dataPattern: "url",
            url: "/raising/forward/designLine.do?method=query",
            showMsgOpt: {
                showMsgId: "alertdiv"
            },
            navGridOption: {
                download: true,
                downloadParam: {
                    downloadUrl:"/raising/forward/designLine.do/downloadExcel.do"
                }
            }
        };
        $("#gridImage").bxgrid(imageOption);
        var h = $('#contentFrame', parent.document).height();
        $("#page-content").height(h-40)
    });
    initTable();

});

//出事蓝图和线性数据表格
function initTable(){
    //获得蓝图和线性数据
    $.ajax({
        url:toolkitPath+ '/raising/forward/lineData/getTableData.do',
        type:"post",
        data:{"projectId":$.cookie("selected_id")},
        dataType:"json",
        success:function (data) {
            if(data.status == "0"){
                var drawingArray = data.drawingList;
                $("#drawingTableBody").html("");
                $("#lineBody").html("");
                for(var i = 0;i<drawingArray.length;i++){
                    var temp = drawingArray[i];
                    filePath = temp.filePath.replace(/\\/g,'/');
                    $("#drawingTableBody").append(
                        "<tr>" +
                        "<td>"+temp.drawingName+"</td>"+
                        "<td>"+temp.info+"</td>"+
                        "<td>"+temp.createTime+"</td>"+
                        "<td><button class='btn btn-sm btn-primary' onclick='download("+"\""+temp.drawingName+"\",\""+filePath+"\""+")'>下载</button></td>"+
                        "</tr>" )
                }
                var lineArray = data.LineList;
                for(var i = 0;i<lineArray.length;i++){
                    var temp = lineArray[i];
                    $("#lineBody").append(
                        "<tr data-id='"+temp.lineId+"' data-drawingId='"+temp.drawingId+"'>\n" +
                        "<td><input type='checkbox' name='lineCheckbox' data-review='"+temp.review+"' data-company='"+temp.personCompany+"' data-title='"+temp.personTitle+"' value='"+temp.lineId+"' /></td>\n" +
                        "<td>"+temp.personName+"</td>\n" +
                        "<td>"+temp.personCompany+"</td>\n" +
                        "<td>"+temp.personTitle+"</td>\n" +
                        "<td>"+temp.telephone+"</td>\n" +
                        "<td>"+temp.drawingName+"</td>\n" +
                        "<td>"+temp.createTime+"</td>\n" +
                        "<td class=\"\"><button class=\"btn btn-sm btn-primary\" onclick='downloadLineInfo("+temp.lineId+")'>下载</button></td>\n" +
                        "<td>"+temp.review+"</td>\n" +
                        "</tr>"
                    )
                }
            }
        },
        error:function () {}

    })

}

function deletedata(code){
    var param = {};
    var arrays = new Array();
    if(code == 1){
        $("input[type='checkbox'][name='drawingCheckbox']:checked").each(function(){
            var temp = $(this).val();
            if(temp != ""){
                arrays.push(temp);
            }
        })
        param = {drawingIds:arrays};
    } else {
        $("input[type='checkbox'][name='lineCheckbox']:checked").each(function(){
            var temp = $(this).val();
            if(temp != ""){
                arrays.push(temp);
            }
        })
        param = {lineIds:arrays};
    }

    if(arrays.length < 1){
        alertDiv("提示","至少选择一条记录");
        return;
    }

    //获得蓝图和线性数据
    var callback = {
        onSuccess: function (data) {
            if(data.status == "0"){
                alertDiv("提示",data.message);
                initTable();
            }else{
                alertDiv("提示",data.message);
            }
        }
    }

    AjaxCommunicator.ajaxRequest('/raising/forward/lineData/delete.do', 'POST', param, callback,true);
}

var num = 0;
var num2 = 0;
function showCheckPage(){
    var arrays = new Array();
    var gridHeadArray = new Array();
    $("input[type='checkbox'][name='lineCheckbox']:checked").each(function(){
        var temp = $(this).val();
        var gridHead = {};

        gridHead.company = $(this).attr("data-company");
        gridHead.title = $(this).attr("data-title");
        gridHead.review = $(this).attr("data-review");
        if(temp != ""){
            arrays.push(temp);
            gridHeadArray.push(gridHead);
        }
    })
    if(arrays.length != 1 ){
        alertDiv("提示","只可对比一项线型详情记录！");
        return;
    }
    var title="线型对比结果"
    var dialogOpt = {
        title: title,
        width: '1000px'
    };
    $("#checkPage").bxdialog(dialogOpt);
    showGroupColGrid(arrays,gridHeadArray);

}

function showGroupColGrid(arrays,gridHeadArray){
    initCompare();
    var check = $("#id-button-borders").prop("checked");
    var errorSelect = $("#errorSelect").val();
    var review = gridHeadArray[0].review;
    $("#reviewSelect").val(review);
    var queryParam = {};
    queryParam.cookieId = $.cookie('selected_id');
    queryParam.lineArray = arrays;
    queryParam.display = check;
    queryParam.errorSelect = errorSelect;

    var colNames = ['里程', 'X', 'Y', 'Z','里程','X','Y','Z','DM','DX','DY','DZ'];
    var colModel = [
        {
            name: 'mileage',
            index: 'mileage',
            width: 80,
            formatter: function (value, grid, row, state) {
                var red = false;
                red = row.dx1 > errorSelect ? true : false;
                red = row.dy1 > errorSelect || red == true ? true : false;
                red = row.dz1 > errorSelect || red == true ? true : false;
                red = row.dx2 > errorSelect || red == true ? true : false;
                red = row.dy2 > errorSelect || red == true ? true : false;
                red = row.dz2 > errorSelect || red == true ? true : false;
                return red == true ? "<span style='color: red'>"+value+"</span>" :value;
            }
        }, {
            name: 'x',
            index: 'x',
            width: 80
        }, {
            name: 'y',
            index: 'y',
            width: 80
        }, {
            name: 'z',
            index: 'z',
            width: 80
        }, {
            name: 'designMileage',
            index: 'designMileage',
            width: 80
        },{
            name: 'designX',
            index: 'designX',
            width: 80
        },
        {
            name: 'designY',
            index: 'designY',
            width: 80
        }, {
            name: 'designZ',
            index: 'designZ',
            width: 80
        },
        {
            name: 'dmileage',
            index: 'dmileage',
            width:40,
            formatter: function (value, grid, row, state) {
                if(value > 0){
                    value = "<span style='color: red'>"+value+"</span>";
                }
                return value;
            }

        },
        {
            name: 'dx',
            index: 'dx',
            width:40,
            formatter: function (value, grid, row, state) {
                if(value > errorSelect){
                    value = "<span style='color: red'>"+value+"</span>";
                }
                return value;
            }

        }, {
            name: 'dy',
            index: 'dy',
            width: 40,
            formatter: function (value, grid, row, state) {
                if(value > errorSelect){
                    value = "<span style='color: red'>"+value+"</span>";
                }
                return value;
            }
        }, {
            name: 'dz',
            index: 'dz',
            width: 40,
            formatter: function (value, grid, row, state) {
                if(value > errorSelect){
                    value = "<span style='color: red'>"+value+"</span>";
                }
                return value;
            }
        }]
    var groupHeader = [
        {
            startColumnName: 'mileage',
            numberOfColumns: 4,
            titleText: '1、'+gridHeadArray[0].company+"— —"+gridHeadArray[0].title
        },
        {
            startColumnName: 'designMileage',
            numberOfColumns: 4,
            titleText: '2、力信导向系统'
        },
        {
            startColumnName: 'dmileage',
            numberOfColumns: 4,
            titleText: '1、2互算'
        }
    ]
    var gridOption = {
        colNames: colNames,
        colModel: colModel,
        caption: "线型对比表",
        multiselect: false
    };
    var groupColGrid = {
        queryParam: queryParam,
        gridOption: gridOption,
        dataPattern: "url",
        url: "/raising/forward/lineData/compare.do",
        showMsgOpt: {
            showMsgId: "infodiv"
        },
        navGridOption: {
            download: false
        }
    };
    $("#groupColGrid").bxgrid(groupColGrid);
    if(num == 0) {
        $("#groupColGrid").bxgrid("rawMethodCallMore", 'setGroupHeaders', { //显示报表头部信息 中铁二局--杨勇
            useColSpanStyle: true,
            groupHeaders: groupHeader
        });
        num++;
    }
    $("#id-button-borders").click(function () {
        queryParam.display = $("#id-button-borders").prop("checked");
        on_query_click(queryParam)
    })
    $("#errorSelect").change(function(){
        if(queryParam.display == false){
            return;
        }
        queryParam.errorSelect = $("#errorSelect").val();
        on_query_click(queryParam);
    })

}

function on_query_click(queryParam) {
    $("#groupColGrid").bxgrid("option", "queryParam", queryParam);
    $("#groupColGrid").bxgrid("query");
}

function reviewChange(){
    var lineId = null;
    $("input[type='checkbox'][name='lineCheckbox']:checked").each(function(){
        lineId = $(this).val();
    })
    var val = $("#reviewSelect").val();
    var t  = {lineId:lineId,review:val};
    $.ajax({
        url:toolkitPath + "/raising/forward/lineData/review.do",
        type:"post",
        data:{"ajaxParam":JSON.stringify(t)},
        success:function(data){
            if(data == "0"){
                $("input[type='checkbox'][name='lineCheckbox']:checked").each(function(){
                    $(this).attr("data-review",val);
                })
                var t = $("input[type='checkbox'][name='lineCheckbox']:checked").parent().parent().children("td:last").html();
                $("input[type='checkbox'][name='lineCheckbox']:checked").parent().parent().children("td:last").html(val);
            }
        },
        error:function () {

        }
    })
}

function initCompare(){

    var paramJson = {"projectId":$.cookie("selected_id")};
    var paramStr = JSON.stringify(paramJson);
    $.ajax({
        url:toolkitPath +"/raising/forward/projectManage//getProject.do",
        type:"get",
        data:{"ajaxParam":paramStr},
        success:function(data){
            if(data.status == "0"){
                var spanStr = "当前项目有效里程为["+data.startMileage+","+data.endMileage+"],有效里程外的比较结果不应纳入审核结果考虑范围";
                $("#prompt").html(spanStr);
            }
        }
    })
}

function downloadCompare(){
    var lineId = null;
    $("input[type='checkbox'][name='lineCheckbox']:checked").each(function(){
        lineId = $(this).val();
    })
    var paramJson = {};
    paramJson.lineId = lineId;
    paramJson.downloadColumnDesc = "里程,X,Y,Z,里程,X,Y,Z,DM,DX,DY,DZ";
    paramJson.downloadColumn = "mileage,x,y,z,designMileage,designX,designY,designZ,dmileage,dx,dy,dz";
    paramJson.columnImageMap = {};
    paramJson.columnSelectMap = {};
    var str = JSON.stringify(paramJson);
    window.location.href=toolkitPath+ "/raising/forward/lineData/downloadCompareResult.do?ajaxParam="+encodeURIComponent(encodeURIComponent(str));
}