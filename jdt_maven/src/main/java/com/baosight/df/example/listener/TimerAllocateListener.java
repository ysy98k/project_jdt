package com.baosight.df.example.listener;

import com.baosight.df.example.listener.task.TimerAllocateTask;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lusongkai on 2017/8/16.
 * 在配置该监听器时必须使用委托表达式的方式，无法使用类的方式
 */
public class TimerAllocateListener implements TaskListener {

    @Autowired
    TaskService taskService;

    public void notify(DelegateTask delegateTask) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerAllocateTask(timer,delegateTask,taskService);
        timer.schedule(timerTask, 10 * 1000);  //10s之后更改审批人，可自行更改
        System.out.println("审批人即将更改");
    }

}
