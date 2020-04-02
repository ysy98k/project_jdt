package com.baosight.df.metamanage.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.dao.RestDao;
import com.baosight.common.constants.Constants;
import com.baosight.common.controller.DownloadExcelController;
import com.baosight.common.utils.RedisUtils;
import com.baosight.df.metamanage.service.TenantMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Set;

@Controller
@RequestMapping("/df/metamanage/tenantMenu.do")
public class TenantMenuController extends DownloadExcelController {

	@Autowired
	private RedisUtils redisUtils;

	@Autowired
	private TenantMenuService service;
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private RestDao dao;

	@Value("${aas.host}")
	private String host;

	@Value("${aas.rest_service_name}")
	private String serviceName;

	@RequestMapping(value = "")
	public ModelAndView frame_manage(HttpServletRequest request,
			HttpServletResponse response) {
		dao.setHost(host);
		dao.setServiceName(serviceName);
		service.dao = dao;

        JSONObject primaryKey = new JSONObject();
        primaryKey.put("key", "menuCode");
        primaryKey.put("keyDesc", "菜单代码");
        service.setPrimaryKey(primaryKey);
		return new ModelAndView("/df/metamanage/tenantMenu");
	}

	@RequestMapping(params = "method=clearMenuCache", method = RequestMethod.POST)
	public @ResponseBody
	JSONObject clearMenuCache(String ajaxParam) {
		String tenant = (String)request.getSession().getAttribute(com.baosight.aas.auth.Constants.SESSION_TENANT_KEY);
		Set<String> keySet = redisUtils.keys(com.baosight.aas.auth.Constants.JDT_REDIS_ROOT + ":"  + com.baosight.aas.auth.Constants.JDT_REDIS_MENU_ROOT + ":" + tenant + ":" + "*");
		for (String keys : keySet) {
			redisUtils.del(keys);
		}
		Set<String> wlsKeySet = redisUtils.keys(com.baosight.aas.auth.Constants.JDT_REDIS_ROOT + ":"  + com.baosight.aas.auth.Constants.JDT_REDIS_WLS_ROOT + ":" + tenant + ":" + "*");
		for (String keys : wlsKeySet) {
			redisUtils.del(keys);
		}
		JSONObject result = new JSONObject();
		result.put("status", Constants.EXECUTE_SUCCESS);
		result.put("returnMsg", "成功清空当前租户的菜单缓存！");
		return result;
	}

	@RequestMapping(params = "method=updateTenantMenu", method = RequestMethod.POST)
	public @ResponseBody
	JSONObject updateTenantMenu(String ajaxParam) {
		JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
		JSONObject returnInfo = service.updateTenantMenu(ajaxParamObj);
		return returnInfo;
	}

	@RequestMapping(params = "method=queryTenantMenu", method = RequestMethod.POST)
	public @ResponseBody
	JSONObject queryTenantMenu(String ajaxParam) {
		JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
		JSONObject returnInfo = service.queryTenantMenu(ajaxParamObj);
		return returnInfo;
	}

	@RequestMapping(params = "method=updateParent", method = RequestMethod.POST)
	public @ResponseBody
	JSONObject updateParent(String ajaxParam) {
		JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
		JSONObject returnInfo = service.updateParent(ajaxParamObj);
		return returnInfo;
	}

	/* 查询一级子节点 */
	@RequestMapping(params = "method=querySubTree", method = RequestMethod.GET)
	public @ResponseBody
	JSONArray querySubTree(String ajaxParam) {
		JSONObject ajaxParamObj = new JSONObject();
		ajaxParamObj.put("parentCode", request.getParameter("parentCode"));
		ajaxParamObj.put("treeparentId", request.getParameter("treeparentId"));
		ajaxParamObj.put("menuType", request.getParameter("menuType"));

		JSONArray returnInfo = service.querySubTree(ajaxParamObj);
		return returnInfo;
	}

	@RequestMapping(params = "method=queryHomeAndBackgroundMenu", method = RequestMethod.POST)
	public @ResponseBody
	JSONObject queryHomeAndBackgroundMenu(String ajaxParam) {
		dao.setHost(host);
		dao.setServiceName(serviceName);
		service.dao = dao;
		JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
		int a=1;
		String b=ajaxParamObj.getString("status");

		JSONObject returnInfo = service.queryHomeAndBackgroundMenu(ajaxParamObj);
		return returnInfo;
	}

	@RequestMapping(params = "method=insertTenantMenu", method = RequestMethod.POST)
	public @ResponseBody
	JSONObject insertTenantMenu(String ajaxParam) {
		JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
		JSONObject returnInfo = service.insertTenantMenu(ajaxParamObj);
		return returnInfo;
	}

	@RequestMapping(params = "method=deleteTenantMenu", method = RequestMethod.POST)
	public @ResponseBody
	JSONObject deleteTenantMenu(String ajaxParam) {
		JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
		JSONObject returnInfo = service.deleteTenantMenu(ajaxParamObj);
		return returnInfo;
	}

    JSONObject getDownloadData(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = service.getDownloadData(ajaxParamObj);
        return returnInfo;
    }

    @RequestMapping(params = "method=insertTenantMenuForUpload", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject insertTenantMenuForUpload(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = service.insertTenantMenuForUpload(ajaxParamObj);
        return returnInfo;
    }

    @RequestMapping(value="download.do",method = RequestMethod.GET)
    public String downloadExcel(HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
        try {
            String ajaxParam = request.getParameter("ajaxParam");
            ajaxParam = URLDecoder.decode(ajaxParam, "UTF-8");
            JSONObject queryResult = getDownloadData(ajaxParam);
            JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
            ajaxParamObj.put("downloadData",queryResult.getJSONArray("rows"));
            download(request,response,ajaxParamObj.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
}
