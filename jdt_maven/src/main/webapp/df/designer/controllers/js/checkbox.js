(function(WidgetObj, type) {
    function Checkbox() { // html节点的操作对象的定位
    }

    var actions = {
       
    };

    function showCheckbox(widget, id) {
        var controlObj = $("#" + id);
        controlObj.empty();
        controlObj.append('<label style="display: inline-block"></label>');
        var $label = controlObj.find("label");
        $label.text(widget.getOptions()["labelText"]);
        $label.width(widget.getOptions()["labelWidth"]);
        var optionList = widget.getOptions()["option"]["optionList"];
        var name = widget.getOptions()["name"];
        for (var i = 0; i < optionList.length; i++) {
            controlObj.append('<div class="checkbox"><label> <input name="' + name + '" type="checkbox" class="ace" value='+optionList[i].value+'/> <span class="lbl">' + optionList[i].name + '</span></label></div>');
        }
    }

    Checkbox.prototype = new BxWidgetCommon(type, actions);

    Checkbox.prototype.initOption = function() {
        var checkbox = this;
        var optionSubId = "checkbox" + DesignerUtils.randomNumber();
        var optionId = checkbox.getId() +"$$" + optionSubId;
        var option = {
    		labelText: '标签',
    		name: checkbox.getId(),
            option: {
                "labelText" : "默认文本",
                "optionList" : [{'id':optionId,'name':'选项1','value':'val1','subId':optionSubId}]
            }
        };
        this.setOptions(option);
        this.showPreviewCore();
    };

    Checkbox.prototype.getValue = function (id) {
        var checklist=$("input[name='"+this.getId()+"']:checked").next();
        var selected = "";
        $.each(checklist, function (index, item) {
            selected += item.innerText + ",";
        });
        selected=selected.substr(0, selected.length - 1);
        return selected;
    };

    Checkbox.prototype.showPreviewCore = function() {
        showCheckbox(this, this.getPreviewId());
    };

    Checkbox.prototype.showCore = function() {
        showCheckbox(this, this.getId());
    };

    Checkbox.prototype.getConfigViewCore = function($configView) {       
        var checkbox = this;
        var bxlabel = $("#"+checkbox.getId()).children('label');
        checkbox.getConfigViewsControl("checkboxId", $configView).text(checkbox.getId());
        /*设置标签文字*/
        var labelText = checkbox.getConfigViewsControl("labelText", $configView);
        labelText.val(checkbox.getOptions()["labelText"]);
        labelText.change(function () { 
        	checkbox.getOptions()["labelText"] = labelText.val();
        	checkbox.refresh();
            checkbox.getConfigViewsControl("labelWidth", $configView).val($("#"+checkbox.getId()).children('label').width());
        });
        /*设置标签宽度*/
        var labelWidth = checkbox.getConfigViewsControl("labelWidth", $configView);
        labelWidth.val($("#"+checkbox.getId()).children('label').width());
        labelWidth.change(function (){
            checkbox.getOptions().labelWidth = $(this).val();
            checkbox.refresh();
        });
        /*设置name属性值*/
        /*var name = checkbox.getConfigViewsControl("name", $configView);
        name.val(checkbox.getOptions()["name"]);
        name.change(function () { 
        	checkbox.getOptions()["name"] = name.val();
        	checkbox.refresh();
        });*/
        /*设置选项*/
        checkbox.initOptionBtn($configView);
        checkbox.getConfigViewsControl("addDataBtn", $configView).unbind().bind("click",function(){           
            checkbox.addElement($configView);           
            checkbox.refresh();
        });        
    };
    
    Checkbox.prototype.getDataConfigViewCore = function($dataConfigView) {

    };
    
    Checkbox.prototype.initOptionBtn=function($configView){//初始化增加值的按钮区域
    	var checkbox = this;
    	var optionList = checkbox.getOptions()["option"]["optionList"];
    	var addDataBtn = checkbox.getConfigViewsControl("addDataBtn", $configView).parent();   	
    	var optionListArray = [];
    	for (var i = 0; i < optionList.length; i++) { 
    		 optionListArray.push(optionList[i]);
    		 checkbox.generateOptionBtn($configView,optionList[i]);  		
    	}
    	checkbox.getConfigViewsControl("optionList", $configView).val(JSON.stringify(optionListArray));
    };
    
   
    Checkbox.prototype.generateOptionBtn=function($configView,generateObj){//封装生成按钮的代码
    	 var checkbox = this;
    	 var addDataBtn = checkbox.getConfigViewsControl("addDataBtn", $configView).parent(); 
    	 var checkboxOptButton ="<div data-attribute='checkboxOption' class='col-sm-12 btn btn-xs btn-yellow' id='"+generateObj.id+"' style='margin:0 15px 5px 15px;width: 86.9%'><span>" + generateObj.name+':'+generateObj.value  + "</span><i class='ace-icon fa fa-times pull-right'data-dismiss='alert'style='margin-top:3px'></i></div>"
	     addDataBtn.before(checkboxOptButton);
		 checkbox.bindOptionBtnDeleteClick(generateObj.subId,$configView);	
		 checkbox.bindOptionBtnClick(generateObj.subId,$configView);
    };
    
    Checkbox.prototype.bindOptionBtnDeleteClick=function(optionSubId,$configView){//封装点击按钮删除的代码
    	var checkbox = this;
    	checkbox.getConfigViewsControl(optionSubId, $configView).children("i").bind("click",function(){// 绑定删除选项按钮事件
            var optionListArray=JSON.parse(checkbox.getConfigViewsControl("optionList", $configView).val());
            checkbox.getConfigViewsControl(optionSubId, $configView).unbind();
            checkbox.deleteElement(optionSubId,optionListArray,$configView);         
        });
    };
    
    Checkbox.prototype.bindOptionBtnClick=function(optionSubId,$configView){//封装点击按钮的代码
    	var checkbox = this;
    	checkbox.getConfigViewsControl(optionSubId, $configView).unbind().bind("click",function(){// 绑定点击按钮事件  
    		var button = $(this);
            var optionListArray=JSON.parse(checkbox.getConfigViewsControl("optionList", $configView).val());
            var opIndex = checkbox.functiontofindIndexByKeyValue(optionListArray,'id',checkbox.getId() + "$$" + optionSubId);
            var clickOpt = optionListArray[opIndex];
            checkbox.getConfigViewsControl("optionName",$configView).val(clickOpt.name);
            checkbox.getConfigViewsControl("optionName",$configView).unbind().change(function () {
            	optionListArray=JSON.parse(checkbox.getConfigViewsControl("optionList", $configView).val());
            	opIndex = checkbox.functiontofindIndexByKeyValue(optionListArray,'id',checkbox.getId() + "$$" + optionSubId);
	        	optionListArray[opIndex].name = $(this).val();
                button.find(">span").text(optionListArray[opIndex].name + ":" + optionListArray[opIndex].value);
                checkbox.getConfigViewsControl("optionList", $configView).val(JSON.stringify(optionListArray));
	        	checkbox.getOptions()["option"]["optionList"] = optionListArray;
                checkbox.refresh();
	        });
            checkbox.getConfigViewsControl("optionVal",$configView).val(clickOpt.value);
            checkbox.getConfigViewsControl("optionVal",$configView).unbind().change(function () {
                optionListArray=JSON.parse(checkbox.getConfigViewsControl("optionList", $configView).val());
                opIndex = checkbox.functiontofindIndexByKeyValue(optionListArray,'id',checkbox.getId() + "$$" + optionSubId);
                optionListArray[opIndex].value = $(this).val();
                button.find(">span").text(optionListArray[opIndex].name + ":" + optionListArray[opIndex].value);
                checkbox.getConfigViewsControl("optionList", $configView).val(JSON.stringify(optionListArray));
                checkbox.getOptions()["option"]["optionList"] = optionListArray;
                checkbox.refresh();
            });
            checkbox.getConfigViewsControl("optionAttribute",$configView).show();
        });
    };
    
    Checkbox.prototype.addElement=function($configView){// 增加元素
    	var checkbox = this;
    	var optionSubId = "checkbox" + DesignerUtils.randomNumber();
    	var optionId = checkbox.getId() + "$$" + optionSubId;
    	var optionListArray = JSON.parse(checkbox.getConfigViewsControl("optionList", $configView).val());
    	var generateObj = {
    		'id': optionId,
            'value':"val",
            'name':"选项",
    		'subId': optionSubId
    	}; 
        checkbox.generateOptionBtn($configView,generateObj);
        optionListArray.push(generateObj);
        checkbox.getConfigViewsControl("optionList", $configView).val(JSON.stringify(optionListArray));
        checkbox.getOptions()["option"]["optionList"] = optionListArray;
        checkbox.refresh();
        checkbox.getConfigViewsControl(generateObj.subId, $configView).trigger("click");
    };
    
    
    Checkbox.prototype.deleteElement=function(optionSubId,optionListArray,$configView){//删除元素
    	var checkbox = this;
    	var deleteIndex = checkbox.functiontofindIndexByKeyValue(optionListArray,'id',checkbox.getId() + "$$" + optionSubId);
    	optionListArray.splice(deleteIndex,1);
        checkbox.getConfigViewsControl("optionList", $configView).val(JSON.stringify(optionListArray));
        checkbox.getOptions()["option"]["optionList"] = optionListArray;
        checkbox.getConfigViewsControl("optionAttribute",$configView).hide();
        checkbox.refresh();
    };
    
    Checkbox.prototype.bindCore = function(eventname, callback) {
        
    };
    Widget.registerType(type, Checkbox);
})(WidgetObject, "checkbox");