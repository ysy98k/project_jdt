<%@ page import="com.raising.forward.service.PropertiesValue" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="java.util.Set" %>
<%@ page import="com.alibaba.fastjson.JSONArray" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/1/14
  Time: 17:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>数据上传测试页面</title>
    <%
        List<String> serverTables = new ArrayList<String>();//存放服务器表信息
        String tableHeaderStr = PropertiesValue.CLIENT_TABLE_HEADER;
        JSONObject tableHeaderJson =  JSONObject.parseObject(tableHeaderStr);

        Set<String> tableHeaderSet = tableHeaderJson.keySet();
        for(String tableHeaderTemp : tableHeaderSet){
            //判断表名是否在表集合中
            JSONArray clientTableArray = tableHeaderJson.getJSONArray(tableHeaderTemp);
            for(int i=0;i<clientTableArray.size();i++){
                String clientTableName = clientTableArray.getString(i);
                String serverTableName = tableHeaderTemp + "_"+clientTableName;//服务器的表明 = header_clientTable d_RingData
                serverTables.add(serverTableName);
            }
        }
    %>
    <style>
        .conditionTitle{
            text-align: right;
            line-height: 30px;
           /* //padding-top: 15px;*/
        }
        .column{
            min-width: 100px;
        }
        #headerClear{
            clear:both;
        }
        #alertdiv{
            padding: 8px 15px;
        }
        .loulou{
            margin-bottom: 10px;
        }
        select {
            width: 70px;
            height: 37px;
            text-align: center;
            text-align-last: center;
        }
        .conditionSelect{
            min-width: 180px;
        }

    </style>
</head>
<body>
<%@ include file="/bxui/bxuihead.jsp" %>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxdialog.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/selectLine.js"></script>


<div class="page-content" style="position:inherit;min-height:600px">
    <div class="page-header">
        <div style="float: left;min-width: 150px; margin-top: 20px">
            <div>
                <label class="labelThree"
                       for="inqu_status-table">表名</label>
                <select id="inqu_status-table" data-bxwidget class="inputThree" style="min-width: 150px;">
                </select>
            </div>
        </div>
        <div style="float: right;margin-top: 20px">
            <a href="javascript:insertPage()" ><span id = "cityname"></span><span id="linemessage"></span><span id="programmessage"></span></a>
        </div>
        <div id="headerClear"></div>
    </div><!-- /.page-header -->
    <div class="row">
        <div class="col-xs-12 searchArea" id="queryarea">

            <div id="alertdiv" class="alertdiv"></div>


            <div class="row loulou">
                <div class="col-sm-2 col-xs-12 conditionTitle">
                    查询条件1
                </div>
                <div class="col-sm-2 col-xs-12">
                    <label class="labelThree"
                           for="inqu_status-condition1">字段名1</label>
                    <select id="inqu_status-condition1" class="conditionSelect"  data-bxwidget class="inputThree column">
                    </select>
                </div>
                <div class="col-sm-2 col-xs-12">
                    <label class="labelThree"
                           for="inqu_status-condition1Releation">关系1</label>
                    <select id="inqu_status-condition1Releation" data-bxwidget class="inputThree">
                        <option value="1">&gt;</option>
                        <option value="2">=</option>
                        <option value="3">&lt;</option>
                        <option value="4">betwenn</option>
                    </select>
                </div>
                <div class="col-sm-2 col-xs-12">
                    <label class="labelThree"
                           for="inqu_status-condition1Value1">条件1</label>
                    <input type="text" id="inqu_status-condition1Value1" data-bxwidget class="inputThree"/>

                </div>
                <div class="col-sm-2 col-xs-12">
                    <label class="labelThree"
                           for="inqu_status-condition1Value2">条件1</label>
                    <input type="text" id="inqu_status-condition1Value2" data-bxwidget class="inputThree"/>

                </div>
            </div>
            <div class="row loulou">
                <div class="col-sm-2 col-xs-12 conditionTitle">
                    查询条件2
                </div>
                <div class="col-sm-2 col-xs-12">
                    <label class="labelThree"
                           for="inqu_status-condition2">字段名2</label>
                    <select id="inqu_status-condition2" class="conditionSelect" data-bxwidget class="inputThree column">
                    </select>
                </div>
                <div class="col-sm-2 col-xs-12">
                    <label class="labelThree"
                           for="inqu_status-condition2Releation">关系2</label>
                    <select id="inqu_status-condition2Releation" data-bxwidget class="inputThree">
                        <option value="1">&gt;</option>
                        <option value="2">=</option>
                        <option value="3">&lt;</option>
                        <option value="4">betwenn</option>
                    </select>
                </div>
                <div class="col-sm-2 col-xs-12">
                    <label class="labelThree"
                           for="inqu_status-condition2Value1">条件2</label>
                    <input type="text" id="inqu_status-condition2Value1" data-bxwidget class="inputThree"/>
                </div>
                <div class="col-sm-2 col-xs-12">
                    <label class="labelThree"
                           for="inqu_status-condition2Value2">条件2</label>
                    <input type="text" id="inqu_status-condition2Value2" data-bxwidget class="inputThree"/>

                </div>
            </div>

            <div class="row loulou">
                <div class="col-sm-6 col-md-6 col-xs-12 no-padding">
                    <div class="col-md-3"></div>
                    <div class="col-sm-3 col-md-2 col-xs-6 no-padding-left-1024">
                        <button class="btn btn-sm btn-block" onclick="on_query_click();">
                            <div class="ace-icon fa fa-search"></div>
                            <span>查询</span>
                        </button>
                    </div>
                    <div class="col-sm-3 col-md-2 col-xs-6 no-padding-left-1024">
                        <button class="btn btn-sm btn-block" onclick="clearCondition();">
                            <div class="ace-icon fa fa-search"></div>
                            <span>清空查询条件</span>
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-xs-12" id="jBaBa">
            <div id="jqGrid"></div>
        </div>
        <!-- /.col -->
    </div>
