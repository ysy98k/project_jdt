<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <title>宝信大数据应用开发平台-管理控制台</title>
    <%
        String path = request.getContextPath();
    %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="description" content="platForm Frameset"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <%--
        <script type="text/javascript" src="<%=path%>/bxui/baosight-require.js"></script>
    --%>
    <link rel="stylesheet" href="<%=path%>/df/console/css/style-frame-index.css"/>
</head>

<body style="min-width: 1000px;min-height:450px;overflow:auto">

<%--<jsp:include page="/TBD/Section/lineChange.jsp"></jsp:include>--%>
<div class="row" id="list-section" style="margin-top: 25px">
</div>
</body>
<%@ include file="/bxui/bxuihead.jsp" %>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/JavaScript">
    $(document).ready(function () {
        var curHeight = document.documentElement.scrollHeight;
        $("#myCarousel").height(curHeight - 220);
        $(".figure1").animate({
            opacity: '1'
        }, 1000, function () {
            $(".carousel-caption").animate({
                left: '0',
                opacity: '1'
            }, 200);
        });
        var callback = {
            onSuccess: function (paramJsonObj) {
                $.cookie('selected_id', 3, {path: "/"});

                $.cookie('selected_name', "西京路-下行线", {path: "/"});
                var data = paramJsonObj.rows;
                for (var i = 0; i < data.length; i++) {
                    $("#list-backstage").append(
                        "<div class=\"col-md-3\" style='margin-bottom: 10px'>\n" +
                        "<div class=\"widget-box widget-color-blue light-border ui-sortable-handle\">\n" +
                        "    <div class=\"widget-header\">\n" +
                        "        <div class=\"widget-title smaller\" >\n" + "<a class=\"backstage-link\"  herf=\"#\"  style=\"color: #ffffff\" menuePara=\" \" cvsUrl=\"" + data[i].cvsUrl + "\">\n" + data[i].sectionName + "</a>" + "<br /><div class=\"project\">" + data[i].projectName + "</div></div>" +
                        "\n" +
                        "        <div class=\"widget-toolbar\">\n" +
                        /*"            <span class=\"badge badge-danger\">Info</span>\n" +*/
                        "        </div>\n" +
                        "    </div>\n" +
                        "\n" +
                        "    <div class=\"widget-body\">\n" +
                        "        <div class=\"widget-main padding-6\" id=\"list-backstage\">\n" +
                        "            <div class=\"alert alert-info\" > " + data[i].createTime + "<br />管理行程:${mileage2}</div>\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "</div>" +
                        "</div>");

                }
                $(".backstage-link").bind('click', function () {
                    parent.document.getElementById("contentFrame").src = "<%=path%>>/raising/frame.do";
                    $("[data-menucode='infoWidgetShow']", parent.document).attr({class: "hover tabSelColor"});
                    $("[data-menucode='infoWidgetShow'] a", parent.document).attr({class: "dropdown-toggle"});
                    $("[data-menucode='chartShow']", parent.document).attr({class: "hover tabSelColor active"});
                    $("[data-menucode='chartShow'] a", parent.document).attr({class: "dropdown-toggle tabSel"});
                    $.cookie('selected_name', $(this).text(), {path: "/"});//全局作用域
                    // $.cookie('selected_name');
                    $.cookie('selected_url', $(this).attr("cvsUrl"), {path: "/"});
                    /*e.CVSUrl;
                    tab页打开区间
                    iframeurl = e.CVSUrl;*/
                });
            }
        };
        /*var paramJsonObj = {"curPage":1,"curRowNum":10};//设置查询参数等*/
        var paramJsonObj = {};
        AjaxCommunicator.ajaxRequest('/sectionManage.do?method=query', 'POST', paramJsonObj, callback,true);


    });


    function readyToOpenForm(node, openLink) {
        if (openLink == "embed") {
            var barMenu = parent.document.getElementById("menuParent_chartShow0");
            $(barMenu).click();
            $(barMenu).find("a").click();
        } else if (openLink == "self") {
            parent.location.href = node;
        } else {
            window.open(node);
        }
    }
</script>
</html>
