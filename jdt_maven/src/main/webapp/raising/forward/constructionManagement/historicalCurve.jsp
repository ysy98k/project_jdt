<%--
  Created by IntelliJ IDEA.
  User: ysy
  Date: 2018/8/8
  Time: 11:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.baosight.aas.auth.Constants" %>
<html>
<head>
    <title>历史曲线</title>
    <%
        String path = request.getContextPath();
        String tenantid = request.getSession().getAttribute(Constants.SESSION_TENANTID_KEY) + "";
    %>

    <meta name="description" content="platForm Frameset"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <style>
        .bai{
            background-color:#FFFFFF;
        }
        #father{
            background: -webkit-linear-gradient(#105899,#3C84C5, #105899); /* Safari 5.1 - 6.0 */
            background: -o-linear-gradient(#105899,#3C84C5, #105899); /* Opera 11.1 - 12.0 */
            background: -moz-linear-gradient(#105899,#3C84C5, #105899); /* Firefox 3.6 - 15 */
            background: linear-gradient(#105899,#3C84C5, #105899);!important; /* 标准的语法 */
            color:white!important;
            min-height:600px;
            height: 100%;
            position:inherit;
        }
        /*头部*/
        .page-header{
            background-color: white;
            margin: -8px -20px 0px!important;
            padding:8px 20px 9px 20px;
        }
        .page-header h5,.page-header h5 small,.page-header div a{
            color:white!important;
        }
        /*头部*/

        .row{
            margin:0px!important;
        }
        .prompt{
            display: inline-block;
            font-size: 12px;
            //font-style: italic;
            color: white;
        }
        .layui-this{
            color: #FFB432!important;
        }



    </style>

</head>

<body class="bai">
<%@ include file="/bxui/bxuihead.jsp" %>
<link rel="stylesheet" href="<%=toolkitPath%>/bxui/daterangepicker/css/daterangepicker.css"/>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/moment.min.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/daterangepicker/js/daterangepicker.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/echarts3/echarts.js"></script>

<script type="text/javascript" src="<%=toolkitPath%>/bxui/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/bxwidget/bxdialog.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-require.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosight-elementui.js"></script>

<link rel="stylesheet" href="<%=toolkitPath%>/raisingui/layui/css/layui.css">
<script type="text/javascript" src="<%=toolkitPath%>/raisingui/layui/layui.js"></script>
<script src="<%=toolkitPath%>/bxui/util/jsUtil.js"></script>

<input type="hidden" value="<%=tenantid%>" id="tenantid" />
<div id="father" class="page-content" style="min-height: 800px;height: 100%;min-width: 1600px;" >
    <div class="page-header">
        <h5 style="color: rgb(38, 121, 181)!important;">
            施工管理管理
            <small style="color:rgb(153, 153, 153)!important;">
                &nbsp;
                <i class="ace-icon fa fa-angle-double-right"></i>
                &nbsp;&nbsp;
                历史曲线
            </small>
        </h5>
        <div style="float: right;margin-top: -15px;">
            <a href="javascript:insertPage()" style="color: rgb(38, 121, 181)!important;" ><span id = "cityname"></span><span id="linemessage"></span><span id="programmessage"></span></a>
        </div>
        <%--<div style="float: right;margin-top: -20px">
            <a href="javascript:insertPage()" ><span id = "cityname"></span><span id="linemessage"></span><span id="programmessage"></span></a>
        </div>--%>
    </div>

    <!-- 内容-->
    <div class="row">

        <!--功能模块-->
        <div class="layui-tab" >
            <ul class="layui-tab-title">
                <li class="">土压</li>
                <li class="layui-this">刀盘</li>
                <li class="">主驱动电机</li>
                <li class="">推进</li>
                <li class="">螺旋输送机</li>
                <li class="">铰接</li>
                <li class="">盾尾密封</li>
                <li class="">注浆</li>
                <li class="">渣土改良</li>
                <li class="">工业水系统</li>
                <li class="">液压站</li>
                <li class="">压缩空气</li>
                <li class="">泡沫压力</li>
            </ul>

        </div>

        <div class="row">
            <div class="col-xs-12 col-sm-6 col-md-4">

            </div>
            <div class="col-xs-12 col-sm-6 col-md-2"></div>
            <div class="col-xs-12 col-sm-6 col-md-3">
                <div class="prompt"  style="display: inline-block;margin-left: 60px;" id="minMileage">0.0</div>
                <div class="prompt"  style="display: inline-block;margin-left: 30px;" id="maxMileage">0.0</div>
            </div>
        </div>
        <!-- 查询条件-->
        <div class="row" >

            <div class="col-xs-12 col-sm-6 col-md-4">
                <div>
                    <label class="labelThree" for="inqu_status-startTime" style="margin-right: 20px;">日期</label>
                    <input type="text" id="inqu_status-startTime" data-bxwidget class="inputThree" onkeyup="timeKeyUp(this)"/>
                    <span></span>
                    <label class="labelThree di">至</label>
                    <input type="text" id="inqu_status-endTime" data-bxwidget class="inputThree" onkeyup="timeKeyUp(this)"/>
                    <span></span>
                </div>
            </div>
            <div class="col-xs-12 col-sm-6 col-md-2">
                <div>
                    <label class="labelThree" for="inqu_status-startTime" style="margin-right: 20px;" >环号</label>
                    <select name="ringNum" onchange="ringNumKeyChange(this)">
                        <option value="-1" selected>未选择</option>
                    </select>
                <%--<input type="text" id="inqu_status-startRingNum" data-bxwidget class="inputThree" style="width: 100px;"/>
                    <label class="labelThree di">至</label>
                    <input type="text" id="inqu_status-endRingNum" data-bxwidget class="inputThree" style="width: 100px;"/>--%>
                </div>
            </div>
            <div class="col-xs-12 col-sm-6 col-md-3">
                <div>
                    <label class="labelThree" for="inqu_status-startTime" style="margin-right: 20px;">里程</label>
                    <input type="text" id="inqu_status-startMileage" data-bxwidget class="inputThree" style="width: 100px;" onkeyup="mileageKeyUp(this)"/>
                    <label class="labelThree di"  >至</label>
                    <input type="text" id="inqu_status-endMileage" data-bxwidget class="inputThree" style="width: 100px;" onkeyup="mileageKeyUp(this)"/>
                    <span></span>
                </div>
            </div>
            <div class="col-xs-12 col-sm-6 col-md-1">
                <div class="col-xs-12 col-sm-12 col-md-12  no-padding-left-1024" >
                    <button class="btn btn-sm btn-block" onclick="on_query_click(this);" style="background-color: #48C275!important;border-color: #48C275!important;">
                        <div class="ace-icon fa fa-search"></div>
                        <span>查询</span>
                    </button>
                </div>
            </div>
        </div>

        <div class="col-xs-12 col-sm-12 col-md-12" style="margin-top:50px;">
            <div id="myCharts" style="min-height:600px;width: 1300px;"></div>
        </div>

    </div>
</div>

<!------------------------------------信息弹出框---------------------------------------------------------->
<div id="updateConfirm" class="hide">
    <span>确认修改么？</span>
</div>
<div class="alertDiv hide"></div>



<div id="detail" class="hide" style="overflow: hidden">
    <div type="text" id = "div_city" name = "city" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
    <div type="text" id = "div_line" name = "line" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
    <div type="text" id = "div_program" name = "program" data-bxwidget="bxcombobox" data-bxtype="string" style="margin:15px 10px 15px 10px">
    </div>
</div>

<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/selectLine.js"></script>
<script type="text/javascript" src="<%=toolkitPath%>/raising/forward/constructionManagement/historicalCurve.js"></script>
<script>


    layui.use(['form', 'layedit', 'laydate','element'], function(){
        var $ = layui.jquery
            ,element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
        var form = layui.form
            ,layer = layui.layer
            ,layedit = layui.layedit
            ,laydate = layui.laydate;

        //触发事件
        var active = {
            tabAdd: function(){
                //新增一个Tab项
                element.tabAdd('demo', {
                    title: '新选项'+ (Math.random()*1000|0) //用于演示
                    ,content: '内容'+ (Math.random()*1000|0)
                    ,id: new Date().getTime() //实际使用一般是规定好的id，这里以时间戳模拟下
                })
            }
            ,tabDelete: function(othis){
                //删除指定Tab项
                element.tabDelete('demo', '44'); //删除：“商品管理”


                othis.addClass('layui-btn-disabled');
            }
            ,tabChange: function(){
                //切换到指定Tab项
                element.tabChange('demo', '22'); //切换到：用户管理
            }
        };

        $('.site-demo-active').on('click', function(){
            var othis = $(this), type = othis.data('type');
            active[type] ? active[type].call(this, othis) : '';
        });

        //Hash地址的定位
        var layid = location.hash.replace(/^#test=/, '');
        element.tabChange('test', layid);

        element.on('tab(test)', function(elem){
            location.hash = 'test='+ $(this).attr('lay-id');
        });

        //日期
        laydate.render({
            elem: '#date'
        });
        laydate.render({
            elem: '#date1'
        });

        //创建一个编辑器
        var editIndex = layedit.build('LAY_demo_editor');

        //自定义验证规则
        form.verify({
            title: function(value){
                if(value.length < 5){
                    return '标题至少得5个字符啊';
                }
            }
            ,pass: [
                /^[\S]{6,12}$/
                ,'密码必须6到12位，且不能出现空格'
            ]
            ,content: function(value){
                layedit.sync(editIndex);
            }
        });

        //监听指定开关
        form.on('switch(switchTest)', function(data){
            layer.msg('开关checked：'+ (this.checked ? 'true' : 'false'), {
                offset: '6px'
            });
            layer.tips('温馨提示：请注意开关状态的文字可以随意定义，而不仅仅是ON|OFF', data.othis)
        });

        //监听提交
        form.on('submit(demo1)', function(data){
            layer.alert(JSON.stringify(data.field), {
                title: '最终的提交信息'
            })
            return false;
        });

        //表单初始赋值
        form.val('example', {
            "username": "贤心" // "name": "value"
            ,"password": "123456"
            ,"interest": 1
            ,"like[write]": true //复选框选中状态
            ,"close": true //开关状态
            ,"sex": "女"
            ,"desc": "我爱 layui"
        })
    });
</script>

</body>
</html>
