<%@ page import="com.baosight.aas.auth.utils.LicenseDateException" %>
<%@ page import="com.baosight.aas.auth.utils.LicenseException" %>
<%@ page import="com.baosight.aas.auth.utils.LicenseMacException" %>
<%@ page import="com.baosight.aas.auth.utils.UserExpireException" %>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException" %>
<%@ page import="org.apache.shiro.authc.UnknownAccountException" %>
<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>

    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>力信盾构大数据平台</title>
    <%
        String path = request.getContextPath();
    %>
    <meta name="description" content="platForm Frameset"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <link rel="stylesheet" href="<%=path%>/bxui/aceadmin/style/css/font-awesome.min.css">
    <script type="text/javascript">
        if (window.parent != window) {
            window.parent.location.reload();
        }
    </script>

</head>

<body class="blue-skin">
<%@ include file="/raising/raisinguihead.jsp" %>


<style>
    .row{
        position:absolute; width:100%;height:100%;
        margin:0px;
    }
    #back{
        position:absolute; width:100%;height:100%;
    }
    #loginTitle{
        position:absolute;
        left:50%;margin-left:-190px;
        top:80px;
        min-height: 100px;
        width:380px;
        background: url('./raising/image/login/u1889.png') no-repeat;
    }
    #tunnel{
        height: 80%;
        text-align: center;
        top:100px;
    }
    #formDiv{
        top:150px;
        color: #25A7F0;
        height: 425px;
    }
    form i{
        color: #0099FF;
        font-size: 28px!important;
        margin-top:4px;
    }
    .inputDiv{
        width:331px;
        margin-bottom: 35px;
        position: relative;
    }
    .inputName{
        display: inline-block;
        width:105px;
        font-size: 20px;
        height: 42px;
        border:1px solid #25A7F0;
        border-right-width:0px ;
    }
    .inputForm{
        display: inline-block;
        padding: 0px;
        margin: 0px;
        position: absolute;
        top:3px;

    }
    .mts_input{
        width:222px;
        height: 42px;
        position:relative;
        top:-3px;
        /*left:1px;*/
        border-right-width: 0px;
        padding:1px 0px!important;
        border: 2px solid rgb(213, 213, 213)!important;
        border-right-width:0px!important;
    }
    .forget{
        display: none;
    }
</style>

<div id="base" class="row" >
    <div id="back">
        <img src="<%=path%>/raising/image/login/u1886.png" style="width: 100%;height: 100%;" >
    </div>

    <div id="loginTitle" ></div>

    <div class="row" style="clear: both;">
        <div class="col-xs-6" id="tunnel" >
            <img src="<%=path%>/raising/image/login/u1887.png" style="width: 100%;height: 95%;max-width: 780px;max-height: 758px;" >
        </div>
        <!--表单-->
        <div id="formDiv" class="col-xs-6" >
            <div class="col-xs-12" style="margin: 0px auto;" style="height:92px;">
                <img src="<%=path%>/raising/image/login/u1890.png">
            </div>
            <%--登录页面--%>
            <form id="loginForm" action="<%=path%>/loginCheck.do" method="post" style="display: block;clear: both;">
                <div style="margin: 0 auto;max-width: 450px;height:350px;border:1px solid #25A7F0;padding:60px 60px;">
                    <div class="inputDiv" >
                        <div class="inputName">
                            <i class="fa fa-user-o" aria-hidden="true" ></i>
                            <span id="nameSpan">用户名:</span>
                        </div>
                        <div class="inputForm">
                            <input class="mts_input" id="inputUser" name="inputUser" style="font-size: 14px;color: black;" />
                            <input id="inputUserHidden" name="inputUserHidden" type="hidden"/>
                            <div onClick="clearInfo('inputUser')" class="icon fa fa-remove"
                                 style="text-decoration:blink;cursor:pointer;height:39px;position:absolute;left:200px;top:12px;"></div>

                            <div class="loginButton forget" style="height: 42px;width: 100px;position:absolute;left:122px;top:-3px; ">
                                <a href="#" id="getCode" onClick="codeCheckSys(this);">
                                    <div class="pull-left buttonContent" id="timeRefresh" style="width:100px;height:100%;line-height:40px;color: white;text-align: center;">
                                        获取验证码
                                    </div>
                                </a>
                            </div>
                        </div>

                    </div>
                    <div class="inputDiv">
                        <div class="inputName">
                            <i class="fa fa-key" aria-hidden="true" ></i>
                            <span id="passwordSpan">密&nbsp;&nbsp; 码:</span>
                        </div>
                        <div class="inputForm">
                            <input type="password" class="mts_input" id="inputPw" name="inputPw" style="font-size: 14px;"/>
                            <div onClick="clearInfo('inputPw')" class="icon fa fa-remove"
                                 style="text-decoration:blink;cursor:pointer;height:39px;position:absolute;left:200px;top:10px;"></div>
                        </div>

                    </div>
                    <div style="color:red;visibility:hidden" id="capsTip">注意，大写键被锁定</div>

                    <div class="inputDiv" style="background-color: #1E89C5;">
                        <a href="#" id="loginButton" onClick="loginSys();" style="text-decoration:none;font-size: 20px;">
                            <div class="buttonContent"
                                 style="height: 40px;width: 130px;margin:0 auto;color: white!important;">

                                <div class="icon fa fa-sign-in" style="margin-top: 7px;position: relative;left:-20px;"></div>
                                <span id="loginFont" style="position:relative;left:20px;">登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录</span>
                            </div>
                        </a>
                    </div>
                    <div  style="float: right;">
                        <a href="javascript:;" class="login" onclick="forgetPassword(this)">忘记登录密码？</a>
                        <a href="javascript:;" class="forget" onclick="toLogin(this)">返回登录</a>
                    </div>
                </div>
            </form>
        </div>
    </div>

