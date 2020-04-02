<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>窗口小部件</title>

</head>

	<body>
		
		<%@ include file="/bxui/bxuihead.jsp"%>
        <script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
		<!-- /sectionController:basics/navbar.layout -->
		

				<!-- /sectionController:basics/content.breadcrumbs -->
				<div class="page-content">
					<!-- /sectionController:settings.box -->
					<div class="page-header">
						<h1>
							窗口小部件
							<small>
								<i class="ace-icon fa fa-angle-double-right"></i>
								窗口插件 &amp; 容器
							</small>
						</h1>
					</div><!-- /.page-header -->

					<div class="row">
						<div class="col-xs-12">
							<!-- PAGE CONTENT BEGINS -->
							<div class="row">
								<div class="col-xs-12 col-sm-6 widget-container-col">
									<!-- #sectionController:custom/widget-box -->
									<div class="widget-box">
										<div class="widget-header">
											<h5 class="widget-title">默认的组件框</h5>

											<!-- #sectionController:custom/widget-box.toolbar -->
											<div class="widget-toolbar">
												<div class="widget-menu">
													<a href="#" data-action="settings" data-toggle="dropdown">
														<i class="ace-icon fa fa-bars"></i>
													</a>

													<ul class="dropdown-menu dropdown-menu-right dropdown-light-blue dropdown-caret dropdown-closer">
														<li>
															<a data-toggle="tab" href="#dropdown1">Option#1</a>
														</li>

														<li>
															<a data-toggle="tab" href="#dropdown2">Option#2</a>
														</li>
													</ul>
												</div>

												<a href="#" data-action="fullscreen" class="orange2">
													<i class="ace-icon fa fa-expand"></i>
												</a>

												<a href="#" data-action="reload">
													<i class="ace-icon fa fa-refresh"></i>
												</a>

												<a href="#" data-action="collapse">
													<i class="ace-icon fa fa-chevron-up"></i>
												</a>

												<a href="#" data-action="close">
													<i class="ace-icon fa fa-times"></i>
												</a>
											</div>

											<!-- /sectionController:custom/widget-box.toolbar -->
										</div>

										<div class="widget-body">
											<div class="widget-main">
												<p class="alert alert-info">
													Nunc aliquam enim ut arcu aliquet adipiscing. Fusce dignissim volutpat justo non consectetur. Nulla fringilla eleifend consectetur.
												</p>
												<p class="alert alert-success">
													Raw denim you probably haven't heard of them jean shorts Austin.
												</p>
											</div>
										</div>
									</div>

									<!-- /sectionController:custom/widget-box -->
								</div>

								<div class="col-xs-12 col-sm-6 widget-container-col">
									<div class="widget-box widget-color-blue">
										<!-- #sectionController:custom/widget-box.options -->
										<div class="widget-header">
											<h5 class="widget-title bigger lighter">
												<i class="ace-icon fa fa-table"></i>
												表格  & 颜色
											</h5> 

											<div class="widget-toolbar widget-toolbar-light no-border">
												<select id="simple-colorpicker-1" class="hide">
													<option data-class="blue" value="#438EB9">#438EB9</option>
													<option data-class="blue2" value="#5090C1">#5090C1</option>
													<option data-class="blue3" value="#6379AA">#6379AA</option>
													<option data-class="green" value="#82AF6F">#82AF6F</option>
													<option data-class="green2" value="#2E8965">#2E8965</option>
													<option data-class="green3" value="#5FBC47">#5FBC47</option>
													<option data-class="red" value="#E2755F">#E2755F</option>
													<option data-class="red2" value="#E04141">#E04141</option>
													<option data-class="red3" value="#D15B47">#D15B47</option>
													<option data-class="orange" value="#FFC657">#FFC657</option>
													<option data-class="purple" value="#7E6EB0">#7E6EB0</option>
													<option data-class="pink" value="#CE6F9E">#CE6F9E</option>
													<option data-class="dark" value="#404040">#404040</option>
													<option data-class="grey" value="#848484">#848484</option>
													<option data-class="default" value="#EEE">#EEE</option>
												</select>
											</div>
										</div>

										<!-- /sectionController:custom/widget-box.options -->
										<div class="widget-body">
											<div class="widget-main no-padding">
												<table class="table table-striped table-bordered table-hover">
													<thead class="thin-border-bottom">
														<tr>
															<th>
																<i class="ace-icon fa fa-user"></i>
																User
															</th>

															<th>
																<i>@</i>
																Email
															</th>
															<th class="hidden-480">Status</th>
														</tr>
													</thead>

													<tbody>
														<tr>
															<td class="">Alex</td>

															<td>
																<a href="#">alex@email.com</a>
															</td>

															<td class="hidden-480">
																<span class="label label-warning">Pending</span>
															</td>
														</tr>

														<tr>
															<td class="">Fred</td>

															<td>
																<a href="#">fred@email.com</a>
															</td>

															<td class="hidden-480">
																<span class="label label-success arrowed-in arrowed-in-right">Approved</span>
															</td>
														</tr>

														<tr>
															<td class="">Jack</td>

															<td>
																<a href="#">jack@email.com</a>
															</td>

															<td class="hidden-480">
																<span class="label label-warning">Pending</span>
															</td>
														</tr>

														<tr>
															<td class="">John</td>

															<td>
																<a href="#">john@email.com</a>
															</td>

															<td class="hidden-480">
																<span class="label label-inverse arrowed">Blocked</span>
															</td>
														</tr>

														<tr>
															<td class="">James</td>

															<td>
																<a href="#">james@email.com</a>
															</td>

															<td class="hidden-480">
																<span class="label label-info arrowed-in arrowed-in-right">Online</span>
															</td>
														</tr>
													</tbody>
												</table>
											</div>
										</div>
									</div>
								</div><!-- /.span -->
							</div><!-- /.row -->

							<div class="space-24"></div>

							<div class="row">
								<div class="col-xs-12 col-sm-6 widget-container-col">
									<div class="widget-box widget-color-orange collapsed">
										<!-- #sectionController:custom/widget-box.options.collapsed -->
										<div class="widget-header widget-header-small">
											<h6 class="widget-title">
												<i class="ace-icon fa fa-sort"></i>
												小标题 & 折叠框
											</h6>

											<div class="widget-toolbar">
												<a href="#" data-action="settings">
													<i class="ace-icon fa fa-cog"></i>
												</a>

												<a href="#" data-action="reload">
													<i class="ace-icon fa fa-refresh"></i>
												</a>

												<a href="#" data-action="collapse">
													<i class="ace-icon fa fa-plus" data-icon-show="fa-plus" data-icon-hide="fa-minus"></i>
												</a>

												<a href="#" data-action="close">
													<i class="ace-icon fa fa-times"></i>
												</a>
											</div>
										</div>

										<!-- /sectionController:custom/widget-box.options.collapsed -->
										<div class="widget-body">
											<div class="widget-main">
												<p class="alert alert-info">
													Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque commodo massa sed ipsum porttitor facilisis.
												</p>
											</div>
										</div>
									</div>
								</div>

								<div class="col-xs-12 col-sm-6 widget-container-col">
									<div class="widget-box">
										<!-- #sectionController:custom/widget-box.header.options -->
										<div class="widget-header widget-header-large">
											<h4 class="widget-title">大标题</h4>

											<div class="widget-toolbar">
												<a href="#" data-action="settings">
													<i class="ace-icon fa fa-cog"></i>
												</a>

												<a href="#" data-action="reload">
													<i class="ace-icon fa fa-refresh"></i>
												</a>

												<a href="#" data-action="collapse">
													<i class="ace-icon fa fa-chevron-up"></i>
												</a>

												<a href="#" data-action="close">
													<i class="ace-icon fa fa-times"></i>
												</a>
											</div>
										</div>

										<!-- /sectionController:custom/widget-box.header.options -->
										<div class="widget-body">
											<div class="widget-main">
												<p class="alert alert-info">
													Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque commodo massa sed ipsum porttitor facilisis.
												</p>
											</div>
										</div>
									</div>
								</div>
							</div>

							<div class="space-24"></div>

							<div class="row">
								<div class="col-xs-12 col-sm-3 widget-container-col">
									<div class="widget-box">
										<div class="widget-header">
											<h5 class="widget-title smaller">带标签</h5>

											<div class="widget-toolbar">
												<span class="label label-success">
													16%
													<i class="ace-icon fa fa-arrow-up"></i>
												</span>
											</div>
										</div>

										<div class="widget-body">
											<div class="widget-main padding-6">
												<div class="alert alert-info"> Hello World! </div>
											</div>
										</div>
									</div>
								</div>

								<div class="col-xs-12 col-sm-3 widget-container-col">
									<div class="widget-box widget-color-dark light-border">
										<div class="widget-header">
											<h5 class="widget-title smaller">带徽章</h5>

											<div class="widget-toolbar">
												<span class="badge badge-danger">Alert</span>
											</div>
										</div>

										<div class="widget-body">
											<div class="widget-main padding-6">
												<div class="alert alert-info"> Hello World! </div>
											</div>
										</div>
									</div>
								</div>

								<div class="col-xs-12 col-sm-6 widget-container-col">
									<div class="widget-box widget-color-dark">
										<div class="widget-header widget-header-small">
											<h6 class="widget-title smaller">带标签和徽章</h6>

											<div class="widget-toolbar no-border">
												<label>
													<input type="checkbox" class="ace ace-switch ace-switch-3" />
													<span class="lbl middle"></span>
												</label>
											</div>

											<div class="widget-toolbar">
												<span class="label label-warning">
													1.2%
													<i class="ace-icon fa fa-arrow-down"></i>
												</span>
												<span class="badge badge-info">info</span>
											</div>
										</div>

										<div class="widget-body">
											<div class="widget-main">
												<div class="alert alert-info">
													Lorem ipsum dolor sit amet, consectetur adipiscing.
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>

							<div class="space"></div>

							<div class="row">
								<div class="col-xs-12 col-sm-6 widget-container-col">
									<div class="widget-box widget-color-dark">
										<div class="widget-header">
											<h5 class="widget-title bigger lighter">工具框</h5>

											<div class="widget-toolbar">
												<div class="progress progress-mini progress-striped active" style="width:100px;" data-percent="61%">
													<div class="progress-bar progress-bar-danger" style="width:61%"></div>
												</div>
											</div>
										</div>

										<div class="widget-body">
											<!-- #sectionController:custom/widget-box.toolbox -->
											<div class="widget-toolbox">
												<div class="btn-toolbar">
													<div class="btn-group">
														<button class="btn btn-sm btn-success btn-white btn-round">
															<i class="ace-icon fa fa-check bigger-110 green"></i>
															Approve
														</button>

														<button class="btn btn-sm btn-danger btn-white btn-round">
															<i class="ace-icon fa fa-times bigger-110 red2"></i>
															Reject
														</button>
													</div>

													<div data-toggle="buttons" class="btn-group">
														<label class="btn btn-sm btn-default btn-white btn-round">
															<i class="icon-only ace-icon fa fa-bold bigger-110"></i>

															<input type="checkbox" value="1" />
														</label>

														<label class="btn btn-sm btn-default btn-white btn-round">
															<i class="icon-only ace-icon fa fa-italic bigger-110"></i>

															<input type="checkbox" value="2" />
														</label>
													</div>

													<div data-toggle="buttons" class="btn-group">
														<label class="btn btn-sm btn-primary btn-white btn-round">
															<i class="icon-only ace-icon fa fa-align-left bigger-110"></i>

															<input type="radio" value="1" />
														</label>

														<label class="btn btn-sm btn-primary btn-white btn-round">
															<i class="icon-only ace-icon fa fa-align-center bigger-110"></i>

															<input type="radio" value="2" />
														</label>

														<label class="btn btn-sm btn-primary btn-white btn-round">
															<i class="icon-only ace-icon fa fa-align-right bigger-110"></i>

															<input type="radio" value="3" />
														</label>
													</div>
												</div>
											</div>

											<!-- /sectionController:custom/widget-box.toolbox -->
											<div class="widget-main padding-16">
												Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque commodo massa sed ipsum porttitor facilisis. Nullam interdum massa vel nisl fringilla sed viverra erat tincidunt. Phasellus in ipsum velit. Maecenas id erat vel sem convallis blandit. Nunc aliquam enim ut arcu aliquet adipiscing. Fusce dignissim volutpat justo non consectetur. Nulla fringilla eleifend consectetur. Etiam justo nisl, gravida id egestas eu, eleifend vel metus. Pellentesque tellus ipsum, euismod in facilisis quis, aliquet quis sem.
											</div>
										</div>
									</div>
								</div>

								<div class="col-xs-12 col-sm-6 widget-container-col">
									<div class="widget-box widget-color-pink">
										<div class="widget-header">
											<h5 class="widget-title">底层工具箱 (页脚)</h5>

											<div class="widget-toolbar">
												<a href="#" data-action="collapse">
													<i class="1 ace-icon fa fa-chevron-up bigger-125"></i>
												</a>
											</div>

											<div class="widget-toolbar no-border">
												<button class="btn btn-xs btn-light bigger">
													<i class="ace-icon fa fa-arrow-left"></i>
													Prev
												</button>

												<button class="btn btn-xs bigger btn-yellow dropdown-toggle" data-toggle="dropdown">
													Next
													<i class="ace-icon fa fa-chevron-down icon-on-right"></i>
												</button>

												<ul class="dropdown-menu dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">
													<li>
														<a href="#">Action</a>
													</li>

													<li>
														<a href="#">Another action</a>
													</li>

													<li>
														<a href="#">Something else here</a>
													</li>

													<li class="divider"></li>

													<li>
														<a href="#">Separated link</a>
													</li>
												</ul>
											</div>
										</div>

										<div class="widget-body">
											<div class="widget-main">
												<p class="alert alert-info">
													Nunc aliquam enim ut arcu aliquet adipiscing. Fusce dignissim volutpat justo non consectetur. Nulla fringilla eleifend consectetur.
												</p>
												<p class="alert alert-success">
													Raw denim you probably haven't heard of them jean shorts Austin.
												</p>
											</div>

											<div class="widget-toolbox padding-8 clearfix">
												<button class="btn btn-xs btn-danger pull-left">
													<i class="ace-icon fa fa-times"></i>
													<span class="bigger-110">I don't accept</span>
												</button>

												<button class="btn btn-xs btn-success pull-right">
													<span class="bigger-110">I accept</span>

													<i class="ace-icon fa fa-arrow-right icon-on-right"></i>
												</button>
											</div>
										</div>
									</div>
								</div>
							</div>

							<div class="space"></div>

							<div class="row">
								<div class="col-sm-6 widget-container-col">
									<div class="widget-box">
										<div class="widget-header widget-header-small">
											<h5 class="widget-title smaller">标签框</h5>

											<!-- #sectionController:custom/widget-box.tabbed -->
											<div class="widget-toolbar no-border">
												<ul class="nav nav-tabs" id="myTab">
													<li class="active">
														<a data-toggle="tab" href="#home">Home</a>
													</li>

													<li>
														<a data-toggle="tab" href="#profile">Profile</a>
													</li>

													<li>
														<a data-toggle="tab" href="#info">Info</a>
													</li>
												</ul>
											</div>

											<!-- /sectionController:custom/widget-box.tabbed -->
										</div>

										<div class="widget-body">
											<div class="widget-main padding-6">
												<div class="tab-content">
													<div id="home" class="tab-pane in active">
														<p>Raw denim you probably haven't heard of them jean shorts Austin.</p>
													</div>

													<div id="profile" class="tab-pane">
														<p>Food truck fixie locavore, accusamus mcsweeney's marfa nulla single-origin coffee squid.</p>
													</div>

													<div id="info" class="tab-pane">
														<p>Etsy mixtape wayfarers, ethical wes anderson tofu before they sold out mcsweeney's organic lomo retro fanny pack lo-fi farm-to-table readymade.</p>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>

								<div class="col-sm-6 widget-container-col">
									<div class="widget-box widget-color-dark">
										<div class="widget-header widget-hea1der-small">
											<h6 class="widget-title">滚动内容</h6>

											<div class="widget-toolbar">Scroll
												<a href="#" data-action="settings">
													<i class="ace-icon fa fa-cog"></i>
												</a>

												<a href="#" data-action="reload">
													<i class="ace-icon fa fa-refresh"></i>
												</a>

												<a href="#" data-action="collapse">
													<i class="ace-icon fa fa-chevron-up"></i>
												</a>

												<a href="#" data-action="close">
													<i class="ace-icon fa fa-times"></i>
												</a>
											</div>
										</div>

										<div class="widget-body">
											<div class="widget-main padding-4">
												<!-- #sectionController:custom/scrollbar -->
												<div class="scrollable" data-height="125">
													<div class="content">
														<div class="alert alert-info">
															Lorem ipsum dolor sit amet, consectetur adipiscing.
														</div>
														<div class="alert alert-danger">
															Lorem ipsum dolor sit amet, consectetur adipiscing.
														</div>
														<div class="alert alert-success">
															Lorem ipsum dolor sit amet, consectetur adipiscing.
														</div>
														<div class="alert">
															Lorem ipsum dolor sit amet, consectetur adipiscing.
														</div>
													</div>
												</div>

												<!-- /sectionController:custom/scrollbar -->
											</div>
										</div>
									</div>
								</div>
							</div>

							<div class="space-24"></div>

							<div class="row">
								<div class="col-sm-6 widget-container-col">
									<!-- #sectionController:custom/widget-box.options.transparent -->
									<div class="widget-box transparent">
										<div class="widget-header">
											<h4 class="widget-title lighter">透明框</h4>

											<div class="widget-toolbar no-border">
												<a href="#" data-action="settings">
													<i class="ace-icon fa fa-cog"></i>
												</a>

												<a href="#" data-action="reload">
													<i class="ace-icon fa fa-refresh"></i>
												</a>

												<a href="#" data-action="collapse">
													<i class="ace-icon fa fa-chevron-up"></i>
												</a>

												<a href="#" data-action="close">
													<i class="ace-icon fa fa-times"></i>
												</a>
											</div>
										</div>

										<div class="widget-body">
											<div class="widget-main padding-6 no-padding-left no-padding-right">
												Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque commodo massa sed ipsum porttitor facilisis. Nullam interdum massa vel nisl fringilla sed viverra erat tincidunt. Phasellus in ipsum velit. Maecenas id erat vel sem convallis blandit. Nunc aliquam enim ut arcu aliquet adipiscing. Fusce dignissim volutpat justo non consectetur.
											</div>
										</div>
									</div>

									<!-- /sectionController:custom/widget-box.options.transparent -->
								</div>

								<div class="col-sm-6 widget-container-col">
									<div class="widget-box transparent">
										<div class="widget-header">
											<h4 class="widget-title lighter">可滚动的标签页</h4>

											<div class="widget-toolbar no-border">
												<ul class="nav nav-tabs" id="myTab2">
													<li class="active">
														<a data-toggle="tab" href="#home2">Home</a>
													</li>

													<li>
														<a data-toggle="tab" href="#profile2">Profile</a>
													</li>

													<li>
														<a data-toggle="tab" href="#info2">Info</a>
													</li>
												</ul>
											</div>
										</div>

										<div class="widget-body">
											<div class="widget-main padding-12 no-padding-left no-padding-right">
												<div class="tab-content padding-4">
													<div id="home2" class="tab-pane in active">
														<!-- #sectionController:custom/scrollbar.horizontal -->
														<div class="scrollable-horizontal" data-width="800">
															<b>Horizontal Scroll</b>
															Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque commodo massa sed ipsum porttitor facilisis. Nullam interdum massa vel nisl fringilla sed viverra erat tincidunt. Phasellus in ipsum velit. Maecenas id erat vel sem convallis blandit. Nunc aliquam enim ut arcu aliquet adipiscing. Fusce dignissim volutpat justo non consectetur.
						Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque commodo massa sed ipsum porttitor facilisis. Nullam interdum massa vel nisl fringilla sed viverra erat tincidunt. Phasellus in ipsum velit. Maecenas id erat vel sem convallis blandit. Nunc aliquam enim ut arcu aliquet adipiscing. Fusce dignissim volutpat justo non consectetur.
						Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque commodo massa sed ipsum porttitor facilisis. Nullam interdum massa vel nisl fringilla sed viverra erat tincidunt. Phasellus in ipsum velit. Maecenas id erat vel sem convallis blandit. Nunc aliquam enim ut arcu aliquet adipiscing. Fusce dignissim volutpat justo non consectetur.
														</div>

														<!-- /sectionController:custom/scrollbar.horizontal -->
													</div>

													<div id="profile2" class="tab-pane">
														<div class="scrollable" data-height="100" data-position="left">
															<b>Scroll on Left</b>
															Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque commodo massa sed ipsum porttitor facilisis. Nullam interdum massa vel nisl fringilla sed viverra erat tincidunt. Phasellus in ipsum velit. Maecenas id erat vel sem convallis blandit. Nunc aliquam enim ut arcu aliquet adipiscing. Fusce dignissim volutpat justo non consectetur.
						Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque commodo massa sed ipsum porttitor facilisis. Nullam interdum massa vel nisl fringilla sed viverra erat tincidunt. Phasellus in ipsum velit. Maecenas id erat vel sem convallis blandit. Nunc aliquam enim ut arcu aliquet adipiscing. Fusce dignissim volutpat justo non consectetur.
						Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque commodo massa sed ipsum porttitor facilisis. Nullam interdum massa vel nisl fringilla sed viverra erat tincidunt. Phasellus in ipsum velit. Maecenas id erat vel sem convallis blandit. Nunc aliquam enim ut arcu aliquet adipiscing. Fusce dignissim volutpat justo non consectetur.
														</div>
													</div>

													<div id="info2" class="tab-pane">
														<div class="scrollable" data-height="100">
															<b>Scroll # 3</b>
															Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque commodo massa sed ipsum porttitor facilisis. Nullam interdum massa vel nisl fringilla sed viverra erat tincidunt. Phasellus in ipsum velit. Maecenas id erat vel sem convallis blandit. Nunc aliquam enim ut arcu aliquet adipiscing. Fusce dignissim volutpat justo non consectetur.
						Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque commodo massa sed ipsum porttitor facilisis. Nullam interdum massa vel nisl fringilla sed viverra erat tincidunt. Phasellus in ipsum velit. Maecenas id erat vel sem convallis blandit. Nunc aliquam enim ut arcu aliquet adipiscing. Fusce dignissim volutpat justo non consectetur.
						Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque commodo massa sed ipsum porttitor facilisis. Nullam interdum massa vel nisl fringilla sed viverra erat tincidunt. Phasellus in ipsum velit. Maecenas id erat vel sem convallis blandit. Nunc aliquam enim ut arcu aliquet adipiscing. Fusce dignissim volutpat justo non consectetur.
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div><!-- PAGE CONTENT ENDS -->
						</div><!-- /.col -->
					</div><!-- /.row -->
				</div><!-- /.page-content -->
			

		

		<!-- inline scripts related to this page -->
		<script type="text/javascript">
			jQuery(function($) {
				var bodyCss=document.body.className;
				
				$('#simple-colorpicker-1').ace_colorpicker({pull_right:true}).on('change', function(){
					var color_class = $(this).find('option:selected').data('class');
					var new_class = 'widget-box';
					if(color_class != 'default')  new_class += ' widget-color-'+color_class;
					$(this).closest('.widget-box').attr('class', new_class);
				});
				
				var curColor= bodyCss.split("-");
				$(".selected").removeClass("selected");
				if(curColor[0]=="black")
					curColor[0]="dark";
				$("#simple-colorpicker-1").find("option[data-class='"+curColor[0]+"']").attr("selected","selected");
				$('#simple-colorpicker-1').change();
				
			    $(".colorpick-btn").each(function(){
					if($(this).data().color==$("#simple-colorpicker-1").find('option:selected').val()){
						$(this).addClass("selected");
					}
				});
			
			
				// scrollables
				$('.scrollable').each(function () {
					var $this = $(this);
					$(this).ace_scroll({
						size: $this.data('height') || 100,
						//styleClass: 'scroll-left scroll-margin scroll-thin scroll-dark scroll-light no-track scroll-visible'
					});
				});
				$('.scrollable-horizontal').each(function () {
					var $this = $(this);
					$(this).ace_scroll(
					  {
						horizontal: true,
						styleClass: 'scroll-top',//show the scrollbars on top(default is bottom)
						size: $this.data('width') || 500,
						mouseWheelLock: true
					  }
					).css({'padding-top': 12});
				});
				
				$(window).on('resize.scroll_reset', function() {
					$('.scrollable-horizontal').ace_scroll('reset');
				});
			
			
				
			    $('.widget-container-col').sortable({
			        connectWith: '.widget-container-col',
					items:'> .widget-box',
					opacity:0.8,
					revert:true,
					forceHelperSize:true,
					placeholder: 'widget-placeholder',
					forcePlaceholderSize:true,
					tolerance:'pointer',
					start: function(event, ui){
						//when an element is moved, it's parent becomes empty with almost zero height.
						//we set a min-height for it to be large enough so that later we can easily drop elements back onto it
						ui.item.parent().css({'min-height':ui.item.height()})
						//ui.sender.css({'min-height':ui.item.height() , 'background-color' : '#F5F5F5'})
					},
					update: function(event, ui) {
						ui.item.parent({'min-height':''})
						//p.style.removeProperty('background-color');
					}
			    });
			
			});
		</script>

		
	</body>
</html>
