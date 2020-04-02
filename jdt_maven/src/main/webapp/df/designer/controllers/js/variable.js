(function (WidgetObj, type) {
    function Variable() { // html节点的操作对象的定位
    }

    var actions = {
        
    };

    function show(widget, id) {
        var $Variable = $("#" + id);
        if(isAvailable(widget.getOption("name"))){
        	$Variable.text(widget.getOption("name") + "："+id);
        }else{
        	$Variable.text(id);
        }      
    }
    
    function hide(widget, id) {
        var $Variable = $("#" + id);
        $Variable.parent().parent().hide();
    }

    Variable.prototype = new WidgetObj(type, actions);

    Variable.prototype.initOption = function () {
        var option = {
            value : '',
            label : '',
            name : ''
        };
        this.setOptions(option);
        this.showPreviewCore();
    };

    Variable.prototype.showPreviewCore = function () {
        show(this, this.getPreviewId());
    };

    Variable.prototype.showCore = function () {
        hide(this, this.getId());
    };

    Variable.prototype.getConfigViewCore = function ($configView) {
        var $this = this;
        $this.getConfigViewsControl("variableId", $configView).text($this.getId());
        var nameArribute = $this.getConfigViewsControl("variableName", $configView);
        nameArribute.val($this.getOption("name"));
        nameArribute.change(function () {
            $this.setOption("name",$(this).val());
            $this.refresh();
        });
    };

    Variable.prototype.getDataConfigViewCore = function ($dataConfigView) {

    };

    Variable.prototype.bindCore = function (eventname, callback) {
        
    };
    Widget.registerType(type, Variable);
})(WidgetObject, "variable");