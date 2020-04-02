<%--
  Created by IntelliJ IDEA.
  User: lusongkai
  Date: 2017/3/14
  Time: 16:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <meta charset="utf-8" />
  <title>500 Error Page - Ace Admin</title>

  <meta name="description" content="500 Error Page" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

  <!-- bootstrap & fontawesome -->
  <link rel="stylesheet" href="<%=request.getContextPath()%>/df/error/assets/css/bootstrap.min.css" />
  <link rel="stylesheet" href="<%=request.getContextPath()%>/df/error/assets/css/font-awesome.min.css" />

  <!-- page specific plugin styles -->

  <!-- text fonts -->
  <link rel="stylesheet" href="<%=request.getContextPath()%>/df/error/assets/css/ace-fonts.css" />

  <!-- ace styles -->
  <link rel="stylesheet" href="<%=request.getContextPath()%>/df/error/assets/css/ace.min.css" />

  <!--[if lte IE 9]>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/df/error/assets/css/ace-part2.min.css" />
  <![endif]-->
  <link rel="stylesheet" href="<%=request.getContextPath()%>/df/error/assets/css/ace-skins.min.css" />
  <link rel="stylesheet" href="<%=request.getContextPath()%>/df/error/assets/css/ace-rtl.min.css" />

  <!--[if lte IE 9]>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/df/error/assets/css/ace-ie.min.css" />
  <![endif]-->

  <!-- inline styles related to this page -->

  <!-- ace settings handler -->
  <script src="<%=request.getContextPath()%>/df/error/assets/js/ace-extra.min.js"></script>

  <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->

  <!--[if lte IE 8]>
  <script src="<%=request.getContextPath()%>/df/error/assets/js/html5shiv.js"></script>
  <script src="<%=request.getContextPath()%>/df/error/assets/js/respond.min.js"></script>
  <![endif]-->
</head>
<body>
<!-- #sectionController:pages/error -->
<div class="error-container">
  <div class="well">
    <h1 class="grey lighter smaller">
										<span class="blue bigger-125">
											<i class="ace-icon fa fa-random"></i>
											500
										</span>
      不存在的页面资源！
    </h1>

    <hr />
    <h3 class="lighter smaller">
      请联系管理员！
    </h3>

    <div class="space"></div>

    <hr />
    <div class="space"></div>

    <div class="center">
      <a href="javascript:history.back()" class="btn btn-grey">
        <i class="ace-icon fa fa-arrow-left"></i>
        Go Back
      </a>

    </div>
  </div>
</div>
</body>
</html>
