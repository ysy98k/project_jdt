$(document).ready(function () {
    getLastPDF();
});

function getLastPDF() {
    var selectid = $.cookie('selected_id');
    if (selectid != null) {
        $.ajax({
            type: 'get',
            url: toolkitPath + '/raising/forward/surveyGraph/lastPDF.do?selected_id=' + selectid,
            cache: false,
            processData: false,
            contentType: false
        }).success(function (allData) {
            if (allData != null) {
                if (allData.msg != null) {
                    showMessage(allData.msg)
                } else {
                    $("#showpdf").show();
                    console.info(allData);
                    console.info("---------------------");
                    console.info("path");
                    showPDF(allData.lastFilePath);
                    getFilesTable(allData.files);
                    var id = "d_" + allData.fileName.split(".")[0];
                    console.info("div id:" + id);
                    $("#" + id).css("background-color", "rgb(230, 241, 248)");
                }
            }
        }).error(function () {
            showMessage("数据库中没有数据，请上传!");
        });
    }
}

function uploadZip(fileAttach) {
    var fileName = fileAttach.files[0].name;
    console.info(fileName);
    if (fileName.indexOf(".pdf") > 0) {
        var formData = new FormData();
        var selectid = $.cookie('selected_id');
        formData.append("file", fileAttach.files[0]);
        if (selectid != null) {
            formData.append("selected_id", selectid);
        }
        $.ajax({
            type: 'post',
            url: toolkitPath + '/raising/forward/surveyGraph/fileUpload.do',
            data: formData,
            cache: false,
            processData: false,
            contentType: false
        }).success(function (allData) {
            if (allData != null) {
                if (allData.msg != null) {
                    showMessage(allData.msg)
                } else {
                    $("#errorAlert").hide();
                    $("#uploadFile").hide();
                    $("#uploadBlackGround").hide();
                    $("#showpdf").show();
                    getFilesTable(allData.files);
                    //alert(allData.result);
                    showPDF(allData.path);
                    var id = "d_" + allData.fileName.split(".")[0];
                    $(".pdfdiv").css("background-color", "white");
                    $("#" + id).css("background-color", "rgb(230, 241, 248)");
                    deleteFile();
                }
            } else {
                showMessage("没有数据！");
            }
        }).error(function () {
            showMessage("上传失败");
        });
    } else {
        showMessage("请上传pdf格式的文件");
    }
}

function getFilesTable(allFiles) {
    var allFilesVar = $("#allFiles");
    allFilesVar.empty();
    for (var i = 0; i < allFiles.length; i++) {
        var fileName = allFiles[i];
        var id = "d_" + fileName.split(".")[0];
        console.info(id);
        allFilesVar.append("<div class='pdfdiv' id='" + id + "' style='margin-top: 5px; cursor:pointer;' onclick='choosePDF(this)'>" +
            "&nbsp;&nbsp;<input class='pdfCB' type='checkbox' id='checkbox1' name='files" + i + "' value='" + fileName + "' style='display: none'/>" +
            "&nbsp;&nbsp;<i class='ace-icon fa fa-file-text grey'></i>&nbsp;&nbsp;" + fileName + "</div>");
    }
}

function choosePDF(divInfo) {
    var fileName = divInfo.id.slice(2) + ".pdf";
    console.info(fileName);
    fileName = encodeURI(encodeURI(fileName));
    var selectid = $.cookie('selected_id');
    if (selectid != null) {
        $.ajax({
            type: 'get',
            url: toolkitPath + '/raising/forward/surveyGraph/getPDF.do?selected_id=' + selectid + '&filename=' + fileName,
            cache: false,
            processData: false,
            contentType: false
        }).success(function (allData) {
            if (allData !== null) {
                if (allData.msg != null) {
                    showMessage(allData.msg)
                } else {
                    showPDF(allData.file);
                    var id = "d_" + allData.fileName.split(".")[0];
                    $(".pdfdiv").css("background-color", "white");
                    $("#" + id).css("background-color", "rgb(230, 241, 248)");
                    console.info(id);
                }
            }
        }).error(function () {
            showMessage("此项目未上传布点图，请上传!");
        });
    }
}

function doDelete() {
    $("#deleteCheck").css('display', 'block');
    $("#uploadBlackGround").css('display', 'block');
    var arr = [];
    $("input[type='checkbox']:checked").each(function (i) {
        arr[i] = $(this).val();
    });
    $("#deleteFilesLabel").html("确定删除 " + arr + " 吗？");
}

