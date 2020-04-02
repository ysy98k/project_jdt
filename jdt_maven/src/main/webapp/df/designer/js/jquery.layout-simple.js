$(document).ready(function () {
	initLayout();
});

function initLayout(){
	var myLayout =$("body").layout(
		{
			applyDefaultStyles: true,//应用默认样式
			scrollToBookmarkOnLoad:false,//页加载时滚动到标签
			showOverflowOnHover:false,//鼠标移过显示被隐藏的，只在禁用滚动条时用
			/*north__closable:true,//可以被关闭
			north__resizable:true,//可以改变大小*/
            east:{
                togglerContent_closed:'<font size="1">属性</br>-</br>数据绑定</font>',
                togglerContent_open:'<div class="ace-icon fa fa-caret-right"></div>',
                spacing_closed:13//关闭时边框的间隙
            },
            west:{
                togglerContent_closed:'<font size="1">布局</br>-</br>组件</br>-</br>控件</font>',
                togglerContent_open:'<div class="ace-icon fa fa-caret-left"></div>',
                spacing_closed:13//关闭时边框的间隙              
            },
            north:{
                togglerContent_closed:'<div class="ace-icon fa fa-caret-down" style="margin-top: -3px;vertical-align: top"></div>',
                togglerContent_open:'<div class="ace-icon fa fa-caret-up" style="margin-top: -3px;vertical-align: top"></div>',
                spacing_closed:6//关闭时边框的间隙
            },
            east__childOptions:	{
                togglerContent_closed:'<div class="ace-icon fa fa-caret-up" style="margin-top: -3px;vertical-align: top"></div>',
                togglerContent_open:'<div class="ace-icon fa fa-caret-down" style="margin-top: -3px;vertical-align: top"></div>',
                minSize:50,	// ALL panes
                south__size:200
            },
            east__size: 245,
            center__onresize:$.layout.callbacks.resizeTabLayout,
            north__size:45,//pane的大小
			spacing_open:6,//边框的间隙
            resizable:false,//可以改变大小
            /*north__spacing_open:0,*/
			/*resizerTip:"可调整大小",//鼠标移到边框时，提示语*/
			//resizerCursor:"resize-p", //鼠标移上的指针样式
			resizerDragOpacity:0.5,//调整大小边框移动时的透明度
			//maskIframesOnResize:"#ifa",//在改变大小的时候，标记iframe（未通过测试）

			sliderTip:"显示侧边栏",//在某个Pane隐藏后，当鼠标移到边框上显示的提示语。
			sliderCursor:"pointer",//在某个Pane隐藏后，当鼠标移到边框上时的指针样式。
			slideTrigger_open:"dblclick",//在某个Pane隐藏后，鼠标触发其显示的事件。(click", "dblclick", "mouseover)
			slideTrigger_close:"click",//在某个Pane隐藏后，鼠标触发其关闭的事件。("click", "mouseout")

			togglerTip_open:"关闭",//pane打开时，当鼠标移动到边框上按钮上，显示的提示语
			togglerTip_closed:"打开",//pane关闭时，当鼠标移动到边框上按钮上，显示的提示语
			togglerLength_open:100,//pane打开时，边框按钮的长度
			togglerLength_closed:200,//pane关闭时，边框按钮的长度

			hideTogglerOnSlide:true,//在边框上隐藏打开/关闭按钮(测试未通过)

			togglerAlign_open:"center",//pane打开时，边框按钮显示的位置
			togglerAlign_closed:"center",//pane关闭时，边框按钮显示的位置
			//togglerContent_open:"<div style='background:red'>AAA</div>",//pane打开时，边框按钮中需要显示的内容可以是符号"<"等。需要加入默认css样式.ui-layout-toggler .content
			//togglerContent_closed:"<img/>",//pane关闭时，同上。
			enableCursorHotkey:true,//启用快捷键CTRL或shift + 上下左右。
			customHotkeyModifier:"shift",//自定义快捷键控制键("CTRL", "SHIFT", "CTRL+SHIFT"),不能使用alt
			fxName:"drop",//打开关闭的动画效果
			fxSpeed:"slow",//动画速度
            onresize : function(){
				for (var id in widgetsUtils.getBxgridList()) {
                    $("#"+id).bxgrid("rawMethodCall","setGridWidth",$("#"+id).parent().parent().width());
				}
                for (var id in widgetsUtils.getBxchartList()) {
                    widgetsUtils.getControllerList()[id].refresh();
                }
            }
			//initClosed:true,//初始时，所有pane关闭
			//initHidden:false, //初始时，所有pane隐藏


			/*其他回调函数

			 显示时调用
			 onshow = ""
			 onshow_start = ""
			 onshow_end = ""
			 隐藏时调用
			 onhide = ""
			 onhide_start = ""
			 onhide_end = ""
			 打开时调用
			 onopen = ""
			 onopen_start = ""
			 onopen_end = ""
			 关闭时调用
			 onclose = ""
			 onclose_start = ""
			 onclose_end = ""
			 改变大小时调用
			 onresize = ons
			 onresize_start = start
			 onresize_end = end
			 */
		});
}

