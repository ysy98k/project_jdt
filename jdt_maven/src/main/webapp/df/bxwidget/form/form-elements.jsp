<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>表单元素</title>

</head>

<body>
	<%@ include file="/bxui/bxuihead.jsp"%>
	<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
	<script type="text/javascript"
		src="<%=toolkitPath%>/bxui/baosight-form.js"></script>
   

	<!-- /sectionController:basics/content.breadcrumbs -->
	<div class="page-content">
		<!-- /sectionController:settings.box -->
		<div class="page-header">
			<h1>
				表单元素 <small> <i class="ace-icon fa fa-angle-double-right"></i>
					常用的表单元素和布局
				</small>
			</h1>
		</div>
		<!-- /.page-header -->

		<div class="row">
			<div class="col-xs-12">
				<!-- PAGE CONTENT BEGINS -->
				<form class="form-horizontal" role="form">
					<!-- #sectionController:elements.form -->
					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right"
							for="form-field-1"> 文本框 </label>

						<div class="col-sm-9">
							<input type="text" id="form-field-1" placeholder="用户名"
								class="col-xs-10 col-sm-5" />
						</div>
					</div>

					<!-- /sectionController:elements.form -->
					<div class="space-4"></div>

					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right"
							for="form-field-2"> 密码框 </label>

						<div class="col-sm-9">
							<input type="password" id="form-field-2" placeholder="密码"
								class="col-xs-10 col-sm-5" /> <span
								class="help-inline col-xs-12 col-sm-7"> <span
								class="middle">提示信息</span>
							</span>
						</div>
					</div>

					<div class="space-4"></div>

					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right"
							for="form-input-readonly"> 只读文本框 </label>

						<div class="col-sm-9">
							<input readonly="" type="text" class="col-xs-10 col-sm-5"
								id="form-input-readonly" value="这里的文本是只读的！" /> <span
								class="help-inline col-xs-12 col-sm-7"> <label
								class="middle"> <input class="ace" type="checkbox"
									id="id-disable-check" /> <span class="lbl"> 设置为Disable！</span>
							</label>
							</span>
						</div>
					</div>

					<div class="space-4"></div>

					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right"
							for="form-field-4">可变宽度</label>

						<div class="col-sm-9">
							<input class="input-sm" type="text" id="form-field-4"
								placeholder=".input-sm" />
							<div class="space-2"></div>

							<div class="help-block" id="input-size-slider"></div>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right"
							for="form-field-5">Grid宽度</label>

						<div class="col-sm-9">
							<div class="clearfix">
								<input class="col-xs-1" type="text" id="form-field-5"
									placeholder=".col-xs-1" />
							</div>

							<div class="space-2"></div>

							<div class="help-block" id="input-span-slider"></div>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right">带图片的文本框</label>

						<div class="col-sm-9">
							<!-- #sectionController:elements.form.input-icon -->
							<span class="input-icon"> <input type="text"
								id="form-field-icon-1" /> <i class="ace-icon fa fa-leaf blue"></i>
							</span> <span class="input-icon input-icon-right"> <input
								type="text" id="form-field-icon-2" /> <i
								class="ace-icon fa fa-leaf green"></i>
							</span>

							<!-- /sectionController:elements.form.input-icon -->
						</div>
					</div>

					<div class="space-4"></div>

					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right"
							for="form-field-6">提示信息和帮助按钮的文本框</label>

						<div class="col-sm-9">
							<input data-rel="tooltip" type="text" id="form-field-6"
								placeholder="鼠标hover会显示" title="提示信息！" data-placement="bottom" />
							<span class="help-button" data-rel="popover" data-trigger="hover"
								data-placement="left" data-content="明细" title="鼠标hover会弹出">?</span>
						</div>
					</div>

					<div class="space-4"></div>

					<div class="form-group">
						<label class="col-sm-3 control-label no-padding-right"
							for="form-field-tags">Tag input</label>

						<div class="col-sm-9">
							<!-- #sectionController:plugins/input.tag-input -->
							<div class="inline">
								<input type="text" name="tags" id="form-field-tags"
									value="Tag Input Control" placeholder="Enter tags ..." />
							</div>

							<!-- /sectionController:plugins/input.tag-input -->
						</div>
					</div>

					<div class="clearfix form-actions">
						<div class="col-md-offset-3 col-md-9">
							<button class="btn btn-info" type="button">
								<i class="ace-icon fa fa-check bigger-110"></i> 提交
							</button>

							&nbsp; &nbsp; &nbsp;
							<button class="btn" type="reset">
								<i class="ace-icon fa fa-undo bigger-110"></i> 重置
							</button>
						</div>
					</div>

					<div class="hr hr-24"></div>

					<div class="row">
						<div class="col-xs-12 col-sm-4">
							<div class="widget-box">
								<div class="widget-header">
									<h4 class="widget-title">文本区域</h4>

									<div class="widget-toolbar">
										<a href="#" data-action="collapse"> <i
											class="ace-icon fa fa-chevron-up"></i>
										</a> <a href="#" data-action="close"> <i
											class="ace-icon fa fa-times"></i>
										</a>
									</div>
								</div>

								<div class="widget-body">
									<div class="widget-main">
										<div>
											<label for="form-field-8">默认</label>

											<textarea class="form-control" id="form-field-8"
												placeholder="默认的文本"></textarea>
										</div>

										<hr />

										<!-- #sectionController:plugins/input.limiter -->
										<div>
											<label for="form-field-9">字符限制的文本区域（50个字符）</label>

											<textarea class="form-control limited" id="form-field-9"
												maxlength="50"></textarea>
										</div>

										<!-- /sectionController:plugins/input.limiter -->
										<hr />

										<!-- #sectionController:plugins/input.autosize -->
										<div>
											<label for="form-field-11">尺寸自动的文本区域</label>

											<textarea id="form-field-11"
												class="autosize-transition form-control"></textarea>
										</div>

										<!-- /sectionController:plugins/input.autosize -->
									</div>
								</div>
							</div>
						</div>
						<!-- /.span -->

						<div class="col-xs-12 col-sm-4">
							<div class="widget-box">
								<div class="widget-header">
									<h4 class="widget-title">有格式的文本框</h4>

									<span class="widget-toolbar"> <a href="#"
										data-action="settings"> <i class="ace-icon fa fa-cog"></i>
									</a> <a href="#" data-action="reload"> <i
											class="ace-icon fa fa-refresh"></i>
									</a> <a href="#" data-action="collapse"> <i
											class="ace-icon fa fa-chevron-up"></i>
									</a> <a href="#" data-action="close"> <i
											class="ace-icon fa fa-times"></i>
									</a>
									</span>
								</div>

								<div class="widget-body">
									<div class="widget-main">
										<div>
											<label for="form-field-mask-1"> 日期 <small
												class="text-success">99/99/9999</small>
											</label>

											<!-- #sectionController:plugins/input.masked-input -->
											<div class="input-group">
												<input class="form-control input-mask-date" type="text"
													id="form-field-mask-1" /> <span class="input-group-btn">
													<button class="btn btn-sm btn-default" type="button">
														<i class="ace-icon fa fa-calendar bigger-110"></i> Go!
													</button>
												</span>
											</div>

											<!-- /sectionController:plugins/input.masked-input -->
										</div>

										<hr />
										<div>
											<label for="form-field-mask-2"> 电话 <small
												class="text-warning">(999) 999-9999</small>
											</label>

											<div class="input-group">
												<span class="input-group-addon"> <i
													class="ace-icon fa fa-phone"></i>
												</span> <input class="form-control input-mask-phone" type="text"
													id="form-field-mask-2" />
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<!-- /.span -->

						<div class="col-xs-12 col-sm-4">
							<div class="widget-box">
								<div class="widget-header">
									<h4 class="widget-title">下拉框</h4>

									<span class="widget-toolbar"> <a href="#"
										data-action="settings"> <i class="ace-icon fa fa-cog"></i>
									</a> <a href="#" data-action="reload"> <i
											class="ace-icon fa fa-refresh"></i>
									</a> <a href="#" data-action="collapse"> <i
											class="ace-icon fa fa-chevron-up"></i>
									</a> <a href="#" data-action="close"> <i
											class="ace-icon fa fa-times"></i>
									</a>
									</span>
								</div>

								<div class="widget-body">
									<div class="widget-main">
										<div>
											<label for="form-field-select-1">默认</label> <select
												class="form-control" id="form-field-select-1">
												<option value=""></option>
												<option value="AL">Alabama</option>
												<option value="AK">Alaska</option>
												<option value="AZ">Arizona</option>
												<option value="AR">Arkansas</option>
												<option value="CA">California</option>
												<option value="CO">Colorado</option>
												<option value="CT">Connecticut</option>
												<option value="DE">Delaware</option>
												<option value="FL">Florida</option>
												<option value="GA">Georgia</option>
												<option value="HI">Hawaii</option>
												<option value="ID">Idaho</option>
												<option value="IL">Illinois</option>
												<option value="IN">Indiana</option>
												<option value="IA">Iowa</option>
												<option value="KS">Kansas</option>
												<option value="KY">Kentucky</option>
												<option value="LA">Louisiana</option>
												<option value="ME">Maine</option>
												<option value="MD">Maryland</option>
												<option value="MA">Massachusetts</option>
												<option value="MI">Michigan</option>
												<option value="MN">Minnesota</option>
												<option value="MS">Mississippi</option>
												<option value="MO">Missouri</option>
												<option value="MT">Montana</option>
												<option value="NE">Nebraska</option>
												<option value="NV">Nevada</option>
												<option value="NH">New Hampshire</option>
												<option value="NJ">New Jersey</option>
												<option value="NM">New Mexico</option>
												<option value="NY">New York</option>
												<option value="NC">North Carolina</option>
												<option value="ND">North Dakota</option>
												<option value="OH">Ohio</option>
												<option value="OK">Oklahoma</option>
												<option value="OR">Oregon</option>
												<option value="PA">Pennsylvania</option>
												<option value="RI">Rhode Island</option>
												<option value="SC">South Carolina</option>
												<option value="SD">South Dakota</option>
												<option value="TN">Tennessee</option>
												<option value="TX">Texas</option>
												<option value="UT">Utah</option>
												<option value="VT">Vermont</option>
												<option value="VA">Virginia</option>
												<option value="WA">Washington</option>
												<option value="WV">West Virginia</option>
												<option value="WI">Wisconsin</option>
												<option value="WY">Wyoming</option>
											</select>
										</div>

										<hr />
										<div>
											<label for="form-field-select-2">多选</label> <select
												class="form-control" id="form-field-select-2"
												multiple="multiple">
												<option value="AL">Alabama</option>
												<option value="AK">Alaska</option>
												<option value="AZ">Arizona</option>
												<option value="AR">Arkansas</option>
												<option value="CA">California</option>
												<option value="CO">Colorado</option>
												<option value="CT">Connecticut</option>
												<option value="DE">Delaware</option>
												<option value="FL">Florida</option>
												<option value="GA">Georgia</option>
												<option value="HI">Hawaii</option>
												<option value="ID">Idaho</option>
												<option value="IL">Illinois</option>
												<option value="IN">Indiana</option>
												<option value="IA">Iowa</option>
												<option value="KS">Kansas</option>
												<option value="KY">Kentucky</option>
												<option value="LA">Louisiana</option>
												<option value="ME">Maine</option>
												<option value="MD">Maryland</option>
												<option value="MA">Massachusetts</option>
												<option value="MI">Michigan</option>
												<option value="MN">Minnesota</option>
												<option value="MS">Mississippi</option>
												<option value="MO">Missouri</option>
												<option value="MT">Montana</option>
												<option value="NE">Nebraska</option>
												<option value="NV">Nevada</option>
												<option value="NH">New Hampshire</option>
												<option value="NJ">New Jersey</option>
												<option value="NM">New Mexico</option>
												<option value="NY">New York</option>
												<option value="NC">North Carolina</option>
												<option value="ND">North Dakota</option>
												<option value="OH">Ohio</option>
												<option value="OK">Oklahoma</option>
												<option value="OR">Oregon</option>
												<option value="PA">Pennsylvania</option>
												<option value="RI">Rhode Island</option>
												<option value="SC">South Carolina</option>
												<option value="SD">South Dakota</option>
												<option value="TN">Tennessee</option>
												<option value="TX">Texas</option>
												<option value="UT">Utah</option>
												<option value="VT">Vermont</option>
												<option value="VA">Virginia</option>
												<option value="WA">Washington</option>
												<option value="WV">West Virginia</option>
												<option value="WI">Wisconsin</option>
												<option value="WY">Wyoming</option>
											</select>
										</div>

										<hr />

										<!-- #sectionController:plugins/input.chosen -->
										<div>
											<label for="form-field-select-3">单选</label> <br /> <select
												class="chosen-select" id="form-field-select-3"
												data-placeholder="Choose a State...">
												<option value=""></option>
												<option value="AL">Alabama</option>
												<option value="AK">Alaska</option>
												<option value="AZ">Arizona</option>
												<option value="AR">Arkansas</option>
												<option value="CA">California</option>
												<option value="CO">Colorado</option>
												<option value="CT">Connecticut</option>
												<option value="DE">Delaware</option>
												<option value="FL">Florida</option>
												<option value="GA">Georgia</option>
												<option value="HI">Hawaii</option>
												<option value="ID">Idaho</option>
												<option value="IL">Illinois</option>
												<option value="IN">Indiana</option>
												<option value="IA">Iowa</option>
												<option value="KS">Kansas</option>
												<option value="KY">Kentucky</option>
												<option value="LA">Louisiana</option>
												<option value="ME">Maine</option>
												<option value="MD">Maryland</option>
												<option value="MA">Massachusetts</option>
												<option value="MI">Michigan</option>
												<option value="MN">Minnesota</option>
												<option value="MS">Mississippi</option>
												<option value="MO">Missouri</option>
												<option value="MT">Montana</option>
												<option value="NE">Nebraska</option>
												<option value="NV">Nevada</option>
												<option value="NH">New Hampshire</option>
												<option value="NJ">New Jersey</option>
												<option value="NM">New Mexico</option>
												<option value="NY">New York</option>
												<option value="NC">North Carolina</option>
												<option value="ND">North Dakota</option>
												<option value="OH">Ohio</option>
												<option value="OK">Oklahoma</option>
												<option value="OR">Oregon</option>
												<option value="PA">Pennsylvania</option>
												<option value="RI">Rhode Island</option>
												<option value="SC">South Carolina</option>
												<option value="SD">South Dakota</option>
												<option value="TN">Tennessee</option>
												<option value="TX">Texas</option>
												<option value="UT">Utah</option>
												<option value="VT">Vermont</option>
												<option value="VA">Virginia</option>
												<option value="WA">Washington</option>
												<option value="WV">West Virginia</option>
												<option value="WI">Wisconsin</option>
												<option value="WY">Wyoming</option>
											</select>
										</div>

										<hr />
										<div>
											<div class="row">
												<div class="col-sm-6">
													<span class="bigger-110">多选</span>
												</div>
												<!-- /.span -->

												<div class="col-sm-6">
													<span class="pull-right inline"> <span class="grey">style:</span>

														<span class="btn-toolbar inline middle no-margin">
															<span id="chosen-multiple-style" data-toggle="buttons"
															class="btn-group no-margin"> <label
																class="btn btn-xs btn-yellow active"> 1 <input
																	type="radio" value="1" />
															</label> <label class="btn btn-xs btn-yellow"> 2 <input
																	type="radio" value="2" />
															</label>
														</span>
													</span>
													</span>
												</div>
												<!-- /.span -->
											</div>

											<div class="space-2"></div>

											<select multiple="" class="chosen-select"
												id="form-field-select-4"
												data-placeholder="Choose a State...">
												<option value="AL">Alabama</option>
												<option value="AK">Alaska</option>
												<option value="AZ">Arizona</option>
												<option value="AR">Arkansas</option>
												<option value="CA">California</option>
												<option value="CO">Colorado</option>
												<option value="CT">Connecticut</option>
												<option value="DE">Delaware</option>
												<option value="FL">Florida</option>
												<option value="GA">Georgia</option>
												<option value="HI">Hawaii</option>
												<option value="ID">Idaho</option>
												<option value="IL">Illinois</option>
												<option value="IN">Indiana</option>
												<option value="IA">Iowa</option>
												<option value="KS">Kansas</option>
												<option value="KY">Kentucky</option>
												<option value="LA">Louisiana</option>
												<option value="ME">Maine</option>
												<option value="MD">Maryland</option>
												<option value="MA">Massachusetts</option>
												<option value="MI">Michigan</option>
												<option value="MN">Minnesota</option>
												<option value="MS">Mississippi</option>
												<option value="MO">Missouri</option>
												<option value="MT">Montana</option>
												<option value="NE">Nebraska</option>
												<option value="NV">Nevada</option>
												<option value="NH">New Hampshire</option>
												<option value="NJ">New Jersey</option>
												<option value="NM">New Mexico</option>
												<option value="NY">New York</option>
												<option value="NC">North Carolina</option>
												<option value="ND">North Dakota</option>
												<option value="OH">Ohio</option>
												<option value="OK">Oklahoma</option>
												<option value="OR">Oregon</option>
												<option value="PA">Pennsylvania</option>
												<option value="RI">Rhode Island</option>
												<option value="SC">South Carolina</option>
												<option value="SD">South Dakota</option>
												<option value="TN">Tennessee</option>
												<option value="TX">Texas</option>
												<option value="UT">Utah</option>
												<option value="VT">Vermont</option>
												<option value="VA">Virginia</option>
												<option value="WA">Washington</option>
												<option value="WV">West Virginia</option>
												<option value="WI">Wisconsin</option>
												<option value="WY">Wyoming</option>
											</select>
										</div>

										<!-- /sectionController:plugins/input.chosen -->
									</div>
								</div>
							</div>
						</div>
						<!-- /.span -->
					</div>
					<!-- /.row -->

					<div class="space-24"></div>

					<h3 class="header smaller lighter blue">复选框 & 单选框</h3>

					<div class="row">
						<div class="col-xs-12 col-sm-5">
							<div class="control-group">
								<label class="control-label bolder blue">复选框</label>

								<!-- #sectionController:custom/checkbox -->
								<div class="checkbox">
									<label> <input name="form-field-checkbox"
										type="checkbox" class="ace" /> <span class="lbl"> 选项1</span>
									</label>
								</div>

								<div class="checkbox">
									<label> <input name="form-field-checkbox"
										type="checkbox" class="ace" /> <span class="lbl"> 选项2</span>
									</label>
								</div>

								<div class="checkbox">
									<label> <input name="form-field-checkbox"
										class="ace ace-checkbox-2" type="checkbox" /> <span
										class="lbl"> 选项3</span>
									</label>
								</div>

								<div class="checkbox">
									<label class="block"> <input name="form-field-checkbox"
										disabled="" type="checkbox" class="ace" /> <span class="lbl">
											disabled</span>
									</label>
								</div>

								<!-- /sectionController:custom/checkbox -->
							</div>
						</div>

						<div class="col-xs-12 col-sm-6">
							<div class="control-group">
								<label class="control-label bolder blue">单选框</label>

								<div class="radio">
									<label> <input name="form-field-radio" type="radio"
										class="ace" /> <span class="lbl"> 单选项1</span>
									</label>
								</div>

								<div class="radio">
									<label> <input name="form-field-radio" type="radio"
										class="ace" /> <span class="lbl"> 单选项2</span>
									</label>
								</div>

								<div class="radio">
									<label> <input name="form-field-radio" type="radio"
										class="ace" /> <span class="lbl"> 单选项3</span>
									</label>
								</div>

								<div class="radio">
									<label> <input disabled="" name="form-field-radio"
										type="radio" class="ace" /> <span class="lbl">
											disabled</span>
									</label>
								</div>
							</div>
						</div>
					</div>
					<!-- /.row -->

					<hr />
					<div class="form-group">
						<label class="control-label col-xs-12 col-sm-3">On/Off 开关</label>

						<div class="controls col-xs-12 col-sm-9">
							<!-- #sectionController:custom/checkbox.switch -->
							<div class="row">
								<div class="col-xs-3">
									<label> <input name="switch-field-1"
										class="ace ace-switch" type="checkbox" /> <span class="lbl"></span>
									</label>
								</div>

								<div class="col-xs-3">
									<label> <input name="switch-field-1"
										class="ace ace-switch ace-switch-2" type="checkbox" /> <span
										class="lbl"></span>
									</label>
								</div>

								<div class="col-xs-3">
									<label> <input name="switch-field-1"
										class="ace ace-switch ace-switch-3" type="checkbox" /> <span
										class="lbl"></span>
									</label>
								</div>

								<div class="col-xs-3">
									<label> <input name="switch-field-1"
										class="ace ace-switch" type="checkbox" /> <span class="lbl"
										data-lbl="CUS&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;TOM"></span>
									</label>
								</div>
							</div>

							<div class="row">
								<div class="col-xs-3">
									<label> <input name="switch-field-1"
										class="ace ace-switch ace-switch-4" type="checkbox" /> <span
										class="lbl"></span>
									</label>
								</div>

								<div class="col-xs-3">
									<label> <input name="switch-field-1"
										class="ace ace-switch ace-switch-5" type="checkbox" /> <span
										class="lbl"></span>
									</label>
								</div>

								<div class="col-xs-3">
									<label> <input name="switch-field-1"
										class="ace ace-switch ace-switch-6" type="checkbox" /> <span
										class="lbl"></span>
									</label>
								</div>

								<div class="col-xs-3">
									<label> <input name="switch-field-1"
										class="ace ace-switch ace-switch-7" type="checkbox" /> <span
										class="lbl"></span>
									</label>
								</div>
							</div>

							<div class="row">
								<div class="col-xs-3">
									<label> <input name="switch-field-1"
										class="ace ace-switch btn-rotate" type="checkbox" /> <span
										class="lbl"></span>
									</label>
								</div>

								<div class="col-xs-3">
									<label> <input name="switch-field-1"
										class="ace ace-switch ace-switch-4 btn-rotate" type="checkbox" />
										<span class="lbl"></span>
									</label>
								</div>

								<div class="col-xs-3">
									<label> <input name="switch-field-1"
										class="ace ace-switch ace-switch-4 btn-empty" type="checkbox" />
										<span class="lbl"></span>
									</label>
								</div>

								<div class="col-xs-3">
									<label> <input name="switch-field-1"
										class="ace ace-switch ace-switch-4 btn-flat" type="checkbox" />
										<span class="lbl"></span>
									</label>
								</div>
							</div>

							<!-- /sectionController:custom/checkbox.switch -->
						</div>
					</div>

					<hr />
					<div class="row">
						<div class="col-sm-4">
							<div class="widget-box">
								<div class="widget-header">
									<h4 class="widget-title">文件上传</h4>

									<div class="widget-toolbar">
										<a href="#" data-action="collapse"> <i
											class="ace-icon fa fa-chevron-up"></i>
										</a> <a href="#" data-action="close"> <i
											class="ace-icon fa fa-times"></i>
										</a>
									</div>
								</div>

								<div class="widget-body">
									<div class="widget-main">
										<div class="form-group">
											<div class="col-xs-12">
												<!-- #sectionController:custom/file-input -->
												<input type="file" id="id-input-file-2" />
											</div>
										</div>

										<div class="form-group">
											<div class="col-xs-12">
												<input multiple="" type="file" id="id-input-file-3" />

												<!-- /sectionController:custom/file-input -->
											</div>
										</div>

										<!-- #sectionController:custom/file-input.filter -->
										<label> <input type="checkbox" name="file-format"
											id="id-file-format" class="ace" /> <span class="lbl">
												只允许图片</span>
										</label>

										<!-- /sectionController:custom/file-input.filter -->
									</div>
								</div>
							</div>
						</div>

						<div class="col-sm-4">
							<div class="widget-box">
								<div class="widget-header">
									<h4 class="widget-title">滑动条</h4>
								</div>

								<div class="widget-body">
									<div class="widget-main">
										<div class="row">
											<div class="col-xs-3 col-md-2">
												<!-- #sectionController:plugins/jquery.slider -->
												<div id="slider-range"></div>
											</div>

											<div class="col-xs-9 col-md-10">
												<div id="slider-eq">
													<span class="ui-slider-green ui-slider-small">77</span> <span
														class="ui-slider-red">55</span> <span
														class="ui-slider-purple" data-rel="tooltip"
														title="Disabled!">33</span> <span
														class="ui-slider-simple ui-slider-orange">40</span> <span
														class="ui-slider-simple ui-slider-dark">88</span>
												</div>

												<!-- /sectionController:plugins/jquery.slider -->
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>

						<div class="col-sm-4">
							<div class="widget-box">
								<div class="widget-header">
									<h4 class="widget-title">微调器</h4>
								</div>

								<div class="widget-body">
									<div class="widget-main">
										<!-- #sectionController:plugins/fuelux.spinner -->
										<input type="text" class="input-mini" id="spinner1" />
										<div class="space-6"></div>

										<input type="text" class="input-mini" id="spinner2" />
										<div class="space-6"></div>

										<input type="text" class="input-mini" id="spinner3" />

										<!-- /sectionController:plugins/fuelux.spinner -->
									</div>
								</div>
							</div>
						</div>
					</div>

					<hr />
					<div class="row">
						<div class="col-sm-4">
							<div class="widget-box">
								<div class="widget-header">
									<h4 class="widget-title">日期选择器</h4>

									<span class="widget-toolbar"> <a href="#"
										data-action="settings"> <i class="ace-icon fa fa-cog"></i>
									</a> <a href="#" data-action="reload"> <i
											class="ace-icon fa fa-refresh"></i>
									</a> <a href="#" data-action="collapse"> <i
											class="ace-icon fa fa-chevron-up"></i>
									</a> <a href="#" data-action="close"> <i
											class="ace-icon fa fa-times"></i>
									</a>
									</span>
								</div>

								<div class="widget-body">
									<div class="widget-main">
										<label for="id-date-picker-1">日期选择器</label>

										<div class="row">
											<div class="col-xs-8 col-sm-11">
												<!-- #sectionController:plugins/date-time.datepicker -->
												<div class="input-group">
													<input class="form-control date-picker"
														id="id-date-picker-1" type="text"
														data-date-format="yyyy-mm-dd" /> <span
														class="input-group-addon"> <i
														class="fa fa-calendar bigger-110"></i>
													</span>
												</div>
											</div>
										</div>

										<div class="space space-8"></div>
										<label>范围选择器</label>

										<div class="row">
											<div class="col-xs-8 col-sm-11">
												<div class="input-daterange input-group">
													<input type="text" class="input-sm form-control"
														name="start" data-date-format="yyyy-mm-dd" /> <span class="input-group-addon"> <i
														class="fa fa-exchange"></i>
													</span> <input type="text" class="input-sm form-control"
														name="end" data-date-format="yyyy-mm-dd" />
												</div>

												<!-- /sectionController:plugins/date-time.datepicker -->
											</div>
										</div>

										<hr />
										<label for="id-date-range-picker-1">日期范围选择器</label>

										<div class="row">
											<div class="col-xs-8 col-sm-11">
												<!-- #sectionController:plugins/date-time.daterangepicker -->
												<label for="id-date-range-picker-1">
												<div class="input-group">
													<span class="input-group-addon"> <i
														class="fa fa-calendar bigger-110"></i>
													</span> <input class="form-control" type="text"
														name="date-range-picker" data-date-format="yyyy-mm-dd" id="id-date-range-picker-1" />
												</div>
												</label>
												<!-- /sectionController:plugins/date-time.daterangepicker -->
											</div>
										</div>

										<hr />
										<label for="timepicker1">时间选择器</label>

										<!-- #sectionController:plugins/date-time.timepicker -->
										<div class="input-group bootstrap-timepicker">
											<input id="timepicker1" type="text" class="form-control" />
											<span class="input-group-addon"> <i
												class="fa fa-clock-o bigger-110"></i>
											</span>
										</div>
										<!-- /sectionController:plugins/date-time.datetimepicker -->
										
										<hr />
										<label for="date-timepicker1">日期/时间 选择器</label>

										<!-- #sectionController:plugins/date-time.datetimepicker -->
										<div class="input-group">
											<input id="date-timepicker1" type="text" class="form-control"  data-date-format="YYYY-MM-DD HH:mm:ss" />
											<span class="input-group-addon">
												<i class="fa fa-clock-o bigger-110"></i>
											</span>
										</div>
									</div>
								</div>
							</div>
						</div>

						<div class="col-sm-4">
							<div class="widget-box">
								<div class="widget-header">
									<h4 class="widget-title">
										<i class="ace-icon fa fa-tint"></i> 颜色选择器
									</h4>
								</div>

								<div class="widget-body">
									<div class="widget-main">
										<div class="clearfix">
											<label for="colorpicker1">颜色选择器</label>
										</div>

										<div class="control-group">
											<div class="bootstrap-colorpicker">
												<!-- #sectionController:plugins/misc.colorpicker -->
												<input id="colorpicker1" type="text" class="input-small" />

												<!-- /sectionController:plugins/misc.colorpicker -->
											</div>
										</div>

										<hr />

										<!-- #sectionController:custom/colorpicker -->
										<div>
											<label for="simple-colorpicker-1">直接选择颜色</label>

											<select id="simple-colorpicker-1" class="hide">
												<option value="#ac725e">#ac725e</option>
												<option value="#d06b64">#d06b64</option>
												<option value="#f83a22">#f83a22</option>
												<option value="#fa573c">#fa573c</option>
												<option value="#ff7537">#ff7537</option>
												<option value="#ffad46" selected="">#ffad46</option>
												<option value="#42d692">#42d692</option>
												<option value="#16a765">#16a765</option>
												<option value="#7bd148">#7bd148</option>
												<option value="#b3dc6c">#b3dc6c</option>
												<option value="#fbe983">#fbe983</option>
												<option value="#fad165">#fad165</option>
												<option value="#92e1c0">#92e1c0</option>
												<option value="#9fe1e7">#9fe1e7</option>
												<option value="#9fc6e7">#9fc6e7</option>
												<option value="#4986e7">#4986e7</option>
												<option value="#9a9cff">#9a9cff</option>
												<option value="#b99aff">#b99aff</option>
												<option value="#c2c2c2">#c2c2c2</option>
												<option value="#cabdbf">#cabdbf</option>
												<option value="#cca6ac">#cca6ac</option>
												<option value="#f691b2">#f691b2</option>
												<option value="#cd74e6">#cd74e6</option>
												<option value="#a47ae2">#a47ae2</option>
												<option value="#555">#555</option>
											</select>
										</div>

										<!-- /sectionController:custom/colorpicker -->
									</div>
								</div>
							</div>
						</div>

						<div class="col-sm-4">
							<div class="widget-box">
								<div class="widget-header">
									<h4 class="widget-title">
										<i class="ace-icon fa fa-tachometer"></i> 旋钮文本框
									</h4>
								</div>

								<div class="widget-body">
									<div class="widget-main">
										<div class="control-group">
											<div class="row">
												<div class="col-xs-6 center">
													<!-- #sectionController:plugins/charts.knob -->
													<div class="knob-container inline">
														<input type="text" class="input-small knob" value="15"
															data-min="0" data-max="100" data-step="10"
															data-width="80" data-height="80" data-thickness=".2" />
													</div>
												</div>

												<div class="col-xs-6  center">
													<div class="knob-container inline">
														<input type="text" class="input-small knob" value="41"
															data-min="0" data-max="100" data-width="80"
															data-height="80" data-thickness=".2"
															data-fgColor="#87B87F" data-displayPrevious="true"
															data-angleArc="250" data-angleOffset="-125" />
													</div>

													<!-- /sectionController:plugins/charts.knob -->
												</div>
											</div>

											<div class="row">
												<div class="col-xs-12 center">
													<div class="knob-container inline">
														<input type="text" class="input-small knob" data-min="0"
															data-max="10" data-width="150" data-height="150"
															data-thickness=".2" data-fgColor="#B8877F"
															data-angleOffset="90" data-cursor="true" />
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>

				<div class="hr hr-18 dotted hr-double"></div>

				<h4 class="pink">
					<i class="ace-icon fa fa-hand-o-right green"></i> <a
						href="#modal-form" role="button" class="blue" data-toggle="modal">
						嵌入在模态框中的表单 </a>
				</h4>

				<div class="hr hr-18 dotted hr-double"></div>
				<h4 class="header green">表单布局</h4>

				<div class="row">
					<div class="col-sm-5">
						<div class="widget-box">
							<div class="widget-header">
								<h4 class="widget-title">默认</h4>
							</div>

							<div class="widget-body">
								<div class="widget-main no-padding">
									<form>
										<!-- <legend>Form</legend> -->
										<fieldset>
											<label>标签名</label> <input type="text" placeholder="输入一些文字" />
											<label class="pull-right"> <input type="checkbox"
												class="ace" /> <span class="lbl"> 选中</span>
											</label>
										</fieldset>

										<div class="form-actions center">
											<button type="button" class="btn btn-sm btn-success">
												提交 <i
													class="ace-icon fa fa-arrow-right icon-on-right bigger-110"></i>
											</button>
										</div>
									</form>
								</div>
							</div>
						</div>
					</div>

					<div class="col-sm-7">
						<div class="widget-box">
							<div class="widget-header">
								<h4 class="widget-title">内联表单</h4>
							</div>

							<div class="widget-body">
								<div class="widget-main">
									<form class="form-inline">
										<input type="text" class="input-small" placeholder="用户名" />
										<input type="password" class="input-small"
											placeholder="密码" /> <label class="inline"> <input
											type="checkbox" class="ace" /> <span class="lbl">
												记住我</span>
										</label>

										<button type="button" class="btn btn-info btn-sm">
											<i class="ace-icon fa fa-key bigger-110"></i>登录
										</button>
									</form>
								</div>
							</div>
						</div>

						<div class="space-6"></div>

						<div class="widget-box">
							<div class="widget-header widget-header-small">
								<h5 class="widget-title lighter">搜索表单</h5>
							</div>

							<div class="widget-body">
								<div class="widget-main">
									<form class="form-search">
										<div class="row">
											<div class="col-xs-12 col-sm-8">
												<div class="input-group">
													<input type="text" class="form-control search-query"
														placeholder="输入查询条件" /> <span
														class="input-group-btn">
														<button type="button" class="btn btn-purple btn-sm">
															搜索 <i
																class="ace-icon fa fa-search icon-on-right bigger-110"></i>
														</button>
													</span>
												</div>
											</div>
										</div>
									</form>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div id="modal-form" class="modal" tabindex="-1">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal">&times;</button>
											<h4 class="blue bigger">请填写以下表格字段</h4>
										</div>

										<div class="modal-body">
											<div class="row">
												<div class="col-xs-12 col-sm-5">
													<div class="space"></div>

													<input type="file" />
												</div>

												<div class="col-xs-12 col-sm-7">
													<div class="form-group">
														<label for="form-field-select-3">位置</label>

														<div>
															<select class="chosen-select" data-placeholder="选择一个国家...">
																<option value="">&nbsp;</option>
																<option value="AL">Alabama</option>
																<option value="AK">Alaska</option>
																<option value="AZ">Arizona</option>
																<option value="AR">Arkansas</option>
																<option value="CA">California</option>
																<option value="CO">Colorado</option>
																<option value="CT">Connecticut</option>
																<option value="DE">Delaware</option>
																<option value="FL">Florida</option>
																<option value="GA">Georgia</option>
																<option value="HI">Hawaii</option>
																<option value="ID">Idaho</option>
																<option value="IL">Illinois</option>
																<option value="IN">Indiana</option>
																<option value="IA">Iowa</option>
																<option value="KS">Kansas</option>
																<option value="KY">Kentucky</option>
																<option value="LA">Louisiana</option>
																<option value="ME">Maine</option>
																<option value="MD">Maryland</option>
																<option value="MA">Massachusetts</option>
																<option value="MI">Michigan</option>
																<option value="MN">Minnesota</option>
																<option value="MS">Mississippi</option>
																<option value="MO">Missouri</option>
																<option value="MT">Montana</option>
																<option value="NE">Nebraska</option>
																<option value="NV">Nevada</option>
																<option value="NH">New Hampshire</option>
																<option value="NJ">New Jersey</option>
																<option value="NM">New Mexico</option>
																<option value="NY">New York</option>
																<option value="NC">North Carolina</option>
																<option value="ND">North Dakota</option>
																<option value="OH">Ohio</option>
																<option value="OK">Oklahoma</option>
																<option value="OR">Oregon</option>
																<option value="PA">Pennsylvania</option>
																<option value="RI">Rhode Island</option>
																<option value="SC">South Carolina</option>
																<option value="SD">South Dakota</option>
																<option value="TN">Tennessee</option>
																<option value="TX">Texas</option>
																<option value="UT">Utah</option>
																<option value="VT">Vermont</option>
																<option value="VA">Virginia</option>
																<option value="WA">Washington</option>
																<option value="WV">West Virginia</option>
																<option value="WI">Wisconsin</option>
																<option value="WY">Wyoming</option>
															</select>
														</div>
													</div>

													<div class="space-4"></div>

													<div class="form-group">
														<label for="form-field-username">用户名</label>

														<div>
															<input class="input-large" type="text" id="form-field-username" placeholder="用户名" value="alexdoe" />
														</div>
													</div>

													<div class="space-4"></div>

													<div class="form-group">
														<label for="form-field-first">姓名</label>

														<div>
															<input class="input-medium" type="text" id="form-field-first" placeholder="名字" value="Alex" />
															<input class="input-medium" type="text" id="form-field-last" placeholder="姓" value="Doe" />
														</div>
													</div>
												</div>
											</div>
										</div>

										<div class="modal-footer">
											<button class="btn btn-sm" data-dismiss="modal">
												<i class="ace-icon fa fa-times"></i>
												取消
											</button>

											<button class="btn btn-sm btn-primary">
												<i class="ace-icon fa fa-check"></i>
												保存
											</button>
										</div>
									</div>
								</div>
							</div><!-- PAGE CONTENT ENDS -->
				<!-- PAGE CONTENT ENDS -->
			</div>
			<!-- /.col -->
		</div>
		<!-- /.row -->
	</div>
	<!-- /.page-content -->
	
	<!-- inline scripts related to this page -->
	<script type="text/javascript">
	jQuery(function($) {
		$('#id-disable-check').on('click', function() {
			var inp = $('#form-input-readonly').get(0);
			if (inp.hasAttribute('disabled')) {
				inp.setAttribute('readonly', 'true');
				inp.removeAttribute('disabled');
				inp.value = "这个文本框是readonly！";
			} else {
				inp.setAttribute('disabled', 'disabled');
				inp.removeAttribute('readonly');
				inp.value = "这个文本框是disabled！";
			}
		});
		
		$('.chosen-select').chosen({
			allow_single_deselect : true
		});
		//resize the chosen on window resize
		$(window).on('resize.chosen', function() {
			$('.chosen-select').next().css({
				'width' : '210px'
			});
		}).trigger('resize.chosen');

		$('#chosen-multiple-style').on('click', function(e) {
			var target = $(e.target).find('input[type=radio]');
			var which = parseInt(target.val());
			if (which == 2)
				$('#form-field-select-4').addClass('tag-input-style');
			else
				$('#form-field-select-4').removeClass('tag-input-style');
		});

		$('[data-rel=tooltip]').tooltip({
			container : 'body'
		});
		$('[data-rel=popover]').popover({
			container : 'body'
		});

		$('textarea[class*=autosize]').autosize({
			append : "\n"
		});
		$('textarea.limited').inputlimiter({
			remText : '%n character%s remaining...',
			limitText : 'max allowed : %n.'
		});
		
		$.mask.definitions['~'] = '[+-]';
		$('.input-mask-date').mask('99/99/9999');
		$('.input-mask-phone').mask('(999) 999-9999');
		
		/*可变大小的文本框*/
		$("#input-size-slider").css('width', '200px').slider(
		{
			value : 1,
			range : "min",
			min : 1,
			max : 8,
			step : 1,
			slide : function(event, ui) {
				var sizing = [ '', 'input-sm', 'input-lg',
						'input-mini', 'input-small',
						'input-medium', 'input-large',
						'input-xlarge', 'input-xxlarge' ];
				var val = parseInt(ui.value);
				$('#form-field-4').attr('class', sizing[val]).val(
						'.' + sizing[val]);
			}
		});

		$("#input-span-slider").slider(
		{
			value : 1,
			range : "min",
			min : 1,
			max : 12,
			step : 1,
			slide : function(event, ui) {
				var val = parseInt(ui.value);
				$('#form-field-5').attr('class', 'col-xs-' + val)
						.val('.col-xs-' + val);
			}
		});
		
		/*滑动条*/
		$("#slider-range").css('height', '200px').slider(
			{
					orientation : "vertical",
					range : true,
					min : 0,
					max : 100,
					values : [ 17, 67 ],
					slide : function(event, ui) {
						var val = ui.values[$(ui.handle).index() - 1] + "";
						if (!ui.handle.firstChild) {
							$("<div class='tooltip right in' style='display:none;left:16px;top:-6px;'><div class='tooltip-arrow'></div><div class='tooltip-inner'></div></div>")
								.prependTo(ui.handle);
						}
						$(ui.handle.firstChild).show().children()
								.eq(1).text(val);
					}
				}).find('a').on('blur', function() {
			        $(this.firstChild).hide();
			});

			$("#slider-range-max").slider({
				range : "max",
				min : 1,
				max : 10,
				value : 2
			});

			$("#slider-eq > span").css({
				width : '90%',
				'float' : 'left',
				margin : '15px'
			}).each(function() {
				// read initial values from markup and remove that
				var value = parseInt($(this).text(), 10);
				$(this).empty().slider({
					value : value,
					range : "min",
					animate : true

				});
			});

			$("#slider-eq > span.ui-slider-purple").slider('disable');//disable third item
			
			/*文件上传*/
			$('#id-input-file-1 , #id-input-file-2').ace_file_input({
				no_file : 'No File ...',
				btn_choose : 'Choose',
				btn_change : 'Change',
				droppable : false,
				onchange : null,
				thumbnail : false
			
			});

			$('#id-input-file-3').ace_file_input({
				style : 'well',
				btn_choose : 'Drop files here or click to choose',
				btn_change : null,
				no_icon : 'ace-icon fa fa-cloud-upload',
				droppable : true,
				thumbnail : 'small',//large | fit				
				preview_error : function(filename, error_code) {
					
				}

			}).on('change', function() {
				
			});

			$('#id-file-format').removeAttr('checked').on('change',
					function() {
						var whitelist_ext, whitelist_mime;
						var btn_choose;
						var no_icon;
						if (this.checked) {
							btn_choose = "Drop images here or click to choose";
							no_icon = "ace-icon fa fa-picture-o";

							whitelist_ext = [ "jpeg", "jpg", "png", "gif",
									"bmp" ];
							whitelist_mime = [ "image/jpg", "image/jpeg",
									"image/png", "image/gif", "image/bmp" ];
						} else {
							btn_choose = "Drop files here or click to choose";
							no_icon = "ace-icon fa fa-cloud-upload";

							whitelist_ext = null;//all extensions are acceptable
							whitelist_mime = null;//all mimes are acceptable
						}
						var file_input = $('#id-input-file-3');
						file_input.ace_file_input('update_settings', {
							'btn_choose' : btn_choose,
							'no_icon' : no_icon,
							'allowExt' : whitelist_ext,
							'allowMime' : whitelist_mime
						});
						file_input.ace_file_input('reset_input');

						file_input.off('file.error.ace').on('file.error.ace',
								function(e, info) {
									
						});

			 });
			
			/*spinner*/
			$('#spinner1').ace_spinner({
				value : 0,
				min : 0,
				max : 200,
				step : 10,
				btn_up_class : 'btn-info',
				btn_down_class : 'btn-info'
			}).on('change', function() {
				
			});
			$('#spinner2').ace_spinner({
				value : 0,
				min : 0,
				max : 10000,
				step : 100,
				touch_spinner : true,
				icon_up : 'ace-icon fa fa-caret-up',
				icon_down : 'ace-icon fa fa-caret-down'
			});
			$('#spinner3').ace_spinner({
				value : 0,
				min : -100,
				max : 100,
				step : 10,
				on_sides : true,
				icon_up : 'ace-icon fa fa-plus smaller-75',
				icon_down : 'ace-icon fa fa-minus smaller-75',
				btn_up_class : 'btn-success',
				btn_down_class : 'btn-danger'
			});
			
			$('.date-picker').datepicker({
				autoclose : true,
				todayHighlight : true
			})
			//show datepicker when clicking on the icon
			.next().on(ace.click_event, function() {
				$(this).prev().focus();
			});

			//or change it into a date range picker
			$('.input-daterange').datepicker({
				autoclose : true
			});

			//to translate the daterange picker, please copy the "examples/daterange-fr.js" contents here before initialization
			$('input[name=date-range-picker]').daterangepicker({
				'applyClass' : 'btn-sm btn-success',
				'cancelClass' : 'btn-sm btn-default',
				locale : {
					applyLabel : 'Apply',
					cancelLabel : 'Cancel',
				}
			}).prev().on(ace.click_event, function() {
				$(this).next().focus();
			});

			$('#timepicker1').timepicker({
				minuteStep : 1,
				showSeconds : true,
				showMeridian : false
			}).next().on(ace.click_event, function() {
				$(this).prev().focus();
			});
			
			$('#date-timepicker1').datetimepicker().next().on(ace.click_event, function(){
				$(this).prev().focus();
			});
			
			$('#colorpicker1').colorpicker();
			$('#simple-colorpicker-1').ace_colorpicker();
			
			$(".knob").knob();
			
			var tag_input = $('#form-field-tags');
			try{
				tag_input.tag(
				  {
					placeholder:tag_input.attr('placeholder'),
					//enable typeahead by specifying the source array
					source: ace.vars['US_STATES'],//defined in ace.js >> ace.enable_search_ahead
					/**
					//or fetch data from database, fetch those that match "query"
					source: function(query, process) {
					  $.ajax({url: 'remote_source.php?q='+encodeURIComponent(query)})
					  .done(function(result_items){
						process(result_items);
					  });
					}
					*/
				  }
				);
		
				//programmatically add a new
				var $tag_obj = $('#form-field-tags').data('tag');
				$tag_obj.add('Programmatically Added');
			}
			catch(e) {
				//display a textarea for old IE, because it doesn't support this plugin or another one I tried!
				tag_input.after('<textarea id="'+tag_input.attr('id')+'" name="'+tag_input.attr('name')+'" rows="3">'+tag_input.val()+'</textarea>').remove();
				//$('#form-field-tags').autosize({append: "\n"});
			}
			
			$('#modal-form input[type=file]').ace_file_input({
				style:'well',
				btn_choose:'Drop files here or click to choose',
				btn_change:null,
				no_icon:'ace-icon fa fa-cloud-upload',
				droppable:true,
				thumbnail:'large'
			})
			
			//chosen plugin inside a modal will have a zero width because the select element is originally hidden
			//and its width cannot be determined.
			//so we set the width after modal is show
			$('#modal-form').on('shown.bs.modal', function () {
				$(this).find('.chosen-container').each(function(){
					$(this).find('a:first-child').css('width' , '210px');
					$(this).find('.chosen-drop').css('width' , '210px');
					$(this).find('.chosen-search input').css('width' , '200px');
				});
			})
			
	});
	</script>
</body>
</html>
