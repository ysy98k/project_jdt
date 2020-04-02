<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.baosight.aas.auth.Constants" %>
<%@ page import="com.baosight.sys.controller.LoginController" %>
<%@ page import="java.util.Properties" %>
<%@ page import="java.io.InputStream" %>

<html>
<head>
  <title>许可证</title>
  <%
    String path = request.getContextPath();

    //loginName:name@test
    String loginName=request.getSession().getAttribute("loginName").toString();
  %>

  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <meta name="description" content="platForm Frameset" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
</head>
<body>
<%@ include file="/bxui/bxuihead.jsp"%>
<script type="text/javascript"
        src="<%=path%>/bxui/baosight-require.js"></script>
<script type="text/javascript"
        src="<%=path%>/df/license/license.js"></script>
<style>
  dl{
    border: solid #ddd 1px;
    padding: 50px 10px;
  }
  dt,dd{
    font-size: 16px;
    line-height:2;
  }
  #cloud{
    display: none;
    background-color: #f0f8ff;
  }
  #product{
    display: none;
    background-color: #f0f8ff;
  }
</style>
<div class="container">
  <div class="row" style="width: 65%;margin: 10% auto;">
    <dl id="product" class="dl-horizontal">
      <dt>签发单位 :</dt>
      <dd id="companyName">...</dd>
      <dt>产品名称 :</dt>
      <dd id="softWareName">...</dd>
      <dt>过期时间 :</dt>
      <dd id="productExpiringDate">9999-01-01</dd>
      <dt>AAS主机MAC地址 :</dt>
      <dd id="hostMac">...</dd>
      <dt>功能版本 :</dt>
      <dd id="productionEdition">...</dd>
      <dt>产品版本 :</dt>
      <dd id="version">...</dd>
      <dt>编译日期 :</dt>
      <dd id="buildDate">...</dd>
      <dt>所属基线 :</dt>
      <dd id="baseLine">...</dd>
      <dt>所属发布包 :</dt>
      <dd id="deliveryPkg">...</dd>
      </dl>
    <dl id="cloud" class="dl-horizontal">
      <dt>返回信息 :</dt>
      <dd id="licenseInfo">...</dd>
      <dt>过期时间 :</dt>
      <dd id="cloudExpiringDate">...</dd>
    </dl>
  </div>
</div>
</body>
</html>
