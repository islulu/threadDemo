package com.luluya.threadDemo.demo04.type03;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jairy
 * @date 2019/5/12
 */
public class NewFixedThreadPoolDemo {
    //因为线程池大小为3，每个任务输出index后sleep 2秒，所以每两秒打印3个数字。
    //定长线程池的大小最好根据系统资源进行设置。如Runtime.getRuntime().availableProcessors()
    public static void main(String[] args) {
        //创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            final int temp = i;
            newFixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + ",i:" + temp);

                }
            });
        }
    }
}
