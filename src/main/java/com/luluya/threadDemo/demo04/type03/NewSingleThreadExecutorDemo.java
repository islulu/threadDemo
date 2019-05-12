package com.luluya.threadDemo.demo04.type03;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jairy
 * @date 2019/5/12
 */
public class NewSingleThreadExecutorDemo {
    public static void main(String[] args) {
        //创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
        ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
        for (int i = 0; i <10 ; i++) {
            final int temp = i;
            newSingleThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("index:" + temp);
                    try {
                        Thread.sleep(200);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            });

        }
    }
}
