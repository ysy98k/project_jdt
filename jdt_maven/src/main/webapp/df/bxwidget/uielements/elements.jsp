<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>UI元素</title>
    <style>
        /* some elements used in demo only */
        .spinner-preview {
            width: 100px;
            height: 100px;
            text-align: center;
            margin-top: 60px;
            margin-right: 100px;
        }

        .lighter {
            color: #999;
        }

        .dropdown-preview {
            margin: 0 5px;
            display: inline-block;
        }

        .dropdown-preview > .dropdown-menu {
            display: block;
            position: static;
            margin-bottom: 5px;
        }

        label.inline {
            margin-bottom: 0px !important;
        }
    </style>
</head>

<body>

<%@ include file="/bxui/bxuihead.jsp" %>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-elementui.js"></script>
<!-- /sectionController:basics/content.breadcrumbs -->
<div class="page-content">
    <!-- /sectionController:settings.box -->
    <div class="page-header">
        <h1>
            UI元素
            <small>
                <i class="ace-icon fa fa-angle-double-right"></i>
                常见的UI样式和UI元素
            </small>
        </h1>
    </div><!-- /.page-header -->

    <div class="row">
        <div class="col-xs-12">
            <!-- PAGE CONTENT BEGINS -->
            <div class="row">
                <div class="col-sm-6">
                    <!-- #sectionController:elements.tab -->
                    <div class="tabbable">
                        <ul class="nav nav-tabs" id="myTab">
                            <li class="active">
                                <a data-toggle="tab" href="#home">
                                    <i class="green ace-icon fa fa-home bigger-120"></i>
                                    Home
                                </a>
                            </li>

                            <li>
                                <a data-toggle="tab" href="#profile">
                                    Info
                                    <span class="badge badge-danger">4</span>
                                </a>
                            </li>

                            <li class="dropdown">
                                <a data-toggle="dropdown" class="dropdown-toggle" href="javascript:void(0);">
                                    Dropdown &nbsp;
                                    <i class="ace-icon fa fa-caret-down bigger-110 width-auto"></i>
                                </a>

                                <ul class="dropdown-menu dropdown-info">
                                    <li>
                                        <a data-toggle="tab" href="#dropdown1">@fat</a>
                                    </li>

                                    <li>
                                        <a data-toggle="tab" href="#dropdown2">@mdo</a>
                                    </li>
                                </ul>
                            </li>
                        </ul>

                        <div class="tab-content">
                            <div id="home" class="tab-pane in active">
                                <p>Raw denim you probably haven't heard of them jean shorts Austin.</p>
                            </div>

                            <div id="profile" class="tab-pane">
                                <p>Food truck fixie locavore, accusamus mcsweeney's marfa nulla single-origin coffee
                                    squid.</p>
                            </div>

                            <div id="dropdown1" class="tab-pane">
                                <p>Etsy mixtape wayfarers, ethical wes anderson tofu before they sold out mcsweeney's
                                    organic lomo retro fanny pack lo-fi farm-to-table readymade.</p>
                            </div>

                            <div id="dropdown2" class="tab-pane">
                                <p>Trust fund seitan letterpress, keytar raw denim keffiyeh etsy art party before they
                                    sold out master cleanse gluten-free squid scenester freegan cosby sweater. Fanny
                                    pack portland seitan DIY, art party locavore wolf cliche high life echo park
                                    Austin.</p>
                            </div>
                        </div>
                    </div>

                    <!-- /sectionController:elements.tab -->
                </div><!-- /.col -->

                <div class="vspace-6-sm"></div>

                <div class="col-sm-6">
                    <div class="tabbable tabs-below">
                        <div class="tab-content">
                            <div id="home2" class="tab-pane in active">
                                <p>Raw denim you probably haven't heard of them jean shorts Austin.</p>
                            </div>

                            <div id="profile2" class="tab-pane">
                                <p>Food truck fixie locavore, accusamus mcsweeney's marfa nulla single-origin coffee
                                    squid.</p>
                            </div>

                            <div id="dropdown12" class="tab-pane">
                                <p>Etsy mixtape wayfarers, ethical wes anderson tofu before they sold out mcsweeney's
                                    organic lomo retro fanny pack lo-fi farm-to-table readymade.</p>
                            </div>
                        </div>

                        <ul class="nav nav-tabs" id="myTab2">
                            <li class="active">
                                <a data-toggle="tab" href="#home2">Home</a>
                            </li>

                            <li>
                                <a data-toggle="tab" href="#profile2">Profile</a>
                            </li>

                            <li>
                                <a data-toggle="tab" href="#dropdown12">More</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div><!-- /.row -->

            <div class="space"></div>

            <div class="row">
                <div class="col-sm-6">
                    <!-- #sectionController:elements.tab.position -->
                    <div class="tabbable tabs-left">
                        <ul class="nav nav-tabs" id="myTab3">
                            <li class="active">
                                <a data-toggle="tab" href="#home3">
                                    <i class="pink ace-icon fa fa-tachometer bigger-110"></i>
                                    Home
                                </a>
                            </li>

                            <li>
                                <a data-toggle="tab" href="#profile3">
                                    <i class="blue ace-icon fa fa-user bigger-110"></i>
                                    Profile
                                </a>
                            </li>

                            <li>
                                <a data-toggle="tab" href="#dropdown13">
                                    <i class="ace-icon fa fa-rocket"></i>
                                    More
                                </a>
                            </li>
                        </ul>

                        <div class="tab-content">
                            <div id="home3" class="tab-pane in active">
                                <p>Raw denim you probably haven't heard of them jean shorts Austin.</p>

                                <p>Raw denim you probably haven't heard of them jean shorts Austin.</p>
                            </div>

                            <div id="profile3" class="tab-pane">
                                <p>Food truck fixie locavore, accusamus mcsweeney's marfa nulla single-origin coffee
                                    squid.</p>

                                <p>Raw denim you probably haven't heard of them jean shorts Austin.</p>
                            </div>

                            <div id="dropdown13" class="tab-pane">
                                <p>Etsy mixtape wayfarers, ethical wes anderson tofu before they sold out mcsweeney's
                                    organic lomo retro fanny pack lo-fi farm-to-table readymade.</p>

                                <p>Raw denim you probably haven't heard of them jean shorts Austin.</p>
                            </div>
                        </div>
                    </div>

                    <!-- /sectionController:elements.tab.position -->
                </div><!-- /.col -->

                <div class="vspace-6-sm"></div>

                <div class="col-sm-6">
                    <!-- #sectionController:elements.tab.option -->
                    <div class="tabbable">
                        <ul class="nav nav-tabs padding-12 tab-color-blue background-blue" id="myTab4">
                            <li class="active">
                                <a data-toggle="tab" href="#home4">Home</a>
                            </li>

                            <li>
                                <a data-toggle="tab" href="#profile4">Profile</a>
                            </li>

                            <li>
                                <a data-toggle="tab" href="#dropdown14">More</a>
                            </li>
                        </ul>

                        <div class="tab-content">
                            <div id="home4" class="tab-pane in active">
                                <p>Raw denim you probably haven't heard of them jean shorts Austin.</p>
                            </div>

                            <div id="profile4" class="tab-pane">
                                <p>Food truck fixie locavore, accusamus mcsweeney's marfa nulla single-origin coffee
                                    squid.</p>
                            </div>

                            <div id="dropdown14" class="tab-pane">
                                <p>Etsy mixtape wayfarers, ethical wes anderson tofu before they sold out mcsweeney's
                                    organic lomo retro fanny pack lo-fi farm-to-table readymade.</p>
                            </div>
                        </div>
                    </div>

                    <!-- /sectionController:elements.tab.option -->
                </div><!-- /.col -->
            </div><!-- /.row -->

            <div class="hr hr-double hr-dotted hr18"></div>

            <div class="row">
                <div class="col-sm-6">
                    <h3 class="row header smaller lighter blue">
                        <span class="col-xs-6"> 折叠菜单 </span><!-- /.col -->

										<span class="col-xs-6">
											<span class="pull-right inline" style="margin-bottom:-2px">
												<span class="grey smaller-80 bolder">风格:</span>

												<span class="btn-toolbar inline middle no-margin">
													<span id="accordion-style" data-toggle="buttons"
                                                          class="btn-group no-margin">
														<label class="btn btn-xs btn-yellow active">
                                                            <input type="radio" value="1"/>
                                                            1
                                                        </label>

														<label class="btn btn-xs btn-yellow">
                                                            <input type="radio" value="2"/>
                                                            2
                                                        </label>
													</span>
												</span>
											</span>
										</span><!-- /.col -->
                    </h3>

                    <!-- #sectionController:elements.accordion -->
                    <div id="accordion" class="accordion-style1 panel-group">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h4 class="panel-title">
                                    <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion"
                                       href="#collapseOne">
                                        <i class="ace-icon fa fa-angle-down bigger-110"
                                           data-icon-hide="ace-icon fa fa-angle-down"
                                           data-icon-show="ace-icon fa fa-angle-right"></i>
                                        &nbsp;Group Item #1
                                    </a>
                                </h4>
                            </div>

                            <div class="panel-collapse collapse in" id="collapseOne">
                                <div class="panel-body">
                                    Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry
                                    richardson ad squid. 3 wolf moon officia aute, non cupidatat skateboard dolor
                                    brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon tempor, sunt
                                    aliqua put a bird on it squid single-origin coffee nulla assumenda shoreditch et.
                                    Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred nesciunt sapiente
                                    ea proident.
                                </div>
                            </div>
                        </div>

                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h4 class="panel-title">
                                    <a class="accordion-toggle collapsed" data-toggle="collapse"
                                       data-parent="#accordion" href="#collapseTwo">
                                        <i class="ace-icon fa fa-angle-right bigger-110"
                                           data-icon-hide="ace-icon fa fa-angle-down"
                                           data-icon-show="ace-icon fa fa-angle-right"></i>
                                        &nbsp;Group Item #2
                                    </a>
                                </h4>
                            </div>

                            <div class="panel-collapse collapse" id="collapseTwo">
                                <div class="panel-body">
                                    Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry
                                    richardson ad squid. 3 wolf moon officia aute, non cupidatat skateboard dolor
                                    brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon tempor, sunt
                                    aliqua put a bird on it squid single-origin coffee nulla assumenda shoreditch et.
                                    Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred nesciunt sapiente
                                    ea proident.
                                </div>
                            </div>
                        </div>

                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h4 class="panel-title">
                                    <a class="accordion-toggle collapsed" data-toggle="collapse"
                                       data-parent="#accordion" href="#collapseThree">
                                        <i class="ace-icon fa fa-angle-right bigger-110"
                                           data-icon-hide="ace-icon fa fa-angle-down"
                                           data-icon-show="ace-icon fa fa-angle-right"></i>
                                        &nbsp;Group Item #3
                                    </a>
                                </h4>
                            </div>

                            <div class="panel-collapse collapse" id="collapseThree">
                                <div class="panel-body">
                                    Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry
                                    richardson ad squid. 3 wolf moon officia aute, non cupidatat skateboard dolor
                                    brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon tempor, sunt
                                    aliqua put a bird on it squid single-origin coffee nulla assumenda shoreditch et.
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- /sectionController:elements.accordion -->
                </div><!-- /.col -->

                <div class="col-sm-6">
                    <h3 class="header smaller lighter green">　进度条　</h3>

                    <div class="row">
                        <div class="col-xs-8">
                            <!-- #sectionController:elements.progressbar -->
                            <div class="progress" data-percent="66%">
                                <div class="progress-bar" style="width:66%;"></div>
                            </div>

                            <div class="progress progress-striped" data-percent="25%">
                                <div class="progress-bar progress-bar-success" style="width: 25%;"></div>
                            </div>

                            <!-- /sectionController:elements.progressbar -->
                            <div class="progress progress-small progress-striped active">
                                <div class="progress-bar progress-bar-warning" style="width: 40%;"></div>
                            </div>

                            <div class="progress progress-mini">
                                <div class="progress-bar progress-danger" style="width: 35%;"></div>
                            </div>

                            <div class="progress">
                                <div class="progress-bar progress-bar-success" style="width: 35%;"></div>

                                <div class="progress-bar progress-bar-warning" style="width: 20%;"></div>

                                <div class="progress-bar progress-bar-danger" style="width: 10%;"></div>
                            </div>

                            <div class="progress progress-striped">
                                <div class="progress-bar progress-bar-purple" style="width: 65%"></div>
                            </div>

                            <div class="progress progress-striped">
                                <div class="progress-bar progress-bar-pink" style="width: 40%"></div>
                            </div>

                            <div class="progress progress-striped active">
                                <div class="progress-bar progress-bar-yellow" style="width: 60%"></div>
                            </div>

                            <div class="progress progress-striped">
                                <div class="progress-bar progress-bar-inverse" style="width: 80%"></div>
                            </div>
                        </div><!-- /.col -->

                        <div class="col-xs-4 center">
                            <!-- #sectionController:plugins/charts.easypiechart -->
                            <div class="easy-pie-chart percentage" data-percent="20" data-color="#D15B47">
                                <span class="percent">20</span>%
                            </div>

                            <hr/>
                            <div class="easy-pie-chart percentage" data-percent="55" data-color="#87CEEB">
                                <span class="percent">55</span>%
                            </div>

                            <hr/>
                            <div class="easy-pie-chart percentage" data-percent="90" data-color="#87B87F">
                                <span class="percent">90</span>%
                            </div>

                            <!-- /sectionController:plugins/charts.easypiechart -->
                        </div><!-- /.col -->
                    </div><!-- /.row -->
                </div><!-- /.col -->
            </div><!-- /.row -->

            <h3 class="header smaller lighter red">提示框　</h3>

            <div class="row">
                <div class="col-sm-6">
                    <div class="widget-box">
                        <div class="widget-header">
                            <h4 class="smaller">
                                提示工具
                                <small>不同的方向和颜色</small>
                            </h4>
                        </div>

                        <div class="widget-body">
                            <div class="widget-main">
                                <p class="muted">
                                    Tight pants next level keffiyeh
                                    <a title="Default tooltip" data-rel="tooltip" href="javascript:void(0);">you
                                        probably</a>
                                    haven't heard of them. Farm-to-table seitan, mcsweeney's fixie sustainable quinoa
                                    8-bit american apparel
                                    <a title="Another tooltip" data-rel="tooltip" href="javascript:void(0);">have a</a>
                                    terry richardson vinyl chambray. A really ironic artisan
                                    <a data-rel="tooltip" href="javascript:void(0);"
                                       data-original-title="Another one here too">whatever</a>
                                    keytar, scenester farm-to-table banksy Austin
                                    <a title="The last tip!" data-rel="tooltip" href="javascript:void(0);">twitter</a>
                                    handle.
                                </p>


                                <!-- #sectionController:elements.tooltip -->
                                <p>
                                    <span class="btn btn-sm" data-rel="tooltip" title="Default">默认</span>
                                    <span class="btn btn-warning btn-sm tooltip-warning" data-rel="tooltip"
                                          data-placement="left" title="Left Warning">左边</span>
                                    <span class="btn btn-success btn-sm tooltip-success" data-rel="tooltip"
                                          data-placement="right" title="Right Success">右边</span>
                                    <span class="btn btn-danger btn-sm tooltip-error" data-rel="tooltip"
                                          data-placement="top" title="Top Danger">顶部</span>
                                    <span class="btn btn-info btn-sm tooltip-info" data-rel="tooltip"
                                          data-placement="bottom" title="Bottm Info">底部</span>
                                </p>

                                <!-- /sectionController:elements.tooltip -->
                            </div>
                        </div>
                    </div>
                </div><!-- /.col -->

                <div class="col-sm-6">
                    <div class="widget-box">
                        <div class="widget-header">
                            <h4 class="smaller">弹出框</h4>
                        </div>

                        <div class="widget-body">
                            <div class="widget-main">
                                <!-- #sectionController:elements.popover -->
                                <p>
                                    <span class="btn btn-sm" data-rel="popover" title="Default"
                                          data-content="Heads up! This alert needs your attention, but it's not super important.">默认</span>
                                    <span class="btn btn-success btn-sm popover-success" data-rel="popover"
                                          data-placement="right"
                                          title="<i class='ace-icon fa fa-check green'></i> Right Success"
                                          data-content="Well done! You successfully read this important alert message.">左边</span>
                                    <span class="btn btn-warning btn-sm popover-warning" data-rel="popover"
                                          data-placement="left"
                                          title="<i class='ace-icon fa fa-exclamation-triangle orange'></i> Left Warning"
                                          data-content="Warning! Best check yo self, you're not looking too good.">右边</span>
                                </p>

                                <p>
                                    <span class="btn btn-danger btn-sm popover-error" data-rel="popover"
                                          data-placement="top"
                                          data-original-title="<i class='ace-icon fa fa-bolt red'></i> Top Danger"
                                          data-content="Oh snap! Change a few things up and try submitting again.">顶部</span>
                                    <span class="btn btn-info btn-sm popover-info" data-rel="popover"
                                          data-placement="bottom" title="Some Info"
                                          data-content="Heads up! This alert needs your attention, but it's not super important.">底部</span>
                                    <span class="btn btn-inverse btn-sm popover-notitle" data-rel="popover"
                                          data-placement="bottom" data-content="Popover without a title!">无标题</span>
                                </p>

                                <!-- /sectionController:elements.popover -->
                            </div>
                        </div>
                    </div>
                </div><!-- /.col -->
            </div><!-- /.row -->


            <div class="row">
                <div class="col-sm-6">
                    <h3 class="header smaller lighter green">
                        <i class="ace-icon fa fa-bullhorn"></i>
                        警告框
                    </h3>

                    <div class="alert alert-danger">
                        <button type="button" class="close" data-dismiss="alert">
                            <i class="ace-icon fa fa-times"></i>
                        </button>

                        <strong>
                            <i class="ace-icon fa fa-times"></i>
                            哦，太简单了!
                        </strong>

                        改变一些事情再试着提交.
                        <br/>
                    </div>

                    <div class="alert alert-warning">
                        <button type="button" class="close" data-dismiss="alert">
                            <i class="ace-icon fa fa-times"></i>
                        </button>
                        <strong>提醒!</strong>

                        最好自己检查下，看起来不好.
                        <br/>
                    </div>

                    <div class="alert alert-block alert-success">
                        <button type="button" class="close" data-dismiss="alert">
                            <i class="ace-icon fa fa-times"></i>
                        </button>

                        <p>
                            <strong>
                                <i class="ace-icon fa fa-check"></i>
                                做的好!
                            </strong>
                            你成功的读取了一条重要的警告信息.
                        </p>

                        <p>
                            <button class="btn btn-sm btn-success">做这个</button>
                            <button class="btn btn-sm">或这一个</button>
                        </p>
                    </div>

                    <div class="alert alert-info">
                        <button type="button" class="close" data-dismiss="alert">
                            <i class="ace-icon fa fa-times"></i>
                        </button>
                        <strong>小心!</strong>

                        这条警告需要你的注意, 但它并不重要.
                        <br/>
                    </div>
                </div><!-- /.col -->

                <div class="col-sm-6">
                    <h3 class="row header smaller lighter orange">
										<span class="col-sm-8">
											<i class="ace-icon fa fa-bell"></i>
											通知框
										</span><!-- /.col -->

										<span class="col-sm-4">
											<label class="pull-right inline">
                                                <small class="muted">深色:</small>

                                                <input id="gritter-light" checked="" type="checkbox"
                                                       class="ace ace-switch ace-switch-5"/>
                                                <span class="lbl middle"></span>
                                            </label>
										</span><!-- /.col -->
                    </h3>

                    <p>
                        <small class="lighter">点击看看</small>
                    </p>

                    <!-- #sectionController:plugins/misc.gritter -->
                    <p>
                        <button class="btn" id="gritter-regular">常规</button>
                        <button class="btn btn-info" id="gritter-sticky">固定</button>
                        <button class="btn btn-success" id="gritter-without-image">没有图</button>
                    </p>

                    <p>
                        <button class="btn btn-danger" id="gritter-error">错误</button>
                        <button class="btn btn-warning" id="gritter-max3">最多3个</button>
                        <button class="btn btn-primary" id="gritter-center">居中</button>
                        <button class="btn btn-inverse" id="gritter-remove">移除</button>
                    </p>

                    <!-- /sectionController:plugins/misc.gritter -->
                </div><!-- /.col -->
            </div><!-- /.row -->


            <div class="row">
                <div class="col-sm-6">
                    <div>
                        <h3 class="header smaller lighter purple">
                            Bootstrap 模态框
                            <small>(Bootbox.js)</small>
                        </h3>

                        <!-- #sectionController:plugins/misc.bootbox -->
                        <p>
                            <button class="btn" id="bootbox-regular">常规对话框</button>
                            <button class="btn btn-info" id="bootbox-confirm">确认框</button>
                            <button class="btn btn-success" id="bootbox-options">更多选项</button>
                        </p>

                        <!-- /sectionController:plugins/misc.bootbox -->
                    </div><!-- /.row -->

                    <div class="space-24"></div>

                    <div class="row">
                        <div class="col-xs-12">
                            <h3 class="header smaller lighter grey">
                                <i class="ace-icon fa fa-spinner fa-spin orange bigger-125"></i>
                                旋转插件
                                <small>(spin.js)</small>
                                <label class="pull-right inline">
                                    <small class="muted">改变值开始...</small>
                                </label>
                            </h3>
                        </div>

                        <div class="col-xs-12">
                            <div class="pull-right center spinner-preview" id="spinner-preview"></div>

                            <form class="form-horizontal" id="spinner-opts">
                                <!-- #sectionController:plugins/jquery.slider -->
                                <label class="inline">
                                    <small class="lighter">线:</small>

                                    <input class="hidden" type="text" name="lines" data-min="5" data-max="16"
                                           data-step="2" value="12"/>
                                </label>

                                <div class="space-0"></div>

                                <label class="inline">
                                    <small class="lighter">长度:</small>

                                    <input class="hidden" type="text" name="length" data-min="0" data-max="30"
                                           value="7"/>
                                </label>

                                <div class="space-0"></div>

                                <label class="inline">
                                    <small class="lighter">宽度:</small>

                                    <input class="hidden" type="text" name="width" data-min="2" data-max="20"
                                           value="4"/>
                                </label>

                                <div class="space-0"></div>

                                <label class="inline">
                                    <small class="lighter">半径:</small>

                                    <input class="hidden" type="text" name="radius" data-min="0" data-max="40"
                                           value="10"/>
                                </label>

                                <div class="space-0"></div>

                                <label class="inline">
                                    <small class="lighter">角度:</small>

                                    <input class="hidden" type="text" name="corners" data-min="0" data-max="1"
                                           data-step="0.1" value="1"/>
                                </label>

                                <div class="space-0"></div>

                                <label class="inline">
                                    <small class="lighter">旋转:</small>

                                    <input class="hidden" type="text" name="rotate" data-min="0" data-max="90"
                                           value="0"/>
                                </label>

                                <div class="space-0"></div>

                                <label class="inline">
                                    <small class="lighter">踪迹:</small>

                                    <input class="hidden" type="text" name="trail" data-min="10" data-max="100"
                                           value="60"/>
                                </label>

                                <div class="space-0"></div>

                                <label class="inline">
                                    <small class="lighter">速度:</small>

                                    <input class="hidden" type="text" name="speed" data-min="0.5" data-max="2.2"
                                           data-step="0.1" value="1"/>
                                </label>

                                <!-- /sectionController:plugins/jquery.slider -->
                            </form>
                        </div><!-- /.row -->
                    </div><!-- /.row -->
                </div><!-- /.col -->

                <div class="col-sm-6">
                    <h3 class="header smaller lighter red">Bootstrap Wells
                    </h3>

                    <div class="well">
                        <h4 class="green smaller lighter">常规 Well</h4>
                        使用 well会产生一种内容凹陷的效果.
                    </div>

                    <div class="well well-lg" style="border-radius:6px">
                        <h4 class="blue">大 Well</h4>
                        通过两个可选类来控制内边距和圆角.
                    </div>
                    <div class="well well-sm" style="border-radius:3px"> 这是一个小 well</div>
                </div><!-- /.col -->
            </div><!-- /.row -->

            <div class="row">
                <div class="col-sm-6">
                    <h3 class="header smaller lighter green">下拉菜单</h3>

                    <!-- #sectionController:elements.dropdown -->
                    <div class="dropdown dropdown-preview">
                        <ul class="dropdown-menu">
                            <li>
                                <a href="javascript:void(0);" tabindex="-1">Action</a>
                            </li>

                            <li>
                                <a href="javascript:void(0);" tabindex="-1">Another action</a>
                            </li>

                            <li>
                                <a href="javascript:void(0);" tabindex="-1">Something else here</a>
                            </li>

                            <li class="divider"></li>

                            <li>
                                <a href="javascript:void(0);" tabindex="-1">Separated link</a>
                            </li>
                        </ul>
                    </div>

                    <!-- /sectionController:elements.dropdown -->

                    <!-- #sectionController:elements.dropdown.submenu -->
                    <div class="dropdown dropdown-preview">
                        <ul class="dropdown-menu dropdown-danger">
                            <li>
                                <a href="javascript:void(0);" tabindex="-1">Action</a>
                            </li>

                            <li>
                                <a href="javascript:void(0);" tabindex="-1">Another action</a>
                            </li>

                            <li>
                                <a href="javascript:void(0);" tabindex="-1">Something else here</a>
                            </li>

                            <li class="divider"></li>

                            <li class="dropdown-hover">
                                <a href="javascript:void(0);" tabindex="-1" class="clearfix">
                                    <span class="pull-left">More options</span>

                                    <i class="ace-icon fa fa-caret-right pull-right"></i>
                                </a>

                                <ul class="dropdown-menu dropdown-danger">
                                    <li>
                                        <a href="javascript:void(0);" tabindex="-1">Second level link</a>
                                    </li>

                                    <li>
                                        <a href="javascript:void(0);" tabindex="-1">Second level link</a>
                                    </li>

                                    <li>
                                        <a href="javascript:void(0);" tabindex="-1">Second level link</a>
                                    </li>

                                    <li>
                                        <a href="javascript:void(0);" tabindex="-1">Second level link</a>
                                    </li>

                                    <li>
                                        <a href="javascript:void(0);" tabindex="-1">Second level link</a>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </div>

                    <div class="dropdown dropdown-preview">
                        <ul class="dropdown-menu dropdown-purple">
                            <li>
                                <a href="javascript:void(0);" tabindex="-1">Action</a>
                            </li>

                            <li>
                                <a href="javascript:void(0);" tabindex="-1">Another action</a>
                            </li>

                            <li>
                                <a href="javascript:void(0);" tabindex="-1">Something else here</a>
                            </li>

                            <li class="divider"></li>

                            <li class="dropdown-hover dropup">
                                <a href="javascript:void(0);" tabindex="-1" class="clearfix">
                                    <span class="pull-left">More options</span>

                                    <i class="ace-icon fa fa-caret-left pull-right"></i>
                                </a>

                                <ul class="dropdown-menu dropdown-menu-right">
                                    <li>
                                        <a href="javascript:void(0);" tabindex="-1">Second level link</a>
                                    </li>

                                    <li>
                                        <a href="javascript:void(0);" tabindex="-1">Second level link</a>
                                    </li>

                                    <li>
                                        <a href="javascript:void(0);" tabindex="-1">Second level link</a>
                                    </li>

                                    <li>
                                        <a href="javascript:void(0);" tabindex="-1">Second level link</a>
                                    </li>

                                    <li>
                                        <a href="javascript:void(0);" tabindex="-1">Second level link</a>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </div>

                    <!-- /sectionController:elements.dropdown.submenu -->
                    <div class="dropup dropdown-preview">
                        <ul class="dropdown-menu dropdown-light">
                            <li>
                                <a href="javascript:void(0);" tabindex="-1">Action</a>
                            </li>

                            <li>
                                <a href="javascript:void(0);" tabindex="-1">Another action</a>
                            </li>

                            <li>
                                <a href="javascript:void(0);" tabindex="-1">Something else here</a>
                            </li>

                            <li class="divider"></li>

                            <li class="dropup dropdown-hover">
                                <a href="javascript:void(0);" tabindex="-1" class="clearfix">
                                    <span class="pull-left">More options</span>

                                    <i class="ace-icon fa fa-caret-right pull-right"></i>
                                </a>

                                <ul class="dropdown-menu pull-left">
                                    <li>
                                        <a href="javascript:void(0);" tabindex="-1">Second level link</a>
                                    </li>

                                    <li>
                                        <a href="javascript:void(0);" tabindex="-1">Second level link</a>
                                    </li>

                                    <li>
                                        <a href="javascript:void(0);" tabindex="-1">Second level link</a>
                                    </li>

                                    <li>
                                        <a href="javascript:void(0);" tabindex="-1">Second level link</a>
                                    </li>

                                    <li>
                                        <a href="javascript:void(0);" tabindex="-1">Second level link</a>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </div>

                    <!-- #sectionController:elements.dropdown.small -->
                    <div class="dropdown dropdown-preview">
                        <ul class="dropdown-menu dropdown-icon-only dropdown-50 dropdown-light">
                            <li class="action-buttons">
                                <a href="javascript:void(0);" tabindex="-1" class="center">
                                    <i class="fa fa-flag blue bigger-130"></i>
                                </a>
                            </li>

                            <li class="action-buttons">
                                <a href="javascript:void(0);" tabindex="-1" class="center">
                                    <i class="fa fa-flag red bigger-130"></i>
                                </a>
                            </li>

                            <li class="action-buttons">
                                <a href="javascript:void(0);" tabindex="-1" class="center">
                                    <i class="fa fa-flag green bigger-130"></i>
                                </a>
                            </li>

                            <li class="action-buttons">
                                <a href="javascript:void(0);" tabindex="-1" class="center">
                                    <i class="fa fa-flag orange bigger-130"></i>
                                </a>
                            </li>
                        </ul>
                    </div>

                    <!-- /sectionController:elements.dropdown.small -->
                </div>

                <div class="col-sm-6">
                    <h3 class="row header smaller lighter blue">
										<span class="col-sm-7">
											<i class="ace-icon fa fa-th-large"></i>
											胶囊式导航栏
										</span><!-- /.col -->

										<span class="col-sm-5">
											<label class="pull-right inline">
                                                <small class="muted">堆叠:</small>

                                                <input id="id-pills-stacked" checked="" type="checkbox"
                                                       class="ace ace-switch ace-switch-5"/>
                                                <span class="lbl middle"></span>
                                            </label>
										</span><!-- /.col -->
                    </h3>

                    <ul class="nav nav-pills">
                        <li class="active">
                            <a href="javascript:void(0);">Home</a>
                        </li>

                        <li class="disabled">
                            <a href="javascript:void(0);">Profile</a>
                        </li>

                        <li>
                            <a href="javascript:void(0);">Messages</a>
                        </li>
                    </ul>
                </div>
            </div>

            <script type="text/javascript">
                var $path_assets = "<%=toolkitPath%>/bxui/aceadmin/style";//this will be used in gritter alerts containing images
            </script>

            <!-- PAGE CONTENT ENDS -->
        </div><!-- /.col -->
    </div><!-- /.row -->
