$(document).ready(function () {
    getLastData();
});

function getLastData() {
    var selectid = $.cookie('selected_id');
    if (selectid != null) {
        $.ajax({
            type: 'get',
            url: toolkitPath + '/raising/forward/surveyInfo/getLastData.do?selected_id=' + selectid,
            cache: false,
            processData: false,
            contentType: false
        }).success(function (allData) {
            if (allData != null) {
                if (allData.msg != null) {
                    showMessage(allData.msg)
                } else {
                    console.info(allData);
                    $("#alltables").show();
                    $("#J_TbData").empty();
                    $("#R_TbData").empty();
                    $("#myTab4").empty();
                    $("#myTabinfo").empty();
                    $("#dateAndTimes").empty();
                    getTitle(allData);
                    getTab(allData);
                    getPatrolDataRow(allData.patrolInfoList);
                    setEngineering(allData);
                    getResultData(allData);
                    setTimeAndDateSelections(allData);
                }
            }
        }).error(function () {
            showMessage("数据库中没有数据，请上传!");
        });
    }
}

function changeData() {
    var reportTime = $("#dateAndTimes").find("option:selected").val();
    var selectid = $.cookie('selected_id');
    $.ajax({
        type: 'get',
        url: toolkitPath + '/raising/forward/surveyInfo/getReportData.do?selected_id=' + selectid + '&reportTime=' + reportTime,
        cache: false,
        processData: false,
        contentType: false
    }).success(function (allData) {
        if (allData !== null) {
            console.info("Time now is: " + allData.reportTimes);
            if (allData.msg != null) {
                showMessage(allData.msg)
            } else {
                $("#errorAlert").hide();
                $("#alltables").show();
                $("#J_TbData").empty();
                $("#R_TbData").empty();
                $("#myTab4").empty();
                $("#myTabinfo").empty();
                getTitle(allData);
                getTab(allData);
                getPatrolDataRow(allData.patrolInfoList);
                getResultData(allData);
            }
        } else {
            showMessage("没有数据！");
        }
    }).error(function () {
        showMessage("数据错误！");
    });
}

function openBootBox() {
    $("#uploadFile").show();
    $("#uploadBlackGround").show();
}

function hideBootBox() {
    $("#uploadFile").hide();
    $("#uploadBlackGround").hide();
}

function checkFile(fileAttach) {
    var len = fileAttach.files.length;
    for (var i = 0; i < len; i++) {
        var temp = fileAttach.files[i].name;
        //alert(temp)
        $("#changeSpan").removeClass("ace-file-container").addClass("ace-file-container selected").attr("data-title", "修改");
        $("#fileName").attr("data-title", temp);
        $("#uploadCoin").removeClass("ace-icon fa fa-upload").addClass("ace-icon fa fa-file");
        $("#removeFile").show();
        console.log("file name: " + temp);
    }
}

function deleteFile() {
    $("#id-input-file-2").val("");
    $("#changeSpan").removeClass("ace-file-container selected").addClass("ace-file-container").attr("data-title", "选择");
    $("#fileName").attr("data-title", "请添加文件...");
    $("#uploadCoin").removeClass("ace-icon fa fa-file").addClass("ace-icon fa fa-upload");
    $("#removeFile").hide();

}

function cancelUpload() {
    deleteFile();
    hideBootBox();
}

function submitUpload(fileAttach) {
    //console.info(fileAttach.files[0].name);
    var formData = new FormData();
    var selectid = $.cookie('selected_id');
    formData.append("file", fileAttach.files[0]);
    if (selectid != null) {
        formData.append("selected_id", selectid);
    }

    $.ajax({
        type: 'post',
        url: toolkitPath + '/raising/forward/surveyInfo/fileUpload.do',
        data: formData,
        cache: false,
        processData: false,
        contentType: false
    }).success(function (allData) {
        if (allData != null) {
            if (allData.msg != null) {
                showMessage(allData.msg)

            } else {
                $("#alltables").show();
                $("#J_TbData").empty();
                $("#R_TbData").empty();
                $("#myTab4").empty();
                $("#myTabinfo").empty();
                $("#dateAndTimes").empty();
                getTitle(allData);
                getTab(allData);
                getPatrolDataRow(allData.patrolInfoList);
                setEngineering(allData);
                setTimeAndDateSelections(allData);
                getResultData(allData);
                $("#uploadFile").hide();
                $("#uploadBlackGround").hide();
                deleteFile();
            }
        } else {
            showMessage("没有数据！");
        }
    }).error(function () {
        showMessage("上传失败");
    });
}

