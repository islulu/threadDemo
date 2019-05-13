package com.luluya.threadDemo.demo05;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jairy
 * @date 2019/5/12
 */
public class ReentrantLockDemo implements Runnable{

    ReentrantLock reentrantLock = new ReentrantLock();

    private synchronized void get(){
        reentrantLock.lock();
        System.out.println("name:" + Thread.currentThread().getName() + " get();");
        set();
        reentrantLock.unlock();
    }

    private synchronized void set(){
        reentrantLock.lock();
        System.out.println("name:" + Thread.currentThread().getName() + " set();");
        reentrantLock.unlock();
    }

    @Override
    public void run() {
        get();
    }

    public static void main(String[] args) {
        SynchronizedDemo synchronizedDemo = new SynchronizedDemo();
        new Thread(synchronizedDemo).start();
        new Thread(synchronizedDemo).start();
    }

}
