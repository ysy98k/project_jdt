(function () {
    // initializing变量用来标示当前是否处于类的创建阶段，
    // - 在类的创建阶段是不能调用原型方法init的
    // fnTest是一个正则表达式，可能的取值为（/\b_super\b/ 或 /.*/）
    // - 对 /xyz/.test(function() { xyz; }) 的测试是为了检测浏览器是否支持test参数为函数的情况
    var initializing = false, fnTest = /xyz/.test(function () {
        xyz;
    }) ? /\b_super\b/ : /.*/;

    // The base Class implementation (does nothing)
    // 基类构造函数
    // 这里的this是window，所以这整段代码就向外界开辟了一扇窗户 - window.Class
    this.Class = function () {
    };

    // 继承方法定义Create a new Class that inherits from this class
    Class.extend = function (prop) {
        // - this指向的是Function（即是Class），那么this.prototype就是父类的原型对象
        // - 注意：_super指向父类的原型对象
        var _super = this.prototype;
        initializing = true;
        var prototype = new this();
        initializing = false;

        // 如果父类和子类有同名方法，并且子类中此方法（name）通过_super调用了父类方法
        // - 则重新定义此方法
        function fn(name, fn) {
            return function () {
                // 将实例方法_super保护起来。
                var tmp = this._super;
                // 在执行子类的实例方法name时，添加另外一个实例方法_super，此方法指向父类的同名方法
                this._super = _super[name];
                // 执行子类的方法name，注意在方法体内this._super可以调用父类的同名方法
                var ret = fn.apply(this, arguments);
                this._super = tmp;
                return ret;
            };
        }

        // Copy the properties over onto the new prototype
        // 拷贝prop中的所有属性到子类原型中
        for (var name in prop) {
            // 如果prop和父类中存在同名的函数，并且此函数中使用了_super方法，则对此方法进行特殊处理 - fn
            // 否则将此方法prop[name]直接赋值给子类的原型
            if (typeof prop[name] === "function" &&
                typeof _super[name] === "function" && fnTest.test(prop[name])) {
                prototype[name] = fn(name, prop[name]);
            } else {
                prototype[name] = prop[name];
            }
        }

        // The dummy class constructor
        function Class() {
            //All construction is actually done in the init method
            //在类的实例化时，调用原型方法init
            if (!initializing && this.init)
                this.init.apply(this, arguments);
        }

        // Populate our constructed prototype object
        // 子类的prototype指向父类的实例（完成继承的关键）
        Class.prototype = prototype;

        // Enforce the constructor to be what we expect
        // 修正constructor指向错误
        Class.prototype.constructor = Class;

        // And make this class extendable
        // 子类自动获取extend方法，arguments.callee指向当前正在执行的函数
        Class.extend = arguments.callee;
        return Class;
    };
})();

