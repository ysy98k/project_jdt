<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="com.baosight.aas.auth.Constants"%>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>系统设置</title>
<%
	String path = request.getContextPath();
	String tenantid=request.getSession().getAttribute(Constants.SESSION_TENANTID_KEY) + "";
%>

<meta name="description" content="platForm Frameset" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
</head>

<body>
    <%@ include file="/bxui/bxuihead.jsp"%>
	<script type="text/javascript" src="<%=path%>/bxui/baosight-require.js"></script>
	<input type="hidden" value="<%=tenantid%>" id="tenantid">
    <link rel="stylesheet" href="<%=toolkitPath%>/bxui/other/css/style-frame-inner.css" />
		<div class="page-content" style="position:inherit">
					<!-- /sectionController:settings.box -->
					<div class="page-header">
						<h1>
							系统设置
							<small>
								<i class="ace-icon fa fa-angle-double-right"></i>
								设置开发平台系统的整体参数
							</small>
						</h1>
                        <button class="btn btn-sm pull-right btn-block" style="width: 100px;margin-top: -25px" onclick="saveCfg();">
                            <div class="ace-icon fa fa-refresh"></div>
                            <span>保存设置</span>
                        </button>
					</div><!-- /.page-header -->

					<div class="row row-pos">
						<div class="col-xs-12">
							<!-- PAGE CONTENT BEGINS -->
							<div class="row">
                                <div class="infoText col-sm-4 textPos">
                                    <span class="menu-icon fa fa-eye iconDark"></span>&nbsp;&nbsp;皮肤设置
								</div></br></br>
                                <div class="infoText col-sm-4 textSubPos">
                                    <div class="ace-settings-item">
                                        <div class="pull-left">
                                            <select id="skin-colorpicker" class="hide bxskin">
                                                <option data-skin="blue-skin" data-skinid="21" value="#438EB9">#438EB9</option>
                                                <option data-skin="green-skin" data-skinid="22" value="#82AF6F">#82AF6F</option>
                                                <option data-skin="black-skin" data-skinid="23" value="#404040">#404040</option>
                                                <option data-skin="pink-skin" data-skinid="24" value="#CE6F9E">#CE6F9E</option>
                                                <option data-skin="darkgreen-skin" data-skinid="25" value="#0d7f39">#0d7f39</option>
                                            </select>
                                        </div>
                                        <span>&nbsp; 选择皮肤</span>
                                    </div>
								</div>
							</div><!-- /.row -->
							<div class="space-12"></div>
						</div><!-- /.col -->
					</div><!-- /.row -->

                    <div class="row row-pos">
                        <div class="col-xs-12">
                            <!-- PAGE CONTENT BEGINS -->
                            <div class="row">
                                <div class="infoText col-sm-12 textPos">
                                    <span class="menu-icon fa fa-home iconDark"></span>&nbsp;&nbsp;首页设置
                                </div></br></br>
                                <div class="infoText col-sm-6 textSubPos">
                                    <label for="homePage" class="pull-left" style="margin-top: 6px">左侧菜单首页代码</label>
                                    <div type="text" id="homePage" name="homePage" data-bxwidget="bxcombobox" data-bxtype="string"  style="width:215px;float: left;padding-left: 15px"></div>
                                </div>
                                <div class="infoText col-sm-6 textSubPos">
                                    <label for="frontHomePage" class="pull-left" style="margin-top: 6px">上侧菜单首页代码</label>
                                    <div type="text" id="frontHomePage" name="frontHomePage" data-bxwidget="bxcombobox" data-bxtype="string" style="width:215px;float: left;padding-left: 15px"></div>
                                </div>
                            </div><!-- /.row -->
                            <div class="space-12"></div>
                        </div><!-- /.col -->
                    </div><!-- /.row -->

                    <div class="row row-pos">
                        <div class="col-xs-12">
                            <!-- PAGE CONTENT BEGINS -->
                            <div class="row">
                                <div class="infoText col-sm-12 col-xs-12 textPos">
                                    <span class="menu-icon fa fa-picture-o iconDark"></span>
                                    &nbsp;&nbsp;logo设置
                                </div></br></br>
                                <div class="infoText col-sm-6 col-xs-12 textSubPos" style="min-height: 170px">
                                    <div class="col-sm-3 col-xs-12"  style="width: 110px">
                                        <div class="row">左侧菜单logo&nbsp;&nbsp;</div>
                                        <div class="row"><font size="1" color="#8089a0">(建议最佳高度20px)</font></div>
                                    </div>
                                    <div class="infoText col-sm-8 col-xs-12">
                                        <span class="profile-picture">
                                        <img id="logoIcon" class="editable img-responsive" src="" />
                                        </span>
                                        <input id="logoText" type="text" style="vertical-align: top;height: 37px;">
                                    </div>
                                </div>
                                <div class="infoText col-sm-6 col-xs-12 textSubPos"  style="min-height: 240px">
                                    <div class="col-sm-3 col-xs-12" style="width: 110px">
                                        <div class="row">上侧菜单logo&nbsp;&nbsp;</div>
                                        <div class="row"><font size="1" color="#8089a0">(建议最佳高度45px)</font></div>
                                    </div>
                                    <div class="col-sm-8 col-xs-12">
                                        <span class="profile-picture">
                                        <img id="bigLogoIcon" class="editable img-responsive" src="" />
                                        </span>
                                    </div>
                                </div>
                            </div><!-- /.row -->
                        </div><!-- /.col -->
                    </div><!-- /.row -->

				</div><!-- /.page-content -->
	
