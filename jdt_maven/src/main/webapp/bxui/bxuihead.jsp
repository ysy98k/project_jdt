<%
    String toolkitPath = request.getContextPath();
    String skinName = (String) request.getSession().getAttribute(
            "skinName");
    if (skinName == null || skinName.equals(""))
        skinName = "blue-skin";
%>

<script type="text/javascript"
        src="<%=toolkitPath%>/bxui/baosightUI.js"></script>

<!--[if lte IE 9]>
<script type="text/javascript" src="<%=toolkitPath%>/bxui/baosightUI-IE9.js"></script>
<![endif]-->

<script type="text/javascript">
    var httpServerPath = window.location.protocol + "//" + window.location.host + "<%=toolkitPath%>";
    var toolkitPath = "<%=toolkitPath%>";
    document.getElementsByTagName( "body")[0].className += " " + "<%=skinName%>";
</script>