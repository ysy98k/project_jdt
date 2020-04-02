package com.baosight.df.example.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;

/**
 * 任务监听器的示例，实现了TaskListener接口
 */
public class JDTTaskListener implements TaskListener {

    private Expression content;

    private Expression name;

    public void notify(DelegateTask delegateTask){
        System.out.println(content.getValue(delegateTask));
        System.out.println(name.getValue(delegateTask));
        System.out.println(this.getClass().getSimpleName() + "," + delegateTask.getEventName());
        System.out.println("JDT任务监听器测试！");
    }
}
