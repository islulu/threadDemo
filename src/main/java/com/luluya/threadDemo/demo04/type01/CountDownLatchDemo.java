package com.luluya.threadDemo.demo04.type01;

import java.util.concurrent.CountDownLatch;

/**
 * @author jairy
 * @date 2019/5/11
 */
public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ",子线程开始执行...");
                countDownLatch.countDown();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {

                }
                System.out.println(Thread.currentThread().getName() + ",子线程结束执行...");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ",子线程开始执行...");
                countDownLatch.countDown();//计数器值每次减去1
                System.out.println(Thread.currentThread().getName() + ",子线程结束执行...");
            }
        }).start();
        System.out.println("等待2个子线程执行完毕...");
        countDownLatch.await();//调用await()方法的线程会被挂起，它会等待直到count值为0才继续执行
        System.out.println("两个子线程执行完毕....");
        System.out.println("主线程继续执行.....");
        for (int i = 0; i < 10; i++) {
            System.out.println("main,i:" + i);
        }
    }

//    private CountDownLatch countDownLatch = new CountDownLatch(1);
//    public void method1(String name) {
//        try {
//            //countDownLatch.await();  //阻塞线程,到countDownLatch对象的count等于0时唤醒
//            countDownLatch.await(10L, TimeUnit.SECONDS); //阻塞线程,如果在10秒后还没有被唤醒,将自动唤醒
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " - " + name + " 被唤醒");
//    }
//
//    public void method2(String name) {
//        System.out.println(Thread.currentThread().getName() + "  " + name);
//        try {
//            TimeUnit.SECONDS.sleep(5);
//            countDownLatch.countDown(); // 将count值减1
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) throws InterruptedException {
//        CountDownLatchDemo test = new CountDownLatchDemo();
//        new Thread(() -> {
//            test.method1("method1");
//        }).start();
//        new Thread(() -> {
//            test.method2("method2");
//        }).start();
//        new Thread(() -> {
//            test.method1("method3");
//        }).start();
//    }
}