var DesignerUtils = (function () {
    function randomNumber() {
        return randomFromInterval(1, 1e6)
    }

    function randomFromInterval(e, t) {
        return Math.floor(Math.random() * (t - e + 1) + e)
    }

    function escapeJquery(srcString) {
        // 转义之后的结果
        var escapseResult = srcString;
        // javascript正则表达式中的特殊字符
        var jsSpecialChars = ["\\", "^", "$", "*", "?", ".", "+", "(", ")", "[",
            "]", "|", "{", "}"];
        // jquery中的特殊字符,不�?�正则表达式中的特殊字符
        var jquerySpecialChars = ["~", "`", "@", "#", "%", "&", "=", "'", "\"",
            ":", ";", "<", ">", ",", "/"];
        for (var i = 0; i < jsSpecialChars.length; i++) {
            escapseResult = escapseResult.replace(new RegExp("\\"
                + jsSpecialChars[i], "g"), "\\"
                + jsSpecialChars[i]);
        }
        for (var i = 0; i < jquerySpecialChars.length; i++) {
            escapseResult = escapseResult.replace(new RegExp(jquerySpecialChars[i],
                "g"), "\\" + jquerySpecialChars[i]);
        }
        return escapseResult;
    }

    //获得数据源对应的值
    var NDRge = /^\<.*\>$/, idReg = /^\#\{(.+)\}$/, sqlReg = /^\$\{(.+)\}$/,
        pdsReg = /^\%\{(.+)\}$/, ccsReg = /^\*\{(.+)\}$/, funcReg = /^FUNC\{(.+)\}$/;

    /*
     * 从下拉树输入框中解析出真实数值
     * #{组件ID}: 返回组件值对应的字符串
     * ${sqlName.col}: 返回sql查寻返回值对应的col列字符串
     * ${sqlName}: 返回sql查寻返回值对应的json字符串
     * %{pdsName}: 返回pds查寻结果字符串
     * *{ccsName}: 返回ccsId
     * FUNC{funcName}: 返回funcName()
     * <*>: 返回空字符串
     * 其它情况返回原值
     * */
    function queryViaComboTree(str) {
        if (!isAvailable(str)) {
            return "";
        }
        str = str.trim();
        if (NDRge.test(str)) return "";

        var value;
        // 组件ID
        if (value = idReg.exec(str)) {
            var widgetObj = widgetsUtils.getControllerList()[value[1]];
            return widgetObj ? widgetObj.getValue() || "" : "";
        }

        // ccsId
        if (value = ccsReg.exec(str)) {
            var ccsSetting = dataSourceditor.getDSOption(value[1]);
            return ccsSetting ? ccsSetting.options.ccsId : "";
        }

        // 自定义函数
        if (value = funcReg.exec(str)) {
            try {
                str = eval(value[1] + "()").toString();
            } catch (error) {
                str = "";
            }
            return str;
        }

        // sql字段
        if (value = sqlReg.exec(str)) {
            return querySql(value[1]);
        }

        // pds属性
        if (value = pdsReg.exec(str)) {
            return queryPds(value[1]);
        }

        // 普通字符串
        return str;
    }

    function querySql(strin) {
        var strout = "";
        var sqlname = strin.split(".")[0];
        var sqlSetting = dataSourceditor.getDSOption(sqlname);
        if (!sqlSetting) return "";

        var sqlOpt = sqlSetting["options"];
        var params = {};
        $.each(sqlOpt.sqlOptions, function (key, value) {
            params[queryViaComboTree(value.optionName)] = queryViaComboTree(value.optionValue);
        });
        var paramJsonObj = {'code': queryViaComboTree(sqlOpt.sqlCode), 'params': params};
        var sqlCallback = {
            onSuccess: function (jsonObj) {
                if (jsonObj.status != 0) return;
                var sqlcol = strin.split(".")[1];
                if (!sqlcol) return strout = JSON.stringify(jsonObj.data);

                $.each(jsonObj.data, function (index, item) {
                    strout += item[sqlcol] + ",";
                });
                strout = strout.substr(0, strout.length - 1);
            },
            onError: function () {
                strout = "";
            }
        };
        AjaxCommunicator.ajaxRequest('/sqlexechandler.do?method=query',
            'POST', paramJsonObj, sqlCallback);
        return strout;
    }

    function queryPds(strin) {
        var strout = "";
        var pdsName = strin.split(".")[0];
        var pdsSetting = dataSourceditor.getDSOption(pdsName);
        if (!pdsSetting) return "";

        var pdsOpt = pdsSetting["options"];
        var valueType = strin.split(".")[1];

        if ("snapshot" == pdsOpt.dataValueType) {
            //snapshot 算法
            var param = {
                instance: queryViaComboTree(pdsOpt.instance),
                attribute: queryViaComboTree(pdsOpt.attribute)
            };
            var pdsCallback = {
                onSuccess: function (jsonObj) {
                    if (jsonObj.status != 0 || jsonObj.quality != 192) return "";
                    strout = ("t" == valueType) ? DateUtil.convertDateToStr(
                        pdsOpt.returnFormatter, new Date(parseInt(jsonObj.timestamp))) : jsonObj.value;
                },
                onError: function () {
                    strout = "";
                }
            };

            AjaxCommunicator.ajaxRequest('/pdshandler.do?method=querySnapshot',
                'POST', param, pdsCallback);
        } else {
            //插值算法
            var intervals = parseInt(queryViaComboTree(pdsOpt.intervals));
            if (!intervals) return "";
            var limit = parseInt(queryViaComboTree(pdsOpt.limit));
            if (!limit) return "";
            var starTimeStamp = DateUtil.convertStrToDate(queryViaComboTree(pdsOpt.startTime)).getTime();
            var endTimeStamp = DateUtil.convertStrToDate(queryViaComboTree(pdsOpt.endTime)).getTime();
            var offset = parseInt(queryViaComboTree(pdsOpt.offset)) || 0;

            if ("t" == valueType) {
                starTimeStamp += intervals * offset;
                while (starTimeStamp <= endTimeStamp && limit > 0) {
                    strout += DateUtil.convertDateToStr(pdsOpt.returnFormatter, new Date(starTimeStamp)) + ",";
                    starTimeStamp += intervals;
                    limit--;
                }
                return strout.substr(0, strout.length - 1);
            }
            var param = {
                instance: queryViaComboTree(pdsOpt.instance),
                attribute: queryViaComboTree(pdsOpt.attribute),
                time: {
                    timeArea: [starTimeStamp, endTimeStamp],
                    intervals: intervals
                },
                limit: limit,
                offset: offset
            };

            var pdsCallback = {
                onSuccess: function (jsonObj) {
                    if (jsonObj.status != 0) return;

                    $.each(jsonObj.value, function (index, item) {
                        strout += (item || "") + ",";
                    });
                    strout = strout.substr(0, strout.length - 1);
                },
                onError: function () {
                    strout = "";
                }
            };
            AjaxCommunicator.ajaxRequest('/pdshandler.do?method=query',
                'POST', param, pdsCallback);
        }
        return strout;
    }

    /*
     * 检查树输入框的值
     * 有效值返回 true, 否则返回 false
     * */
    function checkComboTreeValue(str) {
        if (typeof str != 'string') return true;

        var value;
        // 组件ID
        if (value = idReg.exec(str))
            return Boolean(widgetsUtils.getControllerList()[value[1]]);
        // ccsId
        if (value = ccsReg.exec(str))
            return Boolean(dataSourceditor.getDSOption(value[1]));
        // 自定义函数
        if (value = funcReg.exec(str))
            return eval("/\\bfunction\\s+" + value[1] + "\\b|\\b" + value[1] + "(?=\\s?=\\s?function\\b)/.test(storageUtils.getJS(pageId))");
        // sql字段
        if (value = sqlReg.exec(str))
            return Boolean(dataSourceditor.getDSOption(value[1].split(".")[0]));
        // pds属性
        if (value = pdsReg.exec(str))
            return Boolean(dataSourceditor.getDSOption(value[1].split(".")[0]));

        return true;
    }

    //获取页面可选组件
    function getWidgetsSelectObj() {
        var allobj = widgetsUtils.getControllerList();
        var cells = [];
        for (var i in allobj) {//获取对象
            if (!allobj[i])
                continue;
            var cell = {
                "name": i,
                "type": allobj[i].getType()
            };
            cells.push(cell);
        }
        if (typeof(i) == "undefined") {
            cells = [{"name": "无关联组件", "type": "none"}];
        }

        return cells;
    }

    //生成组件选择器
    function genSelect($select, cells, type) {//$select是选择器对象：如$("#id"),$(".class")..
        for (var i = 0; i < cells.length; i++) {
            var cell = cells[i];
            if (type == "widget") {
                $select.append("<option value='" + cell.name + "@" + cell.type + "'>" + "#{" + cell.name + "}" + "</option>");
            } else {
                $select.append("<option value='" + cell.value + "'>" + cell.label + "</option>");
            }

        }
    }

    // 获取自定义function
    function getCusFuncList() {
        var cusFuncReg = /\bfunction\s+wls_.+?\b|\bwls_.+?(?=\s?=\s?function\b)/g;
        var pagejs = storageUtils.getJS(pageId);
        var funcList = pagejs.match(cusFuncReg) || [];
        $.each(funcList, function (index, item) {
            funcList[index] = item.replace(/function\s/, "");
        });
        return funcList;
    }

    // 刷新元素内部的图表
    function fitSubWidgets(context) {
        var $this = $(context);
        $.each(widgetsUtils.getBxgridList(), function (id, item) {
            if ($this.find("#" + id).length > 0) {
                $("#" + id).bxgrid("rawMethodCall", "setGridWidth", $("#" + id).parent().parent().width());
            }
        });
        $.each(widgetsUtils.getBxchartList(), function (id, item) {
            if ($this.find("#" + id).length > 0) {
                item.refresh();
            }
        });
    }

    /*
     * 获得媒体文件列表
     * 为了提高效率，只取一次文件列表并保存在js中
     * 若要获取最新列表，请保存页面后刷新
     * @param type 文件类型：video,picture,audio
     * */
    var mediaListMap = null;
    var pathPre = "/wls/";

    function getMediaList(type) {
        if (!mediaListMap) {
            mediaListMap = {video: [], audio: [], picture: []};
            var callback = {
                onSuccess: function (res) {
                    if (res.status + "" != "0") return;
                    $.each(res.rows, function (index, item) {
                        if(!item.mediaPath) return;
                        var mediaPath = item.mediaPath.replace(/\\/g,"/");
                        switch(item.mediaType){
                            case "picture" :
                                mediaListMap.picture.push({
                                    label: item.mediaName,
                                    value: pathPre + "picture/" + mediaPath
                                });
                                break;
                            case "video":
                                mediaListMap.video.push({
                                    label: item.mediaName,
                                    value: pathPre + "video/" + mediaPath
                                });
                                break;
                            case "audio":
                                mediaListMap.audio.push({
                                    label: item.mediaName,
                                    value: pathPre + "audio/" + mediaPath
                                });
                                break;
                        }
                    });
                }
            };
            AjaxCommunicator.ajaxRequest('/df/metamanage/mediaManage.do?method=query', 'POST', {}, callback);
        }
        return mediaListMap[type];
    }


    return {
        randomNumber: randomNumber,
        escapeJquery: escapeJquery,
        queryViaComboTree: queryViaComboTree,
        checkComboTreeValue: checkComboTreeValue,
        genSelect: genSelect,
        getWidgetsSelectObj: getWidgetsSelectObj,
        getCusFuncList: getCusFuncList,
        fitSubWidgets: fitSubWidgets,
        getMediaList: getMediaList
    };
})();

