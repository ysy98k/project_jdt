<%@ page import="org.apache.shiro.subject.Subject" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String curpath=(String)request.getParameter("showtype");
%>
<div id="header">
    <!---header--->
    <nav class="navbar navbar-default navbar-fixed-top" role="navigation"
         id="topNav">
        <div class="header_bg1">
            <div class="navbar-left" id="navbarMenu">
                <div style="min-width:190px;float: left; margin-top: 5px; margin-left: 20px;">
                    <img src="<%=path%>/aas/images/AAS-24.png" style="margin: 5px 10px auto 0px;"/> <a href="#"><font
                        class="logoName">权限管理服务 AAS</font></a>
                </div>
                <div class="collapse navbar-collapse" id="navbar-collapse-menu"
                     style="min-width: 350px; margin-top: 9px; float: left; position: absolute;right: 0">
                    <ul class="nav navbar-nav" style="margin-top: -10px">

                        <%
                            String[] names = new String[5];
                            names[0] = "账号管理";
                            names[1] = "租户管理";
                            names[2] = "群组管理";
                            names[3] = "资源管理";
                            names[4] = "组织管理";
                            String[] urls = new String[5];
                            urls[0] = path + "/aas/UserManagement.do";
                            urls[1] = path + "/aas/TenantManagement.do";
                            urls[2] = path + "/aas/GroupManagement.do";
                            urls[3] = path + "/aas/ResourceManagement.do";
                            urls[4] = path + "/aas/OrganizationManagement.do";
                            Subject subject = SecurityUtils.getSubject();
                            System.out.println(subject.getPrincipal());
                            boolean isPerm = subject.isPermitted("Tenant:GET");
                            System.out.println(isPerm);
                            for (int i = 0; i < 5; i++) {
                                if (names[i].equalsIgnoreCase("租户管理") && !isPerm){
                                    continue;
                                }
                                String topMenu = "topMenu";
                                String color = "color: #8c8c8c";

                                if (i == currentmenu) {
                                    topMenu = "topMenu menuSel";
                                    color = "color: #fff";
                                }

                        %>
                        <li data-transition="pop"><a class="<%=topMenu%>"
                                                     href="<%=urls[i]%>"><p style="<%=color%>"><%=names[i] %>
                        </p></a>
                            <img src="<%=path%>/aas/images/top_line.png" width="1"
                                 height="37"
                                 style="float: left; margin-right: 5px; margin-left: 5px; margin-top: 5px;">
                        </li>


                        <%

                            }
                        %>
                    </ul>
                </div>
            </div>
        </div>
    </nav>
</div>