var now = new Date();
//var startTime = DateUtil.getWholeHour(new Date(now.getTime()-1000*60*60*5));
//var endTime = DateUtil.getWholeHour(new Date(now.getTime()-1000*60*60*1));
var startTime = DateUtil.convertStrToDate("2016-01-30 00:00:00");
var endTime = DateUtil.convertStrToDate("2016-04-06 06:00:00");
var oneTime = DateUtil.getWholeHour(new Date(now.getTime()-1000*60*60*1));

var oneTimeArray = [];
oneTimeArray.push(oneTime.getTime());

$(document).ready(function() {
	
    baosightRequire.requireFunct(['bxcharts','bxchartsbar','bxchartsline','bxchartspie','bxchartsgauge'],
    function() { 
        on_query_click();      
    });

});

function on_query_click() {
	
	/*-----------------------------------------------------------柱状图组件——竖----------------------------------------*/
	
//    var barchartOption = {
//    		dataPattern : 'pds',
//    		dataValueType : 'real',
//    		chartOption : {
//    			title: {
//                    text: '工序实绩能耗-小时',
//                    subtext: '',
//                    x: 'left'
//                },
//                xAxis : [ {
//    				type : 'category',
//    				name : '时间'
//    			} ],
//    			yAxis : [ {
//    				type : 'value',    				
//    				name : '千瓦时'
//    			} ]
//    		},   	
//			condition : [
//							{
//								item : 'process',
//								value : ['A050100','A050200','A050300'],
//								label : ['原料','焦化','烧结'],
//								generateInstance : true,
//								order : 1
//							},
//							{
//								item : 'time',
//								value : [startTime.getTime(),endTime.getTime()],
//								type : 'date',
//								intervals : 60*60*1000,
//								returnFormatter: 'yyyy-MM-dd HH:mm:ss',								
//								order : 2
//							}, {		
//								item : 'energyId',
//								value : ['E2101'],
//								label : ['电'],
//								generateAttribute: true,
//								order : 1
//							}, {
//								item : 'propertyId',
//								value : ['R'],
//								label : ['实时值'],
//								generateAttribute: true,
//								order : 2
//							}, {
//								item : 'timegrandid',
//								value : ['day'],
//								label : ['小时'],
//								generateAttribute: true,
//								order : 3
//							}
//	        ],
//			seriesObj : {
//				
//			},
//			chartRelate : {		
//				xdataItem : 'time',
//				ydataItem : 'dataValue',
//				legendItem : 'process'			
//			}
//    };
//    $("#bardemostandard").bxchartsbar(barchartOption);    
    /*-----------------------------------------------------------柱状图组件—-横----------------------------------------*/
//    barchartOption.chartOption.xAxis = [ {
//    				type : 'value',    				
//    				name : '千瓦时'
//    			} ];
//    barchartOption.chartOption.yAxis = [ {
//    				type : 'category',
//    				name : '时间'
//    			} ];
//    var condition = barchartOption.condition;
//    condition[1].returnFormatter = "HH:mm:ss";
//    barchartOption.chartRelate.xdataItem = 'dataValue';
//    barchartOption.chartRelate.ydataItem = 'time';
//    $("#bardemohorizontal").bxchartsbar(barchartOption); 
//    /*-----------------------------------------------------------折线图----------------------------------------*/
//    var linechartOption = {
//    		dataPattern : 'pds',
//    		chartOption : {
//    			title: {
//                    text: '工序实绩能耗-小时',
//                    subtext: '',
//                    x: 'left'
//                },
//                xAxis : [ {
//    				type : 'category',
//    				name : '时间'
//    			} ],
//    			yAxis : [ {
//    				type : 'value',    				
//    				name : '千瓦时'
//    			} ]
//    		},   	
//			condition : [
//							{
//								item : 'process',
//								value : ['A050100','A050300'],
//								label : ['原料','烧结'],
//								generateInstance : true,
//								order : 1
//							},
//							{
//								item : 'time',
//								value : [startTime.getTime(),endTime.getTime()],
//								type : 'date',
//								intervals : 60*60*1000,
//								returnFormatter: 'yyyy-MM-dd HH:mm:ss',								
//								order : 2
//							}, {		
//								item : 'energyId',
//								value : ['E2101'],
//								label : ['电'],
//								generateAttribute: true,
//								order : 1
//							}, {
//								item : 'propertyId',
//								value : ['R'],
//								label : ['实时值'],
//								generateAttribute: true,
//								order : 2
//							}, {
//								item : 'timegrandid',
//								value : ['day'],
//								label : ['小时'],
//								generateAttribute: true,
//								order : 3
//							}
//	        ],
//			seriesObj : {
//				
//			},
//			chartRelate : {		
//				xdataItem : 'time',
//				ydataItem : 'dataValue',
//				legendItem : 'process'			
//			}
//    };
//    $("#linedemostandard").bxchartsline(linechartOption); 
//    /*-----------------------------------------------------------饼图----------------------------------------*/
    var piechartOption = {
    		dataPattern : 'pds',
    		chartOption : {
    			title: {
                    text: '工序实绩能耗-小时',
                    subtext: '',
                    x: 'left'
                }
    		},   	
			condition : [
							{
								item : 'process',
								value : ['A050000'],
								label : ['原料'],
								generateInstance : true,
								order : 1
							},
							{
								item : 'time',
								value : [startTime.getTime()],
								type : 'date',
								intervals : 60*60*1000*24,
								returnFormatter: 'yyyy-MM-dd HH:mm:ss',								
								order : 2
							}, {		
								item : 'energyId',
								value : ['E3201'],
								label : ['电'],
								generateAttribute: true,
								order : 1
							}, {
								item : 'timegrandid',
								value : ['day'],
								label : ['日'],
								generateAttribute: true,
								order : 2
							},
							{
								item : 'propertyId',
								value : ['D_realvalue','D_costvalue'],
								label : ['实时值','历史值'],
								generateAttribute: true,
								order : 3
							}
	        ],
			seriesObj : {
				
			},
			chartRelate : {		
				dataItem : 'dataValue',
				legendItem : 'propertyId'			
			}
    };
    $("#piedemostandard").bxchartspie(piechartOption);  
//    
//    /*-----------------------------------------------------------时间轴饼图----------------------------------------*/
//        var pietimechartOption = {
//    		dataPattern : 'pds',
//    		showPattern : 'timeline',
//    		chartOption : {
//    			options : [{
//	    			title: {
//	                    text: '工序实绩能耗-小时',
//	                    subtext: '',
//	                    x: 'left'
//	                }
//    			}]
//    		},   	
//			condition : [
//							{
//								item : 'process',
//								value : ['A050100','A050200','A050300'],
//								label : ['原料','焦化','烧结'],
//								generateInstance : true,
//								order : 1
//							},
//							{
//								item : 'time',
//								value : [startTime.getTime(),endTime.getTime()],
//								type : 'date',
//								intervals : 60*60*1000,
//								returnFormatter: 'yyyy-MM-dd HH:mm:ss',								
//								order : 2
//							}, {		
//								item : 'energyId',
//								value : ['E2101'],
//								label : ['电'],
//								generateAttribute: true,
//								order : 1
//							}, {
//								item : 'propertyId',
//								value : ['R'],
//								label : ['实时值'],
//								generateAttribute: true,
//								order : 2
//							}, {
//								item : 'timegrandid',
//								value : ['day'],
//								label : ['小时'],
//								generateAttribute: true,
//								order : 3
//							}
//	        ],
//			seriesObj : {
//				name : '工序实绩能耗-小时',
//			},
//			chartRelate : {		
//				xdataItem : 'time',
//				ydataItem : 'dataValue',
//				legendItem : 'process',
//				chartType : 'pie'
//			}
//    };
//    $("#piedemotime").bxchartspie(pietimechartOption);
//    
//    /*-----------------------------------------------------------仪表计----------------------------------------*/
//    var gaugechartOption = {
//    		dataPattern : 'local',
//    		chartOption : {
//    			series : [
//			        {
//			            name:'业务指标',
//			            type:'gauge',
//			            detail : {formatter:'{value}%'},
//			            data:[{value: 50, name: '完成率'}]
//			        }
//               ]
//    		}  			
//    };
//    $("#gaugedemo").bxchartsgauge(gaugechartOption);
    
    
};

