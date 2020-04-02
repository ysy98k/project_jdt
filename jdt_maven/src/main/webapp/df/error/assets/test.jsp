<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>Dashboard - Ace Admin</title>

		<meta name="description" content="overview &amp; stats" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="css/bootstrap.min.css" />
		<link rel="stylesheet" href="css/font-awesome.min.css" />

		<!-- page specific plugin styles -->

		<!-- text fonts -->
		<link rel="stylesheet" href="css/ace-fonts.css" />

		<!-- ace styles -->
		<link rel="stylesheet" href="css/ace.min.css" />

		<!--[if lte IE 9]>
			<link rel="stylesheet" href="css/ace-part2.min.css" />
		<![endif]-->
		<link rel="stylesheet" href="css/ace-skins.min.css" />
		<link rel="stylesheet" href="css/ace-rtl.min.css" />
        
        <link rel="stylesheet" href="css/style-index.css" />

		<!--[if lte IE 9]>
		  <link rel="stylesheet" href="../assets/css/ace-ie.min.css" />
		<![endif]-->

		<!-- inline styles related to this page -->

		<!-- ace settings handler -->
        
		<script src="js/ace-extra.min.js"></script>
        
        <script src="js/jquery.min.js"></script>
   		<script src="js/bootstrap.min.js"></script>

		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->

		<!--[if lte IE 8]>
		<script src="../assets/js/html5shiv.js"></script>
		<script src="../assets/js/respond.min.js"></script>
		<![endif]-->

	</head>

<body class="no-skin">
	<div id="navbar" class="navbar navbar-default">
    	<div class="navbar-container" id="navbar-container">
				<!-- #sectionController:basics/sidebar.mobile.toggle -->
<!--				<button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler">
					<span class="sr-only">Toggle sidebar</span>

					<span class="icon-bar"></span>

					<span class="icon-bar"></span>

					<span class="icon-bar"></span>
				</button>
-->
				<!-- /sectionController:basics/sidebar.mobile.toggle -->
				<div class="navbar-header pull-left">
					<!-- #sectionController:basics/navbar.layout.brand -->
					<a href="#" class="navbar-brand">
						<small>
							<i class="logo"></i>
							<span class="logoText"></span>
						</small>
					</a>

					<!-- /sectionController:basics/navbar.layout.brand -->

					<!-- #sectionController:basics/navbar.toggle -->

					<!-- /sectionController:basics/navbar.toggle -->
				</div>
                

				<!-- #sectionController:basics/navbar.dropdown -->
				<div class="navbar-buttons navbar-header pull-left" role="navigation">
					<ul class="nav ace-nav">
						<li class="xInsightGray">
							<a data-toggle="dropdown" href="#" class="dropdown-toggle">
<!--								<img class="nav-user-photo" src="../assets/avatars/user.jpg" alt="Jason's Photo" />
-->								<span class="navButtonText">
									产&nbsp;&nbsp;&nbsp;&nbsp;品
								</span>

								<i class="ace-icon fa fa-caret-down"></i>
							</a>

							<ul class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-close">
								<li class="dropdown-submenu">
									<a href="#">
                                    	<!--<i class="ace-icon fa fa-power-off"></i>-->
										数据存储服务OTS
									</a>
										<ul class="dropdown-menu menu-right">
										<li class="dropdown-yellow menu-right">
											<a href="#">
											submenu1
											</a>
										</li>
										
										<li class="dropdown-yellow menu-right">
											<a href="#">
											submenu2
											</a>
										</li>
										
										</ul>
								</li>

								<li>
									<a href="#">
										过程数据服务PDS
									</a>
								</li>

<!--								<li class="divider"></li>
-->
								<li>
									<a href="#">
										组态展示服务CVS
									</a>
								</li>
                                
                                <li>
									<a href="#">
										表达式计算服务FCS
									</a>
								</li>
							</ul>
						</li>
                        
                        <li class="xInsightGray">
							<a data-toggle="dropdown" href="#" class="dropdown-toggle">
