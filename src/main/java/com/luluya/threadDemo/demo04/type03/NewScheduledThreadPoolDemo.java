package com.luluya.threadDemo.demo04.type03;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author jairy
 * @date 2019/5/12
 */
public class NewScheduledThreadPoolDemo {
    public static void main(String[] args) {
        //创建一个定长线程池，支持定时及周期性任务执行
        ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(5);
        for (int i = 0; i < 10; i++) {
            final int temp = i;
            newScheduledThreadPool.schedule(new Runnable() {
                @Override
                public void run() {
                    System.out.println("i:" + temp);
                }
            },3, TimeUnit.SECONDS);
        }
    }
}
