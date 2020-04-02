<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ysy
  Date: 2018/1/17
  Time: 15:27
  Description:
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>

</head>
<body>
<%@ include file="/bxui/bxuihead.jsp" %>


<div style="min-height:10px "></div>
<div class="row" id="list-section" style="margin-top: 25px">
</div>
<div id="test"></div>
</div>
</div>

<%--<div class="widget-box widget-color-blue light-border ui-sortable-handle">
    <div class="widget-header">
        <h5 class="widget-title smaller" ></h5>

        <div class="widget-toolbar">
            <span class="badge badge-danger">Info</span>
        </div>
    </div>

    <div class="widget-body">
        <div class="widget-main padding-6" id="list-backstage">
            <div class="alert alert-info" > </div>
        </div>
    </div>
</div>--%>


</body>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-form.js"></script>
<script type="text/javascript">
    var callback = {
        onSuccess: function (paramJsonObj) {
            var data = paramJsonObj;
            for (var i = 0; i < data.length; i++) {
                $("#list-backstage").append(
                    "<div class=\"col-md-3\" style='margin-bottom: 10px'>\n" +
                    "<div class=\"widget-box widget-color-blue light-border ui-sortable-handle\">\n" +
                    "    <div class=\"widget-header\">\n" +
                    "        <h5 class=\"widget-title smaller\" >\n" + "<a class=\"backstage-link\"  herf=\"#\"  style=\"color: #ffffff\" menuePara=\" \" cvsUrl=\"" + data[i].cvsUrl + "\">\n" + data[i].sectionName + "</a>" + "</h5>" + "<br />" +
                    "\n" +
                    "        <div class=\"widget-toolbar\">\n" +
                    /*"            <span class=\"badge badge-danger\">Info</span>\n" +*/
                    "        </div>\n" +
                    "    </div>\n" +
                    "\n" +
                    "    <div class=\"widget-body\">\n" +
                    "        <div class=\"widget-main padding-6\" id=\"list-backstage\">\n" +
                    "            <div class=\"alert alert-info\" > " + data[i].createTime + "<br />管理行程:2000</div>\n" +
                    "        </div>\n" +
                    "    </div>\n" +
                    "</div>" +
                    "</div>");

            }
            $("#test").bind('click', clickFunction(this));
        }
    };
    /*var paramJsonObj = {"curPage":1,"curRowNum":10};//设置查询参数等*/
    var paramJsonObj = {};
    AjaxCommunicator.ajaxRequest('/ListQuery.do?method=query', 'POST', paramJsonObj, callback,true);

    function clickFunction(e) {
        var a = 0;
        /*parent.document.getElementById("contentFrame").src ="/raising/frame.do";*/
        /*e.CVSUrl;*/
        // tab页打开区间
        // iframeurl = e.CVSUrl;
    }

    /* function onclickChangeIframe(path) {
         parent.document.getElementById("contentFrame").src =path;
     }*/
</script>
</html>
