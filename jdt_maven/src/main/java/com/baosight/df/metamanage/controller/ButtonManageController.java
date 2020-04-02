package com.baosight.df.metamanage.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.dao.RestDao;
import com.baosight.common.constants.Constants;
import com.baosight.df.metamanage.service.ButtonManageService;
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

@Controller
@RequestMapping("/df/metamanage/buttonManage.do")
public class ButtonManageController {

    private static final Logger logger = LoggerFactory.getLogger(ButtonManageController.class);

    @Autowired
    private ButtonManageService service;

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

    private final static String split = "_";

    @RequestMapping(value = "")
    public ModelAndView init(HttpServletRequest request,
                             HttpServletResponse response) {
        dao.setHost(aasAddress);
        dao.setServiceName(aasAppPath);
        JSONObject primaryKey = new JSONObject();
        primaryKey.put("key", "buttonName");
        primaryKey.put("keyDesc", "按钮id");
        service.setPrimaryKey(primaryKey);
        logger.info("成功读取权限rest配置文件，host：" + aasAddress + " 服务名：" + aasAppPath);
        return new ModelAndView("/df/metamanage/buttonmanage");
    }

    @RequestMapping(params = "method=query", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject query(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = service.query(ajaxParamObj);
        return returnInfo;
    }
//    @RequestMapping(params = "method=queryPages", method = RequestMethod.POST)
//    public
//    @ResponseBody
//    JSONObject queryPages(String ajaxParam) {
//        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
//        JSONObject returnInfo = service.queryPages(ajaxParamObj);
//        return returnInfo;
//    }

    @RequestMapping(params = "method=insert", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject insert(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = service.insert(ajaxParamObj);
        /* 根据状态判断是否需要新增资源 */
        if (Constants.EXECUTE_FAIL.equals(returnInfo.getString("status"))) {
            return returnInfo;
        }
        JSONObject mapIn = new JSONObject();
        JSONArray buttonArray = ajaxParamObj.getJSONObject("detail").getJSONArray("resultRow");
        JSONArray resourceArray = new JSONArray();
        for (int i = 0; i < buttonArray.size(); i++) {
            JSONObject button = buttonArray.getJSONObject(i);
            JSONObject resource = new JSONObject();
            resource.put("name", "button" + split + button.getString("pageEname") + split + button.getString("buttonName"));
            resource.put("display_name",button.getString("buttonDisplayname"));
            resource.put("description",button.getString("buttonDisplayname"));
            resource.put("service", serviceName);
            resourceArray.add(resource);
        }
        mapIn.put("resources", resourceArray);
        mapIn.put("token", request.getSession().getAttribute(Constants.SESSION_TOKEN_KEY));
        JSONObject restInfo = dao.invoke("post", api, mapIn);
        if (Constants.EXECUTE_SUCCESS.equals(restInfo.getString(AasKeyConstants.KEY_ERRCODE))) {
            returnInfo.put("returnMsg", returnInfo.getString("returnMsg") + "资源新增成功");
        } else {
            returnInfo.put("returnMsg", returnInfo.getString("returnMsg")
                    + "资源新增失败，错误码为："
                    + restInfo.getString(AasKeyConstants.KEY_ERRCODE)+ "；错误信息为："
                    + restInfo
                    .getString(AasKeyConstants.KEY_ERRINFO));
        }
        return returnInfo;
    }

    @RequestMapping(params = "method=delete", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject delete(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = service.delete(ajaxParamObj);
        /* 根据状态判断是否需要删除资源 */
        if (Constants.EXECUTE_FAIL.equals(returnInfo.getString("status"))) {
            return returnInfo;
        }
        StringBuffer resoutceName = new StringBuffer();
        JSONArray resourceArray = ajaxParamObj.getJSONObject("result").getJSONArray("resultRow");
        for (int i = 0; i < resourceArray.size(); i++) {
            JSONObject resource = resourceArray.getJSONObject(i);
            String delRec =  "button" + split + resource.getString("pageEname") + split + resource.getString("buttonName");
            resoutceName.append(delRec);
            if (i < resourceArray.size() - 1) {
                resoutceName.append(",");
            }
        }
        String token = request.getSession().getAttribute("token").toString();
        JSONObject mapIn = new JSONObject();
        mapIn.put("token", token);
        mapIn.put("service", serviceName);
        mapIn.put("name", resoutceName.toString());
        JSONObject restInfo = dao.invoke("delete", api, mapIn);
        if (Constants.EXECUTE_SUCCESS.equals(restInfo.getString(AasKeyConstants.KEY_ERRCODE))) {
            returnInfo.put("returnMsg", returnInfo.getString("returnMsg") + "资源删除成功");
        } else {
            returnInfo.put("returnMsg", returnInfo.getString("returnMsg")
                    + "资源删除失败，错误码为："
                    + restInfo.getString(AasKeyConstants.KEY_ERRCODE)+ "；错误信息为："
                    + restInfo
                    .getString(AasKeyConstants.KEY_ERRINFO));
        }
        return returnInfo;
    }

    @RequestMapping(params = "method=update", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject update(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = service.update(ajaxParamObj);
        return returnInfo;
    }

    /* 查询一级子节点 */
    @RequestMapping(params = "method=querySubTree", method = RequestMethod.GET)
    public @ResponseBody
    JSONArray querySubTree(String ajaxParam) {
        JSONObject ajaxParamObj = new JSONObject();
        ajaxParamObj.put("parentCode", request.getParameter("parentCode"));

        JSONArray returnInfo = service.querySubTree(ajaxParamObj);
        return returnInfo;
    }




    @RequestMapping(params = "method=grantResource", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject grantResource(String ajaxParam) {
        JSONObject returnMsgObj = new JSONObject();
        StringBuffer returnMsgStr = new StringBuffer();
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        String selectedResource = ajaxParamObj.getString("selectedResource");
        String[] selectedResourceArray = selectedResource.split(",");
        //************************* 先增加资源 ***************************
        JSONObject resourceMap = new JSONObject();
        resourceMap.put("token", request.getSession().getAttribute("token").toString());
        JSONArray resourceArray = new JSONArray();
        for (int i = 0; i < selectedResourceArray.length; i++) {
            JSONObject obj = new JSONObject();
            obj.put("name", selectedResourceArray[i]);
            obj.put("display_name", selectedResourceArray[i]);
            obj.put("description", selectedResourceArray[i]);
            obj.put("service", serviceName);
            resourceArray.add(obj);
        }
        resourceMap.put("resources", resourceArray);
        JSONObject resourceReturnInfo = dao.invoke("post", "/api/resource",
                resourceMap);
        if ((ErrorCode.SUCCESS + "").equals(resourceReturnInfo
                .getString(AasKeyConstants.KEY_ERRCODE))) {
            returnMsgStr.append("新增资源成功，资源名：").append(selectedResource);
        } else {
            returnMsgStr.append("新增资源失败：").append(resourceReturnInfo.getString(AasKeyConstants.KEY_ERRCODE))
                    .append(":").append(resourceReturnInfo.getString(AasKeyConstants.KEY_ERRINFO));
            returnMsgObj.put("status", -1);
            returnMsgObj.put("returnMsg", returnMsgStr.toString());
            return returnMsgObj;
        }
        /************************* 为admingroup对资源授权 ***************************/
        JSONArray groupResourceArray = new JSONArray();
        JSONObject action = new JSONObject();
        action.put(Constants.ACTION_GET, true);
        action.put(Constants.ACTION_POST, true);
        action.put(Constants.ACTION_PUT, true);
        action.put(Constants.ACTION_DELETE, true);
        for (int i = 0; i < selectedResourceArray.length; i++) {
            JSONObject gr = new JSONObject();
            gr.put("name", selectedResourceArray[i]);
            gr.put("action", action);
            gr.put("service", serviceName);
            groupResourceArray.add(gr);
        }
        JSONObject groupResMap = new JSONObject();
        groupResMap.put("resources", groupResourceArray);
        groupResMap.put("token", request.getSession().getAttribute("token").toString());
        JSONObject groupResReturnInfo = dao.invoke("put", "/api/usergroup/admingroup/resource", groupResMap);
        if ((ErrorCode.SUCCESS + "").equals(groupResReturnInfo.getString(AasKeyConstants.KEY_ERRCODE))) {
            returnMsgStr.append("\r\n为租户管理群组授权资源成功，授权资源名：" + selectedResource);
            returnMsgObj.put("status", 0);
        } else {
            returnMsgStr.append("为群组授权资源失败：").append(groupResReturnInfo.getString(AasKeyConstants.KEY_ERRCODE))
                    .append(":").append(groupResReturnInfo.getString(AasKeyConstants.KEY_ERRINFO));
            returnMsgObj.put("status", -1);
        }
        returnMsgObj.put("returnMsg", returnMsgStr.toString());
        return returnMsgObj;
    }

}
