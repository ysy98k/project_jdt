package com.baosight.df.example.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;


/**
 * 执行监听器的示例，实现了ExecutionListener接口
 */
public class JDTExecutionListener implements ExecutionListener {

    private Expression content;

    private Expression name;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        execution.setVariable("setInStartListener", true);   //增加一个流程变量，名称为“setInStartListener”，值为true
        System.out.println(content.getValue(execution));
        System.out.println(name.getValue(execution));
        System.out.println(this.getClass().getSimpleName() + "," + execution.getEventName());
        System.out.println("JDT执行监听器测试！");
    }

}
