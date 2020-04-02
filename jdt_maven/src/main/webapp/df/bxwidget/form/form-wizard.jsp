<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>向导 & 校验</title>
</head>
<body>
	<%@ include file="/bxui/bxuihead.jsp"%>
	<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
	<script type="text/javascript"
		src="<%=toolkitPath%>/bxui/baosight-fuelux.js"></script>
	<div class="page-content">
		<div class="page-header">
			<h1>
				向导 & 校验 <small> <i class="ace-icon fa fa-angle-double-right"></i>
					表单校验和向导
				</small>
			</h1>
		</div>
		<!-- /.page-header -->
		<div class="row">
			<div class="col-xs-12">
				<h4 class="lighter">
					<i
						class="ace-icon fa fa-hand-o-right icon-animated-hand-pointer blue"></i>
					<a href="#modal-wizard" data-toggle="modal" class="pink">
						模态框中的向导组件 </a>
				</h4>
				<div class="hr hr-18 hr-double dotted"></div>

				<!-- 弹出的导向模态框 -->
				<div id="modal-wizard" class="modal">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header" data-target="#modal-step-contents">
								<ul class="wizard-steps">
									<li data-target="#modal-step1" class="active"><span
										class="step">1</span> <span class="title">验证状态</span></li>

									<li data-target="#modal-step2"><span class="step">2</span>
										<span class="title">警告框</span></li>

									<li data-target="#modal-step3"><span class="step">3</span>
										<span class="title">支付信息</span></li>

									<li data-target="#modal-step4"><span class="step">4</span>
										<span class="title">其它信息</span></li>
								</ul>
							</div>
							<div class="modal-body step-content" id="modal-step-contents">
								<div class="step-pane active" id="modal-step1">
									<div class="center">
										<h4 class="blue">第一步</h4>
									</div>
								</div>

								<div class="step-pane" id="modal-step2">
									<div class="center">
										<h4 class="blue">第二步</h4>
									</div>
								</div>

								<div class="step-pane" id="modal-step3">
									<div class="center">
										<h4 class="blue">第三步</h4>
									</div>
								</div>

								<div class="step-pane" id="modal-step4">
									<div class="center">
										<h4 class="blue">第四步</h4>
									</div>
								</div>
							</div>
							<div class="modal-footer wizard-actions">
								<button class="btn btn-sm btn-prev">
									<i class="ace-icon fa fa-arrow-left"></i> 上一步
								</button>

								<button class="btn btn-success btn-sm btn-next" data-last="完成">
									下一步 <i class="ace-icon fa fa-arrow-right icon-on-right"></i>
								</button>

								<button class="btn btn-danger btn-sm pull-left"
									data-dismiss="modal">
									<i class="ace-icon fa fa-times"></i> 取消
								</button>
							</div>
						</div>
					</div>
				</div>
				<!-- end 弹出的导向框 -->


				<div class="widget-box">
					<div class="widget-header widget-header-blue widget-header-flat">
						<h4 class="widget-title lighter">向导组件</h4>

						<div class="widget-toolbar">
							<label> <small class="green"> <b>验证开关</b>
							</small> <input id="skip-validation" type="checkbox"
								class="ace ace-switch ace-switch-4" /> <span class="lbl middle"></span>
							</label>
						</div>
					</div>

					<div class="widget-body">
						<div class="widget-main">
							<!-- #sectionController:plugins/fuelux.wizard -->
							<div id="fuelux-wizard" data-target="#step-container">
								<!-- #sectionController:plugins/fuelux.wizard.steps -->
								<ul class="wizard-steps">
									<li data-target="#step1" class="active"><span class="step">1</span>
										<span class="title">验证状态</span></li>

									<li data-target="#step2"><span class="step">2</span> <span
										class="title">警告框</span></li>

									<li data-target="#step3"><span class="step">3</span> <span
										class="title">支付信息</span></li>

									<li data-target="#step4"><span class="step">4</span> <span
										class="title">其它信息</span></li>
								</ul>

								<!-- /sectionController:plugins/fuelux.wizard.steps -->
							</div>

							<hr />

							<!-- #sectionController:plugins/fuelux.wizard.container -->
							<div class="step-content pos-rel" id="step-container">
								<div class="step-pane active" id="step1">
									<h3 class="lighter block green">请输入以下信息：</h3>

									<form class="form-horizontal" id="sample-form">
										<!-- #sectionController:elements.form.input-state -->
										<div class="form-group has-warning">
											<label for="inputWarning"
												class="col-xs-12 col-sm-3 control-label no-padding-right">警告文本框</label>

											<div class="col-xs-12 col-sm-5">
												<span class="block input-icon input-icon-right"> <input
													type="text" id="inputWarning" class="width-100" /> <i
													class="ace-icon fa fa-leaf"></i>
												</span>
											</div>
										</div>

										<!-- /sectionController:elements.form.input-state -->
										<div class="form-group has-error">
											<label for="inputError"
												class="col-xs-12 col-sm-3 col-md-3 control-label no-padding-right">错误文本框</label>

											<div class="col-xs-12 col-sm-5">
												<span class="block input-icon input-icon-right"> <input
													type="text" id="inputError" class="width-100" /> <i
													class="ace-icon fa fa-times-circle"></i>
												</span>
											</div>
										</div>

										<div class="form-group has-success">
											<label for="inputSuccess"
												class="col-xs-12 col-sm-3 control-label no-padding-right">成功文本框</label>

											<div class="col-xs-12 col-sm-5">
												<span class="block input-icon input-icon-right"> <input
													type="text" id="inputSuccess" class="width-100" /> <i
													class="ace-icon fa fa-check-circle"></i>
												</span>
											</div>
										</div>

										<div class="form-group has-info">
											<label for="inputInfo"
												class="col-xs-12 col-sm-3 control-label no-padding-right">信息文本框</label>

											<div class="col-xs-12 col-sm-5">
												<span class="block input-icon input-icon-right"> <input
													type="text" id="inputInfo" class="width-100" /> <i
													class="ace-icon fa fa-info-circle"></i>
												</span>
											</div>
										</div>
									</form>

									<form class="form-horizontal hide" id="validation-form"
										method="get">
										<div class="form-group">
											<label
												class="control-label col-xs-12 col-sm-3 no-padding-right"
												for="email">电子邮件：</label>

											<div class="col-xs-12 col-sm-9">
												<div class="clearfix">
													<input type="email" name="email" id="email"
														class="col-xs-12 col-sm-6" />
												</div>
											</div>
										</div>

										<div class="space-2"></div>

										<div class="form-group">
											<label
												class="control-label col-xs-12 col-sm-3 no-padding-right"
												for="password">密码：</label>

											<div class="col-xs-12 col-sm-9">
												<div class="clearfix">
													<input type="password" name="password" id="password"
														class="col-xs-12 col-sm-4" />
												</div>
											</div>
										</div>

										<div class="space-2"></div>

										<div class="form-group">
											<label
												class="control-label col-xs-12 col-sm-3 no-padding-right"
												for="password2">确认密码：</label>

											<div class="col-xs-12 col-sm-9">
												<div class="clearfix">
													<input type="password" name="password2" id="password2"
														class="col-xs-12 col-sm-4" />
												</div>
											</div>
										</div>

										<div class="hr hr-dotted"></div>

										<div class="form-group">
											<label
												class="control-label col-xs-12 col-sm-3 no-padding-right"
												for="name">公司名称：</label>

											<div class="col-xs-12 col-sm-9">
												<div class="clearfix">
													<input type="text" id="name" name="name"
														class="col-xs-12 col-sm-5" />
												</div>
											</div>
										</div>

										<div class="space-2"></div>

										<div class="form-group">
											<label
												class="control-label col-xs-12 col-sm-3 no-padding-right"
												for="phone">电话号码：</label>

											<div class="col-xs-12 col-sm-9">
												<div class="input-group">
													<span class="input-group-addon"> <i
														class="ace-icon fa fa-phone"></i>
													</span> <input type="tel" id="phone" name="phone" />
												</div>
											</div>
										</div>

										<div class="space-2"></div>

										<div class="form-group">
											<label
												class="control-label col-xs-12 col-sm-3 no-padding-right"
												for="url">公司网址：</label>

											<div class="col-xs-12 col-sm-9">
												<div class="clearfix">
													<input type="url" id="url" name="url"
														class="col-xs-12 col-sm-8" />
												</div>
											</div>
										</div>

										<div class="hr hr-dotted"></div>

										<div class="form-group">
											<label
												class="control-label col-xs-12 col-sm-3 no-padding-right">订阅</label>

											<div class="col-xs-12 col-sm-9">
												<div>
													<label> <input name="subscription" value="1"
														type="checkbox" class="ace" /> <span class="lbl">
															最新消息和公告</span>
													</label>
												</div>

												<div>
													<label> <input name="subscription" value="2"
														type="checkbox" class="ace" /> <span class="lbl">
															产品信息和折扣</span>
													</label>
												</div>
											</div>
										</div>

										<div class="space-2"></div>

										<div class="form-group">
											<label
												class="control-label col-xs-12 col-sm-3 no-padding-right">性别</label>

											<div class="col-xs-12 col-sm-9">
												<div>
													<label class="line-height-1 blue"> <input
														name="gender" value="1" type="radio" class="ace" /> <span
														class="lbl"> 男</span>
													</label>
												</div>

												<div>
													<label class="line-height-1 blue"> <input
														name="gender" value="2" type="radio" class="ace" /> <span
														class="lbl"> 女</span>
													</label>
												</div>
											</div>
										</div>

										<div class="hr hr-dotted"></div>

										<div class="form-group">
											<label
												class="control-label col-xs-12 col-sm-3 no-padding-right"
												for="state">州名</label>

											<div class="col-xs-12 col-sm-9">
												<select id="state" name="state" class="select2"
													data-placeholder="Click to Choose...">
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

										<div class="space-2"></div>

										<div class="form-group">
											<label
												class="control-label col-xs-12 col-sm-3 no-padding-right"
												for="platform">操作系统</label>

											<div class="col-xs-12 col-sm-9">
												<div class="clearfix">
													<select class="input-medium" id="platform" name="platform">
														<option value="">------------------</option>
														<option value="linux">Linux</option>
														<option value="windows">Windows</option>
														<option value="mac">Mac OS</option>
														<option value="ios">iOS</option>
														<option value="android">Android</option>
													</select>
												</div>
											</div>
										</div>

										<div class="space-2"></div>

										<div class="form-group">
											<label
												class="control-label col-xs-12 col-sm-3 no-padding-right"
												for="comment">评论</label>

											<div class="col-xs-12 col-sm-9">
												<div class="clearfix">
													<textarea class="input-xlarge" name="comment" id="comment"></textarea>
												</div>
											</div>
										</div>

										<div class="space-8"></div>

										<div class="form-group">
											<div class="col-xs-12 col-sm-4 col-sm-offset-3">
												<label> <input name="agree" id="agree"
													type="checkbox" class="ace" /> <span class="lbl">我接受协议</span>
												</label>
											</div>
										</div>
									</form>
								</div>

								<div class="step-pane" id="step2">
									<div>
										<div class="alert alert-success">
											<button type="button" class="close" data-dismiss="alert">
												<i class="ace-icon fa fa-times"></i>
											</button>

											<strong> <i class="ace-icon fa fa-check"></i> 执行成功！
											</strong> 成功执行了这次操作！ <br />
										</div>

										<div class="alert alert-danger">
											<button type="button" class="close" data-dismiss="alert">
												<i class="ace-icon fa fa-times"></i>
											</button>

											<strong> <i class="ace-icon fa fa-times"></i> 执行失败！
											</strong> 调整参数，请再尝试一次！ <br />
										</div>

										<div class="alert alert-warning">
											<button type="button" class="close" data-dismiss="alert">
												<i class="ace-icon fa fa-times"></i>
											</button>
											<strong>警告！</strong> 执行成功，但有个别信息有误！ <br />
										</div>

										<div class="alert alert-info">
											<button type="button" class="close" data-dismiss="alert">
												<i class="ace-icon fa fa-times"></i>
											</button>
											<strong>注意！</strong> 这条信息引起你的注意，但是它不是特别重要！ <br />
										</div>
									</div>
								</div>

								<div class="step-pane" id="step3">
									<div class="center">
										<h3 class="blue lighter">显示支付的相关信息</h3>
									</div>
								</div>

								<div class="step-pane" id="step4">
									<div class="center">
										<h3 class="green">恭喜！</h3>
										您的订单将成功提交，请点击“完成”按钮
									</div>
								</div>
							</div>

							<!-- /sectionController:plugins/fuelux.wizard.container -->
							<hr />
							<div class="wizard-actions">
								<!-- #sectionController:plugins/fuelux.wizard.buttons -->
								<button class="btn btn-prev">
									<i class="ace-icon fa fa-arrow-left"></i> 上一步
								</button>

								<button class="btn btn-success btn-next" data-last="完成">
									下一步 <i class="ace-icon fa fa-arrow-right icon-on-right"></i>
								</button>

								<!-- /sectionController:plugins/fuelux.wizard.buttons -->
							</div>

							<!-- /sectionController:plugins/fuelux.wizard -->
						</div>
						<!-- /.widget-main -->
					</div>
					<!-- /.widget-body -->

				</div>

			</div>
		</div>
		<!-- end row -->
	</div>
	<!-- end page content -->

	<script type="text/javascript">
	    $(document).ready(function() {			
			baosightRequire.requireFunct(['bxvalidate','bxwizard'], function() {
				var $validation = false;
				$('#skip-validation').removeAttr('checked').on('click', function(){
					$validation = this.checked;
					if(this.checked) {
						$('#sample-form').hide();
						$('#validation-form').removeClass('hide');
					}
					else {
						$('#validation-form').addClass('hide');
						$('#sample-form').show();
					}
				});
				
				$('#validation-form').bxvalidate();
				var ruleOptionCustom = {
					rules : {
						email: {
							required: true,
							email:true
						},
						password: {
							required: true,
							minlength: 5
						},
						password2: {
							required: true,
							minlength: 5,
							equalTo: "#password"
						},
						name: {
							required: true
						},
						phone: {
							required: true				
						},
						url: {
							required: true,
							url: true
						},
						comment: {
							required: true
						},
						date: {
							required: true,
							date: true
						},
						state: {
							required: true
						},
						platform: {
							required: true
						}
					},
					messages: {
						email: {
							required: "请提供一个有效的邮箱！",
							email: "请提供一个有效的邮箱！"
						},
						password: {
							required: "请提供一个密码！",
							minlength: "密码必须满足最小长度！"
						}
					}
				};
				$("#validation-form").bxvalidate("option", "ruleOptionCustom", ruleOptionCustom);				
				var wizardOpt = {
					bind : {
						'change' : function(e, info){
							if(info.step == 1 && $validation) {
								if(!$("#validation-form").bxvalidate("validate")) return false;
							}
						}	
					}
				};				
				$('#fuelux-wizard').bxwizard(wizardOpt);								
				$('#modal-wizard .modal-header').bxwizard();//使模态导向框的导向功能生效
			});						
		});
	</script>


</body>
</html>