</div>

<div id="detail" class="hide" style="overflow:hidden;min-height: 60px!important;">
    <input type="hidden" id="detail-tid" data-bxwidget data-bxtype="number" data-bxauto />

    <div class="row rowspace" style="position: static">
        <div class="col-sm-12">
            <div class="form-group" style="height: 35px;">
                <label class="col-sm-4 control-label no-padding-right labelfont"
                       for="detail-password"> 新密码 </label>
                <div class="col-sm-8">
                    <input type="password" id="detail-password" class="col-xs-10 col-sm-10" data-bxwidget data-bxtype="string" style="border:1px solid #D5D5D5"/>
                </div>
            </div>
        </div>
    </div>

    <div class="row rowLastSpace" style="margin-top: 10px;position: static">
        <div class="col-sm-12">
            <div class="form-group">
                <label class="col-sm-4 control-label no-padding-right labelfont"
                       for="surePassword"> 确认密码 </label>
                <div class="col-sm-8">
                    <input type="password" id="surePassword" class="col-xs-10 col-sm-10" data-bxwidget data-bxtype="string" />
                </div>
            </div>
        </div>
    </div>
</div>


<script>
    function myrefresh(){
        window.location.reload();
    }
    var code = "123456";
    var phone = "";
    var token = "";
    var uname = "";
    $.ajax({
        type:"get",
        url:toolkitPath+"/df/metamanage/frameSetting.do/queryLogo.do",
        dataType:"json",
        success:function (data) {
            if(typeof(data) != "undefined" ){
                $("#bigLogoIcon").attr("src",data.bigLogoIcon);
            }
        }
    })

    function forgetPassword(obj) {
        $("#nameSpan").html("手机号:");
        $("#passwordSpan").html("验证码:");
        $("#inputUser").val("");
        $("#inputPw").val("");
        $("#loginFont").html("验&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp证");
        $("#loginButton").attr("onclick","checkingSys()");
        $(".forget").show();
        $(".login").hide();
        $(".inputName").find("i")[0].setAttribute("class","fa fa-phone");
        $(".inputName").find("i")[1].setAttribute("class","fa fa-sun-o");
    }

    function toLogin(obj){
        $("#nameSpan").html("用户名:");
        $("#passwordSpan").html("密&nbsp;&nbsp;码:");
        $("#inputUser").val("");
        $("#inputPw").val("");
        $("#loginFont").html("登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp录");
        $("#loginButton").attr("onclick","loginSys()");
        $(".forget").hide();
        $(".login").show();
        $(".inputName").find("i")[0].setAttribute("class","fa fa-user-o");
        $(".inputName").find("i")[1].setAttribute("class","fa fa-key");
    }

    var countdown=60;
    //手机号校验，验证码生成
    function codeCheckSys(val) {
        if($('#inputUser').val()==""){
            $('#inputUser').focus();
            dialogMessage("验证失败","手机不能为空");
            return false;
        }
        //正则表达式
        var reg = /(1[3-9]\d{9}$)/;
        if (!reg.test($('#inputUser').val())){
            $('#inputUser').focus();
            dialogMessage("验证失败","请输入正确格式的手机号码！");
            return false;
        }
        //给手机发送验证码信息
        if(countdown==60){
            $.ajax({
                type:"get",
                url:toolkitPath+"/rest/api/raising/message?telephone="+$('#inputUser').val(),
                dataType:"json",
                success:function (data) {
                    if (data.error) {
                        dialogMessage("验证失败", data.error);
                        countdown=0;
                    }
                    else if(data.success){
                        dialogMessage("验证成功",data.success);
                        code = data.code;
                        uname = data.username;
                        token = data.token;
                    }
                    return;
                }
            })
        }

        if (countdown == 0) {
            $("#timeRefresh").html("&nbsp;&nbsp;获取验证码");
            // val.inerText="免费获取验证码";
            $("#getCode").attr("href","#");
            $("#getCode").attr("onclick","codeCheckSys(this);");
            countdown = 60;
            return;
        } else {
            $("#timeRefresh").text("重新发送(" + countdown+"S" + ")");

            $("#getCode").removeAttrs('href');
            $("#getCode").removeAttrs('onclick');
            // val.inerText="重新发送(" + countdown + ")";
            countdown--;
        }
        setTimeout(function() {
            codeCheckSys(val)
        },1000)

    }

    //验证码校校验并跳转到修改密码页面
    function checkingSys(){
        if($('#inputPw').val()==""){
            $('#inputPw').focus();
            dialogMessage("验证失败","验证码不能为空");

        }else if(code==$('#inputPw').val()){
            $('#inputPw').focus();
            var title = '修改密码';
            var buttons = [{
                text: "取消",
                "class": "btn btn-xs",
                click: function() {
                    $(this).dialog("close");
                }
            },
                {
                    text: "保存",
                    "class": "btn btn-skinColor btn-xs",
                    click: function() {
                        if ($("#detail-password").val() == "" || $("#surePassword").val() == "" ) {
                            dialogMessage("确认密码信息", "密码框不能为空！");
                            return false;
                        }
                        if ($("#detail-password").val() != $("#surePassword").val()) {
                            dialogMessage("确认密码信息", "新密码不一致，请重新输入！");
                            return false;
                        }
                        var postParam = {"username":uname,"password":$("#detail-password").val(),"token":token};
                        var $this = $(this);
                        /*var callback = {
                            onSuccess: function(postData) {
                                if (postData.errcode == 0) {
                                    $this.dialog("close");
                                } else {
                                    dialogMessage("操作失败", "" + postData.errcode + ":" + postData.errinfo);
                                }
                            }
                        };*/
                        $.ajax({
                            type:"post",
                            url:toolkitPath+"/rest/api/raising/pawChange",
                            contentType:"application/json;charset=utf-8",
                            data:JSON.stringify(postParam),
                            dataType:"json",
                            success:function (res) {
                                if (res.success) {
                                    $this.dialog("close");
                                    dialogMessage("密码修改成功","请重新登录！")
                                    setTimeout("location.reload()",3000);
                                } else {
                                    $this.dialog("close");
                                    dialogMessage("操作失败",res.error);
                                }
                            }
                        })
                    }
                }];

            var dialogOpt = {
                title: title,
                buttons: buttons
            };
            baosightRequire.requireFunct([ 'bxdialog'],function () {
                $("#detail").bxdialog(dialogOpt);
                $("div[class='ui-widget-overlay ui-front'][style='z-index: 1049;']")[0].remove();
            })
            $("#loginName").attr("readonly", true);
        }else if(code!=$('#inputPw').val()){
            dialogMessage("验证失败","验证码输入有误或失效！")
        }
    }



