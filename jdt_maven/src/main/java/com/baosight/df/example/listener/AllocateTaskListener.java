package com.baosight.df.example.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.dao.RestDao;
import org.activiti.engine.HistoryService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lusongkai on 2017/8/14.
 * 根据启动流程的用户来分配候选人示例，为任务监听器，也可以调整为其他监听器
 * 在配置该监听器时必须使用委托表达式的方式，无法使用类的方式
 */
public class AllocateTaskListener implements TaskListener {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private HttpServletRequest request;

    @Value("${aas.host}")
    private String serviceAddress;

    @Value("${aas.rest_service_name}")
    private String appPath;

    @Autowired
    private RestDao dao;

    public void notify(DelegateTask delegateTask){
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
        String starter = historicProcessInstance.getStartUserId();      //id_username 格式
        String subStarter = starter.substring(starter.indexOf("_")+1,starter.length());  //username格式
        List candidateUsers = new ArrayList<>();

        dao.setHost(serviceAddress);
        dao.setServiceName(appPath);
        JSONObject params = new JSONObject();
        JSONObject returnObj1 = new JSONObject();

        try{
            String token = request.getSession(false).getAttribute("token").toString();
            params.put("token",token);
            returnObj1 = dao.invoke("get","/api/user/"+subStarter+"/usergroup",params);
            JSONArray groupArray = returnObj1.getJSONArray("groups");
            for(int i=0;i<groupArray.size();i++){
                JSONObject tempObj1 =(JSONObject)groupArray.get(i);
                String groupName = tempObj1.getString("name");
                params.put("token",token);
                JSONObject returnObj2 = dao.invoke("get","/api/usergroup/"+groupName+"/user",params);
                JSONArray userArray = returnObj2.getJSONArray("users");
                for(int j=0;j<userArray.size();j++){
                    JSONObject tempObj2 =(JSONObject)userArray.get(j);
                    candidateUsers.add(tempObj2.getString("id")+"_"+tempObj2.getString("username"));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if(!delegateTask.getAssignee().equals("")){    //如果任务已经设置了审批人则清空
            delegateTask.setAssignee(null);
        }
        delegateTask.addCandidateUsers(candidateUsers);
        System.out.println("任务分配成功！");
    }
}
