<%--
  Created by IntelliJ IDEA.
  User: ysy
  Date: 2018/3/5
  Time: 15:25
  Description: 
--%>
<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="com.baosight.aas.auth.Constants" %>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <title>掘进管理</title>
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
        html,body,#father{
            height:100%;
        }
        .tableTile{
            line-height: 24px;
            font-size: 15px;
            padding-top: 7px;
            background-color: #2184C1!important;
            height:30px;
            min-height: 28px!important;
        }
        #treeDiv{
            max-width: 340px;
        }
    </style>
</head>

<body>
<%@ include file="/bxui/bxuihead.jsp" %>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=path%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-form.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/selectLine.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/historyData.js"></script>


<div class="page-content" id="father"  style="position:inherit;min-height: 500px">
    <!-- /sectionController:settings.box -->
    <div class="page-header" id="header">
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
    </div><!-- /.page-header -->
    <div class="row">
        <div class="col-sm-3" id="treeDiv">
            <div class="widget-box widget-color-blue" id="treeDiv2" style="overflow:scroll;overflow-x:hidden">
                <div class="widget-header tableTile">
                    <div><span class="widget-title lighter smaller">掘进变量</span></div>
                </div>

                <div class="widget-body">
                    <div class="widget-main padding-8 bluetree">
                        <div id="bxtree3"></div>
                    </div>
                </div>

            </div>
        </div>

        <div id="queryarea" class="col-xs-9">

            <div class="col-sm-4 col-xs-12">
                <div class="input-group">
                    <label class="labelThree" for="inqu_status-dateTimepicker1">开始时间 &nbsp;

                        <input id="inqu_status-dateTimepicker1" type="text" data-bxwidget class="inputThree"
                               data-date-format="YYYY-MM-DD HH:mm:ss"/>

                    </label>


                </div>
            </div>
            <div class="col-sm-4 col-xs-12">
                <div class="input-group">
                    <label class="labelThree" for="inqu_status-dateTimepicker2">结束时间 &nbsp;

                        <input id="inqu_status-dateTimepicker2" type="text" data-bxwidget class="inputThree"
                               data-date-format="YYYY-MM-DD HH:mm:ss"/>
                    </label>


                </div>
            </div>

            <div class="col-sm-3 col-md-2 col-xs-6 no-padding-left-1024">
                <button class="btn btn-sm btn-block" onclick="on_query_click();">
                    <div class="ace-icon fa fa-search"></div>
                    <span>查询</span>
                </button>
            </div>
            <div class="col-sm-12">
                <div id="grid" multiselect="false"></div>
            </div>
        </div>
    </div>

</div><!-- /.page-content -->

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

</html>