</div>



<div id="detail" class="hide" style="overflow: hidden">
    <div type="text" id = "div_city" name = "city" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
    <div type="text" id = "div_line" name = "line" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
    <div type="text" id = "div_program" name = "program" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
</div>

<script>
    function getTablesArray(){
        var tablesStr = "<%=serverTables%>";
        tablesStr = tablesStr.substring(tablesStr.indexOf("[")+1);
        tablesStr = tablesStr.substring(0,tablesStr.length-1);
        var tablesArr = tablesStr.split(",");
        return tablesArr;
    }

    var columns = null;

    $(document).ready(function(){
        var tablesArr = getTablesArray();
        for (var i = 0; i < tablesArr.length; i++) {
            var temp = tablesArr[i].trim();
            $("#inqu_status-table").append("<option value='"+temp+"'>"+temp+"</option>");
        }
        var tableName = $("#inqu_status-table").val();
        initGrid(tableName,$("#jqGrid"));

        $("#inqu_status-table").change(function () {
            var table = $(this).val();
            $("#jBaBa").html("");
            var gridDiv =$('<div></div>');
            gridDiv.attr("id","jqGrid");
            $("#jBaBa").append(gridDiv);
            initGrid(table,gridDiv);
        })
    })

    function initGrid(tableName,gridDiv){//每次切换tableName时。都应该初始化table
        var param = {"tableName":tableName};
        var timeColumn = null;
        $.ajax({
            url:toolkitPath+"/rest/api/raising/upload/test/getColumns",
            type:"post",
            data:JSON.stringify(param),
            contentType:"application/json",
            dataType:"json",
            async:false,
            success:function(data){
                if(data.status == '0'){
                    columns = data.columns;
                    timeColumn= data.timeColumn;
                }
            }
        })
        initCondition();

        baosightRequire.requireFunct(['bxgrid', 'bxdiv', 'bxdialog', 'bxalert', 'bxcombobox'],
            function () {
                var colMode = getColModel(columns);
                $("#queryarea").bxdiv();
                var gridOption = {
                    primaryRowKey: "projectId",
                    caption: false,
                    colNames: columns,
                    height: 370,
                    colModel:colMode,
                    sortorder: 'asc',
                    jsonReader: {
                        id: "projectId",
                        repeatitems: false
                    }
                };

                var option = {
                    queryParam: {
                        "tableName":tableName,
                        "columnNames":columns,
                        "timeColumnName":timeColumn,
                        "projectId":$.cookie("selected_id")
                    },
                    dataPattern: "url",
                    url: "/rest/api/raising/upload/test/getRows",
                    showMsgOpt: {
                        showMsgId: "alertdiv"
                    },
                    gridOption: gridOption
                };
                gridDiv.bxgrid(option);
            });

    }

    function getColModel(colNames){
        var colModel = [];
        for(var i =0;i<colNames.length;i++){
            var temp = {};
            temp.name = colNames[i];
            temp.index = colNames[i];
            temp.width = 40;
            temp.readOnly = true;
            temp.editable = false;
            colModel.push(temp);
        }
        return colModel;
    }

    function initCondition(){
        var columns  = this.columns;
        $("#inqu_status-condition1").html("");
        $("#inqu_status-condition2").html("");
        for (var i = 0; i < columns.length; i++) {
            $("#inqu_status-condition1").append("<option value='"+columns[i]+"'>"+columns[i]+"</option>");
            $("#inqu_status-condition2").append("<option value='"+columns[i]+"'>"+columns[i]+"</option>");
        }
    }
    /**
     * 查询按钮点击事件
     */
    function on_query_click() {
        var queryParam = new Object();
        $("#queryarea").bxdiv('setQueryFromDiv', queryParam, "inqu_status");
        var inquParam = queryParam.inqu_status;
        queryParam = JSON.parse(JSON.stringify(inquParam));
        queryParam.tableName = $("#inqu_status-table").val();
        queryParam.columnNames = columns;
        queryParam.projectId = $.cookie("selected_id");
        if(isNullOrEmptyOrUndefiend(queryParam.condition1Value1)){
            delete queryParam.condition1;
            delete queryParam.condition1Releation;
            delete queryParam.condition1Value1;
            delete queryParam.condition1Value2;
        }
        if(isNullOrEmptyOrUndefiend(queryParam.condition2Value1)){
            delete queryParam.condition2;
            delete queryParam.condition2Releation;
            delete queryParam.condition2Value1;
            delete queryParam.condition2Value2;
        }
        $("#jqGrid").bxgrid("option", "queryParam", queryParam);
        $("#jqGrid").bxgrid("query");
    }

    /**
     * 清空查询条件
     */
    function clearCondition(){
        $("#inqu_status-condition1Value1").val("");
        $("#inqu_status-condition1Value2").val("");
        $("#inqu_status-condition2Value1").val("");
        $("#inqu_status-condition2Value2").val("");
    }
