package com.luluya.threadDemo.demo01;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author jairy
 * @date 2019/5/12
 */
public class CallableDemo {

    public static void main(String[] args) {
        //Callable
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
        Future<String> future = newCachedThreadPool.submit(new TaskFuture());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("1.主线程开始执行");
                    //获取执行结果
                    String result = future.get();
                    System.out.println("2.result"+result);
                }catch (Exception e){

                }
            }
        }).start();

        if (newCachedThreadPool != null){
            newCachedThreadPool.shutdown();
        }

        System.out.println("主线程执行任务");
        //Future模式：不需要等待子线程执行完毕后再执行主线程，无需等待
    }

}

class TaskFuture implements Callable<String>{

    @Override
    public String call() throws Exception {
        System.out.println("3.正在执行任务，需要等待五秒事件，执行任务开始");
        Thread.sleep(5000);
        System.out.println("4.正在执行任务，需要等待五秒事件，执行任务结束");
        return "ok拉";
    }
}