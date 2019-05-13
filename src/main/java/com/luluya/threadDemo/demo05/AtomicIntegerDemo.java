package com.luluya.threadDemo.demo05;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jairy
 * @date 2019/5/13
 */
public class AtomicIntegerDemo implements Runnable {

    private static Integer count = 1;
    private static AtomicInteger atomic = new AtomicInteger();

    @Override
    public void run() {
        while (true) {
            int count = getCountAtomic();
            System.out.println(count);
            if (count >= 150) {
                break;
            }
        }
    }

    public synchronized Integer getCount() {
        try {
            Thread.sleep(50);
        } catch (Exception e) {
            // TODO: handle exception
        }

        return count++;
    }

    public Integer getCountAtomic() {
        try {
            Thread.sleep(50);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return atomic.incrementAndGet();
    }

    public static void main(String[] args) {
        AtomicIntegerDemo test0001 = new AtomicIntegerDemo();
        Thread t1 = new Thread(test0001);
        Thread t2 = new Thread(test0001);
        t1.start();
        t2.start();
    }

}