</script>

<script type="text/javascript">
    $(document).ready(function () {
        $("#inputPw").keypress(function () {
            CheckKey();
            onKeyLogin();
        });
        $("#inputUser").keypress(function () {
            onKeyLogin();
        });

        setInterval(function () {
            if ($(".carousel-indicators > li[class=active]").data("skin") != undefined)
                window.document.body.className = $(".carousel-indicators > li[class=active]").data("skin");
        }, 1000);

        $(".carousel-indicators > li").click(function () {
            window.document.body.className = $(this).data("skin");
        });

        function dd(){
            <%
            String exceptionClassName = (String)request.getAttribute("shiroLoginFailure");
            if(request.getAttribute("shiroLoginFailure")!=null){
                if(UnknownAccountException.class.getName().equals(exceptionClassName)){
            %>
            dialogMessage("登录失败", "登录失败，账号不存在！");
            <%
                }else if(IncorrectCredentialsException.class.getName().equals(exceptionClassName)){
            %>
            dialogMessage("登录失败", "登录失败，密码出错！");
            <%
                }else if(LicenseDateException.class.getName().equals(exceptionClassName)){

            %>
            dialogMessage("登录失败", "许可证过期，请重新申请！");
            <%
                }else if(LicenseMacException.class.getName().equals(exceptionClassName)){
            %>
            dialogMessage("登录失败", "许可证mac地址校验出错，请重新申请！");

            <%
                }else if(LicenseException.class.getName().equals(exceptionClassName)){
            %>
            dialogMessage("登录失败", "许可证读取失败，请检查许可证文件存放位置！");
            <%
                }else if(UserExpireException.class.getName().equals(exceptionClassName)){
            %>
            dialogMessage("登录失败", "登录失败，当前用户已经过期！");
            <%
                }else{
            %>
            dialogMessage("登录失败", "登录失败，请确认登录信息！");
            <%
               }
            }
            %>
        }
        dd();

    });

    function clearInfo(id) {
        $("#" + id).val("");
        if("inputUser"  == id){//如果是清除账号，那么连同密码也清除
            $("#inputPw").val("");
        }
    }

    function CheckKey() {
        var e = window.event || arguments.callee.caller.arguments[0];
        var keyCode = e.keyCode || e.which; // 按键的keyCode
        var isShift = e.shiftKey || (keyCode == 16 ) || false; // shift键是否按住
        if ((keyCode >= 65 && keyCode <= 90) && !isShift) {

            $("#capsTip").css("visibility", "visible");
            $("#capsTip2").css("visibility", "visible");
        } else {

            $("#capsTip").css("visibility", "hidden");
            $("#capsTip2").css("visibility", "hidden");
        }
    }

    function onKeyLogin() {
        var e = window.event || arguments.callee.caller.arguments[0];
        var keyCode = e.keyCode || e.which; // 按键的keyCode
        if (keyCode == 13) {
            loginSys();
        }
    }

    function loginSys() {
        if ($("#inputUser").val() == "") {
            dialogMessage("登录失败", "用户名不能为空！");
            return false;
        }
        if ($("#inputPw").val() == "") {
            dialogMessage("登录失败", "密码不能为空！");
            return false;
        }
        $("#inputUserHidden").val($("#inputUser").val() + "@raising");
        document.getElementById("loginForm").submit();
    }

    function dialogMessage(title, centext) {
        baosightRequire.requireFunct([ 'bxdialog'],function () {
            alertDiv(title,"     "+centext);
            $("div[class='ui-widget-overlay ui-front'][style='z-index: 1049;']")[0].remove();
        })
    }

    function chgLoginType(id, loginType) {
        $(".byType").removeClass("curType");
        $("#" + id).addClass("curType");
        $("#loginType").val(loginType);
        $("#inputUser").val("");
        $("#inputPw").val("");
    }
</script>


</body>
</html>

