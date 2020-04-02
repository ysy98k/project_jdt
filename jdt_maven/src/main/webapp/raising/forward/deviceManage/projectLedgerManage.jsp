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
        /*String tenantid = request.getSession().getAttribute(Constants.SESSION_TENANTID_KEY) + "";
        String first = java.net.URLDecoder.decode(request.getParameter("firstLevel"),"UTF-8");
        String second = java.net.URLDecoder.decode(request.getParameter("secondLevel"),"UTF-8");*/
    %>

    <meta name="description" content="platForm Frameset"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <style>
        .ui-jqgrid tr.ui-row-ltr td{
            font-size: 13px;
        }
    </style>
</head>

<body style="background-color: white;">
<%@ include file="/bxui/bxuihead.jsp" %>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css"/>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-form.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/deviceManage/projectLedgerManage.js"></script>


<div id="father" class="page-content" style="position:inherit;min-height:600px">
    <!-- /sectionController:settings.box -->
    <div class="page-header">
        <h5>
            设备管理
            <small>
                &nbsp;
                <i class="ace-icon fa fa-angle-double-right"></i>
                &nbsp;&nbsp;
                项目台账管理
            </small>
        </h5>

    </div>
    <!-- 内容-->
    <div class="row" >
        <div class="col-sm-3 col-xs-12">
            <label class="labelThree col-sm-4" for="inqu_status-tbmName">盾构机名称</label>
            <input type="text" id="inqu_status-tbmName" data-bxwidget class="col-sm-7"/>
        </div>
        <div class="col-sm-1 no-padding-left-1024">
            <button class="btn btn-sm btn-block" onclick="on_query_click();">
                <div class="ace-icon fa fa-search"></div>
                <span>查询</span>
            </button>
        </div>
    </div>

    <div class="row">
        <div id="grid"></div>
    </div>


    <div id="upanCreate" class="hide">
        <div id="pnlCreate"  >

            <div class="modal-dialog">
                <div class="modal-content">
                    <div id="projectTabContent" class="tab-content">
                        <table class="table table-condensed table-bordered">
                            <tbody>

                            <tr>
                                <td class="text-right" style="width: 150px">
                                    <label class="control-label">归档类型</label>
                                </td>
                                <td colspan="3">
                                    <select name="ctl00$MainBodyContent$ddlArchiveTypeId" id="ddlArchiveTypeId" class="form-control input-sm">
                                        <option selected="selected" value="0"> - 请选择 - </option>
                                        <option value="23">设备台账（设备）</option>
                                        <option value="24">┄┄盾构机台账（设备）</option>
                                        <option value="32">┄┄配套设备台账（设备）</option>
                                        <option value="35">┄┄施工进场设备计划表（设备）</option>
                                        <option value="36">┄┄其它（设备）</option>
                                        <option value="37">盾构机监造资料（设备）</option>
                                        <option value="39">┄┄盾构机监造周报（设备）</option>
                                        <option value="41">┄┄盾构机监造月报（设备）</option>
                                        <option value="43">┄┄其它（设备）</option>
                                        <option value="44">设备维护保养资料（设备）</option>
                                        <option value="46">┄┄盾构机维护保养计划表（设备）</option>
                                        <option value="47">┄┄盾构机维护保养日记录表（设备）</option>
                                        <option value="48">┄┄盾构机维护保养周记录表（设备）</option>
                                        <option value="49">┄┄盾构机维护保养月记录表（设备）</option>
                                        <option value="50">┄┄盾构机维护保养季记录表（设备）</option>
                                        <option value="52">┄┄盾构机维护保养半年记录表（设备）</option>
                                        <option value="53">┄┄盾构机维护保养年记录表（设备）</option>
                                        <option value="55">┄┄盾构机故障报修单（设备）</option>
                                        <option value="56">┄┄其它（设备）</option>
                                        <option value="78">┄┄盾构机故障情况记录表（设备）</option>
                                        <option value="57">盾构机状态监测资料（设备）</option>
                                        <option value="58">┄┄盾构机油水监测报告（设备）</option>
                                        <option value="59">┄┄盾构机震动检测报告（设备）</option>
                                        <option value="60">方案及审批材料（设备）</option>
                                        <option value="61">┄┄盾构施工组织设计及专家评审报告（设备）</option>
                                        <option value="62">┄┄盾构机组装调试方案及专家评审报告（设备）</option>
                                        <option value="63">┄┄盾构机吊装（拆）方案及专家评审报告（设备）</option>
                                        <option value="64">┄┄临时施工用电组织设计及专家评审报告（设备）</option>
                                        <option value="65">┄┄联络通道施工方案及专家评审报告（设备）</option>
                                        <option value="66">┄┄其它（设备）</option>
                                        <option value="67">验收资料（设备）</option>
                                        <option value="68">┄┄盾构机选型评估报告（设备）</option>
                                        <option value="69">┄┄盾构机出厂验收报告（设备）</option>
                                        <option value="70">┄┄盾构机井下验收报告（设备）</option>
                                        <option value="71">┄┄盾构机始发前条件验收报告（设备）</option>
                                        <option value="72">┄┄盾构机百环验收报告（设备）</option>
                                        <option value="73">┄┄盾构机到达验收报告（设备）</option>
                                        <option value="74">┄┄端头加固验收报告（设备）</option>
                                        <option value="75">┄┄联络通道加固验收报告（设备）</option>
                                        <option value="76">┄┄其它（设备）</option>
                                        <option value="77">特种作业人员证书（设备）</option>

                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td class="text-right">
                                    <label class="control-label">上传文件</label>

                                </td>
                                <td colspan="3">

                                    <div>
                                        <input type="file" >
                                        <%--<input type="hidden" name="ctl00$MainBodyContent$uploadMonitorFile$hidFileNames" id="ctl00_MainBodyContent_uploadMonitorFile_hidFileNames">
                                        <button id="ctl00_MainBodyContent_uploadMonitorFile" class="uploadifive-button uploadifive-button-diy" type="button" data-script="/Controls/UploadFile.ashx" data-text="选择文件" data-filetype="*" data-auto="false" data-multi="true" onclick="uploadifyControl(this,'ctl00_MainBodyContent_uploadMonitorFile_hidFileNames')">
                                            加载上传控件
                                        </button>--%>
                                    </div>
                                    <div style="color: blue;font-size: 12px;">
                                        注:能上传 图片,office文档,pdf文档,zip压缩文件,大小限制 最大为100M

                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="text-right">
                                    <label class="control-label">文档简要说明</label></td>
                                <td colspan="3">
                                    <textarea name="ctl00$MainBodyContent$txtDescription" rows="2" cols="20" id="txtDescription" class="form-control input-sm"></textarea></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                </div>
            </div>

        </div>
    </div>


    <div id="upanEdit">
    </div>

</div>

<!-- ----------------------------------信息弹出框---------------------------------------------------------->
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



</body>
</html>
