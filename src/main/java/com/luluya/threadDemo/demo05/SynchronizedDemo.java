package com.luluya.threadDemo.demo05;

/**
 * @author jairy
 * @date 2019/5/12
 */
public class SynchronizedDemo implements Runnable{

    private synchronized void get(){
        System.out.println("name:" + Thread.currentThread().getName() + " get();");
        set();
    }

    private synchronized void set(){
        System.out.println("name:" + Thread.currentThread().getName() + " set();");
    }

    @Override
    public void run() {
        get();
    }

    public static void main(String[] args) {
        SynchronizedDemo synchronizedDemo = new SynchronizedDemo();
        new Thread(synchronizedDemo).start();
        new Thread(synchronizedDemo).start();
        new Thread(synchronizedDemo).start();
        new Thread(synchronizedDemo).start();
    }
}