<!--								<img class="nav-user-photo" src="../assets/avatars/user.jpg" alt="Jason's Photo" />
-->								<span class="navButtonText">
									行业解决方案
								</span>
							</a>
						</li>

						<!-- /sectionController:basics/navbar.user_menu -->
					</ul>
				</div>
                
                <div class="navbar-buttons navbar-header pull-right" role="navigation">
					<ul class="nav ace-nav">
                        <li class="xInsightGray">
							<a data-toggle="dropdown" href="#" class="dropdown-toggle">
<!--								<img class="nav-user-photo" src="../assets/avatars/user.jpg" alt="Jason's Photo" />
-->								<span class="user-info">
									用户登录
								</span>
							</a>
						</li>

						<!-- /sectionController:basics/navbar.user_menu -->
					</ul>
				</div>

				<!-- /sectionController:basics/navbar.dropdown -->
			</div><!-- /.navbar-container -->
		</div>

	<div id="myCarousel" class="carousel slide" data-ride="carousel" data-interval="3000">
		<!-- 轮播（Carousel）指标 -->
		<ol class="carousel-indicators">
			<li data-target="#myCarousel" data-slide-to="0" class="active"></li>
			<li data-target="#myCarousel" data-slide-to="1"></li>
			<li data-target="#myCarousel" data-slide-to="2"></li>
			<li data-target="#myCarousel" data-slide-to="3"></li>
		</ol>  
		<!-- 轮播（Carousel）项目 -->
		<div class="carousel-inner">
		<div class="item active">
			<div class="BGBlue">
            	<div class="contentWrapBlue">
                    <div class="carousel-caption">
                    	<div>
                        	<div class="titleCN pull-left">数据存储服务OTS</div>
                            <div class="knowMore pull-right"></div>
                        </div>
                    	<div class="titleEN">Open&nbsp;Table&nbsp;Service</div>
                    	<div class="introText">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;OTS是宝信大数据应用开发平台为开发者提供的半结构化数据存储服务，构筑在业界事实标准Hadoop分布式架构之上，着眼于海量企业数据、机器数据、社会化数据的存储处理。</div>
            		</div>
                    <div class="blankCarousel"></div>
                </div>
            </div>
		</div>
        
		<div class="item">
			<div class="BGGreen">
            	<div class="contentWrapGreen">
                    <div class="carousel-caption">
                    	<div>
                        	<div class="titleCN pull-left">过程数据服务PDS</div>
                            <div class="knowMore pull-right"></div>
                        </div>
                    	<div class="titleEN">Process&nbsp;Data&nbsp;Service</div>
                    	<div class="introText">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;PDS是宝信大数据应用开发平台为开发者提供的过程数据采集、传输、存储和加工处理服务。不论是在智能制造，还是在智慧家居，PDS为开发者提供过程数据处理的端到端完整解决方案。</div>
            		</div>
                    <div class="blankCarousel"></div>
                </div>
            </div>
		</div>
        
		<div class="item">
			<div class="BGOrange">
            	<div class="contentWrapOrange">
                    <div class="carousel-caption">
                    	<div>
                        	<div class="titleCN pull-left">组态展示服务CVS</div>
                            <div class="knowMore pull-right"></div>
                        </div>
                    	<div class="titleEN">Configuration&nbsp;View&nbsp;Service</div>
                    	<div class="introText">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CVS是宝信大数据应用开发平台为开发者提供的可配置的页面展示服务。只需要在可视化设计器里进行简单拖拽就能完成页面开发，并能通过配置轻松实现实时监控报警。</div>
            		</div>
                    <div class="blankCarousel"></div>
                </div>
			</div>
		</div>
        
		<div class="item">
			<div class="BGPurple">
            	<div class="contentWrapPurple">
                    <div class="carousel-caption">
                    	<div>
                        	<div class="titleCN pull-left">表达式计算服务FCS</div>
                            <div class="knowMore pull-right"></div>
                        </div>
                    	<div class="titleEN">Formula&nbsp;Calculation&nbsp;Service</div>
                    	<div class="introText">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FCS是宝信大数据应用开发平台为开发者提供的基于表达式配置解析的计算服务，具备强大的表达式解析引擎，可以灵活配置表达式计算调用方式，并内置丰富的计算函数库。</div>
            		</div>
                    <div class="blankCarousel"></div>
                </div>
			</div>
		</div>
	</div>
       <!-- 轮播（Carousel）导航 -->
       <a class="carousel-control left" href="#myCarousel"
          data-slide="prev">&lsaquo;</a>
       <a class="carousel-control right" href="#myCarousel"
          data-slide="next">&rsaquo;</a>
    </div>
    
    <div class="slogan">
    	<div class="sloganWrap">
    		<div class="cloud"></div>
            <div class="sloganText"></div>
        </div>
        <div class="pageTurn pull-right"></div>
    </div>

    <div class="solution">
    	<div class="pageTurn pull-right"></div>

    	<div class="frame">
        	<div class="section powerCloud">
            	<div class="dash">
                	<div class="title pull-left"><img src="images/powerCloud.png"/><span>能源云</span></div>
                    <a href="#" class="knowMore pull-right">了解更多&gt;&gt;</a>
                    <div class="content pull-left">
                    	<p>基于PDS轻松采集能耗数据</p>
                        <p>基于OTS实现海量能耗数据安全存储</p>
                        <p>基于FCS对能耗数据进行加工分析</p>
                        <p>基于CVS设计器拖拽即可完成能耗监控，分析展示画面的搭建</p>
                    </div>
                </div>
            </div>
            <div class="gap"></div>
            <div class="section trafficCloud">
            	<div class="dash">
                	<div class="title pull-left"><img src="images/trafficCloud.png"/><span>交通云</span></div>
                    <a href="#" class="knowMore pull-right">了解更多&gt;&gt;</a>
                    <div class="content pull-left">
                    	<p>基于强大的OTS，实现交通海量大数据的安全存储，并实现海量信息的快速全文检索</p>
                    </div>
                </div>
            </div>
            <div class="gap"></div>
            <div class="section industryCloud">
            	<div class="dash">
                	<div class="title pull-left"><img src="images/industryCloud.png"/><span>制造云</span></div>
                    <a href="#" class="knowMore pull-right">了解更多&gt;&gt;</a>
                    <div class="content pull-left">
                    	<p>基于PDS进行数据采集</p>
						<p>基于OTS实现海量数据存储</p>
						<p>基于FCS进行数据加工计算，分析处理</p>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="dashLine"></div>
        
        <div class="comunityTitle">开源社区</div>
        <div class="comunityFrame">
        	<a href="#" class="comunityLink">Apache Software</a>
            <div class="comunityLinkSpliter">|</div>
            <a href="#" class="comunityLink">HBase</a>
            <div class="comunityLinkSpliter">|</div>
            <a href="#" class="comunityLink">Hadoop</a>
            <div class="comunityLinkSpliter">|</div>
            <a href="#" class="comunityLink">kafka</a>
            <div class="comunityLinkSpliter">|</div>
            <a href="#" class="comunityLink">Spark</a>
            <div class="comunityLinkSpliter">|</div>
            <a href="#" class="comunityLink">Tomcat</a>
            <div class="comunityLinkSpliter">|</div>
            <a href="#" class="comunityLink">Solr</a>
            <div class="comunityLinkSpliter">|</div>
            <a href="#" class="comunityLink">Sqoop</a>
            <div class="comunityLinkSpliter">|</div>
            <a href="#" class="comunityLink">PostgreSQL</a>
            <div class="comunityLinkSpliter">|</div>
            <a href="#" class="comunityLink">Redis</a>
        </div>
        
    </div>
    
    <div class="bottom">
    	<div><a href="#" class="pull-left">关于我们</a></div>
        <div><a href="#" class="pull-left">法律声明</a></div>
        <div><a href="#" class="pull-left">帮助&amp;支持</a></div>
        <div class="copyright">©&nbsp;1998-2015&nbsp;上海宝信软件股份有限公司&nbsp;版权所有</div>
    </div>

	<link rel="stylesheet" href="css/ace.onpage-help.css" />
	<link rel="stylesheet" href="css/sunburst.css" />

	<script type="text/javascript"> ace.vars['base'] = '..'; </script>
	<script src="js/ace.onpage-help.js"></script>
	<script src="js/rainbow.js"></script>
	<script src="js/language/generic.js"></script>
	<script src="js/language/html.js"></script>
	<script src="js/language/css.js"></script>
	<script src="js/language/javascript.js"></script>
</body>
</html>
