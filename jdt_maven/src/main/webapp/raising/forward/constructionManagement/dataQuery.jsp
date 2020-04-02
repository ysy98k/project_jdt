<%--
  Created by IntelliJ IDEA.
  User: ysy
  Date: 2018/8/8
  Time: 11:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.baosight.aas.auth.Constants" %>
<html>
<head>
    <title>数据查询</title>
    <%
        String path = request.getContextPath();
        String tenantid = request.getSession().getAttribute(Constants.SESSION_TENANTID_KEY) + "";
        String first = java.net.URLDecoder.decode(request.getParameter("firstLevel"),"UTF-8");
        String second = java.net.URLDecoder.decode(request.getParameter("secondLevel"),"UTF-8");
    %>

    <meta name="description" content="platForm Frameset"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <link rel="stylesheet" href="<%=path%>/bxui/daterangepicker/css/daterangepicker.css"/>
    <link rel="stylesheet" href="<%=path%>/bxui/jquery-plugin/css/chosen.css">

    <style>
        .bai{
            background-color:#FFFFFF;
        }
        #father{
            position:inherit;
            min-height:600px;
            margin: 0px;
            padding-bottom: 8px;
        }
        #tbmRecord td{
            font-size: 13px;
        }

        #home td
        {
            border: 1px solid #E1E1E1;
            text-align: center;
            background-clip: padding-box;
            font-size: 13px;
            padding: 3px 0px;
        }
        #tableContent{
        }
        .checkboxType{
            display: inline-block!important;
            min-width: 30px;
            max-width: 120px;
        }
        /*环号表，下拉框宽度*/
        #ringNum_chosen{
            width: 150px!important;
        }
        /*条件提示信息*/
        .prompt{
            display: inline-block;
            font-size: 12px;

        }
        .condition{
            margin-bottom: 10px;
        }
        .title{
            padding-top: 3px;
        }
        .center{
            margin: 0px 10px;
        }
        /*表格*/
        .btn, .btn-default, .btn-default:focus, .btn:focus{
            background-color:rgb(33, 132, 193)!important;
            border-color: #2184c1;
        }
        .ui-jqgrid tr.jqgrow td {
            font-size:13px;
            text-align: center!important;
        }
        .alert{
            padding: 7px 15px!important;
        }
        /*遮罩*/
        .zIndexLoad{
            z-index: 2;
        }



    </style>

</head>

<body class="bai">
<%@ include file="/bxui/bxuihead.jsp" %>

<script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/moment.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/daterangepicker.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raisingui/busy-load/app.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxdialog.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-elementui.js"></script>

<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery-plugin/js/chosen.jquery.js"></script>


<script type="text/javascript" src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>


