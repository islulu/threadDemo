package com.luluya.threadDemo.demo01;

/**
 * @author jairy
 * @date 2019/5/8
 */
public class Test05 {

    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("子线程");
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        });
        //该线程与守护线程一起销毁
        t1.setDaemon(true);
        t1.start();
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("main 主线程 i:" + i);
        }
        System.out.println("-----main 主线程创建结束-----");

    }

}