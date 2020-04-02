var Container = (function () {
    /*var elementProperties = {
     margin: {
     _var: {},
     getValue: function (target) {
     var $t = $(target);
     var values = $t.css("margin").replace(/px/g, "").split(" ");
     this._var.values = values;
     return values[3];
     },
     setValue: function (target, value) {
     var $t = $(target);
     var values = [0, 0, 0, 0];
     if (this._var["values"] != undefined) {
     values = this._var["values"];
     }
     if (value != undefined) {
     values[3] = value;
     }
     var joinString = $.map(values, function (val) {
     return val + "px";
     }).join(" ");
     $t.css("margin", joinString);
     }
     }
     };*/

    var bgurlReg = /^url\((.+)\)$/;
    var positionReg = /^50% 50%$|^0% 50%$|^100% 50%$|^50% 0%$|^50% 100%$/;
    var repeatReg = /^repeat$|^repeat-x$|^repeat-y$|^no-repeat$/;

    var isSuccess = false;
    var showConfigView = function (target, $dest) {
        var $configview = ContainerLoader.getColumnConfigView();
        $dest.empty();
        $dest.append($configview);
        bindConfigViewAction($(target), $configview);
    };

    var getHexColor = function (rgb) {
        rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
        function hex(x) {
            return ("0" + parseInt(x).toString(16)).slice(-2);
        }

        rgb = "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]);
        return rgb;
    };

    var bindConfigViewAction = function ($target, $configview) {
        //页面宽度
        var runPageWidth = pageCssUtils.getCss('.runState', 'width');
        if (runPageWidth) {
            var $runPageWidthInput = $configview.find("#runPageWidth");
            var $runUnitSel = $configview.find("#runUnit");
            if (runPageWidth.indexOf("px") != -1) {
                $runPageWidthInput.val(runPageWidth.replace("px", ""));
                $runUnitSel.val("px");
            } else if (runPageWidth.indexOf("%") != -1) {
                $runPageWidthInput.val(runPageWidth.replace("%", ""));
                $runUnitSel.val("%");
            }
            $runPageWidthInput.change(function (e) {
                var $runWidthObj = {'width': $runPageWidthInput.val() + $runUnitSel.val()};
                pageCssUtils.updateCss('.runState', $runWidthObj);
            });
            $runUnitSel.change(function (e) {
                var $runWidthObj = {'width': $runPageWidthInput.val() + $runUnitSel.val()};
                pageCssUtils.updateCss('.runState', $runWidthObj);
            });
            var editPageWidth = pageCssUtils.getCss('.editState', 'width');
            var $editPageWidthInput = $configview.find("#editPageWidth");
            var $editUnitSel = $configview.find("#editUnit");
            if (editPageWidth.indexOf("px") != -1) {
                $editPageWidthInput.val(editPageWidth.replace("px", ""));
                $editUnitSel.val("px");
            } else if (editPageWidth.indexOf("%") != -1) {
                $editPageWidthInput.val(editPageWidth.replace("%", ""));
                $editUnitSel.val("%");
            }
            $editPageWidthInput.change(function (e) {
                var $editWidthObj = {'width': $editPageWidthInput.val() + $editUnitSel.val()};
                pageCssUtils.updateCss('.editState', $editWidthObj);
                DesignerUtils.fitSubWidgets($(".demo"));
            });
            $editUnitSel.change(function (e) {
                var $editWidthObj = {'width': $editPageWidthInput.val() + $editUnitSel.val()};
                pageCssUtils.updateCss('.editState', $editWidthObj);
                DesignerUtils.fitSubWidgets($(".demo"));
            });
        }

        //页面背景颜色
        var $backgroundColorInput = $configview.find("#backgroundColor");
        var backgroundColor = pageCssUtils.getCss('.demo', 'background-color');
        $backgroundColorInput.colorpicker({color: backgroundColor});
        $backgroundColorInput.parent().width('132px');
        $backgroundColorInput.change(function (e) {
            var $backgroundColorObj = {'background-color': e.target.value};
            pageCssUtils.updateCss('.demo', $backgroundColorObj);
        });

        //生成字体库
        var defaultFontFamily = ['微软雅黑', '宋体', '楷体', '幼圆', '隶书', '黑体', 'Arial', 'Verdana', 'Times New Roman', 'Garamond', 'Comic Sans MS', 'Courier New',
            'Georgia', 'Lucida Console', 'Tahoma'];
        var $fontFamilySel = $configview.find("#fontFamily");
        for (var i = 0; i < defaultFontFamily.length; i++) {
            $fontFamilySel.append("<option value='" + defaultFontFamily[i] + "'>" + defaultFontFamily[i] + "</option>");
        }
        //获取当前字体
        var fontFamily = pageCssUtils.getCss('.demo .box .demoview,.demo .box .view', 'font-family');
        $fontFamilySel.val(fontFamily);
        $fontFamilySel.change(function (e) {
            var $familyObj = {'font-family': e.target.value};
            pageCssUtils.updateCss('.demo .box .demoview,.demo .box .view', $familyObj);
        });

        //获取当前字型
        var $fontStyleSel = $configview.find("#fontStyle");
        var fontStyle = pageCssUtils.getCss('.demo .box .demoview,.demo .box .view', 'font-style');
        $fontStyleSel.val(fontStyle);
        $fontStyleSel.change(function (e) {
            var $styleObj = {'font-style': e.target.value};
            pageCssUtils.updateCss('.demo .box .demoview,.demo .box .view', $styleObj);
        });
        //字体颜色
        /*var $fontColorInput = $configview.find("#fontColor");
         var fontColor=pageCssUtils.getCss('.demo .box','color');
         $fontColorInput.colorpicker({color:fontColor});
         $fontColorInput.parent().width('132px');
         $fontColorInput.change(function (e) {
         var $colorObj={'color':e.target.value};
         pageCssUtils.updateCss('.demo .box',$colorObj);
         });*/
        //生成字号库
        var defaultFontSize = ['10px', '12px', '14px', '16px', '18px', '20px', '22px', '24px', '26px', '28px', '36px', '48px', '72px'];
        var $fontSizeSel = $configview.find("#fontSize");
        for (var m = 0; m < defaultFontSize.length; m++) {
            $fontSizeSel.append("<option value='" + defaultFontSize[m] + "'>" + defaultFontSize[m] + "</option>");
        }
        //获取当前字号
        var fontSize = pageCssUtils.getCss('.demo label', 'font-size');
        $fontSizeSel.val(fontSize);
        $fontSizeSel.change(function (e) {
            var $sizeObj = {'font-size': e.target.value};
            pageCssUtils.updateCss('.demo label', $sizeObj);
        });

        //获取当前标签字粗细
        var $fontWeightSel = $configview.find("#fontWeight");
        var fontWeight = pageCssUtils.getCss('.demo label', 'font-weight');
        $fontWeightSel.val(fontWeight);
        $fontWeightSel.change(function (e) {
            var $weightObj = {'font-weight': e.target.value};
            pageCssUtils.updateCss('.demo label', $weightObj);
        });

        //背景图片相关
        $configview.find(".treeInput_arround").remove();
        var imgObj = DesignerUtils.getMediaList("picture");
        $configview.find(".treeInput").each(function () {
            treeInputUtils.createTreeInput($(this), imgObj);
        });

        var bgurl = bgurlReg.exec(pageCssUtils.getCss('.demo', 'background-image'));
        bgurl = bgurl && bgurl[1];
        $configview.find("#backgroundImg").val(bgurl)
            .change(function (e) {
                pageCssUtils.updateCss('.demo', {'background-image': "url(" + e.target.value + ")"});
            });
        $configview.find("#imgPosition").val(pageCssUtils.getCss('.demo', 'background-position'))
            .change(function (e) {
                pageCssUtils.updateCss('.demo', {'background-position': e.target.value});
            });
        $configview.find("#imgRepeat").val(pageCssUtils.getCss('.demo', 'background-repeat'))
            .change(function (e) {
                pageCssUtils.updateCss('.demo', {'background-repeat': e.target.value});
            });
    };
    var showColumnConfigView = function (target, $dest) {
        var $configview = ContainerLoader.getConfigView();
        $dest.empty();
        $dest.append($configview);
        bindColumnConfigViewAction($(target), $configview);
    };

    var bindColumnConfigViewAction = function ($target, $configview) {

        var $titleInput = $configview.find("#title");
        var $titleObject = $target.children(".column-title");

        $titleInput.val($titleObject.text());
        $titleInput.change(function (e) {
            $titleObject.text(e.target.value);
            if (e.target.value === "") {
                $titleObject.addClass("hidden");
            } else {
                if ($titleObject.hasClass("hidden")) {
                    $titleObject.removeClass("hidden");
                }
            }
        });

        var $paddingTopInput = $configview.find("#paddingTop");
        var paddingTop=$target.css("padding-top");
        if (paddingTop.indexOf("px") != -1) {
            $paddingTopInput.val(paddingTop.replace("px", ""));
        }
        $paddingTopInput.change(function (e) {
            $target.css("padding-top", e.target.value+"px");
            DesignerUtils.fitSubWidgets($target);
        });

        var $paddingLeftInput = $configview.find("#paddingLeft");
        var paddingLeft=$target.css("padding-left");
        if (paddingLeft.indexOf("px") != -1) {
            $paddingLeftInput.val(paddingLeft.replace("px", ""));
        }
        $paddingLeftInput.change(function (e) {
            $target.css("padding-left", e.target.value+"px");
            DesignerUtils.fitSubWidgets($target);
        });

        var $paddingBottomInput = $configview.find("#paddingBottom");
        var paddingBottom=$target.css("padding-bottom");
        if (paddingBottom.indexOf("px") != -1) {
            $paddingBottomInput.val(paddingBottom.replace("px", ""));
        }
        $paddingBottomInput.change(function (e) {
            $target.css("padding-bottom", e.target.value+"px");
            DesignerUtils.fitSubWidgets($target);
        });

        var $paddingRightInput = $configview.find("#paddingRight");
        var paddingRight=$target.css("padding-right");
        if (paddingRight.indexOf("px") != -1) {
            $paddingRightInput.val(paddingRight.replace("px", ""));
        }
        $paddingRightInput.change(function (e) {
            $target.css("padding-right", e.target.value+"px");
            DesignerUtils.fitSubWidgets($target);
        });


        /*列配高度
        var $colHeightInput = $configview.find("#colHeight");
        var colHeight=$target.css("height");
        if (colHeight.indexOf("px") != -1) {
            $colHeightInput.val(colHeight.replace("px", ""));
        }
        $colHeightInput.change(function (e) {
            if(e.target.value<82){
                if (last_gritter) $.gritter.remove(last_gritter);
                last_gritter = $.gritter.add({
                    title: '提示！',
                    text: '列高度最小为82px',
                    class_name: 'gritter-item-wrapper gritter-error'
                });
                $colHeightInput.val('82');
            }
            $target.css("height", e.target.value+"px");
            DesignerUtils.fitSubWidgets($target);
        });*/

        var $backgroundColorInput = $configview.find("#backgroundColor");
        var $backgroundColor = $target.css("background-color");
        $backgroundColor = getHexColor($backgroundColor);
        $backgroundColorInput.colorpicker({color: $backgroundColor});
        $backgroundColorInput.parent().width('132px');
        $backgroundColorInput.change(function (e) {
            $target.css("background-color", e.target.value);
            $target.children("legend").css("border-top-color", e.target.value);
        });

        var $styleInput = $configview.find("#showStyle");
        var options = $styleInput.find("option").map(
            function () {
                return $(this).val();
            }
        ).get();
        var currentClass = "";
        for (var index in options) {
            var val = options[index];
            if ($target.hasClass(val)) {
                $styleInput.val(val);
                currentClass = val;
                break;
            }
        }
        $styleInput.change(function (event) {
            var currentClass = "";
            for (var index in options) {
                var val = options[index];
                if ($target.hasClass(val)) {
                    currentClass = val;
                    break;
                }
            }
            $target.removeClass(currentClass);
            $target.addClass(event.target.value);
        });

        //var $paddingInput = $configview.find("#showPadding");
        //$paddingInput.val(elementProperties.margin.getValue($target));
        //$paddingInput.change(function (e) {
        //    if (!isNaN(parseInt(e.target.value))) {
        //        //$target.css("margin", e + "px 0 0 0");
        //        elementProperties.margin.setValue($target, e.target.value);
        //    }
        //});

        //背景图片相关
        $configview.find(".treeInput_arround").remove();
        var imgObj = DesignerUtils.getMediaList("picture");
        $configview.find(".treeInput").each(function () {
            treeInputUtils.createTreeInput($(this), imgObj);
        });

        var bgurl = bgurlReg.exec($target.css("background-image"));
        bgurl = bgurl && bgurl[1];
        $configview.find("#backgroundImg").val(bgurl)
            .change(function (e) {
                var url = e.target.value.trim() && "url(" + e.target.value + ")";
                $target.css("background-image", url);
            });

        var position = $target.css("background-position");
        if(!positionReg.test(position)){
            position = "50% 0%";
            $target.css("background-position", position);
        }
        $configview.find("#imgPosition").val(position)
            .change(function (e) {
                $target.css("background-position", e.target.value);
            });

        var repeat = $target.css("background-repeat");
        if(!repeatReg.test(repeat)){
            repeat = "repeat";
            $target.css("background-repeat", repeat);
        }
        $configview.find("#imgRepeat").val(repeat)
            .change(function (e) {
                $target.css("background-repeat", e.target.value);
            });

    };

    var showDialog = function (success, cancel) {
        var $dialog = ContainerLoader.getDialogView();
        isSuccess = false;
        //$("body").append($dialog);
        //$dialog.hide();
        var title = "自定义布局";
        var dialogOpt = {
            title: title,
            width: "450px"
        };
        $dialog.bxdialog(dialogOpt);
        bindDialogAction($dialog, success);
        $dialog.on("dialogclose", function (event, ui) {
            if (!isSuccess && cancel != undefined) {
                cancel();
            }
            isSuccess = false;
            $dialog.bxdialog("destroy");
            $dialog.remove();
        });
    };

    var showWarning = function ($dialog, message) {
        var $message = $('<span class="label label-warning">' + message + '</span>');
        var $div = $('<div class="dialog-warning-message"></div>');
        $div.append($message);
        $dialog.append($div);
        //$($dialog.find(".row-fluid")[1]).show();
    };

    var bindDialogAction = function ($dialog, success) {
        var $input = $dialog.find("input");
        var $button = $dialog.find("button");
        $button.click(function (event) {
            var e = 0;
            var t = "";
            var n = $input.val().split(" ", 12);
            $.each(n, function (n, r) {
                var x = parseInt(r);
                if (isNaN(x)) {
                    return;
                }
                e = e + x;
                t += '<div class="span' + r + ' column"></div>';
            });
            if (e == 12) {
                $dialog.find(".label").parent().parent().hide();
                if (success != undefined)
                    success(t);
                isSuccess = true;
                $dialog.bxdialog("close");
            } else {
                showWarning($dialog, "数字总和必须等于12");
            }
        });
    };

    return {
        showConfigView: showConfigView,
        showColumnConfigView: showColumnConfigView,
        showDialog: showDialog
    }
})();

