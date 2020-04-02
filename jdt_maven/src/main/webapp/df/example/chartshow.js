var importChartJs;
$(document).ready(function() {	
	
	importChartJs = type + ".js";
    var onload = function () {
        executeImport();
		$(".thumbnail").mouseover(function(){
			if(this.getBoundingClientRect().right>document.documentElement.clientWidth)
				$(this).css("right",0);
		});
    };   
    var node = document.createElement('script');
    node.type = "text/javascript";
    $(node).load(onload).bind('readystatechange', function () {
        if (node.readyState == 'loaded') {
            onload();
        }
    });
    document.getElementsByTagName('head')[0].appendChild(node);
    node.src = toolkitPath + '/df/example/chartsthumb/' + importChartJs;
});


function executeImport(){
	var barArrayObj = eval(type + "Array");
	$("#title").text(barArrayObj.title);
	var chartArray = barArrayObj.chart;
	var rowCount = 0;
	var rowId = 0;
	for(var i=0;i<chartArray.length;i++){
		var chartObj = chartArray[i];	
		if(rowCount == 0){
			var node = document.createElement('div');
			$(node).attr("class","row");
			$(node).attr("id",rowId);
			$("#showDiv").append($(node));
		}
		var column = document.createElement('div');
		$(column).attr("class","col-xs-3");
		$("#" + rowId).append($(column));
		
		var div = document.createElement('div');
		$(div).attr("class","thumbnail");
		$(column).append($(div));
		
		var aElement = document.createElement('a');		
		$(aElement).attr("href",toolkitPath + "/df/example/editSample.jsp?chart=" + chartObj.chart + "&detail=" + chartObj.detail);
		$(div).append($(aElement));
		
		var img = document.createElement('img');
		$(img).attr("src",toolkitPath + '/df/example/chartsthumb/images/' + chartObj.images + ".png");
		$(aElement).append($(img));
		
		var captionDiv = document.createElement('div');
		$(captionDiv).attr("class","caption");
		var h =  document.createElement('h3');
		$(h).text(chartObj.title);
		var p =  document.createElement('p');
		$(p).text(chartObj.content);
		
		$(captionDiv).append($(h));
		$(captionDiv).append($(p));
		
		$(div).append($(captionDiv));
		
		rowCount++;
		if(rowCount > 3){
			rowCount = 0;
			rowId++;
		}
	}
}