package com.luluya.threadDemo.demo04.type03;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程线程池
 *
 * @author jairy
 * @date 2019/5/12
 */
public class CustomThreadPoolDemo {

    public static void main(String[] args) {
        ThreadPoolExecutor executor =
                new ThreadPoolExecutor(1, 2, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(3));
        try {
            for (int i = 0; i < 6; i++) {
                TaskThread t1 = new TaskThread("任务" + i);
                executor.execute(t1);
            }
        } catch (Exception e) {
            System.out.println("error:" + e.getMessage());
            executor.shutdown();
        }
    }
}

class TaskThread implements Runnable {
    private String taskName;

    public TaskThread(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + taskName);
    }
}