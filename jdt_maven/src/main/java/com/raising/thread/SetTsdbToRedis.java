package com.raising.thread;

import com.baosight.common.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class SetTsdbToRedis implements Runnable {

    private RedisUtils redisUtils;


    private String key;

    private String value;

    private Integer seconds;

    public SetTsdbToRedis(RedisUtils redisUtils,String key,String value,Integer seconds){
        this.key = key;
        this.value = value;
        this.seconds = seconds;
        this.redisUtils = redisUtils;
    }
    @Override
    public void run() {
        try {
            Thread.currentThread().sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.redisUtils.set(this.key,this.value,this.seconds);
    }
}