<input type="hidden" value="<%=tenantid%>" id="tenantid" />
<div id="father" class="page-content" style="height: 100px;">
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
    <!-- 内容-->
    <div class="row">
        <div class="col-xs-12">
            <!-- #sectionController:elements.tab -->
            <div class="tabbable">
                <ul class="nav nav-tabs" id="myTab">
                    <li class="active">
                        <a data-toggle="tab" href="#ring" onclick="tableConvert('ring')">
                            <i class="green ace-icon fa fa-home bigger-120"></i>
                            环信息
                        </a>
                    </li>
                    <li>
                        <a data-toggle="tab" href="#time" onclick="tableConvert('time')">
                            <i class="green ace-icon fa fa-camera-retro bigger-120"></i>
                            时间信息
                        </a>
                    </li>
                    <li>
                        <a data-toggle="tab" href="#mileage" onclick="tableConvert('mileage')">
                            <i class="green ace-icon fa fa-fire bigger-120"></i>
                            里程信息
                        </a>
                    </li>
                    <li>
                        <a data-toggle="tab" href="#trip" onclick="tableConvert('trip')">
                            <i class="green ace-icon fa fa-leaf bigger-120"></i>
                            管理行程信息
                        </a>
                    </li>
                </ul>

                <div id="tableContent" class="tab-content" style="height: 750px;">

                    <div id="ring" class="tab-pane in active">

                        <div class="col-xs-12 searchArea" id="queryarea1">
                            <div id="alertdiv1" class="alertdiv"></div>
                        </div>

                        <div class="col-xs-12 condition">
                            <div class="col-xs-12 col-md-12" >
                                <div class="checkbox checkboxType">
                                <label>
                                    <input name="ring-checkbox" type="checkbox" checked class="ace" >
                                    <span class="lbl"> 土压</span>
                                </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="ring-checkbox" type="checkbox" checked class="ace">
                                        <span class="lbl"> 刀盘</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="ring-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 主驱动电机</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="ring-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 推进</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="ring-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 螺旋输送机</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="ring-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 铰接</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="ring-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 盾尾密封</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="ring-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 注浆</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="ring-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 渣土改良</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="ring-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 工业水系统</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="ring-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 液压站</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="ring-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 压缩空气</span>
                                    </label>
                                </div>
                            </div>

                            <div class="col-xs-12">
                                <div class="col-xs-12  col-md-6" style="max-width: 530px;">
                                    <div class="prompt" style="margin-left: 60px;" id="minRing"></div>
                                    <div class="prompt" style="margin-left: 170px;" id="maxRing"></div>
                                </div>
                            </div>

                            <div class="col-xs-12">
                                <div class="col-xs-12 col-md-6" style="max-width: 530px;">
                                    <label class="labelThree" for="startRingNum" style="margin-right: 20px;">环数</label>
                                    <input type="text" id="startRingNum" data-bxwidget class="inputThree" />
                                    <span></span>
                                    <label class="labelThree center">至</label>
                                    <input type="text" id="endRingNum" data-bxwidget class="inputThree" />
                                    <span></span>
                                </div>
                                <div class="col-xs-12 col-md-2" style="max-width: 110px;">
                                    <button class="btn btn-sm btn-block" style="background-color: #2184c1!important;border-color:#2184c1!important;" onclick="on_query_click('ring');" >
                                        <div class="ace-icon fa fa-search"></div>
                                        <span>查询</span>
                                    </button>
                                </div>
                                <div class="col-xs-12 col-md-2" style="max-width: 110px;">
                                    <button class="btn btn-sm btn-block" style="background-color: #2184c1!important;border-color:#2184c1!important;" onclick="download('ring');" >
                                        <div class="ace-icon fa fa-download"></div>
                                        <span>下载</span>
                                    </button>
                                </div>
                            </div>

                        </div>

                        <div class="col-xs-12">
                            <div id="tbmRecord1"></div>
                        </div>
                    </div>

                    <div id="time" class="tab-pane">

                        <div class="col-xs-12 searchArea" id="queryarea2">
                            <div id="alertdiv2" class="alertdiv"></div>
                        </div>

                        <div class="col-xs-12 ">
                            <div class="checkbox checkboxType">
                                <label>
                                    <input name="time-checkbox" type="checkbox" checked class="ace" >
                                    <span class="lbl"> 土压</span>
                                </label>
                            </div>
                            <div class="checkbox checkboxType">
                                <label>
                                    <input name="time-checkbox" type="checkbox" checked class="ace">
                                    <span class="lbl"> 刀盘</span>
                                </label>
                            </div>
                            <div class="checkbox checkboxType">
                                <label>
                                    <input name="time-checkbox" type="checkbox" class="ace">
                                    <span class="lbl"> 主驱动电机</span>
                                </label>
                            </div>
                            <div class="checkbox checkboxType">
                                <label>
                                    <input name="time-checkbox" type="checkbox" class="ace">
                                    <span class="lbl"> 推进</span>
                                </label>
                            </div>
                            <div class="checkbox checkboxType">
                                <label>
                                    <input name="time-checkbox" type="checkbox" class="ace">
                                    <span class="lbl"> 螺旋输送机</span>
                                </label>
                            </div>
                            <div class="checkbox checkboxType">
                                <label>
                                    <input name="time-checkbox" type="checkbox" class="ace">
                                    <span class="lbl"> 铰接</span>
                                </label>
                            </div>
                            <div class="checkbox checkboxType">
                                <label>
                                    <input name="time-checkbox" type="checkbox" class="ace">
                                    <span class="lbl"> 盾尾密封</span>
                                </label>
                            </div>
                            <div class="checkbox checkboxType">
                                <label>
                                    <input name="time-checkbox" type="checkbox" class="ace">
                                    <span class="lbl"> 注浆</span>
                                </label>
                            </div>
                            <div class="checkbox checkboxType">
                                <label>
                                    <input name="time-checkbox" type="checkbox" class="ace">
                                    <span class="lbl"> 渣土改良</span>
                                </label>
                            </div>
                            <div class="checkbox checkboxType">
                                <label>
                                    <input name="time-checkbox" type="checkbox" class="ace">
                                    <span class="lbl"> 工业水系统</span>
                                </label>
                            </div>
                            <div class="checkbox checkboxType">
                                <label>
                                    <input name="time-checkbox" type="checkbox" class="ace">
                                    <span class="lbl"> 液压站</span>
                                </label>
                            </div>
                            <div class="checkbox checkboxType">
                                <label>
                                    <input name="time-checkbox" type="checkbox" class="ace">
                                    <span class="lbl"> 压缩空气</span>
                                </label>
                            </div>


                        </div>

                        <div class="col-xs-12">
                            <div class="col-xs-12  col-md-6" style="max-width: 530px;">
                                <div class="prompt" style="margin-left: 60px;" id="minTime"></div>
                                <div class="prompt" style="margin-left: 55px;" id="maxTime"></div>
                            </div>
                        </div>

                        <div class="col-xs-12 condition">
                            <div class="col-xs-12 col-md-6" style="max-width: 530px;">
                                <label class="labelThree" for="inqu_status-startTime" style="margin-right: 20px;">日期</label>
                                <input type="text" id="inqu_status-startTime" data-bxwidget class="inputThree" />
                                <span></span>
                                <label class="labelThree center">至</label>
                                <input type="text" id="inqu_status-endTime" data-bxwidget class="inputThree" />
                                <span></span>
                            </div>
                            <div class="col-xs-12 col-md-2" style="max-width: 110px;">
                                <button class="btn btn-sm btn-block" onclick="on_query_click('time');" style="background-color: #2184c1!important;border-color:#2184c1!important;" >
                                    <div class="ace-icon fa fa-search"></div>
                                    <span>查询</span>
                                </button>
                            </div>
                            <div class="col-xs-12 col-md-2" style="max-width: 110px;">
                                <button class="btn btn-sm btn-block" style="background-color: #2184c1!important;border-color:#2184c1!important;" onclick="download('time');" >
                                    <div class="ace-icon fa fa-download"></div>
                                    <span>下载</span>
                                </button>
                            </div>
                        </div>


                        <div class="col-xs-12">
                            <div id="tbmRecord2"></div>
                        </div>
                    </div>

                    <div id="mileage" class="tab-pane">
                        <div class="col-xs-12 searchArea" id="queryarea3">
                            <div id="alertdiv3" class="alertdiv"></div>
                        </div>

                        <div class="col-xs-12 condition">
                            <div class="col-xs-12 col-md-12" >
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="mileage-checkbox" type="checkbox" checked class="ace" >
                                        <span class="lbl"> 土压</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="mileage-checkbox" type="checkbox" checked class="ace">
                                        <span class="lbl"> 刀盘</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="mileage-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 主驱动电机</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="mileage-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 推进</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="mileage-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 螺旋输送机</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="mileage-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 铰接</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="mileage-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 盾尾密封</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="mileage-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 注浆</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="mileage-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 渣土改良</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="mileage-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 工业水系统</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="mileage-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 液压站</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="mileage-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 压缩空气</span>
                                    </label>
                                </div>
                            </div>

                            <div class="col-xs-12">
                                <div class="col-xs-12  col-md-6" style="max-width: 530px;">
                                    <div class="prompt" style="margin-left: 60px;" id="minMileage"></div>
                                    <div class="prompt" style="margin-left: 160px;" id="maxMileage"></div>
                                </div>
                            </div>

                            <div class="col-xs-12">
                                <div class="col-xs-12 col-md-6" style="max-width: 530px;">
                                    <label class="labelThree" for="startRingNum" style="margin-right: 20px;">里程</label>
                                    <input type="text" id="startMileage" data-bxwidget class="inputThree" />
                                    <span></span>
                                    <label class="labelThree center">至</label>
                                    <input type="text" id="endMileage" data-bxwidget class="inputThree" />
                                    <span></span>
                                </div>
                                <div class="col-xs-12 col-md-2" style="max-width: 110px;">
                                    <button class="btn btn-sm btn-block" style="background-color: #2184c1!important;border-color:#2184c1!important;" onclick="on_query_click('mileage');" >
                                        <div class="ace-icon fa fa-search"></div>
                                        <span>查询</span>
                                    </button>
                                </div>
                                <div class="col-xs-12 col-md-2" style="max-width: 110px;">
                                    <button class="btn btn-sm btn-block" style="background-color: #2184c1!important;border-color:#2184c1!important;" onclick="download('mileage');" >
                                        <div class="ace-icon fa fa-download"></div>
                                        <span>下载</span>
                                    </button>
                                </div>
                            </div>
                        </div>


                        <div class="col-xs-12">
                            <div id="tbmRecord3"></div>
                        </div>
                    </div>

                    <div id="trip" class="tab-pane">
                        <div class="col-xs-12 searchArea" id="queryarea4">
                            <div id="alertdiv4" class="alertdiv"></div>
                        </div>

                        <div class="col-xs-12 condition" >
                            <div class="col-xs-12 col-md-12" >
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="trip-checkbox" type="checkbox" checked class="ace" >
                                        <span class="lbl"> 土压</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="trip-checkbox" type="checkbox" checked class="ace">
                                        <span class="lbl"> 刀盘</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="trip-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 主驱动电机</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="trip-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 推进</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="trip-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 螺旋输送机</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="trip-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 铰接</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="trip-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 盾尾密封</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="trip-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 注浆</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="trip-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 渣土改良</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="trip-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 工业水系统</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="trip-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 液压站</span>
                                    </label>
                                </div>
                                <div class="checkbox checkboxType">
                                    <label>
                                        <input name="trip-checkbox" type="checkbox" class="ace">
                                        <span class="lbl"> 压缩空气</span>
                                    </label>
                                </div>
                            </div>

                            <div class="col-xs-12 col-md-2 title" style="margin-top: 10px;">
                                <div class="col-md-4 " style="padding-top: 5px;">
                                    <label for="ringNum" style=""><span>环号：</span></label>
                                </div>
                                <select class="chosen-select " id="ringNum">
                                    <option value="" >未选择</option>
                                </select>
                            </div>

                            <div class="col-xs-12 col-md-1" style="margin-top: 10px;">
                                <button class="btn btn-sm btn-block" style="background-color: #2184c1!important;border-color:#2184c1!important;" onclick="on_query_click('trip');" >
                                    <div class="ace-icon fa fa-search"></div>
                                    <span>查询</span>
                                </button>
                            </div>
                            <div class="col-xs-12 col-md-2" style="margin-top: 10px;max-width: 110px;">
                                <button class="btn btn-sm btn-block" style="background-color: #2184c1!important;border-color:#2184c1!important;" onclick="download('trip');" >
                                    <div class="ace-icon fa fa-download"></div>
                                    <span>下载</span>
                                </button>
                            </div>
                        </div>

                        <div class="col-xs-12">
                            <div id="tbmRecord4"></div>
                        </div>
                    </div>
                </div>
            </div>

        </div><!-- /.col -->

    </div>
</div>

<!------------------------------------信息弹出框---------------------------------------------------------->
<div id="updateConfirm" class="hide">
    <span>确认修改么？</span>
</div>
<div class="alertDiv hide"></div>



<div id="detail" class="hide" style="overflow: hidden">
    <div type="text" id = "div_city" name = "city" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
    <div type="text" id = "div_line" name = "line" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
    <div type="text" id = "div_program" name = "program" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
</div>

<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/selectLine.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/constructionManagement/dataQuery.js"></script>


</body>
</html>
