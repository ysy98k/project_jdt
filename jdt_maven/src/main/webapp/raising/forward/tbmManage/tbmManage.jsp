<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/9/17
  Time: 13:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>盾构机管理</title>
    <style>
        [v-cloak]{ display: none; }

        [role = 'gridcell']{
            font-size: 13px;
        }


        
    </style>
</head>
<body style="background-color: white;">


<%@ include file="/bxui/bxuihead.jsp" %>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/daterangepicker/css/daterangepicker.css"/>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/moment.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/daterangepicker.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/vue/vue.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<%--这里解决chosen下拉框和查询等按钮css冲突的问题--%>
<script type="text/javascript" src="<%=toolkitPath%>/df/error/assets/js/uncompressed/chosen.jquery.js"></script>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/jquery-plugin/css/chosen.css"/>
<%--这里解决chosen下拉框和查询等按钮css冲突的问题--%>
<script type="text/javascript" src="<%=toolkitPath%>/df/designer/js/loadsave.js"></script>
<script src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css"/>

<style>
    .chosen-container>.chosen-single, [class*=chosen-container]>.chosen-single {
        line-height: 28px;
        height: 30px;
        box-shadow: none;
        background: #FAFAFA;
    }
    /*解决两个搜索图标问题*/
    .chosen-container-single .chosen-search::after{
        display: none;
    }
    .query_title_div{
        display:table-cell;
        text-align: right;
        padding: 4px 0px 0px 15px;
        min-width:50px;
        font-size: 15px;
        font-weight:400;
    }
</style>


<div class="jqgridFrame contentFrame">
    <div class="page-content contentFrame">

        <div class="row">
            <div class="col-xs-12 searchArea" id="queryarea">

                <div id="alertdiv" class="alertdiv"></div>

                <div class="row">
                    <div class="col-md-1 query_title_div">
                        <%--<label for="inqu_status-factory" class="query_title"><h5>盾构机厂家：</h5></label>--%>
                            盾构机厂家：
                    </div>
                    <div class="col-md-2" id="factoryDiv">
                        <select class="chosen-select" id="inqu_status-factory" data-bxwidget class="inputThree">
                            <option id="factoryOption" value="">全部</option>
                        </select>
                    </div>

                    <div class="col-md-1 query_title_div">
                        <%--<label for="inqu_status-owner" style="display:inline-block;width:90px"><h5>所有单位：</h5></label>--%>
                        所有单位：
                    </div>
                    <div class="col-md-2" id="ownerDiv">
                        <select class="chosen-select" id="inqu_status-owner"  data-bxwidget class="inputThree">
                            <option id="ownerOption" value="">全部</option>
                        </select>
                    </div>

                    <div class="col-md-1 query_title_div">
                        <%--<label for="inqu_status-tbmStatus" style="display:inline-block;width:90px"><h5>使用状态：</h5></label>--%>
                            使用状态：
                    </div>
                    <div class="col-md-2" id="buildStatusDiv">
                        <select class="chosen-select" id="inqu_status-tbmStatus"   data-bxwidget class="inputThree">
                            <option id="tbmStatusOption" value="">全部</option>
                        </select>
                    </div>
                    <div class="col-md-1 query_title_div">
                        <%--<label for="inqu_status-tbmStatus" style="display:inline-block;width:90px"><h5>使用状态：</h5></label>--%>
                            盾构机名称：
                    </div>
                    <div class="col-sm-2">
                        <%--<label for="inqu_status-tbmName" style="display:inline-block;width:90px;margin-left: 0px"><h5>盾构机名称：</h5></label>--%>
                        <%--盾构机名称：--%>
                        <input type="text" id="inqu_status-tbmName" value="" style="width: 180px" data-bxwidget />
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-6 col-md-6 col-xs-12 no-padding">
                        <div class="col-sm-3 col-md-3 col-xs-6 no-padding-left-1024" style="margin-left: 17px;">
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
                            <button class="btn btn-sm btn-block" onclick="on_query_click();">
                                <div class="ace-icon fa fa-search"></div>
                                <span>查询</span>
                            </button>
                        </div>
                        <div class="col-sm-2 col-md-2 col-xs-6 no-padding-left-1024">

                        </div>
                    </div>
                    <div class="col-sm-6 col-md-6 col-xs-12 no-padding">

                    </div>


                </div>
            </div>

            <div class="col-xs-12">
                <div id="grid"></div>
            </div>
            <!-- /.col -->
        </div>
        <!-- /.row -->
    </div>

</div>



<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/tbmManage/tbmManage.js"></script>
</body>
</html>
