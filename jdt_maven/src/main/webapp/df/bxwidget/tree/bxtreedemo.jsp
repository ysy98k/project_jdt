<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>bxtree展示</title>

</head>
<body>
<%@ include file="/bxui/bxuihead.jsp"%>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript"
        src="<%=toolkitPath%>/df/bxwidget/tree/bxtreedemo.js"></script>

<div class="page-content">
    <!-- /sectionController:settings.box -->
    <div class="page-header">
        <h1>
            树组件 <small> <i class="ace-icon fa fa-angle-double-right"></i>
            bxtree的展示
        </small>
        </h1>
    </div>
    <!-- /.page-header -->

    <div class="row">
        <div class="col-xs-12">
            <div class="row">
                <div class="col-sm-6">
                    <div class="widget-box ">
                        <div class="widget-header">
                            <h4 class="widget-title lighter smaller">树1-通过ccsId获取数据</h4>
                        </div>

                        <div class="widget-body">
                            <div class="widget-main padding-8">
                                <div id="bxtree1"></div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-sm-6">
                    <div class="widget-box">
                        <div class="widget-header bx-header">
                            <h4 class="widget-title lighter smaller">树2-自定义数据</h4>
                        </div>

                        <div class="widget-body">
                            <div class="widget-main padding-8">
                                <div id="bxtree2"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- /.col -->
    </div>
    <!-- /.row -->
    <div class="hr hr-double hr-dotted hr18"></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="row">
                <div class="col-sm-3">
                    <div class="widget-box widget-color-blue">
                        <div class="widget-header">
                            <h4 class="widget-title lighter smaller">树3-蓝色</h4>
                        </div>

                        <div class="widget-body">
                            <div class="widget-main padding-8 bluetree">
                                <div id="bxtree3"></div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-sm-3">
                    <div class="widget-box widget-color-green">
                        <div class="widget-header">
                            <h4 class="widget-title lighter smaller">树4-绿色</h4>
                        </div>

                        <div class="widget-body">
                            <div class="widget-main padding-8 greentree">
                                <div id="bxtree4"></div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-sm-3">
                    <div class="widget-box widget-color-dark">
                        <div class="widget-header">
                            <h4 class="widget-title lighter smaller">树5-黑色</h4>
                        </div>

                        <div class="widget-body">
                            <div class="widget-main padding-8 blacktree">
                                <div id="bxtree5"></div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-sm-3">
                    <div class="widget-box widget-color-pink">
                        <div class="widget-header">
                            <h4 class="widget-title lighter smaller">树6-粉色</h4>
                        </div>

                        <div class="widget-body">
                            <div class="widget-main padding-8 pinktree">
                                <div id="bxtree6"></div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-sm-3">
                    <div class="widget-box widget-color-orange">
                        <div class="widget-header">
                            <h4 class="widget-title lighter smaller">树7-下拉框树组件</h4>
                        </div>


                        <div class="widget-body">
                            <div class="widget-main padding-8 bluetree">
                                <div id="bxtree7"></div>
                            </div>
                        </div>

                    </div>
                </div>

            </div>
        </div>
        <!-- /.col -->
    </div>
    <!-- /.row -->
</div>
<!-- /.page-content -->

</body>
</html>