function cancelCheck() {
    $("#deleteCheck").css('display', 'none');
    $("#uploadBlackGround").css('display', 'none');
}

function deleteRight() {
    var arr = [];
    $("input[type='checkbox']:checked").each(function (i) {
        arr[i] = $(this).val();
    });
    if (Array.isArray(arr) && arr.length === 0) {
        cancelCheck();
        return showMessage("请选择需要删除的文件！");
    }
    var formData = new FormData();
    var selectID = $.cookie('selected_id');
    formData.append("selected_id", selectID);
    formData.append("fileNames", arr);
    $.ajax({
        type: 'post',
        url: toolkitPath + '/raising/forward/surveyGraph/deleteFiles.do',
        data: formData,
        cache: false,
        processData: false,
        contentType: false
    }).success(function (allData) {
        if (allData !== null) {
            if (allData.msg != null) {
                showMessage(allData.msg);
                if (allData.msg === "没有文件！") {
                    window.location.reload();
                }
            } else {
                showPDF(allData.lastFilePath);
                var id = "d_" + allData.fileName.split(".")[0];
                $(".pdfdiv").css("background-color", "white");
                $("#" + id).css("background-color", "rgb(230, 241, 248)");
                getFilesTable(allData.files);
                if (allData.errorDeleteFiles != null) {
                    showMessage(allData.errorDeleteFiles + "删除失败！");
                }
                cancelCheck();
                cancelDelete();
                console.info(id);
            }
        }
    }).error(function () {
        showMessage("数据库中没有数据，请上传!");
    });
}

function hideBootBox() {
    $("#uploadFile").hide();
    $("#uploadBlackGround").hide();
}

function openBootBox() {
    $("#uploadFile").show();
    $("#uploadBlackGround").show();
}

function cancelUpload() {
    deleteFile();
    hideBootBox();
}

function deleteFile() {
    $("#id-input-file-2").val("");
    $("#changeSpan").removeClass("ace-file-container selected").addClass("ace-file-container").attr("data-title", "选择");
    $("#fileName").attr("data-title", "请添加文件...");
    $("#uploadCoin").removeClass("ace-icon fa fa-file").addClass("ace-icon fa fa-upload");
    $("#removeFile").hide();

}

function checkFile(fileAttach) {
    var len = fileAttach.files.length;
    for (var i = 0; i < len; i++) {
        var temp = fileAttach.files[i].name;
        $("#changeSpan").removeClass("ace-file-container").addClass("ace-file-container selected").attr("data-title", "修改");
        $("#fileName").attr("data-title", temp);
        $("#uploadCoin").removeClass("ace-icon fa fa-upload").addClass("ace-icon fa fa-file");
        $("#removeFile").show();
        console.log("file name: " + temp);
    }
}

function showPDF(path) {
    console.info(path);
    path = encodeURI(encodeURI(path));
    console.info(toolkitPath);
    $.ajax({
        type: 'POST',
        url: toolkitPath + '/raising/forward/surveyGraph/preShow.do',
        dataType: 'JSON',
        data: {
            'path': path
        },
        success: function (data) {
            if (data) {
                var url = toolkitPath + '/bxui/pdfjs-2.0.943-dist/web/viewer.html?file=' + data.data;
                var new_url = url.replace(/http/g, "https");
                $('#pdfContainer').attr('src', new_url);
            }
        },
        error: function () {
            showMessage("PDF文件不存在");
        }
    });
}


function showDelete() {
    $(".pdfCB").show();
    $("#uploadDIV").css('display', 'none');
    $("#deleteDIV").css('display', 'block');
    $("#pdflabel").html("删除布点图");
    $(".pdfdiv").removeAttr("onclick");
}

function cancelDelete() {
    $(".pdfCB").hide();
    $("#pdflabel").html("选择布点图");
    $("#uploadDIV").css('display', 'block');
    $("#deleteDIV").css('display', 'none');
    $(".pdfdiv").attr("onclick", "choosePDF(this)");
    clearCheckBox();
}

function allChecked() {
    $("input[type='checkbox']").prop("checked", true);
}

function clearCheckBox() {
    $("input[type='checkbox']").removeAttrs("checked");
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