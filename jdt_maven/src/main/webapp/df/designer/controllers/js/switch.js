(function(WidgetObj, type) {
    function Switch() { // html节点的操作对象的定位
    }

    var actions = {
        
    };

    function show(widget, id) {
    	var controlObj = $("#" + id);
    	var $label = controlObj.parent().parent().find("label[name='widgetLabel']");
        $label.text(widget.getOptions()["labelText"]);
        $label.width(widget.getOptions()["labelWidth"]);
        var option = widget.getOptions()["option"];
        for (key in option) {
            controlObj.prop(key, option[key]);
        }
    }

    Switch.prototype = new WidgetObj(type, actions);

    Switch.prototype.initOption = function() {
        var option = {   
            labelText: '标签',
            option: {

            }
        };
        this.setOptions(option);
    };

    Switch.prototype.getValue = function (id) {
        return $("#" + this.getId()).get(0).checked;
    };

    Switch.prototype.showPreviewCore = function() {
        show(this, this.getPreviewId());
    };

    Switch.prototype.showCore = function() {
        show(this, this.getId());
    };

    Switch.prototype.getConfigViewCore = function($configView) {       
        var $this = this;
        var bxlabel = $("#"+$this.getId()).parent().parent().find("label:first-child");
        var bxswitch = $("#"+$this.getId()).parent().parent().find("input");
        var inputOpt = $this.getOptions()["option"];
        $this.getConfigViewsControl("inputId", $configView).text($this.getId());       
        /*设置input宽度*/
        var widthArribute = $this.getConfigViewsControl("width", $configView);		     
		widthArribute.val(bxswitch.width());
	    widthArribute.change(function (){
             $this.getOptions().width = $(this).val();
             $this.refresh();    
	    });

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
    
    Switch.prototype.getDataConfigViewCore = function($dataConfigView) {

    };
    
    Switch.prototype.bindCore = function(eventname, callback) {
        
    };
    Widget.registerType(type, Switch);
})(WidgetObject, "switch");