function getTitle(allData) {
    //console.info(allData.sheetlist);
    var $trTemp = $("<li class='active'><a data-toggle='tab' id='' href='#sheet'>" + allData.patrolInfoList[0].sheetName + "</a></li>");
    $trTemp.appendTo("#myTab4");

    for (var i = 1; i < allData.sheetlist.length; i++) {
        if (allData.sheetlist[i].indexOf("巡视") === -1) {
            $trTemp = $("<li class=''></li>");
            $trTemp.append("<a data-toggle='tab' href='#" + allData.sheetlist[i] + "'>" + allData.sheetlist[i] + "</a>");
            $trTemp.appendTo("#myTab4");
        }
    }

    $trTemp = $("<li class=''><a data-toggle='tab' href='#resultSheet'>成果表</a></li>");
    $trTemp.appendTo("#myTab4");
}

function getTab(allData) {
    var $trTemp = $("<div id='sheet' class='tab-pane active'>" +
        "<table class='table table-striped table-bordered table-hover' style='margin:0 auto;'>" +
        "<thead class='thin-border-bottom'>" +
        "<tbody id='J_TbData'></tbody></thead></table></div>");
    $trTemp.appendTo("#myTabinfo");

    var reportSheetList = allData.reportSheetList;
    for (var i = 0; i < reportSheetList.length; i++) {
        $trTemp = $("<div id='" + reportSheetList[i][0].sheetName + "' class='tab-pane'>" +
            "<table class='table table-striped table-bordered table-hover' style='margin:0 auto;'>" +
            "<thead class='thin-border-bottom'>" +
            "<tbody id='R_Data" + i + "'></tbody></thead></table><div>");
        $trTemp.appendTo("#myTabinfo");
        getReportDataRow(reportSheetList, i);
    }

    $trTemp = $("<div id='resultSheet' class='tab-pane'>" +
        "<table class='table table-striped table-bordered table-hover' style='margin:0 auto;'>" +
        "<thead class='thin-border-bottom'>" +
        "<tbody id='R_TbData'></tbody></thead></table>" +
        "</div>");
    $trTemp.appendTo("#myTabinfo");
}

function getResultData(allData) {
    var resultDatas = allData.resultDatas;
    //console.info(allData.resultDatas);
    var $trTemp = $("<tr></tr>");
    $trTemp.append("<th rowspan='2'>监测项目</th><th colspan='2'>本期最大变化量</th><th  colspan='2'>累计最大变化量</th><th  colspan='2'>最大地层损失率</th><th  colspan='2'>警戒值(mm)或（‰）</th>");
    $trTemp.appendTo("#R_TbData");
    $trTemp = $("<tr></tr>");
    $trTemp.append("<th>点位</th><th>数值(mm)</th><th>点位</th><th>数值(mm)</th><th>点位</th><th>损失率(‰)</th><th>日变值</th><th>累计值</th>");
    $trTemp.appendTo("#R_TbData");

    for (var i = 0; i < resultDatas.length; i++) {
        $trTemp = $("<tr></tr>");
        var sheetName = resultDatas[i].sheetName;
        $trTemp.append("<td>" + sheetName + "</td>");

        $trTemp.append("<td>" + resultDatas[i].maxCQPoint + "</td>");
        $trTemp.append("<td>" + resultDatas[i].maxCQdata + "</td>");

        $trTemp.append("<td>" + resultDatas[i].maxCVPoint + "</td>");
        $trTemp.append("<td>" + resultDatas[i].maxCVdata + "</td>");


        if (sheetName.indexOf("地表") !== -1) {
            $trTemp.append("<td>" + resultDatas[i].maxFLPoint + "</td>");
            $trTemp.append("<td>" + resultDatas[i].maxFLdata + "</td>");

        } else {
            $trTemp.append("<td>/</td>");
            $trTemp.append("<td>/</td>");
        }
        $trTemp.append("<td>" + resultDatas[i].cumulativeVariationControlValue + "</td>");
        $trTemp.append("<td>" + resultDatas[i].changeRateControlValue + "</td>");
        $trTemp.appendTo("#R_TbData");
    }
}


