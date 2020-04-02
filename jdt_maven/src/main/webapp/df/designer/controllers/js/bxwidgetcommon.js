var BxWidgetCommon = (function () {

    function BxWidgetCommon(type, actions) {

        function BxWidgetCommon() {

        };

        BxWidgetCommon.prototype = new WidgetObject(type, actions);

        /*在arraytosearch数组中查找键值为key，值为value的元素所在的索引*/
        BxWidgetCommon.prototype.functiontofindIndexByKeyValue = function (arraytosearch, key, value) {
            for (var i = 0; i < arraytosearch.length; i++) {
                if (arraytosearch[i][key] == value) {
                    return i;
                }
            }
            return -1;
        };


        /*得到字体的样式*/
        BxWidgetCommon.prototype.getFontStyle = function ($target, $configView) {
            //生成字体库
            var defaultFontFamily = ['none', '微软雅黑', '宋体', '楷体', '幼圆', '隶书', '黑体', 'Arial', 'Verdana', 'Times New Roman', 'Garamond', 'Comic Sans MS', 'Courier New',
                'Georgia', 'Lucida Console', 'Tahoma'];
            var $fontFamilySel = $target.getConfigViewsControl("fontFamily", $configView);
            for (var i = 0; i < defaultFontFamily.length; i++) {
                if (defaultFontFamily[i] == 'none')
                    $fontFamilySel.append("<option value='none'>继承</option>");
                else
                    $fontFamilySel.append("<option value='" + defaultFontFamily[i] + "'>" + defaultFontFamily[i] + "</option>");
            }

            //生成字号库
            var defaultFontSize = ['none', '10px', '12px', '14px', '16px', '18px', '20px', '22px', '24px', '26px', '28px', '36px', '48px', '72px'];
            var $fontSizeSel = $target.getConfigViewsControl("fontSize", $configView);
            for (var m = 0; m < defaultFontSize.length; m++) {
                if (defaultFontSize[m] == 'none')
                    $fontSizeSel.append("<option value='none'>继承</option>");
                else
                    $fontSizeSel.append("<option value='" + defaultFontSize[m] + "'>" + defaultFontSize[m] + "</option>");
            }

            //生成字形库
            var defaultFontStyle = {"none": "继承", "normal": "常规", "italic": "斜体"};
            var $fontStyleSel = $target.getConfigViewsControl("fontStyle", $configView);
            ;
            for (var style in defaultFontStyle) {
                $fontStyleSel.append("<option value='" + style + "'>" + defaultFontStyle[style] + "</option>");
            }
            var defaultFontWeight = {"none": "继承", "normal": "常规", "bold": "加粗"};
            var $fontWeightSel = $target.getConfigViewsControl("fontWeight", $configView);
            for (var weight in defaultFontWeight) {
                $fontWeightSel.append("<option value='" + weight + "'>" + defaultFontWeight[weight] + "</option>");
            }
        };

        /* 初始化组合下拉框 */
        BxWidgetCommon.prototype.initCombTrees = function ($configView) {
            var widgetsObj = [], sqlObj = [], pdsObj = [], funcObj = [];

            var typeList = /^((input)|(bxtree)|(bxcombobox)|(label)|(bxtimepicker)|(textarea)|(radio)|(checkbox)|(bxprogressbar)|(switch))$/;
            $.each(DesignerUtils.getWidgetsSelectObj(), function (index, item) {
                if (typeList.test(item.type))
                    widgetsObj.push({value: "#{" + item.name + "}", label: item.name});
            });

            $.each(dataSourceditor.getDSOptions(), function (key, value) {
                switch (value.ds_type) {
                    case "sql":
                        var sqlOpt = value.options;
                        var column = [];
                        var sqlHead = "${" + key + ".";
                        $.each(sqlOpt.sqlCol.split(","), function (index, item) {
                            column.push({value: sqlHead + item + "}", label: item});
                        });
                        sqlObj.push({
                            label: key,
                            value: sqlHead + "id}",
                            state: {opened: false},
                            children: column
                        });
                        break;
                    case "pds":
                        pdsObj.push({
                            label: key,
                            state: {opened: false},
                            children: [
                                {value: "%{" + key + ".v}", label: "值"},
                                {value: "%{" + key + ".t}", label: "时间"}
                            ]
                        });
                        break;
                }
            });

            $.each(DesignerUtils.getCusFuncList(), function (index, item) {
                funcObj.push({value:"FUNC{"+item+"}", label:item});
            });

            var treeData = [ {
                label: "sql",
                state: {opened: false},
                value: "<无可用的sql配制>",
                children: sqlObj
            }, {
                label: "pds",
                state: {opened: false},
                value: "<无可用的pds配制>",
                children: pdsObj
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

        return new BxWidgetCommon();
    }

    return BxWidgetCommon;
})();