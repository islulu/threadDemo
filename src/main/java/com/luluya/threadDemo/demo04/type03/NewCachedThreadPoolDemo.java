package com.luluya.threadDemo.demo04.type03;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jairy
 * @date 2019/5/12
 */
public class NewCachedThreadPoolDemo {
    // 线程池为无限大，当执行第二个任务时第一个任务已经完成，会复用执行第一个任务的线程，而不用每次新建线程。
    public static void main(String[] args) {
        // 无限大小线程池 jvm自动回收
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
        for(int i=1;i<10;i++){
            final int temp = i;
            newCachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    }catch (Exception e){

                    }
                    System.out.println(Thread.currentThread().getName() + ",i="+temp);
                }
            });
        }
    }
}
