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
    <title>盾构机信息</title>
    <%
        String path = request.getContextPath();
        String tenantid = request.getSession().getAttribute(Constants.SESSION_TENANTID_KEY) + "";
        String first = java.net.URLDecoder.decode(request.getParameter("firstLevel"),"UTF-8");
        String second = java.net.URLDecoder.decode(request.getParameter("secondLevel"),"UTF-8");
    %>

    <meta name="description" content="platForm Frameset"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <link rel="stylesheet" href="<%=path%>/bxui/jquery-plugin/css/chosen.css">
    <style>
        [v-cloak]{ display: none; }
        .bai{
            background-color:#FFFFFF;
        }
        #father{
            background: -webkit-linear-gradient(#105899,#3C84C5, #105899); /* Safari 5.1 - 6.0 */
            background: -o-linear-gradient(#105899,#3C84C5, #105899); /* Opera 11.1 - 12.0 */
            background: -moz-linear-gradient(#105899,#3C84C5, #105899); /* Firefox 3.6 - 15 */
            background: linear-gradient(#105899,#3C84C5, #105899);!important; /* 标准的语法 */
            color:white!important;
            min-height:600px;
            height: 100%;
            position:inherit;
        }
        .hundred{
            height: 100%;
        }
        /*头部*/
        .page-header{
            background-color: white;
            margin: -8px -20px 0px!important;
            padding:8px 20px 9px 20px;
        }
        .page-header h5,.page-header h5 small,.page-header div a{
            color:white!important;
        }
        /*头部*/
        /*导航栏*/
        .tab-content{
            border-width: 0px!important;
        }
        .nav-tabs > li > a{
            background-color: transparent!important;
            color: white!important;
        }
        .nav-tabs > li.active > a{
            background-color: transparent!important;
            color: rgb(255, 180, 50)!important;
        }

        /*导航栏*/
        /*环选择框*/
        li.active-result{
            color: rgb(68, 68, 68);
        }
        div.chosen-container{
            width: 100px!important;

        }
        /**/
        /*行程*/
        .curMs{
            border: 1px solid black;
            height: 30px;
            line-height: 30px;
            width: 70px;
            text-align: center;
            background-color: black;
            color: #00D505;
        }
        /*soil*/
        #soilRingTable{

        }
        #soilRing td,#soilRings td,#segment td{
            border: 1px solid white;
            color: white!important;
            font-size:13px!important;
            height:29px;
            padding: 5px 10px;
        }
        #soilRings td{
            padding: 7px 20px;
        }
        /*soil*/
        /**/
        .btnDefault{
            min-width: 100px;
        }
        /**/

    </style>

</head>

<body class="bai">
<%@ include file="/bxui/bxuihead.jsp" %>

<script type="text/javascript" src="<%=toolkitPath%>/raisingui/busy-load/app.min.js"></script>
<script src="https://github.com/niklasvh/html2canvas/releases/download/v1.0.0-rc.1/html2canvas.min.js"></script>
<script src="https://cdn.bootcss.com/jspdf/1.3.4/jspdf.debug.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/echarts3/echarts.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/vue/vue.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>

<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxdialog.js"></script>

<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-elementui.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery-plugin/js/chosen.jquery.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>

<style>
    .blue-skin .btn-block, .blue-skin .btn-skinColor{
        background-color: #48C275!important;
        border-color: #48C275!important;
    }
</style>

