(function (WidgetObj, type) {
    function IFrame() {
    }
    var actions = {
        "type": "iframe",
        "actions": [
        ]
    };

    IFrame.prototype = new WidgetObj(type, actions);

    IFrame.prototype.initOption = function () {
        var option = {
            src: "",
            option: {

            }
        };
        this.setOptions(option);
    };

    IFrame.prototype.showPreviewCore = function () {
        var id = this.getId();
        $("#" + id).attr("src", this.getRealSrc(this.getOption("src")));
        $("#" + id).outerHeight(this.getOption("height"));
        /*var option = this.getOptions()["option"];
        for (key in option) {
            if(key=="nonePadding"&&option[key])
                $("#" + id).parent().parent().parent().attr("style","padding:0px");
            else
                $("#" + id).parent().parent().parent().attr("style","");
        }*/
    };

    IFrame.prototype.showCore = function () {
        var id = this.getId();
        $("#" + id).attr("src", this.getRealSrc(this.getOption("src")));
        $("#" + id).attr("height", this.getOption("height"));
        var option = this.getOptions()["option"];
        for (key in option) {
            if(key=="nonePadding"&&option[key]){
                $("#" + id).parent().parent().parent().parent().attr("style","padding:0px");
                $("#" + id).attr("style","border:0;width: 100%;");
            }
        }

    };
    
    IFrame.prototype.getRealSrc = function(){
    	var realPath = "";
    	var pageEname = this.getOption("src");
    	var callback = {
						onSuccess : function(paramJsonObj) {
							var pageType = paramJsonObj.pageType;
							if("externLink" == pageType){
								realPath = paramJsonObj.pagePath;
							}else if("localLink" == pageType || "designPage" == pageType){
								realPath = toolkitPath + paramJsonObj.pagePath;
							}
						}
					};
    	var paramJsonObj = {};
    	paramJsonObj.pageEname = pageEname;
		AjaxCommunicator.ajaxRequest('/df/designer/iframe.do?method=queryPage','POST', paramJsonObj, callback,false);
		return realPath;
    };

    IFrame.prototype.getConfigViewCore = function ($configView) {
        var $this = this;
        var opt = this.getOptions();
        var inputOpt = $this.getOptions()["option"];
        $this.getConfigViewsControl("widgetId", $configView).text($this.getId());

        var attributeList = $this.getConfigViewsControl("attribute", $configView).find("[data-attribute]");
        $.each(attributeList, function (index, item) {
            $this.setAttributeFromOption(inputOpt,$(this));
            $(this).change(function () {
                $this.setOptionValue(inputOpt,$(this));
                $this.setOption("option", inputOpt);
                $this.refresh();
            });
        });

        //$this.setWidgetSize($configView);
        var heightArribute = $this.getConfigViewsControl("height", $configView);
        heightArribute.val($("#" + $this.getId()).outerHeight());
        heightArribute.change(function () {
            opt.height = $(this).val();
            $this.setOptions(opt);
            $this.refresh();
        });

        var $srcInput = $this.getConfigViewsControl("srcInput",$configView);
        $srcInput.val($this.getOptions()["src"]);
        $srcInput.change(function () {
            $this.setOption("src", $srcInput.val());
            $this.refresh();
        });


    };

    IFrame.prototype.getDataConfigViewCore = function ($dataConfigView) {

    };

    IFrame.prototype.bindCore = function(eventname, callback) {
        if(eventname!="Init")
            $("#" + this.getId()).attr(eventname, callback.name + '();');
    };

    Widget.registerType(type, IFrame);
})(WidgetObject, "iframe");