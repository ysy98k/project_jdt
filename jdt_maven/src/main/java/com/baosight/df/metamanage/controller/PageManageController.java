package com.baosight.df.metamanage.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.dao.RestDao;
import com.baosight.common.constants.Constants;
import com.baosight.common.controller.DownloadExcelController;
import com.baosight.df.metamanage.service.PageManageService;
import com.baosight.xinsight.constant.AasKeyConstants;
import com.baosight.xinsight.constant.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;

@Controller
@RequestMapping("/df/metamanage/pageManage.do")
public class PageManageController extends DownloadExcelController {

    private static final Logger logger = LoggerFactory
            .getLogger(PageManageController.class);

    @Autowired
    private PageManageService service;

    private String api = "/api/resource";

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RestDao dao;

    @Value("${aas.host}")
    private String aasAddress;

    @Value("${aas.rest_service_name}")
    private String aasAppPath;

    @Value("${service.name}")
    private String serviceName;

    @RequestMapping(value = "")
    public ModelAndView init(HttpServletRequest request,
                             HttpServletResponse response) {
        dao.setHost(aasAddress);
        dao.setServiceName(aasAppPath);
        JSONObject primaryKey = new JSONObject();
        primaryKey.put("key", "pageEname");
        primaryKey.put("keyDesc", "页面英文名");
        service.setPrimaryKey(primaryKey);
        logger.info("成功读取权限rest配置文件，host：" + aasAddress + " 服务名：" + aasAppPath);
        return new ModelAndView("/df/metamanage/pageManage");
    }