</script>

<script>
    var codename;
    var detailData;
    $(document).ready(function () {
        //做右上角默认线路显示通用css。暂时只应用surveyInfo.jsp。
        $("#select_line_div").css("float","right");
        $("#select_line_div").css("margin-top","-20px");

        baosightRequire.requireFunct(['bxcombobox'], function() {
            var selectLineCallBack={
                onSuccess: function (paramJsonObj) {
                    initDefaultProject(paramJsonObj);
                    codename = paramJsonObj.codeName;
                    detailData = paramJsonObj.detailsArray;

                    if ($.cookie('selected_id')!=null && $.cookie('selected_id') != undefined) {
                        var projectId = $.cookie('selected_id');
                        var programname;
                        var ccsId;
                        var linename;
                        var cityname;
                        for(var i = 0; i < detailData.length; i++ ){
                            if(projectId == (detailData[i].projectId+"")) {
                                programname = detailData[i].projectName;
                                var ccsId = detailData[i].ccsId;
                            }
                        }
                        var ccs_id = ccsId.split(".");
                        var ccsStr = ccs_id[0]+"."+ccs_id[1];

                        for(var i =0 ; i <codename[ccsStr].length; i++) {
                            if(ccsId == codename[ccsStr][i].value){
                                linename = codename[ccsStr][i].label;
                            }
                        }
                        for(i = 0; i< codename.cityArray.length;i++){
                            if(ccsStr == codename.cityArray[i].value){
                                cityname = codename.cityArray[i].label
                            }
                        }
                        $("#cityname").html("["+cityname+"]");
                        $("#linemessage").html("["+linename+"]");
                        $("#programmessage").html("["+programname+"]");
                    }else {
                        $("#cityname").html("[请选择城市]");
                        $("#linemessage").html("[请选择线路]");
                        $("#programmessage").html("[请选择项目]");
                    }
                }
            };
            AjaxCommunicator.ajaxRequest('/raising/forward/project/getProject.do', 'POST', {}, selectLineCallBack,true);
        })
    })

    function initDefaultProject(paramJsonObj) {
        baosightRequire.requireFunct(['bxcombobox'], function() {
            if (paramJsonObj.detailsArray == null) {//如果其下该用户下没有任何资源
                $("#cityname").html("[请选择城市]");
                $("#linemessage").html("[请选择线路]");
                $("#programmessage").html("[请选择项目]");

                $("#div_city").bxcombobox({
                    dataPattern: 'local',
                    data: [],
                    async: false
                });
                $("#div_city_select ").prepend("<option value='' selected = selected>--选择城市--</option>");

                $("#div_line").bxcombobox({
                    dataPattern: 'local',
                    data: [],
                    async: false
                });
                $("#div_line_select").prepend("<option value='' selected = selected>--选择线路--</option>");

                $("#div_program").bxcombobox({
                    dataPattern: 'local',
                    data: [],
                    async: false
                });
                $("#div_program_select").prepend("<option value='' selected = selected>--选择项目--</option>");
                return;
            }
            codename = paramJsonObj.codeName;
            detailData = paramJsonObj.detailsArray;
            var cityData = codename.cityArray;
            var lineName = [];
            var programArray = [];
            var projectId = $.cookie("selected_id");

            var ccsId = null;
            for (var i = 0; i < detailData.length; i++) {
                if (projectId == detailData[i].projectId) {
                    ccsId = detailData[i].ccsId;
                }
            }
            var cityCCSId = ccsId.substring(0, ccsId.lastIndexOf("."));//设置默认的城市名
            var default_lineNames = codename[cityCCSId];//设置默认的线路名。
            var default_projectNames = [];
            var projectName = $.cookie("selected_name");//默认的项目名
            for (var i = 0; i < detailData.length; i++) {
                if (ccsId == detailData[i].ccsId) {
                    var program = {};
                    if (projectName == detailData[i].projectName) {
                        program.ccsId = ccsId;
                    }
                    program.label = detailData[i].projectName;
                    program.value = detailData[i].totalLength + "_" + detailData[i].endMileage;
                    default_projectNames.push(program);
                }
            }
            $("#div_city").bxcombobox({
                dataPattern: 'local',
                data: cityData,
                async: false

            });

            $("#div_line").bxcombobox({
                dataPattern: 'local',
                data: default_lineNames,
                async: false
            });

            $("#div_program").bxcombobox({
                dataPattern: 'local',
                data: default_projectNames,
                async: false
            });
            //写默认的城市名
            $("#div_city_select").val(cityCCSId);
            $("#div_line_select").val(ccsId);
            var all_options = document.getElementById("div_program_select").options;
            for(var i=0;i<all_options.length;i++){
                if(all_options[i].innerHTML == projectName ){
                    all_options[i].selected = true;
                }
            }
            $("#div_city").change(function () {
                programArray = [];
                $("#div_program").bxcombobox({
                    dataPattern: 'local',
                    data: programArray,
                    async: false
                });
                $("#div_program_select").prepend("<option value='' selected = selected>--选择项目--</option>");

                var strVal = $('#div_city_select').val();
                if (strVal == "") {
                    return;
                }
                var lineName = codename[strVal];
                $("#div_line").bxcombobox({
                    dataPattern: 'local',
                    data: lineName,
                    async: false
                });
                $("#div_line_select").prepend("<option value='' selected = selected>--选择线路--</option>");
            })

            $("#div_line").change(function () {
                var strVal = $('#div_line_select').val();
                if (strVal == "") {
                }
                programArray = []

                for (var i = 0; i < detailData.length; i++) {
                    if (strVal == detailData[i].ccsId) {
                        var program = {};
                        var totalLength =  detailData[i].totalLength;
                        var tatalMileage = detailData[i].endMileage;
                        program.label = detailData[i].projectName;
                        program.value =  totalLength + "_" + tatalMileage;
                        programArray.push(program);
                    }
                }
                $("#div_program").bxcombobox({
                    dataPattern: 'local',
                    data: programArray,
                    async: false
                });
                $("#div_program_select").prepend("<option value='' selected = selected>--选择项目--</option>");
            })
        })
    }

    function insertPage() {

        var button = [{
            text: "确定",
            "class" : "btn btn-skinColor btn-xs",
            click : function () {
                var projectId = null;
                var collectorName = null;
                var tbmName = null;
                var ringTotal = null;
                var cityname = $('#div_city_select option:selected').text();
                var linename = $('#div_line_select option:selected').text();
                var programname = $('#div_program_select option:selected').text();
                var totalData = $('#div_program_select option:selected').val();
                var totalArr = totalData.split("_");
                for(var i = 0; i <detailData.length; i++){
                    if(programname == detailData[i].projectName){
                        projectId = detailData[i].projectId;
                        collectorName = detailData[i].collectorName;
                        tbmName = detailData[i].tbmName;
                        ringTotal = detailData[i].ringTotal;
                        break;
                    }
                }
                if(projectId == null){
                    alertDiv("提示","请选择合法项目！");
                    return;
                }
                $("#detail").bxdialog('close');
                $.cookie('selected_collection',collectorName, {path: "/"});
                $.cookie('selected_name',programname, {path: "/"});
                $.cookie('selected_id',projectId, {path: "/"});
                $.cookie('totalLength',totalArr[0],{path:"/"});
                $.cookie('totalMileage',totalArr[1],{path:"/"});
                $.cookie('tbmName',tbmName,{path:"/"});
                $.cookie('ringTotal',ringTotal,{path:"/"});
                window.location.reload();

            }
        }]
        var title = "选择区间信息";
        var dialogOpt = {
            title: title,
            buttons:button,
            width: '300px'
        };
        $("#detail").bxdialog(dialogOpt);

    }



</script>

</body>
</html>
