package com.baosight.df.example.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 事件监听器的示例，实现了ActivitiEventListener的接口
 */
public class GlobalEventListener implements ActivitiEventListener {

    public void onEvent(ActivitiEvent event){
        System.out.println("捕获到事件："+event.getType().name()+",type="+ ToStringBuilder.reflectionToString(event));
    }

    public boolean isFailOnException(){
        return false;
    }
}
