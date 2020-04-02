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
    <style>
        .bai{
            background-color:#FFFFFF;
        }

        #tbmRecord td{
            font-size: 13px;
        }

        .caitlin{
            border: 1px solid #E6EAEF;
            border-top: 5px solid #7DB4D8;
            margin-bottom: 40px;
            background-color: #FFFFFF;
            padding-bottom:10px ;
            border-bottom: 0px;
        }
        .title{
            font-size: 18px;
            line-height: 50px;

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
            /*background-color:#ECF0F5;*/
        }
    </style>

</head>

<body class="bai">
<%@ include file="/bxui/bxuihead.jsp" %>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxdialog.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-elementui.js"></script>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/date-time/css/bootstrap-datetimepicker.css">

<input type="hidden" value="<%=tenantid%>" id="tenantid" />
<div id="father" class="page-content" style="position:inherit;min-height:600px">
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
                        <a data-toggle="tab" href="#home">
                            <i class="green ace-icon fa fa-comment-o bigger-120"></i>
                            设备信息
                        </a>
                    </li>

                    <li>
                        <a data-toggle="tab" href="#profile" onclick="getTbmRecord()">
                            <i class="green ace-icon fa fa-leaf bigger-120"></i>
                            盾构机履历
                        </a>
                    </li>
                </ul>
                <div id="tableContent" class="tab-content" style="height: 750px;">
                    <div id="home" class="tab-pane in active">
                        <%--尺寸信息--%>
                        <div class="col-xs-12 col-sm-12 col-md-12 caitlin" >
                            <div class="title">尺寸信息</div>

                            <table class="col-xs-12 col-sm-12 col-md-12">
                                <tr>
                                    <td class="col-xs-6 col-sm-4 col-md-2">盾构机类型</td>
                                    <td class="col-xs-6 col-sm-4 col-md-2" id="TBM_Work_Type"></td>
                                    <td class="col-xs-6 col-sm-4 col-md-2">前盾长度（m）</td>
                                    <td class="col-xs-6 col-sm-4 col-md-2" id="TBM_F_Length"></td>
                                    <td class="col-xs-6 col-sm-4 col-md-2">盾构前端到顶靴间的距离 （m）</td>
                                    <td class="col-xs-6 col-sm-4 col-md-2" id="TBM_A0_Length"></td>
                                </tr>
                                <tr>
                                    <td>铰接类型</td>
                                    <td id="TBM_Articulation_Type"></td>
                                    <td>后盾长度（m）</td>
                                    <td id="TBM_B_Length"></td>
                                    <td>盾壳厚度（m）</td>
                                    <td id="TBM_Thickness"></td>
                                </tr>
                                <tr>
                                    <td>盾构机长（m）</td>
                                    <td id="TBM_Length"></td>
                                    <td>盾体总长（m）</td>
                                    <td id="TBM_All_Length"></td>
                                    <td>总重量（t）</td>
                                    <td id="TBM_Weight"></td>
                                </tr>
                                <tr>
                                    <td>挖掘外径（mm）</td>
                                    <td id="TBM_Driving_Diameter"></td>
                                    <td>环片长度（m）</td>
                                    <td id="RingLen"></td>
                                    <td>盾尾间隙平均修值（mm）</td>
                                    <td id="TBM_Clearance"></td>
                                </tr>
                                <tr>
                                    <td>盾首外径（mm）</td>
                                    <td id="TBM_F_Diameter"></td>
                                    <td>推进油缸千斤顶半径（mm）</td>
                                    <td id="TBM_Z_Radius"></td>
                                    <td>铰接油缸千斤顶半径（mm）</td>
                                    <td id="TBM_J_Radius"></td>
                                </tr>
                                <tr>
                                    <td>盾中外径（mm）</td>
                                    <td id="TBM_M_Diameter"></td>
                                    <td>推进油缸右上角度（°）</td>
                                    <td id="TBM_Z1_Position"></td>
                                    <td>铰接油缸右上安装角度（°）</td>
                                    <td id="TBM_J1_Position"></td>
                                </tr>
                                <tr>
                                    <td>盾尾外径（mm）</td>
                                    <td id="TBM_B_Diameter"></td>
                                    <td>推进油缸右下角度（°）</td>
                                    <td id="TBM_Z2_Position"></td>
                                    <td>铰接油缸右下安装角度（°）</td>
                                    <td id="TBM_J2_Position"></td>
                                </tr>
                                <tr>
                                    <td>盾尾内径（mm）</td>
                                    <td id="TBM_B_Indiamete"></td>
                                    <td>推进油缸左下角度（°）</td>
                                    <td id="TBM_Z3_Position"></td>
                                    <td>铰接油缸左下安装角度（°）</td>
                                    <td id="TBM_J3_Position"></td>
                                </tr>
                                <tr>
                                    <td>环片外径（mm）</td>
                                    <td id="TBM_E_Diameter"></td>
                                    <td>推进油缸左上角度（°）</td>
                                    <td id="TBM_Z4_Position"></td>
                                    <td>铰接油缸左上安装角度（°）</td>
                                    <td id="TBM_J4_Position"></td>
                                </tr>
                            </table>

                        </div>
                        <%--盾构机能力--%>
                        <div class="col-xs-12 col-sm-12 col-md-12 caitlin" class="caitlin">
                            <div class="title">盾构机能力</div>
                            <table class="col-xs-12 col-sm-12 col-md-12">
                                <tr>
                                    <td class="col-xs-6 col-sm-4 col-md-2">最大推力（KN）</td>
                                    <td class="col-xs-6 col-sm-4 col-md-2" id="TBM_Max_Thrust"></td>
                                    <td class="col-xs-6 col-sm-4 col-md-2">最大螺旋机压力（mpa）</td>
                                    <td class="col-xs-6 col-sm-4 col-md-2" id="TBM_Max_ScrewPressure"></td>
                                    <td class="col-xs-6 col-sm-4 col-md-2">最大刀具压力/电流（mpa/a）</td>
                                    <td class="col-xs-6 col-sm-4 col-md-2" id="TBM_Pressure_Current"></td>
                                </tr>
                                <tr>
                                    <td>最大推进压力（MPA）</td>
                                    <td id="TBM_Max_Thrustpressure"></td>
                                    <td>螺旋机排土能力（m³/h）</td>
                                    <td id="TBM_Ability_Dumpingsoil"></td>
                                    <td>最大螺旋机扭矩（kn-m）</td>
                                    <td id="TBM_Max_Screwtorque"></td>
                                </tr>
                                <tr>
                                    <td> 最大刀具扭矩（kn-m）</td>
                                    <td id="TBM_Max_Tooltorque"></td>
                                    <td>顶靴环片距离（m）</td>
                                    <td id="TBM_Shoe_Length"></td>
                                    <td>刀盘转速（r/min）</td>
                                    <td id="TBM_Cutterhead_Speed"></td>
                                </tr>
                                <tr>
                                    <td>水平转弯半径</td>
                                    <td id="TBM_Ability_Hturn"></td>
                                    <td>纵向爬坡能力（%）</td>
                                    <td id="TBM_Ability_Vturn"></td>
                                    <td>最大推进速度（mm/min）</td>
                                    <td id="TBM_Max_Speed"></td>
                                </tr>
                            </table>
                        </div>

                        <%--所属信息--%>
                        <div class="caitlin col-xs-12 col-sm-12 col-md-12 ">
                            <div class="title">所属信息</div>

                            <table class="col-xs-12 col-sm-12 col-md-12">
                                <tr>
                                    <td class="col-xs-6 col-sm-4 col-md-2">盾构机名称</td>
                                    <td class="col-xs-6 col-sm-4 col-md-2" id="TBM_Name"></td>
                                    <td class="col-xs-6 col-sm-4 col-md-2">厂家</td>
                                    <td class="col-xs-6 col-sm-4 col-md-2" id="TBM_Manufactor"></td>
                                    <td class="col-xs-6 col-sm-4 col-md-2">厂家单位联系方式</td>
                                    <td class="col-xs-6 col-sm-4 col-md-2" id="TBM_Manufactor_Telephone"></td>
                                </tr>
                                <tr>
                                    <td>所属单位</td>
                                    <td id="TBM_Owner"></td>
                                    <td>所属单位联系方式</td>
                                    <td id="TBM_Owner_Telephone"></td>
                                    <td>设备负责人联系方式</td>
                                    <td id="TBM_Responsible_Telephone"></td>
                                </tr>
                                <tr>
                                    <td>导向系统厂家</td>
                                    <td id="TBM_Guidance"></td>
                                    <td>导向厂家联系电话</td>
                                    <td id="TBM_Guidance_Telephone"></td>
                                    <td>生产日期</td>
                                    <td id="TBM_Production_Date"></td>
                                </tr>
                                <tr>
                                    <td>维保单位</td>
                                    <td id="TBM_Maintenance"></td>
                                    <td>维保单位联系电话</td>
                                    <td id="TBM_Maintenance_Telephone"></td>
                                    <td>累计掘进长度</td>
                                    <td id="TBM_Driving_Length"></td>
                                </tr>
                            </table>
                        </div>
                    </div>

                    <div id="profile" class="tab-pane">
                        <div class="col-xs-12 searchArea" id="queryarea">
                            <div id="alertdiv" class="alertdiv"></div>
                        </div>
                        <div class="col-xs-12">
                            <div id="tbmRecord"></div>
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
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/constructionManagement/tbmInfo.js"></script>


</body>
</html>
