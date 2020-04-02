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
    %>

    <meta name="description" content="platForm Frameset"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <style>
        [v-cloak]{ display: none; }
        #father{
            background: -webkit-linear-gradient(#105899,#3C84C5, #105899); /* Safari 5.1 - 6.0 */
            background: -o-linear-gradient(#105899,#3C84C5, #105899); /* Opera 11.1 - 12.0 */
            background: -moz-linear-gradient(#105899,#3C84C5, #105899); /* Firefox 3.6 - 15 */
            background: linear-gradient(#105899,#3C84C5, #105899);!important; /* 标准的语法 */
            color:white!important;
        }
        .page-header{
            background-color: white;
            margin: -8px -20px 0px!important;
            padding:8px 20px 9px 20px;
        }
        .page-header h5,.page-header h5 small,.page-header div a{
            color:white!important;
        }
        #ringNumber{
            text-align: center;
            margin-bottom: 20px;
            position:relative;
            right:10px;
        }
        #ringNumber span,.blackGround{
            border: 1px solid #797979;
            background-color: black;
            color:#00D505;
            margin-left: 10px;
            padding:2px 10px
        }
       table,table tr th, table tr td {
           border:1px solid #FFFFFF;
           color:#FFFFFF;
           font-size: 14px;
       }
       table tr td{
           width: 20%;
       }
       .wider{
           width: 35%;
       }
       table { width: 200px; min-height: 25px; line-height: 25px; text-align: center; border-collapse: collapse; padding:2px;}

       .shuli{
           margin:0 auto;
           width:20px;
           line-height:24px;
           border:1px dashed #FFFFFF;

       }
        #progressBackGround{
            width: 300px;margin: 0 auto;border-radius: 4px;
            background: -webkit-linear-gradient(#303030,#787878); /* Safari 5.1 - 6.0 */
            background: -o-linear-gradient(#303030,#787878); /* Opera 11.1 - 12.0 */
            background: -moz-linear-gradient(#303030,#787878); /* Firefox 3.6 - 15 */
            background: linear-gradient(#303030,#787878);!important; /* 标准的语法 */
        }
    </style>

</head>

<body class="bai">
<%@ include file="/bxui/bxuihead.jsp" %>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/echarts3/echarts.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/vue/vue.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxdialog.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-elementui.js"></script>
<input type="hidden" value="<%=tenantid%>" id="tenantid" />
<div id="father" v-cloak class="page-content" style="position:inherit;min-height: 864px;min-width: 1600px;height: 100%">
    <div class="page-header">
        <h5 style="color: rgb(38, 121, 181)!important;">
            导向管理
            <small style="color:rgb(153, 153, 153)!important;">
                &nbsp;
                <i class="ace-icon fa fa-angle-double-right" ></i>
                &nbsp;&nbsp;
                导向管理界面
            </small>
        </h5>
        <div style="float: right;margin-top: -15px;">
            <a href="javascript:insertPage()" style="color: rgb(38, 121, 181)!important;" ><span id = "cityname"></span><span id="linemessage"></span><span id="programmessage"></span></a>
        </div>
    </div>
    <!-- 内容-->
    <div class="row" style="padding-top: 10px;">
        <!--左侧 折线图-->
        <div class="col-xs-12 col-sm-6 col-md-4">
            <div>
                <div id="myCharts1" style="min-height:280px;"></div>
            </div>
            <div id="fillLeft"></div>
            <div>
                <div id="myCharts2" style="min-height:280px;"></div>
            </div>
        </div>
        <!--中间-->
        <div class="col-xs-12 col-sm-6 col-md-4">
            <div id="ringNumber" style="padding-top: 10px; ">盾尾环号<span>{{ringNumber}}</span></div>
            <div class="col-xs-12 col-sm-12 col-md-12" >
                <table class="col-xs-12 col-sm-12 col-md-12">
                    <tr>
                        <td class="wider"></td>
                        <td>切口</td>
                        <td>铰接</td>
                        <td>盾尾</td>
                    </tr>
                    <tr>
                        <td>水平(mm)</td>
                        <td id="incisionHorizontalDeviation">{{incisionHorizontalDeviation}}</td>
                        <td id="articulationHorizontalDeviation">{{articulationHorizontalDeviation}}</td>
                        <td id="shieldTailHorizontalDeviation">{{shieldTailHorizontalDeviation}}</td>
                    </tr>
                    <tr>
                        <td>垂直(mm)</td>
                        <td id="incisionVerticalDeviation">{{incisionVerticalDeviation}}</td>
                        <td id="articulationVerticalDeviation">{{articulationVerticalDeviation}}</td>
                        <td id="shieldVerticalDeviation">{{shieldVerticalDeviation}}</td>
                    </tr>
                </table>
            </div>
            <!--圆图-->
            <div style="clear: both;">
                <div style="position: relative;">
                    <span style="position: relative; top: 70px;">滚动角<span class="blackGround">{{rollingAngle}}</span></span>
                    <span style="position: relative;top:70px;left:280px;">俯仰角<span class="blackGround">{{pitchAngle}}</span></span>
                </div>
                <canvas id="myCanvas" width="100%" ></canvas>
            </div>
            <div style="text-align: center;">
                <div>切口里程<span class="blackGround" style="width: 150px!important;">{{mileage}}</span></div>
                <div style="margin: 20px 0px;">掘进长度<span class="blackGround" style="width: 150px!important;">{{drivingLength}}</span></div>
                <div id="progressBackGround" class="progress progress-striped pos-rel" title="">
                    <div id="progress" class="progress-bar progress-bar-success"  ></div>
                </div>
            </div>
        </div>
        <!--右侧-->
        <div class="col-xs-12 col-sm-6 col-md-4">
            <div style="height: 40px;"></div>
            <div class="col-xs-12 col-sm-10 col-md-12" id="rightTopDiv">
                <table class="col-xs-12 col-sm-12 col-md-12" >
                    <tr>
                        <td >切口</td>
                        <td>铰接</td>
                        <td>盾尾</td>
                        <td class="wider">趋势（mm/m）</td>
                    </tr>
                    <tr>
                        <td id="incisionHorizontalDeviationAbsolute">{{incisionHorizontalDeviationAbsolute}}</td>
                        <td id="articulationHorizontalDeviationAbsolute">{{articulationHorizontalDeviationAbsolute}}</td>
                        <td id="shieldTailHorizontalDeviationAbsolute">{{shieldTailHorizontalDeviationAbsolute}}</td>
                        <td id="horizontalTrendAbsolute">{{horizontalTrendAbsolute}}</td>
                    </tr>
                </table>
                <div  id="rightTopText" class="shuli col-sm-1 col-md-1" style="padding-right: 5px;padding-left: 5px;min-height: 96px;max-width: 28px;">水平偏差</div>
                <div class="col-sm-11 col-md-11">
                    <canvas id="horizontalDeviation" >
                    </canvas>
                </div>
            </div>

            <div class="col-xs-12 col-sm-10 col-md-12" id="rightBottomDiv">
                <table class="col-xs-12 col-sm-12 col-md-12">
                    <tr>
                        <td>切口</td>
                        <td>铰接</td>
                        <td>盾尾</td>
                        <td class="wider">趋势（mm/m）</td>
                    </tr>
                    <tr>
                        <td id="incisionVerticalDeviationAbsolute">{{incisionVerticalDeviationAbsolute}}</td>
                        <td id="articulationVerticalDeviationAbsolute">{{articulationVerticalDeviationAbsolute}}</td>
                        <td id="shieldVerticalDeviationAbsolute">{{shieldVerticalDeviationAbsolute}}</td>
                        <td id="veticalTrendAbsolute">{{veticalTrendAbsolute}}</td>
                    </tr>
                </table>
                <div id="rightBottomText" class="shuli col-sm-1 col-md-1" style="padding-right: 5px;padding-left: 5px;min-height: 96px;max-width: 28px;">垂直偏差</div>
                <div class="col-sm-11 col-md-11" >
                    <canvas id="verticalDeviation">
                    </canvas>
                </div>
            </div>
        </div>
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
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/guidance/guidanceManagement.js"></script>


</body>
</html>
