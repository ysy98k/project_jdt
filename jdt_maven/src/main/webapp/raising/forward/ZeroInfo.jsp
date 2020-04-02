<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="com.baosight.aas.auth.Constants" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html style="height: 100%;">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <title>零位配置</title>
    <%
        String path = request.getContextPath();
        String tenantid = request.getSession().getAttribute(Constants.SESSION_TENANTID_KEY) + "";
        String first = java.net.URLDecoder.decode(request.getParameter("firstLevel"),"UTF-8");
        String second = java.net.URLDecoder.decode(request.getParameter("secondLevel"),"UTF-8");
    %>

    <meta name="description" content="platForm Frameset"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <style>
        .table tr td:first-child {
            width: 70%;
        }
        .tableTile{
            line-height: 24px;
            font-size: 15px;
            padding-top: 7px;
            background-color: #2184C1!important;
            height:30px;
            min-height: 28px!important;
        }
    </style>
</head>

<body style="height: 100%;">
<%@ include file="/bxui/bxuihead.jsp" %>
<input type="hidden" value="<%=tenantid%>" id="tenantid" />
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css"/>



<div class="page-content" style="min-height:600px;height: 100%;">
    <!-- /sectionController:settings.box -->
    <div class="page-header">
        <h5>
            <%=first%>
            <small>
                &nbsp;
                <i class="ace-icon fa fa-angle-double-right"></i>
                &nbsp;&nbsp;
                <%=second%>
            </small>
        </h5>
        <div style="float: right;margin-top: -20px">
            <a href="javascript:insertPage()" ><span id = "cityname"></span><span id="linemessage"></span><span id="programmessage"></span></a>
        </div>

    </div>

    <div class="row" id="contentRow" style="height: calc(100% - 25px)" >
        <div  class="col-md-7" style="float: left">
            <div id="tableDiv1"  class="row">
                <div class="widget-box widget-color-blue col-md-6" style="float: left;margin-right: 10px;">
                    <div class="widget-header tableTile">
                        <div>
                            <span class="widget-title bigger lighter">
                                <i class="ace-icon fa fa-table"></i>
                                盾构尺寸
                            </span>
                        </div>
                    </div>
                    <!-- /sectionController:custom/widget-box.options -->
                    <div class="widget-body">
                    <div class="widget-main no-padding">
                        <table class="table table-striped table-bordered table-hover" style="table-layout:fixed;">
                            <tbody>
                            <tr>
                                <td align="center">切口到盾首距离A(mm)</td>
                                <td align="center" class="hidden-480">
                                    <span class="label label-inverse arrowed">
                                        ${zeroInfo == null || zeroInfo.TBM_A == null ? "一 一" :zeroInfo.TBM_A }
                                    </span>
                                </td>
                            </tr>

                            <tr>
                                <td align="center" class="">盾首至A0距离B(mm)</td>
                                <td align="center" class="hidden-480">
                                    <span class="label label-inverse arrowed">
                                        ${zeroInfo == null || zeroInfo.TBM_B == null ? "一 一" : zeroInfo.TBM_B }
                                    </span>
                                </td>
                            </tr>

                            <tr>
                                <td align="center">A0至盾尾距离C(mm)</td>
                                <td align="center" class="hidden-480">
                                    <span class="label label-inverse arrowed">
                                        ${zeroInfo == null || zeroInfo.TBM_C == null ? "一 一" : zeroInfo.TBM_C  }
                                    </span>
                                </td>
                            </tr>

                            <tr>
                                <td align="center">主动铰接前端面到A0点距离D(mm)</td>
                                <td align="center" class="hidden-480">
                                    <span class="label label-inverse arrowed">
                                        ${zeroInfo == null || zeroInfo.TBM_D == null ? "一 一" : zeroInfo.TBM_D  }
                                    </span>
                                </td>
                            </tr>

                            <tr>
                                <td align="center">A0点至主推油缸撑靴面距离E(mm)</td>
                                <td align="center" class="hidden-480">
                                    <span class="label label-inverse arrowed">
                                        ${zeroInfo == null || zeroInfo.TBM_E == null ? "一 一" : zeroInfo.TBM_E }
                                    </span>
                                </td>
                            </tr>

                            <tr>
                                <td align="center">A0点至间隙仪距离F(mm)</td>
                                <td align="center" class="hidden-480">
                                    <span class="label label-inverse arrowed">
                                        ${zeroInfo == null || zeroInfo.TBM_F == null ? "一 一" : zeroInfo.TBM_F }
                                    </span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                </div>

                <div class="widget-box widget-color-blue col-md-5" style="display: inline">
                    <!-- #sectionController:custom/widget-box.options -->
                    <div class="widget-header tableTile">
                        <div>
                            <span class="widget-title bigger lighter">
                                <i class="ace-icon fa fa-table"></i>
                                激光靶
                            </span>
                        </div>
                    </div>
                    <!-- /sectionController:custom/widget-box.options -->
                    <div class="widget-body">
                        <div class="widget-main no-padding">
                            <table class="table table-striped table-bordered table-hover" style="table-layout:fixed;">

                                <tbody>
                                <tr>
                                    <td align="center" style="width: 50%">X(mm)</td>
                                    <td align="center" class="hidden-480">
                                        <span class="label label-inverse arrowed">
                                            ${zeroInfo == null || zeroInfo.Zero_LT_X == null ? "一 一" : zeroInfo.Zero_LT_X }
                                        </span>
                                    </td>
                                </tr>

                                <tr>
                                    <td align="center" style="width: 50%">Y(mm)</td>
                                    <td align="center" class="hidden-480">
                                        <span class="label label-inverse arrowed">
                                            ${zeroInfo == null || zeroInfo.Zero_LT_Y == null ? "一 一" : zeroInfo.Zero_LT_Y }
                                        </span>
                                    </td>
                                </tr>

                                <tr>
                                    <td align="center" style="width: 50%">Z(mm)</td>
                                    <td align="center" class="hidden-480">
                                        <span class="label label-inverse arrowed">
                                            ${zeroInfo == null || zeroInfo.Zero_LT_Z == null ? "一 一" : zeroInfo.Zero_LT_Z }
                                        </span>
                                    </td>
                                </tr>

                                <tr>
                                    <td align="center" style="width: 50%">滚动角偏差(°)</td>
                                    <td align="center" class="hidden-480">
                                        <span class="label label-inverse arrowed">
                                            ${zeroInfo == null || zeroInfo.Zero_LT_RollAngle == null ? "一 一" : zeroInfo.Zero_LT_RollAngle }
                                        </span>
                                    </td>
                                </tr>

                                <tr>
                                    <td align="center" style="width: 50%">俯仰角偏差(°)</td>
                                    <td align="center" class="hidden-480">
                                        <span class="label label-inverse arrowed">
                                            ${zeroInfo == null || zeroInfo.Zero_LT_PitchAngle == null ? "一 一" : zeroInfo.Zero_LT_PitchAngle }
                                        </span>
                                    </td>
                                </tr>

                                <tr>
                                    <td align="center" style="width: 50%">方位角偏差(°)</td>
                                    <td align="center" class="hidden-480">
                                        <span class="label label-inverse arrowed">
                                            ${zeroInfo == null || zeroInfo.Zero_LT_AzimuthAngle == null ? "一 一" : zeroInfo.Zero_LT_AzimuthAngle}
                                        </span>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div id="tableDiv2" class="row" >
                <div class="widget-box widget-color-blue col-md-6" style="float: left;margin-right: 10px;">
                    <div class="widget-header tableTile">
                        <div>
                            <span class="widget-title bigger lighter">
                                <i class="ace-icon fa fa-table"></i>
                                主推油缸
                            </span>
                        </div>
                    </div>
                    <!-- /sectionController:custom/widget-box.options -->
                    <div class="widget-body">
                        <div class="widget-main no-padding">
                            <table class="table table-striped table-bordered table-hover" style="table-layout:fixed;">
                                <thead>
                                    <tr>
                                        <td align="center" style="width: 15%;">编号</td>
                                        <td align="center" style="width: 35%;">位置(°)</td>
                                        <td align="center" style="width: 35%;">修正量(mm)</td>
                                        <td align="center" style="width: 15%;">启用</td>
                                    </tr>
                                </thead>

                                <tbody>
                                <tr>
                                    <td align="center" >1Z</td>
                                    <td align="center">
                                        <span>
                                            ${zeroInfo == null || zeroInfo.TBM_Z1_Position == null ? "一 一" : zeroInfo.TBM_Z1_Position }
                                        </span>
                                    </td>
                                    <td align="center">
                                        <span class="label label-inverse arrowed">
                                            ${zeroInfo == null || zeroInfo.TBM_Z1_Correct == null ? "一 一" : zeroInfo.TBM_Z1_Correct }
                                        </span>
                                    </td>
                                    <td align="center">
                                        <c:choose>
                                            <c:when test="${zeroInfo != null && zeroInfo.TBM_Z1_Enable != null  && zeroInfo.TBM_Z1_Enable == 1}"><!-- 如果 -->
                                                <input type="checkbox" readonly="readonly" checked="checked" disabled="disabled" />
                                            </c:when>
                                            <c:otherwise><!-- 否则 -->
                                                <input type="checkbox" readonly="readonly" disabled="disabled" />
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>

                                <tr>
                                    <td align="center">2Z</td>
                                    <td align="center">
                                        <span>
                                            ${zeroInfo == null || zeroInfo.TBM_Z2_Position == null ? "一 一" : zeroInfo.TBM_Z2_Position }
                                        </span>
                                    </td>
                                    <td align="center">
                                        <span class="label label-inverse arrowed">
                                            ${zeroInfo == null || zeroInfo.TBM_Z2_Correct == null ? "一 一" : zeroInfo.TBM_Z2_Correct }
                                        </span>
                                    </td>
                                    <td align="center">
                                        <c:choose>
                                            <c:when test="${zeroInfo != null && zeroInfo.TBM_Z2_Enable != null && zeroInfo.TBM_Z2_Enable == 1}"><!-- 如果 -->
                                                <input type="checkbox" readonly="readonly" checked="checked" disabled="disabled" />
                                            </c:when>
                                            <c:otherwise><!-- 否则 -->
                                                <input type="checkbox" readonly="readonly" disabled="disabled" />
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>

                                <tr>
                                    <td align="center">3Z</td>
                                    <td align="center">
                                        <span>
                                            ${zeroInfo == null || zeroInfo.TBM_Z3_Position == null ? "一 一" : zeroInfo.TBM_Z3_Position }
                                        </span>
                                    </td>
                                    <td align="center">
                                        <span class="label label-inverse arrowed">
                                            ${zeroInfo == null || zeroInfo.TBM_Z3_Correct == null ? "一 一" : zeroInfo.TBM_Z3_Correct }
                                        </span>
                                    </td>
                                    <td align="center">
                                        <c:choose>
                                            <c:when test="${zeroInfo != null && zeroInfo.TBM_Z3_Enable != null && zeroInfo.TBM_Z3_Enable == 1}"><!-- 如果 -->
                                                <input type="checkbox" readonly="readonly" checked="checked" disabled="disabled" />
                                            </c:when>
                                            <c:otherwise><!-- 否则 -->
                                                <input type="checkbox" readonly="readonly" disabled="disabled" />
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>

                                <tr>
                                    <td align="center">4Z</td>
                                    <td align="center">
                                        <span>
                                            ${zeroInfo == null || zeroInfo.TBM_Z4_Position == null ? "一 一" : zeroInfo.TBM_Z4_Position }
                                        </span>
                                    </td>
                                    <td align="center">
                                        <span class="label label-inverse arrowed">
                                            ${zeroInfo == null || zeroInfo.TBM_Z4_Correct == null ? "一 一" : zeroInfo.TBM_Z4_Correct }
                                        </span>
                                    </td>
                                    <td align="center">
                                        <c:choose>
                                            <c:when test="${zeroInfo != null && zeroInfo.TBM_Z4_Enable != null && zeroInfo.TBM_Z4_Enable == 1}"><!-- 如果 -->
                                                <input type="checkbox" readonly="readonly" checked="checked" disabled="disabled" />
                                            </c:when>
                                            <c:otherwise><!-- 否则 -->
                                                <input type="checkbox" readonly="readonly" disabled="disabled" />
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="center" colspan="2">安装半径(mm)</td>
                                    <td align="center" colspan="2">
                                        ${zeroInfo == null || zeroInfo.TBM_Z_Radius == null ? "一 一" : zeroInfo.TBM_Z_Radius}
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="widget-box widget-color-blue col-md-5" style="display: inline">
                    <!-- #sectionController:custom/widget-box.options -->
                    <div class="widget-header tableTile">
                        <div>
                            <span class="widget-title bigger lighter">
                                <i class="ace-icon fa fa-table"></i>
                                铰接油缸
                            </span>
                        </div>
                    </div>
                    <!-- /sectionController:custom/widget-box.options -->
                    <div class="widget-body">
                        <div class="widget-main no-padding">
                            <table class="table table-striped table-bordered table-hover" style="table-layout:fixed;">
                                <thead>
                                    <tr>
                                        <td align="center" style="width: 15%;">编号</td>
                                        <td align="center" style="width: 34%;">位置(°)</td>
                                        <td align="center" style="width: 34%;">修正量(mm)</td>
                                        <td align="center" style="width: 17%;">启用</td>
                                    </tr>
                                </thead>

                                <tbody>
                                    <tr>
                                        <td align="center">1J</td>
                                        <td align="center">
                                            <span class="label label-inverse arrowed">
                                                ${zeroInfo == null || zeroInfo.TBM_J1_Position == null ? "一 一" : zeroInfo.TBM_J1_Position }
                                            </span>
                                        </td>
                                        <td align="center">
                                            <span class="label label-inverse arrowed">
                                                ${zeroInfo == null || zeroInfo.TBM_J1_Correct == null ? "一 一" : zeroInfo.TBM_J1_Correct }
                                            </span>
                                        </td>
                                        <td align="center">
                                            <c:choose>
                                                <c:when test="${zeroInfo.TBM_J1_Enable == 1}"><!-- 如果 -->
                                                    <input type="checkbox"  checked="checked" disabled="disabled" />
                                                </c:when>
                                                <c:when test="${zeroInfo != null && zeroInfo.TBM_J1_Enable != null && zeroInfo.TBM_J1_Enable == 1}"><!-- 如果 -->
                                                    <input type="checkbox"  checked="checked" disabled="disabled" />
                                                </c:when>
                                                <c:otherwise><!-- 否则 -->
                                                    <input type="checkbox" disabled="disabled" />
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td align="center">2J</td>
                                        <td align="center">
                                            <span class="label label-inverse arrowed">
                                                ${zeroInfo == null || zeroInfo.TBM_J2_Position == null ? "一 一" : zeroInfo.TBM_J2_Position }
                                            </span>
                                        </td>
                                        <td align="center">
                                            <span class="label label-inverse arrowed">
                                                ${zeroInfo == null || zeroInfo.TBM_J2_Correct == null ? "一 一" : zeroInfo.TBM_J2_Correct }
                                            </span>
                                        </td>
                                        <td align="center">
                                            <c:choose>
                                                <c:when test="${zeroInfo != null && zeroInfo.TBM_J2_Enable != null && zeroInfo.TBM_J2_Enable == 1}"><!-- 如果 -->
                                                    <input type="checkbox" readonly="readonly" checked="checked" disabled="disabled" />
                                                </c:when>
                                                <c:otherwise><!-- 否则 -->
                                                    <input type="checkbox" readonly="readonly" disabled="disabled" />
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td align="center">3J</td>
                                        <td align="center">
                                            <span class="label label-inverse arrowed">
                                                ${zeroInfo == null || zeroInfo.TBM_J3_Position == null ? "一 一" : zeroInfo.TBM_J3_Position }
                                            </span>
                                        </td>
                                        <td align="center">
                                            <span class="label label-inverse arrowed">
                                                ${zeroInfo == null || zeroInfo.TBM_J3_Correct == null ? "一 一" : zeroInfo.TBM_J3_Correct }
                                            </span>
                                        </td>
                                        <td align="center">
                                            <c:choose>
                                                <c:when test="${zeroInfo != null && zeroInfo.TBM_J3_Enable != null && zeroInfo.TBM_J3_Enable == 1}"><!-- 如果 -->
                                                    <input type="checkbox" readonly="readonly" checked="checked" disabled="disabled" />
                                                </c:when>
                                                <c:otherwise><!-- 否则 -->
                                                    <input type="checkbox" readonly="readonly" disabled="disabled" />
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td align="center">4J</td>
                                        <td align="center">
                                            <span class="label label-inverse arrowed">
                                                ${zeroInfo == null || zeroInfo.TBM_J4_Position == null ? "一 一" : zeroInfo.TBM_J4_Position }
                                            </span>
                                        </td>
                                        <td align="center">
                                            <span class="label label-inverse arrowed">
                                                ${zeroInfo == null || zeroInfo.TBM_J4_Correct == null ? "一 一" : zeroInfo.TBM_J4_Correct }
                                            </span>
                                        </td>
                                        <td align="center">
                                            <c:choose>
                                                <c:when test="${zeroInfo != null && zeroInfo.TBM_J4_Enable != null && zeroInfo.TBM_J4_Enable == 1}"><!-- 如果 -->
                                                    <input type="checkbox" readonly="readonly" checked="checked" disabled="disabled" />
                                                </c:when>
                                                <c:otherwise><!-- 否则 -->
                                                    <input type="checkbox" readonly="readonly" disabled="disabled" />
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="center" colspan="2">安装半径(mm)</td>
                                        <td align="center" colspan="2">
                                            ${zeroInfo == null || zeroInfo.TBM_J_Radius == null ? "一 一" : zeroInfo.TBM_J_Radius}
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <img id="img1" class="col-md-5" style="height:10px;display: none"  src="<%=path%>/raising/image/tbmPositive.png" />
        <img id="img2" class="col-md-5" style="height:10px;display: none"  src="<%=path%>/raising/image/tbmPassive.png" />
    </div>
</div>
<!-- ----------------------------------信息弹出框---------------------------------------------------------->
<div id="dialog-message" class="hide">
    <p class="bigger-110 bolder center grey">
        <br/>
        <b id="dialogInfo"></b>
    </p>
</div>
<div id="detail" class="hide" style="overflow: hidden">
    <div type="text" id = "div_city" name = "city" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
    <div type="text" id = "div_line" name = "line" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
    <div type="text" id = "div_program" name = "program" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
</div>

</body>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-form.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxdialog.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxcombobox.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/selectLine.js"></script>
<script>
    function initHeight(){
        var contentHeight = $("#contentRow").height();
        $("#img1").height((contentHeight-15)/2) ;
        $("#img2").height((contentHeight-15)/2);
        $("#img1").show();
        $("#img2").show();

        var img1 = $("#img1").height();
        var table1 = $("#tableDiv1").height();
        if(img1 > table1){
            var num = img1 - table1;
            $("#tableDiv2").css("margin-top",num);
        }

        /*var num = (img1 + img2) - (table1 + table2)-60;
        if(num < 0){
            return;
        }*/


    }
    initHeight();
</script>

</html>
