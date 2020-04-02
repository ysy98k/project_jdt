var dataSourceditor = (function () {
    var dsname="";//当前数据源名称
    var _tempDataSourceOption;//临时数据源配置数据

    function initDataSGrid(options){
        var datalist=[];
        for(var data in options){
            options[data]['ds_name']=data;
            datalist.push(options[data]);
        }

        var dsGridOption = {
            primaryRowKey:"ds_name",
            colNames : ['数据源名称','数据源类型','数据源描述'],
            colModel : [
                {
                    name : 'ds_name',
                    index : 'ds_name',
                    editable : false,
                    width : 100,
                    editoptions: { maxlength: 1024}
                },
                {
                    name : 'ds_type',
                    index : 'ds_type',
                    width : 100,
                    editable : true,
                    edittype: 'select',
                    formatter: 'select',
                    editoptions: {value: {sql: 'sql',pds: 'pds',ccs: 'ccs'}},
                    editrules:{
                        required : true
                    }
                },
                {
                    name : 'ds_desc',
                    index : 'ds_desc',
                    width : 180,
                    editable : true,
                    editoptions: { maxlength: 1024}
                }],
            data:datalist,
            editurl: encodeURI('designer.do?method=query'),
            caption : false,
            sortorder : 'asc',
            onCellSelect : function(rowid,iCol,cellcontent,e){
                var curCheckbox = $(this).find('#' + rowid + ' input[type=checkbox]');
                if(isAvailable(curCheckbox) && curCheckbox.length > 0){
                    if($(e.target).is('input[type=checkbox]')){
                        if($(curCheckbox[0]).prop("checked") == false){
                            $(this).jqGrid("saveRow",rowid,null,"clientArray");
                        }else{
                            $(this).editRow(rowid,true);
                        }
                    }else if($(e.target).is('input[type=button]')||$(e.target).parent().is('a')||$(e.target).is('a')){
                        $(this).jqGrid("setSelection",rowid,false);
                    }else{
                        if($(curCheckbox[0]).prop("checked")){
                            $(this).editRow(rowid, true);
                            $(curCheckbox[0]).prop("checked",true);
                        }else{
                            showDSCfg(rowid);
                        }
                        $(this).jqGrid("setSelection",rowid,false);
                    }
                }
            }
        };

        var gridoption = {
            dataPattern: 'local',
            queryParam : {},
            showMsgOpt : {
                showMsgId : "alertdiv"
            },
            gridOption : dsGridOption,
            navGridOption: {
                refresh:true,
                refreshicon:"hide",
            }
        };
        $("#dataSourseGrid").bxgrid(gridoption);
        $("#dataSourseGrid").bxgrid('refreshLocalData',datalist);

    }

    function initTreeInput(){
        var widgetsObj = [],funcObj=[];
        $.each(DesignerUtils.getWidgetsSelectObj(), function (index, item) {
            widgetsObj.push({value: "#{" + item.name + "}", label: item.name});
        });
        $.each(DesignerUtils.getCusFuncList(), function (index, item) {
            funcObj.push({value: "FUNC{" + item + "}", label: item});
        });
        $("#data_detail").find(".treeInput_arround").remove();
        $("#data_detail").find(".treeInput").each(function (){
            var treeData = [{
                label: "页面组件",
                state: {opened: true},
                value: "<没有可用的组件>",
                children: widgetsObj
            },{
                label: "自定义函数",
                state: {opened: false},
                value: "<无可用的函数>",
                children: funcObj
            }];
            treeInputUtils.createTreeInput($(this), treeData);
        });
    }

    function initTimePicker(){
        $("#pdsdataS_startTime").bxtimepicker({
            defaultValue: "",
            option:{
                errDealMode:2,
                skin : 'default',
                dateFmt : 'yyyy-MM-dd HH:mm:ss',
                isShowClear:true
            }
        });
        $("#pdsdataS_startTime_dateInput").attr("placeholder","请选择时间或组件");
        $("#pdsdataS_startTime_dateInput").attr("onchange","dataSourceditor.getTime($(this),'startTime')");

        $("#pdsdataS_endTime").bxtimepicker({
            defaultValue: "",
            option:{
                errDealMode:2,
                skin : 'default',
                dateFmt : 'yyyy-MM-dd HH:mm:ss',
                isShowClear:true
            }
        });
        $("#pdsdataS_endTime_dateInput").attr("placeholder","请选择时间或组件");
        $("#pdsdataS_endTime_dateInput").attr("onchange","dataSourceditor.getTime($(this),'endTime')");
    }

    function showDSCfg(rowid){
        var seldata = $("#dataSourseGrid").bxgrid('rawMethodCall',"getRowData",rowid);
        if(JSON.stringify(seldata)!="{}"){
            if(_tempDataSourceOption[seldata.ds_name]){
                dsname=seldata.ds_name;
                var dstype=seldata.ds_type;
                dataSourceObj.init(dsname,dstype);
                if(dstype=="sql"&& !seldata.option){
                    $("#pdsdataScfg").hide();
                    $("#ccsdataScfg").hide();
                    $("#sqldataScfg").show();
                }else if(dstype=="pds"&& !seldata.option){
                    $("#sqldataScfg").hide();
                    $("#ccsdataScfg").hide();
                    $("#pdsdataScfg").show();
                }else if(dstype=="ccs"&& !seldata.option){
                    $("#sqldataScfg").hide();
                    $("#pdsdataScfg").hide();
                    $("#ccsdataScfg").show();
                }
            }else{
                alertDiv("提示","请先保存新增的数据源！");
            }
        }
    }

    function confirmDS() {
        var selectArray = $("#dataSourseGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
        if(selectArray.length == 0){
            alertDiv("提示","请至少勾选一条记录进行操作！")
            return;
        }

        //通过ds_name编辑状态来判断是添加还是修改
        for(var j=0;j<selectArray.length;j++){
            var saveData = $("#dataSourseGrid").bxgrid('rawMethodCall',"getRowData",selectArray[j]);
            if(saveData.ds_name.indexOf('class="editable"')>0){
                var nameVal=$("#"+selectArray[j]+"_ds_name").val();
                if(_tempDataSourceOption.hasOwnProperty(nameVal)){
                    alertDiv("错误","数据源名称重复");
                    return;
                }else if(nameVal==""){
                    alertDiv("错误","数据源名称不能为空");
                    return;
                }else if(!(/^[\u4e00-\u9fa5\w]+$/.test(nameVal))){
                    alertDiv("错误","数据源名称只能包括中英文、数字和下划线");
                    return;
                }
            }
        }

        //保存数据
        for(var i=0;i<selectArray.length;i++){
            var saveStatus = $("#dataSourseGrid").bxgrid('rawMethodCallMore',"saveRow",selectArray[i],null,"clientArray");
            /*if(saveStatus == false){
                return;
            }*/
            var saveData = $("#dataSourseGrid").bxgrid('rawMethodCall',"getRowData",selectArray[i]);
            dsname=saveData.ds_name;
            delete saveData.ds_name;
            var type=saveData.ds_type;
            if(type=="sql"){
                saveData.options={
                    sqlCode:"",
                    sqlCol:"",
                    sqlOptions:{
                    }
                };
                $("#pdsdataScfg").hide();
                $("#ccsdataScfg").hide();
                $("#sqldataScfg").show();
            }else if(type=="pds"){
                saveData.options={
                    dataValueType:"interpolation",
                    instance:"",
                    returnFormatter : 'yyyy-MM-dd',
                    intervals:"",
                    startTime:"",
                    endTime:"",
                    attribute: "",
                    limit:"50",
                    offset:"0"
                };
                $("#sqldataScfg").hide();
                $("#ccsdataScfg").hide();
                $("#pdsdataScfg").show();
            }else if(type=="ccs"){
                saveData.options={
                    ccsId:""
                };
                $("#sqldataScfg").hide();
                $("#pdsdataScfg").hide();
                $("#ccsdataScfg").show();
            }
            if(_tempDataSourceOption[dsname]){
                if(_tempDataSourceOption[dsname].ds_type==type)
                    saveData.options=_tempDataSourceOption[dsname].options;
            }
            dataSourceditor.setTempDSOption(dsname,saveData);
            dataSourceObj.init(dsname,type);
        }

        initDataSGrid(_tempDataSourceOption);
        refreshDS();
    }

    function refreshDS(){
        $("#refresh_table_dataSourseGrid").trigger("click");
    }

    function deleteDS() {
        var selectArray = $("#dataSourseGrid").bxgrid('rawMethodCall', 'getGridParam', 'selarrrow');
        if(selectArray.length == 0){
            alertDiv("提示","请至少勾选一条记录进行操作！");
            return;
        }
        var buttons = [ {
            html : "<i class='ace-icon fa fa-check bigger-110'></i>&nbsp; 是",
            "class" : "btn btn-info btn-xs",
            click : function() {
                deleteOK(selectArray);
                $(this).dialog("close");
            }
        }, {
            html : "<i class='ace-icon fa fa-times bigger-110'></i>&nbsp; 否",
            "class" : "btn btn-xs",
            click : function() {
                $(this).dialog("close");
            }
        } ];
        confirmDiv("确认删除", "此操作将删除所选数据源，数据删除后将不可恢复，是否确定删除？", buttons);
    }

    function deleteOK(selectArray) {
        var rownum=selectArray.length;
        for(var i=0;i<rownum;i++){
            var saveData = $("#dataSourseGrid").bxgrid('rawMethodCall',"getRowData",selectArray[0]);
            var key=saveData.ds_name;
            dataSourceditor.delTempDSOptions(key);
            $("#table_dataSourseGrid").jqGrid("delRowData", selectArray[0]);
        }
        initDataSGrid(_tempDataSourceOption);
        refreshDS();
        $("#data_detail").hide();
    }

    function refreshSqlCol(sqlCode){
        var paramJsonObj = {
            sqlCode:sqlCode
        };
        var callback = {
            onSuccess: function (paramJsonObj) {
                if(paramJsonObj.errcode==0){
                    if(paramJsonObj.results[0]){
                        var sqlCol=paramJsonObj.results[0].columns;
                        dataSourceditor.setTempDSOption(dsname+'^options^sqlCol', sqlCol);
                        $("#sqlCol").val(sqlCol);
                    }
                    else{
                        dataSourceditor.setTempDSOption(dsname+'^options^sqlCol', "");
                        $("#sqlCol").val("无数据!");
                    }
                }else{
                    dataSourceditor.setTempDSOption(dsname+'^options^sqlCol', "");
                    $("#sqlCol").val("执行失败，请填写正确的sql编码并确保数据库连接正常！");
                }
            }
        };
        AjaxCommunicator.ajaxRequest('/statementcolshandler.do?method=query', 'POST', paramJsonObj, callback);
    }

    function addDS() {
        $("#dataSourseGrid").bxgrid("addAndCopy");
    }

    var dataSourceObj={//数据源配置对象
        init: function (name,type) {
            $("#dsName").text(name);
            var dataSourcOptions=_tempDataSourceOption[name].options;
            if(type=="sql"){
                var sqlCode=_tempDataSourceOption[dsname].options.sqlCode;
                $("#sqloptionAttribute").hide();
                $("#sqldataCode").val(sqlCode);
                $("#sqldataCode").unbind().bind("change",function () {
                    dataSourceditor.setTempDSOption(dsname+'^options^sqlCode', $(this).val());
                    refreshSqlCol($(this).val());
                });
                refreshSqlCol(sqlCode);
                //绑定增加按参数钮，点击增加参数
                $("#addsqlDataBtn").unbind().bind("click", function () {
                    var newOpt = {
                        optionName:"",
                        optionValue:''
                    };
                    var sqloptionId = dataSourceObj.addSqlOption($(this).parent(), newOpt);
                    dataSourceObj.bindSqlOption(sqloptionId);
                    $("#"+sqloptionId).trigger("click");
                });
                // 初始化加载已有属性
                $("#sqldataScfg").find(".optionbtn").remove();
                var sqlOptions=dataSourcOptions.sqlOptions;
                for (var sqloptionId in sqlOptions) {
                    dataSourceObj.addSqlOption($("#addsqlDataBtn").parent(), sqlOptions[sqloptionId],sqloptionId);
                    $("#"+sqloptionId).children("span").text(sqlOptions[sqloptionId]["optionName"]);
                    dataSourceObj.bindSqlOption(sqloptionId);
                    if(! DesignerUtils.checkComboTreeValue(sqlOptions[sqloptionId].optionValue)){
                        $(this).val('');
                        _tempDataSourceOption[dsname].options.sqlOptions[sqloptionId].optionValue="";
                    }
                }
            }else if(type=="pds"){
                var attributeList = $("#pdsdataScfg").find("[data-attribute]");
                $.each(attributeList, function (index, item) {
                    WidgetObject.prototype.setAttributeFromOption(dataSourcOptions,$(this));
                    if(! DesignerUtils.checkComboTreeValue($(this).val())){
                        $(this).val('');
                        WidgetObject.prototype.setOptionValue(dataSourcOptions,$(this));
                        dataSourceditor.setTempDSOption(dsname+'^options', dataSourcOptions);
                    }

                    if($(this).data("attribute")=="dataValueType"){
                        if( $(this).val()=="interpolation"){
                            $(".interpolationOptions").show();
                        }else{
                            $(".interpolationOptions").hide();
                        }

                    }
                    if($(this).attr("data-time")){
                        var timepickerId=$(this).attr("data-time");
                        $("#"+timepickerId).val($(this).val());
                    }
                    $(this).unbind().bind("change",function () {
                        if($(this).data("attribute")=="dataValueType"){
                            if( $(this).val()=="interpolation"){
                                $(".interpolationOptions").show();
                            }else{
                                $(".interpolationOptions").hide();
                            }
                        }
                        var timepickerId=$(this).attr("data-time");
                        if(timepickerId){
                            $("#"+timepickerId).val($(this).val());
                        }
                        WidgetObject.prototype.setOptionValue(dataSourcOptions,$(this));
                        dataSourceditor.setTempDSOption(dsname+'^options', dataSourcOptions);
                    });
                });
            }else if(type=="ccs"){
                var attributeList = $("#ccsdataScfg").find("[data-attribute]");
                $.each(attributeList, function (index, item) {
                    WidgetObject.prototype.setAttributeFromOption(dataSourcOptions,$(this));
                    $(this).unbind().bind("change",function () {
                        WidgetObject.prototype.setOptionValue(dataSourcOptions,$(this));
                        dataSourceditor.setTempDSOption(dsname+'^options', dataSourcOptions);
                    });
                });
            }
            $("#data_detail").show();
        },
        addSqlOption: function ($btnObj, sqlOpt,sqloptionId) {// 增加参数按钮
            if(!sqloptionId){
                sqloptionId =dsname+ "_sqloption" + DesignerUtils.randomNumber();
            }
            var sqloption = "<div class='col-sm-12 btn btn-xs btn-yellow optionbtn' id='"+ sqloptionId + "' style='margin:0 15px 5px 15px;width: 91%'><span>参数</span><i class='ace-icon fa fa-times pull-right'data-dismiss='alert'style='margin-top:3px'></i></div>"
            $btnObj.before(sqloption);
            var sqlOptions = _tempDataSourceOption[dsname].options.sqlOptions;
            sqlOptions[sqloptionId] = deepCopy(sqlOpt);
            dataSourceditor.setTempDSOption(dsname+"^options^sqlOptions", sqlOptions);
            $("#"+sqloptionId).children("i").unbind().bind("click", function () {// 绑定删除系列按钮
                var sqlOptions = _tempDataSourceOption[dsname].options.sqlOptions;
                $("#"+sqloptionId).unbind();
                dataSourceObj.delSqlOption(sqloptionId, sqlOptions);
            });
            return sqloptionId;
        },
        delSqlOption: function (id, curObj) {// 删除参数对象
            delete curObj[id];
            dataSourceditor.setTempDSOption(dsname+"^options^sqlOptions",curObj);
            $("#sqloptionAttribute").hide();
        },
        bindSqlOption: function (id) {// 为参数按钮绑定属性
            var sqlOptionsAttribute = $("#sqloptionAttribute").find("[data-level]");
            $("#"+id).unbind().bind("click", function () {
                var curBtn = $(this);
                var curId = curBtn.attr("id");
                var sqlOptions = _tempDataSourceOption[dsname].options.sqlOptions;
                $("#sqloptionAttribute").show();
                $.each(sqlOptionsAttribute, function (index, item) {
                    WidgetObject.prototype.setAttributeFromOption(sqlOptions[curId],$(this));
                    $(this).unbind().bind("change",function () {
                        var curSqlOptions = _tempDataSourceOption[dsname].options.sqlOptions;
                        WidgetObject.prototype.setOptionValue(curSqlOptions[curId],$(this));
                        if ($(this).data("attribute") == "optionName")
                            curBtn.children("span").text($(this).val());
                        dataSourceditor.setTempDSOption(dsname+"^options^sqlOptions", curSqlOptions);
                    });
                });
            });
        }
    };

    var dataSourceditor = {
        init: function (pageId) {
            $("#data_detail").hide();
            //设置数据源存储dom
            if($("#dataSourceOption").length==0){
                var span='<span style="display: none" id="dataSourceOption"></span>';
                $(".demo").append(span);
            }
            var options=dataSourceditor.getDSOptions();
            _tempDataSourceOption=options;

            //绑定表格按钮事件
            $("#confirmDS").unbind().bind("click",function () {
                confirmDS();
            });
            $("#refreshDS").unbind().bind("click",function () {
                refreshDS();
            });
            $("#deleteDS").unbind().bind("click",function () {
                deleteDS();
            });
            $("#refreshSqlCol").unbind().bind("click",function () {
                refreshSqlCol($("#sqldataCode").val());
            });
            $("#addDS").unbind().bind("click",function () {
                addDS();
            });
            //初始化数据源表格
            initDataSGrid(options);

            //获取页面最新组件，初始化树下拉框
            initTreeInput();

            //初始化时间控件
            initTimePicker();

            //显示第一条记录的数据源配置
            showDSCfg(1);
        },
        getDSOption: function (key) {
            var _dataSourceOption=dataSourceditor.getDSOptions();
            return _dataSourceOption[key];
        },
        setTempDSOption: function (key, value) {
            var keylist=key.split("^");
            switch(keylist.length){
                case 1:
                    _tempDataSourceOption[keylist[0]] = value;
                    break;
                case 2:
                    _tempDataSourceOption[keylist[0]][keylist[1]] = value;
                    break;
                case 3:
                    _tempDataSourceOption[keylist[0]][keylist[1]][keylist[2]] = value;
                    break;
                case 4:
                    _tempDataSourceOption[keylist[0]][keylist[1]][keylist[2]][keylist[3]] = value;
                    break;
                default:
                    alert("最多支持4级参数");
                    return;
            }
        },
        getDSOptions: function () {
            var _dataSourceOption=$("#dataSourceOption").html()||"{}";
            return JSON.parse(_dataSourceOption);
        },
        setDSOptions: function (option) {
            $("#dataSourceOption").html(JSON.stringify(option));
        },
        delTempDSOptions:function (key) {
            delete _tempDataSourceOption[key];
        },
        saveDSOptions:function () {
            dataSourceditor.setDSOptions(_tempDataSourceOption);
        },
        getTime:function (e,key){
            dataSourceditor.setTempDSOption(dsname+'^options^'+key, e.val());
        }/*,
        removeUselessDSCfg:function(widgetId){
            var options=dataSourceditor.getDSOptions();
            for(var i in options){
                var dstype=options[i].ds_type;
                if(dstype!="ccs"){
                    var objOptions=options[i].options;
                    if(dstype=="pds"){
                        for(var pdsCfg in objOptions){
                            if(objOptions[pdsCfg]==widgetId){
                                objOptions[pdsCfg]=="";
                            }
                        }
                    }else if(dstype=="sql"){
                        var sqlOptions=objOptions.sqlOptions;
                        for(var sqlCfg in sqlOptions){
                            if(sqlOptions[sqlCfg][optionValue]==widgetId){
                                sqlOptions[sqlCfg][optionValue]=="";
                            }
                        }
                    }
                }
            }
            dataSourceditor.setDSOptions(options);
        }*/
    };
    return dataSourceditor;
})();