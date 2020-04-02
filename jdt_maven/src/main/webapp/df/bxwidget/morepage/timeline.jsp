<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>时间轴</title>

</head>

<body>

	<%@ include file="/bxui/bxuihead.jsp"%>
    <script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
	<!-- /sectionController:basics/content.breadcrumbs -->
	<div class="page-content">
		<!-- /sectionController:settings.box -->
		<div class="page-header">
			<h1>
				时间轴 <small> <i class="ace-icon fa fa-angle-double-right"></i>
					基于2种不同风格的时间轴组件
				</small>
			</h1>
		</div>
		<!-- /.page-header -->

		<div class="row">
			<div class="col-xs-12">
				<!-- PAGE CONTENT BEGINS -->
				<div class="align-right">
					<span class="green middle bolder">选择时间轴类型: &nbsp;</span>

					<div class="btn-toolbar inline middle no-margin">
						<div data-toggle="buttons" class="btn-group no-margin">
							<label class="btn btn-sm btn-yellow active"> <span
								class="bigger-110">1</span> <input type="radio" value="1" />
							</label> <label class="btn btn-sm btn-yellow"> <span
								class="bigger-110">2</span> <input type="radio" value="2" />
							</label>
						</div>
					</div>
				</div>

				<div id="timeline-1">
					<div class="row">
						<div class="col-xs-12 col-sm-10 col-sm-offset-1">
							<!-- #sectionController:pages/timeline -->
							<div class="timeline-container">
								<div class="timeline-label">
									<!-- #sectionController:pages/timeline.label -->
									<span class="label label-primary arrowed-in-right label-lg">
										<b>今天</b>
									</span>

									<!-- /sectionController:pages/timeline.label -->
								</div>

								<div class="timeline-items">
									<!-- #sectionController:pages/timeline.item -->
									<div class="timeline-item clearfix">
										<!-- #sectionController:pages/timeline.info -->
										<div class="timeline-info">
											<img alt="Susan't Avatar"
												src="<%=toolkitPath%>/bxui/aceadmin/style/avatars/avatar1.png" />
											<span class="label label-info label-sm">16:22</span>
										</div>

										<!-- /sectionController:pages/timeline.info -->
										<div class="widget-box transparent">
											<div class="widget-header widget-header-small">
												<h5 class="widget-title smaller">
													<a href="#" class="blue">苏珊</a> <span class="grey">购物</span>
												</h5>

												<span class="widget-toolbar no-border"> <i
													class="ace-icon fa fa-clock-o bigger-110"></i> 16:22
												</span> <span class="widget-toolbar"> <a href="#"
													data-action="reload"> <i class="ace-icon fa fa-refresh"></i>
												</a> <a href="#" data-action="collapse"> <i
														class="ace-icon fa fa-chevron-up"></i>
												</a>
												</span>
											</div>

											<div class="widget-body">
												<div class="widget-main">
													去沃尔玛购物
													<div class="space-6"></div>

													<div class="widget-toolbox clearfix">
														<div class="pull-left">
															<i class="ace-icon fa fa-hand-o-right grey bigger-125"></i>
															<a href="#" class="bigger-110">点击阅读 &hellip;</a>
														</div>

														<!-- #sectionController:custom/extra.action-buttons -->
														<div class="pull-right action-buttons">
															<a href="#"> <i
																class="ace-icon fa fa-check green bigger-130"></i>
															</a> <a href="#"> <i
																class="ace-icon fa fa-pencil blue bigger-125"></i>
															</a> <a href="#"> <i
																class="ace-icon fa fa-times red bigger-125"></i>
															</a>
														</div>

														<!-- /sectionController:custom/extra.action-buttons -->
													</div>
												</div>
											</div>
										</div>
									</div>

									<!-- /sectionController:pages/timeline.item -->
									<div class="timeline-item clearfix">
										<div class="timeline-info">
											<i
												class="timeline-indicator ace-icon fa fa-cutlery btn btn-success no-hover"></i>
										</div>

										<div class="widget-box transparent">
											<div class="widget-body">
												<div class="widget-main">
													午餐后去咖啡馆
													<div class="pull-right">
														<i class="ace-icon fa fa-clock-o bigger-110"></i> 12:30
													</div>
												</div>
											</div>
										</div>
									</div>

									<div class="timeline-item clearfix">
										<div class="timeline-info">
											<i
												class="timeline-indicator ace-icon fa fa-star btn btn-warning no-hover green"></i>
										</div>

										<div class="widget-box transparent">
											<div class="widget-header widget-header-small">
												<h5 class="widget-title smaller">设计一个新的logo</h5>

												<span class="widget-toolbar no-border"> <i
													class="ace-icon fa fa-clock-o bigger-110"></i> 9:15
												</span> <span class="widget-toolbar"> <a href="#"
													data-action="reload"> <i class="ace-icon fa fa-refresh"></i>
												</a> <a href="#" data-action="collapse"> <i
														class="ace-icon fa fa-chevron-up"></i>
												</a>
												</span>
											</div>

											<div class="widget-body">
												<div class="widget-main">
													为网站设计一个新的logo，欢迎各位提出意见
													<div class="space-6"></div>

													<div class="widget-toolbox clearfix">
														<div class="pull-right action-buttons">
															<div class="space-4"></div>

															<div>
																<a href="#"> <i
																	class="ace-icon fa fa-heart red bigger-125"></i>
																</a> <a href="#"> <i
																	class="ace-icon fa fa-facebook blue bigger-125"></i>
																</a> <a href="#"> <i
																	class="ace-icon fa fa-reply light-green bigger-130"></i>
																</a>
															</div>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>

									<div class="timeline-item clearfix">
										<div class="timeline-info">
											<i
												class="timeline-indicator ace-icon fa fa-flask btn btn-default no-hover"></i>
										</div>

										<div class="widget-box transparent">
											<div class="widget-body">
												<div class="widget-main">准备期末考试！</div>
											</div>
										</div>
									</div>
								</div>
								<!-- /.timeline-items -->
							</div>
							<!-- /.timeline-container -->

							<div class="timeline-container">
								<div class="timeline-label">
									<span class="label label-success arrowed-in-right label-lg">
										<b>昨天</b>
									</span>
								</div>

								<div class="timeline-items">
									<div class="timeline-item clearfix">
										<div class="timeline-info">
											<i
												class="timeline-indicator ace-icon fa fa-beer btn btn-inverse no-hover"></i>
										</div>

										<div class="widget-box transparent">
											<div class="widget-header widget-header-small">
												<h5 class="widget-title smaller">万圣节派对</h5>

												<span class="widget-toolbar"> <i
													class="ace-icon fa fa-clock-o bigger-110"></i> 1小时之前
												</span>
											</div>

											<div class="widget-body">
												<div class="widget-main">
													<div class="clearfix">
														<div class="pull-left">
															派对上有很多好玩的事情 <br /> 查看右边的照片:
														</div>

														<div class="pull-right">
															<i class="ace-icon fa fa-chevron-left blue bigger-110"></i>

															&nbsp; <img alt="Image 4" width="36"
																src="<%=toolkitPath%>/bxui/aceadmin/style/images/gallery/thumb-4.jpg" />
															<img alt="Image 3" width="36"
																src="<%=toolkitPath%>/bxui/aceadmin/style/images/gallery/thumb-3.jpg" />
															<img alt="Image 2" width="36"
																src="<%=toolkitPath%>/bxui/aceadmin/style/images/gallery/thumb-2.jpg" />
															<img alt="Image 1" width="36"
																src="<%=toolkitPath%>/bxui/aceadmin/style/images/gallery/thumb-1.jpg" />
															&nbsp; <i
																class="ace-icon fa fa-chevron-right blue bigger-110"></i>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>

									<div class="timeline-item clearfix">
										<div class="timeline-info">
											<i
												class="timeline-indicator ace-icon fa fa-cutlery btn btn-success no-hover"></i>
										</div>

										<div class="widget-box transparent">
											<div class="widget-body">
												<div class="widget-main">午餐后去咖啡馆看书</div>
											</div>
										</div>
									</div>

									<div class="timeline-item clearfix">
										<div class="timeline-info">
											<i
												class="timeline-indicator ace-icon fa fa-bug btn btn-danger no-hover"></i>
										</div>

										<div class="widget-box widget-color-red2">
											<div class="widget-header widget-header-small">
												<h5 class="widget-title smaller">重要安全补丁发布</h5>

												<span class="widget-toolbar no-border"> <i
													class="ace-icon fa fa-clock-o bigger-110"></i> 9:15
												</span> <span class="widget-toolbar"> <a href="#"
													data-action="reload"> <i class="ace-icon fa fa-refresh"></i>
												</a> <a href="#" data-action="collapse"> <i
														class="ace-icon fa fa-chevron-up"></i>
												</a>
												</span>
											</div>

											<div class="widget-body">
												<div class="widget-main">请下载最新的安全补丁进行安装</div>
											</div>
										</div>
									</div>
								</div>
								<!-- /.timeline-items -->
							</div>
							<!-- /.timeline-container -->

							<!-- /sectionController:pages/timeline -->
						</div>
					</div>
				</div>

				<div id="timeline-2" class="hide">
					<div class="row">
						<div class="col-xs-12 col-sm-10 col-sm-offset-1">
							<!-- #sectionController:pages/timeline.style2 -->
							<div class="timeline-container timeline-style2">
								<span class="timeline-label"> <b>今天</b>
								</span>

								<div class="timeline-items">
									<div class="timeline-item clearfix">
										<div class="timeline-info">
											<span class="timeline-date">11:15 am</span> <i
												class="timeline-indicator btn btn-info no-hover"></i>
										</div>

										<div class="widget-box transparent">
											<div class="widget-body">
												<div class="widget-main no-padding">
													<span class="bigger-110"> <a href="#"
														class="purple bolder">苏珊</a> 去沃尔玛购物
													</span> <br /> <i
														class="ace-icon fa fa-hand-o-right grey bigger-125"></i> <a
														href="#">点击阅读 &hellip;</a>
												</div>
											</div>
										</div>
									</div>

									<div class="timeline-item clearfix">
										<div class="timeline-info">
											<span class="timeline-date">12:30 am</span> <i
												class="timeline-indicator btn btn-info no-hover"></i>
										</div>

										<div class="widget-box transparent">
											<div class="widget-body">
												<div class="widget-main no-padding">午餐后去咖啡馆</div>
											</div>
										</div>
									</div>

									<div class="timeline-item clearfix">
										<div class="timeline-info">
											<span class="timeline-date">11:15 am</span> <i
												class="timeline-indicator btn btn-info no-hover"></i>
										</div>

										<div class="widget-box transparent">
											<div class="widget-body">
												<div class="widget-main no-padding">
													为网站设计一个新的logo，欢迎各位提出意见 <a href="#"> Click to see <i
														class="ace-icon fa fa-search-plus blue bigger-110"></i>
													</a>

													<div class="space-2"></div>

													<div class="action-buttons">
														<a href="#"> <i
															class="ace-icon fa fa-heart red bigger-125"></i>
														</a> <a href="#"> <i
															class="ace-icon fa fa-facebook blue bigger-125"></i>
														</a> <a href="#"> <i
															class="ace-icon fa fa-reply light-green bigger-130"></i>
														</a>
													</div>
												</div>
											</div>
										</div>
									</div>

									<div class="timeline-item clearfix">
										<div class="timeline-info">
											<span class="timeline-date">9:00 am</span> <i
												class="timeline-indicator btn btn-info no-hover"></i>
										</div>

										<div class="widget-box transparent">
											<div class="widget-body">
												<div class="widget-main no-padding">准备期末考试!</div>
											</div>
										</div>
									</div>
								</div>
								<!-- /.timeline-items -->
							</div>
							<!-- /.timeline-container -->

							<!-- /sectionController:pages/timeline.style2 -->
							<div class="timeline-container timeline-style2">
								<span class="timeline-label"> <b>昨天</b>
								</span>

								<div class="timeline-items">
									<div class="timeline-item clearfix">
										<div class="timeline-info">
											<span class="timeline-date">9:00 am</span> <i
												class="timeline-indicator btn btn-success no-hover"></i>
										</div>

										<div class="widget-box transparent">
											<div class="widget-body">
												<div class="widget-main no-padding">
													<div class="clearfix">
														<div class="pull-left">
															<span class="orange2 bolder">万圣节派对</span> 派对上有很多好玩的事情 <br />
															查看右边的照片:
														</div>

														<div class="pull-right">
															<i class="ace-icon fa fa-chevron-left blue bigger-110"></i>

															&nbsp; <img alt="Image 4" width="36"
																src="<%=toolkitPath%>/bxui/aceadmin/style/images/gallery/thumb-4.jpg" />
															<img alt="Image 3" width="36"
																src="<%=toolkitPath%>/bxui/aceadmin/style/images/gallery/thumb-3.jpg" />
															<img alt="Image 2" width="36"
																src="<%=toolkitPath%>/bxui/aceadmin/style/images/gallery/thumb-2.jpg" />
															<img alt="Image 1" width="36"
																src="<%=toolkitPath%>/bxui/aceadmin/style/images/gallery/thumb-1.jpg" />
															&nbsp; <i
																class="ace-icon fa fa-chevron-right blue bigger-110"></i>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>



									<div class="timeline-item clearfix">
										<div class="timeline-info">
											<span class="timeline-date">9:00 am</span> <i
												class="timeline-indicator btn btn-success no-hover"></i>
										</div>

										<div class="widget-box transparent">
											<div class="widget-body">
												<div class="widget-main no-padding">午餐后去咖啡馆看书</div>
											</div>
										</div>
									</div>

									<div class="timeline-item clearfix">
										<div class="timeline-info">
											<span class="timeline-date">9:00 am</span> <i
												class="timeline-indicator btn btn-success no-hover"></i>
										</div>

										<div class="widget-box transparent">
											<div class="widget-body">
												<div class="widget-main no-padding">
													<span class="red bolder">重要安全补丁发布</span> <br />请下载最新的安全补丁进行安装
												</div>
											</div>
										</div>
									</div>
								</div>
								<!-- /.timeline-items -->
							</div>
							<!-- /.timeline-container -->
						</div>
					</div>
				</div>

				<!-- PAGE CONTENT ENDS -->
			</div>
			<!-- /.col -->
		</div>
		<!-- /.row -->
	</div>
	<!-- /.page-content -->

	<!-- 点击1,2标签时2个类型区域的切换 -->
	<script type="text/javascript">
		jQuery(function($) {
			$('[data-toggle="buttons"] .btn').on('click', function(e) {
				var target = $(this).find('input[type=radio]');
				var which = parseInt(target.val());
				$('[id*="timeline-"]').addClass('hide');
				$('#timeline-' + which).removeClass('hide');
			});
		});
	</script>
</body>
</html>
