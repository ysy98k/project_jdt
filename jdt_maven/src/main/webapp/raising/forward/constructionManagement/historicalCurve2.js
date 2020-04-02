//worker.js
onmessage = function(evt){

    var paramStr = evt.data;//通过evt.data获得发送来的数据
    var param = JSON.parse(paramStr);
    var tagCode = param.tagCode;
    var getRecordURL = param.getRecordURL;
    var startTime = param.startTime;
    var endTime = param.endTime;

    var interval = Math.round((endTime - startTime)/1000);
    if(interval <= 5000 ){//如果小于5s。则使用查询原始值的方式查询
        getRecordURL = getRecordURL+"?limit=1000&start_time="+startTime+"&end_time="+endTime
    }else{
        getRecordURL = getRecordURL+"?limit=1000&start_time="+startTime+"&end_time="+endTime+"&interval="+interval;
    }
    var cursor_mask = "*";
    var recordsArr = [];
    var condition = true;

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
    request.setRequestHeader('Authorization', "Basic " + new Base64().encode('tsdbDataUser@raising' + ":" + '123456'));
    request.send();

    var showData = {};
    showData.dataArr = [];
    showData.timArr = [];
    for (var i = 0; i < recordsArr.length; i++) {
        showData.dataArr.push(recordsArr[i].value);
        showData.timArr.push(getMyDate(recordsArr[i].timestamp));
    }
    showData.code =  tagCode;
    postMessage(JSON.stringify(showData));//将获取到的数据发送会主线程
    self.close();
}

function getMyDate(str) {
    var oDate = new Date(str),
        oYear = oDate.getFullYear(),
        oMonth = oDate.getMonth()+1,
        oDay = oDate.getDate(),
        oHour = oDate.getHours(),
        oMin = oDate.getMinutes(),
        oSen = oDate.getSeconds(),
        oTime = oYear +'-'+ addZero(oMonth) +'-'+ addZero(oDay) +' '+ addZero(oHour) +':'+
            addZero(oMin) +':'+addZero(oSen);
    return oTime;
}
//补零操作
function addZero(num){
    if(parseInt(num) < 10){
        num = '0'+num;
    }
    return num;
}

function Base64() {

    // private property
    _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    // public method for encoding
    this.encode = function(input) {
        var output = "";
        var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
        var i = 0;
        input = _utf8_encode(input);
        while (i < input.length) {
            chr1 = input.charCodeAt(i++);
            chr2 = input.charCodeAt(i++);
            chr3 = input.charCodeAt(i++);
            enc1 = chr1 >> 2;
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
            enc4 = chr3 & 63;
            if (isNaN(chr2)) {
                enc3 = enc4 = 64;
            } else if (isNaN(chr3)) {
                enc4 = 64;
            }
            output = output +
                _keyStr.charAt(enc1) + _keyStr.charAt(enc2) +
                _keyStr.charAt(enc3) + _keyStr.charAt(enc4);
        }
        return output;
    }

    // public method for decoding
    this.decode = function(input) {
        var output = "";
        var chr1, chr2, chr3;
        var enc1, enc2, enc3, enc4;
        var i = 0;
        input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
        while (i < input.length) {
            enc1 = _keyStr.indexOf(input.charAt(i++));
            enc2 = _keyStr.indexOf(input.charAt(i++));
            enc3 = _keyStr.indexOf(input.charAt(i++));
            enc4 = _keyStr.indexOf(input.charAt(i++));
            chr1 = (enc1 << 2) | (enc2 >> 4);
            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
            chr3 = ((enc3 & 3) << 6) | enc4;
            output = output + String.fromCharCode(chr1);
            if (enc3 != 64) {
                output = output + String.fromCharCode(chr2);
            }
            if (enc4 != 64) {
                output = output + String.fromCharCode(chr3);
            }
        }
        output = _utf8_decode(output);
        return output;
    }

    // private method for UTF-8 encoding
    _utf8_encode = function(string) {
        string = string.replace(/\r\n/g, "\n");
        var utftext = "";
        for (var n = 0; n < string.length; n++) {
            var c = string.charCodeAt(n);
            if (c < 128) {
                utftext += String.fromCharCode(c);
            } else if ((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            } else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }

        }
        return utftext;
    }

    // private method for UTF-8 decoding
    _utf8_decode = function(utftext) {
        var string = "";
        var i = 0;
        var c = c1 = c2 = 0;
        while (i < utftext.length) {
            c = utftext.charCodeAt(i);
            if (c < 128) {
                string += String.fromCharCode(c);
                i++;
            } else if ((c > 191) && (c < 224)) {
                c2 = utftext.charCodeAt(i + 1);
                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                i += 2;
            } else {
                c2 = utftext.charCodeAt(i + 1);
                c3 = utftext.charCodeAt(i + 2);
                string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                i += 3;
            }
        }
        return string;
    }
}