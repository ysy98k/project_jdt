<%@ page contentType="text/html; charset=UTF-8"%>

	<%
					String [] titlenames = new String[5];
	titlenames[0]="账号管理";
	titlenames[1]="租户管理";
	titlenames[2]="群组信息管理 ";
	titlenames[3]="资源信息管理";
    titlenames[4]="组织信息管理";
					String [] titledetails = new String[5];
					titledetails[0]="增加、修改、删除账号信息";
					titledetails[1]="增加、修改租户信息";
					titledetails[2]="增加、修改、删除群组信息";
					titledetails[3]="查询资源信息";
                    titledetails[4]="增加、修改、删除组织信息";
					%>
					
			<div class="page-header" style="height: 50px;">
				<h1 class="pull-left">
                    <%=titlenames[currentmenu] %>
					<small> <i class="ace-icon fa fa-angle-double-right"></i>
						<%=titledetails[currentmenu] %>
					</small>
				</h1>

			</div>