function getReportDataRow(reportSheetList, i) {
    if (reportSheetList != null) {
        if (reportSheetList[i][0].sheetName.indexOf("地表") !== -1) {
            console.info(reportSheetList[i][0]);
            var $trTemp = $("<tr></tr>");
            $trTemp.append("<th>测点编号</th><th>测点位置</th><th>初始高程(m)</th><th>本次变化量(mm)</th><th>累计变化量(mm)</th><th>变化速率(mm/d)</th><th>地表损失率(1/1000)</th><th>风险等级</th><th>测点备注</th>");
            $trTemp.appendTo("#R_Data" + i);
            for (var j = 0; j < reportSheetList[i].length; j++) {
                $trTemp = $("<tr></tr>");
                var pointName = reportSheetList[i][j].point;
                $trTemp.append("<td class='" + pointName + "'>" + pointName + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].ringLocation + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].initialHeight + "</td>" +
                    "<td class='" + pointName + "'>" + reportSheetList[i][j].changeQuantity + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].cumulativeVariation + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].changeRate + "</td>" +
                    "<td class='" + pointName + "'>" + reportSheetList[i][j].formationLossRate + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].levelOfRisk + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].remarks + "</td>");
                $trTemp.appendTo("#R_Data" + i);
                if (reportSheetList[i][j].levelOfRisk === "一级") {
                    $("." + pointName).css("background-color", "yellow");
                }
                if (reportSheetList[i][j].levelOfRisk === "二级") {
                    $("." + pointName).css("background-color", "orange");
                }
                if (reportSheetList[i][j].levelOfRisk === "三级") {
                    $("." + pointName).css({"background-color": "#B22222", "color": "white"});
                }
            }
        } else if (reportSheetList[i][0].sheetName.indexOf("建筑") !== -1) {
            $trTemp = $("<tr></tr>");
            $trTemp.append("<th>测点编号</th><th>测点位置</th><th>初始高程(m)</th><th>本次变化量(mm)</th><th>变化速率(mm/d)</th><th>累计变化量(mm)</th><th>风险等级</th><th>测点备注</th>");
            $trTemp.appendTo("#R_Data" + i);
            for (j = 0; j < reportSheetList[i].length; j++) {
                $trTemp = $("<tr></tr>");
                pointName = reportSheetList[i][j].point;
                $trTemp.append("<td class='" + pointName + "'>" + pointName + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].ringLocation + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].initialHeight + "</td>" +
                    "<td class='" + pointName + "'>" + reportSheetList[i][j].changeQuantity + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].changeRate + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].cumulativeVariation + "</td>" +
                    "<td class='" + pointName + "'>" + reportSheetList[i][j].levelOfRisk + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].remarks + "</td>");
                $trTemp.appendTo("#R_Data" + i);
                if (reportSheetList[i][j].levelOfRisk === "一级") {
                    $("." + pointName).css("background-color", "yellow");
                }
                if (reportSheetList[i][j].levelOfRisk === "二级") {
                    $("." + pointName).css("background-color", "orange");
                }
                if (reportSheetList[i][j].levelOfRisk === "三级") {
                    $("." + pointName).css({"background-color": "#B22222", "color": "white"});
                }
            }
        } else {
            $trTemp = $("<tr></tr>");
            $trTemp.append("<th>测点编号</th><th>测点位置</th><th>初始高程(m)</th><th>本次高程(m)</th><th>本次变化量(mm)</th><th>变化速率(mm/d)</th><th>累计变化量(mm)</th><th>风险等级</th><th>测点备注</th>");
            $trTemp.appendTo("#R_Data" + i);
            for (j = 0; j < reportSheetList[i].length; j++) {
                $trTemp = $("<tr></tr>");
                pointName = reportSheetList[i][j].point;
                $trTemp.append("<td class='" + pointName + "'>" + pointName + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].ringLocation + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].initialHeight + "</td>" +
                    "<td class='" + pointName + "'>" + reportSheetList[i][j].height + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].changeQuantity + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].changeRate + "</td>" +
                    "<td class='" + pointName + "'>" + reportSheetList[i][j].cumulativeVariation + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].levelOfRisk + "</td><td class='" + pointName + "'>" + reportSheetList[i][j].remarks + "</td>");
                $trTemp.appendTo("#R_Data" + i);
                if (reportSheetList[i][j].levelOfRisk === "一级") {
                    $("." + pointName).css("background-color", "yellow");
                }
                if (reportSheetList[i][j].levelOfRisk === "二级") {
                    $("." + pointName).css("background-color", "orange");
                }
                if (reportSheetList[i][j].levelOfRisk === "三级") {
                    $("." + pointName).css({"background-color": "#B22222", "color": "white"});
                }
            }
        }
    }
}


