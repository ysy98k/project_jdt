(function (WidgetObj, type) {
    function BxGrid() {
    }

    var actions = {
        "type": "bxgrid",
        "actions": []
    };

    BxGrid.prototype = new BxWidgetCommon(type, actions);

    BxGrid.prototype.initOption = function () {
        var bxgrid = this;
        var gridid = bxgrid.getPreviewId();
        var data = [{
            id: gridid + '_row1',
            name: "示例",
            display_name: 'pay',
            description: 1
        }];
        var gridOption = {
            colNames: ['id', '操作', '文本列', 'ccdId列', '自定义下拉框列'],
            colModel: [{
                name: 'id',
                index: 'id',
                key: true,
                hidden: true,
                forbidCopy: true,
                edittype: 'text'
            },
                {
                    name: 'myac',
                    index: 'myac',
                    fixed: true,
                    sortable: false,
                    forbidCopy: true,
                    resize: false,
                    formatter: 'actions',
                    // 设置编辑和删除按钮
                    formatoptions: {
                        keys: false,
                        delbutton: false,
                        afterSave: save2opt,
                    }
                },
                {
                    name: 'name',
                    index: 'name',
                    hidden: false,
                    forbidCopy: false,
                    editable: true,
                    edittypeSel: 'text',
                    editoptionsVal: '',
                    sortable: true,
                    edittype: 'text'
                },
                {
                    name: 'display_name',
                    index: 'display_name',
                    hidden: false,
                    forbidCopy: false,
                    editable: true,
                    edittypeSel: 'ccs',
                    editoptionsVal: 'payment.status',
                    edittype: 'ccs',
                    editoptions: 'payment.status',
                    sortable: true
                },
                {
                    name: 'description',
                    index: 'description',
                    hidden: false,
                    forbidCopy: false,
                    editable: true,
                    edittypeSel: 'select',
                    editoptionsVal: '0:选项1,1:选项2',
                    edittype: 'select',
                    formatter: 'select',
                    editoptions: {
                        value: {
                            0: '选项1',
                            1: '选项2'
                        }
                    },
                    sortable: true
                }],
            height: 331,
            altRows: true,
            sortable: true,
            sortname: 'name',
            sortorder: 'desc',
            hasCaption: true,
            caption: "表格",
            loading: true,
            hasDownload: false,
            hasAddBtn: false,
            hasDelBtn: false,
            rowColor: "#F9F9F9",
            editurl: encodeURI('designer.do?method=query'),
            multiselect: false,
            data: data,
            gridComplete: function () {
                $(this).jqGrid('hideCol', 'cb');
            },
            initQuery: true,
            rowNum: 10
        };

        var option = {
            queryParam: {},
            dataPattern: "local",
            gridOption: gridOption,
            gridDsSetting: "",
            navGridOption: {
                add: true,
                addicon: 'ace-icon fa fa-plus-circle purple',
                addfunc: addRow,
                del: true,
                delicon: 'ace-icon fa fa-trash-o red',
                delfunc: deleteRow,
                edit: false,
                search: false,
                refresh: false,
                download: false,
                downloadfunc: downloadData
            },
            curRowNum : 10
        };
        this.setOptions(option);
    };

    BxGrid.prototype.showPreviewCore = function () {
        var id = this.getPreviewId();
        var option = this.prepareOption(false);
        if ($("#table_" + id).length) {
            $("#" + id).bxgrid("rawMethodCall", "GridUnload");
        }
        $("#" + id).bxgrid(option);

        if (option.gridOption.altRows) {
            $("#" + id).find(".ui-priority-secondary").css({"background": option["gridOption"].rowColor});
        }
        $("#" + id).find(".ui-jqgrid-title").attr("title", option.gridOption.caption);
    };

    BxGrid.prototype.showCore = function () {
        var id = this.getId();
        var option = this.prepareOption(true);
        if ($("#table_" + id).length) {
            $("#" + id).bxgrid("rawMethodCall", "GridUnload");
        }
        $("#" + id).bxgrid(option);
        option.initQuery = true;

        if (option.gridOption.altRows) {
            $("#" + id).find(".ui-priority-secondary").css({"background": option["gridOption"].rowColor});
        }
        $("#" + id).find(".ui-jqgrid-title").attr("title", option.gridOption.caption);
    };


    BxGrid.prototype.getConfigViewCore = function ($configView) {
        var BxGrid = this;
        var attributeList = BxGrid.getConfigViewsControl("attribute", $configView).find("[data-attribute]");
        var widgetOpt = BxGrid.getOptions()["gridOption"];

        BxGrid.getConfigViewsControl("gridId", $configView).text(BxGrid.getId());
        BxGrid.getConfigViewsControl("rowColor", $configView).colorpicker({color: widgetOpt.rowColor});


        $.each(attributeList,
            function (index, item) {
                BxGrid.setAttributeFromOption(widgetOpt, $(this));
                $(this).change(function () {
                    BxGrid.setOptionValue(widgetOpt, $(this));
                    if ($(this).data('attribute') == 'hasCaption') {
                        if ($(this).context.checked) widgetOpt.caption = BxGrid.getConfigViewsControl("caption", $configView).val();
                        else widgetOpt.caption = false;
                    } else {
                        if (widgetOpt.hasCaption == false) {
                            widgetOpt.caption = false;
                        }
                    }
                    if ($(this).data('attribute') == 'height') {
                        var curheight = parseInt($(this).val()) + 139;
                        $("#" + BxGrid.getId()).height(curheight);
                    }
                    if ($(this).data('attribute') == 'rowNum') {
                        BxGrid.setOption("curRowNum", parseInt(BxGrid.getOption("gridOption").rowNum));
                    }
                    BxGrid.setOption("gridOption", widgetOpt);
                    BxGrid.refresh();
                });
            });
    };

    BxGrid.prototype.initOptionBtn = function ($configView) { // 初始化增加值的按钮区域
        var BxGrid = this;
        var colModel = BxGrid.getOptions()["gridOption"]["colModel"];
        var colNames = BxGrid.getOptions()["gridOption"]["colNames"];
        for (var i = 2; i < colModel.length; i++) {
            BxGrid.generateOptionBtn($configView, colModel[i], colNames[i], i);
        }
    };

    BxGrid.prototype.generateOptionBtn = function ($configView, colModel, colName, index) { // 封装生成按钮的代码
        var BxGrid = this;
        var addColBtn = BxGrid.getConfigViewsControl("addColBtn", $configView).parent();
        if (colModel.id) {
            var colId = colModel.id;
        } else {
            var colId = "optionId" + DesignerUtils.randomNumber();
            BxGrid.getOptions()["gridOption"]["colModel"][index].id = colId;
        }

        var colOptButton = "<div class='col-sm-12 btn btn-xs btn-yellow' id='" + BxGrid.getId() + "$$" + colId + "' style='margin:0 15px 5px 15px;width: 86.9%'><span>" + colName + "</span><i class='ace-icon fa fa-times pull-right'data-dismiss='alert'style='margin-top:3px'></i></div>"
        addColBtn.before(colOptButton);
        BxGrid.bindOptionBtnDeleteClick(colId, $configView);
        BxGrid.bindOptionBtnClick(colId, $configView);
    };

    BxGrid.prototype.bindOptionBtnDeleteClick = function (colId, $configView) { // 封装点击按钮删除的代码
        var BxGrid = this;
        BxGrid.getConfigViewsControl(colId, $configView).children("i").bind("click",
            function () { // 绑定删除选项按钮事件
                BxGrid.getConfigViewsControl(colId, $configView).unbind();
                var colModel = BxGrid.getOptions()["gridOption"]["colModel"];
                var tempColModel = [];
                for (var i = 0; i < colModel.length; i++) {
                    tempColModel.push({});
                    if (colModel[i].id) tempColModel[i].id = BxGrid.getId() + "$$" + colModel[i].id;
                }
                var deleteIndex = BxGrid.functiontofindIndexByKeyValue(tempColModel, 'id', BxGrid.getId() + "$$" + colId);
                BxGrid.getOptions()["gridOption"]["colModel"].splice(deleteIndex, 1);
                BxGrid.getOptions()["gridOption"]["colNames"].splice(deleteIndex, 1);
                BxGrid.getConfigViewsControl("optionCol", $configView).hide();
                BxGrid.refresh();
            });
    };

    BxGrid.prototype.bindOptionBtnClick = function (colId, $configView) { // 封装点击按钮的代码
        var BxGrid = this;
        BxGrid.getConfigViewsControl(colId, $configView).unbind().bind("click",
            function () { // 绑定点击按钮事件
                var button = $(this);
                var colModel = BxGrid.getOptions()["gridOption"]["colModel"];
                var tempColModel = [];
                for (var i = 0; i < colModel.length; i++) {
                    tempColModel.push({});
                    if (colModel[i].id) tempColModel[i].id = BxGrid.getId() + "$$" + colModel[i].id;
                }
                var colIndex = BxGrid.functiontofindIndexByKeyValue(tempColModel, 'id', BxGrid.getId() + "$$" + colId);
                var clickOpt = BxGrid.getOptions()["gridOption"]["colModel"][colIndex];
                var optionCol = BxGrid.getConfigViewsControl("optionCol", $configView).find("[data-level]");
                $.each(optionCol,
                    function (index, item) {
                        if ($(this).data("attribute") == "colNames") {
                            $(this).val(button.find(">span").text());
                        } else {
                            BxGrid.setAttributeFromOption(clickOpt, $(this));
                            if ($(this).data("attribute") == "edittypeSel") {
                                if ($(this).val() == "text") BxGrid.getConfigViewsControl("editoptions", $configView).hide();
                                else {
                                    BxGrid.getConfigViewsControl("editoptions", $configView).show();
                                }
                            }
                        }

                        $(this).unbind().change(function () {
                            if ($(this).data("attribute") == "colNames") {
                                button.find(">span").text($(this).val());
                                BxGrid.getOptions()["gridOption"]["colNames"][colIndex] = $(this).val();
                            } else {
                                if ($(this).data("attribute") == "name") {
                                    var tempName = clickOpt.name;
                                    var newName = $(this).val();
                                    for (var m = 0; m < colModel.length; m++) {
                                        if (colModel[m].name == newName) {
                                            alertDiv("提示信息", "'列英文名'重复或是为空会导致表格数据错误");
                                            $(this).val(tempName);
                                            return;
                                        }
                                    }
                                    clickOpt.index = newName;
                                    var gridData = BxGrid.getOptions()["gridOption"]["data"];
                                    for (var i = 0; i < gridData.length; i++) {
                                        gridData[i][newName] = gridData[i][tempName];
                                        delete gridData[i][tempName];
                                    }
                                    BxGrid.setOptionValue(clickOpt, $(this));
                                } else if ($(this).data("attribute") == "edittypeSel") {
                                    clickOpt.edittypeSel = $(this).val();
                                    clickOpt.edittype = clickOpt.edittypeSel;
                                    if ($(this).val() == "text") {
                                        BxGrid.getConfigViewsControl("editoptions", $configView).hide();
                                        delete clickOpt.formatter;
                                    } else {
                                        BxGrid.getConfigViewsControl("editoptions", $configView).show();
                                        clickOpt.formatter = "select";
                                        if ($(this).val() == "select")
                                            BxGrid.getConfigViewsControl("editoptionsVal", $configView).attr("placeholder", "v1:选项1,v2:选项2..");
                                        else
                                            BxGrid.getConfigViewsControl("editoptionsVal", $configView).attr("placeholder", "ccsId");
                                    }
                                    BxGrid.getConfigViewsControl("editoptionsVal", $configView).val("");
                                    clickOpt.editoptionsVal = "";
                                    clickOpt.editoptions = "";
                                } else if ($(this).data("attribute") == "editoptionsVal") {
                                    clickOpt.editoptionsVal = $(this).val();
                                    if (clickOpt.edittypeSel == "select") {
                                        var editoptionsArray = $(this).val().split(",");
                                        var editoptionsObj = {};
                                        for (i = 0; i < editoptionsArray.length; i++) {
                                            var editoptionKey = editoptionsArray[i].split(":")[0];
                                            var editoptionVal = editoptionsArray[i].split(":")[1];
                                            editoptionsObj[editoptionKey] = editoptionVal;
                                        }
                                        clickOpt.editoptions = {};
                                        clickOpt.editoptions.value = editoptionsObj;

                                    } else if (clickOpt.edittypeSel == "ccs") {
                                        clickOpt.edittype = "ccs";
                                        clickOpt.editoptions = clickOpt.editoptionsVal;
                                    }
                                } else {
                                    BxGrid.setOptionValue(clickOpt, $(this));
                                }
                            }
                            BxGrid.refresh();
                        });
                    });
                BxGrid.getConfigViewsControl("optionCol", $configView).show();
            });
    };

    BxGrid.prototype.addElement = function ($configView) { // 增加元素
        var BxGrid = this;
        var optionId = "optionId" + DesignerUtils.randomNumber();
        var generateObj = {
            editable: true,
            id: optionId,
            name: '',
            index: '',
            edittype: 'text',
            edittypeSel: 'text',
            editoptionsVal: '',
            hidden: false,
            sortable: true,
            width: 60
        };
        BxGrid.getOptions()["gridOption"]["colModel"].push(generateObj);
        BxGrid.getOptions()["gridOption"]["colNames"].push('newCol');
        var gridData = BxGrid.getOptions()["gridOption"]["data"];
        for (var i = 0; i < gridData.length; i++) {
            gridData[i].newCol = "";
        }
        BxGrid.generateOptionBtn($configView, generateObj, "newCol");
        BxGrid.refresh();
        BxGrid.getConfigViewsControl(generateObj.id, $configView).trigger("click");
    };

    BxGrid.prototype.getDataConfigViewCore = function ($dataConfigView) {
        var bxgrid = this;
        $dataConfigView.find(".treeInput_arround").remove();
        bxgrid.initCombTrees($dataConfigView);
        var gridDsSetting = bxgrid.getConfigViewsControl("gridDsSetting", $dataConfigView);
        var dsDalue = bxgrid.getOption("gridDsSetting");
        bxgrid.setOption("gridDsSetting", DesignerUtils.checkComboTreeValue(dsDalue) ? dsDalue : "");
        gridDsSetting.change(function () {
            bxgrid.setOption("gridDsSetting", $(this).val());
            bxgrid.refresh();
        });
        gridDsSetting.val(bxgrid.getOptions()["gridDsSetting"]);

        var attributeList = bxgrid.getConfigViewsControl("dataAttribute", $dataConfigView).find("[data-attribute]");
        var widgetOpt = bxgrid.getOptions()["gridOption"];
        $.each(attributeList,
            function (index, item) {
                bxgrid.setAttributeFromOption(widgetOpt, $(this));
                $(this).change(function () {
                    bxgrid.setOptionValue(widgetOpt, $(this));
                    bxgrid.refresh();
                });
            });

        /* 设置列 */
        bxgrid.initOptionBtn($dataConfigView);
        bxgrid.getConfigViewsControl("addColBtn", $dataConfigView).unbind().bind("click",
            function () {
                bxgrid.addElement($dataConfigView);
                bxgrid.refresh();
            });
    };

    /* 初始化组合下拉框 */
    BxGrid.prototype.initCombTrees = function ($configView) {
        var sqlObj = [], funcObj = [];

        $.each(dataSourceditor.getDSOptions(), function (key, value) {
            switch (value.ds_type) {
                case "sql":
                    var sqlHead = "${" + key;
                    sqlObj.push({
                        label: key,
                        value: sqlHead + "}",
                        state: {opened: false}
                    });
                    break;
            }
        });

        $.each(DesignerUtils.getCusFuncList(), function (index, item) {
            funcObj.push({value: "FUNC{" + item + "}", label: item});
        });

        var treeData = [{
            label: "sql",
            state: {opened: false},
            value: "<无可用的sql配制>",
            children: sqlObj
        }, {
            label: "自定义函数",
            state: {opened: false},
            value: "<无可用的函数>",
            children: funcObj
        }];

        $configView.find(".treeInput").each(function () {
            treeInputUtils.createTreeInput($(this), treeData);
        });
    };

    //表格查询下拉框树，用以配置option属性
    BxGrid.prototype.queryViaComboTree = function (option) {
        var $grid = $("#" + this.getId());
        var str = option.gridDsSetting;
        option.dataPattern = "local";

        //正则表达式
        var NDRge = /^\<.*\>$/, sqlReg = /^\$\{(.+)\}$/, funcReg = /^FUNC\{(.+)\}$/;

        str = str.trim();
        if (NDRge.test(str)) return;

        var value;
        // 自定义函数
        if (value = funcReg.exec(str)) {
            try {
                str = eval(value[1] + "()").toString();
            } catch (error) {
                str = "";
            }
            if (isJSON(str)) option.gridOption.data = JSON.parse(str);
            return;
        }

        // sql字段
        if (value = sqlReg.exec(str)) {
            var sqlname = value[1].split(".")[0];
            var sqlSetting = dataSourceditor.getDSOption(sqlname);
            if (!sqlSetting) return "";

            var sqlOpt = sqlSetting["options"];
            var params = {};
            var queryParam = {};
            $.each(sqlOpt.sqlOptions, function (key, value) {
                params[value.optionName] = DesignerUtils.queryViaComboTree(value.optionValue);
            });

            queryParam.code = sqlOpt.sqlCode;
            queryParam.params = params;

            option.queryParam = queryParam;
            option.dataPattern = "url";
            option.url = "/df/designer/controllers/bxgrid.do?method=query";
            return;
        }

        // 普通字符串
        if (isJSON(str)) option.gridOption.data = JSON.parse(str);
    };

    //设置option
    BxGrid.prototype.prepareOption = function (isRunPattern) {
        var option = this.getOptions();
        //在保存设置时，function无法被序列化为字符串，因此在show的时候要重新设置
        option.navGridOption.addfunc = addRow;
        option.navGridOption.delfunc = deleteRow;
        option.gridOption.rowNum = option.curRowNum;

        if (isRunPattern) {
            option.navGridOption.download = option.gridOption.hasDownload;
            option.navGridOption.add = option.gridOption.hasAddBtn;
            option.gridOption.colModel[1].hidden = !option.gridOption.hasAddBtn;
            option.navGridOption.del = option.gridOption.hasDelBtn;
            if (option.gridOption.hasDownload) option.navGridOption.downloadfunc = downloadData;
            if (!option.initQuery) option.initQuery = option.gridOption.initQuery;

        } else {
            option.navGridOption.add = true;
            option.navGridOption.del = true;
            option.navGridOption.download = false;
            option.gridOption.colModel[1].formatoptions.afterSave = save2opt;
            delete option.initQuery;
        }

        this.queryViaComboTree(option);
        return option;
    };

    function getExplorer() {
        var explorer = window.navigator.userAgent;
        //ie
        if (explorer.indexOf("MSIE") >= 0 || !!window.ActiveXObject || "ActiveXObject" in window) {
            return 'ie';
        }
        //firefox
        else if (explorer.indexOf("Firefox") >= 0) {
            return 'Firefox';
        }
        //Chrome
        else if (explorer.indexOf("Chrome") >= 0) {
            return 'Chrome';
        }
        //Opera
        else if (explorer.indexOf("Opera") >= 0) {
            return 'Opera';
        }
        //Safari
        else if (explorer.indexOf("Safari") >= 0) {
            return 'Safari';
        }
    }

    function downloadData() {
        var gridid = this.id.split("_")[1];
        var thisGrid = $("#gview_table_" + gridid);
        var downGrid = $('<table id="downloadTab"></table>');
        downGrid.append(thisGrid.find(".ui-jqgrid-htable>thead").clone(),
            thisGrid.find(".ui-jqgrid-btable>tbody").clone());
        downGrid.find(".jqgfirstrow").remove();
        downGrid.find(".ui-priority-secondary").removeAttr("style");
        var gridRows = downGrid[0].rows;
        var gridOption = widgetsUtils.getControllerList()[gridid].getOption("gridOption");
        var colOpt = gridOption.colModel;
        for (var i = colOpt.length - 1; i >= 0; i--) {
            if (!colOpt[i].forbidCopy) continue;
            for (var j = 0; j < gridRows.length; j++) {
                gridRows[j].deleteCell(i);
            }
        }

        beginDownload(gridOption.caption, downGrid);
    }

    function beginDownload(tabname, $tab) {//整个表格拷贝到EXCEL中
        if (getExplorer() == 'ie') {
            var curTbl = $tab[0];
            var oXL;
            try {
                oXL = new ActiveXObject("Excel.Application"); //创建AX对象excel
            } catch (e) {
                alert("无法启动Excel!\n\n如果您确信您的电脑中已经安装了Excel，" + "那么请调整IE的安全级别。\n\n具体操作：\n\n" + "工具 → Internet选项 → 安全 → 自定义级别 → 对没有标记为安全的ActiveX进行初始化和脚本运行 → 启用");
                return false;
            }
            var oWB = oXL.Workbooks.Add();
            var oSheet = oWB.ActiveSheet;
            var Lenr = curTbl.rows.length;
            for (var i = 0; i < Lenr; i++) {
                var Lenc = curTbl.rows(i).cells.length;
                for (var j = 0; j < Lenc; j++) {
                    oSheet.Cells(i + 1, j + 1).value = curTbl.rows(i).cells(j).innerText;
                }
            }
            try {
                var fname = oXL.Application.GetSaveAsFilename(tabname + ".xlsx", "Excel 工作簿(*.xlsx), *.xlsx");
            } catch (e) {
                print("Nested catch caught " + e);
            } finally {
                if (fname) oWB.SaveAs(fname);
                oWB.Close(savechanges = false);
                oXL.Quit();
                oXL = null;
            }

        }
        else {
            var uri = 'data:application/vnd.ms-excel;base64,',
                template = '<html xmlns:v="urn:schemas-microsoft-com:vml" xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40">' +
                    '<head><meta charset="UTF-8">' +
                    '<!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]-->' +
                    '<style type="text/css">td{mso-number-format:"\@"}</style></head>' +
                    '<body><table>{table}</table></body>' +
                    '</html>',
                base64 = function (s) {
                    return window.btoa(unescape(encodeURIComponent(s)))
                },
                format = function (s, c) {
                    return s.replace(/{(\w+)}/g,
                        function (m, p) {
                            return c[p];
                        })
                };
            var ctx = {worksheet: tabname || 'Worksheet', table: $tab.html()};
            window.location.href = uri + base64(format(template, ctx));
        }
    }

    /*
     * 将表格内容保存在option 中*/
    function save2opt(rowid, res) {
        var gridid = this.id.split("_")[1];
        var BxGrid = widgetsUtils.getControllerList()[gridid];
        var gridOption = BxGrid.getOptions().gridOption.data;
        for (var i = 0; i < gridOption.length; i++) {
            if (rowid == gridOption[i].id) {
                gridOption.splice(i, 1);
                break;
            }
        }
        gridOption.push($(this).jqGrid("getRowData", rowid));
    }

    function addRow() {
        var gridid = this.id.split("_")[1];
        var BxGrid = widgetsUtils.getControllerList()[gridid];
        var gridtable = this.id;
        var addIndex = "row" + DesignerUtils.randomNumber();
        $("#" + this.id).addRowData(gridid + "_" + addIndex, {});
        if (BxGrid.getOptions()["gridOption"].altRows) {
            var rownum = $("#" + gridtable).find("tr").length;
            if (rownum % 2 === 1) {
                $("#" + gridid + "_" + addIndex).css({"background": BxGrid.getOptions()["gridOption"].rowColor});
            } else {
                $("#" + gridid + "_" + addIndex).removeClass("ui-priority-secondary");
            }
        }
        $("#" + this.id).jqGrid('setSelection', gridid + "_" + addIndex);
        jQuery.fn.fmatter.rowactions.call($("#jEditButton_" + gridid + "_" + addIndex), 'edit');
        jQuery.fn.fmatter.rowactions.call($("#jEditButton_" + gridid + "_" + addIndex), 'save');
        jQuery.fn.fmatter.rowactions.call($("#jEditButton_" + gridid + "_" + addIndex), 'edit');
    }

    function deleteRow() {
        var gridid = this.id.split("_")[1];
        var selectedrow = $("#" + gridid).bxgrid('rawMethodCall', "getGridParam", "selrow");
        var buttons = [{
            text: '确认',
            "class": "btn btn-block btn-xs red",
            click: function () {
                deleteOK(selectedrow, gridid);
                $("#dialog-delect-message").bxdialog('close');
            }
        }];
        var dialogOpt = {
            title: "<i class='ace-icon fa fa-exclamation-triangle red'></i>  确认删除",
            dataPattern: 'text',
            content: "数据删除后将不可恢复，是否确定删除！",
            buttons: buttons
        };
        $("#dialog-delect-message").bxdialog(dialogOpt);
        return;
    }

    function deleteOK(selectedrow, gridid) {
        $("#table_" + gridid).jqGrid("delRowData", selectedrow);
        var BxGrid = widgetsUtils.getControllerList()[gridid];
        if (BxGrid.getOptions()["gridOption"].altRows) {
            var gridrows = $("#table_" + gridid).find("tr");
            $(gridrows).each(function (i) {
                if (i % 2 === 0) {
                    $(this).css({"background": BxGrid.getOptions()["gridOption"].rowColor});
                }
                else {
                    $(this).removeClass("ui-priority-secondary");
                    $(this).removeAttr("style");
                }
            });
        }
        widgetsUtils.getControllerList()[gridid].getOptions().gridOption.data = $("#table_" + gridid).jqGrid("getRowData");
    }

    function isJSON(str) {
        if (typeof str == 'string') {
            try {
                JSON.parse(str);
                return true;
            } catch (e) {
                console.log(e);
                return false;
            }
        } else
            return false;
    }

    Widget.registerType(type, BxGrid);
})(WidgetObject, "bxgrid");