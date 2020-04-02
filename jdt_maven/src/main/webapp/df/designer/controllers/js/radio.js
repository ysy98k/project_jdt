(function(WidgetObj, type) {
    function Radio() { // html节点的操作对象的定位
    }

    var actions = {
        
    };

    function showRadio(widget, id) {
        var controlObj = $("#" + id);
        controlObj.empty();
        controlObj.append('<label style="display: inline-block"></label>');
        var $label = controlObj.find("label");
        $label.text(widget.getOptions()["labelText"]);
        $label.width(widget.getOptions()["labelWidth"]);
        var optionList = widget.getOptions()["option"]["optionList"];
        var name = widget.getOptions()["name"];
        for (var i = 0; i < optionList.length; i++) {
            controlObj.append('<div class="radio"><label> <input name="' + name + '" type="radio" class="ace" value='+optionList[i].value+'/> <span class="lbl">' + optionList[i].name + '</span></label></div>');
        }
    }

    Radio.prototype = new BxWidgetCommon(type, actions);

    Radio.prototype.initOption = function() {
       var radio = this;
       var optionSubId = "option" + DesignerUtils.randomNumber();
       var optionId = radio.getId() +"$$" + optionSubId;
       var option = {
    		labelText: '标签',
    		name: radio.getId(),
            option: {
                "labelText" : "默认文本",
                "optionList" : [{'id':optionId,'name':'选项1','value':'val1','subId':optionSubId}]
            }
        };
        this.setOptions(option);
        this.showPreviewCore();
    };

    Radio.prototype.getValue = function (id) {
        return $("input[name='"+this.getId()+"']:checked").next().text();
    };

    Radio.prototype.showPreviewCore = function() {
        showRadio(this, this.getPreviewId());
    };

    Radio.prototype.showCore = function() {
        showRadio(this, this.getId());
    };

    Radio.prototype.getConfigViewCore = function($configView) {       
        var radio = this;
        var bxlabel = $("#"+radio.getId()).children('label');
        radio.getConfigViewsControl("radioId", $configView).text(radio.getId());
        /*设置标签文字*/
        var labelText = radio.getConfigViewsControl("labelText", $configView);
        labelText.val(radio.getOptions()["labelText"]);
        labelText.change(function () { 
        	radio.getOptions()["labelText"] = labelText.val();
        	radio.refresh();
            radio.getConfigViewsControl("labelWidth", $configView).val($("#"+radio.getId()).children('label').width());
        });
        /*设置标签宽度*/
        var labelWidth = radio.getConfigViewsControl("labelWidth", $configView);
        labelWidth.val($("#"+radio.getId()).children('label').width());
        labelWidth.change(function (){
            radio.getOptions().labelWidth = $(this).val();
            radio.refresh();
        });
        /*设置name属性值*/
        /*var name = radio.getConfigViewsControl("name", $configView);
        name.val(radio.getOptions()["name"]);
        name.change(function () { 
        	radio.getOptions()["name"] = name.val();
        	radio.refresh();
        });*/
        /*设置选项*/
        radio.initOptionBtn($configView);
        radio.getConfigViewsControl("addDataBtn", $configView).unbind().bind("click",function(){           
            radio.addElement($configView);           
            radio.refresh();
        });        
    };
    
    Radio.prototype.getDataConfigViewCore = function($dataConfigView) {

    };
    
    Radio.prototype.initOptionBtn=function($configView){//初始化增加值的按钮区域
    	var radio = this;
    	var optionList = radio.getOptions()["option"]["optionList"];
    	var addDataBtn = radio.getConfigViewsControl("addDataBtn", $configView).parent();   	
    	var optionListArray = [];
    	for (var i = 0; i < optionList.length; i++) { 
    		 optionListArray.push(optionList[i]);
    		 radio.generateOptionBtn($configView,optionList[i]);  		
    	}
    	radio.getConfigViewsControl("optionList", $configView).val(JSON.stringify(optionListArray));
    };
    
   
    Radio.prototype.generateOptionBtn=function($configView,generateObj){//封装生成按钮的代码
    	 var radio = this;
    	 var addDataBtn = radio.getConfigViewsControl("addDataBtn", $configView).parent(); 
    	 var radioOptButton ="<div data-attribute='radioOption' class='col-sm-12 btn btn-xs btn-yellow' id='"+generateObj.id+"' style='margin:0 15px 5px 15px;width: 86.9%'><span>" + generateObj.name+':'+generateObj.value + "</span><i class='ace-icon fa fa-times pull-right'data-dismiss='alert'style='margin-top:3px'></i></div>"
	     addDataBtn.before(radioOptButton);
		 radio.bindOptionBtnDeleteClick(generateObj.subId,$configView);	
		 radio.bindOptionBtnClick(generateObj.subId,$configView);
    };
    
    Radio.prototype.bindOptionBtnDeleteClick=function(optionSubId,$configView){//封装点击按钮删除的代码
    	var radio = this;
    	radio.getConfigViewsControl(optionSubId, $configView).children("i").bind("click",function(){// 绑定删除选项按钮事件
            var optionListArray=JSON.parse(radio.getConfigViewsControl("optionList", $configView).val());
            radio.getConfigViewsControl(optionSubId, $configView).unbind();
            radio.deleteElement(optionSubId,optionListArray,$configView);         
        });
    };
    
    Radio.prototype.bindOptionBtnClick=function(optionSubId,$configView){//封装点击按钮的代码
    	var radio = this;
    	radio.getConfigViewsControl(optionSubId, $configView).unbind().bind("click",function(){// 绑定点击按钮事件   	
    		var button = $(this);
            var optionListArray=JSON.parse(radio.getConfigViewsControl("optionList", $configView).val());
            var index = radio.functiontofindIndexByKeyValue(optionListArray,'id',radio.getId() + "$$" + optionSubId);
            var clickOpt = optionListArray[index];
            radio.getConfigViewsControl("optionName",$configView).val(clickOpt.name);
            radio.getConfigViewsControl("optionName",$configView).unbind().change(function () {
	        	optionListArray[index].name = $(this).val();
                button.find(">span").text(optionListArray[index].name + ":" + optionListArray[index].value);
	        	radio.getConfigViewsControl("optionList", $configView).val(JSON.stringify(optionListArray));
	        	radio.getOptions()["option"]["optionList"] = optionListArray;
                radio.refresh();
	        });
            radio.getConfigViewsControl("optionVal",$configView).val(clickOpt.value);
            radio.getConfigViewsControl("optionVal",$configView).unbind().change(function () {
                optionListArray[index].value = $(this).val();
                button.find(">span").text(optionListArray[index].name + ":" + optionListArray[index].value);
                radio.getConfigViewsControl("optionList", $configView).val(JSON.stringify(optionListArray));
                radio.getOptions()["option"]["optionList"] = optionListArray;
                radio.refresh();
            });
            radio.getConfigViewsControl("optionAttribute",$configView).show();
        });
    };
    
    Radio.prototype.addElement=function($configView){// 增加元素
    	var radio = this;
    	var optionSubId = "option" + DesignerUtils.randomNumber();
    	var optionId = radio.getId() + "$$" + optionSubId;
    	var optionListArray = JSON.parse(radio.getConfigViewsControl("optionList", $configView).val());
    	var generateObj = {
    		'id': optionId,
    		'value':"val",
            'name':"选项",
    		'subId': optionSubId
    	}; 
        radio.generateOptionBtn($configView,generateObj);
        optionListArray.push(generateObj);
        radio.getConfigViewsControl("optionList", $configView).val(JSON.stringify(optionListArray));
        radio.getOptions()["option"]["optionList"] = optionListArray;
        radio.refresh();
        radio.getConfigViewsControl(generateObj.subId, $configView).trigger("click");
    };
    
    
    Radio.prototype.deleteElement=function(optionSubId,optionListArray,$configView){//删除元素
    	var radio = this;
    	var deleteIndex = radio.functiontofindIndexByKeyValue(optionListArray,'id',radio.getId() + "$$" + optionSubId);
    	optionListArray.splice(deleteIndex,1);
        radio.getConfigViewsControl("optionList", $configView).val(JSON.stringify(optionListArray));
        radio.getOptions()["option"]["optionList"] = optionListArray;
        radio.getConfigViewsControl("optionAttribute",$configView).hide();
        radio.refresh();
    };
      
    Radio.prototype.bindCore = function(eventname, callback) {
        
    };
    Widget.registerType(type, Radio);
})(WidgetObject, "radio");