<!-- ----------------------------------信息弹出框---------------------------------------------------------->	
	<div id="dialog-message" class="hide">
		<p class="bigger-110 bolder center grey">
			<br/>
			<b id="dialogInfo"></b>
		</p>
	</div>
</body>
<script type="text/javascript">
$(document).ready(function(){
	var callback = {
			onSuccess : function(paramJsonObj) {
				if(typeof(paramJsonObj)!="undefined"){
					$("#skin-colorpicker").find("option[data-skin='"+paramJsonObj.skinName+"']").prop("selected","selected");
                    $('#skin-colorpicker').change();
                    $(".selected").removeClass("selected");
					$(".colorpick-btn").each(function(){
						if($(this).data().color==paramJsonObj.skinDesc){
							$(this).addClass("selected");
						}
					});
                    /*setTimeout(function(){
                        $(".btn-colorpicker").css("background-color",paramJsonObj.skinDesc);
                    },4000);*/
                    $("#logoIcon").attr("src",paramJsonObj.logoIcon);
                    $("#bigLogoIcon").attr("src",paramJsonObj.bigLogoIcon);
                    $("#logoText").val(paramJsonObj.logoText);



                    baosightRequire.requireFunct(['bxcombobox'], function() {
                        $("#homePage").bxcombobox({
                            dataPattern: 'local',
                            data : paramJsonObj.pageLst,
                            async : false,
                            select : function(event, ui) {
                            }
                        });
                        $("#homePage_select option[value='"+paramJsonObj.homepage+"']").attr("selected","selected");

                        $("#frontHomePage").bxcombobox({
                            dataPattern: 'local',
                            data : paramJsonObj.pageLst,
                            async : false,
                            select : function(event, ui) {
                            }
                        });
                        $("#frontHomePage_select option[value='"+paramJsonObj.frontHomePage+"']").attr("selected","selected");
                    });
				}
			}
		};
		AjaxCommunicator.ajaxRequest(
						'/df/metamanage/frameSetting.do?method=queryFrameSetting',
						'POST', {}, callback);

});