var ContainerLoader = (function () {
    var $configview;
    var $columnConfigview;
    var $dialogview;

    var StyleList = {
        "theme-default": "无",
        "theme-border": "外边框",
        "theme-fieldset": "fieldset",
        "theme-bootstrap": "bootstrap",
    };

    var load = function () {
        var paths = {
            html: "container/container.html",
            columnHtml: "container/column.html"
        };
        ContentLoader.loadHtml(paths.html).done(function (data) {
            var $div = $(data);
            $columnConfigview = $div.find(".configView");
        });
        ContentLoader.loadHtml(paths.columnHtml).done(function (data) {
            var $div = $(data);
            $configview = $div.find(".configView");
            var styleInput = $configview.find("#showStyle");
            for (var value in StyleList) {
                styleInput.append($("<option value='" + value + "'>" + StyleList[value] + "</option>"));
            }
            $dialogview = $div.find(".custom-container-dialog");
        });
    };

    var getConfigView = function () {
        var $clone = $configview.clone(true);
        return $clone;
    };

    var getColumnConfigView = function () {
        var $clone = $columnConfigview.clone(true);
        return $clone;
    };

    var getDialogView = function () {
        var $clone = $dialogview.clone(true);
        return $clone;
    }

    return {
        load: load,
        getConfigView: getConfigView,
        getColumnConfigView: getColumnConfigView,
        getDialogView: getDialogView
    }
})();