<input type="hidden" value="<%=tenantid%>" id="tenantid" />
<div id="father" class="page-content" style="position:inherit;min-height: 864px;min-width: 1600px;">
    <div class="page-header">
        <h5 style="color: rgb(38, 121, 181)!important;">
            施工管理管理
            <small style="color:rgb(153, 153, 153)!important;">
                &nbsp;
                <i class="ace-icon fa fa-angle-double-right"></i>
                &nbsp;&nbsp;
                报表导出
            </small>
        </h5>
        <div style="float: right;margin-top: -15px;">
            <a href="javascript:insertPage()" style="color: rgb(38, 121, 181)!important;" ><span id = "cityname"></span><span id="linemessage"></span><span id="programmessage"></span></a>
        </div>
    </div>
    <!-- 内容-->
    <div class="row" style="height: 98%;">
        <div class="col-xs-12 hundred">
            <!-- #sectionController:elements.tab -->
            <div class="tabbable hundred">
                <ul class="nav nav-tabs" id="myTab">
                    <li class="active">
                        <a data-toggle="tab" href="#ring" onclick="getData('ring')">
                            <i class="green ace-icon fa fa-home bigger-120"></i>
                            环报表
                        </a>
                    </li>
                    <li>
                        <a data-toggle="tab" href="#soilRing" onclick="getData('soilRing')">
                            <i class="green ace-icon fa fa-camera-retro bigger-120"></i>
                            排土量（单环）
                        </a>
                    </li>
                    <li >
                        <a data-toggle="tab" href="#soilRings" onclick="getData('soilRings')">
                            <i class="green ace-icon fa fa-camera-retro bigger-120"></i>
                            排土量（多环）
                        </a>
                    </li>
                    <li>
                        <a data-toggle="tab" href="#segment" onclick="getData('segment')">
                            <i class="green ace-icon fa fa-fire bigger-120"></i>
                            管片姿态
                        </a>
                    </li>
                </ul>

                <div id="tableContent" class="tab-content" style="/*height: 800px;*/">

                    <div id="ring" class="tab-pane in active">
                        <div class="col-xs-12">
                            <div class="col-md-1" style="padding-top: 5px;max-width: 80px;">
                                <label for="ringNum" style=""><span>环号：</span></label>
                            </div>
                            <select class="chosen-select" style="padding-top: 5px;max-width: 80px;"  id="ringNum">
                                <option value="-1" >未选择</option>
                            </select>
                            <div style="float: right">
                                <button class="btn btn-sm btn-block" onclick="dowanload('ring');" >
                                    <div class="ace-icon fa fa-download"></div>
                                    <span>下载</span>
                                </button>
                            </div>
                        </div>
                        <div class="col-xs-12">
                            <div class="col-md-2"></div>
                            <div class="col-md-6">
                                <div style="text-align: center;"><h4 style="font-weight: bolder;">油缸行程</h4></div>

                                <div>
                                    <div style="float: left;margin-left: 100px;width: 70px;text-align: center;">推进前</div>
                                    <div style="float: right;margin-right: 100px;width: 70px;text-align: center">推进后</div>
                                </div>
                                <div style="clear: both;margin-top: 20px;">
                                    <div id="J1SBefore"  class="curMs"  style="float: left;margin-left: 100px;"></div>
                                    <div id="J1SAfter" class="curMs" style="float: right;margin-right: 100px;"></div>
                                </div>
                                <div style="clear: both;margin-top: 70px;">
                                    <div id="J4SBefore" class="curMs" style="display: inline;float: left;margin-left: 0px;"></div>
                                    <div id="J2SBefore" class="curMs" style="display: inline;float: left;margin-left: 130px;"></div>

                                    <div id="J2SAfter" class="curMs" style="display: inline;float: right;margin-right: 0px;"></div>
                                    <div id="J4SAfter" class="curMs" style="display: inline;float: right;margin-right: 130px;"></div>
                                </div>
                                <div style="clear: both;margin-top: 120px;">
                                    <div id="J3SBefore" class="curMs" style="float: left;margin-left: 100px;"></div>
                                    <div id="J3SAfter" class="curMs" style="float: right;margin-right: 100px;"></div>
                                </div>
                                <div class="col-md-3"></div>
                            </div>

                            <div class="col-md-4">
                                <div id="timeChart" style="width: 500px;height: 250px;"></div>
                            </div>
                        </div>
                        <div class="col-xs-12">
                            <div class="col-xs-6 col-md-3">
                                <div id="JN" style="width: 390px;height:230px;"></div>
                            </div>
                            <div class="col-xs-6 col-md-3">
                                <div id="JV" style="width: 350px;height: 230px;"></div>
                            </div>
                            <div class="col-xs-6 col-md-3">
                                <div id="CTor" style="width: 390px;height: 230px;"></div>
                            </div>
                            <div class="col-xs-6 col-md-3">
                                <div id="CRpm" style="width: 350px;height: 230px;"></div>
                            </div>
                        </div>
                        <div class="col-xs-12">
                            <div class="col-xs-6 col-md-3">
                                <div id="SP1" style="width: 350px;height: 230px;"></div>
                            </div>
                            <div class="col-xs-6 col-md-3">
                                <div id="SCRpm" style="width: 350px;height: 230px;"></div>
                            </div>
                            <div class="col-xs-6 col-md-3">
                                <div id="SCTor" style="width: 390px;height: 230px;"></div>
                            </div>
                            <div class="col-xs-6 col-md-3" >
                                <div id="MR_Act_A1" style="width: 350px;height: 230px;"></div>
                            </div>
                        </div>
                    </div>

                    <div id="soilRing" class="tab-pane" style="">
                        <div class="col-xs-12" >
                            <div class="col-xs-12" style="margin-bottom: 20px;">
                                <div class="col-md-1" style="padding-top: 5px;max-width: 80px;">
                                    <label for="soilRingNum" style=""><span>环号：</span></label>
                                </div>
                                <select class="chosen-select" style="padding-top: 5px;max-width: 80px;"  id="soilRingNum">
                                    <option value="-1" >未选择</option>
                                </select>
                                <div style="float: right">
                                    <button class="btn btn-sm btn-block" onclick="dowanload('ring');" >
                                        <div class="ace-icon fa fa-download"></div>
                                        <span>下载</span>
                                    </button>
                                </div>
                            </div>
                            <div class="col-md-12" style="margin-top: 50px;">
                                <div class="col-md-6" >
                                    <table id="soilRingTable">
                                        <tr>
                                            <td>管理行程</td>
                                            <td>累计体积（m3）</td>
                                            <td>理论体积（m3）</td>
                                            <td>皮带机转速（m/s）</td>
                                            <td>土压（MPa）</td>
                                        </tr>
                                        <tr v-for="(value,index) in soil_curMS" >
                                            <td>{{soil_curMS[index] }}</td>
                                            <td>{{ soil_Dumping_volume_Total[index] }}</td>
                                            <td>{{ soil_Dumping_volume_DTotal[index] }}</td>
                                            <td>{{ soil_BeltRpm[index] }}</td>
                                            <td>{{ soil_SP1[index] }}</td>
                                        </tr>
                                    </table>
                                </div>

                                <div class="col-md-6">
                                    <div id="soilRingChart" style="width: 100%;height:400px;"></div>
                                </div>
                            </div>

                        </div>
                    </div>

                    <div id="soilRings" class="tab-pane" style="">
                        <div class="col-xs-12 hundred">
                            <div class="col-md-12">
                                <div class="col-md-3" style="min-width: 350px;">
                                    <label class="labelThree" for="startRing" style="margin-right: 20px;">起始环</label>
                                    <input type="text" id="startRing" style="max-width: 100px;" data-bxwidget class="inputThree" />
                                    <span></span>
                                    <label class="labelThree" style="margin: 0px 10px 5px 10px;">至</label>
                                    <input type="text" id="endRing" style="max-width: 100px;" data-bxwidget class="inputThree"/>
                                </div>
                                <div class="col-md-1 btnDefault">
                                    <button class="btn btn-sm btn-block" onclick="soilRingsSearch();" >
                                        <div class="ace-icon fa fa-search"></div>
                                        <span>查询</span>
                                    </button>
                                </div>
                                <div class="col-md-1 btnDefault">
                                    <button class="btn btn-sm btn-block" onclick="exportCSV('soilRings');" >
                                        <div class="ace-icon fa fa-download"></div>
                                        <span>导出</span>
                                    </button>
                                </div>
                                <div class="col-md-1 btnDefault" style="float: right">
                                    <button class="btn btn-sm btn-block" onclick="dowanload();" >
                                        <div class="ace-icon fa fa-download"></div>
                                        <span>下载</span>
                                    </button>
                                </div>
                            </div>

                            <div class="col-md-6" style=" position: relative;top:50%;margin-top: -200px;">
                                <div id="soilRingsChart" style="margin: 0 auto;width: 100%;height:400px;"></div>
                            </div>
                            <div class="col-md-6" style=" position: relative;top:50%;margin-top: -200px;">
                                <table id="soilRingsTable" style="margin: 0 auto;">
                                    <tr>
                                        <td>环号</td>
                                        <td>体积</td>
                                        <td>设计下限</td>
                                        <td>设计上限</td>
                                        <td>理论体积</td>
                                    </tr>
                                    <tr v-for="(value,index) in soil_ringsList">
                                        <td>{{soil_ringsList[index] }}</td>
                                        <td>{{ soil_ring_Dumping_volume_Total[index] }}</td>
                                        <td>{{ soil_lowerLimit[index] }}</td>
                                        <td>{{ soil_upperLimit[index] }}</td>
                                        <td>{{ soil_ring_Dumping_volume_DTotal[index] }}</td>
                                    </tr>
                                </table>
                            </div>

                        </div>
                    </div>

                    <div id="segment" class="tab-pane">
                        <div class="col-md-12">
                            <div class="col-md-3" style="min-width: 350px;">
                                <label class="labelThree" for="startRing" style="margin-right: 20px;">起始</label>
                                <input type="text" id="segmentStartRing" style="max-width: 100px;" data-bxwidget class="inputThree" />
                                <span></span>
                                <label class="labelThree" style="margin: 0px 10px 5px 10px;">至</label>
                                <input type="text" id="segmentEndRing" style="max-width: 100px;" data-bxwidget class="inputThree"/>
                            </div>

                            <div class="col-md-1 btnDefault" >
                                <button class="btn btn-sm btn-block" onclick="segmentSearch();" >
                                    <div class="ace-icon fa fa-search"></div>
                                    <span>查询</span>
                                </button>
                            </div>

                            <div class="col-md-1 btnDefault">
                                <button class="btn btn-sm btn-block" onclick="exportCSV('segment');" >
                                    <div class="ace-icon fa fa-download"></div>
                                    <span>导出</span>
                                </button>
                            </div>

                            <div class="col-md-1 btnDefault" style="float: right">
                                <button class="btn btn-sm btn-block" onclick="dowanload();" >
                                    <div class="ace-icon fa fa-download"></div>
                                    <span>下载</span>
                                </button>
                            </div>
                        </div>
                        <div class="col-md-12">
                            <div class="col-md-6">
                                <div id="segmentLineChart" style="width: 100%;height:350px;" ></div>
                            </div>
                            <div class="col-md-6">
                                <div id="segmentPieChart" style="width: 100%;height:350px;" ></div>
                            </div>
                        </div>

                        <div class="col-md-12">
                            <table id="segmentTable" style="margin: 0 auto;">
                                <tr>
                                    <td>环号</td>
                                    <td>实测坐标X(m)</td>
                                    <td>实测坐标Y(m)</td>
                                    <td>实测坐标Z(m)</td>
                                    <td>里程</td>
                                    <td>设计坐标X(m)</td>
                                    <td>设计坐标Y(m)</td>
                                    <td>设计坐标Z(m)</td>
                                    <td>水平偏差(mm)</td>
                                    <td>垂直偏差(mm)</td>
                                </tr>
                                <tr v-for="(value,index) in segment_dataList">
                                    <td>{{ segment_dataList[index].MR_Ring_Number }}</td>
                                    <td>{{ segment_dataList[index].MR_Act_A8X }}</td>
                                    <td>{{ segment_dataList[index].MR_Act_A8Y }}</td>
                                    <td>{{ segment_dataList[index].MR_Act_A8Z }}</td>
                                    <td>{{ segment_dataList[index].MR_Des_A8Mileage }}</td>
                                    <td>{{ segment_dataList[index].MR_Des_A8X }}</td>
                                    <td>{{ segment_dataList[index].MR_Des_A8Y }}</td>
                                    <td>{{ segment_dataList[index].MR_Des_A8Z }}</td>
                                    <td>{{ segment_dataList[index].MR_Act_A8HD }}</td>
                                    <td>{{ segment_dataList[index].MR_Act_A8VD }}</td>
                                </tr>
                            </table>
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

<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/constructionManagement/reportTable.js"></script>


</body>
</html>
