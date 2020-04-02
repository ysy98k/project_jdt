//删除数组某一元素
function removeArrVal(arr,val){
    var index = -1
    for (var i = 0; i < arr.length; i++) {
        if (arr[i] == val){
            index = i;
        }
    }
    if (index > -1) {
        arr.splice(index, 1);
    }
}


function getMyDate(str) {
    //补零操作
    function addZero(num){
        if(parseInt(num) < 10){
            num = '0'+num;
        }
        return num;
    }

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

function getDay(str) {
    var oDate = new Date(str),
        oYear = oDate.getFullYear(),
        oMonth = oDate.getMonth()+1,
        oDay = oDate.getDate(),
        oTime = oYear +'-'+ addZero(oMonth) +'-'+ addZero(oDay) ;
    return oTime;
}



function getRangOption(startTime,format){
    if(isNullOrEmptyOrUndefiend(startTime)){//如果传入的时间为空
        var options= {
            "singleDatePicker": true,
            "showDropdowns": true,
            "timePicker": true,
            "timePicker24Hour": true,
            "timePickerSeconds": true,
            "autoUpdateInput": false,
            "opens": "left",
            "drops": "down",
            "locale":{
                "format": format,
                "applyLabel": "确定",
                "cancelLabel": "取消",
                "weekLabel": "W",
                "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
                "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"]
            }
        }
        return options;
    }else{
        var options= {
            "singleDatePicker": true,
            "showDropdowns": true,
            "timePicker": true,
            "timePicker24Hour": true,
            "timePickerSeconds": true,
            "startDate": startTime,
            "opens": "left",
            "drops": "down",
            "locale":{
                "format": format,
                "applyLabel": "确定",
                "cancelLabel": "取消",
                "weekLabel": "W",
                "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
                "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
            }
        }
        return options;
    }
}

function getDateRangPickerOption(startTime){
    var options= {
        "singleDatePicker": true,
        "showDropdowns": true,
        "autoUpdateInput": false,
        "timePicker": true,
        "timePicker24Hour": true,
        "timePickerSeconds": true,
        //"startDate": startTime,
        //"endDate": endTime,
        "opens": "left",
        "drops": "down",
        "locale":{
            "format": 'YYYY-MM-DD HH:mm:ss',
            "applyLabel": "确定",
            "cancelLabel": "取消",
            "weekLabel": "W",
            "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
            "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        }
    }
    if(!isNullOrEmptyOrUndefiend(startTime)){
        options.startDate = startTime;
    }
    return options;
}

function getDateOption(startTime,endTime) {
    var options= {
        "singleDatePicker": true,
        "showDropdowns": true,
        "startDate": startTime,
        "endDate": endTime,
        "singleDatePicker": true,
        "opens": "left",
        "drops": "down",
        "locale":{
            "format": 'YYYY-MM-DD',
            "separator": " -222 ",
            "applyLabel": "确定",
            "cancelLabel": "取消",
            "fromLabel": "起始时间",
            "toLabel": "结束时间'",
            "customRangeLabel": "自定义",
            "weekLabel": "W",
            "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
            "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
            "firstDay": 1
        }
    }
    return options;    
}

//检验grid日期单元格
function checkGridDate(val,name) {
    //验证时间格式为：2012-01-31 09:00:22,忽略前后空格。
    var reDateTime = /^\d{4}\-\d{2}\-\d{2}$/;
    if(!reDateTime.test(val.trim()) ){
        return [false, "列【" + name + "】的值【" + val+ "】格式出错：格式应该为2000-01-01", ""];
    }
    return [true,""];
}

//检验时间单元格
function createTimeCheck(val,name){
    //验证时间格式为：2012-01-31 09:00:22,忽略前后空格。
    var reDateTime = /^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1])) (?:(?:[0-2][0-3])|(?:[0-1][0-9])):[0-5][0-9]:[0-5][0-9]$/;
    if(!reDateTime.test(val.trim()) ){
        return [false, "列【" + name + "】的值【" + val+ "】格式出错：格式应该为2000-01-01 00:00:00", ""];
    }
    return [true,""];
}
function checkMobile(str) { //写一个判断函数
    var  re = /^1\d{10}$/    //正则表达式
    if (re.test(str)) {      //判断字符是否是11位数字
        return true;
    }
    else {
        return false;
    }
}

//检验手机格式
function checkGridMobile(val,name) {

    if(!checkMobile(val.trim())){
        return [false, "列【" + name + "】的值【" + val+ "】格式出错：格式应该为11位数字", ""];
    }
    return [true,""];
}

/**
 * 检查是否是数字
 * @param val
 * @returns {boolean}
 */
function isNumber(val){
    var regPos = /^\d+(\.\d+)?$/; //非负浮点数
    var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
    if(regPos.test(val) || regNeg.test(val)){
        return true;
    }else{
        return false;
    }
}

/**
 * 是否是Int
 * @param val
 * @returns {boolean}
 */
function isInt(val){
    var regPos = /^\d+(\.\d+)?$/; //非负浮点数
    if(regPos.test(val)){
        return true;
    }
    return false;
}

function isNullOrEmptyOrUndefiend(obj){
    if(obj == null || obj == "" || obj == undefined){
        return true;
    }
    return false;
}



/**
 * 得到文件后缀名
 * @param pathfilename
 * @constructor
 */
function GetExtensionFileName(pathfilename) {
    var reg = /(\\+)/g;
    var pString = pathfilename.replace(reg, "#");          //用正则表达式来将\或\\替换成#
    var arr = pString.split("#");  // 以“#”为分隔符，将字符分解为数组 例如 D Program Files bg.png
    var lastString = arr[arr.length - 1];            //取最后一个字符
    var arr2 = lastString.split(".");                  //   再以"."作为分隔符
    return arr2[arr2.length - 1];                   //将后缀名返回出来
}

/**
 * 格式化时间 *
 */
function formatDate(time,format){
    var date = new Date(time);
    if(!format){
        format = 'YY-MM-DD hh:mm:ss';
    }
    var year = date.getFullYear(),
        month = date.getMonth()+1,//月份是从0开始的
        day = date.getDate(),
        hour = date.getHours(),
        min = date.getMinutes(),
        sec = date.getSeconds();
    var preArr = Array.apply(null,Array(10)).map(function(elem, index) {
        return '0'+index;
    });////开个长度为10的数组 格式为 00 01 02 03

    var newTime = format.replace(/YY/g,year)
        .replace(/MM/g,preArr[month]||month)
        .replace(/DD/g,preArr[day]||day)
        .replace(/hh/g,preArr[hour]||hour)
        .replace(/mm/g,preArr[min]||min)
        .replace(/ss/g,preArr[sec]||sec);

    return newTime;
}

/**
 * 请求时使用
 * @constructor
 */
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