var WidgetObject = (function () {
    function WidgetObject(type, actions) {
        this._super = this.prototype;
        this._id = "";
        this._type = type;
        this._option = {};
        this._$html = {};
        this._js = "";
        this._isRunPattern = false;
        if (actions != undefined)
            this._actions = generateActions(type, actions);
        else
            this._actions = {
                type: type,
                actions: defaultActions.actions
            };

        this._titleView = undefined;
        this._configView = undefined;
        this._dataConfigView = undefined;
        this._view = undefined;
        this._preview = undefined;
    }

    var cloneView = function ($view) {
        var $newView = $view.clone();
        $newView.removeClass("view");
        $newView.addClass("demoview");
        return $newView;
    };

    var defaultActions = {
        "actions": [
            {
                "name": "Init",
                "params": "",
                "tooltip": "初始化时触发"
            }
        ]
    };

    var generateActions = function (type, type_action) {
        if (type_action.actions != undefined) {
            type_action.actions = defaultActions.actions.concat(type_action.actions);
        } else {
            type_action.actions = defaultActions.actions;
            if (type_action.type == undefined) {
                type_action.type = type;
            }
        }
        return type_action;
    };

    var storeStr = "Store";

    var configViewId_suffix = "$ready";

    var Joiner_Str = "$$";

    var createNewId = function (type) {
        var id = type + DesignerUtils.randomNumber();
        return id;
    };

    var getStoreId = function (id) {
        return storeStr + id;
    };

    var initConfigId = function ($config, widgetId) {
        $config.find("[id]").each(function (index, item) {
            var $item = $(item);
            var childId = $item.attr("id");
            if (typeof(childId) != "undefined") {
                $item.attr("id", widgetId + Joiner_Str + childId + configViewId_suffix);
            }
        });
    };

    var replaceConfigId = function ($config) {
        var children = $config.find("[id]");
        for (var index = 0; index < children.length; index++) {
            var $item = $(children[index]);
            var childId = $item.attr("id");
            if (typeof(childId) != "undefined") {
                $item.attr("id", childId.replace(configViewId_suffix, ""));
            }
        }
    };

    WidgetObject.prototype = {
        constructor: WidgetObject,

        getId: function () {
            return this._id;
        },
        getControlObj: function () {
            return $("#" + this._id);
        },
        getPreviewId: function () {
            return this._id;
        },
        setRunPattern: function (value) {
            this._isRunPattern = value;
        },
        isRunPattern: function () {
            return this._isRunPattern;
        },
        getType: function () {
            return this._type;
        },
        getOption: function (key) {
            return this._option[key];
        },
        setOption: function (key, value) {
            this._option[key] = value;
        },
        getOptions: function () {
            return this._option;
        },
        setOptions: function (option) {
            this._option = option;
        },
        getConfigViewsControl: function (id, $dest) {
            var controlId = this.getId() + Joiner_Str + id;
            var $item;
            if ($dest == undefined) {
                $item = $("#" + DesignerUtils.escapeJquery(controlId));
            }
            else {
                $item = $dest.find("#" + DesignerUtils.escapeJquery(controlId));
            }
            return $item;
        },
        setHtml: function ($html) {
            this._$html = $html;
        },
        getHtml: function () {
            return this._$html;
        },
        setPreview: function ($preview) {
            this._preview = $preview;
        },
        setView: function ($view) {
            this._view = $view;
        },
        getConfigView: function () {
            if (this._configView == undefined)
                this._configView = this._$html.find(".configView");
            if (!this.isRunPattern()) {
                var retView = this._configView.clone(true);
                replaceConfigId(retView);
                this.getConfigViewCore(retView);
                return retView;
            } else {
                return this._configView;
            }
        },
        getDataConfigView: function () {
            if (this._dataConfigView == undefined)
                this._dataConfigView = this._$html.find(".dataConfigView");
            if (!this.isRunPattern()) {
                var retView = this._dataConfigView.clone(true);
                replaceConfigId(retView);
                this.getDataConfigViewCore(retView);
                return retView;
            } else {
                return this._dataConfigView;
            }
        },
        getPreview: function () {
            if (this._preview == undefined)
                this._preview = this._$html.find(".demoview");
            return this._preview;
        },
        getView: function () {
            if (this._view == undefined)
                this._view = this._$html.find(".view");
            return this._view;
        },
        getTitleView: function () {
            if (this._titleView == undefined)
                this._titleView = this._$html.find(".preview");
            return this._titleView;
        },
        show: function () {
            this.getView().show();
            this.getPreview().hide();
            this.showCore();
            console.log("widget " + this.getId() + " show");
        },
        showPreview: function () {
            this.getView().hide();
            this.getPreview().show();
            this.showPreviewCore();
            console.log("widget " + this.getId() + " show preview");
        },
        saveHtml: function () {
            this._$html.data("id", this._id);
            this.saveHtmlCore();
        },
        saveOption: function () {
            var option = {};
            option.id = this._id;
            option.option = this.getOptions();
            var optionString = JSON.stringify(option);
            var $option = this._$html.find("span.option");
            if ($option.length == 0) {
                $option = $("<span class='option' style='display: none' ></span>");
                this._$html.append($option);
            }
            $option.text(optionString);
            this.saveOptionCore(optionString);
        },
        save: function () {
            this.saveHtml();
            this.saveOption();
        },
        init: function ($html) {
            // 生成新的ID
            if (this._id == "" || this._id == undefined) {
                this._id = createNewId(this._type);
            }

            this.loadEdit($html);
            this.getHtml().attr("data-id", this.getId());

            if (!$html.hasClass("widget"))
                $html.addClass("widget");

            var $config = this._$html.find(".configView");
            var $dataConfig = this._$html.find(".dataConfigView");
            initConfigId($config, this.getId());
            initConfigId($dataConfig, this.getId());
        },
        loadEdit: function ($html) {

            this.setHtml($html);
            this.initCore();
            var $option = $html.find("span.option");
            if ($option.length == 0) {
                this.initOption();
            } else {
                var optionStr = $option.text();
                var option = JSON.parse(optionStr);
                this._id = option.id;
                this.setOptions(option.option);
            }

            // 初始化 用于保存的storeView 以及 用于编辑态展示的 demoView
            var controlId = this.getHtml().data("id");
            var $storeView = this.getHtml().find(".view");
            var $demoView = cloneView($storeView);
            var $temp = $storeView.find("#" + controlId);
            if ($temp.length === 0) {
                $temp = $storeView.find("#" + getStoreId(this.getId()));
            }
            $temp.attr("id", getStoreId(this.getId()));
            $temp = $demoView.find("#" + controlId);
            if ($temp.length === 0) {
                $temp = $demoView.find("#" + getStoreId(this.getId()));
            }
            $temp.attr("id", this.getId());
            this.getHtml().append($demoView);
            $demoView.attr("title", this.getId());
            this.setPreview($demoView);
            this.setView($storeView);
        },
        load: function ($html) {
            this.setHtml($html);
            this.loadCore();
            var $option = $html.find("span.option");
            if ($option.length == 0) {
                this.initOption();
            } else {
                var optionStr = $option.text();
                var option = JSON.parse(optionStr);
                this._id = option.id;
                this.setOptions(option.option);
            }
            // 运行态初始化时，将storeView 中对应控件的ID改为 控件ID
            var controlId = this.getHtml().data("id");
            var $storeView = this.getHtml().find(".view");
            var $demoView = cloneView($storeView);
            var $temp = $storeView.find("#" + controlId);
            if ($temp.length == 0) {
                $temp = $storeView.find("#" + getStoreId(this.getId()));
            }
            $temp.attr("id", this.getId());
            this.setView($storeView);
            this.setPreview($demoView);
        },
        refresh: function () {
            if (this.isRunPattern()) {
                this.show();
            }
            else {
                this.showPreview();
            }
        },
        showConfigView: function ($confDest, $dataDest) {
            var $configView = this.getConfigView();
            $confDest.append($configView);
            $configView.show();
            // var $dataConfigView = this.getDataConfigView();
            // $dataDest.append($dataConfigView);
            // $dataConfigView.show();
        },
        showDataConfigView: function ($dest) {
            var $dataConfigView = this.getDataConfigView();
            $dest.append($dataConfigView);
            $dataConfigView.show();
        },
        bind: function (event, callback) {
            var $this = this;
            var fn = callback;
            if (typeof fn === 'string') {
                // support obj.func1.func2
                var fs = fn.split('.');
                if (fs.length > 1) {
                    fn = window;
                    $.each(fs, function (i, f) {
                        fn = fn[f];
                    });
                } else {
                    fn = window[fn];
                }
            }
            if (typeof fn === 'function') {
                var cb = {
                    name: fn.name,
                    call: function (arg) {
                        fn.apply($this.getSelfObj(), arg);
                    }
                };
                this.bindCore(event, cb);
            }
        },
        unbind: function (event) {

        },
        getActions: function () {
            return this._actions;
        },
        clean: function () {
            if (this.isRunPattern()) {
                // 可能需要remove demoView option等
                this._$html.find("span.option").remove();
                this.getPreview().remove();
                this.getConfigView().remove();
                this.getDataConfigView().remove();
            }
        },
        initCore: function () {

        },
        loadCore: function () {

        },
        initOption: function () {

        },
        showCore: function () {

        },
        saveHtmlCore: function () {

        },
        saveOptionCore: function (str) {
            // alert(jsStr);
        },
        showPreviewCore: function () {

        },
        getConfigViewCore: function ($configView) {

        },
        getDataConfigViewCore: function ($dataConfigView) {// 增加后台数据源配置

        },
        bindCore: function (eventname, callback) {
        },
        unbindCore: function (eventname) {
        },
        setOptionValue: function (optionObj, $attributeObj) {// 设置组件option的值
            var dateLevel = $attributeObj.data("level");
            if (dateLevel === undefined) {
                return;
            }
            var levelArray = dateLevel.split(".");
            var tempObj = optionObj;
            for (var i = 0; i < levelArray.length; i++) {
                var oneLevel = levelArray[i];
                if (oneLevel == $attributeObj.data("attribute")) {
                    if (tempObj[0] != null || tempObj[0] != undefined) {
                        tempObj[0][oneLevel] = this.getAttributeValue($attributeObj);
                    } else
                        tempObj[oneLevel] = this.getAttributeValue($attributeObj);
                } else if (tempObj[oneLevel] != null && tempObj[oneLevel] != undefined) {
                    tempObj = tempObj[oneLevel];
                } else if ((tempObj[oneLevel] == null || tempObj[oneLevel] == undefined) && (oneLevel != $attributeObj.data("attribute"))) {
                    tempObj[oneLevel] = {};
                }
            }
        },
        setAttributeFromOption: function (optionObj, $attributeObj) {// 从组件option中设置当前属性框的值
            var dateLevel = $attributeObj.data("level");
            if (dateLevel === undefined) {
                return;
            }
            var levelArray = dateLevel.split(".");
            var tempObj = optionObj;
            for (var i = 0; i < levelArray.length; i++) {
                var oneLevel = levelArray[i];
                if ((tempObj[oneLevel] != null && tempObj[oneLevel] != undefined) && (oneLevel == $attributeObj.data("attribute"))) {
                    this.setAttributeValue($attributeObj, tempObj[oneLevel]);
                } else if (tempObj[oneLevel] != null && tempObj[oneLevel] != undefined) {
                    tempObj = tempObj[oneLevel];
                } else if ((tempObj[oneLevel] == null || tempObj[oneLevel] == undefined) && (oneLevel != $attributeObj.data("attribute"))) {
                    tempObj[oneLevel] = {};
                } else if ((tempObj[0] != null || tempObj[0] != undefined) && (oneLevel == $attributeObj.data("attribute"))) {
                    this.setAttributeValue($attributeObj, tempObj[0][oneLevel]);
                }
            }
        },
        setAttributeValue: function ($attributeObj, value) {// 设置属性框的值
            if ($attributeObj.attr("type") == "checkbox") {
                $attributeObj.context.checked = value;
            } else {
                $attributeObj.val(value);
            }
        },
        getAttributeValue: function ($attributeObj) {// 从属性框得到值
            if ($attributeObj.data("type") == "array") {
                return $attributeObj.val().split(",");
            } else if ($attributeObj.data("type") == "number") {
                return parseFloat($attributeObj.val());
            } else {
                if ($attributeObj.attr("type") == "checkbox") {
                    return $attributeObj.context.checked;
                } else {
                    return $attributeObj.val();
                }
            }
        },
        setWidgetSize: function ($configView) {
            var $this = this;
            var widgetOpt = $this.getOptions();
            var heightArribute = $this.getConfigViewsControl("height", $configView);
            if (heightArribute != null && heightArribute != undefined) {
                heightArribute.val($("#" + $this.getId()).height());
                heightArribute.change(function () {
                    widgetOpt.height = $(this).val();
                    $this.setOptions(widgetOpt);
                    $this.refresh();
                });
            }
            var widthArribute = $this.getConfigViewsControl("width", $configView);
            if (widthArribute != null && widthArribute != undefined) {
                widthArribute.val($("#" + $this.getId()).width());
                widthArribute.change(function () {
                    widgetOpt.width = $(this).val();
                    $this.setOptions(widgetOpt);
                    $this.refresh();
                });
            }
        },
        disable: function () {
            return this.disableCore();
        },
        disableCore: function () {
        },
        enable: function () {
            return this.enableCore();
        },
        enableCore: function () {
        },
        getSelfObj: function () {
            if (this._self == undefined)
                this._self = new WidgetSelfObject(this);
            return this._self;
        },
        getValue: function () {
        }
    };


    var WidgetSelfObject = function (widObj) {
        this.widgetObj = widObj;
    };

    WidgetSelfObject.prototype = {
        getId: function () {
            return this.widgetObj.getId();
        },
        getControlObj: function () {
            return this.widgetObj.getControlObj();
        },
        getOptions: function () {
            return this.widgetObj.getOptions();
        },
        getOption: function (option) {
            return this.widgetObj.getOption(option);
        },
        setOption: function (option, value) {
            this.widgetObj.setOption(option, value);
        },
        setOptions: function (options) {
            var old = this.getOptions();
            $.extend(old, options);
            this.widgetObj.setOptions(old);
        },
        getType: function () {
            return this.widgetObj.getType();
        },
        refresh: function () {
            this.widgetObj.show();
        }
    };

    return WidgetObject;
})();

