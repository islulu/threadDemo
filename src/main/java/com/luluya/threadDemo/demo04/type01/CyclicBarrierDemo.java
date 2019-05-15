package com.luluya.threadDemo.demo04.type01;

import java.io.Writer;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author jairy
 * @date 2019/5/11
 */
public class CyclicBarrierDemo {

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(4
//            , new Runnable() {
//                @Override
//                public void run() {
//                    System.out.println("当前线程"+Thread.currentThread().getName());
//                }
//            }
//            Runnable方法会从所有线程中选择一个线程进行执行
        );
//        for (int i = 0; i < 5; i++) {
//            WriterThread writerThread = new WriterThread(cyclicBarrier);
//            writerThread.start();
//        }

        int N = 4;
        for(int i=0;i<N;i++) {
            if(i<N-1)
                new WriterThread(cyclicBarrier).start();
            else {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new WriterThread(cyclicBarrier).start();
            }
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
            Thread.sleep(5000);
        } catch (Exception e) {
            // TODO: handle exception
        }
        System.out.println("线程"+Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");

        System.out.println("线程" + Thread.currentThread().getName() + ",写入数据成功.....");

        try {
            cyclicBarrier.await();
        } catch (Exception e){

        }

//        try {
//            cyclicBarrier.await(1000, TimeUnit.MILLISECONDS);
//        } catch (TimeoutException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch(BrokenBarrierException e){
//            e.printStackTrace();
//        }
        System.out.println("所有线程执行完毕..........");
    }

}