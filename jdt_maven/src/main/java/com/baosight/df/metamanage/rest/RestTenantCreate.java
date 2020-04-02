package com.baosight.df.metamanage.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.dao.RestDao;
import com.baosight.common.utils.SqlScriptRunnerUtils;
import com.baosight.xinsight.constant.AasKeyConstants;
import com.baosight.xinsight.constant.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * 租户创建时的回调函数所使用的rest服务
 *
 * @author yetianqi
 */
@Component
@Path("/tenant")
public class RestTenantCreate {

	private static final Logger logger = LoggerFactory
			.getLogger(RestTenantCreate.class);

	@Context
	HttpServletRequest request;
	@Context
	HttpServletResponse response;

	@Autowired
	private RestDao dao;

	@Value("${postgres.host}")
	private String url;

	@Value("${jdt.database.instance}")
	private String instance;

	@Value("${postgres.user}")
	private String userName;
	@Value("${postgres.password}")
	private String password;

	@Value("${aas.host}")
	private String aasAddress;

	@Value("${aas.rest_service_name}")
	private String aasAppPath;

	@Value("${service.name}")
	private String serviceName;

	@POST
	@Path("/action")
	@Produces(MediaType.APPLICATION_JSON)
	public String tenantCreateCallBack() throws Exception {

		JSONObject generalObj = new JSONObject();//设置总的返回结果

		dao.setHost(aasAddress);
		dao.setServiceName(aasAppPath);

		logger.info("成功读取权限服务配置文件，地址：" + aasAddress + aasAppPath);

		HashMap databaseProperty = new HashMap();
		databaseProperty.put("driver", "org.postgresql.Driver");
		databaseProperty.put("url", "jdbc:postgresql://" + url + "/" + instance);
		databaseProperty.put("userName", userName);
		databaseProperty.put("password", password);

		JSONObject mapIn = readJsonToMap();
		String tenant = mapIn.getString("tenant");
		JSONObject initialTenant = SqlScriptRunnerUtils.runInitialScript(
				tenant, databaseProperty);//创建新的schema作为租户

		generalObj.put("create_schema_errcode",initialTenant.getString("errCode"));
		generalObj.put("create_schema_errinfo",initialTenant.getString("msg"));

		JSONObject jdtDefaultScript = SqlScriptRunnerUtils.runImportScript(
				tenant, "jdt-all.sql", databaseProperty);//导入脚本到schema租户中

		generalObj.put("import_sql_errcode",jdtDefaultScript.getString("errCode"));
		generalObj.put("import_sql_errinfo",jdtDefaultScript.getString("msg"));


		/* 获取admin@租户的token */
		JSONObject sessionMap = new JSONObject();
		sessionMap.put("username", "admin");
		sessionMap.put("password", "admin");
		sessionMap.put("tenant", tenant);
		JSONObject sessionReturnInfo = dao.invoke("post", "/api/session",
				sessionMap);
		if ((ErrorCode.SUCCESS + "").equals(sessionReturnInfo
				.getString(AasKeyConstants.KEY_ERRCODE))) {
			/************************* 先增加资源 ***************************/
			String createToken = sessionReturnInfo.getString("token");
			JSONArray resourceArray = new JSONArray();
			JSONObject obj1 = new JSONObject();
			obj1.put("name", "pageManage");
			obj1.put("display_name", "页面信息管理");
			obj1.put("description", "页面信息管理");
			obj1.put("service", serviceName);
			resourceArray.add(obj1);

			JSONObject obj2 = new JSONObject();
			obj2.put("name", "frameSetting");
			obj2.put("display_name", "系统设置");
			obj2.put("description", "系统设置");
			obj2.put("service", serviceName);
			resourceArray.add(obj2);

			JSONObject obj3 = new JSONObject();
			obj3.put("name", "tenantMenu");
			obj3.put("display_name", "菜单配置");
			obj3.put("description", "菜单配置");
			obj3.put("service", serviceName);
			resourceArray.add(obj3);

			JSONObject obj4 = new JSONObject();
			obj4.put("name", "userManagement");
			obj4.put("display_name", "账号管理");
			obj4.put("description", "账号管理");
			obj4.put("service", serviceName);
			resourceArray.add(obj4);

			JSONObject obj5 = new JSONObject();
			obj5.put("name", "groupManagement");
			obj5.put("display_name", "群组管理");
			obj5.put("description", "群组管理");
			obj5.put("service", serviceName);
			resourceArray.add(obj5);

			JSONObject obj6 = new JSONObject();
			obj6.put("name", "resourceManagement");
			obj6.put("display_name", "资源管理");
			obj6.put("description", "资源管理");
			obj6.put("service", serviceName);
			resourceArray.add(obj6);

			JSONObject obj7 = new JSONObject();
			obj7.put("name", "systemManagement");
			obj7.put("display_name", "代码体系管理");
			obj7.put("description", "代码体系管理");
			obj7.put("service", serviceName);
			resourceArray.add(obj7);

			JSONObject obj8 = new JSONObject();
			obj8.put("name", "itemManagement");
			obj8.put("display_name", "代码项管理");
			obj8.put("description", "代码项管理");
			obj8.put("service", serviceName);
			resourceArray.add(obj8);

			JSONObject obj9 = new JSONObject();
			obj9.put("name", "itemRelation");
			obj9.put("display_name", "代码项关联管理");
			obj9.put("description", "代码项关联管理");
			obj9.put("service", serviceName);
			resourceArray.add(obj9);

			JSONObject obj10 = new JSONObject();
			obj10.put("name", "database");
			obj10.put("display_name", "数据源配置");
			obj10.put("description", "数据源配置");
			obj10.put("service", serviceName);
			resourceArray.add(obj10);

			JSONObject obj11 = new JSONObject();
			obj11.put("name", "frameIndex");
			obj11.put("display_name", "框架首页");
			obj11.put("description", "框架首页");
			obj11.put("service", serviceName);
			resourceArray.add(obj11);

			JSONObject resourceMap = new JSONObject();
			resourceMap.put("resources", resourceArray);
			resourceMap.put("token", createToken);

			JSONObject resourceReturnInfo = dao.invoke("post", "/api/resource",
					resourceMap);
			generalObj.put("jdt_aas_init_errcode",resourceReturnInfo.getString(AasKeyConstants.KEY_ERRCODE));
			generalObj.put("jdt_aas_init_errinfo",resourceReturnInfo.getString(AasKeyConstants.KEY_ERRINFO));
		}
		return generalObj.toJSONString();
	}

	private JSONObject readJsonToMap() throws Exception {

		request.setCharacterEncoding("utf-8");
		String data = "";
		if (request.getParameter("params") != null) {
			data = request.getParameter("params");
		} else {

			BufferedReader br = new BufferedReader(new InputStreamReader(
					request.getInputStream(), "UTF-8"));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();
			data = data.trim();
			if (!sb.toString().startsWith("{")) {
				data = "{" + data;
			}
			if (!sb.toString().endsWith("}")) {
				data += "}";
			}
		}
		return JSON.parseObject(data);

	}
}