</div><!-- /.page-content -->

<!-- inline scripts related to this page -->
<script type="text/javascript">
    jQuery(function ($) {
        baosightRequire.requireFunct(['bxeasypiechart'], function () {
            $('#accordion-style').on('click', function (ev) {
                var target = $('input', ev.target);
                var which = parseInt(target.val());
                if (which == 2) $('#accordion').addClass('accordion-style2');
                else $('#accordion').removeClass('accordion-style2');
            });

            //$('[href="#collapseTwo"]').trigger('click');


            var oldie = /msie\s*(8|7|6)/.test(navigator.userAgent.toLowerCase());
            $('.easy-pie-chart.percentage').each(function () {
                $(this).bxeasypiechart({
                    barColor: $(this).data('color'),
                    trackColor: '#EEEEEE',
                    scaleColor: false,
                    lineCap: 'butt',
                    lineWidth: 8,
                    animate: oldie ? false : 1000,
                    size: 75
                });
            });

            $('[data-rel=tooltip]').tooltip();
            $('[data-rel=popover]').popover({html: true});

            $('#gritter-regular').on(ace.click_event, function () {
                $.gritter.add({
                    title: '这是一个常规的通知框！',
                    text: 'This will fade out after a certain amount of time. Vivamus eget tincidunt velit. Cum sociis natoque penatibus et <a href="javascript:void(0);" class="blue">magnis dis parturient</a> montes, nascetur ridiculus mus.',
                    image: $path_assets + '/avatars/avatar1.png',
                    sticky: false,
                    time: '',
                    class_name: (!$('#gritter-light').get(0).checked ? 'gritter-light' : '')
                });

                return false;
            });

            $('#gritter-sticky').on(ace.click_event, function () {
                var unique_id = $.gritter.add({
                    title: '这是一个不变的（不会自动消失）通知框',
                    text: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus eget tincidunt velit. Cum sociis natoque penatibus et <a href="javascript:void(0);" class="red">magnis dis parturient</a> montes, nascetur ridiculus mus.',
                    image: $path_assets + '/avatars/avatar.png',
                    sticky: true,
                    time: '',
                    class_name: 'gritter-info' + (!$('#gritter-light').get(0).checked ? ' gritter-light' : '')
                });

                return false;
            });


            $('#gritter-without-image').on(ace.click_event, function () {
                $.gritter.add({
                    // (string | mandatory) the heading of the notification
                    title: '这个一个没有图像的通知框!',
                    // (string | mandatory) the text inside the notification
                    text: 'This will fade out after a certain amount of time. Vivamus eget tincidunt velit. Cum sociis natoque penatibus et <a href="javascript:void(0);" class="orange">magnis dis parturient</a> montes, nascetur ridiculus mus.',
                    class_name: 'gritter-success' + (!$('#gritter-light').get(0).checked ? ' gritter-light' : '')
                });

                return false;
            });


            $('#gritter-max3').on(ace.click_event, function () {
                $.gritter.add({
                    title: '这个通知框同一时间一个屏幕上最多3个!',
                    text: 'This will fade out after a certain amount of time. Vivamus eget tincidunt velit. Cum sociis natoque penatibus et <a href="javascript:void(0);" class="green">magnis dis parturient</a> montes, nascetur ridiculus mus.',
                    image: $path_assets + '/avatars/avatar3.png',
                    sticky: false,
                    before_open: function () {
                        if ($('.gritter-item-wrapper').length >= 3) {
                            return false;
                        }
                    },
                    class_name: 'gritter-warning' + (!$('#gritter-light').get(0).checked ? ' gritter-light' : '')
                });

                return false;
            });


            $('#gritter-center').on(ace.click_event, function () {
                $.gritter.add({
                    title: '这是一个居中的通知框',
                    text: 'Just add a "gritter-center" class_name to your $.gritter.add or globally to $.gritter.options.class_name',
                    class_name: 'gritter-info gritter-center' + (!$('#gritter-light').get(0).checked ? ' gritter-light' : '')
                });

                return false;
            });

            $('#gritter-error').on(ace.click_event, function () {
                $.gritter.add({
                    title: '这是一个警告通知框',
                    text: 'Just add a "gritter-light" class_name to your $.gritter.add or globally to $.gritter.options.class_name',
                    class_name: 'gritter-error' + (!$('#gritter-light').get(0).checked ? ' gritter-light' : '')
                });

                return false;
            });


            $("#gritter-remove").on(ace.click_event, function () {
                $.gritter.removeAll();
                return false;
            });


            ///////


            $("#bootbox-regular").on(ace.click_event, function () {
                bootbox.prompt("What's you name?", function (result) {
                    if (result === null) {

                    } else {

                    }
                });
            });


            $("#bootbox-confirm").on(ace.click_event, function () {
                bootbox.confirm({
                            message: "你确定?",
                            buttons: {
                                confirm: {
                                    label: "确定",
                                    className: "btn-primary btn-sm",
                                },
                                cancel: {
                                    label: "取消",
                                    className: "btn-sm",
                                }
                            },
                            callback: function (result) {
                                if (result) alert(1)
                            }
                        }
                );
            });


            $("#bootbox-options").on(ace.click_event, function () {
                bootbox.dialog({
                    message: "<span class='bigger-110'>我是带有按钮的自定义框</span>",
                    buttons: {
                        "success": {
                            "label": "<i class='ace-icon fa fa-check'></i> 成功!",
                            "className": "btn-sm btn-success",
                            "callback": function () {
                                //Example.show("great success");
                            }
                        },
                        "danger": {
                            "label": "危险!",
                            "className": "btn-sm btn-danger",
                            "callback": function () {
                                //Example.show("uh oh, look out!");
                            }
                        },
                        "click": {
                            "label": "点击我!",
                            "className": "btn-sm btn-primary",
                            "callback": function () {
                                //Example.show("Primary button");
                            }
                        },
                        "button": {
                            "label": "只是一个按钮...",
                            "className": "btn-sm"
                        }
                    }
                });
            });


            $('#spinner-opts small').css({display: 'inline-block', width: '60px'})

            var slide_styles = ['', 'green', 'red', 'purple', 'orange', 'dark'];
            var ii = 0;
            $("#spinner-opts input[type=text]").each(function () {
                var $this = $(this);
                $this.hide().after('<span />');
                $this.next().addClass('ui-slider-small').
                addClass("inline ui-slider-" + slide_styles[ii++ % slide_styles.length]).
                css({'width': '125px'}).slider({
                    value: parseInt($this.val()),
                    range: "min",
                    animate: true,
                    min: parseInt($this.data('min')),
                    max: parseInt($this.data('max')),
                    step: parseFloat($this.data('step')),
                    slide: function (event, ui) {
                        $this.val(ui.value);
                        spinner_update();
                    }
                });
            });


            $.fn.spin = function (opts) {
                this.each(function () {
                    var $this = $(this),
                            data = $this.data();

                    if (data.spinner) {
                        data.spinner.stop();
                        delete data.spinner;
                    }
                    if (opts !== false) {
                        data.spinner = new Spinner($.extend({color: $this.css('color')}, opts)).spin(this);
                    }
                });
                return this;
            };

            function spinner_update() {
                var opts = {};
                $('#spinner-opts input[type=text]').each(function () {
                    opts[this.name] = parseFloat(this.value);
                    console.log("spin: property: " + this.name + ", value: " + this.value +".");
                });
                $('#spinner-preview').spin(opts);
            }


            $('#id-pills-stacked').removeAttr('checked').on('click', function () {
                $('.nav-pills').toggleClass('nav-stacked');
            });
        });
    });
</script>

</body>
</html>