var skinName;
$('#skin-colorpicker').on('change', function(){
    skinName = $('.bxskin').find('option:selected').data('skin');
    var body = window.parent.document.body;
	window.document.body.className=skinName;
    if(!$('body', parent.document).hasClass('edit')){
        body.className=skinName;
    }
 });
    //editables on first profile page
    $.fn.editable.defaults.mode = 'inline';
    $.fn.editableform.loading = "<div class='editableform-loading'><i class='ace-icon fa fa-spinner fa-spin fa-2x light-blue'></i></div>";
    $.fn.editableform.buttons = '<button type="submit" class="btn btn-info editable-submit"><i class="ace-icon fa fa-check"></i></button>'+
            '<button type="button" class="btn editable-cancel"><i class="ace-icon fa fa-times"></i></button>';

    //editables


    // *** editable avatar *** //
    try {//ie8 throws some harmless exceptions, so let's catch'em

        //first let's add a fake appendChild method for Image element for browsers that have a problem with this
        //because editable plugin calls appendChild, and it causes errors on IE at unpredicted points
        try {
            document.createElement('IMG').appendChild(document.createElement('B'));
        } catch(e) {
            Image.prototype.appendChild = function(el){}
        }

        var last_gritter
        $('#logoIcon').editable({
            type: 'image',
            name: 'logoIcon',
            value: null,
            image: {
                //specify ace file input plugin's options here
                btn_choose: '选择logo',
                droppable: true,
                maxSize: 110000,//~100Kb

                //and a few extra ones here
                name: 'logoIcon',//put the field name here as well, will be used inside the custom plugin
                on_error : function(error_type) {//on_error function will be called when the selected file has a problem
                    if(last_gritter) $.gritter.remove(last_gritter);
                    if(error_type == 1) {//file format error
                        last_gritter = $.gritter.add({
                            title: '文件不是一个图像!',
                            text: '请选择一个jpg|gif|png的图像!',
                            class_name: 'gritter-error gritter-center'
                        });
                    } else if(error_type == 2) {//file size rror
                        last_gritter = $.gritter.add({
                            title: '文件太大!',
                            text: '文件大小不可超过100Kb!',
                            class_name: 'gritter-error gritter-center'
                        });
                    }
                    else {//other error
                    }
                },
                on_success : function() {
                    $.gritter.removeAll();
                }
            },
            url: function(params) {
               // ***UPDATE AVATAR HERE*** //
                //for a working upload example you can replace the contents of this function with
                //examples/profile-avatar-update.js
                var deferred = new $.Deferred
                var value = $('#logoIcon').next().find('input[type=hidden]:eq(0)').val();
                if(!value || value.length == 0) {
                    deferred.resolve();
                    return deferred.promise();
                }
                //dummy upload
                setTimeout(function(){
                    if("FileReader" in window) {
                        //for browsers that have a thumbnail of selected image
                        var thumb = $('#logoIcon').next().find('img').data('thumb');
                        if(thumb) $('#logoIcon').get(0).src = thumb;
                    }
                    deferred.resolve({'status':'OK'});
                } , parseInt(Math.random() * 800 + 800))
                return deferred.promise();
                // ***END OF UPDATE AVATAR HERE*** //
            },
            success: function(response, newValue) {
            }
        })
        $('#bigLogoIcon').editable({
            type: 'image',
            name: 'bigLogoIcon',
            value: null,
            image: {
                btn_choose: '选择logo',
                droppable: true,
                maxSize: 110000,//~100Kb

                name: 'bigLogoIcon',
                on_error : function(error_type) {
                    if(last_gritter) $.gritter.remove(last_gritter);
                    if(error_type == 1) {
                        last_gritter = $.gritter.add({
                            title: '文件不是一个图像!',
                            text: '请选择一个jpg|gif|png的图像!',
                            class_name: 'gritter-error gritter-center'
                        });
                    } else if(error_type == 2) {//file size rror
                        last_gritter = $.gritter.add({
                            title: '文件太大!',
                            text: '文件大小不可超过100Kb!',
                            class_name: 'gritter-error gritter-center'
                        });
                    }
                    else {
                    }
                },
                on_success : function() {
                    $.gritter.removeAll();
                }
            },
            url: function(params) {
                var deferred = new $.Deferred
                var value = $('#bigLogoIcon').next().find('input[type=hidden]:eq(0)').val();
                if(!value || value.length == 0) {
                    deferred.resolve();
                    return deferred.promise();
                }
                //dummy upload
                setTimeout(function(){
                    if("FileReader" in window) {
                        //for browsers that have a thumbnail of selected image
                        var thumb = $('#bigLogoIcon').next().find('img').data('thumb');
                        if(thumb) $('#bigLogoIcon').get(0).src = thumb;
                    }
                    deferred.resolve({'status':'OK'});
                } , parseInt(Math.random() * 800 + 800))
                return deferred.promise();
                // ***END OF UPDATE AVATAR HERE*** //
            },
            success: function(response, newValue) {
            }
        });
    }catch(e) {}
function saveCfg(){
    var paramJsonObj = new Object();
    paramJsonObj={
        "skinName":skinName,
        "logoIcon":$("#logoIcon")[0].src,
        "bigLogoIcon":$("#bigLogoIcon")[0].src,
        "logoText":$("#logoText").val(),
        "homepage":$("#homePage").bxcombobox("selectObj").val(),
        "frontHomePage":$("#frontHomePage").bxcombobox("selectObj").val()
    };
    var callback = {
        onSuccess : function(paramJsonObj) {
            if(last_gritter) $.gritter.remove(last_gritter);
            if(paramJsonObj.status==0){
                last_gritter = $.gritter.add({
                    title: paramJsonObj.returnMsg,
                    text: '刷新网站，应用效果',
                    class_name: 'gritter-success gritter-right'
                });
            }else{
                last_gritter = $.gritter.add({
                    title: '操作失败！',
                    text: paramJsonObj.returnMsg,
                    class_name: 'gritter-error gritter-right'
                });
            }
        }
    };
    AjaxCommunicator.ajaxRequest(
            '/df/metamanage/frameSetting.do?method=updateFrameSetting',
            'POST', paramJsonObj, callback);
}
</script>

</html>
