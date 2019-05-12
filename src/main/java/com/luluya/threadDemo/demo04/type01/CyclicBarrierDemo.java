package com.luluya.threadDemo.demo04.type01;

import java.util.concurrent.CyclicBarrier;

/**
 * @author jairy
 * @date 2019/5/11
 */
public class CyclicBarrierDemo {

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);
        for (int i = 0; i < 5; i++) {
            WriterThread writerThread = new WriterThread(cyclicBarrier);
            writerThread.start();
        }
    }
}

class WriterThread extends Thread {
    private CyclicBarrier cyclicBarrier;

    public WriterThread(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        System.out.println("线程" + Thread.currentThread().getName() + ",正在写入数据");
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            // TODO: handle exception
        }
        System.out.println("线程" + Thread.currentThread().getName() + ",写入数据成功.....");

        try {
            cyclicBarrier.await();
        } catch (Exception e) {
        }
        System.out.println("所有线程执行完毕..........");
    }

}