function getPatrolDataRow(patrolInfoList) {
    if (patrolInfoList != null) {
        //console.info(patrolInfoList);
        var $trTemp = $("<tr></tr>");
        $trTemp.append("<th>分类</th><th>巡视检查内容</th><th>巡视检查结果</th><th>备注</th>");
        $trTemp.appendTo("#J_TbData");
        var t1Length = 0;
        var t2Length = 0;
        var t3Length = 0;
        var t4Length = 0;
        var t5Length = 0;
        for (var i = 0; i < patrolInfoList.length; i++) {
            var flag = patrolInfoList[i].placeMark;
            if (10 < parseInt(flag) && parseInt(flag) < 20) {
                t1Length++
            }

            if (20 < parseInt(flag) && parseInt(flag) < 30) {
                console.info(20 < parseInt(flag) < 30);
                t2Length++
            }

            if (30 < parseInt(flag) && parseInt(flag) < 40) {
                t3Length++
            }
            if (40 < parseInt(flag) && parseInt(flag) < 50) {
                t4Length++
            }
            if (50 < parseInt(flag) && parseInt(flag) < 60) {
                t5Length++
            }

        }
        //console.info(t1Length + " " + t2Length + " " + t3Length + " " + t4Length);

        $trTemp = $("<tr></tr>");
        $trTemp.append("<td rowspan='" + parseInt(t1Length) + "' style='font-size: large'>自然条件</td>");
        $trTemp.append("<td>" + patrolInfoList[0].patrolContent + "</td>");
        $trTemp.append("<td>" + patrolInfoList[0].patrolResult + "</td>");
        $trTemp.append("<td>" + patrolInfoList[0].remark + "</td>");
        $trTemp.appendTo("#J_TbData");
        for (i = 0; i < patrolInfoList.length; i++) {
            flag = patrolInfoList[i].placeMark;
            if (10 < parseInt(flag) && parseInt(flag) < 20 && parseInt(flag) !== 11) {
                $trTemp = $("<tr></tr>");
                $trTemp.append("<td>" + patrolInfoList[i].patrolContent + "</td>");
                $trTemp.append("<td>" + patrolInfoList[i].patrolResult + "</td>");
                $trTemp.append("<td>" + patrolInfoList[i].remark + "</td>");
                $trTemp.appendTo("#J_TbData");
            }
        }
        $trTemp = $("<tr></tr>");
        $trTemp.append("<td rowspan='" + parseInt(t2Length) + "' style='font-size: large'>施工工况</td>");
        $trTemp.append("<td>" + patrolInfoList[t1Length].patrolContent + "</td>");
        $trTemp.append("<td>" + patrolInfoList[t1Length].patrolResult + "</td>");
        $trTemp.append("<td>" + patrolInfoList[t1Length].remark + "</td>");
        $trTemp.appendTo("#J_TbData");
        for (i = 0; i < patrolInfoList.length; i++) {
            flag = patrolInfoList[i].placeMark;
            if (20 < parseInt(flag) && parseInt(flag) < 30 && parseInt(flag) !== 21) {
                $trTemp = $("<tr></tr>");
                $trTemp.append("<td>" + patrolInfoList[i].patrolContent + "</td>");
                $trTemp.append("<td>" + patrolInfoList[i].patrolResult + "</td>");
                $trTemp.append("<td>" + patrolInfoList[i].remark + "</td>");
                $trTemp.appendTo("#J_TbData");
            }
        }

        $trTemp = $("<tr></tr>");
        $trTemp.append("<td rowspan='" + parseInt(t3Length) + "' style='font-size: large'>管片变形</td>");
        $trTemp.append("<td>" + patrolInfoList[t1Length + t2Length].patrolContent + "</td>");
        $trTemp.append("<td>" + patrolInfoList[t1Length + t2Length].patrolResult + "</td>");
        $trTemp.append("<td>" + patrolInfoList[t1Length + t2Length].remark + "</td>");
        $trTemp.appendTo("#J_TbData");
        for (i = 0; i < patrolInfoList.length; i++) {
            flag = patrolInfoList[i].placeMark;
            if (30 < parseInt(flag) && parseInt(flag) < 40 && parseInt(flag) !== 31) {
                $trTemp = $("<tr></tr>");
                $trTemp.append("<td>" + patrolInfoList[i].patrolContent + "</td>");
                $trTemp.append("<td>" + patrolInfoList[i].patrolResult + "</td>");
                $trTemp.append("<td>" + patrolInfoList[i].remark + "</td>");
                $trTemp.appendTo("#J_TbData");
            }
        }
        $trTemp = $("<tr></tr>");
        $trTemp.append("<td rowspan='" + parseInt(t4Length) + "' style='font-size: large'>周边环境</td>");
        $trTemp.append("<td>" + patrolInfoList[t1Length + t2Length + t3Length].patrolContent + "</td>");
        $trTemp.append("<td>" + patrolInfoList[t1Length + t2Length + t3Length].patrolResult + "</td>");
        $trTemp.append("<td>" + patrolInfoList[t1Length + t2Length + t3Length].remark + "</td>");
        $trTemp.appendTo("#J_TbData");
        for (i = 0; i < patrolInfoList.length; i++) {
            flag = patrolInfoList[i].placeMark;
            if (40 < parseInt(flag) && parseInt(flag) < 50 && parseInt(flag) !== 41) {
                $trTemp = $("<tr></tr>");
                $trTemp.append("<td>" + patrolInfoList[i].patrolContent + "</td>");
                $trTemp.append("<td>" + patrolInfoList[i].patrolResult + "</td>");
                $trTemp.append("<td>" + patrolInfoList[i].remark + "</td>");
                $trTemp.appendTo("#J_TbData");
            }
        }

        $trTemp = $("<tr></tr>");
        $trTemp.append("<td rowspan='" + parseInt(t5Length) + "' style='font-size: large'>监测设施</td>");
        $trTemp.append("<td>" + patrolInfoList[t1Length + t2Length + t3Length + t4Length].patrolContent + "</td>");
        $trTemp.append("<td>" + patrolInfoList[t1Length + t2Length + t3Length + t4Length].patrolResult + "</td>");
        $trTemp.append("<td>" + patrolInfoList[t1Length + t2Length + t3Length + t4Length].remark + "</td>");
        $trTemp.appendTo("#J_TbData");
        for (i = 0; i < patrolInfoList.length; i++) {
            flag = patrolInfoList[i].placeMark;
            if (50 < parseInt(flag) && parseInt(flag) < 60 && parseInt(flag) !== 51) {
                $trTemp = $("<tr></tr>");
                $trTemp.append("<td>" + patrolInfoList[i].patrolContent + "</td>");
                $trTemp.append("<td>" + patrolInfoList[i].patrolResult + "</td>");
                $trTemp.append("<td>" + patrolInfoList[i].remark + "</td>");
                $trTemp.appendTo("#J_TbData");
            }
        }

    }
}

