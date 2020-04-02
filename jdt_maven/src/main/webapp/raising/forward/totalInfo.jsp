<%--
  Created by IntelliJ IDEA.
  User: ysy
  Date: 2018/6/27
  Time: 14:14
  Description: 
--%>
<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <%
        String path = request.getContextPath();
    %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>totalInfo</title>

</head>
<style>
    [v-cloak]{ display: none; }
    html,body{
        height: 100%;
    }
    td[class *="fourteen"]{
        width:14%;
        text-align: center;
    }
    td[class *= "sixteen"]{
        width: 16%;

    }
    #tableContent{
        /*height: 700px;*/
        overflow: auto;
    }
    table tr:last-child{
        border-bottom:1px solid #ddd;
    }
    #father .title{
        padding-top: 4px;
        text-align: right;
    }

</style>
<link rel="stylesheet" href="<%=path%>/raisingui/busy-load/app.min.css">
<body>

<%@ include file="/bxui/bxuihead.jsp"%>
<script type="text/javascript" src="<%=toolkitPath%>/raisingui/busy-load/app.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/vue/vue.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-form.js"></script>

<div id="father" style="height: 100%">
    <div class="row" style="padding-top: 20px;padding-bottom: 10px; margin-left: 20px;width: 94%" id="son1">
        <div class="col-md-3">
            <div data-toggle="buttons" id="constructorDiv"  class="btn-group btn-overlap btn-corner">
                <label class="btn btn-sm btn-white btn-info" id="all" onclick="constructorClick(this)">
                    <input type="checkbox" value="all"/>
                    全部
                </label>
                <label class="btn btn-sm btn-white btn-info" id="prostatus.building"  onclick="constructorClick(this)">
                    <input type="checkbox" value="prostatus.building"/>
                    在建
                </label>
                <label class="btn btn-sm btn-white btn-info" id="prostatus.finished" onclick="constructorClick(this)">
                    <input type="checkbox" value="prostatus.finished"/>
                    竣工
                </label>
                <label class="btn btn-sm btn-white btn-info" id="prostatus.nostart"  onclick="constructorClick(this)">
                    <input type="checkbox" value="prostatus.nostart"/>
                    未开始
                </label>
            </div>
        </div>

        <div class="col-md-4">
            <div class="col-md-4 title">
                <label for="communication" style=""><span>通讯状态：</span></label>
            </div>
            <select class="chosen-select" id="communication" onchange="communicationChange()">
                <option value="" >全部</option>
                <option value="已连接">已连接</option>
                <option value="未连接">未连接</option>
                <option value="已断开">已断开</option>
            </select>
        </div>

        <div class="col-md-4" id="cityDiv">
            <div class="col-md-4 title">
                <label for="citySelect"><span id="title2">城市：</span></label>
            </div>


            <select class="chosen-select" id="citySelect" style="float: right;"  onchange="cityChange()" >
                <option value="">全部</option>
            </select>
        </div>
    </div>

    <div class="col-xs-4 col-sm-12 widget-container-col" id="total" >
        <div class="widget-box widget-color-blue" style="margin-top: 0px;margin-bottom: 0px;" id="totalSon1" >
        <!-- #sectionController:custom/widget-box.options -->
            <div class="widget-header" id="totalGrandson1">
                <h5 class="widget-title bigger lighter">
                    <i class="ace-icon fa fa-table" style="margin-top: 8px"></i>
                    项目总览表
                </h5>
            </div>

            <div class="widget-body" id="totalGrandson2" >
                <div class="widget-main no-padding" style="height: 100%;">
                    <div id="tableContent" style="height: 100%;">
                        <table class="table table-striped table-bordered table-hover" >
                            <thead class="thin-border-bottom">
                            <tr>
                                <td class="fourteen" >项目名称</td>
                                <td class="fourteen">施工状态</td>
                                <td class="fourteen">当前环/总环</td>
                                <td class="fourteen">盾构名称</td>
                                <td class="fourteen">盾构机类型</td>
                                <td class="fourteen">通讯状态</td>
                                <td class="sixteen">业主单位</td>
                            </tr>
                            </thead>
                            <tbody id="tableBody">
                                <tr v-cloak v-for="data in showDataList">
                                    <td align="center" class="fourteen">
                                        <a href="javascript:void(0)"
                                           v-bind:data-id="data.projectId"
                                           v-bind:data-collector="data.collectorName"
                                           v-bind:data-totalLength="data.totalLength" v-bind:data-totalMileage="data.totalMileage"
                                           v-bind:data-tbmName="data.tbmName"
                                           v-bind:data-ringTotal="data.ringTotal"
                                           v-bind:data-projectName="data.projectName" onclick="selecteProject(this)">
                                            <%--{{data.projectName}}--%>
                                            <span v-if="data.projectName.length <= 13">{{data.projectName}}</span>
                                            <span v-else v-bind:title="data.projectName">{{data.projectName.substring(0,9)}}...</span>
                                        </a>
                                    </td>
                                    <td align="center" class="fourteen">{{data.constructionState}}</td>
                                    <td align="center" class="fourteen" v-if="data.quality == '192'">{{data.ringNum}}/{{data.ringTotal}}</td>
                                    <td align="center" class="fourteen" v-else><span style="color: #bc5b10;">{{data.ringNum}}</span>/{{data.ringTotal}}</td>
                                    <td align="center" class="fourteen">{{data.tbmName}}</td>
                                    <td align="center" class="fourteen">{{data.tbmType}}</td>
                                    <td align="center" class="fourteen">{{data.communicationState}}</td>
                                    <td align="center" class="sixteen">{{data.sectionOwner}}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <div id="explain" style="color: #bc5b10;text-align: right;background-color:white;padding-right: 15px;">说明：褐色数据代表采集的数据可能存在问题</div>
        </div>
    </div>
</div>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/totalInfo.js"></script>
</body>
</html>
