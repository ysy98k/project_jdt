(function (WidgetObj, type) {
    function Input() { // html节点的操作对象的定位
    }

    var actions = {
        "type": "input",
        "actions": [
            {
                "name": "OnFocus",
                "params": "()",
                "tooltip": "获得焦点事件"
            }, {
                "name": "OnKeyDown",
                "params": "()",
                "tooltip": "在输入框中按下键盘时触发"
            }
        ]
    };

    Input.prototype = new BxWidgetCommon(type, actions);

    Input.prototype.initOption = function () {
       var option = {
            labelText: "标签",
            inputDsSetting:"",
            option: {

            }
        };
        this.setOptions(option);
    };

    Input.prototype.getValue = function (id) {
        return $("#" + this.getId()).val();
    };

    function showInput(widget, id) {
        var $input = $("#" + id);
        /*$input.width(widget.getOptions()["inputWidth"]);*/
        var $label = $("#" + id).parent().parent().find("label");;
        $label.width(widget.getOptions()["labelWidth"]);
        $label.text(widget.getOptions()["labelText"]);
        var option = widget.getOptions()["option"];
        for (var key in option) {
            $input.attr(key, option[key]);
        }
    }

    Input.prototype.showPreviewCore = function () {
    	showInput(this, this.getPreviewId());
    };

    Input.prototype.showCore = function () {
        showInput(this, this.getId());
        if(this.getOptions()["inputDsSetting"] != ""){
            var $input = $("#" + this.getId());
            $input.val(DesignerUtils.queryViaComboTree(this.getOptions()["inputDsSetting"]));
        }
    };

    Input.prototype.getConfigViewCore = function($configView) {
    	var $this = this;
        /*var bxinput = $("#"+$this.getId()).find("input");*/
        var bxlabel = $("#"+$this.getId()).parent().parent().find("label");
        var inputOpt = $this.getOptions()["option"];
        $this.getConfigViewsControl("inputId", $configView).text($this.getId());
        /*设置标签文字*/
        var labelText = $this.getConfigViewsControl("labelText", $configView);
        labelText.val($this.getOptions()["labelText"]);
        labelText.change(function () { 
        	$this.getOptions()["labelText"] = labelText.val();
        	$this.refresh();
            $this.getConfigViewsControl("labelWidth", $configView).val(bxlabel.width());
        });
        /*设置标签宽度*/
        var labelWidth = $this.getConfigViewsControl("labelWidth", $configView);
        labelWidth.val(bxlabel.width());
        labelWidth.change(function (){
             $this.getOptions().labelWidth = $(this).val();
             $this.refresh();    
	    });
        /*var inputWidth = $this.getConfigViewsControl("inputWidth", $configView);
        inputWidth.val(bxinput.width());
        inputWidth.change(function (){
            $this.getOptions().inputWidth = $(this).val();
            $this.refresh();
        });*/
        var attributeList = $this.getConfigViewsControl("attribute", $configView).find("[data-attribute]");                   
        $.each(attributeList, function (index, item) {  
        	$this.setAttributeFromOption(inputOpt,$(this));
            $(this).change(function () {              	        	
	        	$this.setOptionValue(inputOpt,$(this));	        	
                $this.setOption("option", inputOpt);
                $this.refresh();
            });
        });  

    };
    
    Input.prototype.getDataConfigViewCore = function($dataConfigView) {
        var input = this;
        $dataConfigView.find(".treeInput_arround").remove();
        input.initCombTrees($dataConfigView);
        var inputDatasource = input.getConfigViewsControl("inputDsSetting", $dataConfigView);
        inputDatasource.change(function(){
            input.dataSourceExecute($dataConfigView);
            //input.refresh();
        });
        inputDatasource.val(input.getOptions()["inputDsSetting"]);
    };

    Input.prototype.bindCore= function(eventname, callback){
        if(eventname!="Init")
            $('#'+this.getId()).attr(eventname, callback.name+'();');
    };

    Input.prototype.dataSourceExecute = function ($dataConfigView) {
        var input = this;
        var $input = $("#" + this.getId());
        var inputDatasource = input.getConfigViewsControl("inputDsSetting", $dataConfigView).val();
        if(! DesignerUtils.checkComboTreeValue(inputDatasource)) inputDatasource = "";
        input.getOptions()["inputDsSetting"] = inputDatasource;
        $input.val(DesignerUtils.queryViaComboTree(input.getOptions()["inputDsSetting"]));
    };

    Widget.registerType(type, Input);
})(WidgetObject, "input");