var Widget = (function () {

    var registeredWidgets = {};

    function createNewObject(type, $html) {
        if (type == undefined && $html == undefined) {
            return;
        }
        var object = new registeredWidgets[type];
        object.init($html);
        return object;
    }

    function registerType(type, widgetObject) {
        registeredWidgets[type] = widgetObject;
    }

    function loadWidget($html, flag) {
        var type = $html.data("type");
        type = getChartsTypeForUpdate(type);
        var object = new registeredWidgets[type];
        if (flag) {
            object.load($html);
        } else {
            object.loadEdit($html);
        }
        object.setRunPattern(flag);
        return object;
    }

    function getChartsTypeForUpdate(oldType) {//由于charts的type名字更换过，所以升级时需要考虑替换
        if ("bxchartsbarstand" == oldType) {
            return "chartCol";
        } else if ("bxchartsbarstack" == oldType) {
            return "chartColT";
        } else if ("bxchartsbarstandhorizontal" == oldType) {
            return "chartBar";
        } else if ("bxchartsbarstandhorizontalstack" == oldType) {
            return "chartBarT";
        } else if ("bxchartslinestand" == oldType) {
            return "chartLine";
        } else if ("bxchartslinestack" == oldType) {
            return "chartLineT";
        } else if ("bxchartslinesmooth" == oldType) {
            return "chartLineS";
        } else if ("bxchartslinearea" == oldType) {
            return "chartArea";
        } else if ("bxchartslineareastack" == oldType) {
            return "chartAreaT";
        } else if ("bxchartspiestand" == oldType) {
            return "chartPie";
        } else if ("bxchartspiecircular" == oldType) {
            return "chartPieC";
        } else if ("bxchartsgaugestand" == oldType) {
            return "chartGauge";
        } else if ("bxchartsmapstand" == oldType) {
            return "chartMap";
        } else if ("bxchartsradarstand" == oldType) {
            return "chartRadar";
        } else if ("bxchartsradarfill" == oldType) {
            return "chartRadarF";
        } else if ("bxchartsscatterstand" == oldType) {
            return "chartScat";
        } else {
            return oldType;
        }
    }

    function extend(type, newwidget) {
        var _widget = function () {
        };

        var _properties = function () {
        };
        _properties.prototype = {};
        var properties = new _properties();

        _widget.prototype = new WidgetObject(type, newwidget.actions);
        for (var itemKey in newwidget) {
            var item = newwidget[itemKey];
            if (typeof item === 'function') {
                _widget.prototype[itemKey] = item;
            } else {
                properties[itemKey] = item;
            }
        }
        _widget.prototype._properties = properties;
        registerType(type, _widget);
    }

    return {
        registerType: registerType,
        createNew: createNewObject,
        loadWidget: loadWidget,
        extend: extend
    };
})();

