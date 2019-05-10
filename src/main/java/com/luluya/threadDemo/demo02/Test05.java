package com.luluya.threadDemo.demo02;

/**
 * @author jairy
 * @date 2019/5/9
 */
public class Test05 extends Thread {
    public boolean flag = true;
    @Override
    public void run() {
        System.out.println("开始执行子线程....");
        while (flag) {
        }
        System.out.println("线程停止");
    }
    public void setRuning(boolean flag) {
        this.flag = flag;
    }

    public static void main(String[] args) throws InterruptedException {
        Test05 threadVolatileDemo = new Test05();
        threadVolatileDemo.start();
        Thread.sleep(3000);
        threadVolatileDemo.setRuning(false);
        System.out.println("flag 已经设置成false");
        Thread.sleep(1000);
        System.out.println(threadVolatileDemo.flag);
    }
}