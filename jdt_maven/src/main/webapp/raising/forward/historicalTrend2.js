//worker.js
onmessage = function(evt){

    var paramStr = evt.data;//通过evt.data获得发送来的数据
    var param = JSON.parse(paramStr);
    var getRecordURL = param.getRecordURL;
    var startTime = param.startTime;
    var endTime = param.endTime;

    var interval = Math.round((endTime - startTime)/10000);
    if(interval <= 5000 ){//如果小于5s。则使用查询原始值的方式查询
        interval = 0;
    }
    getRecordURL = getRecordURL+"?limit=1000&start_time="+startTime+"&end_time="+endTime+"&interval="+interval;
    var cursor_mask = "*";
    var recordsArr = [];
    var condition = true;

    do {
        //43200000半天
        //7200000 2小时
        //300000  5分钟
        var resultUrl = getRecordURL + "&cursor_mask="+cursor_mask;
        var request = new XMLHttpRequest();
        request.onload = function(){
            condition = false;
            if(request.status == 200){
                var str = request.responseText;
                var data = JSON.parse(str);
                if (Number(data.errcode) == 0) {
                    cursor_mask = data.next_cursor_mask;
                    condition = data.records.length == 1000 ? true : false;
                    recordsArr = recordsArr.concat(data.records);
                }
            }
        }
        request.open("GET",resultUrl,false);
        request.send();
    } while (condition);

    var showData = new Array();
    for (var i = 0; i < recordsArr.length; i++) {
        var temp = [];
        temp[0] = recordsArr[i].timestamp;
        temp[1] = recordsArr[i].value;
        showData.push(temp);
    }
    postMessage(JSON.stringify(showData));//将获取到的数据发送会主线程
    self.close();
}