function setEngineering(allData) {
    var engineering = allData.engineering;
    if (engineering != null) {
        $("#constructionCompany").html(engineering.constructionCompany);
        $("#supervisionCompany").html(engineering.supervisionCompany);
        $("#monitoringCompany").html(engineering.monitoringCompany);
        $("#instrumentModel").html(engineering.instrumentModel);
    }
}

function setTimeAndDateSelections(allData) {
    var dateAndTimes = $("#dateAndTimes");
    var data = allData.reportRelations;
    var rightnowDate = formatDateTime(allData.reportDate);
    var rightnowTime = allData.reportTimes;
    console.info(data);
    dateAndTimes.append("<option value='" + rightnowTime + "'>" + rightnowDate + "&nbsp;&nbsp;&nbsp;" + "第" + rightnowTime + "次" + "</option>");
    for (var i = data.length - 1; i >= 0; i--) {
        var time = data[i].reportTime;
        var date = formatDateTime(data[i].reportDate);
        if (rightnowTime !== time) {
            dateAndTimes.append("<option value='" + time + "'>" + date + "&nbsp;&nbsp;&nbsp;" + "第" + time + "次" + "</option>");
        }
    }
}

function formatDateTime(inputTime) {
    var date = new Date(inputTime);
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    return y + '-' + m + '-' + d;
}


function hideMessage() {
    $("#errorAlert").hide();
}

function showMessage(message) {
    $("#message").html(message);
    $("#errorAlert").show();
    $("#uploadFile").hide();
    $("#uploadBlackGround").hide();

}

function downloadXlsTemplet() {
    window.location.href = toolkitPath + '/raising/forward/surveyInfo/downloadTemp.do'
}

