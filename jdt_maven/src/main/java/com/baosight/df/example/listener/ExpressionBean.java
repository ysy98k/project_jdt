package com.baosight.df.example.listener;

/**
 * 监听器Expression方式
 */
public class ExpressionBean {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void test(){
        System.out.println("测试Expression方式成功！");
    }
}
