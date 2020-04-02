<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2018-07-02
  Time: 15:19
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>区间段信息管理</title>

</head>
<body>
<%@ include file="/bxui/bxuihead.jsp" %>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/daterangepicker/css/daterangepicker.css"/>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/moment.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/daterangepicker.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jstree/dist/jstree.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxtree.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/loadsave.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/backstage/section.js"></script>
<script src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css"/>

<div class="jqgridFrame contentFrame">
    <div class="page-content contentFrame">
        <!-- /sectionController:settings.box -->
        <div class="page-header" style="height: 50px;">
            <h1 class="pull-left">
                区间信息管理
                <small><i class="ace-icon fa fa-angle-double-right"></i>
                    对区间信息进行注册和相关配置
                </small>
            </h1>

        </div>
        <!-- /.page-header -->
        <div class="row" style="margin: 0px -5px;">
            <div class="col-xs-12 searchArea" id="queryarea">

                <div id="alertdiv" class="alertdiv"></div>
                <div class="row">
                    <div class="col-sm-3 col-xs-12">
                        <label class="labelThree"
                               for="inqu_status-sectionName">区间名称</label>
                        <input type="text" id="inqu_status-sectionName" data-bxwidget class="inputThree"/>
                    </div>
                    <div class="col-sm-3 col-xs-12">
                        <label class="labelThree"
                               for="inqu_status-ccsId">线路</label>
                        <select id="inqu_status-ccsId" data-bxwidget class="inputThree">
                            <option value="">全部</option>
                        </select>
                    </div>
                    <div class="col-sm-3 col-xs-12">
                        <label class="labelThree"
                               for="inqu_status-owner">业主</label>
                        <select id="inqu_status-owner" data-bxwidget class="inputThree">
                            <option value="">全部</option>
                        </select>
                    </div>

                    <div class="col-sm-3 col-xs-12">
                        <label class="labelThree"
                               for="inqu_status-ccsSectionType">区间类型</label>
                        <select id="inqu_status-ccsSectionType" data-bxwidget class="inputThree">
                            <option value="">全部</option>
                        </select>
                    </div>


                </div>

                <div class="row">
                    <div class="col-sm-6 col-md-6 col-xs-12 no-padding">
                        <div class="col-sm-3 col-md-3 col-xs-6 no-padding-left-1024">
                            <button class="btn btn-sm btn-block" onclick="addAndCopy();">
                                <div class="ace-icon fa fa-plus"></div>
                                <span>新增/复制</span>
                            </button>
                        </div>
                        <div class="col-sm-3 col-md-2 col-xs-6 no-padding-left-1024">
                            <button class="btn  btn-sm btn-block" onclick="saveRec();">
                                <div class="ace-icon fa fa-save"></div>
                                <span>保存</span>
                            </button>
                        </div>
                        <div class="col-sm-3 col-md-2 col-xs-6 no-padding-left-1024">
                            <button class="btn btn-sm btn-danger width-100" onclick="deleteRec();">
                                <div class="ace-icon fa fa-trash-o"></div>
                                <span>删除</span>
                            </button>
                        </div>
                        <div class="col-sm-3 col-md-2 col-xs-6 no-padding-left-1024">
                            <button class="btn btn-sm btn-block" onclick="on_query_click();">
                                <div class="ace-icon fa fa-search"></div>
                                <span>查询</span>
                            </button>
                        </div>
                    </div>
                    <!-- 和上div同行，位于行右 -->
                </div>

        </div>

            <div class="col-xs-12">
                <div id="jqGrid"></div>
            </div>
            <!-- /.col -->
        </div>
        <!-- /.row -->
    </div>
    <%--编辑ccsId--%>
    <%--height:500px;--%>
    <div id="edit" class="hide row" style="">
        <div class="col-sm-7 col-xs-12">
            <div class="widget-box widget-color-blue" style="height:300px;overflow:scroll;overflow-x:hidden">
                <div class="widget-header">
                    <div margin:5px><h5 class="widget-title lighter smaller">城市线路</h5></div>
                </div>

                <div class="widget-body">
                    <div class="widget-main padding-8 bluetree">
                        <div id="bxtree3"></div>
                    </div>
                </div>

            </div>
        </div>
        <div class="col-sm-5 col-xs-12" style="height:300px;padding-left: 0px;">
            <div style="margin-top: 100px;">
                <label class="labelThree blue smaller lighter" for="ccsIdShow" style="width:55px;">CCSID:</label>
                <input type="text" id="ccsIdShow" data-bxwidget class="inputThree" onclick="moveEnd(this)" style="font-size: 8px;width: 135px;position: relative;left:-8px;" readonly="readonly"/>
                <input type="text" id="ccsIdHide" data-bxwidget class="hide" readonly="readonly"/>
                <input type="text" id="cityHide" data-bxwidget class="hide" readonly="readonly"/>
                <p class="alertdiv alert-info alert auto-hide width-100" style="position:relative;top:125px;">若无可选线路，请联系管理员</p>
            </div>

        </div>
    </div>

</div>
    <div id="showMap" class="hide" style="height: 850px">
    <iframe name="children" src="" width="950px" height="600px"  id="mapDesign" border="0" frameborder="0" marginheight="0" marginwidth="0"></iframe>

    </div>

</body>
</html>