var widgetsUtils = (function () {
    var _controllerTypeList = {};
    var _controllerList = {};
    var _bxgridList = {};
    var _bxchartList = {};
    var configView = window["configViewContainer"] != undefined ? configViewContainer : null;
    var configDataView = window["configDataViewContainer"] != undefined ? configDataViewContainer : null;
    var flag = typeof(isEdit) == "undefined" || !isEdit;

    function getFunction(funcName) {
        var fn = funcName;
        if (typeof fn === 'string') {
            // support obj.func1.func2
            var fs = fn.split('.');
            if (fs.length > 1) {
                fn = window;
                $.each(fs, function (i, f) {
                    fn = fn[f];
                });
            } else {
                fn = window[fn];
            }
        }
        if (typeof fn === 'function') {
            return fn;
        }
    }

    function loadOne($widget, eventList) {
        var widgetObj = Widget.loadWidget($widget, flag);
        widgetObj.refresh();
        widgetObj.clean();
        if (widgetObj.isRunPattern()) {
            if (eventList[widgetObj.getId()]) {
                var eventArray = eventList[widgetObj.getId()];
                var funcName = "";
                for (var i = 0; i < eventArray.length; i++) {
                    var eventObj = eventArray[i];
                    if (eventObj.action.toLowerCase() === "Init".toLowerCase()) {
                        funcName = eventObj.function;
                    }
                    else {
                        widgetObj.bind(eventObj.action, eventObj.function);
                    }
                }
                // init function
                if (funcName != "") {
                    getFunction(funcName).apply(widgetObj.getSelfObj(), [widgetObj]);
                }
            }
        }
        widgetsUtils.addToControllList(widgetObj.getId(), widgetObj);
    }

    /*
     * 按widgetsList顺序依次加载组件
     * 为了不阻塞浏览器渲染页面的redraw队列，一个组件load并渲染后再显示另一个
     * */
    function showWidgetsInFile(widgetsList, eventList) {
        if (!widgetsList.length) return;
        loadOne(widgetsList[0], eventList);
        widgetsList.splice(0, 1);
        setTimeout(function () {
            showWidgetsInFile(widgetsList, eventList)
        }, 0);
    }

    var widgetsUtils = {
        addToTypeList: function (type, object) {
            _controllerTypeList[type] = object;
        },
        create: function ($component) {
            var type = $component.data("type");
            var widgetObj = Widget.createNew(type, $component);
            widgetObj.showPreview();
            this.addToControllList(widgetObj.getId(), widgetObj);
            this.showConfigView(widgetObj.getId());
            return widgetObj;
        },
        addToControllList: function (id, compObject) {
            if (compObject.getType() == 'bxgrid')
                _bxgridList[id] = compObject;
            else if (/^chart/.test(compObject.getType()))
                _bxchartList[id] = compObject;

            _controllerList[id] = compObject;
        },
        showConfigView: function (id) {
            this.cleanConfigView();
            var widgetObj = this.getControllerList()[id.split('_')[0]];
            if (widgetObj == undefined)
                return;
            widgetObj.showConfigView($(configView));
            widgetObj.showDataConfigView($(configDataView));
        },
        getTypeObject: function (type) {
            return _controllerTypeList[type];
        },
        getControllerList: function () {
            return _controllerList;
        },
        getBxgridList: function () {
            return _bxgridList;
        },
        getBxchartList: function () {
            return _bxchartList;
        },
        clearControllerList: function () {
            delete _controllerList;
            delete _bxchartList;
            delete _bxgridList;
            _controllerList = {};
            _bxgridList = {};
            _bxchartList = {};
        },
        removeController: function (id) {
            delete _controllerList[id];
            delete _bxchartList[id];
            delete _bxgridList[id];
        },
        saveControllers: function () {
            var list = this.getControllerList();
            for (var id in list) {
                var obj = list[id];
                obj.save();
            }
        },
        getSaveContent: function () {
            var content = $(".demo").html();
            var $content = $("<div></div>").html(content);
            $content.find(".demoview").remove();
            $content.find(".box .preview").remove();
            return $content.html();
        },
        load: function () {
            widgetsUtils.clearControllerList();
            var $content = $(".demo");
            var list = $content.find("div.widget");
            var eventList = loadEventList();

            // 优先加载信息化组件
            var chartList = [];
            $.each(list, function (index, item) {
                var $item = $(item);
                if (/^chart/.test($item.data("type"))) {
                    chartList.push($item);
                    return;
                }
                loadOne($item, eventList);
            });
            setTimeout(function () {
                showWidgetsInFile(chartList, eventList);
            }, 0)
        },
        cleanConfigView: function () {
            $(configView).empty();// 先清空右边属性区域内容
            $(configDataView).empty();
        }
    };

    function loadEventList() {
        var retList = {};
        var eventCont = storageUtils.getJS(pageId);
        var eventArray = eventCont.match(/function\s+([^\(]+)/g);
        if (eventArray) {
            for (var i = 0; i < eventArray.length; i++) {
                var tempStr = eventArray[i].replace("function", "").trim();
                var eventObj = {};
                eventObj.id = tempStr.split("_")[0];
                eventObj.action = tempStr.split("_")[1];
                eventObj.function = tempStr;
                if (retList[eventObj.id]) {
                    var array = retList[eventObj.id];
                    array.push(eventObj);
                }
                else {
                    var array = [];
                    array.push(eventObj);
                    retList[eventObj.id] = array;
                }
            }
        }
        return retList;
    }

    return widgetsUtils;
})();

/*
 * 设计器页面存储工具类
 * 保存、读取页面的操作步骤，js等
 * 本地浏览器有session就存在session中，否则保存在页面中
 * */
var storageUtils = (function () {
    var is_support_session = Boolean(typeof window.sessionStorage == 'object');
    var storer = is_support_session ? window.sessionStorage : {};
    var HTMLS_LIMIT = 10;
    var defaultCss = {
        ".demo": {
            "background-color": "#ffffff",
            "background-image": "url()",
            "background-position": "top",
            "background-repeat": "repeat"
        },
        ".editState": {
            "width": "100%"
        },
        ".runState": {
            "width": "100%"
        },
        ".demo .box .demoview,.demo .box .view": {
            "color": "#393939",
            "font-family": "微软雅黑",
            "font-style": "normal"
        },
        ".demo label": {
            "font-size": "14px",
            "font-weight": "normal"
        }
    }

    function isSupportSession() {
        return is_support_session;
    }


    /*
     * 自动保存页面操作步骤，包括html和css
     * step为要保存到的步骤位置，0为新增步骤，1为覆盖上一步，2为覆盖倒数第2步，依次类推
     * */
    function saveLayout(pageId, html, css, step) {
        var layoutList = JSON.parse(storer[pageId + "layoutList"] || "[]");
        var currentStep = layoutList.length;
        step = parseInt(step);
        step = step < 0 ? 0 : step > currentStep ? currentStep : step;

        layoutList[currentStep - step] = {html: html, css: css};
        var start = currentStep - step < HTMLS_LIMIT ? 0 : currentStep - step - HTMLS_LIMIT + 1;
        layoutList = layoutList.slice(start, currentStep - step + 1);

        storer[pageId + "layoutList"] = JSON.stringify(layoutList).trim();
        return layoutList;
    }

    function getLayoutList(pageId) {
        return JSON.parse(storer[pageId + "layoutList"] || "[]");
    }

    function getLatestLayout(pageId) {
        var layoutList = getLayoutList(pageId);
        return layoutList[layoutList.length - 1];
    }

    function getLayout(pageId, step) {
        step = step ? parseInt(step) : 0;
        var layoutList = getLayoutList(pageId);
        return layoutList[layoutList.length - step - 1];
    }

    /*
     * 保存页面js
     * */
    function saveJS(pageId, js) {
        storer[pageId + "jsString"] = JSON.stringify(js || "");
    }

    function getJS(pageId) {
        return JSON.parse(storer[pageId + "jsString"] || "\"\"");
    }

    function clean(pageId) {
        delete storer[pageId + "layoutList"];
        delete storer[pageId + "jsString"];
    }

    /*
     * 与数据库交互
     * */
    var loadService = new LoadSaveService();

    function update2DB(pageId, success, failed) {
        var layout = getLatestLayout(pageId);
        var jsString = getJS(pageId);
        loadService.saveDesignData(pageId, layout.html, jsString, JSON.stringify(layout.css),
            function (data) {
                success(data);
            }, function (data) {
                failed(data);
            });
    }

    function create2DB(paramJsonObj, success, failed) {
        var newId = null;
        var layout = getLatestLayout(newId);
        var jsString = getJS(newId);
        loadService.saveasDesignData(paramJsonObj, layout.html, jsString, JSON.stringify(layout.css),
            function (data) {//调用成功的回调函数
                success(data);
            }, function (data) {//调用失败的回调函数
                failed(data);
            });
    }

    function loadFromDB(pageId, callback) {
        if (!pageId) {
            clean(pageId);
            saveLayout(pageId, "", defaultCss, 0);
            saveJS(pageId, "");
            if (callback) callback();
            return;
        }

        var success = function (data) {
            var htmlString = data.htmlstring;
            var jsString = data.jsstring || "";
            var css = data.cssstring ? JSON.parse(data.cssstring) : defaultCss;

            clean(pageId);
            saveLayout(pageId, htmlString, css, 0);//将数据库中记录的html存放到缓存中，后续再从缓存中渲染restoreData出来
            saveJS(pageId, jsString);//将数据库中记录的js存放到缓存中，后续再从缓存中渲染restoreData出来
            var importJs = data.importJs;
            if (callback) {
                callback(importJs);
            }
        };
        var error = function (error) {
            clean(pageId);
            saveLayout(pageId, "", defaultCss, 0);
            saveJS(pageId, "");
            if (callback) {
                callback();
            }
        };
        loadService.loadDesignData(pageId, success, error);
    }

    return {
        isSupportSession: isSupportSession,
        saveLayout: saveLayout,
        getLayoutList: getLayoutList,
        getLatestLayout: getLatestLayout,
        getLayout: getLayout,
        saveJS: saveJS,
        getJS: getJS,
        clean: clean,
        update2DB: update2DB,
        create2DB: create2DB,
        loadFromDB: loadFromDB,
    };
})();

var ContentLoader = (function () {
    var isDebug = true;

    var init = function (debug) {
        isDebug = debug;
    };

    var loadJS = function (jsPath) {
        var dtd = $.Deferred();
        if (isDebug) {
            // 使用这段代码可以调试引入的JS，DEBUG使用
            var node = document.createElement('script');
            node.type = "text/javascript";
            var onload = function () {
                dtd.resolve();
            };
            $(node).load(onload).bind('readystatechange', function () {
                if (node.readyState == 'loaded') {
                    onload();
                }
            });
            document.getElementsByTagName('head')[0].appendChild(node);
            node.src = jsPath;
        } else {
            // 使用这样代码无法调试 引入的js，RELEASE使用
            $.getScript(jsPath, function () {
                dtd.resolve();
            });
        }
        return dtd.promise();
    };

    var loadHtml = function (htmlPath) {
        var dtd = $.Deferred();
        $.get(htmlPath).done(function (data) {
            dtd.resolve(data);
        });
        return dtd.promise();
    };

    var load = function (paths) {
        var dtd = $.Deferred();
        ContentLoader.loadJS(paths.js).done(function () {
            ContentLoader.loadHtml(paths.html).done(function (data) {
                dtd.resolve(data);
            });
        });
        return dtd.promise();
    };

    return {
        init: init,
        loadJS: loadJS,
        loadHtml: loadHtml,
        load: load
    }
})();


var loadWidgets = (function () {

    var prepareWidget = function ($title, widget) {
        //$title.text(widget.name);
        var imgpath = "url(" + widget.icon + ")";
        //$title.css("background", imgpath);
        $title.css("background-image", imgpath);
        $title.css("height", "100%");
        $title.addClass("no-repeat right bottom fixed");
        $title.attr("title", widget.name);
        var $name = $("<span></span>");
        $name.text(widget.name);
        $name.addClass("title");
        $title.append($name);
    };


    var prepare = function (widget, cs) {
        var component = $(cs);   // 添加
        component.attr("data-type", widget.type);
        component.attr("data-name", widget.name);
        if (!component.hasClass("box"))
            component.addClass("box box-element");
        else
            component.addClass("box-element");
        component.addClass("ui-draggable");

        var drag = $("<span class='drag label'>&nbsp;拖动</span>");
        drag.prepend($("<i class='ace-icon fa fa-arrows'></i>"));
        var remove = $("<a href='#close' class='remove label label-important'>&nbsp;删除</a>");
        remove.prepend($("<i class='ace-icon fa fa-trash-o'></i>"));
        var title = $("<div class='preview'></div>");

        prepareWidget(title, widget);

        component.prepend(title);
        component.prepend(drag);
        component.prepend(remove);
        if (widget.index % 2 != 0) {
            component.addClass("left-box");
        }

        var configView = component.find(".configView");
        if (configView != undefined) {
            configView.hide();
        }

        var dataConfigView = component.find(".dataConfigView");
        if (dataConfigView != undefined) {
            dataConfigView.hide();
        }

        widgetsUtils.addToTypeList(widget.type, component);
    };

    var loadOne = function (widget, callback) {
        var dtd = $.Deferred();
        ContentLoader.loadJS(widget.js).done(function () {
            ContentLoader.loadHtml(widget.html).done(function (data) {
                if (callback != undefined) {
                    callback(widget, data);
                }
                dtd.resolve();
            });
        });
        return dtd.promise();
    };

    var load = function () {
        var dtd = $.Deferred();
        $.ajax({
            type: "get",
            url: "controllers/widgets.json",
            cache: true,
            success: function (data) {
                var ret = [];
                var groupList = data.groups;
                for (var i = 0; i < groupList.length; i++) {
                    var obj = groupList[i];
                    var widgets = obj.widgets;
                    var widgetList = [];
                    for (var ii = 0; ii < widgets.length; ii++) {
                        widgetList[widgets[ii].index] = widgets[ii];
                    }
                    for (var iii = 0; iii < widgetList.length; iii++) {
                        if (widgetList[iii] == undefined) {
                            continue;
                        }
                        ret.push(loadOne(widgetList[iii], prepare));
                    }
                    groupList[i].widgets = widgetList;
                }

                $.when.apply($, ret).done(function () {
                    for (var i = 0; i < groupList.length; i++) {
                        var obj = groupList[i];
                        var list = [];
                        var widgetList = obj.widgets;
                        for (var ii = 0; ii < widgetList.length; ii++) {
                            if (widgetList[ii] == undefined) {
                                continue;
                            }
                            var type = widgetList[ii].type;
                            list.push(widgetsUtils.getTypeObject(type));
                        }
                        groupList[i].widgets = list;
                    }
                    dtd.resolve(groupList);
                });
            }
        });
        return dtd.promise();
    };

    return {
        load: load
    };
})();

/*
 * css样式工具
 * */
var pageCssUtils = (function () {
    function loadCss(cssMap) {
        /*var cssString=JSON.stringify(cssList);
         cssString=cssString.substr(1,cssString.length-2);*/
        var styleCss = "";
        for (var n in cssMap) {
            var cssContext = JSON.stringify(cssMap[n]);
            cssContext = cssContext.replace(/,/g, ';');
            cssContext = cssContext.replace(/"/g, '');
            var cssOne = n + cssContext;
            styleCss = styleCss + cssOne;
        }
        $("#demoCss").html(styleCss);
        return;
    }

    function setCss(selector, cssMap) {
        var layout = storageUtils.getLayout(pageId, currentStep);
        layout.css[selector] = cssMap;
        currentCss = layout.css;
        loadCss(currentCss);
        stopsave++;
        saveLayout(true);
        stopsave--;
        return currentCss;
    }

    function updateCss(selector, cssMap) {
        var layout = storageUtils.getLayout(pageId, currentStep);
        layout.css[selector] = layout.css[selector] || {};
        for (var cssKey in cssMap) {
            layout.css[selector][cssKey] = cssMap[cssKey];
        }
        currentCss = layout.css;
        loadCss(currentCss);
        stopsave++;
        saveLayout(true);
        stopsave--;
        return currentCss;
    }

    function getCss(selector, key) {
        var layout = storageUtils.getLayout(pageId, currentStep);
        if (layout.css[selector])
            return key ? layout.css[selector][key] : layout.css[selector];
    }


    return {
        loadCss: loadCss,
        setCss: setCss,
        updateCss: updateCss,
        getCss: getCss
    };
})();

/*
 * treeInput 组件工具类
 * */
var treeInputUtils = (function () {
    var timer = null;
    var icon2top = 6, icon2right = 18;

    function setIconPosition() {
        $(".treeInput_icon").each(function (index, element) {
            var $input = $(element).next(".treeInput");
            $(element).css({
                "top": $input.position().top + icon2top,
                "left": $input.position().left + $input.outerWidth() - icon2right
            });
        });
    }

    /*
     * 生成desinger页面组合下拉框
     * @param $input 要初始化的 input 输入框的 jquery 对象
     * @param treeOption 树对象
     * */
    function createTreeInput($input, treeOption) {
        $input.prev(".treeInput_icon").remove();
        $input.next(".treeInput_arround").remove();
        $input.css("cssText", "padding-right:20px !important");

        // 添加下拉框
        var $newArround = $('<div class="treeInput_arround"><div class="treeInput_tree"></div></div>');
        $newArround.find(".treeInput_tree").bxtree({
            dataPattern: 'local',
            showText: null,
            data: treeOption,
            option: {
                checkbox: {
                    keep_selected_style: false,
                    tie_selection: false,
                    three_state: false,
                    cascade: 'undetermined'
                },
                plugins: ["themes", "wholerow"]
            }
        }).on("select_node.jstree", function (enent, obj) {
            if (obj.node.children.length < 1) {
                $input.val(obj.node.id).focus();
                $input.trigger("change");
                $newArround.slideUp(100);
            }
        });
        $input.after($newArround);

        // 添加icon
        var $icon = $('<i class="treeInput_icon fa fa-pencil-square-o"></i>');
        $icon.on('click', function () {
            $newArround.css('width', $input.outerWidth());
            //设置绝对位置
            $newArround.css({
                'top': $input.outerHeight() + $input.position().top,
                'left': $input.position().left
            });
            $(".treeInput_arround").not($newArround).slideUp(100);
            $newArround.slideToggle(100);
        }).css({
            "top": $input.position().top + icon2top,
            "left": $input.position().left + $input.outerWidth() - icon2right
        });
        $input.before($icon);

        //添加点击事件
        $(document).on('click', function (event) {
            if ($(event.target).is(".treeInput_icon") || $(event.target).parents(".treeInput_arround").length > 0) {
                return;
            }
            $(".treeInput_arround").slideUp(100);
        });

        // set timer
        if (!timer) timer = setInterval(setIconPosition, 100);
    }

    return {
        createTreeInput: createTreeInput
    }
})();

/*
 * 封装取组件的方法
 * 供用户直接调用
 * 内部代码不建议使用
 * */
var _ = function (id) {
    return widgetsUtils.getControllerList()[id];
};
