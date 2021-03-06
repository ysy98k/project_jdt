<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>jQuery UI</title>

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
							jQuery UI
							<small>
								<i class="ace-icon fa fa-angle-double-right"></i>
								jQuery UI 小部件和元件
							</small>
						</h1>
					</div><!-- /.page-header -->

					<div class="row">
						<div class="col-xs-12">
							<!-- PAGE CONTENT BEGINS -->
							<div class="row">
								<div class="col-sm-6">
									<h3 class="header blue lighter smaller">
										<i class="ace-icon fa fa-calendar-o smaller-90"></i>
										日期选择器
									</h3>
									
									<div class="row">
										<div class="col-xs-6"><label for="datepicker">
											<div class="input-group input-group-sm">
												<input type="text" id="datepicker" class="form-control" />
												<span class="input-group-addon">
													<i class="ace-icon fa fa-calendar"></i>
												</span>
											</div></label>
										</div>
									</div>
								</div><!-- ./span -->

								<div class="col-sm-6">
									<h3 class="header blue lighter smaller">
										<i class="ace-icon fa fa-list-alt smaller-90"></i>
										对话框
									</h3>
									<a href="#" id="id-btn-dialog2" class="btn btn-info btn-sm">Confirm Dialog</a>
									<a href="#" id="id-btn-dialog1" class="btn btn-purple btn-sm">Modal Dialog</a>

									<div id="dialog-message" class="hide">
										<p>
											This is the default dialog which is useful for displaying information. The dialog window can be moved, resized and closed with the 'x' icon.
										</p>

										<div class="hr hr-12 hr-double"></div>

										<p>
											Currently using
											<b>36% of your storage space</b>.
										</p>
									</div><!-- #dialog-message -->

									<div id="dialog-confirm" class="hide">
										<div class="alert alert-info bigger-110">
											These items will be permanently deleted and cannot be recovered.
										</div>

										<div class="space-6"></div>

										<p class="bigger-110 bolder center grey">
											<i class="ace-icon fa fa-hand-o-right blue bigger-120"></i>
											Are you sure?
										</p>
									</div><!-- #dialog-confirm -->
								</div><!-- ./span -->
							</div><!-- ./row -->

							<div class="space-12"></div>

							<div class="row">
								<div class="col-sm-6">
									<h3 class="header blue lighter smaller">
										<i class="ace-icon fa fa-list smaller-90"></i>
										可排序的折叠菜单
									</h3>

									<div id="accordion" class="accordion-style2">
										<div class="group">
											<h3 class="accordion-header">Section 1</h3>

											<div>
												<p>
													Mauris mauris ante, blandit et, ultrices a, suscipit eget, quam. Integer
			ut neque. Vivamus nisi metus, molestie vel, gravida in, condimentum sit
			amet, nunc. Nam a nibh. Donec suscipit eros. Nam mi. Proin viverra leo ut
			odio. Curabitur malesuada. Vestibulum a velit eu ante scelerisque vulputate.
												</p>
											</div>
										</div>

										<div class="group">
											<h3 class="accordion-header">Section 2</h3>

											<div>
												<p>
													Sed non urna. Donec et ante. Phasellus eu ligula. Vestibulum sit amet
			purus. Vivamus hendrerit, dolor at aliquet laoreet, mauris turpis porttitor
			velit, faucibus interdum tellus libero ac justo. Vivamus non quam. In
			suscipit faucibus urna.
												</p>
											</div>
										</div>

										<div class="group">
											<h3 class="accordion-header">Section 3</h3>

											<div>
												<p>
													Nam enim risus, molestie et, porta ac, aliquam ac, risus. Quisque lobortis.
			Phasellus pellentesque purus in massa. Aenean in pede. Phasellus ac libero
			ac tellus pellentesque semper. Sed ac felis. Sed commodo, magna quis
			lacinia ornare, quam ante aliquam nisi, eu iaculis leo purus venenatis dui.
												</p>

												<ul>
													<li>List item one</li>
													<li>List item two</li>
													<li>List item three</li>
												</ul>
											</div>
										</div>
									</div><!-- #accordion -->
								</div><!-- ./span -->

								<div class="col-sm-6">
									<h3 class="header blue lighter smaller">
										<i class="ace-icon fa fa-bars smaller-90"></i>
										菜单
									</h3>

									<ul id="menu">
										<li class="ui-state-disabled">
											<a href="#">Aberdeen</a>
										</li>

										<li>
											<a href="#">Ada</a>
										</li>

										<li>
											<a href="#">Adamsville</a>
										</li>

										<li>
											<a href="#">Addyston</a>
										</li>

										<li>
											<a href="#">Delphi</a>

											<ul>
												<li class="ui-state-disabled">
													<a href="#">Ada</a>
												</li>

												<li>
													<a href="#">Saarland</a>
												</li>

												<li>
													<a href="#">Salzburg</a>
												</li>
											</ul>
										</li>

										<li>
											<a href="#">Saarland</a>
										</li>

										<li>
											<a href="#">Salzburg</a>

											<ul>
												<li>
													<a href="#">Delphi</a>

													<ul>
														<li>
															<a href="#">Ada</a>
														</li>

														<li>
															<a href="#">Saarland</a>
														</li>

														<li>
															<a href="#">Salzburg</a>
														</li>
													</ul>
												</li>

												<li>
													<a href="#">Delphi</a>

													<ul>
														<li>
															<a href="#">Ada</a>
														</li>

														<li>
															<a href="#">Saarland</a>
														</li>

														<li>
															<a href="#">Salzburg</a>
														</li>
													</ul>
												</li>

												<li>
													<a href="#">Perch</a>
												</li>
											</ul>
										</li>

										<li class="ui-state-disabled">
											<a href="#">Amesville</a>
										</li>
									</ul>
								</div><!-- ./col -->
							</div><!-- ./row -->

							<div class="space-12"></div>

							<div class="row">
								<div class="col-sm-6">
									<h3 class="header blue lighter smaller">
										<i class="ace-icon fa fa-retweet smaller-90"></i>
										微调
									</h3>

									<input id="spinner" name="value" type="text" />
								</div><!-- ./span -->

								<div class="col-sm-6">
									<h3 class="header blue lighter smaller">
										<i class="ace-icon fa fa-arrows-h smaller-90"></i>
										滑动条
									</h3>

									<p>
										更多的滑动条的例子，请参阅 “表单元素页”。
									</p>

									<div class="space-4"></div>

									<div id="slider"></div>
								</div><!-- ./col -->
							</div><!-- ./row -->

							<div class="space-12"></div>

							<div class="row">
								<div class="col-sm-6">
									<h3 class="header blue lighter smaller">
										<i class="ace-icon fa fa-terminal smaller-90"></i>
										自动匹配
									</h3>

									<div class="row">
										<div class="col-sm-8 col-md-7">
											<input id="search" type="text" class="form-control" placeholder="Type 'a' or 'h'" />
										</div>
									</div>
									<h3 class="header blue lighter smaller">
										<i class="ace-icon fa fa-spinner"></i>
										进度条
									</h3>

									<div id="progressbar"></div>
								</div><!-- ./col -->
								
								<div class="col-sm-6">
									<h3 class="header blue lighter smaller">
										<i class="ace-icon fa fa-folder-o smaller-90"></i>
										标签式的导航菜单
									</h3>

									<div id="tabs">
										<ul>
											<li>
												<a href="#tabs-1">Nunc</a>
											</li>

											<li>
												<a href="#tabs-2">Proin</a>
											</li>

											<li>
												<a href="#tabs-3">Aenean</a>
											</li>
										</ul>

										<div id="tabs-1">
											<p>Proin elit arcu, rutrum commodo, vehicula tempus, commodo a, risus. Curabitur nec arcu. Donec sollicitudin mi sit amet mauris. Nam elementum quam ullamcorper ante. Duis orci. Aliquam sodales tortor vitae ipsum. Ut et mauris vel pede varius sollicitudin.</p>
										</div>

										<div id="tabs-2">
											<p>Morbi tincidunt, dui sit amet facilisis feugiat, odio metus gravida ante, ut pharetra massa metus id nunc. Duis scelerisque molestie turpis. Morbi facilisis. Curabitur ornare consequat nunc. Aenean vel metus. Ut posuere viverra nulla..</p>
										</div>

										<div id="tabs-3">
											<p>Mauris eleifend est et turpis. Duis id erat. Suspendisse potenti. Aliquam vulputate, pede vel vehicula accumsan, mi neque rutrum erat, eu congue orci lorem eget lorem. Praesent eu risus hendrerit ligula tempus pretium.</p>
										</div>
									</div>
								</div><!-- ./col -->
							</div><!-- ./row -->

							<!-- PAGE CONTENT ENDS -->
						</div><!-- /.col -->
					</div><!-- /.row -->
				</div><!-- /.page-content -->
			

		<!-- inline scripts related to this page -->
		<script type="text/javascript">
			jQuery(function($) {
				$( "#datepicker" ).datepicker({
					autoclose : true,
					todayHighlight : true,
					showOtherMonths: true,
					selectOtherMonths: false,
					changeMonth: true,
					changeYear: true,
					//isRTL:true,
			
					showButtonPanel: true,
					beforeShow: function() {
						//change button colors
						var datepicker = $(this).datepicker( "widget" );
						setTimeout(function(){
							var buttons = datepicker.find('.ui-datepicker-buttonpane')
							.find('button');
							buttons.eq(0).addClass('btn btn-xs');
							buttons.eq(1).addClass('btn btn-xs btn-success');
							buttons.wrapInner('<span class="bigger-110" />');
						}, 0);
					}
			
				});
			
			
				//override dialog's title function to allow for HTML titles
				$.widget("ui.dialog", $.extend({}, $.ui.dialog.prototype, {
					_title: function(title) {
						var $title = this.options.title || '&nbsp;';
						if( ("title_html" in this.options) && this.options.title_html == true )
							title.html($title);
						else title.text($title);
					}
				}));
			
				$( "#id-btn-dialog1" ).on('click', function(e) {
					e.preventDefault();
			
					var dialog = $( "#dialog-message" ).removeClass('hide').dialog({
						modal: true,
						title: "<div class='widget-header widget-header-small'><h4 class='smaller'><i class='ace-icon fa fa-check'></i> jQuery UI Dialog</h4></div>",
						title_html: true,
						buttons: [ 
							{
								text: "Cancel",
								"class" : "btn btn-xs",
								click: function() {
									$( this ).dialog( "close" ); 
								} 
							},
							{
								text: "OK",
								"class" : "btn btn-primary btn-xs",
								click: function() {
									$( this ).dialog( "close" ); 
								} 
							}
						]
					});
			
					/**
					dialog.data( "uiDialog" )._title = function(title) {
						title.html( this.options.title );
					};
					**/
				});
			
			
				$( "#id-btn-dialog2" ).on('click', function(e) {
					e.preventDefault();
				
					$( "#dialog-confirm" ).removeClass('hide').dialog({
						resizable: false,
						modal: true,
						title: "<div class='widget-header'><h4 class='smaller'><i class='ace-icon fa fa-exclamation-triangle red'></i> Empty the recycle bin?</h4></div>",
						title_html: true,
						buttons: [
							{
								html: "<i class='ace-icon fa fa-trash-o bigger-110'></i>&nbsp; Delete all items",
								"class" : "btn btn-danger btn-xs",
								click: function() {
									$( this ).dialog( "close" );
								}
							}
							,
							{
								html: "<i class='ace-icon fa fa-times bigger-110'></i>&nbsp; Cancel",
								"class" : "btn btn-xs",
								click: function() {
									$( this ).dialog( "close" );
								}
							}
						]
					});
				});
			
				//custom autocomplete (category selection)
				$.widget( "custom.catcomplete", $.ui.autocomplete, {
					_renderMenu: function( ul, items ) {
						var that = this,
						currentCategory = "";
						$.each( items, function( index, item ) {
							if ( item.category != currentCategory ) {
								ul.append( "<li class='ui-autocomplete-category'>" + item.category + "</li>" );
								currentCategory = item.category;
							}
							that._renderItemData( ul, item );
						});
					}
				});
				
				 var data = [
					{ label: "anders", category: "" },
					{ label: "andreas", category: "" },
					{ label: "antal", category: "" },
					{ label: "annhhx10", category: "Products" },
					{ label: "annk K12", category: "Products" },
					{ label: "annttop C13", category: "Products" },
					{ label: "anders andersson", category: "People" },
					{ label: "andreas andersson", category: "People" },
					{ label: "andreas johnson", category: "People" }
				];
				$( "#search" ).catcomplete({
					delay: 0,
					source: data
				});
				
			
				//Menu
				$( "#menu" ).menu();
			
			
				//spinner
				var spinner = $( "#spinner" ).spinner({
					create: function( event, ui ) {
						//add custom classes and icons
						$(this)
						.next().addClass('btn btn-success').html('<i class="ace-icon fa fa-plus"></i>')
						.next().addClass('btn btn-danger').html('<i class="ace-icon fa fa-minus"></i>')
						
						//larger buttons on touch devices
						if('touchstart' in document.documentElement) 
							$(this).closest('.ui-spinner').addClass('ui-spinner-touch');
					}
				});
			
				//slider example
				$( "#slider" ).slider({
					range: true,
					min: 0,
					max: 500,
					values: [ 75, 300 ]
				});
			
			
			
				//jquery accordion
				$( "#accordion" ).accordion({
					collapsible: true ,
					heightStyle: "content",
					animate: 250,
					header: ".accordion-header"
				}).sortable({
					axis: "y",
					handle: ".accordion-header",
					stop: function( event, ui ) {
						// IE doesn't register the blur when sorting
						// so trigger focusout handlers to remove .ui-state-focus
						ui.item.children( ".accordion-header" ).triggerHandler( "focusout" );
					}
				});
				//jquery tabs
				$( "#tabs" ).tabs();
				
				
				//progressbar
				$( "#progressbar" ).progressbar({
					value: 37,
					create: function( event, ui ) {
						$(this).addClass('progress progress-striped active')
							   .children(0).addClass('progress-bar progress-bar-success');
					}
				});
					
			});
		</script>
	</body>
</html>
