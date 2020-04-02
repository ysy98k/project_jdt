var now = new Date();

$(document).ready(function() {
	
    baosightRequire.requireFunct(['bxcharts','bxchartsline','bxdialog','bxgrid'],
    function() { 
    	$('#startTime').val(DateUtil.convertDateToStr("yyyy-MM-dd HH:mm:ss",DateUtil.getWholeMinute(new Date(now.getTime()-1000*60*15))));
    	$('#endTime').val(DateUtil.convertDateToStr("yyyy-MM-dd HH:mm:ss",DateUtil.getWholeMinute(new Date(now.getTime()+1000*60*15))));
    	$('#timeInterval').val(60);
    	$('#cycle').val(60);
        on_query_click();
        
    });
 
});


function changeStartDate() {
    var dateFormat = 'yyyy-MM-dd HH:mm';
    WdatePicker({
        skin: 'default',
        dateFmt: dateFormat
    });
}

function changeEndDate() {
    var dateFormat = 'yyyy-MM-dd HH:mm';
    WdatePicker({
        skin: 'default',
        dateFmt: dateFormat
    });
}

function on_query_click() {
	
	if($('#timeInterval').val() <= 0){
		var dialogOpt = {
			title: '参数不合法！',
			custom:false,
	        content: '时间间隔要大于0！',	
	    };
		$('#dialog-message').bxdialog(dialogOpt);
		return;
	};
	if($('#cycle').val() <= 0){
		var dialogOpt = {
			title: '参数不合法！',
			custom:false,
	        content: '刷新周期要大于0！',	
	    };
		$('#dialog-message').bxdialog(dialogOpt);
		return;
	};	
	if($('#startTime').val() >= $('#endTime').val()){
		var dialogOpt = {
			title: '参数不合法！',
			custom:false,
	        content: '起始时间要小于结束时间！',	
	    };
		$('#dialog-message').bxdialog(dialogOpt);
		return;
	};	
	
	/*-----------------------------------------------------------图表组件----------------------------------------*/
	var handlerOption = function(option){
		
		var timeGory = {};
		var categoryLst = $("#chartsdemo").bxchartsline("option","condition").categoryLst;
		for(var i = 0;i < categoryLst.length;i++){
			var category = categoryLst[i];
			if(category.item == "time"){
				timeGory = category;
				break;
			}
		}
				
		var nowIndex = timeGory.value.indexOf(timeGory.currentTime);
		var series = option.series;
		for(var i = 0;i<series.length;i++){
			var sery = series[i];
			if(sery.name == "实际值"){
				var data = sery.data;
				var newDate = new Array();
				for(var k = 0;k<data.length;k++){
					if(k <= nowIndex){
						newDate.push(data[k]);
					}
				}
				sery.data = newDate;
			}
		}
	};
	
    var linechartOption = {
    		showPattern : 'dynamic',
    		refreshInterval : $("#cycle").val()*1000,
    		chartOption : {
    			title: {
                    text: '高炉煤气模型预测',
                    subtext: '',
                    x: 'center'
                },
                xAxis : [ {
    				type : 'category',
    				name : '时间'
    			} ],
    			yAxis : [ {
    				type : 'value',    				
    				name : '立方米'
    			} ]
    		},
    		preHandlerFunct : handlerOption,
    		dataPattern : "pds",
			condition : [
							{
								item : 'process',
								value : ['A050100','A050200'],
								label : ['实际值','预测值'],
								generateInstance : true,
								order : 1
							},
							{
								item : 'time',
								value : DateUtil.getTimeLstByInterval(DateUtil.convertStrToDate($("#startTime").val()).getTime(),DateUtil.convertStrToDate($("#endTime").val()).getTime(),$("#timeInterval").val()*1000),
								type : 'date',
								returnFormatter: 'yyyy-MM-dd HH:mm:ss',
								intervals : $("#timeInterval").val()*1000,
								currentTime : DateUtil.getWholeMinute(now).getTime(),
								order : 2
							}, {		
								item : 'energyId',
								value : ['E2101'],
								label : ['电'],
								generateAttribute: true,
								order : 1
							}, {
								item : 'propertyId',
								value : ['R'],
								label : ['实时值'],
								generateAttribute: true,
								order : 2
							}, {
								item : 'timegrandid',
								value : ['second'],
								label : ['秒'],
								generateAttribute: true,
								order : 3
							}
	        ],
			seriesObj : {
				smooth:true,
		        itemStyle: {normal: {areaStyle: {type: 'default'}}},
			},
			chartRelate : {		
				xdataItem : 'time',
				ydataItem : 'dataValue',
				legendItem : 'process'			
			}
    };
    $("#chartsdemo").bxchartsline(linechartOption);

    
    /*-----------------------------------------------------------表格组件----------------------------------------*/
    var grid_data = new Array();
    var callback = {
		onSuccess : function(gridData) {
			var A050100 = gridData.dataValue[0].data;
	    	var A050200 = gridData.dataValue[1].data;
	    	var time = gridData.time;
	    	for(var i=0;i<time.length;i++){
	    		var oneGridData = {};
	    		var oneTime = time[i];
	    		oneGridData.time = oneTime;
	    		oneGridData.A050100 = A050100[i];
	    		oneGridData.A050200 = A050200[i];
	    		grid_data.push(oneGridData);
	    	}
		}
	};    
    
    var paramObj = {
    	dataPattern : "pds",
    	condition : {				
				categoryLst : [
						{
							item : 'process',
							value : ['A050100','A050200'],
							label : ['实际值','预测值'],
							generateInstance : true,
							order : 1
						},
						{
							item : 'time',
							value : DateUtil.getTimeLstByInterval(DateUtil.convertStrToDate($("#startTime").val()).getTime(),DateUtil.convertStrToDate($("#endTime").val()).getTime(),$("#timeInterval").val()*1000),
							type : 'date',
							returnFormatter: 'yyyy-MM-dd HH:mm:ss',
							intervals : $("#timeInterval").val()*1000,
							currentTime : DateUtil.getWholeMinute(now).getTime(),
							order : 2
						}, {		
							item : 'energyId',
							value : ['E2101'],
							label : ['电'],
							generateAttribute: true,
							order : 1
						}, {
							item : 'propertyId',
							value : ['R'],
							label : ['实时值'],
							generateAttribute: true,
							order : 2
						}, {
							item : 'timegrandid',
							value : ['second'],
							label : ['秒'],
							generateAttribute: true,
							order : 3
						} ]
			},
			chartRelate : {						
				legendItem : 'process'		
			}	
    };
    
    AjaxCommunicator.ajaxRequest("/charthandler",'POST', paramObj, callback);
    
    var bxgridOption = {
    		dataPattern : 'local',
    		gridOption : {
    			    colNames : ['时间戳', '实际值', '预测值'],
					colModel : [
					{
						name : 'time',
						index : 'time',
						width : 60,
						editable : true
					},
					{
						name : 'A050100',
						index : 'A050100',
						width : 60,
						editable : true
					}, {
						name : 'A050200',
						index : 'A050200',
						width : 60,
						editable : true
					}],
					caption: "物品买卖交易",
					data: grid_data,
					jsonReader : {
						id : "time",
						repeatitems : false
					}
    		}			
    };
    $("#griddemo").bxgrid(bxgridOption);               
};

