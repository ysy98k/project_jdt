package com.baosight.df.example.listener.task;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lusongkai on 2017/8/18.
 */
public class TimerAllocateTask extends TimerTask {

    private Timer timer;

    private DelegateTask delegateTask;

    private TaskService taskService;

    public TimerAllocateTask(Timer timer, DelegateTask delegateTask, TaskService taskService){
        this.timer = timer;
        this.delegateTask = delegateTask;
        this.taskService = taskService;
    }

    public void run(){
        taskService.setAssignee(delegateTask.getId(), "10211_lsk"); //id_username 格式的用户名
        System.out.println("审批人已更改完成");
        timer.cancel();  //关闭线程
    }

}
