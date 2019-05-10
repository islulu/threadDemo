package com.luluya.threadDemo.demo01;

/**
 * @author jairy
 * @date 2019/5/8
 */
public class Test06 {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("子线程 i:" + i);
            }
        });
        t1.start();
        t1.join();
        for (int i = 0; i < 10; i++) {
            System.out.println("main 主线程 i:" + i);
        }
    }

}