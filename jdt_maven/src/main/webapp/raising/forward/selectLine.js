var codename;
var detailData;
$(document).ready(function () {
    //做右上角默认线路显示通用css。暂时只应用surveyInfo.jsp。
    $("#select_line_div").css("float","right");
    $("#select_line_div").css("margin-top","-20px");

    baosightRequire.requireFunct(['bxcombobox'], function() {
        var selectLineCallBack={
            onSuccess: function (paramJsonObj) {
                initDefaultProject(paramJsonObj);
                codename = paramJsonObj.codeName;
                detailData = paramJsonObj.detailsArray;

                if ($.cookie('selected_id')!=null && $.cookie('selected_id') != undefined) {
                    var projectId = $.cookie('selected_id');
                    var programname;
                    var ccsId;
                    var linename;
                    var cityname;
                    for(var i = 0; i < detailData.length; i++ ){
                        if(projectId == (detailData[i].projectId+"")) {
                            programname = detailData[i].projectName;
                            var ccsId = detailData[i].ccsId;
                        }
                    }
                    var ccs_id = ccsId.split(".");
                    var ccsStr = ccs_id[0]+"."+ccs_id[1];

                    for(var i =0 ; i <codename[ccsStr].length; i++) {
                        if(ccsId == codename[ccsStr][i].value){
                            linename = codename[ccsStr][i].label;
                        }
                    }
                    for(i = 0; i< codename.cityArray.length;i++){
                        if(ccsStr == codename.cityArray[i].value){
                            cityname = codename.cityArray[i].label
                        }
                    }
                    $("#cityname").html("["+cityname+"]");
                    $("#linemessage").html("["+linename+"]");
                    $("#programmessage").html("["+programname+"]");
                }else {
                    $("#cityname").html("[请选择城市]");
                    $("#linemessage").html("[请选择线路]");
                    $("#programmessage").html("[请选择项目]");
                }
            }
        };
        AjaxCommunicator.ajaxRequest('/raising/forward/project/getProject.do', 'POST', {}, selectLineCallBack,true);
    })
})

function initDefaultProject(paramJsonObj) {
    baosightRequire.requireFunct(['bxcombobox'], function() {
        if (paramJsonObj.detailsArray == null) {//如果其下该用户下没有任何资源
            $("#cityname").html("[请选择城市]");
            $("#linemessage").html("[请选择线路]");
            $("#programmessage").html("[请选择项目]");

            $("#div_city").bxcombobox({
                dataPattern: 'local',
                data: [],
                async: false
            });
            $("#div_city_select ").prepend("<option value='' selected = selected>--选择城市--</option>");

            $("#div_line").bxcombobox({
                dataPattern: 'local',
                data: [],
                async: false
            });
            $("#div_line_select").prepend("<option value='' selected = selected>--选择线路--</option>");

            $("#div_program").bxcombobox({
                dataPattern: 'local',
                data: [],
                async: false
            });
            $("#div_program_select").prepend("<option value='' selected = selected>--选择项目--</option>");
            return;
        }
        codename = paramJsonObj.codeName;
        detailData = paramJsonObj.detailsArray;
        var cityData = codename.cityArray;
        var lineName = [];
        var programArray = [];
        var projectId = $.cookie("selected_id");

        var ccsId = null;
        for (var i = 0; i < detailData.length; i++) {
            if (projectId == detailData[i].projectId) {
                ccsId = detailData[i].ccsId;
            }
        }
        var cityCCSId = ccsId.substring(0, ccsId.lastIndexOf("."));//设置默认的城市名
        var default_lineNames = codename[cityCCSId];//设置默认的线路名。
        var default_projectNames = [];
        var projectName = $.cookie("selected_name");//默认的项目名
        for (var i = 0; i < detailData.length; i++) {
            if (ccsId == detailData[i].ccsId) {
                var program = {};
                if (projectName == detailData[i].projectName) {
                    program.ccsId = ccsId;
                }
                program.label = detailData[i].projectName;
                program.value = detailData[i].totalLength + "_" + detailData[i].endMileage;
                default_projectNames.push(program);
            }
        }
        $("#div_city").bxcombobox({
            dataPattern: 'local',
            data: cityData,
            async: false

        });

        $("#div_line").bxcombobox({
            dataPattern: 'local',
            data: default_lineNames,
            async: false
        });

        $("#div_program").bxcombobox({
            dataPattern: 'local',
            data: default_projectNames,
            async: false
        });
        //写默认的城市名
        $("#div_city_select").val(cityCCSId);
        $("#div_line_select").val(ccsId);
        var all_options = document.getElementById("div_program_select").options;
        for(var i=0;i<all_options.length;i++){
            if(all_options[i].innerHTML == projectName ){
                all_options[i].selected = true;
            }
        }
        $("#div_city").change(function () {
            programArray = [];
            $("#div_program").bxcombobox({
                dataPattern: 'local',
                data: programArray,
                async: false
            });
            $("#div_program_select").prepend("<option value='' selected = selected>--选择项目--</option>");

            var strVal = $('#div_city_select').val();
            if (strVal == "") {
                return;
            }
            var lineName = codename[strVal];
            $("#div_line").bxcombobox({
                dataPattern: 'local',
                data: lineName,
                async: false
            });
            $("#div_line_select").prepend("<option value='' selected = selected>--选择线路--</option>");
        })

        $("#div_line").change(function () {
            var strVal = $('#div_line_select').val();
            if (strVal == "") {
            }
            programArray = []

            for (var i = 0; i < detailData.length; i++) {
                if (strVal == detailData[i].ccsId) {
                    var program = {};
                    var totalLength =  detailData[i].totalLength;
                    var tatalMileage = detailData[i].endMileage;
                    program.label = detailData[i].projectName;
                    program.value =  totalLength + "_" + tatalMileage;
                    programArray.push(program);
                }
            }
            $("#div_program").bxcombobox({
                dataPattern: 'local',
                data: programArray,
                async: false
            });
            $("#div_program_select").prepend("<option value='' selected = selected>--选择项目--</option>");
        })
    })
}

function insertPage() {

    var button = [{
        text: "确定",
        "class" : "btn btn-skinColor btn-xs",
        click : function () {
            var projectId = null;
            var collectorName = null;
            var tbmName = null;
            var ringTotal = null;
            var cityname = $('#div_city_select option:selected').text();
            var linename = $('#div_line_select option:selected').text();
            var programname = $('#div_program_select option:selected').text();
            var totalData = $('#div_program_select option:selected').val();
            var totalArr = totalData.split("_");
            for(var i = 0; i <detailData.length; i++){
                if(programname == detailData[i].projectName){
                    projectId = detailData[i].projectId;
                    collectorName = detailData[i].collectorName;
                    tbmName = detailData[i].tbmName;
                    ringTotal = detailData[i].ringTotal;
                    break;
                }
            }
            if(projectId == null){
                alertDiv("提示","请选择合法项目！");
                return;
            }
            $("#detail").bxdialog('close');
            $.cookie('selected_collection',collectorName, {path: "/"});
            $.cookie('selected_name',programname, {path: "/"});
            $.cookie('selected_id',projectId, {path: "/"});
            $.cookie('totalLength',totalArr[0],{path:"/"});
            $.cookie('totalMileage',totalArr[1],{path:"/"});
            $.cookie('tbmName',tbmName,{path:"/"});
            $.cookie('ringTotal',ringTotal,{path:"/"});
            window.location.reload();

        }
    }]
    var title = "选择区间信息";
    var dialogOpt = {
        title: title,
        buttons:button,
        width: '300px'
    };
    $("#detail").bxdialog(dialogOpt);

}


