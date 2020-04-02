package com.baosight.df.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yetianqi on 2017/4/30.
 */
@Controller
@RequestMapping("/df/example/leavewk.do")
public class LeaveWKController {

    private static final Logger logger = LoggerFactory
        .getLogger(LeaveWKController.class);

    @Autowired
    protected RepositoryService repositoryService;

    @Autowired
    protected RuntimeService runtimeService;

    @Autowired
    protected TaskService taskService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IdentityService identityService;

    @RequestMapping(value = "")
    public ModelAndView init(HttpServletRequest request,
                             HttpServletResponse response) {
        return new ModelAndView("/df/example/workflow/leave");
    }

    @RequestMapping(params = "method=queryData", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject queryData(String ajaxParam){
        JSONObject returnObj = new JSONObject();
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        String taskId = ajaxParamObj.getString("taskId");
        if(!StringUtils.isBlank(taskId)) {//办理任务操作，将任务表单中的数据渲染到页面中，此处可以增加自己的业务代码
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            Map<String, Object> taskFormData = taskService.getVariables(taskId);
            returnObj.put("days",taskFormData.get("days"));
            returnObj.put("reason",taskFormData.get("reason"));
            returnObj.put("status", Constants.EXECUTE_SUCCESS);
        }
        return returnObj;
    }


    @RequestMapping(params = "method=submit", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject submit(String ajaxParam){
        JSONObject returnObj = new JSONObject();
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        String taskId = ajaxParamObj.getString("taskId");
        String processDefId = ajaxParamObj.getString("processDefId");
        Map<String, Object> formValues = new HashMap<String, Object>();
        formValues.put("days",ajaxParamObj.getString("days"));
        formValues.put("reason", ajaxParamObj.getString("reason"));
        if(!StringUtils.isBlank(processDefId)){//说明是启动流程操作，此处可以增加自己的业务代码
            HttpSession session = request.getSession(false);
            String userName = (String)session.getAttribute("username");
            String userId = session.getAttribute("userId").toString();
            identityService.setAuthenticatedUserId(userId + "_" + userName);
            ProcessInstance procInst = runtimeService.startProcessInstanceById(processDefId,formValues);
            returnObj.put("returnMsg","启动成功，实例ID：" + procInst.getId());
            returnObj.put("status", Constants.EXECUTE_SUCCESS);
        }else{//办理任务操作，此处可以增加自己的业务代码
            if(!StringUtils.isBlank(taskId)){
                taskService.complete(taskId,formValues);
                returnObj.put("status", Constants.EXECUTE_SUCCESS);
                returnObj.put("returnMsg","当前任务审批成功！");
            }else{
                HttpSession session = request.getSession(false);
                String userName = (String)session.getAttribute("username");
                String userId = session.getAttribute("userId").toString();
                identityService.setAuthenticatedUserId(userId + "_" + userName);
                ProcessInstance procInst = runtimeService.startProcessInstanceByKeyAndTenantId("generalForm",formValues,request.getSession().getAttribute("tenantId").toString());
                returnObj.put("returnMsg","启动成功，实例ID：" + procInst.getId());
                returnObj.put("status", Constants.EXECUTE_SUCCESS);
            }

        }
        return returnObj;
    }
}
