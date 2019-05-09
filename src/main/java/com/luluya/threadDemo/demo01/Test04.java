package com.luluya.threadDemo.demo01;

/**
 * @author jairy
 * @date 2019/5/8
 */
public class Test04 {

    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    //阻塞
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    System.out.println("子线程 i:" + i);
                }
            }
        });
        t1.start();
    }

}