    @RequestMapping(params = "method=query", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject query(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = service.query(ajaxParamObj);
        return returnInfo;
    }

    @RequestMapping(params = "method=insert", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject insert(String ajaxParam) {
        dao.setHost(aasAddress);
        dao.setServiceName(aasAppPath);
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        ajaxParamObj.put("update", false);
        JSONObject returnInfo = service.insert(ajaxParamObj);
		/* 根据状态判断是否需要新增资源 */
        if (Constants.EXECUTE_SUCCESS.equals(returnInfo.getString("status"))) {
            HttpSession session = this.request.getSession();
            String token = session.getAttribute("token").toString();
            JSONObject mapIn = new JSONObject();
            mapIn.put("token", token);
            JSONArray resourceArray = ajaxParamObj.getJSONObject("detail")
                    .getJSONArray("resultRow");
            for (int i = 0; i < resourceArray.size(); i++) {
                JSONObject resource = resourceArray.getJSONObject(i);
                resource.put("name", resource.getString("pageEname"));
                resource.put("display_name", resource.getString("pageCname"));
                resource.put("description", resource.getString("pageDesc"));
                resource.put("service", serviceName);
            }
            mapIn.put("resources", resourceArray);
            JSONObject restInfo = dao.invoke("post", api, mapIn);
            if (Constants.EXECUTE_SUCCESS.equals(restInfo
                    .getString(AasKeyConstants.KEY_ERRCODE))) {
                returnInfo.put("returnMsg", returnInfo.getString("returnMsg")
                        + "资源新增成功");
            } else {
                returnInfo
                        .put("returnMsg",
                                returnInfo.getString("returnMsg")
                                        + "资源新增失败，错误码为："
                                        + restInfo
                                        .getString(AasKeyConstants.KEY_ERRCODE)
                                        + "；错误信息为："
                                        + restInfo
                                        .getString(AasKeyConstants.KEY_ERRINFO));
            }
        }
        return returnInfo;
    }

    @RequestMapping(params = "method=insertForUpload", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject insertForUpload(String ajaxParam) {
        dao.setHost(aasAddress);
        dao.setServiceName(aasAppPath);
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        ajaxParamObj.put("update", true);
        JSONObject returnInfo = service.insert(ajaxParamObj);
		/* 根据状态判断是否需要新增资源 */
        if (Constants.EXECUTE_SUCCESS.equals(returnInfo.getString("status"))) {
            HttpSession session = this.request.getSession();
            String token = session.getAttribute("token").toString();
            JSONObject mapIn = new JSONObject();
            mapIn.put("token", token);
            JSONArray resourceArray = ajaxParamObj.getJSONObject("detail")
                    .getJSONArray("resultRow");
            for (int i = 0; i < resourceArray.size(); i++) {
                JSONObject resource = resourceArray.getJSONObject(i);
                resource.put("name", resource.getString("pageEname"));
                resource.put("display_name", resource.getString("pageCname"));
                resource.put("description", resource.getString("pageDesc"));
                resource.put("service", serviceName);
            }
            mapIn.put("resources", resourceArray);
            mapIn.put("update", true);
            JSONObject restInfo = dao.invoke("post", api, mapIn);
            if (Constants.EXECUTE_SUCCESS.equals(restInfo
                    .getString(AasKeyConstants.KEY_ERRCODE))) {
                returnInfo.put("returnMsg", returnInfo.getString("returnMsg")
                        + "资源导入成功");
            } else {
                returnInfo
                        .put("returnMsg",
                                returnInfo.getString("returnMsg")
                                        + "资源导入失败，错误码为："
                                        + restInfo
                                        .getString(AasKeyConstants.KEY_ERRCODE)
                                        + "；错误信息为："
                                        + restInfo
                                        .getString(AasKeyConstants.KEY_ERRINFO));
            }
        }
        return returnInfo;
    }

    @RequestMapping(params = "method=delete", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject delete(String ajaxParam) {
        dao.setHost(aasAddress);
        dao.setServiceName(aasAppPath);
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = service.delete(ajaxParamObj);
		/* 根据状态判断是否需要删除资源 */
        if (Constants.EXECUTE_SUCCESS.equals(returnInfo.getString("status"))) {

            StringBuffer resoutceName = new StringBuffer();
            JSONArray resourceArray = ajaxParamObj.getJSONObject("result")
                    .getJSONArray("resultRow");
            for (int i = 0; i < resourceArray.size(); i++) {
                JSONObject resource = resourceArray.getJSONObject(i);
                resoutceName.append(resource.getString("pageEname"));
                if (i < resourceArray.size() - 1) {
                    resoutceName.append(",");
                }
            }
            HttpSession session = this.request.getSession();
            String token = session.getAttribute("token").toString();
            JSONObject mapIn = new JSONObject();
            mapIn.put("token", token);
            mapIn.put("service", serviceName);
            mapIn.put("name", resoutceName.toString());
            JSONObject restInfo = dao.invoke("delete", api, mapIn);
            if (Constants.EXECUTE_SUCCESS.equals(restInfo
                    .getString(AasKeyConstants.KEY_ERRCODE))) {
                returnInfo.put("returnMsg", returnInfo.getString("returnMsg")
                        + "资源删除成功");
            } else {
                returnInfo
                        .put("returnMsg",
                                returnInfo.getString("returnMsg")
                                        + "资源删除失败，错误码为："
                                        + restInfo
                                        .getString(AasKeyConstants.KEY_ERRCODE)
                                        + "；错误信息为："
                                        + restInfo
                                        .getString(AasKeyConstants.KEY_ERRINFO));
            }
        }
        return returnInfo;
    }

    @RequestMapping(params = "method=deleteDesignPage", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject deleteDesignPage(String ajaxParam) {
        dao.setHost(aasAddress);
        dao.setServiceName(aasAppPath);
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        service.deleteDesignPage(ajaxParamObj);
        JSONObject returnInfo = service.delete(ajaxParamObj);
		/* 根据状态判断是否需要删除资源 */
        if (Constants.EXECUTE_SUCCESS.equals(returnInfo.getString("status"))) {

            StringBuffer resoutceName = new StringBuffer();
            JSONArray resourceArray = ajaxParamObj.getJSONObject("result")
                    .getJSONArray("resultRow");
            for (int i = 0; i < resourceArray.size(); i++) {
                JSONObject resource = resourceArray.getJSONObject(i);
                resoutceName.append(resource.getString("pageEname"));
                if (i < resourceArray.size() - 1) {
                    resoutceName.append(",");
                }
            }
            HttpSession session = this.request.getSession();
            String token = session.getAttribute("token").toString();
            JSONObject mapIn = new JSONObject();
            mapIn.put("token", token);
            mapIn.put("service", serviceName);
            mapIn.put("name", resoutceName.toString());
            JSONObject restInfo = dao.invoke("delete", api, mapIn);
            if (Constants.EXECUTE_SUCCESS.equals(restInfo
                    .getString(AasKeyConstants.KEY_ERRCODE))) {
                returnInfo.put("returnMsg", returnInfo.getString("returnMsg")
                        + "资源删除成功");
            } else {
                returnInfo
                        .put("returnMsg",
                                returnInfo.getString("returnMsg")
                                        + "资源删除失败，错误码为："
                                        + restInfo
                                        .getString(AasKeyConstants.KEY_ERRCODE)
                                        + "；错误信息为："
                                        + restInfo
                                        .getString(AasKeyConstants.KEY_ERRINFO));
            }
        }
        return returnInfo;
    }

    @RequestMapping(params = "method=update", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject update(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = service.update(ajaxParamObj);
        return returnInfo;
    }

    @RequestMapping(params = "method=queryOne", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject queryOne(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = service.queryOne(ajaxParamObj);
        return returnInfo;
    }

    @RequestMapping(params = "method=grantResource", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject grantResource(String ajaxParam) {
        dao.setHost(aasAddress);
        dao.setServiceName(aasAppPath);
        JSONObject resutnMsgObj = new JSONObject();
        StringBuffer resurnMsgStr = new StringBuffer();
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONArray selectedResource = ajaxParamObj
                .getJSONArray("selectedResource");
        /************************* 先增加资源 ***************************/
        JSONObject resourceMap = new JSONObject();
        resourceMap.put("token", request.getSession().getAttribute("token")
                .toString());
        resourceMap.put("resources", selectedResource);
        JSONObject resourceReturnInfo = dao.invoke("post", "/api/resource",
                resourceMap);
        if ((ErrorCode.SUCCESS + "").equals(resourceReturnInfo
                .getString(AasKeyConstants.KEY_ERRCODE))) {
            StringBuffer resourceNameLst = new StringBuffer();
            for (int i = 0; i < selectedResource.size(); i++) {
                resourceNameLst.append(selectedResource.getJSONObject(i)
                        .getString("name"));
                if (i < selectedResource.size() - 1) {
                    resourceNameLst.append(",");
                }
            }
            resurnMsgStr.append("新增资源成功，资源名：" + resourceNameLst.toString());
        } else {
            resurnMsgStr
                    .append("新增资源失败："
                            + resourceReturnInfo
                            .getString(AasKeyConstants.KEY_ERRCODE)
                            + ":"
                            + resourceReturnInfo
                            .getString(AasKeyConstants.KEY_ERRINFO));
            resutnMsgObj.put("status", -1);
            resutnMsgObj.put("returnMsg", resurnMsgStr.toString());
            return resutnMsgObj;
        }
        resutnMsgObj.put("returnMsg", resurnMsgStr.toString());
        return resutnMsgObj;
    }

    @RequestMapping(value = "download.do", method = RequestMethod.GET)
    public String downloadExcel(HttpServletRequest request,
                                HttpServletResponse response) throws IOException {

        try {
            String ajaxParam = request.getParameter("ajaxParam");
            ajaxParam = URLDecoder.decode(ajaxParam, "UTF-8");
            JSONObject queryResult = query(ajaxParam);
            JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
            ajaxParamObj.put("downloadData", queryResult.getJSONArray("rows"));
            download(request, response, ajaxParamObj.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
        return null;
    }

}
